package com.github.alexthe666.iceandfire.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;

public class ContainerDragon extends Container {
	private IInventory dragonInv;
	private EntityDragonBase dragon;
	private static final String __OBFID = "CL_00001751";

	public ContainerDragon(final EntityDragonBase dragon, EntityPlayer player) {

		this.dragonInv = dragon.inv;
		this.dragon = dragon;
		byte b0 = 3;
		dragon.inv.openInventory(player);
		int i = (b0 - 4) * 18;
		this.addSlotToContainer(new Slot(dragon.inv, 1, 8, 18) {
			@Override
			public boolean isItemValid(ItemStack stack) {
				return super.isItemValid(stack) && EntityDragonBase.isAllowedInSlot(1, stack);
			}

			@Override
			public void onSlotChange(ItemStack stack, ItemStack stack2) {
				dragon.worldObj.playSound((EntityPlayer) null, dragon.posX, dragon.posY, dragon.posZ, SoundEvents.entity_horse_armor, dragon.getSoundCategory(), 1.0F, 1.0F + (dragon.getRNG().nextFloat() - dragon.getRNG().nextFloat()) * 0.2F);
				super.onSlotChange(stack, stack2);
			}
		});
		this.addSlotToContainer(new Slot(dragon.inv, 2, 8, 36) {
			@Override
			public boolean isItemValid(ItemStack stack) {
				return super.isItemValid(stack) && EntityDragonBase.isAllowedInSlot(2, stack);
			}

			@Override
			public void onSlotChange(ItemStack stack, ItemStack stack2) {
				dragon.worldObj.playSound((EntityPlayer) null, dragon.posX, dragon.posY, dragon.posZ, SoundEvents.entity_horse_armor, dragon.getSoundCategory(), 1.0F, 1.0F + (dragon.getRNG().nextFloat() - dragon.getRNG().nextFloat()) * 0.2F);
				super.onSlotChange(stack, stack2);
			}
		});
		this.addSlotToContainer(new Slot(dragon.inv, 3, 153, 18) {
			@Override
			public boolean isItemValid(ItemStack stack) {
				return super.isItemValid(stack) && EntityDragonBase.isAllowedInSlot(3, stack);
			}

			@Override
			public void onSlotChange(ItemStack stack, ItemStack stack2) {
				dragon.worldObj.playSound((EntityPlayer) null, dragon.posX, dragon.posY, dragon.posZ, SoundEvents.entity_horse_armor, dragon.getSoundCategory(), 1.0F, 1.0F + (dragon.getRNG().nextFloat() - dragon.getRNG().nextFloat()) * 0.2F);
				super.onSlotChange(stack, stack2);
			}
		});
		this.addSlotToContainer(new Slot(dragon.inv, 4, 153, 36) {
			@Override
			public boolean isItemValid(ItemStack stack) {
				return super.isItemValid(stack) && EntityDragonBase.isAllowedInSlot(4, stack);
			}

			@Override
			public void onSlotChange(ItemStack stack, ItemStack stack2) {
				dragon.worldObj.playSound((EntityPlayer) null, dragon.posX, dragon.posY, dragon.posZ, SoundEvents.entity_horse_armor, dragon.getSoundCategory(), 1.0F, 1.0F + (dragon.getRNG().nextFloat() - dragon.getRNG().nextFloat()) * 0.2F);
				super.onSlotChange(stack, stack2);
			}
		});
		int j;
		int k;
		for (j = 0; j < 3; ++j) {
			for (k = 0; k < 9; ++k) {
				this.addSlotToContainer(new Slot(player.inventory, k + j * 9 + 9, 8 + k * 18, 128 + j * 18 + i));
			}
		}

		for (j = 0; j < 9; ++j) {
			this.addSlotToContainer(new Slot(player.inventory, j, 8 + j * 18, 186 + i));
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return this.dragonInv.isUseableByPlayer(playerIn) && this.dragon.isEntityAlive() && this.dragon.getDistanceToEntity(playerIn) < 8.0F;
	}

	/**
	 * Take a stack from the specified inventory slot.
	 */
	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
		ItemStack itemstack = null;
		Slot slot = this.inventorySlots.get(index);

		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (index < this.dragonInv.getSizeInventory()) {
				if (!this.mergeItemStack(itemstack1, this.dragonInv.getSizeInventory(), this.inventorySlots.size(), true)) {
					return null;
				}
			} else if (this.getSlot(1).isItemValid(itemstack1) && !this.getSlot(1).getHasStack()) {
				if (!this.mergeItemStack(itemstack1, 1, 2, false)) {
					return null;
				}
			} else if (this.getSlot(0).isItemValid(itemstack1)) {
				if (!this.mergeItemStack(itemstack1, 0, 1, false)) {
					return null;
				}
			} else if (this.dragonInv.getSizeInventory() <= 2 || !this.mergeItemStack(itemstack1, 2, this.dragonInv.getSizeInventory(), false)) {
				return null;
			}

			if (itemstack1.stackSize == 0) {
				slot.putStack((ItemStack) null);
			} else {
				slot.onSlotChanged();
			}
		}

		return itemstack;
	}

	/**
	 * Called when the container is closed.
	 */
	@Override
	public void onContainerClosed(EntityPlayer playerIn) {
		super.onContainerClosed(playerIn);
		this.dragonInv.closeInventory(playerIn);
	}
}