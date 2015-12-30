package com.github.alexthe666.iceandfire.entity.tile;

import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.SlotFurnaceFuel;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.github.alexthe666.iceandfire.core.ModItems;
import com.github.alexthe666.iceandfire.enums.EnumBestiaryPages;
import com.github.alexthe666.iceandfire.inventory.ContainerLectern;

public class TileEntityLectern extends TileEntity implements IUpdatePlayerListBox, ISidedInventory
{
	private static final int[] slotsTop = new int[] {0};
	private static final int[] slotsBottom = new int[] {2, 1};
	private static final int[] slotsSides = new int[] {1};
	private ItemStack[] stacks = new ItemStack[3];
	private int furnaceBurnTime;
	private int currentItemBurnTime;
	private int cookTime;
	private int totalCookTime;
	private String furnaceCustomName;
	public float pageFlip;
	public float pageFlipPrev;
	public float pageHelp1;
	public float pageHelp2;
	private static Random rand = new Random();

	public void update()
	{
		boolean flag = this.isBurning();
		boolean flag1 = false;

		if (this.isBurning())
		{
			--this.furnaceBurnTime;
		}

		if (!this.worldObj.isRemote)
		{
			if (!this.isBurning() && (this.stacks[1] == null || this.stacks[0] == null))
			{
				if (!this.isBurning() && this.cookTime > 0)
				{
					this.cookTime = MathHelper.clamp_int(this.cookTime - 2, 0, this.totalCookTime);
				}
			}
			else
			{
				if (!this.isBurning() && this.canSmelt())
				{
					this.currentItemBurnTime = this.furnaceBurnTime = getItemBurnTime(this.stacks[1]);

					if (this.isBurning())
					{
						flag1 = true;

						if (this.stacks[1] != null)
						{
							--this.stacks[1].stackSize;

							if (this.stacks[1].stackSize == 0)
							{
								this.stacks[1] = stacks[1].getItem().getContainerItem(stacks[1]);
							}
						}
					}
				}

				if (this.isBurning() && this.canSmelt())
				{
					++this.cookTime;

					if (this.cookTime == this.totalCookTime)
					{
						this.cookTime = 0;
						this.totalCookTime = 900;
						this.smeltItem();
						flag1 = true;
					}
				}
				else
				{
					this.cookTime = 0;
				}
			}

			if (flag != this.isBurning())
			{
				flag1 = true;
			}
		}

		if (flag1)
		{
			this.markDirty();
		}
		float f1 = this.pageHelp1;
		do
		{
			this.pageHelp1 += (float)(rand.nextInt(4) - rand.nextInt(4));
		}
		while (f1 == this.pageHelp1);
		this.pageFlipPrev = this.pageFlip;
		float f = (this.pageHelp1 - this.pageFlip) * 0.04F;
		float f3 = 0.02F;
		f = MathHelper.clamp_float(f, -f3, f3);
		this.pageHelp2 += (f - this.pageHelp2) * 0.9F;
		this.pageFlip += this.pageHelp2;
	}

	public int getSizeInventory()
	{
		return this.stacks.length;
	}

	public ItemStack getStackInSlot(int index)
	{
		return this.stacks[index];
	}

	private boolean canSmelt()
	{
		if (this.stacks[0] == null)
		{
			return false;
		}
		else
		{
			ItemStack itemstack = this.stacks[0].copy();
			if (itemstack == null) return false;
			if (this.stacks[2] == null) return true;
			int result = stacks[2].stackSize + itemstack.stackSize;
			return result <= getInventoryStackLimit() && result <= this.stacks[2].getMaxStackSize();
		}
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
		if (index == 0 && !flag)
        {
            this.totalCookTime = 900;
            this.cookTime = 0;
            this.markDirty();
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
			this.furnaceBurnTime = compound.getShort("BurnTime");
			this.cookTime = compound.getShort("CookTime");
			this.totalCookTime = compound.getShort("CookTimeTotal");
			this.currentItemBurnTime = getItemBurnTime(this.stacks[1]);
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
		compound.setShort("BurnTime", (short)this.furnaceBurnTime);
		compound.setShort("CookTime", (short)this.cookTime);
		compound.setShort("CookTimeTotal", (short)this.totalCookTime);
	}

	public void openInventory(EntityPlayer player) {}

	public void closeInventory(EntityPlayer player) {}

	@SideOnly(Side.CLIENT)
	public static boolean isBurning(IInventory i)
	{
		return i.getField(0) > 0;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return true;
	}

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
		return "tile.iceandfire.lectern.name";
	}

	@Override
	public IChatComponent getDisplayName() {
		return new ChatComponentText(StatCollector.translateToLocal("tile.iceandfire.lectern.name"));
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
		return false;
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}
	public boolean isBurning()
	{
		return this.furnaceBurnTime > 0;
	}
	public void smeltItem()
	{
		if (this.canSmelt())
		{
			ItemStack itemstack = this.stacks[0].copy();
			if(this.stacks[0].getItem() == ModItems.bestiary){
				EnumBestiaryPages.addRandomPage(itemstack);
			}

			if (this.stacks[2] == null)
			{
				this.stacks[2] = itemstack.copy();
			}
			else if (this.stacks[2].getItem() == itemstack.getItem())
			{
				this.stacks[2].stackSize += itemstack.stackSize;
			}

			--this.stacks[0].stackSize;

			if (this.stacks[0].stackSize <= 0)
			{
				this.stacks[0] = null;
			}
		}
	}
	public static int getItemBurnTime(ItemStack i)
	{
		if (i == null)
		{
			return 0;
		}
		else
		{
			Item item = i.getItem();
			return item == ModItems.manuscript ? 300 : 0;
		}
	}

	public static boolean isItemFuel(ItemStack i)
	{
		return getItemBurnTime(i) > 0;
	}

	public int[] getSlotsForFace(EnumFacing side)
	{
		return side == EnumFacing.DOWN ? slotsBottom : (side == EnumFacing.UP ? slotsTop : slotsSides);
	}

	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction)
	{
		return this.isItemValidForSlot(index, itemStackIn);
	}

	public String getGuiID()
	{
		return "iceandfire:lectern";
	}

	public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
	{
		return new ContainerLectern(playerInventory, this);
	}

	public int getField(int id)
	{
		switch (id)
		{
		case 0:
			return this.furnaceBurnTime;
		case 1:
			return this.currentItemBurnTime;
		case 2:
			return this.cookTime;
		case 3:
			return this.totalCookTime;
		default:
			return 0;
		}
	}

	public boolean isItemValidForSlot(int index, ItemStack stack)
	{
		return index == 2 ? false : (index != 1 ? true : isItemFuel(stack));
	}

	public void setField(int id, int value)
	{
		switch (id)
		{
		case 0:
			this.furnaceBurnTime = value;
			break;
		case 1:
			this.currentItemBurnTime = value;
			break;
		case 2:
			this.cookTime = value;
			break;
		case 3:
			this.totalCookTime = value;
		}
	}
}