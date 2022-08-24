package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.StairsBlock;
import net.minecraftforge.common.ToolType;

public class BlockGenericStairs extends StairsBlock {

    public BlockGenericStairs(BlockState modelState, String name) {
        super(
    		modelState,
			AbstractBlock.Properties
				.of(modelState.getMaterial())
				.harvestTool(ToolType.PICKAXE)
				.strength(20F)
		);
        
        this.setRegistryName(IceAndFire.MODID, name);
    }
}
