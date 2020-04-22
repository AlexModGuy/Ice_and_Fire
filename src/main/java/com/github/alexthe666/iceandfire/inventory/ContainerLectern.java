package com.github.alexthe666.iceandfire.inventory;

import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityLectern;
import com.github.alexthe666.iceandfire.enums.EnumBestiaryPages;
import com.github.alexthe666.iceandfire.item.ItemBestiary;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerLectern extends SyncedFieldContainer {
    private final IInventory tileFurnace;
    private int[] possiblePagesInt = new int[3];

    public ContainerLectern(InventoryPlayer playerInv, IInventory furnaceInventory) {
        super(furnaceInventory);
        this.tileFurnace = furnaceInventory;
        this.addSlotToContainer(new Slot(furnaceInventory, 0, 15, 47){
            @Override
            public boolean isItemValid(ItemStack stack) {
                return super.isItemValid(stack) && !stack.isEmpty() && stack.getItem() instanceof ItemBestiary;
            }
        });
        this.addSlotToContainer(new Slot(furnaceInventory, 1, 35, 47){
            @Override
            public boolean isItemValid(ItemStack stack) {
                return super.isItemValid(stack) && !stack.isEmpty() && stack.getItem() == IafItemRegistry.manuscript;
            }
        });
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        for (int k = 0; k < 9; ++k) {
            this.addSlotToContainer(new Slot(playerInv, k, 8 + k * 18, 142));
        }
    }

    @Override
    public void addListener(IContainerListener listener) {
        super.addListener(listener);
        listener.sendAllWindowProperties(this, this.tileFurnace);
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int id, int data) {
        this.tileFurnace.setField(id, data);
    }

    public void onUpdate() {
        possiblePagesInt[0] = this.tileFurnace.getField(0);
        possiblePagesInt[1] = this.tileFurnace.getField(1);
        possiblePagesInt[2] = this.tileFurnace.getField(2);
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return this.tileFurnace.isUsableByPlayer(playerIn);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (index < this.tileFurnace.getSizeInventory()) {
                if (!this.mergeItemStack(itemstack1, this.tileFurnace.getSizeInventory(), this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.getSlot(0).isItemValid(itemstack1) && !this.getSlot(0).getHasStack()) {
                if (!this.mergeItemStack(itemstack1, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }

            } else if (this.getSlot(1).isItemValid(itemstack1) && !this.getSlot(1).getHasStack()) {
                if (!this.mergeItemStack(itemstack1, 1, 2, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.tileFurnace.getSizeInventory() <= 5 || !this.mergeItemStack(itemstack1, 5, this.tileFurnace.getSizeInventory(), false)) {
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

    public int getManuscriptAmount() {
        ItemStack itemstack = this.tileFurnace.getStackInSlot(1);
        return itemstack.isEmpty() || itemstack.getItem() != IafItemRegistry.manuscript ? 0 : itemstack.getCount();
    }

    public EnumBestiaryPages[] getPossiblePages() {
        possiblePagesInt[0] = this.tileFurnace.getField(0);
        possiblePagesInt[1] = this.tileFurnace.getField(1);
        possiblePagesInt[2] = this.tileFurnace.getField(2);
        EnumBestiaryPages[] pages = new EnumBestiaryPages[3];
        if (this.tileFurnace.getStackInSlot(0).getItem() == IafItemRegistry.bestiary) {
            if (possiblePagesInt[0] < 0) {
                pages[0] = null;
            } else {
                pages[0] = EnumBestiaryPages.values()[Math.min(EnumBestiaryPages.values().length, possiblePagesInt[0])];
            }
            if (possiblePagesInt[1] < 0) {
                pages[1] = null;
            } else {
                pages[1] = EnumBestiaryPages.values()[Math.min(EnumBestiaryPages.values().length, possiblePagesInt[1])];
            }
            if (possiblePagesInt[2] < 0) {
                pages[2] = null;
            } else {
                pages[2] = EnumBestiaryPages.values()[Math.min(EnumBestiaryPages.values().length, possiblePagesInt[2])];
            }
        }
        return pages;
    }

    public boolean enchantItem(EntityPlayer playerIn, int id) {
        possiblePagesInt[0] = this.tileFurnace.getField(0);
        possiblePagesInt[1] = this.tileFurnace.getField(1);
        possiblePagesInt[2] = this.tileFurnace.getField(2);
        ItemStack itemstack = this.tileFurnace.getStackInSlot(0);
        ItemStack itemstack1 = this.tileFurnace.getStackInSlot(1);
        int i = 3;
        boolean didEnchant = false;
        if ((itemstack1.isEmpty() || itemstack1.getCount() < i) && !playerIn.capabilities.isCreativeMode) {
            return false;
        } else if (this.possiblePagesInt[id] > 0 && !itemstack.isEmpty()) {
            EnumBestiaryPages page = getPossiblePages()[MathHelper.clamp(id, 0, 2)];
            if (page != null) {
                if (itemstack.getItem() == IafItemRegistry.bestiary) {
                    didEnchant = EnumBestiaryPages.addPage(page, itemstack);
                    this.tileFurnace.setInventorySlotContents(0, itemstack);
                    if (this.tileFurnace instanceof TileEntityLectern) {
                        ((TileEntityLectern) this.tileFurnace).randomizePages();
                    }
                }
                if (!playerIn.capabilities.isCreativeMode && didEnchant) {
                    itemstack1.shrink(i);
                    if (itemstack1.isEmpty()) {
                        this.tileFurnace.setInventorySlotContents(1, ItemStack.EMPTY);
                    }
                }
                this.tileFurnace.markDirty();
                //this.xpSeed = playerIn.getXPSeed();
                this.onCraftMatrixChanged(this.tileFurnace);
                playerIn.world.playSound(null, playerIn.getPosition(), IafSoundRegistry.BESTIARY_PAGE, SoundCategory.BLOCKS, 1.0F, playerIn.world.rand.nextFloat() * 0.1F + 0.9F);
            }
            onUpdate();
            return true;
        } else {
            return false;
        }
    }
}