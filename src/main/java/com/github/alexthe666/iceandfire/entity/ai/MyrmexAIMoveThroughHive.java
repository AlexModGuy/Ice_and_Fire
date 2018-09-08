package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityMyrmexBase;
import com.github.alexthe666.iceandfire.entity.MyrmexHive;
import com.github.alexthe666.iceandfire.world.MyrmexWorldData;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.math.BlockPos;

public class MyrmexAIMoveThroughHive extends EntityAIBase {
    private final EntityMyrmexBase myrmex;
    private final double movementSpeed;
    private Path path;
    private BlockPos nextRoom = BlockPos.ORIGIN;

    public MyrmexAIMoveThroughHive(EntityMyrmexBase entityIn, double movementSpeedIn) {
        this.myrmex = entityIn;
        this.movementSpeed = movementSpeedIn;
        this.setMutexBits(1);
    }

    public boolean shouldExecute() {
        if(!this.myrmex.shouldEnterHive() || this.myrmex.canSeeSky()){
            return false;
        }
        MyrmexHive village = MyrmexWorldData.get(this.myrmex.world).getNearestVillage(new BlockPos(this.myrmex), 100);
        if (village == null) {
            return false;
        } else {
            nextRoom = MyrmexHive.getGroundedPos(this.myrmex.world, village.getRandomRoom(this.myrmex.getRNG()));
            this.path = this.myrmex.getNavigator().getPathToPos(nextRoom);
            return this.path != null;
        }
    }

    public boolean shouldContinueExecuting() {
        return !this.myrmex.getNavigator().noPath() && this.myrmex.getDistanceSq(nextRoom) > 3 && this.myrmex.shouldEnterHive();
    }

    public void startExecuting() {
        this.myrmex.getNavigator().setPath(this.path, this.movementSpeed);
    }

    public void resetTask() {
        nextRoom = BlockPos.ORIGIN;
        this.myrmex.getNavigator().setPath(null, this.movementSpeed);

    }
}