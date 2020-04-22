package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import com.github.alexthe666.iceandfire.entity.EntityPixie;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;

import java.util.ArrayList;
import java.util.Random;

public class PixieAISteal extends EntityAIBase {
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

    public PixieAISteal(EntityPixie temptedEntityIn, double speedIn) {
        this.temptedEntity = temptedEntityIn;
        this.speed = speedIn;
        this.setMutexBits(3);
    }

    public boolean shouldExecute() {
        if (!IceAndFire.CONFIG.pixiesStealItems) {
            return false;
        }
        if (temptedEntity.getRNG().nextInt(3) == 0) {
            return false;
        }
        if (temptedEntity.isTamed()) {
            return false;
        }
        if (this.delayTemptCounter > 0) {
            --this.delayTemptCounter;
            return false;
        } else {
            this.temptingPlayer = this.temptedEntity.world.getClosestPlayerToEntity(this.temptedEntity, 10.0D);
            return this.temptingPlayer != null && (this.temptedEntity.getHeldItem(EnumHand.MAIN_HAND).isEmpty() && !this.temptingPlayer.inventory.isEmpty() && !this.temptingPlayer.isCreative());
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
        this.temptedEntity.getLookHelper().setLookPositionWithEntity(this.temptingPlayer, (float) (this.temptedEntity.getHorizontalFaceSpeed() + 20), (float) this.temptedEntity.getVerticalFaceSpeed());
        ArrayList<Integer> slotlist = new ArrayList<Integer>();
        if (this.temptedEntity.getDistanceSq(this.temptingPlayer) < 6.25D && !this.temptingPlayer.inventory.isEmpty()) {
            for (int i = 0; i < this.temptingPlayer.inventory.getSizeInventory(); i++) {
                if (this.temptingPlayer.inventory.getStackInSlot(i) != ItemStack.EMPTY) {
                    slotlist.add(i);
                }
            }
            int slot = slotlist.get(new Random().nextInt(slotlist.size()));
            ItemStack randomItem = this.temptingPlayer.inventory.getStackInSlot(slot);
            this.temptedEntity.setHeldItem(EnumHand.MAIN_HAND, randomItem);
            this.temptingPlayer.inventory.removeStackFromSlot(slot);
            this.temptedEntity.flipAI(true);
            this.temptedEntity.playSound(IafSoundRegistry.PIXIE_TAUNT, 1F, 1F);
            this.temptedEntity.getMoveHelper().action = EntityMoveHelper.Action.WAIT;
            if (temptingPlayer != null) {
                this.temptingPlayer.addPotionEffect(new PotionEffect(this.temptedEntity.negativePotions[this.temptedEntity.getColor()], 100));
            }

        } else {
            this.temptedEntity.getMoveHelper().setMoveTo(this.temptingPlayer.posX, this.temptingPlayer.posY + 1.5F, this.temptingPlayer.posZ, 1D);
        }
    }

    public boolean isRunning() {
        return this.isRunning;
    }
}