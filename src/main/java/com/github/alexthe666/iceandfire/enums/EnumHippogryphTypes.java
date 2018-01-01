package com.github.alexthe666.iceandfire.enums;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

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

	public static EnumHippogryphTypes[] getWildTypes() {
		return new EnumHippogryphTypes[]{BLACK, BROWN, GRAY, CHESTNUT, CREAMY, DARK_BROWN, WHITE};
	}

	public static EnumHippogryphTypes getRandomType() {
		return getWildTypes()[new Random().nextInt(getWildTypes().length - 1)];
	}

	public static EnumHippogryphTypes getBiomeType(Biome biome) {
		List<EnumHippogryphTypes> types = new ArrayList<EnumHippogryphTypes>();
		for (EnumHippogryphTypes type : getWildTypes()) {
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
