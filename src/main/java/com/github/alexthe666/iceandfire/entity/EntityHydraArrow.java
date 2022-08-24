package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.enums.EnumParticles;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.IPacket;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;

public class EntityHydraArrow extends AbstractArrowEntity {

    public EntityHydraArrow(EntityType<? extends AbstractArrowEntity> t, World worldIn) {
        super(t, worldIn);
        this.setBaseDamage(5F);
    }

    public EntityHydraArrow(EntityType<? extends AbstractArrowEntity> t, World worldIn, double x, double y, double z) {
        this(t, worldIn);
        this.setPos(x, y, z);
        this.setBaseDamage(5F);
    }

    public EntityHydraArrow(FMLPlayMessages.SpawnEntity spawnEntity, World worldIn) {
        this(IafEntityRegistry.HYDRA_ARROW.get(), worldIn);
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }


    public EntityHydraArrow(EntityType t, World worldIn, LivingEntity shooter) {
        super(t, shooter, worldIn);
        this.setBaseDamage(5F);
    }

    @Override
    public void tick() {
        super.tick();
        if (level.isClientSide && !this.inGround) {
            double d0 = this.random.nextGaussian() * 0.02D;
            double d1 = this.random.nextGaussian() * 0.02D;
            double d2 = this.random.nextGaussian() * 0.02D;
            double d3 = 10.0D;
            double xRatio = this.getDeltaMovement().x * this.getBbHeight();
            double zRatio = this.getDeltaMovement().z * this.getBbHeight();
            IceAndFire.PROXY.spawnParticle(EnumParticles.Hydra, this.getX() + xRatio + (double) (this.random.nextFloat() * this.getBbWidth() * 1.0F) - (double) this.getBbWidth() - d0 * 10.0D, this.getY() + (double) (this.random.nextFloat() * this.getBbHeight()) - d1 * 10.0D, this.getZ() + zRatio + (double) (this.random.nextFloat() * this.getBbWidth() * 1.0F) - (double) this.getBbWidth() - d2 * 10.0D, 0.1D, 1.0D, 0.1D);
            IceAndFire.PROXY.spawnParticle(EnumParticles.Hydra, this.getX() + xRatio + (double) (this.random.nextFloat() * this.getBbWidth() * 1.0F) - (double) this.getBbWidth() - d0 * 10.0D, this.getY() + (double) (this.random.nextFloat() * this.getBbHeight()) - d1 * 10.0D, this.getZ() + zRatio + (double) (this.random.nextFloat() * this.getBbWidth() * 1.0F) - (double) this.getBbWidth() - d2 * 10.0D, 0.1D, 1.0D, 0.1D);
        }
    }

    protected void damageShield(PlayerEntity player, float damage) {
        if (damage >= 3.0F && player.getUseItem().getItem().isShield(player.getUseItem(), player)) {
            ItemStack copyBeforeUse = player.getUseItem().copy();
            int i = 1 + MathHelper.floor(damage);
            player.getUseItem().hurtAndBreak(i, player, (p_213360_0_) -> {
                p_213360_0_.broadcastBreakEvent(EquipmentSlotType.CHEST);
            });

            if (player.getUseItem().isEmpty()) {
                Hand Hand = player.getUsedItemHand();
                net.minecraftforge.event.ForgeEventFactory.onPlayerDestroyItem(player, copyBeforeUse, Hand);

                if (Hand == net.minecraft.util.Hand.MAIN_HAND) {
                    this.setItemSlot(EquipmentSlotType.MAINHAND, ItemStack.EMPTY);
                } else {
                    this.setItemSlot(EquipmentSlotType.OFFHAND, ItemStack.EMPTY);
                }
                player.stopUsingItem();
                this.playSound(SoundEvents.SHIELD_BREAK, 0.8F, 0.8F + this.level.random.nextFloat() * 0.4F);
            }
        }
    }

    @Override
    protected void doPostHurtEffects(LivingEntity living) {
        if (living instanceof PlayerEntity) {
            this.damageShield((PlayerEntity) living, (float) this.getBaseDamage());
        }
        living.addEffect(new EffectInstance(Effects.POISON, 300, 0));
        Entity shootingEntity = this.getOwner();
        if (shootingEntity instanceof LivingEntity) {
            ((LivingEntity) shootingEntity).heal((float) this.getBaseDamage());
        }
    }

    @Override
    protected ItemStack getPickupItem() {
        return new ItemStack(IafItemRegistry.HYDRA_ARROW);
    }
}
