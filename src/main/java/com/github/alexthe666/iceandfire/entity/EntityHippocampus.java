package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.ai.*;
import com.github.alexthe666.iceandfire.entity.util.ChainBuffer;
import com.github.alexthe666.iceandfire.entity.util.ICustomMoveController;
import com.github.alexthe666.iceandfire.entity.util.IHasCustomizableAttributes;
import com.github.alexthe666.iceandfire.entity.util.ISyncMount;
import com.github.alexthe666.iceandfire.inventory.HippocampusContainerMenu;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import com.github.alexthe666.iceandfire.pathfinding.PathNavigateAmphibious;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.minecraft.sounds.SoundSource;
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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.function.Predicate;

public class EntityHippocampus extends TamableAnimal implements ISyncMount, IAnimatedEntity, IHasCustomizableAttributes, ICustomMoveController, ContainerListener, Saddleable {

    public static final int INV_SLOT_SADDLE = 0;
    public static final int INV_SLOT_CHEST = 1;
    public static final int INV_SLOT_ARMOR = 2;
    public static final int INV_BASE_COUNT = 3;
    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(EntityHippocampus.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> SADDLE = SynchedEntityData.defineId(EntityHippocampus.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> ARMOR = SynchedEntityData.defineId(EntityHippocampus.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> CHESTED = SynchedEntityData.defineId(EntityHippocampus.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Byte> CONTROL_STATE = SynchedEntityData.defineId(EntityHippocampus.class, EntityDataSerializers.BYTE);
    // These are from TamableAnimal
    private static final int FLAG_SITTING = 1;
    private static final int FLAG_TAME = 4;
    private static final Component CONTAINER_TITLE = new TranslatableComponent("entity.iceandfire.hippocampus");

    public static Animation ANIMATION_SPEAK;
    public float onLandProgress;

    public ChainBuffer tail_buffer;

    public SimpleContainer inventory;
    public float sitProgress;
    public int airBorneCounter;
    private int animationTick;
    private Animation currentAnimation;
    private boolean isLandNavigator;
    private LazyOptional<?> itemHandler = null;

    public EntityHippocampus(EntityType<EntityHippocampus> t, Level worldIn) {
        super(t, worldIn);
        IHasCustomizableAttributes.applyAttributesForEntity(t, this);
        this.maxUpStep = 1;
        ANIMATION_SPEAK = Animation.create(15);
        this.switchNavigator(true);
        if (worldIn.isClientSide) {
            tail_buffer = new ChainBuffer();
        }
        this.createInventory();
    }

    public static int getIntFromArmor(ItemStack stack) {
        if (!stack.isEmpty() && stack.getItem() == Items.IRON_HORSE_ARMOR) {
            return 1;
        }
        if (!stack.isEmpty() && stack.getItem() == Items.GOLDEN_HORSE_ARMOR) {
            return 2;
        }
        if (!stack.isEmpty() && stack.getItem() == Items.DIAMOND_HORSE_ARMOR) {
            return 3;
        }
        return 0;
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
    protected void registerGoals() {
//        this.goalSelector.addGoal(0, new HippocampusAIRide(this));
        this.goalSelector.addGoal(0, new AquaticAITempt(this, 1.0D, () -> Items.KELP, false));
        this.goalSelector.addGoal(0, new AquaticAITempt(this, 1.0D, () -> Items.PRISMARINE_CRYSTALS, false));
        this.goalSelector.addGoal(1, new AquaticAIFindWaterTarget(this, 10, true));
        this.goalSelector.addGoal(2, new AquaticAIGetInWater(this, 1.0D));
        this.goalSelector.addGoal(3, new HippocampusAIWander(this, 1));
        this.goalSelector.addGoal(4, new BreedGoal(this, 1.0D));
    }

    @Override
    protected int getExperienceReward(@NotNull Player player) {
        return 2;
    }

    @Override
    public float getWalkTargetValue(BlockPos pos) {
        return this.level.getBlockState(pos.below()).getMaterial() == Material.WATER ? 10.0F : this.level.getMaxLocalRawBrightness(pos) - 0.5F;
    }

    @Override
    public @NotNull MobType getMobType() {
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
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(VARIANT, 0);
        this.entityData.define(ARMOR, 0);
        this.entityData.define(SADDLE, Boolean.FALSE);
        this.entityData.define(CHESTED, Boolean.FALSE);
        this.entityData.define(CONTROL_STATE, (byte) 0);
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

    @Override
    public boolean equipItemIfPossible(@Nullable ItemStack itemStackIn) {
        EquipmentSlot equipmentSlot = getEquipmentSlotForItem(itemStackIn);
        int j = equipmentSlot.getIndex() - 500 + 2;
        if (j >= 0 && j < this.inventory.getContainerSize()) {
            this.inventory.setItem(j, itemStackIn);
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void dropEquipment() {
        super.dropEquipment();
        if (inventory != null && !this.level.isClientSide) {
            for (int i = 0; i < this.inventory.getContainerSize(); ++i) {
                ItemStack itemstack = this.inventory.getItem(i);
                if (!itemstack.isEmpty() && !EnchantmentHelper.hasVanishingCurse(itemstack)) {
                    this.spawnAtLocation(itemstack);
                }
            }
        }
        if (this.isChested()) {
            if (!this.level.isClientSide) {
                this.spawnAtLocation(Blocks.CHEST);
            }
            this.setChested(false);
        }
    }

    protected void dropChestItems() {
        for (int i = 3; i < 18; i++) {
            if (!inventory.getItem(i).isEmpty()) {
                if (!level.isClientSide) {
                    this.spawnAtLocation(inventory.getItem(i), 1);
                }
                inventory.removeItemNoUpdate(i);
            }
        }
    }

    private void updateControlState(int i, boolean newState) {
        byte prevState = entityData.get(CONTROL_STATE);
        if (newState) {
            entityData.set(CONTROL_STATE, (byte) (prevState | (1 << i)));
        } else {
            entityData.set(CONTROL_STATE, (byte) (prevState & ~(1 << i)));
        }
    }

    @Override
    public byte getControlState() {
        return entityData.get(CONTROL_STATE);
    }

    @Override
    public void setControlState(byte state) {
        entityData.set(CONTROL_STATE, state);
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
    public void positionRider(@NotNull Entity passenger) {
        super.positionRider(passenger);
        if (this.hasPassenger(passenger)) {
            yBodyRot = getYRot();
            this.setYBodyRot(passenger.getYRot());
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
        if (getControllingPassenger() instanceof LivingEntity && this.tickCount % 20 == 0) {
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
        if (this.isGoingUp()) {
            if (!this.isInWater() && this.airBorneCounter == 0 && this.onGround && this.getControllingPassenger() == null) {
                this.jumpFromGround();
            } else if (this.isInWater()) {
//                this.setDeltaMovement(this.getDeltaMovement().add(0, 0.4D, 0));
            }
        }
//        if (this.isGoingDown()) {
//            this.setDeltaMovement(this.getDeltaMovement().add(0, -0.4D, 0));
//        }
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
    }

    public boolean isGoingUp() {
        return (entityData.get(CONTROL_STATE) & 1) == 1;
    }

    public boolean isGoingDown() {
        return (entityData.get(CONTROL_STATE) >> 1 & 1) == 1;
    }

    public boolean isBlinking() {
        return this.tickCount % 50 > 43;
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Variant", this.getVariant());
        compound.putBoolean("Chested", this.isChested());
        compound.putBoolean("Saddled", this.isSaddled());
        compound.putInt("Armor", this.getArmor());
        ListTag nbttaglist = new ListTag();
        for (int i = 0; i < this.inventory.getContainerSize(); ++i) {
            ItemStack itemstack = this.inventory.getItem(i);
            if (!itemstack.isEmpty()) {
                CompoundTag CompoundNBT = new CompoundTag();
                CompoundNBT.putByte("Slot", (byte) i);
                itemstack.save(CompoundNBT);
                nbttaglist.add(CompoundNBT);
            }
        }
        compound.put("Items", nbttaglist);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setVariant(compound.getInt("Variant"));
        this.setChested(compound.getBoolean("Chested"));
        this.setSaddled(compound.getBoolean("Saddled"));
        this.setArmor(compound.getInt("Armor"));
        if (inventory != null) {
            ListTag nbttaglist = compound.getList("Items", 10);
            this.createInventory();
            for (int i = 0; i < nbttaglist.size(); ++i) {
                CompoundTag CompoundNBT = nbttaglist.getCompound(i);
                int j = CompoundNBT.getByte("Slot") & 255;
                this.inventory.setItem(j, ItemStack.of(CompoundNBT));
            }
        }
    }

    protected int getInventorySize() {
        return this.isChested() ? 18 : 3;
    }

    private SlotAccess createEquipmentSlotAccess(final int pSlot, final Predicate<ItemStack> pStackFilter) {
        return new SlotAccess() {
            @Override
            public ItemStack get() {
                return EntityHippocampus.this.inventory.getItem(pSlot);
            }

            @Override
            public boolean set(ItemStack p_149528_) {
                if (!pStackFilter.test(p_149528_)) {
                    return false;
                } else {
                    EntityHippocampus.this.inventory.setItem(pSlot, p_149528_);
                    EntityHippocampus.this.updateContainerEquipment();
                    return true;
                }
            }
        };
    }

    protected void createInventory() {
        SimpleContainer simplecontainer = this.inventory;
        this.inventory = new SimpleContainer(this.getInventorySize());
        if (simplecontainer != null) {
            simplecontainer.removeListener(this);
            int i = Math.min(simplecontainer.getContainerSize(), this.inventory.getContainerSize());

            for (int j = 0; j < i; ++j) {
                ItemStack itemstack = simplecontainer.getItem(j);
                if (!itemstack.isEmpty()) {
                    this.inventory.setItem(j, itemstack.copy());
                }
            }
        }

        this.inventory.addListener(this);
        this.updateContainerEquipment();
        this.itemHandler = LazyOptional.of(() -> new InvWrapper(this.inventory));
    }

    protected void updateContainerEquipment() {
        if (!this.level.isClientSide) {
            this.setSaddled(!this.inventory.getItem(INV_SLOT_SADDLE).isEmpty());
            this.setChested(!this.inventory.getItem(INV_SLOT_CHEST).isEmpty());
            this.setArmor(getIntFromArmor(this.inventory.getItem(INV_SLOT_ARMOR)));
        }
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
        if (this.isAlive() && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && itemHandler != null)
            return itemHandler.cast();
        return super.getCapability(capability, facing);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        if (itemHandler != null) {
            LazyOptional<?> oldHandler = itemHandler;
            itemHandler = null;
            oldHandler.invalidate();
        }
    }

    public boolean hasInventoryChanged(Container pInventory) {
        return this.inventory != pInventory;
    }

    @Override
    public boolean isSaddleable() {
        return false;
    }

    @Override
    public void equipSaddle(@Nullable SoundSource pSource) {

    }

    @Override
    public boolean isSaddled() {
        return this.entityData.get(SADDLE);
    }

    public void setSaddled(boolean saddle) {
        this.entityData.set(SADDLE, saddle);
    }

    public boolean isChested() {
        return this.entityData.get(CHESTED);
    }

    public void setChested(boolean chested) {
        this.entityData.set(CHESTED, chested);
        if (!chested)
            dropChestItems();
    }

    public int getArmor() {
        return this.entityData.get(ARMOR);
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
        return this.entityData.get(VARIANT);
    }

    public void setVariant(int variant) {
        this.entityData.set(VARIANT, variant);
    }

    @Override
    @Nullable
    public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor worldIn, @NotNull DifficultyInstance difficultyIn, @NotNull MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
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
    public AgeableMob getBreedOffspring(@NotNull ServerLevel serverWorld, @NotNull AgeableMob ageable) {
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

    public final boolean DISABLE_MOVEMENT_CHECK = true;

    @Override
    public void travel(@NotNull Vec3 pTravelVector) {
        float baseSpeed;
        if (this.isOrderedToSit()) {
            super.travel(Vec3.ZERO);
            return;
        }
        if (this.isAlive()) {
            // Handle riding movement
            if (this.isVehicle() && this.canBeControlledByRider() && this.isSaddled()
                    && this.getControllingPassenger() instanceof LivingEntity rider) {
                // Mouse controlled yaw
                this.setYRot(rider.getYRot());
                this.yRotO = this.getYRot();
                this.setXRot(rider.getXRot() * 0.5F);
                this.setRot(this.getYRot(), this.getXRot());
                this.yBodyRot = this.getYRot();
                this.yHeadRot = this.yBodyRot;

                float sideway = rider.xxa;
                float forward = rider.zza;
                float vertical = this.isGoingUp() ? 1.0F : this.isGoingDown() ? -1.0F : 0F;

                float speedFactor = 0.6f;
                float waterSpeedFactor = 1.0f;
                float landSpeedFactor = 1.0f;
                float speed = (float) this.getAttributeValue(Attributes.MOVEMENT_SPEED) * speedFactor;
                if (!this.isInWater()) {
                    vertical = (float) pTravelVector.y;
                    speed *= .3f;
                }

                Vec3 travelVector = new Vec3(
                        sideway,
                        vertical,
                        forward
                );
                if (this.isControlledByLocalInstance()) {
                    if (this.isInWater()) {
                        this.flyingSpeed = this.getSpeed() * 0.1f;
                        this.setSpeed(speed);

                        this.moveRelative(this.getSpeed(), travelVector);
                        this.move(MoverType.SELF, this.getDeltaMovement());

                        Vec3 currentMotion = this.getDeltaMovement();
                        if (this.horizontalCollision) {
                            currentMotion = new Vec3(currentMotion.x, 0.2D, currentMotion.z);
                        }
                        this.setDeltaMovement(currentMotion.scale(0.7D));

                        this.calculateEntityAnimation(this, false);


                        // Vanilla travel has a smaller friction factor for Y axis
                        // Add more friction in case moving too fast on Y axis
//                    if (this.isFlying() || this.isHovering()) {
//                        this.setDeltaMovement(this.getDeltaMovement().multiply(1.0f, 0.92f, 1.0f));
//                    }
                    } else {
                        this.flyingSpeed = this.getSpeed();
                        this.setSpeed(speed);
                        super.travel(new Vec3(sideway, vertical, forward));
                    }

                } else if (rider instanceof Player) {
                    this.setDeltaMovement(Vec3.ZERO);
                    // Disable server side vehicle movement check, in case of console log spam
                    // Happens when stepping up blocks
                    // Might because client & server's onGround flag is out of sync
                    // I can't get it fixed, so it's disabled
//                    this.noPhysics = DISABLE_MOVEMENT_CHECK;
                }
            } else {

        if (this.isEffectiveAi()) {
                    // Handle AI movement
            if (this.isInWater()) {
                        this.moveRelative(0.1F, pTravelVector);
                        baseSpeed = 0.6F;
                        // 深海探索者（不包括骑手）
                        float speedBonus = EnchantmentHelper.getDepthStrider(this);
                        if (speedBonus > 3.0F) {
                            speedBonus = 3.0F;
                }
                if (!this.onGround) {
                            speedBonus *= 0.5F;
                }
                        if (speedBonus > 0.0F) {
                            baseSpeed += (0.54600006F - baseSpeed) * speedBonus / 3.0F;
                }
                this.move(MoverType.SELF, this.getDeltaMovement());
                        // Friction
                        this.setDeltaMovement(this.getDeltaMovement().multiply(baseSpeed * 0.9D,
                                                                               baseSpeed * 0.9D,
                                                                               baseSpeed * 0.9D
                        ));
            } else {
                        super.travel(pTravelVector);
                    }
                }
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
    protected void playHurtSound(@NotNull DamageSource source) {
        if (this.getAnimation() == this.NO_ANIMATION) {
            this.setAnimation(ANIMATION_SPEAK);
        }
        super.playHurtSound(source);
    }

    @Override
    public @NotNull InteractionResult mobInteract(Player player, @NotNull InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        // Breed item
        if (itemstack.getItem() == Items.PRISMARINE_CRYSTALS && this.getAge() == 0 && !isInLove()) {
            this.setOrderedToSit(false);
            this.setInLove(player);
            this.playSound(SoundEvents.GENERIC_EAT, 1, 1);
            if (!player.isCreative()) {
                itemstack.shrink(1);
            }
            return InteractionResult.SUCCESS;
        }
        // Food item
        if (itemstack.getItem() == Items.KELP) {
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
        // Owner
        if (isOwnedBy(player) && itemstack.getItem() == Items.STICK) {
            this.setOrderedToSit(!this.isOrderedToSit());
            return InteractionResult.SUCCESS;
        }
        // Inventory
        if (isOwnedBy(player) && itemstack.isEmpty() && player.isShiftKeyDown()) {
            this.openInventory(player);
            return InteractionResult.sidedSuccess(this.level.isClientSide);
        }
        // Riding
        if (isOwnedBy(player) && this.isSaddled() && !this.isBaby() && !player.isPassenger()) {
            doPlayerRide(player);
            return InteractionResult.SUCCESS;
        }
        return super.mobInteract(player, hand);
    }

    protected void doPlayerRide(Player pPlayer) {
        this.setOrderedToSit(false);
        if (!this.level.isClientSide) {
            pPlayer.setYRot(this.getYRot());
            pPlayer.setXRot(this.getXRot());
            pPlayer.startRiding(this);
        }
    }

    public void openInventory(Player player) {
        if (!this.level.isClientSide)
            NetworkHooks.openGui((ServerPlayer) player, getMenuProvider());
        IceAndFire.PROXY.setReferencedMob(this);
    }

    public MenuProvider getMenuProvider() {
        return new SimpleMenuProvider((containerId, playerInventory, player) -> new HippocampusContainerMenu(containerId, inventory, playerInventory, this), CONTAINER_TITLE);
    }

    @Override
    public void up(boolean up) {
        updateControlState(0, up);
    }

    @Override
    public void down(boolean down) {
        updateControlState(1, down);
    }

    @Override
    public void attack(boolean attack) {
    }

    @Override
    public void strike(boolean strike) {

    }

    @Override
    public void dismount(boolean dismount) {
        updateControlState(2, dismount);
    }

    @Override
    @Nullable
    protected SoundEvent getAmbientSound() {
        return IafSoundRegistry.HIPPOCAMPUS_IDLE;
    }

    @Override
    @Nullable
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSourceIn) {
        return IafSoundRegistry.HIPPOCAMPUS_HURT;
    }

    @Override
    @Nullable
    protected SoundEvent getDeathSound() {
        return IafSoundRegistry.HIPPOCAMPUS_DIE;
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
        return super.isControlledByLocalInstance();
    }

    @Override
    public boolean canBeControlledByRider() {
        return true;
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

    public int getInventoryColumns() {
        return 5;
    }

    @Override
    public void containerChanged(Container pInvBasic) {
        boolean flag = this.isSaddled();
        this.updateContainerEquipment();
        if (this.tickCount > 20 && !flag && this.isSaddled()) {
            this.playSound(SoundEvents.HORSE_SADDLE, 0.5F, 1.0F);
        }

    }

    class SwimmingMoveHelper extends MoveControl {
        private final EntityHippocampus hippo = EntityHippocampus.this;

        public SwimmingMoveHelper() {
            super(EntityHippocampus.this);
        }

        @Override
        public void tick() {
            if (this.hippo.isVehicle()) {
                double flySpeed = hippo.getRideSpeedModifier() * this.hippo.getAttributeValue(Attributes.MOVEMENT_SPEED);
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
