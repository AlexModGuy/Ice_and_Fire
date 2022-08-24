package com.github.alexthe666.iceandfire.entity.ai;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.ai.goal.RandomWalkingGoal;
import net.minecraft.entity.passive.TameableEntity;

public class HippocampusAIWander extends RandomWalkingGoal {

    public HippocampusAIWander(CreatureEntity creatureIn, double speedIn) {
        super(creatureIn, speedIn);
    }

    public boolean canUse() {
        return !(mob instanceof TameableEntity && ((TameableEntity) mob).isOrderedToSit()) && !this.mob.isInWater() && super.canUse();
    }

    public boolean canContinueToUse() {
        return !(mob instanceof TameableEntity && ((TameableEntity) mob).isOrderedToSit()) && !this.mob.isInWater() && super.canContinueToUse();
    }
}