package com.github.alexthe666.iceandfire.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.PathNavigateGround;

public class PixieAITempt extends EntityAIBase
{
    private final EntityCreature temptedEntity;
    private final double speed;
    private double targetX;
    private double targetY;
    private double targetZ;
    private double pitch;
    private double yaw;
    private EntityPlayer temptingPlayer;
    private int delayTemptCounter;
    private boolean isRunning;

    public PixieAITempt(EntityCreature temptedEntityIn, double speedIn) {
        this.temptedEntity = temptedEntityIn;
        this.speed = speedIn;
        this.setMutexBits(3);
        if (!(temptedEntityIn.getNavigator() instanceof PathNavigateGround)) {
            throw new IllegalArgumentException("Unsupported mob type for TemptGoal");
        }
    }

    public boolean shouldExecute() {
        if (this.delayTemptCounter > 0) {
            --this.delayTemptCounter;
            return false;
        }
        else {
            this.temptingPlayer = this.temptedEntity.world.getClosestPlayerToEntity(this.temptedEntity, 10.0D);
            return this.temptingPlayer == null ? false : !this.temptingPlayer.inventory.isEmpty();
        }
    }

    public boolean shouldContinueExecuting() {
        return this.shouldExecute();
    }

    public void startExecuting() {
        this.targetX = this.temptingPlayer.posX;
        this.targetY = this.temptingPlayer.posY;
        this.targetZ = this.temptingPlayer.posZ;
        this.isRunning = true;
    }

    public void resetTask() {
        this.temptingPlayer = null;
        this.temptedEntity.getNavigator().clearPathEntity();
        this.delayTemptCounter = 100;
        this.isRunning = false;
    }

    public void updateTask() {
        this.temptedEntity.getLookHelper().setLookPositionWithEntity(this.temptingPlayer, (float)(this.temptedEntity.getHorizontalFaceSpeed() + 20), (float)this.temptedEntity.getVerticalFaceSpeed());

        if (this.temptedEntity.getDistanceSqToEntity(this.temptingPlayer) < 6.25D) {
            this.temptedEntity.getNavigator().clearPathEntity();
        }
        else {
            this.temptedEntity.getNavigator().tryMoveToEntityLiving(this.temptingPlayer, this.speed);
        }
    }

    public boolean isRunning() {
        return this.isRunning;
    }
}