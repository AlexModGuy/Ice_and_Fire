package com.github.alexthe666.iceandfire.entity.ai;

import java.util.EnumSet;
import java.util.Random;

import com.github.alexthe666.iceandfire.entity.EntityPixie;

import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.BlockPos;

public class PixieAIEnterHouse extends Goal {

    EntityPixie pixie;
    Random random;

    public PixieAIEnterHouse(EntityPixie entityPixieIn) {
        this.pixie = entityPixieIn;
        this.random = entityPixieIn.getRNG();
        this.setMutexFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean shouldExecute() {
        if (this.pixie.isOwnerClose() || this.pixie.getMoveHelper().isUpdating() || this.pixie.isPixieSitting() || this.random.nextInt(20) != 0 || this.pixie.ticksUntilHouseAI != 0) {
            return false;
        }

        BlockPos blockpos1 = EntityPixie.findAHouse(this.pixie, this.pixie.world);
        return !blockpos1.toString().equals(this.pixie.getPosition().toString());
    }

    @Override
    public boolean shouldContinueExecuting() {
        return false;
    }

    @Override
    public void tick() {
        for (int i = 0; i < 3; ++i) {
            BlockPos blockpos1 = EntityPixie.findAHouse(this.pixie, this.pixie.world);
            this.pixie.getMoveHelper().setMoveTo(blockpos1.getX() + 0.5D, blockpos1.getY() + 0.5D,
                blockpos1.getZ() + 0.5D, 0.25D);
            this.pixie.setHousePosition(blockpos1);
            if (this.pixie.getAttackTarget() == null) {
                this.pixie.getLookController().setLookPosition(blockpos1.getX() + 0.5D, blockpos1.getY() + 0.5D,
                    blockpos1.getZ() + 0.5D, 180.0F, 20.0F);
            }
        }
    }
}
