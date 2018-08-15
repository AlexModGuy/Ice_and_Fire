package com.github.alexthe666.iceandfire.structures;

import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenShrub;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.EnumPlantType;

import java.util.Random;

public class WorldGenMyrmexDecoration {


    public static void generateSkeleton(World worldIn, BlockPos blockpos, BlockPos origin, int radius, Random rand) {
        EnumFacing direction = EnumFacing.HORIZONTALS[new Random().nextInt(3)];
        EnumFacing.Axis oppositeAxis = direction.getAxis() == EnumFacing.Axis.X ? EnumFacing.Axis.Z : EnumFacing.Axis.X;
        int maxRibHeight = rand.nextInt(2);
        for (int spine = 0; spine < 5 + rand.nextInt(2) * 2; spine++) {
            BlockPos segment = blockpos.offset(direction, spine);
            if (origin.distanceSq(segment) <= (double) (radius * radius)) {
                worldIn.setBlockState(segment, Blocks.BONE_BLOCK.getDefaultState().withProperty(BlockBone.AXIS, direction.getAxis()));
            }
            if (spine % 2 != 0) {
                BlockPos rightRib = segment.offset(direction.rotateYCCW());
                BlockPos leftRib = segment.offset(direction.rotateY());
                if (origin.distanceSq(rightRib) <= (double) (radius * radius)) {
                    worldIn.setBlockState(rightRib, Blocks.BONE_BLOCK.getDefaultState().withProperty(BlockBone.AXIS, oppositeAxis));
                }
                if (origin.distanceSq(leftRib) <= (double) (radius * radius)) {
                    worldIn.setBlockState(leftRib, Blocks.BONE_BLOCK.getDefaultState().withProperty(BlockBone.AXIS, oppositeAxis));
                }
                for (int ribHeight = 1; ribHeight < maxRibHeight + 2; ribHeight++) {
                    if (origin.distanceSq(rightRib.up(ribHeight).offset(direction.rotateYCCW())) <= (double) (radius * radius)) {
                        worldIn.setBlockState(rightRib.up(ribHeight).offset(direction.rotateYCCW()), Blocks.BONE_BLOCK.getDefaultState().withProperty(BlockBone.AXIS, EnumFacing.Axis.Y));
                    }
                    if (origin.distanceSq(leftRib.up(ribHeight).offset(direction.rotateY())) <= (double) (radius * radius)) {
                        worldIn.setBlockState(leftRib.up(ribHeight).offset(direction.rotateY()), Blocks.BONE_BLOCK.getDefaultState().withProperty(BlockBone.AXIS, EnumFacing.Axis.Y));
                    }
                }
                if (origin.distanceSq(rightRib.up(maxRibHeight + 2)) <= (double) (radius * radius)) {
                    worldIn.setBlockState(rightRib.up(maxRibHeight + 2), Blocks.BONE_BLOCK.getDefaultState().withProperty(BlockBone.AXIS, oppositeAxis));
                }
                if (origin.distanceSq(leftRib.up(maxRibHeight + 2)) <= (double) (radius * radius)) {
                    worldIn.setBlockState(leftRib.up(maxRibHeight + 2), Blocks.BONE_BLOCK.getDefaultState().withProperty(BlockBone.AXIS, oppositeAxis));
                }
            }

        }
    }

    public static void generateLeaves(World worldIn, BlockPos blockpos, BlockPos origin, int radius, Random rand) {
        IBlockState leaf = Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.OAK).withProperty(BlockLeaves.DECAYABLE, Boolean.valueOf(false));
        for (BiomeDictionary.Type type : BiomeDictionary.getTypes(worldIn.getBiome(blockpos))) {
            if (type == BiomeDictionary.Type.SANDY || type == BiomeDictionary.Type.SAVANNA || type == BiomeDictionary.Type.WASTELAND) {
                leaf = Blocks.LEAVES2.getDefaultState().withProperty(BlockNewLeaf.VARIANT, BlockPlanks.EnumType.ACACIA).withProperty(BlockLeaves.DECAYABLE, Boolean.valueOf(false));
                break;
            }
            if (type == BiomeDictionary.Type.JUNGLE) {
                leaf = Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.JUNGLE).withProperty(BlockLeaves.DECAYABLE, Boolean.valueOf(false));
                break;
            }
            if (type == BiomeDictionary.Type.CONIFEROUS) {
                leaf = Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.SPRUCE).withProperty(BlockLeaves.DECAYABLE, Boolean.valueOf(false));
                break;
            }
        }
        for (int i = blockpos.getY(); i <= blockpos.getY() + 2; ++i) {
            int j = i - blockpos.getY();
            int k = 2 - j;

            for (int l = blockpos.getX() - k; l <= blockpos.getX() + k; ++l) {
                int i1 = l - blockpos.getX();

                for (int j1 = blockpos.getZ() - k; j1 <= blockpos.getZ() + k; ++j1) {
                    int k1 = j1 - blockpos.getZ();

                    if (Math.abs(i1) != k || Math.abs(k1) != k || rand.nextInt(2) != 0) {
                        BlockPos blockpos2 = new BlockPos(l, i, j1);
                        IBlockState state = worldIn.getBlockState(blockpos2);

                        if (state.getBlock().canBeReplacedByLeaves(state, worldIn, blockpos2)) {
                            worldIn.setBlockState(blockpos2, leaf, 3);
                        }
                    }
                }
            }
        }
    }

    public static void generatePumpkins(World worldIn, BlockPos blockpos, BlockPos origin, int radius, Random rand) {
        if(worldIn.getBlockState(blockpos.down()).isFullCube()){
            worldIn.setBlockState(blockpos, BiomeDictionary.hasType(worldIn.getBiome(blockpos), BiomeDictionary.Type.JUNGLE) ? Blocks.MELON_BLOCK.getDefaultState() : Blocks.PUMPKIN.getDefaultState().withProperty(BlockHorizontal.FACING, EnumFacing.getHorizontal(rand.nextInt(3))));
        }
    }

    public static void generateMushrooms(World worldIn, BlockPos blockpos, BlockPos origin, int radius, Random rand) {
        if(worldIn.getBlockState(blockpos.down()).isSideSolid(worldIn, blockpos.down(), EnumFacing.UP)){
            worldIn.setBlockState(blockpos, rand.nextBoolean() ? Blocks.BROWN_MUSHROOM.getDefaultState() : Blocks.RED_MUSHROOM.getDefaultState());
        }
    }
}