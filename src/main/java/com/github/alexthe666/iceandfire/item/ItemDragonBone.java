package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.item.Item;

public class ItemDragonBone extends Item {

	public ItemDragonBone() {
		this.setCreativeTab(IceAndFire.TAB_ITEMS);
		this.setTranslationKey("iceandfire.dragonbone");
		this.setRegistryName(IceAndFire.MODID, "dragonbone");
	}
}
