package com.github.alexthe666.iceandfire.world.feature;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import java.util.function.Supplier;

public class SpawnDragonSkeleton extends Feature<NoneFeatureConfiguration> {

    protected Supplier<EntityType<? extends EntityDragonBase>> dragonType;

    public SpawnDragonSkeleton(Supplier<EntityType<? extends EntityDragonBase>> dt, Codec<NoneFeatureConfiguration> configFactoryIn) {
        super(configFactoryIn);
        dragonType = dt;
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel worldIn = context.level();
        RandomSource rand = context.random();
        BlockPos position = context.origin();

        position = worldIn.getHeightmapPos(Heightmap.Types.WORLD_SURFACE_WG, position.offset(8, 0, 8));

        if (IafConfig.generateDragonSkeletons) {
            if (rand.nextInt(IafConfig.generateDragonSkeletonChance + 1) == 0) {
                EntityDragonBase dragon = dragonType.get().create(worldIn.getLevel());
                dragon.setPos(position.getX() + 0.5F, position.getY() + 1, position.getZ() + 0.5F);
                int dragonage = 10 + rand.nextInt(100);
                dragon.growDragon(dragonage);
                dragon.modelDeadProgress = 20;
                dragon.setModelDead(true);
                dragon.setDeathStage((dragonage / 5) / 2);
                dragon.setYRot(rand.nextInt(360));
                worldIn.addFreshEntity(dragon);
            }
        }

        return true;
    }
}
