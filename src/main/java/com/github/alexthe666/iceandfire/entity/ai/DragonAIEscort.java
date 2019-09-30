package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.Path;

public class DragonAIEscort extends EntityAIBase {
    private final EntityDragonBase dragon;
    private final double movementSpeed;
    private Path path;

    public DragonAIEscort(EntityDragonBase entityIn, double movementSpeedIn) {
        this.dragon = entityIn;
        this.movementSpeed = movementSpeedIn;
        this.setMutexBits(0);
    }

    public boolean shouldExecute() {
        if (!this.dragon.canMove() || this.dragon.getAttackTarget() != null || this.dragon.getOwner() == null || this.dragon.getCommand() != 2) {
            return false;
        }
        return true;
    }

    public void updateTask() {
        if(this.dragon.getOwner() != null) {
            double dist = this.dragon.getDistance(this.dragon.getOwner());
            if(dist > 20 && !this.dragon.isFlying() && !this.dragon.isHovering()){
                this.dragon.getNavigator().tryMoveToEntityLiving(this.dragon.getOwner(), 1.5F);
            }
            System.out.println(!this.dragon.isFlying());
            if(dist > 45 && !this.dragon.isFlying() && !this.dragon.isHovering() && dragon.isAllowedToTriggerFlight()){
                this.dragon.setHovering(true);
                this.dragon.setSleeping(false);
                this.dragon.setSitting(false);
                this.dragon.flyTicks = 0;
            }
        }

    }

    public boolean shouldContinueExecuting() {
        return this.dragon.canMove() && this.dragon.getAttackTarget() == null && this.dragon.getOwner() != null && this.dragon.getOwner().isEntityAlive() && (this.dragon.getDistance(this.dragon.getOwner()) > 15 || !this.dragon.getNavigator().noPath());
    }

}
