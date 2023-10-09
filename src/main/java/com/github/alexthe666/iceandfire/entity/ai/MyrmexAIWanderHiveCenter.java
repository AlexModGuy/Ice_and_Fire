package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityMyrmexBase;
import com.github.alexthe666.iceandfire.entity.util.MyrmexHive;
import com.github.alexthe666.iceandfire.world.MyrmexWorldData;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.pathfinder.Path;

import java.util.EnumSet;

public class MyrmexAIWanderHiveCenter extends Goal {
    private final EntityMyrmexBase myrmex;
    private final double movementSpeed;
    private Path path;
    private BlockPos target = BlockPos.ZERO;

    public MyrmexAIWanderHiveCenter(EntityMyrmexBase entityIn, double movementSpeedIn) {
        this.myrmex = entityIn;
        this.movementSpeed = movementSpeedIn;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (!this.myrmex.canMove() || !this.myrmex.shouldEnterHive() && !this.myrmex.getNavigation().isDone() || this.myrmex.canSeeSky()) {
            return false;
        }
        MyrmexHive village = MyrmexWorldData.get(this.myrmex.level()).getNearestHive(this.myrmex.blockPosition(), 300);
        if (village == null) {
            village = this.myrmex.getHive();
        }
        if (village == null) {
            return false;
        } else {
            target = getNearPos(MyrmexHive.getGroundedPos(this.myrmex.level(), village.getCenter()));

            this.path = this.myrmex.getNavigation().createPath(target, 0);
            return this.path != null;
        }
    }

    @Override
    public boolean canContinueToUse() {
        return !this.myrmex.getNavigation().isDone() && this.myrmex.distanceToSqr(target.getX() + 0.5D, target.getY() + 0.5D, target.getZ() + 0.5D) > 3 && this.myrmex.shouldEnterHive();
    }

    @Override
    public void start() {
        this.myrmex.getNavigation().moveTo(this.path, this.movementSpeed);
    }

    @Override
    public void stop() {
        target = BlockPos.ZERO;
        this.myrmex.getNavigation().moveTo((Path) null, this.movementSpeed);

    }

    public BlockPos getNearPos(BlockPos pos) {
        return pos.offset(this.myrmex.getRandom().nextInt(8) - 4, 0, this.myrmex.getRandom().nextInt(8) - 4);
    }
}