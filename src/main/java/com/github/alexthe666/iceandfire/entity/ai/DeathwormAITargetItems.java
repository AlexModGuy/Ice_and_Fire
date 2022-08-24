package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityDeathWorm;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.goal.TargetGoal;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;

public class DeathwormAITargetItems<T extends ItemEntity> extends TargetGoal {

    protected final DragonAITargetItems.Sorter theNearestAttackableTargetSorter;
    protected final Predicate<? super ItemEntity> targetEntitySelector;
    protected ItemEntity targetEntity;
    private final EntityDeathWorm worm;

    public DeathwormAITargetItems(EntityDeathWorm creature, boolean checkSight) {
        this(creature, checkSight, false);
    }

    public DeathwormAITargetItems(EntityDeathWorm creature, boolean checkSight, boolean onlyNearby) {
        this(creature, 0, checkSight, onlyNearby, null);
    }

    public DeathwormAITargetItems(EntityDeathWorm creature, int chance, boolean checkSight, boolean onlyNearby,
        @Nullable final Predicate<? super T> targetSelector) {
        super(creature, checkSight, onlyNearby);
        this.worm = creature;
        this.theNearestAttackableTargetSorter = new DragonAITargetItems.Sorter(creature);
        this.targetEntitySelector = new Predicate<ItemEntity>() {

            @Override
            public boolean test(ItemEntity item) {
                return item != null && !item.getItem().isEmpty() && item.getItem().getItem() == Blocks.TNT.asItem() &&
                    item.level.getBlockState(item.blockPosition().below()).getMaterial() == Material.SAND;
            }
        };
        this.setFlags(EnumSet.of(Flag.TARGET));

    }

    @Override
    public boolean canUse() {
        List<ItemEntity> list = this.mob.level.getEntitiesOfClass(ItemEntity.class,
            this.getTargetableArea(this.getFollowDistance()), this.targetEntitySelector);
        if (list.isEmpty()) {
            return false;
        } else {
            list.sort(this.theNearestAttackableTargetSorter);
            this.targetEntity = list.get(0);
            return true;
        }
    }

    protected AxisAlignedBB getTargetableArea(double targetDistance) {
        return this.mob.getBoundingBox().inflate(targetDistance, 4.0D, targetDistance);
    }

    @Override
    public void start() {
        this.mob.getNavigation().moveTo(this.targetEntity.getX(), this.targetEntity.getY(),
            this.targetEntity.getZ(), 1);
        super.start();
    }

    public boolean canContinueToUse() {
        Entity itemTarget = targetEntity;

        if (itemTarget == null) {
            return false;
        } else if (!itemTarget.isAlive()) {
            return false;
        } else {
            Team team = this.mob.getTeam();
            Team team1 = itemTarget.getTeam();
            if (team != null && team1 == team) {
                return false;
            } else {
                double d0 = this.getFollowDistance();
                return !(this.mob.distanceToSqr(itemTarget) > d0 * d0);
            }
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.targetEntity == null || !this.targetEntity.isAlive()) {
            this.stop();
        } else if (this.mob.distanceToSqr(this.targetEntity) < 1) {
            EntityDeathWorm deathWorm = (EntityDeathWorm) this.mob;
            this.targetEntity.getItem().shrink(1);
            this.mob.playSound(SoundEvents.GENERIC_EAT, 1, 1);
            deathWorm.setAnimation(EntityDeathWorm.ANIMATION_BITE);
            PlayerEntity thrower = null;
            if (this.targetEntity.getThrower() != null)
                thrower = this.targetEntity.level.getPlayerByUUID(this.targetEntity.getThrower());
            deathWorm.setExplosive(true, thrower);
            stop();
        }

        if (worm.getNavigation().isDone()) {
            worm.getNavigation().moveTo(targetEntity, 1.0F);
        }

    }


}
