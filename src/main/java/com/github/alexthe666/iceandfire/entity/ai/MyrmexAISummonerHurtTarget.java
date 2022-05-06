package com.github.alexthe666.iceandfire.entity.ai;

import java.util.EnumSet;

import com.github.alexthe666.iceandfire.entity.EntityMyrmexSwarmer;

import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.TargetGoal;

public class MyrmexAISummonerHurtTarget extends TargetGoal {
    EntityMyrmexSwarmer tameable;
    LivingEntity attacker;
    private int timestamp;

    public MyrmexAISummonerHurtTarget(EntityMyrmexSwarmer theEntityMyrmexSwarmerIn) {
        super(theEntityMyrmexSwarmerIn, false);
        this.tameable = theEntityMyrmexSwarmerIn;
        this.setMutexFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean shouldExecute() {
        LivingEntity living = this.tameable.getSummoner();

        if (living == null) {
            return false;
        } else {
            this.attacker = living.getLastAttackedEntity();
            int i = living.getLastAttackedEntityTime();
            return i != this.timestamp && this.isSuitableTarget(this.attacker, EntityPredicate.DEFAULT)
                && this.tameable.shouldAttackEntity(this.attacker, living);
        }
    }

    @Override
    public void startExecuting() {
        this.goalOwner.setAttackTarget(this.attacker);
        LivingEntity LivingEntity = this.tameable.getSummoner();

        if (LivingEntity != null) {
            this.timestamp = LivingEntity.getLastAttackedEntityTime();
        }

        super.startExecuting();
    }
}