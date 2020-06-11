package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityTroll;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
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
        this.world = theCreatureIn.world;
        this.setMutexFlags(EnumSet.of(Flag.MOVE));
    }

    public boolean shouldExecute() {
        if (!this.world.isDaytime()) {
            return false;
        } else if (!this.world.canSeeSky(new BlockPos(this.troll.getPosX(), this.troll.getBoundingBox().minY, this.troll.getPosZ()))) {
            return false;
        } else {
            Vec3d vec3d = this.findPossibleShelter();

            if (vec3d == null) {
                return false;
            } else {
                this.shelterX = vec3d.x;
                this.shelterY = vec3d.y;
                this.shelterZ = vec3d.z;
                return true;
            }
        }
    }

    /**
     * Returns whether an in-progress Goal should continue executing
     */
    public boolean shouldContinueExecuting() {
        return !this.troll.getNavigator().noPath();
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting() {
        this.troll.getNavigator().tryMoveToXYZ(this.shelterX, this.shelterY, this.shelterZ, this.movementSpeed);
    }

    @Nullable
    private Vec3d findPossibleShelter() {
        Random random = this.troll.getRNG();
        BlockPos blockpos = new BlockPos(this.troll.getPosX(), this.troll.getBoundingBox().minY, this.troll.getPosZ());

        for (int i = 0; i < 10; ++i) {
            BlockPos blockpos1 = blockpos.add(random.nextInt(20) - 10, random.nextInt(6) - 3, random.nextInt(20) - 10);

            if (!this.world.canSeeSky(blockpos1) && this.troll.getBlockPathWeight(blockpos1) < 0.0F) {
                return new Vec3d(blockpos1.getX(), blockpos1.getY(), blockpos1.getZ());
            }
        }

        return null;
    }
}