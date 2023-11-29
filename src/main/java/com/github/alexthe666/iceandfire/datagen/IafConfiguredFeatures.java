package com.github.alexthe666.iceandfire.datagen;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.world.IafWorldRegistry;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
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
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.function.Function;

public final class IafConfiguredFeatures {
    public static final DeferredRegister<ConfiguredFeature<?, ?>> CONFIGURED_FEATURES = DeferredRegister.create(Registry.CONFIGURED_FEATURE_REGISTRY, IceAndFire.MODID);

    public static final RegistryObject<ConfiguredFeature<?, ?>> FIRE_DRAGON_ROOST = CONFIGURED_FEATURES.register("fire_dragon_roost", () -> new ConfiguredFeature<>(IafWorldRegistry.FIRE_DRAGON_ROOST.get(), FeatureConfiguration.NONE));
    public static final RegistryObject<ConfiguredFeature<?, ?>> ICE_DRAGON_ROOST = CONFIGURED_FEATURES.register("ice_dragon_roost", () -> new ConfiguredFeature<>(IafWorldRegistry.ICE_DRAGON_ROOST.get(), FeatureConfiguration.NONE));
    public static final RegistryObject<ConfiguredFeature<?, ?>> LIGHTNING_DRAGON_ROOST = CONFIGURED_FEATURES.register("lightning_dragon_roost", () -> new ConfiguredFeature<>(IafWorldRegistry.LIGHTNING_DRAGON_ROOST.get(), FeatureConfiguration.NONE));
    public static final RegistryObject<ConfiguredFeature<?, ?>> FIRE_DRAGON_CAVE = CONFIGURED_FEATURES.register("fire_dragon_cave", () -> new ConfiguredFeature<>(IafWorldRegistry.FIRE_DRAGON_CAVE.get(), FeatureConfiguration.NONE));
    public static final RegistryObject<ConfiguredFeature<?, ?>> ICE_DRAGON_CAVE = CONFIGURED_FEATURES.register("ice_dragon_cave", () -> new ConfiguredFeature<>(IafWorldRegistry.ICE_DRAGON_CAVE.get(), FeatureConfiguration.NONE));
    public static final RegistryObject<ConfiguredFeature<?, ?>> LIGHTNING_DRAGON_CAVE = CONFIGURED_FEATURES.register("lightning_dragon_cave", () -> new ConfiguredFeature<>(IafWorldRegistry.LIGHTNING_DRAGON_CAVE.get(), FeatureConfiguration.NONE));
    //TODO: Should be a structure
    public static final RegistryObject<ConfiguredFeature<?, ?>> CYCLOPS_CAVE = CONFIGURED_FEATURES.register("cyclops_cave", () -> new ConfiguredFeature<>(IafWorldRegistry.CYCLOPS_CAVE.get(), FeatureConfiguration.NONE));
    public static final RegistryObject<ConfiguredFeature<?, ?>> PIXIE_VILLAGE = CONFIGURED_FEATURES.register("pixie_village", () -> new ConfiguredFeature<>(IafWorldRegistry.PIXIE_VILLAGE.get(), FeatureConfiguration.NONE));
    public static final RegistryObject<ConfiguredFeature<?, ?>> SIREN_ISLAND = CONFIGURED_FEATURES.register("siren_island", () -> new ConfiguredFeature<>(IafWorldRegistry.SIREN_ISLAND.get(), FeatureConfiguration.NONE));
    public static final RegistryObject<ConfiguredFeature<?, ?>> HYDRA_CAVE = CONFIGURED_FEATURES.register("hydra_cave", () -> new ConfiguredFeature<>(IafWorldRegistry.HYDRA_CAVE.get(), FeatureConfiguration.NONE));
    public static final RegistryObject<ConfiguredFeature<?, ?>> MYRMEX_HIVE_DESERT = CONFIGURED_FEATURES.register("myrmex_hive_desert", () -> new ConfiguredFeature<>(IafWorldRegistry.MYRMEX_HIVE_DESERT.get(), FeatureConfiguration.NONE));
    public static final RegistryObject<ConfiguredFeature<?, ?>> MYRMEX_HIVE_JUNGLE = CONFIGURED_FEATURES.register("myrmex_hive_jungle", () -> new ConfiguredFeature<>(IafWorldRegistry.MYRMEX_HIVE_JUNGLE.get(), FeatureConfiguration.NONE));
    public static final RegistryObject<ConfiguredFeature<?, ?>> SPAWN_DEATH_WORM = CONFIGURED_FEATURES.register("spawn_death_worm", () -> new ConfiguredFeature<>(IafWorldRegistry.SPAWN_DEATH_WORM.get(), FeatureConfiguration.NONE));
    public static final RegistryObject<ConfiguredFeature<?, ?>> SPAWN_DRAGON_SKELETON_L = CONFIGURED_FEATURES.register("spawn_dragon_skeleton_lightning", () -> new ConfiguredFeature<>(IafWorldRegistry.SPAWN_DRAGON_SKELETON_L.get(), FeatureConfiguration.NONE));
    public static final RegistryObject<ConfiguredFeature<?, ?>> SPAWN_DRAGON_SKELETON_F = CONFIGURED_FEATURES.register("spawn_dragon_skeleton_fire", () -> new ConfiguredFeature<>(IafWorldRegistry.SPAWN_DRAGON_SKELETON_F.get(), FeatureConfiguration.NONE));
    public static final RegistryObject<ConfiguredFeature<?, ?>> SPAWN_DRAGON_SKELETON_I = CONFIGURED_FEATURES.register("spawn_dragon_skeleton_ice", () -> new ConfiguredFeature<>(IafWorldRegistry.SPAWN_DRAGON_SKELETON_I.get(), FeatureConfiguration.NONE));
    public static final RegistryObject<ConfiguredFeature<?, ?>> SPAWN_HIPPOCAMPUS = CONFIGURED_FEATURES.register("spawn_hippocampus", () -> new ConfiguredFeature<>(IafWorldRegistry.SPAWN_HIPPOCAMPUS.get(), FeatureConfiguration.NONE));
    public static final RegistryObject<ConfiguredFeature<?, ?>> SPAWN_SEA_SERPENT = CONFIGURED_FEATURES.register("spawn_sea_serpent", () -> new ConfiguredFeature<>(IafWorldRegistry.SPAWN_SEA_SERPENT.get(), FeatureConfiguration.NONE));
    public static final RegistryObject<ConfiguredFeature<?, ?>> SPAWN_STYMPHALIAN_BIRD = CONFIGURED_FEATURES.register("spawn_stymphalian_bird", () -> new ConfiguredFeature<>(IafWorldRegistry.SPAWN_STYMPHALIAN_BIRD.get(), FeatureConfiguration.NONE));
    public static final RegistryObject<ConfiguredFeature<?, ?>> SPAWN_WANDERING_CYCLOPS = CONFIGURED_FEATURES.register("spawn_wandering_cyclops", () -> new ConfiguredFeature<>(IafWorldRegistry.SPAWN_WANDERING_CYCLOPS.get(), FeatureConfiguration.NONE));


