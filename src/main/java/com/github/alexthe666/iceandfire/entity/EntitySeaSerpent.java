package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.entity.ai.*;
import com.github.alexthe666.iceandfire.entity.util.*;
import com.github.alexthe666.iceandfire.enums.EnumSeaSerpent;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import com.google.common.base.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.IPlantable;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public class EntitySeaSerpent extends Animal implements IAnimatedEntity, IMultipartEntity, IVillagerFear, IAnimalFear, IHasCustomizableAttributes {

    public static final Animation ANIMATION_BITE = Animation.create(15);
    public static final Animation ANIMATION_SPEAK = Animation.create(15);
    public static final Animation ANIMATION_ROAR = Animation.create(40);
    public static final int TIME_BETWEEN_ROARS = 300;
    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(EntitySeaSerpent.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Float> SCALE = SynchedEntityData.defineId(EntitySeaSerpent.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Boolean> JUMPING = SynchedEntityData.defineId(EntitySeaSerpent.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> BREATHING = SynchedEntityData.defineId(EntitySeaSerpent.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> ANCIENT = SynchedEntityData.defineId(EntitySeaSerpent.class, EntityDataSerializers.BOOLEAN);
    private static final Predicate<Entity> NOT_SEA_SERPENT = new Predicate<Entity>() {
        @Override
        public boolean apply(@Nullable Entity entity) {
            return entity instanceof LivingEntity && !(entity instanceof EntitySeaSerpent) && DragonUtils.isAlive((LivingEntity) entity);
        }
    };
    private static final Predicate<Entity> NOT_SEA_SERPENT_IN_WATER = new Predicate<Entity>() {
        @Override
        public boolean apply(@Nullable Entity entity) {
            return entity instanceof LivingEntity && !(entity instanceof EntitySeaSerpent) && DragonUtils.isAlive((LivingEntity) entity) && entity.isInWaterOrBubble();
        }
    };
    public int swimCycle;
    public float jumpProgress = 0.0F;
    public float wantJumpProgress = 0.0F;
    public float jumpRot = 0.0F;
    public float prevJumpRot = 0.0F;
    public float breathProgress = 0.0F;
    //true  = melee, false = ranged
    public boolean attackDecision = false;
    private int animationTick;
    private Animation currentAnimation;
    private EntityMutlipartPart[] segments = new EntityMutlipartPart[9];
    private float lastScale;
    private boolean isLandNavigator;
    private boolean changedSwimBehavior = false;
    public int jumpCooldown = 0;
    private int ticksSinceRoar = 0;
    private boolean isBreathing;
    private final float[] tailYaw = new float[5];
    private final float[] prevTailYaw = new float[5];
    private final float[] tailPitch = new float[5];
    private final float[] prevTailPitch = new float[5];

    public EntitySeaSerpent(EntityType<EntitySeaSerpent> t, Level worldIn) {
        super(t, worldIn);
        switchNavigator(false);
        this.noCulling = true;
        resetParts(1.0F);
        this.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
    }

    private static BlockPos clampBlockPosToWater(Entity entity, Level world, BlockPos pos) {
        BlockPos topY = new BlockPos(pos.getX(), entity.getBlockY(), pos.getZ());
        BlockPos bottomY = new BlockPos(pos.getX(), entity.getBlockY(), pos.getZ());
        while (isWaterBlock(world, topY) && topY.getY() < world.getMaxBuildHeight()) {
            topY = topY.above();
        }
        while (isWaterBlock(world, bottomY) && bottomY.getY() > 0) {
            bottomY = bottomY.below();
        }
        return new BlockPos(pos.getX(), Mth.clamp(pos.getY(), bottomY.getY() + 1, topY.getY() - 1), pos.getZ());
    }

    public static boolean isWaterBlock(Level world, BlockPos pos) {
        return world.getFluidState(pos).is(FluidTags.WATER);
    }

    @Override
    public @NotNull SoundSource getSoundSource() {
        return SoundSource.HOSTILE;
    }

    @Override
    public boolean shouldRender(double x, double y, double z) {
        return true;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new SeaSerpentAIGetInWater(this));
        this.goalSelector.addGoal(1, new SeaSerpentAIMeleeJump(this));
        this.goalSelector.addGoal(1, new SeaSerpentAIAttackMelee(this, 1.0D, true));
        this.goalSelector.addGoal(2, new SeaSerpentAIRandomSwimming(this, 1.0D, 2));
        this.goalSelector.addGoal(3, new SeaSerpentAIJump(this, 4));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, EntityMutlipartPart.class)).setAlertOthers());
        this.targetSelector.addGoal(2, new FlyingAITarget(this, LivingEntity.class, 150, false, false, NOT_SEA_SERPENT_IN_WATER));
        this.targetSelector.addGoal(3, new FlyingAITarget(this, Player.class, 0, false, false, NOT_SEA_SERPENT));
    }

    @Override
    public int getExperienceReward() {
        return this.isAncient() ? 30 : 15;
    }

    @Override
    public void pushEntities() {
        List<Entity> entities = this.level().getEntities(this, this.getBoundingBox().expandTowards(0.20000000298023224D, 0.0D, 0.20000000298023224D));
        entities.stream().filter(entity -> !(entity instanceof EntityMutlipartPart) && entity.isPushable()).forEach(entity -> entity.push(this));
    }

    private void switchNavigator(boolean onLand) {
        if (onLand) {
            this.moveControl = new MoveControl(this);
            this.navigation = new GroundPathNavigation(this, level());
            this.navigation.setCanFloat(true);
            this.isLandNavigator = true;
        } else {
            this.moveControl = new EntitySeaSerpent.SwimmingMoveHelper(this);
            this.navigation = new SeaSerpentPathNavigator(this, level());
            this.isLandNavigator = false;
        }
    }

    public boolean isDirectPathBetweenPoints(BlockPos pos) {
        Vec3 vector3d = new Vec3(this.getX(), this.getEyeY(), this.getZ());
        Vec3 bector3d1 = new Vec3(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D);
        return this.level().clip(new ClipContext(vector3d, bector3d1, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this)).getType() == HitResult.Type.MISS;

    }

    @Override
    public @NotNull MobType getMobType() {
        return MobType.WATER;
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Mob.createMobAttributes()
            //HEALTH
            .add(Attributes.MAX_HEALTH, IafConfig.seaSerpentBaseHealth)
            //SPEED
            .add(Attributes.MOVEMENT_SPEED, 0.15D)
            //ATTACK
            .add(Attributes.ATTACK_DAMAGE, 1.0D)
            //FALLOW RANGE
            .add(Attributes.FOLLOW_RANGE, Math.min(2048, IafConfig.dragonTargetSearchLength))
            //ARMOR
            .add(Attributes.ARMOR, 3.0D);
    }


    @Override
    public void setConfigurableAttributes() {
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(IafConfig.seaSerpentBaseHealth);
        this.getAttribute(Attributes.FOLLOW_RANGE).setBaseValue(Math.min(2048, IafConfig.dragonTargetSearchLength));
        this.updateAttributes();
    }

    public void resetParts(float scale) {
        clearParts();
        segments = new EntityMutlipartPart[9];
        for (int i = 0; i < segments.length; i++) {
            if (i > 3) {
                Entity parentToSet = i <= 4 ? this : segments[i-1];
                segments[i] = new EntitySlowPart(parentToSet, 0.5F * scale, 180, 0, 0.5F * scale, 0.5F * scale, 1);
            } else {
                Entity parentToSet = i == 0 ? this : segments[i-1];
                segments[i] = new EntitySlowPart(parentToSet, -0.4F * scale, 180, 0, 0.45F * scale, 0.4F * scale, 1);
            }
            segments[i].copyPosition(this);
        }
    }

    public void onUpdateParts() {
        for (EntityMutlipartPart entity : segments) {
            EntityUtil.updatePart(entity, this);
        }
    }

    private void clearParts() {
        for (EntityMutlipartPart entity : segments) {
            if (entity != null) {
                entity.remove(RemovalReason.DISCARDED);
            }
        }
    }

    @Override
    public void remove(@NotNull RemovalReason reason) {
        clearParts();
        super.remove(reason);
    }

    @Override
    public @NotNull EntityDimensions getDimensions(@NotNull Pose poseIn) {
        return this.getType().getDimensions().scale(this.getScale());
    }

    @Override
    public float getScale() {
        return this.getSeaSerpentScale();
    }

    @Override
    public void refreshDimensions() {
        super.refreshDimensions();
        float scale = this.getSeaSerpentScale();
        if (scale != lastScale) {
            resetParts(this.getSeaSerpentScale());
        }
        lastScale = scale;
    }


    @Override
    public boolean doHurtTarget(@NotNull Entity entityIn) {
        if (this.getAnimation() != ANIMATION_BITE) {
            this.setAnimation(ANIMATION_BITE);
            return true;
        }
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        if(jumpCooldown > 0){
            jumpCooldown--;
        }
        refreshDimensions();
        onUpdateParts();
        if (this.isInWater()) {
            spawnParticlesAroundEntity(ParticleTypes.BUBBLE, this, (int) this.getSeaSerpentScale());

        }
        if (!this.level().isClientSide && this.level().getDifficulty() == Difficulty.PEACEFUL) {
            this.remove(RemovalReason.DISCARDED);
        }
        if (this.getTarget() != null && !this.getTarget().isAlive()) {
            this.setTarget(null);
        }
        for (int i = 0; i < tailYaw.length; i++) {
            prevTailYaw[i] = tailYaw[i];
        }
        for (int i = 0; i < tailPitch.length; i++) {
            prevTailPitch[i] = tailPitch[i];
        }
        this.tailYaw[0] = this.yBodyRot;
        this.tailPitch[0] = this.getXRot();
        for (int i = 1; i < tailYaw.length; i++) {
            tailYaw[i] = prevTailYaw[i - 1];
        }
        for (int i = 1; i < tailPitch.length; i++) {
            tailPitch[i] = prevTailPitch[i - 1];
        }
        AnimationHandler.INSTANCE.updateAnimations(this);
    }

    public float getPieceYaw(int index, float partialTicks){
        if(index < segments.length && index >= 0){
            return prevTailYaw[index] + (tailYaw[index] - prevTailYaw[index]) * partialTicks;
        }
        return 0;
    }

    public float getPiecePitch(int index, float partialTicks){
        if(index < segments.length && index >= 0){
            return prevTailPitch[index] + (tailPitch[index] - prevTailPitch[index]) * partialTicks;
        }
        return 0;
    }


    private void spawnParticlesAroundEntity(ParticleOptions type, Entity entity, int count) {
        for (int i = 0; i < count; i++) {
            int x = (int) Math.round(entity.getX() + this.random.nextFloat() * entity.getBbWidth() * 2.0F - entity.getBbWidth());
            int y = (int) Math.round(entity.getY() + 0.5D + this.random.nextFloat() * entity.getBbHeight());
            int z = (int) Math.round(entity.getZ() + this.random.nextFloat() * entity.getBbWidth() * 2.0F - entity.getBbWidth());
            if (this.level().getBlockState(new BlockPos(x, y, z)).is(Blocks.WATER)) {
                this.level().addParticle(type, x, y, z, 0, 0, 0);
            }
        }
    }

    private void spawnSlamParticles(ParticleOptions type) {
        for (int i = 0; i < this.getSeaSerpentScale() * 3; i++) {
            for (int i1 = 0; i1 < 5; i1++) {
                double motionX = getRandom().nextGaussian() * 0.07D;
                double motionY = getRandom().nextGaussian() * 0.07D;
                double motionZ = getRandom().nextGaussian() * 0.07D;
                float radius = 1.25F * getSeaSerpentScale();
                float angle = (0.01745329251F * this.yBodyRot) + i1 * 1F;
                double extraX = radius * Mth.sin((float) (Math.PI + angle));
                double extraY = 0.8F;
                double extraZ = radius * Mth.cos(angle);
                if (level().isClientSide) {
                    level().addParticle(type, true, this.getX() + extraX, this.getY() + extraY, this.getZ() + extraZ, motionX, motionY, motionZ);
                }
            }
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(VARIANT, 0);
        this.entityData.define(SCALE, 0F);
        this.entityData.define(JUMPING, false);
        this.entityData.define(BREATHING, false);
        this.entityData.define(ANCIENT, false);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Variant", this.getVariant());
        compound.putInt("TicksSinceRoar", ticksSinceRoar);
        compound.putInt("JumpCooldown", jumpCooldown);
        compound.putFloat("Scale", this.getSeaSerpentScale());
        compound.putBoolean("JumpingOutOfWater", this.isJumpingOutOfWater());
        compound.putBoolean("AttackDecision", attackDecision);
        compound.putBoolean("Breathing", this.isBreathing());
        compound.putBoolean("Ancient", this.isAncient());
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setVariant(compound.getInt("Variant"));
        ticksSinceRoar = compound.getInt("TicksSinceRoar");
        jumpCooldown = compound.getInt("JumpCooldown");
        this.setSeaSerpentScale(compound.getFloat("Scale"));
        this.setJumpingOutOfWater(compound.getBoolean("JumpingOutOfWater"));
        attackDecision = compound.getBoolean("AttackDecision");
        this.setBreathing(compound.getBoolean("Breathing"));
        this.setAncient(compound.getBoolean("Ancient"));
        this.setConfigurableAttributes();
    }

    private void updateAttributes() {
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(Math.min(0.25D, 0.15D * this.getSeaSerpentScale() * this.getAncientModifier()));
        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(Math.max(4, IafConfig.seaSerpentAttackStrength * this.getSeaSerpentScale() * this.getAncientModifier()));
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(Math.max(10, IafConfig.seaSerpentBaseHealth * this.getSeaSerpentScale() * this.getAncientModifier()));
        this.getAttribute(Attributes.FOLLOW_RANGE).setBaseValue(Math.min(2048, IafConfig.dragonTargetSearchLength));
        this.heal(30F * this.getSeaSerpentScale());
    }

    private float getAncientModifier() {
        return this.isAncient() ? 1.5F : 1.0F;
    }

    public float getSeaSerpentScale() {
        return this.entityData.get(SCALE).floatValue();
    }

    private void setSeaSerpentScale(float scale) {
        this.entityData.set(SCALE, scale);
    }

    public int getVariant() {
        return this.entityData.get(VARIANT).intValue();
    }

    public void setVariant(int variant) {
        this.entityData.set(VARIANT, variant);
    }

    public boolean isJumpingOutOfWater() {
        return this.entityData.get(JUMPING).booleanValue();
    }

    public void setJumpingOutOfWater(boolean jump) {
        this.entityData.set(JUMPING, jump);
    }

    public boolean isAncient() {
        return this.entityData.get(ANCIENT).booleanValue();
    }

    public void setAncient(boolean ancient) {
        this.entityData.set(ANCIENT, ancient);
    }

    public boolean isBreathing() {
        if (level().isClientSide) {
            boolean breathing = this.entityData.get(BREATHING).booleanValue();
            this.isBreathing = breathing;
            return breathing;
        }
        return isBreathing;
    }

    public void setBreathing(boolean breathing) {
        this.entityData.set(BREATHING, breathing);
        if (!level().isClientSide) {
            this.isBreathing = breathing;
        }
    }

    @Override
    protected void checkFallDamage(double y, boolean onGroundIn, @NotNull BlockState state, @NotNull BlockPos pos) {
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!level().isClientSide) {
            if (level().getDifficulty() == Difficulty.PEACEFUL && this.getTarget() instanceof Player) {
                this.setTarget(null);
            }
        }
        boolean breathing = isBreathing() && this.getAnimation() != ANIMATION_BITE && this.getAnimation() != ANIMATION_ROAR;
        boolean jumping = !this.isInWater() && !this.onGround() && this.getDeltaMovement().y >= 0;
        boolean wantJumping = false; //(ticksSinceJump > TIME_BETWEEN_JUMPS) && this.isInWater();
        boolean ground = !isInWater() && this.onGround();
        boolean prevJumping = this.isJumpingOutOfWater();
        this.ticksSinceRoar++;
        this.jumpCooldown++;
        this.prevJumpRot = jumpRot;
        if (this.ticksSinceRoar > TIME_BETWEEN_ROARS && isAtSurface() && this.getAnimation() != ANIMATION_BITE && jumpProgress == 0 && !isJumpingOutOfWater()) {
            this.setAnimation(ANIMATION_ROAR);
            this.ticksSinceRoar = 0;
        }
        if (this.getAnimation() == ANIMATION_ROAR && this.getAnimationTick() == 1) {
            this.playSound(IafSoundRegistry.SEA_SERPENT_ROAR, this.getSoundVolume() + 1, 1);
        }
        if (this.getAnimation() == ANIMATION_BITE && this.getAnimationTick() == 5) {
            this.playSound(IafSoundRegistry.SEA_SERPENT_BITE, this.getSoundVolume(), 1);
        }
        if (isJumpingOutOfWater() && isWaterBlock(level(), this.blockPosition().above(2))) {
            setJumpingOutOfWater(false);
        }
        if (this.swimCycle < 38) {
            this.swimCycle += 2;
        } else {
            this.swimCycle = 0;
        }
        if (breathing && breathProgress < 20.0F) {
            breathProgress += 0.5F;
        } else if (!breathing && breathProgress > 0.0F) {
            breathProgress -= 0.5F;
        }
        if (jumping && jumpProgress < 10.0F) {
            jumpProgress += 0.5F;
        } else if (!jumping && jumpProgress > 0.0F) {
            jumpProgress -= 0.5F;
        }
        if (wantJumping && wantJumpProgress < 10.0F) {
            wantJumpProgress += 2F;
        } else if (!wantJumping && wantJumpProgress > 0.0F) {
            wantJumpProgress -= 2F;
        }
        if (this.isJumpingOutOfWater() && jumpRot < 1.0F) {
            jumpRot += 0.1F;
        } else if (!this.isJumpingOutOfWater() && jumpRot > 0.0F) {
            jumpRot -= 0.1F;
        }
        if (prevJumping && !this.isJumpingOutOfWater()) {
            this.playSound(IafSoundRegistry.SEA_SERPENT_SPLASH, 5F, 0.75F);
            spawnSlamParticles(ParticleTypes.BUBBLE);
            this.doSplashDamage();
        }
        if (!ground && this.isLandNavigator) {
            switchNavigator(false);
        }
        if (ground && !this.isLandNavigator) {
            switchNavigator(true);
        }
        setXRot(Mth.clamp((float) this.getDeltaMovement().y * 20F, -90, 90));
        if (changedSwimBehavior) {
            changedSwimBehavior = false;
        }
        if (!level().isClientSide) {
            if (attackDecision) {
                this.setBreathing(false);
            }
            if (this.getTarget() != null && this.getAnimation() != ANIMATION_ROAR) {
                if (!attackDecision) {
                    if (!this.getTarget().isInWater() || !this.hasLineOfSight(this.getTarget()) || this.distanceTo(this.getTarget()) < 30 * this.getSeaSerpentScale()) {
                        attackDecision = true;
                    }
                    if (!attackDecision) {
                        shoot(this.getTarget());
                    }
                } else {
                    if (this.distanceToSqr(this.getTarget()) > 200 * this.getSeaSerpentScale()) {
                        attackDecision = false;
                    }
                }
            } else {
                this.setBreathing(false);
            }
        }
        if (this.getAnimation() == ANIMATION_BITE && this.getTarget() != null && (this.isTouchingMob(this.getTarget()) || this.distanceToSqr(this.getTarget()) < 50)) {
            this.hurtMob(this.getTarget());
        }
        breakBlock();
        if (!level().isClientSide && this.isPassenger() && this.getRootVehicle() instanceof Boat) {
            Boat boat = (Boat) this.getRootVehicle();
            boat.remove(RemovalReason.KILLED);
            this.stopRiding();
        }
    }

    private boolean isAtSurface() {
        BlockPos pos = this.blockPosition();
        return isWaterBlock(level(), pos.below()) && !isWaterBlock(level(), pos.above());
    }

    private void doSplashDamage() {
        double getWidth = 2D * this.getSeaSerpentScale();
        List<Entity> list = level().getEntities(this, this.getBoundingBox().inflate(getWidth, getWidth * 0.5D, getWidth), NOT_SEA_SERPENT);
        for (Entity entity : list) {
            if (entity instanceof LivingEntity && DragonUtils.isAlive((LivingEntity) entity)) {
                entity.hurt(this.level().damageSources().mobAttack(this), ((int) this.getAttribute(Attributes.ATTACK_DAMAGE).getValue()));
                destroyBoat(entity);
                double xRatio = this.getX() - entity.getX();
                double zRatio = this.getZ() - entity.getZ();
                float f = Mth.sqrt((float) (xRatio * xRatio + zRatio * zRatio));
                float strength = 0.3F * this.getSeaSerpentScale();
                entity.setDeltaMovement(entity.getDeltaMovement().multiply(0.5D, 1D, 0.5D));
                entity.setDeltaMovement(entity.getDeltaMovement().add(xRatio / f * strength, strength, zRatio / f * strength));
            }
        }

    }

    public void destroyBoat(Entity sailor) {
        if (sailor.getVehicle() != null && sailor.getVehicle() instanceof Boat && !level().isClientSide) {
            Boat boat = (Boat) sailor.getVehicle();
            boat.remove(RemovalReason.KILLED);
            if (this.level().getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
                for (int i = 0; i < 3; ++i) {
                    boat.spawnAtLocation(new ItemStack(boat.getVariant().getPlanks().asItem()), 0.0F);
                }
                for (int j = 0; j < 2; ++j) {
                    boat.spawnAtLocation(new ItemStack(Items.STICK));
                }
            }
        }
    }

    private boolean isPreyAtSurface() {
        if (this.getTarget() != null) {
            BlockPos pos = this.getTarget().blockPosition();
            return !isWaterBlock(level(), pos.above((int) Math.ceil(this.getTarget().getBbHeight())));
        }
        return false;
    }

    private void hurtMob(LivingEntity entity) {
        if (this.getAnimation() == ANIMATION_BITE && entity != null && this.getAnimationTick() == 6) {
            this.getTarget().hurt(this.level().damageSources().mobAttack(this), ((int) this.getAttribute(Attributes.ATTACK_DAMAGE).getValue()));
            EntitySeaSerpent.this.attackDecision = this.getRandom().nextBoolean();
        }
    }

    public void moveJumping() {
        float velocity = 0.5F;
        double x = -Mth.sin(this.getYRot() * 0.017453292F) * Mth.cos(this.getXRot() * 0.017453292F);
        double z = Mth.cos(this.getYRot() * 0.017453292F) * Mth.cos(this.getXRot() * 0.017453292F);
        float f = Mth.sqrt((float) (x * x + z * z));
        x = x / f;
        z = z / f;
        x = x * velocity;
        z = z * velocity;
        this.setDeltaMovement(x, this.getDeltaMovement().y, z);
    }

    public boolean isTouchingMob(Entity entity) {
        if (this.getBoundingBox().expandTowards(1, 1, 1).intersects(entity.getBoundingBox())) {
            return true;
        }
        for (Entity segment : segments) {
            if (segment.getBoundingBox().expandTowards(1, 1, 1).intersects(entity.getBoundingBox())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    public void breakBlock() {
        if (IafConfig.seaSerpentGriefing) {
            for (int a = (int) Math.round(this.getBoundingBox().minX) - 2; a <= (int) Math.round(this.getBoundingBox().maxX) + 2; a++) {
                for (int b = (int) Math.round(this.getBoundingBox().minY) - 1; (b <= (int) Math.round(this.getBoundingBox().maxY) + 2) && (b <= 127); b++) {
                    for (int c = (int) Math.round(this.getBoundingBox().minZ) - 2; c <= (int) Math.round(this.getBoundingBox().maxZ) + 2; c++) {
                        BlockPos pos = new BlockPos(a, b, c);
                        BlockState state = level().getBlockState(pos);
                        FluidState fluidState = level().getFluidState(pos);
                        Block block = state.getBlock();
                        if (!state.isAir() && !state.getShape(level(), pos).isEmpty() && (state.getBlock() instanceof IPlantable || state.getBlock() instanceof LeavesBlock) && fluidState.isEmpty()) {
                            if (block != Blocks.AIR) {
                                if (!level().isClientSide) {
                                    level().destroyBlock(pos, true);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    @Nullable
    public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor worldIn, @NotNull DifficultyInstance difficultyIn, @NotNull MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        spawnDataIn = super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
        this.setVariant(this.getRandom().nextInt(7));
        boolean ancient = this.getRandom().nextInt(16) == 1;
        if (ancient) {
            this.setAncient(true);
            this.setSeaSerpentScale(6.0F + this.getRandom().nextFloat() * 3.0F);

        } else {
            this.setSeaSerpentScale(1.5F + this.getRandom().nextFloat() * 4.0F);
        }
        this.updateAttributes();
        return spawnDataIn;
    }

    public void onWorldSpawn(RandomSource random) {
        this.setVariant(random.nextInt(7));
        boolean ancient = random.nextInt(15) == 1;
        if (ancient) {
            this.setAncient(true);
            this.setSeaSerpentScale(6.0F + random.nextFloat() * 3.0F);

        } else {
            this.setSeaSerpentScale(1.5F + random.nextFloat() * 4.0F);
        }
        this.updateAttributes();
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
    public Animation[] getAnimations() {
        return new Animation[]{ANIMATION_BITE, ANIMATION_ROAR, ANIMATION_SPEAK};
    }

    @Override
    @Nullable
    protected SoundEvent getAmbientSound() {
        return IafSoundRegistry.SEA_SERPENT_IDLE;
    }

    @Override
    @Nullable
    protected SoundEvent getHurtSound(@NotNull DamageSource source) {
        return IafSoundRegistry.SEA_SERPENT_HURT;
    }

    @Override
    @Nullable
    protected SoundEvent getDeathSound() {
        return IafSoundRegistry.SEA_SERPENT_DIE;
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
    public boolean shouldAnimalsFear(Entity entity) {
        return true;
    }

    public boolean isBlinking() {
        return this.tickCount % 50 > 43;
    }

    private void shoot(LivingEntity entity) {
        if (!this.attackDecision) {
            if (!this.isInWater()) {
                this.setBreathing(false);
                this.attackDecision = true;
            }
            if (this.isBreathing()) {
                if (this.tickCount % 40 == 0) {
                    this.playSound(IafSoundRegistry.SEA_SERPENT_BREATH, 4, 1);
                }
                if (this.tickCount % 10 == 0) {
                    setYRot(yBodyRot);
                    float f1 = 0;
                    float f2 = 0;
                    float f3 = 0;
                    float headPosX = f1 + (float) (this.segments[0].getX() + 1.3F * getSeaSerpentScale() * Mth.cos((float) ((getYRot() + 90) * Math.PI / 180)));
                    float headPosZ = f2 + (float) (this.segments[0].getZ() + 1.3F * getSeaSerpentScale() * Mth.sin((float) ((getYRot() + 90) * Math.PI / 180)));
                    float headPosY = f3 + (float) (this.segments[0].getY() + 0.2F * getSeaSerpentScale());
                    double d2 = entity.getX() - headPosX;
                    double d3 = entity.getY() - headPosY;
                    double d4 = entity.getZ() - headPosZ;
                    float inaccuracy = 1.0F;
                    d2 = d2 + this.random.nextGaussian() * 0.007499999832361937D * inaccuracy;
                    d3 = d3 + this.random.nextGaussian() * 0.007499999832361937D * inaccuracy;
                    d4 = d4 + this.random.nextGaussian() * 0.007499999832361937D * inaccuracy;
                    EntitySeaSerpentBubbles entitylargefireball = new EntitySeaSerpentBubbles(
                        IafEntityRegistry.SEA_SERPENT_BUBBLES.get(), level(), this, d2, d3, d4);
                    entitylargefireball.setPos(headPosX, headPosY, headPosZ);
                    if (!level().isClientSide) {
                        level().addFreshEntity(entitylargefireball);
                    }
                    if (!entity.isAlive() || entity == null) {
                        this.setBreathing(false);
                        this.attackDecision = this.getRandom().nextBoolean();
                    }
                }
            } else {
                this.setBreathing(true);
            }
        }
        this.lookAt(entity, 360, 360);
    }

    public EnumSeaSerpent getEnum() {
        switch (this.getVariant()) {
            default:
                return EnumSeaSerpent.BLUE;
            case 1:
                return EnumSeaSerpent.BRONZE;
            case 2:
                return EnumSeaSerpent.DEEPBLUE;
            case 3:
                return EnumSeaSerpent.GREEN;
            case 4:
                return EnumSeaSerpent.PURPLE;
            case 5:
                return EnumSeaSerpent.RED;
            case 6:
                return EnumSeaSerpent.TEAL;
        }
    }

    @Override
    public void travel(@NotNull Vec3 vec) {
        if (this.isEffectiveAi() && this.isInWater()) {
            this.moveRelative(this.getSpeed(), vec);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.9D));
            if (this.getTarget() == null) {
                this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.005D, 0.0D));
            }
        } else {
            super.travel(vec);
        }
    }

    @Override
    public boolean killedEntity(@NotNull ServerLevel world, @NotNull LivingEntity entity) {
        this.attackDecision = this.getRandom().nextBoolean();
        return attackDecision;
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
    public int getMaxFallDistance() {
        return 1000;
    }

    public void onJumpHit(LivingEntity target) {
    }

    public boolean shouldUseJumpAttack(LivingEntity attackTarget) {
        return !attackTarget.isInWater() || isPreyAtSurface();
    }

    @Override
    public boolean isInvulnerableTo(@NotNull DamageSource source) {
        DamageSources damageSources = this.level().damageSources();
        return source == damageSources.fall() || source == damageSources.drown() || source == damageSources.inWall()
                || (source.getEntity() != null && source == damageSources.fallingBlock(source.getEntity()))
                || source == damageSources.lava() || source.is(DamageTypes.IN_FIRE) || super.isInvulnerableTo(source);
    }

    public class SwimmingMoveHelper extends MoveControl {
        private final EntitySeaSerpent dolphin;

        public SwimmingMoveHelper(EntitySeaSerpent dolphinIn) {
            super(dolphinIn);
            this.dolphin = dolphinIn;
        }

        @Override
        public void tick() {
            if (this.dolphin.isInWater()) {
                this.dolphin.setDeltaMovement(this.dolphin.getDeltaMovement().add(0.0D, 0.005D, 0.0D));
            }

            if (this.operation == MoveControl.Operation.MOVE_TO && !this.dolphin.getNavigation().isDone()) {
                double d0 = this.wantedX - this.dolphin.getX();
                double d1 = this.wantedY - this.dolphin.getY();
                double d2 = this.wantedZ - this.dolphin.getZ();
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;
                if (d3 < 2.5000003E-7F) {
                    this.mob.setZza(0.0F);
                } else {
                    float f = (float) (Mth.atan2(d2, d0) * (180F / (float) Math.PI)) - 90.0F;
                    this.dolphin.setYRot(this.rotlerp(this.dolphin.getYRot(), f, 10.0F));
                    this.dolphin.yBodyRot = this.dolphin.getYRot();
                    this.dolphin.yHeadRot = this.dolphin.getYRot();
                    float f1 = (float) (this.speedModifier * 3);
                    if (this.dolphin.isInWater()) {
                        this.dolphin.setSpeed(f1 * 0.02F);
                        float f2 = -((float) (Mth.atan2(d1, Mth.sqrt((float) (d0 * d0 + d2 * d2))) * (180F / (float) Math.PI)));
                        f2 = Mth.clamp(Mth.wrapDegrees(f2), -85.0F, 85.0F);
                        this.dolphin.setDeltaMovement(this.dolphin.getDeltaMovement().add(0.0D, this.dolphin.getSpeed() * d1 * 0.6D, 0.0D));
                        this.dolphin.setXRot(this.rotlerp(this.dolphin.getXRot(), f2, 1.0F));
                        float f3 = Mth.cos(this.dolphin.getXRot() * ((float) Math.PI / 180F));
                        float f4 = Mth.sin(this.dolphin.getXRot() * ((float) Math.PI / 180F));
                        this.dolphin.zza = f3 * f1;
                        this.dolphin.yya = -f4 * f1;
                    } else {
                        this.dolphin.setSpeed(f1 * 0.1F);
                    }

                }
            } else {
                this.dolphin.setSpeed(0.0F);
                this.dolphin.setXxa(0.0F);
                this.dolphin.setYya(0.0F);
                this.dolphin.setZza(0.0F);
            }
        }
    }
}
