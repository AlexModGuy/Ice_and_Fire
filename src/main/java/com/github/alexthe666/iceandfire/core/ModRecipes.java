package com.github.alexthe666.iceandfire.core;

import com.github.alexthe666.iceandfire.enums.EnumDragonArmor;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.BannerPattern;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class ModRecipes {

	public static void init() {
		OreDictionary.registerOre("ingotSilver", ModItems.silverIngot);
		OreDictionary.registerOre("nuggetSilver", ModItems.silverNugget);
		OreDictionary.registerOre("oreSilver", ModBlocks.silverOre);
		OreDictionary.registerOre("blockSilver", ModBlocks.silverBlock);
		OreDictionary.registerOre("gemSapphire", ModItems.sapphireGem);
		OreDictionary.registerOre("oreSapphire", ModBlocks.sapphireOre);
		OreDictionary.registerOre("blockSilver", ModBlocks.silverBlock);
		OreDictionary.registerOre("boneWither", ModItems.witherbone);
		OreDictionary.registerOre("foodMeat", Items.CHICKEN);
		OreDictionary.registerOre("foodMeat", Items.COOKED_CHICKEN);
		OreDictionary.registerOre("foodMeat", Items.BEEF);
		OreDictionary.registerOre("foodMeat", Items.COOKED_BEEF);
		OreDictionary.registerOre("foodMeat", Items.PORKCHOP);
		OreDictionary.registerOre("foodMeat", Items.COOKED_PORKCHOP);
		OreDictionary.registerOre("foodMeat", Items.MUTTON);
		OreDictionary.registerOre("foodMeat", Items.COOKED_MUTTON);
		OreDictionary.registerOre("foodMeat", Items.RABBIT);
		OreDictionary.registerOre("foodMeat", Items.COOKED_RABBIT);

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.dragon_horn, 1, 0), new Object[]{"  Y", " YY", "XY ", Character.valueOf('Y'), ModItems.dragonbone, Character.valueOf('X'), "stickWood"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.podium, 1, 0), new Object[]{"XYX", " Y ", "XYX", Character.valueOf('X'), new ItemStack(Blocks.WOODEN_SLAB, 1, 0), Character.valueOf('Y'), new ItemStack(Blocks.PLANKS, 1, 0)}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.podium, 1, 1), new Object[]{"XYX", " Y ", "XYX", Character.valueOf('X'), new ItemStack(Blocks.WOODEN_SLAB, 1, 1), Character.valueOf('Y'), new ItemStack(Blocks.PLANKS, 1, 1)}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.podium, 1, 2), new Object[]{"XYX", " Y ", "XYX", Character.valueOf('X'), new ItemStack(Blocks.WOODEN_SLAB, 1, 2), Character.valueOf('Y'), new ItemStack(Blocks.PLANKS, 1, 2)}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.podium, 1, 3), new Object[]{"XYX", " Y ", "XYX", Character.valueOf('X'), new ItemStack(Blocks.WOODEN_SLAB, 1, 3), Character.valueOf('Y'), new ItemStack(Blocks.PLANKS, 1, 3)}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.podium, 1, 4), new Object[]{"XYX", " Y ", "XYX", Character.valueOf('X'), new ItemStack(Blocks.WOODEN_SLAB, 1, 4), Character.valueOf('Y'), new ItemStack(Blocks.PLANKS, 1, 4)}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.podium, 1, 5), new Object[]{"XYX", " Y ", "XYX", Character.valueOf('X'), new ItemStack(Blocks.WOODEN_SLAB, 1, 5), Character.valueOf('Y'), new ItemStack(Blocks.PLANKS, 1, 5)}));
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.fire_stew, 1, 0), Items.BOWL, ModBlocks.fire_lily, Items.BLAZE_ROD);
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.frost_stew, 1, 0), Items.BOWL, ModBlocks.frost_lily, Items.PRISMARINE_CRYSTALS);
		GameRegistry.addRecipe(new ItemStack(ModItems.dragonbone_arrow, 5, 0), new Object[]{"X", "Y", "Z", Character.valueOf('X'), ModItems.wither_shard, Character.valueOf('Y'), ModItems.dragonbone});
		addBanner("firedragon", new ItemStack(ModItems.dragon_skull, 1, 0));
		addBanner("icedragon", new ItemStack(ModItems.dragon_skull, 1, 1));
		GameRegistry.addSmelting(ModBlocks.silverOre, new ItemStack(ModItems.silverIngot), 1);
		ModItems.blindfoldArmor.setRepairItem(new ItemStack(Items.STRING));
		ModItems.silverMetal.setRepairItem(new ItemStack(ModItems.silverIngot));
		ModItems.silverTools.setRepairItem(new ItemStack(ModItems.silverIngot));
		ModItems.boneTools.setRepairItem(new ItemStack(ModItems.witherbone));
		ModItems.fireBoneTools.setRepairItem(new ItemStack(ModItems.witherbone));
		ModItems.iceBoneTools.setRepairItem(new ItemStack(ModItems.witherbone));
		EnumDragonArmor.armor_green.material.setRepairItem(new ItemStack(ModItems.dragonscales_green));
		EnumDragonArmor.armor_blue.material.setRepairItem(new ItemStack(ModItems.dragonscales_blue));
		EnumDragonArmor.armor_bronze.material.setRepairItem(new ItemStack(ModItems.dragonscales_bronze));
		EnumDragonArmor.armor_gray.material.setRepairItem(new ItemStack(ModItems.dragonscales_gray));
		EnumDragonArmor.armor_red.material.setRepairItem(new ItemStack(ModItems.dragonscales_red));
		EnumDragonArmor.armor_sapphire.material.setRepairItem(new ItemStack(ModItems.dragonscales_sapphire));
		EnumDragonArmor.armor_silver.material.setRepairItem(new ItemStack(ModItems.dragonscales_silver));
		EnumDragonArmor.armor_white.material.setRepairItem(new ItemStack(ModItems.dragonscales_white));
	}

	public static BannerPattern addBanner(String name, ItemStack craftingStack) {
		Class<?>[] classes = {String.class, String.class, ItemStack.class};
		Object[] names = {name, "iceandfire." + name, craftingStack};
		return EnumHelper.addEnum(BannerPattern.class, name.toUpperCase(), classes, names);
	}
}
