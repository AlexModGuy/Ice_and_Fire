package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.datagen.tags.IafItemTags;
import com.github.alexthe666.iceandfire.entity.EntityCockatrice;
import com.github.alexthe666.iceandfire.entity.ai.base.PickUpTargetGoal;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.item.ItemEntity;

public class CockatriceAITargetItems extends PickUpTargetGoal<EntityCockatrice, ItemEntity> {
    public CockatriceAITargetItems(EntityCockatrice creature, boolean mustSee, boolean mustReach) {
        super(creature, mustSee, mustReach, item -> item.getItem().is(IafItemTags.HEAL_COCKATRICE), 0.1);
    }

    @Override
    public boolean canUse() {
        EntityCockatrice cockatrice = getMob();

        if (!cockatrice.canMove() || cockatrice.getHealth() >= cockatrice.getMaxHealth()) {
            return false;
        }

        return super.canUse();
    }

    @Override
    public void tick() {
        super.tick();

        if (currentTarget != null && mob.distanceToSqr(currentTarget) < 1) {
            EntityCockatrice cockatrice = getMob();
            cockatrice.playSound(SoundEvents.GENERIC_EAT, 1, 1);
            cockatrice.heal(8);
            cockatrice.setAnimation(EntityCockatrice.ANIMATION_EAT);
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
