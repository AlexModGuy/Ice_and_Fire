package com.github.alexthe666.iceandfire.world;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.world.gen.*;
import com.github.alexthe666.iceandfire.world.structure.*;
import net.minecraft.block.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.blockplacer.SimpleBlockPlacer;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.FrequencyConfig;
import net.minecraft.world.gen.placement.IPlacementConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.registries.ForgeRegistries;

public class IafWorldRegistry {

    public static final SurfaceBuilderGlacier GLACIER_SURFACE_BUILDER = new SurfaceBuilderGlacier(SurfaceBuilderConfig::deserialize);
    public static Biome GLACIER_BIOME = new BiomeGlacier();
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
    public static Feature<NoFeatureConfig> MOB_SPAWNS;
    public static IStructurePieceType MAUSOLEUM_PIECE;
    public static Structure<NoFeatureConfig> MAUSOLEUM = new DreadMausoleumStructure(NoFeatureConfig::deserialize);
    public static IStructurePieceType GORGON_PIECE;
<<<<<<< HEAD
    public static Structure<NoFeatureConfig> GORGON_TEMPLE = new GorgonTempleStructure(NoFeatureConfig::deserialize);
=======
    public static Structure<NoFeatureConfig> GORGON_TEMPLE = new GorgonTempleStructure(NoFeatureConfig.field_236558_a_);
    public static final Structure<NoFeatureConfig> GORGON_TEMPLE_SF = func_236394_a_("iceandfire:gorgon_temple", GORGON_TEMPLE, GenerationStage.Decoration.SURFACE_STRUCTURES);
    public static IStructurePieceType GRAVEYARD_PIECE;
    public static Structure<NoFeatureConfig> GRAVEYARD = new GraveyardStructure(NoFeatureConfig.field_236558_a_);
    public static final Structure<NoFeatureConfig> GRAVEYARD_SF = func_236394_a_("iceandfire:graveyard", GRAVEYARD, GenerationStage.Decoration.SURFACE_STRUCTURES);

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
    public static ConfiguredFeature MOB_SPAWNS_CF;
    public static StructureFeature GORGON_TEMPLE_CF;
    public static StructureFeature MAUSOLEUM_CF;
    public static StructureFeature GRAVEYARD_CF;
>>>>>>> aff892f7... server stuff... 2

    static {
        GLACIER_SURFACE_BUILDER.setRegistryName("iceandfire:glacier_surface");
    }

