package com.github.alexthe666.iceandfire.entity.tile;

import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.enums.EnumBestiaryPages;
import com.github.alexthe666.iceandfire.inventory.ContainerLectern;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class TileEntityLectern extends TileEntity implements ITickable, ISidedInventory {
    private static final int[] slotsTop = new int[]{0};
    private static final int[] slotsSides = new int[]{1};
    private static final int[] slotsBottom = new int[]{0};
    private static final Random RANDOM = new Random();
    private static final ArrayList<EnumBestiaryPages> EMPTY_LIST = new ArrayList<>();
    public float pageFlip;
    public float pageFlipPrev;
    public float pageHelp1;
    public float pageHelp2;
    public EnumBestiaryPages[] selectedPages = new EnumBestiaryPages[3];
    net.minecraftforge.items.IItemHandler handlerUp = new net.minecraftforge.items.wrapper.SidedInvWrapper(this, net.minecraft.util.EnumFacing.UP);
    net.minecraftforge.items.IItemHandler handlerDown = new net.minecraftforge.items.wrapper.SidedInvWrapper(this, EnumFacing.DOWN);
    private Random localRand = new Random();
    private NonNullList<ItemStack> stacks = NonNullList.withSize(3, ItemStack.EMPTY);

    @Override
    public void update() {
        float f1 = this.pageHelp1;
        do {
            this.pageHelp1 += RANDOM.nextInt(4) - RANDOM.nextInt(4);
        } while (f1 == this.pageHelp1);
        this.pageFlipPrev = this.pageFlip;
        float f = (this.pageHelp1 - this.pageFlip) * 0.04F;
        float f3 = 0.02F;
        f = MathHelper.clamp(f, -f3, f3);
        this.pageHelp2 += (f - this.pageHelp2) * 0.9F;
        this.pageFlip += this.pageHelp2;

    }

    @Override
    public int getSizeInventory() {
        return 2;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return this.stacks.get(index);
    }

    private boolean canAddPage() {
        if (this.stacks.get(0).isEmpty()) {
            return false;
        } else {
            ItemStack itemstack = this.stacks.get(0).copy();

            if (itemstack.isEmpty()) {
                return false;
            }
            if (itemstack.getItem() != IafItemRegistry.bestiary) {
                return false;
            }

            if (itemstack.getItem() == IafItemRegistry.bestiary) {
                List list = EnumBestiaryPages.possiblePages(itemstack);
                if (list == null || list.isEmpty()) {
                    return false;
                }
            }
            if (this.stacks.get(2).isEmpty())
                return true;
            int result = stacks.get(2).getCount() + itemstack.getCount();
            return result <= getInventoryStackLimit() && result <= this.stacks.get(2).getMaxStackSize();
        }
    }

    private ArrayList<EnumBestiaryPages> getPossiblePages() {
        List list = EnumBestiaryPages.possiblePages(this.stacks.get(0));
        if (list != null && !list.isEmpty()) {
            return (ArrayList<EnumBestiaryPages>) list;
        }
        return EMPTY_LIST;
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        if (!this.stacks.get(index).isEmpty()) {
            ItemStack itemstack;

            if (this.stacks.get(index).getCount() <= count) {
                itemstack = this.stacks.get(index);
                this.stacks.set(index, ItemStack.EMPTY);
                return itemstack;
            } else {
                itemstack = this.stacks.get(index).splitStack(count);

                if (this.stacks.get(index).getCount() == 0) {
                    this.stacks.set(index, ItemStack.EMPTY);
                }

                return itemstack;
            }
        } else {
            return ItemStack.EMPTY;
        }
    }

    public ItemStack getStackInSlotOnClosing(int index) {
        if (!this.stacks.get(index).isEmpty()) {
            ItemStack itemstack = this.stacks.get(index);
            this.stacks.set(index, ItemStack.EMPTY);
            return itemstack;
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        boolean flag = !stack.isEmpty() && stack.isItemEqual(this.stacks.get(index)) && ItemStack.areItemStackTagsEqual(stack, this.stacks.get(index));
        this.stacks.set(index, stack);

        if (!stack.isEmpty() && stack.getCount() > this.getInventoryStackLimit()) {
            stack.setCount(this.getInventoryStackLimit());
        }
        if (index == 0 && !flag) {
            this.markDirty();
            selectedPages = randomizePages();
        }
    }

    public EnumBestiaryPages[] randomizePages() {
        if (stacks.get(0).getItem() == IafItemRegistry.bestiary) {
            List<EnumBestiaryPages> possibleList = getPossiblePages();
            localRand.setSeed(this.world.getWorldTime());
            Collections.shuffle(possibleList, localRand);
            boolean flag = false;
            if (possibleList.size() > 0) {
                selectedPages[0] = possibleList.get(0);
            } else {
                flag = true;
                selectedPages[0] = null;
            }
            if (possibleList.size() > 1) {
                selectedPages[1] = possibleList.get(1);
            } else {
                selectedPages[1] = null;
            }
            if (possibleList.size() > 2) {
                selectedPages[2] = possibleList.get(2);
            } else {
                selectedPages[2] = null;
            }
        }
        return selectedPages;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.stacks = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(compound, this.stacks);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        ItemStackHelper.saveAllItems(compound, this.stacks);

        return compound;
    }

    @Override
    public void openInventory(EntityPlayer player) {
    }

    @Override
    public void closeInventory(EntityPlayer player) {
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return true;
    }

    @Override
    public int getField(int id) {
        if (id == 0) {
            return selectedPages[0] == null ? -1 : selectedPages[0].ordinal();
        }
        if (id == 1) {
            return selectedPages[1] == null ? -1 : selectedPages[1].ordinal();
        }
        if (id == 2) {
            return selectedPages[2] == null ? -1 : selectedPages[2].ordinal();
        }
        return 0;
    }

    @Override
    public void setField(int id, int value) {
        if (id == 0) {
            if (value < 0) {
                selectedPages[0] = null;
            } else {
                selectedPages[0] = EnumBestiaryPages.values()[Math.min(EnumBestiaryPages.values().length - 1, value)];
            }
        }
        if (id == 1) {
            if (value < 0) {
                selectedPages[1] = null;
            } else {
                selectedPages[1] = EnumBestiaryPages.values()[Math.min(EnumBestiaryPages.values().length - 1, value)];
            }
        }
        if (id == 2) {
            if (value < 0) {
                selectedPages[2] = null;
            } else {
                selectedPages[2] = EnumBestiaryPages.values()[Math.min(EnumBestiaryPages.values().length - 1, value)];
            }
        }
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        return true;
    }

    @Override
    public int getFieldCount() {
        return 3;
    }

    @Override
    public void clear() {
        this.stacks.clear();
    }

    @Override
    public String getName() {
        return "tile.iceandfire.lectern.name";
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
        return false;
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        return side == EnumFacing.DOWN ? slotsBottom : (side == EnumFacing.UP ? slotsTop : slotsSides);
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
        return this.isItemValidForSlot(index, itemStackIn);
    }

    public String getGuiID() {
        return "iceandfire:lectern";
    }

    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
        return new ContainerLectern(playerInventory, this);
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        return ItemStack.EMPTY;
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound tag = new NBTTagCompound();
        this.writeToNBT(tag);
        return new SPacketUpdateTileEntity(pos, 1, tag);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
        readFromNBT(packet.getNbtCompound());
    }

    @Override
    public ITextComponent getDisplayName() {
        return this.hasCustomName() ? new TextComponentString(this.getName()) : new TextComponentTranslation(this.getName());
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack itemstack : this.stacks) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    @javax.annotation.Nullable
    public <T> T getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, @javax.annotation.Nullable net.minecraft.util.EnumFacing facing) {
        if (facing != null && capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            if (facing == EnumFacing.DOWN)
                return (T) handlerDown;
            else
                return (T) handlerUp;
        return super.getCapability(capability, facing);
    }

    @Override
    public boolean hasCapability(net.minecraftforge.common.capabilities.Capability<?> capability, @Nullable net.minecraft.util.EnumFacing facing) {
        return getCapability(capability, facing) != null;
    }

}