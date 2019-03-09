package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.item.Item;

public class ItemDragonBone extends Item {

	public ItemDragonBone() {
		this.setCreativeTab(IceAndFire.TAB);
		this.setTranslationKey("iceandfire.dragonbone");
		this.maxStackSize = 8;
		this.setRegistryName(IceAndFire.MODID, "dragonbone");
	}
}
