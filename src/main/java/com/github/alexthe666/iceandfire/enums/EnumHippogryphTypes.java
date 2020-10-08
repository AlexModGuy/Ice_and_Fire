package com.github.alexthe666.iceandfire.enums;

import com.github.alexthe666.iceandfire.config.BiomeConfig;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public enum EnumHippogryphTypes {
    BLACK(false),
    BROWN(false),
    GRAY(false),
    CHESTNUT(false),
    CREAMY(false),
    DARK_BROWN(false),
    WHITE(false),
    RAPTOR(true),
    ALEX(true),
    DODO(true);

    public boolean developer;
    public ResourceLocation TEXTURE;
    public ResourceLocation TEXTURE_BLINK;

    EnumHippogryphTypes(boolean developer) {
        this.developer = developer;
        this.TEXTURE = new ResourceLocation("iceandfire:textures/models/hippogryph/" + name().toLowerCase() + ".png");
        this.TEXTURE_BLINK = new ResourceLocation("iceandfire:textures/models/hippogryph/" + name().toLowerCase() + "_blink.png");

    }

    public static EnumHippogryphTypes[] getWildTypes() {
        return new EnumHippogryphTypes[]{BLACK, BROWN, GRAY, CHESTNUT, CREAMY, DARK_BROWN, WHITE};
    }

    public static EnumHippogryphTypes getRandomType() {
        return getWildTypes()[new Random().nextInt(getWildTypes().length - 1)];
    }

    public static EnumHippogryphTypes getBiomeType(Biome biome) {
        List<EnumHippogryphTypes> types = new ArrayList<EnumHippogryphTypes>();
        if (BiomeConfig.blackHippogryphBiomes.contains(biome.getRegistryName().toString())) {
            types.add(BLACK);
        }
        if (BiomeConfig.brownHippogryphBiomes.contains(biome.getRegistryName().toString())) {
            types.add(BROWN);
        }
        if (BiomeConfig.grayHippogryphBiomes.contains(biome.getRegistryName().toString())) {
            types.add(BROWN);
        }
        if (BiomeConfig.chestnutHippogryphBiomes.contains(biome.getRegistryName().toString())) {
            types.add(CHESTNUT);
        }
        if (BiomeConfig.creamyHippogryphBiomes.contains(biome.getRegistryName().toString())) {
            types.add(CREAMY);
        }
        if (BiomeConfig.darkBrownHippogryphBiomes.contains(biome.getRegistryName().toString())) {
            types.add(DARK_BROWN);
        }
        if (BiomeConfig.whiteHippogryphBiomes.contains(biome.getRegistryName().toString())) {
            types.add(WHITE);
        }
        if (types.isEmpty()) {
            return getRandomType();
        } else {
            if (types.contains(GRAY) && types.contains(CHESTNUT)) {
                return GRAY;
            }
            return types.get(new Random().nextInt(types.size()));
        }

    }
}
