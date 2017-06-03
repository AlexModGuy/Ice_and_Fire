package com.github.alexthe666.iceandfire;

import net.ilexiconn.llibrary.server.config.ConfigEntry;

public class IceAndFireConfig {

    @ConfigEntry(category = "generation")
    public boolean generateSilverOre = true;
    @ConfigEntry(category = "generation")
    public boolean generateSapphireOre = true;
    @ConfigEntry(category = "generation", comment = "Generate dragon skeletons in hot biomes?")
    public boolean generateDragonSkeletons = true;
    @ConfigEntry(category = "generation", comment = "1 out of this number chance per chunk for generation")
    public int generateDragonSkeletonChance = 150;
    @ConfigEntry(category = "generation")
    public boolean generateDragonDens = true;
    @ConfigEntry(category = "generation", comment = "1 out of this number chance per chunk for generation")
    public int generateDragonDenChance = 180;
    @ConfigEntry(category = "generation")
    public boolean generateDragonRoosts = true;
    @ConfigEntry(category = "generation", comment = "1 out of this number chance per chunk for generation")
    public int generateDragonRoostChance = 360;
    @ConfigEntry(category = "generation", comment = "1 out of this number chance per block that gold will generate in dragon lairs.")
    public int dragonDenGoldAmount = 1;
    @ConfigEntry(category = "generation")
    public boolean generateSnowVillages = true;
    @ConfigEntry(category = "generation", comment = "1 out of this number chance per chunk for generation")
    public int generateSnowVillageChance = 100;
    @ConfigEntry(category = "generation", comment = "Dragons cannot spawn in these dimensions' IDs")
    public int[] dragonBlacklistedDimensions = new int[]{1, -1};
    @ConfigEntry(category = "generation", comment = "Snow Villages cannot spawn in these dimensions' IDs")
    public int[] snowVillageBlacklistedDimensions = new int[]{1, -1};
    @ConfigEntry(category = "generation", comment = "true to use Blacklist configs, false to use Whitelist configs")
    public boolean useBlackList = true;

    @ConfigEntry(category = "generation", comment = "Dragons can only spawn in these dimensions' IDs")
    public int[] dragonWhitelistedDimensions = new int[]{0};
    @ConfigEntry(category = "generation", comment = "Snow Villages can only spawn in these dimensions' IDs")
    public int[] snowVillageWhitelistedDimensions = new int[]{0};

    @ConfigEntry(category = "generation", comment = "Ratio of Stone(this number) to Ores in Dragon Caves")
    public int oreToStoneRatioForDragonCaves = 5;
    @ConfigEntry(category = "mobs", comment = "Dragon griefing - 2 is no griefing, 1 is breaking weak blocks, 0 is default")
    public int dragonGriefing = 0;
    @ConfigEntry(category = "mobs", comment = "Dragon Flap Noise Distance - Larger number, further away you can hear it")
    public int dragonFlapNoiseDistance = 4;
    @ConfigEntry(category = "mobs", comment = "Dragon Flute Distance - how many chunks away is the dragon flute effective?")
    public int dragonFluteDistance = 4;
    @ConfigEntry(category = "generation", comment = "glacier biome spawn chance - higher the number, higher the rarity")
    public int glacierSpawnChance = 4;
    @ConfigEntry(category = "mobs", comment = "Max dragon health. Health is scaled to this")
    public int dragonHealth = 500;
    @ConfigEntry(category = "mobs", comment = "Max dragon attack damage. Attack Damage is scaled to this")
    public int dragonAttackDamage = 17;
    @ConfigEntry(category = "mobs", comment = "Can dragons suffocate in blocks?")
    public boolean dragonSuffocation = false;
    @ConfigEntry(category = "mobs", comment = "Advanced dragon AI - dragon flight improved but FPS might fall.")
    public boolean dragonAdvancedAI = true;
    @ConfigEntry(category = "mobs", comment = "How far away dragons will detect gold blocks being destroyed or chests being opened.")
    public int dragonGoldSearchLength = 17;

    @ConfigEntry(category = "compatability", comment = "Triggers option below")
    public boolean useAetherCompat = false;
    @ConfigEntry(category = "compatability", comment = "Aether Dimension ID - Ice Dragons and Fire Dragons will spawn here if option is used")
    public int aetherDimensionID = 3;
}
