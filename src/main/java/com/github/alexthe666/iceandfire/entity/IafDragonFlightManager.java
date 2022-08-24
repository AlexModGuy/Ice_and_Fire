package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.entity.util.DragonUtils;
import com.github.alexthe666.iceandfire.entity.util.IFlyingMount;
import com.github.alexthe666.iceandfire.util.IAFMath;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.pathfinding.NodeProcessor;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;

import javax.annotation.Nullable;

public class IafDragonFlightManager {
    private final EntityDragonBase dragon;
    private Vector3d target;
    private IafDragonAttacks.Air prevAirAttack;
    private Vector3d startAttackVec;
    private Vector3d startPreyVec;
    private boolean hasStartedToScorch = false;
    private LivingEntity prevAttackTarget = null;

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

        if (dragon.getTarget() != null && dragon.getTarget().isAlive()) {
            if (dragon instanceof EntityIceDragon && dragon.isInWater()) {
                if (dragon.getTarget() == null) {
                    dragon.airAttack = IafDragonAttacks.Air.SCORCH_STREAM;
                } else {
                    dragon.airAttack = IafDragonAttacks.Air.TACKLE;
                }
            }
            LivingEntity entity = dragon.getTarget();
            if (dragon.airAttack == IafDragonAttacks.Air.TACKLE) {
                target = new Vector3d(entity.getX(), entity.getY() + entity.getBbHeight(), entity.getZ());
            }
            if (dragon.airAttack == IafDragonAttacks.Air.HOVER_BLAST) {
                float distY = 5 + dragon.getDragonStage() * 2;
                int randomDist = 20;
                if (dragon.distanceToSqr(entity.getX(), dragon.getY(), entity.getZ()) < 16 || dragon.distanceToSqr(entity.getX(), dragon.getY(), entity.getZ()) > 900) {
                    target = new Vector3d(entity.getX() + dragon.getRandom().nextInt(randomDist) - randomDist / 2, entity.getY() + distY, entity.getZ() + dragon.getRandom().nextInt(randomDist) - randomDist / 2);
                }
                dragon.stimulateFire(entity.getX(), entity.getY(), entity.getZ(), 3);
            }
            if (dragon.airAttack == IafDragonAttacks.Air.SCORCH_STREAM && startPreyVec != null && startAttackVec != null) {
                float distX = (float) (startPreyVec.x - startAttackVec.x);
                float distY = 5 + dragon.getDragonStage() * 2;
                float distZ = (float) (startPreyVec.z - startAttackVec.z);
                target = new Vector3d(entity.getX() + distX, entity.getY() + distY, entity.getZ() + distZ);
                dragon.tryScorchTarget();
                hasStartedToScorch = true;
                if (target != null && dragon.distanceToSqr(target.x, target.y, target.z) < 100) {
                    target = new Vector3d(entity.getX() - distX, entity.getY() + distY, entity.getZ() - distZ);
                }
            }

        } else if (target == null || dragon.distanceToSqr(target.x, target.y, target.z) < 4 || !dragon.level.isEmptyBlock(new BlockPos(target)) && (dragon.isHovering() || dragon.isFlying()) || dragon.getCommand() == 2 && dragon.shouldTPtoOwner()) {
            BlockPos viewBlock = null;

            if (dragon instanceof EntityIceDragon && dragon.isInWater()) {
                viewBlock = DragonUtils.getWaterBlockInView(dragon);
            }
            if (dragon.getCommand() == 2 && dragon.isFlying()) {
                viewBlock = DragonUtils.getBlockInViewEscort(dragon);
            } else if (dragon.lookingForRoostAIFlag) {
                double xDist = Math.abs(dragon.getX() - dragon.getRestrictCenter().getX() - 0.5F);
                double zDist = Math.abs(dragon.getZ() - dragon.getRestrictCenter().getZ() - 0.5F);
                double xzDist = Math.sqrt(xDist * xDist + zDist * zDist);
                BlockPos upPos = dragon.getRestrictCenter();
                if (dragon.getDistanceSquared(Vector3d.atCenterOf(dragon.getRestrictCenter())) > 200) {
                    upPos = upPos.above(30);
                }
                viewBlock = upPos;

            }else if(viewBlock == null){
                viewBlock = DragonUtils.getBlockInView(dragon);
            }
            if (viewBlock != null) {
                target = new Vector3d(viewBlock.getX() + 0.5, viewBlock.getY() + 0.5, viewBlock.getZ() + 0.5);
            }
        }
        if (target != null) {
            if (target.y > IafConfig.maxDragonFlight) {
                target = new Vector3d(target.x, IafConfig.maxDragonFlight, target.z);
            }
            if (target.y >= dragon.getY() && !dragon.isModelDead()) {
                dragon.setDeltaMovement(dragon.getDeltaMovement().add(0, 0.1D, 0));

            }
        }

