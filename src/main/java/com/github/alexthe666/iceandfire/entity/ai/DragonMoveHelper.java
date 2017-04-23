package com.github.alexthe666.iceandfire.entity.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityMoveHelper;

public class DragonMoveHelper extends EntityMoveHelper {

    public DragonMoveHelper(EntityLiving entitylivingIn) {
        super(entitylivingIn);
    }

    @Override
    public boolean isUpdating() {
        return this.action == EntityMoveHelper.Action.MOVE_TO || (this.entity instanceof EntityDragonBase && ((EntityDragonBase) this.entity).canMove());
    }
}
