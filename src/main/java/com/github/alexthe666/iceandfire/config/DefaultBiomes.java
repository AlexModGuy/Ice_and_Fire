package com.github.alexthe666.iceandfire.config;

import com.github.alexthe666.iceandfire.config.biome.IafSpawnBiomeData;
import com.github.alexthe666.iceandfire.config.biome.BiomeEntryType;
import net.minecraft.data.worldgen.biome.Biomes;
import net.minecraftforge.common.Tags;

import static net.minecraft.tags.BiomeTags.*;
import static net.minecraftforge.common.Tags.Biomes.*;

public class DefaultBiomes {

    public static final IafSpawnBiomeData OVERWORLD = new IafSpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_OVERWORLD.location().toString(), 0);

    public static final IafSpawnBiomeData FIREDRAGON_ROOST = new IafSpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_OVERWORLD.location().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_HOT.location().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, true, IS_JUNGLE.location().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, true, IS_BADLANDS.location().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, true, IS_SAVANNA.location().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_OVERWORLD.location().toString(), 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_PLAINS.location().toString(), 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, true, IS_WET.location().toString(), 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, true, IS_COLD.location().toString(), 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, true, IS_JUNGLE.location().toString(), 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, true, IS_BADLANDS.location().toString(), 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, true, IS_SAVANNA.location().toString(), 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, true, IS_FOREST.location().toString(), 1);
            //Not sure if firedragons should generate in mountainous terrain or not
            /*.addBiomeEntry(BiomeEntryType.BIOME_DICT, false, "overworld", 2)
            .addBiomeEntry(BiomeEntryType.BIOME_DICT, false, "mountain", 2)
            .addBiomeEntry(BiomeEntryType.BIOME_DICT, false, "snowy", 2);*/

    public static final IafSpawnBiomeData ICEDRAGON_ROOST = new IafSpawnBiomeData()
            //.addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_OVERWORLD.location().toString(), 0)
            //.addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_ICY.location().toString()"icy", 0) This category doesn't seem to exist anymore
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_OVERWORLD.location().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_SNOWY.location().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_OVERWORLD.location().toString(), 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_COLD.location().toString(), 1);

    public static final IafSpawnBiomeData LIGHTNING_ROOST = new IafSpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_OVERWORLD.location().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_JUNGLE.location().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_OVERWORLD.location().toString(), 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_BADLANDS.location().toString(), 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_OVERWORLD.location().toString(), 2)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_SAVANNA.location().toString(), 2);

    public static final IafSpawnBiomeData VERY_HOT = new IafSpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_HOT_OVERWORLD.location().toString(), 0);

    public static final IafSpawnBiomeData VERY_SNOWY = new IafSpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_OVERWORLD.location().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_SNOWY.location().toString(), 0);
            //.addBiomeEntry(BiomeEntryType.BIOME_DICT, false, "overworld", 1)
            //.addBiomeEntry(BiomeEntryType.BIOME_CATEGORY, false, "icy", 1); // Doesn't exist anymore, might need a replacement

    public static final IafSpawnBiomeData SNOWY = new IafSpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_OVERWORLD.location().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_SNOWY.location().toString(), 0);

    public static final IafSpawnBiomeData VERY_HILLY = new IafSpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_OVERWORLD.location().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_MOUNTAIN.location().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_OVERWORLD.location().toString(), 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_HILL.location().toString(), 1);

    public static final IafSpawnBiomeData WOODLAND = new IafSpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_OVERWORLD.location().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_FOREST.location().toString(), 0);

    public static final IafSpawnBiomeData SAVANNAS = new IafSpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_OVERWORLD.location().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_SAVANNA.location().toString(), 0);

    public static final IafSpawnBiomeData BEACHES = new IafSpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_OVERWORLD.location().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_OVERWORLD.location().toString(), 0);

    public static final IafSpawnBiomeData SWAMPS = new IafSpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_OVERWORLD.location().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_SWAMP.location().toString(), 0);

    public static final IafSpawnBiomeData DESERT = new IafSpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_DRY_OVERWORLD.location().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_HOT_OVERWORLD.location().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_SANDY.location().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, true, IS_BADLANDS.location().toString(), 0);

    public static final IafSpawnBiomeData OCEANS = new IafSpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_OVERWORLD.location().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_OCEAN.location().toString(), 0);

    public static final IafSpawnBiomeData PIXIES = new IafSpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_OVERWORLD.location().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_RARE.location().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_MAGICAL.location().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_OVERWORLD.location().toString(), 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_DENSE.location().toString(), 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_FOREST.location().toString(), 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, true, IS_MOUNTAIN.location().toString(), 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, true, IS_HILL.location().toString(), 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, true, IS_WET.location().toString(), 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, true, IS_HOT.location().toString(), 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, true, IS_COLD.location().toString(), 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, true, IS_DRY.location().toString(), 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, true, IS_TAIGA.location().toString(), 1);

    public static final IafSpawnBiomeData JUNGLE = new IafSpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_OVERWORLD.location().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_JUNGLE.location().toString(), 0);

    public static final IafSpawnBiomeData HILLS = new IafSpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_OVERWORLD.location().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_BADLANDS.location().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_OVERWORLD.location().toString(), 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_MOUNTAIN.location().toString(), 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_OVERWORLD.location().toString(), 2)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_HILL.location().toString(), 2);


    public static final IafSpawnBiomeData PLAINS = new IafSpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_OVERWORLD.location().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_PLAINS.location().toString(), 0);

    public static final IafSpawnBiomeData GRAVEYARD = new IafSpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_OVERWORLD.location().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, true, IS_OCEAN.location().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, true, Tags.Biomes.IS_BEACH.location().toString(), 0);

    public static final IafSpawnBiomeData HIPPOGRYPH_BLACK = new IafSpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_OVERWORLD.location().toString(), 0)
            .addBiomeEntry(BiomeEntryType.REGISTRY_NAME, false, "minecraft:desert_hills", 0);

    public static final IafSpawnBiomeData HIPPOGRYPH_DBROWN = new IafSpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_OVERWORLD.location().toString(), 0)
            .addBiomeEntry(BiomeEntryType.REGISTRY_NAME, false, "minecraft:taiga_mountains", 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_OVERWORLD.location().toString(), 1)
            .addBiomeEntry(BiomeEntryType.REGISTRY_NAME, false, "minecraft:taiga_hills", 1);

    public static final IafSpawnBiomeData HIPPOGRYPH_WHITE = new IafSpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_OVERWORLD.location().toString(), 0)
            .addBiomeEntry(BiomeEntryType.REGISTRY_NAME, false, "minecraft:snowy_mountains", 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_OVERWORLD.location().toString(), 1)
            .addBiomeEntry(BiomeEntryType.REGISTRY_NAME, false, "minecraft:snowy_taiga_hills", 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_OVERWORLD.location().toString(), 2)
            .addBiomeEntry(BiomeEntryType.REGISTRY_NAME, false, "minecraft:snowy_taiga_mountains", 2);

    public static final IafSpawnBiomeData HIPPOGRYPH_GRAY = new IafSpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_OVERWORLD.location().toString(), 0)
            .addBiomeEntry(BiomeEntryType.REGISTRY_NAME, false, "minecraft:dark_forest_hills", 0);

    public static final IafSpawnBiomeData HIPPOGRYPH_CHESTNUT = new IafSpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_OVERWORLD.location().toString(), 0)
            .addBiomeEntry(BiomeEntryType.REGISTRY_NAME, false, "minecraft:wooded_mountains", 0);

    public static final IafSpawnBiomeData HIPPOGRYPH_CREAMY = new IafSpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, IS_OVERWORLD.location().toString(), 0)
            .addBiomeEntry(BiomeEntryType.REGISTRY_NAME, false, "minecraft:savanna_plateau", 0);

}
