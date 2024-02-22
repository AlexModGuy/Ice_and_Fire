package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.api.FoodUtils;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.ai.base.PickUpTargetGoal;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;

public class DragonAITargetItems extends PickUpTargetGoal<EntityDragonBase, ItemEntity> {
    private final boolean checkHunger;
    private final boolean canEatFish;

    public DragonAITargetItems(final EntityDragonBase creature, boolean checkHunger, boolean canEatFish) {
        this(creature, 0.2, checkHunger, canEatFish);
    }

    public DragonAITargetItems(final EntityDragonBase dragon, double chance, boolean checkHunger, boolean canEatFish) {
        super(dragon, false, false, item -> FoodUtils.getFoodPoints(dragon, item.getItem(), true, canEatFish) > 0, chance);
        this.checkHunger = checkHunger;
        this.canEatFish = canEatFish;
    }

    @Override
    public boolean canUse() {
        final EntityDragonBase dragon = getMob();

        if (checkHunger && dragon.getHunger() >= 60 || dragon.getHunger() >= EntityDragonBase.MAX_HUNGER || !dragon.canMove()) {
            return false;
        }

        return super.canUse();
    }

    @Override
    public void tick() {
        super.tick();

        if (currentTarget == null) {
            return;
        }

        EntityDragonBase dragon = getMob();

        boolean canReachVertical = dragon.distanceToSqr(currentTarget) < dragon.getBbWidth() * 2 + dragon.getBbHeight() / 2;
        boolean canReachHorizontal = dragon.getHeadPosition().distanceToSqr(currentTarget.position()) < dragon.getBbHeight();

        if (canReachVertical || canReachHorizontal) {
            ItemStack stack = currentTarget.getItem();
            int hunger = FoodUtils.getFoodPoints(dragon, stack, true, canEatFish);

            dragon.playSound(SoundEvents.GENERIC_EAT, 1, 1);
            dragon.setHunger(Math.min(EntityDragonBase.MAX_HUNGER, dragon.getHunger() + hunger));
            dragon.applyFoodEffects(stack);
            dragon.setHealth(Math.min(this.mob.getMaxHealth(), (int) (this.mob.getHealth() + hunger)));

            if (EntityDragonBase.ANIMATION_EAT != null) {
                dragon.setAnimation(EntityDragonBase.ANIMATION_EAT);
            }

            for (int i = 0; i < 4; i++) {
                dragon.spawnItemCrackParticles(stack.getItem());
            }

            stack.shrink(1);
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
        return getFollowDistance();
    }
}
