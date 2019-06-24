package com.github.alexthe666.iceandfire.entity;

import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class IaFDragonFlightManager {
    private EntityDragonBase dragon;
    private Vec3d target;

    public IaFDragonFlightManager(EntityDragonBase dragon){
        this.dragon = dragon;
    }

    public void update() {
        if(target == null || dragon.getDistance(target.x, target.y, target.z) < 2){
            BlockPos viewBlock = DragonUtils.getBlockInView(dragon);
            if(viewBlock != null){
                target = new Vec3d(viewBlock.getX() + 0.5, viewBlock.getY() + 0.5, viewBlock.getZ() + 0.5);
            }
        }
        if(target != null){
            if(target.y > dragon.posY){
                dragon.motionY += 0.5D;
            }
        }
    }

    public Vec3d getFlightTarget(){
        return target == null ? Vec3d.ZERO : target;
    }

    protected static class FlightMoveHelper extends EntityMoveHelper {

        private EntityDragonBase dragon;

        protected FlightMoveHelper(EntityDragonBase dragonBase) {
            super(dragonBase);
            this.dragon = dragonBase;
        }

        public void onUpdateMoveHelper(){
            if (dragon.collidedHorizontally) {
                dragon.rotationYaw += 180.0F;
                this.speed = 0.1F;
            }
            float distX = (float)(dragon.flightManager.getFlightTarget().x - dragon.posX);
            float distY = (float)(dragon.flightManager.getFlightTarget().y - dragon.posY);
            float distZ = (float)(dragon.flightManager.getFlightTarget().z - dragon.posZ);
            double planeDist = (double) MathHelper.sqrt(distX * distX + distZ * distZ);
            double yDistMod = 1.0D - (double)MathHelper.abs(distY * 0.7F) / planeDist;
            distX = (float)((double)distX * yDistMod);
            distZ = (float)((double)distZ * yDistMod);
            planeDist = (double)MathHelper.sqrt(distX * distX + distZ * distZ);
            double dist = (double)MathHelper.sqrt(distX * distX + distZ * distZ + distY * distY);
            float yawCopy = dragon.rotationYaw;
            float atan = (float)MathHelper.atan2((double)distZ, (double)distX);
            float yawTurn = MathHelper.wrapDegrees(dragon.rotationYaw + 90.0F);
            float yawTurnAtan = MathHelper.wrapDegrees(atan * 57.295776F);
            dragon.rotationYaw = IaFDragonFlightManager.approachDegrees(yawTurn, yawTurnAtan, 4.0F) - 90.0F;
            dragon.renderYawOffset = dragon.rotationYaw;
            if (IaFDragonFlightManager.degreesDifferenceAbs(yawCopy, dragon.rotationYaw) < 3.0F) {
                speed = IaFDragonFlightManager.approach((float)speed, 1.8F, 0.005F * (1.8F / (float)speed));
            } else {
                speed = IaFDragonFlightManager.approach((float)speed, 0.2F, 0.025F);
            }

            float finPitch = (float)(-(MathHelper.atan2((double)(-distY), planeDist) * 57.2957763671875D));
            dragon.rotationPitch = finPitch;
            float yawTurnHead = dragon.rotationYaw + 90.0F;
            double lvt_16_1_ = (double)(speed * MathHelper.cos(yawTurnHead * 0.017453292F)) * Math.abs((double)distX / dist);
            double lvt_18_1_ = (double)(speed * MathHelper.sin(yawTurnHead * 0.017453292F)) * Math.abs((double)distZ / dist);
            double lvt_20_1_ = (double)(speed * MathHelper.sin(finPitch * 0.017453292F)) * Math.abs((double)distY / dist);
            //Vec3d lvt_22_1_ = dragon.getMotion();
            //dragon.setMotion(lvt_22_1_.add((new Vec3d(lvt_16_1_, lvt_20_1_, lvt_18_1_)).subtract(lvt_22_1_).scale(0.2D)));
            dragon.motionX += lvt_16_1_ * 0.2D;
            dragon.motionY += lvt_20_1_ * 0.2D;
            dragon.motionZ += lvt_18_1_ * 0.2D;
        }

    }

    public static float approach(float number, float max, float min) {
        min = Math.abs(min);
        return number < max ? MathHelper.clamp(number + min, number, max) : MathHelper.clamp(number - min, max, number);
    }

    public static float approachDegrees(float number, float max, float min) {
        float add = MathHelper.wrapDegrees(max - number);
        return approach(number, number + add, min);
    }

    public static float degreesDifferenceAbs(float f1, float f2) {
        return Math.abs(MathHelper.wrapDegrees(f2 - f1));
    }


}
