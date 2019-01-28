package com.github.alexthe666.iceandfire.entity;

import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityCreature;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nullable;
import java.util.Random;

public class DragonPositionGenerator {

    public static Vec3d findRandomTargetBlock(EntityCreature entitycreatureIn, int xz, int y, @Nullable Vec3d targetVec3) {
        Vec3d vec = generateRandomPos(entitycreatureIn, xz, y, targetVec3, false);
        return vec == null ? entitycreatureIn.getPositionVector() : vec;
    }

    @Nullable
    public static Vec3d generateRandomPos(EntityCreature mob, int xz, int y, @Nullable Vec3d vec, boolean skipWater) {
        PathNavigate pathnavigate = mob.getNavigator();
        Random random = mob.getRNG();
        boolean flag;

        if (mob.hasHome()) {
            double d0 = mob.getHomePosition().distanceSq((double) MathHelper.floor(mob.posX), (double) MathHelper.floor(mob.posY), (double) MathHelper.floor(mob.posZ)) + 4.0D;
            double d1 = (double) (mob.getMaximumHomeDistance() + (float) xz);
            flag = d0 < d1 * d1;
        } else {
            flag = false;
        }

        boolean flag1 = false;
        float f = -99999.0F;
        int k1 = 0;
        int i = 0;
        int j = 0;

        for (int k = 0; k < 10; ++k) {
            int l = random.nextInt(2 * xz + 1) - xz;
            int i1 = random.nextInt(2 * y + 1) - y;
            int j1 = random.nextInt(2 * xz + 1) - xz;

            if (vec == null || (double) l * vec.x + (double) j1 * vec.z >= 0.0D) {
                if (mob.hasHome() && xz > 1) {
                    BlockPos blockpos = mob.getHomePosition();

                    if (mob.posX > (double) blockpos.getX()) {
                        l -= random.nextInt(xz / 2);
                    } else {
                        l += random.nextInt(xz / 2);
                    }

                    if (mob.posZ > (double) blockpos.getZ()) {
                        j1 -= random.nextInt(xz / 2);
                    } else {
                        j1 += random.nextInt(xz / 2);
                    }
                }

                BlockPos blockpos1 = new BlockPos((double) l + mob.posX, (double) i1 + mob.posY, (double) j1 + mob.posZ);

                if ((!flag || mob.isWithinHomeDistanceFromPosition(blockpos1)) && pathnavigate.canEntityStandOnPos(blockpos1)) {
                    if (skipWater) {
                        blockpos1 = moveAboveSolid(blockpos1, mob);
                        if (isWaterDestination(blockpos1, mob)) {
                            continue;
                        }
                    }

                    float f1 = mob.getBlockPathWeight(blockpos1);

                    if (f1 > f) {
                        f = f1;
                        k1 = l;
                        i = i1;
                        j = j1;
                        flag1 = true;
                    }
                }
            }
        }

        if (flag1) {
            return new Vec3d((double) k1 + mob.posX, (double) i + mob.posY, (double) j + mob.posZ);
        } else {
            return null;
        }
    }

    private static BlockPos moveAboveSolid(BlockPos pos, EntityCreature mob) {
        if (!mob.world.getBlockState(pos).getMaterial().isSolid()) {
            return pos;
        } else {
            BlockPos blockpos;

            for (blockpos = pos.up(); blockpos.getY() < mob.world.getHeight() && mob.world.getBlockState(blockpos).getMaterial().isSolid(); blockpos = blockpos.up()) {
                ;
            }

            return blockpos;
        }
    }

    private static boolean isWaterDestination(BlockPos pos, EntityCreature mob) {
        return mob.world.getBlockState(pos).getMaterial() == Material.WATER;
    }
}
