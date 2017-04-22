package com.github.alexthe666.iceandfire.misc;

import com.github.alexthe666.iceandfire.core.*;
import net.minecraft.creativetab.*;
import net.minecraft.item.*;

public class CreativeTab extends CreativeTabs {

	public CreativeTab (String label) {
		super (label);
	}

	@Override
	public Item getTabIconItem () {
		return ModItems.dragon_skull;
	}

}
