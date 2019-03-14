package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.core.ModItems;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;

public class ItemModArmor extends ItemArmor {

	public ItemModArmor(ArmorMaterial material, int renderIndex, EntityEquipmentSlot slot, String gameName, String name) {
		super(material, renderIndex, slot);
		this.setCreativeTab(IceAndFire.TAB);
		this.setTranslationKey(name);
		this.setRegistryName(IceAndFire.MODID, gameName);
	}


	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair){
		ItemStack mat = this.getArmorMaterial().getRepairItemStack();
		if(this.getArmorMaterial() == ModItems.silverMetal){
			NonNullList<ItemStack> silverItems = OreDictionary.getOres("ingotSilver");
			for(ItemStack ingot : silverItems){
				if(OreDictionary.itemMatches(repair, ingot, false)){
					return true;
				}
			}		}
		if (!mat.isEmpty() && net.minecraftforge.oredict.OreDictionary.itemMatches(mat, repair, false)) return true;
		return super.getIsRepairable(toRepair, repair);
	}
}
