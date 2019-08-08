package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.block.BlockBush;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockMyrmexBiolight extends BlockBush {

    public static final PropertyBool CONNECTED_DOWN = PropertyBool.create("down");
    protected static final AxisAlignedBB BUSH_AABB = new AxisAlignedBB(0.25D, 0.0D, 0.25D, 0.75D, 1D, 0.75D);

    public BlockMyrmexBiolight(boolean jungle) {
        super(Material.PLANTS);
        this.setHardness(0F);
        this.setLightLevel(0.6F);
        this.setTranslationKey(jungle ? "iceandfire.myrmex_jungle_biolight" : "iceandfire.myrmex_desert_biolight");
        this.setCreativeTab(IceAndFire.TAB_BLOCKS);
        this.setSoundType(SoundType.PLANT);
        this.setRegistryName(IceAndFire.MODID, jungle ? "myrmex_jungle_biolight" : "myrmex_desert_biolight");
        this.setDefaultState(this.blockState.getBaseState().withProperty(CONNECTED_DOWN, Boolean.valueOf(false)));
    }

    public boolean isPassable(IBlockAccess worldIn, BlockPos pos){
        return true;
    }


    public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state) {
        return worldIn.getBlockState(pos.up()).isOpaqueCube() || worldIn.getBlockState(pos.up()).getBlock() == this;
    }

    public boolean canBlockStay(World worldIn, BlockPos pos) {
        return worldIn.getBlockState(pos.up()).isOpaqueCube() || worldIn.getBlockState(pos.up()).getBlock() == this;
    }

    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        return worldIn.getBlockState(pos).getBlock().isReplaceable(worldIn, pos) && canBlockStay(worldIn, pos);
    }

    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return BUSH_AABB;
    }

    protected boolean canSustainBush(IBlockState state) {
        return true;
    }

    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        return state.withProperty(CONNECTED_DOWN, worldIn.getBlockState(pos.down()).getBlock() == this);
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[]{CONNECTED_DOWN});
    }

    public int getMetaFromState(IBlockState state) {
        return 0;
    }
}