package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityDeathWorm;

import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;

public class DeathWormAIWander extends WaterAvoidingRandomWalkingGoal {

    private final EntityDeathWorm worm;

    public DeathWormAIWander(EntityDeathWorm creatureIn, double speedIn) {
        super(creatureIn, speedIn);
        this.worm = creatureIn;
    }

    @Override
    public boolean shouldExecute() {
        return !worm.isInSand() && !worm.isBeingRidden() && super.shouldExecute();
    }

    @Override
    public boolean shouldContinueExecuting() {
        return !worm.isInSand() && !worm.isBeingRidden() && super.shouldContinueExecuting();
    }
}