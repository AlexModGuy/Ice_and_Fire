package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.*;
import com.github.alexthe666.iceandfire.client.*;
import com.github.alexthe666.iceandfire.core.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.text.*;
import net.minecraftforge.fml.common.registry.*;

import java.util.*;

public class ItemModShovel extends ItemSpade {

	public ItemModShovel (ToolMaterial toolmaterial, String gameName, String name) {
		super (toolmaterial);
		this.setUnlocalizedName (name);
		this.setCreativeTab (IceAndFire.TAB);
		GameRegistry.registerItem (this, gameName);
	}

	@Override
	public boolean hitEntity (ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
		if (this == ModItems.silver_shovel) {
			if (target.getCreatureAttribute () == EnumCreatureAttribute.UNDEAD) {
				target.attackEntityFrom (DamageSource.magic, 2);
			}
		}
		return super.hitEntity (stack, target, attacker);
	}

	@Override
	public void addInformation (ItemStack stack, EntityPlayer player, List list, boolean f) {
		if (this == ModItems.silver_shovel)
			list.add (TextFormatting.GREEN + StatCollector.translateToLocal ("silvertools.hurt"));
	}
}
