package com.github.alexthe666.iceandfire.entity.ai;

import java.util.function.Predicate;

import javax.annotation.Nullable;

import com.github.alexthe666.iceandfire.entity.util.DragonUtils;
import com.github.alexthe666.iceandfire.entity.util.IDreadMob;

import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;

public class DreadAITargetNonDread extends NearestAttackableTargetGoal<LivingEntity> {

    public DreadAITargetNonDread(MobEntity entityIn, Class<LivingEntity> classTarget, boolean checkSight,
        Predicate<LivingEntity> targetSelector) {
        super(entityIn, classTarget, 0, checkSight, false, targetSelector);
    }

    @Override
    protected boolean isSuitableTarget(@Nullable LivingEntity target, EntityPredicate targetPredicate) {
        if (super.isSuitableTarget(target, targetPredicate)) {
            return !(target instanceof IDreadMob) && DragonUtils.isAlive(target);
        }
        return false;
    }

}
