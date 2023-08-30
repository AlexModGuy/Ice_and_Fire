package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntitySeaSerpent;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;
import java.util.concurrent.ThreadLocalRandom;

public class SeaSerpentAIGetInWater extends Goal {

    private final EntitySeaSerpent creature;
    private BlockPos targetPos;

    public SeaSerpentAIGetInWater(EntitySeaSerpent creature) {
        this.creature = creature;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if ((this.creature.jumpCooldown == 0 || this.creature.onGround())
            && !this.creature.level().getFluidState(this.creature.blockPosition()).is(FluidTags.WATER)) {
            targetPos = generateTarget();
            return targetPos != null;
        }
        return false;
    }

    @Override
    public void start() {
        if (targetPos != null) {
            this.creature.getNavigation().moveTo(targetPos.getX(), targetPos.getY(), targetPos.getZ(), 1.5D);
        }
    }

    @Override
    public boolean canContinueToUse() {
        return !this.creature.getNavigation().isDone() && targetPos != null
            && !this.creature.level().getFluidState(this.creature.blockPosition()).is(FluidTags.WATER);
    }

    public BlockPos generateTarget() {
        BlockPos blockpos = null;
        final int range = 16;
        for (int i = 0; i < 15; i++) {
            BlockPos blockpos1 = this.creature.blockPosition().offset(ThreadLocalRandom.current().nextInt(range) - range / 2,
                3, ThreadLocalRandom.current().nextInt(range) - range / 2);
            while (this.creature.level().isEmptyBlock(blockpos1) && blockpos1.getY() > 1) {
                blockpos1 = blockpos1.below();
            }
            if (this.creature.level().getFluidState(blockpos1).is(FluidTags.WATER)) {
                blockpos = blockpos1;
            }
        }
        return blockpos;
    }
}
