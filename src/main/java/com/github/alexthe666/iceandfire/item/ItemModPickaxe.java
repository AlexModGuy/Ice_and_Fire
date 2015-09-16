package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;

import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemPickaxe;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemModPickaxe extends ItemPickaxe {

	public ItemModPickaxe(ToolMaterial toolmaterial, String gameName, String name){
		super(toolmaterial);
		this.setUnlocalizedName(name);
		this.setCreativeTab(IceAndFire.tab);
		GameRegistry.registerItem(this, gameName);
	}
}
