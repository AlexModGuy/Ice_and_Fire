package com.github.alexthe666.iceandfire.world.gen;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.block.BlockGoldPile;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.util.HomePosition;
import com.github.alexthe666.iceandfire.util.WorldUtil;
import com.github.alexthe666.iceandfire.world.IafWorldData;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.Random;
import java.util.stream.Collectors;

public abstract class WorldGenDragonRoosts extends Feature<NoneFeatureConfiguration> implements TypedFeature {
    protected static final Direction[] HORIZONTALS = new Direction[]{Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};

    protected final Block treasureBlock;

    public WorldGenDragonRoosts(final Codec<NoneFeatureConfiguration> configuration, final Block treasureBlock) {
        super(configuration);
        this.treasureBlock = treasureBlock;
    }

    @Override
    public String getId() {
        return "dragon_roost";
    }

    @Override
    public IafWorldData.FeatureType getFeatureType() {
        return IafWorldData.FeatureType.SURFACE;
    }

    @Override
    public boolean place(@NotNull final FeaturePlaceContext<NoneFeatureConfiguration> context) {
        if (!WorldUtil.canGenerate(IafConfig.generateDragonRoostChance, context.level(), context.random(), context.origin(), getId(), true)) {
            return false;
        }

        boolean isMale = new Random().nextBoolean();
        int radius = 12 + context.random().nextInt(8);

        spawnDragon(context, radius, isMale);
        generateSurface(context, radius);
        generateShell(context, radius);
        radius -= 2;
        hollowOut(context, radius);
        radius += 15;
        generateDecoration(context, radius, isMale);

        return true;
    }

    protected void generateRoostPile(final WorldGenLevel level, final RandomSource random, final BlockPos position, final Block block) {
        int radius = random.nextInt(4);

        for (int i = 0; i < radius; i++) {
            int layeredRadius = radius - i;
            double circularArea = getCircularArea(radius);
            BlockPos up = position.above(i);

            for (BlockPos blockpos : BlockPos.betweenClosedStream(up.offset(-layeredRadius, 0, -layeredRadius), up.offset(layeredRadius, 0, layeredRadius)).map(BlockPos::immutable).collect(Collectors.toSet())) {
                if (blockpos.distSqr(position) <= circularArea) {
                    level.setBlock(blockpos, block.defaultBlockState(), Block.UPDATE_CLIENTS);
                }
            }
        }
    }

    protected double getCircularArea(int radius, int height) {
        double area = (radius + height + radius) * 0.333F + 0.5F;
        return Mth.floor(area * area);
    }

    protected double getCircularArea(int radius) {
        double area = (radius + radius) * 0.333F + 0.5F;
        return Mth.floor(area * area);
    }

    protected BlockPos getSurfacePosition(final WorldGenLevel level, final BlockPos position) {
        return level.getHeightmapPos(Heightmap.Types.WORLD_SURFACE_WG, position);
    }

    protected BlockState transform(final Block block) {
        return transform(block.defaultBlockState());
    }

