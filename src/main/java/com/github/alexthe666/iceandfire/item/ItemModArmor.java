package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.client.StatCollector;
import com.github.alexthe666.iceandfire.core.ModItems;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nullable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ItemModArmor extends ItemArmor {

	public ItemModArmor(ArmorMaterial material, int renderIndex, EntityEquipmentSlot slot, String gameName, String name) {
		super(material, renderIndex, slot);
		this.setCreativeTab(IceAndFire.TAB);
		this.setTranslationKey(name);
		this.setRegistryName(IceAndFire.MODID, gameName);
	}

	public String getTranslationKey(ItemStack stack){
		if(this == ModItems.earplugs) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			if (calendar.get(2) + 1 == 4 && calendar.get(5) == 1) {
				return "item.iceandfire.air_pods";
			}
		}
		return super.getTranslationKey(stack);
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		if (this == ModItems.earplugs) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			if (calendar.get(2) + 1 == 4 && calendar.get(5) == 1) {
				tooltip.add(TextFormatting.GREEN + StatCollector.translateToLocal("item.iceandfire.air_pods.desc"));
			}
		}
		super.addInformation(stack, worldIn, tooltip, flagIn);
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
