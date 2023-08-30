package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityDeathWorm;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.goal.JumpGoal;
import net.minecraft.world.phys.Vec3;

public class DeathWormAIJump extends JumpGoal {

    private static final int[] JUMP_DISTANCES = new int[] {
        0, 1, 4, 5, 6, 7
    };
    private final EntityDeathWorm dolphin;
    private final int chance;
    private boolean inWater;
    private int jumpCooldown;

    public DeathWormAIJump(EntityDeathWorm dolphin, int p_i50329_2_) {
        this.dolphin = dolphin;
        this.chance = p_i50329_2_;
    }

    @Override
    public boolean canUse() {
        if (jumpCooldown > 0) {
            jumpCooldown--;
        }
        if (this.dolphin.getRandom().nextInt(this.chance) != 0 || dolphin.isVehicle()
            || dolphin.getTarget() != null) {
            return false;
        } else {
            Direction direction = this.dolphin.getMotionDirection();
            final int i = direction.getStepX();
            final int j = direction.getStepZ();
            BlockPos blockpos = this.dolphin.blockPosition();
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
        return this.dolphin.level().getBlockState(blockpos).is(BlockTags.SAND);
    }

    @SuppressWarnings("deprecation")
    private boolean isAirAbove(BlockPos pos, int dx, int dz, int scale) {
        return this.dolphin.level().getBlockState(pos.offset(dx * scale, 1, dz * scale)).isAir()
            && this.dolphin.level().getBlockState(pos.offset(dx * scale, 2, dz * scale)).isAir();
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    @Override
    public boolean canContinueToUse() {
        final double d0 = this.dolphin.getDeltaMovement().y;
        return jumpCooldown > 0 && (d0 * d0 >= 0.03F || this.dolphin.getXRot() == 0.0F
            || Math.abs(this.dolphin.getXRot()) >= 10.0F || !this.dolphin.isInSand()) && !this.dolphin.onGround();
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
        Direction direction = this.dolphin.getMotionDirection();
        final float up = (dolphin.getScale() > 3 ? 0.7F : 0.4F) + dolphin.getRandom().nextFloat() * 0.4F;
        this.dolphin
            .setDeltaMovement(this.dolphin.getDeltaMovement().add(direction.getStepX() * 0.6D, up, direction.getStepZ() * 0.6D));
        this.dolphin.getNavigation().stop();
        this.dolphin.setWormJumping(30);
        this.jumpCooldown = dolphin.getRandom().nextInt(65) + 32;
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by
     * another one
     */
    @Override
    public void stop() {
        this.dolphin.setXRot(0.0F);
    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    @Override
    public void tick() {
        final boolean flag = this.inWater;
        if (!flag) {
            this.inWater = this.dolphin.level().getBlockState(this.dolphin.blockPosition()).is(BlockTags.SAND);
        }
        Vec3 vector3d = this.dolphin.getDeltaMovement();
        if (vector3d.y * vector3d.y < 0.1F && this.dolphin.getXRot() != 0.0F) {
            this.dolphin.setXRot(Mth.rotLerp(this.dolphin.getXRot(), 0.0F, 0.2F));
        } else {

            final double d0 = (vector3d.horizontalDistance());
            final double d1 = Math.signum(-vector3d.y) * Math.acos(d0 / vector3d.length()) * (180F / (float) Math.PI);
            this.dolphin.setXRot((float) d1);
        }

    }
}
