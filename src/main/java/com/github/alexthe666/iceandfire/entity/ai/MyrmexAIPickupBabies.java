package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityMyrmexBase;
import com.github.alexthe666.iceandfire.entity.EntityMyrmexEgg;
import com.github.alexthe666.iceandfire.entity.EntityMyrmexWorker;
import com.github.alexthe666.iceandfire.entity.ai.base.PickUpTargetGoal;
import net.minecraft.world.entity.LivingEntity;

public class MyrmexAIPickupBabies extends PickUpTargetGoal<EntityMyrmexWorker, LivingEntity> {
    public MyrmexAIPickupBabies(final EntityMyrmexWorker worker) {
        super(worker, false, false, entity -> isYoungMyrmex(entity) || isMyrmexEgg(entity), 1);
    }

    @Override
    public boolean canUse() {
        EntityMyrmexWorker worker = getMob();

        if (!worker.canMove() || worker.holdingSomething() || !worker.getNavigation().isDone() || worker.shouldEnterHive() || !worker.keepSearching || worker.holdingBaby()) {
            return false;
        }

        return super.canUse();
    }

    @Override
    public void tick() {
        super.tick();

        if (currentTarget != null && mob.distanceToSqr(currentTarget) < 2) {
            currentTarget.startRiding(getMob());
            stop();
        }
    }

    @Override
    public boolean canContinueToUse() {
        return !mob.getNavigation().isDone();
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
        return 20;
    }

    private static boolean isYoungMyrmex(final LivingEntity entity) {
        return entity instanceof EntityMyrmexBase base && base.getGrowthStage() < 2 && !base.isInNursery();
    }

    private static boolean isMyrmexEgg(final LivingEntity entity) {
        return entity instanceof EntityMyrmexEgg egg && !egg.isInNursery();
    }
}