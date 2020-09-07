package com.github.alexthe666.iceandfire.world.gen;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.entity.EntityIceDragon;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import com.github.alexthe666.iceandfire.event.WorldGenUtils;
import com.github.alexthe666.iceandfire.world.IafWorldRegistry;
import com.mojang.datafixers.Dynamic;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.ContainerBlock;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;

public class WorldGenIceDragonRoosts extends Feature<NoFeatureConfig> {
    private static final Direction[] HORIZONTALS = new Direction[]{Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};
    private static boolean isMale;
    private BlockPos lastIceRoost = BlockPos.ZERO;

    public WorldGenIceDragonRoosts(Function<Dynamic<?>, ? extends NoFeatureConfig> configFactoryIn) {
        super(configFactoryIn);
    }

    private void transformState(IWorld world, BlockPos blockpos, BlockState state) {
        float hardness = state.getBlock().getBlockHardness(state, world, blockpos);
        if (hardness != -1.0F) {
            if (state.getBlock() instanceof ContainerBlock) {
                return;
            }
            if (state.getMaterial() == Material.ORGANIC) {
                world.setBlockState(blockpos, IafBlockRegistry.FROZEN_GRASS.getDefaultState(), 2);
            } else if (state.getMaterial() == Material.EARTH && state.getBlock() == Blocks.DIRT) {
                world.setBlockState(blockpos, IafBlockRegistry.FROZEN_DIRT.getDefaultState(), 2);
            } else if (state.getMaterial() == Material.EARTH && state.getBlock() == Blocks.GRAVEL) {
                world.setBlockState(blockpos, IafBlockRegistry.FROZEN_GRAVEL.getDefaultState(), 2);
            } else if (state.getMaterial() == Material.ROCK && (state.getBlock() == Blocks.COBBLESTONE || state.getBlock().getTranslationKey().contains("cobblestone"))) {
                world.setBlockState(blockpos, IafBlockRegistry.FROZEN_COBBLESTONE.getDefaultState(), 2);
            } else if (state.getMaterial() == Material.ROCK && state.getBlock() != IafBlockRegistry.FROZEN_COBBLESTONE) {
                world.setBlockState(blockpos, IafBlockRegistry.FROZEN_STONE.getDefaultState(), 2);
            } else if (state.getBlock() == Blocks.GRASS_PATH) {
                world.setBlockState(blockpos, IafBlockRegistry.FROZEN_GRASS_PATH.getDefaultState(), 2);
            } else if (state.getMaterial() == Material.WOOD) {
                world.setBlockState(blockpos, IafBlockRegistry.FROZEN_SPLINTERS.getDefaultState(), 2);
            } else if (state.getMaterial() == Material.LEAVES || state.getMaterial() == Material.PLANTS) {
                world.setBlockState(blockpos, Blocks.AIR.getDefaultState(), 2);
            }
        }
    }

