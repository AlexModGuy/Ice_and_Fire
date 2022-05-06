package com.github.alexthe666.iceandfire.entity.ai;

import java.util.EnumSet;

import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.iceandfire.entity.EntityGhost;
import com.github.alexthe666.iceandfire.entity.util.DragonUtils;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;

public class GhostAICharge extends Goal {

    private EntityGhost ghost;
    public boolean firstPhase = true;
    public Vector3d moveToPos = null;
    public Vector3d offsetOf = Vector3d.ZERO;

    public GhostAICharge(EntityGhost ghost) {
        this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
        this.ghost = ghost;
    }

    @Override
    public boolean shouldExecute() {
        return ghost.getAttackTarget() != null && !ghost.isCharging();
    }

    @Override
    public boolean shouldContinueExecuting() {
        return ghost.getAttackTarget() != null && ghost.getAttackTarget().isAlive();
    }

    @Override
    public void startExecuting() {
        ghost.setCharging(true);
    }

    @Override
    public void resetTask() {
        firstPhase = true;
        this.moveToPos = null;
        ghost.setCharging(false);
    }

    @Override
    public void tick() {
        LivingEntity target = ghost.getAttackTarget();
        if (target != null) {
            if (this.ghost.getAnimation() == IAnimatedEntity.NO_ANIMATION && this.ghost.getDistance(target) < 1.4D) {
                this.ghost.setAnimation(EntityGhost.ANIMATION_HIT);
            }
            if (firstPhase) {
                if (this.moveToPos == null) {
                    BlockPos moveToPos = DragonUtils.getBlockInTargetsViewGhost(ghost, target);
                    this.moveToPos = Vector3d.copyCentered(moveToPos);
                } else {
                    this.ghost.getNavigator().tryMoveToXYZ(this.moveToPos.x + 0.5D, this.moveToPos.y + 0.5D,
                        this.moveToPos.z + 0.5D, 1F);
                    if (this.ghost.getDistanceSq(this.moveToPos.add(0.5D, 0.5D, 0.5D)) < 9D) {
                        if (this.ghost.getAnimation() == IAnimatedEntity.NO_ANIMATION) {
                            this.ghost.setAnimation(EntityGhost.ANIMATION_SCARE);
                        }
                        this.firstPhase = false;
                        this.moveToPos = null;
                        offsetOf = target.getPositionVec().subtract(this.ghost.getPositionVec()).normalize();
                    }
                }
            } else {
                Vector3d fin = target.getPositionVec();
                this.moveToPos = new Vector3d(fin.x, target.getPosY() + target.getEyeHeight() / 2, fin.z);
                this.ghost.getNavigator().tryMoveToEntityLiving(target, 1.2F);
                if (this.ghost.getDistanceSq(this.moveToPos.add(0.5D, 0.5D, 0.5D)) < 3D) {
                    this.resetTask();
                }
            }
        }

    }
}
