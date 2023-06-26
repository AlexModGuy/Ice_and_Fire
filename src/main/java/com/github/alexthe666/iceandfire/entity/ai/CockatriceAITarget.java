package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityCockatrice;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;

import java.util.EnumSet;
import java.util.function.Predicate;

public class CockatriceAITarget<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {
    private final EntityCockatrice cockatrice;

    public CockatriceAITarget(EntityCockatrice entityIn, Class<T> classTarget, boolean checkSight, Predicate<LivingEntity> targetSelector) {
        super(entityIn, classTarget, 0, checkSight, false, targetSelector);
        this.cockatrice = entityIn;
        this.setFlags(EnumSet.of(Flag.TARGET));
    }

    @Override
    public boolean canUse() {
        if (this.mob.getRandom().nextInt(20) != 0 || this.cockatrice.level().getDifficulty() == Difficulty.PEACEFUL) {
            return false;
        }
        if (super.canUse() && target != null && !target.getClass().equals(this.cockatrice.getClass())) {
            if (target instanceof Player && !cockatrice.isOwnedBy(target)) {
                return !cockatrice.isTame();
            } else {
                return !cockatrice.isOwnedBy(target) && cockatrice.canMove();
            }
        }
        return false;
    }
}