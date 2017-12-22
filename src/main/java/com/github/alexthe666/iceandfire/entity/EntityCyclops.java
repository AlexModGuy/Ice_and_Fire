package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.ai.GorgonAIStareAttack;
import com.github.alexthe666.iceandfire.event.EventLiving;
import com.google.common.base.Predicate;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.ilexiconn.llibrary.server.entity.multipart.IMultipartEntity;
import net.ilexiconn.llibrary.server.entity.multipart.PartEntity;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class EntityCyclops extends EntityMob implements IAnimatedEntity, IMultipartEntity {

    private int animationTick;
    private Animation currentAnimation;
    public PartEntity eyeEntity;
    private static final DataParameter<Boolean> BLINDED = EntityDataManager.<Boolean>createKey(EntityCyclops.class, DataSerializers.BOOLEAN);
    public static Animation ANIMATION_STOMP;
    public static Animation ANIMATION_EATPLAYER;
    public static Animation ANIMATION_KICK;
    public static Animation ANIMATION_ROAR;

    public EntityCyclops(World worldIn) {
        super(worldIn);
        this.setSize(1.95F, 7.4F);
        eyeEntity = new EntityCyclopsEye(this.getEntity(), 0.2F, 0, 7.4F, 1.2F, 0.5F, 1);
        ANIMATION_STOMP = Animation.create(27);
        ANIMATION_EATPLAYER = Animation.create(10);
        ANIMATION_KICK = Animation.create(10);
        ANIMATION_ROAR = Animation.create(10);
    }

    protected void initEntityAI() {
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAIRestrictSun(this));
        this.tasks.addTask(3, new EntityAIFleeSun(this, 1.0D));
        this.tasks.addTask(3, new EntityAIAttackMelee(this, 1.0D, false));
        this.tasks.addTask(5, new EntityAIWanderAvoidWater(this, 1.0D));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F, 1.0F));
        this.tasks.addTask(6, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false, new Class[0]));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true, false, new Predicate<EntityPlayer>() {
            @Override
            public boolean apply(@Nullable EntityPlayer entity) {
                return true;
            }
        }));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true, false, new Predicate<Entity>() {
            @Override
            public boolean apply(@Nullable Entity entity) {
                return true;
            }
        }));
    }

    protected void collideWithEntity(Entity entityIn) {
        if (!EventLiving.isAnimaniaSheep(entityIn)) {
            entityIn.applyEntityCollision(this);
        }
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.35D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(10.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(IceAndFire.CONFIG.gorgonMaxHealth);
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
        if(this.getAnimation() != ANIMATION_STOMP){
            this.setAnimation(ANIMATION_STOMP);
        }
        if(this.getAnimation() == ANIMATION_STOMP && this.getAnimationTick() == 14){
            for (int i1 = 0; i1 < 20; i1++) {
                double motionX = getRNG().nextGaussian() * 0.07D;
                double motionY = getRNG().nextGaussian() * 0.07D;
                double motionZ = getRNG().nextGaussian() * 0.07D;
                float radius = 0.75F * -2F;
                float angle = (0.01745329251F * this.renderYawOffset) + i1 * 1F;
                double extraX = (double) (radius * MathHelper.sin((float) (Math.PI + angle)));
                double extraY = 0.8F;
                double extraZ = (double) (radius * MathHelper.cos(angle));

                IBlockState iblockstate = this.world.getBlockState(new BlockPos(MathHelper.floor(this.posX + extraX), MathHelper.floor(this.posY + extraY) - 1, MathHelper.floor(this.posZ + extraZ)));
                if (iblockstate.getMaterial() != Material.AIR) {
                    if (world.isRemote) {
                        world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, true, this.posX + extraX, this.posY + extraY, this.posZ + extraZ, motionX, motionY, motionZ, new int[]{Block.getStateId(iblockstate)});
                    }
                }
            }
        }
        AnimationHandler.INSTANCE.updateAnimations(this);
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
        return new Animation[]{NO_ANIMATION, ANIMATION_STOMP, ANIMATION_EATPLAYER, ANIMATION_KICK, ANIMATION_ROAR};
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
