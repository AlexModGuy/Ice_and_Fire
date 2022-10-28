package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.citadel.server.item.CustomArmorMaterial;
import com.github.alexthe666.citadel.server.item.CustomToolMaterial;
import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import com.github.alexthe666.iceandfire.enums.*;
import com.github.alexthe666.iceandfire.recipe.IafRecipeRegistry;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.BannerPatternItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.IModBusEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.lang.reflect.Field;
import java.util.Locale;

import static com.github.alexthe666.iceandfire.item.DragonSteelTier.*;

@Mod.EventBusSubscriber(modid = IceAndFire.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class IafItemRegistry {

    public static CustomArmorMaterial SILVER_ARMOR_MATERIAL = new IafArmorMaterial("silver", 15, new int[]{1, 4, 5, 2}, 20, SoundEvents.ARMOR_EQUIP_CHAIN, 0);
    public static CustomArmorMaterial COPPER_ARMOR_MATERIAL = new IafArmorMaterial("copper", 10, new int[]{1, 3, 4, 2}, 15, SoundEvents.ARMOR_EQUIP_GOLD, 0);
    public static CustomArmorMaterial BLINDFOLD_ARMOR_MATERIAL = new IafArmorMaterial("blindfold", 5, new int[]{1, 1, 1, 1}, 10, SoundEvents.ARMOR_EQUIP_LEATHER, 0);
    public static CustomArmorMaterial SHEEP_ARMOR_MATERIAL = new IafArmorMaterial("sheep", 5, new int[]{1, 3, 2, 1}, 15, SoundEvents.ARMOR_EQUIP_LEATHER, 0);
    public static CustomArmorMaterial MYRMEX_DESERT_ARMOR_MATERIAL = new IafArmorMaterial("myrmexdesert", 20, new int[]{3, 5, 8, 4}, 15, SoundEvents.ARMOR_EQUIP_LEATHER, 0);
    public static CustomArmorMaterial MYRMEX_JUNGLE_ARMOR_MATERIAL = new IafArmorMaterial("myrmexjungle", 20, new int[]{3, 5, 8, 4}, 15, SoundEvents.ARMOR_EQUIP_LEATHER, 0);
    public static CustomArmorMaterial EARPLUGS_ARMOR_MATERIAL = new IafArmorMaterial("earplugs", 5, new int[]{1, 1, 1, 1}, 10, SoundEvents.ARMOR_EQUIP_LEATHER, 0);
    public static CustomArmorMaterial DEATHWORM_0_ARMOR_MATERIAL = new IafArmorMaterial("yellow seathworm", 15, new int[]{2, 5, 7, 3}, 5, SoundEvents.ARMOR_EQUIP_LEATHER, 1.5F);
    public static CustomArmorMaterial DEATHWORM_1_ARMOR_MATERIAL = new IafArmorMaterial("white seathworm", 15, new int[]{2, 5, 7, 3}, 5, SoundEvents.ARMOR_EQUIP_LEATHER, 1.5F);
    public static CustomArmorMaterial DEATHWORM_2_ARMOR_MATERIAL = new IafArmorMaterial("red deathworm", 15, new int[]{2, 5, 7, 3}, 5, SoundEvents.ARMOR_EQUIP_LEATHER, 1.5F);
    public static CustomArmorMaterial TROLL_MOUNTAIN_ARMOR_MATERIAL = new IafArmorMaterial("mountain troll", 20, new int[]{2, 5, 7, 3}, 10, SoundEvents.ARMOR_EQUIP_LEATHER, 1F);
    public static CustomArmorMaterial TROLL_FOREST_ARMOR_MATERIAL = new IafArmorMaterial("forest troll", 20, new int[]{2, 5, 7, 3}, 10, SoundEvents.ARMOR_EQUIP_LEATHER, 1F);
    public static CustomArmorMaterial TROLL_FROST_ARMOR_MATERIAL = new IafArmorMaterial("frost troll", 20, new int[]{2, 5, 7, 3}, 10, SoundEvents.ARMOR_EQUIP_LEATHER, 1F);
    public static CustomArmorMaterial DRAGONSTEEL_FIRE_ARMOR_MATERIAL = new DragonsteelArmorMaterial("dragonsteel fire", (int) (0.02D * IafConfig.dragonsteelBaseDurabilityEquipment), new int[]{IafConfig.dragonsteelBaseArmor - 6, IafConfig.dragonsteelBaseArmor - 3, IafConfig.dragonsteelBaseArmor, IafConfig.dragonsteelBaseArmor - 5}, 30, SoundEvents.ARMOR_EQUIP_DIAMOND, 6.0F);
    public static CustomArmorMaterial DRAGONSTEEL_ICE_ARMOR_MATERIAL = new DragonsteelArmorMaterial("dragonsteel_ice", (int) (0.02D * IafConfig.dragonsteelBaseDurabilityEquipment), new int[]{IafConfig.dragonsteelBaseArmor - 6, IafConfig.dragonsteelBaseArmor - 3, IafConfig.dragonsteelBaseArmor, IafConfig.dragonsteelBaseArmor - 5}, 30, SoundEvents.ARMOR_EQUIP_DIAMOND, 6.0F);
    public static CustomArmorMaterial DRAGONSTEEL_LIGHTNING_ARMOR_MATERIAL = new DragonsteelArmorMaterial("dragonsteel_lightning", (int) (0.02D * IafConfig.dragonsteelBaseDurabilityEquipment), new int[]{IafConfig.dragonsteelBaseArmor - 6, IafConfig.dragonsteelBaseArmor - 3, IafConfig.dragonsteelBaseArmor, IafConfig.dragonsteelBaseArmor - 5}, 30, SoundEvents.ARMOR_EQUIP_DIAMOND, 6.0F);
    public static CustomToolMaterial SILVER_TOOL_MATERIAL = new CustomToolMaterial("silver", 2, 460, 1.0F, 11.0F, 18);
    public static CustomToolMaterial COPPER_TOOL_MATERIAL = new CustomToolMaterial("copper", 2, 300, 0.0F, 0.7F, 10);
    public static CustomToolMaterial DRAGONBONE_TOOL_MATERIAL = new CustomToolMaterial("Dragonbone", 4, 1660, 4.0F, 10.0F, 22);
    public static CustomToolMaterial FIRE_DRAGONBONE_TOOL_MATERIAL = new CustomToolMaterial("FireDragonbone", 4, 2000, 5.5F, 10F, 22);
    public static CustomToolMaterial ICE_DRAGONBONE_TOOL_MATERIAL = new CustomToolMaterial("IceDragonbone", 4, 2000, 5.5F, 10F, 22);
    public static CustomToolMaterial LIGHTNING_DRAGONBONE_TOOL_MATERIAL = new CustomToolMaterial("LightningDragonbone", 4, 2000, 5.5F, 10F, 22);
    public static CustomToolMaterial TROLL_WEAPON_TOOL_MATERIAL = new CustomToolMaterial("trollWeapon", 2, 300, 1F, 10F, 1);
    public static CustomToolMaterial MYRMEX_CHITIN_TOOL_MATERIAL = new CustomToolMaterial("MyrmexChitin", 3, 600, 1.0F, 6.0F, 8);
    public static CustomToolMaterial HIPPOGRYPH_SWORD_TOOL_MATERIAL = new CustomToolMaterial("HippogryphSword", 2, 500, 2.5F, 10F, 10);
    public static CustomToolMaterial STYMHALIAN_SWORD_TOOL_MATERIAL = new CustomToolMaterial("StymphalianSword", 2, 500, 2, 10.0F, 10);
    public static CustomToolMaterial AMPHITHERE_SWORD_TOOL_MATERIAL = new CustomToolMaterial("AmphithereSword", 2, 500, 1F, 10F, 10);
    public static CustomToolMaterial HIPPOCAMPUS_SWORD_TOOL_MATERIAL = new CustomToolMaterial("HippocampusSword", 0, 500, -2F, 0F, 50);
    public static CustomToolMaterial DREAD_SWORD_TOOL_MATERIAL = new CustomToolMaterial("DreadSword", 0, 100, 1F, 10F, 0);
    public static CustomToolMaterial DREAD_KNIGHT_TOOL_MATERIAL = new CustomToolMaterial("DreadKnightSword", 0, 1200, 13F, 0F, 10);
    public static CustomToolMaterial GHOST_SWORD_TOOL_MATERIAL = new CustomToolMaterial("GhostSword", 2, 3000, 5, 10.0F, 25);

    public static DeferredRegister<Item> deferredRegister = DeferredRegister.create(ForgeRegistries.ITEMS, IceAndFire.MODID);

    public static final RegistryObject<Item> BESTIARY = deferredRegister.register("bestiary", ItemBestiary::new);
    public static final RegistryObject<Item> MANUSCRIPT = deferredRegister.register("manuscript", ItemGeneric::new);
    public static final RegistryObject<Item> SAPPHIRE_GEM = deferredRegister.register("sapphire_gem", ItemGeneric::new);
    public static final RegistryObject<Item> SILVER_INGOT = deferredRegister.register("silver_ingot", ItemGeneric::new);
    public static final RegistryObject<Item> SILVER_NUGGET = deferredRegister.register("silver_nugget", ItemGeneric::new);
    public static final RegistryObject<Item> AMYTHEST_GEM = deferredRegister.register("amythest_gem", ItemGeneric::new);
    public static final RegistryObject<Item> COPPER_INGOT = deferredRegister.register("copper_ingot", ItemGeneric::new);
    public static final RegistryObject<Item> COPPER_NUGGET = deferredRegister.register("copper_nugget", ItemGeneric::new);
    public static final RegistryObject<Item> SILVER_HELMET = deferredRegister.register("armor_silver_metal_helmet", () -> new ItemSilverArmor(SILVER_ARMOR_MATERIAL, EquipmentSlot.HEAD));
    public static final RegistryObject<Item> SILVER_CHESTPLATE = deferredRegister.register("armor_silver_metal_chestplate", () -> new ItemSilverArmor(SILVER_ARMOR_MATERIAL, EquipmentSlot.CHEST));
    public static final RegistryObject<Item> SILVER_LEGGINGS = deferredRegister.register("armor_silver_metal_leggings", () -> new ItemSilverArmor(SILVER_ARMOR_MATERIAL, EquipmentSlot.LEGS));
    public static final RegistryObject<Item> SILVER_BOOTS = deferredRegister.register("armor_silver_metal_boots", () -> new ItemSilverArmor(SILVER_ARMOR_MATERIAL, EquipmentSlot.FEET));
    public static final RegistryObject<Item> SILVER_SWORD = deferredRegister.register("silver_sword", () -> new ItemModSword(SILVER_TOOL_MATERIAL));
    public static final RegistryObject<Item> SILVER_SHOVEL = deferredRegister.register("silver_shovel", () -> new ItemModShovel(SILVER_TOOL_MATERIAL));
    public static final RegistryObject<Item> SILVER_PICKAXE = deferredRegister.register("silver_pickaxe", () -> new ItemModPickaxe(SILVER_TOOL_MATERIAL));
    public static final RegistryObject<Item> SILVER_AXE = deferredRegister.register("silver_axe", () -> new ItemModAxe(SILVER_TOOL_MATERIAL));
    public static final RegistryObject<Item> SILVER_HOE = deferredRegister.register("silver_hoe", () -> new ItemModHoe(SILVER_TOOL_MATERIAL));

    public static final RegistryObject<Item> COPPER_HELMET = deferredRegister.register("armor_copper_metal_helmet", () -> new ItemCopperArmor(COPPER_ARMOR_MATERIAL, EquipmentSlot.HEAD));
    public static final RegistryObject<Item> COPPER_CHESTPLATE = deferredRegister.register("armor_copper_metal_chestplate", () -> new ItemCopperArmor(COPPER_ARMOR_MATERIAL, EquipmentSlot.CHEST));
    public static final RegistryObject<Item> COPPER_LEGGINGS = deferredRegister.register("armor_copper_metal_leggings", () -> new ItemCopperArmor(COPPER_ARMOR_MATERIAL, EquipmentSlot.LEGS));
    public static final RegistryObject<Item> COPPER_BOOTS = deferredRegister.register("armor_copper_metal_boots", () -> new ItemCopperArmor(COPPER_ARMOR_MATERIAL, EquipmentSlot.FEET));
    public static final RegistryObject<Item> COPPER_SWORD = deferredRegister.register("copper_sword", () -> new ItemModSword(COPPER_TOOL_MATERIAL));
    public static final RegistryObject<Item> COPPER_SHOVEL = deferredRegister.register("copper_shovel", () -> new ItemModShovel(COPPER_TOOL_MATERIAL));
    public static final RegistryObject<Item> COPPER_PICKAXE = deferredRegister.register("copper_pickaxe", () -> new ItemModPickaxe(COPPER_TOOL_MATERIAL));
    public static final RegistryObject<Item> COPPER_AXE = deferredRegister.register("copper_axe", () -> new ItemModAxe(COPPER_TOOL_MATERIAL));
    public static final RegistryObject<Item> COPPER_HOE = deferredRegister.register("copper_hoe", () -> new ItemModHoe(COPPER_TOOL_MATERIAL));

    public static final RegistryObject<Item> FIRE_STEW = deferredRegister.register("fire_stew", ItemGeneric::new);
    public static final RegistryObject<Item> FROST_STEW = deferredRegister.register("frost_stew", ItemGeneric::new);
    public static final RegistryObject<Item> LIGHTNING_STEW = deferredRegister.register("lightning_stew", ItemGeneric::new);
    public static final RegistryObject<Item> DRAGONEGG_RED = deferredRegister.register("dragonegg_red", () -> new ItemDragonEgg(EnumDragonEgg.RED));
    public static final RegistryObject<Item> DRAGONEGG_GREEN = deferredRegister.register("dragonegg_green", () -> new ItemDragonEgg(EnumDragonEgg.GREEN));
    public static final RegistryObject<Item> DRAGONEGG_BRONZE = deferredRegister.register("dragonegg_bronze", () -> new ItemDragonEgg(EnumDragonEgg.BRONZE));
    public static final RegistryObject<Item> DRAGONEGG_GRAY = deferredRegister.register("dragonegg_gray", () -> new ItemDragonEgg(EnumDragonEgg.GRAY));
    public static final RegistryObject<Item> DRAGONEGG_BLUE = deferredRegister.register("dragonegg_blue", () -> new ItemDragonEgg(EnumDragonEgg.BLUE));
    public static final RegistryObject<Item> DRAGONEGG_WHITE = deferredRegister.register("dragonegg_white", () -> new ItemDragonEgg(EnumDragonEgg.WHITE));
    public static final RegistryObject<Item> DRAGONEGG_SAPPHIRE = deferredRegister.register("dragonegg_sapphire", () -> new ItemDragonEgg(EnumDragonEgg.SAPPHIRE));
    public static final RegistryObject<Item> DRAGONEGG_SILVER = deferredRegister.register("dragonegg_silver", () -> new ItemDragonEgg(EnumDragonEgg.SILVER));
    public static final RegistryObject<Item> DRAGONEGG_ELECTRIC = deferredRegister.register("dragonegg_electric", () -> new ItemDragonEgg(EnumDragonEgg.ELECTRIC));
    public static final RegistryObject<Item> DRAGONEGG_AMYTHEST = deferredRegister.register("dragonegg_amythest", () -> new ItemDragonEgg(EnumDragonEgg.AMYTHEST));
    public static final RegistryObject<Item> DRAGONEGG_COPPER = deferredRegister.register("dragonegg_copper", () -> new ItemDragonEgg(EnumDragonEgg.COPPER));
    public static final RegistryObject<Item> DRAGONEGG_BLACK = deferredRegister.register("dragonegg_black", () -> new ItemDragonEgg(EnumDragonEgg.BLACK));
    public static final RegistryObject<Item> DRAGONSCALES_RED = deferredRegister.register("dragonscales_red", () -> new ItemDragonScales(EnumDragonEgg.RED));
    public static final RegistryObject<Item> DRAGONSCALES_GREEN = deferredRegister.register("dragonscales_green", () -> new ItemDragonScales(EnumDragonEgg.GREEN));
    public static final RegistryObject<Item> DRAGONSCALES_BRONZE = deferredRegister.register("dragonscales_bronze", () -> new ItemDragonScales(EnumDragonEgg.BRONZE));
    public static final RegistryObject<Item> DRAGONSCALES_GRAY = deferredRegister.register("dragonscales_gray", () -> new ItemDragonScales(EnumDragonEgg.GRAY));
    public static final RegistryObject<Item> DRAGONSCALES_BLUE = deferredRegister.register("dragonscales_blue", () -> new ItemDragonScales(EnumDragonEgg.BLUE));
    public static final RegistryObject<Item> DRAGONSCALES_WHITE = deferredRegister.register("dragonscales_white", () -> new ItemDragonScales(EnumDragonEgg.WHITE));
    public static final RegistryObject<Item> DRAGONSCALES_SAPPHIRE = deferredRegister.register("dragonscales_sapphire", () -> new ItemDragonScales(EnumDragonEgg.SAPPHIRE));
    public static final RegistryObject<Item> DRAGONSCALES_SILVER = deferredRegister.register("dragonscales_silver", () -> new ItemDragonScales(EnumDragonEgg.SILVER));
    public static final RegistryObject<Item> DRAGONSCALES_ELECTRIC = deferredRegister.register("dragonscales_electric", () -> new ItemDragonScales(EnumDragonEgg.ELECTRIC));
    public static final RegistryObject<Item> DRAGONSCALES_AMYTHEST = deferredRegister.register("dragonscales_amythest", () -> new ItemDragonScales(EnumDragonEgg.AMYTHEST));
    public static final RegistryObject<Item> DRAGONSCALES_COPPER = deferredRegister.register("dragonscales_copper", () -> new ItemDragonScales(EnumDragonEgg.COPPER));
    public static final RegistryObject<Item> DRAGONSCALES_BLACK = deferredRegister.register("dragonscales_black", () -> new ItemDragonScales(EnumDragonEgg.BLACK));
    public static final RegistryObject<Item> DRAGON_BONE = deferredRegister.register("dragonbone", () -> new ItemDragonBone());
    public static final RegistryObject<Item> WITHERBONE = deferredRegister.register("witherbone", ItemGeneric::new);
    public static final RegistryObject<Item> FISHING_SPEAR = deferredRegister.register("fishing_spear", () -> new ItemFishingSpear());
    public static final RegistryObject<Item> WITHER_SHARD = deferredRegister.register("wither_shard", ItemGeneric::new);
    public static final RegistryObject<Item> DRAGONBONE_SWORD = deferredRegister.register("dragonbone_sword", () -> new ItemModSword(DRAGONBONE_TOOL_MATERIAL));
    public static final RegistryObject<Item> DRAGONBONE_SHOVEL = deferredRegister.register("dragonbone_shovel", () -> new ItemModShovel(DRAGONBONE_TOOL_MATERIAL));
    public static final RegistryObject<Item> DRAGONBONE_PICKAXE = deferredRegister.register("dragonbone_pickaxe", () -> new ItemModPickaxe(DRAGONBONE_TOOL_MATERIAL));
    public static final RegistryObject<Item> DRAGONBONE_AXE = deferredRegister.register("dragonbone_axe", () -> new ItemModAxe(DRAGONBONE_TOOL_MATERIAL));
    public static final RegistryObject<Item> DRAGONBONE_HOE = deferredRegister.register("dragonbone_hoe", () -> new ItemModHoe(DRAGONBONE_TOOL_MATERIAL));
    public static final RegistryObject<Item> DRAGONBONE_SWORD_FIRE = deferredRegister.register("dragonbone_sword_fire", () -> new ItemAlchemySword(FIRE_DRAGONBONE_TOOL_MATERIAL));
    public static final RegistryObject<Item> DRAGONBONE_SWORD_ICE = deferredRegister.register("dragonbone_sword_ice", () -> new ItemAlchemySword(ICE_DRAGONBONE_TOOL_MATERIAL));
    public static final RegistryObject<Item> DRAGONBONE_SWORD_LIGHTNING = deferredRegister.register("dragonbone_sword_lightning", () -> new ItemAlchemySword(LIGHTNING_DRAGONBONE_TOOL_MATERIAL));
    public static final RegistryObject<Item> DRAGONBONE_ARROW = deferredRegister.register("dragonbone_arrow", () -> new ItemDragonArrow());
    public static final RegistryObject<Item> DRAGON_BOW = deferredRegister.register("dragonbone_bow", () -> new ItemDragonBow());
    public static final RegistryObject<Item> DRAGON_SKULL_FIRE = deferredRegister.register(ItemDragonSkull.getName(0), () -> new ItemDragonSkull(0));
    public static final RegistryObject<Item> DRAGON_SKULL_ICE = deferredRegister.register(ItemDragonSkull.getName(1), () -> new ItemDragonSkull(1));
    public static final RegistryObject<Item> DRAGON_SKULL_LIGHTNING = deferredRegister.register(ItemDragonSkull.getName(2), () -> new ItemDragonSkull(2));
    public static final RegistryObject<ItemDragonArmor> DRAGONARMOR_IRON_0 = deferredRegister.register("dragonarmor_iron_" + ItemDragonArmor.getNameForSlot(0), () -> new ItemDragonArmor(0, 0));
    public static final RegistryObject<ItemDragonArmor> DRAGONARMOR_IRON_1 = deferredRegister.register("dragonarmor_iron_" + ItemDragonArmor.getNameForSlot(1), () -> new ItemDragonArmor(0, 1));
    public static final RegistryObject<ItemDragonArmor> DRAGONARMOR_IRON_2 = deferredRegister.register("dragonarmor_iron_" + ItemDragonArmor.getNameForSlot(2), () -> new ItemDragonArmor(0, 2));
    public static final RegistryObject<ItemDragonArmor> DRAGONARMOR_IRON_3 = deferredRegister.register("dragonarmor_iron_" + ItemDragonArmor.getNameForSlot(3), () -> new ItemDragonArmor(0, 3));
    public static final RegistryObject<ItemDragonArmor> DRAGONARMOR_COPPER_0 = deferredRegister.register("dragonarmor_copper_" + ItemDragonArmor.getNameForSlot(0), () -> new ItemDragonArmor(6, 0));
    public static final RegistryObject<ItemDragonArmor> DRAGONARMOR_COPPER_1 = deferredRegister.register("dragonarmor_copper_" + ItemDragonArmor.getNameForSlot(1), () -> new ItemDragonArmor(6, 1));
    public static final RegistryObject<ItemDragonArmor> DRAGONARMOR_COPPER_2 = deferredRegister.register("dragonarmor_copper_" + ItemDragonArmor.getNameForSlot(2), () -> new ItemDragonArmor(6, 2));
    public static final RegistryObject<ItemDragonArmor> DRAGONARMOR_COPPER_3 = deferredRegister.register("dragonarmor_copper_" + ItemDragonArmor.getNameForSlot(3), () -> new ItemDragonArmor(6, 3));
    public static final RegistryObject<ItemDragonArmor> DRAGONARMOR_GOLD_0 = deferredRegister.register("dragonarmor_gold_" + ItemDragonArmor.getNameForSlot(0), () -> new ItemDragonArmor(1, 0));
    public static final RegistryObject<ItemDragonArmor> DRAGONARMOR_GOLD_1 = deferredRegister.register("dragonarmor_gold_" + ItemDragonArmor.getNameForSlot(1), () -> new ItemDragonArmor(1, 1));
    public static final RegistryObject<ItemDragonArmor> DRAGONARMOR_GOLD_2 = deferredRegister.register("dragonarmor_gold_" + ItemDragonArmor.getNameForSlot(2), () -> new ItemDragonArmor(1, 2));
    public static final RegistryObject<ItemDragonArmor> DRAGONARMOR_GOLD_3 = deferredRegister.register("dragonarmor_gold_" + ItemDragonArmor.getNameForSlot(3), () -> new ItemDragonArmor(1, 3));
    public static final RegistryObject<ItemDragonArmor> DRAGONARMOR_DIAMOND_0 = deferredRegister.register("dragonarmor_diamond_" + ItemDragonArmor.getNameForSlot(0), () -> new ItemDragonArmor(2, 0));
    public static final RegistryObject<ItemDragonArmor> DRAGONARMOR_DIAMOND_1 = deferredRegister.register("dragonarmor_diamond_" + ItemDragonArmor.getNameForSlot(1), () -> new ItemDragonArmor(2, 1));
    public static final RegistryObject<ItemDragonArmor> DRAGONARMOR_DIAMOND_2 = deferredRegister.register("dragonarmor_diamond_" + ItemDragonArmor.getNameForSlot(2), () -> new ItemDragonArmor(2, 2));
    public static final RegistryObject<ItemDragonArmor> DRAGONARMOR_DIAMOND_3 = deferredRegister.register("dragonarmor_diamond_" + ItemDragonArmor.getNameForSlot(3), () -> new ItemDragonArmor(2, 3));
    public static final RegistryObject<ItemDragonArmor> DRAGONARMOR_SILVER_0 = deferredRegister.register("dragonarmor_silver_" + ItemDragonArmor.getNameForSlot(0), () -> new ItemDragonArmor(3, 0));
    public static final RegistryObject<ItemDragonArmor> DRAGONARMOR_SILVER_1 = deferredRegister.register("dragonarmor_silver_" + ItemDragonArmor.getNameForSlot(1), () -> new ItemDragonArmor(3, 1));
    public static final RegistryObject<ItemDragonArmor> DRAGONARMOR_SILVER_2 = deferredRegister.register("dragonarmor_silver_" + ItemDragonArmor.getNameForSlot(2), () -> new ItemDragonArmor(3, 2));
    public static final RegistryObject<ItemDragonArmor> DRAGONARMOR_SILVER_3 = deferredRegister.register("dragonarmor_silver_" + ItemDragonArmor.getNameForSlot(3), () -> new ItemDragonArmor(3, 3));
    public static final RegistryObject<ItemDragonArmor> DRAGONARMOR_DRAGONSTEEL_FIRE_0 = deferredRegister.register("dragonarmor_dragonsteel_fire_" + ItemDragonArmor.getNameForSlot(0), () -> new ItemDragonArmor(4, 0));
    public static final RegistryObject<ItemDragonArmor> DRAGONARMOR_DRAGONSTEEL_FIRE_1 = deferredRegister.register("dragonarmor_dragonsteel_fire_" + ItemDragonArmor.getNameForSlot(1), () -> new ItemDragonArmor(4, 1));
    public static final RegistryObject<ItemDragonArmor> DRAGONARMOR_DRAGONSTEEL_FIRE_2 = deferredRegister.register("dragonarmor_dragonsteel_fire_" + ItemDragonArmor.getNameForSlot(2), () -> new ItemDragonArmor(4, 2));
    public static final RegistryObject<ItemDragonArmor> DRAGONARMOR_DRAGONSTEEL_FIRE_3 = deferredRegister.register("dragonarmor_dragonsteel_fire_" + ItemDragonArmor.getNameForSlot(3), () -> new ItemDragonArmor(4, 3));
    public static final RegistryObject<ItemDragonArmor> DRAGONARMOR_DRAGONSTEEL_ICE_0 = deferredRegister.register("dragonarmor_dragonsteel_ice_" + ItemDragonArmor.getNameForSlot(0), () -> new ItemDragonArmor(5, 0));
    public static final RegistryObject<ItemDragonArmor> DRAGONARMOR_DRAGONSTEEL_ICE_1 = deferredRegister.register("dragonarmor_dragonsteel_ice_" + ItemDragonArmor.getNameForSlot(1), () -> new ItemDragonArmor(5, 1));
    public static final RegistryObject<ItemDragonArmor> DRAGONARMOR_DRAGONSTEEL_ICE_2 = deferredRegister.register("dragonarmor_dragonsteel_ice_" + ItemDragonArmor.getNameForSlot(2), () -> new ItemDragonArmor(5, 2));
    public static final RegistryObject<ItemDragonArmor> DRAGONARMOR_DRAGONSTEEL_ICE_3 = deferredRegister.register("dragonarmor_dragonsteel_ice_" + ItemDragonArmor.getNameForSlot(3), () -> new ItemDragonArmor(5, 3));
    public static final RegistryObject<ItemDragonArmor> DRAGONARMOR_DRAGONSTEEL_LIGHTNING_0 = deferredRegister.register("dragonarmor_dragonsteel_lightning_" + ItemDragonArmor.getNameForSlot(0), () -> new ItemDragonArmor(7, 0));
    public static final RegistryObject<ItemDragonArmor> DRAGONARMOR_DRAGONSTEEL_LIGHTNING_1 = deferredRegister.register("dragonarmor_dragonsteel_lightning_" + ItemDragonArmor.getNameForSlot(1), () -> new ItemDragonArmor(7, 1));
    public static final RegistryObject<ItemDragonArmor> DRAGONARMOR_DRAGONSTEEL_LIGHTNING_2 = deferredRegister.register("dragonarmor_dragonsteel_lightning_" + ItemDragonArmor.getNameForSlot(2), () -> new ItemDragonArmor(7, 2));
    public static final RegistryObject<ItemDragonArmor> DRAGONARMOR_DRAGONSTEEL_LIGHTNING_3 = deferredRegister.register("dragonarmor_dragonsteel_lightning_" + ItemDragonArmor.getNameForSlot(3), () -> new ItemDragonArmor(7, 3));
    public static final RegistryObject<Item> DRAGON_MEAL = deferredRegister.register("dragon_meal", ItemGeneric::new);
    public static final RegistryObject<Item> SICKLY_DRAGON_MEAL = deferredRegister.register("sickly_dragon_meal", () -> new ItemGeneric(1));
    public static final RegistryObject<Item> CREATIVE_DRAGON_MEAL = deferredRegister.register("creative_dragon_meal", () -> new ItemGeneric(2));
    public static final RegistryObject<Item> FIRE_DRAGON_FLESH = deferredRegister.register(ItemDragonFlesh.getNameForType(0), () -> new ItemDragonFlesh(0));
    public static final RegistryObject<Item> ICE_DRAGON_FLESH = deferredRegister.register(ItemDragonFlesh.getNameForType(1), () -> new ItemDragonFlesh(1));
    public static final RegistryObject<Item> LIGHTNING_DRAGON_FLESH = deferredRegister.register(ItemDragonFlesh.getNameForType(2), () -> new ItemDragonFlesh(2));
    public static final RegistryObject<Item> FIRE_DRAGON_HEART = deferredRegister.register("fire_dragon_heart", ItemGeneric::new);
    public static final RegistryObject<Item> ICE_DRAGON_HEART = deferredRegister.register("ice_dragon_heart", ItemGeneric::new);
    public static final RegistryObject<Item> LIGHTNING_DRAGON_HEART = deferredRegister.register("lightning_dragon_heart", ItemGeneric::new);
    public static final RegistryObject<Item> FIRE_DRAGON_BLOOD = deferredRegister.register("fire_dragon_blood", ItemGeneric::new);
    public static final RegistryObject<Item> ICE_DRAGON_BLOOD = deferredRegister.register("ice_dragon_blood", ItemGeneric::new);
    public static final RegistryObject<Item> LIGHTNING_DRAGON_BLOOD = deferredRegister.register("lightning_dragon_blood", ItemGeneric::new);
    public static final RegistryObject<Item> DRAGON_STAFF = deferredRegister.register("dragon_stick", () -> new ItemDragonStaff());
    public static final RegistryObject<Item> DRAGON_HORN = deferredRegister.register("dragon_horn", () -> new ItemDragonHorn());
    public static final RegistryObject<Item> DRAGON_FLUTE = deferredRegister.register("dragon_flute", () -> new ItemDragonFlute());
    public static final RegistryObject<Item> SUMMONING_CRYSTAL_FIRE = deferredRegister.register("summoning_crystal_fire", () -> new ItemSummoningCrystal());
    public static final RegistryObject<Item> SUMMONING_CRYSTAL_ICE = deferredRegister.register("summoning_crystal_ice", () -> new ItemSummoningCrystal());
    public static final RegistryObject<Item> SUMMONING_CRYSTAL_LIGHTNING = deferredRegister.register("summoning_crystal_lightning", () -> new ItemSummoningCrystal());
    public static final RegistryObject<Item> HIPPOGRYPH_EGG = deferredRegister.register("hippogryph_egg", () -> new ItemHippogryphEgg());
    public static final RegistryObject<Item> IRON_HIPPOGRYPH_ARMOR = deferredRegister.register("iron_hippogryph_armor", () -> new ItemGeneric(0, 1));
    public static final RegistryObject<Item> GOLD_HIPPOGRYPH_ARMOR = deferredRegister.register("gold_hippogryph_armor", () -> new ItemGeneric(0, 1));
    public static final RegistryObject<Item> DIAMOND_HIPPOGRYPH_ARMOR = deferredRegister.register("diamond_hippogryph_armor", () -> new ItemGeneric(0, 1));
    public static final RegistryObject<Item> HIPPOGRYPH_TALON = deferredRegister.register("hippogryph_talon", () -> new ItemGeneric(1));
    public static final RegistryObject<Item> HIPPOGRYPH_SWORD = deferredRegister.register("hippogryph_sword", () -> new ItemHippogryphSword());
    public static final RegistryObject<Item> GORGON_HEAD = deferredRegister.register("gorgon_head", () -> new ItemGorgonHead());
    public static final RegistryObject<Item> STONE_STATUE = deferredRegister.register("stone_statue", () -> new ItemStoneStatue());
    public static final RegistryObject<Item> BLINDFOLD = deferredRegister.register("blindfold", () -> new ItemBlindfold());
    public static final RegistryObject<Item> PIXIE_DUST = deferredRegister.register("pixie_dust", () -> new ItemPixieDust());
    public static final RegistryObject<Item> PIXIE_WINGS = deferredRegister.register("pixie_wings", () -> new ItemGeneric(1));
    public static final RegistryObject<Item> PIXIE_WAND = deferredRegister.register("pixie_wand", () -> new ItemPixieWand());
    public static final RegistryObject<Item> AMBROSIA = deferredRegister.register("ambrosia", () -> new ItemAmbrosia());
    public static final RegistryObject<Item> CYCLOPS_EYE = deferredRegister.register("cyclops_eye", () -> new ItemCyclopsEye());
    public static final RegistryObject<Item> SHEEP_HELMET = deferredRegister.register("sheep_helmet", () -> new ItemModArmor(SHEEP_ARMOR_MATERIAL, EquipmentSlot.HEAD));
    public static final RegistryObject<Item> SHEEP_CHESTPLATE = deferredRegister.register("sheep_chestplate", () -> new ItemModArmor(SHEEP_ARMOR_MATERIAL, EquipmentSlot.CHEST));
    public static final RegistryObject<Item> SHEEP_LEGGINGS = deferredRegister.register("sheep_leggings", () -> new ItemModArmor(SHEEP_ARMOR_MATERIAL, EquipmentSlot.LEGS));
    public static final RegistryObject<Item> SHEEP_BOOTS = deferredRegister.register("sheep_boots", () -> new ItemModArmor(SHEEP_ARMOR_MATERIAL, EquipmentSlot.FEET));
    public static final RegistryObject<Item> SHINY_SCALES = deferredRegister.register("shiny_scales", ItemGeneric::new);
    public static final RegistryObject<Item> SIREN_TEAR = deferredRegister.register("siren_tear", () -> new ItemGeneric(1));
    public static final RegistryObject<Item> SIREN_FLUTE = deferredRegister.register("siren_flute", () -> new ItemSirenFlute());
    public static final RegistryObject<Item> HIPPOCAMPUS_FIN = deferredRegister.register("hippocampus_fin", () -> new ItemGeneric(1));
    public static final RegistryObject<Item> HIPPOCAMPUS_SLAPPER = deferredRegister.register("hippocampus_slapper", () -> new ItemHippocampusSlapper());
    public static final RegistryObject<Item> EARPLUGS = deferredRegister.register("earplugs", () -> new ItemModArmor(EARPLUGS_ARMOR_MATERIAL, EquipmentSlot.HEAD));
    public static final RegistryObject<Item> DEATH_WORM_CHITIN_YELLOW = deferredRegister.register("deathworm_chitin_yellow", ItemGeneric::new);
    public static final RegistryObject<Item> DEATH_WORM_CHITIN_WHITE = deferredRegister.register("deathworm_chitin_white", ItemGeneric::new);
    public static final RegistryObject<Item> DEATH_WORM_CHITIN_RED = deferredRegister.register("deathworm_chitin_red", ItemGeneric::new);
    public static final RegistryObject<Item> DEATHWORM_YELLOW_HELMET = deferredRegister.register("deathworm_yellow_helmet", () -> new ItemDeathwormArmor(DEATHWORM_0_ARMOR_MATERIAL, EquipmentSlot.HEAD));
    public static final RegistryObject<Item> DEATHWORM_YELLOW_CHESTPLATE = deferredRegister.register("deathworm_yellow_chestplate", () -> new ItemDeathwormArmor(DEATHWORM_0_ARMOR_MATERIAL, EquipmentSlot.CHEST));
    public static final RegistryObject<Item> DEATHWORM_YELLOW_LEGGINGS = deferredRegister.register("deathworm_yellow_leggings", () -> new ItemDeathwormArmor(DEATHWORM_0_ARMOR_MATERIAL, EquipmentSlot.LEGS));
    public static final RegistryObject<Item> DEATHWORM_YELLOW_BOOTS = deferredRegister.register("deathworm_yellow_boots", () -> new ItemDeathwormArmor(DEATHWORM_0_ARMOR_MATERIAL, EquipmentSlot.FEET));
    public static final RegistryObject<Item> DEATHWORM_WHITE_HELMET = deferredRegister.register("deathworm_white_helmet", () -> new ItemDeathwormArmor(DEATHWORM_1_ARMOR_MATERIAL, EquipmentSlot.HEAD));
    public static final RegistryObject<Item> DEATHWORM_WHITE_CHESTPLATE = deferredRegister.register("deathworm_white_chestplate", () -> new ItemDeathwormArmor(DEATHWORM_1_ARMOR_MATERIAL, EquipmentSlot.CHEST));
    public static final RegistryObject<Item> DEATHWORM_WHITE_LEGGINGS = deferredRegister.register("deathworm_white_leggings", () -> new ItemDeathwormArmor(DEATHWORM_1_ARMOR_MATERIAL, EquipmentSlot.LEGS));
    public static final RegistryObject<Item> DEATHWORM_WHITE_BOOTS = deferredRegister.register("deathworm_white_boots", () -> new ItemDeathwormArmor(DEATHWORM_1_ARMOR_MATERIAL, EquipmentSlot.FEET));
    public static final RegistryObject<Item> DEATHWORM_RED_HELMET = deferredRegister.register("deathworm_red_helmet", () -> new ItemDeathwormArmor(DEATHWORM_2_ARMOR_MATERIAL, EquipmentSlot.HEAD));
    public static final RegistryObject<Item> DEATHWORM_RED_CHESTPLATE = deferredRegister.register("deathworm_red_chestplate", () -> new ItemDeathwormArmor(DEATHWORM_2_ARMOR_MATERIAL, EquipmentSlot.CHEST));
    public static final RegistryObject<Item> DEATHWORM_RED_LEGGINGS = deferredRegister.register("deathworm_red_leggings", () -> new ItemDeathwormArmor(DEATHWORM_2_ARMOR_MATERIAL, EquipmentSlot.LEGS));
    public static final RegistryObject<Item> DEATHWORM_RED_BOOTS = deferredRegister.register("deathworm_red_boots", () -> new ItemDeathwormArmor(DEATHWORM_2_ARMOR_MATERIAL, EquipmentSlot.FEET));
    public static final RegistryObject<Item> DEATHWORM_EGG = deferredRegister.register("deathworm_egg", () -> new ItemDeathwormEgg(false));
    public static final RegistryObject<Item> DEATHWORM_EGG_GIGANTIC = deferredRegister.register("deathworm_egg_giant", () -> new ItemDeathwormEgg(true));
    public static final RegistryObject<Item> DEATHWORM_TOUNGE = deferredRegister.register("deathworm_tounge", () -> new ItemGeneric(1));
    public static final RegistryObject<Item> DEATHWORM_GAUNTLET_YELLOW = deferredRegister.register("deathworm_gauntlet_yellow", () -> new ItemDeathwormGauntlet());
    public static final RegistryObject<Item> DEATHWORM_GAUNTLET_WHITE = deferredRegister.register("deathworm_gauntlet_white", () -> new ItemDeathwormGauntlet());
    public static final RegistryObject<Item> DEATHWORM_GAUNTLET_RED = deferredRegister.register("deathworm_gauntlet_red", () -> new ItemDeathwormGauntlet());
    public static final RegistryObject<Item> ROTTEN_EGG = deferredRegister.register("rotten_egg", () -> new ItemRottenEgg());
    public static final RegistryObject<Item> COCKATRICE_EYE = deferredRegister.register("cockatrice_eye", () -> new ItemGeneric(1));
    public static final RegistryObject<Item> ITEM_COCKATRICE_SCEPTER = deferredRegister.register("cockatrice_scepter", () -> new ItemCockatriceScepter());
    public static final RegistryObject<Item> STYMPHALIAN_BIRD_FEATHER = deferredRegister.register("stymphalian_bird_feather", ItemGeneric::new);
    public static final RegistryObject<Item> STYMPHALIAN_ARROW = deferredRegister.register("stymphalian_arrow", () -> new ItemStymphalianArrow());
    public static final RegistryObject<Item> STYMPHALIAN_FEATHER_BUNDLE = deferredRegister.register("stymphalian_feather_bundle", () -> new ItemStymphalianFeatherBundle());
    public static final RegistryObject<Item> STYMPHALIAN_DAGGER = deferredRegister.register("stymphalian_bird_dagger", () -> new ItemStymphalianDagger());
    public static final RegistryObject<Item> TROLL_TUSK = deferredRegister.register("troll_tusk", ItemGeneric::new);
    public static final RegistryObject<Item> MYRMEX_DESERT_EGG = deferredRegister.register("myrmex_desert_egg", () -> new ItemMyrmexEgg(false));
    public static final RegistryObject<Item> MYRMEX_JUNGLE_EGG = deferredRegister.register("myrmex_jungle_egg", () -> new ItemMyrmexEgg(true));
    public static final RegistryObject<Item> MYRMEX_DESERT_RESIN = deferredRegister.register("myrmex_desert_resin", ItemGeneric::new);
    public static final RegistryObject<Item> MYRMEX_JUNGLE_RESIN = deferredRegister.register("myrmex_jungle_resin", ItemGeneric::new);
    public static final RegistryObject<Item> MYRMEX_DESERT_CHITIN = deferredRegister.register("myrmex_desert_chitin", ItemGeneric::new);
    public static final RegistryObject<Item> MYRMEX_JUNGLE_CHITIN = deferredRegister.register("myrmex_jungle_chitin", ItemGeneric::new);
    public static final RegistryObject<Item> MYRMEX_STINGER = deferredRegister.register("myrmex_stinger", ItemGeneric::new);
    public static final RegistryObject<Item> MYRMEX_DESERT_SWORD = deferredRegister.register("myrmex_desert_sword", () -> new ItemModSword(MYRMEX_CHITIN_TOOL_MATERIAL));
    public static final RegistryObject<Item> MYRMEX_DESERT_SWORD_VENOM = deferredRegister.register("myrmex_desert_sword_venom", () -> new ItemModSword(MYRMEX_CHITIN_TOOL_MATERIAL));
    public static final RegistryObject<Item> MYRMEX_DESERT_SHOVEL = deferredRegister.register("myrmex_desert_shovel", () -> new ItemModShovel(MYRMEX_CHITIN_TOOL_MATERIAL));
    public static final RegistryObject<Item> MYRMEX_DESERT_PICKAXE = deferredRegister.register("myrmex_desert_pickaxe", () -> new ItemModPickaxe(MYRMEX_CHITIN_TOOL_MATERIAL));
    public static final RegistryObject<Item> MYRMEX_DESERT_AXE = deferredRegister.register("myrmex_desert_axe", () -> new ItemModAxe(MYRMEX_CHITIN_TOOL_MATERIAL));
    public static final RegistryObject<Item> MYRMEX_DESERT_HOE = deferredRegister.register("myrmex_desert_hoe", () -> new ItemModHoe(MYRMEX_CHITIN_TOOL_MATERIAL));
    public static final RegistryObject<Item> MYRMEX_JUNGLE_SWORD = deferredRegister.register("myrmex_jungle_sword", () -> new ItemModSword(MYRMEX_CHITIN_TOOL_MATERIAL));
    public static final RegistryObject<Item> MYRMEX_JUNGLE_SWORD_VENOM = deferredRegister.register("myrmex_jungle_sword_venom", () -> new ItemModSword(MYRMEX_CHITIN_TOOL_MATERIAL));
    public static final RegistryObject<Item> MYRMEX_JUNGLE_SHOVEL = deferredRegister.register("myrmex_jungle_shovel", () -> new ItemModShovel(MYRMEX_CHITIN_TOOL_MATERIAL));
    public static final RegistryObject<Item> MYRMEX_JUNGLE_PICKAXE = deferredRegister.register("myrmex_jungle_pickaxe", () -> new ItemModPickaxe(MYRMEX_CHITIN_TOOL_MATERIAL));
    public static final RegistryObject<Item> MYRMEX_JUNGLE_AXE = deferredRegister.register("myrmex_jungle_axe", () -> new ItemModAxe(MYRMEX_CHITIN_TOOL_MATERIAL));
    public static final RegistryObject<Item> MYRMEX_JUNGLE_HOE = deferredRegister.register("myrmex_jungle_hoe", () -> new ItemModHoe(MYRMEX_CHITIN_TOOL_MATERIAL));
    public static final RegistryObject<Item> MYRMEX_DESERT_STAFF = deferredRegister.register("myrmex_desert_staff", () -> new ItemMyrmexStaff(false));
    public static final RegistryObject<Item> MYRMEX_JUNGLE_STAFF = deferredRegister.register("myrmex_jungle_staff", () -> new ItemMyrmexStaff(true));
    public static final RegistryObject<Item> MYRMEX_DESERT_HELMET = deferredRegister.register("myrmex_desert_helmet", () -> new ItemModArmor(MYRMEX_DESERT_ARMOR_MATERIAL, EquipmentSlot.HEAD));
    public static final RegistryObject<Item> MYRMEX_DESERT_CHESTPLATE = deferredRegister.register("myrmex_desert_chestplate", () -> new ItemModArmor(MYRMEX_DESERT_ARMOR_MATERIAL, EquipmentSlot.CHEST));
    public static final RegistryObject<Item> MYRMEX_DESERT_LEGGINGS = deferredRegister.register("myrmex_desert_leggings", () -> new ItemModArmor(MYRMEX_DESERT_ARMOR_MATERIAL, EquipmentSlot.LEGS));
    public static final RegistryObject<Item> MYRMEX_DESERT_BOOTS = deferredRegister.register("myrmex_desert_boots", () -> new ItemModArmor(MYRMEX_DESERT_ARMOR_MATERIAL, EquipmentSlot.FEET));
    public static final RegistryObject<Item> MYRMEX_JUNGLE_HELMET = deferredRegister.register("myrmex_jungle_helmet", () -> new ItemModArmor(MYRMEX_JUNGLE_ARMOR_MATERIAL, EquipmentSlot.HEAD));
    public static final RegistryObject<Item> MYRMEX_JUNGLE_CHESTPLATE = deferredRegister.register("myrmex_jungle_chestplate", () -> new ItemModArmor(MYRMEX_JUNGLE_ARMOR_MATERIAL, EquipmentSlot.CHEST));
    public static final RegistryObject<Item> MYRMEX_JUNGLE_LEGGINGS = deferredRegister.register("myrmex_jungle_leggings", () -> new ItemModArmor(MYRMEX_JUNGLE_ARMOR_MATERIAL, EquipmentSlot.LEGS));
    public static final RegistryObject<Item> MYRMEX_JUNGLE_BOOTS = deferredRegister.register("myrmex_jungle_boots", () -> new ItemModArmor(MYRMEX_JUNGLE_ARMOR_MATERIAL, EquipmentSlot.FEET));
    public static final RegistryObject<Item> MYRMEX_DESERT_SWARM = deferredRegister.register("myrmex_desert_swarm", () -> new ItemMyrmexSwarm(false));
    public static final RegistryObject<Item> MYRMEX_JUNGLE_SWARM = deferredRegister.register("myrmex_jungle_swarm", () -> new ItemMyrmexSwarm(true));
    public static final RegistryObject<Item> AMPHITHERE_FEATHER = deferredRegister.register("amphithere_feather", ItemGeneric::new);
    public static final RegistryObject<Item> AMPHITHERE_ARROW = deferredRegister.register("amphithere_arrow", () -> new ItemAmphithereArrow());
    public static final RegistryObject<Item> AMPHITHERE_MACUAHUITL = deferredRegister.register("amphithere_macuahuitl", () -> new ItemAmphithereMacuahuitl());
    public static final RegistryObject<Item> SERPENT_FANG = deferredRegister.register("sea_serpent_fang", ItemGeneric::new);
    public static final RegistryObject<Item> SEA_SERPENT_ARROW = deferredRegister.register("sea_serpent_arrow", () -> new ItemSeaSerpentArrow());
    public static final RegistryObject<Item> TIDE_TRIDENT_INVENTORY = deferredRegister.register("tide_trident_inventory", () -> new ItemGeneric(0, true));
    public static final RegistryObject<Item> TIDE_TRIDENT = deferredRegister.register("tide_trident", () -> new ItemTideTrident());
    public static final RegistryObject<Item> CHAIN = deferredRegister.register("chain", () -> new ItemChain(false));
    public static final RegistryObject<Item> CHAIN_STICKY = deferredRegister.register("chain_sticky", () -> new ItemChain(true));
    public static final RegistryObject<Item> DRAGONSTEEL_FIRE_INGOT = deferredRegister.register("dragonsteel_fire_ingot", ItemGeneric::new);
    public static final RegistryObject<Item> DRAGONSTEEL_FIRE_SWORD = deferredRegister.register("dragonsteel_fire_sword", () -> new ItemModSword(DRAGONSTEEL_TIER_FIRE));
    public static final RegistryObject<Item> DRAGONSTEEL_FIRE_PICKAXE = deferredRegister.register("dragonsteel_fire_pickaxe", () -> new ItemModPickaxe(DRAGONSTEEL_TIER_FIRE));
    public static final RegistryObject<Item> DRAGONSTEEL_FIRE_AXE = deferredRegister.register("dragonsteel_fire_axe", () -> new ItemModAxe(DRAGONSTEEL_TIER_FIRE));
    public static final RegistryObject<Item> DRAGONSTEEL_FIRE_SHOVEL = deferredRegister.register("dragonsteel_fire_shovel", () -> new ItemModShovel(DRAGONSTEEL_TIER_FIRE));
    public static final RegistryObject<Item> DRAGONSTEEL_FIRE_HOE = deferredRegister.register("dragonsteel_fire_hoe", () -> new ItemModHoe(DRAGONSTEEL_TIER_FIRE));
    public static final RegistryObject<Item> DRAGONSTEEL_FIRE_HELMET = deferredRegister.register("dragonsteel_fire_helmet", () -> new ItemDragonsteelArmor(DRAGONSTEEL_FIRE_ARMOR_MATERIAL, 0, EquipmentSlot.HEAD));
    public static final RegistryObject<Item> DRAGONSTEEL_FIRE_CHESTPLATE = deferredRegister.register("dragonsteel_fire_chestplate", () -> new ItemDragonsteelArmor(DRAGONSTEEL_FIRE_ARMOR_MATERIAL, 1, EquipmentSlot.CHEST));
    public static final RegistryObject<Item> DRAGONSTEEL_FIRE_LEGGINGS = deferredRegister.register("dragonsteel_fire_leggings", () -> new ItemDragonsteelArmor(DRAGONSTEEL_FIRE_ARMOR_MATERIAL, 2, EquipmentSlot.LEGS));
    public static final RegistryObject<Item> DRAGONSTEEL_FIRE_BOOTS = deferredRegister.register("dragonsteel_fire_boots", () -> new ItemDragonsteelArmor(DRAGONSTEEL_FIRE_ARMOR_MATERIAL, 3, EquipmentSlot.FEET));
    public static final RegistryObject<Item> DRAGONSTEEL_ICE_INGOT = deferredRegister.register("dragonsteel_ice_ingot", ItemGeneric::new);
    public static final RegistryObject<Item> DRAGONSTEEL_ICE_SWORD = deferredRegister.register("dragonsteel_ice_sword", () -> new ItemModSword(DRAGONSTEEL_TIER_ICE));
    public static final RegistryObject<Item> DRAGONSTEEL_ICE_PICKAXE = deferredRegister.register("dragonsteel_ice_pickaxe", () -> new ItemModPickaxe(DRAGONSTEEL_TIER_ICE));
    public static final RegistryObject<Item> DRAGONSTEEL_ICE_AXE = deferredRegister.register("dragonsteel_ice_axe", () -> new ItemModAxe(DRAGONSTEEL_TIER_ICE));
    public static final RegistryObject<Item> DRAGONSTEEL_ICE_SHOVEL = deferredRegister.register("dragonsteel_ice_shovel", () -> new ItemModShovel(DRAGONSTEEL_TIER_ICE));
    public static final RegistryObject<Item> DRAGONSTEEL_ICE_HOE = deferredRegister.register("dragonsteel_ice_hoe", () -> new ItemModHoe(DRAGONSTEEL_TIER_ICE));
    public static final RegistryObject<Item> DRAGONSTEEL_ICE_HELMET = deferredRegister.register("dragonsteel_ice_helmet", () -> new ItemDragonsteelArmor(DRAGONSTEEL_ICE_ARMOR_MATERIAL, 0, EquipmentSlot.HEAD));
    public static final RegistryObject<Item> DRAGONSTEEL_ICE_CHESTPLATE = deferredRegister.register("dragonsteel_ice_chestplate", () -> new ItemDragonsteelArmor(DRAGONSTEEL_ICE_ARMOR_MATERIAL, 1, EquipmentSlot.CHEST));
    public static final RegistryObject<Item> DRAGONSTEEL_ICE_LEGGINGS = deferredRegister.register("dragonsteel_ice_leggings", () -> new ItemDragonsteelArmor(DRAGONSTEEL_ICE_ARMOR_MATERIAL, 2, EquipmentSlot.LEGS));
    public static final RegistryObject<Item> DRAGONSTEEL_ICE_BOOTS = deferredRegister.register("dragonsteel_ice_boots", () -> new ItemDragonsteelArmor(DRAGONSTEEL_ICE_ARMOR_MATERIAL, 3, EquipmentSlot.FEET));

    public static final RegistryObject<Item> DRAGONSTEEL_LIGHTNING_INGOT = deferredRegister.register("dragonsteel_lightning_ingot", ItemGeneric::new);
    public static final RegistryObject<Item> DRAGONSTEEL_LIGHTNING_SWORD = deferredRegister.register("dragonsteel_lightning_sword", () -> new ItemModSword(DRAGONSTEEL_TIER_LIGHTNING));
    public static final RegistryObject<Item> DRAGONSTEEL_LIGHTNING_PICKAXE = deferredRegister.register("dragonsteel_lightning_pickaxe", () -> new ItemModPickaxe(DRAGONSTEEL_TIER_LIGHTNING));
    public static final RegistryObject<Item> DRAGONSTEEL_LIGHTNING_AXE = deferredRegister.register("dragonsteel_lightning_axe", () -> new ItemModAxe(DRAGONSTEEL_TIER_LIGHTNING));
    public static final RegistryObject<Item> DRAGONSTEEL_LIGHTNING_SHOVEL = deferredRegister.register("dragonsteel_lightning_shovel", () -> new ItemModShovel(DRAGONSTEEL_TIER_LIGHTNING));
    public static final RegistryObject<Item> DRAGONSTEEL_LIGHTNING_HOE = deferredRegister.register("dragonsteel_lightning_hoe", () -> new ItemModHoe(DRAGONSTEEL_TIER_LIGHTNING));
    public static final RegistryObject<Item> DRAGONSTEEL_LIGHTNING_HELMET = deferredRegister.register("dragonsteel_lightning_helmet", () -> new ItemDragonsteelArmor(DRAGONSTEEL_LIGHTNING_ARMOR_MATERIAL, 0, EquipmentSlot.HEAD));
    public static final RegistryObject<Item> DRAGONSTEEL_LIGHTNING_CHESTPLATE = deferredRegister.register("dragonsteel_lightning_chestplate", () -> new ItemDragonsteelArmor(DRAGONSTEEL_LIGHTNING_ARMOR_MATERIAL, 1, EquipmentSlot.CHEST));
    public static final RegistryObject<Item> DRAGONSTEEL_LIGHTNING_LEGGINGS = deferredRegister.register("dragonsteel_lightning_leggings", () -> new ItemDragonsteelArmor(DRAGONSTEEL_LIGHTNING_ARMOR_MATERIAL, 2, EquipmentSlot.LEGS));
    public static final RegistryObject<Item> DRAGONSTEEL_LIGHTNING_BOOTS = deferredRegister.register("dragonsteel_lightning_boots", () -> new ItemDragonsteelArmor(DRAGONSTEEL_LIGHTNING_ARMOR_MATERIAL, 3, EquipmentSlot.FEET));


    public static final RegistryObject<Item> WEEZER_BLUE_ALBUM = deferredRegister.register("weezer_blue_album", () -> new ItemGeneric(1, true));
    public static final RegistryObject<Item> DRAGON_DEBUG_STICK = deferredRegister.register("dragon_debug_stick", () -> new ItemGeneric(1, true));
    public static final RegistryObject<Item> DREAD_SWORD = deferredRegister.register("dread_sword", () -> new ItemModSword(DREAD_SWORD_TOOL_MATERIAL));
    public static final RegistryObject<Item> DREAD_KNIGHT_SWORD = deferredRegister.register("dread_knight_sword", () -> new ItemModSword(DREAD_KNIGHT_TOOL_MATERIAL));
    public static final RegistryObject<Item> LICH_STAFF = deferredRegister.register("lich_staff", () -> new ItemLichStaff());
    public static final RegistryObject<Item> DREAD_QUEEN_SWORD = deferredRegister.register("dread_queen_sword", () -> new ItemModSword(DRAGONSTEEL_TIER_DREAD_QUEEN));
    public static final RegistryObject<Item> DREAD_QUEEN_STAFF = deferredRegister.register("dread_queen_staff", () -> new ItemDreadQueenStaff());
    public static final RegistryObject<Item> DREAD_SHARD = deferredRegister.register("dread_shard", () -> new ItemGeneric(0));
    public static final RegistryObject<Item> DREAD_KEY = deferredRegister.register("dread_key", () -> new ItemGeneric(0));
    public static final RegistryObject<Item> HYDRA_FANG = deferredRegister.register("hydra_fang", () -> new ItemGeneric(0));
    public static final RegistryObject<Item> HYDRA_HEART = deferredRegister.register("hydra_heart", () -> new ItemHydraHeart());
    public static final RegistryObject<Item> HYDRA_ARROW = deferredRegister.register("hydra_arrow", () -> new ItemHydraArrow());
    public static final RegistryObject<Item> CANNOLI = deferredRegister.register("cannoli", () -> new ItemCannoli());
    public static final RegistryObject<Item> ECTOPLASM = deferredRegister.register("ectoplasm", ItemGeneric::new);
    public static final RegistryObject<Item> GHOST_INGOT = deferredRegister.register("ghost_ingot", () -> new ItemGeneric(1));
    public static final RegistryObject<Item> GHOST_SWORD = deferredRegister.register("ghost_sword", () -> new ItemGhostSword());

    static {
        EnumDragonArmor.initArmors();
        EnumSeaSerpent.initArmors();
        EnumSkullType.initItems();
    }

    public static void addToBus(IEventBus modBus) {
        modBus.addGenericListener(Item.class, IafItemRegistry::registerItems);
        modBus.addGenericListener(Item.class, IafBlockRegistry::registerBlockItems);
    }

    public static void registerItems(RegistryEvent.Register<Item> event) {
        // Items
        try {
            for (Field f : IafItemRegistry.class.getFields()) {
                Object obj = f.get(null);
                if (obj instanceof Item) {
                    if (((Item) obj).getRegistryName() != null) {
                        event.getRegistry().register((Item) obj);
                    }
                } else if (obj instanceof Item[]) {
                    for (Item item : (Item[]) obj) {
                        if (item.getRegistryName() != null) {
                            event.getRegistry().register(item);
                        }
                    }
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        for (EnumDragonArmor armor : EnumDragonArmor.values()) {
            event.getRegistry().register(armor.helmet.get());
            event.getRegistry().register(armor.chestplate.get());
            event.getRegistry().register(armor.leggings.get());
            event.getRegistry().register(armor.boots.get());
        }
        for (EnumSeaSerpent armor : EnumSeaSerpent.values()) {
            event.getRegistry().register(armor.scale.get());
            event.getRegistry().register(armor.helmet.get());
            event.getRegistry().register(armor.chestplate.get());
            event.getRegistry().register(armor.leggings.get());
            event.getRegistry().register(armor.boots.get());
        }
        for (EnumTroll.Weapon weapon : EnumTroll.Weapon.values()) {
        }
        for (EnumTroll troll : EnumTroll.values()) {
        }
        for (EnumSkullType skull : EnumSkullType.values()) {
            event.getRegistry().register(skull.skull_item.get());
        }
        IafRecipeRegistry.preInit();
        //Banner Patterns
        try {
            for (Field f : IafRecipeRegistry.class.getFields()) {
                Object obj = f.get(null);
                if (obj instanceof BannerPattern) {
                    BannerPattern pattern = (BannerPattern) obj;
                    String name = f.getName().replace("PATTERN_", "").toLowerCase(Locale.ROOT);
                    event.getRegistry().register(new BannerPatternItem(pattern, (new Item.Properties()).stacksTo(1).tab(IceAndFire.TAB_ITEMS)).setRegistryName("iceandfire:banner_pattern_" + name));

                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        //spawn Eggs
        //@formatter:off
        event.getRegistry().register(new ForgeSpawnEggItem(IafEntityRegistry.FIRE_DRAGON, 0X340000, 0XA52929, new Item.Properties().tab(IceAndFire.TAB_ITEMS)).setRegistryName("iceandfire:spawn_egg_fire_dragon"));
        event.getRegistry().register(new ForgeSpawnEggItem(IafEntityRegistry.ICE_DRAGON, 0XB5DDFB, 0X7EBAF0, new Item.Properties().tab(IceAndFire.TAB_ITEMS)).setRegistryName("iceandfire:spawn_egg_ice_dragon"));
        event.getRegistry().register(new ForgeSpawnEggItem(IafEntityRegistry.LIGHTNING_DRAGON, 0X422367, 0X725691, new Item.Properties().tab(IceAndFire.TAB_ITEMS)).setRegistryName("iceandfire:spawn_egg_lightning_dragon"));
        event.getRegistry().register(new ForgeSpawnEggItem(IafEntityRegistry.HIPPOGRYPH, 0XD8D8D8, 0XD1B55D, new Item.Properties().tab(IceAndFire.TAB_ITEMS)).setRegistryName("iceandfire:spawn_egg_hippogryph"));
        event.getRegistry().register(new ForgeSpawnEggItem(IafEntityRegistry.GORGON, 0XD0D99F, 0X684530, new Item.Properties().tab(IceAndFire.TAB_ITEMS)).setRegistryName("iceandfire:spawn_egg_gorgon"));
        event.getRegistry().register(new ForgeSpawnEggItem(IafEntityRegistry.PIXIE, 0XFF7F89, 0XE2CCE2, new Item.Properties().tab(IceAndFire.TAB_ITEMS)).setRegistryName("iceandfire:spawn_egg_pixie"));
        event.getRegistry().register(new ForgeSpawnEggItem(IafEntityRegistry.CYCLOPS, 0XB0826E, 0X3A1F0F, new Item.Properties().tab(IceAndFire.TAB_ITEMS)).setRegistryName("iceandfire:spawn_egg_cyclops"));
        event.getRegistry().register(new ForgeSpawnEggItem(IafEntityRegistry.SIREN, 0X8EE6CA, 0XF2DFC8, new Item.Properties().tab(IceAndFire.TAB_ITEMS)).setRegistryName("iceandfire:spawn_egg_siren"));
        event.getRegistry().register(new ForgeSpawnEggItem(IafEntityRegistry.HIPPOCAMPUS, 0X4491C7, 0X4FC56B, new Item.Properties().tab(IceAndFire.TAB_ITEMS)).setRegistryName("iceandfire:spawn_egg_hippocampus"));
        event.getRegistry().register(new ForgeSpawnEggItem(IafEntityRegistry.DEATH_WORM, 0XD1CDA3, 0X423A3A, new Item.Properties().tab(IceAndFire.TAB_ITEMS)).setRegistryName("iceandfire:spawn_egg_death_worm"));
        event.getRegistry().register(new ForgeSpawnEggItem(IafEntityRegistry.COCKATRICE, 0X8F5005, 0X4F5A23, new Item.Properties().tab(IceAndFire.TAB_ITEMS)).setRegistryName("iceandfire:spawn_egg_cockatrice"));
        event.getRegistry().register(new ForgeSpawnEggItem(IafEntityRegistry.STYMPHALIAN_BIRD, 0X744F37, 0X9E6C4B, new Item.Properties().tab(IceAndFire.TAB_ITEMS)).setRegistryName("iceandfire:spawn_egg_stymphalian_bird"));
        event.getRegistry().register(new ForgeSpawnEggItem(IafEntityRegistry.TROLL, 0X3D413D, 0X58433A, new Item.Properties().tab(IceAndFire.TAB_ITEMS)).setRegistryName("iceandfire:spawn_egg_troll"));
        event.getRegistry().register(new ForgeSpawnEggItem(IafEntityRegistry.MYRMEX_WORKER, 0XA16026, 0X594520, new Item.Properties().tab(IceAndFire.TAB_ITEMS)).setRegistryName("iceandfire:spawn_egg_myrmex_worker"));
        event.getRegistry().register(new ForgeSpawnEggItem(IafEntityRegistry.MYRMEX_SOLDIER, 0XA16026, 0X7D622D, new Item.Properties().tab(IceAndFire.TAB_ITEMS)).setRegistryName("iceandfire:spawn_egg_myrmex_soldier"));
        event.getRegistry().register(new ForgeSpawnEggItem(IafEntityRegistry.MYRMEX_SENTINEL, 0XA16026, 0XA27F3A, new Item.Properties().tab(IceAndFire.TAB_ITEMS)).setRegistryName("iceandfire:spawn_egg_myrmex_sentinel"));
        event.getRegistry().register(new ForgeSpawnEggItem(IafEntityRegistry.MYRMEX_ROYAL, 0XA16026, 0XC79B48, new Item.Properties().tab(IceAndFire.TAB_ITEMS)).setRegistryName("iceandfire:spawn_egg_myrmex_royal"));
        event.getRegistry().register(new ForgeSpawnEggItem(IafEntityRegistry.MYRMEX_QUEEN, 0XA16026, 0XECB855, new Item.Properties().tab(IceAndFire.TAB_ITEMS)).setRegistryName("iceandfire:spawn_egg_myrmex_queen"));
        event.getRegistry().register(new ForgeSpawnEggItem(IafEntityRegistry.AMPHITHERE, 0X597535, 0X00AA98, new Item.Properties().tab(IceAndFire.TAB_ITEMS)).setRegistryName("iceandfire:spawn_egg_amphithere"));
        event.getRegistry().register(new ForgeSpawnEggItem(IafEntityRegistry.SEA_SERPENT, 0X008299, 0XC5E6E7, new Item.Properties().tab(IceAndFire.TAB_ITEMS)).setRegistryName("iceandfire:spawn_egg_sea_serpent"));
        event.getRegistry().register(new ForgeSpawnEggItem(IafEntityRegistry.DREAD_THRALL, 0XE0E6E6, 0X00FFFF, new Item.Properties().tab(IceAndFire.TAB_ITEMS)).setRegistryName("iceandfire:spawn_egg_dread_thrall"));
        event.getRegistry().register(new ForgeSpawnEggItem(IafEntityRegistry.DREAD_GHOUL, 0XE0E6E6, 0X7B838A, new Item.Properties().tab(IceAndFire.TAB_ITEMS)).setRegistryName("iceandfire:spawn_egg_dread_ghoul"));
        event.getRegistry().register(new ForgeSpawnEggItem(IafEntityRegistry.DREAD_BEAST, 0XE0E6E6, 0X38373C, new Item.Properties().tab(IceAndFire.TAB_ITEMS)).setRegistryName("iceandfire:spawn_egg_dread_beast"));
        event.getRegistry().register(new ForgeSpawnEggItem(IafEntityRegistry.DREAD_SCUTTLER, 0XE0E6E6, 0X4D5667, new Item.Properties().tab(IceAndFire.TAB_ITEMS)).setRegistryName("iceandfire:spawn_egg_dread_scuttler"));
        event.getRegistry().register(new ForgeSpawnEggItem(IafEntityRegistry.DREAD_LICH, 0XE0E6E6, 0X274860, new Item.Properties().tab(IceAndFire.TAB_ITEMS)).setRegistryName("iceandfire:spawn_egg_lich"));
        event.getRegistry().register(new ForgeSpawnEggItem(IafEntityRegistry.DREAD_KNIGHT, 0XE0E6E6, 0X4A6C6E, new Item.Properties().tab(IceAndFire.TAB_ITEMS)).setRegistryName("iceandfire:spawn_egg_dread_knight"));
        event.getRegistry().register(new ForgeSpawnEggItem(IafEntityRegistry.DREAD_HORSE, 0XE0E6E6, 0XACACAC, new Item.Properties().tab(IceAndFire.TAB_ITEMS)).setRegistryName("iceandfire:spawn_egg_dread_horse"));
        event.getRegistry().register(new ForgeSpawnEggItem(IafEntityRegistry.HYDRA, 0X8B8B78, 0X2E372B, new Item.Properties().tab(IceAndFire.TAB_ITEMS)).setRegistryName("iceandfire:spawn_egg_hydra"));
        event.getRegistry().register(new ForgeSpawnEggItem(IafEntityRegistry.GHOST, 0XB9EDB8, 0X73B276, new Item.Properties().tab(IceAndFire.TAB_ITEMS)).setRegistryName("iceandfire:spawn_egg_ghost"));
    }


}
