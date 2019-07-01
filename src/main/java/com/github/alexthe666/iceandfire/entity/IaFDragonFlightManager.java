package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.core.ModBlocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nullable;

public class IaFDragonFlightManager {
    private EntityDragonBase dragon;
    private Vec3d target;
    private IaFDragonAttacks.Air prevAirAttack;
    private Vec3d startAttackVec;
    private Vec3d startPreyVec;
    private boolean hasStartedToScorch = false;
    private EntityLivingBase prevAttackTarget = null;
    public IaFDragonFlightManager(EntityDragonBase dragon){
        this.dragon = dragon;
    }

    public void update() {
        if(dragon.getAttackTarget() != null && !dragon.getAttackTarget().isDead){
            EntityLivingBase entity = dragon.getAttackTarget();
            if(dragon.airAttack == IaFDragonAttacks.Air.TACKLE){
                target = new Vec3d(entity.posX, entity.posY + entity.height, entity.posZ);
            }
            if(dragon.airAttack == IaFDragonAttacks.Air.HOVER_BLAST){
                float distY = 5 + dragon.getDragonStage() * 2;
                int randomDist = 20;
                if(dragon.getDistance(entity.posX, dragon.posY, entity.posZ) < 4 || dragon.getDistance(entity.posX, dragon.posY, entity.posZ) > 30){
                    target = new Vec3d(entity.posX + dragon.getRNG().nextInt(randomDist) - randomDist/2, entity.posY + distY, entity.posZ + dragon.getRNG().nextInt(randomDist) - randomDist/2);
                }
                dragon.stimulateFire(entity.posX, entity.posY, entity.posZ, 3);
            }
            if(dragon.airAttack == IaFDragonAttacks.Air.SCORCH_STREAM){
                float distX = (float)(startPreyVec.x - startAttackVec.x);
                float distY = 5 + dragon.getDragonStage() * 2;
                float distZ = (float)(startPreyVec.z - startAttackVec.z);
                target = new Vec3d(entity.posX + distX, entity.posY + distY, entity.posZ + distZ);
                dragon.tryScorchTarget();
                hasStartedToScorch = true;
                if(target != null && dragon.getDistance(target.x, target.y, target.z) < 10){
                    target = new Vec3d(entity.posX - distX, entity.posY + distY, entity.posZ- distZ);
                }
            }

        }else if(target == null || dragon.getDistance(target.x, target.y, target.z) < 2){
            BlockPos viewBlock = DragonUtils.getBlockInView(dragon);
            if(viewBlock != null){
                target = new Vec3d(viewBlock.getX() + 0.5, viewBlock.getY() + 0.5, viewBlock.getZ() + 0.5);
            }
        }
        if(target != null){
            if(target.y >= dragon.posY){
                dragon.motionY += 0.4D;
            }
        }
        this.prevAirAttack = dragon.airAttack;
    }

    public Vec3d getFlightTarget(){
        return target == null ? Vec3d.ZERO : target;
    }

    private float getDistanceXZ(double x, double z)
    {
        float f = (float)(dragon.posX - x);
        float f2 = (float)(dragon.posZ - z);
        return f * f + f2 * f2;
    }

    public void onSetAttackTarget(@Nullable EntityLivingBase entitylivingbaseIn) {
        if(prevAttackTarget != entitylivingbaseIn) {
            if (entitylivingbaseIn != null) {
                startPreyVec = new Vec3d(entitylivingbaseIn.posX, entitylivingbaseIn.posY, entitylivingbaseIn.posZ);
            }
            startAttackVec = new Vec3d(dragon.posX, dragon.posY, dragon.posZ);
        }
        prevAttackTarget = entitylivingbaseIn;
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
                dragon.flightManager.target = null;
                return;
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
            if(dist > 1.0F) {
                float yawCopy = dragon.rotationYaw;
                float atan = (float) MathHelper.atan2((double) distZ, (double) distX);
                float yawTurn = MathHelper.wrapDegrees(dragon.rotationYaw + 90);
                float yawTurnAtan = MathHelper.wrapDegrees(atan * 57.295776F);
                dragon.rotationYaw = IaFDragonFlightManager.approachDegrees(yawTurn, yawTurnAtan, dragon.airAttack == IaFDragonAttacks.Air.TACKLE && dragon.getAttackTarget() != null ? 10 : 4.0F) - 90.0F;
                dragon.renderYawOffset = dragon.rotationYaw;
                if (IaFDragonFlightManager.degreesDifferenceAbs(yawCopy, dragon.rotationYaw) < 3.0F) {
                    speed = IaFDragonFlightManager.approach((float) speed, 1.8F, 0.005F * (1.8F / (float) speed));
                } else {
                    speed = IaFDragonFlightManager.approach((float) speed, 0.2F, 0.025F);
                }
                float finPitch = (float) (-(MathHelper.atan2((double) (-distY), planeDist) * 57.2957763671875D));
                dragon.rotationPitch = finPitch;
                float yawTurnHead = dragon.rotationYaw + 90.0F;
                double lvt_16_1_ = (double) (speed * MathHelper.cos(yawTurnHead * 0.017453292F)) * Math.abs((double) distX / dist);
                double lvt_18_1_ = (double) (speed * MathHelper.sin(yawTurnHead * 0.017453292F)) * Math.abs((double) distZ / dist);
                double lvt_20_1_ = (double) (speed * MathHelper.sin(finPitch * 0.017453292F)) * Math.abs((double) distY / dist);
                dragon.motionX += lvt_16_1_ * 0.2D;
                dragon.motionY += lvt_20_1_ * 0.2D;
                dragon.motionZ += lvt_18_1_ * 0.2D;
            }
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
