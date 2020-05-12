package com.github.alexthe666.iceandfire.entity.ai;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;

public class EntityAIAttackMeleeNoCooldown extends MeleeAttackGoal {
    public EntityAIAttackMeleeNoCooldown(CreatureEntity creature, double speed, boolean memory) {
        super(creature, speed, memory);
    }

    public void tick() {
        super.tick();
        this.attackTick = 0;
    }
}
