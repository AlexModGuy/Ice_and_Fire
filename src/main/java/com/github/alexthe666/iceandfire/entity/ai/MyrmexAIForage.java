package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.api.event.GenericGriefEvent;
import com.github.alexthe666.iceandfire.entity.EntityMyrmexBase;
import com.github.alexthe666.iceandfire.entity.EntityMyrmexWorker;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

public class MyrmexAIForage extends Goal {
    private static final int RADIUS = 8;

    private final EntityMyrmexWorker myrmex;
    private final BlockSorter targetSorter;
    private BlockPos targetBlock = null;
    private int wanderRadius = RADIUS * 2;

    public MyrmexAIForage(EntityMyrmexWorker myrmex) {
        super();
        this.myrmex = myrmex;
        this.targetSorter = new BlockSorter();
        this.setMutexFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean shouldExecute() {
        if (!this.myrmex.canMove() || this.myrmex.holdingSomething() || !this.myrmex.getNavigator().noPath() || !myrmex.canSeeSky() || this.myrmex.shouldEnterHive() || !this.myrmex.keepSearching) {
            return false;
        }
        List<BlockPos> allBlocks = new ArrayList<BlockPos>();
        for (BlockPos pos : BlockPos.getAllInBox(this.myrmex.func_233580_cy_().add(-RADIUS, -RADIUS/2, -RADIUS), this.myrmex.func_233580_cy_().add(RADIUS, RADIUS/2, RADIUS)).map(BlockPos::toImmutable).collect(Collectors.toList())) {
            if (MinecraftForge.EVENT_BUS.post(new GenericGriefEvent(this.myrmex, pos.getX(), pos.getY(), pos.getZ())))
                continue;
            if (EntityMyrmexBase.isEdibleBlock(this.myrmex.world.getBlockState(pos))) {
                allBlocks.add(pos);
                this.myrmex.keepSearching = false;
            }
        }
        if (allBlocks.isEmpty()) {
            this.myrmex.keepSearching = true;
            this.wanderRadius += RADIUS;
            Vector3d vec = RandomPositionGenerator.findRandomTarget(this.myrmex, wanderRadius, 7);
            if (vec != null) {
                this.targetBlock = new BlockPos(vec);
            }
            return this.targetBlock != null;
        }
        allBlocks.sort(this.targetSorter);
        this.targetBlock = allBlocks.get(0);
        return targetBlock != null;
    }

    @Override
    public boolean shouldContinueExecuting() {
        if (this.targetBlock == null) {
            return false;
        }
        if (myrmex.shouldEnterHive()) {
            this.myrmex.keepSearching = false;
            return false;
        }
        return this.myrmex.getNavigator().noPath();
    }

    @Override
    public void tick() {
        if (this.myrmex.keepSearching) {
            if(targetBlock != null){
                if (this.myrmex.getNavigator().noPath()) {
                    this.myrmex.getNavigator().tryMoveToXYZ(this.targetBlock.getX() + 0.5D, this.targetBlock.getY(), this.targetBlock.getZ() + 0.5D, 1D);
                }
                if (this.myrmex.getDistanceSq(this.targetBlock.getX() + 0.5D, this.targetBlock.getY() + 0.5D, this.targetBlock.getZ() + 0.5D) < 4) {
                    this.resetTask();
                }
            }
        } else if (this.targetBlock != null) {
            this.myrmex.getNavigator().tryMoveToXYZ(this.targetBlock.getX() + 0.5D, this.targetBlock.getY(), this.targetBlock.getZ() + 0.5D, 1D);
            BlockState block = this.myrmex.world.getBlockState(this.targetBlock);

            if (EntityMyrmexBase.isEdibleBlock(block)) {
                double distance = this.getDistance(this.targetBlock);
                if (distance < 6) {
                    List<ItemStack> drops = block.getBlock().getDrops(block, (ServerWorld) this.myrmex.world, this.targetBlock, this.myrmex.world.getTileEntity(targetBlock)); // use the old method until it gets removed, for backward compatibility
                    if (!drops.isEmpty()) {
                        this.myrmex.world.destroyBlock(this.targetBlock, false);
                        ItemStack heldStack = drops.get(0).copy();
                        heldStack.setCount(1);
                        drops.get(0).shrink(1);
                        this.myrmex.setHeldItem(Hand.MAIN_HAND, heldStack);
                        for (ItemStack stack : drops) {
                            ItemEntity itemEntity = new ItemEntity(this.myrmex.world, this.targetBlock.getX() + this.myrmex.getRNG().nextDouble(), this.targetBlock.getY() + this.myrmex.getRNG().nextDouble(), this.targetBlock.getZ() + this.myrmex.getRNG().nextDouble(), stack);
                            itemEntity.setDefaultPickupDelay();
                            if (!this.myrmex.world.isRemote) {
                                this.myrmex.world.addEntity(itemEntity);
                            }
                        }
                        this.targetBlock = null;
                        this.resetTask();
                        this.myrmex.keepSearching = false;
                        this.wanderRadius = RADIUS;
                        return;
                    }
                }
            }
        }

    }

    public void resetTask() {
        this.targetBlock = null;
        this.myrmex.keepSearching = true;
    }

    private double getDistance(BlockPos pos) {
        double deltaX = this.myrmex.getPosX() - (pos.getX() + 0.5);
        double deltaY = this.myrmex.getPosY() + this.myrmex.getEyeHeight() - (pos.getY() + 0.5);
        double deltaZ = this.myrmex.getPosZ() - (pos.getZ() + 0.5);
        return deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ;
    }

    public class BlockSorter implements Comparator<BlockPos> {
        @Override
        public int compare(BlockPos pos1, BlockPos pos2) {
            double distance1 = MyrmexAIForage.this.getDistance(pos1);
            double distance2 = MyrmexAIForage.this.getDistance(pos2);
            return Double.compare(distance1, distance2);
        }
    }
}
