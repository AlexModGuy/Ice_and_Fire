package com.github.alexthe666.iceandfire.world;

import com.github.alexthe666.citadel.config.biome.SpawnBiomeData;
import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.config.BiomeConfig;
import com.github.alexthe666.iceandfire.datagen.IafPlacedFeatures;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import com.github.alexthe666.iceandfire.world.feature.*;
import com.github.alexthe666.iceandfire.world.gen.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.storage.LevelData;
import net.minecraftforge.common.world.ModifiableBiomeInfo;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

public class IafWorldRegistry {
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, IceAndFire.MODID);

    public static final RegistryObject<Feature<NoneFeatureConfiguration>> FIRE_DRAGON_ROOST = register("fire_dragon_roost", () -> new WorldGenFireDragonRoosts(NoneFeatureConfiguration.CODEC));
    public static final RegistryObject<Feature<NoneFeatureConfiguration>> ICE_DRAGON_ROOST = register("ice_dragon_roost", () -> new WorldGenIceDragonRoosts(NoneFeatureConfiguration.CODEC));
    public static final RegistryObject<Feature<NoneFeatureConfiguration>> LIGHTNING_DRAGON_ROOST = register("lightning_dragon_roost", () -> new WorldGenLightningDragonRoosts(NoneFeatureConfiguration.CODEC));
    public static final RegistryObject<Feature<NoneFeatureConfiguration>> FIRE_DRAGON_CAVE = register("fire_dragon_cave", () -> new WorldGenFireDragonCave(NoneFeatureConfiguration.CODEC));
    public static final RegistryObject<Feature<NoneFeatureConfiguration>> ICE_DRAGON_CAVE = register("ice_dragon_cave", () -> new WorldGenIceDragonCave(NoneFeatureConfiguration.CODEC));
    public static final RegistryObject<Feature<NoneFeatureConfiguration>> LIGHTNING_DRAGON_CAVE = register("lightning_dragon_cave",
            () -> new WorldGenLightningDragonCave(NoneFeatureConfiguration.CODEC));
    //TODO: Should be a structure
    public static final RegistryObject<Feature<NoneFeatureConfiguration>> CYCLOPS_CAVE = register("cyclops_cave", () -> new WorldGenCyclopsCave(NoneFeatureConfiguration.CODEC));
    public static final RegistryObject<Feature<NoneFeatureConfiguration>> PIXIE_VILLAGE = register("pixie_village", () -> new WorldGenPixieVillage(NoneFeatureConfiguration.CODEC));
    public static final RegistryObject<Feature<NoneFeatureConfiguration>> SIREN_ISLAND = register("siren_island", () -> new WorldGenSirenIsland(NoneFeatureConfiguration.CODEC));
    public static final RegistryObject<Feature<NoneFeatureConfiguration>> HYDRA_CAVE = register("hydra_cave", () -> new WorldGenHydraCave(NoneFeatureConfiguration.CODEC));
    public static final RegistryObject<Feature<NoneFeatureConfiguration>> MYRMEX_HIVE_DESERT = register("myrmex_hive_desert", () -> new WorldGenMyrmexHive(false, false, NoneFeatureConfiguration.CODEC));
    public static final RegistryObject<Feature<NoneFeatureConfiguration>> MYRMEX_HIVE_JUNGLE = register("myrmex_hive_jungle", () -> new WorldGenMyrmexHive(false, true, NoneFeatureConfiguration.CODEC));
    public static final RegistryObject<Feature<NoneFeatureConfiguration>> SPAWN_DEATH_WORM = register("spawn_death_worm", () -> new SpawnDeathWorm(NoneFeatureConfiguration.CODEC));
    public static final RegistryObject<Feature<NoneFeatureConfiguration>> SPAWN_DRAGON_SKELETON_L = register("spawn_dragon_skeleton_lightning",
            () -> new SpawnDragonSkeleton(IafEntityRegistry.LIGHTNING_DRAGON.get(), NoneFeatureConfiguration.CODEC));
    public static final RegistryObject<Feature<NoneFeatureConfiguration>> SPAWN_DRAGON_SKELETON_F = register("spawn_dragon_skeleton_fire",
            () -> new SpawnDragonSkeleton(IafEntityRegistry.FIRE_DRAGON.get(), NoneFeatureConfiguration.CODEC));
    public static final RegistryObject<Feature<NoneFeatureConfiguration>> SPAWN_DRAGON_SKELETON_I = register("spawn_dragon_skeleton_ice",
            () -> new SpawnDragonSkeleton(IafEntityRegistry.ICE_DRAGON.get(), NoneFeatureConfiguration.CODEC));
    public static final RegistryObject<Feature<NoneFeatureConfiguration>> SPAWN_HIPPOCAMPUS = register("spawn_hippocampus", () -> new SpawnHippocampus(NoneFeatureConfiguration.CODEC));
    public static final RegistryObject<Feature<NoneFeatureConfiguration>> SPAWN_SEA_SERPENT = register("spawn_sea_serpent", () -> new SpawnSeaSerpent(NoneFeatureConfiguration.CODEC));
    public static final RegistryObject<Feature<NoneFeatureConfiguration>> SPAWN_STYMPHALIAN_BIRD = register("spawn_stymphalian_bird", () -> new SpawnStymphalianBird(NoneFeatureConfiguration.CODEC));
    public static final RegistryObject<Feature<NoneFeatureConfiguration>> SPAWN_WANDERING_CYCLOPS = register("spawn_wandering_cyclops", () -> new SpawnWanderingCyclops(NoneFeatureConfiguration.CODEC));


    private static <C extends FeatureConfiguration, F extends Feature<C>> RegistryObject<F> register(final String name, final Supplier<? extends F> supplier) {
        return FEATURES.register(name, supplier);
    }

    public static boolean isFarEnoughFromSpawn(final LevelAccessor level, final BlockPos position) {
        LevelData spawnPoint = level.getLevelData();
        BlockPos spawnRelative = new BlockPos(spawnPoint.getXSpawn(), position.getY(), spawnPoint.getYSpawn());
        return !spawnRelative.closerThan(position, IafConfig.dangerousWorldGenDistanceLimit);
    }

    public static boolean isFarEnoughFromDangerousGen(final ServerLevelAccessor level, final BlockPos position, final String id) {
        return isFarEnoughFromDangerousGen(level, position, id, IafWorldData.FeatureType.SURFACE);
    }

    public static boolean isFarEnoughFromDangerousGen(final ServerLevelAccessor level, final BlockPos position, final String id, final IafWorldData.FeatureType type) {
        IafWorldData data = IafWorldData.get(level.getLevel());
        return data.check(type, position, id);
    }

    public static HashMap<String, Boolean> LOADED_FEATURES;

    static {
        LOADED_FEATURES = new HashMap<>();
        LOADED_FEATURES.put("iceandfire:fire_lily", false);
        LOADED_FEATURES.put("iceandfire:frost_lily", false);
        LOADED_FEATURES.put("iceandfire:lightning_lily", false);
        LOADED_FEATURES.put("iceandfire:silver_ore", false);
        LOADED_FEATURES.put("iceandfire:sapphire_ore", false);
        LOADED_FEATURES.put("iceandfire:fire_dragon_roost", false);
        LOADED_FEATURES.put("iceandfire:ice_dragon_roost", false);
        LOADED_FEATURES.put("iceandfire:lightning_dragon_roost", false);
        LOADED_FEATURES.put("iceandfire:fire_dragon_cave", false);
        LOADED_FEATURES.put("iceandfire:ice_dragon_cave", false);
        LOADED_FEATURES.put("iceandfire:lightning_dragon_cave", false);
        LOADED_FEATURES.put("iceandfire:cyclops_cave", false);
        LOADED_FEATURES.put("iceandfire:pixie_village", false);
        LOADED_FEATURES.put("iceandfire:siren_island", false);
        LOADED_FEATURES.put("iceandfire:hydra_cave", false);
        LOADED_FEATURES.put("iceandfire:myrmex_hive_desert", false);
        LOADED_FEATURES.put("iceandfire:myrmex_hive_jungle", false);
        LOADED_FEATURES.put("iceandfire:spawn_death_worm", false);
        LOADED_FEATURES.put("iceandfire:spawn_dragon_skeleton_lightning", false);
        LOADED_FEATURES.put("iceandfire:spawn_dragon_skeleton_fire", false);
        LOADED_FEATURES.put("iceandfire:spawn_dragon_skeleton_ice", false);
        LOADED_FEATURES.put("iceandfire:spawn_hippocampus", false);
        LOADED_FEATURES.put("iceandfire:spawn_sea_serpent", false);
        LOADED_FEATURES.put("iceandfire:spawn_stymphalian_bird", false);
        LOADED_FEATURES.put("iceandfire:spawn_wandering_cyclops", false);
    }

    // Only a global variable because it's too bothersome to add it to the method call (alternative: method returns identifier or null)
    private static List<String> ADDED_FEATURES;

    public static void addFeatures(Holder<Biome> biome, HashMap<String, Holder<PlacedFeature>> features, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        ADDED_FEATURES = new ArrayList<>();

        if (safelyTestBiome(BiomeConfig.fireLilyBiomes, biome)) {
            addFeatureToBiome(IafPlacedFeatures.PLACED_FIRE_LILY, features, builder, GenerationStep.Decoration.VEGETAL_DECORATION);
        }
        if (safelyTestBiome(BiomeConfig.lightningLilyBiomes, biome)) {
            addFeatureToBiome(IafPlacedFeatures.PLACED_LIGHTNING_LILY, features, builder, GenerationStep.Decoration.VEGETAL_DECORATION);
        }
        if (safelyTestBiome(BiomeConfig.frostLilyBiomes, biome)) {
            addFeatureToBiome(IafPlacedFeatures.PLACED_FROST_LILY, features, builder, GenerationStep.Decoration.VEGETAL_DECORATION);
        }
        if (safelyTestBiome(BiomeConfig.oreGenBiomes, biome)) {
            addFeatureToBiome(IafPlacedFeatures.PLACED_SILVER_ORE, features, builder, GenerationStep.Decoration.UNDERGROUND_ORES);
        }
        if (safelyTestBiome(BiomeConfig.sapphireBiomes, biome)) {
            addFeatureToBiome(IafPlacedFeatures.PLACED_SAPPHIRE_ORE, features, builder, GenerationStep.Decoration.UNDERGROUND_ORES);
        }


        if (safelyTestBiome(BiomeConfig.fireDragonBiomes, biome)) {
            addFeatureToBiome(IafPlacedFeatures.PLACED_FIRE_DRAGON_ROOST, features, builder);
        }
        if (safelyTestBiome(BiomeConfig.lightningDragonBiomes, biome)) {
            addFeatureToBiome(IafPlacedFeatures.PLACED_LIGHTNING_DRAGON_ROOST, features, builder);
        }
        if (safelyTestBiome(BiomeConfig.iceDragonBiomes, biome)) {
            addFeatureToBiome(IafPlacedFeatures.PLACED_ICE_DRAGON_ROOST, features, builder);
        }


        if (safelyTestBiome(BiomeConfig.fireDragonCaveBiomes, biome)) {
            addFeatureToBiome(IafPlacedFeatures.PLACED_FIRE_DRAGON_CAVE, features, builder, GenerationStep.Decoration.UNDERGROUND_STRUCTURES);
        }
        if (safelyTestBiome(BiomeConfig.lightningDragonCaveBiomes, biome)) {
            addFeatureToBiome(IafPlacedFeatures.PLACED_LIGHTNING_DRAGON_CAVE, features, builder, GenerationStep.Decoration.UNDERGROUND_STRUCTURES);
        }
        if (safelyTestBiome(BiomeConfig.iceDragonCaveBiomes, biome)) {
            addFeatureToBiome(IafPlacedFeatures.PLACED_ICE_DRAGON_CAVE, features, builder, GenerationStep.Decoration.UNDERGROUND_STRUCTURES);
        }


        if (safelyTestBiome(BiomeConfig.cyclopsCaveBiomes, biome)) {
            addFeatureToBiome(IafPlacedFeatures.PLACED_CYCLOPS_CAVE, features, builder);
        }
        if (safelyTestBiome(BiomeConfig.pixieBiomes, biome)) {
            addFeatureToBiome(IafPlacedFeatures.PLACED_PIXIE_VILLAGE, features, builder);
        }
        if (safelyTestBiome(BiomeConfig.hydraBiomes, biome)) {
            addFeatureToBiome(IafPlacedFeatures.PLACED_HYDRA_CAVE, features, builder);
        }
        if (safelyTestBiome(BiomeConfig.desertMyrmexBiomes, biome)) {
            addFeatureToBiome(IafPlacedFeatures.PLACED_MYRMEX_HIVE_DESERT, features, builder);
        }
        if (safelyTestBiome(BiomeConfig.jungleMyrmexBiomes, biome)) {
            addFeatureToBiome(IafPlacedFeatures.PLACED_MYRMEX_HIVE_JUNGLE, features, builder);
        }
        if (safelyTestBiome(BiomeConfig.sirenBiomes, biome)) {
            addFeatureToBiome(IafPlacedFeatures.PLACED_SIREN_ISLAND, features, builder);
        }
        if (safelyTestBiome(BiomeConfig.deathwormBiomes, biome)) {
            addFeatureToBiome(IafPlacedFeatures.PLACED_SPAWN_DEATH_WORM, features, builder);
        }
        if (safelyTestBiome(BiomeConfig.wanderingCyclopsBiomes, biome)) {
            addFeatureToBiome(IafPlacedFeatures.PLACED_SPAWN_WANDERING_CYCLOPS, features, builder);
        }

        if (safelyTestBiome(BiomeConfig.lightningDragonSkeletonBiomes, biome)) {
            addFeatureToBiome(IafPlacedFeatures.PLACED_SPAWN_DRAGON_SKELETON_L, features, builder);
        }
        if (safelyTestBiome(BiomeConfig.fireDragonSkeletonBiomes, biome)) {
            addFeatureToBiome(IafPlacedFeatures.PLACED_SPAWN_DRAGON_SKELETON_F, features, builder);
        }
        if (safelyTestBiome(BiomeConfig.iceDragonSkeletonBiomes, biome)) {
            addFeatureToBiome(IafPlacedFeatures.PLACED_SPAWN_DRAGON_SKELETON_I, features, builder);
        }

        if (safelyTestBiome(BiomeConfig.hippocampusBiomes, biome)) {
            addFeatureToBiome(IafPlacedFeatures.PLACED_SPAWN_HIPPOCAMPUS, features, builder);
        }
        if (safelyTestBiome(BiomeConfig.seaSerpentBiomes, biome)) {
            addFeatureToBiome(IafPlacedFeatures.PLACED_SPAWN_SEA_SERPENT, features, builder);
        }
        if (safelyTestBiome(BiomeConfig.stymphalianBiomes, biome)) {
            addFeatureToBiome(IafPlacedFeatures.PLACED_SPAWN_STYMPHALIAN_BIRD, features, builder);
        }

        if (!ADDED_FEATURES.isEmpty()) {
            StringBuilder featureList = new StringBuilder();

            for (String feature : ADDED_FEATURES) {
                featureList.append("\n").append("\t- ").append(feature);
            }

            IceAndFire.LOGGER.debug("Added the following features to the biome [{}]: {}", biome.unwrapKey().get().location(), featureList);
        }

        ADDED_FEATURES = null;
    }

    private static void addFeatureToBiome(ResourceKey<PlacedFeature> feature, HashMap<String, Holder<PlacedFeature>> features, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        addFeatureToBiome(feature, features, builder, GenerationStep.Decoration.SURFACE_STRUCTURES);
    }

    private static void addFeatureToBiome(ResourceKey<PlacedFeature> featureResource, HashMap<String, Holder<PlacedFeature>> features, ModifiableBiomeInfo.BiomeInfo.Builder builder, GenerationStep.Decoration step) {
        String identifier = featureResource.location().toString();
        Holder<PlacedFeature> feature = features.get(identifier);

        if (feature != null) {
            builder.getGenerationSettings().getFeatures(step).add(feature);
            LOADED_FEATURES.put(identifier, true);
            ADDED_FEATURES.add(identifier);
        } else {
            IceAndFire.LOGGER.warn("Feature [{}] could not be found", identifier);
        }
    }


    private static boolean safelyTestBiome(Pair<String, SpawnBiomeData> entry, Holder<Biome> biomeHolder) {
        try {
            return BiomeConfig.test(entry, biomeHolder);
        } catch (Exception e) {
            return false;
        }
    }

}
