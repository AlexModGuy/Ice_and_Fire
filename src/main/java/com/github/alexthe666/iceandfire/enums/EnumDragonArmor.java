package com.github.alexthe666.iceandfire.enums;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.core.ModItems;
import com.github.alexthe666.iceandfire.item.ItemScaleArmor;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

public enum EnumDragonArmor {

	armor_red(ModItems.red, 12, EnumDragonEgg.RED),
	armor_bronze(ModItems.bronze, 13, EnumDragonEgg.BRONZE),
	armor_green(ModItems.green, 14, EnumDragonEgg.GREEN),
	armor_gray(ModItems.gray, 15, EnumDragonEgg.GRAY);

	public ArmorMaterial material;
	public int armorId;
	public EnumDragonEgg eggType;
	public Item helmet;
	public Item chestplate;
	public Item leggings;
	public Item boots;

	private EnumDragonArmor(ArmorMaterial material, int armorId, EnumDragonEgg eggType){
		this.material = material;
		this.armorId = armorId;
		this.eggType = eggType;
	}

	public static void initArmors(){
		for (int i = 0; i < EnumDragonArmor.values().length; i++)
		{
			EnumDragonArmor.values()[i].helmet = new ItemScaleArmor(EnumDragonArmor.values()[i].eggType, EnumDragonArmor.values()[i], EnumDragonArmor.values()[i].material, 0, EntityEquipmentSlot.HEAD).setUnlocalizedName("iceandfire.dragonHelmet");
			EnumDragonArmor.values()[i].chestplate = new ItemScaleArmor(EnumDragonArmor.values()[i].eggType, EnumDragonArmor.values()[i], EnumDragonArmor.values()[i].material, 1, EntityEquipmentSlot.CHEST).setUnlocalizedName("iceandfire.dragonChestplate");
			EnumDragonArmor.values()[i].leggings = new ItemScaleArmor(EnumDragonArmor.values()[i].eggType, EnumDragonArmor.values()[i], EnumDragonArmor.values()[i].material, 2, EntityEquipmentSlot.LEGS).setUnlocalizedName("iceandfire.dragonLeggings");
			EnumDragonArmor.values()[i].boots = new ItemScaleArmor(EnumDragonArmor.values()[i].eggType, EnumDragonArmor.values()[i], EnumDragonArmor.values()[i].material, 3, EntityEquipmentSlot.FEET).setUnlocalizedName("iceandfire.dragonBoots");
			GameRegistry.registerItem(EnumDragonArmor.values()[i].helmet, EnumDragonArmor.values()[i].name() + "_helmet");
			GameRegistry.registerItem(EnumDragonArmor.values()[i].chestplate, EnumDragonArmor.values()[i].name() + "_chestplate");
			GameRegistry.registerItem(EnumDragonArmor.values()[i].leggings, EnumDragonArmor.values()[i].name() + "_leggings");
			GameRegistry.registerItem( EnumDragonArmor.values()[i].boots, EnumDragonArmor.values()[i].name() + "_boots");
			GameRegistry.addRecipe(new ItemStack(EnumDragonArmor.values()[i].helmet, 1, 0), new Object[] {"XXX", "X X", 'X', getScaleItem(EnumDragonArmor.values()[i])});
			GameRegistry.addRecipe(new ItemStack(EnumDragonArmor.values()[i].chestplate, 1, 0), new Object[] {"X X", "XXX", "XXX", 'X', getScaleItem(EnumDragonArmor.values()[i])});
			GameRegistry.addRecipe(new ItemStack(EnumDragonArmor.values()[i].leggings, 1, 0), new Object[] {"XXX", "X X", "X X", 'X', getScaleItem(EnumDragonArmor.values()[i])});
			GameRegistry.addRecipe(new ItemStack(EnumDragonArmor.values()[i].boots, 1, 0), new Object[] {"X X", "X X", 'X', getScaleItem(EnumDragonArmor.values()[i])});
			IceAndFire.proxy.renderArmors(EnumDragonArmor.values()[i]);
		}
	}

	public static Item getScaleItem(EnumDragonArmor armor){
		switch(armor){
		case armor_red:
			return ModItems.dragonscales_red;
		case armor_bronze:
			return ModItems.dragonscales_bronze;
		case armor_green:
			return ModItems.dragonscales_green;
		case armor_gray:
			return ModItems.dragonscales_gray;
		default:
			return ModItems.dragonscales_red;
		}
	}
}
