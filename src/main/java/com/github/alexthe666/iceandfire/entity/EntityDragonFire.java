package com.github.alexthe666.iceandfire.entity;

import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockGrass;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import com.github.alexthe666.iceandfire.core.ModBlocks;
import com.github.alexthe666.iceandfire.structures.WorldUtils;

public class EntityDragonFire extends EntityFireball
{

	public EntityDragonFire(World worldIn)
	{
		super(worldIn);
		this.setSize(1.7F, 1.7F);
	}

	public EntityDragonFire(World worldIn, double posX, double posY, double posZ, double accelX, double accelY, double accelZ)
	{
		super(worldIn, posX, posY, posZ, accelX, accelY, accelZ);
	}


	public EntityDragonFire(World worldIn, EntityLivingBase shooter, double accelX, double accelY, double accelZ)
	{
		super(worldIn, shooter, accelX, accelY, accelZ);
	}

	public void setSizes(float width, float height){
		this.setSize(width, height);
	}

    public boolean canBeCollidedWith()
    {
        return false;
    }
    
	public void onUpdate()
	{
		super.onUpdate();
		this.extinguish();

		for (int i = 0; i < 2; ++i)
		{
			this.worldObj.spawnParticle(EnumParticleTypes.FLAME, this.posX + ((this.rand.nextDouble() - 0.5D) * 1) * (double)this.width, this.posY + ((this.rand.nextDouble() - 0.5D) * 1) * (double)this.height, this.posZ + ((this.rand.nextDouble() - 0.5D) * 1) * (double)this.width, 0.0D, 0.0D, 0.0D, new int[0]);
		}
		if (ticksExisted > 30) setDead();
	}

	protected void onImpact(MovingObjectPosition movingObject){

		if (!this.worldObj.isRemote)
		{

			if (movingObject.entityHit != null && !(movingObject.entityHit instanceof EntityDragonFire) && movingObject.entityHit != shootingEntity || movingObject.entityHit == null){
				FireExplosion explosion = new FireExplosion(worldObj, shootingEntity, this.posX, this.posY, this.posZ, this.width, true);
				explosion.doExplosionA();
				explosion.doExplosionB(true);
			}

			if (movingObject.entityHit != null && !(movingObject.entityHit instanceof EntityDragonFire) && movingObject.entityHit != shootingEntity){
				movingObject.entityHit.attackEntityFrom(DamageSource.causeFireballDamage(this, movingObject.entityHit), 6.0F);
				this.applyEnchantments(this.shootingEntity, movingObject.entityHit);
			}else{

				return;
			}

			if (movingObject.typeOfHit != movingObject.typeOfHit.ENTITY || movingObject.entityHit != null && !(movingObject.entityHit instanceof EntityDragonFire)){
				boolean flag = this.worldObj.getGameRules().getBoolean("mobGriefing");
				this.setDead();
			}
		}
	}

	private void triggerBlockTransformation(int a, int b, int c) {
		BlockPos pos = new BlockPos(a, b, c);
		if(worldObj.getBlockState(pos).getBlock() instanceof BlockBush){
			WorldUtils.setBlock(worldObj, a, b, c, Blocks.deadbush, 0, 2);
		}
		if(worldObj.getBlockState(pos).getBlock() instanceof BlockGrass){
			WorldUtils.setBlock(worldObj, a, b, c, ModBlocks.charedGrass, 0, 2);
		}
		if(worldObj.getBlockState(pos).getBlock() instanceof BlockDirt){
			WorldUtils.setBlock(worldObj, a, b, c, ModBlocks.charedDirt, 0, 2);
		}		
	}

	public boolean attackEntityFrom(DamageSource source, float amount)
	{
		return false;
	}

}