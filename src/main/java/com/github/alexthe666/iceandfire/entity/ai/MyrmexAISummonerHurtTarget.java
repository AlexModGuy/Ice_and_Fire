package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityMyrmexSwarmer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITarget;

public class MyrmexAISummonerHurtTarget extends EntityAITarget {
    EntityMyrmexSwarmer tameable;
    EntityLivingBase attacker;
    private int timestamp;

    public MyrmexAISummonerHurtTarget(EntityMyrmexSwarmer theEntityMyrmexSwarmerIn) {
        super(theEntityMyrmexSwarmerIn, false);
        this.tameable = theEntityMyrmexSwarmerIn;
        this.setMutexBits(1);
    }

    public boolean shouldExecute() {
        EntityLivingBase entitylivingbase = this.tameable.getSummoner();

        if (entitylivingbase == null) {
            return false;
        } else {
            this.attacker = entitylivingbase.getLastAttackedEntity();
            int i = entitylivingbase.getLastAttackedEntityTime();
            return i != this.timestamp && this.isSuitableTarget(this.attacker, false) && this.tameable.shouldAttackEntity(this.attacker, entitylivingbase);
        }
    }

    public void startExecuting() {
        this.taskOwner.setAttackTarget(this.attacker);
        EntityLivingBase entitylivingbase = this.tameable.getSummoner();

        if (entitylivingbase != null) {
            this.timestamp = entitylivingbase.getLastAttackedEntityTime();
        }

        super.startExecuting();
    }
}