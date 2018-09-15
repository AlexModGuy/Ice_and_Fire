package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.entity.ai.*;
import com.google.common.base.Predicate;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class EntityMyrmexWorker extends EntityMyrmexBase {

    private static final ResourceLocation TEXTURE_DESERT = new ResourceLocation("iceandfire:textures/models/myrmex/myrmex_desert_worker.png");
    private static final ResourceLocation TEXTURE_JUNGLE = new ResourceLocation("iceandfire:textures/models/myrmex/myrmex_jungle_worker.png");
    public boolean keepSearching = true;
    public static final Animation ANIMATION_BITE = Animation.create(15);
    public static final Animation ANIMATION_STING = Animation.create(15);

    public EntityMyrmexWorker(World worldIn) {
        super(worldIn);
        this.setSize(0.9F, 0.6F);
    }

    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (this.getAnimation() == ANIMATION_BITE && this.getAttackTarget() != null && this.getAnimationTick() == 6) {
            double dist = this.getDistanceSq(this.getAttackTarget());
            if (dist < 4) {
                this.getAttackTarget().attackEntityFrom(DamageSource.causeMobDamage(this), ((int) this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue()));
            }
        }
        if (this.getAnimation() == ANIMATION_STING && this.getAttackTarget() != null && this.getAnimationTick() == 6) {
            double dist = this.getDistanceSq(this.getAttackTarget());
            if (dist < 4) {
                this.getAttackTarget().attackEntityFrom(DamageSource.causeMobDamage(this), ((int) this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue() * 2));
                this.getAttackTarget().addPotionEffect(new PotionEffect(MobEffects.POISON, 60, 1));
            }
        }
    }

    protected void initEntityAI() {
        this.tasks.addTask(1, new EntityAIAttackMelee(this, 1.0D, true));
        this.tasks.addTask(2, new MyrmexAILeaveHive(this, 1.0D));
        this.tasks.addTask(3, new MyrmexAIReEnterHive(this, 1.0D));
        this.tasks.addTask(4, new MyrmexAIForage(this));
        this.tasks.addTask(5, new MyrmexAIMoveThroughHive(this, 1.0D));
        this.tasks.addTask(6, new EntityAIWanderAvoidWater(this, 1D));
        this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new MyrmexAIDefendHive(this));
        this.targetTasks.addTask(2, new EntityAIHurtByTarget(this, false, new Class[0]));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityLiving.class, 10, false, true, new Predicate<EntityLiving>() {
            public boolean apply(@Nullable EntityLiving entity) {
                return entity != null && !IMob.VISIBLE_MOB_SELECTOR.apply(entity) && !EntityMyrmexBase.haveSameHive(EntityMyrmexWorker.this, entity);
            }
        }));
        this.targetTasks.addTask(4, new MyrmexAIForageForItems(this, true));

    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(3.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(4.0D);
    }

    @Override
    public ResourceLocation getAdultTexture() {
        return isJungle() ? TEXTURE_JUNGLE : TEXTURE_DESERT;
    }

    @Override
    public float getModelScale() {
        return 0.6F;
    }

    public boolean shouldLeaveHive(){
        return !holdingSomething();
    }

    public boolean shouldEnterHive(){
        return holdingSomething();
    }

    @Override
    public boolean attackEntityAsMob(Entity entityIn) {
        if (this.getAnimation() != this.ANIMATION_STING && this.getAnimation() != this.ANIMATION_BITE) {
            this.setAnimation(this.getRNG().nextBoolean() ? this.ANIMATION_STING : this.ANIMATION_BITE);
            if(!this.world.isRemote && this.getRNG().nextInt(3) == 0 && this.getHeldItem(EnumHand.MAIN_HAND) != ItemStack.EMPTY){
                this.entityDropItem(this.getHeldItem(EnumHand.MAIN_HAND), 0);
                this.setHeldItem(EnumHand.MAIN_HAND, ItemStack.EMPTY);
            }
            if(!this.getPassengers().isEmpty()){
                for(Entity entity : this.getPassengers()){
                    entity.dismountRidingEntity();
                }
            }
            return true;
        }
        return false;
    }

    private boolean holdingSomething(){
        return this.getRidingEntity() != null || !this.getHeldItem(EnumHand.MAIN_HAND).isEmpty() || this.getAttackTarget() != null;
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[]{ANIMATION_PUPA_WIGGLE, ANIMATION_BITE, ANIMATION_STING};
    }

    public void updatePassenger(Entity passenger) {
        super.updatePassenger(passenger);
        if (this.isPassenger(passenger)) {
            renderYawOffset = rotationYaw;
            this.rotationYaw = passenger.rotationYaw;
            float radius = -0.65F;
            float angle = (0.01745329251F * this.renderYawOffset);
            double extraX = (double) (radius * MathHelper.sin((float) (Math.PI + angle)));
            double extraZ = (double) (radius * MathHelper.cos(angle));
            passenger.setPosition(this.posX + extraX, this.posY + this.getEyeHeight() - 0.55F, this.posZ + extraZ);
        }
    }

    public boolean attackEntityFrom(DamageSource source, float amount) {
        StoneEntityProperties properties = EntityPropertiesHandler.INSTANCE.getProperties(this, StoneEntityProperties.class);
        if(amount >= 1.0D && !this.world.isRemote && this.getRNG().nextInt(3) == 0 && this.getHeldItem(EnumHand.MAIN_HAND) != ItemStack.EMPTY && !properties.isStone){
            this.entityDropItem(this.getHeldItem(EnumHand.MAIN_HAND), 0);
            this.setHeldItem(EnumHand.MAIN_HAND, ItemStack.EMPTY);
        }
        if(amount >= 1.0D && !this.getPassengers().isEmpty()){
            for(Entity entity : this.getPassengers()){
                entity.dismountRidingEntity();
            }
        }
        return super.attackEntityFrom(source, amount);
    }
}
