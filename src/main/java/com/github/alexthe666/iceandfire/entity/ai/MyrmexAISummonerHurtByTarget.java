package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityMyrmexSwarmer;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.TargetGoal;

import java.util.EnumSet;

public class MyrmexAISummonerHurtByTarget extends TargetGoal {
    EntityMyrmexSwarmer tameable;
    LivingEntity attacker;
    private int timestamp;

    public MyrmexAISummonerHurtByTarget(EntityMyrmexSwarmer theDefendingTameableIn) {
        super(theDefendingTameableIn, false);
        this.tameable = theDefendingTameableIn;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        LivingEntity living = this.tameable.getSummoner();

        if (living == null) {
            return false;
        } else {
            this.attacker = living.getLastHurtByMob();
            int i = living.getLastHurtByMobTimestamp();
            return i != this.timestamp && this.canAttack(this.attacker, EntityPredicate.DEFAULT) && this.tameable.shouldAttackEntity(this.attacker, living);
        }
    }

    @Override
    public void start() {
        this.mob.setTarget(this.attacker);
        LivingEntity LivingEntity = this.tameable.getSummoner();

        if (LivingEntity != null) {
            this.timestamp = LivingEntity.getLastHurtByMobTimestamp();
        }

        super.start();
    }
}