package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityStymphalianBird;
import com.github.alexthe666.iceandfire.entity.util.DragonUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.Vec3;

public class StymphalianBirdAIAirTarget extends Goal {
    private final EntityStymphalianBird bird;

    public StymphalianBirdAIAirTarget(EntityStymphalianBird bird) {
        this.bird = bird;
    }

    public static BlockPos getNearbyAirTarget(EntityStymphalianBird bird) {
        if (bird.getTarget() == null) {
            BlockPos pos = DragonUtils.getBlockInViewStymphalian(bird);
            if (pos != null && bird.level.getBlockState(pos).getMaterial() == Material.AIR) {
                return pos;
            }
            if (bird.flock != null && bird.flock.isLeader(bird)) {
                bird.flock.setTarget(bird.airTarget);
            }
        } else {
            return new BlockPos((int) bird.getTarget().getX(), (int) bird.getTarget().getY() + bird.getTarget().getEyeHeight(), (int) bird.getTarget().getZ());
        }
        return bird.blockPosition();
    }

    @Override
    public boolean canUse() {
        if (bird != null) {
            if (!bird.isFlying()) {
                return false;
            }
            if (bird.isBaby() || bird.doesWantToLand()) {
                return false;
            }
            if (bird.airTarget != null && (bird.isTargetBlocked(Vec3.atCenterOf(bird.airTarget)))) {
                bird.airTarget = null;
            }

            if (bird.airTarget != null) {
                return false;
            } else {
                Vec3 vec = this.findAirTarget();

                if (vec == null) {
                    return false;
                } else {
                    bird.airTarget = new BlockPos(vec.x, vec.y, vec.z);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean canContinueToUse() {
        if (!bird.isFlying()) {
            return false;
        }
        if (bird.isBaby()) {
            return false;
        }
        return bird.airTarget != null;
    }

    public Vec3 findAirTarget() {
        return Vec3.atCenterOf(getNearbyAirTarget(bird));
    }
}