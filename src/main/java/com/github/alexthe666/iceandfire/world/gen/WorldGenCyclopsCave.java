package com.github.alexthe666.iceandfire.world.gen;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.block.BlockGoldPile;
import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.entity.EntityCyclops;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import com.github.alexthe666.iceandfire.util.WorldUtil;
import com.github.alexthe666.iceandfire.world.IafWorldData;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import java.util.Random;
import java.util.stream.Collectors;

public class WorldGenCyclopsCave extends Feature<NoneFeatureConfiguration> implements TypedFeature {
    public static final ResourceLocation CYCLOPS_CHEST = new ResourceLocation("iceandfire", "chest/cyclops_cave");
    private static final Direction[] HORIZONTALS = new Direction[]{Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};

    public WorldGenCyclopsCave(final Codec<NoneFeatureConfiguration> configuration) {
        super(configuration);
    }

    @Override
    public boolean place(final FeaturePlaceContext<NoneFeatureConfiguration> context) {
        if (!WorldUtil.canGenerate(IafConfig.spawnCyclopsCaveChance, context.level(), context.random(), context.origin(), getId(), true)) {
            return false;
        }

        int size = 16;
        int distance = 6;

        // Unsure :: Checks if the corners of the feature are on solid ground to make sure it doesn't float
        if (context.level().isEmptyBlock(context.origin().offset(size - distance, -3, -size + distance)) || context.level().isEmptyBlock(context.origin().offset(size - distance, -3, size - distance)) || context.level().isEmptyBlock(context.origin().offset(-size + distance, -3, -size + distance)) || context.level().isEmptyBlock(context.origin().offset(-size + distance, -3, size - distance))) {
            return false;
        }

        generateShell(context, size);

        int innerSize = size - 2;
        int x = innerSize + context.random().nextInt(2);
        int y = 10 + context.random().nextInt(2);
        int z = innerSize + context.random().nextInt(2);
        float radius = (x + y + z) * 0.333F + 0.5F;

        int sheepPenCount = 0;

        // Clear out the area
        for (BlockPos position : BlockPos.betweenClosedStream(context.origin().offset(-x, -y, -z), context.origin().offset(x, y, z)).map(BlockPos::immutable).collect(Collectors.toSet())) {
            if (position.distSqr(context.origin()) <= radius * radius && position.getY() > context.origin().getY()) {
                if (!(context.level().getBlockState(context.origin()).getBlock() instanceof AbstractChestBlock)) {
                    context.level().setBlock(position, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
                }
            }
        }

        // Set up the actual content
        for (BlockPos position : BlockPos.betweenClosedStream(context.origin().offset(-x, -y, -z), context.origin().offset(x, y, z)).map(BlockPos::immutable).collect(Collectors.toSet())) {
            if (position.distSqr(context.origin()) <= radius * radius && position.getY() == context.origin().getY()) {
                if (context.random().nextInt(130) == 0 && isTouchingAir(context.level(), position.above())) {
                    generateSkeleton(context.level(), position.above(), context.random(), context.origin(), radius);
                }

                if (context.random().nextInt(130) == 0 && position.distSqr(context.origin()) <= (double) (radius * radius) * 0.8F && sheepPenCount < 2) {
                    generateSheepPen(context.level(), position.above(), context.random(), context.origin(), radius);
                    sheepPenCount++;
                }

                if (context.random().nextInt(80) == 0 && isTouchingAir(context.level(), position.above())) {
                    context.level().setBlock(position.above(), IafBlockRegistry.GOLD_PILE.get().defaultBlockState().setValue(BlockGoldPile.LAYERS, 8), 3);
                    context.level().setBlock(position.above().north(), IafBlockRegistry.GOLD_PILE.get().defaultBlockState().setValue(BlockGoldPile.LAYERS, 1 + new Random().nextInt(7)), 3);
                    context.level().setBlock(position.above().south(), IafBlockRegistry.GOLD_PILE.get().defaultBlockState().setValue(BlockGoldPile.LAYERS, 1 + new Random().nextInt(7)), 3);
                    context.level().setBlock(position.above().west(), IafBlockRegistry.GOLD_PILE.get().defaultBlockState().setValue(BlockGoldPile.LAYERS, 1 + new Random().nextInt(7)), 3);
                    context.level().setBlock(position.above().east(), IafBlockRegistry.GOLD_PILE.get().defaultBlockState().setValue(BlockGoldPile.LAYERS, 1 + new Random().nextInt(7)), 3);
                    context.level().setBlock(position.above(2), Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, HORIZONTALS[new Random().nextInt(3)]), 2);

                    if (context.level().getBlockState(position.above(2)).getBlock() instanceof AbstractChestBlock) {
                        BlockEntity blockEntity = context.level().getBlockEntity(position.above(2));

                        if (blockEntity instanceof ChestBlockEntity chestBlockEntity) {
                            chestBlockEntity.setLootTable(CYCLOPS_CHEST, context.random().nextLong());
                        }
                    }
                }

                if (context.random().nextInt(50) == 0 && isTouchingAir(context.level(), position.above())) {
                    int torchHeight = context.random().nextInt(2) + 1;

                    for (int fence = 0; fence < torchHeight; fence++) {
                        context.level().setBlock(position.above(1 + fence), getFenceState(context.level(), position.above(1 + fence)), 3);
                    }

                    context.level().setBlock(position.above(1 + torchHeight), Blocks.TORCH.defaultBlockState(), 2);
                }
            }
        }

        EntityCyclops cyclops = IafEntityRegistry.CYCLOPS.get().create(context.level().getLevel());
        cyclops.absMoveTo(context.origin().getX() + 0.5, context.origin().getY() + 1.5, context.origin().getZ() + 0.5, context.random().nextFloat() * 360, 0);
        // TODO :: Finalize spawn?
        context.level().addFreshEntity(cyclops);

        return true;
    }

