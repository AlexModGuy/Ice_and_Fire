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
    public final IInventory podium;

    public ContainerPodium(int i, PlayerInventory playerInventory) {
        this(i, new Inventory(1), playerInventory, new IntArray(0));
    }


    public ContainerPodium(int id, IInventory furnaceInventory, PlayerInventory playerInventory, IIntArray vars) {
        super(IafContainerRegistry.PODIUM_CONTAINER.get(), id);
        this.podium = furnaceInventory;
        furnaceInventory.startOpen(playerInventory.player);
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
    public boolean stillValid(PlayerEntity playerIn) {
        return this.podium.stillValid(playerIn);
    }

    /**
     * Take a stack from the specified inventory slot.
     */
    @Override
    public ItemStack quickMoveStack(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();

            if (index < this.podium.getContainerSize()) {
                if (!this.moveItemStackTo(itemstack1, this.podium.getContainerSize(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 0, this.podium.getContainerSize(), false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return itemstack;
    }

    /**
     * Called when the container is closed.
     */
    @Override
    public void removed(PlayerEntity playerIn) {
        super.removed(playerIn);
        this.podium.stopOpen(playerIn);
    }
}