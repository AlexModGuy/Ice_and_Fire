package com.github.alexthe666.iceandfire.entity.ai;

import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.passive.TameableEntity;

public class HippocampusAIWander extends EntityAIWander {

    public HippocampusAIWander(MobEntity creatureIn, double speedIn) {
        super(creatureIn, speedIn);
    }

    public boolean shouldExecute() {
        return !(entity instanceof TameableEntity && ((TameableEntity) entity).isSitting()) && !this.entity.isInWater() && super.shouldExecute();
    }

    public boolean shouldContinueExecuting() {
        return !(entity instanceof TameableEntity && ((TameableEntity) entity).isSitting()) && !this.entity.isInWater() && super.shouldContinueExecuting();
    }
}