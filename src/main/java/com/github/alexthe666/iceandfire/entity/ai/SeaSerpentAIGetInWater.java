package com.github.alexthe666.iceandfire.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nullable;

public class SeaSerpentAIGetInWater extends AquaticAIGetInWater {

    public SeaSerpentAIGetInWater(EntityCreature theCreatureIn, double movementSpeedIn) {
        super(theCreatureIn, movementSpeedIn);
    }

    @Override
    public boolean isAttackerInWater() {
        return false;
    }

    @Nullable
    public Vec3d findPossibleShelter() {
        return findPossibleShelter(30, 20);
    }

}
