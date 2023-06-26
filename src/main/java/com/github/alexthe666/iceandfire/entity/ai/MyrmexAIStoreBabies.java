package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityMyrmexWorker;
import com.github.alexthe666.iceandfire.entity.util.MyrmexHive;
import com.github.alexthe666.iceandfire.world.gen.WorldGenMyrmexHive;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class MyrmexAIStoreBabies extends Goal {
    private final EntityMyrmexWorker myrmex;
    private BlockPos nextRoom = BlockPos.ZERO;

    public MyrmexAIStoreBabies(EntityMyrmexWorker entityIn, double movementSpeedIn) {
        this.myrmex = entityIn;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (!this.myrmex.canMove() || !this.myrmex.holdingBaby() || !this.myrmex.shouldEnterHive() && !this.myrmex.getNavigation().isDone() || this.myrmex.canSeeSky()) {
            return false;
        }
        MyrmexHive village = this.myrmex.getHive();
        if (village == null) {
            return false;
        } else {
            nextRoom = MyrmexHive.getGroundedPos(this.myrmex.level(), village.getRandomRoom(WorldGenMyrmexHive.RoomType.NURSERY, this.myrmex.getRandom(), this.myrmex.blockPosition())).above();
            return true;
        }
    }

    @Override
    public boolean canContinueToUse() {
        return this.myrmex.holdingBaby() && !this.myrmex.getNavigation().isDone() && this.myrmex.distanceToSqr(nextRoom.getX() + 0.5D, nextRoom.getY() + 0.5D, nextRoom.getZ() + 0.5D) > 3 && this.myrmex.shouldEnterHive();
    }

    @Override
    public void start() {
        this.myrmex.getNavigation().moveTo(this.nextRoom.getX(), this.nextRoom.getY(), this.nextRoom.getZ(), 1.5F);
    }

    @Override
    public void tick() {
        if (nextRoom != null && this.myrmex.distanceToSqr(nextRoom.getX() + 0.5D, nextRoom.getY() + 0.5D, nextRoom.getZ() + 0.5D) < 4 && this.myrmex.holdingBaby()) {
            if (!this.myrmex.getPassengers().isEmpty()) {
                for (Entity entity : this.myrmex.getPassengers()) {
                    entity.stopRiding();
                    stop();
                    entity.copyPosition(this.myrmex);
                }
            }
        }
    }

    @Override
    public void stop() {
        nextRoom = BlockPos.ZERO;
        this.myrmex.getNavigation().stop();
    }

}