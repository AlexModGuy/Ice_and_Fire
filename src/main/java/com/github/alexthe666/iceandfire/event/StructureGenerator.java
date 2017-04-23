package com.github.alexthe666.iceandfire.event;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.core.ModBlocks;
import com.github.alexthe666.iceandfire.entity.EntityFireDragon;
import com.github.alexthe666.iceandfire.structures.WorldGenFireDragonCave;
import com.github.alexthe666.iceandfire.structures.WorldGenFireDragonRoosts;
import com.github.alexthe666.iceandfire.structures.WorldGenIceDragonCave;
import com.github.alexthe666.iceandfire.structures.WorldGenIceDragonRoosts;
import com.github.alexthe666.iceandfire.world.village.MapGenSnowVillage;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StructureGenerator implements IWorldGenerator {

    public static final MapGenSnowVillage SNOW_VILLAGE = new MapGenSnowVillage();
    private static final WorldGenFireDragonCave FIRE_DRAGON_CAVE = new WorldGenFireDragonCave();
    private static final WorldGenFireDragonRoosts FIRE_DRAGON_ROOST = new WorldGenFireDragonRoosts();
    private static final WorldGenIceDragonCave ICE_DRAGON_CAVE = new WorldGenIceDragonCave();
    private static final WorldGenIceDragonRoosts ICE_DRAGON_ROOST = new WorldGenIceDragonRoosts();

    public static BlockPos getHeight(World world, BlockPos pos) {
        for (int y = 0; y < 256; y++) {
            BlockPos pos1 = pos.up(y);
            if (world.getBlockState(pos1.up()).getBlock() == Blocks.AIR && world.getBlockState(pos1.down()).getBlock() != Blocks.AIR) {
                return pos1;
            }
        }
        return pos;
    }

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        int x = (chunkX * 16) + random.nextInt(16);
        int z = (chunkZ * 16) + random.nextInt(16);
        BlockPos height = getHeight(world, new BlockPos(x, 0, z));
        if (IceAndFire.CONFIG.generateDragonSkeletons) {
            if (BiomeDictionary.hasType(world.getBiome(height), Type.DRY) && BiomeDictionary.hasType(world.getBiome(height), Type.SANDY) && random.nextInt(IceAndFire.CONFIG.generateDragonSkeletonChance + 1) == 0) {
                EntityFireDragon firedragon = new EntityFireDragon(world);
                firedragon.setPosition(x, height.getY() + 1, z);
                int dragonage = 10 + random.nextInt(100);
                firedragon.growDragon(dragonage);
                firedragon.modelDeadProgress = 20;
                firedragon.setModelDead(true);
                firedragon.setDeathStage((dragonage / 5) / 2);
                firedragon.rotationYaw = random.nextInt(360);
                if (!world.isRemote) {
                    world.spawnEntity(firedragon);
                }
            }
        }
        if (IceAndFire.CONFIG.generateDragonRoosts && !isDimensionBlacklisted(world.provider.getDimension(), true)) {
            boolean isHills = BiomeDictionary.hasType(world.getBiome(height), Type.HILLS) || BiomeDictionary.hasType(world.getBiome(height), Type.MOUNTAIN);
            if (!world.getBiome(height).getEnableSnow() && world.getBiome(height).getTemperature() > -0.5 && world.getBiome(height) != Biomes.ICE_PLAINS && !BiomeDictionary.hasType(world.getBiome(height), Type.COLD) && !BiomeDictionary.hasType(world.getBiome(height), Type.SNOWY) && !BiomeDictionary.hasType(world.getBiome(height), Type.WET) && !BiomeDictionary.hasType(world.getBiome(height), Type.OCEAN) && !BiomeDictionary.hasType(world.getBiome(height), Type.RIVER) || isHills) {
                if (random.nextInt((isHills ? IceAndFire.CONFIG.generateDragonRoostChance : IceAndFire.CONFIG.generateDragonRoostChance * 2) + 1) == 0) {
                    BlockPos surface = world.getHeight(new BlockPos(x, 0, z));
                    FIRE_DRAGON_ROOST.generate(world, random, surface);
                }
            }
            if (BiomeDictionary.hasType(world.getBiome(height), Type.COLD) && BiomeDictionary.hasType(world.getBiome(height), Type.SNOWY)) {
                if (random.nextInt((isHills ? IceAndFire.CONFIG.generateDragonRoostChance : IceAndFire.CONFIG.generateDragonRoostChance * 2) + 1) == 0) {
                    BlockPos surface = world.getHeight(new BlockPos(x, 0, z));
                    ICE_DRAGON_ROOST.generate(world, random, surface);
                }
            }
        }
        if (IceAndFire.CONFIG.generateDragonDens && !isDimensionBlacklisted(world.provider.getDimension(), true)) {
            boolean isHills = BiomeDictionary.hasType(world.getBiome(height), Type.HILLS) || BiomeDictionary.hasType(world.getBiome(height), Type.MOUNTAIN);
            if (!world.getBiome(height).getEnableSnow() && world.getBiome(height).getTemperature() > -0.5 && !BiomeDictionary.hasType(world.getBiome(height), Type.COLD) && !BiomeDictionary.hasType(world.getBiome(height), Type.SNOWY) && !BiomeDictionary.hasType(world.getBiome(height), Type.WET) && !BiomeDictionary.hasType(world.getBiome(height), Type.OCEAN) && !BiomeDictionary.hasType(world.getBiome(height), Type.RIVER) || isHills) {
                if (random.nextInt((isHills ? IceAndFire.CONFIG.generateDragonDenChance : IceAndFire.CONFIG.generateDragonDenChance * 2) + 1) == 0) {
                    int newY = 20 + random.nextInt(20);
                    BlockPos pos = new BlockPos(x, newY, z);
                    if (!world.canBlockSeeSky(pos)) {
                        FIRE_DRAGON_CAVE.generate(world, random, pos);
                    }
                }
            }
            if (BiomeDictionary.hasType(world.getBiome(height), Type.COLD) && BiomeDictionary.hasType(world.getBiome(height), Type.SNOWY)) {
                if (random.nextInt((isHills ? IceAndFire.CONFIG.generateDragonDenChance : IceAndFire.CONFIG.generateDragonDenChance * 2) + 1) == 0) {
                    int newY = 20 + random.nextInt(20);
                    BlockPos pos = new BlockPos(x, newY, z);
                    ICE_DRAGON_CAVE.generate(world, random, pos);
                }
            }
        }
        if (IceAndFire.CONFIG.generateSilverOre) {
            for (int silverAmount = 0; silverAmount < 2; silverAmount++) {
                int oreHeight = random.nextInt(32);
                int xOre = (chunkX * 16) + random.nextInt(16);
                int zOre = (chunkZ * 16) + random.nextInt(16);
                new WorldGenMinable(ModBlocks.silverOre.getDefaultState(), 4 + random.nextInt(4)).generate(world, random, new BlockPos(xOre, oreHeight, zOre));
            }
        }
        if (IceAndFire.CONFIG.generateSapphireOre) {
            if (BiomeDictionary.hasType(world.getBiome(height), Type.SNOWY)) {
                int count = 3 + random.nextInt(6);
                for (int sapphireAmount = 0; sapphireAmount < count; sapphireAmount++) {
                    int oreHeight = random.nextInt(28) + 4;
                    int xOre = (chunkX * 16) + random.nextInt(16);
                    int zOre = (chunkZ * 16) + random.nextInt(16);
                    BlockPos pos = new BlockPos(xOre, oreHeight, zOre);
                    IBlockState state = world.getBlockState(pos);
                    if (state.getBlock().isReplaceableOreGen(state, world, pos, BlockMatcher.forBlock(Blocks.STONE))) {
                        world.setBlockState(pos, ModBlocks.sapphireOre.getDefaultState());
                    }
                }
            }
        }
        if (IceAndFire.CONFIG.generateSnowVillages && !isDimensionBlacklisted(world.provider.getDimension(), false) && BiomeDictionary.hasType(world.getBiome(height), Type.COLD) && BiomeDictionary.hasType(world.getBiome(height), Type.SNOWY)) {
            SNOW_VILLAGE.generate(world, random, height);
        }
        if (BiomeDictionary.hasType(world.getBiome(height), Type.COLD) && BiomeDictionary.hasType(world.getBiome(height), Type.SNOWY)) {
            if (random.nextInt(5) == 0) {
                BlockPos surface = world.getHeight(new BlockPos(x, 0, z));
                if (ModBlocks.frost_lily.canPlaceBlockAt(world, surface)) {
                    world.setBlockState(surface, ModBlocks.frost_lily.getDefaultState());
                }
            }
        }
        if (BiomeDictionary.hasType(world.getBiome(height), Type.HOT) && (BiomeDictionary.hasType(world.getBiome(height), Type.SANDY) || BiomeDictionary.hasType(world.getBiome(height), Type.NETHER))) {
            if (random.nextInt(5) == 0) {
                if (ModBlocks.fire_lily.canPlaceBlockAt(world, height.up())) {
                    world.setBlockState(height.up(), ModBlocks.fire_lily.getDefaultState());
                }
            }
        }
    }

    private boolean isDimensionBlacklisted(int id, boolean dragons) {
        int[] blacklistedArray = dragons ? IceAndFire.CONFIG.dragonBlacklistedDimensions : IceAndFire.CONFIG.snowVillageBlacklistedDimensions;
        List<Integer> dimList = new ArrayList<Integer>();
        for (int dimension : blacklistedArray) {
            dimList.add(dimension);
        }
        if (dimList.contains(id)) {
            return true;
        }
        return false;
    }
}
