package com.github.alexthe666.iceandfire;

import net.ilexiconn.llibrary.server.config.ConfigEntry;
import net.minecraft.init.Biomes;
import net.minecraft.world.biome.Biome;

public class IceAndFireConfig {

	@SuppressWarnings("deprecation")
	@ConfigEntry(category = "clientside", comment = "Wether to display the dragon on the main menu or not")
	public boolean customMainMenu = true;

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
	@ConfigEntry(category = "generation", comment = "true if glacier biomes can spawn")
	public boolean spawnGlaciers = true;
	@ConfigEntry(category = "generation", comment = "glacier biome spawn weight - lower = rarer")
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
	public int dragonFluteDistance = 8;
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
	@ConfigEntry(category = "dragons", comment = "How many blocks away can dragons spot potential prey. Note that increasing this could cause lag.")
	public int dragonTargetSearchLength = 64;

	@SuppressWarnings("deprecation")
	@ConfigEntry(category = "hippogryphs", comment = "True if hippogryphs are allowed to spawn.")
	public boolean spawnHippogryphs = true;
	@SuppressWarnings("deprecation")
	@ConfigEntry(category = "hippogryphs", comment = "Hippogryph spawn weight. Lower = lower chance to spawn.")
	public int hippogryphSpawnRate = 5;

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

	@ConfigEntry(category = "cyclopes", comment = "How far away cyclopes will detect sheep being hurt.")
	public int cyclopesSheepSearchLength = 17;
	@ConfigEntry(category = "cyclopes", comment = "Maximum cyclops health")
	public double cyclopsMaxHealth = 150;
	@ConfigEntry(category = "cyclopes", comment = "How much damage cyclopes cause with their kick and stomp attacks.")
	public double cyclopsAttackStrength = 15;
	@ConfigEntry(category = "cyclopes", comment = "How much damage cyclopes cause with their bite attack.")
	public double cyclopsBiteStrength = 40;
	@ConfigEntry(category = "cyclopes", comment = "Wether to spawn cyclops caves or not")
	public boolean generateCyclopsCaves = true;
	@ConfigEntry(category = "generation", comment = "1 out of this number chance per chunk for generation")
	public int spawnCyclopsChance = 80;

	@SuppressWarnings("deprecation")
	@ConfigEntry(category = "sirens", comment = "Maximum siren health")
	public double sirenMaxHealth = 100D;
	@SuppressWarnings("deprecation")
	@ConfigEntry(category = "sirens", comment = "Wether to spawn siren islands caves or not")
	public boolean generateSirenIslands = true;
	@ConfigEntry(category = "generation", comment = "1 out of this number chance per chunk for generation")
	public int generateSirenChance = 300;

	@SuppressWarnings("deprecation")
	@ConfigEntry(category = "hippocampi", comment = "True if hippocampi are allowed to spawn.")
	public boolean spawnHippocampus = true;

	@ConfigEntry(category = "death worms", comment = "How many blocks away can death worms spot potential prey. Note that increasing this could cause lag.")
	public int deathWormTargetSearchLength = 64;
	@SuppressWarnings("deprecation")
	@ConfigEntry(category = "sirens", comment = "Maximum deathworm health, for small worms. Scaled to size")
	public double deathWormMaxHealth = 30D;
	@SuppressWarnings("deprecation")
	@ConfigEntry(category = "sirens", comment = "Deathworm attack strength, for small worms. Scaled to size")
	public double deathWormAttackStrength = 6D;
	@SuppressWarnings("deprecation")
	@ConfigEntry(category = "death worms", comment = "True if death worms are allowed to spawn.")
	public boolean spawnDeathWorm = true;
	@SuppressWarnings("deprecation")
	@ConfigEntry(category = "death worms", comment = "Death worm spawn weight. Lower = lower chance to spawn.")
	public int deathWormSpawnRate = 5;

	@ConfigEntry(category = "cockatrices", comment = "How far away cockatrices will detect chickens being hurt.")
	public int cockatriceChickenSearchLength = 40;
	@ConfigEntry(category = "cockatrices", comment = "1 out of this number chance per tick for a chicken to lay a cockatrice egg.")
	public int cockatriceEggChance = 4320000;

	@ConfigEntry(category = "stymphalian birds", comment = "How many blocks away can stymphalian birds spot potential prey. Note that increasing this could cause lag.")
	public int stymphalianBirdTargetSearchLength = 64;
	//@SuppressWarnings("deprecation")
	//@ConfigEntry(category = "compatability", comment = "Turn this on if you use the aether mod and want dragons to spawn in that dimension")
	//public boolean useAetherCompat = false;
	//@SuppressWarnings("deprecation")
	//@ConfigEntry(category = "compatability", comment = "Aether Dimension ID - Ice Dragons and Fire Dragons will spawn here if option is used")
	//public int aetherDimensionID = 3;

}
