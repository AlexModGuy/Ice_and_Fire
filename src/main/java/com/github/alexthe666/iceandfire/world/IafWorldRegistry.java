package com.github.alexthe666.iceandfire.world;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.world.gen.*;
import com.github.alexthe666.iceandfire.world.structure.DreadMausoleumStructure;
import com.github.alexthe666.iceandfire.world.structure.GorgonTemplePiece;
import com.github.alexthe666.iceandfire.world.structure.GorgonTempleStructure;
import com.github.alexthe666.iceandfire.world.structure.MausoleumPiece;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.ShipwreckConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.placement.CountRangeConfig;
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
    public static Structure<NoFeatureConfig> GORGON_TEMPLE = new GorgonTempleStructure(NoFeatureConfig::deserialize);

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
        GORGON_TEMPLE = Registry.register(Registry.FEATURE, "iceandfire:gorgon_temple", GORGON_TEMPLE);
        Registry.register(Registry.STRUCTURE_FEATURE, "iceandfire:gorgon_temple", GORGON_TEMPLE);

    }

    public static void setup() {
        for (Biome biome : ForgeRegistries.BIOMES) {
            if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.OVERWORLD)) {
                biome.addFeature(GenerationStage.Decoration.LOCAL_MODIFICATIONS, MOB_SPAWNS.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
            }
            if (IafConfig.generateSilverOre) {
                biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, IafBlockRegistry.SILVER_ORE.getDefaultState(), 8)).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(2, 0, 0, 32))));
            }
            if (IafConfig.generateCopperOre) {
                biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, IafBlockRegistry.COPPER_ORE.getDefaultState(), 3)).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(4, 0, 0, 70))));
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
}
