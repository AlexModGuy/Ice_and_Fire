package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityMyrmexBase;
import com.github.alexthe666.iceandfire.entity.EntityMyrmexSoldier;
import com.github.alexthe666.iceandfire.entity.util.EntityUtil;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.phys.AABB;

import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;

public class MyrmexAIFindGaurdingEntity extends TargetGoal {
    private final EntityUtil.Sorter sorter;
    private final Predicate<? super EntityMyrmexBase> targetEntitySelector;
    private final EntityMyrmexSoldier soldier;

    public MyrmexAIFindGaurdingEntity(final EntityMyrmexSoldier soldier) {
        super(soldier, false, false);
        this.sorter = new EntityUtil.Sorter(soldier);
        this.targetEntitySelector = (Predicate<EntityMyrmexBase>) myrmex -> shouldGuardMyrmex(soldier, myrmex);
        this.soldier = soldier;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (!soldier.canMove() || soldier.getTarget() != null || soldier.guardingEntity != null) {
            return false;
        }

        List<EntityMyrmexBase> list = this.mob.level.getEntitiesOfClass(EntityMyrmexBase.class, this.getTargetableArea(this.getFollowDistance()), this.targetEntitySelector);

        if (list.isEmpty()) {
            return false;
        } else {
            list.sort(this.sorter);
            this.soldier.guardingEntity = list.get(0);
            return true;
        }
    }

    protected AABB getTargetableArea(double targetDistance) {
        return this.mob.getBoundingBox().inflate(targetDistance, 4.0D, targetDistance);
    }

    @Override
    public boolean canContinueToUse() {
        return false;
    }

    private static boolean shouldGuardMyrmex(final EntityMyrmexSoldier instance, final EntityMyrmexBase myrmex) {
        if (myrmex instanceof EntityMyrmexSoldier) {
            return false;
        }

        return myrmex.getGrowthStage() > 1 && EntityMyrmexBase.haveSameHive(instance, myrmex) && !myrmex.isBeingGuarded && myrmex.needsGaurding();
    }
}