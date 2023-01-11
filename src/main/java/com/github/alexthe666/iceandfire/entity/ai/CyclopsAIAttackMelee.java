package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityCyclops;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import org.jetbrains.annotations.NotNull;

public class CyclopsAIAttackMelee extends MeleeAttackGoal {

    public CyclopsAIAttackMelee(EntityCyclops creature, double speedIn, boolean useLongMemory) {
        super(creature, speedIn, useLongMemory);
    }

    @Override
    protected void checkAndPerformAttack(@NotNull LivingEntity entity, double distance) {
        final double d0 = this.getAttackReachSqr(entity);
        if (isCyclopsBlinded() && distance >= 36D) {
            this.stop();
            return;
        }
        if (distance <= d0) {
            this.mob.swing(InteractionHand.MAIN_HAND);
            this.mob.doHurtTarget(entity);
        }
    }

    private boolean isCyclopsBlinded() {
        return this.mob instanceof EntityCyclops && ((EntityCyclops) this.mob).isBlinded();
    }
}
