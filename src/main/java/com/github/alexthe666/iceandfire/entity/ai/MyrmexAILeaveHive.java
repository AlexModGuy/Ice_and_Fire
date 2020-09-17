package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityMyrmexBase;
import com.github.alexthe666.iceandfire.entity.EntityMyrmexQueen;
import com.github.alexthe666.iceandfire.entity.EntityMyrmexWorker;
import com.github.alexthe666.iceandfire.entity.util.MyrmexHive;
import com.github.alexthe666.iceandfire.world.MyrmexWorldData;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;

import java.util.EnumSet;

public class MyrmexAILeaveHive extends Goal {
    private final EntityMyrmexBase myrmex;
    private final double movementSpeed;
    private Path path;
    private BlockPos nextEntrance = BlockPos.ZERO;

    public MyrmexAILeaveHive(EntityMyrmexBase entityIn, double movementSpeedIn) {
        this.myrmex = entityIn;
        this.movementSpeed = movementSpeedIn;
        this.setMutexFlags(EnumSet.of(Flag.MOVE));
    }

    public boolean shouldExecute() {
        if (this.myrmex instanceof EntityMyrmexQueen) {
            return false;
        }
        if (this.myrmex.isChild()) {
            return false;
        }

        if (!this.myrmex.canMove() || !this.myrmex.shouldLeaveHive() || this.myrmex.shouldEnterHive() || this.myrmex.canSeeSky() || this.myrmex instanceof EntityMyrmexWorker && (((EntityMyrmexWorker) this.myrmex).holdingSomething() || !this.myrmex.getHeldItem(Hand.MAIN_HAND).isEmpty()) || this.myrmex.isEnteringHive) {
            return false;
        }
        MyrmexHive village = MyrmexWorldData.get(this.myrmex.world).getNearestHive(this.myrmex.func_233580_cy_(), 1000);
        if (village == null) {
            return false;
        } else {
            nextEntrance = MyrmexHive.getGroundedPos(this.myrmex.world, village.getClosestEntranceToEntity(this.myrmex, this.myrmex.getRNG(), true));
            this.path = this.myrmex.getNavigator().getPathToPos(nextEntrance, 0);
            return this.path != null;
        }
    }

    public boolean shouldContinueExecuting() {

        if (this.myrmex.getDistanceSq(nextEntrance.getX() + 0.5D, nextEntrance.getY() + 0.5D, nextEntrance.getZ() + 0.5D) <= 3 || this.myrmex.shouldEnterHive()) {
            return false;
        }
        return !this.myrmex.getNavigator().noPath() && this.myrmex.getDistanceSq(nextEntrance.getX() + 0.5D, nextEntrance.getY() + 0.5D, nextEntrance.getZ() + 0.5D) > 3 && this.myrmex.shouldLeaveHive();
    }

    public void startExecuting() {
        this.myrmex.getNavigator().setPath(this.path, this.movementSpeed);
    }

    public void resetTask() {
        nextEntrance = BlockPos.ZERO;
        this.myrmex.getNavigator().setPath(null, this.movementSpeed);
    }
}