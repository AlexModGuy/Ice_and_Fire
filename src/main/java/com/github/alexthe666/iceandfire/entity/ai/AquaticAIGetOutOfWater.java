package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntitySiren;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class AquaticAIGetOutOfWater extends Goal {
    private final Mob creature;
    private final double movementSpeed;
    private final Level world;
    private double shelterX;
    private double shelterY;
    private double shelterZ;

    public AquaticAIGetOutOfWater(Mob theCreatureIn, double movementSpeedIn) {
        this.creature = theCreatureIn;
        this.movementSpeed = movementSpeedIn;
        this.world = theCreatureIn.level();
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (!creature.isInWater() || !((EntitySiren) creature).wantsToSing()) {
            return false;
        } else {
            Vec3 Vector3d = this.findPossibleShelter();

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

    @Override
    public boolean canContinueToUse() {
        return !this.creature.getNavigation().isDone();
    }

    @Override
    public void start() {
        this.creature.getNavigation().moveTo(this.shelterX, this.shelterY, this.shelterZ, this.movementSpeed);
    }

    @Nullable
    private Vec3 findPossibleShelter() {
        RandomSource random = this.creature.getRandom();
        BlockPos blockpos = BlockPos.containing(this.creature.getBlockX(), this.creature.getBoundingBox().minY, this.creature.getBlockZ());

        for (int i = 0; i < 10; ++i) {
            BlockPos blockpos1 = blockpos.offset(random.nextInt(20) - 10, random.nextInt(6) - 3, random.nextInt(20) - 10);
            if (this.world.getBlockState(blockpos1).isSolidRender(world, blockpos1)) {
                return new Vec3(blockpos1.getX(), blockpos1.getY(), blockpos1.getZ());
            }
        }

        return null;
    }
}