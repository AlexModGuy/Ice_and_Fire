package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.*;
import com.github.alexthe666.iceandfire.client.*;
import net.minecraft.creativetab.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraftforge.fml.common.registry.*;
import net.minecraftforge.fml.relauncher.*;

import java.util.*;

public class ItemDragonArmor extends Item {

	public int type;
	public String name;

	public ItemDragonArmor (int type, String name) {
		this.type = type;
		this.name = name;
		this.setUnlocalizedName ("iceandfire." + name);
		this.setCreativeTab (IceAndFire.TAB);
		this.maxStackSize = 1;
		GameRegistry.registerItem (this, name);
	}

	@Override
	@SideOnly (Side.CLIENT)
	public void getSubItems (Item itemIn, CreativeTabs tab, List subItems) {
		subItems.add (new ItemStack (itemIn, 1, 0));
		subItems.add (new ItemStack (itemIn, 1, 1));
		subItems.add (new ItemStack (itemIn, 1, 2));
		subItems.add (new ItemStack (itemIn, 1, 3));

	}

	@Override
	@SideOnly (Side.CLIENT)
	public void addInformation (ItemStack stack, EntityPlayer playerIn, List tooltip, boolean advanced) {
		String words;
		switch (stack.getMetadata ()) {
			default:
				words = "dragon.armor_head";
				break;
			case 1:
				words = "dragon.armor_neck";
				break;
			case 2:
				words = "dragon.armor_body";
				break;
			case 3:
				words = "dragon.armor_tail";
				break;
		}
		tooltip.add (StatCollector.translateToLocal (words));
	}
}
