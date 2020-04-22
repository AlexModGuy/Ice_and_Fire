package com.github.alexthe666.iceandfire.structures;

import com.github.alexthe666.iceandfire.block.IaFBlockRegistry;
import com.github.alexthe666.iceandfire.entity.EntityIceDragon;
import com.github.alexthe666.iceandfire.event.StructureGenerator;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.Random;

public class WorldGenIceDragonRoosts extends WorldGenerator {
    private static boolean isMale;

    @Override
    public boolean generate(World worldIn, Random rand, BlockPos position) {
        isMale = rand.nextBoolean();
        int boulders = 0;
        int radius = 12 + rand.nextInt(8);
        {
            int j = radius;
            int k = 2;
            int l = radius;
            float f = (float) (j + k + l) * 0.333F + 0.5F;
            for (BlockPos blockpos : BlockPos.getAllInBox(position.add(-j, k, -l), position.add(j, 0, l))) {
                int yAdd = blockpos.getY() - position.getY();
                if (blockpos.distanceSq(position) <= (double) (f * f) && yAdd < 2 + rand.nextInt(k) && !worldIn.isAirBlock(blockpos.down())) {
                    worldIn.setBlockState(blockpos, IaFBlockRegistry.frozenGrass.getDefaultState());
                }
            }
            for (BlockPos blockpos : BlockPos.getAllInBox(position.add(-j, k, -l), position.add(j, 0, l))) {
                if (worldIn.getBlockState(blockpos).getBlock() == IaFBlockRegistry.frozenGrass && !worldIn.isAirBlock(blockpos.up())) {
                    worldIn.setBlockState(blockpos, IaFBlockRegistry.frozenDirt.getDefaultState());
                }
            }
        }
        {
            int j = radius;
            int k = (radius / 5);
            int l = radius;
            float f = (float) (j + k + l) * 0.333F + 0.5F;
            for (BlockPos blockpos : BlockPos.getAllInBox(position.add(-j, -k, -l), position.add(j, 0, l))) {
                if (blockpos.distanceSq(position) < (double) (f * f)) {
                    worldIn.setBlockState(blockpos, rand.nextBoolean() ? IaFBlockRegistry.frozenGravel.getDefaultState() : IaFBlockRegistry.frozenDirt.getDefaultState());
                }
                if (blockpos.distanceSq(position) == (double) (f * f)) {
                    worldIn.setBlockState(blockpos, rand.nextBoolean() ? IaFBlockRegistry.frozenCobblestone.getDefaultState() : IaFBlockRegistry.frozenCobblestone.getDefaultState());
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
            for (BlockPos blockpos : BlockPos.getAllInBox(up.add(-j, -k + 2, -l), up.add(j, k, l))) {
                if (blockpos.distanceSq(position) <= (double) (f * f)) {
                    worldIn.setBlockState(blockpos, Blocks.AIR.getDefaultState());
                }
            }
        }
        radius += 15;
        {
            int j = radius;
            int k = (radius / 5);
            int l = radius;
            float f = (float) (j + k + l) * 0.333F + 0.5F;
            for (BlockPos blockpos : BlockPos.getAllInBox(position.add(-j, -k, -l), position.add(j, k, l))) {
                if (blockpos.distanceSq(position) <= (double) (f * f)) {
                    double dist = blockpos.distanceSq(position) / (double) (f * f);
                    if (!worldIn.isAirBlock(position) && rand.nextDouble() > dist * 0.5D) {
                        transformState(worldIn, blockpos, worldIn.getBlockState(blockpos));
                    }
                    if (dist > 0.5D && rand.nextInt(1000) == 0) {
                        BlockPos height = worldIn.getHeight(blockpos);
                        new WorldGenRoostBoulder(IaFBlockRegistry.frozenCobblestone, rand.nextInt(3), true).generate(worldIn, rand, height);
                    }
                    if (rand.nextInt(1000) == 0) {
                        BlockPos height = worldIn.getHeight(blockpos);
                        new WorldGenRoostPile(IaFBlockRegistry.dragon_ice).generate(worldIn, rand, height);
                    }
                    if (dist < 0.3D && rand.nextInt(isMale ? 250 : 400) == 0) {
                        BlockPos height = StructureGenerator.degradeSurface(worldIn, worldIn.getHeight(blockpos));
                        new WorldGenRoostGoldPile(IaFBlockRegistry.silverPile).generate(worldIn, rand, height);
                    }
                    if (dist < 0.3D && rand.nextInt(isMale ? 500 : 700) == 0) {
                        BlockPos height = StructureGenerator.degradeSurface(worldIn, worldIn.getHeight(blockpos));
                        worldIn.setBlockState(height, Blocks.CHEST.getDefaultState().withProperty(BlockChest.FACING, EnumFacing.HORIZONTALS[new Random().nextInt(3)]), 2);
                        if (worldIn.getBlockState(height).getBlock() instanceof BlockChest) {
                            TileEntity tileentity1 = worldIn.getTileEntity(height);
                            if (tileentity1 instanceof TileEntityChest && !tileentity1.isInvalid()) {
                                ((TileEntityChest) tileentity1).setLootTable(WorldGenIceDragonCave.ICEDRAGON_CHEST, new Random().nextLong());
                            }
                        }
                    }
                    if (rand.nextInt(5000) == 0) {
                        BlockPos height = worldIn.getHeight(blockpos);
                        new WorldGenRoostArch(IaFBlockRegistry.frozenCobblestone).generate(worldIn, rand, height);
                    }
                }
            }
        }
        {
            EntityIceDragon dragon = new EntityIceDragon(worldIn);
            dragon.setGender(isMale);
            dragon.growDragon(40 + radius);
            dragon.setAgingDisabled(true);
            dragon.setHealth(dragon.getMaxHealth());
            dragon.setVariant(new Random().nextInt(4));
            dragon.setPositionAndRotation(position.getX() + 0.5, worldIn.getHeight(position).getY() + 1.5, position.getZ() + 0.5, rand.nextFloat() * 360, 0);
            dragon.homePos = position;
            dragon.hasHomePosition = true;
            dragon.setHunger(50);
            worldIn.spawnEntity(dragon);
        }

        return false;
    }

    private void transformState(World world, BlockPos blockpos, IBlockState state) {
        float hardness = state.getBlock().getBlockHardness(state, world, blockpos);
        if (hardness != -1.0F) {
            if (state.getBlock() instanceof BlockContainer) {
                return;
            }
            if (state.getMaterial() == Material.GRASS) {
                world.setBlockState(blockpos, IaFBlockRegistry.frozenGrass.getDefaultState());
            } else if (state.getMaterial() == Material.GROUND && state.getBlock() == Blocks.DIRT) {
                world.setBlockState(blockpos, IaFBlockRegistry.frozenDirt.getDefaultState());
            } else if (state.getMaterial() == Material.GROUND && state.getBlock() == Blocks.GRAVEL) {
                world.setBlockState(blockpos, IaFBlockRegistry.frozenGravel.getDefaultState());
            } else if (state.getMaterial() == Material.ROCK && (state.getBlock() == Blocks.COBBLESTONE || state.getBlock().getTranslationKey().contains("cobblestone"))) {
                world.setBlockState(blockpos, IaFBlockRegistry.frozenCobblestone.getDefaultState());
            } else if (state.getMaterial() == Material.ROCK && state.getBlock() != IaFBlockRegistry.frozenCobblestone) {
                world.setBlockState(blockpos, IaFBlockRegistry.frozenStone.getDefaultState());
            } else if (state.getBlock() == Blocks.GRASS_PATH) {
                world.setBlockState(blockpos, IaFBlockRegistry.frozenGrassPath.getDefaultState());
            } else if (state.getMaterial() == Material.WOOD) {
                world.setBlockState(blockpos, IaFBlockRegistry.frozenSplinters.getDefaultState());
            } else if (state.getMaterial() == Material.LEAVES || state.getMaterial() == Material.PLANTS) {
                world.setBlockState(blockpos, Blocks.AIR.getDefaultState());
            }
        }
    }
}
