package com.github.alexthe666.iceandfire.world.gen;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.entity.EntitySiren;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import com.github.alexthe666.iceandfire.util.WorldUtil;
import com.github.alexthe666.iceandfire.world.IafWorldData;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class WorldGenSirenIsland extends Feature<NoneFeatureConfiguration> implements TypedFeature {

    private final int MAX_ISLAND_RADIUS = 10;
    public WorldGenSirenIsland(final Codec<NoneFeatureConfiguration> configuration) {
        super(configuration);
    }

    @Override
    public boolean place(final FeaturePlaceContext<NoneFeatureConfiguration> context) {
        if (!WorldUtil.canGenerate(IafConfig.generateSirenChance, context.level(), context.random(), context.origin(), getId(), false)) {
            return false;
        }

        int up = context.random().nextInt(4) + 1;
        BlockPos center = context.origin().above(up);
        int layer = 0;
        int sirens = 1 + context.random().nextInt(3);

        while (!context.level().getBlockState(center).canOcclude() && center.getY() >= context.level().getMinBuildHeight()) {
            layer++;

            for (float i = 0; i < getRadius(layer, up); i += 0.5f) {
                for (float j = 0; j < 2 * Math.PI * i + context.random().nextInt(2); j += 0.5f) {
                    BlockPos stonePos = BlockPos.containing(Math.floor(center.getX() + Mth.sin(j) * i + context.random().nextInt(2)), center.getY(), Math.floor(center.getZ() + Mth.cos(j) * i + context.random().nextInt(2)));
                    context.level().setBlock(stonePos, getStone(context.random()), Block.UPDATE_ALL);
                    BlockPos upPos = stonePos.above();

                    if (context.level().isEmptyBlock(upPos) && context.level().isEmptyBlock(upPos.east()) && context.level().isEmptyBlock(upPos.north()) && context.level().isEmptyBlock(upPos.north().east()) && context.random().nextInt(3) == 0 && sirens > 0) {
                        spawnSiren(context.level(), context.random(), upPos.north().east());
                        sirens--;
                    }
                }
            }

            center = center.below();
        }

        layer++;

        for (float i = 0; i < getRadius(layer, up); i += 0.5f) {
            for (float j = 0; j < 2 * Math.PI * i + context.random().nextInt(2); j += 0.5f) {
                BlockPos stonePos = BlockPos.containing(Math.floor(center.getX() + Mth.sin(j) * i + context.random().nextInt(2)), center.getY(), Math.floor(center.getZ() + Mth.cos(j) * i + context.random().nextInt(2)));

                while (!context.level().getBlockState(stonePos).canOcclude() && stonePos.getY() >= 0) {
                    context.level().setBlock(stonePos, getStone(context.random()), Block.UPDATE_ALL);
                    stonePos = stonePos.below();
                }
            }
        }

        return true;
    }

    private int getRadius(int layer, int up) {
        return layer > up ? (int) (layer * 0.25) + up : Math.min(layer, MAX_ISLAND_RADIUS);
    }

    private BlockState getStone(RandomSource random) {
        int chance = random.nextInt(100);
        if (chance > 90) {
            return Blocks.MOSSY_COBBLESTONE.defaultBlockState();
        } else if (chance > 70) {
            return Blocks.GRAVEL.defaultBlockState();
        } else if (chance > 45) {
            return Blocks.COBBLESTONE.defaultBlockState();
        } else {
            return Blocks.STONE.defaultBlockState();
        }
    }

    private void spawnSiren(ServerLevelAccessor worldIn, RandomSource rand, BlockPos position) {
        EntitySiren siren = new EntitySiren(IafEntityRegistry.SIREN.get(), worldIn.getLevel());
        siren.setSinging(true);
        siren.setHairColor(rand.nextInt(2));
        siren.setSingingPose(rand.nextInt(2));
        siren.absMoveTo(position.getX() + 0.5D, position.getY() + 1, position.getZ() + 0.5D, rand.nextFloat() * 360, 0);
        worldIn.addFreshEntity(siren);
    }

    @Override
    public IafWorldData.FeatureType getFeatureType() {
        return IafWorldData.FeatureType.OCEAN;
    }

    @Override
    public String getId() {
        return "siren_island";
    }
}
