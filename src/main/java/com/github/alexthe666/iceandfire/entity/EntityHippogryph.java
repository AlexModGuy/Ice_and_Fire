package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.datagen.tags.IafItemTags;
import com.github.alexthe666.iceandfire.entity.ai.HippogryphAIMate;
import com.github.alexthe666.iceandfire.entity.ai.HippogryphAITarget;
import com.github.alexthe666.iceandfire.entity.ai.HippogryphAITargetItems;
import com.github.alexthe666.iceandfire.entity.ai.HippogryphAIWander;
import com.github.alexthe666.iceandfire.entity.util.*;
import com.github.alexthe666.iceandfire.enums.EnumHippogryphTypes;
import com.github.alexthe666.iceandfire.inventory.ContainerHippogryph;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.message.MessageHippogryphArmor;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import com.github.alexthe666.iceandfire.pathfinding.raycoms.AdvancedPathNavigate;
import com.google.common.base.Predicate;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.*;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class EntityHippogryph extends TamableAnimal implements ISyncMount, IAnimatedEntity, IDragonFlute, IVillagerFear, IAnimalFear, IDropArmor, IFlyingMount, ICustomMoveController, IHasCustomizableAttributes {

    private static final int FLIGHT_CHANCE_PER_TICK = 1200;
    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(EntityHippogryph.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> SADDLE = SynchedEntityData.defineId(EntityHippogryph.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> ARMOR = SynchedEntityData.defineId(EntityHippogryph.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> CHESTED = SynchedEntityData.defineId(EntityHippogryph.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> HOVERING = SynchedEntityData.defineId(EntityHippogryph.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> FLYING = SynchedEntityData.defineId(EntityHippogryph.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Byte> CONTROL_STATE = SynchedEntityData.defineId(EntityHippogryph.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Integer> COMMAND = SynchedEntityData.defineId(EntityHippogryph.class, EntityDataSerializers.INT);
    public static Animation ANIMATION_EAT;
    public static Animation ANIMATION_SPEAK;
    public static Animation ANIMATION_SCRATCH;
    public static Animation ANIMATION_BITE;
    public SimpleContainer hippogryphInventory;

    public float sitProgress;
    public float hoverProgress;
    public float flyProgress;
    public int spacebarTicks;
    public int airBorneCounter;
    public BlockPos homePos;
    public boolean hasHomePosition = false;
    public int feedings = 0;
    private boolean isLandNavigator;
    private boolean isSitting;
    private boolean isHovering;
    private boolean isFlying;
    private int animationTick;
    private Animation currentAnimation;
    private int flyTicks;
    private int hoverTicks;
    private boolean hasChestVarChanged = false;
    private boolean isOverAir;

    public EntityHippogryph(EntityType<? extends TamableAnimal> type, Level worldIn) {
        super(type, worldIn);
        this.switchNavigator(true);
        ANIMATION_EAT = Animation.create(25);
        ANIMATION_SPEAK = Animation.create(15);
        ANIMATION_SCRATCH = Animation.create(25);
        ANIMATION_BITE = Animation.create(20);
        initHippogryphInv();
        this.setMaxUpStep(1);
    }

    public static int getIntFromArmor(ItemStack stack) {
        if (!stack.isEmpty() && stack.getItem() == IafItemRegistry.IRON_HIPPOGRYPH_ARMOR.get()) {
            return 1;
        }
        if (!stack.isEmpty() && stack.getItem() == IafItemRegistry.GOLD_HIPPOGRYPH_ARMOR.get()) {
            return 2;
        }
        if (!stack.isEmpty() && stack.getItem() == IafItemRegistry.DIAMOND_HIPPOGRYPH_ARMOR.get()) {
            return 3;
        }
        return 0;
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 40.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.FLYING_SPEED, IafConfig.hippogryphFlightSpeedMod)
                .add(Attributes.ATTACK_DAMAGE, 5.0D)
                .add(Attributes.FOLLOW_RANGE, 32.0D);
    }

    @Override
    public void setConfigurableAttributes() {
        this.getAttribute(Attributes.FLYING_SPEED).setBaseValue(IafConfig.hippogryphFlightSpeedMod);
    }

    protected boolean isOverAir() {
        return isOverAir;
    }

    private boolean isOverAirLogic() {
        return level().isEmptyBlock(BlockPos.containing(this.getBlockX(), this.getBoundingBox().minY - 1, this.getBlockZ()));
    }

    @Override
    public int getExperienceReward() {
        return 10;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.2D, true));
        this.goalSelector.addGoal(4, new HippogryphAIMate(this, 1.0D));
        this.goalSelector.addGoal(5, new TemptGoal(this, 1.0D, Ingredient.of(IafItemTags.TEMPT_HIPPOGRYPH), false));
        //TODO: This doesn't gurantee the hippogryph will fly, it can still and is likely to path on the ground
        this.goalSelector.addGoal(6, new WaterAvoidingRandomFlyingGoal(this, 1D));
        this.goalSelector.addGoal(7, new HippogryphAIWander(this, 1.0D));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, LivingEntity.class, 6.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(3, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(4, new HippogryphAITargetItems<>(this, false));
        this.targetSelector.addGoal(5, new HippogryphAITarget(this, LivingEntity.class, false, new Predicate<Entity>() {
            @Override
            public boolean apply(@Nullable Entity entity) {
                return entity instanceof LivingEntity && !(entity instanceof AbstractHorse) && DragonUtils.isAlive((LivingEntity) entity);
            }
        }));
        this.targetSelector.addGoal(5, new HippogryphAITarget(this, Player.class, 350, false, new Predicate<Player>() {
            @Override
            public boolean apply(@Nullable Player entity) {
                return !entity.isCreative();
            }
        }));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(VARIANT, 0);
        this.entityData.define(ARMOR, 0);
        this.entityData.define(SADDLE, Boolean.FALSE);
        this.entityData.define(CHESTED, Boolean.FALSE);
        this.entityData.define(HOVERING, Boolean.FALSE);
        this.entityData.define(FLYING, Boolean.FALSE);
        this.entityData.define(CONTROL_STATE, (byte) 0);
        this.entityData.define(COMMAND, 0);

    }

    @Override
    public double getYSpeedMod() {
        return 4;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGroundIn, @NotNull BlockState state, @NotNull BlockPos pos) {
    }

    @Override
    public void positionRider(@NotNull Entity passenger, @NotNull MoveFunction callback) {
        super.positionRider(passenger, callback);
        if (this.hasPassenger(passenger)) {
            yBodyRot = getYRot();
            setYHeadRot(passenger.getYHeadRot());
            setYBodyRot(passenger.getYRot());
        }
        passenger.setPos(this.getX(), this.getY() + 1.05F, this.getZ());
    }

    private void initHippogryphInv() {
        SimpleContainer animalchest = this.hippogryphInventory;
        this.hippogryphInventory = new SimpleContainer(18);
        if (animalchest != null) {
            int i = Math.min(animalchest.getContainerSize(), this.hippogryphInventory.getContainerSize());
            for (int j = 0; j < i; ++j) {
                ItemStack itemstack = animalchest.getItem(j);
                if (!itemstack.isEmpty()) {
                    this.hippogryphInventory.setItem(j, itemstack.copy());
                }
            }

            if (level().isClientSide) {
                ItemStack saddle = animalchest.getItem(0);
                ItemStack chest = animalchest.getItem(1);
                IceAndFire.NETWORK_WRAPPER.sendToServer(new MessageHippogryphArmor(this.getId(), 0, saddle != null && saddle.getItem() == Items.SADDLE && !saddle.isEmpty() ? 1 : 0));
                IceAndFire.NETWORK_WRAPPER.sendToServer(new MessageHippogryphArmor(this.getId(), 1, chest != null && chest.getItem() == Blocks.CHEST.asItem() && !chest.isEmpty() ? 1 : 0));
                IceAndFire.NETWORK_WRAPPER.sendToServer(new MessageHippogryphArmor(this.getId(), 2, getIntFromArmor(animalchest.getItem(2))));
            }
        }
    }

    @Override
    @Nullable
    public LivingEntity getControllingPassenger() {
        for (Entity passenger : this.getPassengers()) {
            if (passenger instanceof Player && this.getTarget() != passenger) {
                Player player = (Player) passenger;
                if (this.isTame() && this.getOwnerUUID() != null && this.getOwnerUUID().equals(player.getUUID())) {
                    return player;
                }
            }
        }
        return null;
    }

    public boolean isBlinking() {
        return this.tickCount % 50 > 43;
    }

    @Override
    public @NotNull InteractionResult mobInteract(Player player, @NotNull InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        String s = ChatFormatting.stripFormatting(player.getName().getString());
        boolean isDev = s.equals("Alexthe666") || s.equals("Raptorfarian") || s.equals("tweakbsd");
        if (this.isTame() && this.isOwnedBy(player)) {
            if (itemstack.getItem() == Items.RED_DYE && this.getEnumVariant() != EnumHippogryphTypes.ALEX && isDev) {
                this.setEnumVariant(EnumHippogryphTypes.ALEX);
                if (!player.isCreative()) {
                    itemstack.shrink(1);
                }
                this.playSound(SoundEvents.ZOMBIE_INFECT, 1, 1);
                for (int i = 0; i < 20; i++) {
                    this.level().addParticle(ParticleTypes.CLOUD, this.getX() + (double) (this.random.nextFloat() * this.getBbWidth() * 2.0F) - (double) this.getBbWidth(), this.getY() + (double) (this.random.nextFloat() * this.getBbHeight()), this.getZ() + (double) (this.random.nextFloat() * this.getBbWidth() * 2.0F) - (double) this.getBbWidth(), 0, 0, 0);
                }
                return InteractionResult.SUCCESS;
            }
            if (itemstack.getItem() == Items.LIGHT_GRAY_DYE && this.getEnumVariant() != EnumHippogryphTypes.RAPTOR && isDev) {
                this.setEnumVariant(EnumHippogryphTypes.RAPTOR);
                if (!player.isCreative()) {
                    itemstack.shrink(1);
                }
                this.playSound(SoundEvents.ZOMBIE_INFECT, 1, 1);
                for (int i = 0; i < 20; i++) {
                    this.level().addParticle(ParticleTypes.CLOUD, this.getX() + (double) (this.random.nextFloat() * this.getBbWidth() * 2.0F) - (double) this.getBbWidth(), this.getY() + (double) (this.random.nextFloat() * this.getBbHeight()), this.getZ() + (double) (this.random.nextFloat() * this.getBbWidth() * 2.0F) - (double) this.getBbWidth(), 0, 0, 0);
                }
                return InteractionResult.SUCCESS;
            }
            if (itemstack.is(IafItemTags.BREED_HIPPOGRYPH) && this.getAge() == 0 && !isInLove()) {
                this.setInLove(player);
                this.playSound(SoundEvents.GENERIC_EAT, 1, 1);
                if (!player.isCreative()) {
                    itemstack.shrink(1);
                }
                return InteractionResult.SUCCESS;
            }
            if (itemstack.getItem() == Items.STICK) {
                if (player.isShiftKeyDown()) {
                    if (this.hasHomePosition) {
                        this.hasHomePosition = false;
                        player.displayClientMessage(Component.translatable("hippogryph.command.remove_home"), true);
                        return InteractionResult.SUCCESS;
                    } else {
                        this.homePos = this.blockPosition();
                        this.hasHomePosition = true;
                        player.displayClientMessage(Component.translatable("hippogryph.command.new_home", homePos.getX(), homePos.getY(), homePos.getZ()), true);
                        return InteractionResult.SUCCESS;
                    }
                } else {
                    this.setCommand(this.getCommand() + 1);
                    if (this.getCommand() > 1) {
                        this.setCommand(0);
                    }
                    player.displayClientMessage(Component.translatable("hippogryph.command." + (this.getCommand() == 1 ? "sit" : "stand")), true);

                }
                return InteractionResult.SUCCESS;
            }
            if (itemstack.getItem() == Items.GLISTERING_MELON_SLICE && this.getEnumVariant() != EnumHippogryphTypes.DODO) {
                this.setEnumVariant(EnumHippogryphTypes.DODO);
                if (!player.isCreative()) {
                    itemstack.shrink(1);
                }
                this.playSound(SoundEvents.ZOMBIE_INFECT, 1, 1);
                for (int i = 0; i < 20; i++) {
                    this.level().addParticle(ParticleTypes.ENCHANT, this.getX() + (double) (this.random.nextFloat() * this.getBbWidth() * 2.0F) - (double) this.getBbWidth(), this.getY() + (double) (this.random.nextFloat() * this.getBbHeight()), this.getZ() + (double) (this.random.nextFloat() * this.getBbWidth() * 2.0F) - (double) this.getBbWidth(), 0, 0, 0);
                }
                return InteractionResult.SUCCESS;
            }
            if (itemstack.getItem().isEdible() && itemstack.getItem().getFoodProperties() != null && itemstack.getItem().getFoodProperties().isMeat() && this.getHealth() < this.getMaxHealth()) {
                this.heal(5);
                this.playSound(SoundEvents.GENERIC_EAT, 1, 1);
                for (int i = 0; i < 3; i++) {
                    this.level().addParticle(new ItemParticleOption(ParticleTypes.ITEM, itemstack), this.getX() + (double) (this.random.nextFloat() * this.getBbWidth() * 2.0F) - (double) this.getBbWidth(), this.getY() + (double) (this.random.nextFloat() * this.getBbHeight()), this.getZ() + (double) (this.random.nextFloat() * this.getBbWidth() * 2.0F) - (double) this.getBbWidth(), 0, 0, 0);
                }
                if (!player.isCreative()) {
                    itemstack.shrink(1);
                }
                return InteractionResult.SUCCESS;
            }
            if (itemstack.isEmpty()) {
                if (player.isShiftKeyDown()) {
                    this.openGUI(player);
                    return InteractionResult.SUCCESS;
                } else if (this.isSaddled() && !this.isBaby() && !player.isPassenger()) {
                    player.startRiding(this, true);
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return super.mobInteract(player, hand);
    }

    public void openGUI(Player playerEntity) {
        if (!this.level().isClientSide && (!this.isVehicle() || this.hasPassenger(playerEntity))) {
            NetworkHooks.openScreen((ServerPlayer) playerEntity, new MenuProvider() {
                @Override
                public AbstractContainerMenu createMenu(int p_createMenu_1_, @NotNull Inventory p_createMenu_2_, @NotNull Player p_createMenu_3_) {
                    return new ContainerHippogryph(p_createMenu_1_, hippogryphInventory, p_createMenu_2_, EntityHippogryph.this);
                }

                @Override
                public @NotNull Component getDisplayName() {
                    return Component.translatable("entity.iceandfire.hippogryph");
                }
            });
        }
        IceAndFire.PROXY.setReferencedMob(this);
    }

    @Override
    public boolean isGoingUp() {
        return (entityData.get(CONTROL_STATE).byteValue() & 1) == 1;
    }

    @Override
    public boolean isGoingDown() {
        return (entityData.get(CONTROL_STATE).byteValue() >> 1 & 1) == 1;
    }

    public boolean attack() {
        return (entityData.get(CONTROL_STATE).byteValue() >> 2 & 1) == 1;
    }

    public boolean dismountIAF() {
        return (entityData.get(CONTROL_STATE).byteValue() >> 3 & 1) == 1;
    }

    @Override
    public void up(boolean up) {
        setStateField(0, up);
    }

    @Override
    public void down(boolean down) {
        setStateField(1, down);
    }

    @Override
    public void attack(boolean attack) {
        setStateField(2, attack);
    }

    @Override
    public void strike(boolean strike) {

    }

    @Override
    public void dismount(boolean dismount) {
        setStateField(3, dismount);
    }

    private void setStateField(int i, boolean newState) {
        byte prevState = entityData.get(CONTROL_STATE).byteValue();
        if (newState) {
            entityData.set(CONTROL_STATE, (byte) (prevState | (1 << i)));
        } else {
            entityData.set(CONTROL_STATE, (byte) (prevState & ~(1 << i)));
        }
    }

    @Override
    public byte getControlState() {
        return entityData.get(CONTROL_STATE).byteValue();
    }

    @Override
    public void setControlState(byte state) {
        entityData.set(CONTROL_STATE, state);
    }

    public int getCommand() {
        return this.entityData.get(COMMAND).intValue();
    }

    public void setCommand(int command) {
        this.entityData.set(COMMAND, command);
        this.setOrderedToSit(command == 1);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Variant", this.getVariant());
        compound.putBoolean("Chested", this.isChested());
        compound.putBoolean("Saddled", this.isSaddled());
        compound.putBoolean("Hovering", this.isHovering());
        compound.putBoolean("Flying", this.isFlying());
        compound.putInt("Armor", this.getArmor());
        compound.putInt("Feedings", feedings);
        if (hippogryphInventory != null) {
            ListTag nbttaglist = new ListTag();
            for (int i = 0; i < this.hippogryphInventory.getContainerSize(); ++i) {
                ItemStack itemstack = this.hippogryphInventory.getItem(i);
                if (!itemstack.isEmpty()) {
                    CompoundTag CompoundNBT = new CompoundTag();
                    CompoundNBT.putByte("Slot", (byte) i);
                    itemstack.save(CompoundNBT);
                    nbttaglist.add(CompoundNBT);
                }
            }
            compound.put("Items", nbttaglist);
        }
        compound.putBoolean("HasHomePosition", this.hasHomePosition);
        if (homePos != null && this.hasHomePosition) {
            compound.putInt("HomeAreaX", homePos.getX());
            compound.putInt("HomeAreaY", homePos.getY());
            compound.putInt("HomeAreaZ", homePos.getZ());
        }
        compound.putInt("Command", this.getCommand());
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setVariant(compound.getInt("Variant"));
        this.setChested(compound.getBoolean("Chested"));
        this.setSaddled(compound.getBoolean("Saddled"));
        this.setHovering(compound.getBoolean("Hovering"));
        this.setFlying(compound.getBoolean("Flying"));
        this.setArmor(compound.getInt("Armor"));
        feedings = compound.getInt("Feedings");
        if (hippogryphInventory != null) {
            ListTag nbttaglist = compound.getList("Items", 10);
            this.initHippogryphInv();
            for (int i = 0; i < nbttaglist.size(); ++i) {
                CompoundTag CompoundNBT = nbttaglist.getCompound(i);
                int j = CompoundNBT.getByte("Slot") & 255;
                this.hippogryphInventory.setItem(j, ItemStack.of(CompoundNBT));
            }
        } else {
            ListTag nbttaglist = compound.getList("Items", 10);
            this.initHippogryphInv();
            for (int i = 0; i < nbttaglist.size(); ++i) {
                CompoundTag CompoundNBT = nbttaglist.getCompound(i);
                int j = CompoundNBT.getByte("Slot") & 255;
                this.initHippogryphInv();
                this.hippogryphInventory.setItem(j, ItemStack.of(CompoundNBT));
                //this.setArmorInSlot(j, this.getIntFromArmor(ItemStack.loadItemStackFromNBT(CompoundNBT)));
                ItemStack saddle = hippogryphInventory.getItem(0);
                ItemStack chest = hippogryphInventory.getItem(1);
                if (level().isClientSide) {
                    IceAndFire.NETWORK_WRAPPER.sendToServer(new MessageHippogryphArmor(this.getId(), 0, saddle != null && saddle.getItem() == Items.SADDLE && !saddle.isEmpty() ? 1 : 0));
                    IceAndFire.NETWORK_WRAPPER.sendToServer(new MessageHippogryphArmor(this.getId(), 1, chest != null && chest.getItem() == Blocks.CHEST.asItem() && !chest.isEmpty() ? 1 : 0));
                    IceAndFire.NETWORK_WRAPPER.sendToServer(new MessageHippogryphArmor(this.getId(), 2, getIntFromArmor(hippogryphInventory.getItem(2))));
                }
            }
        }
        this.hasHomePosition = compound.getBoolean("HasHomePosition");
        if (hasHomePosition && compound.getInt("HomeAreaX") != 0 && compound.getInt("HomeAreaY") != 0 && compound.getInt("HomeAreaZ") != 0) {
            homePos = new BlockPos(compound.getInt("HomeAreaX"), compound.getInt("HomeAreaY"), compound.getInt("HomeAreaZ"));
        }
        this.setCommand(compound.getInt("Command"));

        if (this.isOrderedToSit()) {
            this.sitProgress = 20.0F;
        }

        this.setConfigurableAttributes();

    }

    public int getVariant() {
        return this.entityData.get(VARIANT).intValue();
    }

    public void setVariant(int variant) {
        this.entityData.set(VARIANT, variant);
    }

    public EnumHippogryphTypes getEnumVariant() {
        return EnumHippogryphTypes.values()[this.getVariant()];
    }

    public void setEnumVariant(EnumHippogryphTypes variant) {
        this.setVariant(variant.ordinal());
    }

    public boolean isSaddled() {
        return this.entityData.get(SADDLE).booleanValue();
    }

    public void setSaddled(boolean saddle) {
        this.entityData.set(SADDLE, saddle);
    }

    public boolean isChested() {
        return this.entityData.get(CHESTED).booleanValue();
    }

    public void setChested(boolean chested) {
        this.entityData.set(CHESTED, chested);
        this.hasChestVarChanged = true;
    }

    @Override
    public boolean isOrderedToSit() {
        if (level().isClientSide) {
            boolean isSitting = (this.entityData.get(DATA_FLAGS_ID).byteValue() & 1) != 0;
            this.isSitting = isSitting;
            return isSitting;
        }
        return isSitting;
    }

    @Override
    public void setOrderedToSit(boolean sitting) {
        if (!level().isClientSide) {
            this.isSitting = sitting;
        }
        byte b0 = this.entityData.get(DATA_FLAGS_ID).byteValue();
        if (sitting) {
            this.entityData.set(DATA_FLAGS_ID, (byte) (b0 | 1));
        } else {
            this.entityData.set(DATA_FLAGS_ID, (byte) (b0 & -2));
        }
    }

    @Override
    public boolean isHovering() {
        if (level().isClientSide) {
            return this.isHovering = this.entityData.get(HOVERING).booleanValue();
        }
        return isHovering;
    }

    public void setHovering(boolean hovering) {
        this.entityData.set(HOVERING, hovering);
        if (!level().isClientSide) {
            this.isHovering = hovering;
        }
    }

    @Override
    @Nullable
    public Player getRidingPlayer() {
        if (this.getControllingPassenger() instanceof Player) {
            return (Player) this.getControllingPassenger();
        }
        return null;
    }

    @Override
    public double getFlightSpeedModifier() {
        return IafConfig.hippogryphFlightSpeedMod * 0.9F;
    }

    @Override
    public boolean isFlying() {
        if (level().isClientSide) {
            return this.isFlying = this.entityData.get(FLYING).booleanValue();
        }
        return isFlying;
    }

    public void setFlying(boolean flying) {
        this.entityData.set(FLYING, flying);
        if (!level().isClientSide) {
            this.isFlying = flying;
        }
    }

    public int getArmor() {
        return this.entityData.get(ARMOR).intValue();
    }

    public void setArmor(int armorType) {
        this.entityData.set(ARMOR, armorType);
        double armorValue = switch (armorType) {
            case 1 -> 10;
            case 2 -> 20;
            case 3 -> 30;
            default -> 0;
        };
        this.getAttribute(Attributes.ARMOR).setBaseValue(armorValue);
    }

    public boolean canMove() {
        return !this.isOrderedToSit() && this.getControllingPassenger() == null && sitProgress == 0;
    }

    @Override
    @Nullable
    public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor worldIn, @NotNull DifficultyInstance difficultyIn, @NotNull MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        SpawnGroupData data = super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
        this.setEnumVariant(EnumHippogryphTypes.getBiomeType(worldIn.getBiome(this.blockPosition())));
        return data;
    }

    @Override
    public boolean hurt(@NotNull DamageSource dmg, float i) {
        if (this.isVehicle() && dmg.getEntity() != null && this.getControllingPassenger() != null && dmg.getEntity() == this.getControllingPassenger()) {
            return false;
        }
        return super.hurt(dmg, i);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel serverWorld, @NotNull AgeableMob ageable) {
        return null;
    }

    @Override
    public int getAnimationTick() {
        return animationTick;
    }

    @Override
    public void setAnimationTick(int tick) {
        animationTick = tick;
    }

    @Override
    public Animation getAnimation() {
        return currentAnimation;
    }

    @Override
    public void setAnimation(Animation animation) {
        currentAnimation = animation;
    }

    @Override
    public void playAmbientSound() {
        if (this.getAnimation() == this.NO_ANIMATION) {
            this.setAnimation(ANIMATION_SPEAK);
        }
        super.playAmbientSound();
    }

    @Override
    protected void playHurtSound(@NotNull DamageSource source) {
        if (this.getAnimation() == this.NO_ANIMATION) {
            this.setAnimation(ANIMATION_SPEAK);
        }
        super.playHurtSound(source);
    }

    @Override
    protected float getRiddenSpeed(@NotNull Player pPlayer) {
        return (this.isFlying() || this.isHovering()) ? (float) this.getAttributeValue(Attributes.FLYING_SPEED) : (float) this.getAttributeValue(Attributes.MOVEMENT_SPEED) * 0.75F;
    }

    @Override
    @Nullable
    protected SoundEvent getAmbientSound() {
        return IafSoundRegistry.HIPPOGRYPH_IDLE;
    }

    @Override
    @Nullable
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSourceIn) {
        return IafSoundRegistry.HIPPOGRYPH_HURT;
    }

    @Override
    @Nullable
    protected SoundEvent getDeathSound() {
        return IafSoundRegistry.HIPPOGRYPH_DIE;
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[]{IAnimatedEntity.NO_ANIMATION, EntityHippogryph.ANIMATION_EAT, EntityHippogryph.ANIMATION_BITE, EntityHippogryph.ANIMATION_SPEAK, EntityHippogryph.ANIMATION_SCRATCH};
    }

    @Override
    public void travel(@NotNull Vec3 pTravelVector) {
        if (this.isControlledByLocalInstance()) {
            if (this.isInWater()) {
                this.moveRelative(0.02F, pTravelVector);
                this.move(MoverType.SELF, this.getDeltaMovement());
                this.setDeltaMovement(this.getDeltaMovement().scale(0.8F));
            } else if (this.isInLava()) {
                this.moveRelative(0.02F, pTravelVector);
                this.move(MoverType.SELF, this.getDeltaMovement());
                this.setDeltaMovement(this.getDeltaMovement().scale(0.5D));
            } else if (this.isFlying() || this.isHovering()) {
                this.moveRelative(0.1F, pTravelVector);
                this.move(MoverType.SELF, this.getDeltaMovement());
                this.setDeltaMovement(this.getDeltaMovement().scale(0.9D));
            } else {
                super.travel(pTravelVector);
            }
        } else {
            super.travel(pTravelVector);
        }
    }

    @Override
    protected void tickRidden(@NotNull Player player, @NotNull Vec3 travelVector) {
        super.tickRidden(player, travelVector);
        Vec2 vec2 = this.getRiddenRotation(player);
        this.setRot(vec2.y, vec2.x);
        this.yRotO = this.yBodyRot = this.yHeadRot = this.getYRot();
        if (this.isControlledByLocalInstance()) {
            Vec3 vec3 = this.getDeltaMovement();
            float vertical = this.isGoingUp() ? 0.2F : this.isGoingDown() ? -0.2F : 0F;
            if (!this.isFlying() && !this.isHovering()) {
                vertical = (float) travelVector.y;
            }
            this.setDeltaMovement(vec3.add(0, vertical, 0));
        }
    }

    @Override
    protected @NotNull Vec3 getRiddenInput(Player player, @NotNull Vec3 travelVector) {
        float f = player.xxa * 0.5F;
        float f1 = player.zza;
        if (f1 <= 0.0F) {
            f1 *= 0.25F;
        }

        return new Vec3(f, 0.0D, f1);

    }

    protected Vec2 getRiddenRotation(LivingEntity entity) {
        return new Vec2(entity.getXRot() * 0.5F, entity.getYRot());
    }

    @Override
    public boolean doHurtTarget(@NotNull Entity entityIn) {
        if (this.getAnimation() != ANIMATION_SCRATCH && this.getAnimation() != ANIMATION_BITE) {
            this.setAnimation(this.getRandom().nextBoolean() ? ANIMATION_SCRATCH : ANIMATION_BITE);
        } else {
            return true;
        }
        return false;
    }

    // FIXME: There's something majorly wrong with hovering/flying logic. Results in Hippogryphs not landing and other animation issues
    @Override
    public void aiStep() {
        super.aiStep();
        //switchNavigator();
        if (level().getDifficulty() == Difficulty.PEACEFUL && this.getTarget() instanceof Player) {
            this.setTarget(null);
        }
        if (!this.level().isClientSide) {
            if (this.isOrderedToSit() && (this.getCommand() != 1 || this.getControllingPassenger() != null)) {
                this.setOrderedToSit(false);
            }
            if (!this.isOrderedToSit() && this.getCommand() == 1 && this.getControllingPassenger() == null) {
                this.setOrderedToSit(true);
            }
            if (this.isOrderedToSit()) {
                this.getNavigation().stop();
            }
            if (this.random.nextInt(900) == 0 && this.deathTime == 0) {
                this.heal(1.0F);
            }
        }
        if (this.getAnimation() == ANIMATION_BITE && this.getTarget() != null && this.getAnimationTick() == 6) {
            double dist = this.distanceToSqr(this.getTarget());
            if (dist < 8) {
                this.getTarget().hurt(this.level().damageSources().mobAttack(this), ((int) this.getAttribute(Attributes.ATTACK_DAMAGE).getValue()));
            }
        }
        LivingEntity attackTarget = this.getTarget();
        if (this.getAnimation() == ANIMATION_SCRATCH && attackTarget != null && this.getAnimationTick() == 6) {
            double dist = this.distanceToSqr(attackTarget);

            if (dist < 8) {
                attackTarget.hurt(this.level().damageSources().mobAttack(this), ((int) this.getAttribute(Attributes.ATTACK_DAMAGE).getValue()));
                attackTarget.hasImpulse = true;
                float f = Mth.sqrt((float) (0.5 * 0.5 + 0.5 * 0.5));
                attackTarget.setDeltaMovement(attackTarget.getDeltaMovement().add(-0.5 / (double) f, 1, -0.5 / (double) f));
                attackTarget.setDeltaMovement(attackTarget.getDeltaMovement().multiply(0.5D, 1, 0.5D));

                if (attackTarget.onGround()) {
                    attackTarget.setDeltaMovement(attackTarget.getDeltaMovement().add(0, 0.3, 0));
                }
            }
        }
        if (!level().isClientSide && !this.isOverAir() && this.getNavigation().isDone() && attackTarget != null && attackTarget.getY() - 3 > this.getY() && this.getRandom().nextInt(15) == 0 && this.canMove() && !this.isHovering() && !this.isFlying()) {
            this.setHovering(true);
            this.hoverTicks = 0;
            this.flyTicks = 0;
        }
        if (this.isOverAir()) {
            airBorneCounter++;
        } else {
            airBorneCounter = 0;
        }
        if (hasChestVarChanged && hippogryphInventory != null && !this.isChested()) {
            for (int i = 3; i < 18; i++) {
                if (!hippogryphInventory.getItem(i).isEmpty()) {
                    if (!level().isClientSide) {
                        this.spawnAtLocation(hippogryphInventory.getItem(i), 1);
                    }
                    hippogryphInventory.removeItemNoUpdate(i);
                }
            }
            hasChestVarChanged = false;
        }
        if (this.isFlying() && this.tickCount % 40 == 0 || this.isFlying() && this.isOrderedToSit()) {
            this.setFlying(true);
        }
        if (!this.canMove() && attackTarget != null) {
            this.setTarget(null);
        }
        if (!this.canMove()) {
            this.getNavigation().stop();

        }
        AnimationHandler.INSTANCE.updateAnimations(this);
        boolean sitting = isOrderedToSit() && !isHovering() && !isFlying();
        if (sitting && sitProgress < 20.0F) {
            sitProgress += 0.5F;
        } else if (!sitting && sitProgress > 0.0F) {
            sitProgress -= 0.5F;
        }

        boolean hovering = isHovering();
        if (hovering && hoverProgress < 20.0F) {
            hoverProgress += 0.5F;
        } else if (!hovering && hoverProgress > 0.0F) {
            hoverProgress -= 0.5F;
        }
        boolean flying = this.isFlying() || this.isHovering() && airBorneCounter > 10;
        if (flying && flyProgress < 20.0F) {
            flyProgress += 0.5F;
        } else if (!flying && flyProgress > 0.0F) {
            flyProgress -= 0.5F;
        }
        if (flying && this.isLandNavigator) {
            switchNavigator(false);
        }
        if (!flying && !this.isLandNavigator) {
            switchNavigator(true);
        }
        if ((flying || hovering) && !doesWantToLand() && this.getControllingPassenger() == null) {
            double up = isInWater() ? 0.16D : 0.08D;
            this.setDeltaMovement(this.getDeltaMovement().add(0, up, 0));
        }
        if ((flying || hovering) && tickCount % 20 == 0 && this.isOverAir()) {
            this.playSound(SoundEvents.ENDER_DRAGON_FLAP, this.getSoundVolume() * (IafConfig.dragonFlapNoiseDistance / 2), 0.6F + this.random.nextFloat() * 0.6F * this.getVoicePitch());
        }
        if (this.onGround() && this.doesWantToLand() && (this.isFlying() || this.isHovering())) {
            this.setFlying(false);
            this.setHovering(false);
        }
        if (this.isHovering()) {
            if (this.isOrderedToSit()) {
                this.setHovering(false);
            }
            this.hoverTicks++;
            if (this.doesWantToLand()) {
                this.setDeltaMovement(this.getDeltaMovement().add(0, -0.05D, 0));
            } else {
                if (this.getControllingPassenger() == null) {
                    this.setDeltaMovement(this.getDeltaMovement().add(0, 0.08D, 0));
                }
                if (this.hoverTicks > 40) {
                    if (!this.isBaby()) {
                        this.setFlying(true);
                    }
                    this.setHovering(false);
                    this.hoverTicks = 0;
                    this.flyTicks = 0;
                }
            }
        }
        if (this.isOrderedToSit()) {
            this.getNavigation().stop();
        }
        if (this.onGround() && flyTicks != 0) {
            flyTicks = 0;
        }
        if (this.isFlying() && this.doesWantToLand() && this.getControllingPassenger() == null) {
            this.setHovering(false);
            if (this.onGround()) {
                flyTicks = 0;
            }
            this.setFlying(false);
        }
        if (this.isFlying()) {
            this.flyTicks++;
        }
        if ((this.isHovering() || this.isFlying()) && this.isOrderedToSit()) {
            this.setFlying(false);
            this.setHovering(false);
        }
        if (this.isVehicle() && this.isGoingDown() && this.onGround()) {
            this.setHovering(false);
            this.setFlying(false);
        }
        if ((!level().isClientSide && this.getRandom().nextInt(FLIGHT_CHANCE_PER_TICK) == 0 && !this.isOrderedToSit() && !this.isFlying() && this.getPassengers().isEmpty() && !this.isBaby() && !this.isHovering() && !this.isOrderedToSit() && this.canMove() && !this.isOverAir() || this.getY() < -1)) {
            this.setHovering(true);
            this.hoverTicks = 0;
            this.flyTicks = 0;
        }
        if (getTarget() != null && !this.getPassengers().isEmpty() && this.getOwner() != null && this.getPassengers().contains(this.getOwner())) {
            this.setTarget(null);
        }
    }

    public boolean doesWantToLand() {
        return (this.flyTicks > 200 || flyTicks > 40 && this.flyProgress == 0) && !this.isVehicle();
    }

    @Override
    public void tick() {
        super.tick();
        isOverAir = this.isOverAirLogic();
        if (this.isGoingUp()) {
            if (this.airBorneCounter == 0) {
                this.setDeltaMovement(this.getDeltaMovement().add(0, 0.02F, 0));
            }
            if (!this.isFlying() && !this.isHovering()) {
                this.spacebarTicks += 2;
            }
        } else if (this.dismountIAF()) {
            if (this.isFlying() || this.isHovering()) {
                this.setFlying(false);
                this.setHovering(false);
            }
        }
        if (this.attack() && this.getControllingPassenger() != null && this.getControllingPassenger() instanceof Player) {

            LivingEntity target = DragonUtils.riderLookingAtEntity(this, (Player) this.getControllingPassenger(), 3);
            if (this.getAnimation() != ANIMATION_BITE && this.getAnimation() != ANIMATION_SCRATCH) {
                this.setAnimation(this.getRandom().nextBoolean() ? ANIMATION_SCRATCH : ANIMATION_BITE);
            }
            if (target != null && this.getAnimationTick() >= 10 && this.getAnimationTick() < 13) {
                target.hurt(this.level().damageSources().mobAttack(this), ((int) this.getAttribute(Attributes.ATTACK_DAMAGE).getValue()));
            }
        }
        if (this.getControllingPassenger() != null && this.getControllingPassenger().isShiftKeyDown()) {
            this.getControllingPassenger().stopRiding();
        }

        double motion = this.getDeltaMovement().x * this.getDeltaMovement().x + this.getDeltaMovement().z * this.getDeltaMovement().z;//Use squared norm2

        if (this.isFlying() && !this.isHovering() && this.getControllingPassenger() != null && this.isOverAir() && motion < 0.01F) {
            this.setHovering(true);
            this.setFlying(false);
        }
        if (this.isHovering() && !this.isFlying() && this.getControllingPassenger() != null && this.isOverAir() && motion > 0.01F) {
            this.setFlying(true);
            this.setHovering(false);
        }
        if (this.spacebarTicks > 0) {
            this.spacebarTicks--;
        }
        if (this.spacebarTicks > 10 && this.getOwner() != null && this.getPassengers().contains(this.getOwner()) && !this.isFlying() && !this.isHovering()) {
            this.setHovering(true);
        }
        if (this.getTarget() != null && this.getVehicle() == null && !this.getTarget().isAlive() || this.getTarget() != null && this.getTarget() instanceof EntityDragonBase && !this.getTarget().isAlive()) {
            this.setTarget(null);
        }
    }

    public boolean isTargetBlocked(Vec3 target) {
        if (target != null) {
            BlockHitResult rayTrace = this.level().clip(new ClipContext(this.getEyePosition(1.0F), target, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
            BlockPos pos = rayTrace.getBlockPos();
            return !level().isEmptyBlock(pos);
        }
        return false;
    }

    public float getDistanceSquared(Vec3 Vector3d) {
        float f = (float) (this.getX() - Vector3d.x);
        float f1 = (float) (this.getY() - Vector3d.y);
        float f2 = (float) (this.getZ() - Vector3d.z);
        return f * f + f1 * f1 + f2 * f2;
    }

    @Override
    public void die(@NotNull DamageSource cause) {
        super.die(cause);
        if (hippogryphInventory != null && !this.level().isClientSide) {
            for (int i = 0; i < hippogryphInventory.getContainerSize(); ++i) {
                ItemStack itemstack = hippogryphInventory.getItem(i);
                if (!itemstack.isEmpty()) {
                    this.spawnAtLocation(itemstack, 0.0F);
                }
            }
        }
    }

    public void refreshInventory() {
        //This isn't needed (anymore) since it's already being handled by minecraft
        if (!this.level().isClientSide) {
            ItemStack saddle = this.hippogryphInventory.getItem(0);
            ItemStack chest = this.hippogryphInventory.getItem(1);
            this.setSaddled(saddle.getItem() == Items.SADDLE && !saddle.isEmpty());
            this.setChested(chest.getItem() == Blocks.CHEST.asItem() && !chest.isEmpty());
            this.setArmor(getIntFromArmor(this.hippogryphInventory.getItem(2)));
        }
        /*if (this.world.isRemote) {
            IceAndFire.NETWORK_WRAPPER.sendToServer(new MessageHippogryphArmor(this.getEntityId(), 0, saddle != null && saddle.getItem() == Items.SADDLE && !saddle.isEmpty() ? 1 : 0));
            IceAndFire.NETWORK_WRAPPER.sendToServer(new MessageHippogryphArmor(this.getEntityId(), 1, chest != null && chest.getItem() == Item.getItemFromBlock(Blocks.CHEST) && !chest.isEmpty() ? 1 : 0));
            IceAndFire.NETWORK_WRAPPER.sendToServer(new MessageHippogryphArmor(this.getEntityId(), 2, this.getIntFromArmor(this.hippogryphInventory.getStackInSlot(2))));
        }*/

    }

    protected void switchNavigator(boolean onLand) {
        if (onLand) {
            this.moveControl = new MoveControl(this);
            this.navigation = createNavigator(level(), AdvancedPathNavigate.MovementType.CLIMBING);
            this.isLandNavigator = true;
        } else {
            this.moveControl = new FlyingMoveControl(this, 10, true);
            this.navigation = createNavigator(level(), AdvancedPathNavigate.MovementType.FLYING);
            this.isLandNavigator = false;
        }
    }

    @Override
    protected @NotNull PathNavigation createNavigation(@NotNull Level worldIn) {
        return createNavigator(worldIn, AdvancedPathNavigate.MovementType.CLIMBING);
    }

    protected PathNavigation createNavigator(Level worldIn, AdvancedPathNavigate.MovementType type) {
        return createNavigator(worldIn, type, 2, 2);
    }

    protected PathNavigation createNavigator(Level worldIn, AdvancedPathNavigate.MovementType type, float width, float height) {
        AdvancedPathNavigate newNavigator = new AdvancedPathNavigate(this, level(), type, width, height);
        this.navigation = newNavigator;
        newNavigator.setCanFloat(true);
        newNavigator.getNodeEvaluator().setCanOpenDoors(true);
        return newNavigator;
    }

    @Override
    public boolean isAlliedTo(@NotNull Entity entityIn) {
        if (this.isTame()) {
            LivingEntity livingentity = this.getOwner();
            if (entityIn == livingentity) {
                return true;
            }
            if (entityIn instanceof TamableAnimal) {
                return ((TamableAnimal) entityIn).isOwnedBy(livingentity);
            }
            if (livingentity != null) {
                return livingentity.isAlliedTo(entityIn);
            }
        }

        return super.isAlliedTo(entityIn);
    }

    @Override
    public void onHearFlute(Player player) {
        if (this.isTame() && this.isOwnedBy(player)) {
            if (this.isFlying() || this.isHovering()) {
                this.setFlying(false);
                this.setHovering(false);
            }
        }
    }

    @Override
    public boolean shouldAnimalsFear(Entity entity) {
        return DragonUtils.canTameDragonAttack(this, entity);
    }

    @Override
    public void dropArmor() {
        if (hippogryphInventory != null && !this.level().isClientSide) {
            for (int i = 0; i < hippogryphInventory.getContainerSize(); ++i) {
                ItemStack itemstack = hippogryphInventory.getItem(i);
                if (!itemstack.isEmpty()) {
                    this.spawnAtLocation(itemstack, 0.0F);
                }
            }
        }
    }

    @Override
    public boolean isPersistenceRequired() {
        return true;
    }

}
