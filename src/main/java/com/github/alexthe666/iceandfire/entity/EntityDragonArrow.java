package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;

public class EntityDragonArrow extends AbstractArrowEntity {

    public EntityDragonArrow(EntityType typeIn, World worldIn) {
        super(typeIn, worldIn);
        this.setDamage(10);
    }

    public EntityDragonArrow(EntityType typeIn, double x, double y, double z, World world) {
        super(typeIn, x, y, z, world);
        this.setDamage(10);
    }

    public EntityDragonArrow(EntityType typeIn, LivingEntity shooter, World worldIn) {
        super(typeIn, shooter, worldIn);
    }

    @Override
    public void writeAdditional(CompoundNBT tagCompound) {
        super.writeAdditional(tagCompound);
        tagCompound.putDouble("damage", 10);
    }

    @Override
    public void readAdditional(CompoundNBT tagCompund) {
        super.readAdditional(tagCompund);
        this.setDamage(tagCompund.getDouble("damage"));
    }

    @Override
    protected ItemStack getArrowStack() {
        return new ItemStack(IafItemRegistry.DRAGONBONE_ARROW);
    }

}