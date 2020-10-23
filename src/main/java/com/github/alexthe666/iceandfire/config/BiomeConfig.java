package com.github.alexthe666.iceandfire.config;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.google.common.collect.Lists;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;

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
    public static List<? extends String> graveyardBiomes = Lists.newArrayList(DefaultBiomes.GRAVEYARD_BIOMES);
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
    public static Map<String, ForgeConfigSpec.ConfigValue<List<? extends String>>> biomeConfigValues = new HashMap<>();

    public BiomeConfig(final ForgeConfigSpec.Builder builder) {
        builder.comment(
    		"Biome config",
    		"To filter biomes by registry name \"mod_id:biome_id\"",
    		"To filter biomes by category \"@category\"",
    		"To filter biomes by tags \"#tag\"",
    		"\tExamples:",
    		"\t\t\"minecraft:plains\"",
    		"\t\t\"@desert\"",
    		"\t\t\"#overworld\"",
    		"",
    		"If you want to exclude biomes put a ! before the biome identifier",
    		"\tExamples:",
    		"\t\t\"!minecraft:plains\"",
    		"\t\t\"!@desert\"",
    		"\t\t\"!#nether\"",
    		"",
    		"If you want to include biomes that would be satisfied by any in a set use |",
    		"\tExamples:",
    		"\t\t\"|minecraft:plains\"",
    		"\t\t\"|@desert\"",
    		"\t\t\"|#nether\"",
    		"",
    		"If you want a condition that MUST be satisfied use an & before the biome identifier",
    		"Please note using this on a registry name wouldn't be that useful",
    		"\tExamples:",
    		"\t\t\"&minecraft:plains\"",
    		"\t\t\"&@forest\"",
    		"\t\t\"&#overworld\"",
    		"",
    		"NOTE: Any entry without a !, |, or & symbol has a higher precedence",
    		"A list like [\"!minecraft:plains\", \"#overworld\"] would still see the plains as a viable biome",
    		"",
    		"Finally, you can create a expression that can be evaluated by itself using a + to combine identifiers",
    		"\tExamples:",
    		"\t\t\"!#hot+!#dry+!#mountain\"",
    		"",
    		"These expressions can be used to filter biomes in a lot of ways",
    		"Lets say we don't want anything to spawn in any place dry and sandy",
    		"\t\"!#dry+!#sandy\"",
    		"",
    		"But there is a hot place we want them to spawn that's also wet",
    		"\t\"#hot+#wet\"",
    		"",
    		"We just put them as separate values in the list and that'll work out",
    		"\t[\"!#dry+!#sandy\",\"#hot+#wet\"]",
    		"",
    		"NOTE: Any entry that's an expression will not be affected by anything else in the list")
        	.push("biome_configs");
        try {
            for (Field f : BiomeConfig.class.getDeclaredFields()) {
                Object obj = f.get(null);
               if(obj instanceof List){
                   biomeConfigValues.putIfAbsent(f.getName(), builder.defineList(f.getName(), (List)obj, o -> o instanceof String));
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
