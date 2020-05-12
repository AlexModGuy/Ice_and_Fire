package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntitySiren;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.EntityAIWander;

public class SirenAIWander extends EntityAIWander {

    public SirenAIWander(MobEntity creatureIn, double speedIn) {
        super(creatureIn, speedIn);
    }

    public boolean shouldExecute() {
        return !this.entity.isInWater() && !((EntitySiren) entity).isSinging() && super.shouldExecute();
    }

    public boolean shouldContinueExecuting() {
        return !this.entity.isInWater() && !((EntitySiren) entity).isSinging() && super.shouldContinueExecuting();
    }
}