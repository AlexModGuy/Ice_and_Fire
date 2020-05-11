package com.github.alexthe666.iceandfire;

import com.github.alexthe666.iceandfire.block.*;
import com.github.alexthe666.iceandfire.entity.*;
import com.github.alexthe666.iceandfire.enums.EnumDragonArmor;
import com.github.alexthe666.iceandfire.enums.EnumSeaSerpent;
import com.github.alexthe666.iceandfire.enums.EnumSkullType;
import com.github.alexthe666.iceandfire.enums.EnumTroll;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import com.github.alexthe666.iceandfire.recipe.IafRecipeRegistry;
import com.github.alexthe666.iceandfire.world.IafWorldRegistry;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.lang.reflect.Field;

@Mod.EventBusSubscriber
public class CommonProxy {

    @SubscribeEvent
    public static void registerSoundEvents(final RegistryEvent.Register<SoundEvent> event) {
        try {
            for (Field f : IafSoundRegistry.class.getDeclaredFields()) {
                Object obj = f.get(null);
                if (obj instanceof SoundEvent) {
                    event.getRegistry().register((SoundEvent) obj);
                } else if (obj instanceof SoundEvent[]) {
                    for (SoundEvent soundEvent : (SoundEvent[]) obj) {
                        event.getRegistry().register(soundEvent);
                    }
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        try {
            for (Field f : IafBlockRegistry.class.getDeclaredFields()) {
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
                color.scaleBlock = new BlockSeaSerpentScales(color.resourceName, color.color);
                event.getRegistry().register(color.scaleBlock);
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        // ItemBlocks
        try {
            for (Field f : IafBlockRegistry.class.getDeclaredFields()) {
                Object obj = f.get(null);
                if (obj instanceof Block) {
                    Item.Properties props = new Item.Properties();
                    BlockItem itemBlock = new BlockItem((Block) obj, props);
                    itemBlock.setRegistryName(((Block) obj).getRegistryName());
                    event.getRegistry().register(itemBlock);
                } else if (obj instanceof Block[]) {
                    for (Block block : (Block[]) obj) {
                        Item.Properties props = new Item.Properties();
                        BlockItem itemBlock = new BlockItem(block, props);
                        itemBlock.setRegistryName(block.getRegistryName());
                        event.getRegistry().register(itemBlock);
                    }
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        for (EnumSeaSerpent color : EnumSeaSerpent.values()) {
            Item.Properties props = new Item.Properties();
            BlockItem itemBlock = new BlockItem(color.scaleBlock, props);
            itemBlock.setRegistryName(color.scaleBlock.getRegistryName());
            event.getRegistry().register(itemBlock);
        }

        // Items
        try {
            for (Field f : IafItemRegistry.class.getDeclaredFields()) {
                Object obj = f.get(null);
                if (obj instanceof Item) {
                    event.getRegistry().register((Item) obj);
                } else if (obj instanceof Item[]) {
                    for (Item item : (Item[]) obj) {
                        event.getRegistry().register(item);
                    }
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        for (EnumDragonArmor armor : EnumDragonArmor.values()) {
            event.getRegistry().register(armor.helmet);
            event.getRegistry().register(armor.chestplate);
            event.getRegistry().register(armor.leggings);
            event.getRegistry().register(armor.boots);
        }
        for (EnumSeaSerpent armor : EnumSeaSerpent.values()) {
            event.getRegistry().register(armor.scale);
            event.getRegistry().register(armor.helmet);
            event.getRegistry().register(armor.chestplate);
            event.getRegistry().register(armor.leggings);
            event.getRegistry().register(armor.boots);
        }
        for (EnumTroll.Weapon weapon : EnumTroll.Weapon.values()) {
            event.getRegistry().register(weapon.item);
        }
        for (EnumTroll troll : EnumTroll.values()) {
            event.getRegistry().register(troll.leather);
            event.getRegistry().register(troll.helmet);
            event.getRegistry().register(troll.chestplate);
            event.getRegistry().register(troll.leggings);
            event.getRegistry().register(troll.boots);
        }
        for (EnumSkullType skull : EnumSkullType.values()) {
            event.getRegistry().register(skull.skull_item);
        }
        IafRecipeRegistry.preInit();
        //spawn Eggs
        event.getRegistry().register(new SpawnEggItem(IafEntityRegistry.FIRE_DRAGON, 0X340000, 0XA52929, new Item.Properties().group(IceAndFire.TAB_ITEMS)).setRegistryName("iceandfire:spawn_egg_fire_dragon"));
        event.getRegistry().register(new SpawnEggItem(IafEntityRegistry.ICE_DRAGON, 0XB5DDFB, 0X7EBAF0, new Item.Properties().group(IceAndFire.TAB_ITEMS)).setRegistryName("iceandfire:spawn_egg_ice_dragon"));
        event.getRegistry().register(new SpawnEggItem(IafEntityRegistry.HIPPOGRYPH, 0XD8D8D8, 0XD1B55D, new Item.Properties().group(IceAndFire.TAB_ITEMS)).setRegistryName("iceandfire:spawn_egg_hippogryph"));
        event.getRegistry().register(new SpawnEggItem(IafEntityRegistry.GORGON, 0XD0D99F, 0X684530, new Item.Properties().group(IceAndFire.TAB_ITEMS)).setRegistryName("iceandfire:spawn_egg_gorgon"));
        event.getRegistry().register(new SpawnEggItem(IafEntityRegistry.PIXIE, 0XFF7F89, 0XE2CCE2, new Item.Properties().group(IceAndFire.TAB_ITEMS)).setRegistryName("iceandfire:spawn_egg_pixie"));
        event.getRegistry().register(new SpawnEggItem(IafEntityRegistry.CYCLOPS, 0XB0826E, 0X3A1F0F, new Item.Properties().group(IceAndFire.TAB_ITEMS)).setRegistryName("iceandfire:spawn_egg_cyclops"));
        event.getRegistry().register(new SpawnEggItem(IafEntityRegistry.SIREN, 0X8EE6CA, 0XF2DFC8, new Item.Properties().group(IceAndFire.TAB_ITEMS)).setRegistryName("iceandfire:spawn_egg_siren"));
        event.getRegistry().register(new SpawnEggItem(IafEntityRegistry.HIPPOCAMPUS, 0X4491C7, 0X4FC56B, new Item.Properties().group(IceAndFire.TAB_ITEMS)).setRegistryName("iceandfire:spawn_egg_hippocampus"));
        event.getRegistry().register(new SpawnEggItem(IafEntityRegistry.DEATH_WORM, 0XD1CDA3, 0X423A3A, new Item.Properties().group(IceAndFire.TAB_ITEMS)).setRegistryName("iceandfire:spawn_egg_death_worm"));
        event.getRegistry().register(new SpawnEggItem(IafEntityRegistry.COCKATRICE, 0X8F5005, 0X4F5A23, new Item.Properties().group(IceAndFire.TAB_ITEMS)).setRegistryName("iceandfire:spawn_egg_cockatrice"));
        event.getRegistry().register(new SpawnEggItem(IafEntityRegistry.STYMPHALIAN_BIRD, 0X744F37, 0X9E6C4B, new Item.Properties().group(IceAndFire.TAB_ITEMS)).setRegistryName("iceandfire:spawn_egg_stymphalian_bird"));
        event.getRegistry().register(new SpawnEggItem(IafEntityRegistry.TROLL, 0X3D413D, 0X58433A, new Item.Properties().group(IceAndFire.TAB_ITEMS)).setRegistryName("iceandfire:spawn_egg_troll"));
        event.getRegistry().register(new SpawnEggItem(IafEntityRegistry.MYRMEX_WORKER, 0XA16026, 0X594520, new Item.Properties().group(IceAndFire.TAB_ITEMS)).setRegistryName("iceandfire:spawn_egg_myrmex_worker"));
        event.getRegistry().register(new SpawnEggItem(IafEntityRegistry.MYRMEX_SOLDIER, 0XA16026, 0X7D622D, new Item.Properties().group(IceAndFire.TAB_ITEMS)).setRegistryName("iceandfire:spawn_egg_myrmex_soldier"));
        event.getRegistry().register(new SpawnEggItem(IafEntityRegistry.MYRMEX_SENTINEL, 0XA16026, 0XA27F3A, new Item.Properties().group(IceAndFire.TAB_ITEMS)).setRegistryName("iceandfire:spawn_egg_myrmex_sentinel"));
        event.getRegistry().register(new SpawnEggItem(IafEntityRegistry.MYRMEX_ROYAL, 0XA16026, 0XC79B48, new Item.Properties().group(IceAndFire.TAB_ITEMS)).setRegistryName("iceandfire:spawn_egg_myrmex_royal"));
        event.getRegistry().register(new SpawnEggItem(IafEntityRegistry.MYRMEX_QUEEN, 0XA16026, 0XECB855, new Item.Properties().group(IceAndFire.TAB_ITEMS)).setRegistryName("iceandfire:spawn_egg_myrmex_queen"));
        event.getRegistry().register(new SpawnEggItem(IafEntityRegistry.AMPHITHERE, 0X597535, 0X00AA98, new Item.Properties().group(IceAndFire.TAB_ITEMS)).setRegistryName("iceandfire:spawn_egg_amphithere"));
        event.getRegistry().register(new SpawnEggItem(IafEntityRegistry.SEA_SERPENT, 0X008299, 0XC5E6E7, new Item.Properties().group(IceAndFire.TAB_ITEMS)).setRegistryName("iceandfire:spawn_egg_sea_serpent"));
        event.getRegistry().register(new SpawnEggItem(IafEntityRegistry.DREAD_THRALL, 0XE0E6E6, 0X00FFFF, new Item.Properties().group(IceAndFire.TAB_ITEMS)).setRegistryName("iceandfire:spawn_egg_dread_thrall"));
        event.getRegistry().register(new SpawnEggItem(IafEntityRegistry.DREAD_GHOUL, 0XE0E6E6, 0X7B838A, new Item.Properties().group(IceAndFire.TAB_ITEMS)).setRegistryName("iceandfire:spawn_egg_dread_ghoul"));
        event.getRegistry().register(new SpawnEggItem(IafEntityRegistry.DREAD_BEAST, 0XE0E6E6, 0X38373C, new Item.Properties().group(IceAndFire.TAB_ITEMS)).setRegistryName("iceandfire:spawn_egg_dread_beast"));
        event.getRegistry().register(new SpawnEggItem(IafEntityRegistry.DREAD_SCUTTLER, 0XE0E6E6, 0X4D5667, new Item.Properties().group(IceAndFire.TAB_ITEMS)).setRegistryName("iceandfire:spawn_egg_dread_scuttler"));
        event.getRegistry().register(new SpawnEggItem(IafEntityRegistry.DREAD_LICH, 0XE0E6E6, 0X274860, new Item.Properties().group(IceAndFire.TAB_ITEMS)).setRegistryName("iceandfire:spawn_egg_lich"));
        event.getRegistry().register(new SpawnEggItem(IafEntityRegistry.DREAD_KNIGHT, 0XE0E6E6, 0X4A6C6E, new Item.Properties().group(IceAndFire.TAB_ITEMS)).setRegistryName("iceandfire:spawn_egg_dread_knight"));
        event.getRegistry().register(new SpawnEggItem(IafEntityRegistry.DREAD_HORSE, 0XE0E6E6, 0XACACAC, new Item.Properties().group(IceAndFire.TAB_ITEMS)).setRegistryName("iceandfire:spawn_egg_dread_horse"));
        event.getRegistry().register(new SpawnEggItem(IafEntityRegistry.HYDRA, 0X8B8B78, 0X2E372B, new Item.Properties().group(IceAndFire.TAB_ITEMS)).setRegistryName("iceandfire:spawn_egg_hydra"));
    }

    @SubscribeEvent
    public static void registerBiomes(RegistryEvent.Register<Biome> event) {
        event.getRegistry().register(IafWorldRegistry.GLACIER_BIOME);
        BiomeDictionary.addTypes(IafWorldRegistry.GLACIER_BIOME, BiomeDictionary.Type.SNOWY, BiomeDictionary.Type.COLD, BiomeDictionary.Type.SPARSE, BiomeDictionary.Type.DEAD, BiomeDictionary.Type.WASTELAND);
        //BiomeDictionary.addTypes(ModWorld.DREADLANDS_BIOME, BiomeDictionary.Type.SNOWY, BiomeDictionary.Type.COLD, BiomeDictionary.Type.SPOOKY, BiomeDictionary.Type.DEAD, BiomeDictionary.Type.WASTELAND);
        if (IafConfig.spawnGlaciers) {
            BiomeManager.addSpawnBiome(IafWorldRegistry.GLACIER_BIOME);
            BiomeManager.addBiome(BiomeManager.BiomeType.COOL, new BiomeManager.BiomeEntry(IafWorldRegistry.GLACIER_BIOME, IafConfig.glacierSpawnChance));

        }
    }

    public void preRender() {

    }

    public void render() {
    }

    public void postRender() {
    }

    public void spawnParticle(String name, double x, double y, double z, double motX, double motY, double motZ) {
        spawnParticle(name, x, y, z, motX, motY, motZ, 1.0F);
    }

    public void spawnDragonParticle(String name, double x, double y, double z, double motX, double motY, double motZ, EntityDragonBase entityDragonBase) {
    }

    public void spawnParticle(String name, double x, double y, double z, double motX, double motY, double motZ, float size) {
    }

    public void openBestiaryGui(ItemStack book) {
    }

    public void openMyrmexStaffGui(ItemStack staff) {
    }

    public Object getArmorModel(int armorId) {
        return null;
    }

    public Object getFontRenderer() {
        return null;
    }

    public int getDragon3rdPersonView() {
        return 0;
    }

    public void setDragon3rdPersonView(int view) {
    }

    public void openMyrmexAddRoomGui(ItemStack staff, BlockPos pos, Direction facing) {
    }


    public Object getDreadlandsRender(int i) {
        return null;
    }

    public int getPreviousViewType() {
        return 0;
    }

    public void setPreviousViewType(int view) {
    }

    public void updateDragonArmorRender(String clear){}

    public boolean shouldSeeBestiaryContents() {
        return true;
    }

    public void setRefrencedTE(TileEntity tileEntity) {
    }
}
