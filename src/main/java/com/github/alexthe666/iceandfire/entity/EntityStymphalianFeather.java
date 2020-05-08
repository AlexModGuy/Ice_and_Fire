package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityStymphalianFeather extends EntityArrow {

    public EntityStymphalianFeather(World worldIn) {
        super(worldIn);
    }

    public EntityStymphalianFeather(World worldIn, LivingEntity shooter) {
        super(worldIn, shooter);
        this.setDamage(IafConfig.stymphalianBirdFeatherAttackStength);
    }

    public void setDead() {
        super.setDead();
        if (IafConfig.stymphalianBirdFeatherDropChance > 0) {
            if (!world.isRemote && this.rand.nextInt(IafConfig.stymphalianBirdFeatherDropChance) == 0) {
                this.entityDropItem(getArrowStack(), 0.1F);
            }
        }

    }

    public void onUpdate() {
        super.onUpdate();
        if (this.ticksExisted > 100) {
            this.setDead();
        }
    }

    protected void onHit(RayTraceResult raytraceResultIn) {
        if (this.shootingEntity instanceof EntityStymphalianBird && raytraceResultIn.entityHit != null && raytraceResultIn.entityHit instanceof EntityStymphalianBird) {
            return;
        } else {
            super.onHit(raytraceResultIn);
            if (raytraceResultIn.entityHit != null && raytraceResultIn.entityHit instanceof LivingEntity) {
                LivingEntity LivingEntity = (LivingEntity) raytraceResultIn.entityHit;
                LivingEntity.setArrowCountInEntity(LivingEntity.getArrowCountInEntity() - 1);
                ItemStack itemstack1 = LivingEntity.isHandActive() ? LivingEntity.getActiveItemStack() : ItemStack.EMPTY;
                if (itemstack1.getItem().isShield(itemstack1, LivingEntity)) {
                    damageShield(LivingEntity, 1.0F);
                }
            }

        }
    }

    protected void damageShield(LivingEntity entity, float damage) {
        if (damage >= 3.0F && entity.getActiveItemStack().getItem().isShield(entity.getActiveItemStack(), entity)) {
            ItemStack copyBeforeUse = entity.getActiveItemStack().copy();
            int i = 1 + MathHelper.floor(damage);
            entity.getActiveItemStack().damageItem(i, entity);

            if (entity.getActiveItemStack().isEmpty()) {
                Hand Hand = entity.getActiveHand();
                if (entity instanceof PlayerEntity) {
                    net.minecraftforge.event.ForgeEventFactory.onPlayerDestroyItem((PlayerEntity) entity, copyBeforeUse, Hand);
                }

                if (Hand == Hand.MAIN_HAND) {
                    this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, ItemStack.EMPTY);
                } else {
                    this.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, ItemStack.EMPTY);
                }
                this.playSound(SoundEvents.ITEM_SHIELD_BREAK, 0.8F, 0.8F + this.world.rand.nextFloat() * 0.4F);
            }
        }
    }

    @Override
    protected ItemStack getArrowStack() {
        return new ItemStack(IafItemRegistry.STYMPHALIAN_BIRD_FEATHER);
    }
}
