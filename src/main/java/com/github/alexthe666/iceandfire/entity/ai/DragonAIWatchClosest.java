package com.github.alexthe666.iceandfire.entity.ai;

import net.minecraft.entity.*;
import net.minecraft.entity.ai.EntityAIWatchClosest;

public class DragonAIWatchClosest extends EntityAIWatchClosest {

    public DragonAIWatchClosest(EntityLiving entitylivingIn, Class<? extends Entity> watchTargetClass, float maxDistance) {
        super(entitylivingIn, watchTargetClass, maxDistance);
    }

    @Override
    public boolean shouldExecute() {
        if (this.theWatcher instanceof EntityDragonBase && !((EntityDragonBase) this.theWatcher).canMove()) {
            return false;
        }
        return super.shouldExecute();
    }
}
