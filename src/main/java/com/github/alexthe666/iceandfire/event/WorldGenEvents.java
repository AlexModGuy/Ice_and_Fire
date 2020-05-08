package com.github.alexthe666.iceandfire.event;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.entity.*;
import com.github.alexthe666.iceandfire.world.gen.*;
import com.github.alexthe666.iceandfire.world.MyrmexWorldData;
import com.github.alexthe666.iceandfire.world.village.MapGenPixieVillage;
import com.github.alexthe666.iceandfire.world.village.MapGenSnowVillage;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockLog;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
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

public class WorldGenEvents implements IWorldGenerator {

    public static final MapGenSnowVillage SNOW_VILLAGE = new MapGenSnowVillage();
    public static final MapGenPixieVillage PIXIE_VILLAGE = new MapGenPixieVillage();
    public static final WorldGenMyrmexHive JUNGLE_MYRMEX_HIVE = new WorldGenMyrmexHive(false, true);
    public static final WorldGenMyrmexHive DESERT_MYRMEX_HIVE = new WorldGenMyrmexHive(false, false);
    private static final WorldGenFireDragonCave FIRE_DRAGON_CAVE = new WorldGenFireDragonCave();
    private static final WorldGenFireDragonRoosts FIRE_DRAGON_ROOST = new WorldGenFireDragonRoosts();
    private static final WorldGenIceDragonCave ICE_DRAGON_CAVE = new WorldGenIceDragonCave();
    private static final WorldGenIceDragonRoosts ICE_DRAGON_ROOST = new WorldGenIceDragonRoosts();
    private static final WorldGenCyclopsCave CYCLOPS_CAVE = new WorldGenCyclopsCave();
    private static final WorldGenSirenIsland SIREN_ISLAND = new WorldGenSirenIsland();
    private static final WorldGenHydraCave HYDRA_CAVE = new WorldGenHydraCave();
    private static final ResourceLocation GORGON_TEMPLE = new ResourceLocation(IceAndFire.MODID, "gorgon_temple");
    private BlockPos lastSnowVillage = null;
    private BlockPos lastPixieVillage = null;
    private BlockPos lastMyrmexHive = null;
    private BlockPos lastDragonRoost = null;
    private BlockPos lastDragonCave = null;
    private BlockPos lastCyclopsCave = null;
    private BlockPos lastSirenIsland = null;
    private BlockPos lastGorgonTemple = null;
    private BlockPos lastMausoleum = null;
    private BlockPos lastHydraCave = null;

    public static BlockPos getHeight(World world, BlockPos pos) {
        return world.getHeight(pos);
    }

    private static boolean canHeightSkipBlock(BlockPos pos, World world) {
        BlockState state = world.getBlockState(pos);
        return state.getBlock() instanceof BlockLog || state.getBlock() instanceof BlockLiquid;
    }

    public static BlockPos degradeSurface(World world, BlockPos surface) {
        while ((!world.getBlockState(surface).isOpaqueCube() || canHeightSkipBlock(surface, world)) && surface.getY() > 1) {
            surface = surface.down();
        }
        return surface;
    }

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        if (world.getWorldType() == WorldType.FLAT && !IafConfig.spawnStructuresOnSuperflat) {
            return;
        }
        boolean prevLogCascadingWorldGen = net.minecraftforge.common.ForgeModContainer.logCascadingWorldGeneration;
        double spawnCheck = IafConfig.worldGenDistance * IafConfig.worldGenDistance;
        if (!IafConfig.logCascadingWorldGen) {
            net.minecraftforge.common.ForgeModContainer.logCascadingWorldGeneration = false;
        }
        int x = (chunkX * 16) + 8;
        int z = (chunkZ * 16) + 8;
        BlockPos height = getHeight(world, new BlockPos(x, 0, z));
        if (IafConfig.spawnGorgons && !isDimensionBlacklisted(world.provider.getDimension(), false) && (lastGorgonTemple == null || lastGorgonTemple.distanceSq(height) >= spawnCheck)) {
            if (BiomeDictionary.hasType(world.getBiome(height), Type.BEACH)) {
                if (random.nextInt(IafConfig.spawnGorgonsChance + 1) == 0) {
                    BlockPos surface = world.getHeight(new BlockPos(x, 0, z));
                    surface = degradeSurface(world, surface);
                    new WorldGenGorgonTemple(Direction.byHorizontalIndex(random.nextInt(3))).generate(world, random, surface);
                    lastGorgonTemple = surface;
                }
            }
        }
        if (IafConfig.generateSirenIslands && isFarEnoughFromSpawn(world, height) && BiomeDictionary.hasType(world.getBiome(height), Type.OCEAN) && !BiomeDictionary.hasType(world.getBiome(height), Type.COLD)
                && random.nextInt(IafConfig.generateSirenChance + 1) == 0 && !isDimensionBlacklisted(world.provider.getDimension(), false) && (lastSirenIsland == null || lastSirenIsland.distanceSq(height) >= spawnCheck)) {
            SIREN_ISLAND.generate(world, random, height);
            lastSirenIsland = height;
        }

