package com.github.alexthe666.iceandfire.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class SlotLectern extends Slot {

    public SlotLectern(IInventory inv, int slotIndex, int xPosition, int yPosition) {
        super(inv, slotIndex, xPosition, yPosition);
    }

    @Override
    public void setChanged() {
        this.container.setChanged();
    }

    @Override
    public ItemStack onTake(PlayerEntity playerIn, ItemStack stack) {
        this.checkTakeAchievements(stack);
        return super.onTake(playerIn, stack);
    }

    /**
     * the itemStack passed in is the output - ie, iron ingots, and pickaxes,
     * not ore and wood. Typically increases an internal count then calls
     * onCrafting(item).
     */
    @Override
    protected void onQuickCraft(ItemStack stack, int amount) {
        this.checkTakeAchievements(stack);
    }

    /**
     * the itemStack passed in is the output - ie, iron ingots, and pickaxes,
     * not ore and wood.
     */
    @Override
    protected void checkTakeAchievements(ItemStack stack) {
        // thePlayer.addStat(StatList.objectCraftStats[Item.getIdFromItem(stack.getItem())],
        // stack.stackSize);
    }
}