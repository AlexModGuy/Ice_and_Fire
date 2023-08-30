package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityDeathWorm;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class DeathWormAIAttack extends Goal {
    private final EntityDeathWorm worm;
    private int jumpCooldown = 0;

    public DeathWormAIAttack(EntityDeathWorm worm) {
        this.worm = worm;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (jumpCooldown > 0) {
            jumpCooldown--;
        }
        return !(this.worm.getTarget() == null || worm.isVehicle()
            || !worm.onGround() && !worm.isInSandStrict() || jumpCooldown > 0);
    }

    @Override
    public boolean canContinueToUse() {

        return worm.getTarget() != null && worm.getTarget().isAlive();
    }

    @Override
    public boolean isInterruptable() {
        return false;
    }

    @Override
    public void start() {
        LivingEntity target = this.worm.getTarget();
        if (target != null) {
            if (worm.isInSand()) {
                BlockPos topSand = worm.blockPosition();
                while (worm.level().getBlockState(topSand.above()).is(BlockTags.SAND)) {
                    topSand = topSand.above();
                }
                worm.setPos(worm.getX(), topSand.getY() + 0.5F, worm.getZ());
            }
            if (shouldJump()) {
                jumpAttack();
            } else {
                worm.getNavigation().moveTo(target, 1.0F);
            }

        }
    }

    public boolean shouldJump() {
        LivingEntity target = this.worm.getTarget();
        if (target != null) {
            final double distanceXZ = worm.distanceToSqr(target.getX(), worm.getY(), target.getZ());
            final float distanceXZSqrt = (float) Math.sqrt(distanceXZ);
            double d0 = this.worm.getDeltaMovement().y;
            if (distanceXZSqrt < 12 && distanceXZSqrt > 2) {
                return jumpCooldown <= 0
                    && (d0 * d0 >= 0.03F || this.worm.getXRot() == 0.0F || Math.abs(this.worm.getXRot()) >= 10.0F
                    || !this.worm.isInWater())
                    && !this.worm.onGround();
            }
        }
        return false;
    }

    public void jumpAttack() {
        LivingEntity target = this.worm.getTarget();
        if (target == null)
            return;
        worm.lookAt(target, 260, 30);
        final double smoothX = Mth.clamp(Math.abs(target.getX() - worm.getX()), 0, 1);
        //MathHelper.clamp(Math.abs(target.getPosY() - worm.getPosY()), 0, 1);
        final double smoothZ = Mth.clamp(Math.abs(target.getZ() - worm.getZ()), 0, 1);
        final double d0 = (target.getX() - this.worm.getX()) * 0.2 * smoothX;
        //Math.signum(target.getPosY() - this.worm.getPosY());
        final double d2 = (target.getZ() - this.worm.getZ()) * 0.2 * smoothZ;
        final float up = (worm.getScale() > 3 ? 0.8F : 0.5F) + worm.getRandom().nextFloat() * 0.5F;
        this.worm.setDeltaMovement(this.worm.getDeltaMovement().add(d0 * 0.3D, up, d2 * 0.3D));
        this.worm.getNavigation().stop();
        this.worm.setWormJumping(20);
        this.jumpCooldown = worm.getRandom().nextInt(32) + 64;
    }

    @Override
    public void stop() {
        this.worm.setXRot(0.0F);
    }

    @Override
    public void tick() {
        if (jumpCooldown > 0) {
            jumpCooldown--;
        }
        LivingEntity target = this.worm.getTarget();
        if (target != null && this.worm.hasLineOfSight(target)) {
            if (this.worm.distanceTo(target) < 3F) {
                this.worm.doHurtTarget(target);
            }
        }

        Vec3 vector3d = this.worm.getDeltaMovement();
        if (vector3d.y * vector3d.y < 0.1F && this.worm.getXRot() != 0.0F) {
            this.worm.setXRot(Mth.rotLerp(this.worm.getXRot(), 0.0F, 0.2F));
        } else {
            final double d0 = vector3d.horizontalDistance();
            final double d1 = Math.signum(-vector3d.y) * Math.acos(d0 / vector3d.length()) * (180F / (float) Math.PI);
            this.worm.setXRot((float) d1);
        }
        if (shouldJump())
            jumpAttack();
        else if (worm.getNavigation().isDone()) {
            worm.getNavigation().moveTo(target, 1.0F);
        }
    }
}
