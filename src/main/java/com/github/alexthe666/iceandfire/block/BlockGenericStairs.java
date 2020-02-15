package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;

public class BlockGenericStairs extends BlockStairs {

    public BlockGenericStairs(IBlockState modelState, String name) {
        super(modelState);
        this.setLightOpacity(0);
        this.setCreativeTab(IceAndFire.TAB_BLOCKS);
        this.setTranslationKey("iceandfire." + name);
        this.setRegistryName(name);
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }
}
