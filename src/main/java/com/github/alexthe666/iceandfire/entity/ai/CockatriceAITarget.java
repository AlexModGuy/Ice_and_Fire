package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityCockatrice;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.Difficulty;

import java.util.EnumSet;
import java.util.function.Predicate;

public class CockatriceAITarget<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {
    private EntityCockatrice cockatrice;

    public CockatriceAITarget(EntityCockatrice entityIn, Class<T> classTarget, boolean checkSight, Predicate<LivingEntity> targetSelector) {
        super(entityIn, classTarget, 0, checkSight, false, targetSelector);
        this.cockatrice = entityIn;
        this.setMutexFlags(EnumSet.of(Flag.TARGET));
    }

    @Override
    public boolean shouldExecute() {
        if (this.goalOwner.getRNG().nextInt(20) != 0 || this.cockatrice.world.getDifficulty() == Difficulty.PEACEFUL) {
            return false;
        }
        if (super.shouldExecute() && this.target != null && !this.target.getClass().equals(this.cockatrice.getClass())) {
            if (this.target instanceof PlayerEntity && !cockatrice.isOwner(this.target)) {
                return !cockatrice.isTamed();
            } else {
                return !cockatrice.isOwner(this.target) && cockatrice.canMove();
            }
        }
        return false;
    }
}