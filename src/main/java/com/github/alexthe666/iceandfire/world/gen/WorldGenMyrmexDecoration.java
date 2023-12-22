package com.github.alexthe666.iceandfire.world.gen;

import com.github.alexthe666.iceandfire.block.BlockGoldPile;
import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.entity.util.MyrmexHive;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.stream.Collectors;

public class WorldGenMyrmexDecoration {

    public static final ResourceLocation MYRMEX_GOLD_CHEST = new ResourceLocation("iceandfire", "chest/myrmex_loot_chest");
    public static final ResourceLocation DESERT_MYRMEX_FOOD_CHEST = new ResourceLocation("iceandfire", "chest/myrmex_desert_food_chest");
    public static final ResourceLocation JUNGLE_MYRMEX_FOOD_CHEST = new ResourceLocation("iceandfire", "chest/myrmex_jungle_food_chest");
    public static final ResourceLocation MYRMEX_TRASH_CHEST = new ResourceLocation("iceandfire", "chest/myrmex_trash_chest");
    private static final Direction[] HORIZONTALS = new Direction[]{Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};

    public static void generateSkeleton(LevelAccessor worldIn, BlockPos blockpos, BlockPos origin, int radius, RandomSource rand) {
        if (worldIn.getBlockState(blockpos.below()).isFaceSturdy(worldIn, blockpos.below(), Direction.UP)) {
            Direction direction = Direction.from2DDataValue(rand.nextInt(3));
            Direction.Axis oppositeAxis = direction.getAxis() == Direction.Axis.X ? Direction.Axis.Z : Direction.Axis.X;
            int maxRibHeight = rand.nextInt(2);
            for (int spine = 0; spine < 5 + rand.nextInt(2) * 2; spine++) {
                BlockPos segment = blockpos.relative(direction, spine);
                if (origin.distSqr(segment) <= (double) (radius * radius)) {
                    worldIn.setBlock(segment, Blocks.BONE_BLOCK.defaultBlockState().setValue(RotatedPillarBlock.AXIS, direction.getAxis()), 2);
                }
                if (spine % 2 != 0) {
                    BlockPos rightRib = segment.relative(direction.getCounterClockWise());
                    BlockPos leftRib = segment.relative(direction.getClockWise());
                    if (origin.distSqr(rightRib) <= (double) (radius * radius)) {
                        worldIn.setBlock(rightRib, Blocks.BONE_BLOCK.defaultBlockState().setValue(RotatedPillarBlock.AXIS, oppositeAxis), 2);
                    }
                    if (origin.distSqr(leftRib) <= (double) (radius * radius)) {
                        worldIn.setBlock(leftRib, Blocks.BONE_BLOCK.defaultBlockState().setValue(RotatedPillarBlock.AXIS, oppositeAxis), 2);
                    }
                    for (int ribHeight = 1; ribHeight < maxRibHeight + 2; ribHeight++) {
                        if (origin.distSqr(rightRib.above(ribHeight).relative(direction.getCounterClockWise())) <= (double) (radius * radius)) {
                            worldIn.setBlock(rightRib.above(ribHeight).relative(direction.getCounterClockWise()), Blocks.BONE_BLOCK.defaultBlockState().setValue(RotatedPillarBlock.AXIS, Direction.Axis.Y), 2);
                        }
                        if (origin.distSqr(leftRib.above(ribHeight).relative(direction.getClockWise())) <= (double) (radius * radius)) {
                            worldIn.setBlock(leftRib.above(ribHeight).relative(direction.getClockWise()), Blocks.BONE_BLOCK.defaultBlockState().setValue(RotatedPillarBlock.AXIS, Direction.Axis.Y), 2);
                        }
                    }
                    if (origin.distSqr(rightRib.above(maxRibHeight + 2)) <= (double) (radius * radius)) {
                        worldIn.setBlock(rightRib.above(maxRibHeight + 2), Blocks.BONE_BLOCK.defaultBlockState().setValue(RotatedPillarBlock.AXIS, oppositeAxis), 2);
                    }
                    if (origin.distSqr(leftRib.above(maxRibHeight + 2)) <= (double) (radius * radius)) {
                        worldIn.setBlock(leftRib.above(maxRibHeight + 2), Blocks.BONE_BLOCK.defaultBlockState().setValue(RotatedPillarBlock.AXIS, oppositeAxis), 2);
                    }
                }
            }
        }
    }

