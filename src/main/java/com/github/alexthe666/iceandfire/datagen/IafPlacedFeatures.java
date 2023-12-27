package com.github.alexthe666.iceandfire.datagen;

import com.github.alexthe666.iceandfire.world.CustomBiomeFilter;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;

public final class IafPlacedFeatures {

    public static final ResourceKey<PlacedFeature> PLACED_FIRE_DRAGON_ROOST = registerKey("fire_dragon_roost");
    public static final ResourceKey<PlacedFeature> PLACED_ICE_DRAGON_ROOST = registerKey("ice_dragon_roost");
    public static final ResourceKey<PlacedFeature> PLACED_LIGHTNING_DRAGON_ROOST = registerKey("lightning_dragon_roost");
    public static final ResourceKey<PlacedFeature> PLACED_FIRE_DRAGON_CAVE = registerKey("fire_dragon_cave");
    public static final ResourceKey<PlacedFeature> PLACED_ICE_DRAGON_CAVE = registerKey("ice_dragon_cave");
    public static final ResourceKey<PlacedFeature> PLACED_LIGHTNING_DRAGON_CAVE = registerKey("lightning_dragon_cave");
    public static final ResourceKey<PlacedFeature> PLACED_CYCLOPS_CAVE = registerKey("cyclops_cave");
    public static final ResourceKey<PlacedFeature> PLACED_PIXIE_VILLAGE = registerKey("pixie_village");
    public static final ResourceKey<PlacedFeature> PLACED_SIREN_ISLAND = registerKey("siren_island");
    public static final ResourceKey<PlacedFeature> PLACED_HYDRA_CAVE = registerKey("hydra_cave");
    public static final ResourceKey<PlacedFeature> PLACED_MYRMEX_HIVE_DESERT = registerKey("myrmex_hive_desert");
    public static final ResourceKey<PlacedFeature> PLACED_MYRMEX_HIVE_JUNGLE = registerKey("myrmex_hive_jungle");
    public static final ResourceKey<PlacedFeature> PLACED_SPAWN_DEATH_WORM = registerKey("spawn_death_worm");
    public static final ResourceKey<PlacedFeature> PLACED_SPAWN_DRAGON_SKELETON_L = registerKey("spawn_dragon_skeleton_lightning");
    public static final ResourceKey<PlacedFeature> PLACED_SPAWN_DRAGON_SKELETON_F = registerKey("spawn_dragon_skeleton_fire");
    public static final ResourceKey<PlacedFeature> PLACED_SPAWN_DRAGON_SKELETON_I = registerKey("spawn_dragon_skeleton_ice");
    public static final ResourceKey<PlacedFeature> PLACED_SPAWN_HIPPOCAMPUS = registerKey("spawn_hippocampus");
    public static final ResourceKey<PlacedFeature> PLACED_SPAWN_SEA_SERPENT = registerKey("spawn_sea_serpent");
    public static final ResourceKey<PlacedFeature> PLACED_SPAWN_STYMPHALIAN_BIRD = registerKey("spawn_stymphalian_bird");
    public static final ResourceKey<PlacedFeature> PLACED_SPAWN_WANDERING_CYCLOPS = registerKey("spawn_wandering_cyclops");
    public static final ResourceKey<PlacedFeature> PLACED_SILVER_ORE = registerKey("silver_ore");
    public static final ResourceKey<PlacedFeature> PLACED_SAPPHIRE_ORE = registerKey("sapphire_ore");
    public static final ResourceKey<PlacedFeature> PLACED_FIRE_LILY = registerKey("fire_lily");
    public static final ResourceKey<PlacedFeature> PLACED_LIGHTNING_LILY = registerKey("lightning_lily");
    public static final ResourceKey<PlacedFeature> PLACED_FROST_LILY = registerKey("frost_lily");


