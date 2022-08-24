package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityTroll;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.Random;

public class TrollAIFleeSun extends Goal {
    private final EntityTroll troll;
    private final double movementSpeed;
    private final World world;
    private double shelterX;
    private double shelterY;
    private double shelterZ;

    public TrollAIFleeSun(EntityTroll theCreatureIn, double movementSpeedIn) {
        this.troll = theCreatureIn;
        this.movementSpeed = movementSpeedIn;
        this.world = theCreatureIn.level;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (!this.world.isDay()) {
            return false;
        } else if (!this.world.canSeeSky(new BlockPos(this.troll.getX(), this.troll.getBoundingBox().minY, this.troll.getZ()))) {
            return false;
        } else {
            Vector3d Vector3d = this.findPossibleShelter();

            if (Vector3d == null) {
                return false;
            } else {
                this.shelterX = Vector3d.x;
                this.shelterY = Vector3d.y;
                this.shelterZ = Vector3d.z;
                return true;
            }
        }
    }

    /**
     * Returns whether an in-progress Goal should continue executing
     */
    @Override
    public boolean canContinueToUse() {
        return !this.troll.getNavigation().isDone();
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    @Override
    public void start() {
        this.troll.getNavigation().moveTo(this.shelterX, this.shelterY, this.shelterZ, this.movementSpeed);
    }

    @Nullable
    private Vector3d findPossibleShelter() {
        Random random = this.troll.getRandom();
        BlockPos blockpos = new BlockPos(this.troll.getX(), this.troll.getBoundingBox().minY, this.troll.getZ());

        for (int i = 0; i < 10; ++i) {
            BlockPos blockpos1 = blockpos.offset(random.nextInt(20) - 10, random.nextInt(6) - 3, random.nextInt(20) - 10);

            if (!this.world.canSeeSky(blockpos1) && this.troll.getWalkTargetValue(blockpos1) < 0.0F) {
                return new Vector3d(blockpos1.getX(), blockpos1.getY(), blockpos1.getZ());
            }
        }

        return null;
    }
}