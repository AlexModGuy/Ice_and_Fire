package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.EntityFireDragon;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

public class DragonAIBreakBlocks extends EntityAIBase {
    private EntityDragonBase dragon;

    public DragonAIBreakBlocks(EntityDragonBase dragon) {
        this.dragon = dragon;
    }

    @Override
    public boolean shouldExecute() {
        return dragon.getDragonStage() > 2 && !dragon.isModelDead();
    }

    public void updateTask() {
        int radius = (int)(dragon.getRenderSize() * 0.3F);
        for (int a = (int) Math.round(dragon.getEntityBoundingBox().minX) - 1; a <= (int) Math.round(dragon.getEntityBoundingBox().maxX) + 1; a++) {
            for (int b = (int) Math.round(dragon.getEntityBoundingBox().minY) + 1; (b <= (int) Math.round(dragon.getEntityBoundingBox().maxY) + 3) && (b <= 127); b++) {
                for (int c = (int) Math.round(dragon.getEntityBoundingBox().minZ) - 1; c <= (int) Math.round(dragon.getEntityBoundingBox().maxZ) + 1; c++) {
                    BlockPos pos = new BlockPos(a, b, c);
                    IBlockState state = dragon.worldObj.getBlockState(pos);
                    if (state.getBlockHardness(dragon.worldObj, pos) > -1 && state.getBlockHardness(dragon.worldObj, pos) < 5 && !dragon.worldObj.isAirBlock(pos) && state.getMaterial().getMobilityFlag() != EnumPushReaction.DESTROY) {
                        if (dragon.getDragonStage() == 3 && state.getBlockHardness(dragon.worldObj, pos) < 1.5) {
                            dragon.worldObj.destroyBlock(pos, false);
                        }
                        if (dragon.getDragonStage() > 3) {
                            dragon.worldObj.destroyBlock(pos, false);
                        }
                    }
                }
            }
        }
    }
}
