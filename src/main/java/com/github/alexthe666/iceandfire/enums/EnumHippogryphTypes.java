package com.github.alexthe666.iceandfire.enums;

import com.github.alexthe666.iceandfire.config.BiomeConfig;
import com.github.alexthe666.iceandfire.util.IAFBiomeUtil;
import com.github.alexthe666.iceandfire.world.IafWorldRegistry;

import net.minecraft.util.RegistryKey;
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
        if (IAFBiomeUtil.biomeMeetsListConditions(biome, BiomeConfig.blackHippogryphBiomes)) {
            types.add(BLACK);
        }
        if (IAFBiomeUtil.biomeMeetsListConditions(biome, BiomeConfig.brownHippogryphBiomes)) {
            types.add(BROWN);
        }
        if (IAFBiomeUtil.biomeMeetsListConditions(biome, BiomeConfig.grayHippogryphBiomes)) {
            types.add(BROWN);
        }
        if (IAFBiomeUtil.biomeMeetsListConditions(biome, BiomeConfig.chestnutHippogryphBiomes)) {
            types.add(CHESTNUT);
        }
        if (IAFBiomeUtil.biomeMeetsListConditions(biome, BiomeConfig.creamyHippogryphBiomes)) {
            types.add(CREAMY);
        }
        if (IAFBiomeUtil.biomeMeetsListConditions(biome, BiomeConfig.darkBrownHippogryphBiomes)) {
            types.add(DARK_BROWN);
        }
        if (IAFBiomeUtil.biomeMeetsListConditions(biome, BiomeConfig.whiteHippogryphBiomes)) {
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
