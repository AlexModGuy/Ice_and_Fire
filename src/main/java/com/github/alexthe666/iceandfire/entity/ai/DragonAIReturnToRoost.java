package com.github.alexthe666.iceandfire.entity.ai;

import java.util.EnumSet;

import com.github.alexthe666.iceandfire.entity.EntityDragonBase;

import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.vector.Vector3d;

public class DragonAIReturnToRoost extends Goal {

    private final EntityDragonBase dragon;

    public DragonAIReturnToRoost(EntityDragonBase entityIn, double movementSpeedIn) {
        this.dragon = entityIn;
        this.setMutexFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean shouldExecute() {
        return this.dragon.canMove() && this.dragon.lookingForRoostAIFlag
            && (dragon.getAttackTarget() == null || !dragon.getAttackTarget().isAlive())
            && dragon.getHomePosition() != null
            && dragon.getDistanceSquared(Vector3d.copyCentered(dragon.getHomePosition())) > dragon.getWidth()
                * dragon.getWidth();
    }

    @Override
    public void tick() {
        if (this.dragon.getHomePosition() != null) {
            final double dist = Math.sqrt(dragon.getDistanceSquared(Vector3d.copyCentered(dragon.getHomePosition())));
            final double xDist = Math.abs(dragon.getPosX() - dragon.getHomePosition().getX() - 0.5F);
            final double zDist = Math.abs(dragon.getPosZ() - dragon.getHomePosition().getZ() - 0.5F);
            final double xzDist = Math.sqrt(xDist * xDist + zDist * zDist);

            if (dist < this.dragon.getWidth()) {
                this.dragon.setFlying(false);
                this.dragon.setHovering(false);
                this.dragon.getNavigator().tryMoveToXYZ(this.dragon.getHomePosition().getX(),
                    this.dragon.getHomePosition().getY(), this.dragon.getHomePosition().getZ(), 1.0F);
            } else {
                double yAddition = 15 + dragon.getRNG().nextInt(3);
                if (xzDist < 40) {
                    yAddition = 0;
                    if (this.dragon.isOnGround()) {
                        this.dragon.setFlying(false);
                        this.dragon.setHovering(false);
                        this.dragon.flightManager.setFlightTarget(
                            Vector3d.copyCenteredWithVerticalOffset(this.dragon.getHomePosition(), yAddition));
                        this.dragon.getNavigator().tryMoveToXYZ(this.dragon.getHomePosition().getX(),
                            this.dragon.getHomePosition().getY(), this.dragon.getHomePosition().getZ(), 1.0F);
                        return;
                    }
                }
                if (!this.dragon.isFlying() && !this.dragon.isHovering() && xzDist > 40) {
                    this.dragon.setHovering(true);
                }
                if (this.dragon.isFlying()) {
                    this.dragon.flightManager.setFlightTarget(
                        Vector3d.copyCenteredWithVerticalOffset(this.dragon.getHomePosition(), yAddition));
                    this.dragon.getNavigator().tryMoveToXYZ(this.dragon.getHomePosition().getX(),
                        yAddition + this.dragon.getHomePosition().getY(), this.dragon.getHomePosition().getZ(), 1F);
                }
                this.dragon.flyTicks = 0;
            }

        }

    }

    @Override
    public boolean shouldContinueExecuting() {
        return shouldExecute();
    }

}
