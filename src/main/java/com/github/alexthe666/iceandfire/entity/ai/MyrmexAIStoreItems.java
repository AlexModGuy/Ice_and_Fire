package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.block.BlockMyrmexCocoon;
import com.github.alexthe666.iceandfire.entity.EntityMyrmexBase;
import com.github.alexthe666.iceandfire.entity.EntityMyrmexWorker;
import com.github.alexthe666.iceandfire.entity.MyrmexHive;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityMyrmexCocoon;
import com.github.alexthe666.iceandfire.structures.WorldGenMyrmexHive;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class MyrmexAIStoreItems extends EntityAIBase {
    private final EntityMyrmexBase myrmex;
    private final double movementSpeed;
    private Path path;
    private BlockPos nextRoom = BlockPos.ORIGIN;
    private BlockPos nextCocoon = null;

    public MyrmexAIStoreItems(EntityMyrmexBase entityIn, double movementSpeedIn) {
        this.myrmex = entityIn;
        this.movementSpeed = movementSpeedIn;
        this.setMutexBits(1);
    }

    public boolean shouldExecute() {
        if (!this.myrmex.canMove() || this.myrmex instanceof EntityMyrmexWorker && ((EntityMyrmexWorker)this.myrmex).holdingBaby() || !this.myrmex.shouldEnterHive() && !this.myrmex.getNavigator().noPath() || this.myrmex.canSeeSky() || this.myrmex.getHeldItem(EnumHand.MAIN_HAND).isEmpty()) {
            return false;
        }
        MyrmexHive village = this.myrmex.getHive();
        if (village == null) {
            return false;
        } else {
            nextRoom = MyrmexHive.getGroundedPos(this.myrmex.world, village.getRandomRoom(WorldGenMyrmexHive.RoomType.FOOD, this.myrmex.getRNG(), this.myrmex.getPosition()));
            nextCocoon = getNearbyCocoon(nextRoom);
            if (nextCocoon != null) {
                this.path = this.myrmex.getNavigator().getPathToPos(nextCocoon);
                return this.path != null;
            } else {
                return false;
            }
        }
    }

    public boolean shouldContinueExecuting() {
        return !this.myrmex.getHeldItem(EnumHand.MAIN_HAND).isEmpty() && !this.myrmex.getNavigator().noPath() && this.myrmex.getDistanceSq(nextCocoon) > 3 && this.myrmex.shouldEnterHive() && nextCocoon != null && isUseableCocoon(nextCocoon);
    }

    public void startExecuting() {
        this.myrmex.getNavigator().setPath(this.path, this.movementSpeed);
    }

    @Override
    public void updateTask() {
        if (nextCocoon != null && this.myrmex.getDistanceSq(nextCocoon) < 4 && !this.myrmex.getHeldItem(EnumHand.MAIN_HAND).isEmpty() && isUseableCocoon(nextCocoon)) {
            TileEntityMyrmexCocoon cocoon = (TileEntityMyrmexCocoon) this.myrmex.world.getTileEntity(nextCocoon);
            ItemStack itemstack = this.myrmex.getHeldItem(EnumHand.MAIN_HAND);
            if(!itemstack.isEmpty()) {
                for (int i = 0; i < cocoon.getSizeInventory(); ++i) {
                    if(!itemstack.isEmpty()) {
                        ItemStack itemstack1 = cocoon.getStackInSlot(i);
                        if (itemstack1.isEmpty()) {
                            cocoon.setInventorySlotContents(i, itemstack);
                            cocoon.markDirty();
                            this.myrmex.setHeldItem(EnumHand.MAIN_HAND, ItemStack.EMPTY);
                            this.myrmex.isEnteringHive = false;
                            return;
                        }
                        if (ItemStack.areItemsEqual(itemstack1, itemstack)) {
                            int j = Math.min(cocoon.getInventoryStackLimit(), itemstack1.getMaxStackSize());
                            int k = Math.min(itemstack.getCount(), j - itemstack1.getCount());
                            if (k > 0) {
                                itemstack1.grow(k);
                                itemstack.shrink(k);

                                if (itemstack.isEmpty()) {
                                    cocoon.markDirty();
                                }
                                this.myrmex.setHeldItem(EnumHand.MAIN_HAND, ItemStack.EMPTY);
                                this.myrmex.isEnteringHive = false;
                                return;
                            }
                        }
                    }
                }
            }else{
                resetTask();
            }
        }
    }

    public void resetTask() {
        nextRoom = BlockPos.ORIGIN;
        nextCocoon = null;
        this.myrmex.getNavigator().setPath(null, this.movementSpeed);
    }

    public BlockPos getNearbyCocoon(BlockPos roomCenter) {
        int RADIUS_XZ = 15;
        int RADIUS_Y = 7;
        List<BlockPos> closeCocoons = new ArrayList<BlockPos>();
        for (BlockPos blockpos : BlockPos.getAllInBox(roomCenter.add(-RADIUS_XZ, -RADIUS_Y, -RADIUS_XZ), roomCenter.add(RADIUS_XZ, RADIUS_Y, RADIUS_XZ))) {
            if (this.myrmex.world.getBlockState(blockpos).getBlock() instanceof BlockMyrmexCocoon && this.myrmex.world.getTileEntity(blockpos) != null && this.myrmex.world.getTileEntity(blockpos) instanceof TileEntityMyrmexCocoon) {
                if (!((TileEntityMyrmexCocoon) this.myrmex.world.getTileEntity(blockpos)).isFull()) {
                    closeCocoons.add(blockpos);
                }
            }
        }
        if (closeCocoons.isEmpty()) {
            return null;
        }
        return closeCocoons.get(myrmex.getRNG().nextInt(Math.max(closeCocoons.size() - 1, 1)));
    }

    public boolean isUseableCocoon(BlockPos blockpos) {
        if (this.myrmex.world.getBlockState(blockpos).getBlock() instanceof BlockMyrmexCocoon && this.myrmex.world.getTileEntity(blockpos) != null && this.myrmex.world.getTileEntity(blockpos) instanceof TileEntityMyrmexCocoon) {
            if (!((TileEntityMyrmexCocoon) this.myrmex.world.getTileEntity(blockpos)).isFull()) {
                return true;
            }
        }
        return false;
    }
}