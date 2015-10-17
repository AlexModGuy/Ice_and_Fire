package com.github.alexthe666.iceandfire.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import com.github.alexthe666.iceandfire.entity.EntityDragonBase;

public class EntityAIDragonAttackOnCollide extends EntityAIBase
{
	World worldObj;
	EntityDragonBase dragon;
	/** An amount of decrementing ticks that allows the entity to attack once the tick reaches 0. */
	int attackTick;
	/** The speed with which the mob will approach the target */
	double speedTowardsTarget;
	/** When true, the mob will continue chasing its target, even if it can't find a path to them right now. */
	boolean longMemory;
	/** The PathEntity of our entity. */
	PathEntity entityPathEntity;
	Class classTarget;
	private int field_75445_i;
	private double field_151497_i;
	private double field_151495_j;
	private double field_151496_k;
	private static final String __OBFID = "CL_00001595";
	private int failedPathFindingPenalty = 0;
	private boolean canPenalize = false;

	public EntityAIDragonAttackOnCollide(EntityDragonBase entity, Class targetClass, double i, boolean q)
	{
		this(entity, i, q);
		this.classTarget = targetClass;
		canPenalize = classTarget == null || !net.minecraft.entity.player.EntityPlayer.class.isAssignableFrom(classTarget); //Only enable delaying when not targeting players.
	}

	public EntityAIDragonAttackOnCollide(EntityDragonBase entity, double i, boolean q)
	{
		this.dragon = entity;
		this.worldObj = entity.worldObj;
		this.speedTowardsTarget = i;
		this.longMemory = q;
		this.setMutexBits(3);
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	public boolean shouldExecute()
	{
		EntityLivingBase entitylivingbase = this.dragon.getAttackTarget();

		if (entitylivingbase == null || dragon.shouldSetFire(entitylivingbase))
		{
			return false;
		}
		else if (!entitylivingbase.isEntityAlive())
		{
			return false;
		}
		else if (this.classTarget != null && !this.classTarget.isAssignableFrom(entitylivingbase.getClass()))
		{
			return false;
		}
		else
		{
			if (canPenalize)
			{
				if (--this.field_75445_i <= 0)
				{
					this.entityPathEntity = this.dragon.getNavigator().getPathToEntityLiving(entitylivingbase);
					this.field_151497_i = 4 + this.dragon.getRNG().nextInt(7);
					return this.entityPathEntity != null;
				}
				else
				{
					return true;
				}
			}
			this.entityPathEntity = this.dragon.getNavigator().getPathToEntityLiving(entitylivingbase);
			return this.entityPathEntity != null;
		}
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	public boolean continueExecuting()
	{
		EntityLivingBase entitylivingbase = this.dragon.getAttackTarget();
		return entitylivingbase == null ? false : (!entitylivingbase.isEntityAlive() ? false : (!this.longMemory ? !this.dragon.getNavigator().noPath() : this.dragon.func_180485_d(new BlockPos(entitylivingbase))));
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	public void startExecuting()
	{
		this.dragon.getNavigator().setPath(this.entityPathEntity, this.speedTowardsTarget);
		this.field_75445_i = 0;
	}

	/**
	 * Resets the task
	 */
	public void resetTask()
	{
		this.dragon.getNavigator().clearPathEntity();
	}

	/**
	 * Updates the task
	 */
	public void updateTask()
	{
		EntityLivingBase entitylivingbase = this.dragon.getAttackTarget();
		this.dragon.getLookHelper().setLookPositionWithEntity(entitylivingbase, 30.0F, 30.0F);
		double d0 = this.dragon.getDistanceSq(entitylivingbase.posX, entitylivingbase.getEntityBoundingBox().minY, entitylivingbase.posZ);
		double d1 = this.func_179512_a(entitylivingbase);
		--this.field_75445_i;

		if ((this.longMemory || this.dragon.getEntitySenses().canSee(entitylivingbase)) && this.field_75445_i <= 0 && (this.field_151497_i == 0.0D && this.field_151495_j == 0.0D && this.field_151496_k == 0.0D || entitylivingbase.getDistanceSq(this.field_151497_i, this.field_151495_j, this.field_151496_k) >= 1.0D || this.dragon.getRNG().nextFloat() < 0.05F))
		{
			this.field_151497_i = entitylivingbase.posX;
			this.field_151495_j = entitylivingbase.getEntityBoundingBox().minY;
			this.field_151496_k = entitylivingbase.posZ;
			this.field_75445_i = 4 + this.dragon.getRNG().nextInt(7);

			if (this.canPenalize)
			{
				this.field_151497_i += failedPathFindingPenalty;
				if (this.dragon.getNavigator().getPath() != null)
				{
					net.minecraft.pathfinding.PathPoint finalPathPoint = this.dragon.getNavigator().getPath().getFinalPathPoint();
					if (finalPathPoint != null && entitylivingbase.getDistanceSq(finalPathPoint.xCoord, finalPathPoint.yCoord, finalPathPoint.zCoord) < 1)
						failedPathFindingPenalty = 0;
					else
						failedPathFindingPenalty += 10;
				}
				else
				{
					failedPathFindingPenalty += 10;
				}
			}

			if (d0 > 1024.0D)
			{
				this.field_75445_i += 10;
			}
			else if (d0 > 256.0D)
			{
				this.field_75445_i += 5;
			}

			if(!this.dragon.shouldSetFire(entitylivingbase)){
				if (!this.dragon.getNavigator().tryMoveToEntityLiving(entitylivingbase, this.speedTowardsTarget))
				{
					this.field_75445_i += 15;
				}
			}

			this.attackTick = Math.max(this.attackTick - 1, 0);

			if (d0 <= d1 && this.attackTick <= 0)
			{
				this.attackTick = 20;

				if (this.dragon.getHeldItem() != null)
				{
					this.dragon.swingItem();
				}
				this.dragon.attackEntityAsMob(entitylivingbase);
			}
		}
	}

	protected double func_179512_a(EntityLivingBase p_179512_1_)
	{
		return (double)(this.dragon.width * 2.0F * this.dragon.width * 2.0F + p_179512_1_.width);
	}
}