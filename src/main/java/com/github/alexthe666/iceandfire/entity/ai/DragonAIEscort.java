package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class DragonAIEscort extends Goal {
    private final EntityDragonBase dragon;
    private BlockPos previousPosition;
    private final float maxRange = 2000F;

    public DragonAIEscort(EntityDragonBase entityIn, double movementSpeedIn) {
        this.dragon = entityIn;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        return this.dragon.canMove() && this.dragon.getTarget() == null && this.dragon.getOwner() != null && this.dragon.getCommand() == 2;
    }

    @Override
    public void tick() {
        if (this.dragon.getOwner() != null) {
            final float dist = this.dragon.distanceTo(this.dragon.getOwner());
            if (dist > maxRange) {
                return;
            }
            if (dist > this.dragon.getBoundingBox().getSize() && (!this.dragon.isFlying() && !this.dragon.isHovering() || !dragon.isAllowedToTriggerFlight())) {
                if (previousPosition == null || previousPosition.distSqr(this.dragon.getOwner().blockPosition()) > 9) {
                    this.dragon.getNavigation().moveTo(this.dragon.getOwner(), 1F);
                    previousPosition = this.dragon.getOwner().blockPosition();
                }
            }
            if ((dist > 30F || this.dragon.getOwner().getY() - this.dragon.getY() > 8) && !this.dragon.isFlying() && !this.dragon.isHovering() && dragon.isAllowedToTriggerFlight()) {
                this.dragon.setHovering(true);
                this.dragon.setInSittingPose(false);
                this.dragon.setOrderedToSit(false);
                this.dragon.flyTicks = 0;
            }
        }

    }

    @Override
    public boolean canContinueToUse() {
        return this.dragon.getCommand() == 2 && this.dragon.canMove() && this.dragon.getTarget() == null && this.dragon.getOwner() != null && this.dragon.getOwner().isAlive() && (this.dragon.distanceTo(this.dragon.getOwner()) > 15 || !this.dragon.getNavigation().isDone());
    }

}
