package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityDeathWorm;
import com.github.alexthe666.iceandfire.util.IAFMath;
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
    protected final int targetChance;
    private final EntityDeathWorm worm;
    protected ItemEntity targetEntity;
    private List<ItemEntity> list = IAFMath.emptyItemEntityList;

    public DeathwormAITargetItems(EntityDeathWorm creature, boolean checkSight) {
        this(creature, checkSight, false);
    }

    public DeathwormAITargetItems(EntityDeathWorm creature, boolean checkSight, boolean onlyNearby) {
        this(creature, 10, checkSight, onlyNearby, null);
    }

    public DeathwormAITargetItems(EntityDeathWorm creature, int chance, boolean checkSight, boolean onlyNearby,
                                  @Nullable final Predicate<? super T> targetSelector) {
        super(creature, checkSight, onlyNearby);
        this.worm = creature;
        this.targetChance = chance;
        this.theNearestAttackableTargetSorter = new DragonAITargetItems.Sorter(creature);
        this.targetEntitySelector = new Predicate<ItemEntity>() {
            @Override
            public boolean test(ItemEntity item) {
                return item != null && !item.getItem().isEmpty() && item.getItem().getItem() == Blocks.TNT.asItem() &&
                    item.world.getBlockState(item.getPosition().down()).getMaterial() == Material.SAND;
            }
        };
        this.setMutexFlags(EnumSet.of(Flag.TARGET));

    }

    @Override
    public boolean shouldExecute() {
        if (this.targetChance > 0 && this.goalOwner.getRNG().nextInt(this.targetChance) != 0) {
            return false;
        }
        if (this.goalOwner.world.getGameTime() % 4 == 0)
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
        this.goalOwner.getNavigator().tryMoveToXYZ(this.targetEntity.getPosX(), this.targetEntity.getPosY(),
            this.targetEntity.getPosZ(), 1);
        super.startExecuting();
    }

    public boolean shouldContinueExecuting() {
        Entity itemTarget = targetEntity;

        if (itemTarget == null) {
            return false;
        } else if (!itemTarget.isAlive()) {
            return false;
        } else {
            Team team = this.goalOwner.getTeam();
            Team team1 = itemTarget.getTeam();
            if (team != null && team1 == team) {
                return false;
            } else {
                double d0 = this.getTargetDistance();
                return !(this.goalOwner.getDistanceSq(itemTarget) > d0 * d0);
            }
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.targetEntity == null || !this.targetEntity.isAlive()) {
            this.resetTask();
        } else if (this.goalOwner.getDistanceSq(this.targetEntity) < 1) {
            EntityDeathWorm deathWorm = (EntityDeathWorm) this.goalOwner;
            this.targetEntity.getItem().shrink(1);
            this.goalOwner.playSound(SoundEvents.ENTITY_GENERIC_EAT, 1, 1);
            deathWorm.setAnimation(EntityDeathWorm.ANIMATION_BITE);
            PlayerEntity thrower = null;
            if (this.targetEntity.getThrowerId() != null)
                thrower = this.targetEntity.world.getPlayerByUuid(this.targetEntity.getThrowerId());
            deathWorm.setExplosive(true, thrower);
            resetTask();
        }

        if (worm.getNavigator().noPath()) {
            worm.getNavigator().tryMoveToEntityLiving(targetEntity, 1.0F);
        }

    }


}
