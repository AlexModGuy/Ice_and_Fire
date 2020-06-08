package com.github.alexthe666.iceandfire.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;

public class ContainerPodium extends Container {
    private final IInventory podium;

    public ContainerPodium(int i, PlayerInventory playerInventory) {
        this(i, new Inventory(1), playerInventory, new IntArray(0));
    }


    public ContainerPodium(int id, IInventory furnaceInventory, PlayerInventory playerInventory, IIntArray vars) {
        super(IafContainerRegistry.PODIUM_CONTAINER, id);
        this.podium = furnaceInventory;
        furnaceInventory.openInventory(playerInventory.player);
        byte b0 = 51;
        int i;

        this.addSlot(new Slot(furnaceInventory, 0, 80, 20));

        for (i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, i * 18 + b0));
            }
        }

        for (i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 58 + b0));
        }
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return this.podium.isUsableByPlayer(playerIn);
    }

    /**
     * Take a stack from the specified inventory slot.
     */
    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index < this.podium.getSizeInventory()) {
                if (!this.mergeItemStack(itemstack1, this.podium.getSizeInventory(), this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 0, this.podium.getSizeInventory(), false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
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
    public void onContainerClosed(PlayerEntity playerIn) {
        super.onContainerClosed(playerIn);
        this.podium.closeInventory(playerIn);
    }
}