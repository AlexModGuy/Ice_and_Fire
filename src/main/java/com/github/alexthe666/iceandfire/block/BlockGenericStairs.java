package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class BlockGenericStairs extends StairBlock {

    public BlockGenericStairs(BlockState modelState) {
        super(
            modelState,
            BlockBehaviour.Properties
                .of(modelState.getMaterial())
                .strength(20F)
        );
    }
}
