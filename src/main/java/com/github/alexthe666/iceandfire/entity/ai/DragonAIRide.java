package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.util.IFlyingMount;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class DragonAIRide<T extends Mob & IFlyingMount> extends Goal {

    private final T dragon;
    private Player player;

    public DragonAIRide(T dragon) {
        this.dragon = dragon;
        this.setFlags(EnumSet.of(Flag.MOVE));
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
        double speed = 1.8F * dragon.getFlightSpeedModifier();
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
        if (dragon.isGoingUp()) {
            lookVec = lookVec.add(0, 1, 0);
        } else if (dragon.isGoingDown()) {
            lookVec = lookVec.add(0, -1, 0);
        }
        if (player.xxa != 0 || player.zza != 0 || (dragon.fliesLikeElytra())) {
            x += lookVec.x * 10;
            z += lookVec.z * 10;
        }
        if ((dragon.isFlying() || hovering()) && (dragon.fliesLikeElytra() || dragon.isGoingUp() || dragon.isGoingDown())) {
            y += lookVec.y * dragon.getYSpeedMod();
        }
        if (dragon.fliesLikeElytra() && lookVec.y == -1 || !(dragon.isFlying() || hovering()) && !dragon.onGround()) {
            y -= 1;
        }
        dragon.getMoveControl().setWantedPosition(x, y, z, speed);
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    private boolean hovering() {
        return dragon.isHovering() || dragon instanceof EntityDragonBase && ((EntityDragonBase) dragon).useFlyingPathFinder();
    }
}
