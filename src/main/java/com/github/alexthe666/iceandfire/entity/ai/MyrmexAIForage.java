package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.api.event.GenericGriefEvent;
import com.github.alexthe666.iceandfire.entity.EntityMyrmexBase;
import com.github.alexthe666.iceandfire.entity.EntityMyrmexWorker;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.MinecraftForge;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MyrmexAIForage extends EntityAIBase {
    private static final int RADIUS = 8;

    private final EntityMyrmexWorker myrmex;
    private final BlockSorter targetSorter;
    private BlockPos targetBlock = BlockPos.ORIGIN;
    private int wanderRadius = RADIUS * 2;

    public MyrmexAIForage(EntityMyrmexWorker myrmex) {
        super();
        this.myrmex = myrmex;
        this.targetSorter = new BlockSorter();
        this.setMutexBits(1);
    }

    @Override
    public boolean shouldExecute() {
        if (!this.myrmex.canMove() || this.myrmex.holdingSomething() || !this.myrmex.getNavigator().noPath() || !myrmex.canSeeSky() || this.myrmex.shouldEnterHive() || !this.myrmex.keepSearching) {
            return false;
        }
        List<BlockPos> allBlocks = new ArrayList<BlockPos>();
        for (BlockPos pos : BlockPos.getAllInBox(this.myrmex.getPosition().add(-RADIUS, -RADIUS, -RADIUS), this.myrmex.getPosition().add(RADIUS, RADIUS, RADIUS))) {
            if (MinecraftForge.EVENT_BUS.post(new GenericGriefEvent(this.myrmex, pos.getX(), pos.getY(), pos.getZ()))) continue;
            if (EntityMyrmexBase.isEdibleBlock(this.myrmex.world.getBlockState(pos))) {
                allBlocks.add(pos);
                this.myrmex.keepSearching = false;
            }
        }
        if (allBlocks.isEmpty()) {
            this.myrmex.keepSearching = true;
            this.wanderRadius += RADIUS;
            Vec3d vec = RandomPositionGenerator.findRandomTarget(this.myrmex, wanderRadius, 7);
            if (vec != null) {
                this.targetBlock = new BlockPos(vec);
            }
            return true;
        }
        allBlocks.sort(this.targetSorter);
        this.targetBlock = allBlocks.get(0);
        return true;
    }

    @Override
    public boolean shouldContinueExecuting() {
        if (!this.myrmex.keepSearching) {
            if (this.targetBlock == null) {
                return false;
            }
        }
        if (myrmex.shouldEnterHive()) {
            this.myrmex.keepSearching = false;
            return false;
        }
        return this.myrmex.getNavigator().noPath();
    }

    @Override
    public void updateTask() {
        if (this.myrmex.keepSearching) {
            if(this.myrmex.getNavigator().noPath()) {
                this.myrmex.getNavigator().tryMoveToXYZ(this.targetBlock.getX() + 0.5D, this.targetBlock.getY(), this.targetBlock.getZ() + 0.5D, 1D);
            }
            if (this.myrmex.getDistanceSqToCenter(this.targetBlock) < 4) {
                this.resetTask();
            }
        } else if (this.targetBlock != null) {
            this.myrmex.getNavigator().tryMoveToXYZ(this.targetBlock.getX() + 0.5D, this.targetBlock.getY(), this.targetBlock.getZ() + 0.5D, 1D);
            IBlockState block = this.myrmex.world.getBlockState(this.targetBlock);

            if (EntityMyrmexBase.isEdibleBlock(block)) {
                double distance = this.getDistance(this.targetBlock);
                if (distance <= 6) {
                    List<ItemStack> drops = block.getBlock().getDrops(this.myrmex.world, this.targetBlock, block, 0); // use the old method until it gets removed, for backward compatibility
                    if (!drops.isEmpty()) {
                        this.myrmex.world.destroyBlock(this.targetBlock, false);
                        ItemStack heldStack = drops.get(0).copy();
                        heldStack.setCount(1);
                        drops.get(0).shrink(1);
                        this.myrmex.setHeldItem(EnumHand.MAIN_HAND, heldStack);
                        for (ItemStack stack : drops) {
                            EntityItem itemEntity = new EntityItem(this.myrmex.world, this.targetBlock.getX() + this.myrmex.getRNG().nextDouble(), this.targetBlock.getY() + this.myrmex.getRNG().nextDouble(), this.targetBlock.getZ() + this.myrmex.getRNG().nextDouble(), stack);
                            itemEntity.setDefaultPickupDelay();
                            if (!this.myrmex.world.isRemote) {
                                this.myrmex.world.spawnEntity(itemEntity);
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
        this.targetBlock = BlockPos.ORIGIN;
        this.myrmex.keepSearching = true;
    }

    private double getDistance(BlockPos pos) {
        double deltaX = this.myrmex.posX - (pos.getX() + 0.5);
        double deltaY = this.myrmex.posY + this.myrmex.getEyeHeight() - (pos.getY() + 0.5);
        double deltaZ = this.myrmex.posZ - (pos.getZ() + 0.5);
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
