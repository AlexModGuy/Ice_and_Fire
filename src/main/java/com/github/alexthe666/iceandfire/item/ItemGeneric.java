package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.*;
import com.github.alexthe666.iceandfire.core.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.world.*;
import net.minecraftforge.fml.common.registry.*;

public class ItemGeneric extends Item {
	public ItemGeneric (String gameName, String name) {
		this.setCreativeTab (IceAndFire.TAB);
		this.setUnlocalizedName (name);
		GameRegistry.registerItem (this, gameName);
	}

	public void onUpdate (ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		if (entityIn instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entityIn;

			if (this == ModItems.manuscript) {
				player.addStat (ModAchievements.manuscript, 1);
			}
		}
	}
}
