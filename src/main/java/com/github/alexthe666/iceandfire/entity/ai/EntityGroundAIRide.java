package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityDeathWorm;
import com.github.alexthe666.iceandfire.entity.util.IGroundMount;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class EntityGroundAIRide<T extends Mob & IGroundMount> extends Goal {

    private final T dragon;
    private Player player;

    public EntityGroundAIRide(T dragon) {
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        this.dragon = dragon;
    }

    @Override
    public boolean canUse() {
        player = dragon.getRidingPlayer();

        return player != null;
    }

    @Override
    public void start() {
        dragon.getNavigation().stop();
    }

    @Override
    public void tick() {
        dragon.getNavigation().stop();
        dragon.setTarget(null);
        double x = dragon.getX();
        double y = dragon.getY();
        if (dragon instanceof EntityDeathWorm) {
            y = ((EntityDeathWorm) dragon).processRiderY(y);
        }
        double z = dragon.getZ();
        double speed = 1.8F * dragon.getRideSpeedModifier();
        if (player.xxa != 0 || player.zza != 0) {
            Vec3 lookVec = player.getLookAngle();
            if (player.zza < 0) {
                lookVec = lookVec.yRot((float) Math.PI);
            } else if (player.xxa > 0) {
                lookVec = lookVec.yRot((float) Math.PI * 0.5f);
            } else if (player.xxa < 0) {
                lookVec = lookVec.yRot((float) Math.PI * -0.5f);
            }
            if (Math.abs(player.xxa) > 0.0) {
                speed *= 0.25D;
            }
            if (player.zza < 0.0) {
                speed *= 0.15D;
            }
            x += lookVec.x * 10;
            z += lookVec.z * 10;
        }
        dragon.getMoveControl().setWantedPosition(x, y, z, speed);
    }
}
