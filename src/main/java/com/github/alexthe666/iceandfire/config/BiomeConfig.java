package com.github.alexthe666.iceandfire.config;

import com.github.alexthe666.citadel.config.biome.SpawnBiomeConfig;
import com.github.alexthe666.citadel.config.biome.SpawnBiomeData;
import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BiomeConfig {
    public static final Map.Entry<String, SpawnBiomeData> oreGenBiomes = Map.entry("iceandfire:ore_gen_biomes", DefaultBiomes.OVERWORLD);
    public static final Map.Entry<String, SpawnBiomeData> sapphireBiomes = Map.entry("iceandfire:sapphire_gen_biomes", DefaultBiomes.VERY_SNOWY);
    public static final Map.Entry<String, SpawnBiomeData> fireLilyBiomes = Map.entry("iceandfire:fire_lily_biomes", DefaultBiomes.VERY_HOT);
    public static final Map.Entry<String, SpawnBiomeData> frostLilyBiomes = Map.entry("iceandfire:frost_lily_biomes", DefaultBiomes.VERY_SNOWY);
    public static final Map.Entry<String, SpawnBiomeData> lightningLilyBiomes = Map.entry("iceandfire:lightning_lily_biomes", DefaultBiomes.SAVANNAS);
    public static final Map.Entry<String, SpawnBiomeData> fireDragonBiomes = Map.entry("iceandfire:fire_dragon_biomes", DefaultBiomes.FIREDRAGON_ROOST);
    public static final Map.Entry<String, SpawnBiomeData> fireDragonCaveBiomes = Map.entry("iceandfire:fire_dragon_cave_biomes", DefaultBiomes.FIREDRAGON_CAVE);
    public static final Map.Entry<String, SpawnBiomeData> iceDragonBiomes = Map.entry("iceandfire:ice_dragon_biomes", DefaultBiomes.ICEDRAGON_ROOST);
    public static final Map.Entry<String, SpawnBiomeData> iceDragonCaveBiomes = Map.entry("iceandfire:ice_dragon_cave_biomes", DefaultBiomes.ICEDRAGON_CAVE);
    public static final Map.Entry<String, SpawnBiomeData> lightningDragonBiomes = Map.entry("iceandfire:lightning_dragon_biomes", DefaultBiomes.LIGHTNING_ROOST);
    public static final Map.Entry<String, SpawnBiomeData> lightningDragonCaveBiomes = Map.entry("iceandfire:lightning_dragon_cave_biomes", DefaultBiomes.LIGHTNING_CAVE);

    public static final Map.Entry<String, SpawnBiomeData> cyclopsCaveBiomes = Map.entry("iceandfire:cyclops_cave_biomes", DefaultBiomes.BEACHES);
    public static final Map.Entry<String, SpawnBiomeData> hippogryphBiomes = Map.entry("iceandfire:hippogryph_biomes", DefaultBiomes.HILLS);
    public static final Map.Entry<String, SpawnBiomeData> pixieBiomes = Map.entry("iceandfire:pixie_village_biomes", DefaultBiomes.PIXIES);
    public static final Map.Entry<String, SpawnBiomeData> hippocampusBiomes = Map.entry("iceandfire:hippocampus_biomes", DefaultBiomes.OCEANS);
    public static final Map.Entry<String, SpawnBiomeData> seaSerpentBiomes = Map.entry("iceandfire:sea_serpent_biomes", DefaultBiomes.OCEANS);
    public static final Map.Entry<String, SpawnBiomeData> sirenBiomes = Map.entry("iceandfire:siren_biomes", DefaultBiomes.OCEANS);
    public static final Map.Entry<String, SpawnBiomeData> amphithereBiomes = Map.entry("iceandfire:amphithere_biomes", DefaultBiomes.JUNGLE);
    public static final Map.Entry<String, SpawnBiomeData> desertMyrmexBiomes = Map.entry("iceandfire:desert_myrmex_biomes", DefaultBiomes.DESERT);
    public static final Map.Entry<String, SpawnBiomeData> jungleMyrmexBiomes = Map.entry("iceandfire:jungle_myrmex_biomes", DefaultBiomes.JUNGLE);
    public static final Map.Entry<String, SpawnBiomeData> snowyTrollBiomes = Map.entry("iceandfire:snowy_troll_biomes", DefaultBiomes.SNOWY);
    public static final Map.Entry<String, SpawnBiomeData> forestTrollBiomes = Map.entry("iceandfire:forest_troll_biomes", DefaultBiomes.WOODLAND);
    public static final Map.Entry<String, SpawnBiomeData> mountainTrollBiomes = Map.entry("iceandfire:mountain_troll_biomes", DefaultBiomes.VERY_HILLY);

    public static final Map.Entry<String, SpawnBiomeData> stymphalianBiomes = Map.entry("iceandfire:stymphalian_bird_biomes", DefaultBiomes.SWAMPS);
    public static final Map.Entry<String, SpawnBiomeData> hydraBiomes = Map.entry("iceandfire:hydra_cave_biomes", DefaultBiomes.SWAMPS);

    public static final Map.Entry<String, SpawnBiomeData> mausoleumBiomes = Map.entry("iceandfire:mausoleum_biomes", DefaultBiomes.MAUSOLEUM);
    public static final Map.Entry<String, SpawnBiomeData> graveyardBiomes = Map.entry("iceandfire:graveyard_biomes", DefaultBiomes.GRAVEYARD);
    public static final Map.Entry<String, SpawnBiomeData> gorgonTempleBiomes = Map.entry("iceandfire:gorgon_temple_biomes", DefaultBiomes.BEACHES);

    public static final Map.Entry<String, SpawnBiomeData> cockatriceBiomes = Map.entry("iceandfire:cockatrice_biomes", DefaultBiomes.SAVANNAS);
    public static final Map.Entry<String, SpawnBiomeData> deathwormBiomes = Map.entry("iceandfire:deathworm_biomes", DefaultBiomes.DESERT);
    public static final Map.Entry<String, SpawnBiomeData> wanderingCyclopsBiomes = Map.entry("iceandfire:wandering_cyclops_biomes", DefaultBiomes.PLAINS);
    public static final Map.Entry<String, SpawnBiomeData> lightningDragonSkeletonBiomes = Map.entry("iceandfire:lightning_dragon_skeleton_biomes", DefaultBiomes.SAVANNAS);
    public static final Map.Entry<String, SpawnBiomeData> fireDragonSkeletonBiomes = Map.entry("iceandfire:fire_dragon_skeleton_biomes", DefaultBiomes.DESERT);
    public static final Map.Entry<String, SpawnBiomeData> iceDragonSkeletonBiomes = Map.entry("iceandfire:ice_dragon_skeleton_biomes", DefaultBiomes.VERY_SNOWY);

    public static final Map.Entry<String, SpawnBiomeData> blackHippogryphBiomes = Map.entry("iceandfire:hippogryph_black_biomes", DefaultBiomes.HIPPOGRYPH_BLACK);
    public static final Map.Entry<String, SpawnBiomeData> brownHippogryphBiomes = Map.entry("iceandfire:hippogryph_brown_biomes", DefaultBiomes.VERY_HILLY);
    public static final Map.Entry<String, SpawnBiomeData> grayHippogryphBiomes = Map.entry("iceandfire:hippogryph_gray_biomes", DefaultBiomes.HIPPOGRYPH_GRAY);
    public static final Map.Entry<String, SpawnBiomeData> chestnutHippogryphBiomes = Map.entry("iceandfire:hippogryph_chestnut_biomes", DefaultBiomes.HIPPOGRYPH_CHESTNUT);
    public static final Map.Entry<String, SpawnBiomeData> creamyHippogryphBiomes = Map.entry("iceandfire:hippogryph_creamy_biomes", DefaultBiomes.HIPPOGRYPH_CREAMY);
    public static final Map.Entry<String, SpawnBiomeData> darkBrownHippogryphBiomes = Map.entry("iceandfire:hippogryph_dark_brown_biomes", DefaultBiomes.HIPPOGRYPH_DARK_BROWN);
    public static final Map.Entry<String, SpawnBiomeData> whiteHippogryphBiomes = Map.entry("iceandfire:hippogryph_white_biomes", DefaultBiomes.HIPPOGRYPH_WHITE);
    private static boolean init = false;
    private static final Map<String, SpawnBiomeData> biomeConfigValues = new HashMap<>();
    public static void init() {
        try {
            for (Field f : BiomeConfig.class.getFields()) {
                Object obj = f.get(null);
                if (obj instanceof Map.Entry<?,?> mapObj) {
                    String id = (String) mapObj.getKey();
                    SpawnBiomeData data = (SpawnBiomeData) mapObj.getValue();
                    biomeConfigValues.put(id, SpawnBiomeConfig.create(new ResourceLocation(id), data));
                }
            }
                }catch (Exception e){
                    IceAndFire.LOGGER.warn("Encountered error building iceandfire biome config .json files");
                    e.printStackTrace();
                }
        init = true;
    }

    private static Map<String, SpawnBiomeData> getBiomeConfigValues() {
        final class LazyInit {
            private LazyInit() {}
            private static final Map<String, SpawnBiomeData> BIOME_CONFIG_VALUES;
            static {
                var entries = new ArrayList<Map.Entry<String, SpawnBiomeData>>();
                try {
                    for (Field f : BiomeConfig.class.getFields()) {
                        Object obj = f.get(null);
                        if (obj instanceof Map.Entry entry) {
                            String id = (String) entry.getKey();
                            SpawnBiomeData data = (SpawnBiomeData) entry.getValue();
                            entries.add(Map.entry(id, SpawnBiomeConfig.create(new ResourceLocation(id), data)));
                        }
                    }
                }catch (Exception e){
                    IceAndFire.LOGGER.warn("Encountered error building iceandfire biome config .json files");
                    e.printStackTrace();
                }
                BIOME_CONFIG_VALUES = Map.ofEntries(entries.toArray(new Map.Entry[0]));
            }
        }

        return LazyInit.BIOME_CONFIG_VALUES;
    }

    private static ResourceLocation getBiomeName(Holder<Biome> biome) {
        return biome.unwrap().map(ResourceKey::location, (noKey) -> null);
    }

    public static boolean test(Map.Entry<String, SpawnBiomeData> entry, Holder<Biome> biome, ResourceLocation name) {
        if (!init) {
            init();
        }
        return biomeConfigValues.get(entry.getKey()).matches(biome, name);
    }

    public static boolean test(Map.Entry<String, SpawnBiomeData> entry, Holder<Biome> biome) {
        return BiomeConfig.test(entry, biome, getBiomeName(biome));
    }
    public static boolean test(Map.Entry<String, SpawnBiomeData> entry, Holder.Reference<Biome> biome) {
        return test(entry, biome, biome.key().location());
    }

}
