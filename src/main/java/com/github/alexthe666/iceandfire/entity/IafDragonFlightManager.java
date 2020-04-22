package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.util.IAFMath;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.pathfinding.NodeProcessor;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nullable;

public class IafDragonFlightManager {
    private EntityDragonBase dragon;
    private Vec3d target;
    private IafDragonAttacks.Air prevAirAttack;
    private Vec3d startAttackVec;
    private Vec3d startPreyVec;
    private boolean hasStartedToScorch = false;
    private EntityLivingBase prevAttackTarget = null;

    public IafDragonFlightManager(EntityDragonBase dragon) {
        this.dragon = dragon;
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

    public void update() {
        if (dragon.getAttackTarget() != null && !dragon.getAttackTarget().isDead) {
            if (dragon instanceof EntityIceDragon && dragon.isInWater()) {
                if (dragon.getAttackTarget() == null) {
                    dragon.airAttack = IafDragonAttacks.Air.SCORCH_STREAM;
                } else {
                    dragon.airAttack = IafDragonAttacks.Air.TACKLE;
                }
            }
            EntityLivingBase entity = dragon.getAttackTarget();
            if (dragon.airAttack == IafDragonAttacks.Air.TACKLE) {
                target = new Vec3d(entity.posX, entity.posY + entity.height, entity.posZ);
            }
            if (dragon.airAttack == IafDragonAttacks.Air.HOVER_BLAST) {
                float distY = 5 + dragon.getDragonStage() * 2;
                int randomDist = 20;
                if (dragon.getDistance(entity.posX, dragon.posY, entity.posZ) < 4 || dragon.getDistance(entity.posX, dragon.posY, entity.posZ) > 30) {
                    target = new Vec3d(entity.posX + dragon.getRNG().nextInt(randomDist) - randomDist / 2, entity.posY + distY, entity.posZ + dragon.getRNG().nextInt(randomDist) - randomDist / 2);
                }
                dragon.stimulateFire(entity.posX, entity.posY, entity.posZ, 3);
            }
            if (dragon.airAttack == IafDragonAttacks.Air.SCORCH_STREAM && startPreyVec != null && startAttackVec != null) {
                float distX = (float) (startPreyVec.x - startAttackVec.x);
                float distY = 5 + dragon.getDragonStage() * 2;
                float distZ = (float) (startPreyVec.z - startAttackVec.z);
                target = new Vec3d(entity.posX + distX, entity.posY + distY, entity.posZ + distZ);
                dragon.tryScorchTarget();
                hasStartedToScorch = true;
                if (target != null && dragon.getDistance(target.x, target.y, target.z) < 10) {
                    target = new Vec3d(entity.posX - distX, entity.posY + distY, entity.posZ - distZ);
                }
            }

        } else if (target == null || dragon.getDistance(target.x, target.y, target.z) < 2 || !dragon.world.isAirBlock(new BlockPos(target)) && (dragon.isHovering() || dragon.isFlying()) || dragon.getCommand() == 2 && dragon.shouldTPtoOwner()) {
            BlockPos viewBlock = DragonUtils.getBlockInView(dragon);
            if (dragon instanceof EntityIceDragon && !(dragon.isHovering() || dragon.isFlying())) {
                viewBlock = DragonUtils.getWaterBlockInView(dragon);
            }
            if (dragon.getCommand() == 2 && dragon.isFlying()) {
                viewBlock = DragonUtils.getBlockInViewEscort(dragon);
            }
            if (viewBlock != null) {
                target = new Vec3d(viewBlock.getX() + 0.5, viewBlock.getY() + 0.5, viewBlock.getZ() + 0.5);
            }
        }
        if (target != null) {
            if (target.y > IceAndFire.CONFIG.maxDragonFlight) {
                target = new Vec3d(target.x, IceAndFire.CONFIG.maxDragonFlight, target.z);
            }
            if (target.y >= dragon.posY && !dragon.isModelDead()) {
                dragon.motionY += 0.1D;

            }
        }

        this.prevAirAttack = dragon.airAttack;
    }

    public Vec3d getFlightTarget() {
        return target == null ? Vec3d.ZERO : target;
    }

    private float getDistanceXZ(double x, double z) {
        float f = (float) (dragon.posX - x);
        float f2 = (float) (dragon.posZ - z);
        return f * f + f2 * f2;
    }

    public void onSetAttackTarget(@Nullable EntityLivingBase entitylivingbaseIn) {
        if (prevAttackTarget != entitylivingbaseIn) {
            if (entitylivingbaseIn != null) {
                startPreyVec = new Vec3d(entitylivingbaseIn.posX, entitylivingbaseIn.posY, entitylivingbaseIn.posZ);
            } else {
                startPreyVec = new Vec3d(dragon.posX, dragon.posY, dragon.posZ);
            }
            startAttackVec = new Vec3d(dragon.posX, dragon.posY, dragon.posZ);
        }
        prevAttackTarget = entitylivingbaseIn;
    }

    protected static class GroundMoveHelper extends EntityMoveHelper {
        public GroundMoveHelper(EntityLiving entitylivingIn) {
            super(entitylivingIn);
        }

        public float distance(float rotateAngleFrom, float rotateAngleTo) {
            return (float) IAFMath.atan2_accurate(MathHelper.sin(rotateAngleTo - rotateAngleFrom), MathHelper.cos(rotateAngleTo - rotateAngleFrom));
        }

        public void onUpdateMoveHelper() {
            if (this.action == EntityMoveHelper.Action.STRAFE) {
                float f = (float) this.entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue();
                float f1 = (float) this.speed * f;
                float f2 = this.moveForward;
                float f3 = this.moveStrafe;
                float f4 = MathHelper.sqrt(f2 * f2 + f3 * f3);

                if (f4 < 1.0F) {
                    f4 = 1.0F;
                }

                f4 = f1 / f4;
                f2 = f2 * f4;
                f3 = f3 * f4;
                float f5 = MathHelper.sin(this.entity.rotationYaw * 0.017453292F);
                float f6 = MathHelper.cos(this.entity.rotationYaw * 0.017453292F);
                float f7 = f2 * f6 - f3 * f5;
                float f8 = f3 * f6 + f2 * f5;
                PathNavigate pathnavigate = this.entity.getNavigator();
                if (pathnavigate != null) {
                    NodeProcessor nodeprocessor = pathnavigate.getNodeProcessor();
                    if (nodeprocessor != null && nodeprocessor.getPathNodeType(this.entity.world, MathHelper.floor(this.entity.posX + (double) f7), MathHelper.floor(this.entity.posY), MathHelper.floor(this.entity.posZ + (double) f8)) != PathNodeType.WALKABLE) {
                        this.moveForward = 1.0F;
                        this.moveStrafe = 0.0F;
                        f1 = f;
                    }
                }
                this.entity.setAIMoveSpeed(f1);
                this.entity.setMoveForward(this.moveForward);
                this.entity.setMoveStrafing(this.moveStrafe);
                this.action = EntityMoveHelper.Action.WAIT;
            } else if (this.action == EntityMoveHelper.Action.MOVE_TO) {
                this.action = EntityMoveHelper.Action.WAIT;
                EntityDragonBase dragonBase = (EntityDragonBase) entity;
                double d0 = this.posX - this.entity.posX;
                double d1 = this.posZ - this.entity.posZ;
                double d2 = this.posY - this.entity.posY;
                double d3 = d0 * d0 + d2 * d2 + d1 * d1;

                if (d3 < 2.500000277905201E-7D) {
                    this.entity.setMoveForward(0.0F);
                    return;
                }
                float targetDegree = (float) (MathHelper.atan2(d1, d0) * (180D / Math.PI)) - 90.0F;
                float changeRange = 70F;
                if (dragonBase.width > 2F) {
                    float ageMod = 1F - Math.min(dragonBase.getAgeInDays(), 125) / 125F;
                    changeRange = 5 + ageMod * 10;
                }
                this.entity.rotationYaw = this.limitAngle(this.entity.rotationYaw, targetDegree, changeRange);
                this.entity.setAIMoveSpeed((float) (this.speed * this.entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue()));
                if (d2 > (double) this.entity.stepHeight && d0 * d0 + d1 * d1 < (double) Math.max(1.0F, this.entity.width)) {
                    this.entity.getJumpHelper().setJumping();
                    this.action = EntityMoveHelper.Action.JUMPING;
                }
            } else if (this.action == EntityMoveHelper.Action.JUMPING) {
                this.entity.setAIMoveSpeed((float) (this.speed * this.entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue()));

                if (this.entity.onGround) {
                    this.action = EntityMoveHelper.Action.WAIT;
                }
            } else {
                this.entity.setMoveForward(0.0F);
            }
        }

    }

    protected static class FlightMoveHelper extends EntityMoveHelper {

        private EntityDragonBase dragon;

        protected FlightMoveHelper(EntityDragonBase dragonBase) {
            super(dragonBase);
            this.dragon = dragonBase;
        }

        public void onUpdateMoveHelper() {
            if (dragon.collidedHorizontally) {
                dragon.rotationYaw += 180.0F;
                this.speed = 0.1F;
                dragon.flightManager.target = null;
                return;
            }
            float distX = (float) (dragon.flightManager.getFlightTarget().x - dragon.posX);
            float distY = (float) (dragon.flightManager.getFlightTarget().y - dragon.posY);
            float distZ = (float) (dragon.flightManager.getFlightTarget().z - dragon.posZ);
            double planeDist = (double) MathHelper.sqrt(distX * distX + distZ * distZ);
            double yDistMod = 1.0D - (double) MathHelper.abs(distY * 0.7F) / planeDist;
            distX = (float) ((double) distX * yDistMod);
            distZ = (float) ((double) distZ * yDistMod);
            planeDist = (double) MathHelper.sqrt(distX * distX + distZ * distZ);
            double dist = (double) MathHelper.sqrt(distX * distX + distZ * distZ + distY * distY);
            if (dist > 1.0F) {
                float yawCopy = dragon.rotationYaw;
                float atan = (float) MathHelper.atan2((double) distZ, (double) distX);
                float yawTurn = MathHelper.wrapDegrees(dragon.rotationYaw + 90);
                float yawTurnAtan = MathHelper.wrapDegrees(atan * 57.295776F);
                dragon.rotationYaw = IafDragonFlightManager.approachDegrees(yawTurn, yawTurnAtan, dragon.airAttack == IafDragonAttacks.Air.TACKLE && dragon.getAttackTarget() != null ? 10 : 4.0F) - 90.0F;
                dragon.renderYawOffset = dragon.rotationYaw;
                if (IafDragonFlightManager.degreesDifferenceAbs(yawCopy, dragon.rotationYaw) < 3.0F) {
                    speed = IafDragonFlightManager.approach((float) speed, 1.8F, 0.005F * (1.8F / (float) speed));
                } else {
                    speed = IafDragonFlightManager.approach((float) speed, 0.2F, 0.025F);
                    if (dist < 100D && dragon.getAttackTarget() != null) {
                        speed = speed * (dist / 100D);
                    }
                }
                float finPitch = (float) (-(MathHelper.atan2((double) (-distY), planeDist) * 57.2957763671875D));
                dragon.rotationPitch = finPitch;
                float yawTurnHead = dragon.rotationYaw + 90.0F;
                speed *= dragon.getFlightSpeedModifier();
                double lvt_16_1_ = speed * MathHelper.cos(yawTurnHead * 0.017453292F) * Math.abs((double) distX / dist);
                double lvt_18_1_ = speed * MathHelper.sin(yawTurnHead * 0.017453292F) * Math.abs((double) distZ / dist);
                double lvt_20_1_ = speed * MathHelper.sin(finPitch * 0.017453292F) * Math.abs((double) distY / dist);
                dragon.motionX += lvt_16_1_ * 0.2D;
                dragon.motionY += lvt_20_1_ * 0.2D;
                dragon.motionZ += lvt_18_1_ * 0.2D;
            }
        }


    }

    protected static class PlayerFlightMoveHelper<T extends EntityCreature & IFlyingMount> extends EntityMoveHelper {

        private T dragon;

        public PlayerFlightMoveHelper(T dragon) {
            super(dragon);
            this.dragon = dragon;
        }

        @Override
        public void onUpdateMoveHelper() {
            double flySpeed = speed * speedMod();
            Vec3d dragonVec = dragon.getPositionVector();
            Vec3d moveVec = new Vec3d(posX, posY, posZ);
            Vec3d normalized = moveVec.subtract(dragonVec).normalize();
            double dist = dragonVec.distanceTo(moveVec);
            dragon.motionX = normalized.x * flySpeed;
            dragon.motionY = normalized.y * flySpeed;
            dragon.motionZ = normalized.z * flySpeed;
            if (dist > 2.5E-7) {
                float yaw = (float) Math.toDegrees(Math.PI * 2 - Math.atan2(normalized.x, normalized.y));
                dragon.rotationYaw = limitAngle(dragon.rotationYaw, yaw, 5);
                entity.setAIMoveSpeed((float)(speed));
            }
            dragon.move(MoverType.SELF, dragon.motionX, dragon.motionY, dragon.motionZ);
        }

        public double speedMod(){
            return dragon instanceof EntityAmphithere ? 0.75D : 0.5D;
        }
    }
}
