package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityPixie;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;

import java.util.EnumSet;
import java.util.Random;

public class PixieAIMoveRandom extends Goal {
    BlockPos target;
    EntityPixie pixie;
    Random random;

    public PixieAIMoveRandom(EntityPixie entityPixieIn) {
        this.pixie = entityPixieIn;
        this.random = entityPixieIn.getRandom();
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        target = EntityPixie.getPositionRelativetoGround(this.pixie, this.pixie.level, this.pixie.getX() + this.random.nextInt(15) - 7, this.pixie.getZ() + this.random.nextInt(15) - 7, this.random);
        return !this.pixie.isOwnerClose() && !this.pixie.isPixieSitting() && isDirectPathBetweenPoints(this.pixie.blockPosition(), target) && !this.pixie.getMoveControl().hasWanted() && this.random.nextInt(4) == 0 && this.pixie.getHousePos() == null;
    }

    protected boolean isDirectPathBetweenPoints(BlockPos posVec31, BlockPos posVec32) {
        return this.pixie.level.clip(
                new RayTraceContext(new Vector3d(posVec31.getX() + 0.5D, posVec31.getY() + 0.5D, posVec31.getZ() + 0.5D),
                    new Vector3d(posVec32.getX() + 0.5D, posVec32.getY() + this.pixie.getBbHeight() * 0.5D,
                        posVec32.getZ() + 0.5D),
                    RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, this.pixie))
            .getType() == RayTraceResult.Type.MISS;
    }

    @Override
    public boolean canContinueToUse() {
        return false;
    }

    @Override
    public void tick() {
        if (!isDirectPathBetweenPoints(this.pixie.blockPosition(), target)) {
            target = EntityPixie.getPositionRelativetoGround(this.pixie, this.pixie.level, this.pixie.getX() + this.random.nextInt(15) - 7, this.pixie.getZ() + this.random.nextInt(15) - 7, this.random);
        }
        if (this.pixie.level.isEmptyBlock(target)) {

            this.pixie.getMoveControl().setWantedPosition(target.getX() + 0.5D, target.getY() + 0.5D, target.getZ() + 0.5D,
                0.25D);
            if (this.pixie.getTarget() == null) {
                this.pixie.getLookControl().setLookAt(target.getX() + 0.5D, target.getY() + 0.5D,
                    target.getZ() + 0.5D, 180.0F, 20.0F);
            }
        }
    }
}
