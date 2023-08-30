package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityMyrmexBase;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;

import java.util.EnumSet;

public class MyrmexAITradePlayer extends Goal {
    private final EntityMyrmexBase myrmex;

    public MyrmexAITradePlayer(EntityMyrmexBase myrmex) {
        this.myrmex = myrmex;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    /**
     * Returns whether the Goal should begin execution.
     */
    @Override
    public boolean canUse() {
        if (!this.myrmex.isAlive()) {
            return false;
        } else if (this.myrmex.isInWater()) {
            return false;
        } else if (!this.myrmex.onGround()) {
            return false;
        } else if (this.myrmex.hurtMarked) {
            return false;
        } else {
            Player PlayerEntity = this.myrmex.getTradingPlayer();
            if (PlayerEntity == null) {
                return false;
            } else if (this.myrmex.distanceToSqr(PlayerEntity) > 16.0D) {
                return false;
            } else if (this.myrmex.getHive() != null && !this.myrmex.getHive().isPlayerReputationTooLowToTrade(PlayerEntity.getUUID())) {
                return false;
            } else {
                return PlayerEntity.containerMenu != null;
            }
        }
    }

    @Override
    public void tick() {
        this.myrmex.getNavigation().stop();
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    @Override
    public void stop() {
        this.myrmex.setTradingPlayer(null);
    }
}
