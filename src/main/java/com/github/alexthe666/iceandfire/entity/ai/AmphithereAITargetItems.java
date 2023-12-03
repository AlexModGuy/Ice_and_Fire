package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.datagen.tags.IafItemTags;
import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import com.github.alexthe666.iceandfire.util.IAFMath;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.phys.AABB;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;

public class AmphithereAITargetItems<T extends ItemEntity> extends TargetGoal {
    protected final DragonAITargetItems.Sorter theNearestAttackableTargetSorter;
    protected final Predicate<? super ItemEntity> targetEntitySelector;
    protected ItemEntity targetEntity;
    protected final int targetChance;
    @Nonnull
    private List<ItemEntity> list = IAFMath.emptyItemEntityList;

    public AmphithereAITargetItems(Mob creature, boolean checkSight) {
        this(creature, checkSight, false);
    }

    public AmphithereAITargetItems(Mob creature, boolean checkSight, boolean onlyNearby) {
        this(creature, 20, checkSight, onlyNearby, null);
    }

    public AmphithereAITargetItems(Mob creature, int chance, boolean checkSight, boolean onlyNearby, @Nullable final Predicate<? super T> targetSelector) {
        super(creature, checkSight, onlyNearby);
        this.theNearestAttackableTargetSorter = new DragonAITargetItems.Sorter(creature);
        this.targetChance = chance;
        this.targetEntitySelector = (Predicate<ItemEntity>) item -> item != null && !item.getItem().isEmpty() && item.getItem().is(IafItemTags.HEAL_AMPITHERE);
        this.setFlags(EnumSet.of(Flag.TARGET));
    }

    @Override
    public boolean canUse() {
        if (this.targetChance > 0 && this.mob.getRandom().nextInt(this.targetChance) != 0) {
            return false;
        }
        if (!((EntityAmphithere) this.mob).canMove()) {
            list = IAFMath.emptyItemEntityList;
            return false;
        }

        // If the target entity already is what we want skip AABB
        if (targetEntitySelector.test(this.targetEntity)) {
            return true;
        }

        if (this.mob.level().getGameTime() % 4 == 0) // only update the list every 4 ticks
            list = this.mob.level().getEntitiesOfClass(ItemEntity.class, this.getTargetableArea(this.getFollowDistance()), this.targetEntitySelector);

        if (list.isEmpty())
            return false;

        list.sort(this.theNearestAttackableTargetSorter);
        this.targetEntity = list.get(0);
        return true;
    }

    protected AABB getTargetableArea(double targetDistance) {
        return this.mob.getBoundingBox().inflate(targetDistance, 4.0D, targetDistance);
    }

    @Override
    public void start() {
        this.mob.getNavigation().moveTo(this.targetEntity.getX(), this.targetEntity.getY(), this.targetEntity.getZ(), 1);
        super.start();
    }

    @Override
    public void stop() {
        this.targetEntity = null;
        super.stop();
    }

    @Override
    public void tick() {
        super.tick();
        if (this.targetEntity == null || !this.targetEntity.isAlive()) {
            this.stop();
        }
        if (this.targetEntity != null && this.targetEntity.isAlive() && this.mob.distanceToSqr(this.targetEntity) < 1) {
            EntityAmphithere hippo = (EntityAmphithere) this.mob;
            this.targetEntity.getItem().shrink(1);
            this.mob.playSound(SoundEvents.GENERIC_EAT, 1, 1);
            hippo.heal(5);
            stop();
        }
    }

    @Override
    public boolean canContinueToUse() {
        return !this.mob.getNavigation().isDone();
    }

    public static class Sorter implements Comparator<Entity> {
        private final Entity theEntity;

        public Sorter(Entity theEntityIn) {
            this.theEntity = theEntityIn;
        }

        @Override
        public int compare(Entity p_compare_1_, Entity p_compare_2_) {
            final double d0 = this.theEntity.distanceToSqr(p_compare_1_);
            final double d1 = this.theEntity.distanceToSqr(p_compare_2_);
            return Double.compare(d0, d1);
        }
    }
}