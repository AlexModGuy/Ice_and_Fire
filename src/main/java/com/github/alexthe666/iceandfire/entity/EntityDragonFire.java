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

	protected boolean isFireballFiery() {
		return false;
	}

	@Override
	public boolean canBeCollidedWith() {
		return false;
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		for (int i = 0; i < 6; ++i) {
			IceAndFire.PROXY.spawnParticle("dragonfire", worldObj, this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D);
		}
		this.setSize((30 - Math.min(ticksExisted, 30)) * 0.05F, (30 - Math.min(ticksExisted, 30)) * 0.05F);
		if (ticksExisted > 30) {
			setDead();
		}
		if (this.onGround) {
		}
	}

	@Override
	protected void onImpact(RayTraceResult movingObject) {

		if (!this.worldObj.isRemote) {

			if (movingObject.entityHit != null && !(movingObject.entityHit instanceof EntityDragonFire) && movingObject.entityHit != shootingEntity || movingObject.entityHit == null) {
				FireExplosion explosion = new FireExplosion(worldObj, shootingEntity, this.posX, this.posY, this.posZ, 2, true);
				explosion.doExplosionA();
				explosion.doExplosionB(true);
				this.setDead();

			}
			if (movingObject.entityHit != null && !(movingObject.entityHit instanceof EntityDragonFire) && movingObject.entityHit != shootingEntity) {
				movingObject.entityHit.attackEntityFrom(IceAndFire.dragonFire, 6.0F);
				this.applyEnchantments(this.shootingEntity, movingObject.entityHit);
				this.setDead();
			}

			if (movingObject.typeOfHit != Type.ENTITY || movingObject.entityHit != null && !(movingObject.entityHit instanceof EntityDragonFire)) {
				boolean flag = this.worldObj.getGameRules().getBoolean("mobGriefing");
				this.setDead();
			}
		}
		this.setDead();
	}
	
	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		return false;
	}

	public float getCollisionBorderSize() {
		return 0F;
	}

}