package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.*;
import com.github.alexthe666.iceandfire.client.*;
import com.github.alexthe666.iceandfire.enums.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraftforge.fml.common.registry.*;
import net.minecraftforge.fml.relauncher.*;

import java.util.*;

public class ItemDragonScales extends Item {
	EnumDragonEgg type;

	public ItemDragonScales (String name, EnumDragonEgg type) {
		this.setHasSubtypes (true);
		this.setCreativeTab (IceAndFire.TAB);
		this.type = type;
		this.setUnlocalizedName ("iceandfire.dragonscales");
		GameRegistry.registerItem (this, name);
	}

	@Override
	@SideOnly (Side.CLIENT)
	public void addInformation (ItemStack stack, EntityPlayer playerIn, List tooltip, boolean advanced) {
		tooltip.add (type.color + StatCollector.translateToLocal ("dragon." + type.toString ().toLowerCase ()));
	}

}
