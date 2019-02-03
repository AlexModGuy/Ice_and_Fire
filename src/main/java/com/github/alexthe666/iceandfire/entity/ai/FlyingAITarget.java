package com.github.alexthe666.iceandfire.entity.ai;

import com.google.common.base.Predicate;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.util.math.AxisAlignedBB;

import javax.annotation.Nullable;

public class FlyingAITarget extends EntityAINearestAttackableTarget {

    public FlyingAITarget(EntityCreature creature, Class classTarget, boolean checkSight) {
        super(creature, classTarget, checkSight);
    }

    public FlyingAITarget(EntityCreature creature, Class classTarget, boolean checkSight, boolean onlyNearby) {
        super(creature, classTarget, checkSight, onlyNearby);
    }

    public FlyingAITarget(EntityCreature creature, Class classTarget, int chance, boolean checkSight, boolean onlyNearby, @Nullable final Predicate targetSelector) {
        super(creature, classTarget, chance, checkSight, onlyNearby, targetSelector);
    }

        @Override
    protected AxisAlignedBB getTargetableArea(double targetDistance) {
        return this.taskOwner.getEntityBoundingBox().grow(targetDistance, targetDistance, targetDistance);
    }

}
