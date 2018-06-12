package com.github.alexthe666.iceandfire;

import net.ilexiconn.llibrary.server.config.ConfigEntry;
import net.minecraft.init.Biomes;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.config.Configuration;

public class IceAndFireConfig {

	public boolean customMainMenu = true;
	public boolean generateSilverOre = true;
	public boolean generateSapphireOre = true;
	public boolean generateDragonSkeletons = true;
	public int generateDragonSkeletonChance = 150;
	public boolean generateDragonDens = true;
	public int generateDragonDenChance = 180;
	public boolean generateDragonRoosts = true;
	public int generateDragonRoostChance = 360;
	public int dragonDenGoldAmount = 4;
	public boolean generateSnowVillages = true;
	public int generateSnowVillageChance = 100;
	public int[] dragonBlacklistedDimensions = new int[]{1, -1};
	public int[] dragonWhitelistedDimensions = new int[]{0};
	public boolean useDimensionBlackList = true;
	public int[] snowVillageBlacklistedDimensions = new int[]{1, -1};
	public int[] snowVillageWhitelistedDimensions = new int[]{0};
	public boolean spawnGlaciers = true;
	public int glacierSpawnChance = 4;
	public int oreToStoneRatioForDragonCaves = 45;
	public int dragonGriefing = 0;
	public int dragonFlapNoiseDistance = 4;
	public int dragonFluteDistance = 8;
	public int dragonHealth = 500;
	public int dragonAttackDamage = 17;
	public int maxDragonFlight = 128;
	public int dragonGoldSearchLength = 17;
	public boolean canDragonsDespawn = true;
	public int dragonTargetSearchLength = 64;
	public boolean spawnHippogryphs = true;
	public int hippogryphSpawnRate = 5;
	public boolean spawnGorgons = true;
	public int spawnGorgonsChance = 80;
	public double gorgonMaxHealth = 100D;
	public boolean spawnPixies = true;
	public int spawnPixiesChance = 60;
	public int pixieVillageSize = 5;
	public boolean generateCyclopsCaves = true;
	public int spawnCyclopsChance = 80;
	public int cyclopesSheepSearchLength = 17;
	public double cyclopsMaxHealth = 150;
	public double cyclopsAttackStrength = 15;
	public double cyclopsBiteStrength = 40;
	public double sirenMaxHealth = 100D;
	public boolean generateSirenIslands = true;
	public int generateSirenChance = 300;
	public boolean spawnHippocampus = true;
	public int hippocampusSpawnChance = 30;

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
	@ConfigEntry(category = "death worms", comment = "Death worm spawn weight. Lower number = lower chance to spawn.")
	public int deathWormSpawnRate = 3;
	@SuppressWarnings("deprecation")
	@ConfigEntry(category = "death worms", comment = "A double check to see if the game can spawn death worms. Higher number = lower chance to spawn.")
	public int deathWormSpawnCheckChance = 3;

	@ConfigEntry(category = "cockatrices", comment = "How far away cockatrices will detect chickens being hurt.")
	public int cockatriceChickenSearchLength = 40;
	@ConfigEntry(category = "cockatrices", comment = "1 out of this number chance per tick for a chicken to lay a cockatrice egg.")
	public int cockatriceEggChance = 4320000;
	@SuppressWarnings("deprecation")
	@ConfigEntry(category = "cockatrices", comment = "True if cockatrices are allowed to spawn.")
	public boolean spawnCockatrices= true;
	@ConfigEntry(category = "cockatrices", comment = "Death worm spawn weight. Lower number = lower chance to spawn.")
	public int cockatriceSpawnRate = 4;
	@ConfigEntry(category = "cockatrices", comment = "A double check to see if the game can spawn cockatrices. Higher number = lower chance to spawn.")
	public int cockatriceSpawnCheckChance = 0;

	@ConfigEntry(category = "stymphalian birds", comment = "How many blocks away can stymphalian birds spot potential prey. Note that increasing this could cause lag.")
	public int stymphalianBirdTargetSearchLength = 64;
	@ConfigEntry(category = "stymphalian birds", comment = "Strength of damage from each feather fired by a stymphalian bird(default is half a heart)")
	public float stymphalianBirdFeatherAttackStength = 1F;
	@ConfigEntry(category = "stymphalian birds", comment = "How far away stymphalian birds will consider other birds to be in the same flock.")
	public int stymphalianBirdFlockLength = 40;
	@ConfigEntry(category = "stymphalian birds", comment = "How high stymphalian birds can fly, in Y height.")
	public int stymphalianBirdFlightHeight = 80;
	@ConfigEntry(category = "stymphalian birds", comment = "True if stymphalian birds are allowed to spawn.")
	public boolean spawnStymphalianBirds = true;
	@ConfigEntry(category = "stymphalian birds", comment = "1 out of this number chance for hippocampi to spawn.")
	public int stymphalianBirdSpawnChance = 50;

