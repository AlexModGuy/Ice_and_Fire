package com.github.alexthe666.iceandfire;

import com.github.alexthe666.iceandfire.block.*;
import com.github.alexthe666.iceandfire.entity.*;
import com.github.alexthe666.iceandfire.enums.EnumDragonArmor;
import com.github.alexthe666.iceandfire.enums.EnumSeaSerpent;
import com.github.alexthe666.iceandfire.enums.EnumSkullType;
import com.github.alexthe666.iceandfire.enums.EnumTroll;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.item.block.ItemBlockMyrmexResin;
import com.github.alexthe666.iceandfire.item.block.ItemBlockPodium;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import com.github.alexthe666.iceandfire.recipe.IafRecipeRegistry;
import com.github.alexthe666.iceandfire.world.IafWorldRegistry;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;

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
    public static void registerEntities(RegistryEvent.Register<EntityEntry> event) {
        registerUnspawnable(EntityEntryBuilder.<EntityDragonEgg>create(), event, EntityDragonEgg.class, "dragonegg", 1);
        registerUnspawnable(EntityEntryBuilder.<EntityDragonArrow>create(), event, EntityDragonArrow.class, "dragonarrow", 2);
        registerUnspawnable(EntityEntryBuilder.<EntityDragonSkull>create(), event, EntityDragonSkull.class, "dragonskull", 3);
        registerSpawnable(EntityEntryBuilder.<EntityFireDragon>create(), event, EntityFireDragon.class, "firedragon", 5, 0X340000, 0XA52929, 256, 3);
        registerSpawnable(EntityEntryBuilder.<EntityIceDragon>create(), event, EntityIceDragon.class, "icedragon", 7, 0XB5DDFB, 0X7EBAF0, 256, 3);
        registerUnspawnable(EntityEntryBuilder.<EntityDragonFireCharge>create(), event, EntityDragonFireCharge.class, "dragonfirecharge", 8);
        registerUnspawnable(EntityEntryBuilder.<EntityDragonIceCharge>create(), event, EntityDragonIceCharge.class, "dragonicecharge", 9);
        registerSpawnable(EntityEntryBuilder.<EntitySnowVillager>create(), event, EntitySnowVillager.class, "snowvillager", 10, 0X3C2A23, 0X70B1CF);
        registerUnspawnable(EntityEntryBuilder.<EntityHippogryphEgg>create(), event, EntityHippogryphEgg.class, "hippogryphegg", 11);
        registerSpawnable(EntityEntryBuilder.<EntityHippogryph>create(), event, EntityHippogryph.class, "hippogryph", 12, 0XD8D8D8, 0XD1B55D);
        registerUnspawnable(EntityEntryBuilder.<EntityStoneStatue>create(), event, EntityStoneStatue.class, "stonestatue", 13);
        registerSpawnable(EntityEntryBuilder.<EntityGorgon>create(), event, EntityGorgon.class, "gorgon", 14, 0XD0D99F, 0X684530);
        registerSpawnable(EntityEntryBuilder.<EntityPixie>create(), event, EntityPixie.class, "if_pixie", 15, 0XFF7F89, 0XE2CCE2);
        registerSpawnable(EntityEntryBuilder.<EntityCyclops>create(), event, EntityCyclops.class, "cyclops", 17, 0XB0826E, 0X3A1F0F);
        registerSpawnable(EntityEntryBuilder.<EntitySiren>create(), event, EntitySiren.class, "siren", 18, 0X8EE6CA, 0XF2DFC8);
        registerSpawnable(EntityEntryBuilder.<EntityHippocampus>create(), event, EntityHippocampus.class, "hippocampus", 19, 0X4491C7, 0X4FC56B);
        registerSpawnable(EntityEntryBuilder.<EntityDeathWorm>create(), event, EntityDeathWorm.class, "deathworm", 20, 0XD1CDA3, 0X423A3A);
        registerUnspawnable(EntityEntryBuilder.<EntityDeathWormEgg>create(), event, EntityDeathWormEgg.class, "deathwormegg", 21);
        registerSpawnable(EntityEntryBuilder.<EntityCockatrice>create(), event, EntityCockatrice.class, "if_cockatrice", 22, 0X8F5005, 0X4F5A23);
        registerUnspawnable(EntityEntryBuilder.<EntityCockatriceEgg>create(), event, EntityCockatriceEgg.class, "if_cockatriceegg", 23);
        registerSpawnable(EntityEntryBuilder.<EntityStymphalianBird>create(), event, EntityStymphalianBird.class, "stymphalianbird", 24, 0X744F37, 0X9E6C4B);
        registerUnspawnable(EntityEntryBuilder.<EntityStymphalianFeather>create(), event, EntityStymphalianFeather.class, "stymphalianfeather", 25);
        registerUnspawnable(EntityEntryBuilder.<EntityStymphalianArrow>create(), event, EntityStymphalianArrow.class, "stymphalianarrow", 26);
        registerSpawnable(EntityEntryBuilder.<EntityTroll>create(), event, EntityTroll.class, "if_troll", 27, 0X3D413D, 0X58433A);
        registerSpawnable(EntityEntryBuilder.<EntityMyrmexWorker>create(), event, EntityMyrmexWorker.class, "myrmex_worker", 28, 0XA16026, 0X594520);
        registerSpawnable(EntityEntryBuilder.<EntityMyrmexSoldier>create(), event, EntityMyrmexSoldier.class, "myrmex_soldier", 29, 0XA16026, 0X7D622D);
        registerSpawnable(EntityEntryBuilder.<EntityMyrmexSentinel>create(), event, EntityMyrmexSentinel.class, "myrmex_sentinel", 30, 0XA16026, 0XA27F3A);
        registerSpawnable(EntityEntryBuilder.<EntityMyrmexRoyal>create(), event, EntityMyrmexRoyal.class, "myrmex_royal", 31, 0XA16026, 0XC79B48);
        registerSpawnable(EntityEntryBuilder.<EntityMyrmexQueen>create(), event, EntityMyrmexQueen.class, "myrmex_queen", 32, 0XA16026, 0XECB855);
        registerUnspawnable(EntityEntryBuilder.<EntityMyrmexEgg>create(), event, EntityMyrmexEgg.class, "myrmex_egg", 33);
        registerSpawnable(EntityEntryBuilder.<EntityAmphithere>create(), event, EntityAmphithere.class, "amphithere", 34, 0X597535, 0X00AA98);
        registerUnspawnable(EntityEntryBuilder.<EntityAmphithereArrow>create(), event, EntityAmphithereArrow.class, "amphitherearrow", 35);
        registerSpawnable(EntityEntryBuilder.<EntitySeaSerpent>create(), event, EntitySeaSerpent.class, "seaserpent", 36, 0X008299, 0XC5E6E7, 256, 3);
        registerUnspawnable(EntityEntryBuilder.<EntitySeaSerpentBubbles>create(), event, EntitySeaSerpentBubbles.class, "seaserpentbubble", 37);
        registerUnspawnable(EntityEntryBuilder.<EntitySeaSerpentArrow>create(), event, EntitySeaSerpentArrow.class, "seaserpentarrow", 38);
        registerUnspawnable(EntityEntryBuilder.<EntityChainTie>create(), event, EntityChainTie.class, "chaintie", 39);
        registerUnspawnable(EntityEntryBuilder.<EntityPixieCharge>create(), event, EntityPixieCharge.class, "pixiecharge", 40);
        registerUnspawnable(EntityEntryBuilder.<EntityMyrmexSwarmer>create(), event, EntityMyrmexSwarmer.class, "myrmex_swarmer", 41);
        registerUnspawnable(EntityEntryBuilder.<EntityTideTrident>create(), event, EntityTideTrident.class, "tide_trident", 42);
        registerUnspawnable(EntityEntryBuilder.<EntityMobSkull>create(), event, EntityMobSkull.class, "if_mob_skull", 43);
        registerSpawnable(EntityEntryBuilder.<EntityDreadThrall>create(), event, EntityDreadThrall.class, "dread_thrall", 44, 0XE0E6E6, 0X00FFFF);
        registerSpawnable(EntityEntryBuilder.<EntityDreadGhoul>create(), event, EntityDreadGhoul.class, "dread_ghoul", 45, 0XE0E6E6, 0X7B838A);
        registerSpawnable(EntityEntryBuilder.<EntityDreadBeast>create(), event, EntityDreadBeast.class, "dread_beast", 46, 0XE0E6E6, 0X38373C);
        registerSpawnable(EntityEntryBuilder.<EntityDreadScuttler>create(), event, EntityDreadScuttler.class, "dread_scuttler", 47, 0XE0E6E6, 0X4D5667);
        registerSpawnable(EntityEntryBuilder.<EntityDreadLich>create(), event, EntityDreadLich.class, "dread_lich", 48, 0XE0E6E6, 0X274860);
        registerUnspawnable(EntityEntryBuilder.<EntityDreadLichSkull>create(), event, EntityDreadLichSkull.class, "dread_lich_skull", 49);
        registerSpawnable(EntityEntryBuilder.<EntityDreadKnight>create(), event, EntityDreadKnight.class, "dread_knight", 50, 0XE0E6E6, 0X4A6C6E);
        registerSpawnable(EntityEntryBuilder.<EntityDreadHorse>create(), event, EntityDreadHorse.class, "dread_horse", 51, 0XE0E6E6, 0XACACAC);
        registerSpawnable(EntityEntryBuilder.<EntityHydra>create(), event, EntityHydra.class, "if_hydra", 52, 0X8B8B78, 0X2E372B, 256, 3);
        registerUnspawnable(EntityEntryBuilder.<EntityHydraBreath>create(), event, EntityHydraBreath.class, "hydra_breath", 53);
        registerUnspawnable(EntityEntryBuilder.<EntityHydraArrow>create(), event, EntityHydraArrow.class, "hydra_arrow", 54);
    }

    public static void registerSpawnable(EntityEntryBuilder builder, RegistryEvent.Register<EntityEntry> event, Class<? extends Entity> entityClass, String name, int id, int mainColor, int subColor) {
        id += 900;
        builder.entity(entityClass);
        builder.id(new ResourceLocation(IceAndFire.MODID, name), id);
        builder.name(name);
        builder.egg(mainColor, subColor);
        builder.tracker(64, 1, true);
        event.getRegistry().register(builder.build());
    }

    public static void registerSpawnable(EntityEntryBuilder builder, RegistryEvent.Register<EntityEntry> event, Class<? extends Entity> entityClass, String name, int id, int mainColor, int subColor, int range, int frequency) {
        id += 900;
        builder.entity(entityClass);
        builder.id(new ResourceLocation(IceAndFire.MODID, name), id);
        builder.name(name);
        builder.egg(mainColor, subColor);
        builder.tracker(range, frequency, true);
        event.getRegistry().register(builder.build());
    }

    public static void registerUnspawnable(EntityEntryBuilder builder, RegistryEvent.Register<EntityEntry> event, Class<? extends Entity> entityClass, String name, int id) {
        id += 900;
        builder.entity(entityClass);
        builder.id(new ResourceLocation(IceAndFire.MODID, name), id);
        builder.name(name);
        builder.tracker(64, 1, true);
        event.getRegistry().register(builder.build());
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        // ItemBlocks
        try {
            for (Field f : IafBlockRegistry.class.getDeclaredFields()) {
                Object obj = f.get(null);
                if (obj instanceof Block) {
                    ItemBlock itemBlock;
                    if (obj == IafBlockRegistry.jar_pixie) {
                        itemBlock = ((BlockJar) obj).new ItemBlockJar((Block) obj);
                    } else if (obj instanceof BlockPixieHouse) {
                        itemBlock = ((BlockPixieHouse) obj).new ItemBlockPixieHouse((Block) obj);
                    } else if (obj instanceof BlockPodium) {
                        itemBlock = new ItemBlockPodium((Block) obj);
                    } else if (obj instanceof BlockMyrmexResin) {
                        itemBlock = new ItemBlockMyrmexResin((Block) obj);
                    } else if (obj instanceof BlockGenericSlab) {
                        itemBlock = ((BlockGenericSlab)obj).getItemBlock();
                    } else {
                        itemBlock = new ItemBlock((Block) obj);
                    }
                    itemBlock.setRegistryName(((Block) obj).getRegistryName());
                    event.getRegistry().register(itemBlock);
                } else if (obj instanceof Block[]) {
                    for (Block block : (Block[]) obj) {
                        ItemBlock itemBlock = new ItemBlock(block);
                        itemBlock.setRegistryName(block.getRegistryName());
                        event.getRegistry().register(itemBlock);
                    }
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        for (EnumSeaSerpent color : EnumSeaSerpent.values()) {
            ItemBlock itemBlock = new ItemBlock(color.scaleBlock);
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
    }

    @SubscribeEvent
    public static void registerBiomes(RegistryEvent.Register<Biome> event) {
        event.getRegistry().register(IafWorldRegistry.GLACIER_BIOME);
        BiomeDictionary.addTypes(IafWorldRegistry.GLACIER_BIOME, BiomeDictionary.Type.SNOWY, BiomeDictionary.Type.COLD, BiomeDictionary.Type.SPARSE, BiomeDictionary.Type.DEAD, BiomeDictionary.Type.WASTELAND);
        //BiomeDictionary.addTypes(ModWorld.DREADLANDS_BIOME, BiomeDictionary.Type.SNOWY, BiomeDictionary.Type.COLD, BiomeDictionary.Type.SPOOKY, BiomeDictionary.Type.DEAD, BiomeDictionary.Type.WASTELAND);
        if (IceAndFire.CONFIG.spawnGlaciers) {
            BiomeManager.addSpawnBiome(IafWorldRegistry.GLACIER_BIOME);
            BiomeManager.addBiome(BiomeManager.BiomeType.COOL, new BiomeManager.BiomeEntry(IafWorldRegistry.GLACIER_BIOME, IceAndFire.CONFIG.glacierSpawnChance));

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

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equalsIgnoreCase(IceAndFire.MODID)) {
            IceAndFire.syncConfig();
        }
    }

    public int getDragon3rdPersonView() {
        return 0;
    }

    public void setDragon3rdPersonView(int view) {
    }

    public void openMyrmexAddRoomGui(ItemStack staff, BlockPos pos, EnumFacing facing) {
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
}
