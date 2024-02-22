package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.datagen.tags.IafItemTags;
import com.github.alexthe666.iceandfire.entity.EntityHippogryph;
import com.github.alexthe666.iceandfire.entity.ai.base.PickUpTargetGoal;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;

public class HippogryphAITargetItems extends PickUpTargetGoal<EntityHippogryph, ItemEntity> {
    public HippogryphAITargetItems(final EntityHippogryph hippogryph, boolean mustSee, boolean mustReach) {
        super(hippogryph, mustSee, mustReach, item -> item.getItem().is(IafItemTags.TAME_HIPPOGRYPH));
    }

    @Override
    public boolean canUse() {
        if (!getMob().canMove() || this.mob.getRandom().nextInt(20) != 0) {
            return false;
        }

        return super.canUse();
    }

    @Override
    public void tick() {
        super.tick();

        if (currentTarget != null && getAttackReachSqr(currentTarget) >= this.mob.distanceToSqr(currentTarget)) {
            EntityHippogryph hippogryph = getMob();
            hippogryph.playSound(SoundEvents.GENERIC_EAT, 1, 1);
            hippogryph.setAnimation(EntityHippogryph.ANIMATION_EAT);
            hippogryph.feedings++;
            hippogryph.heal(4);

            currentTarget.getItem().shrink(1);

            if (currentTarget.getThrowingEntity() instanceof Player player && hippogryph.feedings > 3 && (hippogryph.feedings > 7 || hippogryph.getRandom().nextInt(3) == 0) && !hippogryph.isTame()) {
                hippogryph.tame(player);
                hippogryph.setTarget(null);
                hippogryph.setCommand(1);
                hippogryph.setOrderedToSit(true);
            }

            stop();
        }
    }

    @Override
    protected void setMovement() {
        if (currentTarget == null) {
            return;
        }

        mob.getNavigation().moveTo(currentTarget, 1);
    }

    @Override
    public boolean canContinueToUse() {
        return !mob.getNavigation().isDone();
    }

    protected double getAttackReachSqr(final Entity attackTarget) {
        return this.mob.getBbWidth() * 2.0F * this.mob.getBbWidth() * 2.0F + attackTarget.getBbWidth();
    }

    @Override
    protected double getSearchRange() {
        return getFollowDistance();
    }
}