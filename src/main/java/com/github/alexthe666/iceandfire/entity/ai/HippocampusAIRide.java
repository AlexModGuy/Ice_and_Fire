package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityHippocampus;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class HippocampusAIRide extends Goal {

    private final EntityHippocampus dragon;
    private Player player;

    public HippocampusAIRide(EntityHippocampus dragon) {
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
            if (dragon.isInWater()) {
                y += lookVec.y * 10;
            }
            z += lookVec.z * 10;
        }
        dragon.getMoveControl().setWantedPosition(x, y, z, speed);
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }
}
