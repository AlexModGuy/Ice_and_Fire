package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntitySeaSerpent;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class SeaSerpentAIRandomSwimming extends RandomStrollGoal {
    public SeaSerpentAIRandomSwimming(PathfinderMob creature, double speed, int chance) {
        super(creature, speed, chance, false);
    }

    @Override
    public boolean canUse() {
        if (this.mob.isVehicle() || this.mob.getTarget() != null) {
            return false;
        } else {
            if (!this.forceTrigger) {
                if (this.mob.getRandom().nextInt(this.interval) != 0) {
                    return false;
                }
            }
            Vec3 vector3d = this.getPosition();
            if (vector3d == null) {
                return false;
            } else {
                this.wantedX = vector3d.x;
                this.wantedY = vector3d.y;
                this.wantedZ = vector3d.z;
                this.forceTrigger = false;
                return true;
            }
        }
    }

    @Override
    @Nullable
    protected Vec3 getPosition() {
        if (((EntitySeaSerpent) this.mob).jumpCooldown <= 0) {
            Vec3 vector3d = findSurfaceTarget(this.mob, 32, 16);
            if (vector3d != null) {
                return vector3d.add(0, 1, 0);
            }
        } else {
            BlockPos blockpos = null;
            final Random random = ThreadLocalRandom.current();
            final int range = 16;
            for (int i = 0; i < 15; i++) {
                BlockPos blockpos1 = this.mob.blockPosition().offset(random.nextInt(range) - range / 2, random.nextInt(range) - range / 2, random.nextInt(range) - range / 2);
                while (this.mob.level().isEmptyBlock(blockpos1) && this.mob.level().getFluidState(blockpos1).isEmpty() && blockpos1.getY() > 1) {
                    blockpos1 = blockpos1.below();
                }
                if (this.mob.level().getFluidState(blockpos1).is(FluidTags.WATER)) {
                    blockpos = blockpos1;
                }
            }
            return blockpos == null ? null : new Vec3(blockpos.getX() + 0.5D, blockpos.getY() + 0.5D, blockpos.getZ() + 0.5D);
        }
        return null;
    }

    private boolean canJumpTo(BlockPos pos, int dx, int dz, int scale) {
        BlockPos blockpos = pos.offset(dx * scale, 0, dz * scale);
        return this.mob.level().getFluidState(blockpos).is(FluidTags.WATER) && !this.mob.level().getBlockState(blockpos).blocksMotion();
    }

    private boolean isAirAbove(BlockPos pos, int dx, int dz, int scale) {
        return this.mob.level().getBlockState(pos.offset(dx * scale, 1, dz * scale)).isAir() && this.mob.level().getBlockState(pos.offset(dx * scale, 2, dz * scale)).isAir();
    }

    private Vec3 findSurfaceTarget(PathfinderMob creature, int i, int i1) {
        BlockPos upPos = creature.blockPosition();
        while (creature.level().getFluidState(upPos).is(FluidTags.WATER)) {
            upPos = upPos.above();
        }
        if (isAirAbove(upPos.below(), 0, 0, 0) && canJumpTo(upPos.below(), 0, 0, 0)) {
            return new Vec3(upPos.getX() + 0.5F, upPos.getY() + 3.5F, upPos.getZ() + 0.5F);
        }
        return null;
    }
}
