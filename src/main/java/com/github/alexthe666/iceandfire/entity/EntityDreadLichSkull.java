package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collections;
import java.util.List;

public class EntityDreadLichSkull extends EntityArrow {

    public EntityAINearestAttackableTarget.Sorter targetSorter;

    public EntityDreadLichSkull(World worldIn) {
        super(worldIn);
        this.setDamage(6F);
    }

    public EntityDreadLichSkull(World worldIn, double x, double y, double z) {
        this(worldIn);
        this.setPosition(x, y, z);
        this.setDamage(6F);
    }

    public EntityDreadLichSkull(World worldIn, EntityLivingBase shooter, double x, double y, double z) {
        super(worldIn, shooter);
        this.setDamage(6);
        targetSorter = new EntityAINearestAttackableTarget.Sorter(shooter);
    }

    public EntityDreadLichSkull(World worldIn, EntityLivingBase shooter, double dmg) {
        super(worldIn, shooter);
        this.setDamage(dmg);
        targetSorter = new EntityAINearestAttackableTarget.Sorter(shooter);
    }

    public boolean isInWater() {
        return false;
    }

    @Override
    protected void entityInit() {
        super.entityInit();
    }

    public void onUpdate() {
        float sqrt = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
        boolean flag = true;
        if(this.shootingEntity != null && this.shootingEntity instanceof EntityLiving && ((EntityLiving) this.shootingEntity).getAttackTarget() != null){
            EntityLivingBase target = ((EntityLiving) this.shootingEntity).getAttackTarget();
            double minusX = target.posX - this.posX;
            double minusY = target.posY - this.posY;
            double minusZ = target.posZ - this.posZ;
            double speed = 0.15D;
            this.motionX += minusX * speed * 0.1D;
            this.motionY += minusY * speed * 0.1D;
            this.motionZ += minusZ * speed * 0.1D;
        }
        if(this.shootingEntity instanceof EntityPlayer){
            EntityLivingBase target = ((EntityPlayer) this.shootingEntity).getAttackingEntity();
            if(target == null || !target.isEntityAlive()){
                double d0 = 10;
                List<EntityLivingBase> list = world.getEntitiesWithinAABB(EntityLivingBase.class, (new AxisAlignedBB(this.posX, this.posY, this.posZ, this.posX + 1.0D, this.posY + 1.0D, this.posZ + 1.0D)).grow(d0, 10.0D, d0), IMob.VISIBLE_MOB_SELECTOR);
                if(targetSorter != null){
                    Collections.sort(list, targetSorter);
                }
                if(!list.isEmpty()){
                    target = list.get(0);
                }
            }
            if(target != null && target.isEntityAlive()){
                double minusX = target.posX - this.posX;
                double minusY = target.posY + target.getEyeHeight() - this.posY;
                double minusZ = target.posZ - this.posZ;
                double speed = 0.25D * Math.min(this.getDistance(target), 10D) / 10D;
                this.motionX += (Math.signum(minusX) * 0.5D - this.motionX) * 0.10000000149011612D;
                this.motionY += (Math.signum(minusY) * 0.5D - this.motionY) * 0.10000000149011612D;
                this.motionZ += (Math.signum(minusZ) * 0.5D - this.motionZ) * 0.10000000149011612D;
                float f1 = MathHelper.sqrt(motionX * motionX + motionZ * motionZ);
                this.rotationYaw = (float)(MathHelper.atan2(motionX, motionZ) * (180D / Math.PI));
                this.rotationPitch = (float)(MathHelper.atan2(motionY, (double)f1) * (180D / Math.PI));
                flag  = false;
            }
        }
        if ((sqrt < 0.1F || this.collided || this.inGround) && this.ticksExisted > 5 && flag) {
            this.setDead();
        }
        double d0 = 0;
        double d1 = 0.01D;
        double d2 = 0D;
        double x = this.posX + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width;
        double y = this.posY + (double) (this.rand.nextFloat() * this.height) - (double) this.height;
        double z = this.posZ + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width;
        float f = (this.width + this.height + this.width) * 0.333F + 0.5F;
        if (particleDistSq(x, y, z) < f * f) {
            IceAndFire.PROXY.spawnParticle("dread_torch", x, y + 0.5D, z, d0, d1, d2);
        }
        super.onUpdate();
    }

    public double particleDistSq(double toX, double toY, double toZ) {
        double d0 = posX - toX;
        double d1 = posY - toY;
        double d2 = posZ - toZ;
        return d0 * d0 + d1 * d1 + d2 * d2;
    }

    public void playSound(SoundEvent soundIn, float volume, float pitch) {
        if (!this.isSilent() && soundIn != SoundEvents.ENTITY_ARROW_HIT && soundIn != SoundEvents.ENTITY_ARROW_HIT_PLAYER) {
            this.world.playSound(null, this.posX, this.posY, this.posZ, soundIn, this.getSoundCategory(), volume, pitch);
        }
    }

    protected void onHit(RayTraceResult raytraceResultIn)
    {
        Entity entity = raytraceResultIn.entityHit;
        if (entity != null) {
            if(this.shootingEntity != null && entity.isOnSameTeam(this.shootingEntity)){
                return;
            }
        }
        super.onHit(raytraceResultIn);
    }
    protected void arrowHit(EntityLivingBase living) {
        super.arrowHit(living);
        if (living != null && (this.shootingEntity == null || !living.isEntityEqual(this.shootingEntity))){
            if (living instanceof EntityPlayer) {
                this.damageShield((EntityPlayer) living, (float) this.getDamage());
            }
        }
    }

    protected void damageShield(EntityPlayer player, float damage) {
        if (damage >= 3.0F && player.getActiveItemStack().getItem().isShield(player.getActiveItemStack(), player)) {
            ItemStack copyBeforeUse = player.getActiveItemStack().copy();
            int i = 1 + MathHelper.floor(damage);
            player.getActiveItemStack().damageItem(i, player);

            if (player.getActiveItemStack().isEmpty()) {
                EnumHand enumhand = player.getActiveHand();
                net.minecraftforge.event.ForgeEventFactory.onPlayerDestroyItem(player, copyBeforeUse, enumhand);

                if (enumhand == EnumHand.MAIN_HAND) {
                    this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, ItemStack.EMPTY);
                } else {
                    this.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, ItemStack.EMPTY);
                }
                player.resetActiveHand();
                this.playSound(SoundEvents.ITEM_SHIELD_BREAK, 0.8F, 0.8F + this.world.rand.nextFloat() * 0.4F);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public int getBrightnessForRender() {
        return 15728880;
    }

    public float getBrightness() {
        return 1.0F;
    }

    public boolean hasNoGravity() {
        return true;
    }

    @Override
    protected ItemStack getArrowStack() {
        return ItemStack.EMPTY;
    }
}
