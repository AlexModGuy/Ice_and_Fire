package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityMyrmexBase;
import com.github.alexthe666.iceandfire.entity.EntityMyrmexQueen;
import com.github.alexthe666.iceandfire.entity.EntityMyrmexWorker;
import com.github.alexthe666.iceandfire.entity.util.MyrmexHive;
import com.github.alexthe666.iceandfire.pathfinding.raycoms.AdvancedPathNavigate;
import com.github.alexthe666.iceandfire.pathfinding.raycoms.PathResult;
import com.github.alexthe666.iceandfire.world.MyrmexWorldData;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class MyrmexAILeaveHive extends Goal {
    private final EntityMyrmexBase myrmex;
    private final double movementSpeed;
    private PathResult path;
    private BlockPos nextEntrance = BlockPos.ZERO;

    public MyrmexAILeaveHive(EntityMyrmexBase entityIn, double movementSpeedIn) {
        this.myrmex = entityIn;
        this.movementSpeed = movementSpeedIn;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (this.myrmex instanceof EntityMyrmexQueen) {
            return false;
        }
        //If it's riding something don't execute
        if (!(this.myrmex.getNavigation() instanceof AdvancedPathNavigate) || this.myrmex.isPassenger()) {
            return false;
        }
        if (this.myrmex.isBaby()) {
            return false;
        }
        if (!this.myrmex.canMove() || !this.myrmex.shouldLeaveHive() || this.myrmex.shouldEnterHive() || !this.myrmex.isInHive() || this.myrmex instanceof EntityMyrmexWorker && (((EntityMyrmexWorker) this.myrmex).holdingSomething() || !this.myrmex.getItemInHand(InteractionHand.MAIN_HAND).isEmpty()) || this.myrmex.isEnteringHive) {
            return false;
        }
        MyrmexHive village = MyrmexWorldData.get(this.myrmex.level()).getNearestHive(this.myrmex.blockPosition(), 1000);
        if (village == null) {
            return false;
        } else {
            nextEntrance = MyrmexHive.getGroundedPos(this.myrmex.level(), village.getClosestEntranceToEntity(this.myrmex, this.myrmex.getRandom(), true));
            this.path = ((AdvancedPathNavigate) this.myrmex.getNavigation()).moveToXYZ(nextEntrance.getX(), nextEntrance.getY(), nextEntrance.getZ(), movementSpeed);
            return true;
        }
    }

    @Override
    public boolean canContinueToUse() {
        if (this.myrmex.isCloseEnoughToTarget(nextEntrance, 12) || this.myrmex.shouldEnterHive()) {
            return false;
        }

        return this.myrmex.shouldLeaveHive();
    }

    @Override
    public void tick() {
        //If the path has been created but the destination couldn't be reached
        //or if the myrmex has reached the end of the path but isn't close enough to the entrance for some reason
        if (!this.myrmex.pathReachesTarget(path,nextEntrance,12)) {
            MyrmexHive village = MyrmexWorldData.get(this.myrmex.level()).getNearestHive(this.myrmex.blockPosition(), 1000);
            nextEntrance = MyrmexHive.getGroundedPos(this.myrmex.level(), village.getClosestEntranceToEntity(this.myrmex, this.myrmex.getRandom(), true));
            path = ((AdvancedPathNavigate) this.myrmex.getNavigation()).moveToXYZ(nextEntrance.getX(), nextEntrance.getY() + 1, nextEntrance.getZ(), movementSpeed);
        }
    }


    @Override
    public void start() {
    }

    @Override
    public void stop() {
        nextEntrance = BlockPos.ZERO;
        this.myrmex.getNavigation().stop();
    }
}