        this.prevAirAttack = dragon.airAttack;
    }

    public Vector3d getFlightTarget() {
        return target == null ? Vector3d.ZERO : target;
    }

    public void setFlightTarget(Vector3d target){
        this.target = target;
    }

    private float getDistanceXZ(double x, double z) {
        float f = (float) (dragon.getX() - x);
        float f2 = (float) (dragon.getZ() - z);
        return f * f + f2 * f2;
    }

    public void onSetAttackTarget(@Nullable LivingEntity LivingEntityIn) {
        if (prevAttackTarget != LivingEntityIn) {
            if (LivingEntityIn != null) {
                startPreyVec = new Vector3d(LivingEntityIn.getX(), LivingEntityIn.getY(), LivingEntityIn.getZ());
            } else {
                startPreyVec = new Vector3d(dragon.getX(), dragon.getY(), dragon.getZ());
            }
            startAttackVec = new Vector3d(dragon.getX(), dragon.getY(), dragon.getZ());
        }
        prevAttackTarget = LivingEntityIn;
    }

    protected static class GroundMoveHelper extends MovementController {
        public GroundMoveHelper(MobEntity LivingEntityIn) {
            super(LivingEntityIn);
        }

        public float distance(float rotateAngleFrom, float rotateAngleTo) {
            return (float) IAFMath.atan2_accurate(MathHelper.sin(rotateAngleTo - rotateAngleFrom), MathHelper.cos(rotateAngleTo - rotateAngleFrom));
        }

        public void tick() {
            if (this.operation == Action.STRAFE) {
                float f = (float) this.mob.getAttribute(Attributes.MOVEMENT_SPEED).getValue();
                float f1 = (float) this.speedModifier * f;
                float f2 = this.strafeForwards;
                float f3 = this.strafeRight;
                float f4 = MathHelper.sqrt(f2 * f2 + f3 * f3);

                if (f4 < 1.0F) {
                    f4 = 1.0F;
                }

                f4 = f1 / f4;
                f2 = f2 * f4;
                f3 = f3 * f4;
                float f5 = MathHelper.sin(this.mob.yRot * 0.017453292F);
                float f6 = MathHelper.cos(this.mob.yRot * 0.017453292F);
                float f7 = f2 * f6 - f3 * f5;
                float f8 = f3 * f6 + f2 * f5;
                PathNavigator pathnavigate = this.mob.getNavigation();
                if (pathnavigate != null) {
                    NodeProcessor nodeprocessor = pathnavigate.getNodeEvaluator();
                    if (nodeprocessor != null && nodeprocessor.getBlockPathType(this.mob.level, MathHelper.floor(this.mob.getX() + (double) f7), MathHelper.floor(this.mob.getY()), MathHelper.floor(this.mob.getZ() + (double) f8)) != PathNodeType.WALKABLE) {
                        this.strafeForwards = 1.0F;
                        this.strafeRight = 0.0F;
                        f1 = f;
                    }
                }
                this.mob.setSpeed(f1);
                this.mob.setZza(this.strafeForwards);
                this.mob.setXxa(this.strafeRight);
                this.operation = Action.WAIT;
            } else if (this.operation == Action.MOVE_TO) {
                this.operation = Action.WAIT;
                EntityDragonBase dragonBase = (EntityDragonBase) mob;
                double d0 = this.getWantedX() - this.mob.getX();
                double d1 = this.getWantedZ() - this.mob.getZ();
                double d2 = this.getWantedY() - this.mob.getY();
                double d3 = d0 * d0 + d2 * d2 + d1 * d1;

                if (d3 < 2.500000277905201E-7D) {
                    this.mob.setZza(0.0F);
                    return;
                }
                float targetDegree = (float) (MathHelper.atan2(d1, d0) * (180D / Math.PI)) - 90.0F;
                float changeRange = 70F;
                if (Math.ceil(dragonBase.getBbWidth()) > 2F) {
                    float ageMod = 1F - Math.min(dragonBase.getAgeInDays(), 125) / 125F;
                    changeRange = 5 + ageMod * 10;
                }
                this.mob.yRot = this.rotlerp(this.mob.yRot, targetDegree, changeRange);
                this.mob.setSpeed((float) (this.speedModifier * this.mob.getAttribute(Attributes.MOVEMENT_SPEED).getValue()));
                if (d2 > (double) this.mob.maxUpStep && d0 * d0 + d1 * d1 < (double) Math.max(1.0F, this.mob.getBbWidth() / 2)) {
                    this.mob.getJumpControl().jump();
                    this.operation = Action.JUMPING;
                }
            } else if (this.operation == Action.JUMPING) {
                this.mob.setSpeed((float) (this.speedModifier * this.mob.getAttribute(Attributes.MOVEMENT_SPEED).getValue()));

                if (this.mob.isOnGround()) {
                    this.operation = Action.WAIT;
                }
            } else {
                this.mob.setZza(0.0F);
            }
        }

    }

    protected static class FlightMoveHelper extends MovementController {

        private final EntityDragonBase dragon;

        protected FlightMoveHelper(EntityDragonBase dragonBase) {
            super(dragonBase);
            this.dragon = dragonBase;
        }

        public void tick() {
            if (dragon.horizontalCollision) {
                dragon.yRot += 180.0F;
                this.speedModifier = 0.1F;
                dragon.flightManager.target = null;
                return;
            }
            float distX = (float) (dragon.flightManager.getFlightTarget().x - dragon.getX());
            float distY = (float) (dragon.flightManager.getFlightTarget().y - dragon.getY());
            float distZ = (float) (dragon.flightManager.getFlightTarget().z - dragon.getZ());
            double planeDist = MathHelper.sqrt(distX * distX + distZ * distZ);
            double yDistMod = 1.0D - (double) MathHelper.abs(distY * 0.7F) / planeDist;
            distX = (float) ((double) distX * yDistMod);
            distZ = (float) ((double) distZ * yDistMod);
            planeDist = MathHelper.sqrt(distX * distX + distZ * distZ);
            double dist = MathHelper.sqrt(distX * distX + distZ * distZ + distY * distY);
            if (dist > 1.0F) {
                float yawCopy = dragon.yRot;
                float atan = (float) MathHelper.atan2(distZ, distX);
                float yawTurn = MathHelper.wrapDegrees(dragon.yRot + 90);
                float yawTurnAtan = MathHelper.wrapDegrees(atan * 57.295776F);
                dragon.yRot = IafDragonFlightManager.approachDegrees(yawTurn, yawTurnAtan, dragon.airAttack == IafDragonAttacks.Air.TACKLE && dragon.getTarget() != null ? 10 : 4.0F) - 90.0F;
                dragon.yBodyRot = dragon.yRot;
                if (IafDragonFlightManager.degreesDifferenceAbs(yawCopy, dragon.yRot) < 3.0F) {
                    speedModifier = IafDragonFlightManager.approach((float) speedModifier, 1.8F, 0.005F * (1.8F / (float) speedModifier));
                } else {
                    speedModifier = IafDragonFlightManager.approach((float) speedModifier, 0.2F, 0.025F);
                    if (dist < 100D && dragon.getTarget() != null) {
                        speedModifier = speedModifier * (dist / 100D);
                    }
                }
                float finPitch = (float) (-(MathHelper.atan2(-distY, planeDist) * 57.2957763671875D));
                dragon.xRot = finPitch;
                float yawTurnHead = dragon.yRot + 90.0F;
                speedModifier *= dragon.getFlightSpeedModifier();
                speedModifier *= Math.min(1, dist / 50 + 0.3);//Make the dragon fly slower when close to target
                double lvt_16_1_ = speedModifier * MathHelper.cos(yawTurnHead * 0.017453292F) * Math.abs((double) distX / dist);
                double lvt_18_1_ = speedModifier * MathHelper.sin(yawTurnHead * 0.017453292F) * Math.abs((double) distZ / dist);
                double lvt_20_1_ = speedModifier * MathHelper.sin(finPitch * 0.017453292F) * Math.abs((double) distY / dist);
                double motionCap = 0.2D;
                dragon.setDeltaMovement(dragon.getDeltaMovement().add(Math.min(lvt_16_1_ * 0.2D, motionCap), Math.min(lvt_20_1_ * 0.2D, motionCap), Math.min(lvt_18_1_ * 0.2D, motionCap)));
            }
        }


    }

    protected static class PlayerFlightMoveHelper<T extends MobEntity & IFlyingMount> extends MovementController {

        private final T dragon;

        public PlayerFlightMoveHelper(T dragon) {
            super(dragon);
            this.dragon = dragon;
        }

        @Override
        public void tick() {
            double flySpeed = speedModifier * speedMod();
            Vector3d dragonVec = dragon.position();
            Vector3d moveVec = new Vector3d(wantedX, wantedY, wantedZ);
            Vector3d normalized = moveVec.subtract(dragonVec).normalize();
            double dist = dragonVec.distanceTo(moveVec);
            dragon.setDeltaMovement(normalized.x * flySpeed, normalized.y * flySpeed, normalized.z * flySpeed);
            if (dist > 2.5E-7) {
                float yaw = (float) Math.toDegrees(Math.PI * 2 - Math.atan2(normalized.x, normalized.y));
                dragon.yRot = rotlerp(dragon.yRot, yaw, 5);
                dragon.setSpeed((float) (speedModifier));
            }
            dragon.move(MoverType.SELF, dragon.getDeltaMovement());
        }

        public double speedMod() {
            return (dragon instanceof EntityAmphithere ? 0.75D : 0.5D) * IafConfig.dragonFlightSpeedMod;
        }
    }
}
