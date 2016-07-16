package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemGeneric extends Item {
	public ItemGeneric(String gameName, String name) {
		this.setCreativeTab(IceAndFire.TAB);
		this.setUnlocalizedName(name);
		GameRegistry.registerItem(this, gameName);
	}
}
