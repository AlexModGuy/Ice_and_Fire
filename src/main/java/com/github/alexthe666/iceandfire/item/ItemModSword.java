package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;

import net.minecraft.item.ItemSword;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemModSword extends ItemSword {

	public ItemModSword(ToolMaterial toolmaterial, String gameName, String name){
		super(toolmaterial);
		this.setUnlocalizedName(name);
		this.setCreativeTab(IceAndFire.tab);
		GameRegistry.registerItem(this, gameName);
	}
}
