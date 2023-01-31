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
import net.minecraft.block.Blocks;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.DimensionSettings;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.blockplacer.SimpleBlockPlacer;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.placement.IPlacementConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraft.world.storage.IWorldInfo;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.function.Supplier;

public class IafWorldRegistry {

    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES,
        IceAndFire.MODID);
    public static final DeferredRegister<Structure<?>> STRUCTURES = DeferredRegister
        .create(ForgeRegistries.STRUCTURE_FEATURES, IceAndFire.MODID);

    public static final RegistryObject<Feature<NoFeatureConfig>> FIRE_DRAGON_ROOST;
    public static final RegistryObject<Feature<NoFeatureConfig>> ICE_DRAGON_ROOST;
    public static final RegistryObject<Feature<NoFeatureConfig>> LIGHTNING_DRAGON_ROOST;
    public static final RegistryObject<Feature<NoFeatureConfig>> FIRE_DRAGON_CAVE;
    public static final RegistryObject<Feature<NoFeatureConfig>> ICE_DRAGON_CAVE;
    public static final RegistryObject<Feature<NoFeatureConfig>> LIGHTNING_DRAGON_CAVE;
    public static final RegistryObject<Feature<NoFeatureConfig>> CYCLOPS_CAVE;
    public static final RegistryObject<Feature<NoFeatureConfig>> PIXIE_VILLAGE;
    public static final RegistryObject<Feature<NoFeatureConfig>> SIREN_ISLAND;
    public static final RegistryObject<Feature<NoFeatureConfig>> HYDRA_CAVE;
    public static final RegistryObject<Feature<NoFeatureConfig>> MYRMEX_HIVE_DESERT;
    public static final RegistryObject<Feature<NoFeatureConfig>> MYRMEX_HIVE_JUNGLE;
    public static final RegistryObject<Feature<NoFeatureConfig>> SPAWN_DEATH_WORM;
    public static final RegistryObject<Feature<NoFeatureConfig>> SPAWN_DRAGON_SKELETON_L;
    public static final RegistryObject<Feature<NoFeatureConfig>> SPAWN_DRAGON_SKELETON_F;
    public static final RegistryObject<Feature<NoFeatureConfig>> SPAWN_DRAGON_SKELETON_I;
    public static final RegistryObject<Feature<NoFeatureConfig>> SPAWN_HIPPOCAMPUS;
    public static final RegistryObject<Feature<NoFeatureConfig>> SPAWN_SEA_SERPENT;
    public static final RegistryObject<Feature<NoFeatureConfig>> SPAWN_STYMPHALIAN_BIRD;
    public static final RegistryObject<Feature<NoFeatureConfig>> SPAWN_WANDERING_CYCLOPS;

    public static final RegistryObject<Structure<NoFeatureConfig>> MAUSOLEUM_RO = STRUCTURES.register("mausoleum",
            () -> {
                Structure<NoFeatureConfig> mausoleum = new DreadMausoleumStructure(NoFeatureConfig.CODEC);
                MAUSOLEUM = mausoleum;
                return mausoleum;
            });
    public static final RegistryObject<Structure<NoFeatureConfig>> GORGON_TEMPLE_RO = STRUCTURES.register("gorgon_temple",
            () -> {
                Structure<NoFeatureConfig> gorgonTemple = new GorgonTempleStructure(NoFeatureConfig.CODEC);
                GORGON_TEMPLE = gorgonTemple;
                return gorgonTemple;
            });
    public static final RegistryObject<Structure<NoFeatureConfig>> GRAVEYARD_RO = STRUCTURES.register("graveyard",
            () -> {
                Structure<NoFeatureConfig> graveyard = new GraveyardStructure(NoFeatureConfig.CODEC);
                GRAVEYARD = graveyard;
                return graveyard;
            });
    @Deprecated
    // Only exists for compatibility with allthemodium since they won't update their mod
    public static Structure<NoFeatureConfig> GRAVEYARD;
    @Deprecated
    // Only exists for compatibility with allthemodium since they won't update their mod
    public static Structure<NoFeatureConfig> GORGON_TEMPLE;
    @Deprecated
    // Only exists for compatibility with allthemodium since they won't update their mod
    public static Structure<NoFeatureConfig> MAUSOLEUM;
    public static IStructurePieceType DUMMY_PIECE;
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
    public static StructureFeature GORGON_TEMPLE_CF;
    public static StructureFeature MAUSOLEUM_CF;
    public static StructureFeature GRAVEYARD_CF;

    static {
        FIRE_DRAGON_ROOST = register("fire_dragon_roost", () -> new WorldGenFireDragonRoosts(NoFeatureConfig.CODEC));
        ICE_DRAGON_ROOST = register("ice_dragon_roost", () -> new WorldGenIceDragonRoosts(NoFeatureConfig.CODEC));
        LIGHTNING_DRAGON_ROOST = register("lightning_dragon_roost",
            () -> new WorldGenLightningDragonRoosts(NoFeatureConfig.CODEC));
        FIRE_DRAGON_CAVE = register("fire_dragon_cave", () -> new WorldGenFireDragonCave(NoFeatureConfig.CODEC));
        ICE_DRAGON_CAVE = register("ice_dragon_cave", () -> new WorldGenIceDragonCave(NoFeatureConfig.CODEC));
        LIGHTNING_DRAGON_CAVE = register("lightning_dragon_cave",
            () -> new WorldGenLightningDragonCave(NoFeatureConfig.CODEC));
        CYCLOPS_CAVE = register("cyclops_cave", () -> new WorldGenCyclopsCave(NoFeatureConfig.CODEC));
        PIXIE_VILLAGE = register("pixie_village", () -> new WorldGenPixieVillage(NoFeatureConfig.CODEC));
        SIREN_ISLAND = register("siren_island", () -> new WorldGenSirenIsland(NoFeatureConfig.CODEC));
        HYDRA_CAVE = register("hydra_cave", () -> new WorldGenHydraCave(NoFeatureConfig.CODEC));
        MYRMEX_HIVE_DESERT = register("myrmex_hive_desert",
            () -> new WorldGenMyrmexHive(false, false, NoFeatureConfig.CODEC));
        MYRMEX_HIVE_JUNGLE = register("myrmex_hive_jungle",
            () -> new WorldGenMyrmexHive(false, true, NoFeatureConfig.CODEC));

        SPAWN_DEATH_WORM = register("spawn_death_worm", () -> new SpawnDeathWorm(NoFeatureConfig.CODEC));
        SPAWN_DRAGON_SKELETON_L = register("spawn_dragon_skeleton_l",
            () -> new SpawnDragonSkeleton(IafEntityRegistry.LIGHTNING_DRAGON.get(), NoFeatureConfig.CODEC));
        SPAWN_DRAGON_SKELETON_F = register("spawn_dragon_skeleton_f",
            () -> new SpawnDragonSkeleton(IafEntityRegistry.FIRE_DRAGON.get(), NoFeatureConfig.CODEC));
        SPAWN_DRAGON_SKELETON_I = register("spawn_dragon_skeleton_i",
            () -> new SpawnDragonSkeleton(IafEntityRegistry.ICE_DRAGON.get(), NoFeatureConfig.CODEC));
        SPAWN_HIPPOCAMPUS = register("spawn_hippocampus", () -> new SpawnHippocampus(NoFeatureConfig.CODEC));
        SPAWN_SEA_SERPENT = register("spawn_sea_serpent", () -> new SpawnSeaSerpent(NoFeatureConfig.CODEC));
        SPAWN_STYMPHALIAN_BIRD = register("spawn_stymphalian_bird",
            () -> new SpawnStymphalianBird(NoFeatureConfig.CODEC));
        SPAWN_WANDERING_CYCLOPS = register("spawn_wandering_cyclops",
            () -> new SpawnWanderingCyclops(NoFeatureConfig.CODEC));
    }

    private static <C extends IFeatureConfig, F extends Feature<C>> RegistryObject<F> register(String name,
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

        COPPER_ORE_CF = Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, "iceandfire:copper_ore", Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.BASE_STONE_OVERWORLD, IafBlockRegistry.COPPER_ORE.getDefaultState(), 5)).range(128).square().count(5));
        SILVER_ORE_CF = Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, "iceandfire:silver_ore", Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.BASE_STONE_OVERWORLD, IafBlockRegistry.SILVER_ORE.getDefaultState(), 8)).range(32).square().count(2));
        SAPPHIRE_ORE_CF = Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, "iceandfire:sapphire_ore", Feature.EMERALD_ORE.withConfiguration(new ReplaceBlockConfig(Blocks.STONE.getDefaultState(), IafBlockRegistry.SAPPHIRE_ORE.getDefaultState())).withPlacement(Placement.EMERALD_ORE.configure(IPlacementConfig.NO_PLACEMENT_CONFIG)));
        AMETHYST_ORE_CF = Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, "iceandfire:amythest_ore", Feature.EMERALD_ORE.withConfiguration(new ReplaceBlockConfig(Blocks.STONE.getDefaultState(), IafBlockRegistry.AMYTHEST_ORE.getDefaultState())).withPlacement(Placement.EMERALD_ORE.configure(IPlacementConfig.NO_PLACEMENT_CONFIG)));
        FIRE_LILY_CF = Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, "iceandfire:fire_lily", Feature.FLOWER.withConfiguration(new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(IafBlockRegistry.FIRE_LILY.getDefaultState()), new SimpleBlockPlacer()).tries(1).build()).withPlacement(Features.Placements.VEGETATION_PLACEMENT).withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT));
        FROST_LILY_CF = Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, "iceandfire:frost_lily", Feature.FLOWER.withConfiguration(new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(IafBlockRegistry.FROST_LILY.getDefaultState()), new SimpleBlockPlacer()).tries(1).build()).withPlacement(Features.Placements.VEGETATION_PLACEMENT).withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT));
        LIGHTNING_LILY_CF = Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, "iceandfire:lightning_lily", Feature.FLOWER.withConfiguration(new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(IafBlockRegistry.LIGHTNING_LILY.getDefaultState()), new SimpleBlockPlacer()).tries(1).build()).withPlacement(Features.Placements.VEGETATION_PLACEMENT).withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT));
        FIRE_DRAGON_ROOST_CF = Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, "iceandfire:fire_dragon_roost",
            FIRE_DRAGON_ROOST.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
        ICE_DRAGON_ROOST_CF = Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, "iceandfire:ice_dragon_roost",
            ICE_DRAGON_ROOST.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
        LIGHTNING_DRAGON_ROOST_CF = Registry.register(WorldGenRegistries.CONFIGURED_FEATURE,
            "iceandfire:lightning_dragon_roost",
            LIGHTNING_DRAGON_ROOST.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
        FIRE_DRAGON_CAVE_CF = Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, "iceandfire:fire_dragon_cave",
            FIRE_DRAGON_CAVE.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
        ICE_DRAGON_CAVE_CF = Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, "iceandfire:ice_dragon_cave",
            ICE_DRAGON_CAVE.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
        LIGHTNING_DRAGON_CAVE_CF = Registry.register(WorldGenRegistries.CONFIGURED_FEATURE,
            "iceandfire:lightning_dragon_cave",
            LIGHTNING_DRAGON_CAVE.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
        CYCLOPS_CAVE_CF = Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, "iceandfire:cyclops_cave",
            CYCLOPS_CAVE.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
        PIXIE_VILLAGE_CF = Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, "iceandfire:pixie_village",
            PIXIE_VILLAGE.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
        SIREN_ISLAND_CF = Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, "iceandfire:siren_island",
            SIREN_ISLAND.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
        HYDRA_CAVE_CF = Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, "iceandfire:hydra_cave",
            HYDRA_CAVE.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
        MYRMEX_HIVE_DESERT_CF = Registry.register(WorldGenRegistries.CONFIGURED_FEATURE,
            "iceandfire:myrmex_hive_desert",
            MYRMEX_HIVE_DESERT.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
        MYRMEX_HIVE_JUNGLE_CF = Registry.register(WorldGenRegistries.CONFIGURED_FEATURE,
            "iceandfire:myrmex_hive_jungle",
            MYRMEX_HIVE_JUNGLE.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
        
        SPAWN_DEATH_WORM_CF = Registry.register(WorldGenRegistries.CONFIGURED_FEATURE,
            "iceandfire:spawn_death_worm_misc",
            SPAWN_DEATH_WORM.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
        SPAWN_DRAGON_SKELETON_L_CF = Registry.register(WorldGenRegistries.CONFIGURED_FEATURE,
            "iceandfire:spawn_dragon_skeleton_l_misc",
            SPAWN_DRAGON_SKELETON_L.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
        SPAWN_DRAGON_SKELETON_F_CF = Registry.register(WorldGenRegistries.CONFIGURED_FEATURE,
            "iceandfire:spawn_dragon_skeleton_f_misc",
            SPAWN_DRAGON_SKELETON_F.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
        SPAWN_DRAGON_SKELETON_I_CF = Registry.register(WorldGenRegistries.CONFIGURED_FEATURE,
            "iceandfire:spawn_dragon_skeleton_i_misc",
            SPAWN_DRAGON_SKELETON_I.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
        SPAWN_HIPPOCAMPUS_CF = Registry.register(WorldGenRegistries.CONFIGURED_FEATURE,
            "iceandfire:spawn_hippocampus_misc",
            SPAWN_HIPPOCAMPUS.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
        SPAWN_SEA_SERPENT_CF = Registry.register(WorldGenRegistries.CONFIGURED_FEATURE,
            "iceandfire:spawn_sea_serpent_misc",
            SPAWN_SEA_SERPENT.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
        SPAWN_STYMPHALIAN_BIRD_CF = Registry.register(WorldGenRegistries.CONFIGURED_FEATURE,
            "iceandfire:spawn_stymphalian_bird_misc",
            SPAWN_STYMPHALIAN_BIRD.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
        SPAWN_WANDERING_CYCLOPS_CF = Registry.register(WorldGenRegistries.CONFIGURED_FEATURE,
            "iceandfire:spawn_wandering_cyclops_misc",
            SPAWN_WANDERING_CYCLOPS.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
    }

    public static void registerStructureConfiguredFeatures() {
        Structure.field_236384_t_ = ImmutableList.<Structure<?>>builder().addAll(Structure.field_236384_t_)
                .add(GORGON_TEMPLE_RO.get(), MAUSOLEUM_RO.get(), GRAVEYARD_RO.get()).build();

        GORGON_TEMPLE_CF = Registry.register(WorldGenRegistries.CONFIGURED_STRUCTURE_FEATURE,
                "iceandfire:gorgon_temple", GORGON_TEMPLE_RO.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
        MAUSOLEUM_CF = Registry.register(WorldGenRegistries.CONFIGURED_STRUCTURE_FEATURE, "iceandfire:mausoleum",
                MAUSOLEUM_RO.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
        GRAVEYARD_CF = Registry.register(WorldGenRegistries.CONFIGURED_STRUCTURE_FEATURE, "iceandfire:graveyard",
                GRAVEYARD_RO.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));

        addStructureSeperation(DimensionSettings.OVERWORLD, GORGON_TEMPLE_RO.get(), new StructureSeparationSettings(
                Math.max(IafConfig.spawnGorgonsChance, 2), Math.max(IafConfig.spawnGorgonsChance / 2, 1), 342226450));
        addStructureSeperation(DimensionSettings.OVERWORLD, MAUSOLEUM_RO.get(),
                new StructureSeparationSettings(Math.max(IafConfig.generateMausoleumChance, 2),
                        Math.max(IafConfig.generateMausoleumChance / 2, 1), 342226451));
        addStructureSeperation(DimensionSettings.OVERWORLD, GRAVEYARD_RO.get(),
                new StructureSeparationSettings(Math.max(IafConfig.generateGraveyardChance * 3, 2),
                        Math.max(IafConfig.generateGraveyardChance * 3 / 2, 1), 342226440));

        putStructureOnAList(GORGON_TEMPLE_RO);
        putStructureOnAList(MAUSOLEUM_RO);
        putStructureOnAList(GRAVEYARD_RO);
    }

    public static void addStructureSeperation(RegistryKey<DimensionSettings> preset, Structure<?> structure,
        StructureSeparationSettings settings) {
        WorldGenRegistries.NOISE_SETTINGS.getValueForKey(preset).getStructures().func_236195_a_().put(structure, settings);
    }

    public static <F extends Structure<?>> void putStructureOnAList(RegistryObject<F> structure) {
        Structure.NAME_STRUCTURE_BIMAP.put(structure.getId().toString(), structure.get());
    }


    public static void setup() {
    }

    public static boolean isFarEnoughFromSpawn(IWorld world, BlockPos pos) {
        IWorldInfo spawnPoint = world.getWorldInfo();
        BlockPos spawnRelative = new BlockPos(spawnPoint.getSpawnX(), pos.getY(), spawnPoint.getSpawnY());

        boolean spawnCheck = !spawnRelative.withinDistance(pos, IafConfig.dangerousWorldGenDistanceLimit);
        return spawnCheck;
    }

    public static boolean isDimensionListedForFeatures(IServerWorld world) {
        ResourceLocation name = world.getWorld().getDimensionKey().getLocation();
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

    public static boolean isDimensionListedForDragons(IServerWorld world) {
        ResourceLocation name = world.getWorld().getDimensionKey().getLocation();
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

    public static boolean isDimensionListedForMobs(IServerWorld world) {
        ResourceLocation name = world.getWorld().getDimensionKey().getLocation();
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
    public static boolean isFarEnoughFromDangerousGen(IServerWorld world, BlockPos pos) {
        boolean canGen = true;
        IafWorldData data = IafWorldData.get(world.getWorld());
        if (data != null) {
            BlockPos last = data.lastGeneratedDangerousStructure;
            canGen = last.distanceSq(pos) > IafConfig.dangerousWorldGenSeparationLimit * IafConfig.dangerousWorldGenSeparationLimit;
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
            event.getGeneration().withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, FIRE_LILY_CF);
        	LOADED_FEATURES.put("FIRE_LILY_CF", true);
        }
    	if (safelyTestBiome(BiomeConfig.lightningLilyBiomes, biome)) {
            event.getGeneration().withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, LIGHTNING_LILY_CF);
        	LOADED_FEATURES.put("LIGHTNING_LILY_CF", true);
        }
    	if (safelyTestBiome(BiomeConfig.iceLilyBiomes, biome)) {
            event.getGeneration().withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, FROST_LILY_CF);
        	LOADED_FEATURES.put("FROST_LILY_CF", true);
        }
    	if (safelyTestBiome(BiomeConfig.oreGenBiomes, biome)) {
            if (IafConfig.generateSilverOre) {
                event.getGeneration().withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, SILVER_ORE_CF);
            	LOADED_FEATURES.put("SILVER_ORE_CF", true);
            }
            if (IafConfig.generateCopperOre) {
                event.getGeneration().withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, COPPER_ORE_CF);
            	LOADED_FEATURES.put("COPPER_ORE_CF", true);
            }
        }
        if (IafConfig.generateSapphireOre && safelyTestBiome(BiomeConfig.sapphireBiomes, biome)) {
            event.getGeneration().withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, SAPPHIRE_ORE_CF);
        	LOADED_FEATURES.put("SAPPHIRE_ORE_CF", true);
        }
        if (IafConfig.generateAmythestOre && safelyTestBiome(BiomeConfig.amethystBiomes, biome)) {
            event.getGeneration().withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, AMETHYST_ORE_CF);
        	LOADED_FEATURES.put("AMETHYST_ORE_CF", true);
        }

        if (safelyTestBiome(BiomeConfig.fireDragonBiomes, biome)) {
            if (IafConfig.generateDragonRoosts) {
                event.getGeneration().withFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, FIRE_DRAGON_ROOST_CF);
                LOADED_FEATURES.put("FIRE_DRAGON_ROOST_CF", true);
            }
            if (IafConfig.generateDragonDens) {
                event.getGeneration().withFeature(GenerationStage.Decoration.UNDERGROUND_STRUCTURES, FIRE_DRAGON_CAVE_CF);
                LOADED_FEATURES.put("FIRE_DRAGON_CAVE_CF", true);
            }
        }
        if (safelyTestBiome(BiomeConfig.lightningDragonBiomes, biome)) {
            if (IafConfig.generateDragonRoosts) {
                event.getGeneration().withFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, LIGHTNING_DRAGON_ROOST_CF);
                LOADED_FEATURES.put("LIGHTNING_DRAGON_ROOST_CF", true);
            }
            if (IafConfig.generateDragonDens) {
                event.getGeneration().withFeature(GenerationStage.Decoration.UNDERGROUND_STRUCTURES, LIGHTNING_DRAGON_CAVE_CF);
                LOADED_FEATURES.put("LIGHTNING_DRAGON_CAVE_CF", true);
            }
        }
        if (safelyTestBiome(BiomeConfig.iceDragonBiomes, biome)) {
            if (IafConfig.generateDragonRoosts) {
                event.getGeneration().withFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, ICE_DRAGON_ROOST_CF);
                LOADED_FEATURES.put("ICE_DRAGON_ROOST_CF", true);
            }
            if (IafConfig.generateDragonDens) {
                event.getGeneration().withFeature(GenerationStage.Decoration.UNDERGROUND_STRUCTURES, ICE_DRAGON_CAVE_CF);
                LOADED_FEATURES.put("ICE_DRAGON_CAVE_CF", true);
            }
        }

        if (IafConfig.generateCyclopsCaves && safelyTestBiome(BiomeConfig.cyclopsCaveBiomes, biome)) {
            event.getGeneration().withFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, CYCLOPS_CAVE_CF);
        	LOADED_FEATURES.put("CYCLOPS_CAVE_CF", true);
        }
        if (IafConfig.spawnGorgons && safelyTestBiome(BiomeConfig.gorgonTempleBiomes, biome)) {
            event.getGeneration().withStructure(GORGON_TEMPLE_CF);
        	LOADED_FEATURES.put("GORGON_TEMPLE_CF", true);
        }
        if (IafConfig.spawnPixies && safelyTestBiome(BiomeConfig.pixieBiomes, biome)) {
            event.getGeneration().withFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, PIXIE_VILLAGE_CF);
        }
        if (IafConfig.generateHydraCaves && safelyTestBiome(BiomeConfig.hydraBiomes, biome)) {
            event.getGeneration().withFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, HYDRA_CAVE_CF);
        	LOADED_FEATURES.put("HYDRA_CAVE_CF", true);
        }
        if (IafConfig.generateMausoleums && safelyTestBiome(BiomeConfig.mausoleumBiomes, biome)) {
            event.getGeneration().withStructure(MAUSOLEUM_CF);
        	LOADED_FEATURES.put("MAUSOLEUM_CF", true);
        }
        if (IafConfig.generateGraveyards && safelyTestBiome(BiomeConfig.graveyardBiomes, biome)) {
            event.getGeneration().withStructure(GRAVEYARD_CF);
        	LOADED_FEATURES.put("GRAVEYARD_CF", true);
        }
        if (IafConfig.generateMyrmexColonies && safelyTestBiome(BiomeConfig.desertMyrmexBiomes, biome)) {
            event.getGeneration().withFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, MYRMEX_HIVE_DESERT_CF);
        	LOADED_FEATURES.put("MYRMEX_HIVE_DESERT_CF", true);
        }
        if (IafConfig.generateMyrmexColonies && safelyTestBiome(BiomeConfig.jungleMyrmexBiomes, biome)) {
            event.getGeneration().withFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, MYRMEX_HIVE_JUNGLE_CF);
        	LOADED_FEATURES.put("MYRMEX_HIVE_JUNGLE_CF", true);
        }
        if (IafConfig.generateSirenIslands && safelyTestBiome(BiomeConfig.sirenBiomes, biome)) {
            event.getGeneration().withFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, SIREN_ISLAND_CF);
        	LOADED_FEATURES.put("SIREN_ISLAND_CF", true);
        }
    	if (IafConfig.spawnDeathWorm && safelyTestBiome(BiomeConfig.deathwormBiomes, biome)) {
    		event.getGeneration().withFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, SPAWN_DEATH_WORM_CF);
        	LOADED_FEATURES.put("SPAWN_DEATH_WORM_CF", true);
    	}
        if (IafConfig.generateWanderingCyclops && safelyTestBiome(BiomeConfig.wanderingCyclopsBiomes, biome)) {
        	event.getGeneration().withFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, SPAWN_WANDERING_CYCLOPS_CF);
        	LOADED_FEATURES.put("SPAWN_WANDERING_CYCLOPS_CF", true);
        }
        if (IafConfig.generateDragonSkeletons) {
            if (safelyTestBiome(BiomeConfig.lightningDragonSkeletonBiomes, biome)) {
        		event.getGeneration().withFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, SPAWN_DRAGON_SKELETON_L_CF);
            	LOADED_FEATURES.put("SPAWN_DRAGON_SKELETON_L_CF", true);
            }
            if (safelyTestBiome(BiomeConfig.fireDragonSkeletonBiomes, biome)) {
        		event.getGeneration().withFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, SPAWN_DRAGON_SKELETON_F_CF);
            	LOADED_FEATURES.put("SPAWN_DRAGON_SKELETON_F_CF", true);
            }
            if (safelyTestBiome(BiomeConfig.iceDragonSkeletonBiomes, biome)) {
            	event.getGeneration().withFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, SPAWN_DRAGON_SKELETON_I_CF);
            	LOADED_FEATURES.put("SPAWN_DRAGON_SKELETON_I_CF", true);
            }
        }
    	if (IafConfig.spawnHippocampus && safelyTestBiome(BiomeConfig.hippocampusBiomes, biome)) {
    		event.getGeneration().withFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, SPAWN_HIPPOCAMPUS_CF);
        	LOADED_FEATURES.put("SPAWN_HIPPOCAMPUS_CF", true);
    	}
        if (IafConfig.spawnSeaSerpents && safelyTestBiome(BiomeConfig.seaSerpentBiomes, biome)) {
        	event.getGeneration().withFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, SPAWN_SEA_SERPENT_CF);
        	LOADED_FEATURES.put("SPAWN_SEA_SERPENT_CF", true);
        }
        if (IafConfig.spawnStymphalianBirds && safelyTestBiome(BiomeConfig.stymphalianBiomes, biome)) {
        	event.getGeneration().withFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, SPAWN_STYMPHALIAN_BIRD_CF);
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
