package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityCockatrice;
import net.minecraft.entity.ai.EntityAIFollowOwner;
import net.minecraft.entity.ai.goal.FollowOwnerGoal;

public class CockatriceAIFollowOwner extends FollowOwnerGoal {
    EntityCockatrice cockatrice;

    public CockatriceAIFollowOwner(EntityCockatrice cockatrice, double speed, float minDist, float maxDist) {
        super(cockatrice, speed, minDist, maxDist);
        this.cockatrice = cockatrice;
        this.setMutexBits(1);
    }

    public boolean shouldExecute() {
        return super.shouldExecute() && cockatrice.getCommand() == 2;
    }
}
