package com.github.alexthe666.iceandfire.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.github.alexthe666.iceandfire.IceAndFire;

import net.minecraft.world.biome.Biome;

public enum EnumHippogryphTypes {
	BLACK(IceAndFire.CONFIG.hippogryphBlackBiomes, false),
	BROWN(IceAndFire.CONFIG.hippogryphBrownBiomes, false),
	CHESTNUT(IceAndFire.CONFIG.hippogryphChestnutBiomes, false),
	CREAMY(IceAndFire.CONFIG.hippogryphCreamyBiomes, false),
	DARK_BROWN(IceAndFire.CONFIG.hippogryphDarkBrownBiomes, false),
	GRAY(IceAndFire.CONFIG.hippogryphGrayBiomes, false),
	WHITE(IceAndFire.CONFIG.hippogryphWhiteBiomes, false),
	RAPTOR(new int[0], true),
	ALEX(new int[0], true),
	DODO(new int[0], true);

	public boolean developer;
	public int[] spawnBiomes;

	private EnumHippogryphTypes(int[] spawnBiomes, boolean developer) {
		this.spawnBiomes = Arrays.copyOf(spawnBiomes, spawnBiomes.length);
		this.developer = developer;
	}

	public static EnumHippogryphTypes getRandomType() {
		return EnumHippogryphTypes.values()[new Random().nextInt(EnumHippogryphTypes.values().length - 3)];
	}

	public static EnumHippogryphTypes getBiomeType(Biome biome) {
		List<EnumHippogryphTypes> types = new ArrayList<EnumHippogryphTypes>();
		for (EnumHippogryphTypes type : EnumHippogryphTypes.values()) {
			for (int i = 0; i < type.spawnBiomes.length; i++) {
				if (type.spawnBiomes[i] == Biome.getIdForBiome(biome)) {
					types.add(type);
				}
			}
		}
		if (types.isEmpty()) {
			return getRandomType();
		} else {
			return types.get(new Random().nextInt(types.size()));
		}

	}
}
