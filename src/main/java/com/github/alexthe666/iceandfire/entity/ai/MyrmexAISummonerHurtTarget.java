package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityMyrmexSwarmer;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.TargetGoal;

import java.util.EnumSet;

public class MyrmexAISummonerHurtTarget extends TargetGoal {
    EntityMyrmexSwarmer tameable;
    LivingEntity attacker;
    private int timestamp;

    public MyrmexAISummonerHurtTarget(EntityMyrmexSwarmer theEntityMyrmexSwarmerIn) {
        super(theEntityMyrmexSwarmerIn, false);
        this.tameable = theEntityMyrmexSwarmerIn;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        LivingEntity living = this.tameable.getSummoner();

        if (living == null) {
            return false;
        } else {
            this.attacker = living.getLastHurtMob();
            int i = living.getLastHurtMobTimestamp();
            return i != this.timestamp && this.canAttack(this.attacker, EntityPredicate.DEFAULT)
                && this.tameable.shouldAttackEntity(this.attacker, living);
        }
    }

    @Override
    public void start() {
        this.mob.setTarget(this.attacker);
        LivingEntity LivingEntity = this.tameable.getSummoner();

        if (LivingEntity != null) {
            this.timestamp = LivingEntity.getLastHurtMobTimestamp();
        }

        super.start();
    }
}