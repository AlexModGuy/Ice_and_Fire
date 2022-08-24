package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import java.util.Random;

public class BlockElementalFlower extends BushBlock {
    public Item itemBlock;
    protected static final VoxelShape SHAPE = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 13.0D, 14.0D);

    public BlockElementalFlower(String name) {
        super(
            Properties
                .of(Material.REPLACEABLE_PLANT)
                .noOcclusion()
                .noCollission()
                .dynamicShape()
                .randomTicks()
                .sound(SoundType.GRASS)
		);

        setRegistryName(IceAndFire.MODID, name);
    }

    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }

    protected boolean mayPlaceOn(BlockState state, IBlockReader worldIn, BlockPos pos) {
        Block block = state.getBlock();
        return block == Blocks.GRASS_BLOCK || block == Blocks.DIRT || block == Blocks.COARSE_DIRT || block == Blocks.PODZOL || block == Blocks.FARMLAND || state.getMaterial() == Material.SAND;
    }

    public boolean canStay(World worldIn, BlockPos pos) {
        BlockState soil = worldIn.getBlockState(pos.below());
        if (this == IafBlockRegistry.FIRE_LILY) {
            return soil.getMaterial() == Material.SAND || soil.getBlock() == Blocks.NETHERRACK;
        } else  if (this == IafBlockRegistry.LIGHTNING_LILY) {
            return soil.getMaterial() == Material.DIRT || soil.getBlock() == Blocks.GRASS;
        } else {
            return soil.getMaterial() == Material.ICE_SOLID || soil.getMaterial() == Material.ICE;
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
