package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityMyrmexWorker;
import com.github.alexthe666.iceandfire.entity.ai.base.PickUpTargetGoal;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;

import java.util.EnumSet;

public class MyrmexAIForageForItems extends PickUpTargetGoal<EntityMyrmexWorker, ItemEntity> {
    public MyrmexAIForageForItems(final EntityMyrmexWorker worker) {
        super(worker, false, false, item -> !item.isInWater(), 1);
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        EntityMyrmexWorker worker = getMob();

        if (!worker.canMove() || worker.holdingSomething() || worker.shouldEnterHive() || !worker.keepSearching || worker.getTarget() != null) {
            return false;
        }

        return super.canUse();
    }

    @Override
    public void tick() {
        super.tick();

        if (currentTarget == null || currentTarget.isInWater()) {
            currentTarget = null;
            return;
        }

       if (mob.distanceToSqr(currentTarget) < 8F) {
           EntityMyrmexWorker worker = getMob();
           worker.onPickupItem(currentTarget);
           worker.setItemInHand(InteractionHand.MAIN_HAND, currentTarget.getItem());
           currentTarget.remove(Entity.RemovalReason.DISCARDED);
           stop();
        }
    }

    @Override
    public boolean canContinueToUse() {
        return !this.mob.getNavigation().isDone() && mob.getTarget() == null;
    }

    @Override
    protected void setMovement() {
        if (currentTarget == null) {
            return;
        }

        mob.getNavigation().moveTo(currentTarget, 1);
    }

    @Override
    protected double getSearchRange() {
        return 32;
    }
}