    @Override
    public boolean place(IWorld worldIn, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos position, NoFeatureConfig config) {
        if(!IafWorldRegistry.isDimensionListedForDragons(worldIn)){
            return false;
        }
        if(!IafConfig.generateDragonRoosts || rand.nextInt(IafConfig.generateDragonRoostChance) != 0 || !IafWorldRegistry.isFarEnoughFromSpawn(worldIn, position)){
            return false;
        }
        if(IafWorldRegistry.isFarEnoughFromSpawn(worldIn, position))
        position = worldIn.getHeight(Heightmap.Type.WORLD_SURFACE_WG, position);
        isMale = rand.nextBoolean();
        int boulders = 0;
        int radius = 12 + rand.nextInt(8);
        {
            int j = radius;
            int k = 2;
            int l = radius;
            float f = (float) (j + k + l) * 0.333F + 0.5F;
            for (BlockPos blockpos : BlockPos.getAllInBox(position.add(-j, k, -l), position.add(j, 0, l)).map(BlockPos::toImmutable).collect(Collectors.toSet())) {
                int yAdd = blockpos.getY() - position.getY();
                if (blockpos.distanceSq(position) <= (double) (f * f) && yAdd < 2 + rand.nextInt(k) && !worldIn.isAirBlock(blockpos.down())) {
                    worldIn.setBlockState(blockpos, IafBlockRegistry.FROZEN_GRASS.getDefaultState(), 2);
                }
            }
            for (BlockPos blockpos : BlockPos.getAllInBox(position.add(-j, k, -l), position.add(j, 0, l)).map(BlockPos::toImmutable).collect(Collectors.toSet())) {
                if (worldIn.getBlockState(blockpos).getBlock() == IafBlockRegistry.FROZEN_GRASS && !worldIn.isAirBlock(blockpos.up())) {
                    worldIn.setBlockState(blockpos, IafBlockRegistry.FROZEN_DIRT.getDefaultState(), 2);
                }
            }
        }
        {
            int j = radius;
            int k = (radius / 5);
            int l = radius;
            float f = (float) (j + k + l) * 0.333F + 0.5F;
            for (BlockPos blockpos : BlockPos.getAllInBox(position.add(-j, -k, -l), position.add(j, 1, l)).map(BlockPos::toImmutable).collect(Collectors.toSet())) {
                if (blockpos.distanceSq(position) < (double) (f * f)) {
                    worldIn.setBlockState(blockpos, rand.nextBoolean() ? IafBlockRegistry.FROZEN_GRAVEL.getDefaultState() : IafBlockRegistry.FROZEN_DIRT.getDefaultState(), 2);
                }
                if (blockpos.distanceSq(position) == (double) (f * f)) {
                    worldIn.setBlockState(blockpos, rand.nextBoolean() ? IafBlockRegistry.FROZEN_COBBLESTONE.getDefaultState() : IafBlockRegistry.FROZEN_COBBLESTONE.getDefaultState(), 2);
                }
            }
        }
        radius -= 2;
        {
            int j = radius;
            int k = 2;
            int l = radius;
            float f = (float) (j + k + l) * 0.333F + 0.5F;
            BlockPos up = position.up(k - 1);
            for (BlockPos blockpos : BlockPos.getAllInBox(up.add(-j, -k + 2, -l), up.add(j, k, l)).map(BlockPos::toImmutable).collect(Collectors.toSet())) {
                if (blockpos.distanceSq(position) <= (double) (f * f)) {
                    worldIn.setBlockState(blockpos, Blocks.AIR.getDefaultState(), 2);
                }
            }
        }
        radius += 15;
        {
            int j = radius;
            int k = (radius / 5);
            int l = radius;
            float f = (float) (j + k + l) * 0.333F + 0.5F;
            for (BlockPos blockpos : BlockPos.getAllInBox(position.add(-j, -k, -l), position.add(j, k, l)).map(BlockPos::toImmutable).collect(Collectors.toSet())) {
                if (blockpos.distanceSq(position) <= (double) (f * f)) {
                    double dist = blockpos.distanceSq(position) / (double) (f * f);
                    if (!worldIn.isAirBlock(position) && rand.nextDouble() > dist * 0.5D) {
                        transformState(worldIn, blockpos, worldIn.getBlockState(blockpos));
                    }
                    if (dist > 0.5D && rand.nextInt(1000) == 0) {
                        BlockPos height = worldIn.getHeight(Heightmap.Type.WORLD_SURFACE_WG, blockpos);
                        new WorldGenRoostBoulder(IafBlockRegistry.FROZEN_COBBLESTONE, rand.nextInt(3), true).generate(worldIn, rand, height);
                    }
                    if (rand.nextInt(1000) == 0) {
                        BlockPos height = worldIn.getHeight(Heightmap.Type.WORLD_SURFACE_WG, blockpos);
                        new WorldGenRoostPile(IafBlockRegistry.DRAGON_ICE).generate(worldIn, rand, height);
                    }
                    if (dist < 0.3D && rand.nextInt(isMale ? 200 : 300) == 0) {
                        BlockPos height = WorldGenUtils.degradeSurface(worldIn, worldIn.getHeight(Heightmap.Type.WORLD_SURFACE_WG, blockpos)).up();
                        new WorldGenRoostGoldPile(IafBlockRegistry.SILVER_PILE).generate(worldIn, rand, height);
                    }
                    if (dist < 0.3D && rand.nextInt(isMale ? 500 : 700) == 0) {
                        BlockPos height = WorldGenUtils.degradeSurface(worldIn, worldIn.getHeight(Heightmap.Type.WORLD_SURFACE_WG, blockpos)).up();
                        worldIn.setBlockState(height, Blocks.CHEST.getDefaultState().with(ChestBlock.FACING, HORIZONTALS[new Random().nextInt(3)]), 2);
                        if (worldIn.getBlockState(height).getBlock() instanceof ChestBlock) {
                            TileEntity tileentity1 = worldIn.getTileEntity(height);
                            if (tileentity1 instanceof ChestTileEntity) {
                                ((ChestTileEntity) tileentity1).setLootTable(WorldGenIceDragonCave.ICEDRAGON_CHEST, new Random().nextLong());
                            }
                        }
                    }
                    if (rand.nextInt(5000) == 0) {
                        BlockPos height = worldIn.getHeight(Heightmap.Type.WORLD_SURFACE_WG, blockpos);
                        new WorldGenRoostArch(IafBlockRegistry.FROZEN_COBBLESTONE).generate(worldIn, rand, height);
                    }
                }
            }
        }
        {
            EntityIceDragon dragon = IafEntityRegistry.ICE_DRAGON.create(worldIn.getWorld());
            dragon.setGender(isMale);
            dragon.growDragon(40 + radius);
            dragon.setAgingDisabled(true);
            dragon.setHealth(dragon.getMaxHealth());
            dragon.setVariant(new Random().nextInt(4));
            dragon.setPositionAndRotation(position.getX() + 0.5, worldIn.getHeight(Heightmap.Type.WORLD_SURFACE_WG, position).getY() + 1.5, position.getZ() + 0.5, rand.nextFloat() * 360, 0);
            dragon.homePos = position;
            dragon.hasHomePosition = true;
            dragon.setHunger(50);
            worldIn.addEntity(dragon);
        }
        return true;
    }
}
