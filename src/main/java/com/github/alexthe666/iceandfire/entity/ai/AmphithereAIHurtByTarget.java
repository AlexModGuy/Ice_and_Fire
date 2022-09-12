package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.player.Player;

public class AmphithereAIHurtByTarget extends HurtByTargetGoal {

    public AmphithereAIHurtByTarget(EntityAmphithere amphithere, boolean help, Class<?>[] classes) {
        super(amphithere, classes);
    }

    protected static void setEntityAttackTarget(Mob creatureIn, LivingEntity LivingEntityIn) {
        EntityAmphithere amphithere = (EntityAmphithere) creatureIn;
        if (amphithere.isTame() || !(LivingEntityIn instanceof Player)) {
            amphithere.setTarget(LivingEntityIn);
        }
    }
}
