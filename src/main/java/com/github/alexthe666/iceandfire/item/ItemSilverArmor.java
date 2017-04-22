package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraftforge.fml.common.registry.*;

public class ItemSilverArmor extends ItemArmor {

	public ItemSilverArmor (ArmorMaterial material, int renderIndex, EntityEquipmentSlot slot, String gameName, String name) {
		super (material, renderIndex, slot);
		this.setCreativeTab (IceAndFire.TAB);
		this.setUnlocalizedName (name);
		GameRegistry.registerItem (this, gameName);
	}
}
