package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityHippogryph;
import com.github.alexthe666.iceandfire.util.IAFMath;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.TargetGoal;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public class HippogryphAITargetItems<T extends ItemEntity> extends TargetGoal {
    protected final DragonAITargetItems.Sorter theNearestAttackableTargetSorter;
    protected final Predicate<? super ItemEntity> targetEntitySelector;
    protected ItemEntity targetEntity;
    protected final int targetChance;
    @Nonnull
    private List<ItemEntity> list = IAFMath.emptyItemEntityList;

    public HippogryphAITargetItems(MobEntity creature, boolean checkSight) {
        this(creature, checkSight, false);
    }

    public HippogryphAITargetItems(MobEntity creature, boolean checkSight, boolean onlyNearby) {
        this(creature, 20, checkSight, onlyNearby, null);
    }

    public HippogryphAITargetItems(MobEntity creature, int chance, boolean checkSight, boolean onlyNearby, @Nullable final Predicate<? super T> targetSelector) {
        super(creature, checkSight, onlyNearby);
        this.theNearestAttackableTargetSorter = new DragonAITargetItems.Sorter(creature);
        this.targetChance = chance;
        this.targetEntitySelector = new Predicate<ItemEntity>() {
            @Override
            public boolean test(ItemEntity item) {
                return item != null && !item.getItem().isEmpty() && item.getItem().getItem() == Items.RABBIT_FOOT;
            }
        };
    }

    @Override
    public boolean shouldExecute() {
        if (this.targetChance > 0 && this.goalOwner.getRNG().nextInt(this.targetChance) != 0) {
            return false;
        }
        if (!((EntityHippogryph) this.goalOwner).canMove()) {
            list = IAFMath.emptyItemEntityList;
            return false;
        }

        if (this.goalOwner.world.getGameTime() % 4 == 0) // only update the list every 4 ticks
            list = this.goalOwner.world.getEntitiesWithinAABB(ItemEntity.class, this.getTargetableArea(this.getTargetDistance()), this.targetEntitySelector);

        if (list.isEmpty()) {
            return false;
        } else {
            list.sort(this.theNearestAttackableTargetSorter);
            this.targetEntity = list.get(0);
            return true;
        }
    }

    protected AxisAlignedBB getTargetableArea(double targetDistance) {
        return this.goalOwner.getBoundingBox().grow(targetDistance, 4.0D, targetDistance);
    }

    @Override
    public void startExecuting() {
        this.goalOwner.getNavigator().tryMoveToXYZ(this.targetEntity.getPosX(), this.targetEntity.getPosY(), this.targetEntity.getPosZ(), 1);
        super.startExecuting();
    }

    @Override
    public void tick() {
        super.tick();
        if (this.targetEntity == null || !this.targetEntity.isAlive()) {
            this.resetTask();
        } else if (this.getAttackReachSqr(targetEntity) >= this.goalOwner.getDistanceSq(targetEntity) ){
            EntityHippogryph hippo = (EntityHippogryph) this.goalOwner;
            this.targetEntity.getItem().shrink(1);
            this.goalOwner.playSound(SoundEvents.ENTITY_GENERIC_EAT, 1, 1);
            hippo.setAnimation(EntityHippogryph.ANIMATION_EAT);
            hippo.feedings++;
            hippo.heal(4);
            if (hippo.feedings > 3 && (hippo.feedings > 7 || hippo.getRNG().nextInt(3) == 0) && !hippo.isTamed() && this.targetEntity.getThrowerId() != null && this.goalOwner.world.getPlayerByUuid(this.targetEntity.getThrowerId()) != null) {
                PlayerEntity owner = this.goalOwner.world.getPlayerByUuid(this.targetEntity.getThrowerId());
                if (owner != null) {
                    hippo.setTamedBy(owner);
                    hippo.setAttackTarget(null);
                    hippo.setCommand(1);
                    hippo.setSitting(true);
                }
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
            final double d0 = this.theEntity.getDistanceSq(p_compare_1_);
            final double d1 = this.theEntity.getDistanceSq(p_compare_2_);
            return Double.compare(d0, d1);
        }
    }

    protected double getAttackReachSqr(Entity attackTarget) {
        return this.goalOwner.getWidth() * 2.0F * this.goalOwner.getWidth() * 2.0F + attackTarget.getWidth();
    }
}