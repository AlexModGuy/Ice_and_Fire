package com.github.alexthe666.iceandfire.world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.config.BiomeConfig;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import com.github.alexthe666.iceandfire.util.IAFBiomeUtil;
import com.github.alexthe666.iceandfire.world.feature.SpawnDeathWorm;
import com.github.alexthe666.iceandfire.world.feature.SpawnDragonSkeleton;
import com.github.alexthe666.iceandfire.world.feature.SpawnHippocampus;
import com.github.alexthe666.iceandfire.world.feature.SpawnSeaSerpent;
import com.github.alexthe666.iceandfire.world.feature.SpawnStymphalianBird;
import com.github.alexthe666.iceandfire.world.feature.SpawnWanderingCyclops;
import com.github.alexthe666.iceandfire.world.gen.WorldGenCyclopsCave;
import com.github.alexthe666.iceandfire.world.gen.WorldGenFireDragonCave;
import com.github.alexthe666.iceandfire.world.gen.WorldGenFireDragonRoosts;
import com.github.alexthe666.iceandfire.world.gen.WorldGenHydraCave;
import com.github.alexthe666.iceandfire.world.gen.WorldGenIceDragonCave;
import com.github.alexthe666.iceandfire.world.gen.WorldGenIceDragonRoosts;
import com.github.alexthe666.iceandfire.world.gen.WorldGenLightningDragonCave;
import com.github.alexthe666.iceandfire.world.gen.WorldGenLightningDragonRoosts;
import com.github.alexthe666.iceandfire.world.gen.WorldGenMyrmexHive;
import com.github.alexthe666.iceandfire.world.gen.WorldGenPixieVillage;
import com.github.alexthe666.iceandfire.world.gen.WorldGenSirenIsland;
import com.github.alexthe666.iceandfire.world.structure.DreadMausoleumStructure;
import com.github.alexthe666.iceandfire.world.structure.GorgonTemplePiece;
import com.github.alexthe666.iceandfire.world.structure.GorgonTempleStructure;
import com.github.alexthe666.iceandfire.world.structure.GraveyardPiece;
import com.github.alexthe666.iceandfire.world.structure.GraveyardStructure;
import com.github.alexthe666.iceandfire.world.structure.MausoleumPiece;
import com.google.common.collect.ImmutableList;

import net.minecraft.block.Blocks;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.DimensionSettings;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.WorldGenRegion;
import net.minecraft.world.gen.blockplacer.SimpleBlockPlacer;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.BlockClusterFeatureConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.Features;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.ReplaceBlockConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.placement.IPlacementConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

public class IafWorldRegistry {

    public static Feature<NoFeatureConfig> FIRE_DRAGON_ROOST;
    public static Feature<NoFeatureConfig> ICE_DRAGON_ROOST;
    public static Feature<NoFeatureConfig> LIGHTNING_DRAGON_ROOST;
    public static Feature<NoFeatureConfig> FIRE_DRAGON_CAVE;
    public static Feature<NoFeatureConfig> ICE_DRAGON_CAVE;
    public static Feature<NoFeatureConfig> LIGHTNING_DRAGON_CAVE;
    public static Feature<NoFeatureConfig> CYCLOPS_CAVE;
    public static Feature<NoFeatureConfig> PIXIE_VILLAGE;
    public static Feature<NoFeatureConfig> SIREN_ISLAND;
    public static Feature<NoFeatureConfig> HYDRA_CAVE;
    public static Feature<NoFeatureConfig> MYRMEX_HIVE_DESERT;
    public static Feature<NoFeatureConfig> MYRMEX_HIVE_JUNGLE;
    public static Feature<NoFeatureConfig> SPAWN_DEATH_WORM;
    public static Feature<NoFeatureConfig> SPAWN_DRAGON_SKELETON_L;
    public static Feature<NoFeatureConfig> SPAWN_DRAGON_SKELETON_F;
    public static Feature<NoFeatureConfig> SPAWN_DRAGON_SKELETON_I;
    public static Feature<NoFeatureConfig> SPAWN_HIPPOCAMPUS;
    public static Feature<NoFeatureConfig> SPAWN_SEA_SERPENT;
    public static Feature<NoFeatureConfig> SPAWN_STYMPHALIAN_BIRD;
    public static Feature<NoFeatureConfig> SPAWN_WANDERING_CYCLOPS;
    public static IStructurePieceType MAUSOLEUM_PIECE;
    public static Structure<NoFeatureConfig> MAUSOLEUM = new DreadMausoleumStructure(NoFeatureConfig.field_236558_a_);
    public static IStructurePieceType GORGON_PIECE;
    public static IStructurePieceType GORGON_EMPTY_PIECE;
    public static Structure<NoFeatureConfig> GORGON_TEMPLE = new GorgonTempleStructure(NoFeatureConfig.field_236558_a_);
    public static IStructurePieceType GRAVEYARD_PIECE;
    public static Structure<NoFeatureConfig> GRAVEYARD = new GraveyardStructure(NoFeatureConfig.field_236558_a_);
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
    public static List<Feature<?>> featureList = new ArrayList<>();
    public static List<Structure<?>> structureFeatureList = new ArrayList<>();


