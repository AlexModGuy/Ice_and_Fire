package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityPixie;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.EnumHand;

import java.util.ArrayList;
import java.util.Random;

public class PixieAITempt extends EntityAIBase
{
    private final EntityPixie temptedEntity;
    private final double speed;
    private double targetX;
    private double targetY;
    private double targetZ;
    private double pitch;
    private double yaw;
    private EntityPlayer temptingPlayer;
    private int delayTemptCounter;
    private boolean isRunning;

    public PixieAITempt(EntityPixie temptedEntityIn, double speedIn) {
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
            return this.temptingPlayer == null ? false : this.temptedEntity.getHeldItem(EnumHand.MAIN_HAND).isEmpty() && !this.temptingPlayer.inventory.isEmpty();
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
        this.temptedEntity.getMoveHelper().action = EntityMoveHelper.Action.WAIT;
        this.delayTemptCounter = 10;
        this.isRunning = false;
    }

    public void updateTask() {
        this.temptedEntity.getLookHelper().setLookPositionWithEntity(this.temptingPlayer, (float)(this.temptedEntity.getHorizontalFaceSpeed() + 20), (float)this.temptedEntity.getVerticalFaceSpeed());
        ArrayList<Integer> slotlist = new ArrayList<Integer>();
        if (this.temptedEntity.getDistanceSqToEntity(this.temptingPlayer) < 6.25D && !this.temptingPlayer.inventory.isEmpty()) {
            for(int i = 0; i < this.temptingPlayer.inventory.getSizeInventory(); i++){
                if(this.temptingPlayer.inventory.getStackInSlot(i) != ItemStack.EMPTY){
                    slotlist.add(i);
                    System.out.println(this.temptingPlayer.inventory.getStackInSlot(i));
                }
            }
            int slot = slotlist.get(new Random().nextInt(slotlist.size()));
            ItemStack randomItem = this.temptingPlayer.inventory.getStackInSlot(slot);
            this.temptedEntity.setHeldItem(EnumHand.MAIN_HAND, randomItem);
            this.temptingPlayer.inventory.removeStackFromSlot(slot);
            this.temptedEntity.flipAI(true);
            this.temptedEntity.getMoveHelper().action = EntityMoveHelper.Action.WAIT;
        }
        else {
            this.temptedEntity.getMoveHelper().setMoveTo(this.temptingPlayer.posX, this.temptingPlayer.posY + 1.5F, this.temptingPlayer.posZ, 1D);
        }
    }

    public boolean isRunning() {
        return this.isRunning;
    }
}