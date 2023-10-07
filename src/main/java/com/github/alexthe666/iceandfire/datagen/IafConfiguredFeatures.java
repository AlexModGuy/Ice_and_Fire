package com.github.alexthe666.iceandfire.datagen;

import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.world.IafWorldRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

import java.util.List;
import java.util.function.Function;

public final class IafConfiguredFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> FIRE_DRAGON_ROOST = registerKey("fire_dragon_roost");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ICE_DRAGON_ROOST = registerKey("ice_dragon_roost");
    public static final ResourceKey<ConfiguredFeature<?, ?>> LIGHTNING_DRAGON_ROOST = registerKey("lightning_dragon_roost");
    public static final ResourceKey<ConfiguredFeature<?, ?>> FIRE_DRAGON_CAVE = registerKey("fire_dragon_cave");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ICE_DRAGON_CAVE = registerKey("ice_dragon_cave");
    public static final ResourceKey<ConfiguredFeature<?, ?>> LIGHTNING_DRAGON_CAVE = registerKey("lightning_dragon_cave");
    //TODO: Should be a structure
    public static final ResourceKey<ConfiguredFeature<?, ?>> CYCLOPS_CAVE = registerKey("cyclops_cave");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PIXIE_VILLAGE = registerKey("pixie_village");
    public static final ResourceKey<ConfiguredFeature<?, ?>> SIREN_ISLAND = registerKey("siren_island");
    public static final ResourceKey<ConfiguredFeature<?, ?>> HYDRA_CAVE = registerKey("hydra_cave");
    public static final ResourceKey<ConfiguredFeature<?, ?>> MYRMEX_HIVE_DESERT = registerKey("myrmex_hive_desert");
    public static final ResourceKey<ConfiguredFeature<?, ?>> MYRMEX_HIVE_JUNGLE = registerKey("myrmex_hive_jungle");
    public static final ResourceKey<ConfiguredFeature<?, ?>> SPAWN_DEATH_WORM = registerKey("spawn_death_worm");
    public static final ResourceKey<ConfiguredFeature<?, ?>> SPAWN_DRAGON_SKELETON_L = registerKey("spawn_dragon_skeleton_lightning");
    public static final ResourceKey<ConfiguredFeature<?, ?>> SPAWN_DRAGON_SKELETON_F = registerKey("spawn_dragon_skeleton_fire");
    public static final ResourceKey<ConfiguredFeature<?, ?>> SPAWN_DRAGON_SKELETON_I = registerKey("spawn_dragon_skeleton_ice");
    public static final ResourceKey<ConfiguredFeature<?, ?>> SPAWN_HIPPOCAMPUS = registerKey("spawn_hippocampus");
    public static final ResourceKey<ConfiguredFeature<?, ?>> SPAWN_SEA_SERPENT = registerKey("spawn_sea_serpent");
    public static final ResourceKey<ConfiguredFeature<?, ?>> SPAWN_STYMPHALIAN_BIRD = registerKey("spawn_stymphalian_bird");
    public static final ResourceKey<ConfiguredFeature<?, ?>> SPAWN_WANDERING_CYCLOPS = registerKey("spawn_wandering_cyclops");


    public static final ResourceKey<ConfiguredFeature<?, ?>> SILVER_ORE = registerKey("silver_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> SAPPHIRE_ORE = registerKey("sapphire_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> FIRE_LILY = registerKey("fire_lily");
    public static final ResourceKey<ConfiguredFeature<?, ?>> FROST_LILY = registerKey("frost_lily");
    public static final ResourceKey<ConfiguredFeature<?, ?>> LIGHTNING_LILY = registerKey("lightning_lily");


    private static Function<Block, RandomPatchConfiguration> flowerConf = (block) -> FeatureUtils.simpleRandomPatchConfiguration(8, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(block.defaultBlockState().getBlock()))));


    public static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation("iceandfire", name));
    }

    public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> context) {
        context.register(FIRE_DRAGON_ROOST, new ConfiguredFeature<>(IafWorldRegistry.FIRE_DRAGON_ROOST.get(), FeatureConfiguration.NONE));
        context.register(ICE_DRAGON_ROOST, new ConfiguredFeature<>(IafWorldRegistry.ICE_DRAGON_ROOST.get(), FeatureConfiguration.NONE));
        context.register(LIGHTNING_DRAGON_ROOST, new ConfiguredFeature<>(IafWorldRegistry.LIGHTNING_DRAGON_ROOST.get(), FeatureConfiguration.NONE));
        context.register(FIRE_DRAGON_CAVE, new ConfiguredFeature<>(IafWorldRegistry.FIRE_DRAGON_CAVE.get(), FeatureConfiguration.NONE));
        context.register(ICE_DRAGON_CAVE, new ConfiguredFeature<>(IafWorldRegistry.ICE_DRAGON_CAVE.get(), FeatureConfiguration.NONE));
        context.register(LIGHTNING_DRAGON_CAVE, new ConfiguredFeature<>(IafWorldRegistry.LIGHTNING_DRAGON_CAVE.get(), FeatureConfiguration.NONE));
        context.register(CYCLOPS_CAVE, new ConfiguredFeature<>(IafWorldRegistry.CYCLOPS_CAVE.get(), FeatureConfiguration.NONE));
        context.register(PIXIE_VILLAGE, new ConfiguredFeature<>(IafWorldRegistry.PIXIE_VILLAGE.get(), FeatureConfiguration.NONE));
        context.register(SIREN_ISLAND, new ConfiguredFeature<>(IafWorldRegistry.SIREN_ISLAND.get(), FeatureConfiguration.NONE));
        context.register(HYDRA_CAVE, new ConfiguredFeature<>(IafWorldRegistry.HYDRA_CAVE.get(), FeatureConfiguration.NONE));
        context.register(MYRMEX_HIVE_DESERT, new ConfiguredFeature<>(IafWorldRegistry.MYRMEX_HIVE_DESERT.get(), FeatureConfiguration.NONE));
        context.register(MYRMEX_HIVE_JUNGLE, new ConfiguredFeature<>(IafWorldRegistry.MYRMEX_HIVE_JUNGLE.get(), FeatureConfiguration.NONE));
        context.register(SPAWN_DEATH_WORM, new ConfiguredFeature<>(IafWorldRegistry.SPAWN_DEATH_WORM.get(), FeatureConfiguration.NONE));
        context.register(SPAWN_DRAGON_SKELETON_L, new ConfiguredFeature<>(IafWorldRegistry.SPAWN_DRAGON_SKELETON_L.get(), FeatureConfiguration.NONE));
        context.register(SPAWN_DRAGON_SKELETON_F, new ConfiguredFeature<>(IafWorldRegistry.SPAWN_DRAGON_SKELETON_F.get(), FeatureConfiguration.NONE));
        context.register(SPAWN_DRAGON_SKELETON_I, new ConfiguredFeature<>(IafWorldRegistry.SPAWN_DRAGON_SKELETON_I.get(), FeatureConfiguration.NONE));
        context.register(SPAWN_HIPPOCAMPUS, new ConfiguredFeature<>(IafWorldRegistry.SPAWN_HIPPOCAMPUS.get(), FeatureConfiguration.NONE));
        context.register(SPAWN_SEA_SERPENT, new ConfiguredFeature<>(IafWorldRegistry.SPAWN_SEA_SERPENT.get(), FeatureConfiguration.NONE));
        context.register(SPAWN_STYMPHALIAN_BIRD, new ConfiguredFeature<>(IafWorldRegistry.SPAWN_STYMPHALIAN_BIRD.get(), FeatureConfiguration.NONE));
        context.register(SPAWN_WANDERING_CYCLOPS, new ConfiguredFeature<>(IafWorldRegistry.SPAWN_WANDERING_CYCLOPS.get(), FeatureConfiguration.NONE));

        RuleTest stoneOreRule = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
        RuleTest deepslateOreRule = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);
        List<OreConfiguration.TargetBlockState> silverOreConfiguration = List.of(OreConfiguration.target(stoneOreRule, IafBlockRegistry.SILVER_ORE.get().defaultBlockState()), OreConfiguration.target(deepslateOreRule, IafBlockRegistry.DEEPSLATE_SILVER_ORE.get().defaultBlockState()));

        context.register(SILVER_ORE, new ConfiguredFeature<>(Feature.ORE,new OreConfiguration(silverOreConfiguration, 4)));

        //TODO: Sapphires should only generate for ice dragon stuff
        context.register(SAPPHIRE_ORE, new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES), IafBlockRegistry.SAPPHIRE_ORE.get().defaultBlockState(), 4, 0.5f)));
        //TODO: Look at VegetationFeatures.java
        context.register(FIRE_LILY, new ConfiguredFeature(Feature.FLOWER, flowerConf.apply(IafBlockRegistry.FIRE_LILY.get())));
        context.register(FROST_LILY, new ConfiguredFeature(Feature.FLOWER, flowerConf.apply(IafBlockRegistry.FROST_LILY.get())));
        context.register(LIGHTNING_LILY, new ConfiguredFeature(Feature.FLOWER, flowerConf.apply(IafBlockRegistry.LIGHTNING_LILY.get())));

    }

}
