package com.github.alexthe666.iceandfire.world.gen;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.block.BlockGoldPile;
import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.entity.EntityCyclops;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import com.github.alexthe666.iceandfire.world.IafWorldRegistry;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import java.util.Random;
import java.util.stream.Collectors;

public class WorldGenCyclopsCave extends Feature<NoneFeatureConfiguration> {

    public static final ResourceLocation CYCLOPS_CHEST = new ResourceLocation("iceandfire", "chest/cyclops_cave");
    private static final Direction[] HORIZONTALS = new Direction[]{Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};

    public WorldGenCyclopsCave(Codec<NoneFeatureConfiguration> configFactoryIn) {
        super(configFactoryIn);
    }

    private void genSheepPen(ServerLevelAccessor worldIn, BlockPos blockpos, Random rand, BlockPos origin, float radius) {

        int width = 5 + rand.nextInt(3);
        int sheeps = 2 + rand.nextInt(3);
        int sheepsSpawned = 0;
        Direction direction = Direction.NORTH;
        BlockPos end = blockpos;
        for (int sideCount = 0; sideCount < 4; sideCount++) {
            for (int side = 0; side < width; side++) {
                if (origin.distSqr(end.relative(direction, side)) <= radius * radius) {
                    worldIn.setBlock(end.relative(direction, side), getFenceState(worldIn, end.relative(direction, side)), 3);
                    if (worldIn.isEmptyBlock(end.relative(direction, side).relative(direction.getClockWise())) && sheepsSpawned < sheeps) {
                        BlockPos sheepPos = end.relative(direction, side).relative(direction.getClockWise());

                        Sheep entitySheep = new Sheep(EntityType.SHEEP, worldIn.getLevel());
                        entitySheep.setPos(sheepPos.getX() + 0.5F, sheepPos.getY() + 0.5F, sheepPos.getZ() + 0.5F);
                        entitySheep.setColor(rand.nextInt(4) == 0 ? DyeColor.YELLOW : DyeColor.WHITE);
                        worldIn.addFreshEntity(entitySheep);

                        sheepsSpawned++;
                    }
                }
            }
            end = end.relative(direction, width);
            direction = direction.getClockWise();
        }
        for (int sideCount = 0; sideCount < 4; sideCount++) {
            for (int side = 0; side < width; side++) {
                if (origin.distSqr(end.relative(direction, side)) <= radius * radius) {
                    worldIn.setBlock(end.relative(direction, side), getFenceState(worldIn, end.relative(direction, side)), 3);
                }
            }
            end = end.relative(direction, width);
            direction = direction.getClockWise();
        }
        for (int x = 1; x < width - 1; x++) {
            for (int z = 1; z < width - 1; z++) {
                if (origin.distSqr(end.offset(x, 0, z)) <= radius * radius) {
                    worldIn.setBlock(end.offset(x, 0, z), Blocks.AIR.defaultBlockState(), 2);
                }
            }
        }
    }

    private boolean isTouchingAir(LevelAccessor worldIn, BlockPos pos) {
        boolean isTouchingAir = true;
        for (Direction direction : HORIZONTALS) {
            if (!worldIn.isEmptyBlock(pos.relative(direction))) {
                isTouchingAir = false;
            }
        }
        return isTouchingAir;
    }

