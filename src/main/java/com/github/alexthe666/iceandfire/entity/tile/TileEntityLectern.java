package com.github.alexthe666.iceandfire.entity.tile;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.enums.EnumBestiaryPages;
import com.github.alexthe666.iceandfire.inventory.ContainerLectern;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.item.ItemBestiary;
import com.github.alexthe666.iceandfire.message.MessageUpdateLectern;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class TileEntityLectern extends LockableTileEntity implements ITickableTileEntity, ISidedInventory {
    private static final int[] slotsTop = new int[]{0};
    private static final int[] slotsSides = new int[]{1};
    private static final int[] slotsBottom = new int[]{0};
    private static final Random RANDOM = new Random();
    private static final ArrayList<EnumBestiaryPages> EMPTY_LIST = new ArrayList<>();
    public final IIntArray furnaceData = new IIntArray() {
        @Override
        public int get(int index) {
            return 0;
        }

        @Override
        public void set(int index, int value) {

        }

        @Override
        public int getCount() {
            return 0;
        }
    };
    public float pageFlip;
    public float pageFlipPrev;
    public float pageHelp1;
    public float pageHelp2;
    public EnumBestiaryPages[] selectedPages = new EnumBestiaryPages[3];
    net.minecraftforge.common.util.LazyOptional<? extends net.minecraftforge.items.IItemHandler>[] handlers =
        net.minecraftforge.items.wrapper.SidedInvWrapper.create(this, Direction.UP, Direction.DOWN);
    private final Random localRand = new Random();
    private NonNullList<ItemStack> stacks = NonNullList.withSize(3, ItemStack.EMPTY);

    public TileEntityLectern() {
        super(IafTileEntityRegistry.IAF_LECTERN.get());
    }

    public static String getGuiID() {
        return IceAndFire.MODID + ":lectern";
    }

    @Override
    public void tick() {
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
    public int getContainerSize() {
        return 2;
    }

    @Override
    public ItemStack getItem(int index) {
        return this.stacks.get(index);
    }

    private List<EnumBestiaryPages> getPossiblePages() {
        final List<EnumBestiaryPages> list = EnumBestiaryPages.possiblePages(this.stacks.get(0));
        if (list != null && !list.isEmpty()) {
            return list;
        }
        return EMPTY_LIST;
    }

    @Override
    public ItemStack removeItem(int index, int count) {
        if (!this.stacks.get(index).isEmpty()) {
            ItemStack itemstack;

            if (this.stacks.get(index).getCount() <= count) {
                itemstack = this.stacks.get(index);
                this.stacks.set(index, ItemStack.EMPTY);
                return itemstack;
            } else {
                itemstack = this.stacks.get(index).split(count);

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
    public void setItem(int index, ItemStack stack) {
        boolean flag = !stack.isEmpty() && stack.sameItem(this.stacks.get(index)) && ItemStack.tagMatches(stack, this.stacks.get(index));
        this.stacks.set(index, stack);

        if (!stack.isEmpty() && stack.getCount() > this.getMaxStackSize()) {
            stack.setCount(this.getMaxStackSize());
        }
        if (index == 0 && !flag) {
            this.setChanged();
            selectedPages = randomizePages(getItem(0), getItem(1));
        }
    }

    public EnumBestiaryPages[] randomizePages(ItemStack bestiary, ItemStack manuscript) {
        if (!level.isClientSide) {
            if (bestiary.getItem() == IafItemRegistry.BESTIARY) {
                List<EnumBestiaryPages> possibleList = getPossiblePages();
                localRand.setSeed(this.level.getGameTime());
                Collections.shuffle(possibleList, localRand);
                if (!possibleList.isEmpty()) {
                    selectedPages[0] = possibleList.get(0);
                } else {
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
            int page1 = selectedPages[0] == null ? -1 : selectedPages[0].ordinal();
            int page2 = selectedPages[1] == null ? -1 : selectedPages[1].ordinal();
            int page3 = selectedPages[2] == null ? -1 : selectedPages[2].ordinal();
            IceAndFire.sendMSGToAll(new MessageUpdateLectern(worldPosition.asLong(), page1, page2, page3, false, 0));
        }
        return selectedPages;
    }

    @Override
    public void load(BlockState blockstate, CompoundNBT compound) {
        super.load(blockstate, compound);
        this.stacks = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(compound, this.stacks);

    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        super.save(compound);
        ItemStackHelper.saveAllItems(compound, this.stacks);
        return compound;
    }

    @Override
    public void startOpen(PlayerEntity player) {
    }

    @Override
    public void stopOpen(PlayerEntity player) {
    }

    @Override
    public boolean canPlaceItem(int index, ItemStack stack) {
        if (stack.isEmpty())
            return false;
        if (index == 0)
            return stack.getItem() instanceof ItemBestiary;
        if (index == 1)
            return stack.getItem() == IafItemRegistry.MANUSCRIPT;
        return false;
    }

    @Override
    public int getMaxStackSize() {
        return 64;
    }

    @Override
    public boolean stillValid(PlayerEntity player) {
        return true;
    }

    @Override
    public void clearContent() {
        this.stacks.clear();
    }

    @Override
    public ITextComponent getName() {
        return new TranslationTextComponent("block.iceandfire.lectern");
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        return false;
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        return side == Direction.DOWN ? slotsBottom : (side == Direction.UP ? slotsTop : slotsSides);
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack itemStackIn, Direction direction) {
        return this.canPlaceItem(index, itemStackIn);
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        return ItemStack.EMPTY;
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

    @Override
    protected ITextComponent getDefaultName() {
        return getName();
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player) {
        return null;
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

    @Override
    public <T> net.minecraftforge.common.util.LazyOptional<T> getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, @Nullable Direction facing) {
        if (!this.remove && facing != null && capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if (facing == Direction.DOWN)
                return handlers[1].cast();
            else
                return handlers[0].cast();
        }
        return super.getCapability(capability, facing);
    }

    @Nullable
    @Override
    public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity player) {
        return new ContainerLectern(id, this, playerInventory, furnaceData);
    }


}