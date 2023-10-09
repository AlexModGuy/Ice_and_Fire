package com.github.alexthe666.iceandfire.entity.util;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class DragonPositionGenerator {

    public static Vec3 findRandomTargetBlock(Mob MobEntityIn, int xz, int y, @Nullable Vec3 targetVec3) {
        Vec3 vec = generateRandomPos(MobEntityIn, xz, y, targetVec3, false);
        return vec == null ? MobEntityIn.position() : vec;
    }

    @Nullable
    public static Vec3 generateRandomPos(Mob mob, int xz, int y, @Nullable Vec3 vec, boolean skipWater) {
        PathNavigation pathnavigate = mob.getNavigation();
        RandomSource random = mob.getRandom();
        boolean flag;

        if (mob.hasRestriction()) {
            double d0 = mob.getRestrictCenter().distToCenterSqr(Mth.floor(mob.getX()), Mth.floor(mob.getY()), Mth.floor(mob.getZ())) + 4.0D;
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

                BlockPos blockpos1 = new BlockPos(l + mob.getBlockX(), i1 + mob.getBlockY(), j1 + mob.getBlockZ());

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
            return new Vec3((double) k1 + mob.getX(), (double) i + mob.getY(), (double) j + mob.getZ());
        } else {
            return null;
        }
    }

    private static BlockPos moveAboveSolid(BlockPos pos, Mob mob) {
        if (!mob.level().getBlockState(pos).isSolid()) {
            return pos;
        } else {
            BlockPos blockpos;

            for (blockpos = pos.above(); blockpos.getY() < mob.level().getMaxBuildHeight() && mob.level().getBlockState(blockpos).isSolid(); blockpos = blockpos.above()) {
            }

            return blockpos;
        }
    }

    private static boolean isWaterDestination(BlockPos pos, Mob mob) {
        return mob.level().getBlockState(pos).is(Blocks.WATER);
    }
}
