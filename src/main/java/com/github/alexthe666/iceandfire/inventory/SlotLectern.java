package com.github.alexthe666.iceandfire.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class SlotLectern extends Slot {

    public SlotLectern(Container inv, int slotIndex, int xPosition, int yPosition) {
        super(inv, slotIndex, xPosition, yPosition);
    }

    @Override
    public void setChanged() {
        this.container.setChanged();
    }

    @Override
    public void onTake(@NotNull Player playerIn, @NotNull ItemStack stack) {
        this.checkTakeAchievements(stack);
        super.onTake(playerIn, stack);
    }

    /**
     * the itemStack passed in is the output - ie, iron ingots, and pickaxes,
     * not ore and wood. Typically increases an internal count then calls
     * onCrafting(item).
     */
    @Override
    protected void onQuickCraft(@NotNull ItemStack stack, int amount) {
        this.checkTakeAchievements(stack);
    }

    /**
     * the itemStack passed in is the output - ie, iron ingots, and pickaxes,
     * not ore and wood.
     */
    @Override
    protected void checkTakeAchievements(@NotNull ItemStack stack) {
        // thePlayer.addStat(StatList.objectCraftStats[Item.getIdFromItem(stack.getItem())],
        // stack.stackSize);
    }
}