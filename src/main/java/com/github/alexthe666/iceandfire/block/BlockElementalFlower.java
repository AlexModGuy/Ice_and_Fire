package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import java.util.Random;

public class BlockElementalFlower extends BushBlock {
    public Item itemBlock;

    public BlockElementalFlower(boolean isFire) {
        super(Properties.create(Material.PLANTS).notSolid().variableOpacity().tickRandomly().sound(SoundType.PLANT));
        setRegistryName(IceAndFire.MODID, isFire ? "fire_lily" : "frost_lily");
    }

    protected boolean isValidGround(BlockState state, IBlockReader worldIn, BlockPos pos) {
        Block block = state.getBlock();
        return true;
    }

    public boolean canStay(World worldIn, BlockPos pos) {
        BlockState soil = worldIn.getBlockState(pos.down());
        if (this == IafBlockRegistry.FIRE_LILY) {
            return soil.getMaterial() == Material.SAND || soil.getBlock() == Blocks.NETHERRACK;
        } else {
            return soil.getMaterial() == Material.PACKED_ICE || soil.getMaterial() == Material.ICE;
        }
    }

    public void updateTick(World worldIn, BlockPos pos, BlockState state, Random rand) {
        this.checkFall(worldIn, pos);
    }

    private boolean checkFall(World worldIn, BlockPos pos) {
        if (!this.canStay(worldIn, pos)) {
            worldIn.destroyBlock(pos, true);
            return false;
        } else {
            return true;
        }
    }

    protected boolean canSustainBush(BlockState state) {
        return true;
    }

}
