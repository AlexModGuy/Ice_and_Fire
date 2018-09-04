package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityMyrmexBase;
import com.github.alexthe666.iceandfire.entity.EntityMyrmexWorker;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MyrmexAIForage extends EntityAIBase {
    private static final int RADIUS = 8;

    private final EntityMyrmexWorker myrmex;
    private final BlockSorter targetSorter;
    private BlockPos targetBlock;

    public MyrmexAIForage(EntityMyrmexWorker myrmex) {
        super();
        this.myrmex = myrmex;
        this.targetSorter = new BlockSorter();
        this.setMutexBits(1);
    }

    @Override
    public boolean shouldExecute() {
        if (!myrmex.canSeeSky()) {
            return false;
        }

        List<BlockPos> allBlocks = new ArrayList<>();
        for (BlockPos pos : BlockPos.getAllInBox(this.myrmex.getPosition().add(-RADIUS, -RADIUS, -RADIUS), this.myrmex.getPosition().add(RADIUS, RADIUS, RADIUS))) {
            if (EntityMyrmexBase.isEdibleBlock(this.myrmex.world.getBlockState(pos))) {
                allBlocks.add(pos);
                this.myrmex.keepSearching = false;
            }
        }
        if (allBlocks.isEmpty()) {
            this.myrmex.keepSearching = true;
            Vec3d vec = RandomPositionGenerator.findRandomTargetBlockTowards(this.myrmex, RADIUS * 2, 7, this.myrmex.getLookVec());
            if(vec != null){
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
        if(!this.myrmex.keepSearching) {
            if (this.targetBlock == null) {
                return false;
            }
        }
        return this.myrmex.getNavigator().noPath();
    }

    @Override
    public void updateTask() {
        if(this.myrmex.keepSearching){
            this.myrmex.getNavigator().tryMoveToXYZ(this.targetBlock.getX() + 0.5D, this.targetBlock.getY(), this.targetBlock.getZ() + 0.5D, 1D);
            if (this.myrmex.getDistanceSqToCenter(this.targetBlock) < 2) {
                this.resetTask();
            }
        }
        if (this.targetBlock != null) {
            this.myrmex.getNavigator().tryMoveToXYZ(this.targetBlock.getX() + 0.5D, this.targetBlock.getY(), this.targetBlock.getZ() + 0.5D, 1D);
            IBlockState block = this.myrmex.world.getBlockState(this.targetBlock);
            if (EntityMyrmexBase.isEdibleBlock(block)) {
                double distance = this.getDistance(this.targetBlock);
                if(this.myrmex.getNavigator().noPath()){
                    this.targetBlock = null;
                    this.resetTask();
                    return;
                }
                if (distance < this.myrmex.getEntityBoundingBox().getAverageEdgeLength() * 2) {
                    this.myrmex.setHeldItem(EnumHand.MAIN_HAND, new ItemStack(Item.getItemFromBlock(block.getBlock())));
                    this.myrmex.world.destroyBlock(this.targetBlock, false);
                    this.targetBlock = null;
                    this.resetTask();
                }
            }
        }
    }

    public void resetTask(){
        this.targetBlock = BlockPos.ORIGIN;
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