    public static void generateLeaves(LevelAccessor worldIn, BlockPos blockpos, BlockPos origin, int radius, RandomSource rand, boolean jungle) {
        if (worldIn.getBlockState(blockpos.below()).isFaceSturdy(worldIn, blockpos.below(), Direction.UP)) {
            BlockState leaf = Blocks.OAK_LEAVES.defaultBlockState().setValue(LeavesBlock.PERSISTENT, Boolean.TRUE);
            if (jungle) {
                leaf = Blocks.JUNGLE_LEAVES.defaultBlockState().setValue(LeavesBlock.PERSISTENT, Boolean.TRUE);
            }
            int i1 = 0;
            for (int i = 0; i1 >= 0 && i < 3; ++i) {
                int j = i1 + rand.nextInt(2);
                int k = i1 + rand.nextInt(2);
                int l = i1 + rand.nextInt(2);
                float f = (float) (j + k + l) * 0.333F + 0.5F;
                for (BlockPos pos : BlockPos.betweenClosedStream(blockpos.offset(-j, -k, -l), blockpos.offset(j, k, l)).map(BlockPos::immutable).collect(Collectors.toSet())) {
                    if (pos.distSqr(blockpos) <= (double) (f * f) && worldIn.isEmptyBlock(pos)) {
                        worldIn.setBlock(pos, leaf, 4);
                    }
                }
                blockpos = blockpos.offset(-(i1 + 1) + rand.nextInt(2 + i1 * 2), -rand.nextInt(2), -(i1 + 1) + rand.nextInt(2 + i1 * 2));
            }
        }
    }

    public static void generatePumpkins(LevelAccessor worldIn, BlockPos blockpos, BlockPos origin, int radius, RandomSource rand, boolean jungle) {
        if (worldIn.getBlockState(blockpos.below()).isFaceSturdy(worldIn, blockpos.below(), Direction.UP)) {
            worldIn.setBlock(blockpos, jungle ? Blocks.MELON.defaultBlockState() : Blocks.PUMPKIN.defaultBlockState(), 2);
        }
    }

    public static void generateCocoon(LevelAccessor worldIn, BlockPos blockpos, RandomSource rand, boolean jungle, ResourceLocation lootTable) {
        if (worldIn.getBlockState(blockpos.below()).isFaceSturdy(worldIn, blockpos.below(), Direction.UP)) {
            worldIn.setBlock(blockpos, jungle ? IafBlockRegistry.JUNGLE_MYRMEX_COCOON.get().defaultBlockState() : IafBlockRegistry.DESERT_MYRMEX_COCOON.get().defaultBlockState(), 3);

            if (worldIn.getBlockEntity(blockpos) != null && worldIn.getBlockEntity(blockpos) instanceof RandomizableContainerBlockEntity) {
                BlockEntity tileentity1 = worldIn.getBlockEntity(blockpos);
                ((RandomizableContainerBlockEntity) tileentity1).setLootTable(lootTable, rand.nextLong());

            }
        }
    }

    public static void generateMushrooms(LevelAccessor worldIn, BlockPos blockpos, BlockPos origin, int radius, RandomSource rand) {
        if (worldIn.getBlockState(blockpos.below()).isFaceSturdy(worldIn, blockpos.below(), Direction.UP)) {
            worldIn.setBlock(blockpos, rand.nextBoolean() ? Blocks.BROWN_MUSHROOM.defaultBlockState() : Blocks.RED_MUSHROOM.defaultBlockState(), 2);
        }
    }

