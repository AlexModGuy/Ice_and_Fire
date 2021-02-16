package com.github.alexthe666.iceandfire.entity.ai;

import java.util.Random;

import com.github.alexthe666.citadel.server.entity.EntityPropertiesHandler;
import com.github.alexthe666.iceandfire.entity.EntityHippogryph;
import com.github.alexthe666.iceandfire.entity.util.DragonUtils;

import net.minecraft.block.material.Material;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;

public class HippogryphAIAirTarget extends Goal {
    private EntityHippogryph hippogryph;

    public HippogryphAIAirTarget(EntityHippogryph dragon) {
        this.hippogryph = dragon;
    }

    public boolean shouldExecute() {
        if (hippogryph != null) {
            if (!hippogryph.isFlying() && !hippogryph.isHovering()) {
                return false;
            }
            if (hippogryph.isSitting()) {
                return false;
            }
            if (hippogryph.isChild()) {
                return false;
            }
            if (hippogryph.getOwner() != null && hippogryph.getPassengers().contains(hippogryph.getOwner())) {
                return false;
            }
            if (hippogryph.airTarget != null && hippogryph.getDistanceSquared(new Vector3d(hippogryph.airTarget.getX(), hippogryph.getPosY(), hippogryph.airTarget.getZ())) > 3) {
                hippogryph.airTarget = null;
            }

            if (hippogryph.airTarget != null) {
                return false;
            } else {
                Vector3d vec = this.findAirTarget();

                if (vec == null) {
                    return false;
                } else {
                    hippogryph.airTarget = new BlockPos(vec.x, vec.y, vec.z);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean continueExecuting() {
        if (!hippogryph.isFlying() && !hippogryph.isHovering()) {
            return false;
        }
        if (hippogryph.isSitting()) {
            return false;
        }
        if (hippogryph.isChild()) {
            return false;
        }
        if (hippogryph.isChild()) {
            return false;
        }
        return hippogryph.airTarget != null;
    }

    public Vector3d findAirTarget() {
        return Vector3d.copyCentered(getNearbyAirTarget());
    }

    public BlockPos getNearbyAirTarget() {
        Random random = this.hippogryph.getRNG();

        if (hippogryph.getAttackTarget() == null) {
            for (int i = 0; i < 10; i++) {
                BlockPos pos = DragonUtils.getBlockInViewHippogryph(hippogryph);
                if (pos != null && hippogryph.world.getBlockState(pos).getMaterial() == Material.AIR) {
                    return pos;
                }
            }
        } else {
            BlockPos pos = new BlockPos((int) hippogryph.getAttackTarget().getPosX(), (int) hippogryph.getAttackTarget().getPosY(), (int) hippogryph.getAttackTarget().getPosZ());
            if (hippogryph.world.getBlockState(pos).getMaterial() == Material.AIR) {
                return pos;
            }
        }
        return hippogryph.getPosition();
    }

}