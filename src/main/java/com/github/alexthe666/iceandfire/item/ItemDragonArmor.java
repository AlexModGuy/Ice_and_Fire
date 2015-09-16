package com.github.alexthe666.iceandfire.item;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.enums.EnumDragonArmor;
import com.github.alexthe666.iceandfire.enums.EnumDragonEgg;

public class ItemDragonArmor extends ItemArmor{

	public EnumDragonArmor type;
	public EnumDragonEgg eggType;
	public ItemDragonArmor(EnumDragonEgg eggType, EnumDragonArmor type, ArmorMaterial material, int renderIndex, int armorType) {
		super(material, renderIndex, armorType);
		this.type = type;
		this.eggType = eggType;
		this.setCreativeTab(IceAndFire.tab);
	}
	
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List tooltip, boolean advanced) {
		tooltip.add(eggType.color + StatCollector.translateToLocal("dragon." + eggType.toString().toLowerCase()));
	}	
}
