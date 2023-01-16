package com.github.alexthe666.iceandfire.config;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.config.biome.IafSpawnBiomeData;
import com.github.alexthe666.iceandfire.config.biome.SpawnBiomeConfig;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import org.apache.commons.lang3.tuple.Pair;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class BiomeConfig {
    public static Pair<String, IafSpawnBiomeData> oreGenBiomes = Pair.of("iceandfire:ore_gen_biomes", DefaultBiomes.OVERWORLD);
    public static Pair<String, IafSpawnBiomeData> sapphireBiomes = Pair.of("iceandfire:sapphire_gen_biomes", DefaultBiomes.VERY_SNOWY);
    public static Pair<String, IafSpawnBiomeData> amethystBiomes = Pair.of("iceandfire:amethyst_gen_biomes", DefaultBiomes.SAVANNAS);
    public static Pair<String, IafSpawnBiomeData> fireLilyBiomes = Pair.of("iceandfire:fire_lily_biomes", DefaultBiomes.VERY_HOT);
    public static Pair<String, IafSpawnBiomeData> iceLilyBiomes = Pair.of("iceandfire:ice_lily_biomes", DefaultBiomes.VERY_SNOWY);
    public static Pair<String, IafSpawnBiomeData> lightningLilyBiomes = Pair.of("iceandfire:lightning_lily_biomes", DefaultBiomes.SAVANNAS);
    public static Pair<String, IafSpawnBiomeData> fireDragonBiomes = Pair.of("iceandfire:fire_dragon_biomes", DefaultBiomes.FIREDRAGON_ROOST);
    public static Pair<String, IafSpawnBiomeData> iceDragonBiomes = Pair.of("iceandfire:ice_dragon_biomes", DefaultBiomes.ICEDRAGON_ROOST);
    public static Pair<String, IafSpawnBiomeData> lightningDragonBiomes = Pair.of("iceandfire:lightning_dragon_biomes", DefaultBiomes.LIGHTNING_ROOST);

    public static Pair<String, IafSpawnBiomeData> cyclopsCaveBiomes = Pair.of("iceandfire:cyclops_cave_biomes", DefaultBiomes.BEACHES);
    public static Pair<String, IafSpawnBiomeData> hippogryphBiomes = Pair.of("iceandfire:hippogryph_biomes", DefaultBiomes.HILLS);
    public static Pair<String, IafSpawnBiomeData> pixieBiomes = Pair.of("iceandfire:pixie_village_biomes", DefaultBiomes.PIXIES);
    public static Pair<String, IafSpawnBiomeData> hippocampusBiomes = Pair.of("iceandfire:hippocampus_biomes", DefaultBiomes.OCEANS);
    public static Pair<String, IafSpawnBiomeData> seaSerpentBiomes = Pair.of("iceandfire:sea_serpent_biomes", DefaultBiomes.OCEANS);
    public static Pair<String, IafSpawnBiomeData> sirenBiomes = Pair.of("iceandfire:siren_biomes", DefaultBiomes.OCEANS);
    public static Pair<String, IafSpawnBiomeData> amphithereBiomes = Pair.of("iceandfire:amphithere_biomes", DefaultBiomes.JUNGLE);
    public static Pair<String, IafSpawnBiomeData> desertMyrmexBiomes = Pair.of("iceandfire:desert_myrmex_biomes", DefaultBiomes.DESERT);
    public static Pair<String, IafSpawnBiomeData> jungleMyrmexBiomes = Pair.of("iceandfire:jungle_myrmex_biomes", DefaultBiomes.JUNGLE);
    public static Pair<String, IafSpawnBiomeData> snowyTrollBiomes = Pair.of("iceandfire:snowy_troll_biomes", DefaultBiomes.SNOWY);
    public static Pair<String, IafSpawnBiomeData> forestTrollBiomes = Pair.of("iceandfire:forest_troll_biomes", DefaultBiomes.WOODLAND);
    public static Pair<String, IafSpawnBiomeData> mountainTrollBiomes = Pair.of("iceandfire:mountain_troll_biomes", DefaultBiomes.VERY_HILLY);

    public static Pair<String, IafSpawnBiomeData> stymphalianBiomes = Pair.of("iceandfire:stymphalian_bird_biomes", DefaultBiomes.SWAMPS);
    public static Pair<String, IafSpawnBiomeData> hydraBiomes = Pair.of("iceandfire:hydra_cave_biomes", DefaultBiomes.SWAMPS);

    // TODO: Either remove or use these to dynamically create the new biome tags
    public static Pair<String, IafSpawnBiomeData> mausoleumBiomes = Pair.of("iceandfire:mausoleum_biomes", DefaultBiomes.VERY_SNOWY);
    public static Pair<String, IafSpawnBiomeData> graveyardBiomes = Pair.of("iceandfire:graveyard_biomes", DefaultBiomes.GRAVEYARD);
    public static Pair<String, IafSpawnBiomeData> gorgonTempleBiomes = Pair.of("iceandfire:gorgon_temple_biomes", DefaultBiomes.BEACHES);

    public static Pair<String, IafSpawnBiomeData> cockatriceBiomes = Pair.of("iceandfire:cockatrice_biomes", DefaultBiomes.SAVANNAS);
    public static Pair<String, IafSpawnBiomeData> deathwormBiomes = Pair.of("iceandfire:deathworm_biomes", DefaultBiomes.DESERT);
    public static Pair<String, IafSpawnBiomeData> wanderingCyclopsBiomes = Pair.of("iceandfire:wandering_cyclops_biomes", DefaultBiomes.PLAINS);
    public static Pair<String, IafSpawnBiomeData> lightningDragonSkeletonBiomes = Pair.of("iceandfire:lightning_dragon_skeleton_biomes", DefaultBiomes.SAVANNAS);
    public static Pair<String, IafSpawnBiomeData> fireDragonSkeletonBiomes = Pair.of("iceandfire:fire_dragon_skeleton_biomes", DefaultBiomes.DESERT);
    public static Pair<String, IafSpawnBiomeData> iceDragonSkeletonBiomes = Pair.of("iceandfire:ice_dragon_skeleton_biomes", DefaultBiomes.VERY_SNOWY);

    public static Pair<String, IafSpawnBiomeData> blackHippogryphBiomes = Pair.of("iceandfire:hippogryph_black_biomes", DefaultBiomes.HIPPOGRYPH_BLACK);
    public static Pair<String, IafSpawnBiomeData> brownHippogryphBiomes = Pair.of("iceandfire:hippogryph_brown_biomes", DefaultBiomes.VERY_HILLY);
    public static Pair<String, IafSpawnBiomeData> grayHippogryphBiomes = Pair.of("iceandfire:hippogryph_gray_biomes", DefaultBiomes.HIPPOGRYPH_GRAY);
    public static Pair<String, IafSpawnBiomeData> chestnutHippogryphBiomes = Pair.of("iceandfire:hippogryph_chestnut_biomes", DefaultBiomes.HIPPOGRYPH_CHESTNUT);
    public static Pair<String, IafSpawnBiomeData> creamyHippogryphBiomes = Pair.of("iceandfire:hippogryph_creamy_biomes", DefaultBiomes.HIPPOGRYPH_CREAMY);
    public static Pair<String, IafSpawnBiomeData> darkBrownHippogryphBiomes = Pair.of("iceandfire:hippogryph_dark_brown_biomes", DefaultBiomes.HIPPOGRYPH_DBROWN);
    public static Pair<String, IafSpawnBiomeData> whiteHippogryphBiomes = Pair.of("iceandfire:hippogryph_white_biomes", DefaultBiomes.HIPPOGRYPH_WHITE);

    private static boolean init = false;
    private static final Map<String, IafSpawnBiomeData> biomeConfigValues = new HashMap<>();

    public static void init() {
        try {
            for (Field f : BiomeConfig.class.getFields()) {
                Object obj = f.get(null);
                if (obj instanceof Pair) {
                    String id = (String) ((Pair) obj).getLeft();
                    IafSpawnBiomeData data = (IafSpawnBiomeData) ((Pair) obj).getRight();
                    biomeConfigValues.put(id, SpawnBiomeConfig.create(new ResourceLocation(id), data));
                }
            }
        }catch (Exception e){
            IceAndFire.LOGGER.warn("Encountered error building iceandfire biome config .json files");
            e.printStackTrace();
        }
        init = true;
    }

    public static boolean test(Pair<String, IafSpawnBiomeData> spawns, Holder<Biome> biome) {
        if (!init) {
            init();
        }
        return biomeConfigValues.get(spawns.getKey()).matches(biome, biome.value().getRegistryName());
    }
}
