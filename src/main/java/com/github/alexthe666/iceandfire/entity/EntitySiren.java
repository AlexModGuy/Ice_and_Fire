package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.enums.EnumDragonEgg;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;

public class EntitySiren extends EntityMob implements IAnimatedEntity {

    private int animationTick;
    private Animation currentAnimation;
    private static final DataParameter<Integer> HAIR_COLOR = EntityDataManager.<Integer>createKey(EntitySiren.class, DataSerializers.VARINT);
    private static final DataParameter<Boolean> AGGRESSIVE = EntityDataManager.<Boolean>createKey(EntitySiren.class, DataSerializers.BOOLEAN);

    public EntitySiren(World worldIn) {
        super(worldIn);
        this.setSize(1.2F, 0.9F);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound tag) {
        super.writeEntityToNBT(tag);
        tag.setInteger("HairColor", this.getHairColor());
        tag.setBoolean("Aggressive", this.isAgressive());

    }

    @Override
    public void readEntityFromNBT(NBTTagCompound tag) {
        super.readEntityFromNBT(tag);
        this.setHairColor(tag.getInteger("HairColor"));
        this.setAggressive(tag.getBoolean("Aggressive"));

    }

    public void setAggressive(boolean aggressive) {
        this.dataManager.set(AGGRESSIVE, aggressive);
    }

    public boolean isAgressive() {
        return this.dataManager.get(AGGRESSIVE);
    }

    public void setHairColor(int hairColor) {
        this.dataManager.set(HAIR_COLOR, hairColor);
    }

    public int getHairColor() {
        return this.dataManager.get(HAIR_COLOR);
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(6.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(IceAndFire.CONFIG.sirenMaxHealth);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(HAIR_COLOR, 0);
        this.dataManager.register(AGGRESSIVE, false);
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
        return new Animation[]{NO_ANIMATION};
    }


}
