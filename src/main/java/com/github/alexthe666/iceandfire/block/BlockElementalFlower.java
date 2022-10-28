package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Random;

public class BlockElementalFlower extends BushBlock {
    public Item itemBlock;
    protected static final VoxelShape SHAPE = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 13.0D, 14.0D);

    public BlockElementalFlower() {
        super(
            Properties
                .of(Material.REPLACEABLE_PLANT)
                .noOcclusion()
                .noCollission()
                .dynamicShape()
                .randomTicks()
                .sound(SoundType.GRASS)
		);
    }

    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    protected boolean mayPlaceOn(BlockState state, BlockGetter worldIn, BlockPos pos) {
        Block block = state.getBlock();
        return block == Blocks.GRASS_BLOCK || block == Blocks.DIRT || block == Blocks.COARSE_DIRT || block == Blocks.PODZOL || block == Blocks.FARMLAND || state.getMaterial() == Material.SAND;
    }

    public boolean canStay(Level worldIn, BlockPos pos) {
        BlockState soil = worldIn.getBlockState(pos.below());
        if (this == IafBlockRegistry.FIRE_LILY.get()) {
            return soil.getMaterial() == Material.SAND || soil.getBlock() == Blocks.NETHERRACK;
        } else if (this == IafBlockRegistry.LIGHTNING_LILY.get()) {
            return soil.getMaterial() == Material.DIRT || soil.getBlock() == Blocks.GRASS;
        } else {
            return soil.getMaterial() == Material.ICE_SOLID || soil.getMaterial() == Material.ICE;
        }
    }

    public void updateTick(Level worldIn, BlockPos pos, BlockState state, Random rand) {
        this.checkFall(worldIn, pos);
    }

    private boolean checkFall(Level worldIn, BlockPos pos) {
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