        if (IafConfig.generateCyclopsCaves && isFarEnoughFromSpawn(world, height) && BiomeDictionary.hasType(world.getBiome(height), Type.BEACH)
                && random.nextInt(IafConfig.spawnCyclopsCaveChance + 1) == 0 && world.getBlockState(height.down()).isOpaqueCube() && !isDimensionBlacklisted(world.provider.getDimension(), false) && (lastCyclopsCave == null || lastCyclopsCave.distanceSq(height) >= spawnCheck)) {
            CYCLOPS_CAVE.generate(world, random, height);
            lastCyclopsCave = height;
        }
        if (IafConfig.generateWanderingCyclops && isFarEnoughFromSpawn(world, height) && BiomeDictionary.hasType(world.getBiome(height), Type.PLAINS) && (lastCyclopsCave == null || lastCyclopsCave.distanceSq(height) >= spawnCheck)) {
            if (random.nextInt(IafConfig.spawnWanderingCyclopsChance + 1) == 0) {
                EntityCyclops cyclops = new EntityCyclops(world);
                cyclops.setPosition(x, height.getY() + 1, z);
                cyclops.setVariant(random.nextInt(3));
                if (!world.isRemote) {
                    world.spawnEntity(cyclops);
                }
                for(int i = 0; i < 3 + random.nextInt(3); i++){
                    EntitySheep sheep = new EntitySheep(world);
                    sheep.setPosition(x, height.getY() + 1, z);
                    sheep.setFleeceColor(EntitySheep.getRandomSheepColor(random));
                    if (!world.isRemote) {
                        world.spawnEntity(sheep);
                    }
                }
                lastCyclopsCave = height;
            }
        }
        if (IafConfig.spawnPixies && isFarEnoughFromSpawn(world, height) && !isDimensionBlacklisted(world.provider.getDimension(), false) && (lastPixieVillage == null || lastPixieVillage.distanceSq(height) >= spawnCheck)) {
            boolean isSpookyForest = BiomeDictionary.hasType(world.getBiome(height), Type.FOREST) && (BiomeDictionary.hasType(world.getBiome(height), Type.SPOOKY) || BiomeDictionary.hasType(world.getBiome(height), Type.MAGICAL));
            if (isSpookyForest && random.nextInt(IafConfig.spawnPixiesChance + 1) == 0) {
                PIXIE_VILLAGE.generate(world, random, height);
                lastPixieVillage = height;
            }
        }
        if (IafConfig.generateDragonRoosts && isFarEnoughFromSpawn(world, height) && !isDimensionBlacklisted(world.provider.getDimension(), true) && (lastDragonRoost == null || lastDragonRoost.distanceSq(height) >= spawnCheck)) {
            boolean isHills = BiomeDictionary.hasType(world.getBiome(height), Type.HILLS) || BiomeDictionary.hasType(world.getBiome(height), Type.MOUNTAIN) && !BiomeDictionary.hasType(world.getBiome(height), Type.SNOWY);
            if (!world.getBiome(height).getEnableSnow() && world.getBiome(height).getDefaultTemperature() > 0.0 && world.getBiome(height) != Biomes.ICE_PLAINS && !BiomeDictionary.hasType(world.getBiome(height), Type.COLD) && !BiomeDictionary.hasType(world.getBiome(height), Type.SNOWY) && !BiomeDictionary.hasType(world.getBiome(height), Type.WET) && !BiomeDictionary.hasType(world.getBiome(height), Type.OCEAN) && !BiomeDictionary.hasType(world.getBiome(height), Type.RIVER)) {
                if (random.nextInt((isHills ? IafConfig.generateDragonRoostChance : IafConfig.generateDragonRoostChance * 2) + 1) == 0) {
                    BlockPos surface = world.getHeight(new BlockPos(x, 0, z));
                    surface = degradeSurface(world, surface);
                    FIRE_DRAGON_ROOST.generate(world, random, surface);
                    lastDragonRoost = surface;
                }
            }
            if (BiomeDictionary.hasType(world.getBiome(height), Type.COLD) && BiomeDictionary.hasType(world.getBiome(height), Type.SNOWY)) {
                if (random.nextInt((isHills ? IafConfig.generateDragonRoostChance : IafConfig.generateDragonRoostChance * 2) + 1) == 0) {
                    BlockPos surface = world.getHeight(new BlockPos(x, 0, z));
                    surface = degradeSurface(world, surface);
                    ICE_DRAGON_ROOST.generate(world, random, surface);
                    lastDragonRoost = surface;
                }
            }
        }
        if (IafConfig.generateDragonSkeletons && !isDimensionBlacklisted(world.provider.getDimension(), true)) {
            if (BiomeDictionary.hasType(world.getBiome(height), Type.DRY) && BiomeDictionary.hasType(world.getBiome(height), Type.SANDY) && random.nextInt(IafConfig.generateDragonSkeletonChance + 1) == 0) {
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
            if (BiomeDictionary.hasType(world.getBiome(height), Type.COLD) && BiomeDictionary.hasType(world.getBiome(height), Type.SNOWY) && random.nextInt(IafConfig.generateDragonSkeletonChance + 1) == 0) {
                EntityIceDragon icedragon = new EntityIceDragon(world);
                icedragon.setPosition(x, height.getY() + 1, z);
                int dragonage = 10 + random.nextInt(100);
                icedragon.growDragon(dragonage);
                icedragon.modelDeadProgress = 20;
                icedragon.setModelDead(true);
                icedragon.setDeathStage((dragonage / 5) / 2);
                icedragon.rotationYaw = random.nextInt(360);
                if (!world.isRemote) {
                    world.spawnEntity(icedragon);
                }
            }
        }
        if (IafConfig.spawnHippocampus && BiomeDictionary.hasType(world.getBiome(height), Type.OCEAN) && random.nextInt(IafConfig.hippocampusSpawnChance + 1) == 0) {
            for (int i = 0; i < random.nextInt(5); i++) {
                BlockPos pos = new BlockPos(x + random.nextInt(10) - 5, 20 + random.nextInt(40), z + random.nextInt(10) - 5);
                if (world.getBlockState(pos).getMaterial() == Material.WATER) {
                    EntityHippocampus campus = new EntityHippocampus(world);
                    campus.setVariant(random.nextInt(5));
                    campus.setLocationAndAngles(pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, 0, 0);
                    if (campus.isNotColliding()) {
                        world.spawnEntity(campus);
                    }
                }
            }
        }
        if (IafConfig.spawnSeaSerpents && BiomeDictionary.hasType(world.getBiome(height), Type.OCEAN) && random.nextInt(IafConfig.seaSerpentSpawnChance + 1) == 0) {
            BlockPos pos = new BlockPos(x + random.nextInt(10) - 5, 20 + random.nextInt(40), z + random.nextInt(10) - 5);
            if (world.getBlockState(pos).getMaterial() == Material.WATER) {
                EntitySeaSerpent serpent = new EntitySeaSerpent(world);
                serpent.onWorldSpawn(random);
                serpent.setLocationAndAngles(pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, 0, 0);
                world.spawnEntity(serpent);
            }
        }
        if (IafConfig.spawnStymphalianBirds && isFarEnoughFromSpawn(world, height) && BiomeDictionary.hasType(world.getBiome(height), Type.SWAMP) && random.nextInt(IafConfig.stymphalianBirdSpawnChance + 1) == 0) {
            for (int i = 0; i < 4 + random.nextInt(4); i++) {
                BlockPos pos = height.add(random.nextInt(10) - 5, 0, random.nextInt(10) - 5);
                if (world.getBlockState(pos.down()).isOpaqueCube()) {
                    EntityStymphalianBird bird = new EntityStymphalianBird(world);
                    bird.setLocationAndAngles(pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, 0, 0);
                    if (bird.isNotColliding()) {
                        world.spawnEntity(bird);
                    }
                }
            }
        }
        if (IafConfig.generateDragonDens && isFarEnoughFromSpawn(world, height) && !isDimensionBlacklisted(world.provider.getDimension(), true) && (lastDragonCave == null || lastDragonCave.distanceSq(height) >= spawnCheck)) {
            boolean isHills = BiomeDictionary.hasType(world.getBiome(height), Type.HILLS) || BiomeDictionary.hasType(world.getBiome(height), Type.MOUNTAIN);
            if (!world.getBiome(height).getEnableSnow() && world.getBiome(height).getDefaultTemperature() > 0.0 && !BiomeDictionary.hasType(world.getBiome(height), Type.COLD) && !BiomeDictionary.hasType(world.getBiome(height), Type.SNOWY) && !BiomeDictionary.hasType(world.getBiome(height), Type.WET) && !BiomeDictionary.hasType(world.getBiome(height), Type.OCEAN) && !BiomeDictionary.hasType(world.getBiome(height), Type.RIVER) && !BiomeDictionary.hasType(world.getBiome(height), Type.BEACH)) {
                if (random.nextInt((isHills ? IafConfig.generateDragonDenChance : IafConfig.generateDragonDenChance * 2) + 1) == 0) {
                    int newY = 20 + random.nextInt(20);
                    BlockPos pos = new BlockPos(x, newY, z);
                    if (!world.canBlockSeeSky(pos)) {
                        FIRE_DRAGON_CAVE.generate(world, random, pos);
                        lastDragonCave = pos;
                    }
                }
            }
            if (BiomeDictionary.hasType(world.getBiome(height), Type.COLD) && BiomeDictionary.hasType(world.getBiome(height), Type.SNOWY) && !BiomeDictionary.hasType(world.getBiome(height), Type.BEACH)) {
                if (random.nextInt((isHills ? IafConfig.generateDragonDenChance : IafConfig.generateDragonDenChance * 2) + 1) == 0) {
                    int newY = 20 + random.nextInt(20);
                    BlockPos pos = new BlockPos(x, newY, z);
                    ICE_DRAGON_CAVE.generate(world, random, pos);
                    lastDragonCave = pos;
                }
            }
        }
        if (IafConfig.generateSilverOre) {
            for (int silverAmount = 0; silverAmount < 2; silverAmount++) {
                int oreHeight = random.nextInt(32);
                int xOre = (chunkX * 16) + random.nextInt(16);
                int zOre = (chunkZ * 16) + random.nextInt(16);
                new WorldGenMinable(IafBlockRegistry.SILVER_ORE.getDefaultState(), 4 + random.nextInt(4)).generate(world, random, new BlockPos(xOre, oreHeight, zOre));
            }
        }
        if (IafConfig.generateSapphireOre) {
            if (BiomeDictionary.hasType(world.getBiome(height), Type.SNOWY)) {
                int count = 3 + random.nextInt(6);
                for (int sapphireAmount = 0; sapphireAmount < count; sapphireAmount++) {
                    int oreHeight = random.nextInt(28) + 4;
                    int xOre = (chunkX * 16) + random.nextInt(16);
                    int zOre = (chunkZ * 16) + random.nextInt(16);
                    BlockPos pos = new BlockPos(xOre, oreHeight, zOre);
                    BlockState state = world.getBlockState(pos);
                    if (state.getBlock().isReplaceableOreGen(state, world, pos, BlockMatcher.forBlock(Blocks.STONE))) {
                        world.setBlockState(pos, IafBlockRegistry.SAPPHIRE_ORE.getDefaultState());
                    }
                }
            }
        }
        if (IafConfig.generateSnowVillages && !isDimensionBlacklisted(world.provider.getDimension(), false) && BiomeDictionary.hasType(world.getBiome(height), Type.COLD) && BiomeDictionary.hasType(world.getBiome(height), Type.SNOWY) && (lastSnowVillage == null || lastSnowVillage.distanceSq(height) >= spawnCheck)) {
            SNOW_VILLAGE.generate(world, random, height);
            lastSnowVillage = height;
        }
        if (IafConfig.generateMyrmexColonies && random.nextInt(IafConfig.myrmexColonyGenChance) == 0 && isFarEnoughFromSpawn(world, height) && MyrmexWorldData.get(world).getNearestHive(height, 500) == null && (BiomeDictionary.hasType(world.getBiome(height), Type.JUNGLE) || BiomeDictionary.hasType(world.getBiome(height), Type.HOT) && BiomeDictionary.hasType(world.getBiome(height), Type.DRY) && BiomeDictionary.hasType(world.getBiome(height), Type.SANDY))
                && !isDimensionBlacklisted(world.provider.getDimension(), false) && (lastMyrmexHive == null || lastMyrmexHive.distanceSq(height) >= spawnCheck)) {
            BlockPos lowestHeight = new BlockPos(height.getX(), world.getChunksLowestHorizon(height.getX(), height.getZ()), height.getZ());
            int down = Math.max(15, lowestHeight.getY() - 20 + random.nextInt(10));
            if (BiomeDictionary.hasType(world.getBiome(height), Type.JUNGLE)) {
                JUNGLE_MYRMEX_HIVE.generate(world, random, new BlockPos(lowestHeight.getX(), down, lowestHeight.getZ()));
            } else {
                DESERT_MYRMEX_HIVE.generate(world, random, new BlockPos(lowestHeight.getX(), down, lowestHeight.getZ()));
            }
            lastMyrmexHive = height;
        }
        if (!isDimensionBlacklisted(world.provider.getDimension(), false)) {
            if (BiomeDictionary.hasType(world.getBiome(height), Type.COLD) && BiomeDictionary.hasType(world.getBiome(height), Type.SNOWY)) {
                if (random.nextInt(15) == 0) {
                    BlockPos surface = world.getHeight(new BlockPos(x, 0, z));
                    if (IafBlockRegistry.FROST_LILY.canPlaceBlockAt(world, surface)) {
                        world.setBlockState(surface, IafBlockRegistry.FROST_LILY.getDefaultState());
                    }
                }
            }
            if (BiomeDictionary.hasType(world.getBiome(height), Type.HOT) && (BiomeDictionary.hasType(world.getBiome(height), Type.SANDY))) {
                if (random.nextInt(15) == 0) {
                    BlockPos surface = world.getHeight(new BlockPos(x, 0, z));
                    if (IafBlockRegistry.FIRE_LILY.canPlaceBlockAt(world, surface)) {
                        world.setBlockState(surface, IafBlockRegistry.FIRE_LILY.getDefaultState());
                    }
                }
            }
            if (BiomeDictionary.hasType(world.getBiome(height), Type.NETHER)) {
                if (random.nextInt(15) == 0) {
                    BlockPos surface = getNetherHeight(world, new BlockPos(x, 0, z));
                    if (surface != null) {
                        world.setBlockState(surface.up(), IafBlockRegistry.FIRE_LILY.getDefaultState());
                    }
                }
            }
        }
        if (IafConfig.generateMausoleums && !isDimensionBlacklisted(world.provider.getDimension(), false) && (lastMausoleum == null || lastMausoleum.distanceSq(height) >= spawnCheck)) {
            if (BiomeDictionary.hasType(world.getBiome(height), Type.COLD) && BiomeDictionary.hasType(world.getBiome(height), Type.SNOWY)) {
                if (random.nextInt(IafConfig.generateMausoleumChance + 1) == 0) {
                    BlockPos surface = world.getHeight(new BlockPos(x, 0, z));
                    surface = degradeSurface(world, surface);
                    new WorldGenMausoleum(Direction.byHorizontalIndex(random.nextInt(3))).generate(world, random, surface);
                    lastMausoleum = surface;
                }
            }
        }
        if (IafConfig.generateHydraCaves && isFarEnoughFromSpawn(world, height) && BiomeDictionary.hasType(world.getBiome(height), Type.SWAMP)
                && random.nextInt(IafConfig.generateHydraChance + 1) == 0 && world.getBlockState(height.down()).isOpaqueCube() && !isDimensionBlacklisted(world.provider.getDimension(), false) && (lastHydraCave == null || lastHydraCave.distanceSq(height) >= spawnCheck)) {
            HYDRA_CAVE.generate(world, random, height);
            lastHydraCave = height;
        }
        if (!IafConfig.logCascadingWorldGen) {
            net.minecraftforge.common.ForgeModContainer.logCascadingWorldGeneration = prevLogCascadingWorldGen;
        }
    }

