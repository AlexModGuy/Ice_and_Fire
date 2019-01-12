package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityMyrmexBase;
import com.github.alexthe666.iceandfire.entity.EntityMyrmexRoyal;
import com.google.common.base.Predicate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.util.math.AxisAlignedBB;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MyrmexAIFindMate<T extends EntityMyrmexBase> extends EntityAITarget {
    protected final DragonAITargetItems.Sorter theNearestAttackableTargetSorter;
    protected final Predicate<? super Entity> targetEntitySelector;
    protected EntityMyrmexBase targetEntity;
    public EntityMyrmexRoyal myrmex;

    public MyrmexAIFindMate(EntityMyrmexRoyal myrmex) {
        super(myrmex, false, false);
        this.theNearestAttackableTargetSorter = new DragonAITargetItems.Sorter(myrmex);
        this.targetEntitySelector = new Predicate<Entity>() {
            @Override
            public boolean apply(@Nullable Entity myrmex) {
                return myrmex != null && myrmex instanceof EntityMyrmexRoyal && ((EntityMyrmexRoyal) myrmex).getGrowthStage() >= 2;
            }
        };
        this.myrmex = myrmex;
        this.setMutexBits(1);
    }

    @Override
    public boolean shouldExecute() {
        if (!this.myrmex.canMove() || this.myrmex.getAttackTarget() != null || this.myrmex.releaseTicks < 400 || this.myrmex.mate != null) {
            return false;
        }
        List<Entity> list = this.taskOwner.world.getEntitiesInAABBexcluding(myrmex, this.getTargetableArea(this.getTargetDistance()), this.targetEntitySelector);
        if (list.isEmpty()) {
            return false;
        } else {
            Collections.sort(list, this.theNearestAttackableTargetSorter);
            this.myrmex.mate = (EntityMyrmexRoyal)list.get(0);
            this.myrmex.world.setEntityState(this.myrmex, (byte) 76);
            return true;
        }
    }

    protected AxisAlignedBB getTargetableArea(double targetDistance) {
        return this.taskOwner.getEntityBoundingBox().grow(targetDistance, 4.0D, targetDistance);
    }

    @Override
    public boolean shouldContinueExecuting() {
        return false;
    }

    public static class Sorter implements Comparator<Entity> {
        private final Entity theEntity;

        public Sorter(EntityMyrmexBase theEntityIn) {
            this.theEntity = theEntityIn;
        }

        public int compare(Entity p_compare_1_, Entity p_compare_2_) {
            double d0 = this.theEntity.getDistanceSq(p_compare_1_);
            double d1 = this.theEntity.getDistanceSq(p_compare_2_);
            return d0 < d1 ? -1 : (d0 > d1 ? 1 : 0);
        }
    }
}