	@SuppressWarnings("deprecation")
	@ConfigEntry(category = "trolls", comment = "True if trolls are allowed to spawn.")
	public boolean spawnTrolls = true;
	@SuppressWarnings("deprecation")
	@ConfigEntry(category = "trolls", comment = "Troll spawn weight. Lower number = lower chance to spawn.")
	public int trollSpawnRate = 10;
	@ConfigEntry(category = "trolls", comment = "A double check to see if the game can spawn trolls. Higher number = lower chance to spawn.")
	public int trollSpawnCheckChance = 1;

	@ConfigEntry(category = "trolls", comment = "Maximum troll health")
	public double trollMaxHealth = 50;
	@ConfigEntry(category = "trolls", comment = "How much damage trolls cause with their kick and stomp attacks.")
	public double trollAttackStrength = 10;

    public void init(Configuration config) {
    	this.customMainMenu = config.getBoolean("Custom main menu", "clientside", true, "Whether to display the dragon on the main menu or not");
		this.generateSilverOre  = config.getBoolean("Generate Silver Ore", "generation", true, "Whether to generate silver ore or not");
		this.generateSapphireOre  = config.getBoolean("Generate Sapphire Ore", "generation", true, "Whether to generate sapphire ore or not");
		this.generateDragonSkeletons  = config.getBoolean("Generate Dragon Skeletons", "generation", true, "Whether to generate dragon skeletons or not");
		this.generateDragonSkeletonChance  = config.getInt("Generate Dragon Skeleton Chance", "generation", 130, 1, 10000, "1 out of this number chance per chunk for generation");
		this.generateDragonDens  = config.getBoolean("Generate Dragon Caves", "generation", true, "Whether to generate dragon caves or not");
		this.generateDragonDenChance  = config.getInt("Generate Dragon Cave Chance", "generation", 180, 1, 10000, "1 out of this number chance per chunk for generation");
		this.generateDragonRoosts  = config.getBoolean("Generate Dragon Roosts", "generation", true, "Whether to generate dragon roosts or not");
		this.generateDragonRoostChance  = config.getInt("Generate Dragon Roost Chance", "generation", 360, 1, 10000, "1 out of this number chance per chunk for generation");
		this.dragonDenGoldAmount  = config.getInt("Dragon Den Gold Amount", "generation", 4, 1, 10000, "1 out of this number chance per block that gold will generate in dragon lairs.");
		this.generateSnowVillages  = config.getBoolean("Generate Snow Villages", "generation", true, "Whether to generate snow villages or not");
		this.generateSnowVillageChance   = config.getInt("Generate Snow Village Chance", "generation", 100, 1, 10000, "1 out of this number chance per chunk for generation");
		this.generateSnowVillages  = config.getBoolean("Generate Snow Villages", "generation", true, "Whether to generate snow villages or not");
		this.dragonBlacklistedDimensions = config.get("generation", "Blacklisted Dragon Dimensions", new int[]{-1, 1}, "Dragons cannot spawn in these dimensions' IDs").getIntList();
		this.dragonWhitelistedDimensions = config.get("generation", "Whitelisted Dragon Dimensions", new int[]{0}, "Dragons can only spawn in these dimensions' IDs").getIntList();
		this.useDimensionBlackList = config.getBoolean("use Dimension Blacklist", "generation", true, "true to use dimensional blacklist, false to use the whitelist.");
		this.snowVillageBlacklistedDimensions = config.get("generation", "Blacklisted Snow Village Dimensions", new int[]{-1, 1}, "Snow Villages cannot spawn in these dimensions' IDs").getIntList();
		this.snowVillageWhitelistedDimensions = config.get("generation", "Whitelisted Snow Village Dimensions", new int[]{0}, "Snow Villages can only spawn in these dimensions' IDs").getIntList();
		this.spawnGlaciers = config.getBoolean("Generate Glaciers", "generation", true, "Whether to generate glacier biomes or not");
		this.glacierSpawnChance = config.getInt("Glacier Spawn Weight", "generation", 4, 1, 10000, "Glacier Spawn Weight. Higher number = more common");
		this.oreToStoneRatioForDragonCaves = config.getInt("Dragon Cave Ore Ratio", "generation", 45, 1, 10000, "Ratio of Stone(this number) to Ores in Dragon Caves");

		this.dragonGriefing = config.getInt("Dragon Griefing", "dragons", 0, 0, 2, "Dragon griefing - 2 is no griefing, 1 is breaking weak blocks, 0 is default");
		this.dragonFlapNoiseDistance = config.getInt("Dragon Flap Noise Distance", "dragons", 4, 0, 10000, "Dragon Flap Noise Distance - Larger number, further away you can hear it");
		this.dragonFluteDistance = config.getInt("Dragon Flute Distance", "dragons", 4, 0, 10000, "Dragon Flute Distance - how many chunks away is the dragon flute effective?");
		this.dragonHealth = config.getInt("Dragon Health", "dragons", 500, 1, 100000, "Max dragon health. Health is scaled to this");
		this.dragonAttackDamage = config.getInt("Dragon Attack Damage", "dragons", 17, 1, 10000, "Max dragon attack damage. Attack Damage is scaled to this");
		this.maxDragonFlight = config.getInt("Max Dragon Flight Height", "dragons", 128, 10, 1000, "How high dragons can fly, in Y height.");
		this.dragonGoldSearchLength = config.getInt("Dragon Gold Search Length", "dragons", 17, 0, 10000, "How far away dragons will detect gold blocks being destroyed or chests being opened");
		this.canDragonsDespawn = config.getBoolean("Dragons Despawn", "dragons", true, "True if dragons can despawn. Note that if this is false there may be SERIOUS lag issues.");
		this.dragonTargetSearchLength = config.getInt("Dragon Target Search Length", "dragons", 64, 1, 10000, "How many blocks away can dragons spot potential prey. Note that increasing this could cause lag.");

		this.spawnHippogryphs = config.getBoolean("Spawn Hippogryphs", "hippogryphs", true, "True if hippogryphs are allowed to spawn");
		this.hippogryphSpawnRate = config.getInt("Hippogryph Spawn Weight", "hippogryphs", 5, 1, 10000, "Hippogryph spawn weight. Lower = lower chance to spawn.");

		this.spawnGorgons = config.getBoolean("Spawn Gorgons", "gorgons", true, "True if gorgon temples are allowed to spawn");
		this.spawnGorgonsChance = config.getInt("Spawn Gorgon Chance", "gorgons", 80, 1, 10000, "1 out of this number chance per chunk for generation");
		this.gorgonMaxHealth = (double)config.getFloat("Gorgon Max Health", "gorgons", 100, 1, 10000, "Maximum gorgon health");

		this.spawnPixies = config.getBoolean("Spawn Pixies", "pixies", true, "True if pixie villages are allowed to spawn");
		this.spawnPixiesChance = config.getInt("Spawn Pixies Chance", "pixies", 60, 1, 10000, "1 out of this number chance per chunk for generation");
		this.pixieVillageSize = config.getInt("Pixie Village Size", "pixies", 5, 1, 10000, "size of pixie villages");

		this.generateCyclopsCaves = config.getBoolean("Spawn Cyclopes", "cyclopes", true, "True if cyclops caves are allowed to spawn");
		this.spawnCyclopsChance = config.getInt("Spawn Cyclops Chance", "cyclopes", 80, 1, 10000, "1 out of this number chance per chunk for generation");
		this.cyclopsMaxHealth = (double)config.getFloat("Cyclops Max Health", "cyclopes", 150, 1, 10000, "Maximum cyclops health");
		this.cyclopesSheepSearchLength = config.getInt("Cyclopes Sheep Search Length", "dragons", 17, 1, 10000, "How many blocks away can cyclopes detect sheep. Note that increasing this could cause lag.");
		this.cyclopsAttackStrength = (double)config.getFloat("Cyclops Attack Strength", "cyclopes", 15, 1, 10000, "Cyclops attack strength");
		this.cyclopsBiteStrength = (double)config.getFloat("Cyclops Bite Strength", "cyclopes", 40, 1, 10000, "Amount of damage done with cyclops bite attack.");

		this.sirenMaxHealth = (double)config.getFloat("Siren Max Health", "sirens", 100, 1, 10000, "Maximum siren health");
		this.generateSirenIslands = config.getBoolean("Spawn Sirens", "sirens", true, "True if siren islands are allowed to spawn");
		this.generateSirenChance = config.getInt("Spawn Sirens Chance", "sirens", 300, 1, 10000, "1 out of this number chance per chunk for generation");

		this.spawnHippocampus = config.getBoolean("Spawn Hippocampus", "hippocampi", true, "True if hippocampi are allowed to spawn");
		this.hippocampusSpawnChance = config.getInt("Spawn Hippocampus Chance", "hippocampi", 30, 1, 10000, "1 out of this number chance per chunk for generation");

    }

    //@SuppressWarnings("deprecation")
	//@ConfigEntry(category = "compatability", comment = "Turn this on if you use the aether mod and want dragons to spawn in that dimension")
	//public boolean useAetherCompat = false;
	//@SuppressWarnings("deprecation")
	//@ConfigEntry(category = "compatability", comment = "Aether Dimension ID - Ice Dragons and Fire Dragons will spawn here if option is used")
	//public int aetherDimensionID = 3;

}
