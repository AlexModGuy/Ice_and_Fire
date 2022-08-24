package com.github.alexthe666.iceandfire.entity.tile;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;

public class TileEntityMyrmexCocoon extends LockableLootTileEntity {

    private NonNullList<ItemStack> chestContents = NonNullList.withSize(18, ItemStack.EMPTY);

    public TileEntityMyrmexCocoon() {
        super(IafTileEntityRegistry.MYRMEX_COCOON.get());
    }

    @Override
    public int getContainerSize() {
        return 18;
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack itemstack : this.chestContents) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }
        return true;
    }


    @Override
    public void load(BlockState bs, CompoundNBT compound) {
        super.load(bs, compound);
        this.chestContents = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);

        if (!this.tryLoadLootTable(compound)) {
            ItemStackHelper.loadAllItems(compound, this.chestContents);
        }
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        super.save(compound);
        if (!this.trySaveLootTable(compound)) {
            ItemStackHelper.saveAllItems(compound, this.chestContents);
        }

        return compound;
    }

    @Override
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent("container.myrmex_cocoon");
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player) {
        return new ChestContainer(ContainerType.GENERIC_9x2, id, player, this, 2);
    }

    @Nullable
    @Override
    public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity player) {
        return new ChestContainer(ContainerType.GENERIC_9x2, id, playerInventory, this, 2);
    }


    @Override
    public int getMaxStackSize() {
        return 64;
    }


    @Override
    protected NonNullList<ItemStack> getItems() {
        return this.chestContents;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> itemsIn) {

    }

    @Override
    public void startOpen(PlayerEntity player) {
        this.unpackLootTable(null);
        player.level.playLocalSound(this.worldPosition.getX(), this.worldPosition.getY(), this.worldPosition.getZ(), SoundEvents.SLIME_JUMP, SoundCategory.BLOCKS, 1, 1, false);
    }

    @Override
    public void stopOpen(PlayerEntity player) {
        this.unpackLootTable(null);
        player.level.playLocalSound(this.worldPosition.getX(), this.worldPosition.getY(), this.worldPosition.getZ(), SoundEvents.SLIME_SQUISH, SoundCategory.BLOCKS, 1, 1, false);
    }

    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(worldPosition, 1, getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket packet) {
        load(this.getBlockState(), packet.getTag());
    }

    @Override
    public CompoundNBT getUpdateTag() {
        return this.save(new CompoundNBT());
    }

    public boolean isFull(ItemStack heldStack) {
        for (ItemStack itemstack : chestContents) {
            if (itemstack.isEmpty() || heldStack != null && !heldStack.isEmpty() && itemstack.sameItem(heldStack) && itemstack.getCount() + heldStack.getCount() < itemstack.getMaxStackSize()) {
                return false;
            }
        }
        return true;
    }
}
