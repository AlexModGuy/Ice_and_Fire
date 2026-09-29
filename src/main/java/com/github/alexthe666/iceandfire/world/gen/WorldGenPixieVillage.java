package com.github.alexthe666.iceandfire.world.gen;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.block.BlockPixieHouse;
import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.entity.EntityPixie;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import com.github.alexthe666.iceandfire.world.IafWorldData;
import com.github.alexthe666.iceandfire.world.IafWorldRegistry;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class WorldGenPixieVillage extends Feature<NoneFeatureConfiguration> implements TypedFeature {
    public WorldGenPixieVillage(Codec<NoneFeatureConfiguration> configFactoryIn) {
        super(configFactoryIn);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel worldIn = context.level();
        RandomSource rand = context.random();
        BlockPos position = context.origin();

        if (rand.nextInt(IafConfig.spawnPixiesChance) != 0 || !IafWorldRegistry.isFarEnoughFromSpawn(worldIn, position)) {
            return false;
        }

        int maxRoads = IafConfig.pixieVillageSize + rand.nextInt(5);
        BlockPos buildPosition = position;
        int placedRoads = 0;
        while(placedRoads < maxRoads){
            int roadLength = 10 + rand.nextInt(15);
            Direction buildingDirection = Direction.from2DDataValue(rand.nextInt(3));
            for(int i = 0; i < roadLength; i++) {
                BlockPos buildPosition2 = buildPosition.relative(buildingDirection, i);
                buildPosition2 = worldIn.getHeightmapPos(Heightmap.Types.WORLD_SURFACE_WG, buildPosition2).below();
                if (worldIn.getBlockState(buildPosition2).getFluidState().isEmpty()) {
                    worldIn.setBlock(buildPosition2, Blocks.DIRT_PATH.defaultBlockState(), 2);
                } else {
                    worldIn.setBlock(buildPosition2, Blocks.SPRUCE_PLANKS.defaultBlockState(), 2);
                }
                if (rand.nextInt(8) == 0) {
                    Direction houseDir = rand.nextBoolean() ? buildingDirection.getClockWise() : buildingDirection.getCounterClockWise();
                    BlockState houseState = IafBlockRegistry.PIXIE_HOUSE_OAK.get().defaultBlockState();
                    int houseColor = rand.nextInt(5);
                    houseState = switch (houseColor) {
                        case 0 -> IafBlockRegistry.PIXIE_HOUSE_MUSHROOM_RED.get().defaultBlockState().setValue(BlockPixieHouse.FACING, houseDir.getOpposite());
                        case 1 -> IafBlockRegistry.PIXIE_HOUSE_MUSHROOM_BROWN.get().defaultBlockState().setValue(BlockPixieHouse.FACING, houseDir.getOpposite());
                        case 2 -> IafBlockRegistry.PIXIE_HOUSE_OAK.get().defaultBlockState().setValue(BlockPixieHouse.FACING, houseDir.getOpposite());
                        case 3 -> IafBlockRegistry.PIXIE_HOUSE_BIRCH.get().defaultBlockState().setValue(BlockPixieHouse.FACING, houseDir.getOpposite());
                        case 4 -> IafBlockRegistry.PIXIE_HOUSE_SPRUCE.get().defaultBlockState().setValue(BlockPixieHouse.FACING, houseDir.getOpposite());
                        case 5 -> IafBlockRegistry.PIXIE_HOUSE_DARK_OAK.get().defaultBlockState().setValue(BlockPixieHouse.FACING, houseDir.getOpposite());
                        default -> houseState;
                    };
                    EntityPixie pixie = IafEntityRegistry.PIXIE.get().create(worldIn.getLevel());
                    pixie.finalizeSpawn(worldIn, getDifficultyAt(worldIn, buildPosition2.above()), MobSpawnType.SPAWNER, null, null);
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

    private static DifficultyInstance getDifficultyAt(final WorldGenLevel level, BlockPos position) {
        // Regions throw for positions outside of their chunks (roads can lead there) even though the difficulty they return does not depend on the position
        if (level instanceof WorldGenRegion region && !region.hasChunk(position.getX() >> 4, position.getZ() >> 4)) {
            position = region.getCenter().getWorldPosition();
        }

        return level.getCurrentDifficultyAt(position);
    }

    @Override
    public IafWorldData.FeatureType getFeatureType() {
        return IafWorldData.FeatureType.SURFACE;
    }

    @Override
    public String getId() {
        return "pixie_village";
    }
}
