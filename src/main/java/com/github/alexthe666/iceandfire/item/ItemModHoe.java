package com.github.alexthe666.iceandfire.item;

import java.util.List;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.core.ModItems;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemModHoe extends ItemHoe {

	public ItemModHoe(ToolMaterial toolmaterial, String gameName, String name){
		super(toolmaterial);
		this.setUnlocalizedName(name);
		this.setCreativeTab(IceAndFire.tab);
		GameRegistry.registerItem(this, gameName);
	}
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
	{
		if(this == ModItems.silver_hoe){
			if(target.getCreatureAttribute() == EnumCreatureAttribute.UNDEAD){
				target.attackEntityFrom(DamageSource.magic, 2);
			}
		}
		return super.hitEntity(stack, target, attacker);
	}

	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean f) {
		if(this == ModItems.silver_hoe)
		list.add(EnumChatFormatting.GREEN + StatCollector.translateToLocal("silvertools.hurt"));
	}
}
