package com.github.alexthe666.iceandfire.entity;

import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockGrass;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.init.Blocks;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.core.ModBlocks;
import com.github.alexthe666.iceandfire.structures.WorldUtils;

public class EntityDragonFire extends EntityFireball {

	public EntityDragonFire(World worldIn) {
		super(worldIn);
	}

	public EntityDragonFire(World worldIn, double posX, double posY, double posZ, double accelX, double accelY, double accelZ) {
		super(worldIn, posX, posY, posZ, accelX, accelY, accelZ);
	}

	public EntityDragonFire(World worldIn, EntityDragonBase shooter, double accelX, double accelY, double accelZ) {
		super(worldIn, shooter, accelX, accelY, accelZ);
	}

	public void setSizes(float width, float height) {
		this.setSize(width, height);
	}

	@Override
	public boolean canBeCollidedWith() {
		return false;
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		this.extinguish();
		for (int i = 0; i < 2; ++i) {
			this.worldObj.spawnParticle(EnumParticleTypes.FLAME, this.posX + ((this.rand.nextDouble() - 0.5D) * width), this.posY + ((this.rand.nextDouble() - 0.5D) * width), this.posZ + ((this.rand.nextDouble() - 0.5D) * width), 0.0D, 0.0D, 0.0D, new int[0]);
		}
		if (ticksExisted > 30)
			setDead();
	}

	@Override
	protected void onImpact(RayTraceResult movingObject) {

		if (!this.worldObj.isRemote) {

			if (movingObject.entityHit != null && !(movingObject.entityHit instanceof EntityDragonFire) && movingObject.entityHit != shootingEntity || movingObject.entityHit == null) {
				FireExplosion explosion = new FireExplosion(worldObj, shootingEntity, this.posX, this.posY, this.posZ, this.width, true);
				explosion.doExplosionA();
				explosion.doExplosionB(true);
				this.setDead();

			}

			if (movingObject.entityHit != null && !(movingObject.entityHit instanceof EntityDragonFire) && movingObject.entityHit != shootingEntity) {
				movingObject.entityHit.attackEntityFrom(IceAndFire.dragonFire, 6.0F);
				this.applyEnchantments(this.shootingEntity, movingObject.entityHit);
			} else {

				return;
			}

			if (movingObject.typeOfHit != Type.ENTITY || movingObject.entityHit != null && !(movingObject.entityHit instanceof EntityDragonFire)) {
				boolean flag = this.worldObj.getGameRules().getBoolean("mobGriefing");
				this.setDead();
			}
		}
	}

	private void triggerBlockTransformation(int a, int b, int c) {
		BlockPos pos = new BlockPos(a, b, c);
		if (worldObj.getBlockState(pos).getBlock() instanceof BlockBush) {
			WorldUtils.setBlock(worldObj, a, b, c, Blocks.DEADBUSH, 0, 2);
		}
		if (worldObj.getBlockState(pos).getBlock() instanceof BlockGrass) {
			WorldUtils.setBlock(worldObj, a, b, c, ModBlocks.charedGrass, 0, 2);
		}
		if (worldObj.getBlockState(pos).getBlock() instanceof BlockDirt) {
			WorldUtils.setBlock(worldObj, a, b, c, ModBlocks.charedDirt, 0, 2);
		}
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		return false;
	}

}