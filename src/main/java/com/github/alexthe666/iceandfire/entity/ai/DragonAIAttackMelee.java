package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.EntityUtils;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

public class DragonAIAttackMelee extends EntityAIBase {
	protected EntityDragonBase dragon;
	private int attackTick;
	private double speedTowardsTarget;
	private boolean longMemory;
	private Path entityPathEntity;
	private int delayCounter;
	private double targetX;
	private double targetY;
	private double targetZ;
	private int failedPathFindingPenalty = 0;
	private boolean canPenalize = false;

	public DragonAIAttackMelee(EntityDragonBase dragon, double speedIn, boolean useLongMemory) {
		this.dragon = dragon;
		this.speedTowardsTarget = speedIn;
		this.longMemory = useLongMemory;
		this.setMutexBits(3);
	}

	@Override
	public boolean shouldExecute() {
		EntityLivingBase entitylivingbase = this.dragon.getAttackTarget();
		if(entitylivingbase == null) {
			return false;
		}
		if (!dragon.canMoveWithoutSleeping()) {
			return false;
		}
		if (!EntityUtils.isEntityAlive(entitylivingbase)) {
			return false;
		} else {
			if (canPenalize) {
				if (--this.delayCounter <= 0) {
					this.entityPathEntity = this.dragon.getNavigator().getPathToEntityLiving(entitylivingbase);
					this.delayCounter = 4 + this.dragon.getRNG().nextInt(7);
					return this.entityPathEntity != null;
				} else {
					return true;
				}
			}
			this.entityPathEntity = this.dragon.getNavigator().getPathToEntityLiving(entitylivingbase);
			return this.entityPathEntity != null;
		}
	}

	@Override
	public boolean shouldContinueExecuting() {
		EntityLivingBase entitylivingbase = this.dragon.getAttackTarget();
		if ((entitylivingbase != null && EntityUtils.isEntityDead(entitylivingbase)) || entitylivingbase == null) {
			this.resetTask();
			return false;
		}
		return entitylivingbase.isEntityAlive() && (!this.longMemory ? dragon.isFlying() || dragon.isHovering() || !this.dragon.getNavigator().noPath() : this.dragon.isWithinHomeDistanceFromPosition(new BlockPos(entitylivingbase)) && (!(entitylivingbase instanceof EntityPlayer) || !((EntityPlayer) entitylivingbase).isSpectator() && !((EntityPlayer) entitylivingbase).isCreative()));
	}

	@Override
	public void startExecuting() {
		this.dragon.getNavigator().setPath(this.entityPathEntity, this.speedTowardsTarget);
		this.delayCounter = 0;
	}

	@Override
	public void resetTask() {
		EntityLivingBase entitylivingbase = this.dragon.getAttackTarget();
		if (entitylivingbase instanceof EntityPlayer && (((EntityPlayer) entitylivingbase).isSpectator() || ((EntityPlayer) entitylivingbase).isCreative())) {
			this.dragon.setAttackTarget(null);
		}
		this.dragon.getNavigator().clearPath();
	}

//	@Override
//	public void updateTask() {
//		super.updateTask();
//		EntityLivingBase attackTarget = this.dragon.getAttackTarget();
//		if(attackTarget == null) {
//			return;
//		}
//		World world = dragon.world;
//		if (!world.isRemote && dragon.onGround && dragon.getNavigator().noPath() && attackTarget.posY - 3 > dragon.posY && dragon.getRNG().nextInt(15) == 0 && dragon.canMove() && !dragon.isHovering() && !dragon.isFlying() && !dragon.isChild()
//				) {
//			dragon.setHovering(true);
//			dragon.setSleeping(false);
//			dragon.setSitting(false);
//			dragon.flyHovering = 0;
//			dragon.hoverTicks = 0;
//			dragon.flyTicks = 0;
//		}
//
//		if (dragon.getAnimation() == EntityDragonBase.ANIMATION_WINGBLAST && (dragon.getAnimationTick() == 17 || dragon.getAnimationTick() == 22 || dragon.getAnimationTick() == 28)) {
//			dragon.spawnGroundEffects();
//			boolean flag = attackTarget.attackEntityFrom(DamageSource.causeMobDamage(dragon), ((int) dragon.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue()) / 4);
//			attackTarget.knockBack(attackTarget, dragon.getDragonStage() * 0.6F, 1, 1);
//			dragon.attackDecision = dragon.getRNG().nextBoolean();
//		}
//		if (!world.isRemote && dragon.isFlying() && dragon.attackDecision && dragon.isDirectPathBetweenPoints(dragon.getPositionVector(), attackTarget.getPositionVector())) {
//			dragon.setTackling(true);
//		}
//		if (!world.isRemote && dragon.isFlying() && dragon.isTackling() && dragon.getEntityBoundingBox().expand(2.0D, 2.0D, 2.0D).intersects(attackTarget.getEntityBoundingBox())) {
//			dragon.attackDecision = true;
//			attackTarget.attackEntityFrom(DamageSource.causeMobDamage(dragon), dragon.getDragonStage() * 3);
//			dragon.spawnGroundEffects();
//			dragon.setFlying(false);
//			dragon.setHovering(false);
//		}
//		dragon.flyTowardsTarget(new BlockPos((int) attackTarget.posX, (int) attackTarget.posY, (int) attackTarget.posZ));
//
//		if (attackTarget.posY + 5 < dragon.posY && !dragon.isStoned() && !world.isRemote && !dragon.isSitting() && !dragon.isFlying() && dragon.getPassengers().isEmpty() && !dragon.isChild() && !dragon.isHovering() && !dragon.isSleeping() && dragon.canMove() && dragon.onGround) {
//			dragon.setHovering(true);
//			dragon.setSleeping(false);
//			dragon.setSitting(false);
//			dragon.flyHovering = 0;
//			dragon.hoverTicks = 0;
//			dragon.flyTicks = 0;
//		}
//		if (dragon.isFlying() && dragon.getEntityBoundingBox().expand(3.0F, 3.0F, 3.0F).intersects(attackTarget.getEntityBoundingBox())) {
//			dragon.attackEntityAsMob(attackTarget);
//		}
//	}

