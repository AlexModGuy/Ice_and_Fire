package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityMyrmexWorker;
import com.github.alexthe666.iceandfire.util.IAFMath;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.phys.AABB;

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
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (!this.myrmex.canMove() || this.myrmex.holdingSomething() || this.myrmex.shouldEnterHive() || !this.myrmex.keepSearching || this.myrmex.getTarget() != null) {
            list = IAFMath.emptyItemEntityList;
            return false;
        }

        if (this.myrmex.level().getGameTime() % 4 == 0) // only update the list every 4 ticks
            list = this.mob.level().getEntitiesOfClass(ItemEntity.class, this.getTargetableArea(32), this.targetEntitySelector);

        if (list.isEmpty())
            return false;

        list.sort(this.theNearestAttackableTargetSorter);
        this.targetEntity = list.get(0);
        return true;
    }

    protected AABB getTargetableArea(double targetDistance) {
        return this.mob.getBoundingBox().inflate(targetDistance, 5, targetDistance);
    }

    @Override
    public void start() {
        this.mob.getNavigation().moveTo(this.targetEntity.getX(), this.targetEntity.getY(), this.targetEntity.getZ(), 1);
        super.start();
    }

    @Override
    public void tick() {
        super.tick();
        if (this.targetEntity == null || (!this.targetEntity.isAlive() || this.targetEntity.isInWater())) {
            this.stop();
        } else if (this.mob.distanceToSqr(this.targetEntity) < 8F) {
            this.myrmex.onPickupItem(targetEntity);
            this.myrmex.setItemInHand(InteractionHand.MAIN_HAND, this.targetEntity.getItem());
            this.targetEntity.remove(Entity.RemovalReason.DISCARDED);
            stop();
        }
    }

    @Override
    public void stop() {
        this.myrmex.getNavigation().stop();
        super.stop();
    }

    @Override
    public boolean canContinueToUse() {
        return !this.mob.getNavigation().isDone() && this.myrmex.getTarget() == null;
    }

    public static class Sorter implements Comparator<Entity> {
        private final Entity theEntity;

        public Sorter(Entity theEntityIn) {
            this.theEntity = theEntityIn;
        }

        @Override
        public int compare(Entity p_compare_1_, Entity p_compare_2_) {
            final double d0 = this.theEntity.distanceToSqr(p_compare_1_);
            final double d1 = this.theEntity.distanceToSqr(p_compare_2_);
            return Double.compare(d0, d1);
        }
    }
}