package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityMyrmexSwarmer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.EntityAITarget;

public class MyrmexAISummonerHurtTarget extends EntityAITarget {
    EntityMyrmexSwarmer tameable;
    LivingEntity attacker;
    private int timestamp;

    public MyrmexAISummonerHurtTarget(EntityMyrmexSwarmer theEntityMyrmexSwarmerIn) {
        super(theEntityMyrmexSwarmerIn, false);
        this.tameable = theEntityMyrmexSwarmerIn;
        this.setMutexBits(1);
    }

    public boolean shouldExecute() {
        LivingEntity LivingEntity = this.tameable.getSummoner();

        if (LivingEntity == null) {
            return false;
        } else {
            this.attacker = LivingEntity.getLastAttackedEntity();
            int i = LivingEntity.getLastAttackedEntityTime();
            return i != this.timestamp && this.isSuitableTarget(this.attacker, false) && this.tameable.shouldAttackEntity(this.attacker, LivingEntity);
        }
    }

    public void startExecuting() {
        this.taskOwner.setAttackTarget(this.attacker);
        LivingEntity LivingEntity = this.tameable.getSummoner();

        if (LivingEntity != null) {
            this.timestamp = LivingEntity.getLastAttackedEntityTime();
        }

        super.startExecuting();
    }
}