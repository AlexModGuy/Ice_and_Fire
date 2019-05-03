package com.github.alexthe666.iceandfire;

import net.minecraftforge.common.config.Configuration;

public class IceAndFireConfig {

	public boolean customMainMenu = true;
	public boolean useVanillaFont = false;
	public boolean logCascadingWorldGen = false;
	public boolean generateSilverOre = true;
	public boolean generateSapphireOre = true;
	public boolean generateDragonSkeletons = true;
	public int generateDragonSkeletonChance = 300;
	public boolean generateDragonDens = true;
	public int generateDragonDenChance = 180;
	public boolean generateDragonRoosts = true;
	public int generateDragonRoostChance = 360;
	public int dragonDenGoldAmount = 4;
	public boolean generateSnowVillages = true;
	public int generateSnowVillageChance = 100;
	public int dangerousWorldGenDistanceLimit = 200;
	public int[] dragonBlacklistedDimensions = new int[]{1, -1};
	public int[] dragonWhitelistedDimensions = new int[]{0};
	public boolean useDimensionBlackList = true;
	public int[] snowVillageBlacklistedDimensions = new int[]{1, -1};
	public int[] snowVillageWhitelistedDimensions = new int[]{0};
	public boolean spawnGlaciers = true;
	public int glacierSpawnChance = 4;
	public int oreToStoneRatioForDragonCaves = 45;
	public int dragonEggTime = 7200;
	public int dragonGriefing = 0;
	public boolean tamedDragonGriefing = true;
	public int dragonFlapNoiseDistance = 4;
	public int dragonFluteDistance = 8;
	public int dragonHealth = 500;
	public int dragonAttackDamage = 17;
	public int maxDragonFlight = 128;
	public int dragonGoldSearchLength = 17;
	public boolean canDragonsDespawn = true;
	public boolean dragonDigWhenStuck = true;
	public boolean dragonDropSkull = true;
	public boolean dragonDropHeart = true;
	public boolean dragonDropBlood = true;
	public int dragonTargetSearchLength = 64;
	public int dragonWanderFromHomeDistance = 40;
	public int dragonHungerTickRate = 3000;
	public boolean spawnHippogryphs = true;
	public int hippogryphSpawnRate = 2;
	public boolean spawnGorgons = true;
	public int spawnGorgonsChance = 75;
	public double gorgonMaxHealth = 100D;
	public boolean spawnPixies = true;
	public int spawnPixiesChance = 60;
	public int pixieVillageSize = 5;
	public boolean pixiesStealItems = true;
	public boolean generateCyclopsCaves = true;
	public int spawnCyclopsChance = 170;
	public int cyclopesSheepSearchLength = 17;
	public double cyclopsMaxHealth = 150;
	public double cyclopsAttackStrength = 15;
	public double cyclopsBiteStrength = 40;
	public boolean cyclopsGriefing = true;
	public double sirenMaxHealth = 50D;
	public boolean generateSirenIslands = true;
	public boolean sirenShader = true;
	public int sirenMaxSingTime = 12000;
	public int sirenTimeBetweenSongs = 2000;
	public int generateSirenChance = 300;
	public boolean spawnHippocampus = true;
	public int hippocampusSpawnChance = 70;
	public int deathWormTargetSearchLength = 64;
	public double deathWormMaxHealth = 10D;
	public double deathWormAttackStrength = 3D;
	public boolean spawnDeathWorm = true;
	public boolean deathWormAttackMonsters = true;
	public int deathWormSpawnRate = 2;
	public int deathWormSpawnCheckChance = 3;
	public int cockatriceChickenSearchLength = 32;
	public int cockatriceEggChance = 30;
	public boolean chickensLayRottenEggs = true;
	public boolean spawnCockatrices= true;
	public int cockatriceSpawnRate = 4;
	public int cockatriceSpawnCheckChance = 0;
	public int stymphalianBirdTargetSearchLength = 64;
	public int stymphalianBirdFeatherDropChance = 25;
	public float stymphalianBirdFeatherAttackStength = 1F;
	public int stymphalianBirdFlockLength = 40;
	public int stymphalianBirdFlightHeight = 80;
	public boolean spawnStymphalianBirds = true;
	public boolean stymphalianBirdsOreDictDrops = true;
	public boolean stympahlianBirdAttackAnimals = false;
	public int stymphalianBirdSpawnChance = 100;
	public boolean spawnTrolls = true;
	public int trollSpawnRate = 20;
	public int trollSpawnCheckChance = 1;
	public boolean trollsDropWeapon = true;
	public double trollMaxHealth = 50;
	public double trollAttackStrength = 10;
	public boolean villagersFearDragons = true;
	public boolean animalsFearDragons = true;
	public boolean generateMyrmexColonies = true;
	public int myrmexPregnantTicks = 2500;
	public int myrmexEggTicks = 3000;
	public int myrmexLarvaTicks = 35000;
	public int myrmexColonyGenChance = 150;
	public int myrmexColonySize = 80;
	public boolean experimentalPathFinder;

