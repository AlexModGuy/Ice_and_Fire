package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityMyrmexBase;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.player.PlayerEntity;

public class MyrmexAILookAtTradePlayer extends LookAtGoal {
    private final EntityMyrmexBase myrmex;

    public MyrmexAILookAtTradePlayer(EntityMyrmexBase myrmex) {
        super(myrmex, PlayerEntity.class, 8.0F);
        this.myrmex = myrmex;
    }

    @Override
    public boolean canUse() {
        if (this.myrmex.hasCustomer() && this.myrmex.getHive() != null) {
            if (!this.myrmex.getHive().isPlayerReputationTooLowToTrade(this.myrmex.getTradingPlayer().getUUID())) {
                this.lookAt = this.myrmex.getTradingPlayer();
                return true;
            }
        }
        return false;
    }
}
