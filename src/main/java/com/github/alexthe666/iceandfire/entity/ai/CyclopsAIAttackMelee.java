package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityCyclops;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.util.EnumHand;

public class CyclopsAIAttackMelee extends EntityAIAttackMelee {

    public CyclopsAIAttackMelee(EntityCyclops creature, double speedIn, boolean useLongMemory) {
        super(creature, speedIn, useLongMemory);
    }

    protected void checkAndPerformAttack(EntityLivingBase entity, double distance) {
        double d0 = this.getAttackReachSqr(entity);
        if (isCyclopsBlinded() && distance >= 36D) {
            this.resetTask();
            return;
        }
        if (distance <= d0 && this.attackTick <= 0) {
            this.attackTick = 20;
            this.attacker.swingArm(EnumHand.MAIN_HAND);
            this.attacker.attackEntityAsMob(entity);
        }
    }

    private boolean isCyclopsBlinded() {
        return this.attacker instanceof EntityCyclops && ((EntityCyclops) this.attacker).isBlinded();
    }
}
