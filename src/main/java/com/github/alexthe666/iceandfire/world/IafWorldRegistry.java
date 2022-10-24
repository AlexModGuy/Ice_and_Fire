package com.github.alexthe666.iceandfire.world;

import com.github.alexthe666.citadel.config.biome.CitadelBiomeDefinitions;
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
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.StructureFeatures;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeResolver;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.WorldGenerationContext;
import net.minecraft.world.level.levelgen.feature.*;
import net.minecraft.world.level.levelgen.feature.configurations.*;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.SimpleStateProvider;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType.StructureTemplateType;
import net.minecraft.world.level.storage.LevelData;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.tuple.Pair;

import java.sql.Struct;
import java.util.HashMap;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.github.alexthe666.iceandfire.block.IafBlockRegistry.*;
import static com.github.alexthe666.iceandfire.block.IafBlockRegistry.COPPER_ORE;
import static net.minecraft.world.level.block.Blocks.*;
import static net.minecraft.world.level.levelgen.VerticalAnchor.absolute;
import static net.minecraft.world.level.levelgen.placement.InSquarePlacement.*;

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

    //public static final RegistryObject<StructureFeature<JigsawConfiguration>> MAUSOLEUM =
    //        STRUCTURES.register("mausoleum", DreadMausoleumStructure::new);
    //public static final RegistryObject<StructureFeature<NoneFeatureConfiguration>> GORGON_TEMPLE = STRUCTURES.register("gorgon_temple",
    //    () -> new GorgonTempleStructure(NoneFeatureConfiguration.CODEC));
    //public static final RegistryObject<StructureFeature<NoneFeatureConfiguration>> GRAVEYARD = STRUCTURES.register("graveyard",
    //    () -> new GraveyardStructure(NoneFeatureConfiguration.CODEC));

    public static final DeferredRegister<StructureFeature<?>> DEFERRED_REGISTRY_STRUCTURE = DeferredRegister.create(
            ForgeRegistries.STRUCTURE_FEATURES,
            IceAndFire.MODID
    );

    public static final RegistryObject<StructureFeature<JigsawConfiguration>> GORGON_TEMPLE = DEFERRED_REGISTRY_STRUCTURE.register("gorgon_temple", GorgonTempleStructure::new);
    public static final RegistryObject<StructureFeature<JigsawConfiguration>> MAUSOLEUM = DEFERRED_REGISTRY_STRUCTURE.register("mausoleum", DreadMausoleumStructure::new);
    public static final RegistryObject<StructureFeature<JigsawConfiguration>> GRAVEYARD = DEFERRED_REGISTRY_STRUCTURE.register("graveyard", GraveyardStructure::new);

    public static StructurePieceType DUMMY_PIECE;
    public static Holder<PlacedFeature> FIRE_LILY_CF;
    public static Holder<PlacedFeature> FROST_LILY_CF;
    public static Holder<PlacedFeature> LIGHTNING_LILY_CF;
    public static Holder<PlacedFeature> COPPER_ORE_CF;
    public static Holder<PlacedFeature> SILVER_ORE_CF;
    public static Holder<PlacedFeature> SAPPHIRE_ORE_CF;
    public static Holder<PlacedFeature> AMETHYST_ORE_CF;
    public static Holder<PlacedFeature> FIRE_DRAGON_ROOST_CF;
    public static Holder<PlacedFeature> ICE_DRAGON_ROOST_CF;
    public static Holder<PlacedFeature> LIGHTNING_DRAGON_ROOST_CF;
    public static Holder<PlacedFeature> FIRE_DRAGON_CAVE_CF;
    public static Holder<PlacedFeature> ICE_DRAGON_CAVE_CF;
    public static Holder<PlacedFeature> LIGHTNING_DRAGON_CAVE_CF;
    public static Holder<PlacedFeature> CYCLOPS_CAVE_CF;
    public static Holder<PlacedFeature> PIXIE_VILLAGE_CF;
    public static Holder<PlacedFeature> SIREN_ISLAND_CF;
    public static Holder<PlacedFeature> HYDRA_CAVE_CF;
    public static Holder<PlacedFeature> MYRMEX_HIVE_DESERT_CF;
    public static Holder<PlacedFeature> MYRMEX_HIVE_JUNGLE_CF;
    public static Holder<PlacedFeature> SPAWN_DEATH_WORM_CF;
    public static Holder<PlacedFeature> SPAWN_DRAGON_SKELETON_L_CF;
    public static Holder<PlacedFeature> SPAWN_DRAGON_SKELETON_F_CF;
    public static Holder<PlacedFeature> SPAWN_DRAGON_SKELETON_I_CF;
    public static Holder<PlacedFeature> SPAWN_HIPPOCAMPUS_CF;
    public static Holder<PlacedFeature> SPAWN_SEA_SERPENT_CF;
    public static Holder<PlacedFeature> SPAWN_STYMPHALIAN_BIRD_CF;
    public static Holder<PlacedFeature> SPAWN_WANDERING_CYCLOPS_CF;
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

    private static HeightRangePlacement maxHeight(int max) {
        return HeightRangePlacement.uniform(WorldGenerationContext::getMinGenY, absolute(max));
    }

    private static HeightRangePlacement minMaxHeight(int min, int max) {
        return HeightRangePlacement.uniform(absolute(min), absolute(max));
    }

    private static CountPlacement count(int count) {
        return CountPlacement.of(count);
    }

    private static <C extends FeatureConfiguration, F extends Feature<C>> Holder<PlacedFeature> register(String registerName, ConfiguredFeature<C, F> feature, PlacementModifier... modifiers) {
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new ResourceLocation(registerName), feature);
        return Holder.direct(PlacementUtils.register(registerName, Holder.direct(feature), modifiers).value());
    }

    public static void registerConfiguredFeatures() {
        // Technically we don't need the piece classes anymore but we should register
        // dummy pieces
        // under the same registry name or else player's will get logspammed by Minecraft in existing worlds.
        DUMMY_PIECE = Registry.register(Registry.STRUCTURE_PIECE, "iceandfire:gorgon_piece", (StructureTemplateType) DummyPiece::new);
        Registry.register(Registry.STRUCTURE_PIECE, "iceandfire:mausoleum_piece", (StructureTemplateType) DummyPiece::new);
        Registry.register(Registry.STRUCTURE_PIECE, "iceandfire:gorgon_piece_empty", (StructureTemplateType) DummyPiece::new);
        Registry.register(Registry.STRUCTURE_PIECE, "iceandfire:graveyard_piece", (StructureTemplateType) DummyPiece::new);

        COPPER_ORE_CF = register("iceandfire:copper_ore",
                new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(OreFeatures.NATURAL_STONE, COPPER_ORE.defaultBlockState(), 8)),
                CountPlacement.of(2), maxHeight(128), spread());

        SILVER_ORE_CF = register("iceandfire:silver_ore",
                new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(OreFeatures.NATURAL_STONE, SILVER_ORE.defaultBlockState(), 8)),
                CountPlacement.of(2), maxHeight(32), spread()
        );

        SAPPHIRE_ORE_CF = register("iceandfire:sapphire_ore",
                new ConfiguredFeature<>(Feature.REPLACE_SINGLE_BLOCK, new ReplaceBlockConfiguration(STONE.defaultBlockState(), SAPPHIRE_ORE.defaultBlockState())),
                CountPlacement.of(UniformInt.of(3, 8))
        );

        AMETHYST_ORE_CF = register("%s:amethyst_ore".formatted(IceAndFire.MODID),
                new ConfiguredFeature<>(Feature.REPLACE_SINGLE_BLOCK, new ReplaceBlockConfiguration(STONE.defaultBlockState(), AMYTHEST_ORE.defaultBlockState())),
                CountPlacement.of(UniformInt.of(3, 8))
        );

        Function<Block, RandomPatchConfiguration> flowerConf = (block) -> FeatureUtils.simpleRandomPatchConfiguration(1, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(block.defaultBlockState().getBlock()))));

        FIRE_LILY_CF = register("%s:fire_lily".formatted(IceAndFire.MODID),
                new ConfiguredFeature<>(Feature.FLOWER, flowerConf.apply(FIRE_LILY)),
                PlacementUtils.HEIGHTMAP);

        FROST_LILY_CF = register("%s:fire_lily".formatted(IceAndFire.MODID),
                new ConfiguredFeature<>(Feature.FLOWER, flowerConf.apply(FROST_LILY)),
                PlacementUtils.HEIGHTMAP);

        LIGHTNING_LILY_CF = register("%s:lightning_lily".formatted(IceAndFire.MODID),
                new ConfiguredFeature<>(Feature.FLOWER, flowerConf.apply(LIGHTNING_LILY)),
                PlacementUtils.HEIGHTMAP);

        BiFunction<String, Feature, Holder<PlacedFeature>> registerSimple = (name, feat) -> {
            return register("%s:%s".formatted(IceAndFire.MODID, name), new ConfiguredFeature<>(feat, FeatureConfiguration.NONE));
        };

        FIRE_DRAGON_ROOST_CF = registerSimple.apply("fire_dragon_roost", FIRE_DRAGON_ROOST.get());
        ICE_DRAGON_ROOST_CF = registerSimple.apply("ice_dragon_roost", ICE_DRAGON_ROOST.get());
        LIGHTNING_DRAGON_ROOST_CF = registerSimple.apply("ice_dragon_roost", ICE_DRAGON_ROOST.get());
        FIRE_DRAGON_CAVE_CF = registerSimple.apply("fire_dragon_cave", FIRE_DRAGON_CAVE.get());
        ICE_DRAGON_CAVE_CF = registerSimple.apply("ice_dragon_cave", ICE_DRAGON_CAVE.get());
        LIGHTNING_DRAGON_CAVE_CF = registerSimple.apply("lightning_dragon_cave", LIGHTNING_DRAGON_CAVE.get());

        CYCLOPS_CAVE_CF = registerSimple.apply("cyclops_cave", CYCLOPS_CAVE.get());
        PIXIE_VILLAGE_CF = registerSimple.apply("pixie_village", PIXIE_VILLAGE.get());
        SIREN_ISLAND_CF = registerSimple.apply("siren_island", SIREN_ISLAND.get());
        PIXIE_VILLAGE_CF = registerSimple.apply("pixie_village", PIXIE_VILLAGE.get());
        SIREN_ISLAND_CF = registerSimple.apply("siren_island", SIREN_ISLAND.get());
        HYDRA_CAVE_CF = registerSimple.apply("hydra_cave", HYDRA_CAVE.get());
        MYRMEX_HIVE_DESERT_CF = registerSimple.apply("myrmex_hive_deser", MYRMEX_HIVE_DESERT.get());
        MYRMEX_HIVE_JUNGLE_CF = registerSimple.apply("myrmex_hive_jungl", MYRMEX_HIVE_JUNGLE.get());
        SPAWN_DEATH_WORM_CF = registerSimple.apply("spawn_death_worm_mis", SPAWN_DEATH_WORM.get());
        SPAWN_DRAGON_SKELETON_L_CF = registerSimple.apply("spawn_dragon_skeleton_l_misc", SPAWN_DRAGON_SKELETON_L.get());
        SPAWN_DRAGON_SKELETON_F_CF = registerSimple.apply("spawn_dragon_skeleton_f_misc", SPAWN_DRAGON_SKELETON_F.get());
        SPAWN_DRAGON_SKELETON_I_CF = registerSimple.apply("spawn_dragon_skeleton_i_misc", SPAWN_DRAGON_SKELETON_I.get());
        SPAWN_HIPPOCAMPUS_CF = registerSimple.apply("spawn_hippocampus_misc", SPAWN_HIPPOCAMPUS.get());
        SPAWN_SEA_SERPENT_CF = registerSimple.apply("spawn_sea_serpent_misc", SPAWN_SEA_SERPENT.get());
        SPAWN_STYMPHALIAN_BIRD_CF = registerSimple.apply("spawn_stymphalian_bird_misc", SPAWN_STYMPHALIAN_BIRD.get());
        SPAWN_WANDERING_CYCLOPS_CF = registerSimple.apply("spawn_wandering_cyclops_misc", SPAWN_WANDERING_CYCLOPS.get());
    }


    public static void registerStructureConfiguredFeatures() {
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
        //if (IafConfig.spawnGorgons && safelyTestBiome(BiomeConfig.gorgonTempleBiomes, biome)) {
        //    event.getGeneration().addStructureStart(GORGON_TEMPLE_CF);
        //    LOADED_FEATURES.put("GORGON_TEMPLE_CF", true);
        //}
        if (IafConfig.spawnPixies && safelyTestBiome(BiomeConfig.pixieBiomes, biome)) {
            event.getGeneration().addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, PIXIE_VILLAGE_CF);
        }
        if (IafConfig.generateHydraCaves && safelyTestBiome(BiomeConfig.hydraBiomes, biome)) {
            event.getGeneration().addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, HYDRA_CAVE_CF);
            LOADED_FEATURES.put("HYDRA_CAVE_CF", true);
        }
        // TODO: morguldir: move this to data files
        //if (IafConfig.generateMausoleums && safelyTestBiome(BiomeConfig.mausoleumBiomes, biome)) {
        //    event.getGeneration().addStructureStart(MAUSOLEUM_CF);
        //    LOADED_FEATURES.put("MAUSOLEUM_CF", true);
        //}
        //if (IafConfig.generateGraveyards && safelyTestBiome(BiomeConfig.graveyardBiomes, biome)) {
        //    event.getGeneration().addStructureStart(GRAVEYARD_CF);
        //    LOADED_FEATURES.put("GRAVEYARD_CF", true);
        //}
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
