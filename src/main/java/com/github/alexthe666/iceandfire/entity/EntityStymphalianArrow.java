package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.IPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;

public class EntityStymphalianArrow extends AbstractArrowEntity {

    public EntityStymphalianArrow(EntityType<? extends AbstractArrowEntity> t, World worldIn) {
        super(t, worldIn);
        this.setBaseDamage(3.5F);
    }

    public EntityStymphalianArrow(EntityType<? extends AbstractArrowEntity> t, World worldIn, double x, double y,
        double z) {
        this(t, worldIn);
        this.setPos(x, y, z);
        this.setBaseDamage(3.5F);
    }

    public EntityStymphalianArrow(FMLPlayMessages.SpawnEntity spawnEntity, World world) {
        this(IafEntityRegistry.STYMPHALIAN_ARROW.get(), world);
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public EntityStymphalianArrow(EntityType t, World worldIn, LivingEntity shooter) {
        super(t, shooter, worldIn);
        this.setBaseDamage(3.5F);
    }

    @Override
    public void tick() {
        super.tick();
        float sqrt = MathHelper.sqrt(this.getDeltaMovement().x * this.getDeltaMovement().x + this.getDeltaMovement().z * this.getDeltaMovement().z);
        if (sqrt < 0.1F) {
            this.setDeltaMovement(this.getDeltaMovement().add(0, -0.01F, 0));
        }
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
    public boolean isNoGravity() {
        return true;
    }

    @Override
    protected ItemStack getPickupItem() {
        return new ItemStack(IafItemRegistry.STYMPHALIAN_ARROW);
    }
}
