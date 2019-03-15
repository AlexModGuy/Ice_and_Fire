package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityMyrmexBase;
import com.github.alexthe666.iceandfire.entity.EntityMyrmexQueen;
import net.minecraft.entity.ai.EntityAIBase;

public class MyrmexQueenAIWander extends MyrmexAIWander {

    public MyrmexQueenAIWander(EntityMyrmexBase myrmex, double speed) {
        super(myrmex, speed);
    }

    public boolean shouldExecute(){
        return (myrmex.canSeeSky() || myrmex.getHive() == null) && super.shouldExecute();
    }
}
