package com.github.alexthe666.iceandfire.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.StairsBlock;
import net.minecraftforge.common.ToolType;

public class BlockGenericStairs extends StairsBlock {

    public BlockGenericStairs(BlockState modelState, String name) {
        super(
    		modelState,
    		Block.Properties
    			.create(modelState.getMaterial())
    			.harvestTool(ToolType.PICKAXE)
    			.hardnessAndResistance(20F)
		);
        
        this.setRegistryName(name);
    }
}