	public boolean spawnAmphitheres = true;
	public int amphithereSpawnRate = 10;
	public float amphithereVillagerSearchLength = 64;
	public int amphithereTameTime = 400;
	public float amphithereFlightSpeed = 1.75F;
	public double amphithereMaxHealth = 50D;
	public double amphithereAttackStrength = 7D;

	public boolean spawnSeaSerpents = true;
	public int seaSerpentSpawnChance = 200;
	public boolean seaSerpentGriefing = true;
	public double seaSerpentBaseHealth = 20D;
	public double seaSerpentAttackStrength = 4D;

    public void init(Configuration config) {
		this.customMainMenu = config.getBoolean("Custom main menu", "all", true, "Whether to display the dragon on the main menu or not");
		this.useVanillaFont = config.getBoolean("Use Vanilla Font", "all", false, "Whether to use the vanilla font in the bestiary or not");
		this.generateSilverOre  = config.getBoolean("Generate Silver Ore", "all", true, "Whether to generate silver ore or not");
		this.logCascadingWorldGen  = config.getBoolean("Log Cascading World Gen", "all", false, "Whether to log cascading world gen lag. We hope to fix all cascading lag in the future, but the server console spam is over the top.");
		this.generateSapphireOre  = config.getBoolean("Generate Sapphire Ore", "all", true, "Whether to generate sapphire ore or not");
		this.generateDragonSkeletons  = config.getBoolean("Generate Dragon Skeletons", "all", true, "Whether to generate dragon skeletons or not");
		this.generateDragonSkeletonChance  = config.getInt("Generate Dragon Skeleton Chance", "all", 300, 1, 10000, "1 out of this number chance per chunk for generation");
		this.generateDragonDens  = config.getBoolean("Generate Dragon Caves", "all", true, "Whether to generate dragon caves or not");
		this.generateDragonDenChance  = config.getInt("Generate Dragon Cave Chance", "all", 180, 1, 10000, "1 out of this number chance per chunk for generation");
		this.generateDragonRoosts  = config.getBoolean("Generate Dragon Roosts", "all", true, "Whether to generate dragon roosts or not");
		this.generateDragonRoostChance  = config.getInt("Generate Dragon Roost Chance", "all", 360, 1, 10000, "1 out of this number chance per chunk for generation");
		this.dragonDenGoldAmount  = config.getInt("Dragon Den Gold Amount", "all", 4, 1, 10000, "1 out of this number chance per block that gold will generate in dragon lairs.");
		this.generateSnowVillages  = config.getBoolean("Generate Snow Villages", "all", true, "Whether to generate snow villages or not");
		this.generateSnowVillageChance   = config.getInt("Generate Snow Village Chance", "all", 100, 1, 10000, "1 out of this number chance per chunk for generation");
		this.generateSnowVillages  = config.getBoolean("Generate Snow Villages", "all", true, "Whether to generate snow villages or not");
		this.dragonBlacklistedDimensions = config.get("all", "Blacklisted Dragon Dimensions", new int[]{-1, 1}, "Dragons cannot spawn in these dimensions' IDs").getIntList();
		this.dragonWhitelistedDimensions = config.get("all", "Whitelisted Dragon Dimensions", new int[]{0}, "Dragons can only spawn in these dimensions' IDs").getIntList();
		this.useDimensionBlackList = config.getBoolean("use Dimension Blacklist", "all", true, "true to use dimensional blacklist, false to use the whitelist.");
		this.snowVillageBlacklistedDimensions = config.get("all", "Blacklisted Snow Village Dimensions", new int[]{-1, 1}, "Snow Villages cannot spawn in these dimensions' IDs").getIntList();
		this.snowVillageWhitelistedDimensions = config.get("all", "Whitelisted Snow Village Dimensions", new int[]{0}, "Snow Villages can only spawn in these dimensions' IDs").getIntList();
		this.spawnGlaciers = config.getBoolean("Generate Glaciers", "all", true, "Whether to generate glacier biomes or not");
		this.glacierSpawnChance = config.getInt("Glacier Spawn Weight", "all", 4, 1, 10000, "Glacier Spawn Weight. Higher number = more common");
		this.oreToStoneRatioForDragonCaves = config.getInt("Dragon Cave Ore Ratio", "all", 45, 1, 10000, "Ratio of Stone(this number) to Ores in Dragon Caves");
		this.dangerousWorldGenDistanceLimit = config.getInt("Dangerous World Gen Distance From Spawn", "all", 200, 0, Integer.MAX_VALUE, "How many blocks away does dangerous(dragons, cyclops, etc.) world gen have to generate from spawn");

		this.dragonEggTime = config.getInt("Dragon Egg Hatch Time", "all", 7200, 1, Integer.MAX_VALUE, "How long it takes(in ticks) for a dragon egg to hatch");
		this.dragonEggTime = 1;
		this.dragonGriefing = config.getInt("Dragon Griefing", "all", 0, 0, 2, "Dragon griefing - 2 is no griefing, 1 is breaking weak blocks, 0 is default");
		this.tamedDragonGriefing = config.getBoolean("Tamed Dragon Griefing", "all", true, "True if tamed dragons can follow the griefing rules.");
		this.dragonFlapNoiseDistance = config.getInt("Dragon Flap Noise Distance", "all", 4, 0, 10000, "Dragon Flap Noise Distance - Larger number, further away you can hear it");
		this.dragonFluteDistance = config.getInt("Dragon Flute Distance", "all", 4, 0, 10000, "Dragon Flute Distance - how many chunks away is the dragon flute effective?");
		this.dragonHealth = config.getInt("Dragon Health", "all", 500, 1, 100000, "Max dragon health. Health is scaled to this");
		this.dragonAttackDamage = config.getInt("Dragon Attack Damage", "all", 17, 1, 10000, "Max dragon attack damage. Attack Damage is scaled to this");
		this.maxDragonFlight = config.getInt("Max Dragon Flight Height", "all", 128, 10, 1000, "How high dragons can fly, in Y height.");
		this.dragonGoldSearchLength = config.getInt("Dragon Gold Search Length", "all", 17, 0, 10000, "How far away dragons will detect gold blocks being destroyed or chests being opened");
		this.canDragonsDespawn = config.getBoolean("Dragons Despawn", "all", true, "True if dragons can despawn. Note that if this is false there may be SERIOUS lag issues.");
		this.dragonDigWhenStuck = config.getBoolean("Dragons Dig When Stuck", "all", true, "True if dragons can break blocks if they get stuck. Turn this off if your dragons randomly explode.");
		this.dragonDropSkull = config.getBoolean("Dragons Drop Skull", "all", true, "True if dragons can drop their skull on death.");
		this.dragonDropHeart = config.getBoolean("Dragons Drop Heart", "all", true, "True if dragons can drop their heart on death.");
		this.dragonDropBlood = config.getBoolean("Dragons Drop Blood", "all", true, "True if dragons can drop their blood on death.");
		this.dragonTargetSearchLength = config.getInt("Dragon Target Search Length", "all", 64, 1, 10000, "How many blocks away can dragons spot potential prey. Note that increasing this could cause lag.");
		this.dragonWanderFromHomeDistance = config.getInt("Dragon Wander From Home Distance", "all", 40, 1, 10000, "How many blocks away can dragons wander from their defined \"home\" position.");
		this.dragonHungerTickRate = config.getInt("Dragon Hunger Tick Rate", "all", 3000, 1, 10000, "Every interval of this number in ticks, dragon hunger decreases.");
		this.villagersFearDragons = config.getBoolean("Villagers Fear Dragons", "all", true, "True if villagers should run away and hide from dragons and other hostile Ice and Fire mobs.");
		this.animalsFearDragons = config.getBoolean("Animals Fear Dragons", "all", true, "True if animals should run away and hide from dragons and other hostile Ice and Fire mobs.");
		this.spawnHippogryphs = config.getBoolean("Spawn Hippogryphs", "all", true, "True if hippogryphs are allowed to spawn");
		this.hippogryphSpawnRate = config.getInt("Hippogryph Spawn Weight", "all", 2, 1, 10000, "Hippogryph spawn weight. Lower = lower chance to spawn.");

		this.spawnGorgons = config.getBoolean("Spawn Gorgons", "all", true, "True if gorgon temples are allowed to spawn");
		this.spawnGorgonsChance = config.getInt("Spawn Gorgon Chance", "all", 75, 1, 10000, "1 out of this number chance per chunk for generation");
		this.gorgonMaxHealth = (double)config.getFloat("Gorgon Max Health", "all", 100, 1, 10000, "Maximum gorgon health");

		this.spawnPixies = config.getBoolean("Spawn Pixies", "all", true, "True if pixie villages are allowed to spawn");
		this.spawnPixiesChance = config.getInt("Spawn Pixies Chance", "all", 60, 1, 10000, "1 out of this number chance per chunk for generation");
		this.pixieVillageSize = config.getInt("Pixie Village Size", "all", 5, 1, 10000, "size of pixie villages");
		this.pixiesStealItems = config.getBoolean("Pixies Steal Items", "all", true, "True if pixies are allowed to steal from players");

		this.generateCyclopsCaves = config.getBoolean("Spawn Cyclopes", "all", true, "True if cyclops caves are allowed to spawn");
		this.spawnCyclopsChance = config.getInt("Spawn Cyclops Chance", "all", 170, 1, 10000, "1 out of this number chance per chunk for generation");
		this.cyclopsMaxHealth = (double)config.getFloat("Cyclops Max Health", "all", 150, 1, 10000, "Maximum cyclops health");
		this.cyclopesSheepSearchLength = config.getInt("Cyclopes Sheep Search Length", "all", 17, 1, 10000, "How many blocks away can cyclopes detect sheep. Note that increasing this could cause lag.");
		this.cyclopsAttackStrength = (double)config.getFloat("Cyclops Attack Strength", "all", 15, 1, 10000, "Cyclops attack strength");
		this.cyclopsBiteStrength = (double)config.getFloat("Cyclops Bite Strength", "all", 40, 1, 10000, "Amount of damage done with cyclops bite attack.");
		this.cyclopsGriefing = config.getBoolean("Cyclops Griefing", "all", true, "Whether or not cyclops can break logs or leaves in their way");

		this.sirenMaxHealth = (double)config.getFloat("Siren Max Health", "all", 50, 1, 10000, "Maximum siren health");
		this.generateSirenIslands = config.getBoolean("Spawn Sirens", "all", true, "True if siren islands are allowed to spawn");
		this.sirenShader = config.getBoolean("Use Siren Shader", "all", true, "True to make the screen pink when sirens attract players");
		this.generateSirenChance = config.getInt("Spawn Sirens Chance", "all", 300, 1, 10000, "1 out of this number chance per chunk for generation");
		this.sirenMaxSingTime = config.getInt("Siren Max Sing Time", "all", 12000, 100, 24000, "how long(in ticks) can a siren use its sing effect on a player, without a cooldown.");
		this.sirenTimeBetweenSongs = config.getInt("Siren Time Between Songs", "all", 2000, 100, 24000, "how long(in ticks) a siren has to wait after failing to lure in a player");

		this.spawnHippocampus = config.getBoolean("Spawn Hippocampus", "all", true, "True if hippocampi are allowed to spawn");
		this.hippocampusSpawnChance = config.getInt("Spawn Hippocampus Chance", "all", 70, 1, 10000, "1 out of this number chance per chunk for generation");

		this.deathWormTargetSearchLength = config.getInt("Death Worm Target Search Length", "all", 64, 1, 10000, "How many blocks away can death worms spot potential prey. Note that increasing this could cause lag");
		this.deathWormMaxHealth = (double)config.getFloat("Death Worm Base Health", "all", 10, 1, 10000, "Default deathworm health, this is scaled to the worm's particular size");
		this.deathWormAttackStrength = (double)config.getFloat("Death Worm Base Attack Strength", "all", 3, 1, 10000, "Default deathworm attack strength, this is scaled to the worm's particular size");
		this.spawnDeathWorm = config.getBoolean("Spawn Death Worms", "all", true, "True if deathworms are allowed to spawn");
		this.deathWormAttackMonsters = config.getBoolean("Death Worms Target Monsters", "all", true, "True if wild deathworms are allowed to target and attack monsters");
		this.deathWormSpawnRate = config.getInt("Death Worm Spawn Weight", "all", 2, 1, 10000, "Deathworm spawn weight. Lower = lower chance to spawn");
		this.deathWormSpawnCheckChance = config.getInt("Death Worm Spawn Check Chance", "all", 3, 0, 10000, "A double check to see if the game can spawn death worms. Higher number = lower chance to spawn.");

		this.cockatriceChickenSearchLength = config.getInt("Cockatrice chicken Search Length", "all", 32, 1, 10000, "How many blocks away can cockatrices detect chickens. Note that increasing this could cause lag.");
		this.cockatriceEggChance = config.getInt("Cockatrice chicken Search Length", "all", 30, 1, Integer.MAX_VALUE, "1 out of this number chance per 6000 ticks for a chicken to lay a cockatrice egg.");
		this.chickensLayRottenEggs = config.getBoolean("Chickens Lay Rotten Eggs", "all", true, "True if chickens lay rotten eggs.");
		this.spawnCockatrices = config.getBoolean("Spawn Cockatrices", "all", true, "True if cockatrices are allowed to spawn");
		this.cockatriceSpawnRate = config.getInt("Cockatrice Spawn Weight", "all", 4, 1, 10000, "Cockatrice spawn weight. Lower = lower chance to spawn");
		this.cockatriceSpawnCheckChance = config.getInt("Cockatrice Spawn Check Chance", "all", 0, 0, 10000, "A double check to see if the game can spawn cockatrices. Higher number = lower chance to spawn.");

		this.stymphalianBirdTargetSearchLength = config.getInt("Stymphalian Bird Target Search Length", "all", 64, 1, 10000, "How many blocks away can stymphalian birds spot potential prey. Note that increasing this could cause lag.");
		this.stymphalianBirdFeatherDropChance = config.getInt("Stymphalian Bird Feather Drop Chance", "all", 25, 0, 10000, "1/this number chance for a stymphalian feather to turn into an item before despawning. Zero means never.");
		this.stymphalianBirdFeatherAttackStength = config.getFloat("Stymphalian Bird Feather Attack Strength", "all", 1, 0, 10000, "Stymphalian bird feather attack strength.");
		this.stymphalianBirdFlockLength = config.getInt("Stymphalian Bird Flock Length", "all", 40, 1, 10000, "How far away stymphalian birds will consider other birds to be in the same flock.");
		this.stymphalianBirdFlightHeight = config.getInt("Max Stymphalian Bird Flight Height", "all", 80, 10, 1000, "How high stymphalian birds can fly, in Y height.");
		this.spawnStymphalianBirds = config.getBoolean("Spawn Stymphalian Birds", "all", true, "True if stymphalian birds are allowed to spawn");
		this.stymphalianBirdsOreDictDrops = config.getBoolean("Stymphalian Birds drop ore dict items", "all", true, "True if stymphalian birds can drop items registered in the ore dictionary to ingotCopper, ingotBronze, nuggetCopper, nuggetBronze.");
		this.stympahlianBirdAttackAnimals = config.getBoolean("Stymphalian Birds Target Animals", "all", false, "True if stymphalian birds are allowed to target and attack animals");
		this.stymphalianBirdSpawnChance = config.getInt("Spawn Stymhphalian Bird Chance", "all", 100, 1, 10000, "1 out of this number chance per chunk for generation");

		this.spawnTrolls = config.getBoolean("Spawn Trolls", "all", true, "True if trolls are allowed to spawn");
		this.trollsDropWeapon = config.getBoolean("Trolls Drop Weapon", "all", true, "True if trolls are allowed to drop their weapon on death.");
		this.trollSpawnRate = config.getInt("Troll Spawn Weight", "all", 500, 1, 10000, "Troll spawn weight. Lower = lower chance to spawn");
		this.trollSpawnCheckChance = config.getInt("Troll Spawn Check Chance", "all", 1, 0, 10000, "A double check to see if the game can spawn trolls. Higher number = lower chance to spawn.");
		this.trollMaxHealth = (double)config.getFloat("Troll Max Health", "all", 50, 1, 10000, "Maximum troll health");
		this.trollAttackStrength = (double)config.getFloat("Troll Attack Strength", "all", 10, 1, 10000, "Troll attack strength");

		this.generateMyrmexColonies = config.getBoolean("Spawn Myrmex", "all", true, "True if myrmex colonies are allowed to spawn");
		this.myrmexPregnantTicks = config.getInt("Myrmex Gestation Length", "all", 2500, 1, 10000, "How many ticks it takes for a Myrmex Queen to produce an egg.");
		this.myrmexEggTicks = config.getInt("Myrmex Hatch Length", "all", 3000, 1, 10000, "How many ticks it takes for a Myrmex Egg to hatch.");
		this.myrmexLarvaTicks = config.getInt("Myrmex Hatch Length", "all", 35000, 1, 100000, "How many ticks it takes for a Myrmex to move from a larva to a pupa, and from a pupa to an adult.");
		this.myrmexColonyGenChance = config.getInt("Myrmex Colony Gen Chance", "all", 150, 1, 10000, "One out of this number chance per chunk to generate a myrmex hive.");
		this.myrmexColonySize = config.getInt("Myrmex Colony Max Size", "all", 80, 10, 10000, "How many maximum individuals a myrmex colony can have.");

		this.experimentalPathFinder = config.getBoolean("Experimental Dragon path Finder", "all", false, "Turning this to true simplifies the dragon's pathfinding process, making them dumber when finding a path, but better for servers with many loaded dragons.");

		this.spawnAmphitheres = config.getBoolean("Spawn Amphitheres", "all", true, "True if amphitheres are allowed to spawn");
		this.amphithereSpawnRate = config.getInt("Amphithere Spawn Weight", "all", 10, 1, 10000, "Amphithere spawn weight. Lower = lower chance to spawn");
		this.amphithereVillagerSearchLength = config.getInt("Amphithere Villager Search Length", "all", 64, 1, 10000, "How many blocks away can ampitheres detect villagers being hurt. Note that increasing this could cause lag.");
		this.amphithereTameTime = config.getInt("Amphithere Tame Time", "all", 400, 1, 10000, "How many ticks it takes while riding an untamed amphithere to tame it.");
		this.amphithereFlightSpeed = config.getFloat("Amphithere Flight Speed", "all", 1.75F, 0.0F, 3.0F, "How fast amphitheres fly.");
		this.amphithereMaxHealth = (double)config.getFloat("Amphithere Max Health", "all", 50, 1, 10000, "Maximum amphithere health");
		this.amphithereAttackStrength = (double)config.getFloat("Amphithere Attack Strength", "all", 7, 1, 10000, "Amphithere attack strength");

		this.spawnSeaSerpents = config.getBoolean("Spawn Sea Serpents", "all", true, "True if sea serpents are allowed to spawn");
		this.seaSerpentSpawnChance = config.getInt("Spawn Sea Serpent Chance", "all", 200, 1, 10000, "1 out of this number chance per chunk for generation");
		this.seaSerpentGriefing = config.getBoolean("Sea Serpent Griefing", "all", true, "Whether or not sea serpents can break weak blocks in their way");
		this.seaSerpentBaseHealth = (double)config.getFloat("Sea Serpent Base Health", "all", 20, 1, 10000, "Default sea serpent health, this is scaled to the sea serpent's particular size");
		this.seaSerpentAttackStrength = (double)config.getFloat("Sea Serpent Base Attack Strength", "all", 4, 1, 10000, "Default sea serpent attack strength, this is scaled to the sea serpent's particular size");


	}
}
