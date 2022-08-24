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
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.*;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class EntitySeaSerpent extends AnimalEntity implements IAnimatedEntity, IMultipartEntity, IVillagerFear, IAnimalFear, IHasCustomizableAttributes {

    public static final Animation ANIMATION_BITE = Animation.create(15);
    public static final Animation ANIMATION_SPEAK = Animation.create(15);
    public static final Animation ANIMATION_ROAR = Animation.create(40);
    public static final int TIME_BETWEEN_ROARS = 300;
    private static final DataParameter<Integer> VARIANT = EntityDataManager.defineId(EntitySeaSerpent.class, DataSerializers.INT);
    private static final DataParameter<Float> SCALE = EntityDataManager.defineId(EntitySeaSerpent.class, DataSerializers.FLOAT);
    private static final DataParameter<Boolean> JUMPING = EntityDataManager.defineId(EntitySeaSerpent.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> BREATHING = EntityDataManager.defineId(EntitySeaSerpent.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> ANCIENT = EntityDataManager.defineId(EntitySeaSerpent.class, DataSerializers.BOOLEAN);
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

    public EntitySeaSerpent(EntityType<EntitySeaSerpent> t, World worldIn) {
        super(t, worldIn);
        IHasCustomizableAttributes.applyAttributesForEntity(t, this);
        switchNavigator(false);
        this.noCulling = true;
        resetParts(1.0F);
        this.setPathfindingMalus(PathNodeType.WATER, 0.0F);
    }

    private static BlockPos clampBlockPosToWater(Entity entity, World world, BlockPos pos) {
        BlockPos topY = new BlockPos(pos.getX(), entity.getY(), pos.getZ());
        BlockPos bottomY = new BlockPos(pos.getX(), entity.getY(), pos.getZ());
        while (isWaterBlock(world, topY) && topY.getY() < world.getMaxBuildHeight()) {
            topY = topY.above();
        }
        while (isWaterBlock(world, bottomY) && bottomY.getY() > 0) {
            bottomY = bottomY.below();
        }
        return new BlockPos(pos.getX(), MathHelper.clamp(pos.getY(), bottomY.getY() + 1, topY.getY() - 1), pos.getZ());
    }

    public static boolean isWaterBlock(World world, BlockPos pos) {
        return world.getFluidState(pos).is(FluidTags.WATER);
    }

    @Override
    public SoundCategory getSoundSource() {
        return SoundCategory.HOSTILE;
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
        this.goalSelector.addGoal(4, new LookRandomlyGoal(this));
        this.goalSelector.addGoal(5, new LookAtGoal(this, PlayerEntity.class, 6.0F));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, EntityMutlipartPart.class)).setAlertOthers());
        this.targetSelector.addGoal(2, new FlyingAITarget(this, LivingEntity.class, 150, false, false, NOT_SEA_SERPENT_IN_WATER));
        this.targetSelector.addGoal(3, new FlyingAITarget(this, PlayerEntity.class, 0, false, false, NOT_SEA_SERPENT));
    }

    @Override
    protected int getExperienceReward(PlayerEntity player) {
        return this.isAncient() ? 30 : 15;
    }

    @Override
    public void pushEntities() {
        List<Entity> entities = this.level.getEntities(this, this.getBoundingBox().expandTowards(0.20000000298023224D, 0.0D, 0.20000000298023224D));
        entities.stream().filter(entity -> !(entity instanceof EntityMutlipartPart) && entity.isPushable()).forEach(entity -> entity.push(this));
    }

    private void switchNavigator(boolean onLand) {
        if (onLand) {
            this.moveControl = new MovementController(this);
            this.navigation = new GroundPathNavigator(this, level);
            this.navigation.setCanFloat(true);
            this.isLandNavigator = true;
        } else {
            this.moveControl = new EntitySeaSerpent.SwimmingMoveHelper(this);
            this.navigation = new SeaSerpentPathNavigator(this, level);
            this.isLandNavigator = false;
        }
    }

    public boolean isDirectPathBetweenPoints(BlockPos pos) {
        Vector3d vector3d = new Vector3d(this.getX(), this.getEyeY(), this.getZ());
        Vector3d bector3d1 = new Vector3d(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D);
        return this.level.clip(new RayTraceContext(vector3d, bector3d1, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, this)).getType() == RayTraceResult.Type.MISS;

    }

    @Override
    public CreatureAttribute getMobType() {
        return CreatureAttribute.WATER;
    }

    public static AttributeModifierMap.MutableAttribute bakeAttributes() {
        return MobEntity.createMobAttributes()
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
    public AttributeModifierMap.MutableAttribute getConfigurableAttributes() {
        return bakeAttributes();
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
            if (entity != null) {
                if (!entity.shouldContinuePersisting()) {
                    level.addFreshEntity(entity);
                }
            }
        }
    }

    private void clearParts() {
        for (EntityMutlipartPart entity : segments) {
            if (entity != null) {
                entity.remove();
            }
        }
    }

    @Override
    public void remove() {
        clearParts();
        super.remove();
    }

    @Override
    public EntitySize getDimensions(Pose poseIn) {
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
    public boolean doHurtTarget(Entity entityIn) {
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
        if (!this.level.isClientSide && this.level.getDifficulty() == Difficulty.PEACEFUL) {
            this.remove();
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
        this.tailPitch[0] = this.xRot;
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


    private void spawnParticlesAroundEntity(IParticleData type, Entity entity, int count) {
        for (int i = 0; i < count; i++) {
            double x = entity.getX() + this.random.nextFloat() * entity.getBbWidth() * 2.0F - entity.getBbWidth();
            double y = entity.getY() + 0.5D + this.random.nextFloat() * entity.getBbHeight();
            double z = entity.getZ() + this.random.nextFloat() * entity.getBbWidth() * 2.0F - entity.getBbWidth();
            if (this.level.getBlockState(new BlockPos(x, y, z)).getMaterial() == Material.WATER) {
                this.level.addParticle(type, x, y, z, 0, 0, 0);
            }
        }
    }

    private void spawnSlamParticles(IParticleData type) {
        for (int i = 0; i < this.getSeaSerpentScale() * 3; i++) {
            for (int i1 = 0; i1 < 5; i1++) {
                double motionX = getRandom().nextGaussian() * 0.07D;
                double motionY = getRandom().nextGaussian() * 0.07D;
                double motionZ = getRandom().nextGaussian() * 0.07D;
                float radius = 1.25F * getSeaSerpentScale();
                float angle = (0.01745329251F * this.yBodyRot) + i1 * 1F;
                double extraX = radius * MathHelper.sin((float) (Math.PI + angle));
                double extraY = 0.8F;
                double extraZ = radius * MathHelper.cos(angle);
                if (level.isClientSide) {
                    level.addParticle(type, true, this.getX() + extraX, this.getY() + extraY, this.getZ() + extraZ, motionX, motionY, motionZ);
                }
            }
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(VARIANT, Integer.valueOf(0));
        this.entityData.define(SCALE, Float.valueOf(0F));
        this.entityData.define(JUMPING, false);
        this.entityData.define(BREATHING, false);
        this.entityData.define(ANCIENT, false);
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT compound) {
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
    public void readAdditionalSaveData(CompoundNBT compound) {
        super.readAdditionalSaveData(compound);
        this.setVariant(compound.getInt("Variant"));
        ticksSinceRoar = compound.getInt("TicksSinceRoar");
        jumpCooldown = compound.getInt("JumpCooldown");
        this.setSeaSerpentScale(compound.getFloat("Scale"));
        this.setJumpingOutOfWater(compound.getBoolean("JumpingOutOfWater"));
        attackDecision = compound.getBoolean("AttackDecision");
        this.setBreathing(compound.getBoolean("Breathing"));
        this.setAncient(compound.getBoolean("Ancient"));
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
        return Float.valueOf(this.entityData.get(SCALE).floatValue());
    }

    private void setSeaSerpentScale(float scale) {
        this.entityData.set(SCALE, Float.valueOf(scale));
    }

    public int getVariant() {
        return Integer.valueOf(this.entityData.get(VARIANT).intValue());
    }

    public void setVariant(int variant) {
        this.entityData.set(VARIANT, Integer.valueOf(variant));
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
        if (level.isClientSide) {
            boolean breathing = this.entityData.get(BREATHING).booleanValue();
            this.isBreathing = breathing;
            return breathing;
        }
        return isBreathing;
    }

    public void setBreathing(boolean breathing) {
        this.entityData.set(BREATHING, breathing);
        if (!level.isClientSide) {
            this.isBreathing = breathing;
        }
    }

    @Override
    protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!level.isClientSide) {
            if (level.getDifficulty() == Difficulty.PEACEFUL && this.getTarget() instanceof PlayerEntity) {
                this.setTarget(null);
            }
        }
        boolean breathing = isBreathing() && this.getAnimation() != ANIMATION_BITE && this.getAnimation() != ANIMATION_ROAR;
        boolean jumping = !this.isInWater() && !this.isOnGround() && this.getDeltaMovement().y >= 0;
        boolean wantJumping = false; //(ticksSinceJump > TIME_BETWEEN_JUMPS) && this.isInWater();
        boolean ground = !isInWater() && this.onGround;
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
        if (isJumpingOutOfWater() && isWaterBlock(level, this.blockPosition().above(2))) {
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
        xRot = MathHelper.clamp((float) this.getDeltaMovement().y * 20F, -90, 90);
        if (changedSwimBehavior) {
            changedSwimBehavior = false;
        }
        if (!level.isClientSide) {
            if (attackDecision) {
                this.setBreathing(false);
            }
            if (this.getTarget() != null && this.getAnimation() != ANIMATION_ROAR) {
                if (!attackDecision) {
                    if (!this.getTarget().isInWater() || !this.canSee(this.getTarget()) || this.distanceTo(this.getTarget()) < 30 * this.getSeaSerpentScale()) {
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
        if (!level.isClientSide && this.isPassenger() && this.getRootVehicle() instanceof BoatEntity) {
            BoatEntity boat = (BoatEntity) this.getRootVehicle();
            boat.remove();
            this.stopRiding();
        }
    }

    private boolean isAtSurface() {
        BlockPos pos = this.blockPosition();
        return isWaterBlock(level, pos.below()) && !isWaterBlock(level, pos.above());
    }

    private void doSplashDamage() {
        double getWidth = 2D * this.getSeaSerpentScale();
        List<Entity> list = level.getEntities(this, this.getBoundingBox().inflate(getWidth, getWidth * 0.5D, getWidth), NOT_SEA_SERPENT);
        for (Entity entity : list) {
            if (entity instanceof LivingEntity && DragonUtils.isAlive((LivingEntity) entity)) {
                entity.hurt(DamageSource.mobAttack(this), ((int) this.getAttribute(Attributes.ATTACK_DAMAGE).getValue()));
                destroyBoat(entity);
                double xRatio = this.getX() - entity.getX();
                double zRatio = this.getZ() - entity.getZ();
                float f = MathHelper.sqrt(xRatio * xRatio + zRatio * zRatio);
                float strength = 0.3F * this.getSeaSerpentScale();
                entity.setDeltaMovement(entity.getDeltaMovement().multiply(0.5D, 1D, 0.5D));
                entity.setDeltaMovement(entity.getDeltaMovement().add(xRatio / f * strength, strength, zRatio / f * strength));
            }
        }

    }

    public void destroyBoat(Entity sailor) {
        if (sailor.getVehicle() != null && sailor.getVehicle() instanceof BoatEntity && !level.isClientSide) {
            BoatEntity boat = (BoatEntity) sailor.getVehicle();
            boat.remove();
            if (this.level.getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
                for (int i = 0; i < 3; ++i) {
                    boat.spawnAtLocation(new ItemStack(boat.getBoatType().getPlanks().asItem()), 0.0F);
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
            return !isWaterBlock(level, pos.above((int) Math.ceil(this.getTarget().getBbHeight())));
        }
        return false;
    }

    private void hurtMob(LivingEntity entity) {
        if (this.getAnimation() == ANIMATION_BITE && entity != null && this.getAnimationTick() == 6) {
            this.getTarget().hurt(DamageSource.mobAttack(this), ((int) this.getAttribute(Attributes.ATTACK_DAMAGE).getValue()));
            EntitySeaSerpent.this.attackDecision = this.getRandom().nextBoolean();
        }
    }

    public void moveJumping() {
        float velocity = 0.5F;
        double x = -MathHelper.sin(this.yRot * 0.017453292F) * MathHelper.cos(this.xRot * 0.017453292F);
        double z = MathHelper.cos(this.yRot * 0.017453292F) * MathHelper.cos(this.xRot * 0.017453292F);
        float f = MathHelper.sqrt(x * x + z * z);
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
                        BlockState state = level.getBlockState(pos);
                        FluidState fluidState = level.getFluidState(pos);
                        Block block = state.getBlock();
                        if (!state.isAir() && !state.getShape(level, pos).isEmpty() && (state.getMaterial() == Material.PLANT || state.getMaterial() == Material.LEAVES) && fluidState.isEmpty()) {
                            if (block != Blocks.AIR) {
                                if (!level.isClientSide) {
                                    level.destroyBlock(pos, true);
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
    public ILivingEntityData finalizeSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
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

    public void onWorldSpawn(Random random) {
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
    protected SoundEvent getHurtSound(DamageSource source) {
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
    protected void playHurtSound(DamageSource source) {
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
                    yRot = yBodyRot;
                    float f1 = 0;
                    float f2 = 0;
                    float f3 = 0;
                    float headPosX = f1 + (float) (this.segments[0].getX() + 1.3F * getSeaSerpentScale() * MathHelper.cos((float) ((yRot + 90) * Math.PI / 180)));
                    float headPosZ = f2 + (float) (this.segments[0].getZ() + 1.3F * getSeaSerpentScale() * MathHelper.sin((float) ((yRot + 90) * Math.PI / 180)));
                    float headPosY = f3 + (float) (this.segments[0].getY() + 0.2F * getSeaSerpentScale());
                    double d2 = entity.getX() - headPosX;
                    double d3 = entity.getY() - headPosY;
                    double d4 = entity.getZ() - headPosZ;
                    float inaccuracy = 1.0F;
                    d2 = d2 + this.random.nextGaussian() * 0.007499999832361937D * inaccuracy;
                    d3 = d3 + this.random.nextGaussian() * 0.007499999832361937D * inaccuracy;
                    d4 = d4 + this.random.nextGaussian() * 0.007499999832361937D * inaccuracy;
                    EntitySeaSerpentBubbles entitylargefireball = new EntitySeaSerpentBubbles(
                        IafEntityRegistry.SEA_SERPENT_BUBBLES.get(), level, this, d2, d3, d4);
                    entitylargefireball.setPos(headPosX, headPosY, headPosZ);
                    if (!level.isClientSide) {
                        level.addFreshEntity(entitylargefireball);
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
    public void travel(Vector3d vec) {
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
    public void killed(ServerWorld world, LivingEntity entity) {
        this.attackDecision = this.getRandom().nextBoolean();
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
    public boolean isInvulnerableTo(DamageSource source) {
        return source == DamageSource.FALL || source == DamageSource.DROWN || source == DamageSource.IN_WALL || source == DamageSource.FALLING_BLOCK || source == DamageSource.LAVA || source.isFire() || super.isInvulnerableTo(source);
    }

    public class SwimmingMoveHelper extends MovementController {
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

            if (this.operation == MovementController.Action.MOVE_TO && !this.dolphin.getNavigation().isDone()) {
                double d0 = this.wantedX - this.dolphin.getX();
                double d1 = this.wantedY - this.dolphin.getY();
                double d2 = this.wantedZ - this.dolphin.getZ();
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;
                if (d3 < 2.5000003E-7F) {
                    this.mob.setZza(0.0F);
                } else {
                    float f = (float) (MathHelper.atan2(d2, d0) * (180F / (float) Math.PI)) - 90.0F;
                    this.dolphin.yRot = this.rotlerp(this.dolphin.yRot, f, 10.0F);
                    this.dolphin.yBodyRot = this.dolphin.yRot;
                    this.dolphin.yHeadRot = this.dolphin.yRot;
                    float f1 = (float) (this.speedModifier * 3);
                    if (this.dolphin.isInWater()) {
                        this.dolphin.setSpeed(f1 * 0.02F);
                        float f2 = -((float) (MathHelper.atan2(d1, MathHelper.sqrt(d0 * d0 + d2 * d2)) * (180F / (float) Math.PI)));
                        f2 = MathHelper.clamp(MathHelper.wrapDegrees(f2), -85.0F, 85.0F);
                        this.dolphin.setDeltaMovement(this.dolphin.getDeltaMovement().add(0.0D, this.dolphin.getSpeed() * d1 * 0.6D, 0.0D));
                        this.dolphin.xRot = this.rotlerp(this.dolphin.xRot, f2, 1.0F);
                        float f3 = MathHelper.cos(this.dolphin.xRot * ((float) Math.PI / 180F));
                        float f4 = MathHelper.sin(this.dolphin.xRot * ((float) Math.PI / 180F));
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
