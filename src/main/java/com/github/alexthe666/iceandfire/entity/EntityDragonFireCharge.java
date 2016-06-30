package com.github.alexthe666.iceandfire.entity;

import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;

import com.github.alexthe666.iceandfire.IceAndFire;

public class EntityDragonFireCharge extends EntityFireball {

	public EntityDragonFireCharge(World worldIn) {
		super(worldIn);
	}

	public EntityDragonFireCharge(World worldIn, double posX, double posY, double posZ, double accelX, double accelY, double accelZ) {
		super(worldIn, posX, posY, posZ, accelX, accelY, accelZ);
	}

	public EntityDragonFireCharge(World worldIn, EntityDragonBase shooter, double accelX, double accelY, double accelZ) {
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
		for (int i = 0; i < 4; ++i) {
			this.worldObj.spawnParticle(EnumParticleTypes.FLAME, this.posX + ((this.rand.nextDouble() - 0.5D) * width), this.posY + ((this.rand.nextDouble() - 0.5D) * width), this.posZ + ((this.rand.nextDouble() - 0.5D) * width), 0.0D, 0.0D, 0.0D, new int[0]);
		}
	}

	@Override
	protected void onImpact(RayTraceResult movingObject) {

		if (!this.worldObj.isRemote) {

			if (movingObject.entityHit != null && !(movingObject.entityHit instanceof EntityDragonFireCharge) && movingObject.entityHit != shootingEntity || movingObject.entityHit == null) {
				FireExplosion explosion = new FireExplosion(worldObj, shootingEntity, this.posX, this.posY, this.posZ, 2, true);
				explosion.doExplosionA();
				explosion.doExplosionB(true);
				this.worldObj.createExplosion(this.shootingEntity, this.posX, this.posY, this.posZ, 1, true);
				this.setDead();

			}
			if (movingObject.entityHit != null && !(movingObject.entityHit instanceof EntityDragonFireCharge) && movingObject.entityHit != shootingEntity) {
				movingObject.entityHit.attackEntityFrom(IceAndFire.dragonFire, 6.0F);
				this.applyEnchantments(this.shootingEntity, movingObject.entityHit);
				((EntityDragonBase)this.shootingEntity).attackDecision = true;
				FireExplosion explosion = new FireExplosion(worldObj, shootingEntity, this.posX, this.posY, this.posZ, 2, true);
				explosion.doExplosionA();
				explosion.doExplosionB(true);
				this.worldObj.createExplosion(this, this.posX, this.posY, this.posZ, 4, true);
				this.setDead();
			}

			if (movingObject.typeOfHit != Type.ENTITY || movingObject.entityHit != null && !(movingObject.entityHit instanceof EntityDragonFireCharge)) {
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