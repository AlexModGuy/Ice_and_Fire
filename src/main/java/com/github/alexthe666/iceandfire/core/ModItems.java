package com.github.alexthe666.iceandfire.core;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.enums.EnumDragonArmor;
import com.github.alexthe666.iceandfire.enums.EnumDragonEgg;
import com.github.alexthe666.iceandfire.item.*;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModItems {

	public static ArmorMaterial silverMetal = EnumHelper.addArmorMaterial("Silver", "iceandfire:armor_silver_metal", 15, new int[]{1, 4, 5, 2}, 20, SoundEvents.ITEM_ARMOR_EQUIP_CHAIN, 0);
	public static ArmorMaterial dragon = EnumHelper.addArmorMaterial("DragonScales", "iceandfire:armor_dragon_scales", 36, new int[]{4, 7, 9, 4}, 15, SoundEvents.ITEM_ARMOR_EQUIP_CHAIN, 2);
	public static ArmorMaterial blindfoldArmor = EnumHelper.addArmorMaterial("Blindfold", "iceandfire:blindfold", 5, new int[]{1, 1, 1, 1}, 10, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0);
	public static ArmorMaterial sheep = EnumHelper.addArmorMaterial("Sheep", "iceandfire:sheep_disguise", 5, new int[]{1, 2, 3, 1}, 15, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0);
	public static ArmorMaterial earplugsArmor = EnumHelper.addArmorMaterial("Earplugs", "iceandfire:earplugs", 5, new int[]{1, 1, 1, 1}, 10, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0);
	public static ArmorMaterial yellow_deathworm = EnumHelper.addArmorMaterial("Yellow Deathworm", "iceandfire:yellow_deathworm", 15, new int[]{2, 5, 7, 3}, 5, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 1.5F);
	public static ArmorMaterial white_deathworm = EnumHelper.addArmorMaterial("White Deathworm", "iceandfire:white_deathworm", 15, new int[]{2, 5, 7, 3}, 5, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 1.5F);
	public static ArmorMaterial red_deathworm = EnumHelper.addArmorMaterial("Red Deathworm", "iceandfire:red_deathworm", 15, new int[]{2, 5, 7, 3}, 5, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 1.5F);
	public static ToolMaterial silverTools = EnumHelper.addToolMaterial("Silver", 2, 460, 11.0F, 1.0F, 18);
	public static ToolMaterial boneTools = EnumHelper.addToolMaterial("Dragonbone", 4, 1660, 10.0F, 4.0F, 22);
	public static ToolMaterial fireBoneTools = EnumHelper.addToolMaterial("FireDragonbone", 4, 2000, 10.0F, 5.5F, 22);
	public static ToolMaterial iceBoneTools = EnumHelper.addToolMaterial("IceDragonbone", 4, 2000, 10.0F, 5.5F, 22);

	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":bestiary")
	public static Item bestiary = new ItemBestiary();
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":manuscript")
	public static Item manuscript = new ItemGeneric("manuscript", "iceandfire.manuscript");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":sapphire_gem")
	public static Item sapphireGem = new ItemGeneric("sapphire_gem", "iceandfire.sapphireGem");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":silver_ingot")
	public static Item silverIngot = new ItemGeneric("silver_ingot", "iceandfire.silverIngot");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":silver_nugget")
	public static Item silverNugget = new ItemGeneric("silver_nugget", "iceandfire.silverNugget");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":silver_helmet")
	public static Item silver_helmet = new ItemModArmor(silverMetal, 0, EntityEquipmentSlot.HEAD, "armor_silver_metal_helmet", "iceandfire.silver_helmet");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":silver_chestplate")
	public static Item silver_chestplate = new ItemModArmor(silverMetal, 1, EntityEquipmentSlot.CHEST, "armor_silver_metal_chestplate", "iceandfire.silver_chestplate");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":silver_leggings")
	public static Item silver_leggings = new ItemModArmor(silverMetal, 2, EntityEquipmentSlot.LEGS, "armor_silver_metal_leggings", "iceandfire.silver_leggings");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":silver_boots")
	public static Item silver_boots = new ItemModArmor(silverMetal, 3, EntityEquipmentSlot.FEET, "armor_silver_metal_boots", "iceandfire.silver_boots");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":silver_sword")
	public static Item silver_sword = new ItemModSword(silverTools, "silver_sword", "iceandfire.silver_sword");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":silver_shovel")
	public static Item silver_shovel = new ItemModShovel(silverTools, "silver_shovel", "iceandfire.silver_shovel");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":silver_pickaxe")
	public static Item silver_pickaxe = new ItemModPickaxe(silverTools, "silver_pickaxe", "iceandfire.silver_pickaxe");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":silver_axe")
	public static Item silver_axe = new ItemModAxe(silverTools, "silver_axe", "iceandfire.silver_axe");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":silver_hoe")
	public static Item silver_hoe = new ItemModHoe(silverTools, "silver_hoe", "iceandfire.silver_hoe");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":fire_stew")
	public static Item fire_stew = new ItemGeneric("fire_stew", "iceandfire.fire_stew");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":frost_stew")
	public static Item frost_stew = new ItemGeneric("frost_stew", "iceandfire.frost_stew");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":dragonegg_red")
	public static Item dragonegg_red = new ItemDragonEgg("dragonegg_red", EnumDragonEgg.RED);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":dragonegg_green")
	public static Item dragonegg_green = new ItemDragonEgg("dragonegg_green", EnumDragonEgg.GREEN);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":dragonegg_bronze")
	public static Item dragonegg_bronze = new ItemDragonEgg("dragonegg_bronze", EnumDragonEgg.BRONZE);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":dragonegg_gray")
	public static Item dragonegg_gray = new ItemDragonEgg("dragonegg_gray", EnumDragonEgg.GRAY);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":dragonegg_blue")
	public static Item dragonegg_blue = new ItemDragonEgg("dragonegg_blue", EnumDragonEgg.BLUE);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":dragonegg_white")
	public static Item dragonegg_white = new ItemDragonEgg("dragonegg_white", EnumDragonEgg.WHITE);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":dragonegg_sapphire")
	public static Item dragonegg_sapphire = new ItemDragonEgg("dragonegg_sapphire", EnumDragonEgg.SAPPHIRE);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":dragonegg_silver")
	public static Item dragonegg_silver = new ItemDragonEgg("dragonegg_silver", EnumDragonEgg.SILVER);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":dragonscales_red")
	public static Item dragonscales_red = new ItemDragonScales("dragonscales_red", EnumDragonEgg.RED);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":dragonscales_green")
	public static Item dragonscales_green = new ItemDragonScales("dragonscales_green", EnumDragonEgg.GREEN);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":dragonscales_bronze")
	public static Item dragonscales_bronze = new ItemDragonScales("dragonscales_bronze", EnumDragonEgg.BRONZE);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":dragonscales_gray")
	public static Item dragonscales_gray = new ItemDragonScales("dragonscales_gray", EnumDragonEgg.GRAY);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":dragonscales_blue")
	public static Item dragonscales_blue = new ItemDragonScales("dragonscales_blue", EnumDragonEgg.BLUE);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":dragonscales_white")
	public static Item dragonscales_white = new ItemDragonScales("dragonscales_white", EnumDragonEgg.WHITE);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":dragonscales_sapphire")
	public static Item dragonscales_sapphire = new ItemDragonScales("dragonscales_sapphire", EnumDragonEgg.SAPPHIRE);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":dragonscales_silver")
	public static Item dragonscales_silver = new ItemDragonScales("dragonscales_silver", EnumDragonEgg.SILVER);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":dragonbone")
	public static Item dragonbone = new ItemDragonBone();
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":witherbone")
	public static Item witherbone = new ItemGeneric("witherbone", "iceandfire.witherbone");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":fishing_spear")
	public static Item fishing_spear = new ItemFishingSpear();
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":wither_shard")
	public static Item wither_shard = new ItemGeneric("wither_shard", "iceandfire.wither_shard");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":dragonbone_sword")
	public static Item dragonbone_sword = new ItemModSword(boneTools, "dragonbone_sword", "iceandfire.dragonbone_sword");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":dragonbone_shovel")
	public static Item dragonbone_shovel = new ItemModShovel(boneTools, "dragonbone_shovel", "iceandfire.dragonbone_shovel");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":dragonbone_pickaxe")
	public static Item dragonbone_pickaxe = new ItemModPickaxe(boneTools, "dragonbone_pickaxe", "iceandfire.dragonbone_pickaxe");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":dragonbone_axe")
	public static Item dragonbone_axe = new ItemModAxe(boneTools, "dragonbone_axe", "iceandfire.dragonbone_axe");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":dragonbone_hoe")
	public static Item dragonbone_hoe = new ItemModHoe(boneTools, "dragonbone_hoe", "iceandfire.dragonbone_hoe");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":dragonbone_sword_fire")
	public static Item dragonbone_sword_fire = new ItemAlchemySword(fireBoneTools, "dragonbone_sword_fire", "iceandfire.dragonbone_sword_fire");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":dragonbone_sword_ice")
	public static Item dragonbone_sword_ice = new ItemAlchemySword(iceBoneTools, "dragonbone_sword_ice", "iceandfire.dragonbone_sword_ice");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":dragonbone_arrow")
	public static Item dragonbone_arrow = new ItemGeneric("dragonbone_arrow", "iceandfire.dragonbone_arrow");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":dragonbone_bow")
	public static Item dragonbone_bow = new ItemDragonBow();
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":dragon_skull")
	public static Item dragon_skull = new ItemDragonSkull();
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":dragonarmor_iron")
	public static Item dragon_armor_iron = new ItemDragonArmor(0, "dragonarmor_iron");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":dragonarmor_gold")
	public static Item dragon_armor_gold = new ItemDragonArmor(1, "dragonarmor_gold");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":dragonarmor_diamond")
	public static Item dragon_armor_diamond = new ItemDragonArmor(2, "dragonarmor_diamond");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":dragon_meal")
	public static Item dragon_meal = new ItemGeneric("dragon_meal", "iceandfire.dragon_meal");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":fire_dragon_flesh")
	public static Item fire_dragon_flesh = new ItemDragonFlesh(true);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":ice_dragon_flesh")
	public static Item ice_dragon_flesh = new ItemDragonFlesh(false);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":fire_dragon_heart")
	public static Item fire_dragon_heart = new ItemGeneric("fire_dragon_heart", "iceandfire.fire_dragon_heart");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":ice_dragon_heart")
	public static Item ice_dragon_heart = new ItemGeneric("ice_dragon_heart", "iceandfire.ice_dragon_heart");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":fire_dragon_blood")
	public static Item fire_dragon_blood = new ItemGeneric("fire_dragon_blood", "iceandfire.fire_dragon_blood");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":ice_dragon_blood")
	public static Item ice_dragon_blood = new ItemGeneric("ice_dragon_blood", "iceandfire.ice_dragon_blood");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":dragon_stick")
	public static Item dragon_stick = new ItemGeneric("dragon_stick", "iceandfire.dragon_stick");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":dragon_horn")
	public static Item dragon_horn = new ItemDragonHornStatic();
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":dragon_horn_fire")
	public static Item dragon_horn_fire = new ItemDragonHornActive("dragon_horn_fire");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":dragon_horn_ice")
	public static Item dragon_horn_ice = new ItemDragonHornActive("dragon_horn_ice");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":dragon_flute")
	public static Item dragon_flute = new ItemDragonFlute();
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":hippogryph_egg")
	public static Item hippogryph_egg = new ItemHippogryphEgg();
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":iron_hippogryph_armor")
	public static Item iron_hippogryph_armor = new ItemGeneric("iron_hippogryph_armor", "iceandfire.iron_hippogryph_armor").setMaxStackSize(1);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":gold_hippogryph_armor")
	public static Item gold_hippogryph_armor = new ItemGeneric("gold_hippogryph_armor", "iceandfire.gold_hippogryph_armor").setMaxStackSize(1);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":diamond_hippogryph_armor")
	public static Item diamond_hippogryph_armor = new ItemGeneric("diamond_hippogryph_armor", "iceandfire.diamond_hippogryph_armor").setMaxStackSize(1);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":gorgon_head")
	public static Item gorgon_head = new ItemGorgonHead();
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":stone_statue")
	public static Item stone_statue = new ItemStoneStatue();
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":blindfold")
	public static Item blindfold = new ItemBlindfold();
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":pixie_dust")
	public static Item pixie_dust = new ItemPixieDust();
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":ambrosia")
	public static Item ambrosia = new ItemAmbrosia();
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":sheep_helmet")
	public static Item sheep_helmet = new ItemModArmor(sheep, 0, EntityEquipmentSlot.HEAD, "sheep_helmet", "iceandfire.sheep_helmet");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":sheep_chestplate")
	public static Item sheep_chestplate = new ItemModArmor(sheep, 1, EntityEquipmentSlot.CHEST, "sheep_chestplate", "iceandfire.sheep_chestplate");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":sheep_leggings")
	public static Item sheep_leggings = new ItemModArmor(sheep, 2, EntityEquipmentSlot.LEGS, "sheep_leggings", "iceandfire.sheep_leggings");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":sheep_boots")
	public static Item sheep_boots = new ItemModArmor(sheep, 3, EntityEquipmentSlot.FEET, "sheep_boots", "iceandfire.sheep_boots");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":shiny_scales")
	public static Item shiny_scales = new ItemGeneric("shiny_scales", "iceandfire.shiny_scales");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":earplugs")
	public static Item earplugs = new ItemModArmor(earplugsArmor, 0, EntityEquipmentSlot.HEAD, "earplugs", "iceandfire.earplugs");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":deathworm_chitin")
	public static Item deathworm_chitin = new ItemDeathWormChitin();
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":deathworm_yellow_helmet")
	public static Item deathworm_yellow_helmet = new ItemDeathwormArmor(yellow_deathworm, 0, EntityEquipmentSlot.HEAD, "deathworm_yellow_helmet", "iceandfire.deathworm_yellow_helmet");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":deathworm_yellow_chestplate")
	public static Item deathworm_yellow_chestplate = new ItemDeathwormArmor(yellow_deathworm, 1, EntityEquipmentSlot.CHEST, "deathworm_yellow_chestplate", "iceandfire.deathworm_yellow_chestplate");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":deathworm_yellow_leggings")
	public static Item deathworm_yellow_leggings = new ItemDeathwormArmor(yellow_deathworm, 2, EntityEquipmentSlot.LEGS, "deathworm_yellow_leggings", "iceandfire.deathworm_yellow_leggings");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":deathworm_yellow_boots")
	public static Item deathworm_yellow_boots = new ItemDeathwormArmor(yellow_deathworm, 3, EntityEquipmentSlot.FEET, "deathworm_yellow_boots", "iceandfire.deathworm_yellow_boots");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":deatite_helmet")
	public static Item deathworm_white_helmet = new ItemDeathwormArmor(white_deathworm, 0, EntityEquipmentSlot.HEAD, "deathworm_white_helmet", "iceandfire.deathworm_white_helmet");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":deathworm_white_chestplate")
	public static Item deathworm_white_chestplate = new ItemDeathwormArmor(white_deathworm, 1, EntityEquipmentSlot.CHEST, "deathworm_white_chestplate", "iceandfire.deathworm_white_chestplate");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":deathworm_white_leggings")
	public static Item deathworm_white_leggings = new ItemDeathwormArmor(white_deathworm, 2, EntityEquipmentSlot.LEGS, "deathworm_white_leggings", "iceandfire.deathworm_white_leggings");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":deathworm_white_boots")
	public static Item deathworm_white_boots = new ItemDeathwormArmor(white_deathworm, 3, EntityEquipmentSlot.FEET, "deathworm_white_boots", "iceandfire.deathworm_white_boots");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":deathworm_red_helmet")
	public static Item deathworm_red_helmet = new ItemDeathwormArmor(red_deathworm, 0, EntityEquipmentSlot.HEAD, "deathworm_red_helmet", "iceandfire.deathworm_red_helmet");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":deathworm_red_chworm_whhestplate")
	public static Item deathworm_red_chestplate = new ItemDeathwormArmor(red_deathworm, 1, EntityEquipmentSlot.CHEST, "deathworm_red_chestplate", "iceandfire.deathworm_red_chestplate");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":deathworm_red_leggings")
	public static Item deathworm_red_leggings = new ItemDeathwormArmor(red_deathworm, 2, EntityEquipmentSlot.LEGS, "deathworm_red_leggings", "iceandfire.deathworm_red_leggings");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":deathworm_red_boots")
	public static Item deathworm_red_boots = new ItemDeathwormArmor(red_deathworm, 3, EntityEquipmentSlot.FEET, "deathworm_red_boots", "iceandfire.deathworm_red_boots");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":deathworm_egg")
	public static Item deathworm_egg = new ItemDeathwormEgg();
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":rotten_egg")
	public static Item rotten_egg = new ItemRottenEgg();

	static {
		EnumDragonArmor.initArmors();
	}
}