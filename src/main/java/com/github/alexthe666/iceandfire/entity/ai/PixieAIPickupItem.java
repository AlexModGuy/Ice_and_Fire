package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.datagen.tags.IafItemTags;
import com.github.alexthe666.iceandfire.entity.EntityPixie;
import com.github.alexthe666.iceandfire.entity.ai.base.PickUpTargetGoal;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class PixieAIPickupItem extends PickUpTargetGoal<EntityPixie, ItemEntity> {
    public PixieAIPickupItem(final EntityPixie pixie, boolean mustSee, boolean mustReach) {
        super(pixie, mustSee, mustReach, item -> checkTamed(pixie, item) || checkUntamed(pixie, item));
    }

    @Override
    public boolean canUse() {
        if (getMob().isPixieSitting()) {
            return false;
        }

        return super.canUse();
    }

    @Override
    public void tick() {
        super.tick();

        if (currentTarget != null && mob.distanceToSqr(currentTarget) < 1) {
            EntityPixie pixie = getMob();
            ItemStack stack = currentTarget.getItem();

            if (stack.is(IafItemTags.HEAL_PIXIE)) {
                pixie.heal(5);
            }

            if (stack.is(IafItemTags.TAME_PIXIE) && currentTarget.getThrowingEntity() instanceof Player player && !pixie.isTame()) {
                pixie.tame(player);
                pixie.setPixieSitting(true);
                pixie.setOnGround(true);
            }

            pixie.setItemInHand(InteractionHand.MAIN_HAND, stack);
            pixie.playSound(IafSoundRegistry.PIXIE_TAUNT, 1, 1);
            stack.shrink(1);

            stop();
        }
    }

    @Override
    public boolean canContinueToUse() {
        return true;
    }

    @Override
    protected void setMovement() {
        if (currentTarget == null) {
            return;
        }

        this.mob.getMoveControl().setWantedPosition(currentTarget.getX(), currentTarget.getY(), currentTarget.getZ(), 0.25D);

        if (/* Attack target */ mob.getTarget() == null) {
            this.mob.getLookControl().setLookAt(currentTarget, 180.0F, 20.0F);
        }
    }

    @Override
    protected double getSearchRange() {
        return getFollowDistance();
    }

    private static boolean checkUntamed(final EntityPixie pixie, final ItemEntity itemEntity) {
        return !pixie.isTame() && itemEntity.getItem().is(IafItemTags.TAME_PIXIE);
    }

    private static boolean checkTamed(final EntityPixie pixie, final ItemEntity itemEntity) {
        return pixie.getHealth() < pixie.getMaxHealth() && pixie.isTame() && itemEntity.getItem().is(IafItemTags.HEAL_PIXIE);
    }
}