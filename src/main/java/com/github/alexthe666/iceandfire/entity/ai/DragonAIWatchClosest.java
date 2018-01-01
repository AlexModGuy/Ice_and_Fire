package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIWatchClosest;

public class DragonAIWatchClosest extends EntityAIWatchClosest {

	public DragonAIWatchClosest(EntityLiving entitylivingIn, Class<? extends Entity> watchTargetClass, float maxDistance) {
		super(entitylivingIn, watchTargetClass, maxDistance);
	}

	@Override
	public boolean shouldExecute() {
		if (this.entity instanceof EntityDragonBase && !((EntityDragonBase) this.entity).canMove() || ((EntityDragonBase) this.entity).getAnimation() == EntityDragonBase.ANIMATION_SHAKEPREY) {
			return false;
		}
		return super.shouldExecute();
	}
}
