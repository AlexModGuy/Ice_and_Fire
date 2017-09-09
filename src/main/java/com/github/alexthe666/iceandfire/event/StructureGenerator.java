package com.github.alexthe666.iceandfire.event;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.core.ModBlocks;
import com.github.alexthe666.iceandfire.entity.EntityFireDragon;
import com.github.alexthe666.iceandfire.structures.*;
import com.github.alexthe666.iceandfire.world.village.MapGenPixieVillage;
import com.github.alexthe666.iceandfire.world.village.MapGenSnowVillage;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StructureGenerator implements IWorldGenerator {

    public static final MapGenSnowVillage SNOW_VILLAGE = new MapGenSnowVillage();
    public static final MapGenPixieVillage PIXIE_VILLAGE = new MapGenPixieVillage();
    private static final WorldGenFireDragonCave FIRE_DRAGON_CAVE = new WorldGenFireDragonCave();
    private static final WorldGenFireDragonRoosts FIRE_DRAGON_ROOST = new WorldGenFireDragonRoosts();
    private static final WorldGenIceDragonCave ICE_DRAGON_CAVE = new WorldGenIceDragonCave();
    private static final WorldGenIceDragonRoosts ICE_DRAGON_ROOST = new WorldGenIceDragonRoosts();
    private static final ResourceLocation GORGON_TEMPLE = new ResourceLocation(IceAndFire.MODID, "gorgon_temple");

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
        if (IceAndFire.CONFIG.spawnGorgons) {
            IBlockState blockState = world.getBlockState(height);
            if(blockState.isFullBlock() && world.isAirBlock(height.up()) && random.nextInt(IceAndFire.CONFIG.spawnGorgonsChance + 1) == 0 && BiomeDictionary.hasType(world.getBiome(height), Type.BEACH)){
                Rotation rotation = Rotation.values()[random.nextInt(Rotation.values().length)];
                Mirror mirror = Mirror.values()[random.nextInt(Mirror.values().length)];
                MinecraftServer server = world.getMinecraftServer();
                TemplateManager templateManager = world.getSaveHandler().getStructureTemplateManager();
                PlacementSettings settings = new PlacementSettings().setRotation(rotation).setMirror(mirror);
                Template template = templateManager.getTemplate(server, GORGON_TEMPLE);
                BlockPos center = height.add(template.getSize().getX()/2, -9, template.getSize().getZ()/2);
                if(world.getBlockState(center).isOpaqueCube()){
                    template.addBlocksToWorldChunk(world, center, settings);
                }
            }
        }
        if (IceAndFire.CONFIG.spawnPixies) {
            boolean isSpookyForest = BiomeDictionary.hasType(world.getBiome(height), Type.FOREST) && (BiomeDictionary.hasType(world.getBiome(height), Type.SPOOKY) || BiomeDictionary.hasType(world.getBiome(height), Type.MAGICAL));
            if (isSpookyForest && random.nextInt(IceAndFire.CONFIG.spawnPixiesChance + 1) == 0) {
                PIXIE_VILLAGE.generate(world, random, height);
            }
        }
        if (IceAndFire.CONFIG.generateDragonRoosts && !isDimensionBlacklisted(world.provider.getDimension(), true)) {
            boolean isHills = BiomeDictionary.hasType(world.getBiome(height), Type.HILLS) || BiomeDictionary.hasType(world.getBiome(height), Type.MOUNTAIN);
            if (!world.getBiome(height).getEnableSnow() && world.getBiome(height).getTemperature() > -0.5 && world.getBiome(height) != Biomes.ICE_PLAINS && !BiomeDictionary.hasType(world.getBiome(height), Type.COLD) && !BiomeDictionary.hasType(world.getBiome(height), Type.SNOWY) && !BiomeDictionary.hasType(world.getBiome(height), Type.WET) && !BiomeDictionary.hasType(world.getBiome(height), Type.OCEAN) && !BiomeDictionary.hasType(world.getBiome(height), Type.RIVER) || isHills || isAether(world)) {
                if (random.nextInt((isHills ? IceAndFire.CONFIG.generateDragonRoostChance : IceAndFire.CONFIG.generateDragonRoostChance * 2) + 1) == 0) {
                    BlockPos surface = world.getHeight(new BlockPos(x, 0, z));
                    FIRE_DRAGON_ROOST.generate(world, random, surface);
                }
            }
            if (BiomeDictionary.hasType(world.getBiome(height), Type.COLD) && BiomeDictionary.hasType(world.getBiome(height), Type.SNOWY) || isAether(world)) {
                if (random.nextInt((isHills ? IceAndFire.CONFIG.generateDragonRoostChance : IceAndFire.CONFIG.generateDragonRoostChance * 2) + 1) == 0) {
                    BlockPos surface = world.getHeight(new BlockPos(x, 0, z));
                    ICE_DRAGON_ROOST.generate(world, random, surface);
                }
            }
        }
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
            if (!world.getBiome(height).getEnableSnow() && world.getBiome(height).getTemperature() > -0.5 && world.getBiome(height) != Biomes.ICE_PLAINS && !BiomeDictionary.hasType(world.getBiome(height), Type.COLD) && !BiomeDictionary.hasType(world.getBiome(height), Type.SNOWY) && !BiomeDictionary.hasType(world.getBiome(height), Type.WET) && !BiomeDictionary.hasType(world.getBiome(height), Type.OCEAN) && !BiomeDictionary.hasType(world.getBiome(height), Type.RIVER) || isHills || isAether(world)) {
                if (random.nextInt((isHills ? IceAndFire.CONFIG.generateDragonRoostChance : IceAndFire.CONFIG.generateDragonRoostChance * 2) + 1) == 0) {
                    BlockPos surface = world.getHeight(new BlockPos(x, 0, z));
                    FIRE_DRAGON_ROOST.generate(world, random, surface);
                }
            }
            if (BiomeDictionary.hasType(world.getBiome(height), Type.COLD) && BiomeDictionary.hasType(world.getBiome(height), Type.SNOWY) || isAether(world)) {
                if (random.nextInt((isHills ? IceAndFire.CONFIG.generateDragonRoostChance : IceAndFire.CONFIG.generateDragonRoostChance * 2) + 1) == 0) {
                    BlockPos surface = world.getHeight(new BlockPos(x, 0, z));
                    ICE_DRAGON_ROOST.generate(world, random, surface);
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
        boolean useBlackOrWhiteLists = IceAndFire.CONFIG.useDimensionBlackList;
        int[] blacklistedArray = dragons ? IceAndFire.CONFIG.dragonBlacklistedDimensions : IceAndFire.CONFIG.snowVillageBlacklistedDimensions;
        int[] whitelistedArray = dragons ? IceAndFire.CONFIG.dragonWhitelistedDimensions : IceAndFire.CONFIG.snowVillageWhitelistedDimensions;
        int[] array = useBlackOrWhiteLists ? blacklistedArray : whitelistedArray;
        List<Integer> dimList = new ArrayList<Integer>();
        for (int dimension : array) {
            dimList.add(dimension);
        }

        if (dimList.contains(id)) {
            return useBlackOrWhiteLists;
        }
        return !useBlackOrWhiteLists;
    }

    private boolean isAether(World world){
        if(IceAndFire.CONFIG.useAetherCompat){
            if(world.provider.getDimension() == IceAndFire.CONFIG.aetherDimensionID){
                return true;
            }
        }
        return false;
    }
}
