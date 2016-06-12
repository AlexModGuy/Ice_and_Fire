package com.github.alexthe666.iceandfire.entity.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.enums.EnumOrder;

public class EntityAIDragonFollow extends EntityAIBase {
	World theWorld;
	float maxDist;
	float minDist;
	private EntityDragonBase dragon;
	private EntityLivingBase theOwner;
	private double speed;
	private PathNavigate petPathfinder;
	private int counter;
	private boolean avoidsWater;

	public EntityAIDragonFollow(EntityDragonBase dragon, float minDist, float maxDist) {
		this.dragon = dragon;
		this.theWorld = dragon.worldObj;
		this.speed = 1;
		this.petPathfinder = dragon.getNavigator();
		this.minDist = minDist;
		this.maxDist = maxDist;
		this.setMutexBits(3);
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	@Override
	public boolean shouldExecute() {
		if (!this.dragon.isTamed()) {
			return false;
		} else {
			EntityLivingBase entitylivingbase = this.dragon.getOwner();

			if (entitylivingbase == null) {
				return false;
			} else if (this.dragon.currentOrder != null && this.dragon.currentOrder != EnumOrder.FOLLOW) {
				return false;
			} else if (this.dragon.isSitting()) {
				return false;
			} else if (this.dragon.getDistanceSqToEntity(entitylivingbase) < this.minDist * this.minDist) {
				return false;
			} else {
				this.theOwner = entitylivingbase;
				return true;
			}
		}
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	@Override
	public boolean continueExecuting() {
		return !this.petPathfinder.noPath() && this.dragon.getDistanceSqToEntity(this.theOwner) > this.maxDist * this.maxDist && !this.dragon.isSitting();
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	@Override
	public void startExecuting() {
		this.counter = 0;

	}

	/**
	 * Resets the task
	 */
	@Override
	public void resetTask() {
		this.theOwner = null;
		this.petPathfinder.clearPathEntity();
	}

	/**
	 * Updates the task
	 */
	@Override
	public void updateTask() {
		this.dragon.getLookHelper().setLookPositionWithEntity(this.theOwner, 10.0F, this.dragon.getVerticalFaceSpeed());

		if (!this.dragon.isSitting()) {
			if (--this.counter <= 0) {
				this.counter = 10;

				if (!this.petPathfinder.tryMoveToEntityLiving(this.theOwner, this.speed)) {
					if (!this.dragon.getLeashed()) {
						if (this.dragon.getDistanceSqToEntity(this.theOwner) >= 144.0D) {
							int i = MathHelper.floor_double(this.theOwner.posX) - 2;
							int j = MathHelper.floor_double(this.theOwner.posZ) - 2;
							int k = MathHelper.floor_double(this.theOwner.getEntityBoundingBox().minY);

							for (int l = 0; l <= 4; ++l) {
								for (int i1 = 0; i1 <= 4; ++i1) {
									if ((l < 1 || i1 < 1 || l > 3 || i1 > 3) && theWorld.getTopSolidOrLiquidBlock(new BlockPos(i + l, k - 1, j + i1)) != null && !this.theWorld.getBlockState(new BlockPos(i + l, k, j + i1)).isNormalCube()) {
										this.dragon.setLocationAndAngles(i + l + 0.5F, k, j + i1 + 0.5F, this.dragon.rotationYaw, this.dragon.rotationPitch);
										this.petPathfinder.clearPathEntity();
										return;
									}
								}
							}
						}
					}
				}
			}
		}
	}
}
