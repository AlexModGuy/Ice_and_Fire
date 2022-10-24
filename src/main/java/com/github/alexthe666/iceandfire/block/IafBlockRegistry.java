package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.enums.EnumDragonEgg;
import com.github.alexthe666.iceandfire.enums.EnumSeaSerpent;
import com.github.alexthe666.iceandfire.item.BlockItemWithRender;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.lang.reflect.Field;

//@Mod.EventBusSubscriber(modid = IceAndFire.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class IafBlockRegistry {

    public static final SoundType SOUND_TYPE_GOLD = new SoundType(1.0F, 1.0F, IafSoundRegistry.GOLD_PILE_BREAK, IafSoundRegistry.GOLD_PILE_STEP, IafSoundRegistry.GOLD_PILE_BREAK, IafSoundRegistry.GOLD_PILE_STEP, IafSoundRegistry.GOLD_PILE_STEP);

    public static final Block LECTERN = new BlockLectern();
    public static final Block PODIUM_OAK = new BlockPodium("oak");
    public static final Block PODIUM_BIRCH = new BlockPodium("birch");
    public static final Block PODIUM_SPRUCE = new BlockPodium("spruce");
    public static final Block PODIUM_JUNGLE = new BlockPodium("jungle");
    public static final Block PODIUM_DARK_OAK = new BlockPodium("dark_oak");
    public static final Block PODIUM_ACACIA = new BlockPodium("acacia");
    public static final Block FIRE_LILY = new BlockElementalFlower("fire_lily");
    public static final Block FROST_LILY = new BlockElementalFlower("frost_lily");
    public static final Block LIGHTNING_LILY = new BlockElementalFlower("lightning_lily");
    public static final Block GOLD_PILE = new BlockGoldPile("gold_pile");
    public static final Block SILVER_PILE = new BlockGoldPile("silver_pile");
    public static final Block COPPER_PILE = new BlockGoldPile("copper_pile");
    public static final Block SILVER_ORE = new BlockIafOre(2, 3.0F, 3.0F, "iceandfire.silverOre", "silver_ore");
    public static final Block SAPPHIRE_ORE = new BlockIafOre(2, 4.0F, 3.0F, "iceandfire.sapphireOre", "sapphire_ore");
    public static final Block COPPER_ORE = new BlockIafOre(0, 3.0F, 3.0F, "iceandfire.copper_ore", "copper_ore");
    public static final Block AMYTHEST_ORE = new BlockIafOre(2, 4.0F, 3.0F, "iceandfire.amythest_ore", "amythest_ore");
    public static final Block SILVER_BLOCK = new BlockGeneric(Material.METAL, "silver_block", 3.0F, 5.0F, SoundType.METAL);
    public static final Block SAPPHIRE_BLOCK = new BlockGeneric(Material.METAL, "sapphire_block", 3.0F, 6.0F, SoundType.METAL);
    public static final Block COPPER_BLOCK = new BlockGeneric(Material.METAL, "copper_block", 4.0F, 5.0F, SoundType.METAL);
    public static final Block AMYTHEST_BLOCK = new BlockGeneric(Material.METAL, "amythest_block", 5.0F, 6.0F, SoundType.METAL);
    public static final Block CHARRED_DIRT = new BlockReturningState(Material.DIRT, "chared_dirt", 0.5F, 0.0F, SoundType.GRAVEL, Blocks.DIRT.defaultBlockState());
    public static final Block CHARRED_GRASS = new BlockReturningState(Material.GRASS, "chared_grass", 0.6F, 0.0F, SoundType.GRAVEL, Blocks.GRASS_BLOCK.defaultBlockState());
    public static final Block CHARRED_STONE = new BlockReturningState(Material.STONE, "chared_stone", 1.5F, 10.0F, SoundType.STONE, Blocks.STONE.defaultBlockState());
    public static final Block CHARRED_COBBLESTONE = new BlockReturningState(Material.STONE, "chared_cobblestone", 2F, 10.0F, SoundType.STONE, Blocks.COBBLESTONE.defaultBlockState());
    public static final Block CHARRED_GRAVEL = new BlockFallingReturningState(Material.DIRT, "chared_gravel", 0.6F, 0F, SoundType.GRAVEL, Blocks.GRAVEL.defaultBlockState());
    public static final Block CHARRED_DIRT_PATH = new BlockCharedPath(0);
    public static final Block ASH = new BlockFallingGeneric(Material.SAND, "ash", 0.5F, 0F, SoundType.SAND);
    public static final Block FROZEN_DIRT = new BlockReturningState(Material.DIRT, "frozen_dirt", 0.5F, 0.0F, SoundType.GLASS, true, Blocks.DIRT.defaultBlockState());
    public static final Block FROZEN_GRASS = new BlockReturningState(Material.GRASS, "frozen_grass", 0.6F, 0.0F, SoundType.GLASS, true, Blocks.GRASS_BLOCK.defaultBlockState());
    public static final Block FROZEN_STONE = new BlockReturningState(Material.STONE, "frozen_stone", 1.5F, 1.0F, SoundType.GLASS, true, Blocks.STONE.defaultBlockState());
    public static final Block FROZEN_COBBLESTONE = new BlockReturningState(Material.STONE, "frozen_cobblestone", 2F, 2.0F, SoundType.GLASS, true, Blocks.COBBLESTONE.defaultBlockState());
    public static final Block FROZEN_GRAVEL = new BlockFallingReturningState(Material.DIRT, "frozen_gravel", 0.6F, 0F, SoundType.GLASS, true, Blocks.GRAVEL.defaultBlockState());
    public static final Block FROZEN_DIRT_PATH = new BlockCharedPath(1);
    public static final Block FROZEN_SPLINTERS = new BlockGeneric(Material.WOOD, "frozen_splinters", 2.0F, 1.0F, SoundType.GLASS, true);
    public static final Block DRAGON_ICE = new BlockGeneric(Material.ICE_SOLID, "dragon_ice", 0.5F, 0F, SoundType.GLASS, true);
    public static final Block DRAGON_ICE_SPIKES = new BlockIceSpikes();
    public static final Block CRACKLED_DIRT = new BlockReturningState(Material.DIRT, "crackled_dirt", 0.5F, 0.0F, SoundType.GRAVEL, Blocks.DIRT.defaultBlockState());
    public static final Block CRACKLED_GRASS = new BlockReturningState(Material.GRASS, "crackled_grass", 0.6F, 0.0F, SoundType.GRAVEL, Blocks.GRASS_BLOCK.defaultBlockState());
    public static final Block CRACKLED_STONE = new BlockReturningState(Material.STONE, "crackled_stone", 1.5F, 1.0F, SoundType.STONE, Blocks.STONE.defaultBlockState());
    public static final Block CRACKLED_COBBLESTONE = new BlockReturningState(Material.STONE, "crackled_cobblestone", 2F, 2F, SoundType.STONE, Blocks.COBBLESTONE.defaultBlockState());
    public static final Block CRACKLED_GRAVEL = new BlockFallingReturningState(Material.DIRT, "crackled_gravel", 0.6F, 0F, SoundType.GRAVEL, Blocks.GRAVEL.defaultBlockState());
    public static final Block CRACKLED_DIRT_PATH = new BlockCharedPath(2);

    public static final Block NEST = new BlockGeneric(Material.PLANT, "nest", 0.5F, 0F, SoundType.GRAVEL, false);

    public static final Block DRAGON_SCALE_RED = new BlockDragonScales("dragonscale_red", EnumDragonEgg.RED);
    public static final Block DRAGON_SCALE_GREEN = new BlockDragonScales("dragonscale_green", EnumDragonEgg.GREEN);
    public static final Block DRAGON_SCALE_BRONZE = new BlockDragonScales("dragonscale_bronze", EnumDragonEgg.BRONZE);
    public static final Block DRAGON_SCALE_GRAY = new BlockDragonScales("dragonscale_gray", EnumDragonEgg.GRAY);
    public static final Block DRAGON_SCALE_BLUE = new BlockDragonScales("dragonscale_blue", EnumDragonEgg.BLUE);
    public static final Block DRAGON_SCALE_WHITE = new BlockDragonScales("dragonscale_white", EnumDragonEgg.WHITE);
    public static final Block DRAGON_SCALE_SAPPHIRE = new BlockDragonScales("dragonscale_sapphire", EnumDragonEgg.SAPPHIRE);
    public static final Block DRAGON_SCALE_SILVER = new BlockDragonScales("dragonscale_silver", EnumDragonEgg.SILVER);
    public static final Block DRAGON_SCALE_ELECTRIC = new BlockDragonScales("dragonscale_electric", EnumDragonEgg.ELECTRIC);
    public static final Block DRAGON_SCALE_AMYTHEST = new BlockDragonScales("dragonscale_amythest", EnumDragonEgg.AMYTHEST);
    public static final Block DRAGON_SCALE_COPPER = new BlockDragonScales("dragonscale_copper", EnumDragonEgg.COPPER);
    public static final Block DRAGON_SCALE_BLACK = new BlockDragonScales("dragonscale_black", EnumDragonEgg.BLACK);

    public static final Block DRAGON_BONE_BLOCK = new BlockDragonBone();
    public static final Block DRAGON_BONE_BLOCK_WALL = new BlockDragonBoneWall(BlockBehaviour.Properties.copy(IafBlockRegistry.DRAGON_BONE_BLOCK));
    public static final Block DRAGONFORGE_FIRE_BRICK = new BlockDragonforgeBricks(0);
    public static final Block DRAGONFORGE_ICE_BRICK = new BlockDragonforgeBricks(1);
    public static final Block DRAGONFORGE_LIGHTNING_BRICK = new BlockDragonforgeBricks(2);
    public static final Block DRAGONFORGE_FIRE_INPUT = new BlockDragonforgeInput(0);
    public static final Block DRAGONFORGE_ICE_INPUT = new BlockDragonforgeInput(1);
    public static final Block DRAGONFORGE_LIGHTNING_INPUT = new BlockDragonforgeInput(2);
    public static final Block DRAGONFORGE_FIRE_CORE = new BlockDragonforgeCore(0, true);
    public static final Block DRAGONFORGE_ICE_CORE = new BlockDragonforgeCore(1, true);
    public static final Block DRAGONFORGE_LIGHTNING_CORE = new BlockDragonforgeCore(2, true);
    public static final Block DRAGONFORGE_FIRE_CORE_DISABLED = new BlockDragonforgeCore(0, false);
    public static final Block DRAGONFORGE_ICE_CORE_DISABLED = new BlockDragonforgeCore(1, false);
    public static final Block DRAGONFORGE_LIGHTNING_CORE_DISABLED = new BlockDragonforgeCore(2, false);
    public static final Block EGG_IN_ICE = new BlockEggInIce();
    public static final Block PIXIE_HOUSE_MUSHROOM_RED = new BlockPixieHouse("mushroom_red");
    public static final Block PIXIE_HOUSE_MUSHROOM_BROWN = new BlockPixieHouse("mushroom_brown");
    public static final Block PIXIE_HOUSE_OAK = new BlockPixieHouse("oak");
    public static final Block PIXIE_HOUSE_BIRCH = new BlockPixieHouse("birch");
    public static final Block PIXIE_HOUSE_SPRUCE = new BlockPixieHouse("spruce");
    public static final Block PIXIE_HOUSE_DARK_OAK = new BlockPixieHouse("dark_oak");
    public static final Block JAR_EMPTY = new BlockJar(-1);
    public static final Block JAR_PIXIE_0 = new BlockJar(0);
    public static final Block JAR_PIXIE_1 = new BlockJar(1);
    public static final Block JAR_PIXIE_2 = new BlockJar(2);
    public static final Block JAR_PIXIE_3 = new BlockJar(3);
    public static final Block JAR_PIXIE_4 = new BlockJar(4);
    public static final Block MYRMEX_DESERT_RESIN = new BlockMyrmexResin(false, "desert");
    public static final Block MYRMEX_DESERT_RESIN_STICKY = new BlockMyrmexResin(true, "desert");
    public static final Block MYRMEX_JUNGLE_RESIN = new BlockMyrmexResin(false, "jungle");
    public static final Block MYRMEX_JUNGLE_RESIN_STICKY = new BlockMyrmexResin(true, "jungle");
    public static final Block DESERT_MYRMEX_COCOON = new BlockMyrmexCocoon(false);
    public static final Block JUNGLE_MYRMEX_COCOON = new BlockMyrmexCocoon(true);
    public static final Block MYRMEX_DESERT_BIOLIGHT = new BlockMyrmexBiolight(false);
    public static final Block MYRMEX_JUNGLE_BIOLIGHT = new BlockMyrmexBiolight(true);
    public static final Block MYRMEX_DESERT_RESIN_BLOCK = new BlockMyrmexConnectedResin(false, false);
    public static final Block MYRMEX_JUNGLE_RESIN_BLOCK = new BlockMyrmexConnectedResin(true, false);
    public static final Block MYRMEX_DESERT_RESIN_GLASS = new BlockMyrmexConnectedResin(false, true);
    public static final Block MYRMEX_JUNGLE_RESIN_GLASS = new BlockMyrmexConnectedResin(true, true);
    public static final Block DRAGONSTEEL_FIRE_BLOCK = new BlockGeneric(Material.METAL, "dragonsteel_fire_block", 10.0F, 1000.0F, SoundType.METAL);
    public static final Block DRAGONSTEEL_ICE_BLOCK = new BlockGeneric(Material.METAL, "dragonsteel_ice_block", 10.0F, 1000.0F, SoundType.METAL);
    public static final Block DRAGONSTEEL_LIGHTNING_BLOCK = new BlockGeneric(Material.METAL, "dragonsteel_lightning_block", 10.0F, 1000.0F, SoundType.METAL);
    public static final BlockDreadBase DREAD_STONE = new BlockDreadBase(Material.STONE, "dread_stone", -1.0F, 100000.0F, SoundType.STONE);
    public static final BlockDreadBase DREAD_STONE_BRICKS = new BlockDreadBase(Material.STONE, "dread_stone_bricks", -1.0F, 100000.0F, SoundType.STONE);
    public static final BlockDreadBase DREAD_STONE_BRICKS_CHISELED = new BlockDreadBase(Material.STONE, "dread_stone_bricks_chiseled", -1.0F, 100000.0F, SoundType.STONE);
    public static final BlockDreadBase DREAD_STONE_BRICKS_CRACKED = new BlockDreadBase(Material.STONE, "dread_stone_bricks_cracked", -1.0F, 100000.0F, SoundType.STONE);
    public static final BlockDreadBase DREAD_STONE_BRICKS_MOSSY = new BlockDreadBase(Material.STONE, "dread_stone_bricks_mossy", -1.0F, 100000.0F, SoundType.STONE);
    public static final BlockDreadBase DREAD_STONE_TILE = new BlockDreadBase(Material.STONE, "dread_stone_tile", -1.0F, 100000.0F, SoundType.STONE);
    public static final Block DREAD_STONE_FACE = new BlockDreadStoneFace();
    public static final Block DREAD_TORCH = new BlockDreadTorch();
    public static final Block DREAD_TORCH_WALL = new BlockDreadTorchWall();
    public static final Block DREAD_STONE_BRICKS_STAIRS = new BlockGenericStairs(DREAD_STONE_BRICKS.defaultBlockState(), "dread_stone_stairs");
    public static final Block DREAD_STONE_BRICKS_SLAB = new SlabBlock(BlockBehaviour.Properties.of(Material.STONE).strength(10F, 10000F)).setRegistryName(IceAndFire.MODID, "dread_stone_slab");
    public static final Block DREADWOOD_LOG = new BlockDreadWoodLog();
    public static final BlockDreadBase DREADWOOD_PLANKS = new BlockDreadBase(Material.WOOD, "dreadwood_planks", -1.0F, 100000.0F, SoundType.WOOD);
    public static final Block DREADWOOD_PLANKS_LOCK = new BlockDreadWoodLock();
    public static final Block DREAD_PORTAL = new BlockDreadPortal();
    public static final Block DREAD_SPAWNER = new BlockDreadSpawner();
    public static final Block BURNT_TORCH = new BlockBurntTorch();
    public static final Block BURNT_TORCH_WALL = new BlockBurntTorchWall();
    public static final Block GHOST_CHEST = new BlockGhostChest();
    public static final Block GRAVEYARD_SOIL = new BlockGraveyardSoil();

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        try {
            for (Field f : IafBlockRegistry.class.getFields()) {
                Object obj = f.get(null);
                if (obj instanceof Block) {
                    event.getRegistry().register((Block) obj);
                } else if (obj instanceof Block[]) {
                    for (Block block : (Block[]) obj) {
                        event.getRegistry().register(block);
                    }
                }
            }
            for (EnumSeaSerpent color : EnumSeaSerpent.values()) {
                event.getRegistry().register(color.scaleBlock);
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @SubscribeEvent
    public static void registerBlockItems(RegistryEvent.Register<Item> event) {
        // ItemBlocks
        try {
            for (Field f : IafBlockRegistry.class.getFields()) {
                Object obj = f.get(null);
                if (obj instanceof Block && !(obj instanceof WallTorchBlock)) {
                    Item.Properties props = new Item.Properties();
                    if (!(obj instanceof INoTab) || ((INoTab) obj).shouldBeInTab()) {
                        props.tab(IceAndFire.TAB_BLOCKS);
                    }
                    BlockItem itemBlock;
                    if (obj instanceof IWallBlock) {
                        itemBlock = new StandingAndWallBlockItem((Block) obj, ((IWallBlock) obj).wallBlock(), props);
                    } else if (obj instanceof BlockGhostChest || obj instanceof BlockDreadPortal || obj instanceof BlockPixieHouse) {
                        itemBlock = new BlockItemWithRender((Block) obj, props);
                    } else {
                        itemBlock = new BlockItem((Block) obj, props);
                    }
                    itemBlock.setRegistryName(((Block) obj).getRegistryName());
                    event.getRegistry().register(itemBlock);
                } else if (obj instanceof Block[]) {
                    for (Block block : (Block[]) obj) {
                        Item.Properties props = new Item.Properties();
                        if (block.getRegistryName() != null) {
                            if (!(block instanceof INoTab) || ((INoTab) block).shouldBeInTab()) {
                                props.tab(IceAndFire.TAB_BLOCKS);
                            }
                            BlockItem itemBlock = new BlockItem(block, props);
                            itemBlock.setRegistryName(block.getRegistryName());
                            event.getRegistry().register(itemBlock);
                        }
                    }
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        for (EnumSeaSerpent color : EnumSeaSerpent.values()) {
            Item.Properties props = new Item.Properties();
            props.tab(IceAndFire.TAB_BLOCKS);
            BlockItem itemBlock = new BlockItem(color.scaleBlock, props);
            itemBlock.setRegistryName(color.scaleBlock.getRegistryName());
            event.getRegistry().register(itemBlock);
        }
    }
}
