package com.github.alexthe666.iceandfire.entity.ai;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.MathHelper;

import com.github.alexthe666.iceandfire.entity.EntityDragonBase;

public class EntityAIDragonAge extends EntityAIBase
{
	protected EntityDragonBase dragon;

	public EntityAIDragonAge(EntityDragonBase dragon)
	{
		this.dragon = dragon;
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	public boolean shouldExecute()
	{

		if (this.dragon.getDragonAge() < 999)
		{
			this.dragon.increaseDragonAgeTick();
			return this.dragon.getDragonAgeTick() >= 2000;
		}else{
			return false;
		}

	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	public boolean continueExecuting()
	{
		return this.shouldExecute();
	}


	/**
	 * Updates the task
	 */
	public void startExecuting()
	{
		if (!this.dragon.worldObj.isRemote)
		{
			this.dragon.setPosition(this.dragon.posX, this.dragon.posY + 1, this.dragon.posZ);
			if (!this.dragon.isEntityInsideOpaqueBlock()){
				this.dragon.setDragonAgeTick(0);
				this.dragon.increaseDragonAge();
				this.dragon.worldObj.setEntityState(this.dragon, (byte)37);
				this.dragon.updateSize();

				if (this.dragon.getMaxHealth() < this.dragon.getHealth())
				{
					this.dragon.heal(MathHelper.ceiling_double_int(this.dragon.getHealth() * 0.15f));
				}

			}
		}
	}
}
