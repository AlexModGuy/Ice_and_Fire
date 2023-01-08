package com.github.alexthe666.iceandfire.entity.tile;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.block.BlockDragonforgeBricks;
import com.github.alexthe666.iceandfire.block.BlockDragonforgeCore;
import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.entity.DragonType;
import com.github.alexthe666.iceandfire.inventory.ContainerDragonForge;
import com.github.alexthe666.iceandfire.message.MessageUpdateDragonforge;
import com.github.alexthe666.iceandfire.recipe.DragonForgeRecipe;
import com.github.alexthe666.iceandfire.recipe.IafRecipeRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.IntArray;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class TileEntityDragonforge extends LockableTileEntity implements ITickableTileEntity, ISidedInventory {

    private static final int[] SLOTS_TOP = new int[]{0, 1};
    private static final int[] SLOTS_BOTTOM = new int[]{2};
    private static final int[] SLOTS_SIDES = new int[]{0, 1};
    private static final Direction[] HORIZONTALS = new Direction[]{
        Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST
    };
    public int fireType;
    public int cookTime;
    public int lastDragonFlameTimer = 0;
    net.minecraftforge.common.util.LazyOptional<? extends IItemHandler>[] handlers = SidedInvWrapper
        .create(this, Direction.UP, Direction.DOWN, Direction.NORTH);
    private NonNullList<ItemStack> forgeItemStacks = NonNullList.withSize(3, ItemStack.EMPTY);
    private boolean prevAssembled;
    private boolean canAddFlameAgain = true;

    public TileEntityDragonforge() {
        super(IafTileEntityRegistry.DRAGONFORGE_CORE.get());
    }

    public TileEntityDragonforge(int fireType) {
        super(IafTileEntityRegistry.DRAGONFORGE_CORE.get());
        this.fireType = fireType;
    }

    @Override
    public int getSizeInventory() {
        return this.forgeItemStacks.size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack itemstack : this.forgeItemStacks) {
            if (!itemstack.isEmpty())
                return false;
        }

        return true;
    }

    private void updateGrills(boolean grill) {
        for (Direction facing : HORIZONTALS) {
            BlockPos grillPos = this.getPos().offset(facing);
            if (grillMatches(world.getBlockState(grillPos).getBlock())) {
                BlockState grillState = getGrillBlock().getDefaultState().with(BlockDragonforgeBricks.GRILL, grill);
                if (world.getBlockState(grillPos) != grillState) {
                    world.setBlockState(grillPos, grillState);
                }
            }
        }
    }

    public Block getGrillBlock() {
        switch (fireType) {
            case 1:
                return IafBlockRegistry.DRAGONFORGE_ICE_BRICK;
            case 2:
                return IafBlockRegistry.DRAGONFORGE_LIGHTNING_BRICK;
            default:
                return IafBlockRegistry.DRAGONFORGE_FIRE_BRICK; // isFire == 0
        }
    }

    public boolean grillMatches(Block block) {
        switch (fireType) {
            case 0:
                return block == IafBlockRegistry.DRAGONFORGE_FIRE_BRICK;
            case 1:
                return block == IafBlockRegistry.DRAGONFORGE_ICE_BRICK;
            case 2:
                return block == IafBlockRegistry.DRAGONFORGE_LIGHTNING_BRICK;
            default:
                return false;
        }
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return this.forgeItemStacks.get(index);
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        return ItemStackHelper.getAndSplit(this.forgeItemStacks, index, count);
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        return ItemStackHelper.getAndRemove(this.forgeItemStacks, index);
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        ItemStack itemstack = this.forgeItemStacks.get(index);
        boolean flag = !stack.isEmpty() && stack.isItemEqual(itemstack)
            && ItemStack.areItemStackTagsEqual(stack, itemstack);
        this.forgeItemStacks.set(index, stack);

        if (stack.getCount() > this.getInventoryStackLimit()) {
            stack.setCount(this.getInventoryStackLimit());
        }

        if (index == 0 && !flag
            || this.cookTime > this.getMaxCookTime()) {
            this.cookTime = 0;
            this.markDirty();
        }
    }

    @Override
    public void read(BlockState state, CompoundNBT compound) {
        super.read(state, compound);
        this.forgeItemStacks = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(compound, this.forgeItemStacks);
        this.cookTime = compound.getInt("CookTime");
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        compound.putInt("CookTime", (short) this.cookTime);
        ItemStackHelper.saveAllItems(compound, this.forgeItemStacks);
        return compound;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    public boolean isBurning() {
        return this.cookTime > 0;
    }

    public int getFireType(Block block) {
        if (block == IafBlockRegistry.DRAGONFORGE_FIRE_CORE
            || block == IafBlockRegistry.DRAGONFORGE_FIRE_CORE_DISABLED) {
            return 0;
        }
        if (block == IafBlockRegistry.DRAGONFORGE_ICE_CORE || block == IafBlockRegistry.DRAGONFORGE_ICE_CORE_DISABLED) {
            return 1;
        }
        if (block == IafBlockRegistry.DRAGONFORGE_LIGHTNING_CORE
            || block == IafBlockRegistry.DRAGONFORGE_LIGHTNING_CORE_DISABLED) {
            return 2;
        }
        return 0;
    }

    public String getTypeID() {
        switch (getFireType(this.getBlockState().getBlock())) {
            case 0:
                return "fire";
            case 1:
                return "ice";
            case 2:
                return "lightning";
            default:
                return "";
        }
    }

    @Override
    public void tick() {
        boolean flag = this.isBurning();
        boolean flag1 = false;
        fireType = getFireType(this.getBlockState().getBlock());
        if (lastDragonFlameTimer > 0) {
            lastDragonFlameTimer--;
        }
        updateGrills(assembled());
        if (!world.isRemote) {
            if (prevAssembled != assembled()) {
                BlockDragonforgeCore.setState(fireType, prevAssembled, world, pos);
            }
            prevAssembled = this.assembled();
            if (!assembled())
                return;
        }
        if (cookTime > 0 && this.canSmelt() && lastDragonFlameTimer == 0) {
            this.cookTime--;
        }
        if (this.getStackInSlot(0).isEmpty() && !world.isRemote) {
            this.cookTime = 0;
        }
        if (!this.world.isRemote) {
            if (this.isBurning()) {
                if (this.canSmelt()) {
                    ++this.cookTime;
                    if (this.cookTime >= getMaxCookTime()) {
                        this.cookTime = 0;
                        this.smeltItem();
                        flag1 = true;
                    }
                } else {
                    if (cookTime > 0) {
                        IceAndFire.sendMSGToAll(new MessageUpdateDragonforge(pos.toLong(), cookTime));
                        this.cookTime = 0;
                    }
                }
            } else if (!this.isBurning() && this.cookTime > 0) {
                this.cookTime = MathHelper.clamp(this.cookTime - 2, 0,
                    getMaxCookTime());
            }

            if (flag != this.isBurning()) {
                flag1 = true;
            }
        }

        if (flag1) {
            this.markDirty();
        }
        if (!canAddFlameAgain) {
            canAddFlameAgain = true;
        }
    }

    public int getMaxCookTime() {
        return getCurrentRecipe().map(DragonForgeRecipe::getCookTime).orElse(100);
    }

    private Block getDefaultOutput() {
        return fireType == 1 ? IafBlockRegistry.DRAGON_ICE : IafBlockRegistry.ASH;
    }

    private ItemStack getCurrentResult() {
        Optional<DragonForgeRecipe> recipe = getCurrentRecipe();
        if (recipe.isPresent())
            return recipe.get().getRecipeOutput();
        return new ItemStack(getDefaultOutput());
    }

    public Optional<DragonForgeRecipe> getCurrentRecipe() {
        return world.getRecipeManager().getRecipe(IafRecipeRegistry.DRAGON_FORGE_TYPE, this, world);
    }

    public List<DragonForgeRecipe> getRecipes() {
        return world.getRecipeManager().getRecipesForType(IafRecipeRegistry.DRAGON_FORGE_TYPE);
    }

    public boolean canSmelt() {
        ItemStack cookStack = this.forgeItemStacks.get(0);
        if (cookStack.isEmpty())
            return false;

        ItemStack forgeRecipeOutput = getCurrentResult();

        if (forgeRecipeOutput.isEmpty())
            return false;

        ItemStack outputStack = this.forgeItemStacks.get(2);
        if (!outputStack.isEmpty() && !outputStack.isItemEqual(forgeRecipeOutput))
            return false;

        int calculatedOutputCount = outputStack.getCount() + forgeRecipeOutput.getCount();
        return (calculatedOutputCount <= this.getInventoryStackLimit()
            && calculatedOutputCount <= outputStack.getMaxStackSize());
    }

    @Override
    public boolean isUsableByPlayer(PlayerEntity player) {
        if (this.world.getTileEntity(this.pos) != this) {
            return false;
        } else {
            return player.getDistanceSq(this.pos.getX() + 0.5D, this.pos.getY() + 0.5D,
                this.pos.getZ() + 0.5D) <= 64.0D;
        }
    }

    public void smeltItem() {
        if (!this.canSmelt())
            return;

        ItemStack cookStack = this.forgeItemStacks.get(0);
        ItemStack bloodStack = this.forgeItemStacks.get(1);
        ItemStack outputStack = this.forgeItemStacks.get(2);

        ItemStack output = getCurrentResult();

        if (outputStack.isEmpty()) {
            this.forgeItemStacks.set(2, output.copy());
        } else {
            outputStack.grow(output.getCount());
        }

        cookStack.shrink(1);
        bloodStack.shrink(1);
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        switch (index) {
            case 1:
                return getRecipes().stream().anyMatch(item -> item.isValidBlood(stack));
            case 0:
                return true;//getRecipes().stream().anyMatch(item -> item.isValidInput(stack))
            default:
                return false;
        }
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        if (side == Direction.DOWN) {
            return SLOTS_BOTTOM;
        } else {
            return side == Direction.UP ? SLOTS_TOP : SLOTS_SIDES;
        }
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, Direction direction) {
        return this.isItemValidForSlot(index, itemStackIn);
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, Direction direction) {
        if (direction == Direction.DOWN && index == 1) {
            Item item = stack.getItem();

            return item == Items.WATER_BUCKET || item == Items.BUCKET;
        }

        return true;
    }

    @Override
    public void clear() {
        this.forgeItemStacks.clear();
    }

    @Override
    public <T> net.minecraftforge.common.util.LazyOptional<T> getCapability(
        net.minecraftforge.common.capabilities.Capability<T> capability, @Nullable Direction facing) {
        if (!this.removed && facing != null
            && capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if (facing == Direction.UP)
                return handlers[0].cast();
            if (facing == Direction.DOWN)
                return handlers[1].cast();
            else
                return handlers[2].cast();
        }
        return super.getCapability(capability, facing);
    }

    @Override
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent("container.dragonforge_fire" + DragonType.getNameFromInt(fireType));
    }

    public void transferPower(int i) {
        if (!world.isRemote) {
            if (this.canSmelt()) {
                if (canAddFlameAgain) {
                    cookTime = Math.min(this.getMaxCookTime() + 1,
                        cookTime + i);
                    canAddFlameAgain = false;
                }
            } else {
                cookTime = 0;
            }
            IceAndFire.sendMSGToAll(new MessageUpdateDragonforge(pos.toLong(), cookTime));
        }
        lastDragonFlameTimer = 40;
    }

    private boolean checkBoneCorners(BlockPos pos) {
        return doesBlockEqual(pos.north().east(), IafBlockRegistry.DRAGON_BONE_BLOCK)
            && doesBlockEqual(pos.north().west(), IafBlockRegistry.DRAGON_BONE_BLOCK)
            && doesBlockEqual(pos.south().east(), IafBlockRegistry.DRAGON_BONE_BLOCK)
            && doesBlockEqual(pos.south().west(), IafBlockRegistry.DRAGON_BONE_BLOCK);
    }

    private boolean checkBrickCorners(BlockPos pos) {
        return doesBlockEqual(pos.north().east(), getBrick()) && doesBlockEqual(pos.north().west(), getBrick())
            && doesBlockEqual(pos.south().east(), getBrick()) && doesBlockEqual(pos.south().west(), getBrick());
    }

    private boolean checkBrickSlots(BlockPos pos) {
        return doesBlockEqual(pos.north(), getBrick()) && doesBlockEqual(pos.east(), getBrick())
            && doesBlockEqual(pos.west(), getBrick()) && doesBlockEqual(pos.south(), getBrick());
    }

    private boolean checkY(BlockPos pos) {
        return doesBlockEqual(pos.up(), getBrick()) && doesBlockEqual(pos.down(), getBrick());
    }

    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(pos, 1, getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket packet) {
        read(this.getBlockState(), packet.getNbtCompound());
    }

    @Override
    public CompoundNBT getUpdateTag() {
        return this.write(new CompoundNBT());
    }

    public boolean assembled() {
        return checkBoneCorners(pos.down()) && checkBrickSlots(pos.down()) && checkBrickCorners(pos)
            && atleastThreeAreBricks(pos) && checkY(pos) && checkBoneCorners(pos.up()) && checkBrickSlots(pos.up());
    }

    private Block getBrick() {
        switch (fireType) {
            case 0:
                return IafBlockRegistry.DRAGONFORGE_FIRE_BRICK;
            case 1:
                return IafBlockRegistry.DRAGONFORGE_ICE_BRICK;
            default:
                return IafBlockRegistry.DRAGONFORGE_LIGHTNING_BRICK;
        }
    }

    private boolean doesBlockEqual(BlockPos pos, Block block) {
        return world.getBlockState(pos).getBlock() == block;
    }

    private boolean atleastThreeAreBricks(BlockPos pos) {
        int count = 0;
        for (Direction facing : HORIZONTALS) {
            if (world.getBlockState(pos.offset(facing)).getBlock() == getBrick()) {
                count++;
            }
        }
        return count > 2;
    }

    @Nullable
    @Override
    public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity player) {
        return new ContainerDragonForge(id, this, playerInventory, new IntArray(0));
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player) {
        return new ContainerDragonForge(id, this, player, new IntArray(0));
    }
}
