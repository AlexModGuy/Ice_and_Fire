package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemGeneric extends Item {
	public ItemGeneric(String gameName, String name) {
		this.setCreativeTab(IceAndFire.TAB);
		this.setUnlocalizedName(name);
		this.setRegistryName(IceAndFire.MODID, gameName);
	}
}
