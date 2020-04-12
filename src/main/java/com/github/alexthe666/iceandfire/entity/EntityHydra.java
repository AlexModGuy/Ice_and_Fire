package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.enums.EnumHippogryphTypes;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.ilexiconn.llibrary.server.entity.multipart.IMultipartEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class EntityHydra extends EntityMob implements IAnimatedEntity, IMultipartEntity, IVillagerFear, IAnimalFear{

    public static final int HEADS = 9;
    private int animationTick;
    private Animation currentAnimation;
    public boolean[] isStriking = new boolean[HEADS];
    public float[] strikingProgress = new float[HEADS];
    public float[] prevStrikeProgress = new float[HEADS];
    public float[] speakingProgress = new float[HEADS];
    public float[] prevSpeakingProgress = new float[HEADS];
    private static final DataParameter<Integer> VARIANT = EntityDataManager.createKey(EntityHydra.class, DataSerializers.VARINT);
    private EntityMutlipartPart[] headBoxes = new EntityMutlipartPart[18];
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
    public EntityHydra(World worldIn) {
        super(worldIn);
        this.setSize(2.8F, 1.39F);
        resetParts();
    }

    @Override
    public void onLivingUpdate(){
        super.onLivingUpdate();
        for(int i = 0; i < HEADS; i++){
            boolean striking = isStriking[i];
            prevStrikeProgress[i] = strikingProgress[i];
            if (striking && strikingProgress[i] < 10.0F) {
                strikingProgress[i] += 0.5F;
            } else if (!striking && strikingProgress[i] > 0.0F) {
                strikingProgress[i] -= 0.5F;
            }
            prevSpeakingProgress[i] = speakingProgress[i];
            if (speakingProgress[i] > 0.0F) {
                speakingProgress[i] -= 0.1F;
            }
        }
    }

    public void resetParts() {
        clearParts();
        headBoxes = new EntityMutlipartPart[HEADS * 2];
        for (int i = 0; i < HEADS; i++) {
            float maxAngle = 5;
            headBoxes[i] = new EntityMutlipartPart(this, 3.2F, ROTATE[getHeadCount()-1][i] * 1.1F, 1.0F, 0.75F, 1.75F, 1);
            headBoxes[HEADS + i] = new EntityMutlipartPart(this, 2.3F, ROTATE[getHeadCount()-1][i] * 1.1F, 1.0F, 0.75F, 0.75F, 1);
        }
    }


    @Override
    public void onUpdate() {
        super.onUpdate();
        onUpdateParts();
        float partY = 1.0F - limbSwingAmount * 0.5F;
        for (int i = 0; i < HEADS; i++) {
            headBoxes[i].setPosition(headBoxes[i].posX, this.posY + partY, headBoxes[i].posZ);
            headBoxes[HEADS + i].setPosition(headBoxes[HEADS + i].posX, this.posY + partY, headBoxes[HEADS + i].posZ);
        }
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


    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("Variant", this.getVariant());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.setVariant(compound.getInteger("Variant"));
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(VARIANT, Integer.valueOf(0));
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(3.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(250D);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(5.0D);
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

    public int getHeadCount(){
        return 9;
    }

}
