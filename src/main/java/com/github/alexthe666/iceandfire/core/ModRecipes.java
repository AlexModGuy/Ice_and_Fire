package com.github.alexthe666.iceandfire.core;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityBanner;
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
		OreDictionary.registerOre("oreSilver", ModBlocks.sapphireOre);
		OreDictionary.registerOre("blockSilver", ModBlocks.silverBlock);
		OreDictionary.registerOre("boneWither", ModItems.witherbone);
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.lectern, 1, 0), new Object[] { " X ", " Y ", " Y ", Character.valueOf('X'), Items.BOOK, Character.valueOf('Y'), "plankWood" }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.podium, 1, 0), new Object[] { "XYX", " Y ", "XYX", Character.valueOf('X'), new ItemStack(Blocks.WOODEN_SLAB, 1, 0), Character.valueOf('Y'), new ItemStack(Blocks.PLANKS, 1, 0) }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.podium, 1, 1), new Object[] { "XYX", " Y ", "XYX", Character.valueOf('X'), new ItemStack(Blocks.WOODEN_SLAB, 1, 1), Character.valueOf('Y'), new ItemStack(Blocks.PLANKS, 1, 1) }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.podium, 1, 2), new Object[] { "XYX", " Y ", "XYX", Character.valueOf('X'), new ItemStack(Blocks.WOODEN_SLAB, 1, 2), Character.valueOf('Y'), new ItemStack(Blocks.PLANKS, 1, 2) }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.podium, 1, 3), new Object[] { "XYX", " Y ", "XYX", Character.valueOf('X'), new ItemStack(Blocks.WOODEN_SLAB, 1, 3), Character.valueOf('Y'), new ItemStack(Blocks.PLANKS, 1, 3) }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.podium, 1, 4), new Object[] { "XYX", " Y ", "XYX", Character.valueOf('X'), new ItemStack(Blocks.WOODEN_SLAB, 1, 4), Character.valueOf('Y'), new ItemStack(Blocks.PLANKS, 1, 4) }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.podium, 1, 5), new Object[] { "XYX", " Y ", "XYX", Character.valueOf('X'), new ItemStack(Blocks.WOODEN_SLAB, 1, 5), Character.valueOf('Y'), new ItemStack(Blocks.PLANKS, 1, 5) }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.sapphireBlock, 1, 0), new Object[] { "XXX", "XXX", "XXX", Character.valueOf('X'), ModItems.sapphireGem }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.silverBlock, 1, 0), new Object[] { "XXX", "XXX", "XXX", Character.valueOf('X'), "ingotSilver" }));
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.bestiary, 1, 0), ModItems.manuscript, ModItems.manuscript, ModItems.manuscript);
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.silverNugget, 9, 0), ModItems.silverIngot);
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.wither_shard, 3, 0), ModItems.witherbone);
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.dragonbone_sword_fire, 1, 0), ModItems.dragonbone_sword, ModItems.fire_dragon_blood);
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.dragonbone_sword_ice, 1, 0), ModItems.dragonbone_sword, ModItems.ice_dragon_blood);
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.fire_stew, 1, 0), Items.BOWL, ModBlocks.fire_lily, Items.BLAZE_ROD);
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.frost_stew, 1, 0), Items.BOWL, ModBlocks.frost_lily, Items.PRISMARINE_CRYSTALS);
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.silverIngot, 1, 0), new Object[] { "XXX", "XXX", "XXX", Character.valueOf('X'), "nuggetSilver" }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.silver_helmet, 1, 0), new Object[] { "XXX", "X X", Character.valueOf('X'), "ingotSilver" }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.silver_chestplate, 1, 0), new Object[] { "X X", "XXX", "XXX", Character.valueOf('X'), "ingotSilver" }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.silver_leggings, 1, 0), new Object[] { "XXX", "X X", "X X", Character.valueOf('X'), "ingotSilver" }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.silver_boots, 1, 0), new Object[] { "X X", "X X", Character.valueOf('X'), "ingotSilver" }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.silver_sword, 1, 0), new Object[] { "X", "X", "Y", Character.valueOf('X'), "ingotSilver", Character.valueOf('Y'), "stickWood" }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.silver_pickaxe, 1, 0), new Object[] { "XXX", " Y ", " Y ", Character.valueOf('X'), "ingotSilver", Character.valueOf('Y'), "stickWood" }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.silver_axe, 1, 0), new Object[] { "XX", "XY", " Y", Character.valueOf('X'), "ingotSilver", Character.valueOf('Y'), "stickWood" }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.silver_shovel, 1, 0), new Object[] { "X", "Y", "Y", Character.valueOf('X'), "ingotSilver", Character.valueOf('Y'), "stickWood" }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.silver_hoe, 1, 0), new Object[] { "XX", " Y", " Y", Character.valueOf('X'), "ingotSilver", Character.valueOf('Y'), "stickWood" }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.dragonbone_sword, 1, 0), new Object[] { "X", "X", "Y", Character.valueOf('X'), ModItems.dragonbone, Character.valueOf('Y'), "boneWither" }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.dragonbone_pickaxe, 1, 0), new Object[] { "XXX", " Y ", " Y ", Character.valueOf('X'), ModItems.dragonbone, Character.valueOf('Y'), "boneWither" }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.dragonbone_axe, 1, 0), new Object[] { "XX", "XY", " Y", Character.valueOf('X'), ModItems.dragonbone, Character.valueOf('Y'), "boneWither" }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.dragonbone_shovel, 1, 0), new Object[] { "X", "Y", "Y", Character.valueOf('X'), ModItems.dragonbone, Character.valueOf('Y'), "boneWither" }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.dragonbone_hoe, 1, 0), new Object[] { "XX", " Y", " Y", Character.valueOf('X'), ModItems.dragonbone, Character.valueOf('Y'), "boneWither" }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.dragonbone_bow, 1, 0), new Object[] { " ZY", "X Y", " ZY", Character.valueOf('X'), "boneWither", Character.valueOf('Y'), Items.STRING, Character.valueOf('Z'), ModItems.dragonbone }));
		GameRegistry.addRecipe(new ItemStack(ModItems.dragonbone_arrow, 5, 0), new Object[] { "X", "Y", "Z", Character.valueOf('X'), ModItems.wither_shard, Character.valueOf('Y'), ModItems.dragonbone, Character.valueOf('Z'), Items.FEATHER });
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.dragon_armor_iron, 1, 0), new Object[] { " XX", "XXX", Character.valueOf('X'), "blockIron" }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.dragon_armor_iron, 1, 1), new Object[] { "XXX", " XX", Character.valueOf('X'), "blockIron" }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.dragon_armor_iron, 1, 2), new Object[] { "XXX", "XXX", "X X", Character.valueOf('X'), "blockIron" }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.dragon_armor_iron, 1, 3), new Object[] { "  X", "XX ", Character.valueOf('X'), "blockIron" }));
		addBanner("firedragon", new ItemStack(ModItems.dragon_skull, 1, 0));
		addBanner("icedragon", new ItemStack(ModItems.dragon_skull, 1, 1));

	}

	public static TileEntityBanner.EnumBannerPattern addBanner(String name, ItemStack craftingStack) {
		Class<?>[] classes = { String.class, String.class, ItemStack.class };
		Object[] names = { name, "iceandfire." + name, craftingStack };
		return EnumHelper.addEnum(TileEntityBanner.EnumBannerPattern.class, name.toUpperCase(), classes, names);
	}
}