    private static void generateShell(final FeaturePlaceContext<NoneFeatureConfiguration> context, int size) {
        int x = size + context.random().nextInt(2);
        int y = 12 + context.random().nextInt(2);
        int z = size + context.random().nextInt(2);
        float radius = (x + y + z) * 0.333F + 0.5F;

        for (BlockPos position : BlockPos.betweenClosedStream(context.origin().offset(-x, -y, -z), context.origin().offset(x, y, z)).map(BlockPos::immutable).collect(Collectors.toSet())) {
            boolean doorwayX = position.getX() >= context.origin().getX() - 2 + context.random().nextInt(2) && position.getX() <= context.origin().getX() + 2 + context.random().nextInt(2);
            boolean doorwayZ = position.getZ() >= context.origin().getZ() - 2 + context.random().nextInt(2) && position.getZ() <= context.origin().getZ() + 2 + context.random().nextInt(2);
            boolean isNotInDoorway = !doorwayX && !doorwayZ && position.getY() > context.origin().getY() || position.getY() > context.origin().getY() + y - (3 + context.random().nextInt(2));

            if (position.distSqr(context.origin()) <= radius * radius) {
                BlockState state = context.level().getBlockState(position);

                if (!(state.getBlock() instanceof AbstractChestBlock) && state.getDestroySpeed(context.level(), position) >= 0 && isNotInDoorway) {
                    context.level().setBlock(position, Blocks.STONE.defaultBlockState(), Block.UPDATE_ALL);
                }
                if (position.getY() == context.origin().getY()) {
                    context.level().setBlock(position, Blocks.MOSSY_COBBLESTONE.defaultBlockState(), Block.UPDATE_ALL);
                }
                if (position.getY() <= context.origin().getY() - 1 && !state.canOcclude()) {
                    context.level().setBlock(position, Blocks.COBBLESTONE.defaultBlockState(), Block.UPDATE_ALL);
                }
            }
        }
    }

    private void generateSheepPen(final ServerLevelAccessor level, final BlockPos position, final RandomSource random, final BlockPos origin, float radius) {
        int width = 5 + random.nextInt(3);
        int sheepAmount = 2 + random.nextInt(3);
        Direction direction = Direction.NORTH;
        BlockPos end = position;

        for (int sideCount = 0; sideCount < 4; sideCount++) {
            for (int side = 0; side < width; side++) {
                BlockPos relativePosition = end.relative(direction, side);

                if (origin.distSqr(relativePosition) <= radius * radius) {
                     level.setBlock(relativePosition, getFenceState(level, relativePosition), Block.UPDATE_ALL);

                    if (level.isEmptyBlock(relativePosition.relative(direction.getClockWise())) && sheepAmount > 0) {
                        BlockPos sheepPos = relativePosition.relative(direction.getClockWise());

                        Sheep sheep = new Sheep(EntityType.SHEEP, level.getLevel());
                        sheep.setPos(sheepPos.getX() + 0.5F, sheepPos.getY() + 0.5F, sheepPos.getZ() + 0.5F);
                        sheep.setColor(random.nextInt(4) == 0 ? DyeColor.YELLOW : DyeColor.WHITE);
                        level.addFreshEntity(sheep);

                        sheepAmount--;
                    }
                }
            }

            end = end.relative(direction, width);
            direction = direction.getClockWise();
        }

        // Go through the fence blocks again and make sure they're properly connected to each other
        for (int sideCount = 0; sideCount < 4; sideCount++) {
            for (int side = 0; side < width; side++) {
                BlockPos relativePosition = end.relative(direction, side);

                if (origin.distSqr(relativePosition) <= radius * radius) {
                     level.setBlock(relativePosition, getFenceState(level, relativePosition), Block.UPDATE_ALL);
                }
            }

            end = end.relative(direction, width);
            direction = direction.getClockWise();
        }
    }

