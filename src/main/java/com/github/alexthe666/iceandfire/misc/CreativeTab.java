package com.github.alexthe666.iceandfire.misc;

import com.github.alexthe666.iceandfire.core.ModItems;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class CreativeTab extends CreativeTabs {

	public CreativeTab(String label) {
		super(label);
	}

	@Override
	public Item getTabIconItem() {
		return ModItems.dragon_skull;
	}

}
