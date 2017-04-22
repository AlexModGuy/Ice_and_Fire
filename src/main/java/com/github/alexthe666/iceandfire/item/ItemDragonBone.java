package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.*;
import net.minecraft.item.*;
import net.minecraftforge.fml.common.registry.*;

public class ItemDragonBone extends Item {

	public ItemDragonBone () {
		this.setCreativeTab (IceAndFire.TAB);
		this.setUnlocalizedName ("iceandfire.dragonbone");
		this.maxStackSize = 8;
		GameRegistry.registerItem (this, "dragonbone");
	}
}
