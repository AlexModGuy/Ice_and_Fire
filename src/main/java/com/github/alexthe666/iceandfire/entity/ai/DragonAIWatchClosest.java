package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;

public class DragonAIWatchClosest extends LookAtPlayerGoal {

    public DragonAIWatchClosest(PathfinderMob LivingEntityIn, Class<? extends LivingEntity> watchTargetClass, float maxDistance) {
        super(LivingEntityIn, watchTargetClass, maxDistance);
    }

    @Override
    public boolean canUse() {
        if (this.mob instanceof EntityDragonBase && ((EntityDragonBase) this.mob).getAnimation() == EntityDragonBase.ANIMATION_SHAKEPREY) {
            return false;
        }
        return super.canUse();
    }

    @Override
    public boolean canContinueToUse() {
        if (this.mob instanceof EntityDragonBase && !((EntityDragonBase) this.mob).canMove()) {
            return false;
        }
        return super.canContinueToUse();
    }
}
