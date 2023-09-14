package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.enums.EnumDragonEgg;
import com.github.alexthe666.iceandfire.item.BlockItemWithRender;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.*;

import java.util.Optional;

@Mod.EventBusSubscriber(modid = IceAndFire.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class IafBlockRegistry {

    public static DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, IceAndFire.MODID);

    public static final SoundType SOUND_TYPE_GOLD = new SoundType(1.0F, 1.0F, IafSoundRegistry.GOLD_PILE_BREAK, IafSoundRegistry.GOLD_PILE_STEP, IafSoundRegistry.GOLD_PILE_BREAK, IafSoundRegistry.GOLD_PILE_STEP, IafSoundRegistry.GOLD_PILE_STEP);

    public static final RegistryObject<Block> LECTERN = BLOCKS.register("lectern", BlockLectern::new);
    public static final RegistryObject<Block> PODIUM_OAK = BLOCKS.register("podium_oak", BlockPodium::new);
    public static final RegistryObject<Block> PODIUM_BIRCH = BLOCKS.register("podium_birch", BlockPodium::new);
    public static final RegistryObject<Block> PODIUM_SPRUCE = BLOCKS.register("podium_spruce", BlockPodium::new);
    public static final RegistryObject<Block> PODIUM_JUNGLE = BLOCKS.register("podium_jungle", BlockPodium::new);
    public static final RegistryObject<Block> PODIUM_DARK_OAK = BLOCKS.register("podium_dark_oak", BlockPodium::new);
    public static final RegistryObject<Block> PODIUM_ACACIA = BLOCKS.register("podium_acacia", BlockPodium::new);
    public static final RegistryObject<Block> FIRE_LILY = BLOCKS.register("fire_lily", BlockElementalFlower::new);
    public static final RegistryObject<Block> FROST_LILY = BLOCKS.register("frost_lily", BlockElementalFlower::new);
    public static final RegistryObject<Block> LIGHTNING_LILY = BLOCKS.register("lightning_lily", BlockElementalFlower::new);
    public static final RegistryObject<Block> GOLD_PILE = BLOCKS.register("gold_pile", BlockGoldPile::new);
    public static final RegistryObject<Block> SILVER_PILE = BLOCKS.register("silver_pile", BlockGoldPile::new);
    public static final RegistryObject<Block> COPPER_PILE = BLOCKS.register("copper_pile", BlockGoldPile::new);
    public static final RegistryObject<Block> SILVER_ORE = BLOCKS.register("silver_ore", () -> new BlockIafOre(2, 3.0F, 3.0F));
    public static final RegistryObject<Block> SAPPHIRE_ORE = BLOCKS.register("sapphire_ore", () -> new BlockIafOre(2, 4.0F, 3.0F));
    public static final RegistryObject<Block> COPPER_ORE = BLOCKS.register("copper_ore", () -> new BlockIafOre(0, 3.0F, 3.0F));
    public static final RegistryObject<Block> AMYTHEST_ORE = BLOCKS.register("amythest_ore", () -> new BlockIafOre(2, 4.0F, 3.0F));
    public static final RegistryObject<Block> SILVER_BLOCK = BLOCKS.register("silver_block", () -> BlockGeneric.builder(3.0F, 5.0F, SoundType.METAL, MapColor.METAL, null, null, false));
    public static final RegistryObject<Block> SAPPHIRE_BLOCK = BLOCKS.register("sapphire_block", () -> BlockGeneric.builder(3.0F, 6.0F, SoundType.METAL, MapColor.METAL, null, null, false));
    public static final RegistryObject<Block> COPPER_BLOCK = BLOCKS.register("copper_block", () -> BlockGeneric.builder(4.0F, 5.0F, SoundType.METAL, MapColor.METAL, null, null, false));
    public static final RegistryObject<Block> AMYTHEST_BLOCK = BLOCKS.register("amythest_block", () -> BlockGeneric.builder(5.0F, 6.0F, SoundType.METAL, MapColor.METAL, null, null, false));
    public static final RegistryObject<Block> CHARRED_DIRT = BLOCKS.register("chared_dirt", () -> BlockReturningState.builder(0.5F, 0.0F, SoundType.GRAVEL, MapColor.DIRT, null, null, false, Blocks.DIRT.defaultBlockState()));
    public static final RegistryObject<Block> CHARRED_GRASS = BLOCKS.register("chared_grass", () -> BlockReturningState.builder(0.6F, 0.0F, SoundType.GRAVEL, MapColor.GRASS, null, null, false, Blocks.GRASS_BLOCK.defaultBlockState()));
    public static final RegistryObject<Block> CHARRED_STONE = BLOCKS.register("chared_stone", () -> BlockReturningState.builder(1.5F, 10.0F, SoundType.STONE, MapColor.STONE, NoteBlockInstrument.BASEDRUM, null, false, Blocks.STONE.defaultBlockState()));
    public static final RegistryObject<Block> CHARRED_COBBLESTONE = BLOCKS.register("chared_cobblestone", () -> BlockReturningState.builder(2F, 10.0F, SoundType.STONE, MapColor.STONE, NoteBlockInstrument.BASEDRUM, null, false, Blocks.COBBLESTONE.defaultBlockState()));
    public static final RegistryObject<Block> CHARRED_GRAVEL = BLOCKS.register("chared_gravel", () -> new BlockFallingReturningState(0.6F, 0F, SoundType.GRAVEL, MapColor.DIRT, Blocks.GRAVEL.defaultBlockState()));
    public static final RegistryObject<Block> CHARRED_DIRT_PATH = BLOCKS.register(BlockCharedPath.getNameFromType(0), () -> new BlockCharedPath(0));
    public static final RegistryObject<Block> ASH = BLOCKS.register("ash", () -> BlockFallingGeneric.builder(0.5F, 0F, SoundType.SAND, MapColor.SAND, NoteBlockInstrument.SNARE));
    public static final RegistryObject<Block> FROZEN_DIRT = BLOCKS.register("frozen_dirt", () -> BlockReturningState.builder(0.5F, 0.0F, SoundType.GLASS, true, MapColor.DIRT, null, null, false, Blocks.DIRT.defaultBlockState()));
    public static final RegistryObject<Block> FROZEN_GRASS = BLOCKS.register("frozen_grass", () -> BlockReturningState.builder(0.6F, 0.0F, SoundType.GLASS, true, MapColor.GRASS, null, null, false, Blocks.GRASS_BLOCK.defaultBlockState()));
    public static final RegistryObject<Block> FROZEN_STONE = BLOCKS.register("frozen_stone", () -> BlockReturningState.builder(1.5F, 1.0F, SoundType.GLASS, true, MapColor.STONE, NoteBlockInstrument.BASEDRUM, null, false, Blocks.STONE.defaultBlockState()));
    public static final RegistryObject<Block> FROZEN_COBBLESTONE = BLOCKS.register("frozen_cobblestone", () -> BlockReturningState.builder(2F, 2.0F, SoundType.GLASS, true, MapColor.STONE, NoteBlockInstrument.BASEDRUM, null, false, Blocks.COBBLESTONE.defaultBlockState()));
    public static final RegistryObject<Block> FROZEN_GRAVEL = BLOCKS.register("frozen_gravel", () -> new BlockFallingReturningState(0.6F, 0F, SoundType.GLASS, true, MapColor.DIRT, Blocks.GRAVEL.defaultBlockState()));
    public static final RegistryObject<Block> FROZEN_DIRT_PATH = BLOCKS.register(BlockCharedPath.getNameFromType(1), () -> new BlockCharedPath(1));
    public static final RegistryObject<Block> FROZEN_SPLINTERS = BLOCKS.register("frozen_splinters", () -> BlockGeneric.builder(2.0F, 1.0F, SoundType.GLASS, true, MapColor.WOOD, NoteBlockInstrument.BASS, null, true));
    public static final RegistryObject<Block> DRAGON_ICE = BLOCKS.register("dragon_ice", () -> BlockGeneric.builder(0.5F, 0F, SoundType.GLASS, true, MapColor.ICE, null, null, false));
    public static final RegistryObject<Block> DRAGON_ICE_SPIKES = BLOCKS.register("dragon_ice_spikes", BlockIceSpikes::new);
    public static final RegistryObject<Block> CRACKLED_DIRT = BLOCKS.register("crackled_dirt", () -> BlockReturningState.builder(0.5F, 0.0F, SoundType.GRAVEL, MapColor.DIRT, null, null, false, Blocks.DIRT.defaultBlockState()));
    public static final RegistryObject<Block> CRACKLED_GRASS = BLOCKS.register("crackled_grass", () -> BlockReturningState.builder(0.6F, 0.0F, SoundType.GRAVEL, MapColor.GRASS, null, null, false, Blocks.GRASS_BLOCK.defaultBlockState()));
    public static final RegistryObject<Block> CRACKLED_STONE = BLOCKS.register("crackled_stone", () -> BlockReturningState.builder(1.5F, 1.0F, SoundType.STONE, MapColor.STONE, NoteBlockInstrument.BASEDRUM, null, false, Blocks.STONE.defaultBlockState()));
    public static final RegistryObject<Block> CRACKLED_COBBLESTONE = BLOCKS.register("crackled_cobblestone", () -> BlockReturningState.builder(2F, 2F, SoundType.STONE, MapColor.STONE, NoteBlockInstrument.BASEDRUM, null, false, Blocks.COBBLESTONE.defaultBlockState()));
    public static final RegistryObject<Block> CRACKLED_GRAVEL = BLOCKS.register("crackled_gravel", () -> new BlockFallingReturningState(0.6F, 0F, SoundType.GRAVEL, MapColor.DIRT, Blocks.GRAVEL.defaultBlockState()));
    public static final RegistryObject<Block> CRACKLED_DIRT_PATH = BLOCKS.register(BlockCharedPath.getNameFromType(2), () -> new BlockCharedPath(2));

    public static final RegistryObject<Block> NEST = BLOCKS.register("nest", () -> BlockGeneric.builder(0.5F, 0F, SoundType.GRAVEL, false, MapColor.PLANT, null, PushReaction.DESTROY, false));

    public static final RegistryObject<Block> DRAGON_SCALE_RED = BLOCKS.register("dragonscale_red", () -> new BlockDragonScales(EnumDragonEgg.RED));
    public static final RegistryObject<Block> DRAGON_SCALE_GREEN = BLOCKS.register("dragonscale_green", () -> new BlockDragonScales(EnumDragonEgg.GREEN));
    public static final RegistryObject<Block> DRAGON_SCALE_BRONZE = BLOCKS.register("dragonscale_bronze", () -> new BlockDragonScales(EnumDragonEgg.BRONZE));
    public static final RegistryObject<Block> DRAGON_SCALE_GRAY = BLOCKS.register("dragonscale_gray", () -> new BlockDragonScales(EnumDragonEgg.GRAY));
    public static final RegistryObject<Block> DRAGON_SCALE_BLUE = BLOCKS.register("dragonscale_blue", () -> new BlockDragonScales(EnumDragonEgg.BLUE));
    public static final RegistryObject<Block> DRAGON_SCALE_WHITE = BLOCKS.register("dragonscale_white", () -> new BlockDragonScales(EnumDragonEgg.WHITE));
    public static final RegistryObject<Block> DRAGON_SCALE_SAPPHIRE = BLOCKS.register("dragonscale_sapphire", () -> new BlockDragonScales(EnumDragonEgg.SAPPHIRE));
    public static final RegistryObject<Block> DRAGON_SCALE_SILVER = BLOCKS.register("dragonscale_silver", () -> new BlockDragonScales(EnumDragonEgg.SILVER));
    public static final RegistryObject<Block> DRAGON_SCALE_ELECTRIC = BLOCKS.register("dragonscale_electric", () -> new BlockDragonScales(EnumDragonEgg.ELECTRIC));
    public static final RegistryObject<Block> DRAGON_SCALE_AMYTHEST = BLOCKS.register("dragonscale_amythest", () -> new BlockDragonScales(EnumDragonEgg.AMYTHEST));
    public static final RegistryObject<Block> DRAGON_SCALE_COPPER = BLOCKS.register("dragonscale_copper", () -> new BlockDragonScales(EnumDragonEgg.COPPER));
    public static final RegistryObject<Block> DRAGON_SCALE_BLACK = BLOCKS.register("dragonscale_black", () -> new BlockDragonScales(EnumDragonEgg.BLACK));

    public static final RegistryObject<Block> DRAGON_BONE_BLOCK = BLOCKS.register("dragon_bone_block", BlockDragonBone::new);
    public static final RegistryObject<Block> DRAGON_BONE_BLOCK_WALL = BLOCKS.register("dragon_bone_wall", () -> new BlockDragonBoneWall(BlockBehaviour.Properties.copy(IafBlockRegistry.DRAGON_BONE_BLOCK.get())));
    public static final RegistryObject<Block> DRAGONFORGE_FIRE_BRICK = BLOCKS.register(BlockDragonforgeBricks.name(0), () -> new BlockDragonforgeBricks(0));
    public static final RegistryObject<Block> DRAGONFORGE_ICE_BRICK = BLOCKS.register(BlockDragonforgeBricks.name(1), () -> new BlockDragonforgeBricks(1));
    public static final RegistryObject<Block> DRAGONFORGE_LIGHTNING_BRICK = BLOCKS.register(BlockDragonforgeBricks.name(2), () -> new BlockDragonforgeBricks(2));
    public static final RegistryObject<Block> DRAGONFORGE_FIRE_INPUT = BLOCKS.register(BlockDragonforgeInput.name(0), () -> new BlockDragonforgeInput(0));
    public static final RegistryObject<Block> DRAGONFORGE_ICE_INPUT = BLOCKS.register(BlockDragonforgeInput.name(1), () -> new BlockDragonforgeInput(1));
    public static final RegistryObject<Block> DRAGONFORGE_LIGHTNING_INPUT = BLOCKS.register(BlockDragonforgeInput.name(2), () -> new BlockDragonforgeInput(2));
    public static final RegistryObject<Block> DRAGONFORGE_FIRE_CORE = BLOCKS.register(BlockDragonforgeCore.name(0, true), () -> new BlockDragonforgeCore(0, true));
    public static final RegistryObject<Block> DRAGONFORGE_ICE_CORE = BLOCKS.register(BlockDragonforgeCore.name(1, true), () -> new BlockDragonforgeCore(1, true));
    public static final RegistryObject<Block> DRAGONFORGE_LIGHTNING_CORE = BLOCKS.register(BlockDragonforgeCore.name(2, true), () -> new BlockDragonforgeCore(2, true));
    public static final RegistryObject<Block> DRAGONFORGE_FIRE_CORE_DISABLED = BLOCKS.register(BlockDragonforgeCore.name(0, false), () -> new BlockDragonforgeCore(0, false));
    public static final RegistryObject<Block> DRAGONFORGE_ICE_CORE_DISABLED = BLOCKS.register(BlockDragonforgeCore.name(1, false), () -> new BlockDragonforgeCore(1, false));
    public static final RegistryObject<Block> DRAGONFORGE_LIGHTNING_CORE_DISABLED = BLOCKS.register(BlockDragonforgeCore.name(2, false), () -> new BlockDragonforgeCore(2, false));
    public static final RegistryObject<Block> EGG_IN_ICE = BLOCKS.register("egginice", BlockEggInIce::new);
    public static final RegistryObject<Block> PIXIE_HOUSE_MUSHROOM_RED = BLOCKS.register(BlockPixieHouse.name("mushroom_red"), BlockPixieHouse::new);
    public static final RegistryObject<Block> PIXIE_HOUSE_MUSHROOM_BROWN = BLOCKS.register(BlockPixieHouse.name("mushroom_brown"), BlockPixieHouse::new);
    public static final RegistryObject<Block> PIXIE_HOUSE_OAK = BLOCKS.register(BlockPixieHouse.name("oak"), BlockPixieHouse::new);
    public static final RegistryObject<Block> PIXIE_HOUSE_BIRCH = BLOCKS.register(BlockPixieHouse.name("birch"), BlockPixieHouse::new);
    public static final RegistryObject<Block> PIXIE_HOUSE_SPRUCE = BLOCKS.register(BlockPixieHouse.name("spruce"), BlockPixieHouse::new);
    public static final RegistryObject<Block> PIXIE_HOUSE_DARK_OAK = BLOCKS.register(BlockPixieHouse.name("dark_oak"), BlockPixieHouse::new);
    public static final RegistryObject<Block> JAR_EMPTY = BLOCKS.register(BlockJar.name(-1), () -> new BlockJar(-1));
    public static final RegistryObject<Block> JAR_PIXIE_0 = BLOCKS.register(BlockJar.name(0), () -> new BlockJar(0));
    public static final RegistryObject<Block> JAR_PIXIE_1 = BLOCKS.register(BlockJar.name(1), () -> new BlockJar(1));
    public static final RegistryObject<Block> JAR_PIXIE_2 = BLOCKS.register(BlockJar.name(2), () -> new BlockJar(2));
    public static final RegistryObject<Block> JAR_PIXIE_3 = BLOCKS.register(BlockJar.name(3), () -> new BlockJar(3));
    public static final RegistryObject<Block> JAR_PIXIE_4 = BLOCKS.register(BlockJar.name(4), () -> new BlockJar(4));
    public static final RegistryObject<Block> MYRMEX_DESERT_RESIN = BLOCKS.register(BlockMyrmexResin.name(false, "desert"), () -> new BlockMyrmexResin(false));
    public static final RegistryObject<Block> MYRMEX_DESERT_RESIN_STICKY = BLOCKS.register(BlockMyrmexResin.name(true, "desert"), () -> new BlockMyrmexResin(true));
    public static final RegistryObject<Block> MYRMEX_JUNGLE_RESIN = BLOCKS.register(BlockMyrmexResin.name(false, "jungle"), () -> new BlockMyrmexResin(false));
    public static final RegistryObject<Block> MYRMEX_JUNGLE_RESIN_STICKY = BLOCKS.register(BlockMyrmexResin.name(true, "jungle"), () -> new BlockMyrmexResin(true));
    public static final RegistryObject<Block> DESERT_MYRMEX_COCOON = BLOCKS.register("desert_myrmex_cocoon", BlockMyrmexCocoon::new);
    public static final RegistryObject<Block> JUNGLE_MYRMEX_COCOON = BLOCKS.register("jungle_myrmex_cocoon", BlockMyrmexCocoon::new);
    public static final RegistryObject<Block> MYRMEX_DESERT_BIOLIGHT = BLOCKS.register("myrmex_desert_biolight", BlockMyrmexBiolight::new);
    public static final RegistryObject<Block> MYRMEX_JUNGLE_BIOLIGHT = BLOCKS.register("myrmex_jungle_biolight", BlockMyrmexBiolight::new);
    public static final RegistryObject<Block> MYRMEX_DESERT_RESIN_BLOCK = BLOCKS.register(BlockMyrmexConnectedResin.name(false, false), () -> new BlockMyrmexConnectedResin(false, false));
    public static final RegistryObject<Block> MYRMEX_JUNGLE_RESIN_BLOCK = BLOCKS.register(BlockMyrmexConnectedResin.name(true, false), () -> new BlockMyrmexConnectedResin(true, false));
    public static final RegistryObject<Block> MYRMEX_DESERT_RESIN_GLASS = BLOCKS.register(BlockMyrmexConnectedResin.name(false, true), () -> new BlockMyrmexConnectedResin(false, true));
    public static final RegistryObject<Block> MYRMEX_JUNGLE_RESIN_GLASS = BLOCKS.register(BlockMyrmexConnectedResin.name(true, true), () -> new BlockMyrmexConnectedResin(true, true));
    public static final RegistryObject<Block> DRAGONSTEEL_FIRE_BLOCK = BLOCKS.register("dragonsteel_fire_block", () -> BlockGeneric.builder(10.0F, 1000.0F, SoundType.METAL, MapColor.METAL, null, null, false));
    public static final RegistryObject<Block> DRAGONSTEEL_ICE_BLOCK = BLOCKS.register("dragonsteel_ice_block", () -> BlockGeneric.builder(10.0F, 1000.0F, SoundType.METAL, MapColor.METAL, null, null, false));
    public static final RegistryObject<Block> DRAGONSTEEL_LIGHTNING_BLOCK = BLOCKS.register("dragonsteel_lightning_block", () -> BlockGeneric.builder(10.0F, 1000.0F, SoundType.METAL, MapColor.METAL, null, null, false));
    public static final RegistryObject<BlockDreadBase> DREAD_STONE = BLOCKS.register("dread_stone", () -> BlockDreadBase.builder(-1.0F, 100000.0F, SoundType.STONE, MapColor.STONE, null, false));
    public static final RegistryObject<BlockDreadBase> DREAD_STONE_BRICKS = BLOCKS.register("dread_stone_bricks", () -> BlockDreadBase.builder(-1.0F, 100000.0F, SoundType.STONE, MapColor.STONE, null, false));
    public static final RegistryObject<BlockDreadBase> DREAD_STONE_BRICKS_CHISELED = BLOCKS.register("dread_stone_bricks_chiseled", () -> BlockDreadBase.builder(-1.0F, 100000.0F, SoundType.STONE, MapColor.STONE, null, false));
    public static final RegistryObject<BlockDreadBase> DREAD_STONE_BRICKS_CRACKED = BLOCKS.register("dread_stone_bricks_cracked", () -> BlockDreadBase.builder(-1.0F, 100000.0F, SoundType.STONE, MapColor.STONE, null, false));
    public static final RegistryObject<BlockDreadBase> DREAD_STONE_BRICKS_MOSSY = BLOCKS.register("dread_stone_bricks_mossy", () -> BlockDreadBase.builder(-1.0F, 100000.0F, SoundType.STONE, MapColor.STONE, null, false));
    public static final RegistryObject<BlockDreadBase> DREAD_STONE_TILE = BLOCKS.register("dread_stone_tile", () -> BlockDreadBase.builder(-1.0F, 100000.0F, SoundType.STONE, MapColor.STONE, null, false));
    public static final RegistryObject<Block> DREAD_STONE_FACE = BLOCKS.register("dread_stone_face", BlockDreadStoneFace::new);
    public static final RegistryObject<Block> DREAD_TORCH = BLOCKS.register("dread_torch", BlockDreadTorch::new);
    public static final RegistryObject<Block> DREAD_TORCH_WALL = BLOCKS.register("dread_torch_wall", BlockDreadTorchWall::new);
    public static final RegistryObject<Block> DREAD_STONE_BRICKS_STAIRS = BLOCKS.register("dread_stone_stairs", () -> new BlockGenericStairs(DREAD_STONE_BRICKS.get().defaultBlockState()));
    public static final RegistryObject<Block> DREAD_STONE_BRICKS_SLAB = BLOCKS.register("dread_stone_slab", () -> new SlabBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).strength(10F, 10000F)));
    public static final RegistryObject<Block> DREADWOOD_LOG = BLOCKS.register("dreadwood_log", BlockDreadWoodLog::new);
    public static final RegistryObject<BlockDreadBase> DREADWOOD_PLANKS = BLOCKS.register("dreadwood_planks", () -> BlockDreadBase.builder(-1.0F, 100000.0F, SoundType.WOOD, MapColor.WOOD, NoteBlockInstrument.BASS, true));
    public static final RegistryObject<Block> DREADWOOD_PLANKS_LOCK = BLOCKS.register("dreadwood_planks_lock", BlockDreadWoodLock::new);
    public static final RegistryObject<Block> DREAD_PORTAL = BLOCKS.register("dread_portal", BlockDreadPortal::new);
    public static final RegistryObject<Block> DREAD_SPAWNER = BLOCKS.register("dread_spawner", BlockDreadSpawner::new);
    public static final RegistryObject<Block> BURNT_TORCH = BLOCKS.register("burnt_torch", BlockBurntTorch::new);
    public static final RegistryObject<Block> BURNT_TORCH_WALL = BLOCKS.register("burnt_torch_wall", BlockBurntTorchWall::new);
    public static final RegistryObject<Block> GHOST_CHEST = BLOCKS.register("ghost_chest", BlockGhostChest::new);
    public static final RegistryObject<Block> GRAVEYARD_SOIL = BLOCKS.register("graveyard_soil", BlockGraveyardSoil::new);

/*    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void registerBlockItems(RegisterEvent event) {
        IafBlockRegistry.BLOCKS.getEntries().stream()
            .map(RegistryObject::get)
            .forEach(block -> {
                Optional<Item> item = registerItemBlock(block);
                if (item.isPresent())
                    IafItemRegistry.ITEMS.register(block.getDescriptionId(), item::get);
            });

    }*/

    public static Optional<Item> registerItemBlock(Block block) {
        if (!(block instanceof WallTorchBlock)) {
            Item.Properties props = new Item.Properties();
/*            if (!(block instanceof INoTab) || ((INoTab) block).shouldBeInTab()) {
                props.tab(IceAndFire.TAB_BLOCKS);
            }*/
            BlockItem itemBlock;
            if (block instanceof IWallBlock) {
                itemBlock = new StandingAndWallBlockItem(block, ((IWallBlock) block).wallBlock(), props, Direction.DOWN);
            } else if (block instanceof BlockGhostChest || block instanceof BlockDreadPortal || block instanceof BlockPixieHouse) {
                itemBlock = new BlockItemWithRender(block, props);
            } else {
                itemBlock = new BlockItem(block, props);
            }
            return Optional.of(itemBlock);
        }
        return Optional.empty();
    }
}
