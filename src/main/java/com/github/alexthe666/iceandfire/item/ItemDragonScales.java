package com.github.alexthe666.iceandfire.item;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.client.StatCollector;
import com.github.alexthe666.iceandfire.enums.EnumDragonEgg;

public class ItemDragonScales extends Item {
	EnumDragonEgg type;
	public ItemDragonScales(String name, EnumDragonEgg type)
	{
		this.setHasSubtypes(true);
		this.setCreativeTab(IceAndFire.tab);
		this.type = type;
		this.setUnlocalizedName("iceandfire.dragonscales");
		GameRegistry.registerItem(this, name);
	}

	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List tooltip, boolean advanced) {
		tooltip.add(type.color + StatCollector.translateToLocal("dragon." + type.toString().toLowerCase()));
	}

}
