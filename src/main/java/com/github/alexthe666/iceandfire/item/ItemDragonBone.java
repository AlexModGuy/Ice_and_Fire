package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemDragonBone extends Item {

	public ItemDragonBone() {
		this.setCreativeTab(IceAndFire.tab);
		this.setUnlocalizedName("iceandfire.dragonbone");
		this.maxStackSize = 8;
		GameRegistry.registerItem(this, "dragonbone");
	}
}
