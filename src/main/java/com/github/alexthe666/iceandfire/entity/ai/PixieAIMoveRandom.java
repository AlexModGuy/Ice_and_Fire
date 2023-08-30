package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityPixie;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class PixieAIMoveRandom extends Goal {
    BlockPos target;
    EntityPixie pixie;
    RandomSource random;

    public PixieAIMoveRandom(EntityPixie entityPixieIn) {
        this.pixie = entityPixieIn;
        this.random = entityPixieIn.getRandom();
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        target = EntityPixie.getPositionRelativetoGround(this.pixie, this.pixie.level(), this.pixie.getX() + this.random.nextInt(15) - 7, this.pixie.getZ() + this.random.nextInt(15) - 7, this.random);
        return !this.pixie.isOwnerClose() && !this.pixie.isPixieSitting() && isDirectPathBetweenPoints(this.pixie.blockPosition(), target) && !this.pixie.getMoveControl().hasWanted() && this.random.nextInt(4) == 0 && this.pixie.getHousePos() == null;
    }

    protected boolean isDirectPathBetweenPoints(BlockPos posVec31, BlockPos posVec32) {
        return this.pixie.level().clip(
                new ClipContext(new Vec3(posVec31.getX() + 0.5D, posVec31.getY() + 0.5D, posVec31.getZ() + 0.5D),
                    new Vec3(posVec32.getX() + 0.5D, posVec32.getY() + this.pixie.getBbHeight() * 0.5D,
                        posVec32.getZ() + 0.5D),
                    ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this.pixie))
            .getType() == HitResult.Type.MISS;
    }

    @Override
    public boolean canContinueToUse() {
        return false;
    }

    @Override
    public void tick() {
        if (!isDirectPathBetweenPoints(this.pixie.blockPosition(), target)) {
            target = EntityPixie.getPositionRelativetoGround(this.pixie, this.pixie.level(), this.pixie.getX() + this.random.nextInt(15) - 7, this.pixie.getZ() + this.random.nextInt(15) - 7, this.random);
        }
        if (this.pixie.level().isEmptyBlock(target)) {

            this.pixie.getMoveControl().setWantedPosition(target.getX() + 0.5D, target.getY() + 0.5D, target.getZ() + 0.5D,
                0.25D);
            if (this.pixie.getTarget() == null) {
                this.pixie.getLookControl().setLookAt(target.getX() + 0.5D, target.getY() + 0.5D,
                    target.getZ() + 0.5D, 180.0F, 20.0F);
            }
        }
    }
}
