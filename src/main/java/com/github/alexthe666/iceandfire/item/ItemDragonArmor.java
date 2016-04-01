package com.github.alexthe666.iceandfire.item;

import java.util.List;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.client.StatCollector;

public class ItemDragonArmor extends Item {

	public int type;
	public String name;
	public ItemDragonArmor(int type, String name){
		this.type = type;
		this.name = name;
		this.setUnlocalizedName("iceandfire." + name);
		this.setCreativeTab(IceAndFire.tab);
		this.maxStackSize = 1;
		GameRegistry.registerItem(this, name);
	}
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item itemIn, CreativeTabs tab, List subItems)
	{
		subItems.add(new ItemStack(itemIn, 1, 0));
		subItems.add(new ItemStack(itemIn, 1, 1));
		subItems.add(new ItemStack(itemIn, 1, 2));
		subItems.add(new ItemStack(itemIn, 1, 3));

	}
    /*public String getUnlocalizedName(ItemStack stack)
    {
    	String base = "iceandfire." + name;
    	switch(stack.getMetadata()){
    	default:
			return base + "_head";
		case 1:
			return base + "_neck";	
		case 2:
			return base + "_body";	
		case 3:
			return base + "_tail";	
		}
    }*/
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List tooltip, boolean advanced) {
		String words;
    	switch(stack.getMetadata()){
    	default:
    		words = "dragon.armor_head";
    		break;
		case 1:
			words = "dragon.armor_neck";	
    		break;
		case 2:
			words = "dragon.armor_body";	
    		break;
		case 3:
			words = "dragon.armor_tail";
    		break;
    	}
    	tooltip.add(StatCollector.translateToLocal(words));
	}
	
	/*@Override
	public ModelResourceLocation getModel(ItemStack stack, EntityPlayer player, int useRemaining)
	{
		switch(stack.getMetadata()){
		default:
			return new ModelResourceLocation("iceandfire:dragonarmor_iron_head", "inventory");
		case 1:
			return new ModelResourceLocation("iceandfire:dragonarmor_iron_neck", "inventory");	
		case 2:
			return new ModelResourceLocation("iceandfire:dragonarmor_iron_body", "inventory");	
		case 3:
			return new ModelResourceLocation("iceandfire:dragonarmor_iron_tail", "inventory");	
		}
	}*/
}
