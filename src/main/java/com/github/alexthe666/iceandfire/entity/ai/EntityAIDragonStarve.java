package com.github.alexthe666.iceandfire.entity.ai;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;

import com.github.alexthe666.iceandfire.entity.EntityDragonBase;

public class EntityAIDragonStarve extends EntityAIBase
{
	EntityDragonBase dragon = null;

	public EntityAIDragonStarve(EntityDragonBase var1)
	{
		this.dragon = var1;
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	public boolean shouldExecute()
	{
		//if (fossilOptions.DinoHunger)
		//{
		this.dragon.decreaseHungerTick();
		return this.dragon.getHungerTick() <= 0 && dragon.isTamed();
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	public void startExecuting()
	{
		this.dragon.setHungerTick(300);
		this.dragon.decreaseHunger();
		if (this.dragon.getHunger() <= 0)
		{
			this.handleStarvation();
		}
	}


private void handleStarvation()
{
	if (this.dragon.getHealth() <= 5 && dragon.isTamed())
	{
		System.out.println("dragon has starved to 5 health");
		this.dragon.attackEntityFrom(DamageSource.starve, 1);
	}

}
}
