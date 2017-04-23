package com.github.alexthe666.iceandfire.entity.ai;

import net.minecraft.block.material.Material;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Random;

public class DragonAIAirTarget extends EntityAIBase {
    private EntityDragonBase dragon;
    private World theWorld;

    public DragonAIAirTarget(EntityDragonBase dragon) {
        this.dragon = dragon;
        this.theWorld = dragon.world;
    }

    public boolean shouldExecute() {
        if (dragon != null) {
            if (!dragon.isFlying() && !dragon.isHovering()) {
                return false;
            }
            if (dragon.isSleeping()) {
                return false;
            }
            if (dragon.isChild()) {
                return false;
            }
            if (dragon.getOwner() != null && dragon.getPassengers().contains(dragon.getOwner())) {
                return false;
            }
            if (dragon.airTarget != null && dragon.getDistanceSquared(new Vec3d(dragon.airTarget.getX(), dragon.posY, dragon.airTarget.getZ())) > 3) {
                dragon.airTarget = null;
            }

            if (dragon.airTarget != null) {
                return false;
            } else {
                Vec3d vec = this.findAirTarget();
                if (vec == null) {
                    return false;
                } else {
                    dragon.airTarget = new BlockPos(vec.xCoord, vec.yCoord, vec.zCoord);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean continueExecuting() {
        return dragon.airTarget != null;
    }

    public Vec3d findAirTarget() {
        Random random = this.dragon.getRNG();

        if (dragon.getAttackTarget() == null) {
            for (int i = 0; i < 10; ++i) {
                if (dragon.homeArea != null) {
                    BlockPos blockpos1 = new BlockPos((int) this.dragon.homeArea.getX() + ((32 + random.nextInt(32)) * (random.nextBoolean() ? -1 : 1)), ((int) this.dragon.homeArea.getY() + ((3 + random.nextInt(64)) * (random.nextBoolean() ? -1 : 1))), (int) this.dragon.homeArea.getZ() + ((32 + random.nextInt(32)) * (random.nextBoolean() ? -1 : 1)));
                    BlockPos blockpos1ground = new BlockPos((int) this.dragon.posX + ((6 + random.nextInt(10)) * (random.nextBoolean() ? -1 : 1)), (int) this.dragon.posY + 7 + random.nextInt(6), (int) this.dragon.posZ + ((6 + random.nextInt(10)) * (random.nextBoolean() ? -1 : 1)));
                    if (dragon.doesWantToLand()) {
                        return new Vec3d(dragon.homeArea.getX(), dragon.homeArea.getY(), dragon.homeArea.getZ());
                    }
                    if (dragon.onGround) {
                        if (dragon.world.getBlockState(blockpos1ground).getMaterial() == Material.AIR) {
                            return new Vec3d(blockpos1ground.getX(), blockpos1ground.getY(), blockpos1ground.getZ());
                        }
                    } else {
                        if (dragon.world.getBlockState(blockpos1).getMaterial() == Material.AIR) {
                            return new Vec3d(blockpos1.getX(), blockpos1.getY(), blockpos1.getZ());
                        }
                    }
                } else {
                    BlockPos blockpos1 = new BlockPos((int) this.dragon.posX + ((6 + random.nextInt(10)) * (random.nextBoolean() ? -1 : 1)), (int) this.dragon.posY + 3 + ((random.nextInt(6)) * (random.nextBoolean() ? -1 : 1)), (int) this.dragon.posZ + ((6 + random.nextInt(10)) * (random.nextBoolean() ? -1 : 1)));
                    BlockPos blockpos1ground = new BlockPos((int) this.dragon.posX + ((6 + random.nextInt(10)) * (random.nextBoolean() ? -1 : 1)), (int) this.dragon.posY + 7 + random.nextInt(6), (int) this.dragon.posZ + ((6 + random.nextInt(10)) * (random.nextBoolean() ? -1 : 1)));
                    if (dragon.doesWantToLand()) {
                        return null;
                    }
                    if (dragon.onGround) {
                        if (dragon.world.getBlockState(blockpos1ground).getMaterial() == Material.AIR) {
                            return new Vec3d(blockpos1ground.getX(), blockpos1ground.getY(), blockpos1ground.getZ());
                        }
                    } else {
                        if (dragon.world.getBlockState(blockpos1).getMaterial() == Material.AIR) {
                            return new Vec3d(blockpos1.getX(), blockpos1.getY(), blockpos1.getZ());
                        }
                    }
                }
            }
        } else {
            BlockPos blockpos1 = new BlockPos((int) dragon.getAttackTarget().posX, (int) dragon.getAttackTarget().posY, (int) dragon.getAttackTarget().posZ);
            if (dragon.world.getBlockState(blockpos1).getMaterial() == Material.AIR) {
                return new Vec3d(blockpos1.getX(), blockpos1.getY(), blockpos1.getZ());
            }
        }

        return null;
    }

}