	@Override
	public void updateTask() {
		EntityLivingBase targetEntity = this.dragon.getAttackTarget();
		if (targetEntity != null) {
			if (!dragon.isPassenger(targetEntity)) {
				this.dragon.getLookHelper().setLookPositionWithEntity(targetEntity, 30.0F, 30.0F);
			}
			if (dragon.getAnimation() == EntityDragonBase.ANIMATION_SHAKEPREY) {
				this.resetTask();
				return;
			}
			double d0 = this.dragon.getDistanceSq(targetEntity.posX, targetEntity.getEntityBoundingBox().minY, targetEntity.posZ);
			double d1 = this.getAttackReachSqr(targetEntity);
			--this.delayCounter;
			if ((this.longMemory || this.dragon.getEntitySenses().canSee(targetEntity)) && this.delayCounter <= 0 && (this.targetX == 0.0D && this.targetY == 0.0D && this.targetZ == 0.0D || targetEntity.getDistanceSq(this.targetX, this.targetY, this.targetZ) >= 1.0D || this.dragon.getRNG().nextFloat() < 0.05F)) {
				this.targetX = targetEntity.posX;
				this.targetY = targetEntity.getEntityBoundingBox().minY;
				this.targetZ = targetEntity.posZ;
				this.delayCounter = 4 + this.dragon.getRNG().nextInt(7);

				if (this.canPenalize) {
					this.delayCounter += failedPathFindingPenalty;
					if (this.dragon.getNavigator().getPath() != null) {
						net.minecraft.pathfinding.PathPoint finalPathPoint = this.dragon.getNavigator().getPath().getFinalPathPoint();
						if (finalPathPoint != null && targetEntity.getDistanceSq(finalPathPoint.x, finalPathPoint.y, finalPathPoint.z) < 1)
							failedPathFindingPenalty = 0;
						else
							failedPathFindingPenalty += 10;
					} else {
						failedPathFindingPenalty += 10;
					}
				}

				if (d0 > 1024.0D) {
					this.delayCounter += 10;
				} else if (d0 > 256.0D) {
					this.delayCounter += 5;
				}
				if (!dragon.isBreathingFire()) {
					if (!this.dragon.getNavigator().tryMoveToEntityLiving(targetEntity, this.speedTowardsTarget) && this.dragon.canMove()) {
						this.delayCounter += 15;
					}
				} else {
					this.dragon.getNavigator().clearPath();
				}
			}

			this.attackTick = Math.max(this.attackTick - 1, 0);

			if(this.attackTick <= 0) {
				if (d0 <= d1) {
					this.attackTick = 20;
					this.dragon.swingArm(EnumHand.MAIN_HAND);
					this.dragon.attackEntityAsMob(targetEntity);
				} else {
//					if (!dragon.isTargetBlocked(new Vec3d(targetEntity.posX, targetEntity.posY, targetEntity.posZ))) {
//						dragon.shootDragonBreathAtMob(targetEntity);
//					}
				}
			}
		}
	}

	protected double getAttackReachSqr(EntityLivingBase attackTarget) {
		return this.dragon.width * 2.0F * this.dragon.width * 2.0F + attackTarget.width;
	}
}