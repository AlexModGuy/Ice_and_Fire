package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.world.*;
import net.minecraftforge.fml.common.registry.*;

import java.util.*;

public class ItemDragonHornStatic extends Item {

	public ItemDragonHornStatic () {
		this.maxStackSize = 1;
		this.setCreativeTab (IceAndFire.TAB);
		this.setUnlocalizedName ("iceandfire.dragon_horn");
		GameRegistry.registerItem (this, "dragon_horn");
	}

	@Override
	public void onCreated (ItemStack itemStack, World world, EntityPlayer player) {
		itemStack.setTagCompound (new NBTTagCompound ());
	}

	@Override
	public void onUpdate (ItemStack stack, World world, Entity entity, int f, boolean f1) {
		if (stack.getTagCompound () == null) {
			stack.setTagCompound (new NBTTagCompound ());
		}
	}

	@Override
	public void addInformation (ItemStack stack, EntityPlayer player, List<String> list, boolean f) {
	}
}
