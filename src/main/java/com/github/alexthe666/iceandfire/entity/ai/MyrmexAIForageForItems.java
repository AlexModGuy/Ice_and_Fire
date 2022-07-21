package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityMyrmexWorker;
import com.github.alexthe666.iceandfire.util.IAFMath;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.goal.TargetGoal;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;

import javax.annotation.Nonnull;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;

public class MyrmexAIForageForItems<T extends ItemEntity> extends TargetGoal {
    protected final DragonAITargetItems.Sorter theNearestAttackableTargetSorter;
    protected final Predicate<? super ItemEntity> targetEntitySelector;
    public EntityMyrmexWorker myrmex;
    protected ItemEntity targetEntity;

    @Nonnull
    private List<ItemEntity> list = IAFMath.emptyItemEntityList;

    public MyrmexAIForageForItems(EntityMyrmexWorker myrmex) {
        super(myrmex, false, false);
        this.theNearestAttackableTargetSorter = new DragonAITargetItems.Sorter(myrmex);
        this.targetEntitySelector = new Predicate<ItemEntity>() {
            @Override
            public boolean test(ItemEntity item) {
                return item != null && !item.getItem().isEmpty() && !item.isInWater();
            }
        };
        this.myrmex = myrmex;
        this.setMutexFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean shouldExecute() {
        if (!this.myrmex.canMove() || this.myrmex.holdingSomething() || this.myrmex.shouldEnterHive() || !this.myrmex.keepSearching || this.myrmex.getAttackTarget() != null) {
            list = IAFMath.emptyItemEntityList;
            return false;
        }

        if (this.myrmex.world.getGameTime() % 4 == 0) // only update the list every 4 ticks
            list = this.goalOwner.world.getEntitiesWithinAABB(ItemEntity.class, this.getTargetableArea(32), this.targetEntitySelector);

        if (list.isEmpty())
            return false;

        list.sort(this.theNearestAttackableTargetSorter);
        this.targetEntity = list.get(0);
        return true;
    }

    protected AxisAlignedBB getTargetableArea(double targetDistance) {
        return this.goalOwner.getBoundingBox().grow(targetDistance, 5, targetDistance);
    }

    @Override
    public void startExecuting() {
        this.goalOwner.getNavigator().tryMoveToXYZ(this.targetEntity.getPosX(), this.targetEntity.getPosY(), this.targetEntity.getPosZ(), 1);
        super.startExecuting();
    }

    @Override
    public void tick() {
        super.tick();
        if (this.targetEntity == null || (!this.targetEntity.isAlive() || this.targetEntity.isInWater())) {
            this.resetTask();
        } else if (this.goalOwner.getDistanceSq(this.targetEntity) < 8F) {
            this.myrmex.onPickupItem(targetEntity);
            this.myrmex.setHeldItem(Hand.MAIN_HAND, this.targetEntity.getItem());
            this.targetEntity.remove();
            resetTask();
        }
    }

    @Override
    public void resetTask() {
        this.myrmex.getNavigator().clearPath();
        super.resetTask();
    }

    @Override
    public boolean shouldContinueExecuting() {
        return !this.goalOwner.getNavigator().noPath() && this.myrmex.getAttackTarget() == null;
    }

    public static class Sorter implements Comparator<Entity> {
        private final Entity theEntity;

        public Sorter(Entity theEntityIn) {
            this.theEntity = theEntityIn;
        }

        @Override
        public int compare(Entity p_compare_1_, Entity p_compare_2_) {
            final double d0 = this.theEntity.getDistanceSq(p_compare_1_);
            final double d1 = this.theEntity.getDistanceSq(p_compare_2_);
            return Double.compare(d0, d1);
        }
    }
}