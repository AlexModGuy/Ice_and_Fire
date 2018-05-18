package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.entity.ai.*;
import com.github.alexthe666.iceandfire.event.EventLiving;
import com.google.common.base.Predicate;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityPolarBear;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class EntityStymphalianBird extends EntityCreature implements IAnimatedEntity {

    private int animationTick;
    private Animation currentAnimation;
    private static final DataParameter<Integer> VICTOR_ENTITY = EntityDataManager.<Integer>createKey(EntityStymphalianBird.class, DataSerializers.VARINT);
    private EntityLivingBase victorEntity;

    public EntityStymphalianBird(World worldIn) {
        super(worldIn);
        this.setSize(0.9F, 0.9F);
    }

    protected void initEntityAI() {
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(3, new EntityAIAttackMelee(this, 1.5D, false));
        this.tasks.addTask(5, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityLivingBase.class, 6.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, new Class[0]));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityLivingBase.class, 0, true, true, new Predicate<EntityLivingBase>() {
            @Override
            public boolean apply(@Nullable EntityLivingBase entity) {
                return  !EntityGorgon.isStoneMob(entity) && (entity instanceof EntityPlayer && !((EntityPlayer)entity).isCreative() || entity instanceof EntityVillager || entity instanceof EntityGolem);
            }
        }));
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(VICTOR_ENTITY, Integer.valueOf(0));
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound tag) {
        super.writeEntityToNBT(tag);
        tag.setInteger("VictorEntity", this.dataManager.get(VICTOR_ENTITY).intValue());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound tag) {
        super.readEntityFromNBT(tag);
        this.setVictorEntity(tag.getInteger("VictorEntity"));
    }

    public void setVictorEntity(int entityId) {
        this.dataManager.set(VICTOR_ENTITY, Integer.valueOf(entityId));
    }

    public boolean hasVictorEntity() {
        return ((Integer) this.dataManager.get(VICTOR_ENTITY)).intValue() != 0;
    }

    @Nullable
    public Entity getVictorEntity() {
        if (!this.hasVictorEntity()) {
            return null;
        } else if (this.world.isRemote) {
            if (this.victorEntity != null) {
                return this.victorEntity;
            } else {
                Entity entity = this.world.getEntityByID(((Integer) this.dataManager.get(VICTOR_ENTITY)).intValue());
                if (entity instanceof EntityLivingBase) {
                    this.victorEntity = (EntityLivingBase) entity;
                    return this.victorEntity;
                } else {
                    return null;
                }
            }
        } else {
            return this.world.getEntityByID(((Integer) this.dataManager.get(VICTOR_ENTITY)).intValue());
        }
    }


    
    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        AnimationHandler.INSTANCE.updateAnimations(this);
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
        return new Animation[0];
    }
}
