package com.github.alexthe666.iceandfire.entity;

import java.util.Collection;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.github.alexthe666.iceandfire.core.ModItems;
import com.google.common.collect.Sets;

public class EntityDragonArrow extends EntityArrow
{

    public EntityDragonArrow(World worldIn)
    {
        super(worldIn);
    }

    public EntityDragonArrow(World worldIn, double x, double y, double z)
    {
        super(worldIn, x, y, z);
    }

    public EntityDragonArrow(World worldIn, EntityLivingBase shooter)
    {
        super(worldIn, shooter);
    }

    public void writeEntityToNBT(NBTTagCompound tagCompound)
    {
    	super.writeEntityToNBT(tagCompound);
        tagCompound.setDouble("damage", 5);
    }

    public void readEntityFromNBT(NBTTagCompound tagCompund)
    {
    	super.readEntityFromNBT(tagCompund);
    	tagCompund.setDouble("damage", 5);
    }

	@Override
	protected ItemStack getArrowStack() {
		return ModItems.dragonbone_arrow;
	}

}