package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.*;
import com.github.alexthe666.iceandfire.client.*;
import com.github.alexthe666.iceandfire.entity.*;
import com.github.alexthe666.iceandfire.enums.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraftforge.fml.common.registry.*;
import net.minecraftforge.fml.relauncher.*;

import java.util.*;

public class ItemDragonEgg extends Item {
	public EnumDragonEgg type;

	public ItemDragonEgg (String name, EnumDragonEgg type) {
		this.setHasSubtypes (true);
		this.setCreativeTab (IceAndFire.TAB);
		this.type = type;
		this.setUnlocalizedName ("iceandfire.dragonegg");
		this.maxStackSize = 1;
		GameRegistry.registerItem (this, name);
	}

	@Override
	public void onCreated (ItemStack itemStack, World world, EntityPlayer player) {
		itemStack.setTagCompound (new NBTTagCompound ());
	}

	@Override
	@SideOnly (Side.CLIENT)
	public void addInformation (ItemStack stack, EntityPlayer playerIn, List tooltip, boolean advanced) {
		tooltip.add (type.color + StatCollector.translateToLocal ("dragon." + type.toString ().toLowerCase ()));
	}

	@Override
	public EnumActionResult onItemUse (ItemStack stack, EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (side != EnumFacing.UP) {
			return EnumActionResult.PASS;
		} else {
			EntityDragonEgg egg = new EntityDragonEgg (worldIn);
			egg.setType (type);
			egg.setPosition (pos.getX () + 0.5, pos.getY () + 1, pos.getZ () + 0.5);
			if (!worldIn.isRemote) {
				worldIn.spawnEntity (egg);
			}
			if (!player.capabilities.isCreativeMode) {
				--stack.stackSize;

				if (stack.stackSize <= 0) {
					player.inventory.setInventorySlotContents (player.inventory.currentItem, (ItemStack) null);
				}
			}
			return EnumActionResult.SUCCESS;

		}
	}
}
