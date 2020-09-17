package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.iceandfire.IafConfig;
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
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class EntityDreadScuttler extends EntityDreadMob implements IAnimatedEntity, IVillagerFear, IAnimalFear {

    private static final DataParameter<Float> SCALE = EntityDataManager.createKey(EntityDreadScuttler.class, DataSerializers.FLOAT);
    private static final DataParameter<Byte> CLIMBING = EntityDataManager.createKey(EntityDreadScuttler.class, DataSerializers.BYTE);
    private static final float INITIAL_WIDTH = 1.5F;
    private static final float INITIAL_HEIGHT = 1.3F;
    public static Animation ANIMATION_SPAWN = Animation.create(40);
    public static Animation ANIMATION_BITE = Animation.create(15);
    private int animationTick;
    private Animation currentAnimation;
    private float firstWidth = -1.0F;
    private float firstHeight = -1.0F;

    public EntityDreadScuttler(EntityType type, World worldIn) {
        super(type, worldIn);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(1, new SwimGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(7, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, IDreadMob.class));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.addGoal(3, new DreadAITargetNonDread(this, LivingEntity.class, false, new Predicate<LivingEntity>() {
            @Override
            public boolean apply(LivingEntity entity) {
                return entity instanceof LivingEntity && DragonUtils.canHostilesTarget(entity);
            }
        }));
    }

    public static AttributeModifierMap.MutableAttribute bakeAttributes() {
        return MobEntity.func_233666_p_()
                //HEALTH
                .func_233815_a_(Attributes.field_233818_a_, 40.0D)
                //SPEED
                .func_233815_a_(Attributes.field_233821_d_, 0.34D)
                //ATTACK
                .func_233815_a_(Attributes.field_233823_f_, 7.0D)
                //FOLLOW RANGE
                .func_233815_a_(Attributes.field_233819_b_, 12.0D)
                //ARMOR
                .func_233815_a_(Attributes.field_233826_i_, 10.0D);
    }


    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(CLIMBING, Byte.valueOf((byte) 0));
        this.dataManager.register(SCALE, Float.valueOf(1F));
    }

    public float getScale() {
        return Float.valueOf(this.dataManager.get(SCALE).floatValue());
    }

    public void setScale(float scale) {
        this.dataManager.set(SCALE, Float.valueOf(scale));
    }

    @Override
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putFloat("Scale", this.getScale());
    }

    @Override
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        this.setScale(compound.getFloat("Scale"));
    }

    public boolean attackEntityAsMob(Entity entityIn) {
        if (this.getAnimation() == NO_ANIMATION) {
            this.setAnimation(ANIMATION_BITE);
        }
        return true;
    }

    public void livingTick() {
        super.livingTick();
        if (Math.abs(firstWidth - INITIAL_WIDTH * getScale()) > 0.01F || Math.abs(firstHeight - INITIAL_HEIGHT * getScale()) > 0.01F) {
            firstWidth = INITIAL_WIDTH * getScale();
            firstHeight = INITIAL_HEIGHT * getScale();
        }
        if (!this.world.isRemote) {
            this.setBesideClimbableBlock(this.collidedHorizontally);
        }
        if (this.getAnimation() == ANIMATION_SPAWN && this.getAnimationTick() < 30) {
            BlockState belowBlock = world.getBlockState(this.func_233580_cy_().down());
            if (belowBlock.getBlock() != Blocks.AIR) {
                for (int i = 0; i < 5; i++) {
                    this.world.addParticle(new BlockParticleData(ParticleTypes.BLOCK, belowBlock), this.getPosX() + (double) (this.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), this.getBoundingBox().minY, this.getPosZ() + (double) (this.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), this.rand.nextGaussian() * 0.02D, this.rand.nextGaussian() * 0.02D, this.rand.nextGaussian() * 0.02D);
                }
            }
            this.setMotion(0, this.getMotion().y, 0);
        }
        if (this.getAttackTarget() != null && this.getDistance(this.getAttackTarget()) < 4 && this.canEntityBeSeen(this.getAttackTarget())) {
            if (this.getAnimation() == NO_ANIMATION) {
                this.setAnimation(ANIMATION_BITE);
            }
            this.faceEntity(this.getAttackTarget(), 360, 80);
            if (this.getAnimation() == ANIMATION_BITE && this.getAnimationTick() == 6) {
                this.getAttackTarget().attackEntityFrom(DamageSource.causeMobDamage(this), (float) this.getAttribute(Attributes.field_233823_f_).getValue());
                this.getAttackTarget().func_233627_a_(0.25F, this.getPosX() - this.getAttackTarget().getPosX(), this.getPosZ() - this.getAttackTarget().getPosZ());
            }
        }

        AnimationHandler.INSTANCE.updateAnimations(this);
    }

    public boolean isOnLadder() {
        return this.isBesideClimbableBlock();
    }

    public void setInWeb() {
    }

    public CreatureAttribute getCreatureAttribute() {
        return CreatureAttribute.ARTHROPOD;
    }

    public boolean isPotionApplicable(EffectInstance potioneffectIn) {
        return potioneffectIn.getPotion() != Effects.POISON && super.isPotionApplicable(potioneffectIn);
    }

    public boolean isBesideClimbableBlock() {
        return (this.dataManager.get(CLIMBING).byteValue() & 1) != 0;
    }

    public void setBesideClimbableBlock(boolean climbing) {
        byte b0 = this.dataManager.get(CLIMBING).byteValue();

        if (climbing) {
            b0 = (byte) (b0 | 1);
        } else {
            b0 = (byte) (b0 & -2);
        }

        this.dataManager.set(CLIMBING, Byte.valueOf(b0));
    }

    @Nullable
    public ILivingEntityData onInitialSpawn(IWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        ILivingEntityData data = super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
        this.setAnimation(ANIMATION_SPAWN);
        this.setScale(0.5F + rand.nextFloat() * 1.15F);
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
        return new Animation[]{ANIMATION_SPAWN, ANIMATION_BITE};
    }

    @Override
    public boolean shouldAnimalsFear(Entity entity) {
        return true;
    }

    @Override
    public boolean shouldFear() {
        return true;
    }

    @Override
    public Entity getCommander() {
        return null;
    }

    @Override
    public boolean isOnSameTeam(Entity entityIn) {
        return entityIn instanceof IDreadMob || super.isOnSameTeam(entityIn);
    }

    @Nullable
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_SPIDER_AMBIENT;
    }

    @Nullable
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_SPIDER_HURT;
    }

    @Nullable
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_SPIDER_DEATH;
    }

    protected float getSoundPitch() {
        return super.getSoundPitch() * 0.70F;
    }

    protected void playStepSound(BlockPos pos, Block blockIn) {
        this.playSound(IafSoundRegistry.MYRMEX_WALK, 0.25F, 1.0F);
    }

    public float getRenderScale() {
        return getScale();
    }

}