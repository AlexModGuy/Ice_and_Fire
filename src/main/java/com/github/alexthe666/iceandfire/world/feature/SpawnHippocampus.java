package com.github.alexthe666.iceandfire.world.feature;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.entity.EntityHippocampus;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.material.Fluids;

public class SpawnHippocampus extends Feature<NoneFeatureConfiguration> {

    public SpawnHippocampus(Codec<NoneFeatureConfiguration> configFactoryIn) {
        super(configFactoryIn);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel worldIn = context.level();
        RandomSource rand = context.random();
        BlockPos position = context.origin();

        position = worldIn.getHeightmapPos(Heightmap.Types.WORLD_SURFACE_WG, position.offset(8, 0, 8));
        BlockPos oceanPos = worldIn.getHeightmapPos(Heightmap.Types.OCEAN_FLOOR_WG, position.offset(8, 0, 8));

        if (rand.nextInt(IafConfig.hippocampusSpawnChance + 1) == 0) {
            for (int i = 0; i < rand.nextInt(5); i++) {
                BlockPos pos = oceanPos.offset(rand.nextInt(10) - 5, rand.nextInt(30), rand.nextInt(10) - 5);
                if (worldIn.getFluidState(pos).getType() == Fluids.WATER) {
                    EntityHippocampus campus = IafEntityRegistry.HIPPOCAMPUS.get().create(worldIn.getLevel());
                    campus.setVariant(rand.nextInt(6));
                    campus.moveTo(pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, 0, 0);
                    worldIn.addFreshEntity(campus);
                }
            }
        }

        return true;
    }
}
