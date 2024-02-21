package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.datagen.tags.IafItemTags;
import com.github.alexthe666.iceandfire.entity.EntityPixie;
import com.github.alexthe666.iceandfire.entity.util.EntityUtil;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import com.github.alexthe666.iceandfire.util.IAFMath;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;

public class PixieAIPickupItem extends TargetGoal {
    protected final Predicate<? super ItemEntity> targetEntitySelector;
    protected @Nullable ItemEntity targetEntity;
    private List<ItemEntity> list = IAFMath.emptyItemEntityList;

    public PixieAIPickupItem(EntityPixie creature, boolean checkSight) {
        this(creature, checkSight, false);
    }

    public PixieAIPickupItem(EntityPixie creature, boolean checkSight, boolean onlyNearby) {
        super(creature, checkSight, onlyNearby);
        this.targetEntitySelector = (Predicate<ItemEntity>) item -> checkTamed(creature, item) || checkUntamed(creature, item);
        this.setFlags(EnumSet.of(Flag.TARGET));
    }

    private boolean checkUntamed(final EntityPixie pixie, final ItemEntity itemEntity) {
        if (itemEntity == null || pixie.isTame()) {
            return false;
        }

        return itemEntity.getItem().is(IafItemTags.TAME_PIXIE);
    }

    private boolean checkTamed(final EntityPixie pixie, final ItemEntity itemEntity) {
        if (itemEntity == null || pixie.getHealth() >= pixie.getMaxHealth() || !pixie.isTame()) {
            return false;
        }

        return itemEntity.getItem().is(IafItemTags.HEAL_PIXIE);
    }

    @Override
    public boolean canUse() {
        if (((EntityPixie) mob).isPixieSitting()) {
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
        return this.mob.getBoundingBox().inflate(targetDistance, 4.0, targetDistance);
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

        this.mob.getMoveControl().setWantedPosition(this.targetEntity.getX(), this.targetEntity.getY(), this.targetEntity.getZ(), 0.25D);
        LivingEntity attackTarget = this.mob.getTarget();

        if (attackTarget == null) {
            this.mob.getLookControl().setLookAt(this.targetEntity.getX(), this.targetEntity.getY(), this.targetEntity.getZ(), 180.0F, 20.0F);
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (this.targetEntity == null || !this.targetEntity.isAlive()) {
            updateList(false);
            updateWantedPosition();
        } else if (this.mob.distanceToSqr(this.targetEntity) < 1) {
            EntityPixie pixie = (EntityPixie) this.mob;

            if (this.targetEntity.getItem().is(IafItemTags.HEAL_PIXIE)) {
                pixie.heal(5);
            } else if (this.targetEntity.getItem().is(IafItemTags.TAME_PIXIE)) {
                if (!pixie.isTame() && this.targetEntity.getThrowingEntity() instanceof Player player) {
                    pixie.tame(player);
                    pixie.setPixieSitting(true);
                    pixie.setOnGround(true);
                }
            }

            pixie.setItemInHand(InteractionHand.MAIN_HAND, this.targetEntity.getItem());
            this.targetEntity.getItem().shrink(1);
            pixie.playSound(IafSoundRegistry.PIXIE_TAUNT, 1F, 1F);
            stop();
        } else if (!mob.getMoveControl().hasWanted()) {
            updateList(false);
            updateWantedPosition();
        }
    }

    @Override
    public boolean canContinueToUse() {
        return true;
    }

    @Override
    public void stop() {
        super.stop();
        targetEntity = null;
        list = Collections.emptyList();
    }
}