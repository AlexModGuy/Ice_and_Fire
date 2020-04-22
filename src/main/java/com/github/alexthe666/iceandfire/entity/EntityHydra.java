package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import com.google.common.base.Predicate;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.ilexiconn.llibrary.server.entity.multipart.IMultipartEntity;
import net.ilexiconn.llibrary.server.entity.multipart.PartEntity;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class EntityHydra extends EntityMob implements IAnimatedEntity, IMultipartEntity, IVillagerFear, IAnimalFear {

    public static final ResourceLocation LOOT = LootTableList.register(new ResourceLocation("iceandfire", "hydra"));
    public static final int HEADS = 9;
    public static final double HEAD_HEALTH_THRESHOLD = 20;
    private static final DataParameter<Integer> VARIANT = EntityDataManager.createKey(EntityHydra.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> HEAD_COUNT = EntityDataManager.createKey(EntityHydra.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> SEVERED_HEAD = EntityDataManager.createKey(EntityHydra.class, DataSerializers.VARINT);
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

    public EntityHydra(World worldIn) {
        super(worldIn);
        this.setSize(2.8F, 1.39F);
        resetParts();
        headDamageThreshold = Math.max(5, (float)IceAndFire.CONFIG.hydraMaxHealth * 0.08F);
    }

    protected void initEntityAI() {
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAIAttackMelee(this, 1.0D, true));
        this.tasks.addTask(5, new EntityAIWanderAvoidWater(this, 1.0D));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(7, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true, false, new Predicate<Entity>() {
            @Override
            public boolean apply(@Nullable Entity entity) {
                return entity.isEntityAlive();
            }
        }));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityLiving.class, 0, true, false, new Predicate<Entity>() {
            @Override
            public boolean apply(@Nullable Entity entity) {
                StoneEntityProperties properties = EntityPropertiesHandler.INSTANCE.getProperties(entity, StoneEntityProperties.class);
                return entity instanceof EntityLivingBase && DragonUtils.isAlive((EntityLiving) entity) && !(entity instanceof PartEntity) && !(entity instanceof IMob) && (properties == null || properties != null && !properties.isStone) || (entity instanceof IBlacklistedFromStatues && ((IBlacklistedFromStatues) entity).canBeTurnedToStone());
            }
        }));
    }

    public boolean attackEntityAsMob(Entity entityIn) {
        return false;
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (this.getAttackTarget() != null && this.canEntityBeSeen(this.getAttackTarget())) {
            int index = rand.nextInt(getHeadCount());
            if (!isBreathing[index] && !isStriking[index]) {
                if (this.getDistance(this.getAttackTarget()) < 6) {
                    if (strikeCooldown == 0 && strikingProgress[index] == 0) {
                        isBreathing[index] = false;
                        isStriking[index] = true;
                        this.world.setEntityState(this, (byte) (40 + index));
                        strikeCooldown = 3;
                    }
                } else if (rand.nextBoolean() && breathCooldown == 0) {
                    isBreathing[index] = true;
                    isStriking[index] = false;
                    this.world.setEntityState(this, (byte) (50 + index));
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
                if (this.getAttackTarget() != null && this.getDistance(this.getAttackTarget()) < 6) {
                    this.getAttackTarget().attackEntityFrom(DamageSource.causeMobDamage(this), (float) this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue());
                    this.getAttackTarget().addPotionEffect(new PotionEffect(MobEffects.POISON, 100, 3, false, false));
                    this.getAttackTarget().knockBack(this.getAttackTarget(), 0.25F, this.posX - this.getAttackTarget().posX, this.posZ - this.getAttackTarget().posZ);
                }
            }
            if (breathing) {
                EntityLivingBase entity = this.getAttackTarget();
                if (ticksExisted % 7 == 0 && entity != null && i < this.getHeadCount()) {
                    Vec3d vec3d = this.getLook(1.0F);
                    if(rand.nextFloat() < 0.2F){
                        this.playSound(IafSoundRegistry.HYDRA_SPIT, this.getSoundVolume(), this.getSoundPitch());
                    }
                    double headPosX = this.headBoxes[i].posX + vec3d.x * 1.0D;
                    double headPosY = this.headBoxes[i].posY + 1.3F;
                    double headPosZ = this.headBoxes[i].posZ + vec3d.z * 1.0D;
                    double d2 = entity.posX - headPosX + this.rand.nextGaussian() * 0.4D;
                    double d3 = entity.posY + entity.getEyeHeight() - headPosY + this.rand.nextGaussian() * 0.4D;
                    double d4 = entity.posZ - headPosZ + this.rand.nextGaussian() * 0.4D;
                    EntityHydraBreath entitylargefireball = new EntityHydraBreath(world, this, d2, d3, d4);
                    entitylargefireball.setPosition(headPosX, headPosY, headPosZ);
                    if (!world.isRemote && !entity.isDead) {
                        world.spawnEntity(entitylargefireball);
                    }
                    entitylargefireball.setPosition(headPosX, headPosY, headPosZ);
                }
                if (isBreathing[i] && (entity == null || entity.isDead || breathTicks[i] > 60) && !world.isRemote) {
                    isBreathing[i] = false;
                    breathTicks[i] = 0;
                    breathCooldown = 15;
                    this.world.setEntityState(this, (byte) (60 + i));
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
        if (this.getHeadCount() == 1 && !this.isBurning()) {
            this.setHeadCount(2);
            this.setSeveredHead(1);
            onlyRegrowOneHeadNotTwo = true;
        }

        if (this.getSeveredHead() != -1 && this.getSeveredHead() < this.getHeadCount()) {
            this.setSeveredHead(MathHelper.clamp(this.getSeveredHead(), 0, this.getHeadCount() - 1));
            regrowHeadCooldown++;
            if (regrowHeadCooldown >= 100) {
                headDamageTracker[this.getSeveredHead()] = 0;
                this.setSeveredHead(-1);
                if (this.isBurning()) {
                    this.setHeadCount(this.getHeadCount() - 1);
                } else {
                    this.playSound(IafSoundRegistry.HYDRA_REGEN_HEAD, this.getSoundVolume(), this.getSoundPitch());
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
        }
    }


    @Override
    public void onUpdate() {
        super.onUpdate();
        if (prevHeadCount != this.getHeadCount()) {
            resetParts();
        }
        onUpdateParts();
        float partY = 1.0F - limbSwingAmount * 0.5F;
        for (int i = 0; i < getHeadCount(); i++) {
            headBoxes[i].setPosition(headBoxes[i].posX, this.posY + partY, headBoxes[i].posZ);
            headBoxes[HEADS + i].setPosition(headBoxes[HEADS + i].posX, this.posY + partY, headBoxes[HEADS + i].posZ);
        }
        if (getHeadCount() > 1 && !isBurning()) {
            if (this.getHealth() < this.getMaxHealth() && this.ticksExisted % 30 == 0) {
                int level = getHeadCount() - 1;
                if (this.getSeveredHead() != -1) {
                    level--;
                }
                this.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 30, level, false, false));
            }
        }
        if (isBurning()) {
            this.removePotionEffect(MobEffects.REGENERATION);
        }

        prevHeadCount = this.getHeadCount();
    }

    public void onUpdateParts() {
        for (Entity entity : headBoxes) {
            if (entity != null) {
                entity.onUpdate();
            }
        }
    }

    private void clearParts() {
        for (Entity entity : headBoxes) {
            if (entity != null) {
                entity.setDead();
                world.removeEntityDangerously(entity);
            }
        }
    }

    public void setDead() {
        clearParts();
        super.setDead();
    }

    protected void playHurtSound(DamageSource source) {
        speakingProgress[rand.nextInt(getHeadCount())] = 1F;
        super.playHurtSound(source);
    }

    public void playLivingSound() {
        speakingProgress[rand.nextInt(getHeadCount())] = 1F;
        super.playLivingSound();
    }

    public int getTalkInterval() {
        return 100 / getHeadCount();
    }

    @Nullable
    protected ResourceLocation getLootTable() {
        return LOOT;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("Variant", this.getVariant());
        compound.setInteger("HeadCount", this.getHeadCount());
        compound.setInteger("SeveredHead", this.getSeveredHead());
        for(int i = 0; i < HEADS; i++){
            compound.setFloat("HeadDamage" + i, headDamageTracker[i]);
        }
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.setVariant(compound.getInteger("Variant"));
        this.setHeadCount(compound.getInteger("HeadCount"));
        this.setSeveredHead(compound.getInteger("SeveredHead"));
        for(int i = 0; i < HEADS; i++){
            headDamageTracker[i] = compound.getFloat("HeadDamage" + i);
        }
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(VARIANT, Integer.valueOf(0));
        this.dataManager.register(HEAD_COUNT, Integer.valueOf(3));
        this.dataManager.register(SEVERED_HEAD, Integer.valueOf(-1));
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(3.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(IceAndFire.CONFIG.generateHydraChance);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(1.0D);
        this.getEntityAttribute(EntityLivingBase.SWIM_SPEED).setBaseValue(2.0D);
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (lastHitHead > this.getHeadCount()) {
            lastHitHead = this.getHeadCount() - 1;
        }
        int headIndex = lastHitHead;
        headDamageTracker[headIndex] += amount;

        if (headDamageTracker[headIndex] > headDamageThreshold && (this.getSeveredHead() == -1 || this.getSeveredHead() >= this.getHeadCount())) {
            headDamageTracker[headIndex] = 0;
            this.regrowHeadCooldown = 0;
            this.setSeveredHead(headIndex);
            this.playSound(SoundEvents.ENTITY_GUARDIAN_FLOP, this.getSoundVolume(), this.getSoundPitch());
        }
        if (this.getHealth() <= amount + 5 && this.getHeadCount() > 1 && !source.canHarmInCreative()) {
            amount = 0;
        }
        return super.attackEntityFrom(source, amount);
    }

    @Override
    @Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
        livingdata = super.onInitialSpawn(difficulty, livingdata);
        this.setVariant(rand.nextInt(3));
        return livingdata;
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
    public boolean isNoDespawnRequired() {
        return true;
    }

    @Override
    protected boolean canDespawn() {
        return false;
    }

    public int getVariant() {
        return this.dataManager.get(VARIANT).intValue();
    }

    public void setVariant(int variant) {
        this.dataManager.set(VARIANT, variant);
    }

    public int getHeadCount() {
        return MathHelper.clamp(this.dataManager.get(HEAD_COUNT).intValue(), 1, HEADS);
    }

    public void setHeadCount(int count) {
        this.dataManager.set(HEAD_COUNT, MathHelper.clamp(count, 1, HEADS));
    }

    public int getSeveredHead() {
        return MathHelper.clamp(this.dataManager.get(SEVERED_HEAD).intValue(), -1, HEADS);
    }

    public void setSeveredHead(int count) {
        this.dataManager.set(SEVERED_HEAD, MathHelper.clamp(count, -1, HEADS));
    }

    @SideOnly(Side.CLIENT)
    public void handleStatusUpdate(byte id) {
        if (id >= 40 && id <= 48) {
            int index = id - 40;
            isStriking[MathHelper.clamp(index, 0, 8)] = true;
        } else if (id >= 50 && id <= 58) {
            int index = id - 50;
            isBreathing[MathHelper.clamp(index, 0, 8)] = true;
        } else if (id >= 60 && id <= 68) {
            int index = id - 60;
            isBreathing[MathHelper.clamp(index, 0, 8)] = false;
        } else {
            super.handleStatusUpdate(id);
        }
    }

    public boolean isPotionApplicable(PotionEffect potioneffectIn) {
        return potioneffectIn.getPotion() != MobEffects.POISON && super.isPotionApplicable(potioneffectIn);
    }

    public void onHitHead(float damage, int headIndex) {
        lastHitHead = headIndex;
    }

    public void triggerHeadFlags(int index) {
        lastHitHead = index;
    }

    @Nullable
    protected SoundEvent getAmbientSound() {
        return IafSoundRegistry.HYDRA_IDLE;
    }

    @Nullable
    protected SoundEvent getHurtSound(DamageSource source) {
        return IafSoundRegistry.HYDRA_HURT;
    }

    @Nullable
    protected SoundEvent getDeathSound() {
        return IafSoundRegistry.HYDRA_DIE;
    }

}
