package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.core.ModAchievements;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;

public class EntityDragonIceCharge extends EntityFireball implements IDragonProjectile {

	public EntityDragonIceCharge(World worldIn) {
		super(worldIn);

	}

	public EntityDragonIceCharge(World worldIn, double posX, double posY, double posZ, double accelX, double accelY, double accelZ) {
		super(worldIn, posX, posY, posZ, accelX, accelY, accelZ);
		double d0 = (double) MathHelper.sqrt_double(accelX * accelX + accelY * accelY + accelZ * accelZ);
		this.accelerationX = accelX / d0 * 0.07D;
		this.accelerationY = accelY / d0 * 0.07D;
		this.accelerationZ = accelZ / d0 * 0.07D;
	}

	public EntityDragonIceCharge(World worldIn, EntityDragonBase shooter, double accelX, double accelY, double accelZ) {
		super(worldIn, shooter, accelX, accelY, accelZ);
		double d0 = (double) MathHelper.sqrt_double(accelX * accelX + accelY * accelY + accelZ * accelZ);
		this.accelerationX = accelX / d0 * 0.07D;
		this.accelerationY = accelY / d0 * 0.07D;
		this.accelerationZ = accelZ / d0 * 0.07D;
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
		for (int i = 0; i < 14; ++i) {
			IceAndFire.PROXY.spawnParticle("snowflake", worldObj, this.posX + this.rand.nextDouble() * 1 * (this.rand.nextBoolean() ? -1 : 1), this.posY + this.rand.nextDouble() * 1 * (this.rand.nextBoolean() ? -1 : 1), this.posZ + this.rand.nextDouble() * 1 * (this.rand.nextBoolean() ? -1 : 1), 0.0D, 0.0D, 0.0D);
		}
	}

	@Override
	protected void onImpact(RayTraceResult movingObject) {
		if (!this.worldObj.isRemote) {
			if (movingObject.entityHit != null && movingObject.entityHit instanceof IDragonProjectile){
				return;
			}
			if (movingObject.entityHit != null && !(movingObject.entityHit instanceof IDragonProjectile) && movingObject.entityHit != shootingEntity || movingObject.entityHit == null) {
				if (this.shootingEntity != null && (movingObject.entityHit == this.shootingEntity || (this.shootingEntity instanceof EntityDragonBase & movingObject.entityHit instanceof EntityTameable && ((EntityDragonBase) shootingEntity).isOwner(((EntityDragonBase) shootingEntity).getOwner())))) {
					return;
				}
				if(this.shootingEntity != null) {
					IceExplosion explosion = new IceExplosion(worldObj, shootingEntity, this.posX, this.posY, this.posZ, 2 + ((EntityDragonBase) this.shootingEntity).getDragonStage(), true);
					explosion.doExplosionA();
					explosion.doExplosionB(true);
					FireChargeExplosion explosion2 = new FireChargeExplosion(worldObj, shootingEntity, this.posX, this.posY, this.posZ, 2 + ((EntityDragonBase) this.shootingEntity).getDragonStage(), false, false);
					explosion2.doExplosionA();
					explosion2.doExplosionB(true);
				}
				this.setDead();
			}
			if (movingObject.entityHit != null && !(movingObject.entityHit instanceof IDragonProjectile) && movingObject.entityHit != shootingEntity) {
				if (this.shootingEntity != null && (movingObject.entityHit == this.shootingEntity || (this.shootingEntity instanceof EntityDragonBase & movingObject.entityHit instanceof EntityTameable && ((EntityDragonBase) shootingEntity).getOwner() == ((EntityTameable) movingObject.entityHit).getOwner()))) {
					return;
				}
				if(this.shootingEntity != null){
					movingObject.entityHit.attackEntityFrom(IceAndFire.dragonFire, 10.0F);
					if(movingObject.entityHit instanceof EntityLivingBase && ((EntityLivingBase)movingObject.entityHit).getHealth() == 0){
						((EntityDragonBase) this.shootingEntity).attackDecision = true;
					}
				}
				movingObject.entityHit.setFire(5);
				if(movingObject.entityHit.isDead && movingObject.entityHit instanceof EntityPlayer){
					((EntityPlayer)movingObject.entityHit).addStat(ModAchievements.dragonKillPlayer, 1);
				}
				this.applyEnchantments(this.shootingEntity, movingObject.entityHit);
				IceExplosion explosion = new IceExplosion(worldObj, null, this.posX, this.posY, this.posZ, 2, true);
				if (shootingEntity != null) {
					explosion = new IceExplosion(worldObj, shootingEntity, this.posX, this.posY, this.posZ, 2, true);
				}
				explosion.doExplosionA();
				explosion.doExplosionB(true);
				this.worldObj.createExplosion(this, this.posX, this.posY, this.posZ, 4, true);
				this.setDead();
			}

			if (movingObject.typeOfHit != Type.ENTITY || movingObject.entityHit != null && !(movingObject.entityHit instanceof IDragonProjectile)) {
				boolean flag = this.worldObj.getGameRules().getBoolean("mobGriefing");
				this.setDead();
			}
		}
		this.setDead();
	}

	public void setAim(Entity fireball, Entity entity, float yaw, float pitch, float a, float b, float c) {
		float f = -MathHelper.sin(pitch * 0.017453292F) * MathHelper.cos(yaw * 0.017453292F);
		float f1 = -MathHelper.sin(yaw * 0.017453292F);
		float f2 = MathHelper.cos(pitch * 0.017453292F) * MathHelper.cos(yaw * 0.017453292F);
		this.setThrowableHeading(fireball, (double) f, (double) f1, (double) f2, b, c);
		fireball.motionX += entity.motionX;
		fireball.motionZ += entity.motionZ;

		if (!entity.onGround) {
			fireball.motionY += entity.motionY;
		}
	}

	public void setThrowableHeading(Entity fireball, double x, double y, double z, float velocity, float inaccuracy) {
		float f = MathHelper.sqrt_double(x * x + y * y + z * z);
		x = x / (double) f;
		y = y / (double) f;
		z = z / (double) f;
		x = x + this.rand.nextGaussian() * 0.007499999832361937D * (double) inaccuracy;
		y = y + this.rand.nextGaussian() * 0.007499999832361937D * (double) inaccuracy;
		z = z + this.rand.nextGaussian() * 0.007499999832361937D * (double) inaccuracy;
		x = x * (double) velocity;
		y = y * (double) velocity;
		z = z * (double) velocity;
		fireball.motionX = x;
		fireball.motionY = y;
		fireball.motionZ = z;
		float f1 = MathHelper.sqrt_double(x * x + z * z);
		fireball.rotationYaw = (float) (MathHelper.atan2(x, z) * (180D / Math.PI));
		fireball.rotationPitch = (float) (MathHelper.atan2(y, (double) f1) * (180D / Math.PI));
		fireball.prevRotationYaw = fireball.rotationYaw;
		fireball.prevRotationPitch = fireball.rotationPitch;
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		return false;
	}

	public float getCollisionBorderSize() {
		return 0F;
	}

}