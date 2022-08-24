package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityHippogryph;
import com.github.alexthe666.iceandfire.entity.util.DragonUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;

import javax.annotation.Nullable;
import java.util.function.Predicate;

public class HippogryphAITarget<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {
    private final EntityHippogryph hippogryph;

    public HippogryphAITarget(EntityHippogryph entityIn, Class<T> classTarget, boolean checkSight, @Nullable Predicate<LivingEntity> targetPredicate) {
        super(entityIn, classTarget, 20, checkSight, false, targetPredicate);
        this.hippogryph = entityIn;
    }

    public HippogryphAITarget(EntityHippogryph entityIn, Class<T> classTarget, int i, boolean checkSight, @Nullable Predicate<LivingEntity> targetPredicate) {
        super(entityIn, classTarget, i, checkSight, false, targetPredicate);
        this.hippogryph = entityIn;
    }


    @Override
    public boolean canUse() {
        if (this.mob.getRandom().nextInt(20) != 0) {
            return false;
        }
        if (super.canUse() && target != null && !target.getClass().equals(this.hippogryph.getClass())) {
            if (this.hippogryph.getBbWidth() >= target.getBbWidth()) {
                if (target instanceof PlayerEntity) {
                    return !hippogryph.isTame();
                } else {
                    if (!hippogryph.isOwnedBy(target) && hippogryph.canMove() && target instanceof AnimalEntity) {
                        if (hippogryph.isTame()) {
                            return DragonUtils.canTameDragonAttack(hippogryph, target);
                        } else {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}