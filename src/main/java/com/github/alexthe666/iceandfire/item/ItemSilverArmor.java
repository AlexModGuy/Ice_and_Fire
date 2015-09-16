package com.github.alexthe666.iceandfire.item;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.enums.EnumDragonArmor;
import com.github.alexthe666.iceandfire.enums.EnumDragonEgg;

public class ItemSilverArmor extends ItemArmor{

	public ItemSilverArmor(ArmorMaterial material, int renderIndex, int armorType, String gameName, String name) {
		super(material, renderIndex, armorType);
		this.setCreativeTab(IceAndFire.tab);
		this.setUnlocalizedName(name);
		GameRegistry.registerItem(this, gameName);
	}
}
