package com.github.alexthe666.iceandfire.entity.ai;

import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.EnumSet;

public class AquaticAITempt extends Goal {
    private final Mob temptedEntity;
    private final double speed;
    private final boolean scaredByPlayerMovement;
    private final TagKey<Item> temptItems;
    private double targetX;
    private double targetY;
    private double targetZ;
    private double pitch;
    private double yaw;
    private Player temptingPlayer;
    private int delayTemptCounter;
    private boolean isRunning;

    public AquaticAITempt(Mob temptedEntityIn, double speedIn, boolean scaredByPlayerMovementIn, final TagKey<Item> temptItems) {
        this.temptedEntity = temptedEntityIn;
        this.speed = speedIn;
        this.temptItems = temptItems;
        this.scaredByPlayerMovement = scaredByPlayerMovementIn;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    /**
     * Returns whether the Goal should begin execution.
     */
    @Override
    public boolean canUse() {
        if (this.delayTemptCounter > 0) {
            --this.delayTemptCounter;
            return false;
        } else {
            this.temptingPlayer = this.temptedEntity.level().getNearestPlayer(this.temptedEntity, 10.0D);

            if (this.temptingPlayer == null) {
                return false;
            } else {
                return this.isTempting(this.temptingPlayer.getMainHandItem()) || this.isTempting(this.temptingPlayer.getOffhandItem());
            }
        }
    }

    protected boolean isTempting(final ItemStack stack) {
        return stack.is(temptItems);
    }

    /**
     * Returns whether an in-progress Goal should continue executing
     */
    @Override
    public boolean canContinueToUse() {
        if (this.scaredByPlayerMovement) {
            if (this.temptedEntity.distanceToSqr(this.temptingPlayer) < 36.0D) {
                if (this.temptingPlayer.distanceToSqr(this.targetX, this.targetY, this.targetZ) > 0.010000000000000002D) {
                    return false;
                }

                if (Math.abs(this.temptingPlayer.getXRot() - this.pitch) > 5.0D
                    || Math.abs(this.temptingPlayer.getYRot() - this.yaw) > 5.0D) {
                    return false;
                }
            } else {
                this.targetX = this.temptingPlayer.getX();
                this.targetY = this.temptingPlayer.getY();
                this.targetZ = this.temptingPlayer.getZ();
            }

            this.pitch = this.temptingPlayer.getXRot();
            this.yaw = this.temptingPlayer.getYRot();
        }

        return this.canUse();
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    @Override
    public void start() {
        this.targetX = this.temptingPlayer.getX();
        this.targetY = this.temptingPlayer.getY();
        this.targetZ = this.temptingPlayer.getZ();
        this.isRunning = true;
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    @Override
    public void stop() {
        this.temptingPlayer = null;
        this.temptedEntity.getNavigation().stop();
        this.delayTemptCounter = 100;
        this.isRunning = false;
    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    @Override
    public void tick() {
        this.temptedEntity.getLookControl().setLookAt(this.temptingPlayer,
            this.temptedEntity.getMaxHeadYRot() + 20, this.temptedEntity.getMaxHeadXRot());

        if (this.temptedEntity.distanceToSqr(this.temptingPlayer) < 6.25D) {
            this.temptedEntity.getNavigation().stop();
        } else {
            this.temptedEntity.getNavigation().moveTo(this.temptingPlayer, this.speed);
        }
    }

    /**
     * @see #isRunning
     */
    public boolean isRunning() {
        return this.isRunning;
    }
}