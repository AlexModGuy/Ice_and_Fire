package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.entity.util.DragonUtils;
import com.github.alexthe666.iceandfire.entity.util.IFlyingMount;
import com.github.alexthe666.iceandfire.util.IAFMath;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.NodeEvaluator;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;


public class IafDragonFlightManager {
    private final EntityDragonBase dragon;
    private Vec3 target;
    private IafDragonAttacks.Air prevAirAttack;
    private Vec3 startAttackVec;
    private Vec3 startPreyVec;
    private boolean hasStartedToScorch = false;
    private LivingEntity prevAttackTarget = null;

    public IafDragonFlightManager(EntityDragonBase dragon) {
        this.dragon = dragon;
    }

    public static float approach(float number, float max, float min) {
        min = Math.abs(min);
        return number < max ? Mth.clamp(number + min, number, max) : Mth.clamp(number - min, max, number);
    }

    public static float approachDegrees(float number, float max, float min) {
        float add = Mth.wrapDegrees(max - number);
        return approach(number, number + add, min);
    }

    public static float degreesDifferenceAbs(float f1, float f2) {
        return Math.abs(Mth.wrapDegrees(f2 - f1));
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
                target = new Vec3(entity.getX(), entity.getY() + entity.getBbHeight(), entity.getZ());
            }
            if (dragon.airAttack == IafDragonAttacks.Air.HOVER_BLAST) {
                float distY = 5 + dragon.getDragonStage() * 2;
                int randomDist = 20;
                if (dragon.distanceToSqr(entity.getX(), dragon.getY(), entity.getZ()) < 16 || dragon.distanceToSqr(entity.getX(), dragon.getY(), entity.getZ()) > 900) {
                    target = new Vec3(entity.getX() + dragon.getRandom().nextInt(randomDist) - randomDist / 2, entity.getY() + distY, entity.getZ() + dragon.getRandom().nextInt(randomDist) - randomDist / 2);
                }
                dragon.stimulateFire(entity.getX(), entity.getY(), entity.getZ(), 3);
            }
            if (dragon.airAttack == IafDragonAttacks.Air.SCORCH_STREAM && startPreyVec != null && startAttackVec != null) {
                float distX = (float) (startPreyVec.x - startAttackVec.x);
                float distY = 5 + dragon.getDragonStage() * 2;
                float distZ = (float) (startPreyVec.z - startAttackVec.z);
                target = new Vec3(entity.getX() + distX, entity.getY() + distY, entity.getZ() + distZ);
                dragon.tryScorchTarget();
                hasStartedToScorch = true;
                if (target != null && dragon.distanceToSqr(target.x, target.y, target.z) < 100) {
                    target = new Vec3(entity.getX() - distX, entity.getY() + distY, entity.getZ() - distZ);
                }
            }

        } else if (target == null || dragon.distanceToSqr(target.x, target.y, target.z) < 4
                || !dragon.level().isEmptyBlock(BlockPos.containing(target.x, target.y, target.z))
                        && (dragon.isHovering() || dragon.isFlying())
                || dragon.getCommand() == 2 && dragon.shouldTPtoOwner()) {
            BlockPos viewBlock = null;

            if (dragon instanceof EntityIceDragon && dragon.isInWater()) {
                viewBlock = DragonUtils.getWaterBlockInView(dragon);
            }
            if (dragon.getCommand() == 2 && dragon.useFlyingPathFinder()) {
                if (dragon instanceof EntityIceDragon && dragon.isInWater()) {
                    viewBlock = DragonUtils.getWaterBlockInViewEscort(dragon);
                } else {
                    viewBlock = DragonUtils.getBlockInViewEscort(dragon);
                }
            } else if (dragon.lookingForRoostAIFlag) {
                // FIXME :: Unused
//                double xDist = Math.abs(dragon.getX() - dragon.getRestrictCenter().getX() - 0.5F);
//                double zDist = Math.abs(dragon.getZ() - dragon.getRestrictCenter().getZ() - 0.5F);
//                double xzDist = Math.sqrt(xDist * xDist + zDist * zDist);
                BlockPos upPos = dragon.getRestrictCenter();
                if (dragon.getDistanceSquared(Vec3.atCenterOf(dragon.getRestrictCenter())) > 200) {
                    upPos = upPos.above(30);
                }
                viewBlock = upPos;

            }else if(viewBlock == null){
                viewBlock = DragonUtils.getBlockInView(dragon);
                if (dragon.isInWater()) {
                    // If the dragon is in water, take off to reach the air target
                    dragon.setHovering(true);
                }
            }
            if (viewBlock != null) {
                target = new Vec3(viewBlock.getX() + 0.5, viewBlock.getY() + 0.5, viewBlock.getZ() + 0.5);
            }
        }
        if (target != null) {
            if (target.y > IafConfig.maxDragonFlight) {
                target = new Vec3(target.x, IafConfig.maxDragonFlight, target.z);
            }
            if (target.y >= dragon.getY() && !dragon.isModelDead()) {
                dragon.setDeltaMovement(dragon.getDeltaMovement().add(0, 0.1D, 0));

            }
        }

        this.prevAirAttack = dragon.airAttack;
    }

    public Vec3 getFlightTarget() {
        return target == null ? Vec3.ZERO : target;
    }

    public void setFlightTarget(Vec3 target) {
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
                startPreyVec = new Vec3(LivingEntityIn.getX(), LivingEntityIn.getY(), LivingEntityIn.getZ());
            } else {
                startPreyVec = new Vec3(dragon.getX(), dragon.getY(), dragon.getZ());
            }
            startAttackVec = new Vec3(dragon.getX(), dragon.getY(), dragon.getZ());
        }
        prevAttackTarget = LivingEntityIn;
    }

    protected static class GroundMoveHelper extends MoveControl {
        public GroundMoveHelper(Mob LivingEntityIn) {
            super(LivingEntityIn);
        }

        public float distance(float rotateAngleFrom, float rotateAngleTo) {
            return (float) IAFMath.atan2_accurate(Mth.sin(rotateAngleTo - rotateAngleFrom), Mth.cos(rotateAngleTo - rotateAngleFrom));
        }

        @Override
        public void tick() {
            if (this.operation == Operation.STRAFE) {
                float f = (float) this.mob.getAttribute(Attributes.MOVEMENT_SPEED).getValue();
                float f1 = (float) this.speedModifier * f;
                float f2 = this.strafeForwards;
                float f3 = this.strafeRight;
                float f4 = Mth.sqrt(f2 * f2 + f3 * f3);

                if (f4 < 1.0F) {
                    f4 = 1.0F;
                }

                f4 = f1 / f4;
                f2 = f2 * f4;
                f3 = f3 * f4;
                float f5 = Mth.sin(this.mob.getYRot() * 0.017453292F);
                float f6 = Mth.cos(this.mob.getYRot() * 0.017453292F);
                float f7 = f2 * f6 - f3 * f5;
                float f8 = f3 * f6 + f2 * f5;
                PathNavigation pathnavigate = this.mob.getNavigation();
                if (pathnavigate != null) {
                    NodeEvaluator nodeprocessor = pathnavigate.getNodeEvaluator();
                    if (nodeprocessor != null && nodeprocessor.getBlockPathType(this.mob.level(), Mth.floor(this.mob.getX() + (double) f7), Mth.floor(this.mob.getY()), Mth.floor(this.mob.getZ() + (double) f8)) != BlockPathTypes.WALKABLE) {
                        this.strafeForwards = 1.0F;
                        this.strafeRight = 0.0F;
                        f1 = f;
                    }
                }
                this.mob.setSpeed(f1);
                this.mob.setZza(this.strafeForwards);
                this.mob.setXxa(this.strafeRight);
                this.operation = Operation.WAIT;
            } else if (this.operation == Operation.MOVE_TO) {
                this.operation = Operation.WAIT;
                EntityDragonBase dragonBase = (EntityDragonBase) mob;
                double d0 = this.getWantedX() - this.mob.getX();
                double d1 = this.getWantedZ() - this.mob.getZ();
                double d2 = this.getWantedY() - this.mob.getY();
                double d3 = d0 * d0 + d2 * d2 + d1 * d1;

                if (d3 < 2.500000277905201E-7D) {
                    this.mob.setZza(0.0F);
                    return;
                }
                float targetDegree = (float) (Mth.atan2(d1, d0) * (180D / Math.PI)) - 90.0F;
                float changeRange = 70F;
                if (Math.ceil(dragonBase.getBbWidth()) > 2F) {
                    float ageMod = 1F - Math.min(dragonBase.getAgeInDays(), 125) / 125F;
                    changeRange = 5 + ageMod * 10;
                }
                this.mob.setYRot(this.rotlerp(this.mob.getYRot(), targetDegree, changeRange));
                this.mob.setSpeed((float) (this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED)));
                if (d2 > (double) this.mob.maxUpStep() && d0 * d0 + d1 * d1 < (double) Math.max(1.0F, this.mob.getBbWidth() / 2)) {
                    this.mob.getJumpControl().jump();
                    this.operation = Operation.JUMPING;
                }
            } else if (this.operation == Operation.JUMPING) {
                this.mob.setSpeed((float) (this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED)));

                if (this.mob.onGround()) {
                    this.operation = Operation.WAIT;
                }
            } else {
                this.mob.setZza(0.0F);
            }
        }

    }

    protected static class FlightMoveHelper extends MoveControl {

        private final EntityDragonBase dragon;

        protected FlightMoveHelper(EntityDragonBase dragonBase) {
            super(dragonBase);
            this.dragon = dragonBase;
        }

        @Override
        public void tick() {
            if (dragon.horizontalCollision) {
                dragon.setYRot(dragon.getYRot() + 180.0F);
                this.speedModifier = 0.1F;
                dragon.flightManager.target = null;
                return;
            }
            float distX = (float) (dragon.flightManager.getFlightTarget().x - dragon.getX());
            float distY = (float) (dragon.flightManager.getFlightTarget().y - dragon.getY());
            float distZ = (float) (dragon.flightManager.getFlightTarget().z - dragon.getZ());
            double planeDist = Math.sqrt(distX * distX + distZ * distZ);
            double yDistMod = 1.0D - (double) Mth.abs(distY * 0.7F) / planeDist;
            distX = (float) ((double) distX * yDistMod);
            distZ = (float) ((double) distZ * yDistMod);
            planeDist = Mth.sqrt(distX * distX + distZ * distZ);
            double dist = Math.sqrt(distX * distX + distZ * distZ + distY * distY);
            if (dist > 1.0F) {
                float yawCopy = dragon.getYRot();
                float atan = (float) Mth.atan2(distZ, distX);
                float yawTurn = Mth.wrapDegrees(dragon.getYRot() + 90);
                float yawTurnAtan = Mth.wrapDegrees(atan * 57.295776F);
                dragon.setYRot(IafDragonFlightManager.approachDegrees(yawTurn, yawTurnAtan, dragon.airAttack == IafDragonAttacks.Air.TACKLE && dragon.getTarget() != null ? 10 : 4.0F) - 90.0F);
                dragon.yBodyRot = dragon.getYRot();
                if (IafDragonFlightManager.degreesDifferenceAbs(yawCopy, dragon.getYRot()) < 3.0F) {
                    speedModifier = IafDragonFlightManager.approach((float) speedModifier, 1.8F, 0.005F * (1.8F / (float) speedModifier));
                } else {
                    speedModifier = IafDragonFlightManager.approach((float) speedModifier, 0.2F, 0.025F);
                    if (dist < 100D && dragon.getTarget() != null) {
                        speedModifier = speedModifier * (dist / 100D);
                    }
                }
                float finPitch = (float) (-(Mth.atan2(-distY, planeDist) * 57.2957763671875D));
                dragon.setXRot(finPitch);
                float yawTurnHead = dragon.getYRot() + 90.0F;
                speedModifier *= dragon.getFlightSpeedModifier();
                speedModifier *= Math.min(1, dist / 50 + 0.3);//Make the dragon fly slower when close to target
                double x = speedModifier * Mth.cos(yawTurnHead * 0.017453292F) * Math.abs((double) distX / dist);
                double y = speedModifier * Mth.sin(finPitch * 0.017453292F) * Math.abs((double) distY / dist);
                double z = speedModifier * Mth.sin(yawTurnHead * 0.017453292F) * Math.abs((double) distZ / dist);
                double motionCap = 0.2D;
                dragon.setDeltaMovement(dragon.getDeltaMovement().add(Math.min(x * 0.2D, motionCap), Math.min(y * 0.2D, motionCap), Math.min(z * 0.2D, motionCap)));
            }
        }


    }

    protected static class PlayerFlightMoveHelper<T extends Mob & IFlyingMount> extends MoveControl {

        private final T dragon;

        public PlayerFlightMoveHelper(T dragon) {
            super(dragon);
            this.dragon = dragon;
        }

        @Override
        public void tick() {
            if (dragon instanceof EntityDragonBase theDragon && theDragon.getControllingPassenger() != null) {
                // New ride system doesn't need move controller
                // The flight move control is disabled here, the walking move controller will stay Operation.WAIT so nothing will happen too
                return;
            }

            double flySpeed = speedModifier * speedMod() * 3;
            Vec3 dragonVec = dragon.position();
            Vec3 moveVec = new Vec3(wantedX, wantedY, wantedZ);
            Vec3 normalized = moveVec.subtract(dragonVec).normalize();
            double dist = dragonVec.distanceTo(moveVec);
            dragon.setDeltaMovement(normalized.x * flySpeed, normalized.y * flySpeed, normalized.z * flySpeed);
            if (dist > 2.5E-7) {
                float yaw = (float) Math.toDegrees(Math.PI * 2 - Math.atan2(normalized.x, normalized.y));
                dragon.setYRot(rotlerp(dragon.getYRot(), yaw, 5));
                dragon.setSpeed((float) (speedModifier));
            }
            dragon.move(MoverType.SELF, dragon.getDeltaMovement());
        }

        public double speedMod() {
            return (dragon instanceof EntityAmphithere ? 0.6D : 1.25D) * IafConfig.dragonFlightSpeedMod * dragon.getAttributeValue(Attributes.MOVEMENT_SPEED);
        }
    }
}
