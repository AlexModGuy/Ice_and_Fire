package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.item.Item;

public class ItemGeneric extends Item {
	public ItemGeneric(String gameName, String name) {
		this.setCreativeTab(IceAndFire.TAB_ITEMS);
		this.setTranslationKey(name);
		this.setRegistryName(IceAndFire.MODID, gameName);
	}
}
