package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import org.jetbrains.annotations.NotNull;

public class EntityDragonArrow extends AbstractArrow {

    public EntityDragonArrow(EntityType<? extends AbstractArrow> typeIn, Level worldIn) {
        super(typeIn, worldIn);
        this.setBaseDamage(10);
    }

    public EntityDragonArrow(EntityType<? extends AbstractArrow> typeIn, double x, double y, double z,
                             Level world) {
        super(typeIn, x, y, z, world);
        this.setBaseDamage(10);
    }

    public EntityDragonArrow(PlayMessages.SpawnEntity spawnEntity, Level worldIn) {
        this(IafEntityRegistry.DRAGON_ARROW.get(), worldIn);
    }

    @Override
    public @NotNull Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public EntityDragonArrow(EntityType<? extends AbstractArrow> typeIn, LivingEntity shooter, Level worldIn) {
        super(typeIn, shooter, worldIn);
        this.setBaseDamage(10.0F);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag tagCompound) {
        super.addAdditionalSaveData(tagCompound);
        tagCompound.putDouble("damage", 10);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag tagCompund) {
        super.readAdditionalSaveData(tagCompund);
        this.setBaseDamage(tagCompund.getDouble("damage"));
    }

    @Override
    protected @NotNull ItemStack getPickupItem() {
        return new ItemStack(IafItemRegistry.DRAGONBONE_ARROW.get());
    }

}