package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntitySiren;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.PathNavigateGround;

public class SirenAIVanillaSwimming extends EntityAIBase {
    private final EntitySiren entity;

    public SirenAIVanillaSwimming(EntitySiren entityIn) {
        this.entity = entityIn;
        this.setMutexBits(4);
        if (entityIn.getNavigator() instanceof PathNavigateGround) {
            ((PathNavigateGround) entityIn.getNavigator()).setCanSwim(true);
        }
    }

    public boolean shouldExecute() {
        return (this.entity.isInWater() || this.entity.isInLava()) && this.entity.wantsToSing();
    }

    public void updateTask() {
        if (this.entity.getRNG().nextFloat() < 0.8F) {
            this.entity.getJumpHelper().setJumping();
        }
    }
}