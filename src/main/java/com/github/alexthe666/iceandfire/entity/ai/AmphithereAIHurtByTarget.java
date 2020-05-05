package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.player.PlayerEntity;

public class AmphithereAIHurtByTarget extends EntityAIHurtByTarget {

    public AmphithereAIHurtByTarget(EntityAmphithere amphithere, boolean help, Class[] classes) {
        super(amphithere, help, classes);
    }

    protected void setEntityAttackTarget(EntityCreature creatureIn, LivingEntity LivingEntityIn) {
        EntityAmphithere amphithere = (EntityAmphithere) creatureIn;
        if (amphithere.isTamed() || !(LivingEntityIn instanceof PlayerEntity)) {
            amphithere.setAttackTarget(LivingEntityIn);
        }
    }
}
