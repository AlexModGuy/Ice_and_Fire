package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;

import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemAxe;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemModAxe extends ItemAxe {

	public ItemModAxe(ToolMaterial toolmaterial, String gameName, String name){
		super(toolmaterial);
		this.setUnlocalizedName(name);
		this.setCreativeTab(IceAndFire.tab);
		GameRegistry.registerItem(this, gameName);
	}
}
