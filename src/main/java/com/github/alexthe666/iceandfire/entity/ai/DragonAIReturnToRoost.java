package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.util.DragonUtils;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class DragonAIReturnToRoost extends Goal {

    private final EntityDragonBase dragon;

    public DragonAIReturnToRoost(EntityDragonBase entityIn, double movementSpeedIn) {
        this.dragon = entityIn;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        return this.dragon.canMove() && this.dragon.lookingForRoostAIFlag
            && (dragon.getTarget() == null || !dragon.getTarget().isAlive())
            && dragon.getRestrictCenter() != null
            && DragonUtils.isInHomeDimension(dragon)
            && dragon.getDistanceSquared(Vec3.atCenterOf(dragon.getRestrictCenter())) > dragon.getBbWidth()
            * dragon.getBbWidth();
    }

    @Override
    public void tick() {
        if (this.dragon.getRestrictCenter() != null) {
            final double dist = Math.sqrt(dragon.getDistanceSquared(Vec3.atCenterOf(dragon.getRestrictCenter())));
            final double xDist = Math.abs(dragon.getX() - dragon.getRestrictCenter().getX() - 0.5F);
            final double zDist = Math.abs(dragon.getZ() - dragon.getRestrictCenter().getZ() - 0.5F);
            final double xzDist = Math.sqrt(xDist * xDist + zDist * zDist);

            if (dist < this.dragon.getBbWidth()) {
                this.dragon.setFlying(false);
                this.dragon.setHovering(false);
                this.dragon.getNavigation().moveTo(this.dragon.getRestrictCenter().getX(),
                    this.dragon.getRestrictCenter().getY(), this.dragon.getRestrictCenter().getZ(), 1.0F);
            } else {
                double yAddition = 15 + dragon.getRandom().nextInt(3);
                if (xzDist < 40) {
                    yAddition = 0;
                    if (this.dragon.onGround()) {
                        this.dragon.setFlying(false);
                        this.dragon.setHovering(false);
                        this.dragon.flightManager.setFlightTarget(
                            Vec3.upFromBottomCenterOf(this.dragon.getRestrictCenter(), yAddition));
                        this.dragon.getNavigation().moveTo(this.dragon.getRestrictCenter().getX(),
                            this.dragon.getRestrictCenter().getY(), this.dragon.getRestrictCenter().getZ(), 1.0F);
                        return;
                    }
                }
                if (!this.dragon.isFlying() && !this.dragon.isHovering() && xzDist > 40) {
                    this.dragon.setHovering(true);
                }
                if (this.dragon.isFlying()) {
                    this.dragon.flightManager.setFlightTarget(
                        Vec3.upFromBottomCenterOf(this.dragon.getRestrictCenter(), yAddition));
                    this.dragon.getNavigation().moveTo(this.dragon.getRestrictCenter().getX(),
                        yAddition + this.dragon.getRestrictCenter().getY(), this.dragon.getRestrictCenter().getZ(), 1F);
                }
                this.dragon.flyTicks = 0;
            }

        }

    }

    @Override
    public boolean canContinueToUse() {
        return canUse();
    }

}
