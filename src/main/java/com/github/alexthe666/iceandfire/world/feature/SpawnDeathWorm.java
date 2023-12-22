package com.github.alexthe666.iceandfire.world.feature;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.entity.EntityDeathWorm;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import com.github.alexthe666.iceandfire.world.IafWorldRegistry;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class SpawnDeathWorm extends Feature<NoneFeatureConfiguration> {

    public SpawnDeathWorm(Codec<NoneFeatureConfiguration> configFactoryIn) {
        super(configFactoryIn);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel worldIn = context.level();
        RandomSource rand = context.random();
        BlockPos position = context.origin();
        position = worldIn.getHeightmapPos(Heightmap.Types.WORLD_SURFACE_WG, position.offset(8, 0, 8));

        if (IafWorldRegistry.isFarEnoughFromSpawn(worldIn, position)) {
            if (rand.nextInt(IafConfig.deathWormSpawnRate + 1) == 0) {
                EntityDeathWorm deathWorm = IafEntityRegistry.DEATH_WORM.get().create(worldIn.getLevel());
                deathWorm.setPos(position.getX() + 0.5F, position.getY() + 1, position.getZ() + 0.5F);
                deathWorm.finalizeSpawn(worldIn, worldIn.getCurrentDifficultyAt(position), MobSpawnType.CHUNK_GENERATION, null, null);
                worldIn.addFreshEntity(deathWorm);
            }
        }

        return true;
    }
}
