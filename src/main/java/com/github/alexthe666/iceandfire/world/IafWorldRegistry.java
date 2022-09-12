package com.github.alexthe666.iceandfire.world;

import com.github.alexthe666.citadel.config.biome.SpawnBiomeData;
import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.config.BiomeConfig;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import com.github.alexthe666.iceandfire.world.feature.*;
import com.github.alexthe666.iceandfire.world.gen.*;
import com.github.alexthe666.iceandfire.world.structure.DreadMausoleumStructure;
import com.github.alexthe666.iceandfire.world.structure.DummyPiece;
import com.github.alexthe666.iceandfire.world.structure.GorgonTempleStructure;
import com.github.alexthe666.iceandfire.world.structure.GraveyardStructure;
import com.google.common.collect.ImmutableList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.Features;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.*;
import net.minecraft.world.level.levelgen.feature.blockplacers.SimpleBlockPlacer;
import net.minecraft.world.level.levelgen.feature.configurations.*;
import net.minecraft.world.level.levelgen.feature.stateproviders.SimpleStateProvider;
import net.minecraft.world.level.storage.LevelData;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.function.Supplier;

public class IafWorldRegistry {

    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES,
        IceAndFire.MODID);
    public static final DeferredRegister<StructureFeature<?>> STRUCTURES = DeferredRegister
        .create(ForgeRegistries.STRUCTURE_FEATURES, IceAndFire.MODID);

    public static final RegistryObject<Feature<NoneFeatureConfiguration>> FIRE_DRAGON_ROOST;
    public static final RegistryObject<Feature<NoneFeatureConfiguration>> ICE_DRAGON_ROOST;
    public static final RegistryObject<Feature<NoneFeatureConfiguration>> LIGHTNING_DRAGON_ROOST;
    public static final RegistryObject<Feature<NoneFeatureConfiguration>> FIRE_DRAGON_CAVE;
    public static final RegistryObject<Feature<NoneFeatureConfiguration>> ICE_DRAGON_CAVE;
    public static final RegistryObject<Feature<NoneFeatureConfiguration>> LIGHTNING_DRAGON_CAVE;
    public static final RegistryObject<Feature<NoneFeatureConfiguration>> CYCLOPS_CAVE;
    public static final RegistryObject<Feature<NoneFeatureConfiguration>> PIXIE_VILLAGE;
    public static final RegistryObject<Feature<NoneFeatureConfiguration>> SIREN_ISLAND;
    public static final RegistryObject<Feature<NoneFeatureConfiguration>> HYDRA_CAVE;
    public static final RegistryObject<Feature<NoneFeatureConfiguration>> MYRMEX_HIVE_DESERT;
    public static final RegistryObject<Feature<NoneFeatureConfiguration>> MYRMEX_HIVE_JUNGLE;
    public static final RegistryObject<Feature<NoneFeatureConfiguration>> SPAWN_DEATH_WORM;
    public static final RegistryObject<Feature<NoneFeatureConfiguration>> SPAWN_DRAGON_SKELETON_L;
    public static final RegistryObject<Feature<NoneFeatureConfiguration>> SPAWN_DRAGON_SKELETON_F;
    public static final RegistryObject<Feature<NoneFeatureConfiguration>> SPAWN_DRAGON_SKELETON_I;
    public static final RegistryObject<Feature<NoneFeatureConfiguration>> SPAWN_HIPPOCAMPUS;
    public static final RegistryObject<Feature<NoneFeatureConfiguration>> SPAWN_SEA_SERPENT;
    public static final RegistryObject<Feature<NoneFeatureConfiguration>> SPAWN_STYMPHALIAN_BIRD;
    public static final RegistryObject<Feature<NoneFeatureConfiguration>> SPAWN_WANDERING_CYCLOPS;

    public static final RegistryObject<StructureFeature<NoneFeatureConfiguration>> MAUSOLEUM = STRUCTURES.register("mausoleum",
        () -> new DreadMausoleumStructure(NoneFeatureConfiguration.CODEC));
    public static final RegistryObject<StructureFeature<NoneFeatureConfiguration>> GORGON_TEMPLE = STRUCTURES.register("gorgon_temple",
        () -> new GorgonTempleStructure(NoneFeatureConfiguration.CODEC));
    public static final RegistryObject<StructureFeature<NoneFeatureConfiguration>> GRAVEYARD = STRUCTURES.register("graveyard",
        () -> new GraveyardStructure(NoneFeatureConfiguration.CODEC));

    public static StructurePieceType DUMMY_PIECE;
    public static ConfiguredFeature FIRE_LILY_CF;
    public static ConfiguredFeature FROST_LILY_CF;
    public static ConfiguredFeature LIGHTNING_LILY_CF;
    public static ConfiguredFeature COPPER_ORE_CF;
    public static ConfiguredFeature SILVER_ORE_CF;
    public static ConfiguredFeature SAPPHIRE_ORE_CF;
    public static ConfiguredFeature AMETHYST_ORE_CF;
    public static ConfiguredFeature FIRE_DRAGON_ROOST_CF;
    public static ConfiguredFeature ICE_DRAGON_ROOST_CF;
    public static ConfiguredFeature LIGHTNING_DRAGON_ROOST_CF;
    public static ConfiguredFeature FIRE_DRAGON_CAVE_CF;
    public static ConfiguredFeature ICE_DRAGON_CAVE_CF;
    public static ConfiguredFeature LIGHTNING_DRAGON_CAVE_CF;
    public static ConfiguredFeature CYCLOPS_CAVE_CF;
    public static ConfiguredFeature PIXIE_VILLAGE_CF;
    public static ConfiguredFeature SIREN_ISLAND_CF;
    public static ConfiguredFeature HYDRA_CAVE_CF;
    public static ConfiguredFeature MYRMEX_HIVE_DESERT_CF;
    public static ConfiguredFeature MYRMEX_HIVE_JUNGLE_CF;
    public static ConfiguredFeature SPAWN_DEATH_WORM_CF;
    public static ConfiguredFeature SPAWN_DRAGON_SKELETON_L_CF;
    public static ConfiguredFeature SPAWN_DRAGON_SKELETON_F_CF;
    public static ConfiguredFeature SPAWN_DRAGON_SKELETON_I_CF;
    public static ConfiguredFeature SPAWN_HIPPOCAMPUS_CF;
    public static ConfiguredFeature SPAWN_SEA_SERPENT_CF;
    public static ConfiguredFeature SPAWN_STYMPHALIAN_BIRD_CF;
    public static ConfiguredFeature SPAWN_WANDERING_CYCLOPS_CF;
    public static ConfiguredStructureFeature GORGON_TEMPLE_CF;
    public static ConfiguredStructureFeature MAUSOLEUM_CF;
    public static ConfiguredStructureFeature GRAVEYARD_CF;

    static {
        FIRE_DRAGON_ROOST = register("fire_dragon_roost", () -> new WorldGenFireDragonRoosts(NoneFeatureConfiguration.CODEC));
        ICE_DRAGON_ROOST = register("ice_dragon_roost", () -> new WorldGenIceDragonRoosts(NoneFeatureConfiguration.CODEC));
        LIGHTNING_DRAGON_ROOST = register("lightning_dragon_roost",
            () -> new WorldGenLightningDragonRoosts(NoneFeatureConfiguration.CODEC));
        FIRE_DRAGON_CAVE = register("fire_dragon_cave", () -> new WorldGenFireDragonCave(NoneFeatureConfiguration.CODEC));
        ICE_DRAGON_CAVE = register("ice_dragon_cave", () -> new WorldGenIceDragonCave(NoneFeatureConfiguration.CODEC));
        LIGHTNING_DRAGON_CAVE = register("lightning_dragon_cave",
            () -> new WorldGenLightningDragonCave(NoneFeatureConfiguration.CODEC));
        CYCLOPS_CAVE = register("cyclops_cave", () -> new WorldGenCyclopsCave(NoneFeatureConfiguration.CODEC));
        PIXIE_VILLAGE = register("pixie_village", () -> new WorldGenPixieVillage(NoneFeatureConfiguration.CODEC));
        SIREN_ISLAND = register("siren_island", () -> new WorldGenSirenIsland(NoneFeatureConfiguration.CODEC));
        HYDRA_CAVE = register("hydra_cave", () -> new WorldGenHydraCave(NoneFeatureConfiguration.CODEC));
        MYRMEX_HIVE_DESERT = register("myrmex_hive_desert",
            () -> new WorldGenMyrmexHive(false, false, NoneFeatureConfiguration.CODEC));
        MYRMEX_HIVE_JUNGLE = register("myrmex_hive_jungle",
            () -> new WorldGenMyrmexHive(false, true, NoneFeatureConfiguration.CODEC));

        SPAWN_DEATH_WORM = register("spawn_death_worm", () -> new SpawnDeathWorm(NoneFeatureConfiguration.CODEC));
        SPAWN_DRAGON_SKELETON_L = register("spawn_dragon_skeleton_l",
            () -> new SpawnDragonSkeleton(IafEntityRegistry.LIGHTNING_DRAGON.get(), NoneFeatureConfiguration.CODEC));
        SPAWN_DRAGON_SKELETON_F = register("spawn_dragon_skeleton_f",
            () -> new SpawnDragonSkeleton(IafEntityRegistry.FIRE_DRAGON.get(), NoneFeatureConfiguration.CODEC));
        SPAWN_DRAGON_SKELETON_I = register("spawn_dragon_skeleton_i",
            () -> new SpawnDragonSkeleton(IafEntityRegistry.ICE_DRAGON.get(), NoneFeatureConfiguration.CODEC));
        SPAWN_HIPPOCAMPUS = register("spawn_hippocampus", () -> new SpawnHippocampus(NoneFeatureConfiguration.CODEC));
        SPAWN_SEA_SERPENT = register("spawn_sea_serpent", () -> new SpawnSeaSerpent(NoneFeatureConfiguration.CODEC));
        SPAWN_STYMPHALIAN_BIRD = register("spawn_stymphalian_bird",
            () -> new SpawnStymphalianBird(NoneFeatureConfiguration.CODEC));
        SPAWN_WANDERING_CYCLOPS = register("spawn_wandering_cyclops",
            () -> new SpawnWanderingCyclops(NoneFeatureConfiguration.CODEC));
    }

    private static <C extends FeatureConfiguration, F extends Feature<C>> RegistryObject<F> register(String name,
                                                                                                     Supplier<? extends F> supplier) {
        return FEATURES.register(name, supplier);
    }

    public static void registerConfiguredFeatures() {
        // Technically we don't need the piece classes anymore but we should register
        // dummy pieces
        // under the same registry name or else player's will get logspammed by Minecraft in existing worlds.
        DUMMY_PIECE = Registry.register(Registry.STRUCTURE_PIECE, "iceandfire:gorgon_piece", DummyPiece::new);
        Registry.register(Registry.STRUCTURE_PIECE, "iceandfire:mausoleum_piece", DummyPiece::new);
        Registry.register(Registry.STRUCTURE_PIECE, "iceandfire:gorgon_piece_empty", DummyPiece::new);
        Registry.register(Registry.STRUCTURE_PIECE, "iceandfire:graveyard_piece", DummyPiece::new);

        COPPER_ORE_CF = Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, "iceandfire:copper_ore", Feature.ORE.configured(new OreConfiguration(OreConfiguration.Predicates.NATURAL_STONE, IafBlockRegistry.COPPER_ORE.defaultBlockState(), 5)).rangeUniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(128)).squared().count(5));
        SILVER_ORE_CF = Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, "iceandfire:silver_ore", Feature.ORE.configured(new OreConfiguration(OreConfiguration.Predicates.NATURAL_STONE, IafBlockRegistry.SILVER_ORE.defaultBlockState(), 8)).rangeUniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(32)).squared().count(2));
        SAPPHIRE_ORE_CF = Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, "iceandfire:sapphire_ore", Feature.REPLACE_SINGLE_BLOCK.configured(new ReplaceBlockConfiguration(Blocks.STONE.defaultBlockState(), IafBlockRegistry.SAPPHIRE_ORE.defaultBlockState())).count(UniformInt.of(3, 8)));
        AMETHYST_ORE_CF = Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, "iceandfire:amythest_ore", Feature.REPLACE_SINGLE_BLOCK.configured(new ReplaceBlockConfiguration(Blocks.STONE.defaultBlockState(), IafBlockRegistry.AMYTHEST_ORE.defaultBlockState())).count(UniformInt.of(3, 8)));
        FIRE_LILY_CF = Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, "iceandfire:fire_lily", Feature.FLOWER.configured(new RandomPatchConfiguration.GrassConfigurationBuilder(new SimpleStateProvider(IafBlockRegistry.FIRE_LILY.defaultBlockState()), new SimpleBlockPlacer()).tries(1).build()).decorated(Features.Decorators.ADD_32).decorated(Features.Decorators.HEIGHTMAP_SQUARE));
        FROST_LILY_CF = Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, "iceandfire:frost_lily", Feature.FLOWER.configured(new RandomPatchConfiguration.GrassConfigurationBuilder(new SimpleStateProvider(IafBlockRegistry.FROST_LILY.defaultBlockState()), new SimpleBlockPlacer()).tries(1).build()).decorated(Features.Decorators.ADD_32).decorated(Features.Decorators.HEIGHTMAP_SQUARE));
        LIGHTNING_LILY_CF = Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, "iceandfire:lightning_lily", Feature.FLOWER.configured(new RandomPatchConfiguration.GrassConfigurationBuilder(new SimpleStateProvider(IafBlockRegistry.LIGHTNING_LILY.defaultBlockState()), new SimpleBlockPlacer()).tries(1).build()).decorated(Features.Decorators.ADD_32).decorated(Features.Decorators.HEIGHTMAP_SQUARE));
        FIRE_DRAGON_ROOST_CF = Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, "iceandfire:fire_dragon_roost",
            FIRE_DRAGON_ROOST.get().configured(FeatureConfiguration.NONE));
        ICE_DRAGON_ROOST_CF = Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, "iceandfire:ice_dragon_roost",
            ICE_DRAGON_ROOST.get().configured(FeatureConfiguration.NONE));
        LIGHTNING_DRAGON_ROOST_CF = Registry.register(BuiltinRegistries.CONFIGURED_FEATURE,
            "iceandfire:lightning_dragon_roost",
            LIGHTNING_DRAGON_ROOST.get().configured(FeatureConfiguration.NONE));
        FIRE_DRAGON_CAVE_CF = Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, "iceandfire:fire_dragon_cave",
            FIRE_DRAGON_CAVE.get().configured(FeatureConfiguration.NONE));
        ICE_DRAGON_CAVE_CF = Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, "iceandfire:ice_dragon_cave",
            ICE_DRAGON_CAVE.get().configured(FeatureConfiguration.NONE));
        LIGHTNING_DRAGON_CAVE_CF = Registry.register(BuiltinRegistries.CONFIGURED_FEATURE,
            "iceandfire:lightning_dragon_cave",
            LIGHTNING_DRAGON_CAVE.get().configured(FeatureConfiguration.NONE));
        CYCLOPS_CAVE_CF = Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, "iceandfire:cyclops_cave",
            CYCLOPS_CAVE.get().configured(FeatureConfiguration.NONE));
        PIXIE_VILLAGE_CF = Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, "iceandfire:pixie_village",
            PIXIE_VILLAGE.get().configured(FeatureConfiguration.NONE));
        SIREN_ISLAND_CF = Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, "iceandfire:siren_island",
            SIREN_ISLAND.get().configured(FeatureConfiguration.NONE));
        HYDRA_CAVE_CF = Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, "iceandfire:hydra_cave",
            HYDRA_CAVE.get().configured(FeatureConfiguration.NONE));
        MYRMEX_HIVE_DESERT_CF = Registry.register(BuiltinRegistries.CONFIGURED_FEATURE,
            "iceandfire:myrmex_hive_desert",
            MYRMEX_HIVE_DESERT.get().configured(FeatureConfiguration.NONE));
        MYRMEX_HIVE_JUNGLE_CF = Registry.register(BuiltinRegistries.CONFIGURED_FEATURE,
            "iceandfire:myrmex_hive_jungle",
            MYRMEX_HIVE_JUNGLE.get().configured(FeatureConfiguration.NONE));

        SPAWN_DEATH_WORM_CF = Registry.register(BuiltinRegistries.CONFIGURED_FEATURE,
            "iceandfire:spawn_death_worm_misc",
            SPAWN_DEATH_WORM.get().configured(FeatureConfiguration.NONE));
        SPAWN_DRAGON_SKELETON_L_CF = Registry.register(BuiltinRegistries.CONFIGURED_FEATURE,
            "iceandfire:spawn_dragon_skeleton_l_misc",
            SPAWN_DRAGON_SKELETON_L.get().configured(FeatureConfiguration.NONE));
        SPAWN_DRAGON_SKELETON_F_CF = Registry.register(BuiltinRegistries.CONFIGURED_FEATURE,
            "iceandfire:spawn_dragon_skeleton_f_misc",
            SPAWN_DRAGON_SKELETON_F.get().configured(FeatureConfiguration.NONE));
        SPAWN_DRAGON_SKELETON_I_CF = Registry.register(BuiltinRegistries.CONFIGURED_FEATURE,
            "iceandfire:spawn_dragon_skeleton_i_misc",
            SPAWN_DRAGON_SKELETON_I.get().configured(FeatureConfiguration.NONE));
        SPAWN_HIPPOCAMPUS_CF = Registry.register(BuiltinRegistries.CONFIGURED_FEATURE,
            "iceandfire:spawn_hippocampus_misc",
            SPAWN_HIPPOCAMPUS.get().configured(FeatureConfiguration.NONE));
        SPAWN_SEA_SERPENT_CF = Registry.register(BuiltinRegistries.CONFIGURED_FEATURE,
            "iceandfire:spawn_sea_serpent_misc",
            SPAWN_SEA_SERPENT.get().configured(FeatureConfiguration.NONE));
        SPAWN_STYMPHALIAN_BIRD_CF = Registry.register(BuiltinRegistries.CONFIGURED_FEATURE,
            "iceandfire:spawn_stymphalian_bird_misc",
            SPAWN_STYMPHALIAN_BIRD.get().configured(FeatureConfiguration.NONE));
        SPAWN_WANDERING_CYCLOPS_CF = Registry.register(BuiltinRegistries.CONFIGURED_FEATURE,
            "iceandfire:spawn_wandering_cyclops_misc",
            SPAWN_WANDERING_CYCLOPS.get().configured(FeatureConfiguration.NONE));
    }

    public static void registerStructureConfiguredFeatures() {
        StructureFeature.NOISE_AFFECTING_FEATURES = ImmutableList.<StructureFeature<?>>builder().addAll(StructureFeature.NOISE_AFFECTING_FEATURES)
            .add(GORGON_TEMPLE.get(), MAUSOLEUM.get(), GRAVEYARD.get()).build();

        GORGON_TEMPLE_CF = Registry.register(BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE,
            "iceandfire:gorgon_temple", GORGON_TEMPLE.get().configured(FeatureConfiguration.NONE));
        MAUSOLEUM_CF = Registry.register(BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE, "iceandfire:mausoleum",
            MAUSOLEUM.get().configured(FeatureConfiguration.NONE));
        GRAVEYARD_CF = Registry.register(BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE, "iceandfire:graveyard",
            GRAVEYARD.get().configured(FeatureConfiguration.NONE));

        addStructureSeperation(NoiseGeneratorSettings.OVERWORLD, GORGON_TEMPLE.get(), new StructureFeatureConfiguration(
            Math.max(IafConfig.spawnGorgonsChance, 2), Math.max(IafConfig.spawnGorgonsChance / 2, 1), 342226450));
        addStructureSeperation(NoiseGeneratorSettings.OVERWORLD, MAUSOLEUM.get(),
            new StructureFeatureConfiguration(Math.max(IafConfig.generateMausoleumChance, 2),
                Math.max(IafConfig.generateMausoleumChance / 2, 1), 342226451));
        addStructureSeperation(NoiseGeneratorSettings.OVERWORLD, GRAVEYARD.get(),
            new StructureFeatureConfiguration(Math.max(IafConfig.generateGraveyardChance * 3, 2),
                Math.max(IafConfig.generateGraveyardChance * 3 / 2, 1), 342226440));

        putStructureOnAList(GORGON_TEMPLE);
        putStructureOnAList(MAUSOLEUM);
        putStructureOnAList(GRAVEYARD);
    }

    public static void addStructureSeperation(ResourceKey<NoiseGeneratorSettings> preset, StructureFeature<?> structure,
                                              StructureFeatureConfiguration settings) {
        BuiltinRegistries.NOISE_GENERATOR_SETTINGS.get(preset).structureSettings().structureConfig().put(structure, settings);
    }

    public static <F extends StructureFeature<?>> void putStructureOnAList(RegistryObject<F> structure) {
        StructureFeature.STRUCTURES_REGISTRY.put(structure.getId().toString(), structure.get());
    }


    public static void setup() {
    }

    public static boolean isFarEnoughFromSpawn(LevelAccessor world, BlockPos pos) {
        LevelData spawnPoint = world.getLevelData();
        BlockPos spawnRelative = new BlockPos(spawnPoint.getXSpawn(), pos.getY(), spawnPoint.getYSpawn());

        boolean spawnCheck = !spawnRelative.closerThan(pos, IafConfig.dangerousWorldGenDistanceLimit);
        return spawnCheck;
    }

    public static boolean isDimensionListedForFeatures(ServerLevelAccessor world) {
        ResourceLocation name = world.getLevel().dimension().location();
        if (name == null) {
            return false;
        }
        if (IafConfig.useDimensionBlackList) {
            for (String blacklisted : IafConfig.featureBlacklistedDimensions) {
                if (name.toString().equals(blacklisted)) {
                    return false;
                }
            }
            return true;
        } else {
            for (String whitelist : IafConfig.featureWhitelistedDimensions) {
                if (name.toString().equals(whitelist)) {
                    return true;
                }
            }
            return false;
        }
    }

    public static boolean isDimensionListedForDragons(ServerLevelAccessor world) {
        ResourceLocation name = world.getLevel().dimension().location();
        if (name == null) {
            return false;
        }
        if (IafConfig.useDimensionBlackList) {
            for (String blacklisted : IafConfig.dragonBlacklistedDimensions) {
                if (name.toString().equals(blacklisted)) {
                    return false;
                }
            }
            return true;
        } else {
            for (String whitelist : IafConfig.dragonWhitelistedDimensions) {
                if (name.toString().equals(whitelist)) {
                    return true;
                }
            }
            return false;
        }
    }

    public static boolean isDimensionListedForMobs(ServerLevelAccessor world) {
        ResourceLocation name = world.getLevel().dimension().location();
        if (name == null) {
            return false;
        }
        if (IafConfig.useDimensionBlackList) {
            for (String blacklisted : IafConfig.mobBlacklistedDimensions) {
                if (name.toString().equals(blacklisted)) {
                    return false;
                }
            }
            return true;
        } else {
            for (String whitelist : IafConfig.mobWhitelistedDimensions) {
                if (name.toString().equals(whitelist)) {
                    return true;
                }
            }
            return false;
        }
    }

    public static boolean isFarEnoughFromDangerousGen(ServerLevelAccessor world, BlockPos pos) {
        boolean canGen = true;
        IafWorldData data = IafWorldData.get(world.getLevel());
        if (data != null) {
            BlockPos last = data.lastGeneratedDangerousStructure;
            canGen = last.distSqr(pos) > IafConfig.dangerousWorldGenSeparationLimit * IafConfig.dangerousWorldGenSeparationLimit;
            if (canGen) {
                data.setLastGeneratedDangerousStructure(pos);
            }

        }
        return canGen;
    }

    public static HashMap<String, Boolean> LOADED_FEATURES;
    static {
    	LOADED_FEATURES = new HashMap<String, Boolean>();
    	LOADED_FEATURES.put("FIRE_LILY_CF", false);
    	LOADED_FEATURES.put("FROST_LILY_CF", false);
    	LOADED_FEATURES.put("LIGHTNING_LILY_CF", false);
    	LOADED_FEATURES.put("COPPER_ORE_CF", false);
    	LOADED_FEATURES.put("SILVER_ORE_CF", false);
    	LOADED_FEATURES.put("SAPPHIRE_ORE_CF", false);
    	LOADED_FEATURES.put("AMETHYST_ORE_CF", false);
    	LOADED_FEATURES.put("FIRE_DRAGON_ROOST_CF", false);
    	LOADED_FEATURES.put("ICE_DRAGON_ROOST_CF", false);
    	LOADED_FEATURES.put("LIGHTNING_DRAGON_ROOST_CF", false);
    	LOADED_FEATURES.put("FIRE_DRAGON_CAVE_CF", false);
    	LOADED_FEATURES.put("ICE_DRAGON_CAVE_CF", false);
    	LOADED_FEATURES.put("LIGHTNING_DRAGON_CAVE_CF", false);
    	LOADED_FEATURES.put("CYCLOPS_CAVE_CF", false);
    	LOADED_FEATURES.put("PIXIE_VILLAGE_CF", false);
    	LOADED_FEATURES.put("SIREN_ISLAND_CF", false);
    	LOADED_FEATURES.put("HYDRA_CAVE_CF", false);
    	LOADED_FEATURES.put("MYRMEX_HIVE_DESERT_CF", false);
    	LOADED_FEATURES.put("MYRMEX_HIVE_JUNGLE_CF", false);
    	LOADED_FEATURES.put("SPAWN_DEATH_WORM_CF", false);
    	LOADED_FEATURES.put("SPAWN_DRAGON_SKELETON_L_CF", false);
    	LOADED_FEATURES.put("SPAWN_DRAGON_SKELETON_F_CF", false);
    	LOADED_FEATURES.put("SPAWN_DRAGON_SKELETON_I_CF", false);
    	LOADED_FEATURES.put("SPAWN_HIPPOCAMPUS_CF", false);
    	LOADED_FEATURES.put("SPAWN_SEA_SERPENT_CF", false);
    	LOADED_FEATURES.put("SPAWN_STYMPHALIAN_BIRD_CF", false);
    	LOADED_FEATURES.put("SPAWN_WANDERING_CYCLOPS_CF", false);
    	LOADED_FEATURES.put("GORGON_TEMPLE_CF", false);
    	LOADED_FEATURES.put("MAUSOLEUM_CF", false);
    	LOADED_FEATURES.put("GRAVEYARD_CF", false);
    }
    public static void onBiomesLoad(BiomeLoadingEvent event) {
    	Biome biome = ForgeRegistries.BIOMES.getValue(event.getName());

    	if (safelyTestBiome(BiomeConfig.fireLilyBiomes, biome)) {
            event.getGeneration().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, FIRE_LILY_CF);
            LOADED_FEATURES.put("FIRE_LILY_CF", true);
        }
    	if (safelyTestBiome(BiomeConfig.lightningLilyBiomes, biome)) {
            event.getGeneration().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, LIGHTNING_LILY_CF);
            LOADED_FEATURES.put("LIGHTNING_LILY_CF", true);
        }
    	if (safelyTestBiome(BiomeConfig.iceLilyBiomes, biome)) {
            event.getGeneration().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, FROST_LILY_CF);
            LOADED_FEATURES.put("FROST_LILY_CF", true);
        }
    	if (safelyTestBiome(BiomeConfig.oreGenBiomes, biome)) {
            if (IafConfig.generateSilverOre) {
                event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, SILVER_ORE_CF);
                LOADED_FEATURES.put("SILVER_ORE_CF", true);
            }
            if (IafConfig.generateCopperOre) {
                event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, COPPER_ORE_CF);
                LOADED_FEATURES.put("COPPER_ORE_CF", true);
            }
        }
        if (IafConfig.generateSapphireOre && safelyTestBiome(BiomeConfig.sapphireBiomes, biome)) {
            event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, SAPPHIRE_ORE_CF);
            LOADED_FEATURES.put("SAPPHIRE_ORE_CF", true);
        }
        if (IafConfig.generateAmythestOre && safelyTestBiome(BiomeConfig.amethystBiomes, biome)) {
            event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, AMETHYST_ORE_CF);
            LOADED_FEATURES.put("AMETHYST_ORE_CF", true);
        }

        if (safelyTestBiome(BiomeConfig.fireDragonBiomes, biome)) {
            if (IafConfig.generateDragonRoosts) {
                event.getGeneration().addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, FIRE_DRAGON_ROOST_CF);
                LOADED_FEATURES.put("FIRE_DRAGON_ROOST_CF", true);
            }
            if (IafConfig.generateDragonDens) {
                event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_STRUCTURES, FIRE_DRAGON_CAVE_CF);
                LOADED_FEATURES.put("FIRE_DRAGON_CAVE_CF", true);
            }
        }
        if (safelyTestBiome(BiomeConfig.lightningDragonBiomes, biome)) {
            if (IafConfig.generateDragonRoosts) {
                event.getGeneration().addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, LIGHTNING_DRAGON_ROOST_CF);
                LOADED_FEATURES.put("LIGHTNING_DRAGON_ROOST_CF", true);
            }
            if (IafConfig.generateDragonDens) {
                event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_STRUCTURES, LIGHTNING_DRAGON_CAVE_CF);
                LOADED_FEATURES.put("LIGHTNING_DRAGON_CAVE_CF", true);
            }
        }
        if (safelyTestBiome(BiomeConfig.iceDragonBiomes, biome)) {
            if (IafConfig.generateDragonRoosts) {
                event.getGeneration().addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, ICE_DRAGON_ROOST_CF);
                LOADED_FEATURES.put("ICE_DRAGON_ROOST_CF", true);
            }
            if (IafConfig.generateDragonDens) {
                event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_STRUCTURES, ICE_DRAGON_CAVE_CF);
                LOADED_FEATURES.put("ICE_DRAGON_CAVE_CF", true);
            }
        }

        if (IafConfig.generateCyclopsCaves && safelyTestBiome(BiomeConfig.cyclopsCaveBiomes, biome)) {
            event.getGeneration().addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, CYCLOPS_CAVE_CF);
            LOADED_FEATURES.put("CYCLOPS_CAVE_CF", true);
        }
        if (IafConfig.spawnGorgons && safelyTestBiome(BiomeConfig.gorgonTempleBiomes, biome)) {
            event.getGeneration().addStructureStart(GORGON_TEMPLE_CF);
            LOADED_FEATURES.put("GORGON_TEMPLE_CF", true);
        }
        if (IafConfig.spawnPixies && safelyTestBiome(BiomeConfig.pixieBiomes, biome)) {
            event.getGeneration().addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, PIXIE_VILLAGE_CF);
        }
        if (IafConfig.generateHydraCaves && safelyTestBiome(BiomeConfig.hydraBiomes, biome)) {
            event.getGeneration().addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, HYDRA_CAVE_CF);
            LOADED_FEATURES.put("HYDRA_CAVE_CF", true);
        }
        if (IafConfig.generateMausoleums && safelyTestBiome(BiomeConfig.mausoleumBiomes, biome)) {
            event.getGeneration().addStructureStart(MAUSOLEUM_CF);
            LOADED_FEATURES.put("MAUSOLEUM_CF", true);
        }
        if (IafConfig.generateGraveyards && safelyTestBiome(BiomeConfig.graveyardBiomes, biome)) {
            event.getGeneration().addStructureStart(GRAVEYARD_CF);
            LOADED_FEATURES.put("GRAVEYARD_CF", true);
        }
        if (IafConfig.generateMyrmexColonies && safelyTestBiome(BiomeConfig.desertMyrmexBiomes, biome)) {
            event.getGeneration().addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, MYRMEX_HIVE_DESERT_CF);
            LOADED_FEATURES.put("MYRMEX_HIVE_DESERT_CF", true);
        }
        if (IafConfig.generateMyrmexColonies && safelyTestBiome(BiomeConfig.jungleMyrmexBiomes, biome)) {
            event.getGeneration().addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, MYRMEX_HIVE_JUNGLE_CF);
            LOADED_FEATURES.put("MYRMEX_HIVE_JUNGLE_CF", true);
        }
        if (IafConfig.generateSirenIslands && safelyTestBiome(BiomeConfig.sirenBiomes, biome)) {
            event.getGeneration().addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, SIREN_ISLAND_CF);
            LOADED_FEATURES.put("SIREN_ISLAND_CF", true);
        }
    	if (IafConfig.spawnDeathWorm && safelyTestBiome(BiomeConfig.deathwormBiomes, biome)) {
            event.getGeneration().addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, SPAWN_DEATH_WORM_CF);
            LOADED_FEATURES.put("SPAWN_DEATH_WORM_CF", true);
    	}
        if (IafConfig.generateWanderingCyclops && safelyTestBiome(BiomeConfig.wanderingCyclopsBiomes, biome)) {
            event.getGeneration().addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, SPAWN_WANDERING_CYCLOPS_CF);
            LOADED_FEATURES.put("SPAWN_WANDERING_CYCLOPS_CF", true);
        }
        if (IafConfig.generateDragonSkeletons) {
            if (safelyTestBiome(BiomeConfig.lightningDragonSkeletonBiomes, biome)) {
                event.getGeneration().addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, SPAWN_DRAGON_SKELETON_L_CF);
                LOADED_FEATURES.put("SPAWN_DRAGON_SKELETON_L_CF", true);
            }
            if (safelyTestBiome(BiomeConfig.fireDragonSkeletonBiomes, biome)) {
                event.getGeneration().addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, SPAWN_DRAGON_SKELETON_F_CF);
                LOADED_FEATURES.put("SPAWN_DRAGON_SKELETON_F_CF", true);
            }
            if (safelyTestBiome(BiomeConfig.iceDragonSkeletonBiomes, biome)) {
                event.getGeneration().addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, SPAWN_DRAGON_SKELETON_I_CF);
                LOADED_FEATURES.put("SPAWN_DRAGON_SKELETON_I_CF", true);
            }
        }
    	if (IafConfig.spawnHippocampus && safelyTestBiome(BiomeConfig.hippocampusBiomes, biome)) {
            event.getGeneration().addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, SPAWN_HIPPOCAMPUS_CF);
            LOADED_FEATURES.put("SPAWN_HIPPOCAMPUS_CF", true);
    	}
        if (IafConfig.spawnSeaSerpents && safelyTestBiome(BiomeConfig.seaSerpentBiomes, biome)) {
            event.getGeneration().addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, SPAWN_SEA_SERPENT_CF);
            LOADED_FEATURES.put("SPAWN_SEA_SERPENT_CF", true);
        }
        if (IafConfig.spawnStymphalianBirds && safelyTestBiome(BiomeConfig.stymphalianBiomes, biome)) {
            event.getGeneration().addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, SPAWN_STYMPHALIAN_BIRD_CF);
            LOADED_FEATURES.put("SPAWN_STYMPHALIAN_BIRD_CF", true);
        }
    }

    private static boolean safelyTestBiome(Pair<String, SpawnBiomeData> entry, Biome biome){
        try{
            return BiomeConfig.test(entry, biome);
        }catch (Exception e){
            return false;
        }
    }
}
