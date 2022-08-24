package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.ai.*;
import com.github.alexthe666.iceandfire.entity.util.*;
import com.github.alexthe666.iceandfire.enums.EnumHippogryphTypes;
import com.github.alexthe666.iceandfire.inventory.ContainerHippogryph;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.message.MessageHippogryphArmor;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import com.github.alexthe666.iceandfire.pathfinding.PathNavigateFlyingCreature;
import com.github.alexthe666.iceandfire.pathfinding.raycoms.AdvancedPathNavigate;
import com.github.alexthe666.iceandfire.world.IafWorldRegistry;
import com.google.common.base.Predicate;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.*;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class EntityHippogryph extends TameableEntity implements ISyncMount, IAnimatedEntity, IDragonFlute, IVillagerFear, IAnimalFear, IDropArmor, IFlyingMount, ICustomMoveController {

    private static final int FLIGHT_CHANCE_PER_TICK = 1200;
    private static final DataParameter<Integer> VARIANT = EntityDataManager.defineId(EntityHippogryph.class, DataSerializers.INT);
    private static final DataParameter<Boolean> SADDLE = EntityDataManager.defineId(EntityHippogryph.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> ARMOR = EntityDataManager.defineId(EntityHippogryph.class, DataSerializers.INT);
    private static final DataParameter<Boolean> CHESTED = EntityDataManager.defineId(EntityHippogryph.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> HOVERING = EntityDataManager.defineId(EntityHippogryph.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> FLYING = EntityDataManager.defineId(EntityHippogryph.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Byte> CONTROL_STATE = EntityDataManager.defineId(EntityHippogryph.class, DataSerializers.BYTE);
    private static final DataParameter<Integer> COMMAND = EntityDataManager.defineId(EntityHippogryph.class, DataSerializers.INT);
    public static Animation ANIMATION_EAT;
    public static Animation ANIMATION_SPEAK;
    public static Animation ANIMATION_SCRATCH;
    public static Animation ANIMATION_BITE;
    public Inventory hippogryphInventory;

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
    private int navigatorType = -1;
    private boolean isOverAir;

    public EntityHippogryph(EntityType type, World worldIn) {
        super(type, worldIn);
        this.switchNavigator(true);
        ANIMATION_EAT = Animation.create(25);
        ANIMATION_SPEAK = Animation.create(15);
        ANIMATION_SCRATCH = Animation.create(25);
        ANIMATION_BITE = Animation.create(20);
        initHippogryphInv();
        this.maxUpStep = 1;
    }

    public static int getIntFromArmor(ItemStack stack) {
        if (!stack.isEmpty() && stack.getItem() != null && stack.getItem() == IafItemRegistry.IRON_HIPPOGRYPH_ARMOR) {
            return 1;
        }
        if (!stack.isEmpty() && stack.getItem() != null && stack.getItem() == IafItemRegistry.GOLD_HIPPOGRYPH_ARMOR) {
            return 2;
        }
        if (!stack.isEmpty() && stack.getItem() != null && stack.getItem() == IafItemRegistry.DIAMOND_HIPPOGRYPH_ARMOR) {
            return 3;
        }
        return 0;
    }

    public static AttributeModifierMap.MutableAttribute bakeAttributes() {
        return MobEntity.createMobAttributes()
            //HEALTH
            .add(Attributes.MAX_HEALTH, 40.0D)
            //SPEED
            .add(Attributes.MOVEMENT_SPEED, 0.3D)
            //ATTACK
            .add(Attributes.ATTACK_DAMAGE, 5.0D)
            //FOLLOW RANGE
            .add(Attributes.FOLLOW_RANGE, 32.0D);
    }

    protected void switchNavigator() {
        if (this.isVehicle() && this.isOverAir()) {
            if (navigatorType != 1) {
                this.moveControl = new IafDragonFlightManager.PlayerFlightMoveHelper(this);
                this.navigation = new PathNavigateFlyingCreature(this, level);
                navigatorType = 1;
            }
        }
        if (!this.isVehicle() || !this.isOverAir()) {
            if (navigatorType != 0) {
                this.moveControl = new MovementController(this);
                this.navigation = new GroundPathNavigator(this, level);
                navigatorType = 0;
            }
        }
    }

    protected boolean isOverAir() {
        return isOverAir;
    }

    private boolean isOverAirLogic() {
        return level.isEmptyBlock(new BlockPos(this.getX(), this.getBoundingBox().minY - 1, this.getZ()));
    }

    protected int getExperienceReward(PlayerEntity player) {
        return 10;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new DragonAIRide(this));
        this.goalSelector.addGoal(1, new SwimGoal(this));
        this.goalSelector.addGoal(2, new SitGoal(this));
        this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.5D, true));
        this.goalSelector.addGoal(4, new HippogryphAIMate(this, 1.0D));
        this.goalSelector.addGoal(5, new TemptGoal(this, 1.0D, Ingredient.of(Items.RABBIT, Items.COOKED_RABBIT), false));
        this.goalSelector.addGoal(6, new AIFlyRandom());
        this.goalSelector.addGoal(7, new HippogryphAIWander(this, 1.0D));
        this.goalSelector.addGoal(8, new LookAtGoal(this, LivingEntity.class, 6.0F));
        this.goalSelector.addGoal(8, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(3, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(4, new HippogryphAITargetItems(this, false));
        this.targetSelector.addGoal(5, new HippogryphAITarget(this, LivingEntity.class, false, new Predicate<Entity>() {
            @Override
            public boolean apply(@Nullable Entity entity) {
                return entity instanceof LivingEntity && !(entity instanceof AbstractHorseEntity) && DragonUtils.isAlive((LivingEntity) entity);
            }
        }));
        this.targetSelector.addGoal(5, new HippogryphAITarget(this, PlayerEntity.class, 350, false, new Predicate<PlayerEntity>() {
            @Override
            public boolean apply(@Nullable PlayerEntity entity) {
                return !entity.isCreative();
            }
        }));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(VARIANT, Integer.valueOf(0));
        this.entityData.define(ARMOR, Integer.valueOf(0));
        this.entityData.define(SADDLE, Boolean.valueOf(false));
        this.entityData.define(CHESTED, Boolean.valueOf(false));
        this.entityData.define(HOVERING, Boolean.valueOf(false));
        this.entityData.define(FLYING, Boolean.valueOf(false));
        this.entityData.define(CONTROL_STATE, Byte.valueOf((byte) 0));
        this.entityData.define(COMMAND, Integer.valueOf(0));

    }

    public double getYSpeedMod() {
        return 4;
    }

    protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
    }

    public boolean checkSpawnRules(IWorld worldIn, SpawnReason spawnReasonIn) {
        if (worldIn instanceof IServerWorld && !IafWorldRegistry.isDimensionListedForMobs((IServerWorld) level)) {
            return false;
        }
        return super.checkSpawnRules(worldIn, spawnReasonIn);
    }

    @Override
    public boolean isControlledByLocalInstance() {
        return false;
    }

    @Override
    public boolean canBeControlledByRider() {
        return true;
    }

    public void positionRider(Entity passenger) {
        super.positionRider(passenger);
        if (this.hasPassenger(passenger)) {
            yBodyRot = yRot;
            this.yRot = passenger.yRot;
        }
        passenger.setPos(this.getX(), this.getY() + 1.05F, this.getZ());
    }

    private void initHippogryphInv() {
        Inventory animalchest = this.hippogryphInventory;
        this.hippogryphInventory = new Inventory(18);
        if (animalchest != null) {
            int i = Math.min(animalchest.getContainerSize(), this.hippogryphInventory.getContainerSize());
            for (int j = 0; j < i; ++j) {
                ItemStack itemstack = animalchest.getItem(j);
                if (!itemstack.isEmpty()) {
                    this.hippogryphInventory.setItem(j, itemstack.copy());
                }
            }

            if (level.isClientSide) {
                ItemStack saddle = animalchest.getItem(0);
                ItemStack chest = animalchest.getItem(1);
                IceAndFire.NETWORK_WRAPPER.sendToServer(new MessageHippogryphArmor(this.getId(), 0, saddle != null && saddle.getItem() == Items.SADDLE && !saddle.isEmpty() ? 1 : 0));
                IceAndFire.NETWORK_WRAPPER.sendToServer(new MessageHippogryphArmor(this.getId(), 1, chest != null && chest.getItem() == Blocks.CHEST.asItem() && !chest.isEmpty() ? 1 : 0));
                IceAndFire.NETWORK_WRAPPER.sendToServer(new MessageHippogryphArmor(this.getId(), 2, getIntFromArmor(animalchest.getItem(2))));
            }
        }
    }

    @Nullable
    public Entity getControllingPassenger() {
        for (Entity passenger : this.getPassengers()) {
            if (passenger instanceof PlayerEntity && this.getTarget() != passenger) {
                PlayerEntity player = (PlayerEntity) passenger;
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

    public ActionResultType mobInteract(PlayerEntity player, Hand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        String s = TextFormatting.stripFormatting(player.getName().getContents());
        boolean isDev = s.equals("Alexthe666") || s.equals("Raptorfarian") || s.equals("tweakbsd");
        if (this.isTame() && this.isOwnedBy(player)) {
            if (itemstack != null && itemstack.getItem() == Items.RED_DYE && this.getEnumVariant() != EnumHippogryphTypes.ALEX && isDev) {
                this.setEnumVariant(EnumHippogryphTypes.ALEX);
                if (!player.isCreative()) {
                    itemstack.shrink(1);
                }
                this.playSound(SoundEvents.ZOMBIE_INFECT, 1, 1);
                for (int i = 0; i < 20; i++) {
                    this.level.addParticle(ParticleTypes.CLOUD, this.getX() + (double) (this.random.nextFloat() * this.getBbWidth() * 2.0F) - (double) this.getBbWidth(), this.getY() + (double) (this.random.nextFloat() * this.getBbHeight()), this.getZ() + (double) (this.random.nextFloat() * this.getBbWidth() * 2.0F) - (double) this.getBbWidth(), 0, 0, 0);
                }
                return ActionResultType.SUCCESS;
            }
            if (itemstack != null && itemstack.getItem() == Items.LIGHT_GRAY_DYE && this.getEnumVariant() != EnumHippogryphTypes.RAPTOR && isDev) {
                this.setEnumVariant(EnumHippogryphTypes.RAPTOR);
                if (!player.isCreative()) {
                    itemstack.shrink(1);
                }
                this.playSound(SoundEvents.ZOMBIE_INFECT, 1, 1);
                for (int i = 0; i < 20; i++) {
                    this.level.addParticle(ParticleTypes.CLOUD, this.getX() + (double) (this.random.nextFloat() * this.getBbWidth() * 2.0F) - (double) this.getBbWidth(), this.getY() + (double) (this.random.nextFloat() * this.getBbHeight()), this.getZ() + (double) (this.random.nextFloat() * this.getBbWidth() * 2.0F) - (double) this.getBbWidth(), 0, 0, 0);
                }
                return ActionResultType.SUCCESS;
            }
            if (itemstack != null && itemstack.getItem() == Items.RABBIT_STEW && this.getAge() == 0 && !isInLove()) {
                this.setInLove(player);
                this.playSound(SoundEvents.GENERIC_EAT, 1, 1);
                if (!player.isCreative()) {
                    itemstack.shrink(1);
                }
                return ActionResultType.SUCCESS;
            }
            if (itemstack != null && itemstack.getItem() == Items.STICK) {
                if (player.isShiftKeyDown()) {
                    if (this.hasHomePosition) {
                        this.hasHomePosition = false;
                        player.displayClientMessage(new TranslationTextComponent("hippogryph.command.remove_home"), true);
                        return ActionResultType.SUCCESS;
                    } else {
                        BlockPos pos = this.blockPosition();
                        this.homePos = pos;
                        this.hasHomePosition = true;
                        player.displayClientMessage(new TranslationTextComponent("hippogryph.command.new_home", homePos.getX(), homePos.getY(), homePos.getZ()), true);
                        return ActionResultType.SUCCESS;
                    }
                } else {
                    this.setCommand(this.getCommand() + 1);
                    if (this.getCommand() > 1) {
                        this.setCommand(0);
                    }
                    player.displayClientMessage(new TranslationTextComponent("hippogryph.command." + (this.getCommand() == 1 ? "sit" : "stand")), true);

                }
                return ActionResultType.SUCCESS;
            }
            if (itemstack != null && itemstack.getItem() == Items.GLISTERING_MELON_SLICE && this.getEnumVariant() != EnumHippogryphTypes.DODO) {
                this.setEnumVariant(EnumHippogryphTypes.DODO);
                if (!player.isCreative()) {
                    itemstack.shrink(1);
                }
                this.playSound(SoundEvents.ZOMBIE_INFECT, 1, 1);
                for (int i = 0; i < 20; i++) {
                    this.level.addParticle(ParticleTypes.ENCHANT, this.getX() + (double) (this.random.nextFloat() * this.getBbWidth() * 2.0F) - (double) this.getBbWidth(), this.getY() + (double) (this.random.nextFloat() * this.getBbHeight()), this.getZ() + (double) (this.random.nextFloat() * this.getBbWidth() * 2.0F) - (double) this.getBbWidth(), 0, 0, 0);
                }
                return ActionResultType.SUCCESS;
            }
            if (itemstack != null && itemstack.getItem().isEdible() && itemstack.getItem().getFoodProperties() != null && itemstack.getItem().getFoodProperties().isMeat() && this.getHealth() < this.getMaxHealth()) {
                this.heal(5);
                this.playSound(SoundEvents.GENERIC_EAT, 1, 1);
                for (int i = 0; i < 3; i++) {
                    this.level.addParticle(new ItemParticleData(ParticleTypes.ITEM, itemstack), this.getX() + (double) (this.random.nextFloat() * this.getBbWidth() * 2.0F) - (double) this.getBbWidth(), this.getY() + (double) (this.random.nextFloat() * this.getBbHeight()), this.getZ() + (double) (this.random.nextFloat() * this.getBbWidth() * 2.0F) - (double) this.getBbWidth(), 0, 0, 0);
                }
                if (!player.isCreative()) {
                    itemstack.shrink(1);
                }
                return ActionResultType.SUCCESS;
            }
            if (itemstack.isEmpty()) {
                if (player.isShiftKeyDown()) {
                    this.openGUI(player);
                    return ActionResultType.SUCCESS;
                } else if (this.isSaddled() && !this.isBaby() && !player.isPassenger()) {
                    player.startRiding(this, true);
                    return ActionResultType.SUCCESS;
                }
            }
        }
        return super.mobInteract(player, hand);
    }

    public void openGUI(PlayerEntity playerEntity) {
        if (!this.level.isClientSide && (!this.isVehicle() || this.hasPassenger(playerEntity))) {
            NetworkHooks.openGui((ServerPlayerEntity) playerEntity, new INamedContainerProvider() {
                @Override
                public Container createMenu(int p_createMenu_1_, PlayerInventory p_createMenu_2_, PlayerEntity p_createMenu_3_) {
                    return new ContainerHippogryph(p_createMenu_1_, hippogryphInventory, p_createMenu_2_, EntityHippogryph.this);
                }

                @Override
                public ITextComponent getDisplayName() {
                    return new TranslationTextComponent("entity.iceandfire.hippogryph");
                }
            });
        }
        IceAndFire.PROXY.setReferencedMob(this);
    }

    public boolean isGoingUp() {
        return (entityData.get(CONTROL_STATE).byteValue() & 1) == 1;
    }

    public boolean isGoingDown() {
        return (entityData.get(CONTROL_STATE).byteValue() >> 1 & 1) == 1;
    }

    public boolean attack() {
        return (entityData.get(CONTROL_STATE).byteValue() >> 2 & 1) == 1;
    }

    public boolean dismountIAF() {
        return (entityData.get(CONTROL_STATE).byteValue() >> 3 & 1) == 1;
    }

    public void up(boolean up) {
        setStateField(0, up);
    }

    public void down(boolean down) {
        setStateField(1, down);
    }

    public void attack(boolean attack) {
        setStateField(2, attack);
    }

    @Override
    public void strike(boolean strike) {

    }

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

    public byte getControlState() {
        return entityData.get(CONTROL_STATE).byteValue();
    }

    public void setControlState(byte state) {
        entityData.set(CONTROL_STATE, Byte.valueOf(state));
    }

    public int getCommand() {
        return Integer.valueOf(this.entityData.get(COMMAND).intValue());
    }

    public void setCommand(int command) {
        this.entityData.set(COMMAND, Integer.valueOf(command));
        this.setOrderedToSit(command == 1);
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Variant", this.getVariant());
        compound.putBoolean("Chested", this.isChested());
        compound.putBoolean("Saddled", this.isSaddled());
        compound.putBoolean("Hovering", this.isHovering());
        compound.putBoolean("Flying", this.isFlying());
        compound.putInt("Armor", this.getArmor());
        compound.putInt("Feedings", feedings);
        if (hippogryphInventory != null) {
            ListNBT nbttaglist = new ListNBT();
            for (int i = 0; i < this.hippogryphInventory.getContainerSize(); ++i) {
                ItemStack itemstack = this.hippogryphInventory.getItem(i);
                if (!itemstack.isEmpty()) {
                    CompoundNBT CompoundNBT = new CompoundNBT();
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
    public void readAdditionalSaveData(CompoundNBT compound) {
        super.readAdditionalSaveData(compound);
        this.setVariant(compound.getInt("Variant"));
        this.setChested(compound.getBoolean("Chested"));
        this.setSaddled(compound.getBoolean("Saddled"));
        this.setHovering(compound.getBoolean("Hovering"));
        this.setFlying(compound.getBoolean("Flying"));
        this.setArmor(compound.getInt("Armor"));
        feedings = compound.getInt("Feedings");
        if (hippogryphInventory != null) {
            ListNBT nbttaglist = compound.getList("Items", 10);
            this.initHippogryphInv();
            for (int i = 0; i < nbttaglist.size(); ++i) {
                CompoundNBT CompoundNBT = nbttaglist.getCompound(i);
                int j = CompoundNBT.getByte("Slot") & 255;
                this.hippogryphInventory.setItem(j, ItemStack.of(CompoundNBT));
            }
        } else {
            ListNBT nbttaglist = compound.getList("Items", 10);
            this.initHippogryphInv();
            for (int i = 0; i < nbttaglist.size(); ++i) {
                CompoundNBT CompoundNBT = nbttaglist.getCompound(i);
                int j = CompoundNBT.getByte("Slot") & 255;
                this.initHippogryphInv();
                this.hippogryphInventory.setItem(j, ItemStack.of(CompoundNBT));
                //this.setArmorInSlot(j, this.getIntFromArmor(ItemStack.loadItemStackFromNBT(CompoundNBT)));
                ItemStack saddle = hippogryphInventory.getItem(0);
                ItemStack chest = hippogryphInventory.getItem(1);
                if (level.isClientSide) {
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
    }

    public int getVariant() {
        return Integer.valueOf(this.entityData.get(VARIANT).intValue());
    }

    public void setVariant(int variant) {
        this.entityData.set(VARIANT, Integer.valueOf(variant));
    }

    public EnumHippogryphTypes getEnumVariant() {
        return EnumHippogryphTypes.values()[this.getVariant()];
    }

    public void setEnumVariant(EnumHippogryphTypes variant) {
        this.setVariant(variant.ordinal());
    }

    public boolean isSaddled() {
        return Boolean.valueOf(this.entityData.get(SADDLE).booleanValue());
    }

    public void setSaddled(boolean saddle) {
        this.entityData.set(SADDLE, Boolean.valueOf(saddle));
    }

    public boolean isChested() {
        return Boolean.valueOf(this.entityData.get(CHESTED).booleanValue());
    }

    public void setChested(boolean chested) {
        this.entityData.set(CHESTED, Boolean.valueOf(chested));
        this.hasChestVarChanged = true;
    }

    public boolean isOrderedToSit() {
        if (level.isClientSide) {
            boolean isSitting = (this.entityData.get(DATA_FLAGS_ID).byteValue() & 1) != 0;
            this.isSitting = isSitting;
            return isSitting;
        }
        return isSitting;
    }

    public void setOrderedToSit(boolean sitting) {
        if (!level.isClientSide) {
            this.isSitting = sitting;
        }
        byte b0 = this.entityData.get(DATA_FLAGS_ID).byteValue();
        if (sitting) {
            this.entityData.set(DATA_FLAGS_ID, Byte.valueOf((byte) (b0 | 1)));
        } else {
            this.entityData.set(DATA_FLAGS_ID, Byte.valueOf((byte) (b0 & -2)));
        }
    }

    public boolean isHovering() {
        if (level.isClientSide) {
            return this.isHovering = Boolean.valueOf(this.entityData.get(HOVERING).booleanValue());
        }
        return isHovering;
    }

    public void setHovering(boolean hovering) {
        this.entityData.set(HOVERING, Boolean.valueOf(hovering));
        if (!level.isClientSide) {
            this.isHovering = Boolean.valueOf(hovering);
        }
    }

    public boolean isRidingPlayer(PlayerEntity player) {
        return getRidingPlayer() != null && player != null && getRidingPlayer().getUUID().equals(player.getUUID());
    }

    @Nullable
    public PlayerEntity getRidingPlayer() {
        if (this.getControllingPassenger() instanceof PlayerEntity) {
            return (PlayerEntity) this.getControllingPassenger();
        }
        return null;
    }

    @Override
    public double getFlightSpeedModifier() {
        return IafConfig.hippogryphFlightSpeedMod * 0.9F;
    }

    public boolean isFlying() {
        if (level.isClientSide) {
            return this.isFlying = Boolean.valueOf(this.entityData.get(FLYING).booleanValue());
        }
        return isFlying;
    }

    public void setFlying(boolean flying) {
        this.entityData.set(FLYING, Boolean.valueOf(flying));
        if (!level.isClientSide) {
            this.isFlying = Boolean.valueOf(flying);
        }
    }

    public int getArmor() {
        return Integer.valueOf(this.entityData.get(ARMOR).intValue());
    }

    public void setArmor(int armorType) {
        this.entityData.set(ARMOR, Integer.valueOf(armorType));
        double armorValue = 0;
        switch (armorType) {
            case 1:
                armorValue = 10;
                break;
            case 2:
                armorValue = 20;
                break;
            case 3:
                armorValue = 30;
        }
        this.getAttribute(Attributes.ARMOR).setBaseValue(armorValue);
    }

    public boolean canMove() {
        return !this.isOrderedToSit() && this.getControllingPassenger() == null && sitProgress == 0;
    }

    @Nullable
    public ILivingEntityData finalizeSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        ILivingEntityData data = super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
        this.setEnumVariant(EnumHippogryphTypes.getBiomeType(worldIn.getBiome(this.blockPosition())));
        return data;
    }

    @Override
    public boolean hurt(DamageSource dmg, float i) {
        if (this.isVehicle() && dmg.getEntity() != null && this.getControllingPassenger() != null && dmg.getEntity() == this.getControllingPassenger()) {
            return false;
        }
        return super.hurt(dmg, i);
    }

    @Nullable
    @Override
    public AgeableEntity getBreedOffspring(ServerWorld serverWorld, AgeableEntity ageable) {
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

    public void playAmbientSound() {
        if (this.getAnimation() == this.NO_ANIMATION) {
            this.setAnimation(ANIMATION_SPEAK);
        }
        super.playAmbientSound();
    }

    protected void playHurtSound(DamageSource source) {
        if (this.getAnimation() == this.NO_ANIMATION) {
            this.setAnimation(ANIMATION_SPEAK);
        }
        super.playHurtSound(source);
    }

    @Nullable
    protected SoundEvent getAmbientSound() {
        return IafSoundRegistry.HIPPOGRYPH_IDLE;
    }

    @Nullable
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return IafSoundRegistry.HIPPOGRYPH_HURT;
    }

    @Nullable
    protected SoundEvent getDeathSound() {
        return IafSoundRegistry.HIPPOGRYPH_DIE;
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[]{IAnimatedEntity.NO_ANIMATION, EntityHippogryph.ANIMATION_EAT, EntityHippogryph.ANIMATION_BITE, EntityHippogryph.ANIMATION_SPEAK, EntityHippogryph.ANIMATION_SCRATCH};
    }

    public boolean shouldDismountInWater(Entity rider) {
        return true;
    }

    public boolean isDirectPathBetweenPoints(Vector3d vec1, Vector3d vec2) {
        RayTraceResult movingobjectposition = this.level.clip(new RayTraceContext(vec1, new Vector3d(vec2.x, vec2.y + (double) this.getBbHeight() * 0.5D, vec2.z), RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, this));

        return movingobjectposition == null || movingobjectposition.getType() != RayTraceResult.Type.BLOCK;
    }


    @Override
    public void travel(Vector3d move) {
        if (!this.canMove() && !this.isVehicle()) {
            super.travel(Vector3d.ZERO);
            return;
        }
        super.travel(move);
    }

    @Override
    public boolean doHurtTarget(Entity entityIn) {
        if (this.getAnimation() != ANIMATION_SCRATCH && this.getAnimation() != ANIMATION_BITE) {
            this.setAnimation(this.getRandom().nextBoolean() ? ANIMATION_SCRATCH : ANIMATION_BITE);
        } else {
            return true;
        }
        return false;
    }

    public ItemEntity createEgg(EntityHippogryph partner) {
        int i = MathHelper.floor(this.getX());
        int j = MathHelper.floor(this.getY());
        int k = MathHelper.floor(this.getZ());
        ItemStack stack = new ItemStack(IafItemRegistry.HIPPOGRYPH_EGG);
        ItemEntity egg = new ItemEntity(this.level, i, j, k, stack);
        return egg;
    }

    public void fall(float distance, float damageMultiplier) {
    }

    @Override
    public void aiStep() {
        super.aiStep();
        //switchNavigator();
        if (level.getDifficulty() == Difficulty.PEACEFUL && this.getTarget() instanceof PlayerEntity) {
            this.setTarget(null);
        }
        if (!this.level.isClientSide) {
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
                this.getTarget().hurt(DamageSource.mobAttack(this), ((int) this.getAttribute(Attributes.ATTACK_DAMAGE).getValue()));
            }
        }
        LivingEntity attackTarget = this.getTarget();
        if (this.getAnimation() == ANIMATION_SCRATCH && attackTarget != null && this.getAnimationTick() == 6) {
            double dist = this.distanceToSqr(attackTarget);

            if (dist < 8) {
                attackTarget.hurt(DamageSource.mobAttack(this), ((int) this.getAttribute(Attributes.ATTACK_DAMAGE).getValue()));
                attackTarget.hasImpulse = true;
                float f = MathHelper.sqrt(0.5 * 0.5 + 0.5 * 0.5);
                attackTarget.setDeltaMovement(attackTarget.getDeltaMovement().add(-0.5 / (double) f, 1, -0.5 / (double) f));
                attackTarget.setDeltaMovement(attackTarget.getDeltaMovement().multiply(0.5D, 1, 0.5D));

                if (attackTarget.isOnGround()) {
                    attackTarget.setDeltaMovement(attackTarget.getDeltaMovement().add(0, 0.3, 0));
                }
            }
        }
        if (!level.isClientSide && !this.isOverAir() && this.getNavigation().isDone() && attackTarget != null && attackTarget.getY() - 3 > this.getY() && this.getRandom().nextInt(15) == 0 && this.canMove() && !this.isHovering() && !this.isFlying()) {
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
                    if (!level.isClientSide) {
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
        if ((flying || hovering) && !doesWantToLand()) {
            double up = isInWater() ? 0.16D : 0.08D;
            this.setDeltaMovement(this.getDeltaMovement().add(0, up, 0));
        }
        if ((flying || hovering) && tickCount % 20 == 0 && this.isOverAir()) {
            this.playSound(SoundEvents.ENDER_DRAGON_FLAP, this.getSoundVolume() * (IafConfig.dragonFlapNoiseDistance / 2), 0.6F + this.random.nextFloat() * 0.6F * this.getVoicePitch());
        }
        if (this.isOnGround() && this.doesWantToLand() && (this.isFlying() || this.isHovering())) {
            this.setFlying(false);
            this.setHovering(false);
        }
        if (this.isHovering()) {
            if (this.isOrderedToSit()) {
                this.setHovering(false);
            }
            this.hoverTicks++;
            if (this.doesWantToLand()) {
                this.setDeltaMovement(this.getDeltaMovement().add(0, -0.25D, 0));
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
        if (this.isOnGround() && flyTicks != 0) {
            flyTicks = 0;
        }
        if (this.isFlying() && this.doesWantToLand() && this.getControllingPassenger() == null) {
            this.setHovering(false);
            if (this.isOnGround()) {
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
        if (this.isVehicle() && this.isGoingDown() && this.isOnGround()) {
            this.setHovering(false);
            this.setFlying(false);
        }
        if ((!level.isClientSide && this.getRandom().nextInt(FLIGHT_CHANCE_PER_TICK) == 0 && !this.isOrderedToSit() && !this.isFlying() && this.getPassengers().isEmpty() && !this.isBaby() && !this.isHovering() && !this.isOrderedToSit() && this.canMove() && !this.isOverAir() || this.getY() < -1)) {
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
                this.setDeltaMovement(this.getDeltaMovement().add(0, 0.4F, 0));
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
        if (this.attack() && this.getControllingPassenger() != null && this.getControllingPassenger() instanceof PlayerEntity) {

            LivingEntity target = DragonUtils.riderLookingAtEntity(this, (PlayerEntity) this.getControllingPassenger(), 3);
            if (this.getAnimation() != ANIMATION_BITE && this.getAnimation() != ANIMATION_SCRATCH) {
                this.setAnimation(this.getRandom().nextBoolean() ? ANIMATION_SCRATCH : ANIMATION_BITE);
            }
            if (target != null && this.getAnimationTick() >= 10 && this.getAnimationTick() < 13) {
                target.hurt(DamageSource.mobAttack(this), ((int) this.getAttribute(Attributes.ATTACK_DAMAGE).getValue()));
            }
        }
        if (this.getControllingPassenger() != null && this.getControllingPassenger().isShiftKeyDown()) {
            this.getControllingPassenger().stopRiding();
        }

        double motion = this.getDeltaMovement().x * this.getDeltaMovement().x + this.getDeltaMovement().z * this.getDeltaMovement().z;//Use squared norm2

        if (this.isFlying() && !this.isHovering() && this.getControllingPassenger() != null && this.isOverAir() &&  motion < 0.01F) {
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

    public boolean isTargetBlocked(Vector3d target) {
        if (target != null) {
            RayTraceResult rayTrace = this.level.clip(new RayTraceContext(this.getEyePosition(1.0F), target, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, this));

            if (rayTrace != null && rayTrace.getLocation() != null) {
                BlockPos pos = new BlockPos(rayTrace.getLocation());
                return !level.isEmptyBlock(pos);
            }
        }
        return false;
    }

    public float getDistanceSquared(Vector3d Vector3d) {
        float f = (float) (this.getX() - Vector3d.x);
        float f1 = (float) (this.getY() - Vector3d.y);
        float f2 = (float) (this.getZ() - Vector3d.z);
        return f * f + f1 * f1 + f2 * f2;
    }

    public boolean setSlot(int inventorySlot, @Nullable ItemStack itemStackIn) {
        int j = inventorySlot - 500 + 2;
        if (j >= 0 && j < this.hippogryphInventory.getContainerSize()) {
            this.hippogryphInventory.setItem(j, itemStackIn);
            return true;
        } else {
            return false;
        }
    }

    public void die(DamageSource cause) {
        super.die(cause);
        if (hippogryphInventory != null && !this.level.isClientSide) {
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
        if (!this.level.isClientSide) {
            ItemStack saddle = this.hippogryphInventory.getItem(0);
            ItemStack chest = this.hippogryphInventory.getItem(1);
            this.setSaddled(saddle != null && saddle.getItem() == Items.SADDLE && !saddle.isEmpty());
            this.setChested(chest != null && chest.getItem() == Blocks.CHEST.asItem() && !chest.isEmpty());
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
            this.moveControl = new MovementController(this);
            this.navigation = createNavigator(level, AdvancedPathNavigate.MovementType.CLIMBING);
            this.isLandNavigator = true;
        } else {
            this.moveControl = new EntityHippogryph.FlyMoveHelper(this);
            this.navigation = createNavigator(level, AdvancedPathNavigate.MovementType.FLYING);
            this.isLandNavigator = false;
        }
    }

    protected PathNavigator createNavigation(World worldIn) {
        return createNavigator(worldIn, AdvancedPathNavigate.MovementType.CLIMBING);
    }

    protected PathNavigator createNavigator(World worldIn, AdvancedPathNavigate.MovementType type) {
        return createNavigator(worldIn, type, 2, 2);
    }

    protected PathNavigator createNavigator(World worldIn, AdvancedPathNavigate.MovementType type, float width, float height) {
        AdvancedPathNavigate newNavigator = new AdvancedPathNavigate(this, level, type, width, height);
        this.navigation = newNavigator;
        newNavigator.setCanFloat(true);
        newNavigator.getNodeEvaluator().setCanOpenDoors(true);
        return newNavigator;
    }

    @Override
    public boolean isAlliedTo(Entity entityIn) {
        if (this.isTame()) {
            LivingEntity livingentity = this.getOwner();
            if (entityIn == livingentity) {
                return true;
            }
            if (entityIn instanceof TameableEntity) {
                return ((TameableEntity) entityIn).isOwnedBy(livingentity);
            }
            if (livingentity != null) {
                return livingentity.isAlliedTo(entityIn);
            }
        }

        return super.isAlliedTo(entityIn);
    }

    @Override
    public void onHearFlute(PlayerEntity player) {
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

    public void dropArmor() {
        if (hippogryphInventory != null && !this.level.isClientSide) {
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

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    class FlyMoveHelper extends MovementController {
        public FlyMoveHelper(EntityHippogryph pixie) {
            super(pixie);
            this.speedModifier = 1.75F;
        }

        public void tick() {
            if (this.operation == MovementController.Action.MOVE_TO) {
                if (EntityHippogryph.this.horizontalCollision) {
                    EntityHippogryph.this.yRot += 180.0F;
                    BlockPos target = DragonUtils.getBlockInViewHippogryph(EntityHippogryph.this, 180);
                    this.speedModifier = 0.1F;
                    if (target != null) {
                        this.wantedX = target.getX() + 0.5F;
                        this.wantedY = target.getY() + 0.5F;
                        this.wantedZ = target.getZ() + 0.5F;
                    }
                }
                double d0 = this.wantedX - EntityHippogryph.this.getX();
                double d1 = this.wantedY - EntityHippogryph.this.getY();
                double d2 = this.wantedZ - EntityHippogryph.this.getZ();
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;
                d3 = MathHelper.sqrt(d3);

                if (d3 < EntityHippogryph.this.getBoundingBox().getSize()) {
                    this.operation = MovementController.Action.WAIT;
                    EntityHippogryph.this.setDeltaMovement(EntityHippogryph.this.getDeltaMovement().multiply(0.5D, 0.5D, 0.5D));
                } else {
                    EntityHippogryph.this.setDeltaMovement(EntityHippogryph.this.getDeltaMovement().add(d0 / d3 * 0.1D * this.speedModifier, d1 / d3 * 0.1D * this.speedModifier, d2 / d3 * 0.1D * this.speedModifier));

                    if (EntityHippogryph.this.getTarget() == null) {
                        EntityHippogryph.this.yRot = -((float) MathHelper.atan2(EntityHippogryph.this.getDeltaMovement().x, EntityHippogryph.this.getDeltaMovement().z)) * (180F / (float) Math.PI);
                        EntityHippogryph.this.yBodyRot = EntityHippogryph.this.yRot;
                    } else {
                        double d4 = EntityHippogryph.this.getTarget().getX() - EntityHippogryph.this.getX();
                        double d5 = EntityHippogryph.this.getTarget().getZ() - EntityHippogryph.this.getZ();
                        EntityHippogryph.this.yRot = -((float) MathHelper.atan2(d4, d5)) * (180F / (float) Math.PI);
                        EntityHippogryph.this.yBodyRot = EntityHippogryph.this.yRot;
                    }
                }
            }
        }
    }

    class AIFlyRandom extends Goal {
        BlockPos target;

        public AIFlyRandom() {
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        public boolean canUse() {
            return EntityHippogryph.this.isFlying() && !EntityHippogryph.this.isVehicle();
        }

        public boolean canContinueToUse() {
            return canUse();
        }

        public void tick() {
            if (target == null || !isDirectPathBetweenPoints(EntityHippogryph.this.position(), Vector3d.atCenterOf(target)) || EntityHippogryph.this.getDistanceSquared(Vector3d.atCenterOf(target)) < 9) {
                if (EntityHippogryph.this.getTarget() != null) {
                    target = EntityHippogryph.this.getTarget().blockPosition();
                } else {
                    target = DragonUtils.getBlockInViewHippogryph(EntityHippogryph.this, 0);
                    if (EntityHippogryph.this.doesWantToLand()) {
                        while (target != null && target.getY() > 3 && EntityHippogryph.this.level.isEmptyBlock(target)) {
                            target = target.below();
                        }
                    }
                }
            }

            if (target != null && (EntityHippogryph.this.doesWantToLand() || EntityHippogryph.this.level.isEmptyBlock(target))) {
                EntityHippogryph.this.getMoveControl().setWantedPosition((double) target.getX() + 0.5D, (double) target.getY() + 0.5D, (double) target.getZ() + 0.5D, 0.75D);
                if (EntityHippogryph.this.getTarget() == null) {
                    EntityHippogryph.this.getLookControl().setLookAt((double) target.getX() + 0.5D, (double) target.getY() + 0.5D, (double) target.getZ() + 0.5D, 180.0F, 20.0F);
                }
            }
        }
    }

}
