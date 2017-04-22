package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.*;
import com.github.alexthe666.iceandfire.client.*;
import com.github.alexthe666.iceandfire.core.*;
import com.github.alexthe666.iceandfire.entity.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.init.*;
import net.minecraft.item.*;
import net.minecraft.potion.*;
import net.minecraft.util.*;
import net.minecraft.util.text.*;
import net.minecraftforge.fml.common.registry.*;
import net.minecraftforge.fml.relauncher.*;

import java.util.*;

public class ItemAlchemySword extends ItemSword {

	public ItemAlchemySword (ToolMaterial toolmaterial, String gameName, String name) {
		super (toolmaterial);
		this.setUnlocalizedName (name);
		this.setCreativeTab (IceAndFire.TAB);
		GameRegistry.registerItem (this, gameName);
	}

	@Override
	public boolean hitEntity (ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
		if (this == ModItems.dragonbone_sword_fire) {
			if (target instanceof EntityIceDragon) {
				target.attackEntityFrom (DamageSource.inFire, 13.5F);
			}
			target.setFire (5);
			target.knockBack (target, 1F, attacker.posX - target.posX, attacker.posZ - target.posZ);
		}
		if (this == ModItems.dragonbone_sword_ice) {
			if (target instanceof EntityFireDragon) {
				target.attackEntityFrom (DamageSource.drown, 13.5F);
			}
			target.addPotionEffect (new PotionEffect (MobEffects.SLOWNESS, 100, 2));
			target.addPotionEffect (new PotionEffect (MobEffects.MINING_FATIGUE, 100, 2));
			target.knockBack (target, 1F, attacker.posX - target.posX, attacker.posZ - target.posZ);
		}
		return super.hitEntity (stack, target, attacker);
	}

	@Override
	public void addInformation (ItemStack stack, EntityPlayer player, List list, boolean f) {
		if (this == ModItems.dragonbone_sword_fire) {
			list.add (TextFormatting.GREEN + StatCollector.translateToLocal ("dragon_sword_fire.hurt1"));
			list.add (TextFormatting.DARK_RED + StatCollector.translateToLocal ("dragon_sword_fire.hurt2"));
		}
		if (this == ModItems.dragonbone_sword_ice) {
			list.add (TextFormatting.GREEN + StatCollector.translateToLocal ("dragon_sword_ice.hurt1"));
			list.add (TextFormatting.AQUA + StatCollector.translateToLocal ("dragon_sword_ice.hurt2"));

		}
	}

	@SideOnly (Side.CLIENT)
	public boolean hasEffect (ItemStack stack) {
		return true;
	}
}
