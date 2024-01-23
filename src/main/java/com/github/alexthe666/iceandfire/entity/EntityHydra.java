package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.entity.util.*;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import com.google.common.base.Predicate;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class EntityHydra extends Monster implements IAnimatedEntity, IMultipartEntity, IVillagerFear, IAnimalFear, IHasCustomizableAttributes {

    public static final int HEADS = 9;
    public static final double HEAD_HEALTH_THRESHOLD = 20;
    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(EntityHydra.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> HEAD_COUNT = SynchedEntityData.defineId(EntityHydra.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> SEVERED_HEAD = SynchedEntityData.defineId(EntityHydra.class, EntityDataSerializers.INT);
    private static final float[][] ROTATE = new float[][]{
        {0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F},// 1 total heads
        {10F, -10F, 0F, 0F, 0F, 0F, 0F, 0F, 0F},// 2 total heads
        {10F, 0F, -10F, 0F, 0F, 0F, 0F, 0F, 0F},// 3 total heads
        {25F, 10F, -10F, -25F, 0F, 0F, 0F, 0F, 0F},//etc...
        {30F, 15F, 0F, -15F, -30F, 0F, 0F, 0F, 0F},
        {40F, 25F, 5F, -5F, -25F, -40F, 0F, 0F, 0F},
        {40F, 30F, 15F, 0F, -15F, -30F, -40F, 0F, 0F},
        {45F, 30F, 20F, 5F, -5F, -20F, -30F, -45F, 0F},
        {50F, 37F, 25F, 15F, 0, -15F, -25F, -37F, -50F},
    };
    public boolean[] isStriking = new boolean[HEADS];
    public float[] strikingProgress = new float[HEADS];
    public float[] prevStrikeProgress = new float[HEADS];
    public boolean[] isBreathing = new boolean[HEADS];
    public float[] speakingProgress = new float[HEADS];
    public float[] prevSpeakingProgress = new float[HEADS];
    public float[] breathProgress = new float[HEADS];
    public float[] prevBreathProgress = new float[HEADS];
    public int[] breathTicks = new int[HEADS];
    public float[] headDamageTracker = new float[HEADS];
    private int animationTick;
    private Animation currentAnimation;
    private EntityHydraHead[] headBoxes = new EntityHydraHead[HEADS * 9];
    private int strikeCooldown = 0;
    private int breathCooldown = 0;
    private int lastHitHead = 0;
    private int prevHeadCount = -1;
    private int regrowHeadCooldown = 0;
    private boolean onlyRegrowOneHeadNotTwo = false;
    private float headDamageThreshold = 20;

    public EntityHydra(EntityType<EntityHydra> type, Level worldIn) {
        super(type, worldIn);
        resetParts();
        headDamageThreshold = Math.max(5, (float) IafConfig.hydraMaxHealth * 0.08F);
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Mob.createMobAttributes()
            //HEALTH
            .add(Attributes.MAX_HEALTH, IafConfig.hydraMaxHealth)
            //SPEED
            .add(Attributes.MOVEMENT_SPEED, 0.3D)
            //ATTACK
            .add(Attributes.ATTACK_DAMAGE, 3.0D)
            //ARMOR
            .add(Attributes.ARMOR, 1.0D);
    }

    @Override
    public void setConfigurableAttributes() {
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(IafConfig.hydraMaxHealth);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal(this, LivingEntity.class, 10, true, false, new Predicate<Entity>() {
            @Override
            public boolean apply(@Nullable Entity entity) {
                return entity instanceof LivingEntity && DragonUtils.isAlive((LivingEntity) entity) && !(entity instanceof EntityMutlipartPart) && !(entity instanceof Enemy) || (entity instanceof IBlacklistedFromStatues && ((IBlacklistedFromStatues) entity).canBeTurnedToStone());
            }
        }));
    }

    @Override
    public boolean doHurtTarget(@NotNull Entity entityIn) {
        return false;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        LivingEntity attackTarget = this.getTarget();
        if (attackTarget != null && this.hasLineOfSight(attackTarget)) {
            int index = random.nextInt(getHeadCount());
            if (!isBreathing[index] && !isStriking[index]) {
                if (this.distanceTo(attackTarget) < 6) {
                    if (strikeCooldown == 0 && strikingProgress[index] == 0) {
                        isBreathing[index] = false;
                        isStriking[index] = true;
                        this.level().broadcastEntityEvent(this, (byte) (40 + index));
                        strikeCooldown = 3;
                    }
                } else if (random.nextBoolean() && breathCooldown == 0) {
                    isBreathing[index] = true;
                    isStriking[index] = false;
                    this.level().broadcastEntityEvent(this, (byte) (50 + index));
                    breathCooldown = 15;
                }

            }

        }
        for (int i = 0; i < HEADS; i++) {
            boolean striking = isStriking[i];
            boolean breathing = isBreathing[i];
            prevStrikeProgress[i] = strikingProgress[i];
            if (striking && strikingProgress[i] > 9) {
                isStriking[i] = false;
                if (attackTarget != null && this.distanceTo(attackTarget) < 6) {
                    attackTarget.hurt(level().damageSources().mobAttack(this), (float) this.getAttribute(Attributes.ATTACK_DAMAGE).getValue());
                    attackTarget.addEffect(new MobEffectInstance(MobEffects.POISON, 100, 3, false, false));
                    attackTarget.knockback(0.25F, this.getX() - attackTarget.getX(), this.getZ() - attackTarget.getZ());
                }
            }
            if (breathing) {
                if (tickCount % 7 == 0 && attackTarget != null && i < this.getHeadCount()) {
                    Vec3 Vector3d = this.getViewVector(1.0F);
                    if (random.nextFloat() < 0.2F) {
                        this.playSound(IafSoundRegistry.HYDRA_SPIT, this.getSoundVolume(), this.getVoicePitch());
                    }
                    double headPosX = this.headBoxes[i].getX() + Vector3d.x;
                    double headPosY = this.headBoxes[i].getY() + 1.3F;
                    double headPosZ = this.headBoxes[i].getZ() + Vector3d.z;
                    double d2 = attackTarget.getX() - headPosX + this.random.nextGaussian() * 0.4D;
                    double d3 = attackTarget.getY() + attackTarget.getEyeHeight() - headPosY + this.random.nextGaussian() * 0.4D;
                    double d4 = attackTarget.getZ() - headPosZ + this.random.nextGaussian() * 0.4D;
                    EntityHydraBreath entitylargefireball = new EntityHydraBreath(IafEntityRegistry.HYDRA_BREATH.get(),
                        level(), this, d2, d3, d4);
                    entitylargefireball.setPos(headPosX, headPosY, headPosZ);
                    if (!level().isClientSide) {
                        level().addFreshEntity(entitylargefireball);
                    }
                }
                if (isBreathing[i] && (attackTarget == null || !attackTarget.isAlive() || breathTicks[i] > 60) && !level().isClientSide) {
                    isBreathing[i] = false;
                    breathTicks[i] = 0;
                    breathCooldown = 15;
                    this.level().broadcastEntityEvent(this, (byte) (60 + i));
                }
                breathTicks[i]++;
            } else {
                breathTicks[i] = 0;
            }
            if (striking && strikingProgress[i] < 10.0F) {
                strikingProgress[i] += 2.5F;
            } else if (!striking && strikingProgress[i] > 0.0F) {
                strikingProgress[i] -= 2.5F;
            }
            prevSpeakingProgress[i] = speakingProgress[i];
            if (speakingProgress[i] > 0.0F) {
                speakingProgress[i] -= 0.1F;
            }
            prevBreathProgress[i] = breathProgress[i];
            if (breathing && breathProgress[i] < 10.0F) {
                breathProgress[i] += 1.0F;
            } else if (!breathing && breathProgress[i] > 0.0F) {
                breathProgress[i] -= 1.0F;
            }

        }
        if (strikeCooldown > 0) {
            strikeCooldown--;
        }
        if (breathCooldown > 0) {
            breathCooldown--;
        }
        if (this.getHeadCount() == 1 && this.getSeveredHead() != -1) {
            this.setSeveredHead(-1);
        }
        if (this.getHeadCount() == 1 && !this.isOnFire()) {
            this.setHeadCount(2);
            this.setSeveredHead(1);
            onlyRegrowOneHeadNotTwo = true;
        }

        if (this.getSeveredHead() != -1 && this.getSeveredHead() < this.getHeadCount()) {
            this.setSeveredHead(Mth.clamp(this.getSeveredHead(), 0, this.getHeadCount() - 1));
            regrowHeadCooldown++;
            if (regrowHeadCooldown >= 100) {
                headDamageTracker[this.getSeveredHead()] = 0;
                this.setSeveredHead(-1);
                if (this.isOnFire()) {
                    this.setHeadCount(this.getHeadCount() - 1);
                } else {
                    this.playSound(IafSoundRegistry.HYDRA_REGEN_HEAD, this.getSoundVolume(), this.getVoicePitch());
                    if (!onlyRegrowOneHeadNotTwo) {
                        this.setHeadCount(this.getHeadCount() + 1);
                    }
                }
                onlyRegrowOneHeadNotTwo = false;
                regrowHeadCooldown = 0;
            }
        } else {
            regrowHeadCooldown = 0;
        }
    }

    public void resetParts() {
        clearParts();
        headBoxes = new EntityHydraHead[HEADS * 2];
        for (int i = 0; i < getHeadCount(); i++) {
            float maxAngle = 5;
            headBoxes[i] = new EntityHydraHead(this, 3.2F, ROTATE[getHeadCount() - 1][i] * 1.1F, 1.0F, 0.75F, 1.75F, 1, i, false);
            headBoxes[HEADS + i] = new EntityHydraHead(this, 2.1F, ROTATE[getHeadCount() - 1][i] * 1.1F, 1.0F, 0.75F, 0.75F, 1, i, true);
            headBoxes[i].copyPosition(this);
            headBoxes[HEADS + i].copyPosition(this);
            headBoxes[i].setParent(this);
            headBoxes[HEADS + i].setParent(this);
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (prevHeadCount != this.getHeadCount()) {
            resetParts();
        }

        float partY = 1.0F - this.walkAnimation.speed() * 0.5F;

        for (int i = 0; i < getHeadCount(); i++) {
            headBoxes[i].setPos(headBoxes[i].getX(), this.getY() + partY, headBoxes[i].getZ());
            EntityUtil.updatePart(headBoxes[i], this);

            headBoxes[HEADS + i].setPos(headBoxes[HEADS + i].getX(), this.getY() + partY, headBoxes[HEADS + i].getZ());
            EntityUtil.updatePart(headBoxes[HEADS + 1], this);
        }

        if (getHeadCount() > 1 && !isOnFire()) {
            if (this.getHealth() < this.getMaxHealth() && this.tickCount % 30 == 0) {
                int level = getHeadCount() - 1;
                if (this.getSeveredHead() != -1) {
                    level--;
                }

                this.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 30, level, false, false));
            }
        }

        if (isOnFire()) {
            this.removeEffect(MobEffects.REGENERATION);
        }

        prevHeadCount = this.getHeadCount();
    }

    private void clearParts() {
        for (Entity entity : headBoxes) {
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
    protected void playHurtSound(@NotNull DamageSource source) {
        speakingProgress[random.nextInt(getHeadCount())] = 1F;
        super.playHurtSound(source);
    }

    @Override
    public void playAmbientSound() {
        speakingProgress[random.nextInt(getHeadCount())] = 1F;
        super.playAmbientSound();
    }

    @Override
    public int getAmbientSoundInterval() {
        return 100 / getHeadCount();
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Variant", this.getVariant());
        compound.putInt("HeadCount", this.getHeadCount());
        compound.putInt("SeveredHead", this.getSeveredHead());
        for (int i = 0; i < HEADS; i++) {
            compound.putFloat("HeadDamage" + i, headDamageTracker[i]);
        }
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setVariant(compound.getInt("Variant"));
        this.setHeadCount(compound.getInt("HeadCount"));
        this.setSeveredHead(compound.getInt("SeveredHead"));
        for (int i = 0; i < HEADS; i++) {
            headDamageTracker[i] = compound.getFloat("HeadDamage" + i);
        }
        this.setConfigurableAttributes();
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(VARIANT, 0);
        this.entityData.define(HEAD_COUNT, 3);
        this.entityData.define(SEVERED_HEAD, -1);
    }

    @Override
    public boolean hurt(@NotNull DamageSource source, float amount) {
        if (lastHitHead > this.getHeadCount()) {
            lastHitHead = this.getHeadCount() - 1;
        }
        int headIndex = lastHitHead;
        headDamageTracker[headIndex] += amount;

        if (headDamageTracker[headIndex] > headDamageThreshold && (this.getSeveredHead() == -1 || this.getSeveredHead() >= this.getHeadCount())) {
            headDamageTracker[headIndex] = 0;
            this.regrowHeadCooldown = 0;
            this.setSeveredHead(headIndex);
            this.playSound(SoundEvents.GUARDIAN_FLOP, this.getSoundVolume(), this.getVoicePitch());
        }
        if (this.getHealth() <= amount + 5 && this.getHeadCount() > 1 && !source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            amount = 0;
        }
        return super.hurt(source, amount);
    }

    @Override
    @Nullable
    public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor worldIn, @NotNull DifficultyInstance difficultyIn, @NotNull MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        SpawnGroupData data = super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
        this.setVariant(random.nextInt(3));
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
        return new Animation[]{};
    }

    @Override
    public boolean shouldAnimalsFear(Entity entity) {
        return true;
    }

    @Override
    public boolean isPersistenceRequired() {
        return true;
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    public int getVariant() {
        return this.entityData.get(VARIANT).intValue();
    }

    public void setVariant(int variant) {
        this.entityData.set(VARIANT, variant);
    }

    public int getHeadCount() {
        return Mth.clamp(this.entityData.get(HEAD_COUNT).intValue(), 1, HEADS);
    }

    public void setHeadCount(int count) {
        this.entityData.set(HEAD_COUNT, Mth.clamp(count, 1, HEADS));
    }

    public int getSeveredHead() {
        return Mth.clamp(this.entityData.get(SEVERED_HEAD).intValue(), -1, HEADS);
    }

    public void setSeveredHead(int count) {
        this.entityData.set(SEVERED_HEAD, Mth.clamp(count, -1, HEADS));
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id >= 40 && id <= 48) {
            int index = id - 40;
            isStriking[Mth.clamp(index, 0, 8)] = true;
        } else if (id >= 50 && id <= 58) {
            int index = id - 50;
            isBreathing[Mth.clamp(index, 0, 8)] = true;
        } else if (id >= 60 && id <= 68) {
            int index = id - 60;
            isBreathing[Mth.clamp(index, 0, 8)] = false;
        } else {
            super.handleEntityEvent(id);
        }
    }

    @Override
    public boolean canBeAffected(MobEffectInstance potioneffectIn) {
        return potioneffectIn.getEffect() != MobEffects.POISON && super.canBeAffected(potioneffectIn);
    }

    public void onHitHead(float damage, int headIndex) {
        lastHitHead = headIndex;
    }

    public void triggerHeadFlags(int index) {
        lastHitHead = index;
    }

    @Override
    @Nullable
    protected SoundEvent getAmbientSound() {
        return IafSoundRegistry.HYDRA_IDLE;
    }

    @Override
    @Nullable
    protected SoundEvent getHurtSound(@NotNull DamageSource source) {
        return IafSoundRegistry.HYDRA_HURT;
    }

    @Override
    @Nullable
    protected SoundEvent getDeathSound() {
        return IafSoundRegistry.HYDRA_DIE;
    }

}
