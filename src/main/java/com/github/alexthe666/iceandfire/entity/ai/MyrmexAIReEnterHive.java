package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityMyrmexBase;
import com.github.alexthe666.iceandfire.entity.util.MyrmexHive;
import com.github.alexthe666.iceandfire.world.MyrmexWorldData;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.math.BlockPos;

import java.util.EnumSet;

public class MyrmexAIReEnterHive extends Goal {
    private final EntityMyrmexBase myrmex;
    private final double movementSpeed;
    private Path path;
    private BlockPos nextEntrance = BlockPos.ZERO;
    private boolean first = true;
    private MyrmexHive hive;

    public MyrmexAIReEnterHive(EntityMyrmexBase entityIn, double movementSpeedIn) {
        this.myrmex = entityIn;
        this.movementSpeed = movementSpeedIn;
        this.setMutexFlags(EnumSet.of(Flag.MOVE));
    }

    public boolean shouldExecute() {
        if (!this.myrmex.canMove() || this.myrmex.shouldLeaveHive() || !this.myrmex.shouldEnterHive() || !this.myrmex.canSeeSky() || !first) {
            return false;
        }
        MyrmexHive village = this.myrmex.getHive();
        if (village == null) {
            village = MyrmexWorldData.get(this.myrmex.world).getNearestHive(new BlockPos(this.myrmex), 500);
        }
        if (village == null) {
            return false;
        } else {
            this.hive = village;
            nextEntrance = MyrmexHive.getGroundedPos(this.myrmex.world, hive.getClosestEntranceToEntity(this.myrmex, this.myrmex.getRNG(), false));
            this.path = this.myrmex.getNavigator().getPathToPos(nextEntrance, 0);
            first = true;
            return this.path != null;
        }
    }

    public void tick() {
        if (first) {
            hive.setWorld(this.myrmex.world);
            nextEntrance = MyrmexHive.getGroundedPos(this.myrmex.world, hive.getClosestEntranceToEntity(this.myrmex, this.myrmex.getRNG(), false));
        }
        this.path = this.myrmex.getNavigator().getPathToPos(nextEntrance, 0);
        this.myrmex.getNavigator().setPath(this.path, this.movementSpeed);
        if (this.myrmex.getDistanceSq(nextEntrance.getX() + 0.5D, nextEntrance.getY() + 0.5D, nextEntrance.getZ() + 0.5D) < 9 && first) {
            if (hive != null) {
                nextEntrance = hive.getClosestEntranceBottomToEntity(this.myrmex, this.myrmex.getRNG());
                first = false;
                this.myrmex.getNavigator().clearPath();
                this.path = this.myrmex.getNavigator().getPathToPos(nextEntrance, 0);
                this.myrmex.getNavigator().setPath(this.path, this.movementSpeed);
            }
        }
        this.myrmex.isEnteringHive = !(this.myrmex.getDistanceSq(nextEntrance.getX() + 0.5D, nextEntrance.getY() + 0.5D, nextEntrance.getZ() + 0.5D) < 15) || first;
    }

    public boolean shouldContinueExecuting() {
        if (this.myrmex.getDistanceSq(nextEntrance.getX() + 0.5D, nextEntrance.getY() + 0.5D, nextEntrance.getZ() + 0.5D) < 15 && !first) {
            return false;
        }
        return this.myrmex.shouldEnterHive();
    }

    public void resetTask() {
        nextEntrance = BlockPos.ZERO;
        this.myrmex.getNavigator().setPath(null, this.movementSpeed);
        first = true;
    }
}