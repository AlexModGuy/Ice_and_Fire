package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.enums.EnumDragonEgg;
import com.github.alexthe666.iceandfire.item.BlockItemWithRender;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.item.IafTabRegistry;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import net.minecraft.core.Direction;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = IceAndFire.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class IafBlockRegistry {

    public static DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, IceAndFire.MODID);

    public static final SoundType SOUND_TYPE_GOLD = new SoundType(1.0F, 1.0F, IafSoundRegistry.GOLD_PILE_BREAK, IafSoundRegistry.GOLD_PILE_STEP, IafSoundRegistry.GOLD_PILE_BREAK, IafSoundRegistry.GOLD_PILE_STEP, IafSoundRegistry.GOLD_PILE_STEP);

    public static final RegistryObject<Block> LECTERN = register("lectern", BlockLectern::new);
    public static final RegistryObject<Block> PODIUM_OAK = register("podium_oak", BlockPodium::new);
    public static final RegistryObject<Block> PODIUM_BIRCH = register("podium_birch", BlockPodium::new);
    public static final RegistryObject<Block> PODIUM_SPRUCE = register("podium_spruce", BlockPodium::new);
    public static final RegistryObject<Block> PODIUM_JUNGLE = register("podium_jungle", BlockPodium::new);
    public static final RegistryObject<Block> PODIUM_DARK_OAK = register("podium_dark_oak", BlockPodium::new);
    public static final RegistryObject<Block> PODIUM_ACACIA = register("podium_acacia", BlockPodium::new);
    public static final RegistryObject<Block> FIRE_LILY = register("fire_lily", BlockElementalFlower::new);
    public static final RegistryObject<Block> FROST_LILY = register("frost_lily", BlockElementalFlower::new);
    public static final RegistryObject<Block> LIGHTNING_LILY = register("lightning_lily", BlockElementalFlower::new);
    public static final RegistryObject<Block> GOLD_PILE = register("gold_pile", BlockGoldPile::new);
    public static final RegistryObject<Block> SILVER_PILE = register("silver_pile", BlockGoldPile::new);
    public static final RegistryObject<Block> COPPER_PILE = register("copper_pile", BlockGoldPile::new);
    public static final RegistryObject<Block> SILVER_ORE = register("silver_ore", () -> new DropExperienceBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).strength(3, 3).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> DEEPSLATE_SILVER_ORE = register("deepslate_silver_ore", () -> new DropExperienceBlock(BlockBehaviour.Properties.of().mapColor(MapColor.DEEPSLATE).strength(3,3).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> SILVER_BLOCK = register("silver_block", () -> BlockGeneric.builder(3.0F, 5.0F, SoundType.METAL, MapColor.METAL, null, null, false));
    public static final RegistryObject<Block> SAPPHIRE_ORE = register("sapphire_ore", () -> new DropExperienceBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).strength(4,3).requiresCorrectToolForDrops(), UniformInt.of(3,7)));
    public static final RegistryObject<Block> SAPPHIRE_BLOCK = register("sapphire_block", () -> BlockGeneric.builder(3.0F, 6.0F, SoundType.METAL, MapColor.METAL, null, null, false));
    public static final RegistryObject<Block> RAW_SILVER_BLOCK = register("raw_silver_block", () -> BlockGeneric.builder(3.0F, 5.0F, SoundType.STONE, MapColor.METAL, NoteBlockInstrument.BASEDRUM, null, false));
    public static final RegistryObject<Block> CHARRED_DIRT = register("chared_dirt", () -> BlockReturningState.builder(0.5F, 0.0F, SoundType.GRAVEL, MapColor.DIRT, null, null, false, Blocks.DIRT.defaultBlockState()));
    public static final RegistryObject<Block> CHARRED_GRASS = register("chared_grass", () -> BlockReturningState.builder(0.6F, 0.0F, SoundType.GRAVEL, MapColor.GRASS, null, null, false, Blocks.GRASS_BLOCK.defaultBlockState()));
    public static final RegistryObject<Block> CHARRED_STONE = register("chared_stone", () -> BlockReturningState.builder(1.5F, 10.0F, SoundType.STONE, MapColor.STONE, NoteBlockInstrument.BASEDRUM, null, false, Blocks.STONE.defaultBlockState()));
    public static final RegistryObject<Block> CHARRED_COBBLESTONE = register("chared_cobblestone", () -> BlockReturningState.builder(2F, 10.0F, SoundType.STONE, MapColor.STONE, NoteBlockInstrument.BASEDRUM, null, false, Blocks.COBBLESTONE.defaultBlockState()));
    public static final RegistryObject<Block> CHARRED_GRAVEL = register("chared_gravel", () -> new BlockFallingReturningState(0.6F, 0F, SoundType.GRAVEL, MapColor.DIRT, Blocks.GRAVEL.defaultBlockState()));
    public static final RegistryObject<Block> CHARRED_DIRT_PATH = register(BlockCharedPath.getNameFromType(0), () -> new BlockCharedPath(0));
    public static final RegistryObject<Block> ASH = register("ash", () -> BlockFallingGeneric.builder(0.5F, 0F, SoundType.SAND, MapColor.SAND, NoteBlockInstrument.SNARE));
    public static final RegistryObject<Block> FROZEN_DIRT = register("frozen_dirt", () -> BlockReturningState.builder(0.5F, 0.0F, SoundType.GLASS, true, MapColor.DIRT, null, null, false, Blocks.DIRT.defaultBlockState()));
    public static final RegistryObject<Block> FROZEN_GRASS = register("frozen_grass", () -> BlockReturningState.builder(0.6F, 0.0F, SoundType.GLASS, true, MapColor.GRASS, null, null, false, Blocks.GRASS_BLOCK.defaultBlockState()));
    public static final RegistryObject<Block> FROZEN_STONE = register("frozen_stone", () -> BlockReturningState.builder(1.5F, 1.0F, SoundType.GLASS, true, MapColor.STONE, NoteBlockInstrument.BASEDRUM, null, false, Blocks.STONE.defaultBlockState()));
    public static final RegistryObject<Block> FROZEN_COBBLESTONE = register("frozen_cobblestone", () -> BlockReturningState.builder(2F, 2.0F, SoundType.GLASS, true, MapColor.STONE, NoteBlockInstrument.BASEDRUM, null, false, Blocks.COBBLESTONE.defaultBlockState()));
    public static final RegistryObject<Block> FROZEN_GRAVEL = register("frozen_gravel", () -> new BlockFallingReturningState(0.6F, 0F, SoundType.GLASS, true, MapColor.DIRT, Blocks.GRAVEL.defaultBlockState()));
    public static final RegistryObject<Block> FROZEN_DIRT_PATH = register(BlockCharedPath.getNameFromType(1), () -> new BlockCharedPath(1));
    public static final RegistryObject<Block> FROZEN_SPLINTERS = register("frozen_splinters", () -> BlockGeneric.builder(2.0F, 1.0F, SoundType.GLASS, true, MapColor.WOOD, NoteBlockInstrument.BASS, null, true));
    public static final RegistryObject<Block> DRAGON_ICE = register("dragon_ice", () -> BlockGeneric.builder(0.5F, 0F, SoundType.GLASS, true, MapColor.ICE, null, null, false));
    public static final RegistryObject<Block> DRAGON_ICE_SPIKES = register("dragon_ice_spikes", BlockIceSpikes::new);
    public static final RegistryObject<Block> CRACKLED_DIRT = register("crackled_dirt", () -> BlockReturningState.builder(0.5F, 0.0F, SoundType.GRAVEL, MapColor.DIRT, null, null, false, Blocks.DIRT.defaultBlockState()));
    public static final RegistryObject<Block> CRACKLED_GRASS = register("crackled_grass", () -> BlockReturningState.builder(0.6F, 0.0F, SoundType.GRAVEL, MapColor.GRASS, null, null, false, Blocks.GRASS_BLOCK.defaultBlockState()));
    public static final RegistryObject<Block> CRACKLED_STONE = register("crackled_stone", () -> BlockReturningState.builder(1.5F, 1.0F, SoundType.STONE, MapColor.STONE, NoteBlockInstrument.BASEDRUM, null, false, Blocks.STONE.defaultBlockState()));
    public static final RegistryObject<Block> CRACKLED_COBBLESTONE = register("crackled_cobblestone", () -> BlockReturningState.builder(2F, 2F, SoundType.STONE, MapColor.STONE, NoteBlockInstrument.BASEDRUM, null, false, Blocks.COBBLESTONE.defaultBlockState()));
    public static final RegistryObject<Block> CRACKLED_GRAVEL = register("crackled_gravel", () -> new BlockFallingReturningState(0.6F, 0F, SoundType.GRAVEL, MapColor.DIRT, Blocks.GRAVEL.defaultBlockState()));
    public static final RegistryObject<Block> CRACKLED_DIRT_PATH = register(BlockCharedPath.getNameFromType(2), () -> new BlockCharedPath(2));

    public static final RegistryObject<Block> NEST = register("nest", () -> BlockGeneric.builder(0.5F, 0F, SoundType.GRAVEL, false, MapColor.PLANT, null, PushReaction.DESTROY, false));

    public static final RegistryObject<Block> DRAGON_SCALE_RED = register("dragonscale_red", () -> new BlockDragonScales(EnumDragonEgg.RED));
    public static final RegistryObject<Block> DRAGON_SCALE_GREEN = register("dragonscale_green", () -> new BlockDragonScales(EnumDragonEgg.GREEN));
    public static final RegistryObject<Block> DRAGON_SCALE_BRONZE = register("dragonscale_bronze", () -> new BlockDragonScales(EnumDragonEgg.BRONZE));
    public static final RegistryObject<Block> DRAGON_SCALE_GRAY = register("dragonscale_gray", () -> new BlockDragonScales(EnumDragonEgg.GRAY));
    public static final RegistryObject<Block> DRAGON_SCALE_BLUE = register("dragonscale_blue", () -> new BlockDragonScales(EnumDragonEgg.BLUE));
    public static final RegistryObject<Block> DRAGON_SCALE_WHITE = register("dragonscale_white", () -> new BlockDragonScales(EnumDragonEgg.WHITE));
    public static final RegistryObject<Block> DRAGON_SCALE_SAPPHIRE = register("dragonscale_sapphire", () -> new BlockDragonScales(EnumDragonEgg.SAPPHIRE));
    public static final RegistryObject<Block> DRAGON_SCALE_SILVER = register("dragonscale_silver", () -> new BlockDragonScales(EnumDragonEgg.SILVER));
    public static final RegistryObject<Block> DRAGON_SCALE_ELECTRIC = register("dragonscale_electric", () -> new BlockDragonScales(EnumDragonEgg.ELECTRIC));
    public static final RegistryObject<Block> DRAGON_SCALE_AMYTHEST = register("dragonscale_amythest", () -> new BlockDragonScales(EnumDragonEgg.AMYTHEST));
    public static final RegistryObject<Block> DRAGON_SCALE_COPPER = register("dragonscale_copper", () -> new BlockDragonScales(EnumDragonEgg.COPPER));
    public static final RegistryObject<Block> DRAGON_SCALE_BLACK = register("dragonscale_black", () -> new BlockDragonScales(EnumDragonEgg.BLACK));

    public static final RegistryObject<Block> DRAGON_BONE_BLOCK = register("dragon_bone_block", BlockDragonBone::new);
    public static final RegistryObject<Block> DRAGON_BONE_BLOCK_WALL = register("dragon_bone_wall", () -> new BlockDragonBoneWall(BlockBehaviour.Properties.copy(IafBlockRegistry.DRAGON_BONE_BLOCK.get())));
    public static final RegistryObject<Block> DRAGONFORGE_FIRE_BRICK = register(BlockDragonforgeBricks.name(0), () -> new BlockDragonforgeBricks(0));
    public static final RegistryObject<Block> DRAGONFORGE_ICE_BRICK = register(BlockDragonforgeBricks.name(1), () -> new BlockDragonforgeBricks(1));
    public static final RegistryObject<Block> DRAGONFORGE_LIGHTNING_BRICK = register(BlockDragonforgeBricks.name(2), () -> new BlockDragonforgeBricks(2));
    public static final RegistryObject<Block> DRAGONFORGE_FIRE_INPUT = register(BlockDragonforgeInput.name(0), () -> new BlockDragonforgeInput(0));
    public static final RegistryObject<Block> DRAGONFORGE_ICE_INPUT = register(BlockDragonforgeInput.name(1), () -> new BlockDragonforgeInput(1));
    public static final RegistryObject<Block> DRAGONFORGE_LIGHTNING_INPUT = register(BlockDragonforgeInput.name(2), () -> new BlockDragonforgeInput(2));
    public static final RegistryObject<Block> DRAGONFORGE_FIRE_CORE = register(BlockDragonforgeCore.name(0, true), () -> new BlockDragonforgeCore(0, true));
    public static final RegistryObject<Block> DRAGONFORGE_ICE_CORE = register(BlockDragonforgeCore.name(1, true), () -> new BlockDragonforgeCore(1, true));
    public static final RegistryObject<Block> DRAGONFORGE_LIGHTNING_CORE = register(BlockDragonforgeCore.name(2, true), () -> new BlockDragonforgeCore(2, true));
    public static final RegistryObject<Block> DRAGONFORGE_FIRE_CORE_DISABLED = register(BlockDragonforgeCore.name(0, false), () -> new BlockDragonforgeCore(0, false));
    public static final RegistryObject<Block> DRAGONFORGE_ICE_CORE_DISABLED = register(BlockDragonforgeCore.name(1, false), () -> new BlockDragonforgeCore(1, false));
    public static final RegistryObject<Block> DRAGONFORGE_LIGHTNING_CORE_DISABLED = register(BlockDragonforgeCore.name(2, false), () -> new BlockDragonforgeCore(2, false));
    public static final RegistryObject<Block> EGG_IN_ICE = register("egginice", BlockEggInIce::new);
    public static final RegistryObject<Block> PIXIE_HOUSE_MUSHROOM_RED = registerWithRender(BlockPixieHouse.name("mushroom_red"), BlockPixieHouse::new);
    public static final RegistryObject<Block> PIXIE_HOUSE_MUSHROOM_BROWN = registerWithRender(BlockPixieHouse.name("mushroom_brown"), BlockPixieHouse::new);
    public static final RegistryObject<Block> PIXIE_HOUSE_OAK = registerWithRender(BlockPixieHouse.name("oak"), BlockPixieHouse::new);
    public static final RegistryObject<Block> PIXIE_HOUSE_BIRCH = registerWithRender(BlockPixieHouse.name("birch"), BlockPixieHouse::new);
    public static final RegistryObject<Block> PIXIE_HOUSE_SPRUCE = registerWithRender(BlockPixieHouse.name("spruce"), BlockPixieHouse::new);
    public static final RegistryObject<Block> PIXIE_HOUSE_DARK_OAK = registerWithRender(BlockPixieHouse.name("dark_oak"), BlockPixieHouse::new);
    public static final RegistryObject<Block> JAR_EMPTY = register(BlockJar.name(-1), () -> new BlockJar(-1));
    public static final RegistryObject<Block> JAR_PIXIE_0 = register(BlockJar.name(0), () -> new BlockJar(0));
    public static final RegistryObject<Block> JAR_PIXIE_1 = register(BlockJar.name(1), () -> new BlockJar(1));
    public static final RegistryObject<Block> JAR_PIXIE_2 = register(BlockJar.name(2), () -> new BlockJar(2));
    public static final RegistryObject<Block> JAR_PIXIE_3 = register(BlockJar.name(3), () -> new BlockJar(3));
    public static final RegistryObject<Block> JAR_PIXIE_4 = register(BlockJar.name(4), () -> new BlockJar(4));
    public static final RegistryObject<Block> MYRMEX_DESERT_RESIN = register(BlockMyrmexResin.name(false, "desert"), () -> new BlockMyrmexResin(false));
    public static final RegistryObject<Block> MYRMEX_DESERT_RESIN_STICKY = register(BlockMyrmexResin.name(true, "desert"), () -> new BlockMyrmexResin(true));
    public static final RegistryObject<Block> MYRMEX_JUNGLE_RESIN = register(BlockMyrmexResin.name(false, "jungle"), () -> new BlockMyrmexResin(false));
    public static final RegistryObject<Block> MYRMEX_JUNGLE_RESIN_STICKY = register(BlockMyrmexResin.name(true, "jungle"), () -> new BlockMyrmexResin(true));
    public static final RegistryObject<Block> DESERT_MYRMEX_COCOON = register("desert_myrmex_cocoon", BlockMyrmexCocoon::new);
    public static final RegistryObject<Block> JUNGLE_MYRMEX_COCOON = register("jungle_myrmex_cocoon", BlockMyrmexCocoon::new);
    public static final RegistryObject<Block> MYRMEX_DESERT_BIOLIGHT = register("myrmex_desert_biolight", BlockMyrmexBiolight::new);
    public static final RegistryObject<Block> MYRMEX_JUNGLE_BIOLIGHT = register("myrmex_jungle_biolight", BlockMyrmexBiolight::new);
    public static final RegistryObject<Block> MYRMEX_DESERT_RESIN_BLOCK = register(BlockMyrmexConnectedResin.name(false, false), () -> new BlockMyrmexConnectedResin(false, false));
    public static final RegistryObject<Block> MYRMEX_JUNGLE_RESIN_BLOCK = register(BlockMyrmexConnectedResin.name(true, false), () -> new BlockMyrmexConnectedResin(true, false));
    public static final RegistryObject<Block> MYRMEX_DESERT_RESIN_GLASS = register(BlockMyrmexConnectedResin.name(false, true), () -> new BlockMyrmexConnectedResin(false, true));
    public static final RegistryObject<Block> MYRMEX_JUNGLE_RESIN_GLASS = register(BlockMyrmexConnectedResin.name(true, true), () -> new BlockMyrmexConnectedResin(true, true));
    public static final RegistryObject<Block> DRAGONSTEEL_FIRE_BLOCK = register("dragonsteel_fire_block", () -> BlockGeneric.builder(10.0F, 1000.0F, SoundType.METAL, MapColor.METAL, null, null, false));
    public static final RegistryObject<Block> DRAGONSTEEL_ICE_BLOCK = register("dragonsteel_ice_block", () -> BlockGeneric.builder(10.0F, 1000.0F, SoundType.METAL, MapColor.METAL, null, null, false));
    public static final RegistryObject<Block> DRAGONSTEEL_LIGHTNING_BLOCK = register("dragonsteel_lightning_block", () -> BlockGeneric.builder(10.0F, 1000.0F, SoundType.METAL, MapColor.METAL, null, null, false));
    public static final RegistryObject<BlockDreadBase> DREAD_STONE = register("dread_stone", () -> BlockDreadBase.builder(-1.0F, 100000.0F, SoundType.STONE, MapColor.STONE, null, false));
    public static final RegistryObject<BlockDreadBase> DREAD_STONE_BRICKS = register("dread_stone_bricks", () -> BlockDreadBase.builder(-1.0F, 100000.0F, SoundType.STONE, MapColor.STONE, null, false));
    public static final RegistryObject<BlockDreadBase> DREAD_STONE_BRICKS_CHISELED = register("dread_stone_bricks_chiseled", () -> BlockDreadBase.builder(-1.0F, 100000.0F, SoundType.STONE, MapColor.STONE, null, false));
    public static final RegistryObject<BlockDreadBase> DREAD_STONE_BRICKS_CRACKED = register("dread_stone_bricks_cracked", () -> BlockDreadBase.builder(-1.0F, 100000.0F, SoundType.STONE, MapColor.STONE, null, false));
    public static final RegistryObject<BlockDreadBase> DREAD_STONE_BRICKS_MOSSY = register("dread_stone_bricks_mossy", () -> BlockDreadBase.builder(-1.0F, 100000.0F, SoundType.STONE, MapColor.STONE, null, false));
    public static final RegistryObject<BlockDreadBase> DREAD_STONE_TILE = register("dread_stone_tile", () -> BlockDreadBase.builder(-1.0F, 100000.0F, SoundType.STONE, MapColor.STONE, null, false));
    public static final RegistryObject<Block> DREAD_STONE_FACE = register("dread_stone_face", BlockDreadStoneFace::new);
    public static final RegistryObject<TorchBlock> DREAD_TORCH = registerWallBlock("dread_torch", BlockDreadTorch::new);
    public static final RegistryObject<BlockDreadTorchWall> DREAD_TORCH_WALL = registerWallTorch("dread_torch_wall", BlockDreadTorchWall::new);
    public static final RegistryObject<Block> DREAD_STONE_BRICKS_STAIRS = register("dread_stone_stairs", () -> new BlockGenericStairs(DREAD_STONE_BRICKS.get().defaultBlockState()));
    public static final RegistryObject<Block> DREAD_STONE_BRICKS_SLAB = register("dread_stone_slab", () -> new SlabBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).strength(10F, 10000F)));
    public static final RegistryObject<Block> DREADWOOD_LOG = register("dreadwood_log", BlockDreadWoodLog::new);
    public static final RegistryObject<BlockDreadBase> DREADWOOD_PLANKS = register("dreadwood_planks", () -> BlockDreadBase.builder(-1.0F, 100000.0F, SoundType.WOOD, MapColor.WOOD, NoteBlockInstrument.BASS, true));
    public static final RegistryObject<Block> DREADWOOD_PLANKS_LOCK = register("dreadwood_planks_lock", BlockDreadWoodLock::new);
    public static final RegistryObject<Block> DREAD_PORTAL = registerWithRender("dread_portal", BlockDreadPortal::new);
    public static final RegistryObject<Block> DREAD_SPAWNER = register("dread_spawner", BlockDreadSpawner::new);
    public static final RegistryObject<TorchBlock> BURNT_TORCH = registerWallBlock("burnt_torch", BlockBurntTorch::new);
    public static final RegistryObject<BlockBurntTorchWall> BURNT_TORCH_WALL = registerWallTorch("burnt_torch_wall", BlockBurntTorchWall::new);
    public static final RegistryObject<Block> GHOST_CHEST = registerWithRender("ghost_chest", BlockGhostChest::new);
    public static final RegistryObject<Block> GRAVEYARD_SOIL = register("graveyard_soil", BlockGraveyardSoil::new);


    public static <T extends Block> RegistryObject<T> register(String name, Supplier<T> block) {
        RegistryObject<T> ret = BLOCKS.register(name, block);
        IafItemRegistry.registerItem(name, () -> new BlockItem(ret.get(), new Item.Properties()), false);
        IafTabRegistry.TAB_BLOCKS_LIST.add(ret);
        return ret;
    }

    public static <T extends TorchBlock> RegistryObject<T> registerWallBlock(String name, Supplier<T> block) {
        RegistryObject<T> ret = BLOCKS.register(name, block);
        IafItemRegistry.registerItem(name, () -> new StandingAndWallBlockItem(ret.get(), ((IWallBlock) ret.get()).wallBlock(), new Item.Properties(), Direction.DOWN), false);
        IafTabRegistry.TAB_BLOCKS_LIST.add(ret);
        return ret;
    }

    public static <T extends Block> RegistryObject<T> registerWithRender(String name, Supplier<T> block) {
        RegistryObject<T> ret = BLOCKS.register(name, block);
        IafItemRegistry.registerItem(name, () -> new BlockItemWithRender(ret.get(), new Item.Properties()), false);
        IafTabRegistry.TAB_BLOCKS_LIST.add(ret);
        return ret;
    }

    public static <T extends WallTorchBlock> RegistryObject<T> registerWallTorch(String name, Supplier<T> block) {
        return BLOCKS.register(name, block);
    }

}
