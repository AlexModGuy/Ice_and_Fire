package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.datagen.tags.IafItemTags;
import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import com.github.alexthe666.iceandfire.entity.ai.base.PickUpTargetGoal;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.item.ItemEntity;

public class AmphithereAITargetItems extends PickUpTargetGoal<EntityAmphithere, ItemEntity> {
    public AmphithereAITargetItems(final EntityAmphithere ampithere) {
        super(ampithere, false, false, item -> item.getItem().is(IafItemTags.HEAL_AMPITHERE), 0.2);
    }

    @Override
    public boolean canUse() {
        if (!getMob().canMove() || mob.getHealth() >= mob.getMaxHealth()) {
            return false;
        }

        return super.canUse();
    }

    @Override
    public void tick() {
        super.tick();

        if (currentTarget != null && mob.distanceToSqr(currentTarget) < 1) {
            EntityAmphithere ampithere = getMob();
            ampithere.playSound(SoundEvents.GENERIC_EAT, 1, 1);
            ampithere.heal(5);
            currentTarget.getItem().shrink(1);
            stop();
        }
    }

    @Override
    public boolean canContinueToUse() {
        return !this.mob.getNavigation().isDone();
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
        return getFollowDistance();
    }
}