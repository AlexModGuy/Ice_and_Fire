package com.github.alexthe666.iceandfire.event;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.core.ModBlocks;
import com.github.alexthe666.iceandfire.entity.*;
import com.github.alexthe666.iceandfire.structures.*;
import com.github.alexthe666.iceandfire.world.MyrmexWorldData;
import com.github.alexthe666.iceandfire.world.village.MapGenPixieVillage;
import com.github.alexthe666.iceandfire.world.village.MapGenSnowVillage;
import net.minecraft.block.material.Material;
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

public class StructureGenerator implements IWorldGenerator {

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
	private static final ResourceLocation GORGON_TEMPLE = new ResourceLocation(IceAndFire.MODID, "gorgon_temple");

	public static BlockPos getHeight(World world, BlockPos pos) {
		return world.getHeight(pos);
	}

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
		boolean prevLogCascadingWorldGen = net.minecraftforge.common.ForgeModContainer.logCascadingWorldGeneration;
		if(!IceAndFire.CONFIG.logCascadingWorldGen) {
			net.minecraftforge.common.ForgeModContainer.logCascadingWorldGeneration = false;
		}
		int x = (chunkX * 16) + 8;
		int z = (chunkZ * 16) + 8;
		BlockPos height = getHeight(world, new BlockPos(x, 0, z));
		if (IceAndFire.CONFIG.spawnGorgons && isFarEnoughFromSpawn(world, height)) {
			IBlockState blockState = world.getBlockState(height.down());
			if (blockState.isFullBlock() && world.isAirBlock(height.up()) && random.nextInt(IceAndFire.CONFIG.spawnGorgonsChance + 1) == 0 && BiomeDictionary.hasType(world.getBiome(height), Type.BEACH)) {
				Rotation rotation = Rotation.values()[random.nextInt(Rotation.values().length)];
				Mirror mirror = Mirror.values()[random.nextInt(Mirror.values().length)];
				MinecraftServer server = world.getMinecraftServer();
				TemplateManager templateManager = world.getSaveHandler().getStructureTemplateManager();
				PlacementSettings settings = new PlacementSettings().setRotation(rotation).setMirror(mirror);
				Template template = templateManager.getTemplate(server, GORGON_TEMPLE);
				BlockPos center = height.add(template.getSize().getX() / 2, -9, template.getSize().getZ() / 2);
				BlockPos corner1 = height.down();
				BlockPos corner2 = height.add(template.getSize().getX(), -1, 0);
				BlockPos corner3 = height.add(template.getSize().getX(), -1, template.getSize().getZ());
				BlockPos corner4 = height.add(0, -1, template.getSize().getZ());
				if (world.getBlockState(center).isOpaqueCube() && world.getBlockState(corner1).isOpaqueCube() && world.getBlockState(corner2).isOpaqueCube() && world.getBlockState(corner3).isOpaqueCube() && world.getBlockState(corner4).isOpaqueCube()) {
					template.addBlocksToWorldChunk(world, center, settings);
				}
			}
		}
		if(IceAndFire.CONFIG.generateSirenIslands && isFarEnoughFromSpawn(world, height) && BiomeDictionary.hasType(world.getBiome(height), Type.OCEAN) && !BiomeDictionary.hasType(world.getBiome(height), Type.COLD) && random.nextInt(IceAndFire.CONFIG.generateSirenChance + 1) == 0){
			SIREN_ISLAND.generate(world, random, height);
		}

