package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.DragonUtils;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.EntityDreadThrall;
import com.github.alexthe666.iceandfire.entity.IDreadMob;
import com.google.common.base.Predicate;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetExpiringGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;

import javax.annotation.Nullable;

public class DreadAITargetNonDread extends NearestAttackableTargetGoal {

    public DreadAITargetNonDread(MobEntity entityIn, Class<LivingEntity> classTarget, boolean checkSight, Predicate<LivingEntity> targetSelector) {
        super(entityIn, classTarget, 0, checkSight, false, targetSelector);
    }

    protected boolean isSuitableTarget(@Nullable LivingEntity target, EntityPredicate targetPredicate) {
        if(super.isSuitableTarget(target, targetPredicate)){
            if(target instanceof IDreadMob || !DragonUtils.isAlive(target)){
                return false;
            }
            return true;
        }
        return false;
    }


}
