package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.datagen.tags.IafItemTags;
import com.github.alexthe666.iceandfire.entity.EntityHippogryph;
import com.github.alexthe666.iceandfire.entity.util.EntityUtil;
import com.github.alexthe666.iceandfire.util.IAFMath;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public class HippogryphAITargetItems extends TargetGoal {
    protected final Predicate<? super ItemEntity> targetEntitySelector;
    protected @Nullable ItemEntity targetEntity;
    protected final int targetChance;
    private List<ItemEntity> list = IAFMath.emptyItemEntityList;

    public HippogryphAITargetItems(Mob creature, boolean checkSight) {
        this(creature, checkSight, false);
    }

    public HippogryphAITargetItems(Mob creature, boolean checkSight, boolean onlyNearby) {
        this(creature, 20, checkSight, onlyNearby);
    }

    public HippogryphAITargetItems(Mob creature, int chance, boolean checkSight, boolean onlyNearby) {
        super(creature, checkSight, onlyNearby);
        this.targetChance = chance;
        this.targetEntitySelector = (Predicate<ItemEntity>) item -> item != null && !item.getItem().isEmpty() && item.getItem().is(IafItemTags.TAME_HIPPOGRYPH);
    }

    @Override
    public boolean canUse() {
        if (this.targetChance > 0 && this.mob.getRandom().nextInt(this.targetChance) != 0) {
            return false;
        }

        if (!((EntityHippogryph) this.mob).canMove()) {
            list = IAFMath.emptyItemEntityList;
            return false;
        }

        updateList(true);
        return targetEntity != null;
    }

    private void updateList(boolean forceNew) {
        list = EntityUtil.updateList(mob, forceNew ? null : list, () -> this.mob.level.getEntitiesOfClass(ItemEntity.class, this.getTargetableArea(this.getFollowDistance()), this.targetEntitySelector));
        targetEntity = !list.isEmpty() ? list.get(0) : null;
    }

    protected AABB getTargetableArea(double targetDistance) {
        return this.mob.getBoundingBox().inflate(targetDistance, 4.0D, targetDistance);
    }

    @Override
    public void start() {
        updateWantedPosition();
        super.start();
    }

    private void updateWantedPosition() {
        if (targetEntity == null) {
            stop();
            return;
        }

        this.mob.getNavigation().moveTo(this.targetEntity.getX(), this.targetEntity.getY(), this.targetEntity.getZ(), 1);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.targetEntity == null || !this.targetEntity.isAlive()) {
            this.stop();
        } else if (this.getAttackReachSqr(targetEntity) >= this.mob.distanceToSqr(targetEntity)) {
            EntityHippogryph hippo = (EntityHippogryph) this.mob;
            this.targetEntity.getItem().shrink(1);
            this.mob.playSound(SoundEvents.GENERIC_EAT, 1, 1);
            hippo.setAnimation(EntityHippogryph.ANIMATION_EAT);
            hippo.feedings++;
            hippo.heal(4);

            if (hippo.feedings > 3 && (hippo.feedings > 7 || hippo.getRandom().nextInt(3) == 0) && !hippo.isTame() && this.targetEntity.getThrowingEntity() instanceof Player player) {
                    hippo.tame(player);
                    hippo.setTarget(null);
                    hippo.setCommand(1);
                    hippo.setOrderedToSit(true);
            }

            stop();
        } else if (!mob.getMoveControl().hasWanted()) {
            updateList(false);
            updateWantedPosition();
        }
    }

    @Override
    public boolean canContinueToUse() {
        return !this.mob.getNavigation().isDone();
    }

    protected double getAttackReachSqr(Entity attackTarget) {
        return this.mob.getBbWidth() * 2.0F * this.mob.getBbWidth() * 2.0F + attackTarget.getBbWidth();
    }

    @Override
    public void stop() {
        super.stop();
        targetEntity = null;
        list = Collections.emptyList();
    }
}