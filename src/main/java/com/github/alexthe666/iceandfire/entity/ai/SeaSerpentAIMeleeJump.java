package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntitySeaSerpent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.JumpGoal;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;

public class SeaSerpentAIMeleeJump  extends JumpGoal {
    private final EntitySeaSerpent dolphin;
    private int attackCooldown = 0;
    private boolean inWater;

    public SeaSerpentAIMeleeJump(EntitySeaSerpent dolphin) {
        this.dolphin = dolphin;
    }

    @Override
    public boolean canUse() {
        return this.dolphin.getTarget() != null && dolphin.shouldUseJumpAttack(this.dolphin.getTarget()) && !this.dolphin.onGround();
    }

    @Override
    public boolean canContinueToUse() {
        final double d0 = this.dolphin.getDeltaMovement().y;
        return dolphin.getTarget() != null && dolphin.jumpCooldown > 0
            && (d0 * d0 >= 0.03F || this.dolphin.getXRot() == 0.0F
            || Math.abs(this.dolphin.getXRot()) >= 10.0F || !this.dolphin.isInWater())
            && !this.dolphin.onGround();
    }

    @Override
    public boolean isInterruptable() {
        return false;
    }

    @Override
    public void start() {
        LivingEntity target = this.dolphin.getTarget();
        if (target != null) {
            final double distanceXZ = dolphin.distanceToSqr(target.getX(), dolphin.getY(), target.getZ());
            if (distanceXZ < 300) {
                dolphin.lookAt(target, 260, 30);
                final double smoothX = Mth.clamp(Math.abs(target.getX() - dolphin.getX()), 0, 1);
                final double smoothZ = Mth.clamp(Math.abs(target.getZ() - dolphin.getZ()), 0, 1);
                final double d0 = (target.getX() - this.dolphin.getX()) * 0.3 * smoothX;
                final double d2 = (target.getZ() - this.dolphin.getZ()) * 0.3 * smoothZ;
                final float up = 1F + dolphin.getRandom().nextFloat() * 0.8F;
                this.dolphin.setDeltaMovement(this.dolphin.getDeltaMovement().add(d0 * 0.3D, up, d2 * 0.3D));
                this.dolphin.getNavigation().stop();
                this.dolphin.jumpCooldown = dolphin.getRandom().nextInt(32) + 32;
            } else {
                dolphin.getNavigation().moveTo(target, 1.0F);
            }

        }
    }

    @Override
    public void stop() {
        this.dolphin.setXRot(0.0F);
        this.attackCooldown = 0;
    }

    @Override
    public void tick() {
        final boolean flag = this.inWater;
        if (!flag) {
            FluidState fluidstate = this.dolphin.level().getFluidState(this.dolphin.blockPosition());
            this.inWater = fluidstate.is(FluidTags.WATER);
        }
        if (attackCooldown > 0) {
            attackCooldown--;
        }
        if (this.inWater && !flag) {
            this.dolphin.playSound(SoundEvents.DOLPHIN_JUMP, 1.0F, 1.0F);
        }
        LivingEntity target = this.dolphin.getTarget();
        if (target != null) {
            if (this.dolphin.distanceTo(target) < 3F && attackCooldown <= 0) {
                this.dolphin.onJumpHit(target);
                attackCooldown = 20;
            } else if (this.dolphin.distanceTo(target) < 5F) {
                this.dolphin.setAnimation(EntitySeaSerpent.ANIMATION_BITE);
            }
        }

        Vec3 vector3d = this.dolphin.getDeltaMovement();
        if (vector3d.y * vector3d.y < 0.1F && this.dolphin.getXRot() != 0.0F) {
            this.dolphin.setXRot(Mth.rotLerp(this.dolphin.getXRot(), 0.0F, 0.2F));
        } else {
            final double d0 = vector3d.horizontalDistance();
            final double d1 = Math.signum(-vector3d.y) * Math.acos(d0 / vector3d.length()) * (180F / (float) Math.PI);
            this.dolphin.setXRot((float) d1);
        }

    }
}
