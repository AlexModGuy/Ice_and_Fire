package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityMyrmexBase;
import com.github.alexthe666.iceandfire.entity.EntityMyrmexEgg;
import com.github.alexthe666.iceandfire.entity.EntityMyrmexWorker;
import com.github.alexthe666.iceandfire.entity.util.EntityUtil;
import com.github.alexthe666.iceandfire.util.IAFMath;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.phys.AABB;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;

public class MyrmexAIPickupBabies extends TargetGoal {
    protected final Predicate<? super LivingEntity> targetEntitySelector;
    public EntityMyrmexWorker myrmex;
    protected @Nullable LivingEntity targetEntity;

    private List<LivingEntity> listBabies = IAFMath.emptyLivingEntityList;

    public MyrmexAIPickupBabies(EntityMyrmexWorker myrmex) {
        super(myrmex, false, false);
        this.targetEntitySelector = (Predicate<LivingEntity>) toCheck -> isYoungMyrmex(toCheck) || isMyrmexEgg(toCheck);
        this.myrmex = myrmex;
        this.setFlags(EnumSet.of(Flag.TARGET));
    }

    private boolean isYoungMyrmex(final Entity entity) {
        return entity instanceof EntityMyrmexBase base && base.getGrowthStage() < 2 && !base.isInNursery();
    }

    private boolean isMyrmexEgg(final Entity entity) {
        return entity instanceof EntityMyrmexEgg egg && !egg.isInNursery();
    }

    @Override
    public boolean canUse() {
        if (!this.myrmex.canMove() || this.myrmex.holdingSomething() || !this.myrmex.getNavigation().isDone() || this.myrmex.shouldEnterHive() || !this.myrmex.keepSearching || this.myrmex.holdingBaby()) {
            listBabies = IAFMath.emptyLivingEntityList;
            return false;
        }

        updateList(true);
        return targetEntity != null;
    }

    protected AABB getTargetableArea(double targetDistance) {
        return this.mob.getBoundingBox().inflate(targetDistance, 4.0D, targetDistance);
    }

    private void updateList(boolean forceNew) {
        listBabies = EntityUtil.updateList(mob, forceNew ? null : listBabies, () -> this.mob.level.getEntitiesOfClass(LivingEntity.class, this.getTargetableArea(20), this.targetEntitySelector));
        targetEntity = !listBabies.isEmpty() ? listBabies.get(0) : null;
    }

    @Override
    public void start() {
        updateWantedPosition();
        super.start();
    }

    private void updateWantedPosition() {
        if (targetEntity == null) {
            stop();
            return;
        }

        this.mob.getNavigation().moveTo(this.targetEntity.getX(), this.targetEntity.getY(), this.targetEntity.getZ(), 1);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.targetEntity != null && this.targetEntity.isAlive() && this.mob.distanceToSqr(this.targetEntity) < 2) {
            this.targetEntity.startRiding(this.myrmex);
            stop();
        } else if (!mob.getMoveControl().hasWanted()) {
            updateList(false);
            updateWantedPosition();
        }
    }

    @Override
    public boolean canContinueToUse() {
        return !this.mob.getNavigation().isDone();
    }

    @Override
    public void stop() {
        super.stop();
        targetEntity = null;
        listBabies = Collections.emptyList();
    }
}