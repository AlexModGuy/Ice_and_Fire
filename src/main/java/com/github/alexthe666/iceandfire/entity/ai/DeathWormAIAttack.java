package com.github.alexthe666.iceandfire.entity.ai;

import java.util.EnumSet;

import com.github.alexthe666.iceandfire.entity.EntityDeathWorm;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;

public class DeathWormAIAttack extends Goal {
    private EntityDeathWorm worm;
    private int jumpCooldown = 0;

    public DeathWormAIAttack(EntityDeathWorm worm) {
        this.worm = worm;
        this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean shouldExecute() {
        if (jumpCooldown > 0) {
            jumpCooldown--;
        }
        return !(this.worm.getAttackTarget() == null || worm.isBeingRidden()
            || !worm.isOnGround() && !worm.isInSandStrict() || jumpCooldown > 0);
    }

    @Override
    public boolean shouldContinueExecuting() {

        return worm.getAttackTarget() != null && worm.getAttackTarget().isAlive();
    }

    @Override
    public boolean isPreemptible() {
        return false;
    }

    @Override
    public void startExecuting() {
        LivingEntity target = this.worm.getAttackTarget();
        if (target != null) {
            if(worm.isInSand()){
                BlockPos topSand = worm.getPosition();
                while(worm.world.getBlockState(topSand.up()).isIn(BlockTags.SAND)){
                    topSand = topSand.up();
                }
                worm.setPosition(worm.getPosX(), topSand.getY() + 0.5F, worm.getPosZ());
            }
            if (shouldJump()) {
                jumpAttack();
            } else {
                worm.getNavigator().tryMoveToEntityLiving(target, 1.0F);
            }

        }
    }

    public boolean shouldJump() {
        LivingEntity target = this.worm.getAttackTarget();
        if (target != null) {
            final double distanceXZ = worm.getDistanceSq(target.getPosX(), worm.getPosY(), target.getPosZ());
            final float distanceXZSqrt = (float) Math.sqrt(distanceXZ);
            double d0 = this.worm.getMotion().y;
            if (distanceXZSqrt < 12 && distanceXZSqrt > 2) {
                return jumpCooldown <= 0
                    && (d0 * d0 >= 0.03F || this.worm.rotationPitch == 0.0F || Math.abs(this.worm.rotationPitch) >= 10.0F
                    || !this.worm.isInWater())
                    && !this.worm.isOnGround();
            }
        }
        return false;
    }

    public void jumpAttack() {
        LivingEntity target = this.worm.getAttackTarget();
        if (target == null)
            return;
        worm.faceEntity(target, 260, 30);
        final double smoothX = MathHelper.clamp(Math.abs(target.getPosX() - worm.getPosX()), 0, 1);
        //MathHelper.clamp(Math.abs(target.getPosY() - worm.getPosY()), 0, 1);
        final double smoothZ = MathHelper.clamp(Math.abs(target.getPosZ() - worm.getPosZ()), 0, 1);
        final double d0 = (target.getPosX() - this.worm.getPosX()) * 0.2 * smoothX;
        //Math.signum(target.getPosY() - this.worm.getPosY());
        final double d2 = (target.getPosZ() - this.worm.getPosZ()) * 0.2 * smoothZ;
        final float up = (worm.getRenderScale() > 3 ? 0.8F : 0.5F) + worm.getRNG().nextFloat() * 0.5F;
        this.worm.setMotion(this.worm.getMotion().add(d0 * 0.3D, up, d2 * 0.3D));
        this.worm.getNavigator().clearPath();
        this.worm.setWormJumping(20);
        this.jumpCooldown = worm.getRNG().nextInt(32) + 64;
    }

    @Override
    public void resetTask() {
        this.worm.rotationPitch = 0.0F;
    }

    @Override
    public void tick() {
        if (jumpCooldown > 0) {
            jumpCooldown--;
        }
        LivingEntity target = this.worm.getAttackTarget();
        if (target != null && this.worm.canEntityBeSeen(target)) {
            if (this.worm.getDistance(target) < 3F) {
                this.worm.attackEntityAsMob(target);
            }
        }

        Vector3d vector3d = this.worm.getMotion();
        if (vector3d.y * vector3d.y < 0.1F && this.worm.rotationPitch != 0.0F) {
            this.worm.rotationPitch = MathHelper.rotLerp(this.worm.rotationPitch, 0.0F, 0.2F);
        } else {
            final double d0 = Math.sqrt(Entity.horizontalMag(vector3d));
            final double d1 = Math.signum(-vector3d.y) * Math.acos(d0 / vector3d.length()) * (180F / (float) Math.PI);
            this.worm.rotationPitch = (float) d1;
        }
        if (shouldJump())
            jumpAttack();
        else if (worm.getNavigator().noPath()){
            worm.getNavigator().tryMoveToEntityLiving(target, 1.0F);
        }
    }
}
