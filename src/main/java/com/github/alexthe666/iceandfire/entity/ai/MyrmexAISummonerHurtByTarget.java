package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityMyrmexSwarmer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITarget;

public class MyrmexAISummonerHurtByTarget extends EntityAITarget {
    EntityMyrmexSwarmer tameable;
    EntityLivingBase attacker;
    private int timestamp;

    public MyrmexAISummonerHurtByTarget(EntityMyrmexSwarmer theDefendingTameableIn) {
        super(theDefendingTameableIn, false);
        this.tameable = theDefendingTameableIn;
        this.setMutexBits(1);
    }

    public boolean shouldExecute() {
        EntityLivingBase entitylivingbase = this.tameable.getSummoner();

        if (entitylivingbase == null) {
            return false;
        } else {
            this.attacker = entitylivingbase.getRevengeTarget();
            int i = entitylivingbase.getRevengeTimer();
            return i != this.timestamp && this.isSuitableTarget(this.attacker, false) && this.tameable.shouldAttackEntity(this.attacker, entitylivingbase);
        }
    }

    public void startExecuting() {
        this.taskOwner.setAttackTarget(this.attacker);
        EntityLivingBase entitylivingbase = this.tameable.getSummoner();

        if (entitylivingbase != null) {
            this.timestamp = entitylivingbase.getRevengeTimer();
        }

        super.startExecuting();
    }
}