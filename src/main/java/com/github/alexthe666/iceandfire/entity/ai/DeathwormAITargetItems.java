package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.datagen.tags.IafItemTags;
import com.github.alexthe666.iceandfire.entity.EntityDeathWorm;
import com.github.alexthe666.iceandfire.entity.ai.base.PickUpTargetGoal;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;

public class DeathwormAITargetItems extends PickUpTargetGoal<EntityDeathWorm, ItemEntity> {
    public DeathwormAITargetItems(EntityDeathWorm creature) {
        super(creature, false, false, DeathwormAITargetItems::isItemOnSand, 0.1);
    }

    @Override
    public void tick() {
        super.tick();

        if (currentTarget != null && mob.distanceToSqr(currentTarget) < 1) {
            EntityDeathWorm worm = getMob();
            worm.playSound(SoundEvents.GENERIC_EAT, 1, 1);
            worm.setAnimation(EntityDeathWorm.ANIMATION_BITE);
            worm.setExplosive(currentTarget.getThrowingEntity() instanceof Player player ? player : null);
            currentTarget.getItem().shrink(1);
            stop();
        }
    }

    @Override
    public boolean canContinueToUse() {
        if (currentTarget != null) {
            Entity thrower = currentTarget.getThrowingEntity();
            if (/* Can be null */ thrower != null && thrower.isAlliedTo(mob)) {
                return false;
            } else {
                double distance = this.getFollowDistance();
                return !(mob.distanceToSqr(currentTarget) > distance * distance);
            }
        }

        return true;
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

    private static boolean isItemOnSand(final ItemEntity itemEntity) {
        return itemEntity.getItem().is(IafItemTags.TNT) && itemEntity.getLevel().getBlockState(itemEntity.blockPosition().below()).is(BlockTags.SAND);
    }
}
