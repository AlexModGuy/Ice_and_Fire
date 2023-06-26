package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityPixie;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class PixieAIEnterHouse extends Goal {

    EntityPixie pixie;
    RandomSource random;

    public PixieAIEnterHouse(EntityPixie entityPixieIn) {
        this.pixie = entityPixieIn;
        this.random = entityPixieIn.getRandom();
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (this.pixie.isOwnerClose() || this.pixie.getMoveControl().hasWanted() || this.pixie.isPixieSitting() || this.random.nextInt(20) != 0 || this.pixie.ticksUntilHouseAI != 0) {
            return false;
        }

        BlockPos blockpos1 = EntityPixie.findAHouse(this.pixie, this.pixie.level());
        return !blockpos1.toString().equals(this.pixie.blockPosition().toString());
    }

    @Override
    public boolean canContinueToUse() {
        return false;
    }

    @Override
    public void tick() {
        for (int i = 0; i < 3; ++i) {
            BlockPos blockpos1 = EntityPixie.findAHouse(this.pixie, this.pixie.level());
            this.pixie.getMoveControl().setWantedPosition(blockpos1.getX() + 0.5D, blockpos1.getY() + 0.5D,
                blockpos1.getZ() + 0.5D, 0.25D);
            this.pixie.setHousePosition(blockpos1);
            if (this.pixie.getTarget() == null) {
                this.pixie.getLookControl().setLookAt(blockpos1.getX() + 0.5D, blockpos1.getY() + 0.5D,
                    blockpos1.getZ() + 0.5D, 180.0F, 20.0F);
            }
        }
    }
}
