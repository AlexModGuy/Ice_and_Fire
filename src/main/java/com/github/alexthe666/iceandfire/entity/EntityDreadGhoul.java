package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.iceandfire.entity.ai.DreadAITargetNonDread;
import com.github.alexthe666.iceandfire.entity.util.DragonUtils;
import com.github.alexthe666.iceandfire.entity.util.IAnimalFear;
import com.github.alexthe666.iceandfire.entity.util.IDreadMob;
import com.github.alexthe666.iceandfire.entity.util.IVillagerFear;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import com.google.common.base.Predicate;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class EntityDreadGhoul extends EntityDreadMob implements IAnimatedEntity, IVillagerFear, IAnimalFear {

    private static final DataParameter<Float> SCALE = EntityDataManager.defineId(EntityDreadGhoul.class, DataSerializers.FLOAT);
    private static final DataParameter<Integer> VARIANT = EntityDataManager.defineId(EntityDreadGhoul.class, DataSerializers.INT);
    private static final DataParameter<Integer> SCREAMS = EntityDataManager.defineId(EntityDreadGhoul.class, DataSerializers.INT);
    private static final float INITIAL_WIDTH = 0.6F;
    private static final float INITIAL_HEIGHT = 1.8F;
    public static Animation ANIMATION_SPAWN = Animation.create(40);
    public static Animation ANIMATION_SLASH = Animation.create(25);
    private int animationTick;
    private Animation currentAnimation;
    private int hostileTicks = 0;
    private float firstWidth = 1.0F;
    private float firstHeight = 1.0F;

    public EntityDreadGhoul(EntityType type, World worldIn) {
        super(type, worldIn);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(1, new SwimGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomWalkingGoal(this, 0.5D));
        this.goalSelector.addGoal(6, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(7, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, IDreadMob.class));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, 10,true,false,new Predicate<LivingEntity>() {
            @Override
            public boolean apply(@Nullable LivingEntity entity) {
                return DragonUtils.canHostilesTarget(entity);
            }
        }));
        this.targetSelector.addGoal(3, new DreadAITargetNonDread(this, LivingEntity.class, false, new Predicate<LivingEntity>() {
            @Override
            public boolean apply(@Nullable LivingEntity entity) {
                return DragonUtils.canHostilesTarget(entity);
            }
        }));
    }

    public static AttributeModifierMap.MutableAttribute bakeAttributes() {
        return MobEntity.createMobAttributes()
            //HEALTH
            .add(Attributes.MAX_HEALTH, 30.0D)
            //SPEED
            .add(Attributes.MOVEMENT_SPEED, 0.35D)
            //ATTACK
            .add(Attributes.ATTACK_DAMAGE, 5.0D)
            //FOLLOW RANGE
            .add(Attributes.FOLLOW_RANGE, 128.0D)
            //ARMOR
            .add(Attributes.ARMOR, 4.0D);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(VARIANT, Integer.valueOf(0));
        this.entityData.define(SCREAMS, Integer.valueOf(0));
        this.entityData.define(SCALE, Float.valueOf(1F));
    }

    public float getSize() {
        return Float.valueOf(this.entityData.get(SCALE).floatValue());
    }

    public void setSize(float scale) {
        this.entityData.set(SCALE, Float.valueOf(scale));
    }

    public boolean doHurtTarget(Entity entityIn) {
        if (this.getAnimation() == NO_ANIMATION) {
            this.setAnimation(ANIMATION_SLASH);
        }
        return true;
    }

    public void aiStep() {
        super.aiStep();
        LivingEntity attackTarget = this.getTarget();
        if (Math.abs(firstWidth - INITIAL_WIDTH * getSize()) > 0.01F || Math.abs(firstHeight - INITIAL_HEIGHT * getSize()) > 0.01F) {
            firstWidth = INITIAL_WIDTH * getSize();
            firstHeight = INITIAL_HEIGHT * getSize();
        }
        if (this.getAnimation() == ANIMATION_SPAWN && this.getAnimationTick() < 30) {
            BlockState belowBlock = level.getBlockState(this.blockPosition().below());
            if (belowBlock.getBlock() != Blocks.AIR) {
                for (int i = 0; i < 5; i++) {
                    this.level.addParticle(new BlockParticleData(ParticleTypes.BLOCK, belowBlock), this.getX() + (double) (this.random.nextFloat() * this.getBbWidth() * 2.0F) - (double) this.getBbWidth(), this.getBoundingBox().minY, this.getZ() + (double) (this.random.nextFloat() * this.getBbWidth() * 2.0F) - (double) this.getBbWidth(), this.random.nextGaussian() * 0.02D, this.random.nextGaussian() * 0.02D, this.random.nextGaussian() * 0.02D);
                }
            }
            this.setDeltaMovement(0, this.getDeltaMovement().y, this.getDeltaMovement().z);
        }
        if (attackTarget != null && this.distanceTo(attackTarget) < 4 && this.canSee(attackTarget)) {
            if (this.getAnimation() == NO_ANIMATION) {
                this.setAnimation(ANIMATION_SLASH);
            }
            this.lookAt(attackTarget, 360, 80);
            if (this.getAnimation() == ANIMATION_SLASH && (this.getAnimationTick() == 9 || this.getAnimationTick() == 19)) {
                attackTarget.hurt(DamageSource.mobAttack(this), (float) this.getAttribute(Attributes.ATTACK_DAMAGE).getValue());
                attackTarget.knockback(0.25F, this.getX() - attackTarget.getX(), this.getZ() - attackTarget.getZ());
            }
        }
        if (!level.isClientSide) {
            if (this.getTarget() != null) {
                hostileTicks++;
                if (this.getScreamStage() == 0) {
                    if (hostileTicks > 20) {
                        this.setScreamStage(1);
                    }
                } else {
                    if (this.tickCount % 20 < 10) {
                        this.setScreamStage(1);
                    } else {
                        this.setScreamStage(2);
                    }
                }
            } else {
                if (this.getScreamStage() > 0) {
                    if (this.tickCount % 20 < 10 && this.getScreamStage() == 2) {
                        this.setScreamStage(1);
                    } else {
                        this.setScreamStage(0);
                    }
                }
                hostileTicks = 0;
            }
        }
        AnimationHandler.INSTANCE.updateAnimations(this);
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Variant", this.getVariant());
        compound.putInt("ScreamStage", this.getScreamStage());
        compound.putFloat("DreadScale", this.getSize());
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT compound) {
        super.readAdditionalSaveData(compound);
        this.setVariant(compound.getInt("Variant"));
        this.setScreamStage(compound.getInt("ScreamStage"));
        this.setSize(compound.getFloat("DreadScale"));
    }

    public int getVariant() {
        return this.entityData.get(VARIANT).intValue();
    }

    public void setVariant(int variant) {
        this.entityData.set(VARIANT, variant);
    }

    public int getScreamStage() {
        return this.entityData.get(SCREAMS).intValue();
    }

    public void setScreamStage(int screamStage) {
        this.entityData.set(SCREAMS, screamStage);
    }

    public float getScale() {
        return getSize();
    }

    @Nullable
    public ILivingEntityData finalizeSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        ILivingEntityData data = super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
        this.setAnimation(ANIMATION_SPAWN);
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
        return new Animation[]{ANIMATION_SPAWN, ANIMATION_SLASH};
    }

    @Override
    public boolean shouldAnimalsFear(Entity entity) {
        return true;
    }

    @Override
    public boolean shouldFear() {
        return true;
    }

    @Nullable
    protected SoundEvent getAmbientSound() {
        return IafSoundRegistry.DREAD_GHOUL_IDLE;
    }

    @Nullable
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ZOMBIE_HURT;
    }

    @Nullable
    protected SoundEvent getDeathSound() {
        return SoundEvents.ZOMBIE_DEATH;
    }

    protected void playStepSound(BlockPos pos, Block blockIn) {
        this.playSound(SoundEvents.ZOMBIE_STEP, 0.15F, 1.0F);
    }

    protected float getVoicePitch() {
        return super.getVoicePitch() * 0.70F;
    }
}