    private boolean isDimensionBlacklisted(int id, boolean dragons) {
        if(id == IafConfig.dreadlandsDimensionId){
            return true;
        }
        boolean useBlackOrWhiteLists = IafConfig.useDimensionBlackList;
        int[] blacklistedArray = dragons ? IafConfig.dragonBlacklistedDimensions : IafConfig.structureBlacklistedDimensions;
        int[] whitelistedArray = dragons ? IafConfig.dragonWhitelistedDimensions : IafConfig.structureWhitelistedDimensions;
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

    private BlockPos getNetherHeight(World world, BlockPos pos) {
        for (int i = 0; i < 255; i++) {
            BlockPos ground = pos.up(i);
            if (world.getBlockState(ground).getBlock() == Blocks.NETHERRACK && world.isAirBlock(ground.up())) {
                return ground;
            }
        }
        return null;
    }

    private boolean isFarEnoughFromSpawn(World world, BlockPos pos) {
        BlockPos spawnRelative = new BlockPos(world.getSpawnPoint().getX(), pos.getY(), world.getSpawnPoint().getZ());
        boolean spawnCheck = spawnRelative.distanceSq(pos) > IafConfig.dangerousWorldGenDistanceLimit * IafConfig.dangerousWorldGenDistanceLimit;
        return spawnCheck;
    }

    //private boolean isAether(World world) {
    //	if (IafConfig.useAetherCompat) {
    //		if (world.provider.getDimension() == IafConfig.aetherDimensionID) {
    //			return true;
    //		}
    //	}
    //	return false;
    //}
}
