package com.github.alexthe666.iceandfire.config;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.google.common.collect.Lists;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BiomeConfig {
    public static List<? extends String> overworldSpawnBiomes = Lists.newArrayList(DefaultBiomes.OVERWORLD_BIOMES);
    public static List<? extends String> oreGenBiomes = Lists.newArrayList(DefaultBiomes.OVERWORLD_BIOMES);
    public static List<? extends String> sapphireBiomes = Lists.newArrayList(DefaultBiomes.VERY_SNOWY);
    public static List<? extends String> amethystBiomes = Lists.newArrayList(DefaultBiomes.SAVANNAS);
    public static List<? extends String> fireLilyBiomes = Lists.newArrayList(DefaultBiomes.VERY_HOT);
    public static List<? extends String> iceLilyBiomes = Lists.newArrayList(DefaultBiomes.VERY_SNOWY);
    public static List<? extends String> lightningLilyBiomes = Lists.newArrayList(DefaultBiomes.SAVANNAS);
    public static List<? extends String> fireDragonBiomes = Lists.newArrayList(DefaultBiomes.FIREDRAGON_ROOST);
    public static List<? extends String> iceDragonBiomes = Lists.newArrayList(DefaultBiomes.ICEDRAGON_ROOST);
    public static List<? extends String> lightningDragonBiomes = Lists.newArrayList(DefaultBiomes.LIGHTNING_ROOSTS);
    public static List<? extends String> gorgonTempleBiomes = Lists.newArrayList(DefaultBiomes.BEACHES);
    public static List<? extends String> cyclopsCaveBiomes = Lists.newArrayList(DefaultBiomes.BEACHES);
    public static List<? extends String> hippogryphBiomes = Lists.newArrayList(DefaultBiomes.HILLS);
    public static List<? extends String> pixieBiomes = Lists.newArrayList(DefaultBiomes.PIXIES);
    public static List<? extends String> hippocampusBiomes = Lists.newArrayList(DefaultBiomes.OCEANS);
    public static List<? extends String> seaSerpentBiomes = Lists.newArrayList(DefaultBiomes.OCEANS);
    public static List<? extends String> sirenBiomes = Lists.newArrayList(DefaultBiomes.OCEANS);
    public static List<? extends String> amphithereBiomes = Lists.newArrayList(DefaultBiomes.JUNGLE);
    public static List<? extends String> desertMyrmexBiomes = Lists.newArrayList(DefaultBiomes.DESERT);
    public static List<? extends String> jungleMyrmexBiomes = Lists.newArrayList(DefaultBiomes.JUNGLE);
    public static List<? extends String> snowyTrollBiomes = Lists.newArrayList(DefaultBiomes.SNOWY);
    public static List<? extends String> forestTrollBiomes = Lists.newArrayList(DefaultBiomes.WOODLAND);
    public static List<? extends String> mountainTrollBiomes = Lists.newArrayList(DefaultBiomes.VERY_HILLY);

    public static List<? extends String> stymphalianBiomes = Lists.newArrayList(DefaultBiomes.SWAMPS);
    public static List<? extends String> hydraBiomes = Lists.newArrayList(DefaultBiomes.SWAMPS);
    public static List<? extends String> mausoleumBiomes = Lists.newArrayList(DefaultBiomes.VERY_SNOWY);
    public static List<? extends String> cockatriceBiomes = Lists.newArrayList(DefaultBiomes.SAVANNAS);
    public static List<? extends String> deathwormBiomes = Lists.newArrayList(DefaultBiomes.DESERT);
    public static List<? extends String> wanderingCyclopsBiomes = Lists.newArrayList(DefaultBiomes.PLAINS);
    public static List<? extends String> lightningDragonSkeletonBiomes = Lists.newArrayList(DefaultBiomes.SAVANNAS);
    public static List<? extends String> fireDragonSkeletonBiomes = Lists.newArrayList(DefaultBiomes.DESERT);
    public static List<? extends String> iceDragonSkeletonBiomes = Lists.newArrayList(DefaultBiomes.VERY_SNOWY);

    public static List<? extends String> blackHippogryphBiomes = Lists.newArrayList("minecraft:desert_hills");
    public static List<? extends String> brownHippogryphBiomes = Lists.newArrayList(DefaultBiomes.VERY_HILLY);
    public static List<? extends String> grayHippogryphBiomes = Lists.newArrayList("minecraft:dark_forest_hills");
    public static List<? extends String> chestnutHippogryphBiomes = Lists.newArrayList("minecraft:wooded_mountains");
    public static List<? extends String> creamyHippogryphBiomes = Lists.newArrayList("minecraft:savanna_plateau");
    public static List<? extends String> darkBrownHippogryphBiomes = Lists.newArrayList("minecraft:taiga_mountains", "minecraft:taiga_hills");
    public static List<? extends String> whiteHippogryphBiomes = Lists.newArrayList("minecraft:snowy_mountains", "minecraft:snowy_taiga_mountains", "minecraft:snowy_taiga_hills");
    public Map<String, ForgeConfigSpec.ConfigValue<List<? extends String>>> biomeConfigValues = new HashMap<>();

    public BiomeConfig(final ForgeConfigSpec.Builder builder) {
        builder.push("biome_configs");
        try {
            for (Field f : BiomeConfig.class.getDeclaredFields()) {
                Object obj = f.get(null);
               if(obj instanceof List){
                   biomeConfigValues.putIfAbsent(f.getName(), builder.comment("biome list").defineList(f.getName(), (List)obj, o -> o instanceof String));
               }
            }
        }catch (Exception e){
            IceAndFire.LOGGER.warn("Encountered error building iceandfire-biomes.toml");
            e.printStackTrace();
        }
    }

    public static void bake(ModConfig config) {
        try {
            for (Field f : BiomeConfig.class.getDeclaredFields()) {
                Object obj = f.get(null);
                if(obj instanceof List){
                    ForgeConfigSpec.ConfigValue<List<? extends String>> configValue = ConfigHolder.BIOME.biomeConfigValues.get(f.getName());
                    if(config != null){
                        f.set(null, configValue.get());
                    }
                }
            }
        }catch (Exception e){
            IceAndFire.LOGGER.warn("Encountered error building iceandfire-biomes.toml");
            e.printStackTrace();
        }
    }
}