    private void generateSkeleton(final LevelAccessor level, final BlockPos position, final RandomSource random, final BlockPos origin, float radius) {
        Direction direction = HORIZONTALS[new Random().nextInt(3)];
        Direction.Axis oppositeAxis = direction.getAxis() == Direction.Axis.X ? Direction.Axis.Z : Direction.Axis.X;
        int maxRibHeight = random.nextInt(2);

        for (int spine = 0; spine < 5 + random.nextInt(2) * 2; spine++) {
            BlockPos segment = position.relative(direction, spine);

            if (origin.distSqr(segment) <= radius * radius) {
                level.setBlock(segment, Blocks.BONE_BLOCK.defaultBlockState().setValue(RotatedPillarBlock.AXIS, direction.getAxis()), 2);
            }

            if (spine % 2 != 0) {
                BlockPos rightRib = segment.relative(direction.getCounterClockWise());
                BlockPos leftRib = segment.relative(direction.getClockWise());

                if (origin.distSqr(rightRib) <= radius * radius) {
                    level.setBlock(rightRib, Blocks.BONE_BLOCK.defaultBlockState().setValue(RotatedPillarBlock.AXIS, oppositeAxis), 2);
                }

                if (origin.distSqr(leftRib) <= radius * radius) {
                    level.setBlock(leftRib, Blocks.BONE_BLOCK.defaultBlockState().setValue(RotatedPillarBlock.AXIS, oppositeAxis), 2);
                }

                for (int ribHeight = 1; ribHeight < maxRibHeight + 2; ribHeight++) {
                    if (origin.distSqr(rightRib.above(ribHeight).relative(direction.getCounterClockWise())) <= radius * radius) {
                        level.setBlock(rightRib.above(ribHeight).relative(direction.getCounterClockWise()), Blocks.BONE_BLOCK.defaultBlockState().setValue(RotatedPillarBlock.AXIS, Direction.Axis.Y), Block.UPDATE_CLIENTS);
                    }

                    if (origin.distSqr(leftRib.above(ribHeight).relative(direction.getClockWise())) <= radius * radius) {
                        level.setBlock(leftRib.above(ribHeight).relative(direction.getClockWise()), Blocks.BONE_BLOCK.defaultBlockState().setValue(RotatedPillarBlock.AXIS, Direction.Axis.Y), Block.UPDATE_CLIENTS);
                    }
                }

                if (origin.distSqr(rightRib.above(maxRibHeight + 2)) <= radius * radius) {
                    level.setBlock(rightRib.above(maxRibHeight + 2), Blocks.BONE_BLOCK.defaultBlockState().setValue(RotatedPillarBlock.AXIS, oppositeAxis), Block.UPDATE_CLIENTS);
                }

                if (origin.distSqr(leftRib.above(maxRibHeight + 2)) <= radius * radius) {
                    level.setBlock(leftRib.above(maxRibHeight + 2), Blocks.BONE_BLOCK.defaultBlockState().setValue(RotatedPillarBlock.AXIS, oppositeAxis), Block.UPDATE_CLIENTS);
                }
            }

        }
    }

    private boolean isTouchingAir(final LevelAccessor level, final BlockPos position) {
        boolean isTouchingAir = true;

        for (Direction direction : HORIZONTALS) {
            if (!level.isEmptyBlock(position.relative(direction))) {
                isTouchingAir = false;
            }
        }

        return isTouchingAir;
    }

    private BlockState getFenceState(final LevelAccessor level, final BlockPos position) {
        boolean east = level.getBlockState(position.east()).getBlock() == Blocks.OAK_FENCE;
        boolean west = level.getBlockState(position.west()).getBlock() == Blocks.OAK_FENCE;
        boolean north = level.getBlockState(position.north()).getBlock() == Blocks.OAK_FENCE;
        boolean south = level.getBlockState(position.south()).getBlock() == Blocks.OAK_FENCE;

        return Blocks.OAK_FENCE.defaultBlockState().setValue(FenceBlock.EAST, east).setValue(FenceBlock.WEST, west).setValue(FenceBlock.NORTH, north).setValue(FenceBlock.SOUTH, south);
    }

    @Override
    public IafWorldData.FeatureType getFeatureType() {
        return IafWorldData.FeatureType.SURFACE;
    }

    @Override
    public String getId() {
        return "cyclops_cave";
    }
}
