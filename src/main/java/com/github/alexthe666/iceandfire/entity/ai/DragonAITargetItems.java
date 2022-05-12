package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.api.FoodUtils;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.EntityIceDragon;
import com.github.alexthe666.iceandfire.util.IAFMath;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.goal.TargetGoal;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;

import javax.annotation.Nonnull;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;

public class DragonAITargetItems<T extends ItemEntity> extends TargetGoal {

    protected final DragonAITargetItems.Sorter theNearestAttackableTargetSorter;
    protected final Predicate<? super ItemEntity> targetEntitySelector;
    private final int targetChance;
    protected ItemEntity targetEntity;
    private boolean isIce = false;

    @Nonnull
    private List<ItemEntity> list = IAFMath.emptyItemEntityList;

    public DragonAITargetItems(EntityDragonBase creature, boolean checkSight) {
        this(creature, checkSight, false);
        this.setMutexFlags(EnumSet.of(Flag.TARGET));
    }

    public DragonAITargetItems(EntityDragonBase creature, boolean checkSight, boolean onlyNearby) {
        this(creature, 20, checkSight, onlyNearby);
        isIce = creature instanceof EntityIceDragon;
    }

    public DragonAITargetItems(EntityDragonBase creature, int chance, boolean checkSight, boolean onlyNearby) {
        super(creature, checkSight, onlyNearby);
        isIce = creature instanceof EntityIceDragon;
        this.targetChance = chance;
        this.theNearestAttackableTargetSorter = new DragonAITargetItems.Sorter(creature);
        this.setMutexFlags(EnumSet.of(Flag.MOVE));
        this.targetEntitySelector = new Predicate<ItemEntity>() {

            @Override
            public boolean test(ItemEntity item) {
                return item != null && !item.getItem().isEmpty() && item.getItem().getItem() != null
                    && FoodUtils.getFoodPoints(item.getItem(), true, isIce) > 0;
            }
        };
    }

    @Override
    public boolean shouldExecute() {
        final EntityDragonBase dragon = (EntityDragonBase) this.goalOwner;
        if (dragon.getHunger() >= 100 || !dragon.canMove() || (this.targetChance > 0 && this.goalOwner.getRNG().nextInt(10) != 0)) {
            list = IAFMath.emptyItemEntityList;
            return false;
        } else {

            if (this.goalOwner.world.getGameTime() % 4 == 0) // only update the list every 4 ticks
                list = this.goalOwner.world.getLoadedEntitiesWithinAABB(ItemEntity.class,
                        this.getTargetableArea(this.getTargetDistance()), this.targetEntitySelector);

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
        if (this.targetEntity == null || !this.targetEntity.isAlive()) {
            this.resetTask();
        } else if (this.goalOwner.getDistanceSq(this.targetEntity) < this.goalOwner.getWidth() * 2 + this.goalOwner.getHeight() / 2 ||
            (this.goalOwner instanceof EntityDragonBase &&
                ((EntityDragonBase) this.goalOwner).getHeadPosition().squareDistanceTo(this.targetEntity.getPositionVec()) < this.goalOwner.getHeight())) {

            this.goalOwner.playSound(SoundEvents.ENTITY_GENERIC_EAT, 1, 1);
            final int hunger = FoodUtils.getFoodPoints(this.targetEntity.getItem(), true, isIce);
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
            this.targetEntity.getItem().shrink(1);
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
            final double d0 = this.theEntity.getDistanceSq(p_compare_1_);
            final double d1 = this.theEntity.getDistanceSq(p_compare_2_);
            return Double.compare(d0, d1);
        }
    }
}
