package com.github.alexthe666.iceandfire;

import net.ilexiconn.llibrary.server.config.ConfigEntry;
import net.minecraft.init.Biomes;
import net.minecraft.world.biome.Biome;

public class IceAndFireConfig {

	@SuppressWarnings("deprecation")
	@ConfigEntry(category = "generation")
	public boolean generateSilverOre = true;
	@SuppressWarnings("deprecation")
	@ConfigEntry(category = "generation")
	public boolean generateSapphireOre = true;
	@SuppressWarnings("deprecation")
	@ConfigEntry(category = "generation", comment = "Generate dragon skeletons in hot biomes?")
	public boolean generateDragonSkeletons = true;
	@SuppressWarnings("deprecation")
	@ConfigEntry(category = "generation", comment = "1 out of this number chance per chunk for generation")
	public int generateDragonSkeletonChance = 150;
	@SuppressWarnings("deprecation")
	@ConfigEntry(category = "generation")
	public boolean generateDragonDens = true;
	@SuppressWarnings("deprecation")
	@ConfigEntry(category = "generation", comment = "1 out of this number chance per chunk for generation")
	public int generateDragonDenChance = 180;
	@SuppressWarnings("deprecation")
	@ConfigEntry(category = "generation")
	public boolean generateDragonRoosts = true;
	@SuppressWarnings("deprecation")
	@ConfigEntry(category = "generation", comment = "1 out of this number chance per chunk for generation")
	public int generateDragonRoostChance = 360;
	@SuppressWarnings("deprecation")
	@ConfigEntry(category = "generation", comment = "1 out of this number chance per block that gold will generate in dragon lairs.")
	public int dragonDenGoldAmount = 4;
	@SuppressWarnings("deprecation")
	@ConfigEntry(category = "generation")
	public boolean generateSnowVillages = true;
	@SuppressWarnings("deprecation")
	@ConfigEntry(category = "generation", comment = "1 out of this number chance per chunk for generation")
	public int generateSnowVillageChance = 100;
	@SuppressWarnings("deprecation")
	@ConfigEntry(category = "generation", comment = "Dragons cannot spawn in these dimensions' IDs")
	public int[] dragonBlacklistedDimensions = new int[]{1, -1};
	@SuppressWarnings("deprecation")
	@ConfigEntry(category = "generation", comment = "Snow Villages cannot spawn in these dimensions' IDs")
	public int[] snowVillageBlacklistedDimensions = new int[]{1, -1};
	@SuppressWarnings("deprecation")
	@ConfigEntry(category = "generation", comment = "true to use Blacklist configs, false to use Whitelist configs")
	public boolean useDimensionBlackList = true;
	@SuppressWarnings("deprecation")
	@ConfigEntry(category = "generation", comment = "Dragons can only spawn in these dimensions' IDs")
	public int[] dragonWhitelistedDimensions = new int[]{0};
	@SuppressWarnings("deprecation")
	@ConfigEntry(category = "generation", comment = "Snow Villages can only spawn in these dimensions' IDs")
	public int[] snowVillageWhitelistedDimensions = new int[]{0};
	@SuppressWarnings("deprecation")
	@ConfigEntry(category = "generation", comment = "glacier biome spawn chance - higher the number, higher the rarity")
	public int glacierSpawnChance = 4;
	@SuppressWarnings("deprecation")
	@ConfigEntry(category = "generation", comment = "Ratio of Stone(this number) to Ores in Dragon Caves")
	public int oreToStoneRatioForDragonCaves = 45;

	@SuppressWarnings("deprecation")
	@ConfigEntry(category = "dragons", comment = "Dragon griefing - 2 is no griefing, 1 is breaking weak blocks, 0 is default")
	public int dragonGriefing = 0;
	@SuppressWarnings("deprecation")
	@ConfigEntry(category = "dragons", comment = "Dragon Flap Noise Distance - Larger number, further away you can hear it")
	public int dragonFlapNoiseDistance = 4;
	@SuppressWarnings("deprecation")
	@ConfigEntry(category = "dragons", comment = "Dragon Flute Distance - how many chunks away is the dragon flute effective?")
	public int dragonFluteDistance = 4;
	@SuppressWarnings("deprecation")
	@ConfigEntry(category = "dragons", comment = "Max dragon health. Health is scaled to this")
	public int dragonHealth = 500;
	@SuppressWarnings("deprecation")
	@ConfigEntry(category = "dragons", comment = "Max dragon attack damage. Attack Damage is scaled to this")
	public int dragonAttackDamage = 17;
	@SuppressWarnings("deprecation")
	@ConfigEntry(category = "dragons", comment = "How high dragons can fly, in Y height.")
	public int maxDragonFlight = 128;
	@SuppressWarnings("deprecation")
	@ConfigEntry(category = "dragons", comment = "How far away dragons will detect gold blocks being destroyed or chests being opened.")
	public int dragonGoldSearchLength = 17;
	@SuppressWarnings("deprecation")
	@ConfigEntry(category = "dragons", comment = "Modifier of how far 3rd person distance is whilst riding a dragon, scaled against size.")
	public float dragonRiding3rdPersonDistanceModifier = 0.8F;
	@SuppressWarnings("deprecation")
	@ConfigEntry(category = "dragons", comment = "True if dragons can despawn. Note that if this is false there may be SERIOUS lag issues.")
	public boolean canDragonsDespawn = true;

