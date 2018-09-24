package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityMyrmexWorker;
import com.github.alexthe666.iceandfire.entity.MyrmexHive;
import com.github.alexthe666.iceandfire.structures.WorldGenMyrmexHive;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.math.BlockPos;

public class MyrmexAIStoreBabies extends EntityAIBase {
    private final EntityMyrmexWorker myrmex;
    private final double movementSpeed;
    private Path path;
    private BlockPos nextRoom = BlockPos.ORIGIN;

    public MyrmexAIStoreBabies(EntityMyrmexWorker entityIn, double movementSpeedIn) {
        this.myrmex = entityIn;
        this.movementSpeed = movementSpeedIn;
        this.setMutexBits(1);
    }

    public boolean shouldExecute() {
        if (!this.myrmex.canMove() || !this.myrmex.holdingBaby() || !this.myrmex.shouldEnterHive() && !this.myrmex.getNavigator().noPath() || this.myrmex.canSeeSky()) {
            return false;
        }
        MyrmexHive village = this.myrmex.getHive();
        if (village == null) {
            return false;
        } else {
            nextRoom = MyrmexHive.getGroundedPos(this.myrmex.world, village.getRandomRoom(WorldGenMyrmexHive.RoomType.NURSERY, this.myrmex.getRNG(), this.myrmex.getPosition()));
            this.path = this.myrmex.getNavigator().getPathToPos(nextRoom);
            return this.path != null;
        }
    }

    public boolean shouldContinueExecuting() {
        return !this.myrmex.holdingBaby() && !this.myrmex.getNavigator().noPath() && this.myrmex.getDistanceSq(nextRoom) > 3 && this.myrmex.shouldEnterHive();
    }

    public void startExecuting() {
        this.myrmex.getNavigator().setPath(this.path, this.movementSpeed);
    }

    @Override
    public void updateTask() {
        if (nextRoom != null && this.myrmex.getDistanceSq(nextRoom) < 4 && this.myrmex.holdingBaby()) {
            if(!this.myrmex.getPassengers().isEmpty()){
                for(Entity entity : this.myrmex.getPassengers()){
                    entity.dismountRidingEntity();
                    entity.copyLocationAndAnglesFrom(this.myrmex);
                }
                resetTask();
            }
        }
    }

    public void resetTask() {
        nextRoom = BlockPos.ORIGIN;
        this.myrmex.getNavigator().setPath(null, this.movementSpeed);
    }

}