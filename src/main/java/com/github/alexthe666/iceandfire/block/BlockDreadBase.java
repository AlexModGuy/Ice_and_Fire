package com.github.alexthe666.iceandfire.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockDreadBase extends BlockGeneric implements IDragonProof, IDreadBlock {
    public static final PropertyBool PLAYER_PLACED = PropertyBool.create("player_placed");

    public BlockDreadBase(Material materialIn, String gameName, String name, String toolUsed, int toolStrength, float hardness, float resistance, SoundType sound) {
        super(materialIn, gameName, name, toolUsed, toolStrength, hardness, resistance, sound);
        this.setDefaultState(this.blockState.getBaseState().withProperty(PLAYER_PLACED, Boolean.valueOf(false)));
    }

    public BlockDreadBase(Material materialIn, String gameName, String name, String toolUsed, int toolStrength, float hardness, float resistance, SoundType sound, boolean slippery) {
        super(materialIn, gameName, name, toolUsed, toolStrength, hardness, resistance, sound, slippery);
        this.setDefaultState(this.blockState.getBaseState().withProperty(PLAYER_PLACED, Boolean.valueOf(false)));
    }

    public BlockDreadBase(Material materialIn, String gameName, String name, float hardness, float resistance, SoundType sound) {
        super(materialIn, gameName, name, hardness, resistance, sound);
        this.setDefaultState(this.blockState.getBaseState().withProperty(PLAYER_PLACED, Boolean.valueOf(false)));
    }

    @Override
    public float getBlockHardness(IBlockState blockState, World worldIn, BlockPos pos) {
        return blockState.getValue(PLAYER_PLACED) ? super.getBlockHardness(blockState, worldIn, pos) : -1;
    }

    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(PLAYER_PLACED, Boolean.valueOf(meta > 0));
    }

    public int getMetaFromState(IBlockState state) {
        return state.getValue(PLAYER_PLACED).booleanValue() ? 1 : 0;
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, PLAYER_PLACED);
    }

    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return this.getDefaultState().withProperty(PLAYER_PLACED, true);
    }

}
