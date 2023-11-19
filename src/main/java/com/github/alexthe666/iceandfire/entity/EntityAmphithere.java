package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.client.model.IFChainBuffer;
import com.github.alexthe666.iceandfire.entity.ai.*;
import com.github.alexthe666.iceandfire.entity.props.MiscProperties;
import com.github.alexthe666.iceandfire.entity.util.*;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import com.github.alexthe666.iceandfire.pathfinding.PathNavigateFlyingCreature;
import com.github.alexthe666.iceandfire.world.IafWorldRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.ai.navigation.WallClimberNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Random;

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
        IHasCustomizableAttributes.applyAttributesForEntity(type, this);
        this.maxUpStep = 1;
        if (worldIn.isClientSide) {
            roll_buffer = new IFChainBuffer();
            pitch_buffer = new IFChainBuffer();
            tail_buffer = new IFChainBuffer();
        }
        switchNavigator(0);
    }

    public static BlockPos getPositionRelativetoGround(Entity entity, Level world, double x, double z, Random rand) {
        BlockPos pos = new BlockPos(x, entity.getY(), z);
        for (int yDown = 0; yDown < 6 + rand.nextInt(6); yDown++) {
            if (!world.isEmptyBlock(pos.below(yDown))) {
                return pos.above(yDown);
            }
        }
        return pos;
    }

    public static boolean canAmphithereSpawnOn(EntityType<EntityAmphithere> parrotIn, LevelAccessor worldIn, MobSpawnType reason, BlockPos p_223317_3_, Random random) {
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

    public static BlockPos getPositionInOrbit(EntityAmphithere entity, Level world, BlockPos orbit, Random rand) {
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
        BlockPos radialPos = new BlockPos(orbit.getX() + extraX, orbit.getY(), orbit.getZ() + extraZ);
        //world.setBlockState(radialPos.down(4), Blocks.QUARTZ_BLOCK.getDefaultState());
        // world.setBlockState(orbit.down(4), Blocks.GOLD_BLOCK.getDefaultState());
        entity.orbitRadius = possibleOrbitRadius;
        return radialPos;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGroundIn, @NotNull BlockState state, @NotNull BlockPos pos) {
    }

    @Override
    public float getWalkTargetValue(@NotNull BlockPos pos) {
        if (this.isFlying()) {
            if (level.isEmptyBlock(pos)) {
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

        if (itemstack != null && itemstack.getItem() == Items.COOKIE) {
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
        if (itemstack != null && itemstack.getItem() == Items.COCOA_BEANS && this.getHealth() < this.getMaxHealth()) {
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
                    player.displayClientMessage(new TranslatableComponent("amphithere.command.new_home", homePos.getX(), homePos.getY(), homePos.getZ()), true);
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
                    player.displayClientMessage(new TranslatableComponent("amphithere.command." + this.getCommand()), true);
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
//        this.goalSelector.addGoal(0, new DragonAIRide(this));
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
        this.targetSelector.addGoal(3, new AmphithereAITargetItems(this, false));
    }

    public boolean isStill() {
        return Math.abs(this.getDeltaMovement().x) < 0.05 && Math.abs(this.getDeltaMovement().z) < 0.05;
    }

    protected void switchNavigator(int navigatorType) {
        if (navigatorType == 0) {
            this.moveControl = new MoveControl(this);
            this.navigation = new WallClimberNavigation(this, level);
            this.navigatorType = 0;
        } else if (navigatorType == 1) {
            this.moveControl = new EntityAmphithere.FlyMoveHelper(this);
            this.navigation = new PathNavigateFlyingCreature(this, level);
            this.navigatorType = 1;
        } else {
            this.moveControl = new IafDragonFlightManager.PlayerFlightMoveHelper(this);
            this.navigation = new PathNavigateFlyingCreature(this, level);
            this.navigatorType = 2;
        }
    }

    public boolean onLeaves() {
        BlockState state = level.getBlockState(this.blockPosition().below());
        return state.getBlock() instanceof LeavesBlock;
    }

    @Override
    public boolean hurt(@NotNull DamageSource source, float damage) {
        if (!this.isTame() && this.isFlying() && !isOnGround() && source.isProjectile() && !level.isClientSide) {
            this.isFallen = true;
        }
        if (source.getEntity() instanceof LivingEntity && source.getEntity().isPassengerOfSameVehicle(this) && this.isTame() && this.isOwnedBy((LivingEntity) source.getEntity())) {
            return false;
        }
        return super.hurt(source, damage);
    }

    @Override
    public void positionRider(@NotNull Entity passenger) {
        super.positionRider(passenger);
        if (this.hasPassenger(passenger) && this.isTame()) {
            // TODO: remove, or add a 360 check
//            this.setYBodyRot(passenger.getYRot());
//            this.setYHeadRot(passenger.getYHeadRot());
        }
        if (!this.level.isClientSide && !this.isTame() && passenger instanceof Player && this.getAnimation() == NO_ANIMATION && random.nextInt(15) == 0) {
            this.setAnimation(ANIMATION_BITE_RIDER);
        }
        if (!this.level.isClientSide && this.getAnimation() == ANIMATION_BITE_RIDER && this.getAnimationTick() == 6 && !this.isTame()) {
            passenger.hurt(DamageSource.mobAttack(this), 1);
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
        return stack.getItem() == Items.COOKIE;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (level.getDifficulty() == Difficulty.PEACEFUL && this.getTarget() instanceof Player) {
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
        if (!level.isClientSide) {
            if (this.isOrderedToSit() && (this.getCommand() != 1 || this.getControllingPassenger() != null)) {
                this.setOrderedToSit(false);
            }
            if (!this.isOrderedToSit() && this.getCommand() == 1 && this.getControllingPassenger() == null) {
                this.setOrderedToSit(true);
            }
            if (this.isOrderedToSit()) {
                this.getNavigation().stop();
                //TODO
                //this.getMoveHelper().action = MovementController.Action.WAIT;
            }
            if (flying) {
                ticksFlying++;
            } else {
                ticksFlying = 0;
            }
        }
        if (isFlying() && this.onGround) {
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
        if (!level.isClientSide) {
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
            this.level.broadcastEntityEvent(this, (byte) 45);
            this.tame((Player) this.getUntamedRider());
            if (this.getTarget() == this.getUntamedRider())
                this.setTarget(null);
        }
        if (isStill()) {
            this.ticksStill++;
        } else {
            this.ticksStill = 0;
        }
        if (!this.isFlying() && !this.isBaby() && ((this.isOnGround() && this.random.nextInt(200) == 0 && flightCooldown == 0 && this.getPassengers().isEmpty() && !this.isNoAi() && canMove()) || this.getY() < -1)) {
            this.setDeltaMovement(this.getDeltaMovement().x, this.getDeltaMovement().y + 0.5D, this.getDeltaMovement().z);
            this.setFlying(true);
        }
        if (this.getControllingPassenger() != null && this.isFlying() && !this.onGround) {

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
        // TODO: ??
        if (this.isFlying() && this.getControllingPassenger() == null) {
            this.setDeltaMovement(this.getDeltaMovement().x, this.getDeltaMovement().y + 0.08D, this.getDeltaMovement().z);
        }
        if (this.isFallen && this.flightBehavior != FlightBehavior.NONE) {
            this.flightBehavior = FlightBehavior.NONE;
        }
        if (this.flightBehavior == FlightBehavior.NONE && this.getControllingPassenger() == null && this.isFlying()) {
            this.setDeltaMovement(this.getDeltaMovement().x, this.getDeltaMovement().y - 0.3D, this.getDeltaMovement().z);
        }
        if (this.isFlying() && !this.isOnGround() && this.isFallen && this.getControllingPassenger() == null) {
            this.setDeltaMovement(this.getDeltaMovement().x, this.getDeltaMovement().y - 0.2D, this.getDeltaMovement().z);
            this.setXRot(Math.max(this.getXRot() + 5, 75));
        }
        if (this.isFallen && this.onGround) {
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
        if (level.isClientSide) {
            if (!onGround) {
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
            this.entityData.set(DATA_FLAGS_ID, (byte) (b0 | 1));
        } else {
            this.entityData.set(DATA_FLAGS_ID, (byte) (b0 & -2));
        }
    }

    @Override
    @Nullable
    public Entity getControllingPassenger() {
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
            //FOLLOW RANGE
            .add(Attributes.FOLLOW_RANGE, 32.0D);
    }

    @Override
    public AttributeSupplier.Builder getConfigurableAttributes() {
        return bakeAttributes();
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
    }

    public boolean getCanSpawnHere() {
        int i = Mth.floor(this.getX());
        int j = Mth.floor(this.getBoundingBox().minY);
        int k = Mth.floor(this.getZ());
        BlockPos blockpos = new BlockPos(i, j, k);
        Block block = this.level.getBlockState(blockpos.below()).getBlock();
        return this.level.canSeeSkyFromBelowWater(blockpos.above());
    }

    @Override
    public void tick() {
        super.tick();
        LivingEntity target = this.getTarget();
        if (target != null && this.getAnimation() == ANIMATION_BITE && this.getAnimationTick() == 7) {
            double dist = this.distanceToSqr(target);
            if (dist < 10) {
                target.knockback(0.6F, Mth.sin(this.getYRot() * 0.017453292F), -Mth.cos(this.getYRot() * 0.017453292F));
                target.hurt(DamageSource.mobAttack(this), ((int) this.getAttribute(Attributes.ATTACK_DAMAGE).getValue()));
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
                target.hurt(DamageSource.mobAttack(this), ((int) this.getAttribute(Attributes.ATTACK_DAMAGE).getValue() / 2));
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
                    this.setDeltaMovement(Vector3d.x / 2.0D - Vector3d1.x, this.isOnGround() ? Math.min(0.4D, Vector3d.y / 2.0D + 0.5) : Vector3d.y, Vector3d.z / 2.0D - Vector3d1.z);
                }
            }
        }
        if (this.getAnimation() == ANIMATION_TAIL_WHIP && target != null && this.getAnimationTick() == 7) {
            double dist = this.distanceToSqr(target);
            if (dist < 10) {
                target.hurt(DamageSource.mobAttack(this), ((int) this.getAttribute(Attributes.ATTACK_DAMAGE).getValue()));
                target.hasImpulse = true;
                float f = Mth.sqrt((float) (0.5 * 0.5 + 0.5 * 0.5));
                double d0;
                double d1 = target.getX() - this.getX();
                for (d0 = target.getZ() - this.getZ(); d1 * d1 + d0 * d0 < 1.0E-4D; d0 = (Math.random() - Math.random()) * 0.01D) {
                    d1 = (Math.random() - Math.random()) * 0.01D;
                }
                Vec3 Vector3d = this.getDeltaMovement();
                Vec3 Vector3d1 = (new Vec3(d0, 0.0D, d1)).normalize().scale(0.5);
                this.setDeltaMovement(Vector3d.x / 2.0D - Vector3d1.x, this.isOnGround() ? Math.min(0.4D, Vector3d.y / 2.0D + 0.5) : Vector3d.y, Vector3d.z / 2.0D - Vector3d1.z);

            }
        }
        if (this.isGoingUp() && !level.isClientSide) {
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
                if (this.onGround) {
                    this.setFlying(false);
                }
            }
        }
        if (this.getUntamedRider() != null && this.getUntamedRider().isShiftKeyDown()) {
            if (this.getUntamedRider() instanceof LivingEntity)
                MiscProperties.setDismountedDragon((LivingEntity) this.getUntamedRider(), true);
            this.getUntamedRider().stopRiding();
        }
        if (this.attack() && this.getControllingPassenger() != null && this.getControllingPassenger() instanceof Player) {
            LivingEntity riderTarget = DragonUtils.riderLookingAtEntity(this, (Player) this.getControllingPassenger(), 2.5D);
            if (this.getAnimation() != ANIMATION_BITE) {
                this.setAnimation(ANIMATION_BITE);
            }
            if (riderTarget != null) {
                riderTarget.hurt(DamageSource.mobAttack(this), ((int) this.getAttribute(Attributes.ATTACK_DAMAGE).getValue()));
            }
        }
        if (target != null && this.isOwnedBy(target)) {
            this.setTarget(null);
        }
        if (target != null && this.isOnGround() && this.isFlying() && ticksFlying > 40) {
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

    public boolean isRidingPlayer(Player player) {
        return getRidingPlayer() != null && player != null && getRidingPlayer().getUUID().equals(player.getUUID());
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
    public boolean isFlying() {
        if (level.isClientSide) {
            return this.isFlying = this.entityData.get(FLYING).booleanValue();
        }
        return isFlying;
    }

    public void setFlying(boolean flying) {
        this.entityData.set(FLYING, flying);
        if (!level.isClientSide) {
            this.isFlying = flying;
        }
    }

    public int getVariant() {
        return this.entityData.get(VARIANT).intValue();
    }

    public void setVariant(int variant) {
        this.entityData.set(VARIANT, variant);
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
        EntityAmphithere amphithere = new EntityAmphithere(IafEntityRegistry.AMPHITHERE.get(), level);
        amphithere.setVariant(this.getVariant());
        return amphithere;
    }

    @Override
    protected int getExperienceReward(@NotNull Player player) {
        return 10;
    }

    @Override
    @Nullable
    public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor worldIn, @NotNull DifficultyInstance difficultyIn, @NotNull MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        spawnDataIn = super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
        this.setVariant(this.getRandom().nextInt(5));
        return spawnDataIn;
    }



    public void fall(float distance, float damageMultiplier) {
    }

    @Override
    public boolean canPhaseThroughBlock(LevelAccessor world, BlockPos pos) {
        return world.getBlockState(pos).getBlock() instanceof LeavesBlock;
    }

    @Override
    public void setDeltaMovement(double pX, double pY, double pZ) {
        super.setDeltaMovement(pX, pY, pZ);
    }

    public boolean allowLocalMotionControl = true;
    public boolean allowMousePitchControl = true;
    protected boolean gliding = false;
    protected float glidingSpeedBonus = 0;
    public double minimumSpeed = .2f;
    public double maximumSpeed = .6f;
    @Override
    public void travel(@NotNull Vec3 pTravelVector) {
        if (!this.canMove() && !this.isVehicle()) {
            super.travel(pTravelVector.multiply(0, 1, 0));
            return;
        }

        // TODO: add pitch animation
        // TODO: how to use fliesLikeElytra flag
        // TODO: match original speed

        // Player riding controls
        // Note: when motion is handled by the client no server side setDeltaMovement() should be called
        // otherwise the movement will halt
        // Todo: move wrongly fix
        if (allowLocalMotionControl && this.getControllingPassenger() != null && canBeControlledByRider()) {
            LivingEntity rider = (LivingEntity) this.getControllingPassenger();
            if (rider == null) {
                super.travel(pTravelVector);
                return;
            }


//            final int i = rider.getPassengers().indexOf(this);
//            final float radius = (i == 2 ? -0.2F : 0.5F) + (((Player) rider).isFallFlying() ? 2 : 0);
//            final float angle = (0.01745329251F * ((Player) rider).yBodyRot) + (i == 1 ? 90 : i == 0 ? -90 : 0);
//            final double extraX = radius * Mth.sin((float) (Math.PI + angle));
//            final double extraZ = radius * Mth.cos(angle);
//            final double extraY = (rider.isShiftKeyDown() ? 1.2D : 1.4D) + (i == 2 ? 0.4D : 0D);
//            this.yHeadRot = ((Player) rider).yHeadRot;
//            this.setYRot(((Player) rider).yHeadRot);
            this.setYRot(rider.getYRot());
            this.yRotO = this.getYRot();
//            this.yHeadRotO = this.getYRot();
//            this.yBodyRotO = this.getYRot();
            this.setXRot(rider.getXRot() * 0.5F);
            this.setRot(this.getYRot(), this.getXRot());
            this.yBodyRot = this.getYRot();

            this.yHeadRotO = this.yHeadRot;
            this.yHeadRot = this.yBodyRot;

            // Flying control, include flying through waterfalls
            if (isHovering() || isFlying()) {
//                double forward = rider.zza;
                double forward = 1f;
                double strafing = rider.xxa;
                double vertical = 0;
                float speed = (float) this.getAttributeValue(Attributes.MOVEMENT_SPEED) * .5f;
                // Bigger difference in speed for young and elder dragons
//                float airSpeedModifier = (float) (5.2f + 1.0f * Mth.map(Math.min(this.getAgeInDays(), 125), 0, 125, 0f, 1.5f));
                float airSpeedModifier = (float) (5.2f + 1.0f * Mth.map(speed, this.minimumSpeed, this.maximumSpeed, 0f, 1.5f));
                // Apply speed mod
                speed *= airSpeedModifier;
                // Set flag for logic and animation
//                if (forward > 0) {
//                    this.setFlying(true);
//                }

                gliding = allowMousePitchControl && rider.isSprinting();
                gliding = true;
                if (!gliding) {
                    // Mouse controlled yaw
                    speed += glidingSpeedBonus;
                    // Slower on going astern
                    forward *= rider.zza > 0 ? 1.0f : 0.5f;
                    // Slower on going sideways
                    strafing *= 0.4f;
                    if (isGoingUp() && !isGoingDown()) {
                        vertical = 1f;
                    } else if (isGoingDown() && !isGoingUp()) {
                        vertical = -1f;
                    }
                    // Damp the vertical motion so the dragon's head is more responsive to the control
                    else if (isControlledByLocalInstance()) {
//                        this.setDeltaMovement(this.getDeltaMovement().multiply(1.0f, 0.8f, 1.0f));
                    }
                } else {
                    // Mouse controlled yaw and pitch
                    speed *= 1.5f;
                    strafing *= 0.1f;
                    // Diving is faster
                    // Todo: a new and better algorithm much like elytra flying
                    glidingSpeedBonus = (float) Mth.clamp(glidingSpeedBonus + this.getDeltaMovement().y * -0.05d, -0.8d, 1.5d);
                    speed += glidingSpeedBonus;
                    // Try to match the moving vector to the rider's look vector
                    forward = Mth.abs(Mth.cos(this.getXRot() * ((float) Math.PI / 180F)));
                    vertical = Mth.abs(Mth.sin(this.getXRot() * ((float) Math.PI / 180F)));
                    // Pitch is still responsive to spacebar and x key
                    if (isGoingUp() && !isGoingDown()) {
                        vertical = Math.max(vertical, 0.5);
                    } else if (isGoingDown() && !isGoingUp()) {
                        vertical = Math.min(vertical, -0.5);
                    } else if (isGoingUp() && isGoingDown()) {
                        vertical = 0;
                    }
                    // X rotation takes minus on looking upward
                    else if (this.getXRot() < 0) {
                        vertical *= 1;
                    } else if (this.getXRot() > 0) {
                        vertical *= -1;
                    } else if (isControlledByLocalInstance()) {
//                        this.setDeltaMovement(this.getDeltaMovement().multiply(1.0f, 0.8f, 1.0f));
                    }
                }
                // Speed bonus damping
                glidingSpeedBonus -= glidingSpeedBonus * 0.01d;

                if (this.isControlledByLocalInstance()) {
                    // Vanilla friction on Y axis is smaller, which will influence terminal speed for climbing and diving
                    // use same friction coefficient on all axis simplifies how travel vector is computed
                    this.flyingSpeed = speed * 0.1F;
                    this.setSpeed(speed);

                    this.moveRelative(flyingSpeed, new Vec3(strafing, vertical, forward));
                    this.move(MoverType.SELF, this.getDeltaMovement());
                    this.setDeltaMovement(this.getDeltaMovement().multiply(new Vec3(0.9, 0.9, 0.9)));

                    Vec3 currentMotion = this.getDeltaMovement();
                    if (this.horizontalCollision) {
                        currentMotion = new Vec3(currentMotion.x, 0.1D, currentMotion.z);
                    }
                    this.setDeltaMovement(currentMotion);

                    this.calculateEntityAnimation(this, false);
                } else {
                    this.setDeltaMovement(Vec3.ZERO);
                }
                this.tryCheckInsideBlocks();
                return;
            }
            // In water move control, for those that can't swim
            else if (isInWater() || isInLava()) {
                double forward = rider.zza;
                double strafing = rider.xxa;
                double vertical = 0;
                float speed = (float) this.getAttributeValue(Attributes.MOVEMENT_SPEED);

                if (isGoingUp() && !isGoingDown()) {
                    vertical = 0.5f;
                } else if (isGoingDown() && !isGoingUp()) {
                    vertical = -0.5f;
                }

                this.flyingSpeed = speed;
                // Float in water for those can't swim is done in LivingEntity#aiStep on server side
                // Leave this handled by both side before we have a better solution
                this.setSpeed(speed);
                // Overwrite the zza in setSpeed
                this.setZza((float) forward);
                // Vanilla in water behavior includes float on water and moving very slow
                // in lava behavior includes moving slow and sink
                super.travel(pTravelVector.add(strafing, vertical, forward));

                return;
            }
            // Walking control
            else {
                double forward = rider.zza;
                double strafing = rider.xxa;
                // Inherit y motion for dropping
                double vertical = pTravelVector.y;
                float speed = (float) this.getAttributeValue(Attributes.MOVEMENT_SPEED);

                float groundSpeedModifier = (float) (1.8F * this.getFlightSpeedModifier());
                speed *= groundSpeedModifier;
                // Try to match the original riding speed
                forward *= speed;
                // Faster sprint
                forward *= rider.isSprinting() ? 1.2f : 1.0f;
                // Slower going back
                forward *= rider.zza > 0 ? 1.0f : 0.2f;
                // Slower going sideway
                strafing *= 0.05f;

                if (this.isControlledByLocalInstance()) {
                    this.flyingSpeed = speed * 0.1F;
                    this.setSpeed(speed);

                    // Vanilla walking behavior includes going up steps
                    super.travel(new Vec3(strafing, vertical, forward));
                } else {
                    this.setDeltaMovement(Vec3.ZERO);
                }
                this.tryCheckInsideBlocks();
                return;
            }
        }
        // No rider move control
        else {
            super.travel(pTravelVector);
        }
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
            this.level.addParticle(ParticleTypes.HEART, this.getX() + this.random.nextFloat() * this.getBbWidth() * 2.0F - this.getBbWidth(), this.getY() + 0.5D + (this.random.nextFloat() * this.getBbHeight()), this.getZ() + this.random.nextFloat() * this.getBbWidth() * 2.0F - this.getBbWidth(), d0, d1, d2);
        }
    }

    @Override
    public void onHearFlute(Player player) {
        if (!this.isOnGround() && this.isTame()) {
            this.isFallen = true;
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
        return super.isControlledByLocalInstance();
    }

    @Override
    public boolean canBeControlledByRider() {
        return true;
    }

    @Override
    public double getFlightSpeedModifier() {
        return 0.555D;
    }

    @Override
    public boolean fliesLikeElytra() {
        return !this.onGround;
    }

    private boolean isOverAir() {
        return level.isEmptyBlock(this.blockPosition().below());
    }

    public boolean canBlockPosBeSeen(BlockPos pos) {
        Vec3 Vector3d = new Vec3(this.getX(), this.getEyeY(), this.getZ());
        Vec3 Vector3d1 = new Vec3(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D);
        return this.level.clip(new ClipContext(Vector3d, Vector3d1, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this)).getType() == HitResult.Type.MISS;
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
            return this.mob.isOnGround() && super.canUse() && ((EntityAmphithere) this.mob).canMove();
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
                target = EntityAmphithere.getPositionRelativetoGround(EntityAmphithere.this, EntityAmphithere.this.level, EntityAmphithere.this.getX() + EntityAmphithere.this.random.nextInt(30) - 15, EntityAmphithere.this.getZ() + EntityAmphithere.this.random.nextInt(30) - 15, EntityAmphithere.this.random);
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
                target = EntityAmphithere.getPositionRelativetoGround(EntityAmphithere.this, EntityAmphithere.this.level, EntityAmphithere.this.getX() + EntityAmphithere.this.random.nextInt(30) - 15, EntityAmphithere.this.getZ() + EntityAmphithere.this.random.nextInt(30) - 15, EntityAmphithere.this.random);
            }
            if (EntityAmphithere.this.level.isEmptyBlock(target)) {
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
                EntityAmphithere.this.orbitPos = EntityAmphithere.getPositionRelativetoGround(EntityAmphithere.this, EntityAmphithere.this.level, EntityAmphithere.this.getX() + EntityAmphithere.this.random.nextInt(30) - 15, EntityAmphithere.this.getZ() + EntityAmphithere.this.random.nextInt(30) - 15, EntityAmphithere.this.random);
                target = EntityAmphithere.getPositionInOrbit(EntityAmphithere.this, level, EntityAmphithere.this.orbitPos, EntityAmphithere.this.random);
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
                target = EntityAmphithere.getPositionInOrbit(EntityAmphithere.this, level, EntityAmphithere.this.orbitPos, EntityAmphithere.this.random);
            }
            if (EntityAmphithere.this.level.isEmptyBlock(target)) {
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
                BlockPos target = EntityAmphithere.getPositionRelativetoGround(EntityAmphithere.this, EntityAmphithere.this.level, EntityAmphithere.this.getX() + EntityAmphithere.this.random.nextInt(15) - 7, EntityAmphithere.this.getZ() + EntityAmphithere.this.random.nextInt(15) - 7, EntityAmphithere.this.random);
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
