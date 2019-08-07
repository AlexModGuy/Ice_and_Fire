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
        this.setMutexBits(1);
    }

    public boolean shouldExecute() {
        if (!this.dragon.canMove() || this.dragon.getAttackTarget() != null || this.dragon.getOwner() == null || this.dragon.getCommand() != 2) {
            return false;
        }
        return true;
    }

    public void updateTask() {
        if(this.dragon.getOwner() != null && (this.dragon.getDistance(this.dragon.getOwner()) > 30)) {
            this.dragon.getNavigator().tryMoveToEntityLiving(this.dragon.getOwner(), 1.5F);
            if(this.dragon.getDistance(this.dragon.getOwner()) > 60 && !this.dragon.isFlying() && !this.dragon.isHovering()){
                this.dragon.setHovering(true);
                this.dragon.flyTicks = 0;
            }
        }

    }

    public boolean shouldContinueExecuting() {
        return this.dragon.canMove() && this.dragon.getAttackTarget() == null && this.dragon.getOwner() != null && this.dragon.getOwner().isEntityAlive() && (this.dragon.getDistance(this.dragon.getOwner()) < 15 || !this.dragon.getNavigator().noPath());
    }

}
