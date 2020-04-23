package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import com.github.alexthe666.iceandfire.entity.ai.DreadAITargetNonDread;
import com.google.common.base.Predicate;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

import javax.annotation.Nullable;

public class EntityDreadGhoul extends EntityDreadMob implements IAnimatedEntity, IVillagerFear, IAnimalFear {

    public static final ResourceLocation LOOT = LootTableList.register(new ResourceLocation("iceandfire", "dread_ghoul"));
    private static final DataParameter<Float> SCALE = EntityDataManager.createKey(EntityDreadGhoul.class, DataSerializers.FLOAT);
    private static final DataParameter<Integer> VARIANT = EntityDataManager.createKey(EntityDreadGhoul.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> SCREAMS = EntityDataManager.createKey(EntityDreadGhoul.class, DataSerializers.VARINT);
    private static final float INITIAL_WIDTH = 0.6F;
    private static final float INITIAL_HEIGHT = 1.8F;
    public static Animation ANIMATION_SPAWN = Animation.create(40);
    public static Animation ANIMATION_SLASH = Animation.create(25);
    private int animationTick;
    private Animation currentAnimation;
    private int hostileTicks = 0;
    private float firstWidth = 1.0F;
    private float firstHeight = 1.0F;

    public EntityDreadGhoul(World worldIn) {
        super(worldIn);
        this.setSize(INITIAL_WIDTH, INITIAL_HEIGHT);
    }

    protected void initEntityAI() {
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAIAttackMelee(this, 1.0D, true));
        this.tasks.addTask(5, new EntityAIWanderAvoidWater(this, 0.5D));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(7, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, new Class[] {IDreadMob.class}));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
        this.targetTasks.addTask(3, new DreadAITargetNonDread(this, EntityLivingBase.class, false, new Predicate<Entity>() {
            @Override
            public boolean apply(@Nullable Entity entity) {
                return entity instanceof EntityLivingBase && DragonUtils.canHostilesTarget(entity);
            }
        }));
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.35D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(5.0D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(128.0D);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(4.0D);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(VARIANT, Integer.valueOf(0));
        this.dataManager.register(SCREAMS, Integer.valueOf(0));
        this.dataManager.register(SCALE, Float.valueOf(1F));
    }

    public float getScale() {
        return Float.valueOf(this.dataManager.get(SCALE).floatValue());
    }

    public void setScale(float scale) {
        this.dataManager.set(SCALE, Float.valueOf(scale));
    }

    public boolean attackEntityAsMob(Entity entityIn) {
        if (this.getAnimation() == NO_ANIMATION) {
            this.setAnimation(ANIMATION_SLASH);
        }
        return true;
    }

    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (Math.abs(firstWidth - INITIAL_WIDTH * getScale()) > 0.01F || Math.abs(firstHeight - INITIAL_HEIGHT * getScale()) > 0.01F) {
            firstWidth = INITIAL_WIDTH * getScale();
            firstHeight = INITIAL_HEIGHT * getScale();
            this.setSize(firstWidth, firstHeight);
        }
        if (this.getAnimation() == ANIMATION_SPAWN && this.getAnimationTick() < 30) {
            Block belowBlock = world.getBlockState(this.getPosition().down()).getBlock();
            if (belowBlock != Blocks.AIR) {
                for (int i = 0; i < 5; i++) {
                    this.world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, this.posX + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, this.getEntityBoundingBox().minY, this.posZ + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, this.rand.nextGaussian() * 0.02D, this.rand.nextGaussian() * 0.02D, this.rand.nextGaussian() * 0.02D, Block.getIdFromBlock(belowBlock));
                }
            }
            this.motionX = 0.0D;
            this.motionZ = 0.0D;
        }
        if (this.getAttackTarget() != null && this.getDistance(this.getAttackTarget()) < 4 && this.canEntityBeSeen(this.getAttackTarget())) {
            if (this.getAnimation() == NO_ANIMATION) {
                this.setAnimation(ANIMATION_SLASH);
            }
            this.faceEntity(this.getAttackTarget(), 360, 80);
            if (this.getAnimation() == ANIMATION_SLASH && (this.getAnimationTick() == 9 || this.getAnimationTick() == 19)) {
                this.getAttackTarget().attackEntityFrom(DamageSource.causeMobDamage(this), (float) this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue());
                this.getAttackTarget().knockBack(this.getAttackTarget(), 0.25F, this.posX - this.getAttackTarget().posX, this.posZ - this.getAttackTarget().posZ);
            }
        }
        if (!world.isRemote) {
            if (this.getAttackTarget() != null) {
                hostileTicks++;
                if (this.getScreamStage() == 0) {
                    if (hostileTicks > 20) {
                        this.setScreamStage(1);
                    }
                } else {
                    if (this.ticksExisted % 20 < 10) {
                        this.setScreamStage(1);
                    } else {
                        this.setScreamStage(2);
                    }
                }
            } else {
                if (this.getScreamStage() > 0) {
                    if (this.ticksExisted % 20 < 10 && this.getScreamStage() == 2) {
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
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("Variant", this.getVariant());
        compound.setInteger("ScreamStage", this.getScreamStage());
        compound.setFloat("Scale", this.getScale());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.setVariant(compound.getInteger("Variant"));
        this.setScreamStage(compound.getInteger("ScreamStage"));
        this.setScale(compound.getFloat("Scale"));
    }

    public int getVariant() {
        return this.dataManager.get(VARIANT).intValue();
    }

    public void setVariant(int variant) {
        this.dataManager.set(VARIANT, variant);
    }

    public int getScreamStage() {
        return this.dataManager.get(SCREAMS).intValue();
    }

    public void setScreamStage(int screamStage) {
        this.dataManager.set(SCREAMS, screamStage);
    }


    @Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
        IEntityLivingData data = super.onInitialSpawn(difficulty, livingdata);
        this.setAnimation(ANIMATION_SPAWN);
        this.setVariant(rand.nextInt(3));
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
    protected ResourceLocation getLootTable() {
        return LOOT;
    }

    @Nullable
    protected SoundEvent getAmbientSound() {
        return IafSoundRegistry.DREAD_GHOUL_IDLE;
    }

    @Nullable
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_ZOMBIE_HURT;
    }

    @Nullable
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_ZOMBIE_DEATH;
    }

    protected void playStepSound(BlockPos pos, Block blockIn) {
        this.playSound(SoundEvents.ENTITY_ZOMBIE_STEP, 0.15F, 1.0F);
    }

    protected float getSoundPitch() {
        return super.getSoundPitch() * 0.70F;
    }
}