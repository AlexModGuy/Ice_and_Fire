package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.event.EventLiving;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.ilexiconn.llibrary.server.entity.multipart.IMultipartEntity;
import net.ilexiconn.llibrary.server.entity.multipart.PartEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityCyclops extends EntityMob implements IAnimatedEntity, IMultipartEntity {

    private int animationTick;
    private Animation currentAnimation;
    public PartEntity eyeEntity;
    private static final DataParameter<Boolean> BLINDED = EntityDataManager.<Boolean>createKey(EntityCyclops.class, DataSerializers.BOOLEAN);


    public EntityCyclops(World worldIn) {
        super(worldIn);
        this.setSize(1.95F, 7.4F);
        eyeEntity = new EntityCyclopsEye(this.getEntity(), 0.2F, 0, 7.4F, 1.2F, 0.5F, 1);

    }

    protected void collideWithEntity(Entity entityIn) {
        if (!EventLiving.isAnimaniaSheep(entityIn)) {
            entityIn.applyEntityCollision(this);
        }
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(BLINDED, false);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setBoolean("Blind", this.isBlinded());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.setBlinded(compound.getBoolean("Blind"));
    }

    public void setBlinded(boolean blind) {
        this.dataManager.set(BLINDED, blind);
    }

    public boolean isBlinded() {
        return this.dataManager.get(BLINDED);
    }


    protected boolean canDespawn() {
        return false;
    }

    public EnumCreatureAttribute getCreatureAttribute() {
        return EnumCreatureAttribute.UNDEAD;
    }

    public void onLivingUpdate(){
        super.onLivingUpdate();
        onUpdateParts();
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

    public Entity[] getParts() {
        return new Entity[]{eyeEntity};
    };

    public boolean isBlinking() {
        return this.ticksExisted % 50 > 40 && !this.isBlinded();
    }


    public void onHitEye(DamageSource source, float damage) {
        this.setBlinded(true);
    }
}
