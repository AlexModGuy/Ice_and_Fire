package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.citadel.server.item.CustomArmorMaterial;
import com.github.alexthe666.citadel.server.item.CustomToolMaterial;
import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import com.github.alexthe666.iceandfire.enums.*;
import com.github.alexthe666.iceandfire.recipe.IafRecipeRegistry;
import net.minecraft.core.Registry;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.BannerPatternItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
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
    public static CustomArmorMaterial DRAGONSTEEL_FIRE_ARMOR_MATERIAL = new DragonsteelArmorMaterial("dragonsteel_fire", (int) (0.02D * IafConfig.dragonsteelBaseDurabilityEquipment), new int[]{IafConfig.dragonsteelBaseArmor - 6, IafConfig.dragonsteelBaseArmor - 3, IafConfig.dragonsteelBaseArmor, IafConfig.dragonsteelBaseArmor - 5}, 30, SoundEvents.ARMOR_EQUIP_DIAMOND, 6.0F);
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

    public static DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, IceAndFire.MODID);


    public static final RegistryObject<Item> BESTIARY = ITEMS.register("bestiary", ItemBestiary::new);
    public static final RegistryObject<Item> MANUSCRIPT = ITEMS.register("manuscript", ItemGeneric::new);
    public static final RegistryObject<Item> SAPPHIRE_GEM = ITEMS.register("sapphire_gem", ItemGeneric::new);
    public static final RegistryObject<Item> SILVER_INGOT = ITEMS.register("silver_ingot", ItemGeneric::new);
    public static final RegistryObject<Item> SILVER_NUGGET = ITEMS.register("silver_nugget", ItemGeneric::new);
    public static final RegistryObject<Item> AMYTHEST_GEM = ITEMS.register("amythest_gem", ItemGeneric::new);
    public static final RegistryObject<Item> COPPER_INGOT = ITEMS.register("copper_ingot", ItemGeneric::new);
    public static final RegistryObject<Item> COPPER_NUGGET = ITEMS.register("copper_nugget", ItemGeneric::new);
    public static final RegistryObject<Item> SILVER_HELMET = ITEMS.register("armor_silver_metal_helmet", () -> new ItemSilverArmor(SILVER_ARMOR_MATERIAL, EquipmentSlot.HEAD));
    public static final RegistryObject<Item> SILVER_CHESTPLATE = ITEMS.register("armor_silver_metal_chestplate", () -> new ItemSilverArmor(SILVER_ARMOR_MATERIAL, EquipmentSlot.CHEST));
    public static final RegistryObject<Item> SILVER_LEGGINGS = ITEMS.register("armor_silver_metal_leggings", () -> new ItemSilverArmor(SILVER_ARMOR_MATERIAL, EquipmentSlot.LEGS));
    public static final RegistryObject<Item> SILVER_BOOTS = ITEMS.register("armor_silver_metal_boots", () -> new ItemSilverArmor(SILVER_ARMOR_MATERIAL, EquipmentSlot.FEET));
    public static final RegistryObject<Item> SILVER_SWORD = ITEMS.register("silver_sword", () -> new ItemModSword(SILVER_TOOL_MATERIAL));
    public static final RegistryObject<Item> SILVER_SHOVEL = ITEMS.register("silver_shovel", () -> new ItemModShovel(SILVER_TOOL_MATERIAL));
    public static final RegistryObject<Item> SILVER_PICKAXE = ITEMS.register("silver_pickaxe", () -> new ItemModPickaxe(SILVER_TOOL_MATERIAL));
    public static final RegistryObject<Item> SILVER_AXE = ITEMS.register("silver_axe", () -> new ItemModAxe(SILVER_TOOL_MATERIAL));
    public static final RegistryObject<Item> SILVER_HOE = ITEMS.register("silver_hoe", () -> new ItemModHoe(SILVER_TOOL_MATERIAL));

    public static final RegistryObject<Item> COPPER_HELMET = ITEMS.register("armor_copper_metal_helmet", () -> new ItemCopperArmor(COPPER_ARMOR_MATERIAL, EquipmentSlot.HEAD));
    public static final RegistryObject<Item> COPPER_CHESTPLATE = ITEMS.register("armor_copper_metal_chestplate", () -> new ItemCopperArmor(COPPER_ARMOR_MATERIAL, EquipmentSlot.CHEST));
    public static final RegistryObject<Item> COPPER_LEGGINGS = ITEMS.register("armor_copper_metal_leggings", () -> new ItemCopperArmor(COPPER_ARMOR_MATERIAL, EquipmentSlot.LEGS));
    public static final RegistryObject<Item> COPPER_BOOTS = ITEMS.register("armor_copper_metal_boots", () -> new ItemCopperArmor(COPPER_ARMOR_MATERIAL, EquipmentSlot.FEET));
    public static final RegistryObject<Item> COPPER_SWORD = ITEMS.register("copper_sword", () -> new ItemModSword(COPPER_TOOL_MATERIAL));
    public static final RegistryObject<Item> COPPER_SHOVEL = ITEMS.register("copper_shovel", () -> new ItemModShovel(COPPER_TOOL_MATERIAL));
    public static final RegistryObject<Item> COPPER_PICKAXE = ITEMS.register("copper_pickaxe", () -> new ItemModPickaxe(COPPER_TOOL_MATERIAL));
    public static final RegistryObject<Item> COPPER_AXE = ITEMS.register("copper_axe", () -> new ItemModAxe(COPPER_TOOL_MATERIAL));
    public static final RegistryObject<Item> COPPER_HOE = ITEMS.register("copper_hoe", () -> new ItemModHoe(COPPER_TOOL_MATERIAL));

    public static final RegistryObject<Item> FIRE_STEW = ITEMS.register("fire_stew", ItemGeneric::new);
    public static final RegistryObject<Item> FROST_STEW = ITEMS.register("frost_stew", ItemGeneric::new);
    public static final RegistryObject<Item> LIGHTNING_STEW = ITEMS.register("lightning_stew", ItemGeneric::new);
    public static final RegistryObject<Item> DRAGONEGG_RED = ITEMS.register("dragonegg_red", () -> new ItemDragonEgg(EnumDragonEgg.RED));
    public static final RegistryObject<Item> DRAGONEGG_GREEN = ITEMS.register("dragonegg_green", () -> new ItemDragonEgg(EnumDragonEgg.GREEN));
    public static final RegistryObject<Item> DRAGONEGG_BRONZE = ITEMS.register("dragonegg_bronze", () -> new ItemDragonEgg(EnumDragonEgg.BRONZE));
    public static final RegistryObject<Item> DRAGONEGG_GRAY = ITEMS.register("dragonegg_gray", () -> new ItemDragonEgg(EnumDragonEgg.GRAY));
    public static final RegistryObject<Item> DRAGONEGG_BLUE = ITEMS.register("dragonegg_blue", () -> new ItemDragonEgg(EnumDragonEgg.BLUE));
    public static final RegistryObject<Item> DRAGONEGG_WHITE = ITEMS.register("dragonegg_white", () -> new ItemDragonEgg(EnumDragonEgg.WHITE));
    public static final RegistryObject<Item> DRAGONEGG_SAPPHIRE = ITEMS.register("dragonegg_sapphire", () -> new ItemDragonEgg(EnumDragonEgg.SAPPHIRE));
    public static final RegistryObject<Item> DRAGONEGG_SILVER = ITEMS.register("dragonegg_silver", () -> new ItemDragonEgg(EnumDragonEgg.SILVER));
    public static final RegistryObject<Item> DRAGONEGG_ELECTRIC = ITEMS.register("dragonegg_electric", () -> new ItemDragonEgg(EnumDragonEgg.ELECTRIC));
    public static final RegistryObject<Item> DRAGONEGG_AMYTHEST = ITEMS.register("dragonegg_amythest", () -> new ItemDragonEgg(EnumDragonEgg.AMYTHEST));
    public static final RegistryObject<Item> DRAGONEGG_COPPER = ITEMS.register("dragonegg_copper", () -> new ItemDragonEgg(EnumDragonEgg.COPPER));
    public static final RegistryObject<Item> DRAGONEGG_BLACK = ITEMS.register("dragonegg_black", () -> new ItemDragonEgg(EnumDragonEgg.BLACK));
    public static final RegistryObject<Item> DRAGONSCALES_RED = ITEMS.register("dragonscales_red", () -> new ItemDragonScales(EnumDragonEgg.RED));
    public static final RegistryObject<Item> DRAGONSCALES_GREEN = ITEMS.register("dragonscales_green", () -> new ItemDragonScales(EnumDragonEgg.GREEN));
    public static final RegistryObject<Item> DRAGONSCALES_BRONZE = ITEMS.register("dragonscales_bronze", () -> new ItemDragonScales(EnumDragonEgg.BRONZE));
    public static final RegistryObject<Item> DRAGONSCALES_GRAY = ITEMS.register("dragonscales_gray", () -> new ItemDragonScales(EnumDragonEgg.GRAY));
    public static final RegistryObject<Item> DRAGONSCALES_BLUE = ITEMS.register("dragonscales_blue", () -> new ItemDragonScales(EnumDragonEgg.BLUE));
    public static final RegistryObject<Item> DRAGONSCALES_WHITE = ITEMS.register("dragonscales_white", () -> new ItemDragonScales(EnumDragonEgg.WHITE));
    public static final RegistryObject<Item> DRAGONSCALES_SAPPHIRE = ITEMS.register("dragonscales_sapphire", () -> new ItemDragonScales(EnumDragonEgg.SAPPHIRE));
    public static final RegistryObject<Item> DRAGONSCALES_SILVER = ITEMS.register("dragonscales_silver", () -> new ItemDragonScales(EnumDragonEgg.SILVER));
    public static final RegistryObject<Item> DRAGONSCALES_ELECTRIC = ITEMS.register("dragonscales_electric", () -> new ItemDragonScales(EnumDragonEgg.ELECTRIC));
    public static final RegistryObject<Item> DRAGONSCALES_AMYTHEST = ITEMS.register("dragonscales_amythest", () -> new ItemDragonScales(EnumDragonEgg.AMYTHEST));
    public static final RegistryObject<Item> DRAGONSCALES_COPPER = ITEMS.register("dragonscales_copper", () -> new ItemDragonScales(EnumDragonEgg.COPPER));
    public static final RegistryObject<Item> DRAGONSCALES_BLACK = ITEMS.register("dragonscales_black", () -> new ItemDragonScales(EnumDragonEgg.BLACK));
    public static final RegistryObject<Item> DRAGON_BONE = ITEMS.register("dragonbone", () -> new ItemDragonBone());
    public static final RegistryObject<Item> WITHERBONE = ITEMS.register("witherbone", ItemGeneric::new);
    public static final RegistryObject<Item> FISHING_SPEAR = ITEMS.register("fishing_spear", () -> new ItemFishingSpear());
    public static final RegistryObject<Item> WITHER_SHARD = ITEMS.register("wither_shard", ItemGeneric::new);
    public static final RegistryObject<Item> DRAGONBONE_SWORD = ITEMS.register("dragonbone_sword", () -> new ItemModSword(DRAGONBONE_TOOL_MATERIAL));
    public static final RegistryObject<Item> DRAGONBONE_SHOVEL = ITEMS.register("dragonbone_shovel", () -> new ItemModShovel(DRAGONBONE_TOOL_MATERIAL));
    public static final RegistryObject<Item> DRAGONBONE_PICKAXE = ITEMS.register("dragonbone_pickaxe", () -> new ItemModPickaxe(DRAGONBONE_TOOL_MATERIAL));
    public static final RegistryObject<Item> DRAGONBONE_AXE = ITEMS.register("dragonbone_axe", () -> new ItemModAxe(DRAGONBONE_TOOL_MATERIAL));
    public static final RegistryObject<Item> DRAGONBONE_HOE = ITEMS.register("dragonbone_hoe", () -> new ItemModHoe(DRAGONBONE_TOOL_MATERIAL));
    public static final RegistryObject<Item> DRAGONBONE_SWORD_FIRE = ITEMS.register("dragonbone_sword_fire", () -> new ItemAlchemySword(FIRE_DRAGONBONE_TOOL_MATERIAL));
    public static final RegistryObject<Item> DRAGONBONE_SWORD_ICE = ITEMS.register("dragonbone_sword_ice", () -> new ItemAlchemySword(ICE_DRAGONBONE_TOOL_MATERIAL));
    public static final RegistryObject<Item> DRAGONBONE_SWORD_LIGHTNING = ITEMS.register("dragonbone_sword_lightning", () -> new ItemAlchemySword(LIGHTNING_DRAGONBONE_TOOL_MATERIAL));
    public static final RegistryObject<Item> DRAGONBONE_ARROW = ITEMS.register("dragonbone_arrow", () -> new ItemDragonArrow());
    public static final RegistryObject<Item> DRAGON_BOW = ITEMS.register("dragonbone_bow", () -> new ItemDragonBow());
    public static final RegistryObject<Item> DRAGON_SKULL_FIRE = ITEMS.register(ItemDragonSkull.getName(0), () -> new ItemDragonSkull(0));
    public static final RegistryObject<Item> DRAGON_SKULL_ICE = ITEMS.register(ItemDragonSkull.getName(1), () -> new ItemDragonSkull(1));
    public static final RegistryObject<Item> DRAGON_SKULL_LIGHTNING = ITEMS.register(ItemDragonSkull.getName(2), () -> new ItemDragonSkull(2));
    public static final RegistryObject<ItemDragonArmor> DRAGONARMOR_IRON_0 = ITEMS.register("dragonarmor_iron_" + ItemDragonArmor.getNameForSlot(0), () -> new ItemDragonArmor(ItemDragonArmor.DragonArmorType.IRON, 0));
    public static final RegistryObject<ItemDragonArmor> DRAGONARMOR_IRON_1 = ITEMS.register("dragonarmor_iron_" + ItemDragonArmor.getNameForSlot(1), () -> new ItemDragonArmor(ItemDragonArmor.DragonArmorType.IRON, 1));
    public static final RegistryObject<ItemDragonArmor> DRAGONARMOR_IRON_2 = ITEMS.register("dragonarmor_iron_" + ItemDragonArmor.getNameForSlot(2), () -> new ItemDragonArmor(ItemDragonArmor.DragonArmorType.IRON, 2));
    public static final RegistryObject<ItemDragonArmor> DRAGONARMOR_IRON_3 = ITEMS.register("dragonarmor_iron_" + ItemDragonArmor.getNameForSlot(3), () -> new ItemDragonArmor(ItemDragonArmor.DragonArmorType.IRON, 3));
    public static final RegistryObject<ItemDragonArmor> DRAGONARMOR_COPPER_0 = ITEMS.register("dragonarmor_copper_" + ItemDragonArmor.getNameForSlot(0), () -> new ItemDragonArmor(ItemDragonArmor.DragonArmorType.COPPER, 0));
    public static final RegistryObject<ItemDragonArmor> DRAGONARMOR_COPPER_1 = ITEMS.register("dragonarmor_copper_" + ItemDragonArmor.getNameForSlot(1), () -> new ItemDragonArmor(ItemDragonArmor.DragonArmorType.COPPER, 1));
    public static final RegistryObject<ItemDragonArmor> DRAGONARMOR_COPPER_2 = ITEMS.register("dragonarmor_copper_" + ItemDragonArmor.getNameForSlot(2), () -> new ItemDragonArmor(ItemDragonArmor.DragonArmorType.COPPER, 2));
    public static final RegistryObject<ItemDragonArmor> DRAGONARMOR_COPPER_3 = ITEMS.register("dragonarmor_copper_" + ItemDragonArmor.getNameForSlot(3), () -> new ItemDragonArmor(ItemDragonArmor.DragonArmorType.COPPER, 3));
    public static final RegistryObject<ItemDragonArmor> DRAGONARMOR_GOLD_0 = ITEMS.register("dragonarmor_gold_" + ItemDragonArmor.getNameForSlot(0), () -> new ItemDragonArmor(ItemDragonArmor.DragonArmorType.GOLD, 0));
    public static final RegistryObject<ItemDragonArmor> DRAGONARMOR_GOLD_1 = ITEMS.register("dragonarmor_gold_" + ItemDragonArmor.getNameForSlot(1), () -> new ItemDragonArmor(ItemDragonArmor.DragonArmorType.GOLD, 1));
    public static final RegistryObject<ItemDragonArmor> DRAGONARMOR_GOLD_2 = ITEMS.register("dragonarmor_gold_" + ItemDragonArmor.getNameForSlot(2), () -> new ItemDragonArmor(ItemDragonArmor.DragonArmorType.GOLD, 2));
    public static final RegistryObject<ItemDragonArmor> DRAGONARMOR_GOLD_3 = ITEMS.register("dragonarmor_gold_" + ItemDragonArmor.getNameForSlot(3), () -> new ItemDragonArmor(ItemDragonArmor.DragonArmorType.GOLD, 3));
    public static final RegistryObject<ItemDragonArmor> DRAGONARMOR_DIAMOND_0 = ITEMS.register("dragonarmor_diamond_" + ItemDragonArmor.getNameForSlot(0), () -> new ItemDragonArmor(ItemDragonArmor.DragonArmorType.DIAMOND, 0));
    public static final RegistryObject<ItemDragonArmor> DRAGONARMOR_DIAMOND_1 = ITEMS.register("dragonarmor_diamond_" + ItemDragonArmor.getNameForSlot(1), () -> new ItemDragonArmor(ItemDragonArmor.DragonArmorType.DIAMOND, 1));
    public static final RegistryObject<ItemDragonArmor> DRAGONARMOR_DIAMOND_2 = ITEMS.register("dragonarmor_diamond_" + ItemDragonArmor.getNameForSlot(2), () -> new ItemDragonArmor(ItemDragonArmor.DragonArmorType.DIAMOND, 2));
    public static final RegistryObject<ItemDragonArmor> DRAGONARMOR_DIAMOND_3 = ITEMS.register("dragonarmor_diamond_" + ItemDragonArmor.getNameForSlot(3), () -> new ItemDragonArmor(ItemDragonArmor.DragonArmorType.DIAMOND, 3));
    public static final RegistryObject<ItemDragonArmor> DRAGONARMOR_SILVER_0 = ITEMS.register("dragonarmor_silver_" + ItemDragonArmor.getNameForSlot(0), () -> new ItemDragonArmor(ItemDragonArmor.DragonArmorType.SILVER, 0));
    public static final RegistryObject<ItemDragonArmor> DRAGONARMOR_SILVER_1 = ITEMS.register("dragonarmor_silver_" + ItemDragonArmor.getNameForSlot(1), () -> new ItemDragonArmor(ItemDragonArmor.DragonArmorType.SILVER, 1));
    public static final RegistryObject<ItemDragonArmor> DRAGONARMOR_SILVER_2 = ITEMS.register("dragonarmor_silver_" + ItemDragonArmor.getNameForSlot(2), () -> new ItemDragonArmor(ItemDragonArmor.DragonArmorType.SILVER, 2));
    public static final RegistryObject<ItemDragonArmor> DRAGONARMOR_SILVER_3 = ITEMS.register("dragonarmor_silver_" + ItemDragonArmor.getNameForSlot(3), () -> new ItemDragonArmor(ItemDragonArmor.DragonArmorType.SILVER, 3));
    public static final RegistryObject<ItemDragonArmor> DRAGONARMOR_DRAGONSTEEL_FIRE_0 = ITEMS.register("dragonarmor_dragonsteel_fire_" + ItemDragonArmor.getNameForSlot(0), () -> new ItemDragonArmor(ItemDragonArmor.DragonArmorType.FIRE, 0));
    public static final RegistryObject<ItemDragonArmor> DRAGONARMOR_DRAGONSTEEL_FIRE_1 = ITEMS.register("dragonarmor_dragonsteel_fire_" + ItemDragonArmor.getNameForSlot(1), () -> new ItemDragonArmor(ItemDragonArmor.DragonArmorType.FIRE, 1));
    public static final RegistryObject<ItemDragonArmor> DRAGONARMOR_DRAGONSTEEL_FIRE_2 = ITEMS.register("dragonarmor_dragonsteel_fire_" + ItemDragonArmor.getNameForSlot(2), () -> new ItemDragonArmor(ItemDragonArmor.DragonArmorType.FIRE, 2));
    public static final RegistryObject<ItemDragonArmor> DRAGONARMOR_DRAGONSTEEL_FIRE_3 = ITEMS.register("dragonarmor_dragonsteel_fire_" + ItemDragonArmor.getNameForSlot(3), () -> new ItemDragonArmor(ItemDragonArmor.DragonArmorType.FIRE, 3));
    public static final RegistryObject<ItemDragonArmor> DRAGONARMOR_DRAGONSTEEL_ICE_0 = ITEMS.register("dragonarmor_dragonsteel_ice_" + ItemDragonArmor.getNameForSlot(0), () -> new ItemDragonArmor(ItemDragonArmor.DragonArmorType.ICE, 0));
    public static final RegistryObject<ItemDragonArmor> DRAGONARMOR_DRAGONSTEEL_ICE_1 = ITEMS.register("dragonarmor_dragonsteel_ice_" + ItemDragonArmor.getNameForSlot(1), () -> new ItemDragonArmor(ItemDragonArmor.DragonArmorType.ICE, 1));
    public static final RegistryObject<ItemDragonArmor> DRAGONARMOR_DRAGONSTEEL_ICE_2 = ITEMS.register("dragonarmor_dragonsteel_ice_" + ItemDragonArmor.getNameForSlot(2), () -> new ItemDragonArmor(ItemDragonArmor.DragonArmorType.ICE, 2));
    public static final RegistryObject<ItemDragonArmor> DRAGONARMOR_DRAGONSTEEL_ICE_3 = ITEMS.register("dragonarmor_dragonsteel_ice_" + ItemDragonArmor.getNameForSlot(3), () -> new ItemDragonArmor(ItemDragonArmor.DragonArmorType.ICE, 3));
    public static final RegistryObject<ItemDragonArmor> DRAGONARMOR_DRAGONSTEEL_LIGHTNING_0 = ITEMS.register("dragonarmor_dragonsteel_lightning_" + ItemDragonArmor.getNameForSlot(0), () -> new ItemDragonArmor(ItemDragonArmor.DragonArmorType.LIGHTNING, 0));
    public static final RegistryObject<ItemDragonArmor> DRAGONARMOR_DRAGONSTEEL_LIGHTNING_1 = ITEMS.register("dragonarmor_dragonsteel_lightning_" + ItemDragonArmor.getNameForSlot(1), () -> new ItemDragonArmor(ItemDragonArmor.DragonArmorType.LIGHTNING, 1));
    public static final RegistryObject<ItemDragonArmor> DRAGONARMOR_DRAGONSTEEL_LIGHTNING_2 = ITEMS.register("dragonarmor_dragonsteel_lightning_" + ItemDragonArmor.getNameForSlot(2), () -> new ItemDragonArmor(ItemDragonArmor.DragonArmorType.LIGHTNING, 2));
    public static final RegistryObject<ItemDragonArmor> DRAGONARMOR_DRAGONSTEEL_LIGHTNING_3 = ITEMS.register("dragonarmor_dragonsteel_lightning_" + ItemDragonArmor.getNameForSlot(3), () -> new ItemDragonArmor(ItemDragonArmor.DragonArmorType.LIGHTNING, 3));
    public static final RegistryObject<Item> DRAGON_MEAL = ITEMS.register("dragon_meal", ItemGeneric::new);
    public static final RegistryObject<Item> SICKLY_DRAGON_MEAL = ITEMS.register("sickly_dragon_meal", () -> new ItemGeneric(1));
    public static final RegistryObject<Item> CREATIVE_DRAGON_MEAL = ITEMS.register("creative_dragon_meal", () -> new ItemGeneric(2));
    public static final RegistryObject<Item> FIRE_DRAGON_FLESH = ITEMS.register(ItemDragonFlesh.getNameForType(0), () -> new ItemDragonFlesh(0));
    public static final RegistryObject<Item> ICE_DRAGON_FLESH = ITEMS.register(ItemDragonFlesh.getNameForType(1), () -> new ItemDragonFlesh(1));
    public static final RegistryObject<Item> LIGHTNING_DRAGON_FLESH = ITEMS.register(ItemDragonFlesh.getNameForType(2), () -> new ItemDragonFlesh(2));
    public static final RegistryObject<Item> FIRE_DRAGON_HEART = ITEMS.register("fire_dragon_heart", ItemGeneric::new);
    public static final RegistryObject<Item> ICE_DRAGON_HEART = ITEMS.register("ice_dragon_heart", ItemGeneric::new);
    public static final RegistryObject<Item> LIGHTNING_DRAGON_HEART = ITEMS.register("lightning_dragon_heart", ItemGeneric::new);
    public static final RegistryObject<Item> FIRE_DRAGON_BLOOD = ITEMS.register("fire_dragon_blood", ItemGeneric::new);
    public static final RegistryObject<Item> ICE_DRAGON_BLOOD = ITEMS.register("ice_dragon_blood", ItemGeneric::new);
    public static final RegistryObject<Item> LIGHTNING_DRAGON_BLOOD = ITEMS.register("lightning_dragon_blood", ItemGeneric::new);
    public static final RegistryObject<Item> DRAGON_STAFF = ITEMS.register("dragon_stick", () -> new ItemDragonStaff());
    public static final RegistryObject<Item> DRAGON_HORN = ITEMS.register("dragon_horn", () -> new ItemDragonHorn());
    public static final RegistryObject<Item> DRAGON_FLUTE = ITEMS.register("dragon_flute", () -> new ItemDragonFlute());
    public static final RegistryObject<Item> SUMMONING_CRYSTAL_FIRE = ITEMS.register("summoning_crystal_fire", () -> new ItemSummoningCrystal());
    public static final RegistryObject<Item> SUMMONING_CRYSTAL_ICE = ITEMS.register("summoning_crystal_ice", () -> new ItemSummoningCrystal());
    public static final RegistryObject<Item> SUMMONING_CRYSTAL_LIGHTNING = ITEMS.register("summoning_crystal_lightning", () -> new ItemSummoningCrystal());
    public static final RegistryObject<Item> HIPPOGRYPH_EGG = ITEMS.register("hippogryph_egg", () -> new ItemHippogryphEgg());
    public static final RegistryObject<Item> IRON_HIPPOGRYPH_ARMOR = ITEMS.register("iron_hippogryph_armor", () -> new ItemGeneric(0, 1));
    public static final RegistryObject<Item> GOLD_HIPPOGRYPH_ARMOR = ITEMS.register("gold_hippogryph_armor", () -> new ItemGeneric(0, 1));
    public static final RegistryObject<Item> DIAMOND_HIPPOGRYPH_ARMOR = ITEMS.register("diamond_hippogryph_armor", () -> new ItemGeneric(0, 1));
    public static final RegistryObject<Item> HIPPOGRYPH_TALON = ITEMS.register("hippogryph_talon", () -> new ItemGeneric(1));
    public static final RegistryObject<Item> HIPPOGRYPH_SWORD = ITEMS.register("hippogryph_sword", () -> new ItemHippogryphSword());
    public static final RegistryObject<Item> GORGON_HEAD = ITEMS.register("gorgon_head", () -> new ItemGorgonHead());
    public static final RegistryObject<Item> STONE_STATUE = ITEMS.register("stone_statue", () -> new ItemStoneStatue());
    public static final RegistryObject<Item> BLINDFOLD = ITEMS.register("blindfold", () -> new ItemBlindfold());
    public static final RegistryObject<Item> PIXIE_DUST = ITEMS.register("pixie_dust", () -> new ItemPixieDust());
    public static final RegistryObject<Item> PIXIE_WINGS = ITEMS.register("pixie_wings", () -> new ItemGeneric(1));
    public static final RegistryObject<Item> PIXIE_WAND = ITEMS.register("pixie_wand", () -> new ItemPixieWand());
    public static final RegistryObject<Item> AMBROSIA = ITEMS.register("ambrosia", () -> new ItemAmbrosia());
    public static final RegistryObject<Item> CYCLOPS_EYE = ITEMS.register("cyclops_eye", () -> new ItemCyclopsEye());
    public static final RegistryObject<Item> SHEEP_HELMET = ITEMS.register("sheep_helmet", () -> new ItemModArmor(SHEEP_ARMOR_MATERIAL, EquipmentSlot.HEAD));
    public static final RegistryObject<Item> SHEEP_CHESTPLATE = ITEMS.register("sheep_chestplate", () -> new ItemModArmor(SHEEP_ARMOR_MATERIAL, EquipmentSlot.CHEST));
    public static final RegistryObject<Item> SHEEP_LEGGINGS = ITEMS.register("sheep_leggings", () -> new ItemModArmor(SHEEP_ARMOR_MATERIAL, EquipmentSlot.LEGS));
    public static final RegistryObject<Item> SHEEP_BOOTS = ITEMS.register("sheep_boots", () -> new ItemModArmor(SHEEP_ARMOR_MATERIAL, EquipmentSlot.FEET));
    public static final RegistryObject<Item> SHINY_SCALES = ITEMS.register("shiny_scales", ItemGeneric::new);
    public static final RegistryObject<Item> SIREN_TEAR = ITEMS.register("siren_tear", () -> new ItemGeneric(1));
    public static final RegistryObject<Item> SIREN_FLUTE = ITEMS.register("siren_flute", () -> new ItemSirenFlute());
    public static final RegistryObject<Item> HIPPOCAMPUS_FIN = ITEMS.register("hippocampus_fin", () -> new ItemGeneric(1));
    public static final RegistryObject<Item> HIPPOCAMPUS_SLAPPER = ITEMS.register("hippocampus_slapper", () -> new ItemHippocampusSlapper());
    public static final RegistryObject<Item> EARPLUGS = ITEMS.register("earplugs", () -> new ItemModArmor(EARPLUGS_ARMOR_MATERIAL, EquipmentSlot.HEAD));
    public static final RegistryObject<Item> DEATH_WORM_CHITIN_YELLOW = ITEMS.register("deathworm_chitin_yellow", ItemGeneric::new);
    public static final RegistryObject<Item> DEATH_WORM_CHITIN_WHITE = ITEMS.register("deathworm_chitin_white", ItemGeneric::new);
    public static final RegistryObject<Item> DEATH_WORM_CHITIN_RED = ITEMS.register("deathworm_chitin_red", ItemGeneric::new);
    public static final RegistryObject<Item> DEATHWORM_YELLOW_HELMET = ITEMS.register("deathworm_yellow_helmet", () -> new ItemDeathwormArmor(DEATHWORM_0_ARMOR_MATERIAL, EquipmentSlot.HEAD));
    public static final RegistryObject<Item> DEATHWORM_YELLOW_CHESTPLATE = ITEMS.register("deathworm_yellow_chestplate", () -> new ItemDeathwormArmor(DEATHWORM_0_ARMOR_MATERIAL, EquipmentSlot.CHEST));
    public static final RegistryObject<Item> DEATHWORM_YELLOW_LEGGINGS = ITEMS.register("deathworm_yellow_leggings", () -> new ItemDeathwormArmor(DEATHWORM_0_ARMOR_MATERIAL, EquipmentSlot.LEGS));
    public static final RegistryObject<Item> DEATHWORM_YELLOW_BOOTS = ITEMS.register("deathworm_yellow_boots", () -> new ItemDeathwormArmor(DEATHWORM_0_ARMOR_MATERIAL, EquipmentSlot.FEET));
    public static final RegistryObject<Item> DEATHWORM_WHITE_HELMET = ITEMS.register("deathworm_white_helmet", () -> new ItemDeathwormArmor(DEATHWORM_1_ARMOR_MATERIAL, EquipmentSlot.HEAD));
    public static final RegistryObject<Item> DEATHWORM_WHITE_CHESTPLATE = ITEMS.register("deathworm_white_chestplate", () -> new ItemDeathwormArmor(DEATHWORM_1_ARMOR_MATERIAL, EquipmentSlot.CHEST));
    public static final RegistryObject<Item> DEATHWORM_WHITE_LEGGINGS = ITEMS.register("deathworm_white_leggings", () -> new ItemDeathwormArmor(DEATHWORM_1_ARMOR_MATERIAL, EquipmentSlot.LEGS));
    public static final RegistryObject<Item> DEATHWORM_WHITE_BOOTS = ITEMS.register("deathworm_white_boots", () -> new ItemDeathwormArmor(DEATHWORM_1_ARMOR_MATERIAL, EquipmentSlot.FEET));
    public static final RegistryObject<Item> DEATHWORM_RED_HELMET = ITEMS.register("deathworm_red_helmet", () -> new ItemDeathwormArmor(DEATHWORM_2_ARMOR_MATERIAL, EquipmentSlot.HEAD));
    public static final RegistryObject<Item> DEATHWORM_RED_CHESTPLATE = ITEMS.register("deathworm_red_chestplate", () -> new ItemDeathwormArmor(DEATHWORM_2_ARMOR_MATERIAL, EquipmentSlot.CHEST));
    public static final RegistryObject<Item> DEATHWORM_RED_LEGGINGS = ITEMS.register("deathworm_red_leggings", () -> new ItemDeathwormArmor(DEATHWORM_2_ARMOR_MATERIAL, EquipmentSlot.LEGS));
    public static final RegistryObject<Item> DEATHWORM_RED_BOOTS = ITEMS.register("deathworm_red_boots", () -> new ItemDeathwormArmor(DEATHWORM_2_ARMOR_MATERIAL, EquipmentSlot.FEET));
    public static final RegistryObject<Item> DEATHWORM_EGG = ITEMS.register("deathworm_egg", () -> new ItemDeathwormEgg(false));
    public static final RegistryObject<Item> DEATHWORM_EGG_GIGANTIC = ITEMS.register("deathworm_egg_giant", () -> new ItemDeathwormEgg(true));
    public static final RegistryObject<Item> DEATHWORM_TOUNGE = ITEMS.register("deathworm_tounge", () -> new ItemGeneric(1));
    public static final RegistryObject<Item> DEATHWORM_GAUNTLET_YELLOW = ITEMS.register("deathworm_gauntlet_yellow", () -> new ItemDeathwormGauntlet());
    public static final RegistryObject<Item> DEATHWORM_GAUNTLET_WHITE = ITEMS.register("deathworm_gauntlet_white", () -> new ItemDeathwormGauntlet());
    public static final RegistryObject<Item> DEATHWORM_GAUNTLET_RED = ITEMS.register("deathworm_gauntlet_red", () -> new ItemDeathwormGauntlet());
    public static final RegistryObject<Item> ROTTEN_EGG = ITEMS.register("rotten_egg", () -> new ItemRottenEgg());
    public static final RegistryObject<Item> COCKATRICE_EYE = ITEMS.register("cockatrice_eye", () -> new ItemGeneric(1));
    public static final RegistryObject<Item> ITEM_COCKATRICE_SCEPTER = ITEMS.register("cockatrice_scepter", () -> new ItemCockatriceScepter());
    public static final RegistryObject<Item> STYMPHALIAN_BIRD_FEATHER = ITEMS.register("stymphalian_bird_feather", ItemGeneric::new);
    public static final RegistryObject<Item> STYMPHALIAN_ARROW = ITEMS.register("stymphalian_arrow", () -> new ItemStymphalianArrow());
    public static final RegistryObject<Item> STYMPHALIAN_FEATHER_BUNDLE = ITEMS.register("stymphalian_feather_bundle", () -> new ItemStymphalianFeatherBundle());
    public static final RegistryObject<Item> STYMPHALIAN_DAGGER = ITEMS.register("stymphalian_bird_dagger", () -> new ItemStymphalianDagger());
    public static final RegistryObject<Item> TROLL_TUSK = ITEMS.register("troll_tusk", ItemGeneric::new);
    public static final RegistryObject<Item> MYRMEX_DESERT_EGG = ITEMS.register("myrmex_desert_egg", () -> new ItemMyrmexEgg(false));
    public static final RegistryObject<Item> MYRMEX_JUNGLE_EGG = ITEMS.register("myrmex_jungle_egg", () -> new ItemMyrmexEgg(true));
    public static final RegistryObject<Item> MYRMEX_DESERT_RESIN = ITEMS.register("myrmex_desert_resin", ItemGeneric::new);
    public static final RegistryObject<Item> MYRMEX_JUNGLE_RESIN = ITEMS.register("myrmex_jungle_resin", ItemGeneric::new);
    public static final RegistryObject<Item> MYRMEX_DESERT_CHITIN = ITEMS.register("myrmex_desert_chitin", ItemGeneric::new);
    public static final RegistryObject<Item> MYRMEX_JUNGLE_CHITIN = ITEMS.register("myrmex_jungle_chitin", ItemGeneric::new);
    public static final RegistryObject<Item> MYRMEX_STINGER = ITEMS.register("myrmex_stinger", ItemGeneric::new);
    public static final RegistryObject<Item> MYRMEX_DESERT_SWORD = ITEMS.register("myrmex_desert_sword", () -> new ItemModSword(MYRMEX_CHITIN_TOOL_MATERIAL));
    public static final RegistryObject<Item> MYRMEX_DESERT_SWORD_VENOM = ITEMS.register("myrmex_desert_sword_venom", () -> new ItemModSword(MYRMEX_CHITIN_TOOL_MATERIAL));
    public static final RegistryObject<Item> MYRMEX_DESERT_SHOVEL = ITEMS.register("myrmex_desert_shovel", () -> new ItemModShovel(MYRMEX_CHITIN_TOOL_MATERIAL));
    public static final RegistryObject<Item> MYRMEX_DESERT_PICKAXE = ITEMS.register("myrmex_desert_pickaxe", () -> new ItemModPickaxe(MYRMEX_CHITIN_TOOL_MATERIAL));
    public static final RegistryObject<Item> MYRMEX_DESERT_AXE = ITEMS.register("myrmex_desert_axe", () -> new ItemModAxe(MYRMEX_CHITIN_TOOL_MATERIAL));
    public static final RegistryObject<Item> MYRMEX_DESERT_HOE = ITEMS.register("myrmex_desert_hoe", () -> new ItemModHoe(MYRMEX_CHITIN_TOOL_MATERIAL));
    public static final RegistryObject<Item> MYRMEX_JUNGLE_SWORD = ITEMS.register("myrmex_jungle_sword", () -> new ItemModSword(MYRMEX_CHITIN_TOOL_MATERIAL));
    public static final RegistryObject<Item> MYRMEX_JUNGLE_SWORD_VENOM = ITEMS.register("myrmex_jungle_sword_venom", () -> new ItemModSword(MYRMEX_CHITIN_TOOL_MATERIAL));
    public static final RegistryObject<Item> MYRMEX_JUNGLE_SHOVEL = ITEMS.register("myrmex_jungle_shovel", () -> new ItemModShovel(MYRMEX_CHITIN_TOOL_MATERIAL));
    public static final RegistryObject<Item> MYRMEX_JUNGLE_PICKAXE = ITEMS.register("myrmex_jungle_pickaxe", () -> new ItemModPickaxe(MYRMEX_CHITIN_TOOL_MATERIAL));
    public static final RegistryObject<Item> MYRMEX_JUNGLE_AXE = ITEMS.register("myrmex_jungle_axe", () -> new ItemModAxe(MYRMEX_CHITIN_TOOL_MATERIAL));
    public static final RegistryObject<Item> MYRMEX_JUNGLE_HOE = ITEMS.register("myrmex_jungle_hoe", () -> new ItemModHoe(MYRMEX_CHITIN_TOOL_MATERIAL));
    public static final RegistryObject<Item> MYRMEX_DESERT_STAFF = ITEMS.register("myrmex_desert_staff", () -> new ItemMyrmexStaff(false));
    public static final RegistryObject<Item> MYRMEX_JUNGLE_STAFF = ITEMS.register("myrmex_jungle_staff", () -> new ItemMyrmexStaff(true));
    public static final RegistryObject<Item> MYRMEX_DESERT_HELMET = ITEMS.register("myrmex_desert_helmet", () -> new ItemModArmor(MYRMEX_DESERT_ARMOR_MATERIAL, EquipmentSlot.HEAD));
    public static final RegistryObject<Item> MYRMEX_DESERT_CHESTPLATE = ITEMS.register("myrmex_desert_chestplate", () -> new ItemModArmor(MYRMEX_DESERT_ARMOR_MATERIAL, EquipmentSlot.CHEST));
    public static final RegistryObject<Item> MYRMEX_DESERT_LEGGINGS = ITEMS.register("myrmex_desert_leggings", () -> new ItemModArmor(MYRMEX_DESERT_ARMOR_MATERIAL, EquipmentSlot.LEGS));
    public static final RegistryObject<Item> MYRMEX_DESERT_BOOTS = ITEMS.register("myrmex_desert_boots", () -> new ItemModArmor(MYRMEX_DESERT_ARMOR_MATERIAL, EquipmentSlot.FEET));
    public static final RegistryObject<Item> MYRMEX_JUNGLE_HELMET = ITEMS.register("myrmex_jungle_helmet", () -> new ItemModArmor(MYRMEX_JUNGLE_ARMOR_MATERIAL, EquipmentSlot.HEAD));
    public static final RegistryObject<Item> MYRMEX_JUNGLE_CHESTPLATE = ITEMS.register("myrmex_jungle_chestplate", () -> new ItemModArmor(MYRMEX_JUNGLE_ARMOR_MATERIAL, EquipmentSlot.CHEST));
    public static final RegistryObject<Item> MYRMEX_JUNGLE_LEGGINGS = ITEMS.register("myrmex_jungle_leggings", () -> new ItemModArmor(MYRMEX_JUNGLE_ARMOR_MATERIAL, EquipmentSlot.LEGS));
    public static final RegistryObject<Item> MYRMEX_JUNGLE_BOOTS = ITEMS.register("myrmex_jungle_boots", () -> new ItemModArmor(MYRMEX_JUNGLE_ARMOR_MATERIAL, EquipmentSlot.FEET));
    public static final RegistryObject<Item> MYRMEX_DESERT_SWARM = ITEMS.register("myrmex_desert_swarm", () -> new ItemMyrmexSwarm(false));
    public static final RegistryObject<Item> MYRMEX_JUNGLE_SWARM = ITEMS.register("myrmex_jungle_swarm", () -> new ItemMyrmexSwarm(true));
    public static final RegistryObject<Item> AMPHITHERE_FEATHER = ITEMS.register("amphithere_feather", ItemGeneric::new);
    public static final RegistryObject<Item> AMPHITHERE_ARROW = ITEMS.register("amphithere_arrow", () -> new ItemAmphithereArrow());
    public static final RegistryObject<Item> AMPHITHERE_MACUAHUITL = ITEMS.register("amphithere_macuahuitl", () -> new ItemAmphithereMacuahuitl());
    public static final RegistryObject<Item> SERPENT_FANG = ITEMS.register("sea_serpent_fang", ItemGeneric::new);
    public static final RegistryObject<Item> SEA_SERPENT_ARROW = ITEMS.register("sea_serpent_arrow", () -> new ItemSeaSerpentArrow());
    public static final RegistryObject<Item> TIDE_TRIDENT_INVENTORY = ITEMS.register("tide_trident_inventory", () -> new ItemGeneric(0, true));
    public static final RegistryObject<Item> TIDE_TRIDENT = ITEMS.register("tide_trident", () -> new ItemTideTrident());
    public static final RegistryObject<Item> CHAIN = ITEMS.register("chain", () -> new ItemChain(false));
    public static final RegistryObject<Item> CHAIN_STICKY = ITEMS.register("chain_sticky", () -> new ItemChain(true));
    public static final RegistryObject<Item> DRAGONSTEEL_FIRE_INGOT = ITEMS.register("dragonsteel_fire_ingot", ItemGeneric::new);
    public static final RegistryObject<Item> DRAGONSTEEL_FIRE_SWORD = ITEMS.register("dragonsteel_fire_sword", () -> new ItemModSword(DRAGONSTEEL_TIER_FIRE));
    public static final RegistryObject<Item> DRAGONSTEEL_FIRE_PICKAXE = ITEMS.register("dragonsteel_fire_pickaxe", () -> new ItemModPickaxe(DRAGONSTEEL_TIER_FIRE));
    public static final RegistryObject<Item> DRAGONSTEEL_FIRE_AXE = ITEMS.register("dragonsteel_fire_axe", () -> new ItemModAxe(DRAGONSTEEL_TIER_FIRE));
    public static final RegistryObject<Item> DRAGONSTEEL_FIRE_SHOVEL = ITEMS.register("dragonsteel_fire_shovel", () -> new ItemModShovel(DRAGONSTEEL_TIER_FIRE));
    public static final RegistryObject<Item> DRAGONSTEEL_FIRE_HOE = ITEMS.register("dragonsteel_fire_hoe", () -> new ItemModHoe(DRAGONSTEEL_TIER_FIRE));
    public static final RegistryObject<Item> DRAGONSTEEL_FIRE_HELMET = ITEMS.register("dragonsteel_fire_helmet", () -> new ItemDragonsteelArmor(DRAGONSTEEL_FIRE_ARMOR_MATERIAL, 0, EquipmentSlot.HEAD));
    public static final RegistryObject<Item> DRAGONSTEEL_FIRE_CHESTPLATE = ITEMS.register("dragonsteel_fire_chestplate", () -> new ItemDragonsteelArmor(DRAGONSTEEL_FIRE_ARMOR_MATERIAL, 1, EquipmentSlot.CHEST));
    public static final RegistryObject<Item> DRAGONSTEEL_FIRE_LEGGINGS = ITEMS.register("dragonsteel_fire_leggings", () -> new ItemDragonsteelArmor(DRAGONSTEEL_FIRE_ARMOR_MATERIAL, 2, EquipmentSlot.LEGS));
    public static final RegistryObject<Item> DRAGONSTEEL_FIRE_BOOTS = ITEMS.register("dragonsteel_fire_boots", () -> new ItemDragonsteelArmor(DRAGONSTEEL_FIRE_ARMOR_MATERIAL, 3, EquipmentSlot.FEET));
    public static final RegistryObject<Item> DRAGONSTEEL_ICE_INGOT = ITEMS.register("dragonsteel_ice_ingot", ItemGeneric::new);
    public static final RegistryObject<Item> DRAGONSTEEL_ICE_SWORD = ITEMS.register("dragonsteel_ice_sword", () -> new ItemModSword(DRAGONSTEEL_TIER_ICE));
    public static final RegistryObject<Item> DRAGONSTEEL_ICE_PICKAXE = ITEMS.register("dragonsteel_ice_pickaxe", () -> new ItemModPickaxe(DRAGONSTEEL_TIER_ICE));
    public static final RegistryObject<Item> DRAGONSTEEL_ICE_AXE = ITEMS.register("dragonsteel_ice_axe", () -> new ItemModAxe(DRAGONSTEEL_TIER_ICE));
    public static final RegistryObject<Item> DRAGONSTEEL_ICE_SHOVEL = ITEMS.register("dragonsteel_ice_shovel", () -> new ItemModShovel(DRAGONSTEEL_TIER_ICE));
    public static final RegistryObject<Item> DRAGONSTEEL_ICE_HOE = ITEMS.register("dragonsteel_ice_hoe", () -> new ItemModHoe(DRAGONSTEEL_TIER_ICE));
    public static final RegistryObject<Item> DRAGONSTEEL_ICE_HELMET = ITEMS.register("dragonsteel_ice_helmet", () -> new ItemDragonsteelArmor(DRAGONSTEEL_ICE_ARMOR_MATERIAL, 0, EquipmentSlot.HEAD));
    public static final RegistryObject<Item> DRAGONSTEEL_ICE_CHESTPLATE = ITEMS.register("dragonsteel_ice_chestplate", () -> new ItemDragonsteelArmor(DRAGONSTEEL_ICE_ARMOR_MATERIAL, 1, EquipmentSlot.CHEST));
    public static final RegistryObject<Item> DRAGONSTEEL_ICE_LEGGINGS = ITEMS.register("dragonsteel_ice_leggings", () -> new ItemDragonsteelArmor(DRAGONSTEEL_ICE_ARMOR_MATERIAL, 2, EquipmentSlot.LEGS));
    public static final RegistryObject<Item> DRAGONSTEEL_ICE_BOOTS = ITEMS.register("dragonsteel_ice_boots", () -> new ItemDragonsteelArmor(DRAGONSTEEL_ICE_ARMOR_MATERIAL, 3, EquipmentSlot.FEET));

    public static final RegistryObject<Item> DRAGONSTEEL_LIGHTNING_INGOT = ITEMS.register("dragonsteel_lightning_ingot", ItemGeneric::new);
    public static final RegistryObject<Item> DRAGONSTEEL_LIGHTNING_SWORD = ITEMS.register("dragonsteel_lightning_sword", () -> new ItemModSword(DRAGONSTEEL_TIER_LIGHTNING));
    public static final RegistryObject<Item> DRAGONSTEEL_LIGHTNING_PICKAXE = ITEMS.register("dragonsteel_lightning_pickaxe", () -> new ItemModPickaxe(DRAGONSTEEL_TIER_LIGHTNING));
    public static final RegistryObject<Item> DRAGONSTEEL_LIGHTNING_AXE = ITEMS.register("dragonsteel_lightning_axe", () -> new ItemModAxe(DRAGONSTEEL_TIER_LIGHTNING));
    public static final RegistryObject<Item> DRAGONSTEEL_LIGHTNING_SHOVEL = ITEMS.register("dragonsteel_lightning_shovel", () -> new ItemModShovel(DRAGONSTEEL_TIER_LIGHTNING));
    public static final RegistryObject<Item> DRAGONSTEEL_LIGHTNING_HOE = ITEMS.register("dragonsteel_lightning_hoe", () -> new ItemModHoe(DRAGONSTEEL_TIER_LIGHTNING));
    public static final RegistryObject<Item> DRAGONSTEEL_LIGHTNING_HELMET = ITEMS.register("dragonsteel_lightning_helmet", () -> new ItemDragonsteelArmor(DRAGONSTEEL_LIGHTNING_ARMOR_MATERIAL, 0, EquipmentSlot.HEAD));
    public static final RegistryObject<Item> DRAGONSTEEL_LIGHTNING_CHESTPLATE = ITEMS.register("dragonsteel_lightning_chestplate", () -> new ItemDragonsteelArmor(DRAGONSTEEL_LIGHTNING_ARMOR_MATERIAL, 1, EquipmentSlot.CHEST));
    public static final RegistryObject<Item> DRAGONSTEEL_LIGHTNING_LEGGINGS = ITEMS.register("dragonsteel_lightning_leggings", () -> new ItemDragonsteelArmor(DRAGONSTEEL_LIGHTNING_ARMOR_MATERIAL, 2, EquipmentSlot.LEGS));
    public static final RegistryObject<Item> DRAGONSTEEL_LIGHTNING_BOOTS = ITEMS.register("dragonsteel_lightning_boots", () -> new ItemDragonsteelArmor(DRAGONSTEEL_LIGHTNING_ARMOR_MATERIAL, 3, EquipmentSlot.FEET));


    public static final RegistryObject<Item> WEEZER_BLUE_ALBUM = ITEMS.register("weezer_blue_album", () -> new ItemGeneric(1, true));
    public static final RegistryObject<Item> DRAGON_DEBUG_STICK = ITEMS.register("dragon_debug_stick", () -> new ItemGeneric(1, true));
    public static final RegistryObject<Item> DREAD_SWORD = ITEMS.register("dread_sword", () -> new ItemModSword(DREAD_SWORD_TOOL_MATERIAL));
    public static final RegistryObject<Item> DREAD_KNIGHT_SWORD = ITEMS.register("dread_knight_sword", () -> new ItemModSword(DREAD_KNIGHT_TOOL_MATERIAL));
    public static final RegistryObject<Item> LICH_STAFF = ITEMS.register("lich_staff", () -> new ItemLichStaff());
    public static final RegistryObject<Item> DREAD_QUEEN_SWORD = ITEMS.register("dread_queen_sword", () -> new ItemModSword(DRAGONSTEEL_TIER_DREAD_QUEEN));
    public static final RegistryObject<Item> DREAD_QUEEN_STAFF = ITEMS.register("dread_queen_staff", () -> new ItemDreadQueenStaff());
    public static final RegistryObject<Item> DREAD_SHARD = ITEMS.register("dread_shard", () -> new ItemGeneric(0));
    public static final RegistryObject<Item> DREAD_KEY = ITEMS.register("dread_key", () -> new ItemGeneric(0));
    public static final RegistryObject<Item> HYDRA_FANG = ITEMS.register("hydra_fang", () -> new ItemGeneric(0));
    public static final RegistryObject<Item> HYDRA_HEART = ITEMS.register("hydra_heart", () -> new ItemHydraHeart());
    public static final RegistryObject<Item> HYDRA_ARROW = ITEMS.register("hydra_arrow", () -> new ItemHydraArrow());
    public static final RegistryObject<Item> CANNOLI = ITEMS.register("cannoli", () -> new ItemCannoli());
    public static final RegistryObject<Item> ECTOPLASM = ITEMS.register("ectoplasm", ItemGeneric::new);
    public static final RegistryObject<Item> GHOST_INGOT = ITEMS.register("ghost_ingot", () -> new ItemGeneric(1));
    public static final RegistryObject<Item> GHOST_SWORD = ITEMS.register("ghost_sword", () -> new ItemGhostSword());

    public static final RegistryObject<BannerPatternItem> PATTERN_FIRE = ITEMS.register("banner_pattern_fire", () -> new BannerPatternItem(IafRecipeRegistry.PATTERN_FIRE, unstackable()));
    public static final RegistryObject<BannerPatternItem> PATTERN_ICE = ITEMS.register("banner_pattern_ice", () -> new BannerPatternItem(IafRecipeRegistry.PATTERN_ICE, unstackable()));
    public static final RegistryObject<BannerPatternItem> PATTERN_LIGHTNING = ITEMS.register("banner_pattern_lightning", () -> new BannerPatternItem(IafRecipeRegistry.PATTERN_LIGHTNING, unstackable()));
    public static final RegistryObject<BannerPatternItem> PATTERN_FIRE_HEAD = ITEMS.register("banner_pattern_fire_head", () -> new BannerPatternItem(IafRecipeRegistry.PATTERN_FIRE_HEAD, unstackable()));
    public static final RegistryObject<BannerPatternItem> PATTERN_ICE_HEAD = ITEMS.register("banner_pattern_ice_head", () -> new BannerPatternItem(IafRecipeRegistry.PATTERN_ICE_HEAD, unstackable()));
    public static final RegistryObject<BannerPatternItem> PATTERN_LIGHTNING_HEAD = ITEMS.register("banner_pattern_lightning_head", () -> new BannerPatternItem(IafRecipeRegistry.PATTERN_LIGHTNING_HEAD, unstackable()));
    public static final RegistryObject<BannerPatternItem> PATTERN_AMPHITHERE = ITEMS.register("banner_pattern_amphithere", () -> new BannerPatternItem(IafRecipeRegistry.PATTERN_AMPHITHERE, unstackable()));
    public static final RegistryObject<BannerPatternItem> PATTERN_BIRD = ITEMS.register("banner_pattern_bird", () -> new BannerPatternItem(IafRecipeRegistry.PATTERN_BIRD, unstackable()));
    public static final RegistryObject<BannerPatternItem> PATTERN_EYE = ITEMS.register("banner_pattern_eye", () -> new BannerPatternItem(IafRecipeRegistry.PATTERN_EYE, unstackable()));
    public static final RegistryObject<BannerPatternItem> PATTERN_FAE = ITEMS.register("banner_pattern_fae", () -> new BannerPatternItem(IafRecipeRegistry.PATTERN_FAE, unstackable()));
    public static final RegistryObject<BannerPatternItem> PATTERN_FEATHER = ITEMS.register("banner_pattern_feather", () -> new BannerPatternItem(IafRecipeRegistry.PATTERN_FEATHER, unstackable()));
    public static final RegistryObject<BannerPatternItem> PATTERN_GORGON = ITEMS.register("banner_pattern_gorgon", () -> new BannerPatternItem(IafRecipeRegistry.PATTERN_GORGON, unstackable()));
    public static final RegistryObject<BannerPatternItem> PATTERN_HIPPOCAMPUS = ITEMS.register("banner_pattern_hippocampus", () -> new BannerPatternItem(IafRecipeRegistry.PATTERN_HIPPOCAMPUS, unstackable()));
    public static final RegistryObject<BannerPatternItem> PATTERN_HIPPOGRYPH_HEAD = ITEMS.register("banner_pattern_hippogryph_head", () -> new BannerPatternItem(IafRecipeRegistry.PATTERN_HIPPOGRYPH_HEAD, unstackable()));
    public static final RegistryObject<BannerPatternItem> PATTERN_MERMAID = ITEMS.register("banner_pattern_mermaid", () -> new BannerPatternItem(IafRecipeRegistry.PATTERN_MERMAID, unstackable()));
    public static final RegistryObject<BannerPatternItem> PATTERN_SEA_SERPENT = ITEMS.register("banner_pattern_sea_serpent", () -> new BannerPatternItem(IafRecipeRegistry.PATTERN_SEA_SERPENT, unstackable()));
    public static final RegistryObject<BannerPatternItem> PATTERN_TROLL = ITEMS.register("banner_pattern_troll", () -> new BannerPatternItem(IafRecipeRegistry.PATTERN_TROLL, unstackable()));
    public static final RegistryObject<BannerPatternItem> PATTERN_WEEZER = ITEMS.register("banner_pattern_weezer", () -> new BannerPatternItem(IafRecipeRegistry.PATTERN_WEEZER, unstackable()));
    public static final RegistryObject<BannerPatternItem> PATTERN_DREAD = ITEMS.register("banner_pattern_dread", () -> new BannerPatternItem(IafRecipeRegistry.PATTERN_DREAD, unstackable()));

    static {
        EnumDragonArmor.initArmors();
        EnumSeaSerpent.initArmors();
        EnumSkullType.initItems();
        EnumTroll.initArmors();
    }

    public static Item.Properties defaultBuilder() {
        return new Item.Properties().tab(IceAndFire.TAB_ITEMS);
    }

    public static Item.Properties unstackable() {
        return defaultBuilder().stacksTo(1);
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void registerItems(RegistryEvent.Register<Item> event) {
        //spawn Eggs
        //@formatter:off
        ITEMS.register("spawn_egg_fire_dragon", () -> new ForgeSpawnEggItem(IafEntityRegistry.FIRE_DRAGON, 0X340000, 0XA52929, new Item.Properties().tab(IceAndFire.TAB_ITEMS)));
        ITEMS.register("spawn_egg_ice_dragon", () -> new ForgeSpawnEggItem(IafEntityRegistry.ICE_DRAGON, 0XB5DDFB, 0X7EBAF0, new Item.Properties().tab(IceAndFire.TAB_ITEMS)));
        ITEMS.register("spawn_egg_lightning_dragon", () -> new ForgeSpawnEggItem(IafEntityRegistry.LIGHTNING_DRAGON, 0X422367, 0X725691, new Item.Properties().tab(IceAndFire.TAB_ITEMS)));
        ITEMS.register("spawn_egg_hippogryph", () -> new ForgeSpawnEggItem(IafEntityRegistry.HIPPOGRYPH, 0XD8D8D8, 0XD1B55D, new Item.Properties().tab(IceAndFire.TAB_ITEMS)));
        ITEMS.register("spawn_egg_gorgon", () -> new ForgeSpawnEggItem(IafEntityRegistry.GORGON, 0XD0D99F, 0X684530, new Item.Properties().tab(IceAndFire.TAB_ITEMS)));
        ITEMS.register("spawn_egg_pixie", () -> new ForgeSpawnEggItem(IafEntityRegistry.PIXIE, 0XFF7F89, 0XE2CCE2, new Item.Properties().tab(IceAndFire.TAB_ITEMS)));
        ITEMS.register("spawn_egg_cyclops", () -> new ForgeSpawnEggItem(IafEntityRegistry.CYCLOPS, 0XB0826E, 0X3A1F0F, new Item.Properties().tab(IceAndFire.TAB_ITEMS)));
        ITEMS.register("spawn_egg_siren", () -> new ForgeSpawnEggItem(IafEntityRegistry.SIREN, 0X8EE6CA, 0XF2DFC8, new Item.Properties().tab(IceAndFire.TAB_ITEMS)));
        ITEMS.register("spawn_egg_hippocampus", () -> new ForgeSpawnEggItem(IafEntityRegistry.HIPPOCAMPUS, 0X4491C7, 0X4FC56B, new Item.Properties().tab(IceAndFire.TAB_ITEMS)));
        ITEMS.register("spawn_egg_death_worm", () -> new ForgeSpawnEggItem(IafEntityRegistry.DEATH_WORM, 0XD1CDA3, 0X423A3A, new Item.Properties().tab(IceAndFire.TAB_ITEMS)));
        ITEMS.register("spawn_egg_cockatrice", () -> new ForgeSpawnEggItem(IafEntityRegistry.COCKATRICE, 0X8F5005, 0X4F5A23, new Item.Properties().tab(IceAndFire.TAB_ITEMS)));
        ITEMS.register("spawn_egg_stymphalian_bird", () -> new ForgeSpawnEggItem(IafEntityRegistry.STYMPHALIAN_BIRD, 0X744F37, 0X9E6C4B, new Item.Properties().tab(IceAndFire.TAB_ITEMS)));
        ITEMS.register("spawn_egg_troll", () -> new ForgeSpawnEggItem(IafEntityRegistry.TROLL, 0X3D413D, 0X58433A, new Item.Properties().tab(IceAndFire.TAB_ITEMS)));
        ITEMS.register("spawn_egg_myrmex_worker", () -> new ForgeSpawnEggItem(IafEntityRegistry.MYRMEX_WORKER, 0XA16026, 0X594520, new Item.Properties().tab(IceAndFire.TAB_ITEMS)));
        ITEMS.register("spawn_egg_myrmex_soldier", () -> new ForgeSpawnEggItem(IafEntityRegistry.MYRMEX_SOLDIER, 0XA16026, 0X7D622D, new Item.Properties().tab(IceAndFire.TAB_ITEMS)));
        ITEMS.register("spawn_egg_myrmex_sentinel", () -> new ForgeSpawnEggItem(IafEntityRegistry.MYRMEX_SENTINEL, 0XA16026, 0XA27F3A, new Item.Properties().tab(IceAndFire.TAB_ITEMS)));
        ITEMS.register("spawn_egg_myrmex_royal", () -> new ForgeSpawnEggItem(IafEntityRegistry.MYRMEX_ROYAL, 0XA16026, 0XC79B48, new Item.Properties().tab(IceAndFire.TAB_ITEMS)));
        ITEMS.register("spawn_egg_myrmex_queen", () -> new ForgeSpawnEggItem(IafEntityRegistry.MYRMEX_QUEEN, 0XA16026, 0XECB855, new Item.Properties().tab(IceAndFire.TAB_ITEMS)));
        ITEMS.register("spawn_egg_amphithere", () -> new ForgeSpawnEggItem(IafEntityRegistry.AMPHITHERE, 0X597535, 0X00AA98, new Item.Properties().tab(IceAndFire.TAB_ITEMS)));
        ITEMS.register("spawn_egg_sea_serpent", () -> new ForgeSpawnEggItem(IafEntityRegistry.SEA_SERPENT, 0X008299, 0XC5E6E7, new Item.Properties().tab(IceAndFire.TAB_ITEMS)));
        ITEMS.register("spawn_egg_dread_thrall", () -> new ForgeSpawnEggItem(IafEntityRegistry.DREAD_THRALL, 0XE0E6E6, 0X00FFFF, new Item.Properties().tab(IceAndFire.TAB_ITEMS)));
        ITEMS.register("spawn_egg_dread_ghoul", () -> new ForgeSpawnEggItem(IafEntityRegistry.DREAD_GHOUL, 0XE0E6E6, 0X7B838A, new Item.Properties().tab(IceAndFire.TAB_ITEMS)));
        ITEMS.register("spawn_egg_dread_beast", () -> new ForgeSpawnEggItem(IafEntityRegistry.DREAD_BEAST, 0XE0E6E6, 0X38373C, new Item.Properties().tab(IceAndFire.TAB_ITEMS)));
        ITEMS.register("spawn_egg_dread_scuttler", () -> new ForgeSpawnEggItem(IafEntityRegistry.DREAD_SCUTTLER, 0XE0E6E6, 0X4D5667, new Item.Properties().tab(IceAndFire.TAB_ITEMS)));
        ITEMS.register("spawn_egg_lich", () -> new ForgeSpawnEggItem(IafEntityRegistry.DREAD_LICH, 0XE0E6E6, 0X274860, new Item.Properties().tab(IceAndFire.TAB_ITEMS)));
        ITEMS.register("spawn_egg_dread_knight", () -> new ForgeSpawnEggItem(IafEntityRegistry.DREAD_KNIGHT, 0XE0E6E6, 0X4A6C6E, new Item.Properties().tab(IceAndFire.TAB_ITEMS)));
        ITEMS.register("spawn_egg_dread_horse", () -> new ForgeSpawnEggItem(IafEntityRegistry.DREAD_HORSE, 0XE0E6E6, 0XACACAC, new Item.Properties().tab(IceAndFire.TAB_ITEMS)));
        ITEMS.register("spawn_egg_hydra", () -> new ForgeSpawnEggItem(IafEntityRegistry.HYDRA, 0X8B8B78, 0X2E372B, new Item.Properties().tab(IceAndFire.TAB_ITEMS)));
        ITEMS.register("spawn_egg_ghost", () -> new ForgeSpawnEggItem(IafEntityRegistry.GHOST, 0XB9EDB8, 0X73B276, new Item.Properties().tab(IceAndFire.TAB_ITEMS)));
    }

    /**
     Set repair materials etc.
    */
    @SubscribeEvent( priority = EventPriority.LOW)
    public static void setRepairMaterials(final RegistryEvent.Register<Item> event) {

        IafItemRegistry.BLINDFOLD_ARMOR_MATERIAL.setRepairMaterial(Ingredient.of(new ItemStack(Items.STRING)));
        IafItemRegistry.SILVER_ARMOR_MATERIAL.setRepairMaterial(Ingredient.of(new ItemStack(IafItemRegistry.SILVER_INGOT.get())));
        IafItemRegistry.SILVER_TOOL_MATERIAL.setRepairMaterial(Ingredient.of(new ItemStack(IafItemRegistry.SILVER_INGOT.get())));
        IafItemRegistry.DRAGONBONE_TOOL_MATERIAL.setRepairMaterial(Ingredient.of(new ItemStack(IafItemRegistry.DRAGON_BONE.get())));
        IafItemRegistry.FIRE_DRAGONBONE_TOOL_MATERIAL.setRepairMaterial(Ingredient.of(new ItemStack(IafItemRegistry.DRAGON_BONE.get())));
        IafItemRegistry.ICE_DRAGONBONE_TOOL_MATERIAL.setRepairMaterial(Ingredient.of(new ItemStack(IafItemRegistry.DRAGON_BONE.get())));
        for (EnumDragonArmor armor : EnumDragonArmor.values()) {
            armor.armorMaterial.setRepairMaterial(Ingredient.of(new ItemStack(EnumDragonArmor.getScaleItem(armor))));
        }
        IafItemRegistry.DRAGONSTEEL_FIRE_ARMOR_MATERIAL.setRepairMaterial(Ingredient.of(new ItemStack(IafItemRegistry.DRAGONSTEEL_FIRE_INGOT.get())));
        IafItemRegistry.DRAGONSTEEL_ICE_ARMOR_MATERIAL.setRepairMaterial(Ingredient.of(new ItemStack(IafItemRegistry.DRAGONSTEEL_ICE_INGOT.get())));
        IafItemRegistry.DRAGONSTEEL_LIGHTNING_ARMOR_MATERIAL.setRepairMaterial(Ingredient.of(new ItemStack(IafItemRegistry.DRAGONSTEEL_LIGHTNING_INGOT.get())));
        IafItemRegistry.SHEEP_ARMOR_MATERIAL.setRepairMaterial(Ingredient.of(new ItemStack(Blocks.WHITE_WOOL)));
        IafItemRegistry.EARPLUGS_ARMOR_MATERIAL.setRepairMaterial(Ingredient.of(new ItemStack(Blocks.OAK_BUTTON)));
        IafItemRegistry.DEATHWORM_0_ARMOR_MATERIAL.setRepairMaterial(Ingredient.of(new ItemStack(IafItemRegistry.DEATH_WORM_CHITIN_YELLOW.get())));
        IafItemRegistry.DEATHWORM_1_ARMOR_MATERIAL.setRepairMaterial(Ingredient.of(new ItemStack(IafItemRegistry.DEATH_WORM_CHITIN_RED.get())));
        IafItemRegistry.DEATHWORM_2_ARMOR_MATERIAL.setRepairMaterial(Ingredient.of(new ItemStack(IafItemRegistry.DEATH_WORM_CHITIN_WHITE.get())));
        IafItemRegistry.TROLL_WEAPON_TOOL_MATERIAL.setRepairMaterial(Ingredient.of(new ItemStack(Blocks.STONE)));
        IafItemRegistry.TROLL_MOUNTAIN_ARMOR_MATERIAL.setRepairMaterial(Ingredient.of(new ItemStack(EnumTroll.MOUNTAIN.leather.get())));
        IafItemRegistry.TROLL_FOREST_ARMOR_MATERIAL.setRepairMaterial(Ingredient.of(new ItemStack(EnumTroll.FOREST.leather.get())));
        IafItemRegistry.TROLL_FROST_ARMOR_MATERIAL.setRepairMaterial(Ingredient.of(new ItemStack(EnumTroll.FROST.leather.get())));
        IafItemRegistry.HIPPOGRYPH_SWORD_TOOL_MATERIAL.setRepairMaterial(Ingredient.of(new ItemStack(IafItemRegistry.HIPPOGRYPH_TALON.get())));
        IafItemRegistry.HIPPOCAMPUS_SWORD_TOOL_MATERIAL.setRepairMaterial(Ingredient.of(new ItemStack(IafItemRegistry.SHINY_SCALES.get())));
        IafItemRegistry.AMPHITHERE_SWORD_TOOL_MATERIAL.setRepairMaterial(Ingredient.of(new ItemStack(IafItemRegistry.AMPHITHERE_FEATHER.get())));
        IafItemRegistry.STYMHALIAN_SWORD_TOOL_MATERIAL.setRepairMaterial(Ingredient.of(new ItemStack(IafItemRegistry.STYMPHALIAN_BIRD_FEATHER.get())));
        IafItemRegistry.MYRMEX_CHITIN_TOOL_MATERIAL.setRepairMaterial(Ingredient.of(new ItemStack(IafItemRegistry.MYRMEX_DESERT_CHITIN.get())));
        IafItemRegistry.MYRMEX_DESERT_ARMOR_MATERIAL.setRepairMaterial(Ingredient.of(new ItemStack(IafItemRegistry.MYRMEX_DESERT_CHITIN.get())));
        IafItemRegistry.MYRMEX_JUNGLE_ARMOR_MATERIAL.setRepairMaterial(Ingredient.of(new ItemStack(IafItemRegistry.MYRMEX_JUNGLE_CHITIN.get())));
        IafItemRegistry.DREAD_SWORD_TOOL_MATERIAL.setRepairMaterial(Ingredient.of(new ItemStack(IafItemRegistry.DREAD_SHARD.get())));
        IafItemRegistry.DREAD_KNIGHT_TOOL_MATERIAL.setRepairMaterial(Ingredient.of(new ItemStack(IafItemRegistry.DREAD_SHARD.get())));

        for (EnumSeaSerpent serpent : EnumSeaSerpent.values()) {
            serpent.armorMaterial.setRepairMaterial(Ingredient.of(new ItemStack(serpent.scale.get())));
        }
    }
}
