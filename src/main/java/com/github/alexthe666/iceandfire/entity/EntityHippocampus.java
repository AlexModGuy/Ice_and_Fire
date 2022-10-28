package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.ai.*;
import com.github.alexthe666.iceandfire.entity.util.*;
import com.github.alexthe666.iceandfire.inventory.ContainerHippocampus;
import com.github.alexthe666.iceandfire.message.MessageHippogryphArmor;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import com.github.alexthe666.iceandfire.pathfinding.PathNavigateAmphibious;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
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
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nullable;

public class EntityHippocampus extends TamableAnimal implements ISyncMount, IAnimatedEntity, IDropArmor, IHasCustomizableAttributes, ICustomMoveController {

    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(EntityHippocampus.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> SADDLE = SynchedEntityData.defineId(EntityHippocampus.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> ARMOR = SynchedEntityData.defineId(EntityHippocampus.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> CHESTED = SynchedEntityData.defineId(EntityHippocampus.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Byte> CONTROL_STATE = SynchedEntityData.defineId(EntityHippocampus.class, EntityDataSerializers.BYTE);
    public static Animation ANIMATION_SPEAK;
    public float onLandProgress;

    public ChainBuffer tail_buffer;

    public SimpleContainer hippocampusInventory;
    public float sitProgress;
    public int airBorneCounter;
    private int animationTick;
    private Animation currentAnimation;
    private boolean isLandNavigator;
    private boolean isSitting;
    private boolean hasChestVarChanged = false;

    public EntityHippocampus(EntityType<EntityHippocampus> t, Level worldIn) {
        super(t, worldIn);
        IHasCustomizableAttributes.applyAttributesForEntity(t, this);
        this.maxUpStep = 1;
        ANIMATION_SPEAK = Animation.create(15);
        this.switchNavigator(true);
        if (worldIn.isClientSide) {
            tail_buffer = new ChainBuffer();
        }
        initHippocampusInv();
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new HippocampusAIRide(this));
        this.goalSelector.addGoal(0, new AquaticAITempt(this, 1.0D, () -> Items.KELP, false));
        this.goalSelector.addGoal(0, new AquaticAITempt(this, 1.0D, () -> Items.PRISMARINE_CRYSTALS, false));
        this.goalSelector.addGoal(1, new AquaticAIFindWaterTarget(this, 10, true));
        this.goalSelector.addGoal(2, new AquaticAIGetInWater(this, 1.0D));
        this.goalSelector.addGoal(3, new HippocampusAIWander(this, 1));
        this.goalSelector.addGoal(4, new BreedGoal(this, 1.0D));

    }

    @Override
    protected int getExperienceReward(Player player) {
        return 2;
    }

    @Override
    public float getWalkTargetValue(BlockPos pos) {
        return this.level.getBlockState(pos.below()).getMaterial() == Material.WATER ? 10.0F : this.level.getMaxLocalRawBrightness(pos) - 0.5F;
    }

    @Override
    public MobType getMobType() {
        return MobType.WATER;
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }


    private void switchNavigator(boolean onLand) {
        if (onLand) {
            this.moveControl = new MoveControl(this);
            this.navigation = new PathNavigateAmphibious(this, level);
            this.isLandNavigator = true;
        } else {
            this.moveControl = new EntityHippocampus.SwimmingMoveHelper();
            this.navigation = new WaterBoundPathNavigation(this, level);
            this.isLandNavigator = false;
        }
    }

    @Override
    public boolean isAlliedTo(Entity entityIn) {
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
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(VARIANT, Integer.valueOf(0));
        this.entityData.define(ARMOR, Integer.valueOf(0));
        this.entityData.define(SADDLE, Boolean.valueOf(false));
        this.entityData.define(CHESTED, Boolean.valueOf(false));
        this.entityData.define(CONTROL_STATE, Byte.valueOf((byte) 0));
    }

    private void initHippocampusInv() {
        SimpleContainer animalchest = this.hippocampusInventory;
        this.hippocampusInventory = new SimpleContainer(18);
        if (animalchest != null) {
            int i = Math.min(animalchest.getContainerSize(), this.hippocampusInventory.getContainerSize());
            for (int j = 0; j < i; ++j) {
                ItemStack itemstack = animalchest.getItem(j);
                if (!itemstack.isEmpty()) {
                    this.hippocampusInventory.setItem(j, itemstack.copy());
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

    @Override
    @Nullable
    public Entity getControllingPassenger() {
        for (Entity passenger : this.getPassengers()) {
            if (passenger instanceof Player && this.getTarget() != passenger) {
                Player player = (Player) passenger;
                return player;
            }
        }
        return null;
    }

    public static int getIntFromArmor(ItemStack stack) {
        if (!stack.isEmpty() && stack.getItem() != null && stack.getItem() == Items.IRON_HORSE_ARMOR) {
            return 1;
        }
        if (!stack.isEmpty() && stack.getItem() != null && stack.getItem() == Items.GOLDEN_HORSE_ARMOR) {
            return 2;
        }
        if (!stack.isEmpty() && stack.getItem() != null && stack.getItem() == Items.DIAMOND_HORSE_ARMOR) {
            return 3;
        }
        return 0;
    }

    @Override
    public boolean equipItemIfPossible(@Nullable ItemStack itemStackIn) {
        EquipmentSlot equipmentSlot = getEquipmentSlotForItem(itemStackIn);
        int j = equipmentSlot.getIndex() - 500 + 2;
        if (j >= 0 && j < this.hippocampusInventory.getContainerSize()) {
            this.hippocampusInventory.setItem(j, itemStackIn);
            return true;
        } else {
            return false;
        }
    }


    @Override
    public void die(DamageSource cause) {
        super.die(cause);
        if (hippocampusInventory != null && !this.level.isClientSide) {
            for (int i = 0; i < hippocampusInventory.getContainerSize(); ++i) {
                ItemStack itemstack = hippocampusInventory.getItem(i);
                if (!itemstack.isEmpty()) {
                    this.spawnAtLocation(itemstack, 0.0F);
                }
            }
        }
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

    public static AttributeSupplier.Builder bakeAttributes() {
        return Mob.createMobAttributes()
            //HEALTH
            .add(Attributes.MAX_HEALTH, 40.0D)
            //SPEED
            .add(Attributes.MOVEMENT_SPEED, 0.3D)
            //ATTACK
            .add(Attributes.ATTACK_DAMAGE, 1.0D);
    }

    @Override
    public AttributeSupplier.Builder getConfigurableAttributes() {
        return bakeAttributes();
    }

    @Override
    public boolean canBeRiddenInWater(Entity rider) {
        return true;
    }

    @Override
    public void positionRider(Entity passenger) {
        super.positionRider(passenger);
        if (this.hasPassenger(passenger)) {
            yBodyRot = getYRot();
            this.setYRot(passenger.getYRot());
        }
        double ymod1 = this.onLandProgress * -0.02;
        passenger.setPos(this.getX(), this.getY() + 0.6F + ymod1, this.getZ());
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level.isClientSide) {
            if (this.random.nextInt(900) == 0 && this.deathTime == 0) {
                this.heal(1.0F);
            }
        }
        AnimationHandler.INSTANCE.updateAnimations(this);
        if (getControllingPassenger() != null && getControllingPassenger() instanceof LivingEntity && this.tickCount % 20 == 0) {
            ((LivingEntity) getControllingPassenger()).addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, 30, 0, true, false));
        }
        if (!this.onGround) {
            airBorneCounter++;
        } else {
            airBorneCounter = 0;
        }
        if (level.isClientSide) {
            tail_buffer.calculateChainSwingBuffer(40, 10, 1F, this);
        }
        if (this.up()) {
            if (!this.isInWater() && this.airBorneCounter == 0 && this.onGround) {
                this.jumpFromGround();
            } else if (this.isInWater()) {
                this.setDeltaMovement(this.getDeltaMovement().add(0, 0.4D, 0));
            }
        }
        if (this.down()) {
            this.setDeltaMovement(this.getDeltaMovement().add(0, -0.4D, 0));
        }
        if (this.isInWater() && this.isLandNavigator) {
            switchNavigator(false);
        }
        if (!this.isInWater() && !this.isLandNavigator) {
            switchNavigator(true);
        }
        boolean inWater = !this.isInWater();
        if (inWater && onLandProgress < 20.0F) {
            onLandProgress += 1F;
        } else if (!inWater && onLandProgress > 0.0F) {
            onLandProgress -= 1F;
        }
        boolean sitting = isOrderedToSit();
        if (sitting && sitProgress < 20.0F) {
            sitProgress += 0.5F;
        } else if (!sitting && sitProgress > 0.0F) {
            sitProgress -= 0.5F;
        }
        if (hasChestVarChanged && hippocampusInventory != null && !this.isChested()) {
            for (int i = 3; i < 18; i++) {
                if (!hippocampusInventory.getItem(i).isEmpty()) {
                    if (!level.isClientSide) {
                        this.spawnAtLocation(hippocampusInventory.getItem(i), 1);
                    }
                    hippocampusInventory.removeItemNoUpdate(i);
                }
            }
            hasChestVarChanged = false;
        }
    }

    public boolean up() {
        return (Byte.valueOf(entityData.get(CONTROL_STATE).byteValue()) & 1) == 1;
    }

    public boolean down() {
        return (Byte.valueOf(entityData.get(CONTROL_STATE).byteValue()) >> 1 & 1) == 1;
    }

    public boolean dismountIAF() {
        return (Byte.valueOf(entityData.get(CONTROL_STATE).byteValue()) >> 2 & 1) == 1;
    }


    public boolean isBlinking() {
        return this.tickCount % 50 > 43;
    }

    public boolean getCanSpawnHere() {
        return this.getY() > 30 && this.getY() < this.level.getSeaLevel();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Variant", this.getVariant());
        compound.putBoolean("Chested", this.isChested());
        compound.putBoolean("Saddled", this.isSaddled());
        compound.putInt("Armor", this.getArmor());
        if (hippocampusInventory != null) {
            ListTag nbttaglist = new ListTag();
            for (int i = 0; i < this.hippocampusInventory.getContainerSize(); ++i) {
                ItemStack itemstack = this.hippocampusInventory.getItem(i);
                if (!itemstack.isEmpty()) {
                    CompoundTag CompoundNBT = new CompoundTag();
                    CompoundNBT.putByte("Slot", (byte) i);
                    itemstack.save(CompoundNBT);
                    nbttaglist.add(CompoundNBT);
                }
            }
            compound.put("Items", nbttaglist);
        }

    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setVariant(compound.getInt("Variant"));
        this.setChested(compound.getBoolean("Chested"));
        this.setSaddled(compound.getBoolean("Saddled"));
        this.setArmor(compound.getInt("Armor"));
        if (hippocampusInventory != null) {
            ListTag nbttaglist = compound.getList("Items", 10);
            this.initHippocampusInv();
            for (int i = 0; i < nbttaglist.size(); ++i) {
                CompoundTag CompoundNBT = nbttaglist.getCompound(i);
                int j = CompoundNBT.getByte("Slot") & 255;
                this.hippocampusInventory.setItem(j, ItemStack.of(CompoundNBT));
            }
        } else {
            ListTag nbttaglist = compound.getList("Items", 10);
            this.initHippocampusInv();
            for (int i = 0; i < nbttaglist.size(); ++i) {
                CompoundTag CompoundNBT = nbttaglist.getCompound(i);
                int j = CompoundNBT.getByte("Slot") & 255;
                this.initHippocampusInv();
                this.hippocampusInventory.setItem(j, ItemStack.of(CompoundNBT));
                //this.setArmorInSlot(j, this.getIntFromArmor(ItemStack.loadItemStackFromNBT(CompoundNBT)));
                ItemStack saddle = hippocampusInventory.getItem(0);
                ItemStack chest = hippocampusInventory.getItem(1);
                if (level.isClientSide) {
                    IceAndFire.NETWORK_WRAPPER.sendToServer(new MessageHippogryphArmor(this.getId(), 0, saddle != null && saddle.getItem() == Items.SADDLE && !saddle.isEmpty() ? 1 : 0));
                    IceAndFire.NETWORK_WRAPPER.sendToServer(new MessageHippogryphArmor(this.getId(), 1, chest != null && chest.getItem() == Blocks.CHEST.asItem() && !chest.isEmpty() ? 1 : 0));
                    IceAndFire.NETWORK_WRAPPER.sendToServer(new MessageHippogryphArmor(this.getId(), 2, getIntFromArmor(hippocampusInventory.getItem(2))));
                }
            }
        }
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
        if (level.isClientSide) {
            boolean isSitting = (this.entityData.get(DATA_FLAGS_ID).byteValue() & 1) != 0;
            this.isSitting = isSitting;
            return isSitting;
        }
        return isSitting;
    }

    @Override
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

    public int getArmor() {
        return this.entityData.get(ARMOR).intValue();
    }

    public void setArmor(int armorType) {
        this.entityData.set(ARMOR, armorType);
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

    public int getVariant() {
        return this.entityData.get(VARIANT).intValue();
    }

    public void setVariant(int variant) {
        this.entityData.set(VARIANT, variant);
    }

    @Override
    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        SpawnGroupData data = super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
        this.setVariant(this.getRandom().nextInt(6));
        return data;
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
    public Animation[] getAnimations() {
        return new Animation[]{IAnimatedEntity.NO_ANIMATION, ANIMATION_SPEAK};
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverWorld, AgeableMob ageable) {
        if (ageable instanceof EntityHippocampus) {
            EntityHippocampus hippo = new EntityHippocampus(IafEntityRegistry.HIPPOCAMPUS.get(), this.level);
            hippo.setVariant(this.getRandom().nextBoolean() ? this.getVariant() : ((EntityHippocampus) ageable).getVariant());
            return hippo;
        }
        return null;
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    public boolean isInWater() {
        return super.isInWater();
    }

    @Override
    public void travel(Vec3 vec) {
        float f4;
        if (this.isOrderedToSit()) {
            super.travel(Vec3.ZERO);
            return;
        }
        if (this.isEffectiveAi()) {
            float f5;
            if (this.isInWater()) {
                this.moveRelative(0.1F, vec);
                f4 = 0.6F;
                float d0 = EnchantmentHelper.getDepthStrider(this);
                if (d0 > 3.0F) {
                    d0 = 3.0F;
                }
                if (!this.onGround) {
                    d0 *= 0.5F;
                }
                if (d0 > 0.0F) {
                    f4 += (0.54600006F - f4) * d0 / 3.0F;
                }
                this.move(MoverType.SELF, this.getDeltaMovement());
                this.setDeltaMovement(this.getDeltaMovement().multiply(f4 * 0.9D, f4 * 0.9D, f4 * 0.9D));
            } else {
                super.travel(vec);
            }
        }
        this.animationSpeedOld = this.animationSpeed;
        double deltaX = this.getX() - this.xo;
        double deltaZ = this.getZ() - this.zo;
        double deltaY = this.getY() - this.yo;
        float delta = Mth.sqrt((float) (deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ)) * 4.0F;
        if (delta > 1.0F) {
            delta = 1.0F;
        }
        this.animationSpeed += (delta - this.animationSpeed) * 0.4F;
        this.animationPosition += this.animationSpeed;

    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.getItem() == Items.PRISMARINE_CRYSTALS;
    }

    @Override
    public void playAmbientSound() {
        if (this.getAnimation() == this.NO_ANIMATION) {
            this.setAnimation(ANIMATION_SPEAK);
        }
        super.playAmbientSound();
    }

    @Override
    protected void playHurtSound(DamageSource source) {
        if (this.getAnimation() == this.NO_ANIMATION) {
            this.setAnimation(ANIMATION_SPEAK);
        }
        super.playHurtSound(source);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (itemstack != null && itemstack.getItem() == Items.PRISMARINE_CRYSTALS && this.getAge() == 0 && !isInLove()) {
            this.setOrderedToSit(false);
            this.setInLove(player);
            this.playSound(SoundEvents.GENERIC_EAT, 1, 1);
            if (!player.isCreative()) {
                itemstack.shrink(1);
            }
            return InteractionResult.SUCCESS;
        }
        if (itemstack != null && itemstack.getItem() == Items.KELP) {
            if (!level.isClientSide) {
                this.heal(5);
                this.playSound(SoundEvents.GENERIC_EAT, 1, 1);
                for (int i = 0; i < 3; i++) {
                    this.level.addParticle(new ItemParticleOption(ParticleTypes.ITEM, itemstack), this.getX() + this.random.nextFloat() * this.getBbWidth() * 2.0F - this.getBbWidth(), this.getY() + this.random.nextFloat() * this.getBbHeight(), this.getZ() + this.random.nextFloat() * this.getBbWidth() * 2.0F - this.getBbWidth(), 0, 0, 0);
                }
                if (!player.isCreative()) {
                    itemstack.shrink(1);
                }
            }
            if (!this.isTame() && this.getRandom().nextInt(3) == 0) {
                this.tame(player);
                for (int i = 0; i < 6; i++) {
                    this.level.addParticle(ParticleTypes.HEART, this.getX() + this.random.nextFloat() * this.getBbWidth() * 2.0F - this.getBbWidth(), this.getY() + this.random.nextFloat() * this.getBbHeight(), this.getZ() + this.random.nextFloat() * this.getBbWidth() * 2.0F - this.getBbWidth(), 0, 0, 0);
                }
            }
            return InteractionResult.SUCCESS;

        }
        if (isOwnedBy(player) && itemstack != null && itemstack.getItem() == Items.PRISMARINE_CRYSTALS && this.getAge() == 0 && !isInLove()) {
            this.setOrderedToSit(false);
            this.setInLove(player);
            this.playSound(SoundEvents.GENERIC_EAT, 1, 1);
            if (!player.isCreative()) {
                itemstack.shrink(1);
            }
            return InteractionResult.SUCCESS;
        }
        if (isOwnedBy(player) && itemstack != null && itemstack.getItem() == Items.STICK) {
            this.setOrderedToSit(!this.isOrderedToSit());
            return InteractionResult.SUCCESS;
        }
        if (isOwnedBy(player) && itemstack.isEmpty()) {
            if (player.isShiftKeyDown()) {
                this.openGUI(player);
                return InteractionResult.SUCCESS;
            } else if (this.isSaddled() && !this.isBaby() && !player.isPassenger()) {
                player.startRiding(this, true);
                return InteractionResult.SUCCESS;
            }
        }
        return super.mobInteract(player, hand);
    }

    public void openGUI(Player playerEntity) {

        if (!this.level.isClientSide && (!this.isVehicle() || this.hasPassenger(playerEntity))) {
            NetworkHooks.openGui((ServerPlayer) playerEntity, new MenuProvider() {
                @Override
                public AbstractContainerMenu createMenu(int p_createMenu_1_, Inventory p_createMenu_2_, Player p_createMenu_3_) {
                    return new ContainerHippocampus(p_createMenu_1_, hippocampusInventory, p_createMenu_2_, EntityHippocampus.this);
                }

                @Override
                public Component getDisplayName() {
                    return new TranslatableComponent("entity.iceandfire.hippocampus");
                }
            });
        }
        IceAndFire.PROXY.setReferencedMob(this);
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
    }

    @Override
    public void strike(boolean strike) {

    }

    @Override
    public void dismount(boolean dismount) {
        setStateField(2, dismount);
    }

    public void refreshInventory() {
        //This isn't needed (anymore) since it's already being handled by minecraft
        if (!this.level.isClientSide) {
            ItemStack saddle = this.hippocampusInventory.getItem(0);
            ItemStack chest = this.hippocampusInventory.getItem(1);
            this.setSaddled(saddle != null && saddle.getItem() == Items.SADDLE && !saddle.isEmpty());
            this.setChested(chest != null && chest.getItem() == Blocks.CHEST.asItem() && !chest.isEmpty());
            this.setArmor(getIntFromArmor(this.hippocampusInventory.getItem(2)));
        }
    }

    @Override
    @Nullable
    protected SoundEvent getAmbientSound() {
        return IafSoundRegistry.HIPPOCAMPUS_IDLE;
    }

    @Override
    @Nullable
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return IafSoundRegistry.HIPPOCAMPUS_HURT;
    }

    @Override
    @Nullable
    protected SoundEvent getDeathSound() {
        return IafSoundRegistry.HIPPOCAMPUS_DIE;
    }

    @Override
    public void dropArmor() {
        if (hippocampusInventory != null && !this.level.isClientSide) {
            for (int i = 0; i < hippocampusInventory.getContainerSize(); ++i) {
                ItemStack itemstack = hippocampusInventory.getItem(i);
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


    @Override
    public boolean isControlledByLocalInstance() {
        return false;
    }

    @Override
    public boolean canBeControlledByRider() {
        return true;
    }

    public boolean isRidingPlayer(Player player) {
        return getRidingPlayer() != null && player != null && getRidingPlayer().getUUID().equals(player.getUUID());
    }

    @Nullable
    public Player getRidingPlayer() {
        if (this.getControllingPassenger() instanceof Player) {
            return (Player) this.getControllingPassenger();
        }
        return null;
    }

    public double getRideSpeedModifier() {
        return this.isInWater() ? 0.7F * IafConfig.hippocampusSwimSpeedMod : 0.55F;
    }

    class SwimmingMoveHelper extends MoveControl {
        private final EntityHippocampus hippo = EntityHippocampus.this;

        public SwimmingMoveHelper() {
            super(EntityHippocampus.this);
        }

        @Override
        public void tick() {
            if (this.hippo.isVehicle()) {
                double flySpeed = 0.8F * hippo.getRideSpeedModifier();
                Vec3 dragonVec = hippo.position();
                Vec3 moveVec = new Vec3(wantedX, wantedY, wantedZ);
                Vec3 normalized = moveVec.subtract(dragonVec).normalize();
                double dist = dragonVec.distanceTo(moveVec);
                hippo.setDeltaMovement(normalized.x * flySpeed, normalized.y * flySpeed, normalized.z * flySpeed);
                if (dist > 2.5E-7) {
                    float yaw = (float) Math.toDegrees(Math.PI * 2 - Math.atan2(normalized.x, normalized.y));
                    hippo.setYRot(rotlerp(hippo.getYRot(), yaw, 5));
                    hippo.setSpeed((float) (speedModifier));
                }
                hippo.move(MoverType.SELF, hippo.getDeltaMovement());
            } else if (this.operation == MoveControl.Operation.MOVE_TO && !this.hippo.getNavigation().isDone()) {
                double distanceX = this.wantedX - this.hippo.getX();
                double distanceY = this.wantedY - this.hippo.getY();
                double distanceZ = this.wantedZ - this.hippo.getZ();
                double distance = Math.abs(distanceX * distanceX + distanceZ * distanceZ);
                double distanceWithY = Math.sqrt(distanceX * distanceX + distanceY * distanceY + distanceZ * distanceZ);
                distanceY = distanceY / distanceWithY;
                float angle = (float) (Math.atan2(distanceZ, distanceX) * 180.0D / Math.PI) - 90.0F;
                this.hippo.setYRot(this.rotlerp(this.hippo.getYRot(), angle, 30.0F));
                this.hippo.setSpeed(1F);
                float f1 = 0;
                float f2 = 0;
                if (distance < Math.max(1.0F, this.hippo.getBbWidth())) {
                    float f = this.hippo.getYRot() * 0.017453292F;
                    f1 -= (double) (Mth.sin(f) * 0.35F);
                    f2 += (double) (Mth.cos(f) * 0.35F);
                }
                this.hippo.setDeltaMovement(this.hippo.getDeltaMovement().add(f1, this.hippo.getSpeed() * distanceY * 0.1D, f2));
            } else if (this.operation == MoveControl.Operation.JUMPING) {
                this.hippo.setSpeed((float) (this.speedModifier * this.hippo.getAttribute(Attributes.MOVEMENT_SPEED).getValue()));
                if (this.hippo.onGround) {
                    this.operation = MoveControl.Operation.WAIT;
                }
            } else {
                this.hippo.setSpeed(0.0F);
            }
        }
    }
}
