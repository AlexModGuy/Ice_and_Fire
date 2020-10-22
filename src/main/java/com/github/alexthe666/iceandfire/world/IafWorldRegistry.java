package com.github.alexthe666.iceandfire.world;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.config.BiomeConfig;
import com.github.alexthe666.iceandfire.util.IAFBiomeUtil;
import com.github.alexthe666.iceandfire.world.gen.*;
import com.github.alexthe666.iceandfire.world.structure.*;
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
import net.minecraft.world.gen.feature.structure.PillagerOutpostStructure;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.placement.IPlacementConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

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
    public static Feature<NoFeatureConfig> MOB_SPAWNS;
    public static IStructurePieceType MAUSOLEUM_PIECE;
    public static Structure<NoFeatureConfig> MAUSOLEUM = new DreadMausoleumStructure(NoFeatureConfig.field_236558_a_);
    public static final Structure<NoFeatureConfig> MAUSOLEUM_SF = func_236394_a_("iceandfire:mausoleum", MAUSOLEUM, GenerationStage.Decoration.SURFACE_STRUCTURES);
    public static IStructurePieceType GORGON_PIECE;
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

        GRAVEYARD_PIECE = Registry.register(Registry.STRUCTURE_PIECE, "iceandfire:graveyard_piece", GraveyardPiece.Piece::new);
        GRAVEYARD = Registry.register(Registry.STRUCTURE_FEATURE, "iceandfire:graveyard", GRAVEYARD);
        putStructureOnAList("iceandfire:graveyard", GRAVEYARD);

        addStructureSeperation(DimensionSettings.field_242734_c, GORGON_TEMPLE, new StructureSeparationSettings(Math.max(IafConfig.spawnGorgonsChance, 2), Math.max(IafConfig.spawnGorgonsChance / 2, 1), 34222645));
        addStructureSeperation(DimensionSettings.field_242734_c, MAUSOLEUM, new StructureSeparationSettings(Math.max(IafConfig.generateMausoleumChance, 2), Math.max(IafConfig.generateMausoleumChance / 2, 1), 34222645));
        addStructureSeperation(DimensionSettings.field_242734_c, GRAVEYARD, new StructureSeparationSettings(Math.max(IafConfig.generateGraveyardChance, 2), Math.max(IafConfig.generateGraveyardChance / 2, 1), 34222644));

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

    public static void addStructureSeperation(RegistryKey<DimensionSettings> preset, Structure structure, StructureSeparationSettings settings) {
        WorldGenRegistries.field_243658_j.func_230516_a_(preset).func_236108_a_().func_236195_a_().put(structure, settings);
    }

    public static <F extends Structure<?>> void putStructureOnAList(String nameForList, F structure) {
        Structure.field_236365_a_.put(nameForList.toLowerCase(Locale.ROOT), structure);
    }

    private static <F extends Structure<?>> F func_236394_a_(String p_236394_0_, F p_236394_1_, GenerationStage.Decoration p_236394_2_) {
        return Registry.register(Registry.STRUCTURE_FEATURE, p_236394_0_.toLowerCase(Locale.ROOT), p_236394_1_);
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

    public static void onBiomesLoad(BiomeLoadingEvent event) {
    	Biome biome = ForgeRegistries.BIOMES.getValue(event.getName());

    	if (IAFBiomeUtil.biomeMeetsListConditions(biome, BiomeConfig.fireLilyBiomes)) {
            event.getGeneration().func_242513_a(GenerationStage.Decoration.VEGETAL_DECORATION, FIRE_LILY_CF);
        }
    	if (IAFBiomeUtil.biomeMeetsListConditions(biome, BiomeConfig.lightningLilyBiomes)) {
            event.getGeneration().func_242513_a(GenerationStage.Decoration.VEGETAL_DECORATION, LIGHTNING_LILY_CF);
        }
    	if (IAFBiomeUtil.biomeMeetsListConditions(biome, BiomeConfig.iceLilyBiomes)) {
            event.getGeneration().func_242513_a(GenerationStage.Decoration.VEGETAL_DECORATION, FROST_LILY_CF);
        }
    	if (IAFBiomeUtil.biomeMeetsListConditions(biome, BiomeConfig.oreGenBiomes)) {
            if (IafConfig.generateSilverOre) {
                event.getGeneration().func_242513_a(GenerationStage.Decoration.UNDERGROUND_ORES, SILVER_ORE_CF);
            }
            if (IafConfig.generateCopperOre) {
                event.getGeneration().func_242513_a(GenerationStage.Decoration.UNDERGROUND_ORES, COPPER_ORE_CF);
            }
        }
        if (IafConfig.generateSapphireOre && IAFBiomeUtil.biomeMeetsListConditions(biome, BiomeConfig.sapphireBiomes)) {
            event.getGeneration().func_242513_a(GenerationStage.Decoration.UNDERGROUND_ORES, SAPPHIRE_ORE_CF);
        }
        if (IafConfig.generateAmythestOre && IAFBiomeUtil.biomeMeetsListConditions(biome, BiomeConfig.amethystBiomes)) {
            event.getGeneration().func_242513_a(GenerationStage.Decoration.UNDERGROUND_ORES, AMETHYST_ORE_CF);
        }
        if (IafConfig.generateDragonRoosts) {
            if (IAFBiomeUtil.biomeMeetsListConditions(biome, BiomeConfig.fireDragonBiomes)) {
                event.getGeneration().func_242513_a(GenerationStage.Decoration.SURFACE_STRUCTURES, FIRE_DRAGON_ROOST_CF);
                event.getGeneration().func_242513_a(GenerationStage.Decoration.UNDERGROUND_STRUCTURES, FIRE_DRAGON_CAVE_CF);
            }
            if (IAFBiomeUtil.biomeMeetsListConditions(biome, BiomeConfig.lightningDragonBiomes)) {
                event.getGeneration().func_242513_a(GenerationStage.Decoration.SURFACE_STRUCTURES, LIGHTNING_DRAGON_ROOST_CF);
                event.getGeneration().func_242513_a(GenerationStage.Decoration.UNDERGROUND_STRUCTURES, LIGHTNING_DRAGON_CAVE_CF);
            }
            if (IAFBiomeUtil.biomeMeetsListConditions(biome, BiomeConfig.iceDragonBiomes)) {
                event.getGeneration().func_242513_a(GenerationStage.Decoration.SURFACE_STRUCTURES, ICE_DRAGON_ROOST_CF);
                event.getGeneration().func_242513_a(GenerationStage.Decoration.UNDERGROUND_STRUCTURES, ICE_DRAGON_CAVE_CF);
            }
        }
        if (IafConfig.generateCyclopsCaves && IAFBiomeUtil.biomeMeetsListConditions(biome, BiomeConfig.cyclopsCaveBiomes)) {
            event.getGeneration().func_242513_a(GenerationStage.Decoration.SURFACE_STRUCTURES, CYCLOPS_CAVE_CF);
        }
        if (IafConfig.spawnGorgons && IAFBiomeUtil.biomeMeetsListConditions(biome, BiomeConfig.gorgonTempleBiomes)) {
            event.getGeneration().func_242516_a(GORGON_TEMPLE_CF);
        }
        if (IafConfig.spawnPixies && IAFBiomeUtil.biomeMeetsListConditions(biome, BiomeConfig.pixieBiomes)) {
            event.getGeneration().func_242513_a(GenerationStage.Decoration.SURFACE_STRUCTURES, PIXIE_VILLAGE_CF);
        }
        if (IafConfig.generateHydraCaves && IAFBiomeUtil.biomeMeetsListConditions(biome, BiomeConfig.hydraBiomes)) {
            event.getGeneration().func_242513_a(GenerationStage.Decoration.SURFACE_STRUCTURES, HYDRA_CAVE_CF);
        }
        if (IafConfig.generateMausoleums && IAFBiomeUtil.biomeMeetsListConditions(biome, BiomeConfig.mausoleumBiomes)) {
            event.getGeneration().func_242516_a(MAUSOLEUM_CF);
        }
        if (IafConfig.generateGraveyards && IAFBiomeUtil.biomeMeetsListConditions(biome, BiomeConfig.graveyardBiomes)) {
            event.getGeneration().func_242516_a(GRAVEYARD_CF);
        }
        if (IafConfig.generateMyrmexColonies && IAFBiomeUtil.biomeMeetsListConditions(biome, BiomeConfig.desertMyrmexBiomes)) {
            event.getGeneration().func_242513_a(GenerationStage.Decoration.SURFACE_STRUCTURES, MYRMEX_HIVE_DESERT_CF);
        }
        if (IafConfig.generateMyrmexColonies && IAFBiomeUtil.biomeMeetsListConditions(biome, BiomeConfig.jungleMyrmexBiomes)) {
            event.getGeneration().func_242513_a(GenerationStage.Decoration.SURFACE_STRUCTURES, MYRMEX_HIVE_JUNGLE_CF);
        }
        if (IafConfig.generateMyrmexColonies && IAFBiomeUtil.biomeMeetsListConditions(biome, BiomeConfig.sirenBiomes)) {
            event.getGeneration().func_242513_a(GenerationStage.Decoration.SURFACE_STRUCTURES, SIREN_ISLAND_CF);
        }
        if (IAFBiomeUtil.biomeMeetsListConditions(biome, BiomeConfig.overworldSpawnBiomes)) {
            event.getGeneration().func_242513_a(GenerationStage.Decoration.SURFACE_STRUCTURES, MOB_SPAWNS_CF);
        }
    }
}
