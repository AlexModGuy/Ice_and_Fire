package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.enums.EnumDragonEgg;
import com.github.alexthe666.iceandfire.enums.EnumSeaSerpent;
import com.github.alexthe666.iceandfire.item.BlockItemWithRender;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.item.ItemBestiary;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.lang.reflect.Field;
import java.util.function.BiFunction;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = IceAndFire.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class IafBlockRegistry {

    public static DeferredRegister<Block> deferredRegister = DeferredRegister.create(ForgeRegistries.BLOCKS, IceAndFire.MODID);

    public static final SoundType SOUND_TYPE_GOLD = new SoundType(1.0F, 1.0F, IafSoundRegistry.GOLD_PILE_BREAK, IafSoundRegistry.GOLD_PILE_STEP, IafSoundRegistry.GOLD_PILE_BREAK, IafSoundRegistry.GOLD_PILE_STEP, IafSoundRegistry.GOLD_PILE_STEP);

    public static final RegistryObject<Block> LECTERN = deferredRegister.register("lectern", () -> new BlockLectern());
    public static final RegistryObject<Block> PODIUM_OAK = deferredRegister.register("oak", () -> new BlockPodium());
    public static final RegistryObject<Block> PODIUM_BIRCH = deferredRegister.register("birch", () -> new BlockPodium());
    public static final RegistryObject<Block> PODIUM_SPRUCE = deferredRegister.register("spruce", () -> new BlockPodium());
    public static final RegistryObject<Block> PODIUM_JUNGLE = deferredRegister.register("jungle", () -> new BlockPodium());
    public static final RegistryObject<Block> PODIUM_DARK_OAK = deferredRegister.register("dark_oak", () -> new BlockPodium());
    public static final RegistryObject<Block> PODIUM_ACACIA = deferredRegister.register("acacia", () -> new BlockPodium());
    public static final RegistryObject<Block> FIRE_LILY = deferredRegister.register("fire_lily", () -> new BlockElementalFlower());
    public static final RegistryObject<Block> FROST_LILY = deferredRegister.register("frost_lily", () -> new BlockElementalFlower());
    public static final RegistryObject<Block> LIGHTNING_LILY = deferredRegister.register("lightning_lily", () -> new BlockElementalFlower());
    public static final RegistryObject<Block> GOLD_PILE = deferredRegister.register("gold_pile", () -> new BlockGoldPile());
    public static final RegistryObject<Block> SILVER_PILE = deferredRegister.register("silver_pile", () -> new BlockGoldPile());
    public static final RegistryObject<Block> COPPER_PILE = deferredRegister.register("copper_pile", () -> new BlockGoldPile());
    public static final RegistryObject<Block> SILVER_ORE = deferredRegister.register("silver_ore", () -> new BlockIafOre(2, 3.0F, 3.0F));
    public static final RegistryObject<Block> SAPPHIRE_ORE = deferredRegister.register("sapphire_ore", () -> new BlockIafOre(2, 4.0F, 3.0F));
    public static final RegistryObject<Block> COPPER_ORE = deferredRegister.register("copper_ore", () -> new BlockIafOre(0, 3.0F, 3.0F));
    public static final RegistryObject<Block> AMYTHEST_ORE = deferredRegister.register("amythest_ore", () -> new BlockIafOre(2, 4.0F, 3.0F));
    public static final RegistryObject<Block> SILVER_BLOCK = deferredRegister.register("silver_block", () -> new BlockGeneric(Material.METAL, 3.0F, 5.0F, SoundType.METAL));
    public static final RegistryObject<Block> SAPPHIRE_BLOCK = deferredRegister.register("sapphire_block", () -> new BlockGeneric(Material.METAL, 3.0F, 6.0F, SoundType.METAL));
    public static final RegistryObject<Block> COPPER_BLOCK = deferredRegister.register("copper_block", () -> new BlockGeneric(Material.METAL, 4.0F, 5.0F, SoundType.METAL));
    public static final RegistryObject<Block> AMYTHEST_BLOCK = deferredRegister.register("amythest_block", () -> new BlockGeneric(Material.METAL, 5.0F, 6.0F, SoundType.METAL));
    public static final RegistryObject<Block> CHARRED_DIRT = deferredRegister.register("chared_dirt", () -> new BlockReturningState(Material.DIRT, 0.5F, 0.0F, SoundType.GRAVEL, Blocks.DIRT.defaultBlockState()));
    public static final RegistryObject<Block> CHARRED_GRASS = deferredRegister.register("chared_grass", () -> new BlockReturningState(Material.GRASS, 0.6F, 0.0F, SoundType.GRAVEL, Blocks.GRASS_BLOCK.defaultBlockState()));
    public static final RegistryObject<Block> CHARRED_STONE = deferredRegister.register("chared_stone", () -> new BlockReturningState(Material.STONE, 1.5F, 10.0F, SoundType.STONE, Blocks.STONE.defaultBlockState()));
    public static final RegistryObject<Block> CHARRED_COBBLESTONE = deferredRegister.register("chared_cobblestone", () -> new BlockReturningState(Material.STONE, 2F, 10.0F, SoundType.STONE, Blocks.COBBLESTONE.defaultBlockState()));
    public static final RegistryObject<Block> CHARRED_GRAVEL = deferredRegister.register("chared_gravel", () -> new BlockFallingReturningState(Material.DIRT, 0.6F, 0F, SoundType.GRAVEL, Blocks.GRAVEL.defaultBlockState()));
    public static final RegistryObject<Block> CHARRED_DIRT_PATH = deferredRegister.register(BlockCharedPath.getNameFromType(0), () -> new BlockCharedPath(0));
    public static final RegistryObject<Block> ASH = deferredRegister.register("ash", () -> new BlockFallingGeneric(Material.SAND, 0.5F, 0F, SoundType.SAND));
    public static final RegistryObject<Block> FROZEN_DIRT = deferredRegister.register("frozen_dirt", () -> new BlockReturningState(Material.DIRT, 0.5F, 0.0F, SoundType.GLASS, true, Blocks.DIRT.defaultBlockState()));
    public static final RegistryObject<Block> FROZEN_GRASS = deferredRegister.register("frozen_grass", () -> new BlockReturningState(Material.GRASS, 0.6F, 0.0F, SoundType.GLASS, true, Blocks.GRASS_BLOCK.defaultBlockState()));
    public static final RegistryObject<Block> FROZEN_STONE = deferredRegister.register("frozen_stone", () -> new BlockReturningState(Material.STONE, 1.5F, 1.0F, SoundType.GLASS, true, Blocks.STONE.defaultBlockState()));
    public static final RegistryObject<Block> FROZEN_COBBLESTONE = deferredRegister.register("frozen_cobblestone", () -> new BlockReturningState(Material.STONE, 2F, 2.0F, SoundType.GLASS, true, Blocks.COBBLESTONE.defaultBlockState()));
    public static final RegistryObject<Block> FROZEN_GRAVEL = deferredRegister.register("frozen_gravel", () -> new BlockFallingReturningState(Material.DIRT, 0.6F, 0F, SoundType.GLASS, true, Blocks.GRAVEL.defaultBlockState()));
    public static final RegistryObject<Block> FROZEN_DIRT_PATH = deferredRegister.register(BlockCharedPath.getNameFromType(1), () -> new BlockCharedPath(1));
    public static final RegistryObject<Block> FROZEN_SPLINTERS = deferredRegister.register("frozen_splinters", () -> new BlockGeneric(Material.WOOD, 2.0F, 1.0F, SoundType.GLASS, true));
    public static final RegistryObject<Block> DRAGON_ICE = deferredRegister.register("dragon_ice", () -> new BlockGeneric(Material.ICE_SOLID, 0.5F, 0F, SoundType.GLASS, true));
    public static final RegistryObject<Block> DRAGON_ICE_SPIKES = deferredRegister.register("dragon_ice_spikes", () -> new BlockIceSpikes());
    public static final RegistryObject<Block> CRACKLED_DIRT = deferredRegister.register("crackled_dirt", () -> new BlockReturningState(Material.DIRT, 0.5F, 0.0F, SoundType.GRAVEL, Blocks.DIRT.defaultBlockState()));
    public static final RegistryObject<Block> CRACKLED_GRASS = deferredRegister.register("crackled_grass", () -> new BlockReturningState(Material.GRASS, 0.6F, 0.0F, SoundType.GRAVEL, Blocks.GRASS_BLOCK.defaultBlockState()));
    public static final RegistryObject<Block> CRACKLED_STONE = deferredRegister.register("crackled_stone", () -> new BlockReturningState(Material.STONE, 1.5F, 1.0F, SoundType.STONE, Blocks.STONE.defaultBlockState()));
    public static final RegistryObject<Block> CRACKLED_COBBLESTONE = deferredRegister.register("crackled_cobblestone", () -> new BlockReturningState(Material.STONE, 2F, 2F, SoundType.STONE, Blocks.COBBLESTONE.defaultBlockState()));
    public static final RegistryObject<Block> CRACKLED_GRAVEL = deferredRegister.register("crackled_gravel", () -> new BlockFallingReturningState(Material.DIRT, 0.6F, 0F, SoundType.GRAVEL, Blocks.GRAVEL.defaultBlockState()));
    public static final RegistryObject<Block> CRACKLED_DIRT_PATH = deferredRegister.register(BlockCharedPath.getNameFromType(2), () -> new BlockCharedPath(2));

    public static final RegistryObject<Block> NEST = deferredRegister.register("nest", () -> new BlockGeneric(Material.PLANT, 0.5F, 0F, SoundType.GRAVEL, false));

    public static final RegistryObject<Block> DRAGON_SCALE_RED = deferredRegister.register("dragonscale_red", () -> new BlockDragonScales(EnumDragonEgg.RED));
    public static final RegistryObject<Block> DRAGON_SCALE_GREEN = deferredRegister.register("dragonscale_green", () -> new BlockDragonScales(EnumDragonEgg.GREEN));
    public static final RegistryObject<Block> DRAGON_SCALE_BRONZE = deferredRegister.register("dragonscale_bronze", () -> new BlockDragonScales(EnumDragonEgg.BRONZE));
    public static final RegistryObject<Block> DRAGON_SCALE_GRAY = deferredRegister.register("dragonscale_gray", () -> new BlockDragonScales(EnumDragonEgg.GRAY));
    public static final RegistryObject<Block> DRAGON_SCALE_BLUE = deferredRegister.register("dragonscale_blue", () -> new BlockDragonScales(EnumDragonEgg.BLUE));
    public static final RegistryObject<Block> DRAGON_SCALE_WHITE = deferredRegister.register("dragonscale_white", () -> new BlockDragonScales(EnumDragonEgg.WHITE));
    public static final RegistryObject<Block> DRAGON_SCALE_SAPPHIRE = deferredRegister.register("dragonscale_sapphire", () -> new BlockDragonScales(EnumDragonEgg.SAPPHIRE));
    public static final RegistryObject<Block> DRAGON_SCALE_SILVER = deferredRegister.register("dragonscale_silver", () -> new BlockDragonScales(EnumDragonEgg.SILVER));
    public static final RegistryObject<Block> DRAGON_SCALE_ELECTRIC = deferredRegister.register("dragonscale_electric", () -> new BlockDragonScales(EnumDragonEgg.ELECTRIC));
    public static final RegistryObject<Block> DRAGON_SCALE_AMYTHEST = deferredRegister.register("dragonscale_amythest", () -> new BlockDragonScales(EnumDragonEgg.AMYTHEST));
    public static final RegistryObject<Block> DRAGON_SCALE_COPPER = deferredRegister.register("dragonscale_copper", () -> new BlockDragonScales(EnumDragonEgg.COPPER));
    public static final RegistryObject<Block> DRAGON_SCALE_BLACK = deferredRegister.register("dragonscale_black", () -> new BlockDragonScales(EnumDragonEgg.BLACK));

    public static final RegistryObject<Block> DRAGON_BONE_BLOCK = deferredRegister.register("dragon_bone_block", () -> new BlockDragonBone());
    public static final RegistryObject<Block> DRAGON_BONE_BLOCK_WALL = deferredRegister.register("dragon_bone_wall", () -> new BlockDragonBoneWall(BlockBehaviour.Properties.copy(IafBlockRegistry.DRAGON_BONE_BLOCK.get())));
    public static final RegistryObject<Block> DRAGONFORGE_FIRE_BRICK = deferredRegister.register(BlockDragonforgeBricks.name(0), () -> new BlockDragonforgeBricks(0));
    public static final RegistryObject<Block> DRAGONFORGE_ICE_BRICK = deferredRegister.register(BlockDragonforgeBricks.name(1), () -> new BlockDragonforgeBricks(1));
    public static final RegistryObject<Block> DRAGONFORGE_LIGHTNING_BRICK = deferredRegister.register(BlockDragonforgeBricks.name(2), () -> new BlockDragonforgeBricks(2));
    public static final RegistryObject<Block> DRAGONFORGE_FIRE_INPUT = deferredRegister.register(BlockDragonforgeInput.name(0), () -> new BlockDragonforgeInput(0));
    public static final RegistryObject<Block> DRAGONFORGE_ICE_INPUT = deferredRegister.register(BlockDragonforgeInput.name(1), () -> new BlockDragonforgeInput(1));
    public static final RegistryObject<Block> DRAGONFORGE_LIGHTNING_INPUT = deferredRegister.register(BlockDragonforgeInput.name(2), () -> new BlockDragonforgeInput(2));
    public static final RegistryObject<Block> DRAGONFORGE_FIRE_CORE = deferredRegister.register(BlockDragonforgeCore.name(0, true), () -> new BlockDragonforgeCore(0, true));
    public static final RegistryObject<Block> DRAGONFORGE_ICE_CORE = deferredRegister.register(BlockDragonforgeCore.name(1, true), () -> new BlockDragonforgeCore(1, true));
    public static final RegistryObject<Block> DRAGONFORGE_LIGHTNING_CORE = deferredRegister.register(BlockDragonforgeCore.name(2, true), () -> new BlockDragonforgeCore(2, true));
    public static final RegistryObject<Block> DRAGONFORGE_FIRE_CORE_DISABLED = deferredRegister.register(BlockDragonforgeCore.name(0, false), () -> new BlockDragonforgeCore(0, false));
    public static final RegistryObject<Block> DRAGONFORGE_ICE_CORE_DISABLED = deferredRegister.register(BlockDragonforgeCore.name(1, false), () -> new BlockDragonforgeCore(1, false));
    public static final RegistryObject<Block> DRAGONFORGE_LIGHTNING_CORE_DISABLED = deferredRegister.register(BlockDragonforgeCore.name(2, false), () -> new BlockDragonforgeCore(2, false));
    public static final RegistryObject<Block> EGG_IN_ICE = deferredRegister.register("egginice", () -> new BlockEggInIce());
    public static final RegistryObject<Block> PIXIE_HOUSE_MUSHROOM_RED = deferredRegister.register(BlockPixieHouse.name("mushroom_red"), () -> new BlockPixieHouse());
    public static final RegistryObject<Block> PIXIE_HOUSE_MUSHROOM_BROWN = deferredRegister.register(BlockPixieHouse.name("mushroom_brown"), () -> new BlockPixieHouse());
    public static final RegistryObject<Block> PIXIE_HOUSE_OAK = deferredRegister.register(BlockPixieHouse.name("oak"), () -> new BlockPixieHouse());
    public static final RegistryObject<Block> PIXIE_HOUSE_BIRCH = deferredRegister.register(BlockPixieHouse.name("birch"), () -> new BlockPixieHouse());
    public static final RegistryObject<Block> PIXIE_HOUSE_SPRUCE = deferredRegister.register(BlockPixieHouse.name("spruce"), () -> new BlockPixieHouse());
    public static final RegistryObject<Block> PIXIE_HOUSE_DARK_OAK = deferredRegister.register(BlockPixieHouse.name("dark_oak"), () -> new BlockPixieHouse());
    public static final RegistryObject<Block> JAR_EMPTY = deferredRegister.register(BlockJar.name(-1), () -> new BlockJar(-1));
    public static final RegistryObject<Block> JAR_PIXIE_0 = deferredRegister.register(BlockJar.name(0), () -> new BlockJar(0));
    public static final RegistryObject<Block> JAR_PIXIE_1 = deferredRegister.register(BlockJar.name(1), () -> new BlockJar(1));
    public static final RegistryObject<Block> JAR_PIXIE_2 = deferredRegister.register(BlockJar.name(2), () -> new BlockJar(2));
    public static final RegistryObject<Block> JAR_PIXIE_3 = deferredRegister.register(BlockJar.name(3), () -> new BlockJar(3));
    public static final RegistryObject<Block> JAR_PIXIE_4 = deferredRegister.register(BlockJar.name(4), () -> new BlockJar(4));
    public static final RegistryObject<Block> MYRMEX_DESERT_RESIN = deferredRegister.register(BlockMyrmexResin.name(false, "desert"), () -> new BlockMyrmexResin(false));
    public static final RegistryObject<Block> MYRMEX_DESERT_RESIN_STICKY = deferredRegister.register(BlockMyrmexResin.name(true, "desert"), () -> new BlockMyrmexResin(true));
    public static final RegistryObject<Block> MYRMEX_JUNGLE_RESIN = deferredRegister.register(BlockMyrmexResin.name(false, "jungle"), () -> new BlockMyrmexResin(false));
    public static final RegistryObject<Block> MYRMEX_JUNGLE_RESIN_STICKY = deferredRegister.register(BlockMyrmexResin.name(true, "jungle"), () -> new BlockMyrmexResin(true));
    public static final RegistryObject<Block> DESERT_MYRMEX_COCOON = deferredRegister.register("desert_myrmex_cocoon", () -> new BlockMyrmexCocoon());
    public static final RegistryObject<Block> JUNGLE_MYRMEX_COCOON = deferredRegister.register("jungle_myrmex_cocoon", () -> new BlockMyrmexCocoon());
    public static final RegistryObject<Block> MYRMEX_DESERT_BIOLIGHT = deferredRegister.register("myrmex_desert_biolight", () -> new BlockMyrmexBiolight());
    public static final RegistryObject<Block> MYRMEX_JUNGLE_BIOLIGHT = deferredRegister.register("myrmex_jungle_biolight", () -> new BlockMyrmexBiolight());
    public static final RegistryObject<Block> MYRMEX_DESERT_RESIN_BLOCK = deferredRegister.register(BlockMyrmexConnectedResin.name(false, false), () -> new BlockMyrmexConnectedResin(false, false));
    public static final RegistryObject<Block> MYRMEX_JUNGLE_RESIN_BLOCK = deferredRegister.register(BlockMyrmexConnectedResin.name(true, false), () -> new BlockMyrmexConnectedResin(true, false));
    public static final RegistryObject<Block> MYRMEX_DESERT_RESIN_GLASS = deferredRegister.register(BlockMyrmexConnectedResin.name(false, true), () -> new BlockMyrmexConnectedResin(false, true));
    public static final RegistryObject<Block> MYRMEX_JUNGLE_RESIN_GLASS = deferredRegister.register(BlockMyrmexConnectedResin.name(true, true), () -> new BlockMyrmexConnectedResin(true, true));
    public static final RegistryObject<Block> DRAGONSTEEL_FIRE_BLOCK = deferredRegister.register("dragonsteel_fire_block", () -> new BlockGeneric(Material.METAL, 10.0F, 1000.0F, SoundType.METAL));
    public static final RegistryObject<Block> DRAGONSTEEL_ICE_BLOCK = deferredRegister.register("dragonsteel_ice_block", () -> new BlockGeneric(Material.METAL, 10.0F, 1000.0F, SoundType.METAL));
    public static final RegistryObject<Block> DRAGONSTEEL_LIGHTNING_BLOCK = deferredRegister.register("dragonsteel_lightning_block", () -> new BlockGeneric(Material.METAL, 10.0F, 1000.0F, SoundType.METAL));
    public static final RegistryObject<BlockDreadBase> DREAD_STONE = deferredRegister.register("dread_stone", () -> new BlockDreadBase(Material.STONE, -1.0F, 100000.0F, SoundType.STONE));
    public static final RegistryObject<BlockDreadBase> DREAD_STONE_BRICKS = deferredRegister.register("dread_stone_bricks", () -> new BlockDreadBase(Material.STONE, -1.0F, 100000.0F, SoundType.STONE));
    public static final RegistryObject<BlockDreadBase> DREAD_STONE_BRICKS_CHISELED = deferredRegister.register("dread_stone_bricks_chiseled", () -> new BlockDreadBase(Material.STONE, -1.0F, 100000.0F, SoundType.STONE));
    public static final RegistryObject<BlockDreadBase> DREAD_STONE_BRICKS_CRACKED = deferredRegister.register("dread_stone_bricks_cracked", () -> new BlockDreadBase(Material.STONE, -1.0F, 100000.0F, SoundType.STONE));
    public static final RegistryObject<BlockDreadBase> DREAD_STONE_BRICKS_MOSSY = deferredRegister.register("dread_stone_bricks_mossy", () -> new BlockDreadBase(Material.STONE, -1.0F, 100000.0F, SoundType.STONE));
    public static final RegistryObject<BlockDreadBase> DREAD_STONE_TILE = deferredRegister.register("dread_stone_tile", () -> new BlockDreadBase(Material.STONE, -1.0F, 100000.0F, SoundType.STONE));
    public static final RegistryObject<Block> DREAD_STONE_FACE = deferredRegister.register("dread_stone_face", () -> new BlockDreadStoneFace());
    public static final RegistryObject<Block> DREAD_TORCH = deferredRegister.register("dread_torch", () -> new BlockDreadTorch());
    public static final RegistryObject<Block> DREAD_TORCH_WALL = deferredRegister.register("dread_torch_wall", () -> new BlockDreadTorchWall());
    public static final RegistryObject<Block> DREAD_STONE_BRICKS_STAIRS = deferredRegister.register("dread_stone_stairs", () -> new BlockGenericStairs(DREAD_STONE_BRICKS.get().defaultBlockState()));
    public static final RegistryObject<Block> DREAD_STONE_BRICKS_SLAB = deferredRegister.register("dread_stone_slab", () -> new SlabBlock(BlockBehaviour.Properties.of(Material.STONE).strength(10F, 10000F)));
    public static final RegistryObject<Block> DREADWOOD_LOG = deferredRegister.register("dreadwood_log", () -> new BlockDreadWoodLog());
    public static final RegistryObject<BlockDreadBase> DREADWOOD_PLANKS = deferredRegister.register("dreadwood_planks", () -> new BlockDreadBase(Material.WOOD, -1.0F, 100000.0F, SoundType.WOOD));
    public static final RegistryObject<Block> DREADWOOD_PLANKS_LOCK = deferredRegister.register("dreadwood_planks_lock", () -> new BlockDreadWoodLock());
    public static final RegistryObject<Block> DREAD_PORTAL = deferredRegister.register("dread_portal", () -> new BlockDreadPortal());
    public static final RegistryObject<Block> DREAD_SPAWNER = deferredRegister.register("dread_spawner", () -> new BlockDreadSpawner());
    public static final RegistryObject<Block> BURNT_TORCH = deferredRegister.register("burnt_torch", () -> new BlockBurntTorch());
    public static final RegistryObject<Block> BURNT_TORCH_WALL = deferredRegister.register("burnt_torch_wall", () -> new BlockBurntTorchWall());
    public static final RegistryObject<Block> GHOST_CHEST = deferredRegister.register("ghost_chest", () -> new BlockGhostChest());
    public static final RegistryObject<Block> GRAVEYARD_SOIL = deferredRegister.register("graveyard_soil", () -> new BlockGraveyardSoil());

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
                //deferredRegister.register("block_sea_serpent_scale_block_%s".formatted(color.name().toLowerCase()), () -> color.scaleBlock.get());
                //event.getRegistry().register(color.scaleBlock.get());
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    //@SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void registerBlockItems(RegistryEvent.Register<Item> event) {
        IafBlockRegistry.deferredRegister.getEntries().stream()
                .map(RegistryObject::get)
                .forEach(block -> event.getRegistry().register(registerItemBlock(block)));
        // ItemBlocks
        for (EnumSeaSerpent color : EnumSeaSerpent.values()) {
            Item.Properties props = new Item.Properties();
            props.tab(IceAndFire.TAB_BLOCKS);
            Supplier<BlockItem> lambda = () -> new BlockItem(color.scaleBlock.get(), props);
            //RegistryObject<?> itemBlock = IafItemRegistry.deferredRegister.register(color.resourceName, lambda);
            BlockItem itemBlock = new BlockItem(color.scaleBlock.get(), props);
            itemBlock.setRegistryName(color.scaleBlock.get().getRegistryName());
            event.getRegistry().register(itemBlock);
        }
    }
    public static Item registerItemBlock(Block block) {
        if (!(block instanceof WallTorchBlock)) {
            Item.Properties props = new Item.Properties();
            if (!(block instanceof INoTab) || ((INoTab) block).shouldBeInTab()) {
                props.tab(IceAndFire.TAB_BLOCKS);
            }
            BlockItem itemBlock;
            if (block instanceof IWallBlock) {
                itemBlock = new StandingAndWallBlockItem((Block) block, ((IWallBlock) block).wallBlock(), props);
            } else if (block instanceof BlockGhostChest || block instanceof BlockDreadPortal || block instanceof BlockPixieHouse) {
                itemBlock = new BlockItemWithRender((Block) block, props);
            } else {
                itemBlock = new BlockItem((Block) block, props);
            }
            itemBlock.setRegistryName(((Block) block).getRegistryName());
            return itemBlock;
        }
        return new BlockItem(block, new Item.Properties());
    }
}
