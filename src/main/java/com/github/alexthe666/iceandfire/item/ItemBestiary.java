package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.*;
import com.github.alexthe666.iceandfire.client.*;
import com.github.alexthe666.iceandfire.core.*;
import com.github.alexthe666.iceandfire.enums.*;
import net.minecraft.creativetab.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.util.*;
import net.minecraft.util.text.*;
import net.minecraft.world.*;
import net.minecraftforge.fml.common.registry.*;
import net.minecraftforge.fml.relauncher.*;

import java.util.*;

public class ItemBestiary extends Item {

	public ItemBestiary () {
		this.maxStackSize = 1;
		this.setCreativeTab (IceAndFire.TAB);
		this.setUnlocalizedName ("iceandfire.bestiary");
		GameRegistry.registerItem (this, "bestiary");
	}

	@Override
	public void onCreated (ItemStack itemStack, World world, EntityPlayer player) {
		itemStack.setTagCompound (new NBTTagCompound ());
		itemStack.getTagCompound ().setIntArray ("Pages", new int[]{0});

	}

	@SideOnly (Side.CLIENT)
	public void getSubItems (Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
		subItems.add (new ItemStack (itemIn));
		ItemStack stack = new ItemStack (ModItems.bestiary);
		stack.setTagCompound (new NBTTagCompound ());
		stack.getTagCompound ().setIntArray ("Pages", new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8});
		subItems.add (stack);

	}

	@Override
	public ActionResult<ItemStack> onItemRightClick (ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
		if (worldIn.isRemote) {
			IceAndFire.PROXY.openBestiaryGui (itemStackIn);
		}
		return new ActionResult (EnumActionResult.PASS, itemStackIn);
	}

	@Override
	public void onUpdate (ItemStack stack, World world, Entity entity, int f, boolean f1) {
		if (stack.getTagCompound () == null) {
			stack.setTagCompound (new NBTTagCompound ());
			stack.getTagCompound ().setIntArray ("Pages", new int[]{EnumBestiaryPages.INTRODUCTION.ordinal ()});

		}
	}

	@Override
	public void addInformation (ItemStack stack, EntityPlayer player, List<String> list, boolean f) {
		if (stack.getTagCompound () != null) {
			list.add (StatCollector.translateToLocal ("bestiary.contains"));
			List<EnumBestiaryPages> pages = EnumBestiaryPages.containedPages (EnumBestiaryPages.toList (stack.getTagCompound ().getIntArray ("Pages")));
			for (EnumBestiaryPages page : pages) {
				list.add (TextFormatting.WHITE + "-" + StatCollector.translateToLocal ("bestiary." + EnumBestiaryPages.values ()[page.ordinal ()].toString ().toLowerCase ()));
			}

		}
	}

}