    public static void register() {
        FIRE_DRAGON_ROOST = registerFeature("iceandfire:fire_dragon_roost", new WorldGenFireDragonRoosts(NoFeatureConfig.field_236558_a_));
        ICE_DRAGON_ROOST = registerFeature("iceandfire:ice_dragon_roost", new WorldGenIceDragonRoosts(NoFeatureConfig.field_236558_a_));
        LIGHTNING_DRAGON_ROOST = registerFeature("iceandfire:lightning_dragon_roost", new WorldGenLightningDragonRoosts(NoFeatureConfig.field_236558_a_));
        FIRE_DRAGON_CAVE = registerFeature("iceandfire:fire_dragon_cave", new WorldGenFireDragonCave(NoFeatureConfig.field_236558_a_));
        ICE_DRAGON_CAVE = registerFeature("iceandfire:ice_dragon_cave", new WorldGenIceDragonCave(NoFeatureConfig.field_236558_a_));
        LIGHTNING_DRAGON_CAVE = registerFeature("iceandfire:lightning_dragon_cave", new WorldGenLightningDragonCave(NoFeatureConfig.field_236558_a_));
        CYCLOPS_CAVE = registerFeature("iceandfire:cyclops_cave", new WorldGenCyclopsCave(NoFeatureConfig.field_236558_a_));
        PIXIE_VILLAGE = registerFeature("iceandfire:pixie_village", new WorldGenPixieVillage(NoFeatureConfig.field_236558_a_));
        SIREN_ISLAND = registerFeature("iceandfire:siren_island", new WorldGenSirenIsland(NoFeatureConfig.field_236558_a_));
        HYDRA_CAVE = registerFeature("iceandfire:hydra_cave", new WorldGenHydraCave(NoFeatureConfig.field_236558_a_));
        MYRMEX_HIVE_DESERT = registerFeature("iceandfire:myrmex_hive_desert", new WorldGenMyrmexHive(false, false, NoFeatureConfig.field_236558_a_));
        MYRMEX_HIVE_JUNGLE = registerFeature("iceandfire:myrmex_hive_jungle", new WorldGenMyrmexHive(false, true, NoFeatureConfig.field_236558_a_));

        SPAWN_DEATH_WORM = registerFeature("iceandfire:spawn_death_worm", new SpawnDeathWorm(NoFeatureConfig.field_236558_a_));
        SPAWN_DRAGON_SKELETON_L = registerFeature("iceandfire:spawn_dragon_skeleton_l", new SpawnDragonSkeleton(IafEntityRegistry.LIGHTNING_DRAGON, NoFeatureConfig.field_236558_a_));
        SPAWN_DRAGON_SKELETON_F = registerFeature("iceandfire:spawn_dragon_skeleton_f", new SpawnDragonSkeleton(IafEntityRegistry.FIRE_DRAGON, NoFeatureConfig.field_236558_a_));
        SPAWN_DRAGON_SKELETON_I = registerFeature("iceandfire:spawn_dragon_skeleton_i", new SpawnDragonSkeleton(IafEntityRegistry.ICE_DRAGON, NoFeatureConfig.field_236558_a_));
        SPAWN_HIPPOCAMPUS = registerFeature("iceandfire:spawn_hippocampus", new SpawnHippocampus(NoFeatureConfig.field_236558_a_));
        SPAWN_SEA_SERPENT = registerFeature("iceandfire:spawn_sea_serpent", new SpawnSeaSerpent(NoFeatureConfig.field_236558_a_));
        SPAWN_STYMPHALIAN_BIRD = registerFeature("iceandfire:spawn_stymphalian_bird", new SpawnStymphalianBird(NoFeatureConfig.field_236558_a_));
        SPAWN_WANDERING_CYCLOPS = registerFeature("iceandfire:spawn_wandering_cyclops", new SpawnWanderingCyclops(NoFeatureConfig.field_236558_a_));
        
        MAUSOLEUM_PIECE = Registry.register(Registry.STRUCTURE_PIECE, "iceandfire:mausoleum_piece", MausoleumPiece.Piece::new);
        MAUSOLEUM = registerStructureFeature( "iceandfire:mausoleum", MAUSOLEUM);
        putStructureOnAList("iceandfire:mausoleum", MAUSOLEUM);

        GORGON_PIECE = Registry.register(Registry.STRUCTURE_PIECE, "iceandfire:gorgon_piece", GorgonTemplePiece.Piece::new);
        GORGON_EMPTY_PIECE = Registry.register(Registry.STRUCTURE_PIECE, "iceandfire:gorgon_piece_empty", GorgonTemplePiece.EmptyPiece::new);
        GORGON_TEMPLE = registerStructureFeature( "iceandfire:gorgon_temple", GORGON_TEMPLE);
        putStructureOnAList("iceandfire:gorgon_temple", GORGON_TEMPLE);

        GRAVEYARD_PIECE = Registry.register(Registry.STRUCTURE_PIECE, "iceandfire:graveyard_piece", GraveyardPiece.Piece::new);
        GRAVEYARD = registerStructureFeature( "iceandfire:graveyard", GRAVEYARD);
        putStructureOnAList("iceandfire:graveyard", GRAVEYARD);

        addStructureSeperation(DimensionSettings.field_242734_c, GORGON_TEMPLE, new StructureSeparationSettings(Math.max(IafConfig.spawnGorgonsChance, 2), Math.max(IafConfig.spawnGorgonsChance / 2, 1), 342226450));
        addStructureSeperation(DimensionSettings.field_242734_c, MAUSOLEUM, new StructureSeparationSettings(Math.max(IafConfig.generateMausoleumChance, 2), Math.max(IafConfig.generateMausoleumChance / 2, 1), 342226451));
        addStructureSeperation(DimensionSettings.field_242734_c, GRAVEYARD, new StructureSeparationSettings(Math.max(IafConfig.generateGraveyardChance * 3, 2), Math.max(IafConfig.generateGraveyardChance * 3 / 2, 1), 342226440));

        GORGON_TEMPLE_CF = Registry.register(WorldGenRegistries.field_243654_f, "iceandfire:gorgon_temple", GORGON_TEMPLE.func_236391_a_(IFeatureConfig.NO_FEATURE_CONFIG));
        MAUSOLEUM_CF = Registry.register(WorldGenRegistries.field_243654_f, "iceandfire:mausoleum", MAUSOLEUM.func_236391_a_(IFeatureConfig.NO_FEATURE_CONFIG));
        GRAVEYARD_CF = Registry.register(WorldGenRegistries.field_243654_f, "iceandfire:graveyard", GRAVEYARD.func_236391_a_(IFeatureConfig.NO_FEATURE_CONFIG));

        Structure.field_236384_t_= ImmutableList.<Structure<?>>builder().addAll(Structure.field_236384_t_).add(GORGON_TEMPLE, MAUSOLEUM, GRAVEYARD).build();

        COPPER_ORE_CF = Registry.register(WorldGenRegistries.field_243653_e, "iceandfire:copper_ore", Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.field_241882_a, IafBlockRegistry.COPPER_ORE.getDefaultState(), 5)).func_242733_d(128).func_242728_a().func_242731_b(5));
        SILVER_ORE_CF = Registry.register(WorldGenRegistries.field_243653_e, "iceandfire:silver_ore", Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.field_241882_a, IafBlockRegistry.SILVER_ORE.getDefaultState(), 8)).func_242733_d(32).func_242728_a().func_242731_b(2));
        SAPPHIRE_ORE_CF = Registry.register(WorldGenRegistries.field_243653_e, "iceandfire:sapphire_ore", Feature.EMERALD_ORE.withConfiguration(new ReplaceBlockConfig(Blocks.STONE.getDefaultState(), IafBlockRegistry.SAPPHIRE_ORE.getDefaultState())).withPlacement(Placement.EMERALD_ORE.configure(IPlacementConfig.NO_PLACEMENT_CONFIG)));
        AMETHYST_ORE_CF = Registry.register(WorldGenRegistries.field_243653_e, "iceandfire:amythest_ore", Feature.EMERALD_ORE.withConfiguration(new ReplaceBlockConfig(Blocks.STONE.getDefaultState(), IafBlockRegistry.AMYTHEST_ORE.getDefaultState())).withPlacement(Placement.EMERALD_ORE.configure(IPlacementConfig.NO_PLACEMENT_CONFIG)));
        FIRE_LILY_CF = Registry.register(WorldGenRegistries.field_243653_e, "iceandfire:fire_lily", Feature.FLOWER.withConfiguration(new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(IafBlockRegistry.FIRE_LILY.getDefaultState()), new SimpleBlockPlacer()).tries(1).build()).withPlacement(Features.Placements.field_244000_k).withPlacement(Features.Placements.field_244001_l));
        FROST_LILY_CF = Registry.register(WorldGenRegistries.field_243653_e, "iceandfire:frost_lily", Feature.FLOWER.withConfiguration(new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(IafBlockRegistry.FROST_LILY.getDefaultState()), new SimpleBlockPlacer()).tries(1).build()).withPlacement(Features.Placements.field_244000_k).withPlacement(Features.Placements.field_244001_l));
        LIGHTNING_LILY_CF = Registry.register(WorldGenRegistries.field_243653_e, "iceandfire:lightning_lily", Feature.FLOWER.withConfiguration(new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(IafBlockRegistry.LIGHTNING_LILY.getDefaultState()), new SimpleBlockPlacer()).tries(1).build()).withPlacement(Features.Placements.field_244000_k).withPlacement(Features.Placements.field_244001_l));
        FIRE_DRAGON_ROOST_CF = Registry.register(WorldGenRegistries.field_243653_e, "iceandfire:fire_dragon_roost", FIRE_DRAGON_ROOST.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
        ICE_DRAGON_ROOST_CF = Registry.register(WorldGenRegistries.field_243653_e, "iceandfire:ice_dragon_roost", ICE_DRAGON_ROOST.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
        LIGHTNING_DRAGON_ROOST_CF = Registry.register(WorldGenRegistries.field_243653_e, "iceandfire:lightning_dragon_roost", LIGHTNING_DRAGON_ROOST.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
        FIRE_DRAGON_CAVE_CF = Registry.register(WorldGenRegistries.field_243653_e, "iceandfire:fire_dragon_cave", FIRE_DRAGON_CAVE.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
        ICE_DRAGON_CAVE_CF = Registry.register(WorldGenRegistries.field_243653_e, "iceandfire:ice_dragon_cave", ICE_DRAGON_CAVE.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
        LIGHTNING_DRAGON_CAVE_CF = Registry.register(WorldGenRegistries.field_243653_e, "iceandfire:lightning_dragon_cave", LIGHTNING_DRAGON_CAVE.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
        CYCLOPS_CAVE_CF = Registry.register(WorldGenRegistries.field_243653_e, "iceandfire:cyclops_cave", CYCLOPS_CAVE.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
        PIXIE_VILLAGE_CF = Registry.register(WorldGenRegistries.field_243653_e, "iceandfire:pixie_village", PIXIE_VILLAGE.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
        SIREN_ISLAND_CF = Registry.register(WorldGenRegistries.field_243653_e, "iceandfire:siren_island", SIREN_ISLAND.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
        HYDRA_CAVE_CF = Registry.register(WorldGenRegistries.field_243653_e, "iceandfire:hydra_cave", HYDRA_CAVE.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
        MYRMEX_HIVE_DESERT_CF = Registry.register(WorldGenRegistries.field_243653_e, "iceandfire:myrmex_hive_desert", MYRMEX_HIVE_DESERT.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
        MYRMEX_HIVE_JUNGLE_CF = Registry.register(WorldGenRegistries.field_243653_e, "iceandfire:myrmex_hive_jungle", MYRMEX_HIVE_JUNGLE.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
        
        SPAWN_DEATH_WORM_CF = Registry.register(WorldGenRegistries.field_243653_e, "iceandfire:spawn_death_worm_misc", SPAWN_DEATH_WORM.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
        SPAWN_DRAGON_SKELETON_L_CF = Registry.register(WorldGenRegistries.field_243653_e, "iceandfire:spawn_dragon_skeleton_l_misc", SPAWN_DRAGON_SKELETON_L.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
        SPAWN_DRAGON_SKELETON_F_CF = Registry.register(WorldGenRegistries.field_243653_e, "iceandfire:spawn_dragon_skeleton_f_misc", SPAWN_DRAGON_SKELETON_F.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
        SPAWN_DRAGON_SKELETON_I_CF = Registry.register(WorldGenRegistries.field_243653_e, "iceandfire:spawn_dragon_skeleton_i_misc", SPAWN_DRAGON_SKELETON_I.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
        SPAWN_HIPPOCAMPUS_CF = Registry.register(WorldGenRegistries.field_243653_e, "iceandfire:spawn_hippocampus_misc", SPAWN_HIPPOCAMPUS.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
        SPAWN_SEA_SERPENT_CF = Registry.register(WorldGenRegistries.field_243653_e, "iceandfire:spawn_sea_serpent_misc", SPAWN_SEA_SERPENT.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
        SPAWN_STYMPHALIAN_BIRD_CF = Registry.register(WorldGenRegistries.field_243653_e, "iceandfire:spawn_stymphalian_bird_misc", SPAWN_STYMPHALIAN_BIRD.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
        SPAWN_WANDERING_CYCLOPS_CF = Registry.register(WorldGenRegistries.field_243653_e, "iceandfire:spawn_wandering_cyclops_misc", SPAWN_WANDERING_CYCLOPS.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
    }

    public static void addStructureSeperation(RegistryKey<DimensionSettings> preset, Structure structure, StructureSeparationSettings settings) {
        WorldGenRegistries.field_243658_j.func_230516_a_(preset).func_236108_a_().func_236195_a_().put(structure, settings);
    }

    public static <F extends Structure<?>> void putStructureOnAList(String nameForList, F structure) {
        Structure.field_236365_a_.put(nameForList.toLowerCase(Locale.ROOT), structure);
    }

    private static Feature<NoFeatureConfig> registerFeature(String registryName, Feature<NoFeatureConfig> feature) {
        featureList.add(feature.setRegistryName(registryName));
        return feature;
    }


    private static Structure<NoFeatureConfig> registerStructureFeature(String registryName, Structure<NoFeatureConfig> feature) {
        structureFeatureList.add(feature);
        return feature;
    }

    public static void setup() {
    }

    public static boolean isFarEnoughFromSpawn(IWorld world, BlockPos pos) {
        BlockPos spawnRelative = new BlockPos(0, pos.getY(), 0);
        boolean spawnCheck = !spawnRelative.withinDistance(pos, IafConfig.dangerousWorldGenDistanceLimit);
        return spawnCheck;
    }

    public static boolean isDimensionListed(IServerWorld world) {
        ResourceLocation name = world.getWorld().func_234923_W_().func_240901_a_();
        if (name == null) {
            return false;
        }
        if (IafConfig.useDimensionBlackList) {
            for (String blacklisted : IafConfig.blacklistedDimensions) {
                if (name.toString().equals(blacklisted)) {
                    return false;
                }
            }
            return true;
        } else {
            for (String whitelist : IafConfig.whitelistedDimensions) {
                if (name.toString().equals(whitelist)) {
                    return true;
                }
            }
            return false;
        }
    }

    public static boolean isDimensionListedForDragons(IServerWorld world) {
        ResourceLocation name = world.getWorld().func_234923_W_().func_240901_a_();
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
        ResourceLocation name = world.getWorld().func_234923_W_().func_240901_a_();
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
    public static boolean isFarEnoughFromDangerousGen(IWorld world, BlockPos pos) {
        boolean canGen = true;
        IafWorldData data = IafWorldData.get(((WorldGenRegion) world).getWorld());
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

    	if (IAFBiomeUtil.parseListForBiomeCheck(BiomeConfig.fireLilyBiomes, biome)) {
            event.getGeneration().func_242513_a(GenerationStage.Decoration.VEGETAL_DECORATION, FIRE_LILY_CF);
        	LOADED_FEATURES.put("FIRE_LILY_CF", true);
        }
    	if (IAFBiomeUtil.parseListForBiomeCheck(BiomeConfig.lightningLilyBiomes, biome)) {
            event.getGeneration().func_242513_a(GenerationStage.Decoration.VEGETAL_DECORATION, LIGHTNING_LILY_CF);
        	LOADED_FEATURES.put("LIGHTNING_LILY_CF", true);
        }
    	if (IAFBiomeUtil.parseListForBiomeCheck(BiomeConfig.iceLilyBiomes, biome)) {
            event.getGeneration().func_242513_a(GenerationStage.Decoration.VEGETAL_DECORATION, FROST_LILY_CF);
        	LOADED_FEATURES.put("FROST_LILY_CF", true);
        }
    	if (IAFBiomeUtil.parseListForBiomeCheck(BiomeConfig.oreGenBiomes, biome)) {
            if (IafConfig.generateSilverOre) {
                event.getGeneration().func_242513_a(GenerationStage.Decoration.UNDERGROUND_ORES, SILVER_ORE_CF);
            	LOADED_FEATURES.put("SILVER_ORE_CF", true);
            }
            if (IafConfig.generateCopperOre) {
                event.getGeneration().func_242513_a(GenerationStage.Decoration.UNDERGROUND_ORES, COPPER_ORE_CF);
            	LOADED_FEATURES.put("COPPER_ORE_CF", true);
            }
        }
        if (IafConfig.generateSapphireOre && IAFBiomeUtil.parseListForBiomeCheck(BiomeConfig.sapphireBiomes, biome)) {
            event.getGeneration().func_242513_a(GenerationStage.Decoration.UNDERGROUND_ORES, SAPPHIRE_ORE_CF);
        	LOADED_FEATURES.put("SAPPHIRE_ORE_CF", true);
        }
        if (IafConfig.generateAmythestOre && IAFBiomeUtil.parseListForBiomeCheck(BiomeConfig.amethystBiomes, biome)) {
            event.getGeneration().func_242513_a(GenerationStage.Decoration.UNDERGROUND_ORES, AMETHYST_ORE_CF);
        	LOADED_FEATURES.put("AMETHYST_ORE_CF", true);
        }
        if (IafConfig.generateDragonRoosts) {
            if (IAFBiomeUtil.parseListForBiomeCheck(BiomeConfig.fireDragonBiomes, biome)) {
                event.getGeneration().func_242513_a(GenerationStage.Decoration.SURFACE_STRUCTURES, FIRE_DRAGON_ROOST_CF);
                event.getGeneration().func_242513_a(GenerationStage.Decoration.UNDERGROUND_STRUCTURES, FIRE_DRAGON_CAVE_CF);
            	LOADED_FEATURES.put("FIRE_DRAGON_ROOST_CF", true);
            	LOADED_FEATURES.put("FIRE_DRAGON_CAVE_CF", true);
            }
            if (IAFBiomeUtil.parseListForBiomeCheck(BiomeConfig.lightningDragonBiomes, biome)) {
                event.getGeneration().func_242513_a(GenerationStage.Decoration.SURFACE_STRUCTURES, LIGHTNING_DRAGON_ROOST_CF);
                event.getGeneration().func_242513_a(GenerationStage.Decoration.UNDERGROUND_STRUCTURES, LIGHTNING_DRAGON_CAVE_CF);
            	LOADED_FEATURES.put("LIGHTNING_DRAGON_ROOST_CF", true);
            	LOADED_FEATURES.put("LIGHTNING_DRAGON_CAVE_CF", true);
            }
            if (IAFBiomeUtil.parseListForBiomeCheck(BiomeConfig.iceDragonBiomes, biome)) {
                event.getGeneration().func_242513_a(GenerationStage.Decoration.SURFACE_STRUCTURES, ICE_DRAGON_ROOST_CF);
                event.getGeneration().func_242513_a(GenerationStage.Decoration.UNDERGROUND_STRUCTURES, ICE_DRAGON_CAVE_CF);
            	LOADED_FEATURES.put("ICE_DRAGON_ROOST_CF", true);
            	LOADED_FEATURES.put("ICE_DRAGON_CAVE_CF", true);
            }
        }
        if (IafConfig.generateCyclopsCaves && IAFBiomeUtil.parseListForBiomeCheck(BiomeConfig.cyclopsCaveBiomes, biome)) {
            event.getGeneration().func_242513_a(GenerationStage.Decoration.SURFACE_STRUCTURES, CYCLOPS_CAVE_CF);
        	LOADED_FEATURES.put("CYCLOPS_CAVE_CF", true);
        }
        if (IafConfig.spawnGorgons && IAFBiomeUtil.parseListForBiomeCheck(BiomeConfig.gorgonTempleBiomes, biome)) {
            event.getGeneration().func_242516_a(GORGON_TEMPLE_CF);
        	LOADED_FEATURES.put("GORGON_TEMPLE_CF", true);
        }
        if (IafConfig.spawnPixies && IAFBiomeUtil.parseListForBiomeCheck(BiomeConfig.pixieBiomes, biome)) {
            event.getGeneration().func_242513_a(GenerationStage.Decoration.SURFACE_STRUCTURES, PIXIE_VILLAGE_CF);
        }
        if (IafConfig.generateHydraCaves && IAFBiomeUtil.parseListForBiomeCheck(BiomeConfig.hydraBiomes, biome)) {
            event.getGeneration().func_242513_a(GenerationStage.Decoration.SURFACE_STRUCTURES, HYDRA_CAVE_CF);
        	LOADED_FEATURES.put("HYDRA_CAVE_CF", true);
        }
        if (IafConfig.generateMausoleums && IAFBiomeUtil.parseListForBiomeCheck(BiomeConfig.mausoleumBiomes, biome)) {
            event.getGeneration().func_242516_a(MAUSOLEUM_CF);
        	LOADED_FEATURES.put("MAUSOLEUM_CF", true);
        }
        if (IafConfig.generateGraveyards && IAFBiomeUtil.parseListForBiomeCheck(BiomeConfig.graveyardBiomes, biome)) {
            event.getGeneration().func_242516_a(GRAVEYARD_CF);
        	LOADED_FEATURES.put("GRAVEYARD_CF", true);
        }
        if (IafConfig.generateMyrmexColonies && IAFBiomeUtil.parseListForBiomeCheck(BiomeConfig.desertMyrmexBiomes, biome)) {
            event.getGeneration().func_242513_a(GenerationStage.Decoration.SURFACE_STRUCTURES, MYRMEX_HIVE_DESERT_CF);
        	LOADED_FEATURES.put("MYRMEX_HIVE_DESERT_CF", true);
        }
        if (IafConfig.generateMyrmexColonies && IAFBiomeUtil.parseListForBiomeCheck(BiomeConfig.jungleMyrmexBiomes, biome)) {
            event.getGeneration().func_242513_a(GenerationStage.Decoration.SURFACE_STRUCTURES, MYRMEX_HIVE_JUNGLE_CF);
        	LOADED_FEATURES.put("MYRMEX_HIVE_JUNGLE_CF", true);
        }
        if (IafConfig.generateMyrmexColonies && IAFBiomeUtil.parseListForBiomeCheck(BiomeConfig.sirenBiomes, biome)) {
            event.getGeneration().func_242513_a(GenerationStage.Decoration.SURFACE_STRUCTURES, SIREN_ISLAND_CF);
        	LOADED_FEATURES.put("SIREN_ISLAND_CF", true);
        }
    	if (IafConfig.spawnDeathWorm && IAFBiomeUtil.parseListForBiomeCheck(BiomeConfig.deathwormBiomes, biome)) {
    		event.getGeneration().func_242513_a(GenerationStage.Decoration.SURFACE_STRUCTURES, SPAWN_DEATH_WORM_CF);
        	LOADED_FEATURES.put("SPAWN_DEATH_WORM_CF", true);
    	}
        if (IafConfig.generateWanderingCyclops && IAFBiomeUtil.parseListForBiomeCheck(BiomeConfig.wanderingCyclopsBiomes, biome)) {
        	event.getGeneration().func_242513_a(GenerationStage.Decoration.SURFACE_STRUCTURES, SPAWN_WANDERING_CYCLOPS_CF);
        	LOADED_FEATURES.put("SPAWN_WANDERING_CYCLOPS_CF", true);
        }
        if (IafConfig.generateDragonSkeletons) {
            if (IAFBiomeUtil.parseListForBiomeCheck(BiomeConfig.lightningDragonSkeletonBiomes, biome)) {
        		event.getGeneration().func_242513_a(GenerationStage.Decoration.SURFACE_STRUCTURES, SPAWN_DRAGON_SKELETON_L_CF);
            	LOADED_FEATURES.put("SPAWN_DRAGON_SKELETON_L_CF", true);
            }
            if (IAFBiomeUtil.parseListForBiomeCheck(BiomeConfig.fireDragonSkeletonBiomes, biome)) {
        		event.getGeneration().func_242513_a(GenerationStage.Decoration.SURFACE_STRUCTURES, SPAWN_DRAGON_SKELETON_F_CF);
            	LOADED_FEATURES.put("SPAWN_DRAGON_SKELETON_F_CF", true);
            }
            if (IAFBiomeUtil.parseListForBiomeCheck(BiomeConfig.iceDragonSkeletonBiomes, biome)) {
            	event.getGeneration().func_242513_a(GenerationStage.Decoration.SURFACE_STRUCTURES, SPAWN_DRAGON_SKELETON_I_CF);
            	LOADED_FEATURES.put("SPAWN_DRAGON_SKELETON_I_CF", true);
            }
        }
    	if (IafConfig.spawnHippocampus && IAFBiomeUtil.parseListForBiomeCheck(BiomeConfig.hippocampusBiomes, biome)) {
    		event.getGeneration().func_242513_a(GenerationStage.Decoration.SURFACE_STRUCTURES, SPAWN_HIPPOCAMPUS_CF);
        	LOADED_FEATURES.put("SPAWN_HIPPOCAMPUS_CF", true);
    	}
        if (IafConfig.spawnSeaSerpents && IAFBiomeUtil.parseListForBiomeCheck(BiomeConfig.seaSerpentBiomes, biome)) {
        	event.getGeneration().func_242513_a(GenerationStage.Decoration.SURFACE_STRUCTURES, SPAWN_SEA_SERPENT_CF);
        	LOADED_FEATURES.put("SPAWN_SEA_SERPENT_CF", true);
        }
        if (IafConfig.spawnStymphalianBirds && IAFBiomeUtil.parseListForBiomeCheck(BiomeConfig.stymphalianBiomes, biome)) {
        	event.getGeneration().func_242513_a(GenerationStage.Decoration.SURFACE_STRUCTURES, SPAWN_STYMPHALIAN_BIRD_CF);
        	LOADED_FEATURES.put("SPAWN_STYMPHALIAN_BIRD_CF", true);
        }
    }
}
