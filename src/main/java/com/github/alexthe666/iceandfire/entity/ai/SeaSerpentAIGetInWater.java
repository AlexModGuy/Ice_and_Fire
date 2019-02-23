package com.github.alexthe666.iceandfire.entity.ai;

import net.minecraft.entity.EntityCreature;

public class SeaSerpentAIGetInWater extends AquaticAIGetInWater {

    public SeaSerpentAIGetInWater(EntityCreature theCreatureIn, double movementSpeedIn) {
        super(theCreatureIn, movementSpeedIn);
    }

    @Override
    public boolean isAttackerInWater() {
        return false;
    }
}
