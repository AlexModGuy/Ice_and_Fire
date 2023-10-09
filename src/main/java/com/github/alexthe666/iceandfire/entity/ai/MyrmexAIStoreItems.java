package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.block.BlockMyrmexCocoon;
import com.github.alexthe666.iceandfire.entity.EntityMyrmexBase;
import com.github.alexthe666.iceandfire.entity.EntityMyrmexWorker;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityMyrmexCocoon;
import com.github.alexthe666.iceandfire.entity.util.MyrmexHive;
import com.github.alexthe666.iceandfire.pathfinding.raycoms.AdvancedPathNavigate;
import com.github.alexthe666.iceandfire.pathfinding.raycoms.PathFindingStatus;
import com.github.alexthe666.iceandfire.pathfinding.raycoms.PathResult;
import com.github.alexthe666.iceandfire.world.gen.WorldGenMyrmexHive;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class MyrmexAIStoreItems extends Goal {
    private final EntityMyrmexBase myrmex;
    private final double movementSpeed;
    private BlockPos nextRoom = null;
    private BlockPos nextCocoon = null;
    private BlockPos mainRoom = null;
    private boolean first = true; //first stage - enter the main hive room then storage room
    private PathResult path;

    public MyrmexAIStoreItems(EntityMyrmexBase entityIn, double movementSpeedIn) {
        this.myrmex = entityIn;
        this.movementSpeed = movementSpeedIn;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (!this.myrmex.canMove() || this.myrmex instanceof EntityMyrmexWorker && ((EntityMyrmexWorker) this.myrmex).holdingBaby() || !this.myrmex.shouldEnterHive() && !this.myrmex.getNavigation().isDone() || this.myrmex.getItemInHand(InteractionHand.MAIN_HAND).isEmpty()) {
            return false;
        }
        if (!(this.myrmex.getNavigation() instanceof AdvancedPathNavigate) || this.myrmex.isPassenger()) {
            return false;
        }
        if (this.myrmex.getWaitTicks() > 0) {
            return false;
        }
        MyrmexHive village = this.myrmex.getHive();
        if (village == null) {
            return false;
        }
        if (!this.myrmex.isInHive()) {
            return false;
        }
        first = true;
        mainRoom = MyrmexHive.getGroundedPos(this.myrmex.level(), village.getCenter());

        nextRoom = MyrmexHive.getGroundedPos(this.myrmex.level(), village.getRandomRoom(WorldGenMyrmexHive.RoomType.FOOD, this.myrmex.getRandom(), this.myrmex.blockPosition()));
        nextCocoon = getNearbyCocoon(nextRoom);
        if (nextCocoon == null) {
            this.myrmex.setWaitTicks(20 + ThreadLocalRandom.current().nextInt(40));
        }
        this.path = ((AdvancedPathNavigate) this.myrmex.getNavigation()).moveToXYZ(mainRoom.getX() + 0.5D, mainRoom.getY() + 0.5D, mainRoom.getZ() + 0.5D, this.movementSpeed);
        return nextCocoon != null;

    }

    @Override
    public boolean canContinueToUse() {
        return !this.myrmex.getItemInHand(InteractionHand.MAIN_HAND).isEmpty() && nextCocoon != null && isUseableCocoon(nextCocoon) && !this.myrmex.isCloseEnoughToTarget(nextCocoon, 3) && this.myrmex.shouldEnterHive();
    }

    @Override
    public void tick() {
        if (first && mainRoom != null) {
            if (this.myrmex.isCloseEnoughToTarget(mainRoom, 10)) {
                this.path = ((AdvancedPathNavigate) this.myrmex.getNavigation()).moveToXYZ(nextCocoon.getX() + 0.5D, nextCocoon.getY() + 0.5D, nextCocoon.getZ() + 0.5D, this.movementSpeed);
                first = false;
            } else if (!this.myrmex.pathReachesTarget(path, mainRoom, 9)) {
                //Simple way to stop executing this task
                nextCocoon = null;
            }
        }

        if (!first && nextCocoon != null) {
            final double dist = 9.0D; // 3 * 3
            if (this.myrmex.isCloseEnoughToTarget(nextCocoon, dist) && !this.myrmex.getItemInHand(InteractionHand.MAIN_HAND).isEmpty() && isUseableCocoon(nextCocoon)) {
                TileEntityMyrmexCocoon cocoon = (TileEntityMyrmexCocoon) this.myrmex.level().getBlockEntity(nextCocoon);
                ItemStack itemstack = this.myrmex.getItemInHand(InteractionHand.MAIN_HAND);
                if (!itemstack.isEmpty()) {
                    for (int i = 0; i < cocoon.getContainerSize(); ++i) {
                        if (!itemstack.isEmpty()) {
                            ItemStack cocoonStack = cocoon.getItem(i);
                            if (cocoonStack.isEmpty()) {
                                cocoon.setItem(i, itemstack.copy());
                                cocoon.setChanged();

                                this.myrmex.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
                                this.myrmex.isEnteringHive = false;
                                return;
                            } else if (cocoonStack.getItem() == itemstack.getItem()) {
                                final int j = Math.min(cocoon.getMaxStackSize(), cocoonStack.getMaxStackSize());
                                final int k = Math.min(itemstack.getCount(), j - cocoonStack.getCount());
                                if (k > 0) {
                                    cocoonStack.grow(k);
                                    itemstack.shrink(k);

                                    if (itemstack.isEmpty()) {
                                        cocoon.setChanged();
                                    }
                                    this.myrmex.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
                                    this.myrmex.isEnteringHive = false;
                                    return;
                                }
                            }
                        }
                    }
                }
            }
            //In case the myrmex isn't close enough to the cocoon and walked to it's destination try a different one
            else if (!this.myrmex.getItemInHand(InteractionHand.MAIN_HAND).isEmpty() && this.path.getStatus() == PathFindingStatus.COMPLETE && !this.myrmex.pathReachesTarget(path, nextCocoon, dist)) {
                nextCocoon = getNearbyCocoon(nextRoom);
                if (nextCocoon != null) {
                    this.path = ((AdvancedPathNavigate) this.myrmex.getNavigation()).moveToXYZ(nextCocoon.getX() + 0.5D, nextCocoon.getY() + 0.5D, nextCocoon.getZ() + 0.5D, this.movementSpeed);
                }
            } else if (this.myrmex.pathReachesTarget(path, nextCocoon, dist) && this.path.isCancelled()) {
                stop();
            }
        }
    }

    @Override
    public void stop() {
        nextRoom = null;
        nextCocoon = null;
        mainRoom = null;
        first = true;
    }

    public BlockPos getNearbyCocoon(BlockPos roomCenter) {
        int RADIUS_XZ = 15;
        int RADIUS_Y = 7;
        List<BlockPos> closeCocoons = new ArrayList<>();
        BlockPos.betweenClosedStream(roomCenter.offset(-RADIUS_XZ, -RADIUS_Y, -RADIUS_XZ), roomCenter.offset(RADIUS_XZ, RADIUS_Y, RADIUS_XZ)).forEach(blockpos -> {
            BlockEntity te = this.myrmex.level().getBlockEntity(blockpos);
            if (te instanceof TileEntityMyrmexCocoon) {
                if (!((TileEntityMyrmexCocoon) te).isFull(this.myrmex.getItemInHand(InteractionHand.MAIN_HAND))) {
                    closeCocoons.add(te.getBlockPos());
                }
            }
        });
        if (closeCocoons.isEmpty()) {
            return null;
        }
        return closeCocoons.get(myrmex.getRandom().nextInt(Math.max(closeCocoons.size() - 1, 1)));
    }

    public boolean isUseableCocoon(BlockPos blockpos) {
        if (this.myrmex.level().getBlockState(blockpos).getBlock() instanceof BlockMyrmexCocoon && this.myrmex.level().getBlockEntity(blockpos) != null && this.myrmex.level().getBlockEntity(blockpos) instanceof TileEntityMyrmexCocoon) {
            return !((TileEntityMyrmexCocoon) this.myrmex.level().getBlockEntity(blockpos)).isFull(this.myrmex.getItemInHand(InteractionHand.MAIN_HAND));
        }
        return false;
    }
}