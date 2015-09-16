package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.core.ModItems;
import com.github.alexthe666.iceandfire.enums.EnumDragonEgg;
import com.github.alexthe666.iceandfire.enums.EnumOrder;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityDragonEgg extends EntityLiving{

	public EntityDragonEgg(World worldIn) {
		super(worldIn);
		this.setSize(0.45F, 0.55F);
	}
	@Override
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0D);
		this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(10.0D);
	}
	public void writeEntityToNBT(NBTTagCompound tag)
	{
		super.writeEntityToNBT(tag);
		tag.setByte("Color", (byte)this.getType().meta);
	}

	public void readEntityFromNBT(NBTTagCompound tag)
	{
		super.readEntityFromNBT(tag);
		this.setType(EnumDragonEgg.byMetadata(tag.getByte("Color")));
	}

	protected void entityInit()
	{
		super.entityInit();
		this.dataWatcher.addObject(18, 0);
	}

	public EnumDragonEgg getType()
	{
		return EnumDragonEgg.byMetadata(this.dataWatcher.getWatchableObjectInt(18));
	}

	public void setType(EnumDragonEgg newtype)
	{
		this.dataWatcher.updateObject(18, newtype.meta);
	}

	public String getTexture(){
		String i = getType().isFire ? "firedragon/" : "icedragon/";
		return "iceandfire:textures/models/" + i + "egg_" + getType().name().toLowerCase() + ".png";

	}
	public boolean attackEntityFrom(DamageSource source, float amount)
	{
		if (source.getEntity() instanceof EntityPlayer && !((EntityPlayer)source.getEntity()).capabilities.allowEdit)
		{
			this.worldObj.spawnEntityInWorld(new EntityItem(worldObj, this.posX, this.posY, this.posZ, this.getItem()));
			this.setDead();
			return false;
		}else{
			return super.attackEntityFrom(source, amount);
		}

	}

	private ItemStack getItem() {
		switch(getType().meta){
		default:
			return new ItemStack(ModItems.dragonegg_red);
		case 1:
			return new ItemStack(ModItems.dragonegg_green);
		case 2:
			return new ItemStack(ModItems.dragonegg_bronze);
		case 3:
			return new ItemStack(ModItems.dragonegg_gray);
		case 4:
			return new ItemStack(ModItems.dragonegg_blue);
		case 5:
			return new ItemStack(ModItems.dragonegg_white);
		}
	}

}
