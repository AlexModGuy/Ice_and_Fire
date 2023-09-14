package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.api.event.GenericGriefEvent;
import com.github.alexthe666.iceandfire.entity.EntityMyrmexBase;
import com.github.alexthe666.iceandfire.entity.EntityMyrmexWorker;
import com.github.alexthe666.iceandfire.pathfinding.raycoms.AdvancedPathNavigate;
import com.github.alexthe666.iceandfire.pathfinding.raycoms.PathResult;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class MyrmexAIForage extends Goal {

    private static final int RADIUS = 16;
    private final EntityMyrmexWorker myrmex;
    private final BlockSorter targetSorter;
    private BlockPos targetBlock = null;
    private int wanderRadius;
    private final int chance;
    private PathResult path;
    private int failedToFindPath = 0;

    public MyrmexAIForage(EntityMyrmexWorker myrmex, int chanceIn) {
        super();
        this.myrmex = myrmex;
        this.targetSorter = new BlockSorter();
        this.setFlags(EnumSet.of(Flag.MOVE));
        this.chance = chanceIn;
    }

    @Override
    public boolean canUse() {
        if (!this.myrmex.canMove() || this.myrmex.holdingSomething() || !this.myrmex.getNavigation().isDone()
            || this.myrmex.isInHive() || this.myrmex.shouldEnterHive()) {
            return false;
        }
        if (!(this.myrmex.getNavigation() instanceof AdvancedPathNavigate) || this.myrmex.isPassenger()) {
            return false;
        }
        if (this.myrmex.getWaitTicks() > 0) {
            return false;
        }

        // Get nearby edible blocks
        List<BlockPos> edibleBlocks = getEdibleBlocks();
        // If there are no edible blocks nearby
        if (edibleBlocks.isEmpty()) {
            return myrmex.getRandom().nextInt(chance) == 0 && increaseRadiusAndWander();
        }
        // Set closest block as target
        edibleBlocks.sort(this.targetSorter);
        this.targetBlock = edibleBlocks.get(0);
        this.path = ((AdvancedPathNavigate) this.myrmex.getNavigation()).moveToXYZ(targetBlock.getX(),
            targetBlock.getY(), targetBlock.getZ(), 1D);
        return myrmex.getRandom().nextInt(chance) == 0;
    }

    @Override
    public boolean canContinueToUse() {
        if (this.targetBlock == null) return false;
        if (this.myrmex.getWaitTicks() > 0) return false;
        if (myrmex.shouldEnterHive()) {
            this.myrmex.keepSearching = false;
            return false;
        }
        return !this.myrmex.holdingSomething();
    }

    @Override
    public void tick() {
        // If we haven't found an edible block as a target
        if (targetBlock != null && this.myrmex.keepSearching) {
            // If the myrmex is close enough to the target or at the end of the path
            if (this.myrmex.isCloseEnoughToTarget(targetBlock, 12)
                || !this.myrmex.pathReachesTarget(path, targetBlock, 12)) {
                failedToFindPath = 0;
                List<BlockPos> edibleBlocks = getEdibleBlocks();
                // If we found an edible block nearby
                if (!edibleBlocks.isEmpty()) {
                    this.myrmex.keepSearching = false;
                    edibleBlocks.sort(this.targetSorter);
                    this.targetBlock = edibleBlocks.get(0);
                    this.path = ((AdvancedPathNavigate) this.myrmex.getNavigation()).moveToXYZ(targetBlock.getX(),
                        targetBlock.getY(), targetBlock.getZ(), 1D);
                }
                // If there are still no edible blocks nearby
                else {
                    increaseRadiusAndWander();
                }
            }
            // if we have found an edible block
        } else if (!this.myrmex.keepSearching) {
            failedToFindPath = 0;
            BlockState block = this.myrmex.level().getBlockState(this.targetBlock);
            // Test if the block is edible
            if (EntityMyrmexBase.isEdibleBlock(block)) {
                final double distance = this.getDistanceSq(this.targetBlock);
                if (distance < 6) {
                    block.getBlock();
                    // Routine to break block and add item to myrmex
                    List<ItemStack> drops = Block.getDrops(block, (ServerLevel) this.myrmex.level(), this.targetBlock,
                        this.myrmex.level().getBlockEntity(targetBlock)); // use the old method until it gets removed, for
                    // backward compatibility
                    if (!drops.isEmpty()) {
                        this.myrmex.level().destroyBlock(this.targetBlock, false);
                        ItemStack heldStack = drops.get(0).copy();
                        heldStack.setCount(1);
                        drops.get(0).shrink(1);
                        this.myrmex.setItemInHand(InteractionHand.MAIN_HAND, heldStack);
                        for (ItemStack stack : drops) {
                            ItemEntity itemEntity = new ItemEntity(this.myrmex.level(),
                                this.targetBlock.getX() + this.myrmex.getRandom().nextDouble(),
                                this.targetBlock.getY() + this.myrmex.getRandom().nextDouble(),
                                this.targetBlock.getZ() + this.myrmex.getRandom().nextDouble(), stack);
                            itemEntity.setDefaultPickUpDelay();
                            if (!this.myrmex.level().isClientSide) {
                                this.myrmex.level().addFreshEntity(itemEntity);
                            }
                        }
                        this.targetBlock = null;
                        this.stop();
                        this.myrmex.keepSearching = false;
                        this.wanderRadius = RADIUS;
                    }
                }
                // If the myrmex reached the end of the path but is still not close enough to
                // the target
                else if (!this.myrmex.pathReachesTarget(path, targetBlock, 12)) {
                    List<BlockPos> edibleBlocks = getEdibleBlocks();
                    // If we found an edible block nearby
                    if (!edibleBlocks.isEmpty()) {
                        this.myrmex.keepSearching = false;
                        // This time choose a different random edible block
                        this.targetBlock = edibleBlocks.get(this.myrmex.getRandom().nextInt(edibleBlocks.size()));
                        this.path = ((AdvancedPathNavigate) this.myrmex.getNavigation()).moveToXYZ(targetBlock.getX(),
                            targetBlock.getY(), targetBlock.getZ(), 1D);
                    } else {
                        this.myrmex.keepSearching = true;
                    }
                }
            }
        }

    }

    @Override
    public void stop() {
        this.targetBlock = null;
        this.myrmex.keepSearching = true;
    }

    private double getDistanceSq(BlockPos pos) {
        final double deltaX = this.myrmex.getX() - (pos.getX() + 0.5);
        final double deltaY = this.myrmex.getY() + this.myrmex.getEyeHeight() - (pos.getY() + 0.5);
        final double deltaZ = this.myrmex.getZ() - (pos.getZ() + 0.5);
        return deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ;
    }

    public class BlockSorter implements Comparator<BlockPos> {

        @Override
        public int compare(BlockPos pos1, BlockPos pos2) {
            double distance1 = MyrmexAIForage.this.getDistanceSq(pos1);
            double distance2 = MyrmexAIForage.this.getDistanceSq(pos2);
            return Double.compare(distance1, distance2);
        }
    }

    private List<BlockPos> getEdibleBlocks() {
        List<BlockPos> allBlocks = new ArrayList<>();
        BlockPos.betweenClosedStream(this.myrmex.blockPosition().offset(-RADIUS, -RADIUS / 2, -RADIUS),
            this.myrmex.blockPosition().offset(RADIUS, RADIUS / 2, RADIUS)).map(BlockPos::immutable).forEach(pos -> {
            if (!MinecraftForge.EVENT_BUS
                .post(new GenericGriefEvent(this.myrmex, pos.getX(), pos.getY(), pos.getZ()))) {
                if (EntityMyrmexBase.isEdibleBlock(this.myrmex.level().getBlockState(pos))) {
                    allBlocks.add(pos);
                    this.myrmex.keepSearching = false;
                }
            }
        });
        return allBlocks;
    }

    private boolean increaseRadiusAndWander() {
        this.myrmex.keepSearching = true;
        if (myrmex.getHive() != null) {
            wanderRadius = myrmex.getHive().getWanderRadius();
            myrmex.getHive().setWanderRadius(wanderRadius * 2);
        }
        // Increase wander radius
        wanderRadius *= 2;
        // this.myrmex.setWaitTicks(80+new Random().nextInt(40));
        // Set target as random position inside wanderRadius
        if (wanderRadius >= IafConfig.myrmexMaximumWanderRadius) {
            wanderRadius = IafConfig.myrmexMaximumWanderRadius;
            this.myrmex.setWaitTicks(80 + ThreadLocalRandom.current().nextInt(40));
            // Keep track of how many times the myrmex has potentially not found a path to a
            // target
            failedToFindPath++;
            if (failedToFindPath >= 10) {
                this.myrmex.setWaitTicks(800 + ThreadLocalRandom.current().nextInt(40));
            }
        }
        Vec3 vec = DefaultRandomPos.getPos(this.myrmex, wanderRadius, 7);
        if (vec != null) {
            this.targetBlock = BlockPos.containing(vec);
        }
        if (this.targetBlock != null) {
            this.path = ((AdvancedPathNavigate) this.myrmex.getNavigation()).moveToXYZ(targetBlock.getX(),
                targetBlock.getY(), targetBlock.getZ(), 1D);
            return true;
        }
        return false;
    }

}
