package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityMyrmexBase;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;

public class MyrmexAIWander extends WaterAvoidingRandomStrollGoal {

    protected EntityMyrmexBase myrmex;

    public MyrmexAIWander(EntityMyrmexBase myrmex, double speed) {
        super(myrmex, speed);
        this.myrmex = myrmex;
    }

    @Override
    public boolean canUse() {
        return myrmex.canMove() && myrmex.shouldWander() && super.canUse();
    }
}
