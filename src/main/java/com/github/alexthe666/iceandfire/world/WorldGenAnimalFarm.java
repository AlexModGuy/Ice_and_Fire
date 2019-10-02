package com.github.alexthe666.iceandfire.world;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.entity.passive.*;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.BiomeDictionary;

import java.util.Random;

public class WorldGenAnimalFarm extends WorldGenerator {
    @Override
    public boolean generate(World worldIn, Random rand, BlockPos position) {
        if (worldIn == null) {
            return false;
        }

        Biome biome = worldIn.getBiome(position);
        boolean sandy = BiomeDictionary.hasType(biome, BiomeDictionary.Type.SANDY);
        Block fence = Blocks.OAK_FENCE;
        Block fence_gate = Blocks.OAK_FENCE_GATE;
        if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.SAVANNA)) {
            fence = Blocks.ACACIA_FENCE;
            fence_gate = Blocks.ACACIA_FENCE_GATE;
            for (int animals = 0; animals < rand.nextInt(2) + 1; animals++) {
                EntityAnimal animal = new EntityCow(worldIn);
                animal.setPositionAndRotation(position.getX() + 0.5F + (-3 + rand.nextInt(6)), position.getY() + 1.5F, position.getZ() + 0.5F + (-3 + rand.nextInt(6)), rand.nextFloat() * 360, 0);
                worldIn.spawnEntity(animal);
            }
        } else if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.CONIFEROUS)) {
            fence = Blocks.SPRUCE_FENCE;
            fence_gate = Blocks.SPRUCE_FENCE_GATE;
            for (int animals = 0; animals < rand.nextInt(2) + 1; animals++) {
                EntityAnimal animal = new EntitySheep(worldIn);
                animal.setPositionAndRotation(position.getX() + 0.5F + (-3 + rand.nextInt(6)), position.getY() + 1.5F, position.getZ() + 0.5F + (-3 + rand.nextInt(6)), rand.nextFloat() * 360, 0);
                worldIn.spawnEntity(animal);
            }
        } else if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.JUNGLE)) {
            fence = Blocks.JUNGLE_FENCE;
            fence_gate = Blocks.JUNGLE_FENCE_GATE;
            for (int animals = 0; animals < rand.nextInt(2) + 1; animals++) {
                EntityAnimal animal = new EntityChicken(worldIn);
                animal.setPositionAndRotation(position.getX() + 0.5F + (-3 + rand.nextInt(6)), position.getY() + 1.5F, position.getZ() + 0.5F + (-3 + rand.nextInt(6)), rand.nextFloat() * 360, 0);
                worldIn.spawnEntity(animal);
            }
        } else if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.FOREST) && BiomeDictionary.hasType(biome, BiomeDictionary.Type.DENSE)) {
            fence = Blocks.DARK_OAK_FENCE;
            fence_gate = Blocks.DARK_OAK_FENCE_GATE;
        } else if (biome == Biomes.BIRCH_FOREST || biome == Biomes.BIRCH_FOREST_HILLS || biome == Biomes.MUTATED_BIRCH_FOREST || biome == Biomes.MUTATED_BIRCH_FOREST_HILLS) {
            fence = Blocks.BIRCH_FENCE;
            fence_gate = Blocks.BIRCH_FENCE_GATE;
        }
        if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.SANDY)) {
            for (int animals = 0; animals < rand.nextInt(2) + 1; animals++) {
                EntityAnimal animal = new EntityPig(worldIn);
                animal.setPositionAndRotation(position.getX() + 0.5F + (-3 + rand.nextInt(6)), position.getY() + 1.5F, position.getZ() + 0.5F + (-3 + rand.nextInt(6)), rand.nextFloat() * 360, 0);
                worldIn.spawnEntity(animal);
            }
        }
        if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.PLAINS)) {
            for (int animals = 0; animals < rand.nextInt(2) + 1; animals++) {
                EntityAnimal animal = new EntityChicken(worldIn);
                animal.setPositionAndRotation(position.getX() + 0.5F + (-3 + rand.nextInt(6)), position.getY() + 1.5F, position.getZ() + 0.5F + (-3 + rand.nextInt(6)), rand.nextFloat() * 360, 0);
                worldIn.spawnEntity(animal);
            }
        }
        for (int x = -4; x < +5; x++) {
            for (int z = -4; z < +5; z++) {
                if (((x % 4 == 0 || z % 4 == 0) || (x % -4 == 0 || z % -4 == 0)) && Math.abs(x) != 0 && Math.abs(z) != 0) {
                    worldIn.setBlockState(position.add(x, 0, z), sandy ? Blocks.SAND.getDefaultState() : Blocks.GRASS.getDefaultState());
                    worldIn.setBlockState(position.add(x, 1, z), fence.getDefaultState());
                } else {
                    worldIn.setBlockState(position.add(x, 0, z), Blocks.GRASS_PATH.getDefaultState());
                }
                if (x == 0) {
                    worldIn.setBlockState(position.add(0, 1, 4), fence_gate.getDefaultState().withProperty(BlockFenceGate.FACING, EnumFacing.SOUTH));
                    worldIn.setBlockState(position.add(0, 1, -4), fence_gate.getDefaultState().withProperty(BlockFenceGate.FACING, EnumFacing.NORTH));
                    worldIn.setBlockState(position.add(0, 0, 4), Blocks.GRASS_PATH.getDefaultState());
                    worldIn.setBlockState(position.add(0, 0, -4), Blocks.GRASS_PATH.getDefaultState());
                }
                if (z == 0) {
                    worldIn.setBlockState(position.add(4, 1, 0), fence_gate.getDefaultState().withProperty(BlockFenceGate.FACING, EnumFacing.EAST));
                    worldIn.setBlockState(position.add(-4, 1, 0), fence_gate.getDefaultState().withProperty(BlockFenceGate.FACING, EnumFacing.WEST));
                    worldIn.setBlockState(position.add(4, 0, 0), Blocks.GRASS_PATH.getDefaultState());
                    worldIn.setBlockState(position.add(-4, 0, 0), Blocks.GRASS_PATH.getDefaultState());
                }
            }
        }
        return true;
    }
}
