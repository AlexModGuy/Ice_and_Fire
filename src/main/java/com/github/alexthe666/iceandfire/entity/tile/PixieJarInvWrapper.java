package com.github.alexthe666.iceandfire.entity.tile;

import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nonnull;

public class PixieJarInvWrapper implements IItemHandlerModifiable {

    private TileEntityJar tile;
    private EnumFacing side;

    public PixieJarInvWrapper(TileEntityJar tile, EnumFacing side) {
        this.tile = tile;
        this.side = side;
    }

    @Override
    public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
        // this.tile.hasProduced = false;
    }

    @Override
    public int getSlots() {
        return 1;
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int slot) {
        return this.tile.hasProduced ? new ItemStack(IafItemRegistry.pixie_dust) : ItemStack.EMPTY;
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        return stack;
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (this.tile.hasProduced) {
            if (!simulate)
                this.tile.hasProduced = false;
            return new ItemStack(IafItemRegistry.pixie_dust);
        }
        return ItemStack.EMPTY;
    }

    @Override
    public int getSlotLimit(int slot) {
        return 1;
    }
}
