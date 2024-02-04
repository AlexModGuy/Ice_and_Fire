package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.client.model.IFChainBuffer;
import com.github.alexthe666.iceandfire.datagen.tags.IafItemTags;
import com.github.alexthe666.iceandfire.entity.ai.*;
import com.github.alexthe666.iceandfire.entity.props.EntityDataProvider;
import com.github.alexthe666.iceandfire.entity.util.*;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import com.github.alexthe666.iceandfire.pathfinding.PathNavigateFlyingCreature;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.ai.navigation.WallClimberNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class EntityAmphithere extends TamableAnimal implements ISyncMount, IAnimatedEntity, IPhasesThroughBlock, IFlapable, IDragonFlute, IFlyingMount, IHasCustomizableAttributes, ICustomMoveController {

    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(EntityAmphithere.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> FLYING = SynchedEntityData.defineId(EntityAmphithere.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> FLAP_TICKS = SynchedEntityData.defineId(EntityAmphithere.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Byte> CONTROL_STATE = SynchedEntityData.defineId(EntityAmphithere.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Integer> COMMAND = SynchedEntityData.defineId(EntityAmphithere.class, EntityDataSerializers.INT);
    public static Animation ANIMATION_BITE = Animation.create(15);
    public static Animation ANIMATION_BITE_RIDER = Animation.create(15);
    public static Animation ANIMATION_WING_BLAST = Animation.create(30);
    public static Animation ANIMATION_TAIL_WHIP = Animation.create(30);
    public static Animation ANIMATION_SPEAK = Animation.create(10);
    public float flapProgress;
    public float groundProgress = 0;
    public float sitProgress = 0;
    public float diveProgress = 0;

    public IFChainBuffer roll_buffer;
    public IFChainBuffer tail_buffer;
    public IFChainBuffer pitch_buffer;
    @Nullable
    public BlockPos orbitPos = null;
    public float orbitRadius = 0.0F;
    public boolean isFallen;
    public BlockPos homePos;
    public boolean hasHomePosition = false;
    protected FlightBehavior flightBehavior = FlightBehavior.WANDER;
    protected int ticksCircling = 0;
    private int animationTick;
    private Animation currentAnimation;
    private int flapTicks = 0;
    private int flightCooldown = 0;
    private int ticksFlying = 0;
    private boolean isFlying;
    private boolean changedFlightBehavior = false;
    private int ticksStill = 0;
    private int ridingTime = 0;
    private boolean isSitting;
    /*
          0 = ground/walking
          1 = ai flight
          2 = controlled flight
       */
    private int navigatorType = 0;

    public EntityAmphithere(EntityType<EntityAmphithere> type, Level worldIn) {
        super(type, worldIn);
        if (worldIn.isClientSide) {
            roll_buffer = new IFChainBuffer();
            pitch_buffer = new IFChainBuffer();
            tail_buffer = new IFChainBuffer();
        }
        this.setMaxUpStep(1F);
        switchNavigator(0);
    }

    public static BlockPos getPositionRelativetoGround(Entity entity, Level world, int x, int z, RandomSource rand) {
        BlockPos pos = new BlockPos(x, entity.getBlockY(), z);
        for (int yDown = 0; yDown < 6 + rand.nextInt(6); yDown++) {
            if (!world.isEmptyBlock(pos.below(yDown))) {
                return pos.above(yDown);
            }
        }
        return pos;
    }

    public static boolean canAmphithereSpawnOn(EntityType<EntityAmphithere> parrotIn, ServerLevelAccessor worldIn, MobSpawnType reason, BlockPos p_223317_3_, RandomSource random) {
        BlockState blockState = worldIn.getBlockState(p_223317_3_.below());
        Block block = blockState.getBlock();
        return (blockState.is(BlockTags.LEAVES)
                || block == Blocks.GRASS_BLOCK
                || blockState.is(BlockTags.LOGS)
                || block == Blocks.AIR);
    }

    @Override
    public boolean checkSpawnObstruction(LevelReader worldIn) {
        if (worldIn.isUnobstructed(this) && !worldIn.containsAnyLiquid(this.getBoundingBox())) {
            BlockPos blockpos = this.blockPosition();
            if (blockpos.getY() < worldIn.getSeaLevel()) {
                return false;
            }

            BlockState blockstate = worldIn.getBlockState(blockpos.below());
            return blockstate.is(Blocks.GRASS_BLOCK) || blockstate.is(BlockTags.LEAVES);
        }

        return false;
    }

    public static BlockPos getPositionInOrbit(EntityAmphithere entity, Level world, BlockPos orbit, RandomSource rand) {
        float possibleOrbitRadius = (entity.orbitRadius + 10.0F);
        float radius = 10;
        if (entity.getCommand() == 2) {
            if (entity.getOwner() != null) {
                orbit = entity.getOwner().blockPosition().above(7);
                radius = 5;
            }
        } else if (entity.hasHomePosition) {
            orbit = entity.homePos.above(30);
            radius = 30;
        }
        float angle = (0.01745329251F * possibleOrbitRadius);
        double extraX = radius * Mth.sin((float) (Math.PI + angle));
        double extraZ = radius * Mth.cos(angle);
        BlockPos radialPos = BlockPos.containing(orbit.getX() + extraX, orbit.getY(), orbit.getZ() + extraZ);
        entity.orbitRadius = possibleOrbitRadius;
        return radialPos;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGroundIn, @NotNull BlockState state, @NotNull BlockPos pos) {
    }

    @Override
    public float getWalkTargetValue(@NotNull BlockPos pos) {
        if (this.isFlying()) {
            if (level().isEmptyBlock(pos)) {
                return 10F;
            } else {
                return 0F;
            }
        } else {
            return super.getWalkTargetValue(pos);
        }
    }

    @Override
    public @NotNull InteractionResult mobInteract(Player player, @NotNull InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);

        if (itemstack != null && itemstack.is(IafItemTags.BREED_AMPITHERE)) {
            if (this.getAge() == 0 && !isInLove()) {
                this.setOrderedToSit(false);
                this.setInLove(player);
                this.playSound(SoundEvents.GENERIC_EAT, 1, 1);
                if (!player.isCreative()) {
                    itemstack.shrink(1);
                }
            }
            return InteractionResult.SUCCESS;
        }
        if (itemstack != null && itemstack.is(IafItemTags.HEAL_AMPITHERE) && this.getHealth() < this.getMaxHealth()) {
            this.heal(5);
            this.playSound(SoundEvents.GENERIC_EAT, 1, 1);
            if (!player.isCreative()) {
                itemstack.shrink(1);
            }
            return InteractionResult.SUCCESS;
        }
        if (super.mobInteract(player, hand) == InteractionResult.PASS) {
            if (itemstack != null && itemstack.getItem() == IafItemRegistry.DRAGON_STAFF.get() && this.isOwnedBy(player)) {
                if (player.isShiftKeyDown()) {
                    BlockPos pos = this.blockPosition();
                    this.homePos = pos;
                    this.hasHomePosition = true;
                    player.displayClientMessage(Component.translatable("amphithere.command.new_home", homePos.getX(), homePos.getY(), homePos.getZ()), true);
                    return InteractionResult.SUCCESS;
                }
                return InteractionResult.SUCCESS;
            }
            if (player.isShiftKeyDown() && this.isOwnedBy(player)) {
                if (player.getItemInHand(hand).isEmpty()) {
                    this.setCommand(this.getCommand() + 1);
                    if (this.getCommand() > 2) {
                        this.setCommand(0);
                    }
                    player.displayClientMessage(Component.translatable("amphithere.command." + this.getCommand()), true);
                    this.playSound(SoundEvents.ZOMBIE_INFECT, 1, 1);
                    return InteractionResult.SUCCESS;
                }
                return InteractionResult.SUCCESS;
            } else if ((!this.isTame() || this.isOwnedBy(player)) && !this.isBaby() && itemstack.isEmpty()) {
                player.startRiding(this);
                return InteractionResult.SUCCESS;
            }

        }
        return super.mobInteract(player, hand);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(1, new AmphithereAIAttackMelee(this, 1.0D, true));
        this.goalSelector.addGoal(2, new AmphithereAIFollowOwner(this, 1.0D, 10.0F, 2.0F));
        this.goalSelector.addGoal(3, new AmphithereAIFleePlayer(this, 32.0F, 0.8D, 1.8D));
        this.goalSelector.addGoal(3, new AIFlyWander());
        this.goalSelector.addGoal(3, new AIFlyCircle());
        this.goalSelector.addGoal(3, new AILandWander(this, 1.0D));
        this.goalSelector.addGoal(4, new EntityAIWatchClosestIgnoreRider(this, LivingEntity.class, 6.0F));
        this.goalSelector.addGoal(4, new BreedGoal(this, 1.0D));
        this.targetSelector.addGoal(1, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(3, new AmphithereAIHurtByTarget(this, false, new Class[0]));
        this.targetSelector.addGoal(3, new AmphithereAITargetItems<>(this, false));
    }

    public boolean isStill() {
        return Math.abs(this.getDeltaMovement().x) < 0.05 && Math.abs(this.getDeltaMovement().z) < 0.05;
    }

    protected void switchNavigator(int navigatorType) {
        if (navigatorType == 0) {
            this.moveControl = new MoveControl(this);
            this.navigation = new WallClimberNavigation(this, level());
            this.navigatorType = 0;
        } else if (navigatorType == 1) {
            this.moveControl = new EntityAmphithere.FlyMoveHelper(this);
            this.navigation = new PathNavigateFlyingCreature(this, level());
            this.navigatorType = 1;
        } else {
            this.moveControl = new FlyingMoveControl(this, 20, false);
            this.navigation = new PathNavigateFlyingCreature(this, level());
            this.navigatorType = 2;
        }
    }

    public boolean onLeaves() {
        BlockState state = level().getBlockState(this.blockPosition().below());
        return state.getBlock() instanceof LeavesBlock;
    }

    @Override
    public boolean hurt(@NotNull DamageSource source, float damage) {
        if (!this.isTame() && this.isFlying() && !onGround() && source.is(DamageTypeTags.IS_PROJECTILE) && !level().isClientSide) {
            this.isFallen = true;
        }
        if (source.getEntity() instanceof LivingEntity && source.getEntity().isPassengerOfSameVehicle(this) && this.isTame() && this.isOwnedBy((LivingEntity) source.getEntity())) {
            return false;
        }
        return super.hurt(source, damage);
    }

    @Override
    public void positionRider(@NotNull Entity passenger, @NotNull MoveFunction callback) {
        super.positionRider(passenger, callback);
        if (this.hasPassenger(passenger) && this.isTame()) {
            this.setYBodyRot(passenger.getYRot());
            this.setYHeadRot(passenger.getYHeadRot());
        }
        if (!this.level().isClientSide && !this.isTame() && passenger instanceof Player && this.getAnimation() == NO_ANIMATION && random.nextInt(15) == 0) {
            this.setAnimation(ANIMATION_BITE_RIDER);
        }
        if (!this.level().isClientSide && this.getAnimation() == ANIMATION_BITE_RIDER && this.getAnimationTick() == 6 && !this.isTame()) {
            passenger.hurt(this.level().damageSources().mobAttack(this), 1);
        }
        float pitch_forward = 0;
        if (this.getXRot() > 0 && this.isFlying()) {
            pitch_forward = (getXRot() / 45F) * 0.45F;
        } else {
            pitch_forward = 0;
        }
        float scaled_ground = this.groundProgress * 0.1F;
        float radius = (this.isTame() ? 0.5F : 0.3F) - scaled_ground * 0.5F + pitch_forward;
        float angle = (0.01745329251F * this.yBodyRot);
        double extraX = radius * Mth.sin((float) (Math.PI + angle));
        double extraZ = radius * Mth.cos(angle);
        passenger.setPos(this.getX() + extraX, this.getY() + 0.7F - scaled_ground * 0.14F + pitch_forward, this.getZ() + extraZ);

    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(IafItemTags.BREED_AMPITHERE);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (level().getDifficulty() == Difficulty.PEACEFUL && this.getTarget() instanceof Player) {
            this.setTarget(null);
        }
        if (this.isInWater() && this.jumping) {
            this.setDeltaMovement(this.getDeltaMovement().x, this.getDeltaMovement().y + 0.1D, this.getDeltaMovement().z);
        }
        if (this.isBaby() && this.getTarget() != null) {
            this.setTarget(null);
        }
        if (this.isInLove()) {
            this.setFlying(false);
        }
        if (this.isOrderedToSit() && this.getTarget() != null) {
            this.setTarget(null);
        }
        boolean flapping = this.isFlapping();
        boolean flying = this.isFlying() && this.isOverAir() || (this.isOverAir() && !onLeaves());
        boolean diving = flying && this.getDeltaMovement().y <= -0.1F || this.isFallen;
        boolean sitting = isOrderedToSit() && !isFlying();
        boolean notGrounded = flying || this.getAnimation() == ANIMATION_WING_BLAST;
        if (!level().isClientSide) {
            if (this.isOrderedToSit() && (this.getCommand() != 1 || this.getControllingPassenger() != null)) {
                this.setOrderedToSit(false);
            }
            if (!this.isOrderedToSit() && this.getCommand() == 1 && this.getControllingPassenger() == null) {
                this.setOrderedToSit(true);
            }
            if (this.isOrderedToSit()) {
                this.getNavigation().stop();
                //TODO
//                this.getMoveHelper().action = MovementController.Action.WAIT;
            }
            if (flying) {
                ticksFlying++;
            } else {
                ticksFlying = 0;
            }
        }
        if (isFlying() && this.onGround()) {
            this.setFlying(false);
        }
        if (sitting && sitProgress < 20.0F) {
            sitProgress += 0.5F;
        } else if (!sitting && sitProgress > 0.0F) {
            sitProgress -= 0.5F;
        }
        if (flightCooldown > 0) {
            flightCooldown--;
        }
        if (!level().isClientSide) {
            if (this.flightBehavior == FlightBehavior.CIRCLE) {
                ticksCircling++;
            } else {
                ticksCircling = 0;
            }
        }
        if (this.getUntamedRider() != null && !this.isTame()) {
            ridingTime++;
        }
        if (this.getUntamedRider() == null) {
            ridingTime = 0;
        }
        if (!this.isTame() && ridingTime > IafConfig.amphithereTameTime && this.getUntamedRider() != null && this.getUntamedRider() instanceof Player) {
            this.level().broadcastEntityEvent(this, (byte) 45);
            this.tame((Player) this.getUntamedRider());
            if (this.getTarget() == this.getUntamedRider())
                this.setTarget(null);
        }
        if (isStill()) {
            this.ticksStill++;
        } else {
            this.ticksStill = 0;
        }
        if (!this.isFlying() && !this.isBaby() && ((this.onGround() && this.random.nextInt(200) == 0 && flightCooldown == 0 && this.getPassengers().isEmpty() && !this.isNoAi() && canMove()) || this.getY() < -1)) {
            this.setDeltaMovement(this.getDeltaMovement().x, this.getDeltaMovement().y + 0.5D, this.getDeltaMovement().z);
            this.setFlying(true);
        }
        if (this.getControllingPassenger() != null && this.isFlying() && !this.onGround()) {

            if (this.getControllingPassenger().getXRot() > 25 && this.getDeltaMovement().y > -1.0F) {
                this.setDeltaMovement(this.getDeltaMovement().x, this.getDeltaMovement().y - 0.1D, this.getDeltaMovement().z);

            }
            if (this.getControllingPassenger().getXRot() < -25 && this.getDeltaMovement().y < 1.0F) {
                this.setDeltaMovement(this.getDeltaMovement().x, this.getDeltaMovement().y + 0.1D, this.getDeltaMovement().z);

            }
        }
        if (notGrounded && groundProgress > 0.0F) {
            groundProgress -= 2F;
        } else if (!notGrounded && groundProgress < 20.0F) {
            groundProgress += 2F;
        }
        if (diving && diveProgress < 20.0F) {
            diveProgress += 1F;
        } else if (!diving && diveProgress > 0.0F) {
            diveProgress -= 1F;
        }

        if (this.isFallen && this.flightBehavior != FlightBehavior.NONE) {
            this.flightBehavior = FlightBehavior.NONE;
        }
        if (this.flightBehavior == FlightBehavior.NONE && this.getControllingPassenger() == null && this.isFlying()) {
            this.setDeltaMovement(this.getDeltaMovement().x, this.getDeltaMovement().y - 0.3D, this.getDeltaMovement().z);
        }
        if (this.isFlying() && !this.onGround() && this.isFallen && this.getControllingPassenger() == null) {
            this.setDeltaMovement(this.getDeltaMovement().x, this.getDeltaMovement().y - 0.2D, this.getDeltaMovement().z);
            this.setXRot(Math.max(this.getXRot() + 5, 75));
        }
        if (this.isFallen && this.onGround()) {
            this.setFlying(false);
            if (this.isTame()) {
                flightCooldown = 50;
            } else {
                flightCooldown = 12000;
            }
            this.isFallen = false;
        }
        if (flying && this.isOverAir()) {
            if (this.getRidingPlayer() == null && this.navigatorType != 1) {
                switchNavigator(1);
            }
            if (this.getRidingPlayer() != null && this.navigatorType != 2) {
                switchNavigator(2);
            }
        }
        if (!flying && this.navigatorType != 0) {
            switchNavigator(0);
        }
        if ((this.hasHomePosition || this.getCommand() == 2) && this.flightBehavior == FlightBehavior.WANDER) {
            this.flightBehavior = FlightBehavior.CIRCLE;
        }
        if (flapping && flapProgress < 10.0F) {
            flapProgress += 1F;
        } else if (!flapping && flapProgress > 0.0F) {
            flapProgress -= 1F;
        }
        if (flapTicks > 0) {
            flapTicks--;
        }
        if (level().isClientSide) {
            if (!onGround()) {
                if (this.isVehicle())
                    roll_buffer.calculateChainFlapBufferHead(40, 1, 2F, 0.5F, this);
                else {
                    yBodyRot = getYRot();
                    roll_buffer.calculateChainFlapBuffer(70, 1, 2F, 0.5F, this);

                }
                pitch_buffer.calculateChainPitchBuffer(90, 10, 10F, 0.5F, this);
            }
            tail_buffer.calculateChainSwingBuffer(70, 20, 5F, this);
        }
        if (changedFlightBehavior) {
            changedFlightBehavior = false;
        }
        if (!flapping && (this.getDeltaMovement().y > 0.15F || this.getDeltaMovement().y > 0 && this.tickCount % 200 == 0) && this.isOverAir()) {
            flapWings();
        }
        AnimationHandler.INSTANCE.updateAnimations(this);
    }

    @Override
    public boolean isFlapping() {
        return flapTicks > 0;
    }

    public int getCommand() {
        return this.entityData.get(COMMAND).intValue();
    }

    public void setCommand(int command) {
        this.entityData.set(COMMAND, command);
        this.setOrderedToSit(command == 1);
    }

    @Override
    public void flapWings() {
        this.flapTicks = 20;
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

    @Nullable
    public Entity getUntamedRider() {
        for (Entity passenger : this.getPassengers()) {
            if (passenger instanceof Player) {
                return passenger;
            }
        }
        return null;
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

    public static AttributeSupplier.Builder bakeAttributes() {
        return Mob.createMobAttributes()
                //HEALTH
                .add(Attributes.MAX_HEALTH, IafConfig.amphithereMaxHealth)
                //SPEED
                .add(Attributes.MOVEMENT_SPEED, 0.4D)
                //ATTACK
                .add(Attributes.ATTACK_DAMAGE, IafConfig.amphithereAttackStrength)
                .add(Attributes.FLYING_SPEED, IafConfig.amphithereFlightSpeed)
                //FOLLOW RANGE
                .add(Attributes.FOLLOW_RANGE, 32.0D);
    }

    @Override
    public void setConfigurableAttributes() {
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(IafConfig.amphithereMaxHealth);
        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(IafConfig.amphithereAttackStrength);
        this.getAttribute(Attributes.FLYING_SPEED).setBaseValue(IafConfig.amphithereFlightSpeed);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(VARIANT, 0);
        this.entityData.define(FLYING, false);
        this.entityData.define(FLAP_TICKS, 0);
        this.entityData.define(CONTROL_STATE, (byte) 0);
        this.entityData.define(COMMAND, 0);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Variant", this.getVariant());
        compound.putBoolean("Flying", this.isFlying());
        compound.putInt("FlightCooldown", flightCooldown);
        compound.putInt("RidingTime", ridingTime);
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
        this.setFlying(compound.getBoolean("Flying"));
        flightCooldown = compound.getInt("FlightCooldown");
        ridingTime = compound.getInt("RidingTime");
        this.hasHomePosition = compound.getBoolean("HasHomePosition");
        if (hasHomePosition && compound.getInt("HomeAreaX") != 0 && compound.getInt("HomeAreaY") != 0 && compound.getInt("HomeAreaZ") != 0) {
            homePos = new BlockPos(compound.getInt("HomeAreaX"), compound.getInt("HomeAreaY"), compound.getInt("HomeAreaZ"));
        }
        this.setCommand(compound.getInt("Command"));
        this.setConfigurableAttributes();
    }

    //TODO: Create entity placements
    public boolean getCanSpawnHere() {
        int i = Mth.floor(this.getX());
        int j = Mth.floor(this.getBoundingBox().minY);
        int k = Mth.floor(this.getZ());
        BlockPos blockpos = new BlockPos(i, j, k);
        Block block = this.level().getBlockState(blockpos.below()).getBlock();
        return this.level().canSeeSkyFromBelowWater(blockpos.above());
    }

    @Override
    public void tick() {
        super.tick();
        LivingEntity target = this.getTarget();
        if (target != null && this.getAnimation() == ANIMATION_BITE && this.getAnimationTick() == 7) {
            double dist = this.distanceToSqr(target);
            if (dist < 10) {
                target.knockback(0.6F, Mth.sin(this.getYRot() * 0.017453292F), -Mth.cos(this.getYRot() * 0.017453292F));
                target.hurt(this.level().damageSources().mobAttack(this), ((int) this.getAttribute(Attributes.ATTACK_DAMAGE).getValue()));
            }
        }
        if (this.getAnimation() == ANIMATION_WING_BLAST && this.getAnimationTick() == 5) {
            this.playSound(IafSoundRegistry.AMPHITHERE_GUST, 1, 1);
        }
        if ((this.getAnimation() == ANIMATION_BITE || this.getAnimation() == ANIMATION_BITE_RIDER) && this.getAnimationTick() == 1) {
            this.playSound(IafSoundRegistry.AMPHITHERE_BITE, 1, 1);
        }
        if (target != null && this.getAnimation() == ANIMATION_WING_BLAST && this.getAnimationTick() > 5 && this.getAnimationTick() < 22) {

            double dist = this.distanceToSqr(target);
            if (dist < 25) {
                target.hurt(this.level().damageSources().mobAttack(this), ((int) this.getAttribute(Attributes.ATTACK_DAMAGE).getValue() / 2));
                target.hasImpulse = true;
                if (!(this.random.nextDouble() < this.getAttribute(Attributes.KNOCKBACK_RESISTANCE).getValue())) {
                    this.hasImpulse = true;
                    double d1 = target.getX() - this.getX();

                    double d0;
                    for (d0 = target.getZ() - this.getZ(); d1 * d1 + d0 * d0 < 1.0E-4D; d0 = (Math.random() - Math.random()) * 0.01D) {
                        d1 = (Math.random() - Math.random()) * 0.01D;
                    }
                    Vec3 Vector3d = this.getDeltaMovement();
                    Vec3 Vector3d1 = (new Vec3(d0, 0.0D, d1)).normalize().scale(0.5);
                    this.setDeltaMovement(Vector3d.x / 2.0D - Vector3d1.x, this.onGround() ? Math.min(0.4D, Vector3d.y / 2.0D + 0.5) : Vector3d.y, Vector3d.z / 2.0D - Vector3d1.z);
                }
            }
        }
        if (this.getAnimation() == ANIMATION_TAIL_WHIP && target != null && this.getAnimationTick() == 7) {
            double dist = this.distanceToSqr(target);
            if (dist < 10) {
                target.hurt(this.level().damageSources().mobAttack(this), ((int) this.getAttribute(Attributes.ATTACK_DAMAGE).getValue()));
                target.hasImpulse = true;
                float f = Mth.sqrt((float) (0.5 * 0.5 + 0.5 * 0.5));
                double d0;
                double d1 = target.getX() - this.getX();
                for (d0 = target.getZ() - this.getZ(); d1 * d1 + d0 * d0 < 1.0E-4D; d0 = (Math.random() - Math.random()) * 0.01D) {
                    d1 = (Math.random() - Math.random()) * 0.01D;
                }
                Vec3 Vector3d = this.getDeltaMovement();
                Vec3 Vector3d1 = (new Vec3(d0, 0.0D, d1)).normalize().scale(0.5);
                this.setDeltaMovement(Vector3d.x / 2.0D - Vector3d1.x, this.onGround() ? Math.min(0.4D, Vector3d.y / 2.0D + 0.5) : Vector3d.y, Vector3d.z / 2.0D - Vector3d1.z);

            }
        }
        if (this.isGoingUp() && !level().isClientSide) {
            if (!this.isFlying()) {
                this.setDeltaMovement(this.getDeltaMovement().add(0, 1, 0));
                this.setFlying(true);
            }
        }
        if (!this.isOverAir() && this.isFlying() && ticksFlying > 25) {
            this.setFlying(false);
        }
        if (this.dismountIAF()) {
            if (this.isFlying()) {
                if (this.onGround()) {
                    this.setFlying(false);
                }
            }
        }
        if (this.getUntamedRider() != null && this.getUntamedRider().isShiftKeyDown()) {
            if (this.getUntamedRider() instanceof LivingEntity rider) {
                EntityDataProvider.getCapability(rider).ifPresent(data -> data.miscData.setDismounted(true));
            }

            this.getUntamedRider().stopRiding();
        }
        if (this.attack() && this.getControllingPassenger() != null && this.getControllingPassenger() instanceof Player) {
            LivingEntity riderTarget = DragonUtils.riderLookingAtEntity(this, (Player) this.getControllingPassenger(), 2.5D);
            if (this.getAnimation() != ANIMATION_BITE) {
                this.setAnimation(ANIMATION_BITE);
            }
            if (riderTarget != null) {
                riderTarget.hurt(this.level().damageSources().mobAttack(this), ((int) this.getAttribute(Attributes.ATTACK_DAMAGE).getValue()));
            }
        }
        if (target != null && this.isOwnedBy(target)) {
            this.setTarget(null);
        }
        if (target != null && this.onGround() && this.isFlying() && ticksFlying > 40) {
            this.setFlying(false);
        }
    }

    @Override
    public boolean doHurtTarget(@NotNull Entity entityIn) {
        if (this.getAnimation() != ANIMATION_BITE && this.getAnimation() != ANIMATION_TAIL_WHIP && this.getAnimation() != ANIMATION_WING_BLAST && this.getControllingPassenger() == null) {
            if (random.nextBoolean()) {
                this.setAnimation(ANIMATION_BITE);
            } else {
                this.setAnimation(this.getRandom().nextBoolean() || this.isFlying() ? ANIMATION_WING_BLAST : ANIMATION_TAIL_WHIP);
            }
            return true;
        }
        return false;
    }

    @Override
    public @Nullable Player getRidingPlayer() {
        if (this.getControllingPassenger() instanceof Player player) {
            return player;
        }

        return null;
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

    public int getVariant() {
        return this.entityData.get(VARIANT);
    }

    public void setVariant(int variant) {
        this.entityData.set(VARIANT, variant);
    }

    @Override
    public boolean isGoingUp() {
        return (entityData.get(CONTROL_STATE) & 1) == 1;
    }

    @Override
    public boolean isGoingDown() {
        return (entityData.get(CONTROL_STATE) >> 1 & 1) == 1;
    }

    public boolean attack() {
        return (entityData.get(CONTROL_STATE) >> 2 & 1) == 1;
    }

    public boolean dismountIAF() {
        return (entityData.get(CONTROL_STATE) >> 3 & 1) == 1;
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
    @Nullable
    protected SoundEvent getAmbientSound() {
        return IafSoundRegistry.AMPHITHERE_IDLE;
    }

    @Override
    @Nullable
    protected SoundEvent getHurtSound(@NotNull DamageSource source) {
        return IafSoundRegistry.AMPHITHERE_HURT;
    }

    @Override
    @Nullable
    protected SoundEvent getDeathSound() {
        return IafSoundRegistry.AMPHITHERE_DIE;
    }

    @Override
    public int getAnimationTick() {
        return animationTick;
    }

    @Override
    public void setAnimationTick(int tick) {
        this.animationTick = tick;
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
        return new Animation[]{ANIMATION_BITE, ANIMATION_BITE_RIDER, ANIMATION_WING_BLAST, ANIMATION_TAIL_WHIP, ANIMATION_SPEAK};
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

    public boolean isBlinking() {
        return this.tickCount % 50 > 40;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel serverWorld, @NotNull AgeableMob ageableEntity) {
        EntityAmphithere amphithere = new EntityAmphithere(IafEntityRegistry.AMPHITHERE.get(), level());
        amphithere.setVariant(this.getVariant());
        return amphithere;
    }

    @Override
    public int getExperienceReward() {
        return 10;
    }

    @Override
    @Nullable
    public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor worldIn, @NotNull DifficultyInstance difficultyIn, @NotNull MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        spawnDataIn = super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
        this.setVariant(this.getRandom().nextInt(5));
        return spawnDataIn;
    }

    @Override
    public boolean canPhaseThroughBlock(LevelAccessor world, BlockPos pos) {
        return world.getBlockState(pos).getBlock() instanceof LeavesBlock;
    }

    // FIXME: I don't know what's is overriding the flight speed (I assume it's on the server side)
    @Override
    protected float getRiddenSpeed(@NotNull Player pPlayer) {
        return (this.isFlying() || this.isHovering()) ? (float) this.getAttributeValue(Attributes.FLYING_SPEED) * 2F : (float) this.getAttributeValue(Attributes.MOVEMENT_SPEED) * 0.5F;
    }

    @Override
    public void travel(@NotNull Vec3 travelVector) {
        if (this.isControlledByLocalInstance()) {
            if (this.isInWater()) {
                this.moveRelative(0.02F, travelVector);
                this.move(MoverType.SELF, this.getDeltaMovement());
                this.setDeltaMovement(this.getDeltaMovement().scale(0.8F));
            } else if (this.isInLava()) {
                this.moveRelative(0.02F, travelVector);
                this.move(MoverType.SELF, this.getDeltaMovement());
                this.setDeltaMovement(this.getDeltaMovement().scale(0.5D));
            } else if (this.isFlying() || this.isHovering()) {
                this.moveRelative(0.1F, travelVector);
                this.move(MoverType.SELF, this.getDeltaMovement());
                this.setDeltaMovement(this.getDeltaMovement().scale(0.9D));
            } else {
                super.travel(travelVector);
            }
        } else {
            super.travel(travelVector);
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

    public boolean canMove() {
        return this.getControllingPassenger() == null && sitProgress == 0 && !this.isOrderedToSit();
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 45) {
            this.playEffect();
        } else {
            super.handleEntityEvent(id);
        }
    }

    protected void playEffect() {
        for (int i = 0; i < 7; ++i) {
            double d0 = this.random.nextGaussian() * 0.02D;
            double d1 = this.random.nextGaussian() * 0.02D;
            double d2 = this.random.nextGaussian() * 0.02D;
            this.level().addParticle(ParticleTypes.HEART, this.getX() + this.random.nextFloat() * this.getBbWidth() * 2.0F - this.getBbWidth(), this.getY() + 0.5D + (this.random.nextFloat() * this.getBbHeight()), this.getZ() + this.random.nextFloat() * this.getBbWidth() * 2.0F - this.getBbWidth(), d0, d1, d2);
        }
    }

    @Override
    public void onHearFlute(Player player) {
        if (!this.onGround() && this.isTame()) {
            this.isFallen = true;
        }
    }

    @Override
    public boolean isPersistenceRequired() {
        return true;
    }

    @Override
    public double getFlightSpeedModifier() {
        return 0.555D;
    }

    @Override
    public boolean fliesLikeElytra() {
        return !this.onGround();
    }

    private boolean isOverAir() {
        return level().isEmptyBlock(this.blockPosition().below());
    }

    public boolean canBlockPosBeSeen(BlockPos pos) {
        Vec3 Vector3d = new Vec3(this.getX(), this.getEyeY(), this.getZ());
        Vec3 Vector3d1 = new Vec3(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D);
        return this.level().clip(new ClipContext(Vector3d, Vector3d1, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this)).getType() == HitResult.Type.MISS;
    }

    public enum FlightBehavior {
        CIRCLE,
        WANDER,
        NONE
    }

    class AILandWander extends WaterAvoidingRandomStrollGoal {
        public AILandWander(PathfinderMob creature, double speed) {
            super(creature, speed, 10);
        }

        @Override
        public boolean canUse() {
            return this.mob.onGround() && super.canUse() && ((EntityAmphithere) this.mob).canMove();
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }
    }

    class AIFlyWander extends Goal {
        BlockPos target;

        public AIFlyWander() {
        }

        @Override
        public boolean canUse() {
            if (EntityAmphithere.this.flightBehavior != FlightBehavior.WANDER || !EntityAmphithere.this.canMove()) {
                return false;
            }
            if (EntityAmphithere.this.isFlying()) {
                target = EntityAmphithere.getPositionRelativetoGround(EntityAmphithere.this, EntityAmphithere.this.level(), EntityAmphithere.this.getBlockX() + EntityAmphithere.this.random.nextInt(30) - 15, EntityAmphithere.this.getBlockZ() + EntityAmphithere.this.random.nextInt(30) - 15, EntityAmphithere.this.random);
                EntityAmphithere.this.orbitPos = null;
                return (!EntityAmphithere.this.getMoveControl().hasWanted() || EntityAmphithere.this.ticksStill >= 50);
            } else {
                return false;
            }
        }

        protected boolean isDirectPathBetweenPoints(Entity e) {
            return EntityAmphithere.this.canBlockPosBeSeen(target);
        }

        @Override
        public boolean canContinueToUse() {
            return false;
        }

        @Override
        public void tick() {
            if (!isDirectPathBetweenPoints(EntityAmphithere.this)) {
                target = EntityAmphithere.getPositionRelativetoGround(EntityAmphithere.this, EntityAmphithere.this.level(), EntityAmphithere.this.getBlockX() + EntityAmphithere.this.random.nextInt(30) - 15, EntityAmphithere.this.getBlockZ() + EntityAmphithere.this.random.nextInt(30) - 15, EntityAmphithere.this.random);
            }
            if (EntityAmphithere.this.level().isEmptyBlock(target)) {
                EntityAmphithere.this.moveControl.setWantedPosition(target.getX() + 0.5D, target.getY() + 0.5D, target.getZ() + 0.5D, 0.25D);
                if (EntityAmphithere.this.getTarget() == null) {
                    EntityAmphithere.this.getLookControl().setLookAt(target.getX() + 0.5D, target.getY() + 0.5D, target.getZ() + 0.5D, 180.0F, 20.0F);

                }
            }
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }
    }

    class AIFlyCircle extends Goal {
        BlockPos target;

        public AIFlyCircle() {
        }

        @Override
        public boolean canUse() {
            if (EntityAmphithere.this.flightBehavior != FlightBehavior.CIRCLE || !EntityAmphithere.this.canMove()) {
                return false;
            }
            if (EntityAmphithere.this.isFlying()) {
                EntityAmphithere.this.orbitPos = EntityAmphithere.getPositionRelativetoGround(EntityAmphithere.this, EntityAmphithere.this.level(), EntityAmphithere.this.getBlockX() + EntityAmphithere.this.random.nextInt(30) - 15, EntityAmphithere.this.getBlockZ() + EntityAmphithere.this.random.nextInt(30) - 15, EntityAmphithere.this.random);
                target = EntityAmphithere.getPositionInOrbit(EntityAmphithere.this, level(), EntityAmphithere.this.orbitPos, EntityAmphithere.this.random);
                return true;
            } else {
                return false;
            }
        }

        protected boolean isDirectPathBetweenPoints() {
            return EntityAmphithere.this.canBlockPosBeSeen(target);
        }

        @Override
        public boolean canContinueToUse() {
            return false;
        }

        @Override
        public void tick() {
            if (!isDirectPathBetweenPoints()) {
                target = EntityAmphithere.getPositionInOrbit(EntityAmphithere.this, level(), EntityAmphithere.this.orbitPos, EntityAmphithere.this.random);
            }
            if (EntityAmphithere.this.level().isEmptyBlock(target)) {
                EntityAmphithere.this.moveControl.setWantedPosition(target.getX() + 0.5D, target.getY() + 0.5D, target.getZ() + 0.5D, 0.25D);
                if (EntityAmphithere.this.getTarget() == null) {
                    EntityAmphithere.this.getLookControl().setLookAt(target.getX() + 0.5D, target.getY() + 0.5D, target.getZ() + 0.5D, 180.0F, 20.0F);

                }
            }
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }
    }

    class FlyMoveHelper extends MoveControl {
        public FlyMoveHelper(EntityAmphithere entity) {
            super(entity);
            this.speedModifier = 1.75F;
        }

        @Override
        public void tick() {
            if (!EntityAmphithere.this.canMove()) {
                return;
            }
            if (EntityAmphithere.this.horizontalCollision) {
                EntityAmphithere.this.setYRot(getYRot() + 180.0F);
                this.speedModifier = 0.1F;
                BlockPos target = EntityAmphithere.getPositionRelativetoGround(EntityAmphithere.this, EntityAmphithere.this.level(), EntityAmphithere.this.getBlockX() + EntityAmphithere.this.random.nextInt(15) - 7, EntityAmphithere.this.getBlockZ() + EntityAmphithere.this.random.nextInt(15) - 7, EntityAmphithere.this.random);
                this.wantedX = target.getX();
                this.wantedY = target.getY();
                this.wantedZ = target.getZ();
            }
            if (this.operation == MoveControl.Operation.MOVE_TO) {

                double d0 = this.wantedX - EntityAmphithere.this.getX();
                double d1 = this.wantedY - EntityAmphithere.this.getY();
                double d2 = this.wantedZ - EntityAmphithere.this.getZ();
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;
                d3 = Mth.sqrt((float) d3);
                if (d3 < 6 && EntityAmphithere.this.getTarget() == null) {
                    if (!EntityAmphithere.this.changedFlightBehavior && EntityAmphithere.this.flightBehavior == FlightBehavior.WANDER && EntityAmphithere.this.random.nextInt(30) == 0) {
                        EntityAmphithere.this.flightBehavior = FlightBehavior.CIRCLE;
                        EntityAmphithere.this.changedFlightBehavior = true;
                    }
                    if (!EntityAmphithere.this.changedFlightBehavior && EntityAmphithere.this.flightBehavior == FlightBehavior.CIRCLE && EntityAmphithere.this.random.nextInt(5) == 0 && ticksCircling > 150) {
                        EntityAmphithere.this.flightBehavior = FlightBehavior.WANDER;
                        EntityAmphithere.this.changedFlightBehavior = true;
                    }
                    if (EntityAmphithere.this.hasHomePosition && EntityAmphithere.this.flightBehavior != FlightBehavior.NONE || EntityAmphithere.this.getCommand() == 2) {
                        EntityAmphithere.this.flightBehavior = FlightBehavior.CIRCLE;
                    }
                }
                if (d3 < 1 && EntityAmphithere.this.getTarget() == null) {
                    this.operation = MoveControl.Operation.WAIT;
                    EntityAmphithere.this.setDeltaMovement(EntityAmphithere.this.getDeltaMovement().multiply(0.5D, 0.5D, 0.5D));
                } else {
                    EntityAmphithere.this.setDeltaMovement(EntityAmphithere.this.getDeltaMovement().add(d0 / d3 * 0.5D * this.speedModifier, d1 / d3 * 0.5D * this.speedModifier, d2 / d3 * 0.5D * this.speedModifier));
                    float f1 = (float) (-(Mth.atan2(d1, d3) * (180D / Math.PI)));
                    EntityAmphithere.this.setXRot(f1);
                    if (EntityAmphithere.this.getTarget() == null) {
                        EntityAmphithere.this.setYRot(-((float) Mth.atan2(EntityAmphithere.this.getDeltaMovement().x, EntityAmphithere.this.getDeltaMovement().z)) * (180F / (float) Math.PI));
                        EntityAmphithere.this.yBodyRot = EntityAmphithere.this.getYRot();
                    } else {
                        double d4 = EntityAmphithere.this.getTarget().getX() - EntityAmphithere.this.getX();
                        double d5 = EntityAmphithere.this.getTarget().getZ() - EntityAmphithere.this.getZ();
                        EntityAmphithere.this.setYRot(-((float) Mth.atan2(d4, d5)) * (180F / (float) Math.PI));
                        EntityAmphithere.this.yBodyRot = EntityAmphithere.this.getYRot();
                    }
                }
            }
        }
    }
}
