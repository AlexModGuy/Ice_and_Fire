package com.github.alexthe666.iceandfire.enums;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;

public enum EnumHippogryphTypes {
	BLACK(false, BiomeDictionary.Type.DRY, BiomeDictionary.Type.HOT, BiomeDictionary.Type.SANDY),
	BROWN(false, BiomeDictionary.Type.MOUNTAIN),
	GRAY(false, BiomeDictionary.Type.SPOOKY, BiomeDictionary.Type.DENSE, BiomeDictionary.Type.FOREST),
	CHESTNUT(false, BiomeDictionary.Type.FOREST),
	CREAMY(false, BiomeDictionary.Type.SAVANNA),
	DARK_BROWN(false, BiomeDictionary.Type.CONIFEROUS),
	WHITE(false, BiomeDictionary.Type.SNOWY),
	RAPTOR(true),
	ALEX(true),
	DODO(true);

	public boolean developer;
	public BiomeDictionary.Type[] spawnBiomes;

	private EnumHippogryphTypes(boolean developer, BiomeDictionary.Type... biomes) {
		this.spawnBiomes = biomes;
		this.developer = developer;
	}

	public static EnumHippogryphTypes getRandomType() {
		return EnumHippogryphTypes.values()[new Random().nextInt(EnumHippogryphTypes.values().length - 3)];
	}

	public static EnumHippogryphTypes getBiomeType(Biome biome) {
		List<EnumHippogryphTypes> types = new ArrayList<EnumHippogryphTypes>();
		for (EnumHippogryphTypes type : EnumHippogryphTypes.values()) {
			for(BiomeDictionary.Type biomeTypes : type.spawnBiomes) {
				if(BiomeDictionary.hasType(biome, biomeTypes)){
					types.add(type);
				}
			}
		}
		if (types.isEmpty()) {
			return getRandomType();
		} else {
			if(types.contains(GRAY) && types.contains(CHESTNUT)){
				return GRAY;
			}
			return types.get(new Random().nextInt(types.size()));
		}

	}
}
