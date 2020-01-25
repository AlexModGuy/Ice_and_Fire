package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class DragonAIRide extends EntityAIBase {

    private EntityDragonBase dragon;
    private EntityPlayer player;

    public DragonAIRide(EntityDragonBase dragon) {
        this.dragon = dragon;
        this.setMutexBits(0);
    }

    @Override
    public boolean shouldExecute() {
        player = dragon.getRidingPlayer();
        return player != null;
    }

    @Override
    public void startExecuting() {
        dragon.getNavigator().clearPath();
    }

    @Override
    public void updateTask() {
        double x = dragon.posX;
        double y = dragon.posY;
        double z = dragon.posZ;
        double speed = 1.8F * dragon.getFlightSpeedModifier();
        if (player.moveStrafing != 0 || player.moveForward != 0) {
            Vec3d lookVec = player.getLookVec();
            if (player.moveForward < 0) {
                lookVec = lookVec.rotateYaw((float)Math.PI);
            } else if (player.moveStrafing > 0) {
                lookVec = lookVec.rotateYaw((float)Math.PI * 0.5f);
            } else if (player.moveStrafing < 0) {
                lookVec = lookVec.rotateYaw((float)Math.PI * -0.5f);
            }
            x += lookVec.x * 10;
            y += lookVec.y * 10;
            z += lookVec.z * 10;
        }
        dragon.getMoveHelper().setMoveTo(x, y, z, speed);
    }
}
