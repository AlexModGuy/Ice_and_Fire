package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.ai.GorgonAIStareAttack;
import com.google.common.base.Predicate;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class EntityGorgon extends EntityMob implements IAnimatedEntity {

    public static Animation ANIMATION_SCARE;
    private int animationTick;
    private Animation currentAnimation;
    public int stareTime;

    public EntityGorgon(World worldIn) {
        super(worldIn);
        this.setSize(0.8F, 1.99F);
        ANIMATION_SCARE = Animation.create(30);
    }

    public void attackEntityWithRangedAttack(EntityLivingBase entity) {

        if(!(entity instanceof EntityPlayer) && entity instanceof EntityLiving){
            forcePreyToLook((EntityLiving) entity);
        }
    }

    public void onLivingUpdate() {
        super.onLivingUpdate();
        if(this.getAttackTarget() != null && isEntityLookingAt(this, this.getAttackTarget()) && isEntityLookingAt(this.getAttackTarget(), this)) {
            if (this.getAnimation() != ANIMATION_SCARE) {
                this.setAnimation(ANIMATION_SCARE);
            }
            if(this.getAttackTarget() instanceof EntityLiving && !(this.getAttackTarget() instanceof EntityPlayer)){
                forcePreyToLook((EntityLiving) this.getAttackTarget());
                this.getLookHelper().setLookPosition(this.getAttackTarget().posX, this.getAttackTarget().posY + (double)this.getAttackTarget().getEyeHeight(), this.getAttackTarget().posZ, (float)this.getHorizontalFaceSpeed(), (float)this.getVerticalFaceSpeed());

            }
            if(stareTime > 25){
                //TODO custom damage type for player death msg
                if(this.getAttackTarget() instanceof EntityPlayer){
                    EntityStoneStatue statue = new EntityStoneStatue(world);
                    statue.setPositionAndRotation(this.getAttackTarget().posX, this.getAttackTarget().posY, this.getAttackTarget().posZ, this.getAttackTarget().rotationYaw, this.getAttackTarget().rotationPitch);
                    statue.smallArms = true;
                    if (!world.isRemote) {
                        world.spawnEntity(statue);
                    }
                    statue.prevRotationYaw = this.getAttackTarget().rotationYaw;
                    statue.rotationYaw = this.getAttackTarget().rotationYaw;
                    statue.rotationYawHead = this.getAttackTarget().rotationYaw;
                    statue.renderYawOffset = this.getAttackTarget().rotationYaw;
                    statue.prevRenderYawOffset = this.getAttackTarget().rotationYaw;
                    this.getAttackTarget().attackEntityFrom(IceAndFire.gorgon, Integer.MAX_VALUE);
                }else{
                    if(this.getAttackTarget() instanceof EntityLiving) {
                        StoneEntityProperties properties = EntityPropertiesHandler.INSTANCE.getProperties(this.getAttackTarget(), StoneEntityProperties.class);
                        if (properties != null) {
                            properties.isStone = true;
                        }
                        //IceAndFire.NETWORK_WRAPPER.sendToServer(new MessageStoneStatue(this.getAttackTarget().getEntityId(), true));
                        if (this.getAttackTarget() instanceof EntityDragonBase) {
                            EntityDragonBase dragon = (EntityDragonBase) this.getAttackTarget();
                            dragon.setFlying(false);
                            dragon.setHovering(false);
                            dragon.airTarget = null;
                        }
                        if (this.getAttackTarget() instanceof EntityHippogryph) {
                            EntityHippogryph dragon = (EntityHippogryph) this.getAttackTarget();
                            dragon.setFlying(false);
                            dragon.setHovering(false);
                            dragon.airTarget = null;
                        }
                    }else{
                        this.getAttackTarget().setDead();
                    }
                }
            }
            if (stareTime < 30) {
                stareTime++;
            } else {
                stareTime = 0;
            }
        }
        AnimationHandler.INSTANCE.updateAnimations(this);
    }

    public int getVerticalFaceSpeed() {
        return 10;
    }

    public int getHorizontalFaceSpeed() {
        return 10;
    }

    private static boolean isEntityLookingAt(EntityLivingBase looker, EntityLivingBase seen){
        Vec3d vec3d = looker.getLook(1.0F).normalize();
        Vec3d vec3d1 = new Vec3d(seen.posX - looker.posX, seen.getEntityBoundingBox().minY + (double)seen.getEyeHeight() - (looker.posY + (double)looker.getEyeHeight()), seen.posZ - looker.posZ);
        double d0 = vec3d1.lengthVector();
        vec3d1 = vec3d1.normalize();
        double d1 = vec3d.dotProduct(vec3d1);
        return d1 > 1.0D - 0.025D / d0 ? looker.canEntityBeSeen(seen) : false;
    }

    protected void initEntityAI() {
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAIRestrictSun(this));
        this.tasks.addTask(3, new EntityAIFleeSun(this, 1.0D));
        this.tasks.addTask(3, new EntityAIAvoidEntity(this, EntityDragonBase.class, 6.0F, 1.0D, 1.2D));
        this.tasks.addTask(4, new GorgonAIStareAttack(this, 1.0D, 0, 15.0F));
        this.tasks.addTask(5, new EntityAIWanderAvoidWater(this, 1.0D){
            public boolean shouldExecute() {
                executionChance = 20;
                return super.shouldExecute();
            }
        });
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F, 1.0F){
            public boolean shouldContinueExecuting() {
                if(this.closestEntity != null && this.closestEntity instanceof EntityPlayer && ((EntityPlayer) this.closestEntity).isCreative()){
                    return false;
                }
               return super.shouldContinueExecuting();
            }
        });
        this.tasks.addTask(6, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false, new Class[0]));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true, false, new Predicate<EntityPlayer>() {
            @Override
            public boolean apply(@Nullable EntityPlayer entity) {
                return true;
            }
        }));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityLiving.class, 0, true, false, new Predicate<Entity>() {
            @Override
            public boolean apply(@Nullable Entity entity) {
                return entity instanceof EntityLiving && !(entity instanceof IBlacklistedFromStatues);
            }
        }));
    }

    public EnumCreatureAttribute getCreatureAttribute() {
        return EnumCreatureAttribute.UNDEAD;
    }

    public void forcePreyToLook(EntityLiving mob){
        mob.getLookHelper().setLookPosition(this.posX, this.posY + (double)this.getEyeHeight(), this.posZ, (float)mob.getHorizontalFaceSpeed(), (float)mob.getVerticalFaceSpeed());
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(24.0D);
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
        return new Animation[]{ANIMATION_SCARE};
    }
}
