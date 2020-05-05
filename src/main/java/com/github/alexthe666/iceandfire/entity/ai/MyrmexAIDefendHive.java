package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityMyrmexBase;
import com.github.alexthe666.iceandfire.entity.MyrmexHive;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.EntityAITarget;

public class MyrmexAIDefendHive extends EntityAITarget {
    EntityMyrmexBase myrmex;
    LivingEntity villageAgressorTarget;

    public MyrmexAIDefendHive(EntityMyrmexBase myrmex) {
        super(myrmex, false, true);
        this.myrmex = myrmex;
        this.setMutexBits(1);
    }

    public boolean shouldExecute() {
        MyrmexHive village = this.myrmex.getHive();

        if (!this.myrmex.canMove() || village == null) {
            return false;
        } else {
            this.villageAgressorTarget = village.findNearestVillageAggressor(this.myrmex);
            if (this.isSuitableTarget(this.villageAgressorTarget, false)) {
                return true;
            } else if (this.taskOwner.getRNG().nextInt(20) == 0) {
                this.villageAgressorTarget = village.getNearestTargetPlayer(this.myrmex, this.myrmex.world);
                return this.isSuitableTarget(this.villageAgressorTarget, false);
            } else {
                return false;
            }
        }
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting() {
        this.myrmex.setAttackTarget(this.villageAgressorTarget);
        super.startExecuting();
    }
}