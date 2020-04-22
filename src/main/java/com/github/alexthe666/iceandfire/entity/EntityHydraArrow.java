package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EntityHydraArrow  extends EntityArrow {

    public EntityHydraArrow(World worldIn) {
        super(worldIn);
        this.setDamage(5F);
    }

    public EntityHydraArrow(World worldIn, double x, double y, double z) {
        this(worldIn);
        this.setPosition(x, y, z);
        this.setDamage(5F);
    }

    public EntityHydraArrow(World worldIn, EntityLivingBase shooter) {
        super(worldIn, shooter);
        this.setDamage(5F);
    }

    public void onUpdate() {
        super.onUpdate();
        if (world.isRemote && !this.inGround) {
            double d0 = this.rand.nextGaussian() * 0.02D;
            double d1 = this.rand.nextGaussian() * 0.02D;
            double d2 = this.rand.nextGaussian() * 0.02D;
            double d3 = 10.0D;
            double xRatio = motionX * height;
            double zRatio = motionZ * height;
            IceAndFire.PROXY.spawnParticle("hydra", this.posX + xRatio + (double) (this.rand.nextFloat() * this.width * 1.0F) - (double) this.width - d0 * 10.0D, this.posY + (double) (this.rand.nextFloat() * this.height) - d1 * 10.0D, this.posZ + zRatio + (double) (this.rand.nextFloat() * this.width * 1.0F) - (double) this.width - d2 * 10.0D, 0.1D, 1.0D, 0.1D);
            IceAndFire.PROXY.spawnParticle("hydra", this.posX + xRatio + (double) (this.rand.nextFloat() * this.width * 1.0F) - (double) this.width - d0 * 10.0D, this.posY + (double) (this.rand.nextFloat() * this.height) - d1 * 10.0D, this.posZ + zRatio + (double) (this.rand.nextFloat() * this.width * 1.0F) - (double) this.width - d2 * 10.0D, 0.1D, 1.0D, 0.1D);

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

    protected void arrowHit(EntityLivingBase living) {
        if (living instanceof EntityPlayer) {
            this.damageShield((EntityPlayer) living, (float) this.getDamage());
        }
        living.addPotionEffect(new PotionEffect(MobEffects.POISON, 300, 0));
        if(this.shootingEntity instanceof EntityLivingBase){
            ((EntityLivingBase)this.shootingEntity).heal((float)this.getDamage());
        }
    }

    @Override
    protected ItemStack getArrowStack() {
        return new ItemStack(IafItemRegistry.hydra_arrow);
    }
}
