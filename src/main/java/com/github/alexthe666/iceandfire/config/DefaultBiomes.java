package com.github.alexthe666.iceandfire.config;

import com.github.alexthe666.citadel.config.biome.BiomeEntryType;
import com.github.alexthe666.citadel.config.biome.SpawnBiomeData;
import net.minecraftforge.common.Tags;

import static net.minecraft.tags.BiomeTags.*;
import static net.minecraftforge.common.Tags.Biomes.*;

public class DefaultBiomes {

    public static final SpawnBiomeData OVERWORLD = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_OVERWORLD.location().toString(), 0);

    public static final SpawnBiomeData FIREDRAGON_ROOST = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_HOT_OVERWORLD.location().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, true, IS_JUNGLE.location().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, true, IS_BADLANDS.location().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, true, IS_SAVANNA.location().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_OVERWORLD.location().toString(), 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_PLAINS.location().toString(), 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, true, IS_WET_OVERWORLD.location().toString(), 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, true, IS_COLD_OVERWORLD.location().toString(), 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, true, IS_JUNGLE.location().toString(), 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, true, IS_BADLANDS.location().toString(), 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, true, IS_SAVANNA.location().toString(), 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, true, IS_FOREST.location().toString(), 1)
            .addBiomeEntry(BiomeEntryType.REGISTRY_NAME, false, "terralith:steppe", 2)
            .addBiomeEntry(BiomeEntryType.REGISTRY_NAME, false, "byg:rose_fields", 3)
            .addBiomeEntry(BiomeEntryType.REGISTRY_NAME, false, "byg:prairie", 4)
            .addBiomeEntry(BiomeEntryType.REGISTRY_NAME, false, "byg:firecracker_shrubland", 5)
            .addBiomeEntry(BiomeEntryType.REGISTRY_NAME, false, "byg:autumnal_valley", 6)
            .addBiomeEntry(BiomeEntryType.REGISTRY_NAME, false, "byg:lush_stacks", 7);
    public static final SpawnBiomeData FIREDRAGON_CAVE = FIREDRAGON_ROOST;
    public static final SpawnBiomeData ICEDRAGON_ROOST = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_COLD_OVERWORLD.location().toString(), 0)
            .addBiomeEntry(BiomeEntryType.REGISTRY_NAME, false, "byg:shattered_glacier", 1)
            .addBiomeEntry(BiomeEntryType.REGISTRY_NAME, false, "byg:frosted_taiga", 2)
            .addBiomeEntry(BiomeEntryType.REGISTRY_NAME, false, "byg:frosted_coniferous_forest", 3);

    public static final SpawnBiomeData ICEDRAGON_CAVE = ICEDRAGON_ROOST;

    public static final SpawnBiomeData LIGHTNING_ROOST = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_OVERWORLD.location().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_JUNGLE.location().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_OVERWORLD.location().toString(), 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_BADLANDS.location().toString(), 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_OVERWORLD.location().toString(), 2)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_SAVANNA.location().toString(), 2)
            .addBiomeEntry(BiomeEntryType.REGISTRY_NAME, false, "terralith:ashen_savanna", 3)
            .addBiomeEntry(BiomeEntryType.REGISTRY_NAME, false, "terralith:savanna_badlands", 4)
            .addBiomeEntry(BiomeEntryType.REGISTRY_NAME, false, "terralith:savanna_slopes", 5)
            .addBiomeEntry(BiomeEntryType.REGISTRY_NAME, false, "byg:guiana_shield", 6)
            .addBiomeEntry(BiomeEntryType.REGISTRY_NAME, false, "byg:temperate_rainforest", 7)
            .addBiomeEntry(BiomeEntryType.REGISTRY_NAME, false, "byg:tropical_rainforest", 8)
            .addBiomeEntry(BiomeEntryType.REGISTRY_NAME, false, "byg:jacaranda_forest", 8)
            .addBiomeEntry(BiomeEntryType.REGISTRY_NAME, false, "byg:sierra_badlands", 9);

    public static final SpawnBiomeData LIGHTNING_CAVE = LIGHTNING_ROOST;
    public static final SpawnBiomeData VERY_HOT = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_HOT_OVERWORLD.location().toString(), 0)
            .addBiomeEntry(BiomeEntryType.REGISTRY_NAME, false, "byg:firecracker_shrubland", 1);

    public static final SpawnBiomeData VERY_SNOWY = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_OVERWORLD.location().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_SNOWY.location().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, true, IS_WET_OVERWORLD.location().toString(), 0)
            .addBiomeEntry(BiomeEntryType.REGISTRY_NAME, false, "terralith:wintry_forest", 1)
            .addBiomeEntry(BiomeEntryType.REGISTRY_NAME, false, "terralith:wintry_lowlands", 2);

    public static final SpawnBiomeData MAUSOLEUM = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_OVERWORLD.location().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_SNOWY.location().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, true, IS_WET_OVERWORLD.location().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, true, IS_WATER.location().toString(), 0)
            .addBiomeEntry(BiomeEntryType.REGISTRY_NAME, false, "terralith:wintry_forest", 1)
            .addBiomeEntry(BiomeEntryType.REGISTRY_NAME, false, "terralith:wintry_lowlands", 2);

    public static final SpawnBiomeData SNOWY = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_OVERWORLD.location().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_SNOWY.location().toString(), 0);

    public static final SpawnBiomeData VERY_HILLY = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_OVERWORLD.location().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, Tags.Biomes.IS_MOUNTAIN.location().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_OVERWORLD.location().toString(), 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_HILL.location().toString(), 1);

    public static final SpawnBiomeData WOODLAND = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_OVERWORLD.location().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_FOREST.location().toString(), 0);

    public static final SpawnBiomeData SAVANNAS = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_OVERWORLD.location().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_SAVANNA.location().toString(), 0)
            .addBiomeEntry(BiomeEntryType.REGISTRY_NAME, false, "terralith:fractured_savanna", 1)
            .addBiomeEntry(BiomeEntryType.REGISTRY_NAME, false, "terralith:savanna_badlands", 2)
            .addBiomeEntry(BiomeEntryType.REGISTRY_NAME, false, "terralith:savanna_slopes", 3)
            .addBiomeEntry(BiomeEntryType.REGISTRY_NAME, false, "byg:baobab_savanna", 4)
            .addBiomeEntry(BiomeEntryType.REGISTRY_NAME, false, "byg:araucaria_savanna", 5);

    public static final SpawnBiomeData BEACHES = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_OVERWORLD.location().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_BEACH.location().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, true, IS_OCEAN.location().toString(),0)
            .addBiomeEntry(BiomeEntryType.REGISTRY_NAME, false, "byg:dacite_shore", 1)
            .addBiomeEntry(BiomeEntryType.REGISTRY_NAME, false, "byg:basalt_barrera", 2);

    public static final SpawnBiomeData SWAMPS = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_OVERWORLD.location().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_SWAMP.location().toString(), 0)
            .addBiomeEntry(BiomeEntryType.REGISTRY_NAME, false, "terralith:orchid_swamp", 1)
            .addBiomeEntry(BiomeEntryType.REGISTRY_NAME, false, "byg:crag_gardens", 2)
            .addBiomeEntry(BiomeEntryType.REGISTRY_NAME, false, "byg:cypress_swamplands", 3)
            .addBiomeEntry(BiomeEntryType.REGISTRY_NAME, false, "byg:bayou", 4);

    public static final SpawnBiomeData DESERT = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_DRY_OVERWORLD.location().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_HOT_OVERWORLD.location().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_SANDY.location().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, true, IS_BADLANDS.location().toString(), 0)
            .addBiomeEntry(BiomeEntryType.REGISTRY_NAME, false, "terralith:desert_canyon", 1)
            .addBiomeEntry(BiomeEntryType.REGISTRY_NAME, false, "terralith:desert_oasis", 2)
            .addBiomeEntry(BiomeEntryType.REGISTRY_NAME, false, "terralith:desert_spires", 3)
            .addBiomeEntry(BiomeEntryType.REGISTRY_NAME, false, "terralith:ancient_sands", 4)
            .addBiomeEntry(BiomeEntryType.REGISTRY_NAME, false, "byg:mojave_desert", 5)
            .addBiomeEntry(BiomeEntryType.REGISTRY_NAME, false, "byg:atacama_desert", 6)
            .addBiomeEntry(BiomeEntryType.REGISTRY_NAME, false, "byg:windswept_valley", 7);

    public static final SpawnBiomeData OCEANS = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_OVERWORLD.location().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_OCEAN.location().toString(), 0);

    public static final SpawnBiomeData PIXIES = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_OVERWORLD.location().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_RARE.location().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_MAGICAL.location().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_OVERWORLD.location().toString(), 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_DENSE.location().toString(), 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_FOREST.location().toString(), 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, true, Tags.Biomes.IS_MOUNTAIN.location().toString(), 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, true, IS_HILL.location().toString(), 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, true, IS_WET_OVERWORLD.location().toString(), 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, true, IS_HOT.location().toString(), 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, true, IS_COLD.location().toString(), 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, true, IS_DRY.location().toString(), 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, true, IS_TAIGA.location().toString(), 1)
            .addBiomeEntry(BiomeEntryType.REGISTRY_NAME, false, "terralith:siberian_taiga", 2)
            .addBiomeEntry(BiomeEntryType.REGISTRY_NAME, false, "terralith:skylands", 3)
            .addBiomeEntry(BiomeEntryType.REGISTRY_NAME, false, "terralith:mirage_isles", 4)
            .addBiomeEntry(BiomeEntryType.REGISTRY_NAME, false, "byg:amaranth_fields", 5)
            .addBiomeEntry(BiomeEntryType.REGISTRY_NAME, false, "byg:skyris_vale", 6)
            .addBiomeEntry(BiomeEntryType.REGISTRY_NAME, false, "byg:rainbow_beach", 7);

    public static final SpawnBiomeData JUNGLE = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_OVERWORLD.location().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_JUNGLE.location().toString(), 0)
            .addBiomeEntry(BiomeEntryType.REGISTRY_NAME, false, "byg:guiana_shield", 1)
            .addBiomeEntry(BiomeEntryType.REGISTRY_NAME, false, "byg:temperate_rainforest", 2)
            .addBiomeEntry(BiomeEntryType.REGISTRY_NAME, false, "byg:tropical_rainforest", 3)
            .addBiomeEntry(BiomeEntryType.REGISTRY_NAME, false, "byg:jacaranda_forest", 4);

    public static final SpawnBiomeData HILLS = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_OVERWORLD.location().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_BADLANDS.location().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_OVERWORLD.location().toString(), 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, Tags.Biomes.IS_MOUNTAIN.location().toString(), 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_OVERWORLD.location().toString(), 2)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_HILL.location().toString(), 2)
            .addBiomeEntry(BiomeEntryType.REGISTRY_NAME, false, "terralith:rocky_mountains", 3);

    public static final SpawnBiomeData PLAINS = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_OVERWORLD.location().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_PLAINS.location().toString(), 0)
            .addBiomeEntry(BiomeEntryType.REGISTRY_NAME, false, "terralith:steppe", 1)
            .addBiomeEntry(BiomeEntryType.REGISTRY_NAME, false, "byg:firecracker_shrubland", 2)
            .addBiomeEntry(BiomeEntryType.REGISTRY_NAME, false, "byg:autumnal_valley", 3);

    public static final SpawnBiomeData GRAVEYARD = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_OVERWORLD.location().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, true, IS_WATER.location().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, true, IS_BEACH.location().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, true, IS_UNDERGROUND.location().toString(), 0);

    public static final SpawnBiomeData HIPPOGRYPH_BLACK = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.REGISTRY_NAME, false, "minecraft:badlands", 0)
            .addBiomeEntry(BiomeEntryType.REGISTRY_NAME, false, "terralith:painted_mountains", 1);

    public static final SpawnBiomeData HIPPOGRYPH_DARK_BROWN = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_OVERWORLD.location().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_FOREST.location().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, Tags.Biomes.IS_MOUNTAIN.location().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_SNOWY.location().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_OVERWORLD.location().toString(), 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_FOREST.location().toString(), 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_HILL.location().toString(), 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_SNOWY.location().toString(), 1)
            .addBiomeEntry(BiomeEntryType.REGISTRY_NAME, false, "terralith:scarlet_mountains", 2);

    public static final SpawnBiomeData HIPPOGRYPH_WHITE = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_OVERWORLD.location().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_SPARSE.location().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_SNOWY.location().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, Tags.Biomes.IS_MOUNTAIN.location().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_PEAK.location().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_OVERWORLD.location().toString(), 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_SPARSE.location().toString(), 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_SNOWY.location().toString(), 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, Tags.Biomes.IS_MOUNTAIN.location().toString(), 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_SLOPE.location().toString(), 1)
            .addBiomeEntry(BiomeEntryType.REGISTRY_NAME, false, "terralith:rocky_mountains", 2);

    public static final SpawnBiomeData HIPPOGRYPH_GRAY = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.REGISTRY_NAME, false, "minecraft:windswept_forest", 0)
            .addBiomeEntry(BiomeEntryType.REGISTRY_NAME, false, "minecraft:windswept_hills", 1)
            .addBiomeEntry(BiomeEntryType.REGISTRY_NAME, false, "terralith:windswept_spires", 2);

    public static final SpawnBiomeData HIPPOGRYPH_CHESTNUT = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_OVERWORLD.location().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, Tags.Biomes.IS_MOUNTAIN.location().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_BADLANDS.location().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_DRY.location().toString(), 0)
            .addBiomeEntry(BiomeEntryType.REGISTRY_NAME, false, "terralith:savanna_badlands", 1);

    public static final SpawnBiomeData HIPPOGRYPH_CREAMY = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.REGISTRY_NAME, false, "minecraft:savanna_plateau", 0)
            .addBiomeEntry(BiomeEntryType.REGISTRY_NAME, false, "terralith:fractured_savanna", 1);

}