    public static void register() {
        FIRE_DRAGON_ROOST = Registry.register(Registry.FEATURE, "iceandfire:fire_dragon_roost", new WorldGenFireDragonRoosts(NoFeatureConfig::deserialize));
        ICE_DRAGON_ROOST = Registry.register(Registry.FEATURE, "iceandfire:ice_dragon_roost", new WorldGenIceDragonRoosts(NoFeatureConfig::deserialize));
        LIGHTNING_DRAGON_ROOST = Registry.register(Registry.FEATURE, "iceandfire:lightning_dragon_roost", new WorldGenLightningDragonRoosts(NoFeatureConfig::deserialize));
        FIRE_DRAGON_CAVE = Registry.register(Registry.FEATURE, "iceandfire:fire_dragon_cave", new WorldGenFireDragonCave(NoFeatureConfig::deserialize));
        ICE_DRAGON_CAVE = Registry.register(Registry.FEATURE, "iceandfire:ice_dragon_cave", new WorldGenIceDragonCave(NoFeatureConfig::deserialize));
        LIGHTNING_DRAGON_CAVE = Registry.register(Registry.FEATURE, "iceandfire:lightning_dragon_cave", new WorldGenLightningDragonCave(NoFeatureConfig::deserialize));
        CYCLOPS_CAVE = Registry.register(Registry.FEATURE, "iceandfire:cyclops_cave", new WorldGenCyclopsCave(NoFeatureConfig::deserialize));
        PIXIE_VILLAGE = Registry.register(Registry.FEATURE, "iceandfire:pixie_village", new WorldGenPixieVillage(NoFeatureConfig::deserialize));
        SIREN_ISLAND = Registry.register(Registry.FEATURE, "iceandfire:siren_island", new WorldGenSirenIsland(NoFeatureConfig::deserialize));
        HYDRA_CAVE = Registry.register(Registry.FEATURE, "iceandfire:hydra_cave", new WorldGenHydraCave(NoFeatureConfig::deserialize));
        MYRMEX_HIVE_DESERT = Registry.register(Registry.FEATURE, "iceandfire:myrmex_hive_desert", new WorldGenMyrmexHive(false, false, NoFeatureConfig::deserialize));
        MYRMEX_HIVE_JUNGLE = Registry.register(Registry.FEATURE, "iceandfire:myrmex_hive_jungle", new WorldGenMyrmexHive(false, true, NoFeatureConfig::deserialize));
        MOB_SPAWNS = Registry.register(Registry.FEATURE, "iceandfire:mob_spawns", new WorldGenMobSpawn(NoFeatureConfig::deserialize));
        MAUSOLEUM_PIECE = Registry.register(Registry.STRUCTURE_PIECE, "iceandfire:mausoleum_piece", MausoleumPiece.Piece::new);
        MAUSOLEUM = Registry.register(Registry.FEATURE, "iceandfire:mausoleum", MAUSOLEUM);
        Registry.register(Registry.STRUCTURE_FEATURE, "iceandfire:mausoleum", MAUSOLEUM);

        GORGON_PIECE = Registry.register(Registry.STRUCTURE_PIECE, "iceandfire:gorgon_piece", GorgonTemplePiece.Piece::new);
<<<<<<< HEAD
        GORGON_TEMPLE = Registry.register(Registry.FEATURE, "iceandfire:gorgon_temple", GORGON_TEMPLE);
        Registry.register(Registry.STRUCTURE_FEATURE, "iceandfire:gorgon_temple", GORGON_TEMPLE);
=======
        GORGON_TEMPLE = Registry.register(Registry.STRUCTURE_FEATURE, "iceandfire:gorgon_temple", GORGON_TEMPLE);
        putStructureOnAList("iceandfire:gorgon_temple", GORGON_TEMPLE);

        GRAVEYARD_PIECE = Registry.register(Registry.STRUCTURE_PIECE, "iceandfire:graveyard_piece", GraveyardPiece.Piece::new);
        GRAVEYARD = Registry.register(Registry.STRUCTURE_FEATURE, "iceandfire:graveyard", GRAVEYARD);
        putStructureOnAList("iceandfire:graveyard", GRAVEYARD);

        addStructureSeperation(DimensionSettings.field_242734_c, GORGON_TEMPLE, new StructureSeparationSettings(Math.max(IafConfig.spawnGorgonsChance, 2), Math.max(IafConfig.spawnGorgonsChance / 2, 1), 34222645));
        addStructureSeperation(DimensionSettings.field_242734_c, MAUSOLEUM, new StructureSeparationSettings(Math.max(IafConfig.generateMausoleumChance, 2), Math.max(IafConfig.generateMausoleumChance / 2, 1), 34222645));
        addStructureSeperation(DimensionSettings.field_242734_c, GRAVEYARD, new StructureSeparationSettings(Math.max(IafConfig.spawnGorgonsChance, 2), Math.max(IafConfig.spawnGorgonsChance / 2, 1), 34222644));

        GORGON_TEMPLE_CF = Registry.register(WorldGenRegistries.field_243654_f, "iceandfire:gorgon_temple", GORGON_TEMPLE.func_236391_a_(IFeatureConfig.NO_FEATURE_CONFIG));
        MAUSOLEUM_CF = Registry.register(WorldGenRegistries.field_243654_f, "iceandfire:mausoleum", MAUSOLEUM.func_236391_a_(IFeatureConfig.NO_FEATURE_CONFIG));
        GRAVEYARD_CF = Registry.register(WorldGenRegistries.field_243654_f, "iceandfire:graveyard", GRAVEYARD.func_236391_a_(IFeatureConfig.NO_FEATURE_CONFIG));


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
        MOB_SPAWNS_CF = Registry.register(WorldGenRegistries.field_243653_e, "iceandfire:misc_mob_spawns", MOB_SPAWNS.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));

    }
