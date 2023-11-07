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
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.Mth;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class TileEntityDragonforge extends BaseContainerBlockEntity implements WorldlyContainer {

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

    public TileEntityDragonforge(BlockPos pos, BlockState state) {
        super(IafTileEntityRegistry.DRAGONFORGE_CORE.get(), pos, state);
    }

    public TileEntityDragonforge(BlockPos pos, BlockState state, int fireType) {
        super(IafTileEntityRegistry.DRAGONFORGE_CORE.get(), pos, state);
        this.fireType = fireType;
    }

    public static void tick(Level level, BlockPos pos, BlockState state, TileEntityDragonforge entityDragonforge) {
        boolean flag = entityDragonforge.isBurning();
        boolean flag1 = false;
        entityDragonforge.fireType = entityDragonforge.getFireType(entityDragonforge.getBlockState().getBlock());
        if (entityDragonforge.lastDragonFlameTimer > 0) {
            entityDragonforge.lastDragonFlameTimer--;
        }
        entityDragonforge.updateGrills(entityDragonforge.assembled());
        if (!level.isClientSide) {
            if (entityDragonforge.prevAssembled != entityDragonforge.assembled()) {
                BlockDragonforgeCore.setState(entityDragonforge.fireType, entityDragonforge.prevAssembled, level, pos);
            }
            entityDragonforge.prevAssembled = entityDragonforge.assembled();
            if (!entityDragonforge.assembled())
                return;
        }
        if (entityDragonforge.cookTime > 0 && entityDragonforge.canSmelt() && entityDragonforge.lastDragonFlameTimer == 0) {
            entityDragonforge.cookTime--;
        }
        if (entityDragonforge.getItem(0).isEmpty() && !level.isClientSide) {
            entityDragonforge.cookTime = 0;
        }
        if (!entityDragonforge.level.isClientSide) {
            if (entityDragonforge.isBurning()) {
                if (entityDragonforge.canSmelt()) {
                    ++entityDragonforge.cookTime;
                    if (entityDragonforge.cookTime >= entityDragonforge.getMaxCookTime()) {
                        entityDragonforge.cookTime = 0;
                        entityDragonforge.smeltItem();
                        flag1 = true;
                    }
                } else {
                    if (entityDragonforge.cookTime > 0) {
                        IceAndFire.sendMSGToAll(new MessageUpdateDragonforge(pos.asLong(), entityDragonforge.cookTime));
                        entityDragonforge.cookTime = 0;
                    }
                }
            } else if (!entityDragonforge.isBurning() && entityDragonforge.cookTime > 0) {
                entityDragonforge.cookTime = Mth.clamp(entityDragonforge.cookTime - 2, 0,
                    entityDragonforge.getMaxCookTime());
            }

            if (flag != entityDragonforge.isBurning()) {
                flag1 = true;
            }
        }

        if (flag1) {
            entityDragonforge.setChanged();
        }
        if (!entityDragonforge.canAddFlameAgain) {
            entityDragonforge.canAddFlameAgain = true;
        }
    }

    @Override
    public int getContainerSize() {
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
            BlockPos grillPos = this.getBlockPos().relative(facing);
            if (grillMatches(level.getBlockState(grillPos).getBlock())) {
                BlockState grillState = getGrillBlock().defaultBlockState().setValue(BlockDragonforgeBricks.GRILL, grill);
                if (level.getBlockState(grillPos) != grillState) {
                    level.setBlockAndUpdate(grillPos, grillState);
                }
            }
        }
    }

    public Block getGrillBlock() {
        return switch (fireType) {
            case 1 -> IafBlockRegistry.DRAGONFORGE_ICE_BRICK.get();
            case 2 -> IafBlockRegistry.DRAGONFORGE_LIGHTNING_BRICK.get();
            default -> IafBlockRegistry.DRAGONFORGE_FIRE_BRICK.get(); // isFire == 0
        };
    }

    public boolean grillMatches(Block block) {
        return switch (fireType) {
            case 0 -> block == IafBlockRegistry.DRAGONFORGE_FIRE_BRICK.get();
            case 1 -> block == IafBlockRegistry.DRAGONFORGE_ICE_BRICK.get();
            case 2 -> block == IafBlockRegistry.DRAGONFORGE_LIGHTNING_BRICK.get();
            default -> false;
        };
    }

    @Override
    public @NotNull ItemStack getItem(int index) {
        return this.forgeItemStacks.get(index);
    }

    @Override
    public @NotNull ItemStack removeItem(int index, int count) {
        return ContainerHelper.removeItem(this.forgeItemStacks, index, count);
    }

    @Override
    public @NotNull ItemStack removeItemNoUpdate(int index) {
        return ContainerHelper.takeItem(this.forgeItemStacks, index);
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        ItemStack itemstack = this.forgeItemStacks.get(index);
        boolean flag = !stack.isEmpty() && ItemStack.isSameItem(stack, itemstack)
            && ItemStack.matches(stack, itemstack);
        this.forgeItemStacks.set(index, stack);

        if (stack.getCount() > this.getMaxStackSize()) {
            stack.setCount(this.getMaxStackSize());
        }

        if (index == 0 && !flag
            || this.cookTime > this.getMaxCookTime()) {
            this.cookTime = 0;
            this.setChanged();
        }
    }

    @Override
    public void load(@NotNull CompoundTag compound) {
        super.load(compound);
        this.forgeItemStacks = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(compound, this.forgeItemStacks);
        this.cookTime = compound.getInt("CookTime");
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        compound.putInt("CookTime", (short) this.cookTime);
        ContainerHelper.saveAllItems(compound, this.forgeItemStacks);
    }

    @Override
    public int getMaxStackSize() {
        return 64;
    }

    public boolean isBurning() {
        return this.cookTime > 0;
    }

    public int getFireType(Block block) {
        if (block == IafBlockRegistry.DRAGONFORGE_FIRE_CORE.get()
            || block == IafBlockRegistry.DRAGONFORGE_FIRE_CORE_DISABLED.get()) {
            return 0;
        }
        if (block == IafBlockRegistry.DRAGONFORGE_ICE_CORE.get() || block == IafBlockRegistry.DRAGONFORGE_ICE_CORE_DISABLED.get()) {
            return 1;
        }
        if (block == IafBlockRegistry.DRAGONFORGE_LIGHTNING_CORE.get()
            || block == IafBlockRegistry.DRAGONFORGE_LIGHTNING_CORE_DISABLED.get()) {
            return 2;
        }
        return 0;
    }

    public String getTypeID() {
        return switch (getFireType(this.getBlockState().getBlock())) {
            case 0 -> "fire";
            case 1 -> "ice";
            case 2 -> "lightning";
            default -> "";
        };
    }

    public int getMaxCookTime() {
        return getCurrentRecipe().map(DragonForgeRecipe::getCookTime).orElse(100);
    }

    private Block getDefaultOutput() {
        return fireType == 1 ? IafBlockRegistry.DRAGON_ICE.get() : IafBlockRegistry.ASH.get();
    }

    private ItemStack getCurrentResult() {
        Optional<DragonForgeRecipe> recipe = getCurrentRecipe();
        return recipe.map(DragonForgeRecipe::getResultItem)
                .orElseGet(() -> new ItemStack(getDefaultOutput()));
    }

    public Optional<DragonForgeRecipe> getCurrentRecipe() {
        return level.getRecipeManager().getRecipeFor(IafRecipeRegistry.DRAGON_FORGE_TYPE.get(), this, level);
    }

    public List<DragonForgeRecipe> getRecipes() {
        return level.getRecipeManager().getAllRecipesFor(IafRecipeRegistry.DRAGON_FORGE_TYPE.get());
    }

    public boolean canSmelt() {
        ItemStack cookStack = this.forgeItemStacks.get(0);
        if (cookStack.isEmpty())
            return false;

        ItemStack forgeRecipeOutput = getCurrentResult();

        if (forgeRecipeOutput.isEmpty())
            return false;

        ItemStack outputStack = this.forgeItemStacks.get(2);
        if (!outputStack.isEmpty() && !ItemStack.isSameItem(outputStack, forgeRecipeOutput))
            return false;

        int calculatedOutputCount = outputStack.getCount() + forgeRecipeOutput.getCount();
        return (calculatedOutputCount <= this.getMaxStackSize()
            && calculatedOutputCount <= outputStack.getMaxStackSize());
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        if (player.level().getBlockEntity(this.worldPosition) != this) {
            return false;
        } else {
            return player.distanceToSqr(this.worldPosition.getX() + 0.5D, this.worldPosition.getY() + 0.5D,
                this.worldPosition.getZ() + 0.5D) <= 64.0D;
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
    public boolean canPlaceItem(int index, @NotNull ItemStack stack) {
        return switch (index) {
            case 1 -> getRecipes().stream().anyMatch(item -> item.isValidBlood(stack));
            case 0 -> true;//getRecipes().stream().anyMatch(item -> item.isValidInput(stack))
            default -> false;
        };
    }

    @Override
    public int @NotNull [] getSlotsForFace(@NotNull Direction side) {
        if (side == Direction.DOWN) {
            return SLOTS_BOTTOM;
        } else {
            return side == Direction.UP ? SLOTS_TOP : SLOTS_SIDES;
        }
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, @NotNull ItemStack itemStackIn, Direction direction) {
        return this.canPlaceItem(index, itemStackIn);
    }

    @Override
    public boolean canTakeItemThroughFace(int index, @NotNull ItemStack stack, @NotNull Direction direction) {
        if (direction == Direction.DOWN && index == 1) {
            Item item = stack.getItem();

            return item == Items.WATER_BUCKET || item == Items.BUCKET;
        }

        return true;
    }

    @Override
    public void clearContent() {
        this.forgeItemStacks.clear();
    }

    @Override
    public <T> net.minecraftforge.common.util.@NotNull LazyOptional<T> getCapability(
        net.minecraftforge.common.capabilities.@NotNull Capability<T> capability, @Nullable Direction facing) {
        if (!this.remove && facing != null
            && capability == ForgeCapabilities.ITEM_HANDLER) {
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
    protected @NotNull Component getDefaultName() {
        return Component.translatable("container.dragonforge_fire" + DragonType.getNameFromInt(fireType));
    }

    public void transferPower(int i) {
        if (!level.isClientSide) {
            if (this.canSmelt()) {
                if (canAddFlameAgain) {
                    cookTime = Math.min(this.getMaxCookTime() + 1,
                        cookTime + i);
                    canAddFlameAgain = false;
                }
            } else {
                cookTime = 0;
            }
            IceAndFire.sendMSGToAll(new MessageUpdateDragonforge(worldPosition.asLong(), cookTime));
        }
        lastDragonFlameTimer = 40;
    }

    private boolean checkBoneCorners(BlockPos pos) {
        return doesBlockEqual(pos.north().east(), IafBlockRegistry.DRAGON_BONE_BLOCK.get())
            && doesBlockEqual(pos.north().west(), IafBlockRegistry.DRAGON_BONE_BLOCK.get())
            && doesBlockEqual(pos.south().east(), IafBlockRegistry.DRAGON_BONE_BLOCK.get())
            && doesBlockEqual(pos.south().west(), IafBlockRegistry.DRAGON_BONE_BLOCK.get());
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
        return doesBlockEqual(pos.above(), getBrick()) && doesBlockEqual(pos.below(), getBrick());
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket packet) {
        load(packet.getTag());
    }

    @Override
    public @NotNull CompoundTag getUpdateTag() {
        return this.saveWithFullMetadata();
    }

    public boolean assembled() {
        return checkBoneCorners(worldPosition.below()) && checkBrickSlots(worldPosition.below()) && checkBrickCorners(worldPosition)
            && atleastThreeAreBricks(worldPosition) && checkY(worldPosition) && checkBoneCorners(worldPosition.above()) && checkBrickSlots(worldPosition.above());
    }

    private Block getBrick() {
        return switch (fireType) {
            case 0 -> IafBlockRegistry.DRAGONFORGE_FIRE_BRICK.get();
            case 1 -> IafBlockRegistry.DRAGONFORGE_ICE_BRICK.get();
            default -> IafBlockRegistry.DRAGONFORGE_LIGHTNING_BRICK.get();
        };
    }

    private boolean doesBlockEqual(BlockPos pos, Block block) {
        return level.getBlockState(pos).getBlock() == block;
    }

    private boolean atleastThreeAreBricks(BlockPos pos) {
        int count = 0;
        for (Direction facing : HORIZONTALS) {
            if (level.getBlockState(pos.relative(facing)).getBlock() == getBrick()) {
                count++;
            }
        }
        return count > 2;
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, @NotNull Inventory playerInventory, @NotNull Player player) {
        return new ContainerDragonForge(id, this, playerInventory, new SimpleContainerData(0));
    }

    @Override
    protected @NotNull AbstractContainerMenu createMenu(int id, @NotNull Inventory player) {
        return new ContainerDragonForge(id, this, player, new SimpleContainerData(0));
    }
}
