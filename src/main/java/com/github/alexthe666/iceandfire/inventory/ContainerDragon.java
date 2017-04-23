package com.github.alexthe666.iceandfire.inventory;

import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.item.ItemDragonArmor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;

public class ContainerDragon extends Container {
    private IInventory dragonInv;
    private EntityDragonBase dragon;

    public ContainerDragon(final EntityDragonBase dragon, EntityPlayer player) {

        this.dragonInv = dragon.dragonInv;
        this.dragon = dragon;
        byte b0 = 3;
        dragonInv.openInventory(player);
        int i = (b0 - 4) * 18;
        this.addSlotToContainer(new Slot(dragon.dragonInv, 0, 8, 18) {
            @Override
            public boolean isItemValid(ItemStack stack) {
                return super.isItemValid(stack) && !stack.isEmpty() && stack.getItem() != null && stack.getItem() instanceof ItemDragonArmor && stack.getMetadata() == 0;
            }
        });
        this.addSlotToContainer(new Slot(dragon.dragonInv, 1, 8, 36) {
            @Override
            public boolean isItemValid(ItemStack stack) {
                return super.isItemValid(stack) && !stack.isEmpty() && stack.getItem() != null && stack.getItem() instanceof ItemDragonArmor && stack.getMetadata() == 1;
            }
        });
        this.addSlotToContainer(new Slot(dragon.dragonInv, 2, 153, 18) {
            @Override
            public boolean isItemValid(ItemStack stack) {
                return super.isItemValid(stack) && !stack.isEmpty() && stack.getItem() != null && stack.getItem() instanceof ItemDragonArmor && stack.getMetadata() == 2;
            }
        });
        this.addSlotToContainer(new Slot(dragon.dragonInv, 3, 153, 36) {
            @Override
            public boolean isItemValid(ItemStack stack) {
                return super.isItemValid(stack) && !stack.isEmpty() && stack.getItem() != null && stack.getItem() instanceof ItemDragonArmor && stack.getMetadata() == 3;
            }
        });
        int j;
        int k;
        for (j = 0; j < 3; ++j) {
            for (k = 0; k < 9; ++k) {
                this.addSlotToContainer(new Slot(player.inventory, k + j * 9 + 9, 8 + k * 18, 150 + j * 18 + i));
            }
        }

        for (j = 0; j < 9; ++j) {
            this.addSlotToContainer(new Slot(player.inventory, j, 8 + j * 18, 208 + i));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return this.dragonInv.isUsableByPlayer(playerIn) && this.dragon.isEntityAlive() && this.dragon.getDistanceToEntity(playerIn) < 8.0F;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (index < this.dragonInv.getSizeInventory()) {
                if (!this.mergeItemStack(itemstack1, this.dragonInv.getSizeInventory(), this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.getSlot(1).isItemValid(itemstack1) && !this.getSlot(1).getHasStack()) {
                if (!this.mergeItemStack(itemstack1, 1, 2, false)) {
                    return ItemStack.EMPTY;
                }

            } else if (this.getSlot(0).isItemValid(itemstack1)) {
                if (!this.mergeItemStack(itemstack1, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.dragonInv.getSizeInventory() <= 2 || !this.mergeItemStack(itemstack1, 2, this.dragonInv.getSizeInventory(), false)) {
                return ItemStack.EMPTY;
            }
            if (itemstack1.isEmpty()) {
                slot.putStack((ItemStack) ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
        }
        return itemstack;
    }

    @Override
    public void onContainerClosed(EntityPlayer playerIn) {
        super.onContainerClosed(playerIn);
        this.dragonInv.closeInventory(playerIn);
    }

}