>>>>>>> aff892f7... server stuff... 2

    }

    public static void setup() {
        for (Biome biome : ForgeRegistries.BIOMES) {
            if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.OVERWORLD)) {
                biome.addFeature(GenerationStage.Decoration.LOCAL_MODIFICATIONS, MOB_SPAWNS.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
            }
            if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.NETHER) || BiomeDictionary.hasType(biome, BiomeDictionary.Type.HOT) && !BiomeDictionary.hasType(biome, BiomeDictionary.Type.SAVANNA)) {
                biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.FLOWER.withConfiguration(new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(IafBlockRegistry.FIRE_LILY.getDefaultState()), new SimpleBlockPlacer()).tries(1).build()).withPlacement(Placement.COUNT_HEIGHTMAP_32.configure(new FrequencyConfig(2))));
            }
            if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.SAVANNA)) {
                biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.FLOWER.withConfiguration(new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(IafBlockRegistry.LIGHTNING_LILY.getDefaultState()), new SimpleBlockPlacer()).tries(1).build()).withPlacement(Placement.COUNT_HEIGHTMAP_32.configure(new FrequencyConfig(2))));
            }
            if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.SNOWY)) {
                biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.FLOWER.withConfiguration(new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(IafBlockRegistry.FROST_LILY.getDefaultState()), new SimpleBlockPlacer()).tries(1).build()).withPlacement(Placement.COUNT_HEIGHTMAP_32.configure(new FrequencyConfig(2))));
            }
            if (IafConfig.generateSilverOre) {
                biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, IafBlockRegistry.SILVER_ORE.getDefaultState(), 8)).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(2, 0, 0, 32))));
            }
            if (IafConfig.generateCopperOre) {
                biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, IafBlockRegistry.COPPER_ORE.getDefaultState(), 5)).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(10, 32, 32, 80))));
            }
            if (IafConfig.generateSapphireOre && BiomeDictionary.hasType(biome, BiomeDictionary.Type.SNOWY)) {
                biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.EMERALD_ORE.withConfiguration(new ReplaceBlockConfig(Blocks.STONE.getDefaultState(), IafBlockRegistry.SAPPHIRE_ORE.getDefaultState())).withPlacement(Placement.EMERALD_ORE.configure(IPlacementConfig.NO_PLACEMENT_CONFIG)));
            }
            if (IafConfig.generateAmythestOre && BiomeDictionary.hasType(biome, BiomeDictionary.Type.SAVANNA)) {
                biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.EMERALD_ORE.withConfiguration(new ReplaceBlockConfig(Blocks.STONE.getDefaultState(), IafBlockRegistry.AMYTHEST_ORE.getDefaultState())).withPlacement(Placement.EMERALD_ORE.configure(IPlacementConfig.NO_PLACEMENT_CONFIG)));
            }
            if (!BiomeDictionary.hasType(biome, BiomeDictionary.Type.WATER) && !BiomeDictionary.hasType(biome, BiomeDictionary.Type.RIVER) && !BiomeDictionary.hasType(biome, BiomeDictionary.Type.OCEAN)) {
                if (IafConfig.generateDragonRoosts) {
                    if (biome.getPrecipitation() == Biome.RainType.SNOW || biome.getDefaultTemperature() < 0.0) {
                        biome.addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, ICE_DRAGON_ROOST.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
                    } else if (biome.getPrecipitation() != Biome.RainType.SNOW && biome.getDefaultTemperature() >= 0.0 && (BiomeDictionary.hasType(biome, BiomeDictionary.Type.SAVANNA) || BiomeDictionary.hasType(biome, BiomeDictionary.Type.MESA) || BiomeDictionary.hasType(biome, BiomeDictionary.Type.JUNGLE))) {
                        biome.addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, LIGHTNING_DRAGON_ROOST.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
                    } else if (biome.getPrecipitation() != Biome.RainType.SNOW && biome.getDefaultTemperature() >= 0.0) {
                        biome.addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, FIRE_DRAGON_ROOST.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
                    }
                    if (biome.getPrecipitation() == Biome.RainType.SNOW || biome.getDefaultTemperature() < 0.0) {
                        biome.addFeature(GenerationStage.Decoration.UNDERGROUND_STRUCTURES, ICE_DRAGON_CAVE.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
                    } else if (biome.getPrecipitation() != Biome.RainType.SNOW && biome.getDefaultTemperature() >= 0.0 && (BiomeDictionary.hasType(biome, BiomeDictionary.Type.SAVANNA) || BiomeDictionary.hasType(biome, BiomeDictionary.Type.MESA) || BiomeDictionary.hasType(biome, BiomeDictionary.Type.JUNGLE))) {
                        biome.addFeature(GenerationStage.Decoration.UNDERGROUND_STRUCTURES, LIGHTNING_DRAGON_CAVE.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
                    } else if (biome.getPrecipitation() != Biome.RainType.SNOW && biome.getDefaultTemperature() >= 0.0) {
                        biome.addFeature(GenerationStage.Decoration.UNDERGROUND_STRUCTURES, FIRE_DRAGON_CAVE.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
                    }
                }
                if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.BEACH)) {
                    biome.addStructure(GORGON_TEMPLE.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
                    biome.addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, GORGON_TEMPLE.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Placement.NOPE.configure(IPlacementConfig.NO_PLACEMENT_CONFIG)));
                    biome.addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, CYCLOPS_CAVE.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
                }
                if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.SWAMP)) {
                    biome.addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, HYDRA_CAVE.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
                }
                if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.MAGICAL) || BiomeDictionary.hasType(biome, BiomeDictionary.Type.FOREST) && BiomeDictionary.hasType(biome, BiomeDictionary.Type.SPOOKY)) {
                    biome.addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, PIXIE_VILLAGE.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
                }
                if (biome.getPrecipitation() == Biome.RainType.SNOW || biome.getDefaultTemperature() < 0.0 && BiomeDictionary.hasType(biome, BiomeDictionary.Type.SNOWY)) {
                    biome.addStructure(MAUSOLEUM.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
                    biome.addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, MAUSOLEUM.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Placement.NOPE.configure(IPlacementConfig.NO_PLACEMENT_CONFIG)));
                }
                if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.SANDY) && BiomeDictionary.hasType(biome, BiomeDictionary.Type.HOT) && BiomeDictionary.hasType(biome, BiomeDictionary.Type.DRY)) {
                    biome.addFeature(GenerationStage.Decoration.UNDERGROUND_STRUCTURES, MYRMEX_HIVE_DESERT.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
                }
                if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.JUNGLE)) {
                    biome.addFeature(GenerationStage.Decoration.UNDERGROUND_STRUCTURES, MYRMEX_HIVE_JUNGLE.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
                }
            } else {
                if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.OCEAN)) {
                    biome.addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, SIREN_ISLAND.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
                }
            }

        }
    }

    public static boolean isFarEnoughFromSpawn(IWorld world, BlockPos pos) {
        BlockPos spawnRelative = new BlockPos(0, pos.getY(), 0);
        boolean spawnCheck = spawnRelative.distanceSq(pos) > IafConfig.dangerousWorldGenDistanceLimit * IafConfig.dangerousWorldGenDistanceLimit;
        return spawnCheck;
    }

    public static boolean isDimensionListed(IWorld world) {
        ResourceLocation name = world.getDimension().getType().getRegistryName();
        if(name == null){
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

    public static boolean isDimensionListedForDragons(IWorld world) {
        ResourceLocation name = world.getDimension().getType().getRegistryName();
        if(name == null){
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


    public static boolean isFarEnoughFromDangerousGen(IWorld world, BlockPos pos) {
        /*boolean canGen = true;
        IafWorldData data = IafWorldData.get(world.getWorld());
        if (data != null) {
            BlockPos last = data.lastGeneratedDangerousStructure;
            canGen = last.distanceSq(pos) > IafConfig.dangerousWorldGenSeparationLimit * IafConfig.dangerousWorldGenSeparationLimit;
            if (canGen) {
                data.setLastGeneratedDangerousStructure(pos);
            }
        }
        return canGen;*/
        return true;
    }
<<<<<<< HEAD
=======

    public static void onBiomesLoad(BiomeLoadingEvent event) {
        if (BiomeConfig.fireLilyBiomes.contains(event.getName().toString())) {
            event.getGeneration().func_242513_a(GenerationStage.Decoration.VEGETAL_DECORATION, FIRE_LILY_CF);
        }
        if (BiomeConfig.lightningLilyBiomes.contains(event.getName().toString())) {
            event.getGeneration().func_242513_a(GenerationStage.Decoration.VEGETAL_DECORATION, LIGHTNING_LILY_CF);
        }
        if (BiomeConfig.iceLilyBiomes.contains(event.getName().toString())) {
            event.getGeneration().func_242513_a(GenerationStage.Decoration.VEGETAL_DECORATION, FROST_LILY_CF);
        }
        if (BiomeConfig.oreGenBiomes.contains(event.getName().toString())) {
            if (IafConfig.generateSilverOre) {
                event.getGeneration().func_242513_a(GenerationStage.Decoration.UNDERGROUND_ORES, SILVER_ORE_CF);
            }
            if (IafConfig.generateCopperOre) {
                event.getGeneration().func_242513_a(GenerationStage.Decoration.UNDERGROUND_ORES, COPPER_ORE_CF);
            }
        }
        if (IafConfig.generateSapphireOre && BiomeConfig.sapphireBiomes.contains(event.getName().toString())) {
            event.getGeneration().func_242513_a(GenerationStage.Decoration.UNDERGROUND_ORES, SAPPHIRE_ORE_CF);
        }
        if (IafConfig.generateAmythestOre && BiomeConfig.amethystBiomes.contains(event.getName().toString())) {
            event.getGeneration().func_242513_a(GenerationStage.Decoration.UNDERGROUND_ORES, AMETHYST_ORE_CF);
        }
        if (IafConfig.generateDragonRoosts) {
            if (BiomeConfig.fireDragonBiomes.contains(event.getName().toString())) {
                event.getGeneration().func_242513_a(GenerationStage.Decoration.SURFACE_STRUCTURES, FIRE_DRAGON_ROOST_CF);
                event.getGeneration().func_242513_a(GenerationStage.Decoration.UNDERGROUND_STRUCTURES, FIRE_DRAGON_CAVE_CF);
            }
            if (BiomeConfig.lightningDragonBiomes.contains(event.getName().toString())) {
                event.getGeneration().func_242513_a(GenerationStage.Decoration.SURFACE_STRUCTURES, LIGHTNING_DRAGON_ROOST_CF);
                event.getGeneration().func_242513_a(GenerationStage.Decoration.UNDERGROUND_STRUCTURES, LIGHTNING_DRAGON_CAVE_CF);
            }
            if (BiomeConfig.iceDragonBiomes.contains(event.getName().toString())) {
                event.getGeneration().func_242513_a(GenerationStage.Decoration.SURFACE_STRUCTURES, ICE_DRAGON_ROOST_CF);
                event.getGeneration().func_242513_a(GenerationStage.Decoration.UNDERGROUND_STRUCTURES, ICE_DRAGON_CAVE_CF);
            }
        }
        if (IafConfig.generateCyclopsCaves && BiomeConfig.cyclopsCaveBiomes.contains(event.getName().toString())) {
            event.getGeneration().func_242513_a(GenerationStage.Decoration.SURFACE_STRUCTURES, CYCLOPS_CAVE_CF);
        }
        if (IafConfig.spawnGorgons && BiomeConfig.gorgonTempleBiomes.contains(event.getName().toString())) {
            event.getGeneration().func_242516_a(GORGON_TEMPLE_CF);
        }
        if (IafConfig.spawnPixies && BiomeConfig.pixieBiomes.contains(event.getName().toString())) {
            event.getGeneration().func_242513_a(GenerationStage.Decoration.SURFACE_STRUCTURES, PIXIE_VILLAGE_CF);
        }
        if (IafConfig.generateHydraCaves && BiomeConfig.hydraBiomes.contains(event.getName().toString())) {
            event.getGeneration().func_242513_a(GenerationStage.Decoration.SURFACE_STRUCTURES, HYDRA_CAVE_CF);
        }
        if (IafConfig.generateMausoleums && BiomeConfig.mausoleumBiomes.contains(event.getName().toString())) {
            event.getGeneration().func_242516_a(MAUSOLEUM_CF);
        }
        if (IafConfig.generateMausoleums && BiomeConfig.graveyardBiomes.contains(event.getName().toString())) {
            event.getGeneration().func_242516_a(GRAVEYARD_CF);
        }
        if (IafConfig.generateMyrmexColonies && BiomeConfig.desertMyrmexBiomes.contains(event.getName().toString())) {
            event.getGeneration().func_242513_a(GenerationStage.Decoration.SURFACE_STRUCTURES, MYRMEX_HIVE_DESERT_CF);
        }
        if (IafConfig.generateMyrmexColonies && BiomeConfig.jungleMyrmexBiomes.contains(event.getName().toString())) {
            event.getGeneration().func_242513_a(GenerationStage.Decoration.SURFACE_STRUCTURES, MYRMEX_HIVE_JUNGLE_CF);
        }
        if (IafConfig.generateMyrmexColonies && BiomeConfig.sirenBiomes.contains(event.getName().toString())) {
            event.getGeneration().func_242513_a(GenerationStage.Decoration.SURFACE_STRUCTURES, SIREN_ISLAND_CF);
        }
        if (BiomeConfig.overworldSpawnBiomes.contains(event.getName().toString())) {
            event.getGeneration().func_242513_a(GenerationStage.Decoration.SURFACE_STRUCTURES, MOB_SPAWNS_CF);
        }
    }

    public static String getBiomeName(Biome biome) {
        ResourceLocation loc = ForgeRegistries.BIOMES.getKey(biome);
        return loc == null ? "" : loc.toString();
    }
>>>>>>> aff892f7... server stuff... 2
}
