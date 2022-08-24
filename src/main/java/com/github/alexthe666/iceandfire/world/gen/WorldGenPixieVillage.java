package com.github.alexthe666.iceandfire.world.gen;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.block.BlockPixieHouse;
import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.entity.EntityPixie;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import com.github.alexthe666.iceandfire.world.IafWorldRegistry;
import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.SpawnReason;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

import java.util.Random;

public class WorldGenPixieVillage extends Feature<NoFeatureConfig> {

    private static final Direction[] HORIZONTALS = new Direction[]{Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};

    public WorldGenPixieVillage(Codec<NoFeatureConfig> configFactoryIn) {
        super(configFactoryIn);
    }

    @Override
    public boolean place(ISeedReader worldIn, ChunkGenerator p_230362_3_, Random rand, BlockPos position, NoFeatureConfig p_230362_6_) {
        if (!IafWorldRegistry.isDimensionListedForFeatures(worldIn)) {
            return false;
        }
        if (!IafConfig.spawnPixies || rand.nextInt(IafConfig.spawnPixiesChance) != 0 || !IafWorldRegistry.isFarEnoughFromSpawn(worldIn, position)) {
            return false;
        }
        position = worldIn.getHeightmapPos(Heightmap.Type.WORLD_SURFACE_WG, position);
        int maxRoads = IafConfig.pixieVillageSize + rand.nextInt(5);
        BlockPos buildPosition = position;
        int placedRoads = 0;
        while(placedRoads < maxRoads){
            int roadLength = 10 + rand.nextInt(15);
            Direction buildingDirection = Direction.from2DDataValue(rand.nextInt(3));
            for(int i = 0; i < roadLength; i++) {
                BlockPos buildPosition2 = buildPosition.relative(buildingDirection, i);
                buildPosition2 = worldIn.getHeightmapPos(Heightmap.Type.WORLD_SURFACE_WG, buildPosition2).below();
                if (worldIn.getBlockState(buildPosition2).getFluidState().isEmpty()) {
                    worldIn.setBlock(buildPosition2, Blocks.GRASS_PATH.defaultBlockState(), 2);
                } else {
                    worldIn.setBlock(buildPosition2, Blocks.SPRUCE_PLANKS.defaultBlockState(), 2);
                }
                if (rand.nextInt(8) == 0) {
                    Direction houseDir = rand.nextBoolean() ? buildingDirection.getClockWise() : buildingDirection.getCounterClockWise();
                    BlockState houseState = IafBlockRegistry.PIXIE_HOUSE_OAK.defaultBlockState();
                    int houseColor = rand.nextInt(5);
                    switch (houseColor) {
                        case 0:
                            houseState = IafBlockRegistry.PIXIE_HOUSE_MUSHROOM_RED.defaultBlockState().setValue(BlockPixieHouse.FACING, houseDir.getOpposite());
                            break;
                        case 1:
                            houseState = IafBlockRegistry.PIXIE_HOUSE_MUSHROOM_BROWN.defaultBlockState().setValue(BlockPixieHouse.FACING, houseDir.getOpposite());
                            break;
                        case 2:
                            houseState = IafBlockRegistry.PIXIE_HOUSE_OAK.defaultBlockState().setValue(BlockPixieHouse.FACING, houseDir.getOpposite());
                            break;
                        case 3:
                            houseState = IafBlockRegistry.PIXIE_HOUSE_BIRCH.defaultBlockState().setValue(BlockPixieHouse.FACING, houseDir.getOpposite());
                            break;
                        case 4:
                            houseState = IafBlockRegistry.PIXIE_HOUSE_SPRUCE.defaultBlockState().setValue(BlockPixieHouse.FACING, houseDir.getOpposite());
                            break;
                        case 5:
                            houseState = IafBlockRegistry.PIXIE_HOUSE_DARK_OAK.defaultBlockState().setValue(BlockPixieHouse.FACING, houseDir.getOpposite());
                            break;
                    }
                    EntityPixie pixie = IafEntityRegistry.PIXIE.get().create(worldIn.getLevel());
                    pixie.finalizeSpawn(worldIn, worldIn.getCurrentDifficultyAt(buildPosition2.above()), SpawnReason.SPAWNER, null, null);
                    pixie.setPos(buildPosition2.getX(), buildPosition2.getY() + 2, buildPosition2.getZ());
                    pixie.setPersistenceRequired();
                    worldIn.addFreshEntity(pixie);

                    worldIn.setBlock(buildPosition2.relative(houseDir).above(), houseState, 2);
                    if (!worldIn.getBlockState(buildPosition2.relative(houseDir)).canOcclude()) {
                        worldIn.setBlock(buildPosition2.relative(houseDir), Blocks.COARSE_DIRT.defaultBlockState(), 2);
                        worldIn.setBlock(buildPosition2.relative(houseDir).below(), Blocks.COARSE_DIRT.defaultBlockState(), 2);
                    }
                }
            }
            buildPosition = buildPosition.relative(buildingDirection, rand.nextInt(roadLength));
            placedRoads++;
        }

        return true;
    }
}
