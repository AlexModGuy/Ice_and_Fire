package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntitySiren;
import net.minecraft.entity.ai.goal.RandomWalkingGoal;

public class SirenAIWander extends RandomWalkingGoal {

    private final EntitySiren siren;

    public SirenAIWander(EntitySiren creatureIn, double speedIn) {
        super(creatureIn, speedIn);
        this.siren = creatureIn;
    }

    @Override
    public boolean canUse() {
        return !this.siren.isInWater() && !siren.isSinging() && super.canUse();
    }

    @Override
    public boolean canContinueToUse() {
        return !this.siren.isInWater() && !siren.isSinging() && super.canContinueToUse();
    }
}