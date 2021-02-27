package com.github.alexthe666.iceandfire.entity.ai;

import java.util.EnumSet;

import com.github.alexthe666.iceandfire.entity.EntityMyrmexBase;
import com.github.alexthe666.iceandfire.entity.util.MyrmexHive;
import com.github.alexthe666.iceandfire.pathfinding.raycoms.AdvancedPathNavigate;
import com.github.alexthe666.iceandfire.pathfinding.raycoms.PathResult;
import com.github.alexthe666.iceandfire.world.MyrmexWorldData;

import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.BlockPos;

public class MyrmexAIReEnterHive extends Goal {
    private final EntityMyrmexBase myrmex;
    private final double movementSpeed;
    private PathResult path;
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
            village = MyrmexWorldData.get(this.myrmex.world).getNearestHive(this.myrmex.getPosition(), 500);
        }
        if (!(this.myrmex.getNavigator() instanceof AdvancedPathNavigate) ||this.myrmex.isPassenger()){
            return false;
        }
        if (village == null) {
            return false;
        } else {
            this.hive = village;
            nextEntrance = MyrmexHive.getGroundedPos(this.myrmex.world, hive.getClosestEntranceToEntity(this.myrmex, this.myrmex.getRNG(), false));
            this.path = ((AdvancedPathNavigate)this.myrmex.getNavigator()).moveToXYZ(nextEntrance.getX(), nextEntrance.getY(),  nextEntrance.getZ(), 1);
            first = true;
            return this.path != null;
        }
    }

    public void tick() {
        if (first) {
            hive.setWorld(this.myrmex.world);
            nextEntrance = MyrmexHive.getGroundedPos(this.myrmex.world, hive.getClosestEntranceToEntity(this.myrmex, this.myrmex.getRNG(), false));
        }
        ((AdvancedPathNavigate)this.myrmex.getNavigator()).moveToXYZ(nextEntrance.getX(), nextEntrance.getY(),  nextEntrance.getZ(), movementSpeed);
        if (this.myrmex.getDistanceSq(nextEntrance.getX() + 0.5D, nextEntrance.getY() + 0.5D, nextEntrance.getZ() + 0.5D) < 9 && first) {
            if (hive != null) {
                nextEntrance = hive.getClosestEntranceBottomToEntity(this.myrmex, this.myrmex.getRNG());
                first = false;
                this.path = ((AdvancedPathNavigate)this.myrmex.getNavigator()).moveToXYZ(nextEntrance.getX(), nextEntrance.getY(),  nextEntrance.getZ(), 1);
            }
        }
        this.myrmex.isEnteringHive = this.myrmex.getDistanceSq(nextEntrance.getX() + 0.5D, nextEntrance.getY() + 0.5D, nextEntrance.getZ() + 0.5D) > 14 && !first;
    }

    public boolean shouldContinueExecuting() {
        if (this.myrmex.getDistanceSq(nextEntrance.getX() + 0.5D, nextEntrance.getY() + 0.5D, nextEntrance.getZ() + 0.5D) < 15 && !first) {
            return false;
        }
        return true;
    }

    public void resetTask() {
        nextEntrance = BlockPos.ZERO;
        first = true;
    }
}