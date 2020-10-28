package com.github.alexthe666.iceandfire.entity.ai;

import java.util.EnumSet;

import com.github.alexthe666.iceandfire.entity.EntityMyrmexBase;
import com.github.alexthe666.iceandfire.entity.EntityMyrmexWorker;
import com.github.alexthe666.iceandfire.entity.util.MyrmexHive;
import com.github.alexthe666.iceandfire.world.MyrmexWorldData;

import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.math.BlockPos;

public class MyrmexAIMoveThroughHive extends Goal {
    private final EntityMyrmexBase myrmex;
    private final double movementSpeed;
    private Path path;
    private BlockPos nextRoom = BlockPos.ZERO;

    public MyrmexAIMoveThroughHive(EntityMyrmexBase entityIn, double movementSpeedIn) {
        this.myrmex = entityIn;
        this.movementSpeed = movementSpeedIn;
        this.setMutexFlags(EnumSet.of(Flag.MOVE));
    }

    public boolean shouldExecute() {
        if (!this.myrmex.canMove() || this.myrmex instanceof EntityMyrmexWorker && ((EntityMyrmexWorker) this.myrmex).holdingSomething() || !this.myrmex.shouldMoveThroughHive() || !this.myrmex.shouldEnterHive() && !this.myrmex.getNavigator().noPath() || this.myrmex.canSeeSky()) {
            return false;
        }
        MyrmexHive village = MyrmexWorldData.get(this.myrmex.world).getNearestHive(this.myrmex.func_233580_cy_(), 300);
        if (village == null) {
            village = this.myrmex.getHive();
        }
        if (village == null) {
            return false;
        } else {
            nextRoom = MyrmexHive.getGroundedPos(this.myrmex.world, village.getRandomRoom(this.myrmex.getRNG(), this.myrmex.func_233580_cy_()));
            this.path = this.myrmex.getNavigator().getPathToPos(nextRoom, 0);
            return this.path != null;
        }
    }

    public boolean shouldContinueExecuting() {
        return !this.myrmex.getNavigator().noPath() && this.myrmex.getDistanceSq(nextRoom.getX() + 0.5D, nextRoom.getY() + 0.5D, nextRoom.getZ() + 0.5D) > 3 && this.myrmex.shouldEnterHive() && !(this.myrmex instanceof EntityMyrmexWorker && ((EntityMyrmexWorker) this.myrmex).holdingBaby());
    }

    public void startExecuting() {
        this.myrmex.getNavigator().setPath(this.path, this.movementSpeed);
    }

    public void resetTask() {
        nextRoom = BlockPos.ZERO;
        this.myrmex.getNavigator().setPath(null, this.movementSpeed);

    }
}