package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityHippocampus;
import com.github.alexthe666.iceandfire.entity.IFlyingMount;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

public class HippocampusAIRide extends EntityAIBase {

    private EntityHippocampus dragon;
    private PlayerEntity player;

    public HippocampusAIRide(EntityHippocampus dragon) {
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
        dragon.getNavigator().clearPath();
        dragon.setAttackTarget(null);
        double x = dragon.getPosX();
        double y = dragon.getPosY();
        double z = dragon.getPosZ();
        double speed = 1.8F * dragon.getRideSpeedModifier();
        if (player.moveStrafing != 0 || player.moveForward != 0) {
            Vec3d lookVec = player.getLookVec();
            if (player.moveForward < 0) {
                lookVec = lookVec.rotateYaw((float)Math.PI);
            } else if (player.moveStrafing > 0) {
                lookVec = lookVec.rotateYaw((float)Math.PI * 0.5f);
            } else if (player.moveStrafing < 0) {
                lookVec = lookVec.rotateYaw((float)Math.PI * -0.5f);
            }
            if(Math.abs(player.moveStrafing) > 0.0){
                speed *= 0.25D;
            }
            if(player.moveForward < 0.0){
                speed *= 0.15D;
            }
            x += lookVec.x * 10;
            if(dragon.isInWater()){
                y += lookVec.y * 10;
            }
            z += lookVec.z * 10;
        }
        dragon.getMoveHelper().setMoveTo(x, y, z, speed);
    }
}