    private void genSkeleton(LevelAccessor worldIn, BlockPos blockpos, Random rand, BlockPos origin, float radius) {
        Direction direction = HORIZONTALS[new Random().nextInt(3)];
        Direction.Axis oppositeAxis = direction.getAxis() == Direction.Axis.X ? Direction.Axis.Z : Direction.Axis.X;
        int maxRibHeight = rand.nextInt(2);
        for (int spine = 0; spine < 5 + rand.nextInt(2) * 2; spine++) {
            BlockPos segment = blockpos.relative(direction, spine);
            if (origin.distSqr(segment) <= radius * radius) {
                worldIn.setBlock(segment, Blocks.BONE_BLOCK.defaultBlockState().setValue(RotatedPillarBlock.AXIS, direction.getAxis()), 2);
            }
            if (spine % 2 != 0) {
                BlockPos rightRib = segment.relative(direction.getCounterClockWise());
                BlockPos leftRib = segment.relative(direction.getClockWise());
                if (origin.distSqr(rightRib) <= radius * radius) {
                    worldIn.setBlock(rightRib, Blocks.BONE_BLOCK.defaultBlockState().setValue(RotatedPillarBlock.AXIS, oppositeAxis), 2);
                }
                if (origin.distSqr(leftRib) <= radius * radius) {
                    worldIn.setBlock(leftRib, Blocks.BONE_BLOCK.defaultBlockState().setValue(RotatedPillarBlock.AXIS, oppositeAxis), 2);
                }
                for (int ribHeight = 1; ribHeight < maxRibHeight + 2; ribHeight++) {
                    if (origin.distSqr(rightRib.above(ribHeight).relative(direction.getCounterClockWise())) <= radius * radius) {
                        worldIn.setBlock(rightRib.above(ribHeight).relative(direction.getCounterClockWise()), Blocks.BONE_BLOCK.defaultBlockState().setValue(RotatedPillarBlock.AXIS, Direction.Axis.Y), 2);
                    }
                    if (origin.distSqr(leftRib.above(ribHeight).relative(direction.getClockWise())) <= radius * radius) {
                        worldIn.setBlock(leftRib.above(ribHeight).relative(direction.getClockWise()), Blocks.BONE_BLOCK.defaultBlockState().setValue(RotatedPillarBlock.AXIS, Direction.Axis.Y), 2);
                    }
                }
                if (origin.distSqr(rightRib.above(maxRibHeight + 2)) <= radius * radius) {
                    worldIn.setBlock(rightRib.above(maxRibHeight + 2), Blocks.BONE_BLOCK.defaultBlockState().setValue(RotatedPillarBlock.AXIS, oppositeAxis), 2);
                }
                if (origin.distSqr(leftRib.above(maxRibHeight + 2)) <= radius * radius) {
                    worldIn.setBlock(leftRib.above(maxRibHeight + 2), Blocks.BONE_BLOCK.defaultBlockState().setValue(RotatedPillarBlock.AXIS, oppositeAxis), 2);
                }
            }

        }
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel worldIn = context.level();
        Random rand = context.random();
        BlockPos position = context.origin();
        if (!IafWorldRegistry.isDimensionListedForFeatures(worldIn)) {
            return false;
        }
        if (!IafConfig.generateCyclopsCaves || rand.nextInt(IafConfig.spawnCyclopsCaveChance) != 0 || !IafWorldRegistry.isFarEnoughFromSpawn(worldIn, position)) {
            return false;
        }
        position = worldIn.getHeightmapPos(Heightmap.Types.WORLD_SURFACE_WG, position);
        if (!worldIn.getFluidState(position.below()).isEmpty()) {
            return false;
        }
        int i1 = 16;
        int i2 = i1 - 2;
        int sheepPenCount = 0;
        int dist = 6;
        if (worldIn.isEmptyBlock(position.offset(i1 - dist, -3, -i1 + dist)) || worldIn.isEmptyBlock(position.offset(i1 - dist, -3, i1 - dist)) || worldIn.isEmptyBlock(position.offset(-i1 + dist, -3, -i1 + dist)) || worldIn.isEmptyBlock(position.offset(-i1 + dist, -3, i1 - dist))) {
            return false;
        }

        {
            int ySize = rand.nextInt(2);
            int j = i1 + rand.nextInt(2);
            int k = 12 + ySize;
            int l = i1 + rand.nextInt(2);
            float f = (j + k + l) * 0.333F + 0.5F;


            for (BlockPos blockpos : BlockPos.betweenClosedStream(position.offset(-j, -k, -l), position.offset(j, k, l)).map(BlockPos::immutable).collect(Collectors.toSet())) {
                boolean doorwayX = blockpos.getX() >= position.getX() - 2 + rand.nextInt(2) && blockpos.getX() <= position.getX() + 2 + rand.nextInt(2);
                boolean doorwayZ = blockpos.getZ() >= position.getZ() - 2 + rand.nextInt(2) && blockpos.getZ() <= position.getZ() + 2 + rand.nextInt(2);
                boolean isNotInDoorway = !doorwayX && !doorwayZ && blockpos.getY() > position.getY() || blockpos.getY() > position.getY() + k - (3 + rand.nextInt(2));
                if (blockpos.distSqr(position) <= f * f) {
                    if (!(worldIn.getBlockState(position).getBlock() instanceof AbstractChestBlock) && worldIn.getBlockState(position).getDestroySpeed(worldIn, position) >= 0 && isNotInDoorway) {
                        worldIn.setBlock(blockpos, Blocks.STONE.defaultBlockState(), 3);
                    }
                    if (blockpos.getY() == position.getY()) {
                        worldIn.setBlock(blockpos, Blocks.MOSSY_COBBLESTONE.defaultBlockState(), 3);
                    }
                    if (blockpos.getY() <= position.getY() - 1 && !worldIn.getBlockState(blockpos).canOcclude()) {
                        worldIn.setBlock(blockpos, Blocks.COBBLESTONE.defaultBlockState(), 3);
                    }
                }
            }


        }
        {
            int ySize = rand.nextInt(2);
            int j = i2 + rand.nextInt(2);
            int k = 10 + ySize;
            int l = i2 + rand.nextInt(2);
            float f = (j + k + l) * 0.333F + 0.5F;
            for (BlockPos blockpos : BlockPos.betweenClosedStream(position.offset(-j, -k, -l), position.offset(j, k, l)).map(BlockPos::immutable).collect(Collectors.toSet())) {
                if (blockpos.distSqr(position) <= f * f && blockpos.getY() > position.getY()) {
                    if (!(worldIn.getBlockState(position).getBlock() instanceof AbstractChestBlock)) {
                        worldIn.setBlock(blockpos, Blocks.AIR.defaultBlockState(), 3);

                    }
                }
            }
            for (BlockPos blockpos : BlockPos.betweenClosedStream(position.offset(-j, -k, -l), position.offset(j, k, l)).map(BlockPos::immutable).collect(Collectors.toSet())) {
                if (blockpos.distSqr(position) <= f * f && blockpos.getY() == position.getY()) {
                    if (rand.nextInt(130) == 0 && isTouchingAir(worldIn, blockpos.above())) {
                        this.genSkeleton(worldIn, blockpos.above(), rand, position, f);
                    }

                    if (rand.nextInt(130) == 0 && blockpos.distSqr(position) <= (double) (f * f) * 0.8F && sheepPenCount < 2) {
                        this.genSheepPen(worldIn, blockpos.above(), rand, position, f);
                        sheepPenCount++;
                    }
                    if (rand.nextInt(80) == 0 && isTouchingAir(worldIn, blockpos.above())) {
                        worldIn.setBlock(blockpos.above(), IafBlockRegistry.GOLD_PILE.get().defaultBlockState().setValue(BlockGoldPile.LAYERS, 8), 3);
                        worldIn.setBlock(blockpos.above().north(), IafBlockRegistry.GOLD_PILE.get().defaultBlockState().setValue(BlockGoldPile.LAYERS, 1 + new Random().nextInt(7)), 3);
                        worldIn.setBlock(blockpos.above().south(), IafBlockRegistry.GOLD_PILE.get().defaultBlockState().setValue(BlockGoldPile.LAYERS, 1 + new Random().nextInt(7)), 3);
                        worldIn.setBlock(blockpos.above().west(), IafBlockRegistry.GOLD_PILE.get().defaultBlockState().setValue(BlockGoldPile.LAYERS, 1 + new Random().nextInt(7)), 3);
                        worldIn.setBlock(blockpos.above().east(), IafBlockRegistry.GOLD_PILE.get().defaultBlockState().setValue(BlockGoldPile.LAYERS, 1 + new Random().nextInt(7)), 3);
                        worldIn.setBlock(blockpos.above(2), Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, HORIZONTALS[new Random().nextInt(3)]), 2);
                        if (worldIn.getBlockState(blockpos.above(2)).getBlock() instanceof AbstractChestBlock) {
                            BlockEntity tileentity1 = worldIn.getBlockEntity(blockpos.above(2));
                            if (tileentity1 instanceof ChestBlockEntity) {
                                ((ChestBlockEntity) tileentity1).setLootTable(CYCLOPS_CHEST, rand.nextLong());
                            }
                        }

                    }


                    if (rand.nextInt(50) == 0 && isTouchingAir(worldIn, blockpos.above())) {
                        int torchHeight = rand.nextInt(2) + 1;
                        for (int fence = 0; fence < torchHeight; fence++) {
                            worldIn.setBlock(blockpos.above(1 + fence), getFenceState(worldIn, blockpos.above(1 + fence)), 3);
                        }
                        worldIn.setBlock(blockpos.above(1 + torchHeight), Blocks.TORCH.defaultBlockState(), 2);
                    }
                }
            }
        }
        EntityCyclops cyclops = IafEntityRegistry.CYCLOPS.get().create(worldIn.getLevel());
        cyclops.absMoveTo(position.getX() + 0.5, position.getY() + 1.5, position.getZ() + 0.5, rand.nextFloat() * 360, 0);
        worldIn.addFreshEntity(cyclops);
        return true;
    }

    public BlockState getFenceState(LevelAccessor world, BlockPos pos) {
        boolean east = world.getBlockState(pos.east()).getBlock() == Blocks.OAK_FENCE;
        boolean west = world.getBlockState(pos.west()).getBlock() == Blocks.OAK_FENCE;
        boolean north = world.getBlockState(pos.north()).getBlock() == Blocks.OAK_FENCE;
        boolean south = world.getBlockState(pos.south()).getBlock() == Blocks.OAK_FENCE;
        return Blocks.OAK_FENCE.defaultBlockState().setValue(FenceBlock.EAST, east).setValue(FenceBlock.WEST, west).setValue(FenceBlock.NORTH, north).setValue(FenceBlock.SOUTH, south);
    }
}
