package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.IFlyingMount;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;

public class DragonAIRide<T extends EntityCreature & IFlyingMount> extends EntityAIBase {

    private T dragon;
    private EntityPlayer player;

    public DragonAIRide(T dragon) {
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
        double x = dragon.posX;
        double y = dragon.posY;
        double z = dragon.posZ;
        double speed = 1.8F * dragon.getFlightSpeedModifier();
        Vec3d lookVec = player.getLookVec();

        if (player.moveStrafing != 0 || player.moveForward != 0 || (dragon.fliesLikeElytra())) {
            if (player.moveForward < 0) {
                lookVec = lookVec.rotateYaw((float)Math.PI);
            } else if (player.moveStrafing > 0) {
                lookVec = lookVec.rotateYaw((float)Math.PI * 0.5f);
            } else if (player.moveStrafing < 0) {
                lookVec = lookVec.rotateYaw((float)Math.PI * -0.5f);
            }
            if(dragon.up()){
                lookVec = lookVec.add(0, 1, 0);
            }
            if(dragon.down()){
                lookVec = lookVec.add(0, -1, 0);
            }
            x += lookVec.x * 10;
            if(dragon.isFlying() && (dragon.fliesLikeElytra() || dragon.up() || dragon.down())){
                y += lookVec.y * 10;
            }
            z += lookVec.z * 10;
        }
        dragon.getMoveHelper().setMoveTo(x, y, z, speed);
    }
}
