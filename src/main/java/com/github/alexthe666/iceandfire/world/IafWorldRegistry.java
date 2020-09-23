package com.github.alexthe666.iceandfire.world;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.world.gen.*;
import com.github.alexthe666.iceandfire.world.structure.DreadMausoleumStructure;
import com.github.alexthe666.iceandfire.world.structure.GorgonTemplePiece;
import com.github.alexthe666.iceandfire.world.structure.GorgonTempleStructure;
import com.github.alexthe666.iceandfire.world.structure.MausoleumPiece;
import net.minecraft.block.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.DimensionSettings;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.blockplacer.SimpleBlockPlacer;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.PillagerOutpostStructure;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.FrequencyConfig;
import net.minecraft.world.gen.placement.IPlacementConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Locale;

public class IafWorldRegistry {

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
    public static Structure<NoFeatureConfig> MAUSOLEUM = new DreadMausoleumStructure(NoFeatureConfig.field_236558_a_);
    public static IStructurePieceType GORGON_PIECE;
    public static Structure<NoFeatureConfig> GORGON_TEMPLE = new GorgonTempleStructure(NoFeatureConfig.field_236558_a_);
    public static final Structure<NoFeatureConfig> MAUSOLEUM_SF = func_236394_a_("iceandfire:mausoleum", MAUSOLEUM, GenerationStage.Decoration.SURFACE_STRUCTURES);
    public static final Structure<NoFeatureConfig> GORGON_TEMPLE_SF = func_236394_a_("iceandfire:gorgon_temple", GORGON_TEMPLE, GenerationStage.Decoration.SURFACE_STRUCTURES);

    public static void register() {
        FIRE_DRAGON_ROOST = Registry.register(Registry.FEATURE, "iceandfire:fire_dragon_roost", new WorldGenFireDragonRoosts(NoFeatureConfig.field_236558_a_));
        ICE_DRAGON_ROOST = Registry.register(Registry.FEATURE, "iceandfire:ice_dragon_roost", new WorldGenIceDragonRoosts(NoFeatureConfig.field_236558_a_));
        LIGHTNING_DRAGON_ROOST = Registry.register(Registry.FEATURE, "iceandfire:lightning_dragon_roost", new WorldGenLightningDragonRoosts(NoFeatureConfig.field_236558_a_));
        FIRE_DRAGON_CAVE = Registry.register(Registry.FEATURE, "iceandfire:fire_dragon_cave", new WorldGenFireDragonCave(NoFeatureConfig.field_236558_a_));
        ICE_DRAGON_CAVE = Registry.register(Registry.FEATURE, "iceandfire:ice_dragon_cave", new WorldGenIceDragonCave(NoFeatureConfig.field_236558_a_));
        LIGHTNING_DRAGON_CAVE = Registry.register(Registry.FEATURE, "iceandfire:lightning_dragon_cave", new WorldGenLightningDragonCave(NoFeatureConfig.field_236558_a_));
        CYCLOPS_CAVE = Registry.register(Registry.FEATURE, "iceandfire:cyclops_cave", new WorldGenCyclopsCave(NoFeatureConfig.field_236558_a_));
        PIXIE_VILLAGE = Registry.register(Registry.FEATURE, "iceandfire:pixie_village", new WorldGenPixieVillage(NoFeatureConfig.field_236558_a_));
        SIREN_ISLAND = Registry.register(Registry.FEATURE, "iceandfire:siren_island", new WorldGenSirenIsland(NoFeatureConfig.field_236558_a_));
        HYDRA_CAVE = Registry.register(Registry.FEATURE, "iceandfire:hydra_cave", new WorldGenHydraCave(NoFeatureConfig.field_236558_a_));
        MYRMEX_HIVE_DESERT = Registry.register(Registry.FEATURE, "iceandfire:myrmex_hive_desert", new WorldGenMyrmexHive(false, false, NoFeatureConfig.field_236558_a_));
        MYRMEX_HIVE_JUNGLE = Registry.register(Registry.FEATURE, "iceandfire:myrmex_hive_jungle", new WorldGenMyrmexHive(false, true, NoFeatureConfig.field_236558_a_));
        MOB_SPAWNS = Registry.register(Registry.FEATURE, "iceandfire:mob_spawns", new WorldGenMobSpawn(NoFeatureConfig.field_236558_a_));
        MAUSOLEUM_PIECE = Registry.register(Registry.STRUCTURE_PIECE, "iceandfire:mausoleum_piece", MausoleumPiece.Piece::new);
        MAUSOLEUM = Registry.register(Registry.STRUCTURE_FEATURE, "iceandfire:mausoleum", MAUSOLEUM);
        putStructureOnAList("iceandfire:mausoleum", MAUSOLEUM);

        GORGON_PIECE = Registry.register(Registry.STRUCTURE_PIECE, "iceandfire:gorgon_piece", GorgonTemplePiece.Piece::new);
        GORGON_TEMPLE = Registry.register(Registry.STRUCTURE_FEATURE, "iceandfire:gorgon_temple", GORGON_TEMPLE);
        putStructureOnAList("iceandfire:gorgon_temple", GORGON_TEMPLE);
        addStructureSeperation(DimensionSettings.Preset.field_236122_b_, GORGON_TEMPLE, new StructureSeparationSettings(15, 7, 34222645));
        addStructureSeperation(DimensionSettings.Preset.field_236122_b_, MAUSOLEUM, new StructureSeparationSettings(8, 4, 34222645));
    }

    public static void addStructureSeperation(DimensionSettings.Preset preset, Structure structure, StructureSeparationSettings settings) {
        preset.func_236137_b_().func_236108_a_().func_236195_a_().put(structure, settings);
    }

    public static <F extends Structure<?>> void putStructureOnAList(String nameForList, F structure) {
        Structure.field_236365_a_.put(nameForList.toLowerCase(Locale.ROOT), structure);
    }

    private static <F extends Structure<?>> F func_236394_a_(String p_236394_0_, F p_236394_1_, GenerationStage.Decoration p_236394_2_) {
        return Registry.register(Registry.STRUCTURE_FEATURE, p_236394_0_.toLowerCase(Locale.ROOT), p_236394_1_);
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
                    biome.func_235063_a_(GORGON_TEMPLE.func_236391_a_(IFeatureConfig.NO_FEATURE_CONFIG));
                    biome.addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, CYCLOPS_CAVE.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
                }
                if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.SWAMP)) {
                    biome.addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, HYDRA_CAVE.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
                }
                if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.MAGICAL) || BiomeDictionary.hasType(biome, BiomeDictionary.Type.FOREST) && BiomeDictionary.hasType(biome, BiomeDictionary.Type.SPOOKY)) {
                    biome.addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, PIXIE_VILLAGE.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
                }
                if (biome.getPrecipitation() == Biome.RainType.SNOW || biome.getDefaultTemperature() < 0.0 && BiomeDictionary.hasType(biome, BiomeDictionary.Type.SNOWY)) {
                    biome.func_235063_a_(MAUSOLEUM.func_236391_a_(IFeatureConfig.NO_FEATURE_CONFIG));
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
        boolean spawnCheck = !spawnRelative.withinDistance(pos, IafConfig.dangerousWorldGenDistanceLimit);
        return spawnCheck;
    }

    public static boolean isDimensionListed(IWorld world) {
        ResourceLocation name = world.getWorld().func_234923_W_().func_240901_a_();
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
        ResourceLocation name = world.getWorld().func_234923_W_().func_240901_a_();
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
}
