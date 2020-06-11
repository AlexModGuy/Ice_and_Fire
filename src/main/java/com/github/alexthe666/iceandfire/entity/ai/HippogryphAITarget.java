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
    private EntityHippogryph hippogryph;

    public HippogryphAITarget(EntityHippogryph entityIn, Class<T> classTarget, boolean checkSight, @Nullable Predicate<LivingEntity> targetPredicate) {
        super(entityIn, classTarget, 20, checkSight, false, targetPredicate);
        this.hippogryph = entityIn;
    }

    @Override
    public boolean shouldExecute() {
        if (this.goalOwner.getRNG().nextInt(20) != 0) {
            return false;
        }
        if (super.shouldExecute() && this.target != null && !this.target.getClass().equals(this.hippogryph.getClass())) {
            if (this.hippogryph.getWidth() >= this.target.getWidth()) {
                if (this.target instanceof PlayerEntity) {
                    return !hippogryph.isTamed();
                } else {
                    if (!hippogryph.isOwner(this.target) && hippogryph.canMove() && this.target instanceof AnimalEntity) {
                        if (hippogryph.isTamed()) {
                            return DragonUtils.canTameDragonAttack(hippogryph, this.target);
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