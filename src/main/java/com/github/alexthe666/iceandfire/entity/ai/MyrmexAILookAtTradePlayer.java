package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityMyrmexBase;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;

public class MyrmexAILookAtTradePlayer extends EntityAIWatchClosest {
    private final EntityMyrmexBase myrmex;

    public MyrmexAILookAtTradePlayer(EntityMyrmexBase myrmex) {
        super(myrmex, EntityPlayer.class, 8.0F);
        this.myrmex = myrmex;
    }

    public boolean shouldExecute() {
        if (this.myrmex.isTrading()) {
            this.closestEntity = this.myrmex.getCustomer();
            return true;
        } else {
            return false;
        }
    }
}
