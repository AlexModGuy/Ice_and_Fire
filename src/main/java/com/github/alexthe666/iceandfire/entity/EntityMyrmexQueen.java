package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.entity.ai.*;
import com.google.common.base.Predicate;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class EntityMyrmexQueen extends EntityMyrmexBase {

    private static final ResourceLocation TEXTURE_DESERT = new ResourceLocation("iceandfire:textures/models/myrmex/myrmex_desert_queen.png");
    private static final ResourceLocation TEXTURE_JUNGLE = new ResourceLocation("iceandfire:textures/models/myrmex/myrmex_jungle_queen.png");
    public static final Animation ANIMATION_BITE = Animation.create(15);
    public static final Animation ANIMATION_STING = Animation.create(15);
    public static final Animation ANIMATION_EGG = Animation.create(20);
    private int eggTicks = 0;

    public EntityMyrmexQueen(World worldIn) {
        super(worldIn);
        this.setSize(2.9F, 1.86F);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound tag) {
        super.writeEntityToNBT(tag);
        tag.setInteger("EggTicks", eggTicks);

    }

    @Override
    public void readEntityFromNBT(NBTTagCompound tag) {
        super.readEntityFromNBT(tag);
        this.eggTicks = tag.getInteger("EggTicks");
    }


    public void onLivingUpdate() {
        super.onLivingUpdate();
        eggTicks++;

        if(eggTicks > 2000 || this.hive != null && this.hive.repopulate() && eggTicks > 2000){
            float radius = -5.25F;
            float angle = (0.01745329251F * this.renderYawOffset);
            double extraX = (double) (radius * MathHelper.sin((float) (Math.PI + angle)));
            double extraZ = (double) (radius * MathHelper.cos(angle));
            BlockPos eggPos = new BlockPos(this.posX + extraX, this.posY + 0.75F, this.posZ + extraZ);
            if(world.isAirBlock(eggPos)){
                this.setAnimation(ANIMATION_EGG);
                if(this.getAnimationTick() == 10){
                    EntityMyrmexEgg egg = new EntityMyrmexEgg(this.world);
                    egg.setJungle(this.isJungle());
                    egg.setMyrmexCaste(this.getRNG().nextInt(2));
                    egg.setLocationAndAngles(this.posX + extraX, this.posY + 0.75F, this.posZ + extraZ, 0, 0);
                    if(!world.isRemote){
                        world.spawnEntity(egg);
                    }
                    eggTicks = 0;
                }

            }


        }
        if (this.getAnimation() == ANIMATION_BITE && this.getAttackTarget() != null && this.getAnimationTick() == 6) {
            double dist = this.getDistanceSq(this.getAttackTarget());
            if (dist < 8) {
                this.getAttackTarget().attackEntityFrom(DamageSource.causeMobDamage(this), ((int) this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue()));
            }
        }
        if (this.getAnimation() == ANIMATION_STING && this.getAttackTarget() != null && this.getAnimationTick() == 6) {
            double dist = this.getDistanceSq(this.getAttackTarget());
            if (dist < 8) {
                this.getAttackTarget().attackEntityFrom(DamageSource.causeMobDamage(this), ((int) this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue() * 2));
                this.getAttackTarget().addPotionEffect(new PotionEffect(MobEffects.POISON, 200, 2));
                this.getAttackTarget().isAirBorne = true;
                float f = MathHelper.sqrt(0.5 * 0.5 + 0.5 * 0.5);
                this.getAttackTarget().motionX /= 2.0D;
                this.getAttackTarget().motionZ /= 2.0D;
                this.getAttackTarget().motionX -= 0.5 / (double) f * 4;
                this.getAttackTarget().motionZ -= 0.5 / (double) f * 4;

                if (this.getAttackTarget().onGround) {
                    this.getAttackTarget().motionY /= 2.0D;
                    this.getAttackTarget().motionY += 4;

                    if (this.getAttackTarget().motionY > 0.4000000059604645D) {
                        this.getAttackTarget().motionY = 0.4000000059604645D;
                    }
                }
            }
        }

    }

    protected void initEntityAI() {
        this.tasks.addTask(1, new EntityAIAttackMelee(this, 1.0D, true));
        this.tasks.addTask(3, new MyrmexAILeaveHive(this, 1.0D));
        this.tasks.addTask(3, new MyrmexAIReEnterHive(this, 1.0D));
        this.tasks.addTask(4, new MyrmexAIMoveThroughHive(this, 1.0D));
        this.tasks.addTask(4, new MyrmexAIWanderHiveCenter(this, 1.0D));
        this.tasks.addTask(5, new MyrmexAIWander(this, 1D));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        this.tasks.addTask(7, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new MyrmexAIDefendHive(this));
        this.targetTasks.addTask(2, new EntityAIHurtByTarget(this, false, new Class[0]));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityLiving.class, 10, true, true, new Predicate<EntityLiving>() {
            public boolean apply(@Nullable EntityLiving entity) {
                return entity != null && !IMob.VISIBLE_MOB_SELECTOR.apply(entity) && !EntityMyrmexBase.haveSameHive(EntityMyrmexQueen.this, entity);
            }
        }));

    }

    public boolean shouldMoveThroughHive(){
        return false;
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.2D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(10.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(120);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(15.0D);
    }

    @Override
    public ResourceLocation getAdultTexture() {
        return isJungle() ? TEXTURE_JUNGLE : TEXTURE_DESERT;
    }

    @Override
    public float getModelScale() {
        return 1.75F;
    }

    @Override
    public int getCasteImportance() {
        return 3;
    }

    public boolean shouldLeaveHive(){
        return false;
    }

    public boolean shouldEnterHive(){
        return true;
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

    @Override
    public Animation[] getAnimations() {
        return new Animation[]{ANIMATION_PUPA_WIGGLE, ANIMATION_BITE, ANIMATION_STING, ANIMATION_EGG};
    }
}
