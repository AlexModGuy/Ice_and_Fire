package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.goal.LookAtGoal;

public class DragonAIWatchClosest extends LookAtGoal {

    public DragonAIWatchClosest(LivingEntity LivingEntityIn, Class<? extends Entity> watchTargetClass, float maxDistance) {
        super(LivingEntityIn, watchTargetClass, maxDistance);
    }

    @Override
    public boolean shouldExecute() {
        if (this.entity instanceof EntityDragonBase && (!((EntityDragonBase) this.entity).canMove() || ((EntityDragonBase) this.entity).getAnimation() == EntityDragonBase.ANIMATION_SHAKEPREY)) {
            return false;
        }
        return super.shouldExecute();
    }
}
