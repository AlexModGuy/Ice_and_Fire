package com.github.alexthe666.iceandfire.enums;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.core.ModItems;
import com.github.alexthe666.iceandfire.item.ItemDragonArmor;

import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;

public enum EnumDragonArmor {
	
	armor_red(ModItems.red, 12, EnumDragonEgg.RED),
	armor_bronze(ModItems.bronze, 13, EnumDragonEgg.BRONZE),
	armor_green(ModItems.green, 14, EnumDragonEgg.GREEN),
	armor_gray(ModItems.gray, 15, EnumDragonEgg.GRAY);
	
	public ArmorMaterial material;
	public int armorId;
	public EnumDragonEgg eggType;
	public static Item helmet;
	public static Item chestplate;
	public static Item leggings;
	public static Item boots;
	
	private EnumDragonArmor(ArmorMaterial material, int armorId, EnumDragonEgg eggType){
		this.material = material;
		this.armorId = armorId;
		this.eggType = eggType;
	}
	
	public static void initArmors(){
		 for (int i = 0; i < EnumDragonArmor.values().length; i++)
			{
			 EnumDragonArmor.values()[i].helmet = new ItemDragonArmor(EnumDragonArmor.values()[i].eggType, EnumDragonArmor.values()[i], EnumDragonArmor.values()[i].material, EnumDragonArmor.values()[i].armorId, 0).setUnlocalizedName("iceandfire.dragonHelmet");
			 EnumDragonArmor.values()[i].chestplate = new ItemDragonArmor(EnumDragonArmor.values()[i].eggType, EnumDragonArmor.values()[i], EnumDragonArmor.values()[i].material, EnumDragonArmor.values()[i].armorId, 1).setUnlocalizedName("iceandfire.dragonChestplate");
			 EnumDragonArmor.values()[i].leggings = new ItemDragonArmor(EnumDragonArmor.values()[i].eggType, EnumDragonArmor.values()[i], EnumDragonArmor.values()[i].material, EnumDragonArmor.values()[i].armorId, 2).setUnlocalizedName("iceandfire.dragonLeggings");
			 EnumDragonArmor.values()[i].boots = new ItemDragonArmor(EnumDragonArmor.values()[i].eggType, EnumDragonArmor.values()[i], EnumDragonArmor.values()[i].material, EnumDragonArmor.values()[i].armorId, 3).setUnlocalizedName("iceandfire.dragonBoots");
			 GameRegistry.registerItem(helmet, EnumDragonArmor.values()[i].name() + "_helmet");
			 GameRegistry.registerItem(chestplate, EnumDragonArmor.values()[i].name() + "_chestplate");
			 GameRegistry.registerItem(leggings, EnumDragonArmor.values()[i].name() + "_leggings");
			 GameRegistry.registerItem(boots, EnumDragonArmor.values()[i].name() + "_boots");
			 IceAndFire.proxy.renderArmors(EnumDragonArmor.values()[i]);
			}
	}
}
