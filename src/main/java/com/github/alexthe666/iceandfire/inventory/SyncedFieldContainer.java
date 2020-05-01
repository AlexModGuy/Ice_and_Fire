package com.github.alexthe666.iceandfire.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class SyncedFieldContainer extends Container {
    private final int[] fields;
    private final IInventory inventory;

    public SyncedFieldContainer(IInventory inventory) {
        this.inventory = inventory;
        this.fields = new int[inventory.getFieldCount()];
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        for (IContainerListener listener : this.listeners) {
            for (int fieldIndex = 0; fieldIndex < this.fields.length; fieldIndex++) {
                int field = this.inventory.getField(fieldIndex);
                if (field != this.fields[fieldIndex]) {
                    listener.sendWindowProperty(this, fieldIndex, field);
                }
            }
        }
        for (int fieldIndex = 0; fieldIndex < this.fields.length; fieldIndex++) {
            this.fields[fieldIndex] = this.inventory.getField(fieldIndex);
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void updateProgressBar(int id, int data) {
        this.inventory.setField(id, data);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex) {
        ItemStack transferred = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(slotIndex);
        int otherSlots = this.inventorySlots.size() - 36;
        if (slot != null && slot.getHasStack()) {
            ItemStack current = slot.getStack();
            transferred = current.copy();
            if (slotIndex < otherSlots) {
                if (!this.mergeItemStack(current, otherSlots, this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(current, 0, otherSlots, false)) {
                return ItemStack.EMPTY;
            }
            if (current.getCount() == 0) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
        }
        return transferred;
    }
}
