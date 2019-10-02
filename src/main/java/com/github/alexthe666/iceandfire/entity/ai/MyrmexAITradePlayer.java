package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityMyrmexBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;

public class MyrmexAITradePlayer extends EntityAIBase {
    private final EntityMyrmexBase myrmex;

    public MyrmexAITradePlayer(EntityMyrmexBase myrmex) {
        this.myrmex = myrmex;
        this.setMutexBits(5);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute() {
        if (!this.myrmex.isEntityAlive()) {
            return false;
        } else if (this.myrmex.isInWater()) {
            return false;
        } else if (!this.myrmex.onGround) {
            return false;
        } else if (this.myrmex.velocityChanged) {
            return false;
        } else {
            EntityPlayer entityplayer = this.myrmex.getCustomer();

            if (entityplayer == null) {
                return false;
            } else if (this.myrmex.getDistanceSq(entityplayer) > 16.0D) {
                return false;
            } else {
                return entityplayer.openContainer != null;
            }
        }
    }

    public void updateTask() {
        this.myrmex.getNavigator().clearPath();
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    public void resetTask() {
        this.myrmex.setCustomer(null);
    }
}
