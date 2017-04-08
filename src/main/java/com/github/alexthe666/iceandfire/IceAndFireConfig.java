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
	@ConfigEntry(category = "generation")
	public boolean generateSnowVillages = true;
	@ConfigEntry(category = "generation", comment = "1 out of this number chance per chunk for generation")
	public int generateSnowVillageChance = 100;
	@ConfigEntry(category = "generation", comment = "Dragons cannot spawn in these dimensions' IDs")
	public int[] dragonBlacklistedDimensions = new int[]{1, -1};
	@ConfigEntry(category = "generation", comment = "Snow Villages cannot spawn in these dimensions' IDs")
	public int[] snowVillageBlacklistedDimensions = new int[]{1, -1};
	@ConfigEntry(category = "generation", comment = "Ratio of Stone(this number) to Ores in Dragon Caves")
	public int oreToStoneRatioForDragonCaves = 5;
	@ConfigEntry(category = "mobs", comment = "Dragon griefing - 2 is no griefing, 1 is breaking weak blocks, 0 is default")
	public int dragonGriefing = 0;
}
