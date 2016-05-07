package com.github.alexthe666.iceandfire.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIAttackMelee;

import com.github.alexthe666.iceandfire.entity.EntityDragonBase;

public class EntityAIDragonAttackOnCollide extends EntityAIAttackMelee
{

	public EntityAIDragonAttackOnCollide(EntityCreature entity, double d, boolean d1) {
		super(entity, d, d1);
	}

	public boolean shouldExecute()
	{
		EntityDragonBase dragon = ((EntityDragonBase)attacker);
		if(dragon.getAttackTarget() != null){
			float d = dragon.getDistanceToEntity(dragon.getAttackTarget());
			if(d <= 1.78F * dragon.getDragonSize()){
				return super.shouldExecute();
			}
		}
		return false;
	}

}