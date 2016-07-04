package com.github.alexthe666.iceandfire.entity.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;

import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.google.common.base.Predicate;

import fossilsarcheology.api.FoodMappings;

public class DragonAITarget<T extends EntityLivingBase> extends EntityAINearestAttackableTarget<T> {
	private EntityDragonBase dragon;

	public DragonAITarget(EntityDragonBase entityIn, Class<T> classTarget, boolean checkSight, Predicate<? super T> targetSelector) {
		super(entityIn, classTarget, 1, checkSight, false, targetSelector);
		this.dragon = entityIn;
	}

	@Override
	public boolean shouldExecute() {
		if(super.shouldExecute() && this.targetEntity != null && !this.targetEntity.getClass().equals(this.dragon.getClass())){	
			if(this.dragon.width >= this.targetEntity.width){
				if(this.targetEntity != dragon.getOwner() && FoodMappings.instance().getEntityFoodAmount(this.targetEntity.getClass(), this.dragon.diet) > 0){
					return true;
				}
			}
		}
		return false;
	}
}