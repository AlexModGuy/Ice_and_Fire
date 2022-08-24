package com.github.alexthe666.iceandfire.world.gen;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.entity.EntityHydra;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import com.github.alexthe666.iceandfire.world.IafWorldRegistry;
import com.mojang.serialization.Codec;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.SkullBlock;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.Features;
import net.minecraft.world.gen.feature.NoFeatureConfig;

import java.util.Random;
import java.util.stream.Collectors;

public class WorldGenHydraCave extends Feature<NoFeatureConfig> {

    public static final ResourceLocation HYDRA_CHEST = new ResourceLocation("iceandfire", "chest/hydra_cave");
    protected static final ConfiguredFeature SWAMP_FEATURE = Features.SWAMP_TREE;
    private static final Direction[] HORIZONTALS = new Direction[]{Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};

    public WorldGenHydraCave(Codec<NoFeatureConfig> configFactoryIn) {
        super(configFactoryIn);
    }

    @Override
    public boolean place(ISeedReader worldIn, ChunkGenerator p_230362_3_, Random rand, BlockPos position, NoFeatureConfig p_230362_6_) {
        if (!IafWorldRegistry.isDimensionListedForFeatures(worldIn)) {
            return false;
        }
        if (!IafConfig.generateHydraCaves || rand.nextInt(IafConfig.generateHydraChance) != 0 || !IafWorldRegistry.isFarEnoughFromSpawn(worldIn, position) || !IafWorldRegistry.isFarEnoughFromDangerousGen(worldIn, position)) {
            return false;
        }
        position = worldIn.getHeightmapPos(Heightmap.Type.WORLD_SURFACE_WG, position);
        int i1 = 8;
        int i2 = i1 - 2;
        int dist = 6;
        if (worldIn.isEmptyBlock(position.offset(i1 - dist, -3, -i1 + dist)) || worldIn.isEmptyBlock(position.offset(i1 - dist, -3, i1 - dist)) || worldIn.isEmptyBlock(position.offset(-i1 + dist, -3, -i1 + dist)) || worldIn.isEmptyBlock(position.offset(-i1 + dist, -3, i1 - dist))) {
            return false;
        }

        {
            int ySize = rand.nextInt(2);
            int j = i1 + rand.nextInt(2);
            int k = 5 + ySize;
            int l = i1 + rand.nextInt(2);
            float f = (j + k + l) * 0.333F + 0.5F;


            for (BlockPos blockpos : BlockPos.betweenClosedStream(position.offset(-j, -k, -l), position.offset(j, k, l)).map(BlockPos::immutable).collect(Collectors.toSet())) {
                boolean doorwayX = blockpos.getX() >= position.getX() - 2 + rand.nextInt(2) && blockpos.getX() <= position.getX() + 2 + rand.nextInt(2);
                boolean doorwayZ = blockpos.getZ() >= position.getZ() - 2 + rand.nextInt(2) && blockpos.getZ() <= position.getZ() + 2 + rand.nextInt(2);
                boolean isNotInDoorway = !doorwayX && !doorwayZ && blockpos.getY() > position.getY() || blockpos.getY() > position.getY() + k - (1 + rand.nextInt(2));
                if (blockpos.distSqr(position) <= f * f) {
                    if (!(worldIn.getBlockState(position).getBlock() instanceof ChestBlock) && isNotInDoorway) {
                        worldIn.setBlock(blockpos, Blocks.GRASS_BLOCK.defaultBlockState(), 3);
                        if (worldIn.getBlockState(position.below()).getBlock() == Blocks.GRASS_BLOCK) {
                            worldIn.setBlock(blockpos.below(), Blocks.DIRT.defaultBlockState(), 3);
                        }
                        if (rand.nextInt(4) == 0) {
                            worldIn.setBlock(blockpos.above(), Blocks.GRASS.defaultBlockState(), 2);
                        }
                        if (rand.nextInt(9) == 0) {
                            SWAMP_FEATURE.place(worldIn, p_230362_3_, rand, blockpos.above());
                        }

                    }
                    if (blockpos.getY() == position.getY()) {
                        worldIn.setBlock(blockpos, Blocks.GRASS_BLOCK.defaultBlockState(), 3);
                    }
                    if (blockpos.getY() <= position.getY() - 1 && !worldIn.getBlockState(blockpos).canOcclude()) {
                        worldIn.setBlock(blockpos, Blocks.STONE.defaultBlockState(), 3);

                    }
                }
            }


        }
        {
            int ySize = rand.nextInt(2);
            int j = i2 + rand.nextInt(2);
            int k = 4 + ySize;
            int l = i2 + rand.nextInt(2);
            float f = (j + k + l) * 0.333F + 0.5F;
            for (BlockPos blockpos : BlockPos.betweenClosedStream(position.offset(-j, -k, -l), position.offset(j, k, l)).map(BlockPos::immutable).collect(Collectors.toSet())) {
                if (blockpos.distSqr(position) <= f * f && blockpos.getY() > position.getY()) {
                    if (!(worldIn.getBlockState(position).getBlock() instanceof ChestBlock)) {
                        worldIn.setBlock(blockpos, Blocks.AIR.defaultBlockState(), 3);

                    }
                }
            }
            for (BlockPos blockpos : BlockPos.betweenClosedStream(position.offset(-j, -k, -l), position.offset(j, k + 8, l)).map(BlockPos::immutable).collect(Collectors.toSet())) {
                if (blockpos.distSqr(position) <= f * f && blockpos.getY() == position.getY()) {
                    if (rand.nextInt(30) == 0 && isTouchingAir(worldIn, blockpos.above())) {
                        worldIn.setBlock(blockpos.above(1), Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, HORIZONTALS[new Random().nextInt(3)]), 2);
                        if (worldIn.getBlockState(blockpos.above(1)).getBlock() instanceof ChestBlock) {
                            TileEntity tileentity1 = worldIn.getBlockEntity(blockpos.above(1));
                            if (tileentity1 instanceof ChestTileEntity) {
                                ((ChestTileEntity) tileentity1).setLootTable(HYDRA_CHEST, rand.nextLong());
                            }
                        }
                        continue;
                    }
                    if (rand.nextInt(45) == 0 && isTouchingAir(worldIn, blockpos.above())) {
                        worldIn.setBlock(blockpos.above(), Blocks.SKELETON_SKULL.defaultBlockState().setValue(SkullBlock.ROTATION, rand.nextInt(15)), 2);
                        TileEntity tileentity1 = worldIn.getBlockEntity(blockpos.above(1));
                        continue;
                    }
                    if (rand.nextInt(35) == 0 && isTouchingAir(worldIn, blockpos.above())) {
                        worldIn.setBlock(blockpos.above(), Blocks.OAK_LEAVES.defaultBlockState().setValue(LeavesBlock.PERSISTENT, true), 2);
                        for (Direction facing : Direction.values()) {
                            if (rand.nextFloat() < 0.3F && facing != Direction.DOWN) {
                                worldIn.setBlock(blockpos.above().relative(facing), Blocks.OAK_LEAVES.defaultBlockState(), 2);
                            }
                        }
                        continue;
                    }
                    if (rand.nextInt(15) == 0 && isTouchingAir(worldIn, blockpos.above())) {
                        worldIn.setBlock(blockpos.above(), Blocks.TALL_GRASS.defaultBlockState(), 2);
                        continue;
                    }
                    if (rand.nextInt(15) == 0 && isTouchingAir(worldIn, blockpos.above())) {
                        worldIn.setBlock(blockpos.above(), rand.nextBoolean() ? Blocks.BROWN_MUSHROOM.defaultBlockState() : Blocks.RED_MUSHROOM.defaultBlockState(), 2);
                        continue;
                    }
                }
            }
        }
        EntityHydra hydra = new EntityHydra(IafEntityRegistry.HYDRA.get(), worldIn.getLevel());
        hydra.setVariant(rand.nextInt(3));
        hydra.restrictTo(position, 15);
        hydra.absMoveTo(position.getX() + 0.5, position.getY() + 1.5, position.getZ() + 0.5, rand.nextFloat() * 360, 0);
        worldIn.addFreshEntity(hydra);
        return true;
    }

    private boolean isTouchingAir(IWorld worldIn, BlockPos pos) {
        boolean isTouchingAir = true;
        for (Direction direction : HORIZONTALS) {
            if (!worldIn.isEmptyBlock(pos.relative(direction))) {
                isTouchingAir = false;
            }
        }
        return isTouchingAir;
    }
}