    public static final RegistryObject<ConfiguredFeature<?, ?>> SILVER_ORE = CONFIGURED_FEATURES.register("silver_ore", () -> {
        RuleTest stoneOreRule = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
        RuleTest deepslateOreRule = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);
        List<OreConfiguration.TargetBlockState> silverOreConfiguration = List.of(OreConfiguration.target(stoneOreRule, IafBlockRegistry.SILVER_ORE.get().defaultBlockState()), OreConfiguration.target(deepslateOreRule, IafBlockRegistry.DEEPSLATE_SILVER_ORE.get().defaultBlockState()));
        return new ConfiguredFeature<>(Feature.ORE,new OreConfiguration(silverOreConfiguration, 4));
    });

    private static Function<Block, RandomPatchConfiguration> flowerConf = (block) -> FeatureUtils.simpleRandomPatchConfiguration(8, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(block.defaultBlockState().getBlock()))));

    public static final RegistryObject<ConfiguredFeature<?, ?>> SAPPHIRE_ORE = CONFIGURED_FEATURES.register("sapphire_ore", () -> new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES), IafBlockRegistry.SAPPHIRE_ORE.get().defaultBlockState(), 4, 0.5f)));
    public static final RegistryObject<ConfiguredFeature<?, ?>> FIRE_LILY = CONFIGURED_FEATURES.register("fire_lily", () -> new ConfiguredFeature<>(Feature.FLOWER, flowerConf.apply(IafBlockRegistry.FIRE_LILY.get())));
    public static final RegistryObject<ConfiguredFeature<?, ?>> FROST_LILY = CONFIGURED_FEATURES.register("frost_lily", () -> new ConfiguredFeature<>(Feature.FLOWER, flowerConf.apply(IafBlockRegistry.FROST_LILY.get())));
    public static final RegistryObject<ConfiguredFeature<?, ?>> LIGHTNING_LILY = CONFIGURED_FEATURES.register("lightning_lily", () -> new ConfiguredFeature<>(Feature.FLOWER, flowerConf.apply(IafBlockRegistry.LIGHTNING_LILY.get())));
}
