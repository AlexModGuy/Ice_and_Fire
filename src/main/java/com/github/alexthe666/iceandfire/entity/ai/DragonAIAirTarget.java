package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.Utils;
import com.github.alexthe666.iceandfire.entity.DragonUtils;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class DragonAIAirTarget extends EntityAIBase {
    private EntityDragonBase dragon;

    public DragonAIAirTarget(EntityDragonBase dragon) {
        this.dragon = dragon;
    }

    public boolean shouldExecute() {
        if (dragon != null) {
            if (!dragon.isFlying() && !dragon.isHovering() || dragon.onGround) {
                return false;
            }
            if (dragon.isSleeping()) {
                return false;
            }
            if (dragon.isChild()) {
                return false;
            }
            if (dragon.getControllingPassenger() != null) {
                return false;
            }
            if (dragon.airTarget != null && (dragon.isTargetBlocked(new Vec3d(dragon.airTarget)))) {
                dragon.airTarget = null;
            }

            if (dragon.airTarget != null) {
                return false;
            } else {
                dragon.airTarget = this.findAirTarget();
                return dragon.airTarget != null;
            }
        }
        return false;
    }

    @Override
    public boolean shouldContinueExecuting() {
        if (!dragon.isFlying() && !dragon.isHovering()) {
            return false;
        }
        if (dragon.isSleeping()) {
            return false;
        }
        if (dragon.isChild()) {
            return false;
        }
        if (dragon.isStoned()) {
            return false;
        }
        return dragon.airTarget != null;
    }

    private BlockPos findAirTarget() {
        Entity entity = dragon.getAttackTarget();
        if (entity == null || Utils.isEntityDead(entity)) {
            BlockPos pos = DragonUtils.getBlockInView(dragon);
            if (pos != null && dragon.isTargetInAir(pos)) {
                return pos;
            }
        } else {
            return new BlockPos((int) entity.posX, (int) entity.posY, (int) entity.posZ);
        }
        return null;
    }
}