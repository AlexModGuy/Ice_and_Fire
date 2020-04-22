package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityStymphalianArrow extends EntityArrow {

    public EntityStymphalianArrow(World worldIn) {
        super(worldIn);
        this.setDamage(3.5F);
    }

    public EntityStymphalianArrow(World worldIn, double x, double y, double z) {
        this(worldIn);
        this.setPosition(x, y, z);
        this.setDamage(3.5F);
    }

    public EntityStymphalianArrow(World worldIn, EntityLivingBase shooter) {
        super(worldIn, shooter);
        this.setDamage(3.5F);
    }

    public void onUpdate() {
        super.onUpdate();
        float sqrt = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
        if (sqrt < 0.1F) {
            this.motionY -= 0.01F;
        }
    }

    protected void onHit(RayTraceResult raytraceResultIn) {
        if (raytraceResultIn.entityHit != null && raytraceResultIn.entityHit instanceof EntityPlayer) {
            this.damageShield((EntityPlayer) raytraceResultIn.entityHit, (float) this.getDamage());
        }
        super.onHit(raytraceResultIn);
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

    public boolean hasNoGravity() {
        return true;
    }

    @Override
    protected ItemStack getArrowStack() {
        return new ItemStack(IafItemRegistry.stymphalian_arrow);
    }
}
