package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityDreadQueen;
import com.github.alexthe666.iceandfire.entity.EntityBlackFrostDragon;
import net.minecraft.entity.ai.EntityAIBase;

import java.util.List;

public class DreadAIDragonFindQueen extends EntityAIBase {
    private final EntityBlackFrostDragon dragon;
    private EntityDreadQueen queen;

    public DreadAIDragonFindQueen(EntityBlackFrostDragon dragon) {
        this.dragon = dragon;
        this.setMutexBits(0);
    }

    public boolean shouldExecute() {
        if (this.dragon.isBeingRidden()) {
            return false;
        } else {
            List<EntityDreadQueen> list = this.dragon.world.getEntitiesWithinAABB(EntityDreadQueen.class, this.dragon.getEntityBoundingBox().grow(128.0D, 128.0D, 128.0D));

            if (list.isEmpty()) {
                return false;
            } else {
                for (EntityDreadQueen entityirongolem : list) {
                   if(!entityirongolem.isBeingRidden()){
                       this.queen = entityirongolem;
                       break;
                   }
                }

                return this.queen != null;
            }
        }
    }

    public boolean shouldContinueExecuting() {
        return this.queen != null && !this.queen.isRiding();
    }

    public void startExecuting() {
        this.queen.getNavigator().clearPath();
    }

    public void resetTask() {
        this.queen = null;
        this.dragon.getNavigator().clearPath();
    }

    public void updateTask() {
        this.dragon.getLookHelper().setLookPositionWithEntity(this.queen, 30.0F, 30.0F);
        if(this.dragon.isFlying() || this.dragon.isHovering()){
            dragon.getMoveHelper().setMoveTo(this.queen.posX, this.queen.posY + 1, this.queen.posZ, 1.2D);
        }else{
            this.dragon.getNavigator().tryMoveToEntityLiving(this.queen, 1.2D);
        }

        if (this.dragon.getDistanceSq(this.queen) < 0.66D * dragon.getRenderSize()) {
            this.dragon.getNavigator().clearPath();
            this.queen.startRiding(dragon);
        }
    }
}