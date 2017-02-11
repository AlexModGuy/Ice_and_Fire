package com.github.alexthe666.iceandfire.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import com.github.alexthe666.iceandfire.core.ModItems;

public class EntityDragonArrow extends EntityArrow {

	public EntityDragonArrow(World worldIn) {
		super(worldIn);
		this.setDamage(8);
	}

	public EntityDragonArrow(World worldIn, double x, double y, double z) {
		super(worldIn, x, y, z);
	}

	public EntityDragonArrow(World worldIn, EntityLivingBase shooter) {
		super(worldIn, shooter);
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound tagCompound) {
		super.writeEntityToNBT(tagCompound);
		tagCompound.setDouble("damage", 8);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound tagCompund) {
		super.readEntityFromNBT(tagCompund);
		tagCompund.setDouble("damage", 8);
	}

	@Override
	protected ItemStack getArrowStack() {
		return new ItemStack(ModItems.dragonbone_arrow);
	}

}