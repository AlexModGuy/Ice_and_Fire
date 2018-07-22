package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityMyrmexBase;
import com.github.alexthe666.iceandfire.entity.MyrmexHive;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.village.Village;

public class MyrmexAIDefendHive extends EntityAITarget {
    EntityMyrmexBase myrmex;
    EntityLivingBase villageAgressorTarget;

    public MyrmexAIDefendHive(EntityMyrmexBase myrmex) {
        super(myrmex, false, true);
        this.myrmex = myrmex;
        this.setMutexBits(1);
    }

    public boolean shouldExecute() {
        MyrmexHive village = this.myrmex.getHive();

        if (village == null) {
            return false;
        } else {
            this.villageAgressorTarget = village.findNearestVillageAggressor(this.myrmex);
            if (this.isSuitableTarget(this.villageAgressorTarget, false)) {
                return true;
            } else if (this.taskOwner.getRNG().nextInt(20) == 0) {
                this.villageAgressorTarget = village.getNearestTargetPlayer(this.myrmex);
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