    private void generateDecoration(@NotNull final FeaturePlaceContext<NoneFeatureConfiguration> context, int radius, boolean isMale) {
        int height = (radius / 5);
        double circularArea = getCircularArea(radius, height);

        BlockPos.betweenClosedStream(context.origin().offset(-radius, -height, -radius), context.origin().offset(radius, height, radius)).map(BlockPos::immutable).forEach(position -> {
            if (position.distSqr(context.origin()) <= circularArea) {
                double distance = position.distSqr(context.origin()) / circularArea;

                if (!context.level().isEmptyBlock(context.origin()) && context.random().nextDouble() > distance * 0.5) {
                    BlockState state = context.level().getBlockState(position);

                    if (!(state.getBlock() instanceof BaseEntityBlock) && state.getDestroySpeed(context.level(), position) >= 0) {
                        BlockState transformed = transform(state);

                        if (transformed != state) {
                            context.level().setBlock(position, transformed, Block.UPDATE_CLIENTS);
                        }
                    }
                }

                handleCustomGeneration(context, position, distance);

                if (distance > 0.5 && context.random().nextInt(1000) == 0) {
                    // FIXME
                    new WorldGenRoostBoulder(transform(Blocks.COBBLESTONE).getBlock(), context.random().nextInt(3), true).generate(context.level(), context.random(), getSurfacePosition(context.level(), position));
                }

                if (distance < 0.3 && context.random().nextInt(isMale ? 200 : 300) == 0) {
                    generateTreasurePile(context.level(), context.random(), position);
                }

                if (distance < 0.3D && context.random().nextInt(isMale ? 500 : 700) == 0) {
                    // TODO :: Using non-world-generation since that one does not seem to keep track of blcks we remove / place ourselves (maybe due to Block.UPDATE_CLIENTS usage?)
                    BlockPos surfacePosition = context.level().getHeightmapPos(Heightmap.Types.WORLD_SURFACE, position);
                    boolean wasPlaced = context.level().setBlock(surfacePosition, Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, HORIZONTALS[new Random().nextInt(3)]), Block.UPDATE_CLIENTS);

                    if (wasPlaced) {
                        BlockEntity blockEntity = context.level().getBlockEntity(surfacePosition);

                        if (blockEntity instanceof ChestBlockEntity chest) {
                            chest.setLootTable(getRoostLootTable(), context.random().nextLong());
                        }
                    }
                }

                if (context.random().nextInt(5000) == 0) {
                    // FIXME
                    new WorldGenRoostArch(transform(Blocks.COBBLESTONE).getBlock()).generate(context.level(), context.random(), getSurfacePosition(context.level(), position));
                }
            }
        });
    }

    private void hollowOut(@NotNull final FeaturePlaceContext<NoneFeatureConfiguration> context, int radius) {
        int height = 2;
        double circularArea = getCircularArea(radius, height);
        BlockPos up = context.origin().above(height - 1);

        BlockPos.betweenClosedStream(up.offset(-radius, 0, -radius), up.offset(radius, height, radius)).map(BlockPos::immutable).forEach(position -> {
            if (position.distSqr(context.origin()) <= circularArea) {
                context.level().setBlock(position, Blocks.AIR.defaultBlockState(), Block.UPDATE_CLIENTS);
            }
        });
    }

    private void generateShell(@NotNull final FeaturePlaceContext<NoneFeatureConfiguration> context, int radius) {
        int height = (radius / 5);
        double circularArea = getCircularArea(radius, height);

        BlockPos.betweenClosedStream(context.origin().offset(-radius, -height, -radius), context.origin().offset(radius, 1, radius)).map(BlockPos::immutable).forEach(position -> {
            if (position.distSqr(context.origin()) < circularArea) {
                context.level().setBlock(position, context.random().nextBoolean() ? transform(Blocks.GRAVEL) : transform(Blocks.DIRT), Block.UPDATE_CLIENTS);
            } else if (position.distSqr(context.origin()) == circularArea) {
                context.level().setBlock(position, transform(Blocks.COBBLESTONE), Block.UPDATE_CLIENTS);
            }
        });
    }

    private void generateSurface(@NotNull final FeaturePlaceContext<NoneFeatureConfiguration> context, int radius) {
        int height = 2;
        double circularArea = getCircularArea(radius, height);

        BlockPos.betweenClosedStream(context.origin().offset(-radius, height, -radius), context.origin().offset(radius, 0, radius)).map(BlockPos::immutable).forEach(position -> {
            int heightDifference = position.getY() - context.origin().getY();

            if (position.distSqr(context.origin()) <= circularArea && heightDifference < 2 + context.random().nextInt(height) && !context.level().isEmptyBlock(position.below())) {
                if (context.level().isEmptyBlock(position.above())) {
                    context.level().setBlock(position, transform(Blocks.GRASS), Block.UPDATE_CLIENTS);
                } else {
                    // TODO :: Usually not much / anything of this survives the next generation steps
                    context.level().setBlock(position, transform(Blocks.DIRT), Block.UPDATE_CLIENTS);
                }
            }
        });
    }

    private void generateTreasurePile(final WorldGenLevel level, final RandomSource random, final BlockPos origin) {
        int layers = random.nextInt(3);

        for (int i = 0; i < layers; i++) {
            int radius = layers - i;
            double circularArea = getCircularArea(radius);

            for (BlockPos position : BlockPos.betweenClosedStream(origin.offset(-radius, i, -radius), origin.offset(radius, i, radius)).map(BlockPos::immutable).collect(Collectors.toSet())) {
                if (position.distSqr(origin) <= circularArea) {
                    position = level.getHeightmapPos(Heightmap.Types.WORLD_SURFACE, position);

                    if (treasureBlock instanceof BlockGoldPile) {
                        BlockState state = level.getBlockState(position);
                        boolean placed = false;

                        if (state.isAir()) {
                            level.setBlock(position, treasureBlock.defaultBlockState().setValue(BlockGoldPile.LAYERS, 1 + random.nextInt(7)), Block.UPDATE_CLIENTS);
                            placed = true;
                        } else if (state.getBlock() instanceof SnowLayerBlock) {
                            level.setBlock(position.below(), treasureBlock.defaultBlockState().setValue(BlockGoldPile.LAYERS, state.getValue(SnowLayerBlock.LAYERS)), Block.UPDATE_CLIENTS);
                            placed = true;
                        }

                        if (placed && level.getBlockState(position.below()).getBlock() instanceof BlockGoldPile) {
                            level.setBlock(position.below(), treasureBlock.defaultBlockState().setValue(BlockGoldPile.LAYERS, 8), Block.UPDATE_CLIENTS);
                        }
                    }
                }
            }
        }
    }

    private void spawnDragon(@NotNull final FeaturePlaceContext<NoneFeatureConfiguration> context, int ageOffset, boolean isMale) {
        EntityDragonBase dragon = getDragonType().create(context.level().getLevel());
        dragon.setGender(isMale);
        dragon.growDragon(40 + ageOffset);
        dragon.setAgingDisabled(true);
        dragon.setHealth(dragon.getMaxHealth());
        dragon.setVariant(new Random().nextInt(4));
        dragon.absMoveTo(context.origin().getX() + 0.5, context.level().getHeightmapPos(Heightmap.Types.WORLD_SURFACE_WG, context.origin()).getY() + 1.5, context.origin().getZ() + 0.5, context.random().nextFloat() * 360, 0);
        dragon.homePos = new HomePosition(context.origin(), context.level().getLevel());
        dragon.hasHomePosition = true;
        dragon.setHunger(50);
        context.level().addFreshEntity(dragon);
    }

    protected abstract EntityType<? extends EntityDragonBase> getDragonType();

    protected abstract ResourceLocation getRoostLootTable();

    protected abstract BlockState transform(final BlockState block);

    protected abstract void handleCustomGeneration(@NotNull final FeaturePlaceContext<NoneFeatureConfiguration> context, final BlockPos position, double distance);
}
