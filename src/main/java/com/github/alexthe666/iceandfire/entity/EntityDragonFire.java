package com.github.alexthe666.iceandfire.entity;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.github.alexthe666.iceandfire.core.ModBlocks;
import com.github.alexthe666.iceandfire.structures.BlockMeta;
import com.github.alexthe666.iceandfire.structures.WorldUtils;

public class EntityDragonFire extends EntityFireball
{

	public EntityDragonFire(World worldIn)
	{
		super(worldIn);
	}

	public EntityDragonFire(World worldIn, double posX, double posY, double posZ, double accelX, double accelY, double accelZ)
	{
		super(worldIn, posX, posY, posZ, accelX, accelY, accelZ);
	}

	public EntityDragonFire(World worldIn, EntityLivingBase shooter, double accelX, double accelY, double accelZ)
	{
		super(worldIn, shooter, accelX, accelY, accelZ);
	}

	public void onUpdate()
	{
		super.onUpdate();
		this.extinguish();

		if(this.width < 2.5)
			this.setSize(width + 0.00005F * ticksExisted, height + 0.00005F * ticksExisted);
		for (int i = 0; i < 2; ++i)
		{
			this.worldObj.spawnParticle(EnumParticleTypes.FLAME, this.posX + (this.rand.nextDouble() - 0.5D) * (double)this.width, this.posY + (this.rand.nextDouble() - 0.5D) * (double)this.height, this.posZ + (this.rand.nextDouble() - 0.5D) * (double)this.width, 0.0D, 0.0D, 0.0D, new int[0]);
		}
		if (ticksExisted > 25) setDead();
	}

	protected void onImpact(MovingObjectPosition movingObject){

		if (!this.worldObj.isRemote)
		{
			if (movingObject.entityHit != null && !(movingObject.entityHit instanceof EntityDragonFire)){
				movingObject.entityHit.attackEntityFrom(DamageSource.causeFireballDamage(this, movingObject.entityHit), 6.0F);
				this.func_174815_a(this.shootingEntity, movingObject.entityHit);
			}
			if (movingObject.typeOfHit != movingObject.typeOfHit.ENTITY || movingObject.entityHit != null && !(movingObject.entityHit instanceof EntityDragonFire)){
				boolean flag = this.worldObj.getGameRules().getGameRuleBooleanValue("mobGriefing");
				//this.worldObj.newExplosion((Entity)null, this.posX, this.posY, this.posZ, (float)1, flag, flag);
				WorldUtils.setBlock(worldObj, (int)posX, (int)posY, (int)posZ, Blocks.fire, 0, 3);
				this.setDead();
			}
		}
	}

	public boolean attackEntityFrom(DamageSource source, float amount)
	{
		return false;
	}


}