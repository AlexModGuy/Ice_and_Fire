package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityMyrmexBase;
import com.github.alexthe666.iceandfire.entity.util.MyrmexHive;
import com.github.alexthe666.iceandfire.pathfinding.raycoms.AdvancedPathNavigate;
import com.github.alexthe666.iceandfire.pathfinding.raycoms.PathResult;
import com.github.alexthe666.iceandfire.world.MyrmexWorldData;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class MyrmexAIReEnterHive extends Goal {
    private final EntityMyrmexBase myrmex;
    private final double movementSpeed;
    private PathResult path;
    private BlockPos currentTarget = BlockPos.ZERO;
    private Phases currentPhase = Phases.GOTOENTRANCE;

    private enum Phases {
        GOTOENTRANCE,
        GOTOEXIT,
        GOTOCENTER
    }

    private MyrmexHive hive;

    public MyrmexAIReEnterHive(EntityMyrmexBase entityIn, double movementSpeedIn) {
        this.myrmex = entityIn;
        this.movementSpeed = movementSpeedIn;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (!this.myrmex.canMove() || this.myrmex.shouldLeaveHive() || !this.myrmex.shouldEnterHive() || currentPhase != Phases.GOTOENTRANCE) {
            return false;
        }
        MyrmexHive village = this.myrmex.getHive();
        if (village == null) {
            village = MyrmexWorldData.get(this.myrmex.level()).getNearestHive(this.myrmex.blockPosition(), 500);
        }
        if (!(this.myrmex.getNavigation() instanceof AdvancedPathNavigate) || this.myrmex.isPassenger()) {
            return false;
        }
        if (village == null || this.myrmex.isInHive()) {
            return false;
        } else {
            this.hive = village;
            currentTarget = MyrmexHive.getGroundedPos(this.myrmex.level(), hive.getClosestEntranceToEntity(this.myrmex, this.myrmex.getRandom(), false));
            this.path = ((AdvancedPathNavigate) this.myrmex.getNavigation()).moveToXYZ(currentTarget.getX(), currentTarget.getY(), currentTarget.getZ(), 1);
            currentPhase = Phases.GOTOENTRANCE;
            return this.path != null;
        }
    }

    @Override
    public void tick() {
        //Fallback for if for some reason the myrmex can't reach the entrance try a different one (random)
        if (currentPhase == Phases.GOTOENTRANCE && !this.myrmex.pathReachesTarget(path, currentTarget, 12)) {
            currentTarget = MyrmexHive.getGroundedPos(this.myrmex.level(), hive.getClosestEntranceToEntity(this.myrmex, this.myrmex.getRandom(), true));
            this.path = ((AdvancedPathNavigate) this.myrmex.getNavigation()).moveToXYZ(currentTarget.getX(), currentTarget.getY(), currentTarget.getZ(), movementSpeed);
        }
        if (currentPhase == Phases.GOTOENTRANCE && this.myrmex.isCloseEnoughToTarget(currentTarget, 12)) {
            if (hive != null) {
                currentTarget = hive.getClosestEntranceBottomToEntity(this.myrmex, this.myrmex.getRandom());
                currentPhase = Phases.GOTOEXIT;
                this.path = ((AdvancedPathNavigate) this.myrmex.getNavigation()).moveToXYZ(currentTarget.getX(), currentTarget.getY(), currentTarget.getZ(), 1);
            }
        }
        if (currentPhase == Phases.GOTOEXIT && this.myrmex.isCloseEnoughToTarget(currentTarget, 12)) {
            if (hive != null) {
                currentTarget = MyrmexHive.getGroundedPos(this.myrmex.level(), hive.getCenter());
                currentPhase = Phases.GOTOCENTER;
                this.path = ((AdvancedPathNavigate) this.myrmex.getNavigation()).moveToXYZ(currentTarget.getX(), currentTarget.getY(), currentTarget.getZ(), 1);
            }
        }
        this.myrmex.isEnteringHive = !this.myrmex.isCloseEnoughToTarget(currentTarget, 14) && currentPhase != Phases.GOTOCENTER;
    }

    @Override
    public boolean canContinueToUse() {
        return !(this.myrmex.isCloseEnoughToTarget(currentTarget, 9) && currentPhase != Phases.GOTOCENTER);
    }

    @Override
    public void stop() {
        currentTarget = BlockPos.ZERO;
        currentPhase = Phases.GOTOENTRANCE;
    }
}