	@SuppressWarnings("deprecation")
	@ConfigEntry(category = "hippogryphs", comment = "True if hippogryphs are allowed to spawn.")
	public boolean spawnHippogryphs = true;
	@SuppressWarnings("deprecation")
	@ConfigEntry(category = "hippogryphs", comment = "List of biome ids for black hippogryphs to spawn in.")
	public int[] hippogryphBlackBiomes = new int[]{Biome.getIdForBiome(Biomes.DESERT_HILLS)};
	@SuppressWarnings("deprecation")
	@ConfigEntry(category = "hippogryphs", comment = "List of biome ids for brown hippogryphs to spawn in.")
	public int[] hippogryphBrownBiomes = new int[]{Biome.getIdForBiome(Biomes.EXTREME_HILLS)};
	@SuppressWarnings("deprecation")
	@ConfigEntry(category = "hippogryphs", comment = "List of biome ids for chestnut hippogryphs to spawn in.")
	public int[] hippogryphChestnutBiomes = new int[]{Biome.getIdForBiome(Biomes.BIRCH_FOREST_HILLS)};
	@SuppressWarnings("deprecation")
	@ConfigEntry(category = "hippogryphs", comment = "List of biome ids for creamy hippogryphs to spawn in.")
	public int[] hippogryphCreamyBiomes = new int[]{Biome.getIdForBiome(Biomes.SAVANNA_PLATEAU)};
	@SuppressWarnings("deprecation")
	@ConfigEntry(category = "hippogryphs", comment = "List of biome ids for dark brown hippogryphs to spawn in.")
	public int[] hippogryphDarkBrownBiomes = new int[]{Biome.getIdForBiome(Biomes.TAIGA_HILLS)};
	@SuppressWarnings("deprecation")
	@ConfigEntry(category = "hippogryphs", comment = "List of biome ids for gray hippogryphs to spawn in.")
	public int[] hippogryphGrayBiomes = new int[]{Biome.getIdForBiome(Biomes.MUTATED_ROOFED_FOREST)};
	@SuppressWarnings("deprecation")
	@ConfigEntry(category = "hippogryphs", comment = "List of biome ids for white hippogryphs to spawn in.")
	public int[] hippogryphWhiteBiomes = new int[]{Biome.getIdForBiome(Biomes.ICE_MOUNTAINS)};

	@SuppressWarnings("deprecation")
	@ConfigEntry(category = "gorgons", comment = "Wether to spawn gorgon structures or not")
	public boolean spawnGorgons = true;
	@SuppressWarnings("deprecation")
	@ConfigEntry(category = "generation", comment = "1 out of this number chance per chunk for generation")
	public int spawnGorgonsChance = 80;
	@SuppressWarnings("deprecation")
	@ConfigEntry(category = "gorgons", comment = "Maximum gorgon health")
	public double gorgonMaxHealth = 100D;

	@SuppressWarnings("deprecation")
	@ConfigEntry(category = "pixies", comment = "Wether to spawn pixie villages or not")
	public boolean spawnPixies = true;
	@SuppressWarnings("deprecation")
	@ConfigEntry(category = "pixies", comment = "1 out of this number chance per chunk for generation")
	public int spawnPixiesChance = 60;
	@SuppressWarnings("deprecation")
	@ConfigEntry(category = "pixies", comment = "The size of pixie villages.")
	public int pixieVillageSize = 5;

	//@SuppressWarnings("deprecation")
	//@ConfigEntry(category = "compatability", comment = "Turn this on if you use the aether mod and want dragons to spawn in that dimension")
	//public boolean useAetherCompat = false;
	//@SuppressWarnings("deprecation")
	//@ConfigEntry(category = "compatability", comment = "Aether Dimension ID - Ice Dragons and Fire Dragons will spawn here if option is used")
	//public int aetherDimensionID = 3;

}
