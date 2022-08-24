package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.IPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;

public class EntitySeaSerpentArrow extends AbstractArrowEntity {

    public EntitySeaSerpentArrow(EntityType<? extends AbstractArrowEntity> t, World worldIn) {
        super(t, worldIn);
        this.setBaseDamage(3F);
    }

    public EntitySeaSerpentArrow(EntityType<? extends AbstractArrowEntity> t, World worldIn, double x, double y,
        double z) {
        this(t, worldIn);
        this.setPos(x, y, z);
        this.setBaseDamage(3F);
    }

    public EntitySeaSerpentArrow(FMLPlayMessages.SpawnEntity spawnEntity, World world) {
        this(IafEntityRegistry.SEA_SERPENT_ARROW.get(), world);
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }


    public EntitySeaSerpentArrow(EntityType t, World worldIn, LivingEntity shooter) {
        super(t, shooter, worldIn);
        this.setBaseDamage(3F);
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
            this.level.addParticle(ParticleTypes.BUBBLE, this.getX() + xRatio + this.random.nextFloat() * this.getBbWidth() * 1.0F - this.getBbWidth() - d0 * 10.0D, this.getY() + this.random.nextFloat() * this.getBbHeight() - d1 * 10.0D, this.getZ() + zRatio + this.random.nextFloat() * this.getBbWidth() * 1.0F - this.getBbWidth() - d2 * 10.0D, d0, d1, d2);
            this.level.addParticle(ParticleTypes.SPLASH, this.getX() + xRatio + this.random.nextFloat() * this.getBbWidth() * 1.0F - this.getBbWidth() - d0 * 10.0D, this.getY() + this.random.nextFloat() * this.getBbHeight() - d1 * 10.0D, this.getZ() + zRatio + this.random.nextFloat() * this.getBbWidth() * 1.0F - this.getBbWidth() - d2 * 10.0D, d0, d1, d2);

        }
    }

    @Override
    public boolean isInWater() {
        return false;
    }

    protected void damageShield(PlayerEntity player, float damage) {
        if (damage >= 3.0F && player.getUseItem().getItem().isShield(player.getUseItem(), player)) {
            ItemStack copyBeforeUse = player.getUseItem().copy();
            int i = 1 + MathHelper.floor(damage);
            player.getUseItem().hurtAndBreak(i, player, (entity) -> {
                entity.broadcastBreakEvent(EquipmentSlotType.MAINHAND);
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
    protected ItemStack getPickupItem() {
        return new ItemStack(IafItemRegistry.SEA_SERPENT_ARROW);
    }
}
