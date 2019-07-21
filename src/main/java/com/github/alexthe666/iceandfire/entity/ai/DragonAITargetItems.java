package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.api.FoodUtils;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.google.common.base.Predicate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.Comparator;
import java.util.List;

public class DragonAITargetItems<T extends EntityItem> extends EntityAITarget {
    protected final Sorter theNearestAttackableTargetSorter;
    protected final Predicate<? super EntityItem> targetEntitySelector;
    private final int targetChance;
    protected EntityItem targetEntity;
    private boolean isIce = false;

    public DragonAITargetItems(EntityDragonBase dragonBase, boolean checkSight) {
        this(dragonBase, checkSight, false);
    }

    public DragonAITargetItems(EntityDragonBase dragonBase, boolean checkSight, boolean onlyNearby) {
        this(dragonBase, 5, checkSight, onlyNearby);
    }

    public DragonAITargetItems(EntityDragonBase dragonBase, int chance, boolean checkSight, boolean onlyNearby) {
        super(dragonBase, checkSight, onlyNearby);
        isIce = !dragonBase.isFire;
        this.targetChance = chance;
        this.theNearestAttackableTargetSorter = new Sorter(dragonBase);
        this.setMutexBits(1);
        this.targetEntitySelector = (Predicate<EntityItem>) item -> item != null && FoodUtils.getFoodPoints(item.getItem(), true, isIce) > 0;
    }

    @Override
    public boolean shouldExecute() {
        if (((EntityDragonBase) this.taskOwner).getHunger() >= 100) {
            return false;
        }
        if (!((EntityDragonBase) this.taskOwner).canMove()) {
            return false;
        }

        if (this.targetChance > 0 && this.taskOwner.getRNG().nextInt(targetChance) != 0) {
            return false;
        } else {
			List<EntityItem> list = this.taskOwner.world.getEntitiesWithinAABB(EntityItem.class, this.getTargetableArea(this.getTargetDistance()), this.targetEntitySelector);

            if (list.isEmpty()) {
                return false;
            } else {
                list.sort(this.theNearestAttackableTargetSorter);
                this.targetEntity = list.get(0);
                return true;
            }
        }
    }

    protected AxisAlignedBB getTargetableArea(double targetDistance) {
        return this.taskOwner.getEntityBoundingBox().expand(targetDistance, 4.0D, targetDistance);
    }

    @Override
    public void startExecuting() {
        this.taskOwner.getNavigator().tryMoveToXYZ(this.targetEntity.posX, this.targetEntity.posY, this.targetEntity.posZ, 1);
        super.startExecuting();
    }

    @Override
    public void updateTask() {
        super.updateTask();
        if (this.targetEntity == null || this.targetEntity.isDead) {
            this.resetTask();
            return;
        }
        if (this.taskOwner.getDistanceSq(this.targetEntity) < 1) {
            this.targetEntity.getItem().shrink(1);
            this.taskOwner.playSound(SoundEvents.ENTITY_GENERIC_EAT, 1, 1);
            int hunger = FoodUtils.getFoodPoints(this.targetEntity.getItem(), true, isIce);
            ((EntityDragonBase) this.taskOwner).setHunger(Math.min(100, ((EntityDragonBase) this.taskOwner).getHunger() + hunger));
            this.taskOwner.setHealth(Math.min(this.taskOwner.getMaxHealth(), (int) (this.taskOwner.getHealth() + FoodUtils.getFoodPoints(this.targetEntity.getItem(), true, isIce))));
            if (EntityDragonBase.ANIMATION_EAT != null) {
                ((EntityDragonBase) this.taskOwner).setAnimation(EntityDragonBase.ANIMATION_EAT);
            }
            for (int i = 0; i < 4; i++) {
                ((EntityDragonBase) this.taskOwner).spawnItemCrackParticles(this.targetEntity.getItem().getItem());
            }
            resetTask();
        }
    }

    @Override
    public boolean shouldContinueExecuting() {
        return !this.taskOwner.getNavigator().noPath();
    }

    public static class Sorter implements Comparator<Entity> {
        private final Entity theEntity;

        public Sorter(Entity theEntityIn) {
            this.theEntity = theEntityIn;
        }

        public int compare(Entity p_compare_1_, Entity p_compare_2_) {
            double d0 = this.theEntity.getDistanceSq(p_compare_1_);
            double d1 = this.theEntity.getDistanceSq(p_compare_2_);
            return Double.compare(d0, d1);
        }
    }
}