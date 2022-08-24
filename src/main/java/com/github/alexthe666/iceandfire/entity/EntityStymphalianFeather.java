package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.IPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;

public class EntityStymphalianFeather extends AbstractArrowEntity {

    public EntityStymphalianFeather(EntityType<? extends AbstractArrowEntity> t, World worldIn) {
        super(t, worldIn);
    }

    public EntityStymphalianFeather(EntityType<? extends AbstractArrowEntity> t, World worldIn, LivingEntity shooter) {
        super(t, shooter, worldIn);
        this.setBaseDamage(IafConfig.stymphalianBirdFeatherAttackStength);
    }

    public EntityStymphalianFeather(FMLPlayMessages.SpawnEntity spawnEntity, World world) {
        this(IafEntityRegistry.STYMPHALIAN_FEATHER.get(), world);
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void remove() {
        super.remove();
        if (IafConfig.stymphalianBirdFeatherDropChance > 0) {
            if (!level.isClientSide && this.random.nextInt(IafConfig.stymphalianBirdFeatherDropChance) == 0) {
                this.spawnAtLocation(getPickupItem(), 0.1F);
            }
        }

    }

    @Override
    public void tick() {
        super.tick();
        if (this.tickCount > 100) {
            this.remove();
        }
    }

    @Override
    protected void onHitEntity(EntityRayTraceResult entityHit) {
        Entity shootingEntity = this.getOwner();
        if (shootingEntity instanceof EntityStymphalianBird && entityHit.getEntity() != null && entityHit.getEntity() instanceof EntityStymphalianBird) {
            return;
        } else {
            super.onHitEntity(entityHit);
            if (entityHit.getEntity() != null && entityHit.getEntity() instanceof EntityStymphalianBird) {
                LivingEntity LivingEntity = (LivingEntity) entityHit.getEntity();
                LivingEntity.setArrowCount(LivingEntity.getArrowCount() - 1);
                ItemStack itemstack1 = LivingEntity.isUsingItem() ? LivingEntity.getUseItem() : ItemStack.EMPTY;
                if (itemstack1.getItem().isShield(itemstack1, LivingEntity)) {
                    damageShield(LivingEntity, 1.0F);
                }
            }

        }
    }

    protected void damageShield(LivingEntity entity, float damage) {
        if (damage >= 3.0F && entity.getUseItem().getItem().isShield(entity.getUseItem(), entity)) {
            ItemStack copyBeforeUse = entity.getUseItem().copy();
            int i = 1 + MathHelper.floor(damage);
            Hand Hand = entity.getUsedItemHand();
            copyBeforeUse.hurtAndBreak(i, entity, (player1) -> {
                player1.broadcastBreakEvent(Hand);
            });
            if (entity.getUseItem().isEmpty()) {
                if (entity instanceof PlayerEntity) {
                    net.minecraftforge.event.ForgeEventFactory.onPlayerDestroyItem((PlayerEntity) entity, copyBeforeUse, Hand);
                }

                if (Hand == net.minecraft.util.Hand.MAIN_HAND) {
                    this.setItemSlot(EquipmentSlotType.MAINHAND, ItemStack.EMPTY);
                } else {
                    this.setItemSlot(EquipmentSlotType.OFFHAND, ItemStack.EMPTY);
                }
                this.playSound(SoundEvents.SHIELD_BREAK, 0.8F, 0.8F + this.level.random.nextFloat() * 0.4F);
            }
        }
    }

    @Override
    protected ItemStack getPickupItem() {
        return new ItemStack(IafItemRegistry.STYMPHALIAN_BIRD_FEATHER);
    }
}
