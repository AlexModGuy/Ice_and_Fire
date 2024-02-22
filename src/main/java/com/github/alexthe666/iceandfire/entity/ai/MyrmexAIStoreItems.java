package com.github.alexthe666.iceandfire.entity.ai;

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

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class MyrmexAIStoreItems extends Goal {
    private static final int DISTANCE_SQR = 12;

    private final EntityMyrmexBase myrmex;
    private final double movementSpeed;
    private BlockPos nextRoom = null;
    private BlockPos nextCocoon = null;
    private BlockPos mainRoom = null;
    private boolean shouldEnterMainRoom; // first stage - enter the main hive room then storage room
    private PathResult<?> path;

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

        return myrmex.isInHive();
    }

    @Override
    public boolean canContinueToUse() {
        return isUseableCocoon(nextCocoon) && !this.myrmex.isCloseEnoughToTarget(nextCocoon, DISTANCE_SQR) && (myrmex.isInHive() || myrmex.shouldEnterHive());
    }

    @Override
    public void start() {
        super.start();
        initialize();
    }

    private void initialize() {
        MyrmexHive hive = myrmex.getHive();

        if (hive == null) {
            return;
        }

        mainRoom = MyrmexHive.getGroundedPos(this.myrmex.level, hive.getCenter());
        nextRoom = MyrmexHive.getGroundedPos(this.myrmex.level, hive.getRandomRoom(WorldGenMyrmexHive.RoomType.FOOD, this.myrmex.getRandom(), this.myrmex.blockPosition()));
        this.path = ((AdvancedPathNavigate) this.myrmex.getNavigation()).moveToXYZ(mainRoom.getX() + 0.5D, mainRoom.getY() + 0.5D, mainRoom.getZ() + 0.5D, this.movementSpeed);
    }

    @Override
    public void tick() {
        ItemStack stack = myrmex.getItemInHand(InteractionHand.MAIN_HAND);

        if (stack.isEmpty()) {
            stop();
            return;
        }

        if (shouldEnterMainRoom && mainRoom != null) {
            if (this.myrmex.isCloseEnoughToTarget(mainRoom, DISTANCE_SQR)) {
                nextCocoonPath();
                shouldEnterMainRoom = false;
            } else if (!this.myrmex.pathReachesTarget(path, mainRoom, DISTANCE_SQR)) {
                stop();
                return;
            }
        }

        if (nextCocoon == null && !shouldEnterMainRoom) {
            stop();
            return;
        }

        if (!shouldEnterMainRoom) {
            if (this.myrmex.isCloseEnoughToTarget(nextCocoon, DISTANCE_SQR) && isUseableCocoon(nextCocoon)) {
                TileEntityMyrmexCocoon cocoon = (TileEntityMyrmexCocoon) this.myrmex.level.getBlockEntity(nextCocoon);

                for (int i = 0; i < cocoon.getContainerSize(); ++i) {
                    if (!stack.isEmpty()) {
                        ItemStack cocoonStack = cocoon.getItem(i);
                        if (cocoonStack.isEmpty()) {
                            cocoon.setItem(i, stack.copy());
                            cocoon.setChanged();

                            this.myrmex.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
                            this.myrmex.isEnteringHive = false;
                            return;
                        } else if (cocoonStack.getItem() == stack.getItem()) {
                            final int j = Math.min(cocoon.getMaxStackSize(), cocoonStack.getMaxStackSize());
                            final int k = Math.min(stack.getCount(), j - cocoonStack.getCount());
                            if (k > 0) {
                                cocoonStack.grow(k);
                                stack.shrink(k);

                                if (stack.isEmpty()) {
                                    cocoon.setChanged();
                                }
                                this.myrmex.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
                                this.myrmex.isEnteringHive = false;
                                return;
                            }
                        }
                    }
                }
            } else if (/* Arrived at cocoon but cannot reach target */ path.getStatus() == PathFindingStatus.COMPLETE && !myrmex.pathReachesTarget(path, nextCocoon, DISTANCE_SQR)) {
                nextCocoonPath();
            } else if (/* Something interrupted the task */ this.path.isCancelled() && this.myrmex.pathReachesTarget(path, nextCocoon, DISTANCE_SQR)) {
                stop();
            }
        }
    }

    private void nextCocoonPath() {
        nextCocoon = getNearbyCocoon(nextRoom);

        if (nextCocoon != null) {
            path = ((AdvancedPathNavigate) this.myrmex.getNavigation()).moveToXYZ(nextCocoon.getX() + 0.5D, nextCocoon.getY() + 0.5D, nextCocoon.getZ() + 0.5D, this.movementSpeed);
        }
    }

    @Override
    public void stop() {
        mainRoom = null;
        nextRoom = null;
        nextCocoon = null;
        path = null;
    }

    public BlockPos getNearbyCocoon(BlockPos roomCenter) {
        List<BlockPos> closeCocoons = new ArrayList<>();
        int RADIUS_XZ = 15;
        int RADIUS_Y = 7;

        BlockPos.betweenClosedStream(roomCenter.offset(-RADIUS_XZ, -RADIUS_Y, -RADIUS_XZ), roomCenter.offset(RADIUS_XZ, RADIUS_Y, RADIUS_XZ)).forEach(position -> {
            if (isUseableCocoon(position)) {
                closeCocoons.add(position);
            }
        });

        if (closeCocoons.isEmpty()) {
            return null;
        }

        return closeCocoons.get(myrmex.getRandom().nextInt(Math.max(closeCocoons.size() - 1, 1)));
    }

    public boolean isUseableCocoon(final @Nullable BlockPos position) {
        if (position == null) {
            return false;
        }

        ItemStack stack = myrmex.getItemInHand(InteractionHand.MAIN_HAND);

        if (stack.isEmpty()) {
            return false;
        }

        return myrmex.getLevel().getBlockEntity(position) instanceof TileEntityMyrmexCocoon cocoon && !cocoon.isFull(stack);
    }
}