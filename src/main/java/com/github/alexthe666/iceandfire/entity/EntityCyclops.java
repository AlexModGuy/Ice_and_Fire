package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.ai.CyclopsAIAttackMelee;
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
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityPolarBear;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityWolf;
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
        this.stepHeight = 2;
        eyeEntity = new EntityCyclopsEye(this.getEntity(), 0.2F, 0, 7.4F, 1.2F, 0.5F, 1);
        ANIMATION_STOMP = Animation.create(27);
        ANIMATION_EATPLAYER = Animation.create(40);
        ANIMATION_KICK = Animation.create(20);
        ANIMATION_ROAR = Animation.create(20);

    }

    protected void initEntityAI() {
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAIRestrictSun(this));
        this.tasks.addTask(3, new EntityAIFleeSun(this, 1.0D));
        this.tasks.addTask(3, new CyclopsAIAttackMelee(this, 1.0D, false));
        this.tasks.addTask(5, new EntityAIWanderAvoidWater(this, 1.0D));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F, 1.0F));
        this.tasks.addTask(6, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false, new Class[0]));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityLivingBase.class, 0, true, false, new Predicate<EntityLivingBase>() {
            @Override
            public boolean apply(@Nullable EntityLivingBase entity) {
                return !(entity instanceof EntityCyclops) && !EventLiving.isAnimaniaSheep(entity) && !(entity instanceof EntityAnimal && !(entity instanceof EntityWolf || entity instanceof EntityPolarBear || entity instanceof EntityDragonBase)) || entity instanceof EntityGorgon || entity instanceof EntityVillager;
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

    public boolean attackEntityAsMob(Entity entityIn) {
        int attackDescision = this.getRNG().nextInt(3);
        if(attackDescision == 0){
            this.setAnimation(ANIMATION_STOMP);
            return true;
        }else if(attackDescision == 1){
            if(!entityIn.isPassenger(this) && entityIn.width < 1.95F && !(entityIn instanceof EntityDragonBase)){
                this.setAnimation(ANIMATION_EATPLAYER);
                entityIn.startRiding(this, true);
            }else{
                this.setAnimation(ANIMATION_STOMP);
            }
            return true;
        }else{
            this.setAnimation(ANIMATION_KICK);
            return true;
        }
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.35D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(IceAndFire.CONFIG.cyclopsAttackStrength);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(IceAndFire.CONFIG.cyclopsMaxHealth);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1D);

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

    public void updatePassenger(Entity passenger) {
        super.updatePassenger(passenger);
        if (this.isPassenger(passenger)) {
            passenger.motionX = 0;
            passenger.motionZ = 0;
            this.setAnimation(ANIMATION_EATPLAYER);
            double raiseUp = this.getAnimationTick() < 10 ? 0 : Math.min((this.getAnimationTick()  * 3 - 30) * 0.2, 5.2F);
            float pullIn = this.getAnimationTick() < 15 ? 0 : Math.min((this.getAnimationTick() - 15) * 0.15F, 0.75F);
            renderYawOffset = rotationYaw;
            this.rotationYaw *= 0;
            float radius = -2.75F + pullIn;
            float angle = (0.01745329251F * this.renderYawOffset) + 3.15F;
            double extraX = (double) (radius * MathHelper.sin((float) (Math.PI + angle)));
            double extraZ = (double) (radius * MathHelper.cos(angle));
            double extraY = raiseUp;
            passenger.setPosition(this.posX + extraX, this.posY + extraY, this.posZ + extraZ);
            if(this.getAnimationTick() == 32){
                passenger.attackEntityFrom(DamageSource.causeMobDamage(this), passenger instanceof EntityPlayer ? (float)IceAndFire.CONFIG.cyclopsBiteStrength : passenger instanceof EntityLivingBase ? (float) ((EntityLivingBase) passenger).getMaxHealth() * 2F : (float) this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue() * 2F);
                passenger.dismountRidingEntity();
            }
        }
    }

    @Override
    public void travel(float strafe, float forward, float vertical) {
        if (this.getAnimation() == ANIMATION_EATPLAYER) {
            strafe = 0;
            forward = 0;
            vertical = 0;
            super.travel(strafe, forward, vertical);
            return;
        }
        super.travel(strafe, forward, vertical);
    }
    public boolean canPassengerSteer(){
        return false;
    }

    public boolean canBePushed() {
        return false;
    }

    @Override
    public boolean shouldRiderSit() {
        return false;
    }


    public void onLivingUpdate(){
        super.onLivingUpdate();
        if(this.isBlinded() && this.getAttackTarget() != null && this.getDistanceSqToEntity(this.getAttackTarget()) > 6){
            this.setAttackTarget(null);
        }
        if(this.getAnimation()  == ANIMATION_STOMP && this.getAttackTarget() != null && this.getDistanceSqToEntity(this.getAttackTarget()) < 6D && this.getAnimationTick() == 14){
            this.getAttackTarget().attackEntityFrom(DamageSource.causeMobDamage(this), (float)this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue());
        }
        if(this.getAnimation()  == ANIMATION_KICK && this.getAttackTarget() != null && this.getDistanceSqToEntity(this.getAttackTarget()) < 6D && this.getAnimationTick() == 12){
            this.getAttackTarget().attackEntityFrom(DamageSource.causeMobDamage(this), (float)this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue());
            this.getAttackTarget().knockBack(this.getAttackTarget(), 2, 1, 1);

        }
        if(this.getAnimation() != ANIMATION_EATPLAYER && this.getAttackTarget() != null && !this.getPassengers().isEmpty() && this.getPassengers().contains(this.getAttackTarget())){
            this.setAnimation(ANIMATION_EATPLAYER);
        }
        if(this.getAnimation() == NO_ANIMATION && this.getAttackTarget() != null && this.getRNG().nextInt(55) == 0){
            this.setAnimation(ANIMATION_ROAR);
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
        if(!this.isBlinded()){
            this.setBlinded(true);
            this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(6F);
            this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.35D);
            this.setAnimation(ANIMATION_ROAR);
            this.attackEntityFrom(source, damage * 3);
        }
    }
}
