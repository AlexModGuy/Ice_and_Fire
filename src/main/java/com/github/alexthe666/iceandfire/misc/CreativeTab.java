package com.github.alexthe666.iceandfire.misc;

import com.github.alexthe666.iceandfire.core.ModItems;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class CreativeTab extends CreativeTabs {

	public CreativeTab(String label) {
		super(label);
	}

	@Override
	public ItemStack getTabIconItem() {
		return new ItemStack(ModItems.dragon_skull);
	}

	@Override
	public boolean hasSearchBar() {
		return false;
	}
}
