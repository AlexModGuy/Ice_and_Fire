package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockMyrmexConnectedResin extends Block {

    public static final PropertyBool UP = PropertyBool.create("up");
    public static final PropertyBool DOWN = PropertyBool.create("down");
    public static final PropertyBool NORTH = PropertyBool.create("north");
    public static final PropertyBool EAST = PropertyBool.create("east");
    public static final PropertyBool SOUTH = PropertyBool.create("south");
    public static final PropertyBool WEST = PropertyBool.create("west");

    public BlockMyrmexConnectedResin(boolean jungle, boolean glass) {
        super(Material.ROCK);
        this.setDefaultState(this.blockState.getBaseState().withProperty(UP, Boolean.valueOf(false))
                .withProperty(DOWN, Boolean.valueOf(false))
                .withProperty(NORTH, Boolean.valueOf(false))
                .withProperty(EAST, Boolean.valueOf(false))
                .withProperty(SOUTH, Boolean.valueOf(false))
                .withProperty(WEST, Boolean.valueOf(false))
        );
        if (glass) {
            this.setHardness(1.5F);
            this.setSoundType(SoundType.GLASS);
            this.setTranslationKey(jungle ? "iceandfire.myrmex_jungle_resin_glass" : "iceandfire.myrmex_desert_resin_glass");
            this.setRegistryName(IceAndFire.MODID, jungle ? "myrmex_jungle_resin_glass" : "myrmex_desert_resin_glass");
        } else {
            this.setHardness(3.5F);
            this.setSoundType(SoundType.STONE);
            this.setTranslationKey(jungle ? "iceandfire.myrmex_jungle_resin_block" : "iceandfire.myrmex_desert_resin_block");
            this.setRegistryName(IceAndFire.MODID, jungle ? "myrmex_jungle_resin_block" : "myrmex_desert_resin_block");
        }
        this.setCreativeTab(IceAndFire.TAB_BLOCKS);

    }

    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        return state.withProperty(UP, canFenceConnectTo(worldIn, pos, EnumFacing.UP))
                .withProperty(DOWN, canFenceConnectTo(worldIn, pos, EnumFacing.DOWN))
                .withProperty(NORTH, canFenceConnectTo(worldIn, pos, EnumFacing.NORTH))
                .withProperty(SOUTH, canFenceConnectTo(worldIn, pos, EnumFacing.SOUTH))
                .withProperty(EAST, canFenceConnectTo(worldIn, pos, EnumFacing.EAST))
                .withProperty(WEST, canFenceConnectTo(worldIn, pos, EnumFacing.WEST));
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, UP, DOWN, NORTH, SOUTH, EAST, WEST);
    }

    public int getMetaFromState(IBlockState state) {
        return 0;
    }

    private boolean canFenceConnectTo(IBlockAccess world, BlockPos pos, EnumFacing facing) {
        BlockPos other = pos.offset(facing);
        Block block = world.getBlockState(other).getBlock();
        return block == this;
    }

    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }


    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        IBlockState iblockstate = blockAccess.getBlockState(pos.offset(side));
        Block block = iblockstate.getBlock();

        if (block == this) {
            return false;
        }

        return block != this && super.shouldSideBeRendered(blockState, blockAccess, pos, side);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }

}
