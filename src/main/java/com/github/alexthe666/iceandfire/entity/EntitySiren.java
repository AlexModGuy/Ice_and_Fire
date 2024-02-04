package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.ai.AquaticAIGetInWater;
import com.github.alexthe666.iceandfire.entity.ai.AquaticAIGetOutOfWater;
import com.github.alexthe666.iceandfire.entity.ai.SirenAIFindWaterTarget;
import com.github.alexthe666.iceandfire.entity.ai.SirenAIWander;
import com.github.alexthe666.iceandfire.entity.props.EntityDataProvider;
import com.github.alexthe666.iceandfire.entity.util.ChainBuffer;
import com.github.alexthe666.iceandfire.entity.util.IHasCustomizableAttributes;
import com.github.alexthe666.iceandfire.entity.util.IHearsSiren;
import com.github.alexthe666.iceandfire.entity.util.IVillagerFear;
import com.github.alexthe666.iceandfire.enums.EnumParticles;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.message.MessageSirenSong;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import com.google.common.base.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.AmphibiousPathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public class EntitySiren extends Monster implements IAnimatedEntity, IVillagerFear, IHasCustomizableAttributes {

    public static final int SEARCH_RANGE = 32;
    public static final Predicate<Entity> SIREN_PREY = new Predicate<Entity>() {
        @Override
        public boolean apply(@Nullable Entity p_apply_1_) {
            return (p_apply_1_ instanceof Player && !((Player) p_apply_1_).isCreative() && !p_apply_1_.isSpectator()) || p_apply_1_ instanceof AbstractVillager || p_apply_1_ instanceof IHearsSiren;
        }
    };
    private static final EntityDataAccessor<Integer> HAIR_COLOR = SynchedEntityData.defineId(EntitySiren.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> AGGRESSIVE = SynchedEntityData.defineId(EntitySiren.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> SING_POSE = SynchedEntityData.defineId(EntitySiren.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> SINGING = SynchedEntityData.defineId(EntitySiren.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> SWIMMING = SynchedEntityData.defineId(EntitySiren.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> CHARMED = SynchedEntityData.defineId(EntitySiren.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Byte> CLIMBING = SynchedEntityData.defineId(EntitySiren.class, EntityDataSerializers.BYTE);
    public static Animation ANIMATION_BITE = Animation.create(20);
    public static Animation ANIMATION_PULL = Animation.create(20);
    public ChainBuffer tail_buffer;
    public float singProgress;
    public float swimProgress;
    public int singCooldown;
    private int animationTick;
    private Animation currentAnimation;
    private boolean isSinging;
    private boolean isSwimming;
    private boolean isLandNavigator;
    private int ticksAgressive;

    public EntitySiren(EntityType<EntitySiren> t, Level worldIn) {
        super(t, worldIn);
        this.switchNavigator(true);
        if (worldIn.isClientSide) {
            tail_buffer = new ChainBuffer();
        }
        this.setMaxUpStep(1F);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new SirenAIFindWaterTarget(this));
        this.goalSelector.addGoal(1, new AquaticAIGetInWater(this, 1.0D));
        this.goalSelector.addGoal(1, new AquaticAIGetOutOfWater(this, 1.0D));
        this.goalSelector.addGoal(2, new SirenAIWander(this, 1));
        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F, 1.0F));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal(this, Player.class, 10, true, false, new Predicate<Player>() {
            @Override
            public boolean apply(@Nullable Player entity) {
                return EntitySiren.this.isAgressive() && !(entity.isCreative() || entity.isSpectator());
            }
        }));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal(this, AbstractVillager.class, 10, true, false, new Predicate<AbstractVillager>() {
            @Override
            public boolean apply(@Nullable AbstractVillager entity) {
                return EntitySiren.this.isAgressive();
            }
        }));
    }

    public static boolean isWearingEarplugs(LivingEntity entity) {
        ItemStack helmet = entity.getItemBySlot(EquipmentSlot.HEAD);
        return helmet.getItem() == IafItemRegistry.EARPLUGS.get() || helmet != ItemStack.EMPTY && helmet.getItem().getDescriptionId().contains("earmuff");
    }

    @Override
    public int getExperienceReward() {
        return 8;
    }

    @Override
    public float getWalkTargetValue(@NotNull BlockPos pos) {
        return level().getBlockState(pos).is(Blocks.WATER) ? 10F : super.getWalkTargetValue(pos);
    }

    @Override
    public boolean doHurtTarget(@NotNull Entity entityIn) {
        if (this.getRandom().nextInt(2) == 0) {
            if (this.getAnimation() != ANIMATION_PULL) {
                this.setAnimation(ANIMATION_PULL);
                this.playSound(IafSoundRegistry.NAGA_ATTACK, 1, 1);
            }
        } else {
            if (this.getAnimation() != ANIMATION_BITE) {
                this.setAnimation(ANIMATION_BITE);
                this.playSound(IafSoundRegistry.NAGA_ATTACK, 1, 1);
            }
        }
        return true;
    }

    public boolean isDirectPathBetweenPoints(Vec3 vec1, Vec3 pos) {
        Vec3 Vector3d1 = new Vec3(pos.x() + 0.5D, pos.y() + 0.5D, pos.z() + 0.5D);
        return this.level().clip(new ClipContext(vec1, Vector3d1, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this)).getType() == HitResult.Type.MISS;
    }

    @Override
    public float getPathfindingMalus(@NotNull BlockPathTypes nodeType) {
        return nodeType == BlockPathTypes.WATER ? 0F : super.getPathfindingMalus(nodeType);
    }

    private void switchNavigator(boolean onLand) {
        if (onLand) {
            this.moveControl = new MoveControl(this);
            this.navigation = new AmphibiousPathNavigation(this, level());
            this.isLandNavigator = true;
        } else {
            this.moveControl = new EntitySiren.SwimmingMoveHelper();
            this.navigation = new WaterBoundPathNavigation(this, level());
            this.isLandNavigator = false;
        }
    }

    private boolean isPathOnHighGround() {
        if (this.navigation != null && this.navigation.getPath() != null && this.navigation.getPath().getEndNode() != null) {
            BlockPos target = new BlockPos(this.navigation.getPath().getEndNode().x, this.navigation.getPath().getEndNode().y, this.navigation.getPath().getEndNode().z);
            BlockPos siren = this.blockPosition();
            return level().isEmptyBlock(siren.above()) && level().isEmptyBlock(target.above()) && target.getY() >= siren.getY();
        }
        return false;
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        yBodyRot = getYRot();

        LivingEntity attackTarget = this.getTarget();
        if (singCooldown > 0) {
            singCooldown--;
            this.setSinging(false);
        }
        if (!level().isClientSide && attackTarget == null && !this.isAgressive()) {
            this.setSinging(true);
        }
        if (this.getAnimation() == ANIMATION_BITE && attackTarget != null && this.distanceToSqr(attackTarget) < 7D && this.getAnimationTick() == 5) {
            attackTarget.hurt(this.level().damageSources().mobAttack(this), (float) this.getAttribute(Attributes.ATTACK_DAMAGE).getValue());
        }
        if (this.getAnimation() == ANIMATION_PULL && attackTarget != null && this.distanceToSqr(attackTarget) < 16D && this.getAnimationTick() == 5) {
            attackTarget.hurt(this.level().damageSources().mobAttack(this), (float) this.getAttribute(Attributes.ATTACK_DAMAGE).getValue());
            double attackmotionX = (Math.signum(this.getX() - attackTarget.getX()) * 0.5D - attackTarget.getDeltaMovement().z) * 0.100000000372529 * 5;
            double attackmotionY = (Math.signum(this.getY() - attackTarget.getY() + 1) * 0.5D - attackTarget.getDeltaMovement().y) * 0.100000000372529 * 5;
            double attackmotionZ = (Math.signum(this.getZ() - attackTarget.getZ()) * 0.5D - attackTarget.getDeltaMovement().z) * 0.100000000372529 * 5;

            attackTarget.setDeltaMovement(attackTarget.getDeltaMovement().add(attackmotionX, attackmotionY, attackmotionZ));
            // float angle = (float) (Math.atan2(attackTarget.getDeltaMovement().z, attackTarget.getDeltaMovement().x) * 180.0D / Math.PI) - 90.0F;
            // entity.moveForward = 0.5F;
            double d0 = this.getX() - attackTarget.getX();
            double d2 = this.getZ() - attackTarget.getZ();
            double d1 = this.getY() - 1 - attackTarget.getY();
            double d3 = Math.sqrt((float) (d0 * d0 + d2 * d2));
            float f = (float) (Mth.atan2(d2, d0) * (180D / Math.PI)) - 90.0F;
            float f1 = (float) (-(Mth.atan2(d1, d3) * (180D / Math.PI)));
            attackTarget.setXRot(updateRotation(attackTarget.getXRot(), f1, 30F));
            attackTarget.setYRot(updateRotation(attackTarget.getYRot(), f, 30F));
        }
        if (level().isClientSide) {
            tail_buffer.calculateChainSwingBuffer(40, 10, 2.5F, this);
        }
        if (this.isAgressive()) {
            ticksAgressive++;
        } else {
            ticksAgressive = 0;
        }
        if (ticksAgressive > 300 && this.isAgressive() && attackTarget == null && !level().isClientSide) {
            this.setAggressive(false);
            this.ticksAgressive = 0;
            this.setSinging(false);
        }

        if (this.isInWater() && !this.isSwimming()) {
            this.setSwimming(true);
        }
        if (!this.isInWater() && this.isSwimming()) {
            this.setSwimming(false);
        }
        LivingEntity target = getTarget();
        boolean pathOnHighGround = this.isPathOnHighGround() || !level().isClientSide && target != null && !target.isInWater();
        if (target == null || !target.isInWater() && !target.isInWater()) {
            if (pathOnHighGround && this.isInWater()) {
                jumpFromGround();
                doWaterSplashEffect();
            }
        }
        if ((this.isInWater() && !pathOnHighGround) && this.isLandNavigator) {
            switchNavigator(false);
        }
        if ((!this.isInWater() || pathOnHighGround) && !this.isLandNavigator) {
            switchNavigator(true);
        }
        if (target instanceof Player && ((Player) target).isCreative()) {
            this.setTarget(null);
            this.setAggressive(false);
        }
        if (target != null && !this.isAgressive()) {
            this.setAggressive(true);
        }
        boolean singing = isActuallySinging() && !this.isAgressive() && !this.isInWater() && onGround();
        if (singing && singProgress < 20.0F) {
            singProgress += 1F;
        } else if (!singing && singProgress > 0.0F) {
            singProgress -= 1F;
        }
        boolean swimming = isSwimming();
        if (swimming && swimProgress < 20.0F) {
            swimProgress += 1F;
        } else if (!swimming && swimProgress > 0.0F) {
            swimProgress -= 0.5F;
        }
        if (!level().isClientSide && !EntityGorgon.isStoneMob(this) && this.isActuallySinging()) {
            updateLure();
            checkForPrey();

        }
        if (!level().isClientSide && EntityGorgon.isStoneMob(this) && this.isSinging()) {
            this.setSinging(false);
        }
        if (isActuallySinging() && !this.isInWater()) {
            if (this.getRandom().nextInt(3) == 0) {
                yBodyRot = getYRot();
                if (this.level().isClientSide) {
                    float radius = -0.9F;
                    float angle = (0.01745329251F * this.yBodyRot) - 3F;
                    double extraX = radius * Mth.sin((float) (Math.PI + angle));
                    double extraY = 1.2F;
                    double extraZ = radius * Mth.cos(angle);
                    IceAndFire.PROXY.spawnParticle(EnumParticles.Siren_Music, this.getX() + extraX + this.random.nextFloat() - 0.5, this.getY() + extraY + this.random.nextFloat() - 0.5, this.getZ() + extraZ + this.random.nextFloat() - 0.5, 0, 0, 0);
                }
            }
        }
        if (this.isActuallySinging() && !this.isInWater() && this.tickCount % 200 == 0) {
            this.playSound(IafSoundRegistry.SIREN_SONG, 2, 1);
        }
        AnimationHandler.INSTANCE.updateAnimations(this);
    }

    private void checkForPrey() {
        this.setSinging(true);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.getEntity() != null && source.getEntity() instanceof LivingEntity) {
            this.triggerOtherSirens((LivingEntity) source.getEntity());
        }
        return super.hurt(source, amount);
    }

    public void triggerOtherSirens(LivingEntity aggressor) {
        List<Entity> entities = level().getEntities(this, this.getBoundingBox().inflate(12, 12, 12));
        for (Entity entity : entities) {
            if (entity instanceof EntitySiren) {
                ((EntitySiren) entity).setTarget(aggressor);
                ((EntitySiren) entity).setAggressive(true);
                ((EntitySiren) entity).setSinging(false);

            }
        }
    }

    public void updateLure() {
        if (this.tickCount % 20 == 0) {
            List<LivingEntity> entities = level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(50, 12, 50), SIREN_PREY);

            for (LivingEntity entity : entities) {
                if (isWearingEarplugs(entity)) {
                    continue;
                }

                EntityDataProvider.getCapability(entity).ifPresent(data -> {
                    if (data.sirenData.isCharmed || data.sirenData.charmedBy == null) {
                        data.sirenData.setCharmed(this);
                    }
                });
            }
        }
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("HairColor", this.getHairColor());
        tag.putBoolean("Aggressive", this.isAgressive());
        tag.putInt("SingingPose", this.getSingingPose());
        tag.putBoolean("Singing", this.isSinging());
        tag.putBoolean("Swimming", this.isSwimming());
        tag.putBoolean("Passive", this.isCharmed());

    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setHairColor(tag.getInt("HairColor"));
        this.setAggressive(tag.getBoolean("Aggressive"));
        this.setSingingPose(tag.getInt("SingingPose"));
        this.setSinging(tag.getBoolean("Singing"));
        this.setSwimming(tag.getBoolean("Swimming"));
        this.setCharmed(tag.getBoolean("Passive"));
        this.setConfigurableAttributes();
    }

    public boolean isSinging() {
        if (level().isClientSide) {
            return this.isSinging = this.entityData.get(SINGING).booleanValue();
        }
        return isSinging;
    }

    public void setSinging(boolean singing) {
        if (singCooldown > 0) {
            singing = false;
        }
        this.entityData.set(SINGING, singing);
        if (!level().isClientSide) {
            this.isSinging = singing;
            IceAndFire.sendMSGToAll(new MessageSirenSong(this.getId(), singing));
        }
    }

    public boolean wantsToSing() {
        return this.isSinging() && this.isInWater() && !this.isAgressive();
    }

    public boolean isActuallySinging() {
        return isSinging() && !wantsToSing();
    }

    @Override
    public boolean isSwimming() {
        if (level().isClientSide) {
            return this.isSwimming = this.entityData.get(SWIMMING).booleanValue();
        }
        return isSwimming;
    }

    @Override
    public void setSwimming(boolean swimming) {
        this.entityData.set(SWIMMING, swimming);
        if (!level().isClientSide) {
            this.isSwimming = swimming;
        }
    }

    @Override
    public void setAggressive(boolean aggressive) {
        this.entityData.set(AGGRESSIVE, aggressive);
    }

    public boolean isAgressive() {
        return this.entityData.get(AGGRESSIVE).booleanValue();
    }

    public boolean isCharmed() {
        return this.entityData.get(CHARMED).booleanValue();
    }

    public void setCharmed(boolean aggressive) {
        this.entityData.set(CHARMED, aggressive);
    }

    public int getHairColor() {
        return this.entityData.get(HAIR_COLOR).intValue();
    }

    public void setHairColor(int hairColor) {
        this.entityData.set(HAIR_COLOR, hairColor);
    }

    public int getSingingPose() {
        return this.entityData.get(SING_POSE).intValue();
    }

    public void setSingingPose(int pose) {
        this.entityData.set(SING_POSE, Mth.clamp(pose, 0, 2));
    }


    public static AttributeSupplier.Builder bakeAttributes() {
        return Mob.createMobAttributes()
                //HEALTH
                .add(Attributes.MAX_HEALTH, IafConfig.sirenMaxHealth)
                //SPEED
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                //ATTACK
                .add(Attributes.ATTACK_DAMAGE, 6.0D);
    }

    @Override
    public void setConfigurableAttributes() {
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(IafConfig.sirenMaxHealth);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(HAIR_COLOR, 0);
        this.entityData.define(SING_POSE, 0);
        this.entityData.define(AGGRESSIVE, Boolean.FALSE);
        this.entityData.define(SINGING, Boolean.FALSE);
        this.entityData.define(SWIMMING, Boolean.FALSE);
        this.entityData.define(CHARMED, Boolean.FALSE);
        this.entityData.define(CLIMBING, (byte) 0);
    }

    @Override
    @Nullable
    public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor worldIn, @NotNull DifficultyInstance difficultyIn, @NotNull MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        spawnDataIn = super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
        this.setHairColor(this.getRandom().nextInt(3));
        this.setSingingPose(this.getRandom().nextInt(3));
        return spawnDataIn;
    }

    public static float updateRotation(float angle, float targetAngle, float maxIncrease) {
        float f = Mth.wrapDegrees(targetAngle - angle);
        if (f > maxIncrease) {
            f = maxIncrease;
        }
        if (f < -maxIncrease) {
            f = -maxIncrease;
        }
        return angle + f;
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
        return new Animation[]{NO_ANIMATION, ANIMATION_BITE, ANIMATION_PULL};
    }

    @Override
    @Nullable
    protected SoundEvent getAmbientSound() {
        return this.isAgressive() ? IafSoundRegistry.NAGA_IDLE : IafSoundRegistry.MERMAID_IDLE;
    }

    @Override
    @Nullable
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSourceIn) {
        return this.isAgressive() ? IafSoundRegistry.NAGA_HURT : IafSoundRegistry.MERMAID_HURT;
    }

    @Override
    @Nullable
    protected SoundEvent getDeathSound() {
        return this.isAgressive() ? IafSoundRegistry.NAGA_DIE : IafSoundRegistry.MERMAID_DIE;
    }

    @Override
    public void travel(@NotNull Vec3 motion) {
        super.travel(motion);
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
    public boolean shouldFear() {
        return isAgressive();
    }

    class SwimmingMoveHelper extends MoveControl {
        private final EntitySiren siren = EntitySiren.this;

        public SwimmingMoveHelper() {
            super(EntitySiren.this);
        }

        @Override
        public void tick() {
            if (this.operation == MoveControl.Operation.MOVE_TO) {
                double distanceX = this.wantedX - siren.getX();
                double distanceY = this.wantedY - siren.getY();
                double distanceZ = this.wantedZ - siren.getZ();
                double distance = Math.abs(distanceX * distanceX + distanceZ * distanceZ);
                double distanceWithY = Math.sqrt(distanceX * distanceX + distanceY * distanceY + distanceZ * distanceZ);
                distanceY = distanceY / distanceWithY;
                float angle = (float) (Math.atan2(distanceZ, distanceX) * 180.0D / Math.PI) - 90.0F;
                siren.setYRot(this.rotlerp(siren.getYRot(), angle, 30.0F));
                siren.setSpeed(1F);
                float f1 = 0;
                float f2 = 0;
                if (distance < (double) Math.max(1.0F, siren.getBbWidth())) {
                    float f = siren.getYRot() * 0.017453292F;
                    f1 -= (double) (Mth.sin(f) * 0.35F);
                    f2 += (double) (Mth.cos(f) * 0.35F);
                }
                siren.setDeltaMovement(siren.getDeltaMovement().add(f1, siren.getSpeed() * distanceY * 0.1D, f2));
            } else if (this.operation == MoveControl.Operation.JUMPING) {
                siren.setSpeed((float) (this.speedModifier * siren.getAttribute(Attributes.MOVEMENT_SPEED).getValue()));
                if (siren.onGround()) {
                    this.operation = MoveControl.Operation.WAIT;
                }
            } else {
                siren.setSpeed(0.0F);
            }
        }
    }
}
