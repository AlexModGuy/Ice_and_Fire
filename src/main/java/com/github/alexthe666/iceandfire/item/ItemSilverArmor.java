package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;

public class ItemSilverArmor extends ItemArmor {

	public ItemSilverArmor(ArmorMaterial material, int renderIndex, EntityEquipmentSlot slot, String gameName, String name) {
		super(material, renderIndex, slot);
		this.setCreativeTab(IceAndFire.TAB);
		this.setUnlocalizedName(name);
		this.setRegistryName(IceAndFire.MODID, gameName);
	}
}
