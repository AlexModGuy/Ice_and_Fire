package com.github.alexthe666.iceandfire.entity.ai;

import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;

import com.github.alexthe666.iceandfire.api.FoodUtils;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.EntityIceDragon;

import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.TargetGoal;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;

public class DragonAITargetItems<T extends ItemEntity> extends TargetGoal {

    protected final DragonAITargetItems.Sorter theNearestAttackableTargetSorter;
    protected final Predicate<? super ItemEntity> targetEntitySelector;
    private final int targetChance;
    protected ItemEntity targetEntity;
    private boolean isIce = false;

    public DragonAITargetItems(MobEntity creature, boolean checkSight) {
        this(creature, checkSight, false);
        this.setMutexFlags(EnumSet.of(Flag.TARGET));
    }

    public DragonAITargetItems(MobEntity creature, boolean checkSight, boolean onlyNearby) {
        this(creature, 20, checkSight, onlyNearby);
        isIce = creature instanceof EntityIceDragon;
    }

    public DragonAITargetItems(MobEntity creature, int chance, boolean checkSight, boolean onlyNearby) {
        super(creature, checkSight, onlyNearby);
        isIce = creature instanceof EntityIceDragon;
        this.targetChance = chance;
        this.theNearestAttackableTargetSorter = new DragonAITargetItems.Sorter(creature);
        this.setMutexFlags(EnumSet.of(Flag.MOVE));
        this.targetEntitySelector = new Predicate<ItemEntity>() {

            @Override
            public boolean test(ItemEntity item) {
                return !item.getItem().isEmpty() && item.getItem().getItem() != null
                    && FoodUtils.getFoodPoints(item.getItem(), true, isIce) > 0;
            }
        };
    }

    @Override
    public boolean shouldExecute() {
        if (((EntityDragonBase) this.goalOwner).getHunger() >= 100) { return false; }
        if (!((EntityDragonBase) this.goalOwner).canMove()) { return false; }

        if (this.targetChance > 0 && this.goalOwner.getRNG().nextInt(10) != 0) {
            return false;
        } else {

            List<ItemEntity> list = this.goalOwner.world.getLoadedEntitiesWithinAABB(ItemEntity.class,
                this.getTargetableArea(this.getTargetDistance()), this.targetEntitySelector);

            if (list.isEmpty()) {
                return false;
            } else {
                Collections.sort(list, this.theNearestAttackableTargetSorter);
                this.targetEntity = list.get(0);
                return true;
            }
        }
    }

    protected AxisAlignedBB getTargetableArea(double targetDistance) {
        return this.goalOwner.getBoundingBox().grow(targetDistance, 4.0D, targetDistance);
    }

    @Override
    public void startExecuting() {
        this.goalOwner.getNavigator().tryMoveToXYZ(this.targetEntity.getPosX(), this.targetEntity.getPosY(),
            this.targetEntity.getPosZ(), 1);
        super.startExecuting();
    }

    @Override
    public void tick() {
        super.tick();
        if (this.targetEntity == null || this.targetEntity.isAlive()) {
            this.resetTask();
        } else if (this.goalOwner.getDistanceSq(this.targetEntity) < 1) {
            this.targetEntity.getItem().shrink(1);
            this.goalOwner.playSound(SoundEvents.ENTITY_GENERIC_EAT, 1, 1);
            int hunger = FoodUtils.getFoodPoints(this.targetEntity.getItem(), true, isIce);
            final EntityDragonBase dragon = ((EntityDragonBase) this.goalOwner);
            dragon.setHunger(Math.min(100, ((EntityDragonBase) this.goalOwner).getHunger() + hunger));
            dragon.eatFoodBonus(this.targetEntity.getItem());
            this.goalOwner.setHealth(Math.min(this.goalOwner.getMaxHealth(), (int) (this.goalOwner.getHealth()
                + FoodUtils.getFoodPoints(this.targetEntity.getItem(), true, isIce))));
            if (EntityDragonBase.ANIMATION_EAT != null) {
                dragon.setAnimation(EntityDragonBase.ANIMATION_EAT);
            }
            for (int i = 0; i < 4; i++) {
                dragon.spawnItemCrackParticles(this.targetEntity.getItem().getItem());
            }
            resetTask();
        }
    }

    @Override
    public boolean shouldContinueExecuting() {
        return !this.goalOwner.getNavigator().noPath();
    }

    public static class Sorter implements Comparator<Entity> {

        private final Entity theEntity;

        public Sorter(Entity theEntityIn) {
            this.theEntity = theEntityIn;
        }

        @Override
        public int compare(Entity p_compare_1_, Entity p_compare_2_) {
            double d0 = this.theEntity.getDistanceSq(p_compare_1_);
            double d1 = this.theEntity.getDistanceSq(p_compare_2_);
            return d0 < d1 ? -1 : (d0 > d1 ? 1 : 0);
        }
    }
}
