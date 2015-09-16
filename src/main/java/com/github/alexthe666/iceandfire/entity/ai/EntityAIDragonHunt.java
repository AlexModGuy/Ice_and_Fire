package com.github.alexthe666.iceandfire.entity.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;

import com.github.alexthe666.iceandfire.entity.EntityDragonBase;

public class EntityAIDragonHunt extends EntityAINearestAttackableTarget
{
	private EntityDragonBase dragon;
	private final Class targetClass;

	public EntityAIDragonHunt(EntityDragonBase mob, Class prey, int hungryTicks, boolean see)
	{
		super(mob, prey, see);
		this.dragon = mob;
		this.targetClass = prey;
	}


	public boolean shouldExecute()
	{
		EntityLivingBase closestLivingEntity = (EntityLivingBase) this.dragon.closestEntity();

		/*	if(!this.dragon.isHungry() || !dragon.isTamed()){
			return false;
		}
		if(closestLivingEntity != null){
			if(closestLivingEntity.width < dragon.width && closestLivingEntity.width < dragon.width){
				return super.shouldExecute();
			}
		}*/

		return super.shouldExecute();
	}
}