    public static void generateGold(LevelAccessor worldIn, BlockPos blockpos, BlockPos origin, int radius, RandomSource rand) {
        BlockState gold = IafBlockRegistry.GOLD_PILE.get().defaultBlockState();
        int choice = rand.nextInt(2);
        if (choice == 1) {
            gold = IafBlockRegistry.SILVER_PILE.get().defaultBlockState();
        } else if (choice == 2) {
            gold = IafBlockRegistry.COPPER_PILE.get().defaultBlockState();
        }
        if (worldIn.getBlockState(blockpos.below()).isFaceSturdy(worldIn, blockpos.below(), Direction.UP)) {
            worldIn.setBlock(blockpos, gold.setValue(BlockGoldPile.LAYERS, 8), 3);
            worldIn.setBlock(MyrmexHive.getGroundedPos(worldIn, blockpos.north()), gold.setValue(BlockGoldPile.LAYERS, 1 + RandomSource.create().nextInt(7)), 3);
            worldIn.setBlock(MyrmexHive.getGroundedPos(worldIn, blockpos.south()), gold.setValue(BlockGoldPile.LAYERS, 1 + RandomSource.create().nextInt(7)), 3);
            worldIn.setBlock(MyrmexHive.getGroundedPos(worldIn, blockpos.west()), gold.setValue(BlockGoldPile.LAYERS, 1 + RandomSource.create().nextInt(7)), 3);
            worldIn.setBlock(MyrmexHive.getGroundedPos(worldIn, blockpos.east()), gold.setValue(BlockGoldPile.LAYERS, 1 + RandomSource.create().nextInt(7)), 3);
            if (rand.nextInt(3) == 0) {
                worldIn.setBlock(blockpos.above(), Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, HORIZONTALS[RandomSource.create().nextInt(3)]), 2);
                if (worldIn.getBlockState(blockpos.above()).getBlock() instanceof ChestBlock) {
                    BlockEntity tileentity1 = worldIn.getBlockEntity(blockpos.above());
                    if (tileentity1 instanceof ChestBlockEntity) {
                        ((ChestBlockEntity) tileentity1).setLootTable(MYRMEX_GOLD_CHEST, rand.nextLong());
                    }
                }
            }
        }
    }

    public static void generateTrashHeap(LevelAccessor worldIn, BlockPos blockpos, BlockPos origin, int radius, RandomSource rand) {
        if (worldIn.getBlockState(blockpos.below()).isFaceSturdy(worldIn, blockpos.below(), Direction.UP)) {
            Block blob = Blocks.DIRT;
            switch (rand.nextInt(3)) {
                case 0:
                    blob = Blocks.DIRT;
                    break;
                case 1:
                    blob = Blocks.SAND;
                    break;
                case 2:
                    blob = Blocks.COBBLESTONE;
                    break;
                case 3:
                    blob = Blocks.GRAVEL;
                    break;
            }
            int i1 = 0;
            for (int i = 0; i1 >= 0 && i < 3; ++i) {
                int j = i1 + rand.nextInt(2);
                int k = i1 + rand.nextInt(2);
                int l = i1 + rand.nextInt(2);
                float f = (float) (j + k + l) * 0.333F + 0.5F;
                for (BlockPos pos : BlockPos.betweenClosedStream(blockpos.offset(-j, -k, -l), blockpos.offset(j, k, l)).map(BlockPos::immutable).collect(Collectors.toSet())) {
                    if (pos.distSqr(blockpos) <= (double) (f * f)) {
                        worldIn.setBlock(pos, blob.defaultBlockState(), 4);
                    }
                }
                blockpos = blockpos.offset(-(i1 + 1) + rand.nextInt(2 + i1 * 2), -rand.nextInt(2), -(i1 + 1) + rand.nextInt(2 + i1 * 2));
            }

        }
    }

    public static void generateTrashOre(LevelAccessor worldIn, BlockPos blockpos, BlockPos origin, int radius, RandomSource rand) {
        Block current = worldIn.getBlockState(blockpos).getBlock();
        if (origin.distSqr(blockpos) <= (double) (radius * radius)) {
            if (current == Blocks.DIRT || current == Blocks.SAND || current == Blocks.COBBLESTONE || current == Blocks.GRAVEL) {
                Block ore = Blocks.REDSTONE_ORE;
                if (rand.nextInt(3) == 0) {
                    ore = rand.nextBoolean() ? Blocks.GOLD_ORE : IafBlockRegistry.SILVER_ORE.get();
                    if (rand.nextInt(2) == 0) {
                        ore = Blocks.COPPER_ORE;
                    }
                } else if (rand.nextInt(3) == 0) {
                    ore = Blocks.DIAMOND_ORE;
                } else if (rand.nextInt(2) == 0) {
                    ore = rand.nextBoolean() ? Blocks.EMERALD_ORE : IafBlockRegistry.SAPPHIRE_ORE.get();
                    if(rand.nextInt(2) == 0){
                        ore = Blocks.AMETHYST_CLUSTER;
                    }
                }
                worldIn.setBlock(blockpos, ore.defaultBlockState(), 2);
            }
        }
    }
}