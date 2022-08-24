package com.github.alexthe666.iceandfire.entity.util;

import net.minecraft.block.material.Material;
import net.minecraft.entity.MobEntity;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;

import javax.annotation.Nullable;
import java.util.Random;

public class DragonPositionGenerator {

    public static Vector3d findRandomTargetBlock(MobEntity MobEntityIn, int xz, int y, @Nullable Vector3d targetVec3) {
        Vector3d vec = generateRandomPos(MobEntityIn, xz, y, targetVec3, false);
        return vec == null ? MobEntityIn.position() : vec;
    }

    @Nullable
    public static Vector3d generateRandomPos(MobEntity mob, int xz, int y, @Nullable Vector3d vec, boolean skipWater) {
        PathNavigator pathnavigate = mob.getNavigation();
        Random random = mob.getRandom();
        boolean flag;

        if (mob.hasRestriction()) {
            double d0 = mob.getRestrictCenter().distSqr(MathHelper.floor(mob.getX()), MathHelper.floor(mob.getY()), MathHelper.floor(mob.getZ()), true) + 4.0D;
            double d1 = mob.getRestrictRadius() + (float) xz;
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
                if (mob.hasRestriction() && xz > 1) {
                    BlockPos blockpos = mob.getRestrictCenter();

                    if (mob.getX() > (double) blockpos.getX()) {
                        l -= random.nextInt(xz / 2);
                    } else {
                        l += random.nextInt(xz / 2);
                    }

                    if (mob.getZ() > (double) blockpos.getZ()) {
                        j1 -= random.nextInt(xz / 2);
                    } else {
                        j1 += random.nextInt(xz / 2);
                    }
                }

                BlockPos blockpos1 = new BlockPos((double) l + mob.getX(), (double) i1 + mob.getY(), (double) j1 + mob.getZ());

                if ((!flag || mob.isWithinRestriction(blockpos1)) && pathnavigate.isStableDestination(blockpos1)) {
                    if (skipWater) {
                        blockpos1 = moveAboveSolid(blockpos1, mob);
                        if (isWaterDestination(blockpos1, mob)) {
                            continue;
                        }
                    }

                    float f1 = 0.0F;

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
            return new Vector3d((double) k1 + mob.getX(), (double) i + mob.getY(), (double) j + mob.getZ());
        } else {
            return null;
        }
    }

    private static BlockPos moveAboveSolid(BlockPos pos, MobEntity mob) {
        if (!mob.level.getBlockState(pos).getMaterial().isSolid()) {
            return pos;
        } else {
            BlockPos blockpos;

            for (blockpos = pos.above(); blockpos.getY() < mob.level.getMaxBuildHeight() && mob.level.getBlockState(blockpos).getMaterial().isSolid(); blockpos = blockpos.above()) {
            }

            return blockpos;
        }
    }

    private static boolean isWaterDestination(BlockPos pos, MobEntity mob) {
        return mob.level.getBlockState(pos).getMaterial() == Material.WATER;
    }
}
