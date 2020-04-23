package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.block.BlockMyrmexCocoon;
import com.github.alexthe666.iceandfire.entity.EntityMyrmexBase;
import com.github.alexthe666.iceandfire.entity.EntityMyrmexWorker;
import com.github.alexthe666.iceandfire.entity.MyrmexHive;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityMyrmexCocoon;
import com.github.alexthe666.iceandfire.world.gen.WorldGenMyrmexHive;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class MyrmexAIStoreItems extends EntityAIBase {
    private final EntityMyrmexBase myrmex;
    private final double movementSpeed;
    private BlockPos nextRoom = null;
    private BlockPos nextCocoon = null;
    private BlockPos mainRoom = null;
    private boolean first = true; //first stage - enter the main hive room then storage room

    public MyrmexAIStoreItems(EntityMyrmexBase entityIn, double movementSpeedIn) {
        this.myrmex = entityIn;
        this.movementSpeed = movementSpeedIn;
        this.setMutexBits(1);
    }

    public boolean shouldExecute() {
        if (!this.myrmex.canMove() || this.myrmex instanceof EntityMyrmexWorker && ((EntityMyrmexWorker) this.myrmex).holdingBaby() || !this.myrmex.shouldEnterHive() && !this.myrmex.getNavigator().noPath() || this.myrmex.canSeeSky() || this.myrmex.getHeldItem(EnumHand.MAIN_HAND).isEmpty()) {
            return false;
        }
        MyrmexHive village = this.myrmex.getHive();
        if (village == null) {
            return false;
        } else {
            first = true;
            mainRoom = MyrmexHive.getGroundedPos(this.myrmex.world, village.getCenter());
            nextRoom = MyrmexHive.getGroundedPos(this.myrmex.world, village.getRandomRoom(WorldGenMyrmexHive.RoomType.FOOD, this.myrmex.getRNG(), this.myrmex.getPosition()));
            nextCocoon = getNearbyCocoon(nextRoom);
            return nextCocoon != null;
        }
    }

    public boolean shouldContinueExecuting() {
        return !this.myrmex.getHeldItem(EnumHand.MAIN_HAND).isEmpty() && nextCocoon != null && isUseableCocoon(nextCocoon) && this.myrmex.getDistanceSq(nextCocoon) > 3 && this.myrmex.shouldEnterHive();
    }

    @Override
    public void updateTask() {
        if (first && mainRoom != null) {
            if(this.myrmex.getNavigator().noPath()){
                this.myrmex.getNavigator().tryMoveToXYZ(mainRoom.getX() + 0.5D, mainRoom.getY() + 0.5D, mainRoom.getZ() + 0.5D, this.movementSpeed);

            }
            if (this.myrmex.getDistanceSq(mainRoom) < 10D) {
                first = false;
                return;
            }
        }
        if (!first && nextCocoon != null) {
            if(this.myrmex.getNavigator().noPath()) {
                this.myrmex.getNavigator().tryMoveToXYZ(nextCocoon.getX() + 0.5D, nextCocoon.getY() + 0.5D, nextCocoon.getZ() + 0.5D, this.movementSpeed);
            }
            if (this.myrmex.getDistanceSq(nextCocoon) < 5.5D && !this.myrmex.getHeldItem(EnumHand.MAIN_HAND).isEmpty() && isUseableCocoon(nextCocoon)) {
                TileEntityMyrmexCocoon cocoon = (TileEntityMyrmexCocoon) this.myrmex.world.getTileEntity(nextCocoon);
                ItemStack itemstack = this.myrmex.getHeldItem(EnumHand.MAIN_HAND);
                if (!itemstack.isEmpty()) {
                    for (int i = 0; i < cocoon.getSizeInventory(); ++i) {
                        if (!itemstack.isEmpty()) {
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
                }
            }
        }
    }

    public void resetTask() {
        nextRoom = null;
        nextCocoon = null;
        mainRoom = null;
        first = true;
    }

    public BlockPos getNearbyCocoon(BlockPos roomCenter) {
        int RADIUS_XZ = 15;
        int RADIUS_Y = 7;
        List<BlockPos> closeCocoons = new ArrayList<BlockPos>();
        for (BlockPos blockpos : BlockPos.getAllInBox(roomCenter.add(-RADIUS_XZ, -RADIUS_Y, -RADIUS_XZ), roomCenter.add(RADIUS_XZ, RADIUS_Y, RADIUS_XZ))) {
            TileEntity te = this.myrmex.world.getTileEntity(blockpos);
            if (te != null && te instanceof TileEntityMyrmexCocoon) {
                if (!((TileEntityMyrmexCocoon) te).isFull(this.myrmex.getHeldItem(EnumHand.MAIN_HAND))) {
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
            return !((TileEntityMyrmexCocoon) this.myrmex.world.getTileEntity(blockpos)).isFull(this.myrmex.getHeldItem(EnumHand.MAIN_HAND));
        }
        return false;
    }
}