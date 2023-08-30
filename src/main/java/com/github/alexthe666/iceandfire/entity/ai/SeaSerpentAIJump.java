package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntitySeaSerpent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.goal.JumpGoal;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;

public class SeaSerpentAIJump extends JumpGoal {

    private static final int[] JUMP_DISTANCES = new int[] {
        0, 2, 4, 5, 6, 7
    };
    private final EntitySeaSerpent serpent;
    private final int chance;
    private boolean inWater;

    public SeaSerpentAIJump(EntitySeaSerpent dolphin, int chance) {
        this.serpent = dolphin;
        this.chance = chance;
    }

    @Override
    public boolean canUse() {
        if (this.serpent.getRandom().nextInt(this.chance) != 0 || serpent.getTarget() != null
            || serpent.jumpCooldown != 0) {
            return false;
        } else {
            Direction direction = this.serpent.getMotionDirection();
            final int i = direction.getStepX();
            final int j = direction.getStepZ();
            BlockPos blockpos = this.serpent.blockPosition();
            for (int k : JUMP_DISTANCES) {
                if (!this.canJumpTo(blockpos, i, j, k) || !this.isAirAbove(blockpos, i, j, k)) {
                    return false;
                }
            }
            return true;
        }
    }

    private boolean canJumpTo(BlockPos pos, int dx, int dz, int scale) {
        BlockPos blockpos = pos.offset(dx * scale, 0, dz * scale);
        return this.serpent.level().getFluidState(blockpos).is(FluidTags.WATER)
            && !this.serpent.level().getBlockState(blockpos).blocksMotion();
    }

    private boolean isAirAbove(BlockPos pos, int dx, int dz, int scale) {
        return this.serpent.level().getBlockState(pos.offset(dx * scale, 1, dz * scale)).isAir()
            && this.serpent.level().getBlockState(pos.offset(dx * scale, 2, dz * scale)).isAir();
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    @Override
    public boolean canContinueToUse() {
        double d0 = this.serpent.getDeltaMovement().y;
        return serpent.jumpCooldown > 0 && (d0 * d0 >= 0.03F || this.serpent.getXRot() == 0.0F
            || Math.abs(this.serpent.getXRot()) >= 10.0F || !this.serpent.isInWater())
            && !this.serpent.onGround();
    }

    @Override
    public boolean isInterruptable() {
        return false;
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    @Override
    public void start() {
        Direction direction = this.serpent.getMotionDirection();
        final float up = 1F + serpent.getRandom().nextFloat() * 0.8F;
        this.serpent
            .setDeltaMovement(this.serpent.getDeltaMovement().add(direction.getStepX() * 0.6D, up, direction.getStepZ() * 0.6D));
        this.serpent.setJumpingOutOfWater(true);
        this.serpent.getNavigation().stop();
        this.serpent.jumpCooldown = serpent.getRandom().nextInt(100) + 100;
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by
     * another one
     */
    @Override
    public void stop() {
        this.serpent.setJumpingOutOfWater(false);
        this.serpent.setXRot(0.0F);
    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    @Override
    public void tick() {
        final boolean flag = this.inWater;
        if (!flag) {
            FluidState fluidstate = this.serpent.level().getFluidState(this.serpent.blockPosition());
            this.inWater = fluidstate.is(FluidTags.WATER);
        }

        if (this.inWater && !flag) {
            this.serpent.playSound(SoundEvents.DOLPHIN_JUMP, 1.0F, 1.0F);
        }

        Vec3 vector3d = this.serpent.getDeltaMovement();
        if (vector3d.y * vector3d.y < 0.1F && this.serpent.getXRot() != 0.0F) {
            this.serpent.setXRot(Mth.rotLerp(this.serpent.getXRot(), 0.0F, 0.2F));
        } else {
            final double d0 = vector3d.horizontalDistance();
            final double d1 = Math.signum(-vector3d.y) * Math.acos(d0 / vector3d.length()) * (180F / (float) Math.PI);
            this.serpent.setXRot((float) d1);
        }

    }
}
