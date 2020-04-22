package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityAmphithereArrow extends EntityArrow {

    public EntityAmphithereArrow(World worldIn) {
        super(worldIn);
        this.setDamage(2.5F);
    }

    public EntityAmphithereArrow(World worldIn, double x, double y, double z) {
        this(worldIn);
        this.setPosition(x, y, z);
        this.setDamage(2.5F);
    }

    public EntityAmphithereArrow(World worldIn, EntityLivingBase shooter) {
        super(worldIn, shooter);
        this.setDamage(2.5F);
    }

    public void onUpdate() {
        super.onUpdate();
        if ((ticksExisted == 1 || this.ticksExisted % 70 == 0) && !this.inGround && !this.onGround) {
            this.playSound(IafSoundRegistry.AMPHITHERE_GUST, 1, 1);
        }
        if (world.isRemote && !this.inGround) {
            double d0 = this.rand.nextGaussian() * 0.02D;
            double d1 = this.rand.nextGaussian() * 0.02D;
            double d2 = this.rand.nextGaussian() * 0.02D;
            double d3 = 10.0D;
            double xRatio = motionX * height;
            double zRatio = motionZ * height;
            this.world.spawnParticle(EnumParticleTypes.CLOUD, this.posX + xRatio + (double) (this.rand.nextFloat() * this.width * 1.0F) - (double) this.width - d0 * 10.0D, this.posY + (double) (this.rand.nextFloat() * this.height) - d1 * 10.0D, this.posZ + zRatio + (double) (this.rand.nextFloat() * this.width * 1.0F) - (double) this.width - d2 * 10.0D, d0, d1, d2);

        }
    }

    protected void arrowHit(EntityLivingBase living) {
        if (living instanceof EntityPlayer) {
            this.damageShield((EntityPlayer) living, (float) this.getDamage());
        }
        living.isAirBorne = true;
        double xRatio = motionX;
        double zRatio = motionZ;
        float strength = -1.4F;
        float f = MathHelper.sqrt(xRatio * xRatio + zRatio * zRatio);
        living.motionX /= 2.0D;
        living.motionZ /= 2.0D;
        living.motionX -= xRatio / (double) f * (double) strength;
        living.motionZ -= zRatio / (double) f * (double) strength;
        living.motionY = 0.6D;
        spawnExplosionParticle();
    }

    public void spawnExplosionParticle() {
        if (this.world.isRemote) {
            for (int height = 0; height < 1 + rand.nextInt(2); height++) {
                for (int i = 0; i < 20; ++i) {
                    double d0 = this.rand.nextGaussian() * 0.02D;
                    double d1 = this.rand.nextGaussian() * 0.02D;
                    double d2 = this.rand.nextGaussian() * 0.02D;
                    double d3 = 10.0D;
                    double xRatio = motionX * height;
                    double zRatio = motionZ * height;
                    this.world.spawnParticle(EnumParticleTypes.CLOUD, this.posX + xRatio + (double) (this.rand.nextFloat() * this.width * 5.0F) - (double) this.width - d0 * 10.0D, this.posY + (double) (this.rand.nextFloat() * this.height) - d1 * 10.0D + height, this.posZ + zRatio + (double) (this.rand.nextFloat() * this.width * 5.0F) - (double) this.width - d2 * 10.0D, d0, d1, d2);
                }
            }
        } else {
            this.world.setEntityState(this, (byte) 20);
        }
    }

    /**
     * Handler for {@link World#setEntityState}
     */
    @SideOnly(Side.CLIENT)
    public void handleStatusUpdate(byte id) {
        if (id == 20) {
            this.spawnExplosionParticle();
        } else {
            super.handleStatusUpdate(id);
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

    @Override
    protected ItemStack getArrowStack() {
        return new ItemStack(IafItemRegistry.amphithere_arrow);
    }
}
