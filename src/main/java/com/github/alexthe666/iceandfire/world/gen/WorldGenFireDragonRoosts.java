package com.github.alexthe666.iceandfire.world.gen;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import com.github.alexthe666.iceandfire.entity.util.HomePosition;
import com.github.alexthe666.iceandfire.event.WorldGenUtils;
import com.github.alexthe666.iceandfire.world.IafWorldRegistry;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.material.Material;

import java.util.Random;

public class WorldGenFireDragonRoosts extends Feature<NoneFeatureConfiguration> {
    private static final Direction[] HORIZONTALS = new Direction[]{Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};
    private static boolean isMale;
    public static ResourceLocation DRAGON_CHEST = new ResourceLocation("iceandfire", "chest/fire_dragon_roost");

    public WorldGenFireDragonRoosts(Codec<NoneFeatureConfiguration> configFactoryIn) {
        super(configFactoryIn);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel worldIn = context.level();
        Random rand = context.random();
        BlockPos position = context.origin();
        if (!IafWorldRegistry.isDimensionListedForDragons(worldIn)) {
            return false;
        }
        if (!IafConfig.generateDragonRoosts || rand.nextInt(IafConfig.generateDragonRoostChance) != 0 || !IafWorldRegistry.isFarEnoughFromSpawn(worldIn, position) || !IafWorldRegistry.isFarEnoughFromDangerousGen(worldIn, position)) {
            return false;
        }
        if (!worldIn.getFluidState(worldIn.getHeightmapPos(Heightmap.Types.WORLD_SURFACE_WG, position).below()).isEmpty()) {
            return false;
        }

        isMale = new Random().nextBoolean();
        int boulders = 0;
        int radius = 12 + rand.nextInt(8);
        position = worldIn.getHeightmapPos(Heightmap.Types.WORLD_SURFACE_WG, position);
        worldIn.setBlock(position, Blocks.AIR.defaultBlockState(), 2);
        BlockPos finalPosition = position;
        if (!worldIn.isClientSide()) {
            EntityDragonBase dragon = IafEntityRegistry.FIRE_DRAGON.get().create(worldIn.getLevel());
            dragon.setGender(isMale);
            dragon.setPersistenceRequired();
            dragon.growDragon(40 + radius);
            dragon.setAgingDisabled(true);
            dragon.setHealth(dragon.getMaxHealth());
            dragon.setVariant(new Random().nextInt(4));
            dragon.absMoveTo(position.getX() + 0.5, 1 + worldIn.getHeightmapPos(Heightmap.Types.WORLD_SURFACE_WG, position).getY() + 1.5, position.getZ() + 0.5, rand.nextFloat() * 360, 0);
            dragon.homePos = new HomePosition(position, worldIn.getLevel());
            dragon.hasHomePosition = true;
            dragon.setHunger(50);
            worldIn.addFreshEntity(dragon);
        }
        {
            int j = radius;
            int k = 2;
            int l = radius;
            float f = (j + k + l) * 0.333F + 0.5F;
            BlockPos.betweenClosedStream(position.offset(-j, k, -l), position.offset(j, 0, l)).map(BlockPos::immutable).forEach(blockPos -> {
                int yAdd = blockPos.getY() - finalPosition.getY();
                if (blockPos.distSqr(finalPosition) <= f * f && yAdd < 2 + rand.nextInt(k) && !worldIn.isEmptyBlock(blockPos.below())) {
                    if (worldIn.isEmptyBlock(blockPos.above()))
                        worldIn.setBlock(blockPos, IafBlockRegistry.CHARRED_GRASS.get().defaultBlockState(), 2);
                    else
                        worldIn.setBlock(blockPos, IafBlockRegistry.CHARRED_DIRT.get().defaultBlockState(), 2);
                }
            });
        }
        {
            int j = radius;
            int k = (radius / 5);
            int l = radius;
            float f = (j + k + l) * 0.333F + 0.5F;
            BlockPos.betweenClosedStream(position.offset(-j, -k, -l), position.offset(j, 1, l)).map(BlockPos::immutable).forEach(blockPos -> {
                if (blockPos.distSqr(finalPosition) < f * f) {
                    worldIn.setBlock(blockPos, rand.nextBoolean() ? IafBlockRegistry.CHARRED_GRAVEL.get().defaultBlockState() : IafBlockRegistry.CHARRED_DIRT.get().defaultBlockState(), 2);
                } else if (blockPos.distSqr(finalPosition) == f * f) {
                    worldIn.setBlock(blockPos, IafBlockRegistry.CHARRED_COBBLESTONE.get().defaultBlockState(), 2);
                }
            });
        }
        radius -= 2;
        {
            int j = radius;
            int k = 2;
            int l = radius;
            float f = (j + k + l) * 0.333F + 0.5F;
            BlockPos up = position.above(k - 1);
            BlockPos.betweenClosedStream(up.offset(-j, -k + 2, -l), up.offset(j, k, l)).map(BlockPos::immutable).forEach(blockPos -> {
                if (blockPos.distSqr(finalPosition) <= f * f) {
                    worldIn.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 2);
                }
            });
        }
        radius += 15;
        {
            int j = radius;
            int k = (radius / 5);
            int l = radius;
            float f = (j + k + l) * 0.333F + 0.5F;
            BlockPos.betweenClosedStream(position.offset(-j, -k, -l), position.offset(j, k, l)).map(BlockPos::immutable).forEach(blockPos -> {
                if (blockPos.distSqr(finalPosition) <= f * f) {
                    double dist = blockPos.distSqr(finalPosition) / (f * f);
                    if (!worldIn.isEmptyBlock(blockPos) && rand.nextDouble() > dist * 0.5D) {
                        transformState(worldIn, blockPos, worldIn.getBlockState(blockPos));
                    }
                    if (dist > 0.5D && rand.nextInt(1000) == 0) {
                        BlockPos height = worldIn.getHeightmapPos(Heightmap.Types.WORLD_SURFACE_WG, blockPos);
                        new WorldGenRoostBoulder(IafBlockRegistry.CHARRED_COBBLESTONE.get(), rand.nextInt(3), true).generate(worldIn, rand, height);
                    }
                    if (rand.nextInt(1000) == 0) {
                        BlockPos height = worldIn.getHeightmapPos(Heightmap.Types.WORLD_SURFACE_WG, blockPos);
                        new WorldGenRoostPile(IafBlockRegistry.ASH.get()).generate(worldIn, rand, height);
                    }
                    if (dist < 0.3D && rand.nextInt(isMale ? 200 : 300) == 0) {
                        BlockPos height = WorldGenUtils.degradeSurface(worldIn, worldIn.getHeightmapPos(Heightmap.Types.WORLD_SURFACE_WG, blockPos)).above(1);
                        new WorldGenRoostGoldPile(IafBlockRegistry.GOLD_PILE.get()).generate(worldIn, rand, height);
                    }
                    if (dist < 0.3D && rand.nextInt(isMale ? 500 : 700) == 0) {
                        BlockPos height = WorldGenUtils.degradeSurface(worldIn, worldIn.getHeightmapPos(Heightmap.Types.WORLD_SURFACE_WG, blockPos)).above();
                        worldIn.setBlock(height, Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, HORIZONTALS[new Random().nextInt(3)]), 2);
                        if (worldIn.getBlockState(height).getBlock() instanceof ChestBlock) {
                            BlockEntity tileentity1 = worldIn.getBlockEntity(height);
                            if (tileentity1 instanceof ChestBlockEntity) {
                                ((ChestBlockEntity) tileentity1).setLootTable(DRAGON_CHEST, new Random().nextLong());
                            }
                        }
                    }
                    if (rand.nextInt(5000) == 0) {
                        BlockPos height = worldIn.getHeightmapPos(Heightmap.Types.WORLD_SURFACE_WG, blockPos);
                        new WorldGenRoostArch(IafBlockRegistry.CHARRED_COBBLESTONE.get()).generate(worldIn, rand, height);
                    }
                }
            });
        }
        return false;
    }

    private void transformState(LevelAccessor world, BlockPos blockpos, BlockState state) {
        float hardness = state.getDestroySpeed(world, blockpos);
        if (hardness != -1.0F) {
            if (state.getBlock() instanceof BaseEntityBlock) {
                return;
            }
            if (state.getMaterial() == Material.GRASS) {
                world.setBlock(blockpos, IafBlockRegistry.CHARRED_GRASS.get().defaultBlockState(), 2);
            } else if (state.getMaterial() == Material.DIRT && state.getBlock() == Blocks.DIRT) {
                world.setBlock(blockpos, IafBlockRegistry.CHARRED_DIRT.get().defaultBlockState(), 2);
            } else if (state.getMaterial() == Material.DIRT && state.getBlock() == Blocks.GRAVEL) {
                world.setBlock(blockpos, IafBlockRegistry.CHARRED_GRAVEL.get().defaultBlockState(), 2);
            } else if (state.getMaterial() == Material.STONE && (state.getBlock() == Blocks.COBBLESTONE || state.getBlock().getDescriptionId().contains("cobblestone"))) {
                world.setBlock(blockpos, IafBlockRegistry.CHARRED_COBBLESTONE.get().defaultBlockState(), 2);
            } else if (state.getMaterial() == Material.STONE && state.getBlock() != IafBlockRegistry.CHARRED_COBBLESTONE.get()) {
                world.setBlock(blockpos, IafBlockRegistry.CHARRED_STONE.get().defaultBlockState(), 2);
            } else if (state.getBlock() == Blocks.DIRT_PATH) {
                world.setBlock(blockpos, IafBlockRegistry.CHARRED_DIRT_PATH.get().defaultBlockState(), 2);
            } else if (state.getMaterial() == Material.WOOD) {
                world.setBlock(blockpos, IafBlockRegistry.ASH.get().defaultBlockState(), 2);
            } else if (state.getMaterial() == Material.LEAVES || state.getMaterial() == Material.PLANT) {
                world.setBlock(blockpos, Blocks.AIR.defaultBlockState(), 2);
            }
        }
    }
}
