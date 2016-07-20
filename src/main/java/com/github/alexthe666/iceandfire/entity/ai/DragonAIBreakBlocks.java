package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.EntityFireDragon;
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
        for(int x = -radius; x < radius; x++){
            for(int y = 0; y < radius * 1.25; y++){
                for(int z = -radius; z < radius; z++){
                    BlockPos pos = new BlockPos(dragon).add(x, y, z);
                    IBlockState state = dragon.worldObj.getBlockState(pos);
                    if(state.getBlockHardness(dragon.worldObj, pos) > -1 && state.getBlockHardness(dragon.worldObj, pos) < 5 && !dragon.worldObj.isAirBlock(pos) && state.getMaterial().getMobilityFlag() != EnumPushReaction.DESTROY){
                        if(dragon.getDragonStage() == 3 && state.getBlockHardness(dragon.worldObj, pos) < 1.5){
                            dragon.worldObj.destroyBlock(pos, true);
                        }
                        if(dragon.getDragonStage() > 3){
                            dragon.worldObj.destroyBlock(pos, true);
                        }
                    }
                }
            }
        }
    }
}
