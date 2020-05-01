package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.BlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockDreadStoneFace extends BlockHorizontal implements IDreadBlock, IDragonProof {
    public static final PropertyBool PLAYER_PLACED = PropertyBool.create("player_placed");

    public BlockDreadStoneFace() {
        super(Material.ROCK);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(PLAYER_PLACED, Boolean.valueOf(false)));
        this.setTranslationKey("iceandfire.dread_stone_face");
        this.setHarvestLevel("pickaxe", 3);
        this.setHardness(20F);
        this.setResistance(10000F);
        this.setSoundType(SoundType.STONE);
        this.setCreativeTab(IceAndFire.TAB_BLOCKS);
        setRegistryName(IceAndFire.MODID, "dread_stone_face");
    }

    @Override
    public float getBlockHardness(BlockState blockState, World worldIn, BlockPos pos) {
        return blockState.getValue(PLAYER_PLACED) ? super.getBlockHardness(blockState, worldIn, pos) : -1;
    }

    public BlockState withRotation(BlockState state, Rotation rot) {
        return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
    }

    public BlockState withMirror(BlockState state, Mirror mirrorIn) {
        return state.withRotation(mirrorIn.toRotation(state.getValue(FACING)));
    }

    public BlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite()).withProperty(PLAYER_PLACED, true);
    }

    public BlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(FACING, EnumFacing.byHorizontalIndex(meta % 4)).withProperty(PLAYER_PLACED, (meta / 4 == 0));
    }

    public int getMetaFromState(BlockState state) {
        return state.getValue(FACING).getHorizontalIndex() * (state.getValue(PLAYER_PLACED) ? 1 : 2);
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, PLAYER_PLACED);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

}
