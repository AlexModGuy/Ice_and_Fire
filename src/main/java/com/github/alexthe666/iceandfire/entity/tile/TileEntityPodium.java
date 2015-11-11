package com.github.alexthe666.iceandfire.entity.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.StatCollector;

import com.github.alexthe666.iceandfire.item.ItemDragonEgg;

public class TileEntityPodium extends TileEntity implements IUpdatePlayerListBox, ISidedInventory{
	private static final int[] slotsTop = new int[] {0};
	private ItemStack[] stacks = new ItemStack[1];

	@Override
	public void update() {

	}

	public int getSizeInventory()
	{
		return this.stacks.length;
	}

	public ItemStack getStackInSlot(int index)
	{
		return this.stacks[index];
	}

	public ItemStack decrStackSize(int index, int count)
	{
		if (this.stacks[index] != null)
		{
			ItemStack itemstack;

			if (this.stacks[index].stackSize <= count)
			{
				itemstack = this.stacks[index];
				this.stacks[index] = null;
				return itemstack;
			}
			else
			{
				itemstack = this.stacks[index].splitStack(count);

				if (this.stacks[index].stackSize == 0)
				{
					this.stacks[index] = null;
				}

				return itemstack;
			}
		}
		else
		{
			return null;
		}
	}

	public ItemStack getStackInSlotOnClosing(int index)
	{
		if (this.stacks[index] != null)
		{
			ItemStack itemstack = this.stacks[index];
			this.stacks[index] = null;
			return itemstack;
		}
		else
		{
			return null;
		}
	}

	public void setInventorySlotContents(int index, ItemStack stack)
	{
		boolean flag = stack != null && stack.isItemEqual(this.stacks[index]) && ItemStack.areItemStackTagsEqual(stack, this.stacks[index]);
		this.stacks[index] = stack;

		if (stack != null && stack.stackSize > this.getInventoryStackLimit())
		{
			stack.stackSize = this.getInventoryStackLimit();
		}
	}

	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		NBTTagList nbttaglist = compound.getTagList("Items", 10);
		this.stacks = new ItemStack[this.getSizeInventory()];

		for (int i = 0; i < nbttaglist.tagCount(); ++i)
		{
			NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
			byte b0 = nbttagcompound1.getByte("Slot");

			if (b0 >= 0 && b0 < this.stacks.length)
			{
				this.stacks[b0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
			}
		}
	}

	public void writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		NBTTagList nbttaglist = new NBTTagList();

		for (int i = 0; i < this.stacks.length; ++i)
		{
			if (this.stacks[i] != null)
			{
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Slot", (byte)i);
				this.stacks[i].writeToNBT(nbttagcompound1);
				nbttaglist.appendTag(nbttagcompound1);
			}
		}
		compound.setTag("Items", nbttaglist);
	}

	public void openInventory(EntityPlayer player) {}

	public void closeInventory(EntityPlayer player) {}


	public boolean canInsertItem(int index, ItemStack stack, EnumFacing direction)
	{
		return index != 0 ? true : stack.getItem() instanceof ItemDragonEgg;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return true;
	}

	public int getField(int id){
		return 0;
	}

	public void setField(int id, int value){}

	public int getFieldCount()
	{
		return 4;
	}


	@Override
	public void clear() {
		for (int i = 0; i < this.stacks.length; ++i)
		{
			this.stacks[i] = null;
		}
	}

	public String getName()
	{
		return "tile.iceandfire.podium.name";
	}

	@Override
	public IChatComponent getDisplayName() {
		return new ChatComponentText(StatCollector.translateToLocal("tile.iceandfire.podium.name"));
	}

	public int[] getSlotsForFace(EnumFacing side)
	{
		return slotsTop;
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
		return false;
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return false;
	}

}
