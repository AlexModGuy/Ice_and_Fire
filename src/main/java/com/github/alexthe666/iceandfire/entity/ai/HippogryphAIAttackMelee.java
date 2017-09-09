package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityHippogryph;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

public class HippogryphAIAttackMelee extends EntityAIBase {
	protected EntityHippogryph hippogryph;
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

	public HippogryphAIAttackMelee(EntityHippogryph hippo, double speedIn, boolean useLongMemory) {
		this.hippogryph = hippo;
		this.speedTowardsTarget = speedIn;
		this.longMemory = useLongMemory;
		this.setMutexBits(3);
	}

	@Override
	public boolean shouldExecute() {
		EntityLivingBase entitylivingbase = this.hippogryph.getAttackTarget();
		if (!hippogryph.canMove()) {
			return false;
		}
		if (!hippogryph.isSitting()) {
			return false;
		}
		if (entitylivingbase == null) {
			return false;
		} else if (!entitylivingbase.isEntityAlive()) {
			return false;
		} else {
			if (canPenalize) {
				if (--this.delayCounter <= 0) {
					this.entityPathEntity = this.hippogryph.getNavigator().getPathToEntityLiving(entitylivingbase);
					this.delayCounter = 4 + this.hippogryph.getRNG().nextInt(7);
					return this.entityPathEntity != null;
				} else {
					return true;
				}
			}
			this.entityPathEntity = this.hippogryph.getNavigator().getPathToEntityLiving(entitylivingbase);
			return this.entityPathEntity != null;
		}
	}

	@Override
	public boolean shouldContinueExecuting() {
		EntityLivingBase entitylivingbase = this.hippogryph.getAttackTarget();
		if (entitylivingbase != null && entitylivingbase.isDead) {
			this.resetTask();
			return false;
		}
		return entitylivingbase == null ? false : (!entitylivingbase.isEntityAlive() ? false : (!this.longMemory ? !this.hippogryph.getNavigator().noPath() : (!this.hippogryph.isWithinHomeDistanceFromPosition(new BlockPos(entitylivingbase)) ? false : !(entitylivingbase instanceof EntityPlayer) || !((EntityPlayer) entitylivingbase).isSpectator() && !((EntityPlayer) entitylivingbase).isCreative())));
	}

	@Override
	public void startExecuting() {
		this.hippogryph.getNavigator().setPath(this.entityPathEntity, this.speedTowardsTarget);
		this.delayCounter = 0;
	}

	@Override
	public void resetTask() {
		EntityLivingBase entitylivingbase = this.hippogryph.getAttackTarget();

		if (entitylivingbase instanceof EntityPlayer && (((EntityPlayer) entitylivingbase).isSpectator() || ((EntityPlayer) entitylivingbase).isCreative())) {
			this.hippogryph.setAttackTarget((EntityLivingBase) null);
		}

		this.hippogryph.getNavigator().clearPathEntity();
	}

	@Override
	public void updateTask() {
		EntityLivingBase entity = this.hippogryph.getAttackTarget();
		if (!hippogryph.isPassenger(entity)) {
			this.hippogryph.getLookHelper().setLookPositionWithEntity(entity, 30.0F, 30.0F);
		}
		double d0 = this.hippogryph.getDistanceSq(entity.posX, entity.getEntityBoundingBox().minY, entity.posZ);
		double d1 = this.getAttackReachSqr(entity);
		--this.delayCounter;

		if ((this.longMemory || this.hippogryph.getEntitySenses().canSee(entity)) && this.delayCounter <= 0 && (this.targetX == 0.0D && this.targetY == 0.0D && this.targetZ == 0.0D || entity.getDistanceSq(this.targetX, this.targetY, this.targetZ) >= 1.0D || this.hippogryph.getRNG().nextFloat() < 0.05F)) {
			this.targetX = entity.posX;
			this.targetY = entity.getEntityBoundingBox().minY;
			this.targetZ = entity.posZ;
			this.delayCounter = 4 + this.hippogryph.getRNG().nextInt(7);

			if (this.canPenalize) {
				this.delayCounter += failedPathFindingPenalty;
				if (this.hippogryph.getNavigator().getPath() != null) {
					net.minecraft.pathfinding.PathPoint finalPathPoint = this.hippogryph.getNavigator().getPath().getFinalPathPoint();
					if (finalPathPoint != null && entity.getDistanceSq(finalPathPoint.x, finalPathPoint.y, finalPathPoint.z) < 1)
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
			if (!this.hippogryph.getNavigator().tryMoveToEntityLiving(entity, this.speedTowardsTarget) && this.hippogryph.canMove()) {
				this.delayCounter += 15;
			}
		}

		this.attackTick = Math.max(this.attackTick - 1, 0);

		if (d0 <= d1 && this.attackTick <= 0) {
			this.attackTick = 20;
			this.hippogryph.swingArm(EnumHand.MAIN_HAND);
			this.hippogryph.attackEntityAsMob(entity);
		}
	}

	protected double getAttackReachSqr(EntityLivingBase attackTarget) {
		return this.hippogryph.width * 2.0F * this.hippogryph.width * 2.0F + attackTarget.width;
	}
}