package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.enums.EnumParticles;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import org.jetbrains.annotations.NotNull;

public class EntityHydraArrow extends AbstractArrow {

    public EntityHydraArrow(EntityType<? extends AbstractArrow> t, Level worldIn) {
        super(t, worldIn);
        this.setBaseDamage(5F);
    }

    public EntityHydraArrow(EntityType<? extends AbstractArrow> t, Level worldIn, double x, double y, double z) {
        this(t, worldIn);
        this.setPos(x, y, z);
        this.setBaseDamage(5F);
    }

    public EntityHydraArrow(PlayMessages.SpawnEntity spawnEntity, Level worldIn) {
        this(IafEntityRegistry.HYDRA_ARROW.get(), worldIn);
    }

    @Override
    public @NotNull Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }


    public EntityHydraArrow(EntityType t, Level worldIn, LivingEntity shooter) {
        super(t, shooter, worldIn);
        this.setBaseDamage(5F);
    }

    @Override
    public void tick() {
        super.tick();
        if (level().isClientSide && !this.inGround) {
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

    protected void damageShield(Player player, float damage) {
        if (damage >= 3.0F && player.getUseItem().getItem().canPerformAction(player.getUseItem(), ToolActions.SHIELD_BLOCK)) {
            ItemStack copyBeforeUse = player.getUseItem().copy();
            int i = 1 + Mth.floor(damage);
            player.getUseItem().hurtAndBreak(i, player, (p_213360_0_) -> {
                p_213360_0_.broadcastBreakEvent(EquipmentSlot.CHEST);
            });

            if (player.getUseItem().isEmpty()) {
                InteractionHand Hand = player.getUsedItemHand();
                net.minecraftforge.event.ForgeEventFactory.onPlayerDestroyItem(player, copyBeforeUse, Hand);

                if (Hand == net.minecraft.world.InteractionHand.MAIN_HAND) {
                    this.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
                } else {
                    this.setItemSlot(EquipmentSlot.OFFHAND, ItemStack.EMPTY);
                }
                player.stopUsingItem();
                this.playSound(SoundEvents.SHIELD_BREAK, 0.8F, 0.8F + this.level().random.nextFloat() * 0.4F);
            }
        }
    }

    @Override
    protected void doPostHurtEffects(@NotNull LivingEntity living) {
        if (living instanceof Player) {
            this.damageShield((Player) living, (float) this.getBaseDamage());
        }
        living.addEffect(new MobEffectInstance(MobEffects.POISON, 300, 0));
        Entity shootingEntity = this.getOwner();
        if (shootingEntity instanceof LivingEntity) {
            ((LivingEntity) shootingEntity).heal((float) this.getBaseDamage());
        }
    }

    @Override
    protected @NotNull ItemStack getPickupItem() {
        return new ItemStack(IafItemRegistry.HYDRA_ARROW.get());
    }
}
