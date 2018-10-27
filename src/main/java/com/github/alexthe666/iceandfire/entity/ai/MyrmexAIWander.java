package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityMyrmexBase;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;

public class MyrmexAIWander extends EntityAIWanderAvoidWater {

    private EntityMyrmexBase myrmex;

    public MyrmexAIWander(EntityMyrmexBase myrmex, double speed) {
        super(myrmex, speed);
        this.myrmex = myrmex;
    }

    public boolean shouldExecute(){
        return myrmex.canMove() && myrmex.shouldWander() && super.shouldExecute();
    }
}