    private static List<PlacementModifier> orePlacement(PlacementModifier pCountPlacement, PlacementModifier pHeightRange) {
        return List.of(pCountPlacement, InSquarePlacement.spread(), pHeightRange, BiomeFilter.biome());
    }
    private static List<PlacementModifier> commonOrePlacement(int pCount, PlacementModifier pHeightRange) {
        return orePlacement(CountPlacement.of(pCount), pHeightRange);
    }
    public static void bootstrap(BootstapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> features = context.lookup(Registries.CONFIGURED_FEATURE);

        // Surface
        context.register(PLACED_FIRE_DRAGON_ROOST, new PlacedFeature(features.getOrThrow(IafConfiguredFeatures.FIRE_DRAGON_ROOST), List.of(PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome())));
        context.register(PLACED_ICE_DRAGON_ROOST, new PlacedFeature(features.getOrThrow(IafConfiguredFeatures.ICE_DRAGON_ROOST), List.of(PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome())));
        context.register(PLACED_LIGHTNING_DRAGON_ROOST, new PlacedFeature(features.getOrThrow(IafConfiguredFeatures.LIGHTNING_DRAGON_ROOST), List.of(PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome())));
        context.register(PLACED_CYCLOPS_CAVE, new PlacedFeature(features.getOrThrow(IafConfiguredFeatures.CYCLOPS_CAVE), List.of(PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome())));
        context.register(PLACED_PIXIE_VILLAGE, new PlacedFeature(features.getOrThrow(IafConfiguredFeatures.PIXIE_VILLAGE), List.of(PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome())));
        context.register(PLACED_SIREN_ISLAND, new PlacedFeature(features.getOrThrow(IafConfiguredFeatures.SIREN_ISLAND), List.of(PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome())));
        context.register(PLACED_HYDRA_CAVE, new PlacedFeature(features.getOrThrow(IafConfiguredFeatures.HYDRA_CAVE), List.of(PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome())));
        context.register(PLACED_MYRMEX_HIVE_DESERT, new PlacedFeature(features.getOrThrow(IafConfiguredFeatures.MYRMEX_HIVE_DESERT), List.of(PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome())));
        context.register(PLACED_MYRMEX_HIVE_JUNGLE, new PlacedFeature(features.getOrThrow(IafConfiguredFeatures.MYRMEX_HIVE_JUNGLE), List.of(PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome())));
        context.register(PLACED_SPAWN_DEATH_WORM, new PlacedFeature(features.getOrThrow(IafConfiguredFeatures.SPAWN_DEATH_WORM), List.of(PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome())));
        context.register(PLACED_SPAWN_DRAGON_SKELETON_L, new PlacedFeature(features.getOrThrow(IafConfiguredFeatures.SPAWN_DRAGON_SKELETON_L), List.of(PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome())));
        context.register(PLACED_SPAWN_DRAGON_SKELETON_F, new PlacedFeature(features.getOrThrow(IafConfiguredFeatures.SPAWN_DRAGON_SKELETON_F), List.of(PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome())));
        context.register(PLACED_SPAWN_DRAGON_SKELETON_I, new PlacedFeature(features.getOrThrow(IafConfiguredFeatures.SPAWN_DRAGON_SKELETON_I), List.of(PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome())));
        context.register(PLACED_SPAWN_HIPPOCAMPUS, new PlacedFeature(features.getOrThrow(IafConfiguredFeatures.SPAWN_HIPPOCAMPUS), List.of(PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome())));
        context.register(PLACED_SPAWN_SEA_SERPENT, new PlacedFeature(features.getOrThrow(IafConfiguredFeatures.SPAWN_SEA_SERPENT), List.of(PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome())));
        context.register(PLACED_SPAWN_STYMPHALIAN_BIRD, new PlacedFeature(features.getOrThrow(IafConfiguredFeatures.SPAWN_STYMPHALIAN_BIRD), List.of(PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome())));
        context.register(PLACED_SPAWN_WANDERING_CYCLOPS, new PlacedFeature(features.getOrThrow(IafConfiguredFeatures.SPAWN_WANDERING_CYCLOPS), List.of(PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome())));
       //TODO: Check gen chance VegetationFeatures.java
        context.register(PLACED_FIRE_LILY, new PlacedFeature(features.getOrThrow(IafConfiguredFeatures.FIRE_LILY), List.of(RarityFilter.onAverageOnceEvery(32), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome())));
        context.register(PLACED_FROST_LILY, new PlacedFeature(features.getOrThrow(IafConfiguredFeatures.FROST_LILY), List.of(RarityFilter.onAverageOnceEvery(32), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome())));
        context.register(PLACED_LIGHTNING_LILY, new PlacedFeature(features.getOrThrow(IafConfiguredFeatures.LIGHTNING_LILY), List.of(RarityFilter.onAverageOnceEvery(32), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome())));

        // Underground
        //TODO: Make it different from copper ore
        context.register(PLACED_SILVER_ORE, new PlacedFeature(features.getOrThrow(IafConfiguredFeatures.SILVER_ORE), commonOrePlacement(16, HeightRangePlacement.triangle(VerticalAnchor.absolute(-16), VerticalAnchor.absolute(112)))));
        //TODO: Maybe copper emerald ore?
        context.register(PLACED_SAPPHIRE_ORE, new PlacedFeature(features.getOrThrow(IafConfiguredFeatures.SAPPHIRE_ORE), commonOrePlacement(4, HeightRangePlacement.triangle(VerticalAnchor.absolute(-16), VerticalAnchor.absolute(112)))));
        context.register(PLACED_FIRE_DRAGON_CAVE, new PlacedFeature(features.getOrThrow(IafConfiguredFeatures.FIRE_DRAGON_CAVE), List.of(CustomBiomeFilter.biome())));
        context.register(PLACED_ICE_DRAGON_CAVE, new PlacedFeature(features.getOrThrow(IafConfiguredFeatures.ICE_DRAGON_CAVE), List.of(CustomBiomeFilter.biome())));
        context.register(PLACED_LIGHTNING_DRAGON_CAVE, new PlacedFeature(features.getOrThrow(IafConfiguredFeatures.LIGHTNING_DRAGON_CAVE), List.of(CustomBiomeFilter.biome())));

    }

    public static ResourceKey<PlacedFeature> registerKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation("iceandfire",name));
    }
}