		if(IceAndFire.CONFIG.generateCyclopsCaves && isFarEnoughFromSpawn(world, height) && BiomeDictionary.hasType(world.getBiome(height), Type.BEACH) && random.nextInt(IceAndFire.CONFIG.spawnCyclopsChance + 1) == 0 && world.getBlockState(height.down()).isOpaqueCube()){
			CYCLOPS_CAVE.generate(world, random, height);
		}
		if (IceAndFire.CONFIG.spawnPixies && isFarEnoughFromSpawn(world, height)) {
			boolean isSpookyForest = BiomeDictionary.hasType(world.getBiome(height), Type.FOREST) && (BiomeDictionary.hasType(world.getBiome(height), Type.SPOOKY) || BiomeDictionary.hasType(world.getBiome(height), Type.MAGICAL));
			if (isSpookyForest && random.nextInt(IceAndFire.CONFIG.spawnPixiesChance + 1) == 0) {
				PIXIE_VILLAGE.generate(world, random, height);
			}
		}
		if (IceAndFire.CONFIG.generateDragonRoosts && isFarEnoughFromSpawn(world, height) && !isDimensionBlacklisted(world.provider.getDimension(), true)) {
			boolean isHills = BiomeDictionary.hasType(world.getBiome(height), Type.HILLS) || BiomeDictionary.hasType(world.getBiome(height), Type.MOUNTAIN) && !BiomeDictionary.hasType(world.getBiome(height), Type.SNOWY);
			if (!world.getBiome(height).getEnableSnow() && world.getBiome(height).getDefaultTemperature() > -0.5 && world.getBiome(height) != Biomes.ICE_PLAINS && !BiomeDictionary.hasType(world.getBiome(height), Type.SNOWY) && !BiomeDictionary.hasType(world.getBiome(height), Type.SNOWY) && !BiomeDictionary.hasType(world.getBiome(height), Type.WET) && !BiomeDictionary.hasType(world.getBiome(height), Type.OCEAN) && !BiomeDictionary.hasType(world.getBiome(height), Type.RIVER)) {
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
			if (BiomeDictionary.hasType(world.getBiome(height), Type.COLD) && BiomeDictionary.hasType(world.getBiome(height), Type.SNOWY) && random.nextInt(IceAndFire.CONFIG.generateDragonSkeletonChance + 1) == 0) {
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
		if (IceAndFire.CONFIG.spawnHippocampus && BiomeDictionary.hasType(world.getBiome(height), Type.OCEAN) && random.nextInt(IceAndFire.CONFIG.hippocampusSpawnChance + 1) == 0) {
			for(int i = 0; i < random.nextInt(5); i++){
				BlockPos pos = new BlockPos(x + random.nextInt(10) - 5, 20 + random.nextInt(40), z + random.nextInt(10) - 5);
				if(world.getBlockState(pos).getMaterial() == Material.WATER){
					EntityHippocampus campus = new EntityHippocampus(world);
					campus.setVariant(random.nextInt(5));
					campus.setLocationAndAngles(pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, 0, 0);
					if(campus.isNotColliding()){
						world.spawnEntity(campus);
					}
				}
			}
		}
		if (IceAndFire.CONFIG.spawnSeaSerpents && BiomeDictionary.hasType(world.getBiome(height), Type.OCEAN) && random.nextInt(IceAndFire.CONFIG.seaSerpentSpawnChance + 1) == 0) {
			BlockPos pos = new BlockPos(x + random.nextInt(10) - 5, 20 + random.nextInt(40), z + random.nextInt(10) - 5);
			if(world.getBlockState(pos).getMaterial() == Material.WATER){
				EntitySeaSerpent serpent = new EntitySeaSerpent(world);
				serpent.onWorldSpawn(random);
				serpent.setLocationAndAngles(pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, 0, 0);
				world.spawnEntity(serpent);
			}
		}
		if (IceAndFire.CONFIG.spawnStymphalianBirds && isFarEnoughFromSpawn(world, height) && BiomeDictionary.hasType(world.getBiome(height), Type.SWAMP) && random.nextInt(IceAndFire.CONFIG.stymphalianBirdSpawnChance + 1) == 0) {
			for(int i = 0; i < 4 + random.nextInt(4); i++){
				BlockPos pos = height.add(random.nextInt(10) - 5, 0, random.nextInt(10) - 5);
				if(world.getBlockState(pos.down()).isOpaqueCube()){
					EntityStymphalianBird bird = new EntityStymphalianBird(world);
					bird.setLocationAndAngles(pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, 0, 0);
					if(bird.isNotColliding()){
						world.spawnEntity(bird);
					}
				}
			}
		}
		if (IceAndFire.CONFIG.generateDragonDens && isFarEnoughFromSpawn(world, height) && !isDimensionBlacklisted(world.provider.getDimension(), true)) {
			boolean isHills = BiomeDictionary.hasType(world.getBiome(height), Type.HILLS) || BiomeDictionary.hasType(world.getBiome(height), Type.MOUNTAIN);
			if (!world.getBiome(height).getEnableSnow() && world.getBiome(height).getDefaultTemperature() > -0.5 && !BiomeDictionary.hasType(world.getBiome(height), Type.COLD) && !BiomeDictionary.hasType(world.getBiome(height), Type.SNOWY) && !BiomeDictionary.hasType(world.getBiome(height), Type.WET) && !BiomeDictionary.hasType(world.getBiome(height), Type.OCEAN) && !BiomeDictionary.hasType(world.getBiome(height), Type.RIVER) && !BiomeDictionary.hasType(world.getBiome(height), Type.BEACH)) {
				if (random.nextInt((isHills ? IceAndFire.CONFIG.generateDragonDenChance : IceAndFire.CONFIG.generateDragonDenChance * 2) + 1) == 0) {
					int newY = 20 + random.nextInt(20);
					BlockPos pos = new BlockPos(x, newY, z);
					if (!world.canBlockSeeSky(pos)) {
						FIRE_DRAGON_CAVE.generate(world, random, pos);
					}
				}
			}
			if (BiomeDictionary.hasType(world.getBiome(height), Type.COLD) && BiomeDictionary.hasType(world.getBiome(height), Type.SNOWY) && !BiomeDictionary.hasType(world.getBiome(height), Type.BEACH)) {
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
		if (IceAndFire.CONFIG.generateMyrmexColonies && random.nextInt(IceAndFire.CONFIG.myrmexColonyGenChance) == 0 && isFarEnoughFromSpawn(world, height) && MyrmexWorldData.get(world).getNearestHive(height, 100) == null && (BiomeDictionary.hasType(world.getBiome(height), Type.JUNGLE) || BiomeDictionary.hasType(world.getBiome(height), Type.HOT) && BiomeDictionary.hasType(world.getBiome(height), Type.DRY) && BiomeDictionary.hasType(world.getBiome(height), Type.SANDY))) {
			BlockPos lowestHeight = new BlockPos(height.getX(), world.getChunksLowestHorizon(height.getX(), height.getZ()), height.getZ());
			int down = Math.max(15, lowestHeight.getY() - 20 + random.nextInt(10));
			if(BiomeDictionary.hasType(world.getBiome(height), Type.JUNGLE)){
				JUNGLE_MYRMEX_HIVE.generate(world, random, new BlockPos(lowestHeight.getX(), down, lowestHeight.getZ()));
			}else{
				DESERT_MYRMEX_HIVE.generate(world, random, new BlockPos(lowestHeight.getX(), down, lowestHeight.getZ()));
			}
		}
		if (BiomeDictionary.hasType(world.getBiome(height), Type.COLD) && BiomeDictionary.hasType(world.getBiome(height), Type.SNOWY)) {
			if (random.nextInt(5) == 0) {
				BlockPos surface = world.getHeight(new BlockPos(x, 0, z));
				if (ModBlocks.frost_lily.canPlaceBlockAt(world, surface)) {
					world.setBlockState(surface, ModBlocks.frost_lily.getDefaultState());
			}
			}
		}
		if (BiomeDictionary.hasType(world.getBiome(height), Type.HOT) && (BiomeDictionary.hasType(world.getBiome(height), Type.SANDY))){
			if (random.nextInt(5) == 0) {
				BlockPos surface = world.getHeight(new BlockPos(x, 0, z));
				if (ModBlocks.fire_lily.canPlaceBlockAt(world, surface)) {
					world.setBlockState(surface, ModBlocks.fire_lily.getDefaultState());
				}
			}
		}
		if (BiomeDictionary.hasType(world.getBiome(height), Type.NETHER)){
			if (random.nextInt(5) == 0) {
				BlockPos surface = getNetherHeight(world, new BlockPos(x, 0, z));
				if(surface != null){
					world.setBlockState(surface.up(), ModBlocks.fire_lily.getDefaultState());
				}
			}
		}
		if(!IceAndFire.CONFIG.logCascadingWorldGen) {
			net.minecraftforge.common.ForgeModContainer.logCascadingWorldGeneration = prevLogCascadingWorldGen;
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

	private BlockPos getNetherHeight(World world, BlockPos pos){
		for(int i = 0; i < 255; i++){
			BlockPos ground = pos.up(i);
			if(world.getBlockState(ground).getBlock() == Blocks.NETHERRACK && world.isAirBlock(ground.up())){
				return ground;
			}
		}
		return null;
	}

	private boolean isFarEnoughFromSpawn(World world, BlockPos pos){
		if(IceAndFire.CONFIG.dangerousWorldGenDistanceLimit == 0){
			return true;
		}
		BlockPos spawnRelative = new BlockPos(world.getSpawnPoint().getX(), pos.getY(), world.getSpawnPoint().getZ());
		return spawnRelative.distanceSq(pos) > IceAndFire.CONFIG.dangerousWorldGenDistanceLimit * IceAndFire.CONFIG.dangerousWorldGenDistanceLimit;
	}

	//private boolean isAether(World world) {
	//	if (IceAndFire.CONFIG.useAetherCompat) {
	//		if (world.provider.getDimension() == IceAndFire.CONFIG.aetherDimensionID) {
	//			return true;
	//		}
	//	}
	//	return false;
	//}
}
