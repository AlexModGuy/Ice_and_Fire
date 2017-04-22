package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.*;
import com.github.alexthe666.iceandfire.entity.tile.*;
import net.minecraft.block.*;
import net.minecraft.block.material.*;
import net.minecraft.block.properties.*;
import net.minecraft.block.state.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.tileentity.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraftforge.fml.common.registry.*;
import net.minecraftforge.fml.relauncher.*;

import java.util.*;

public class BlockLectern extends BlockContainer {
	public static final PropertyDirection FACING = PropertyDirection.create ("facing", EnumFacing.Plane.HORIZONTAL);

	public BlockLectern () {
		super (Material.WOOD);
		this.setHardness (2.0F);
		this.setResistance (5.0F);
		this.setDefaultState (this.blockState.getBaseState ().withProperty (FACING, EnumFacing.NORTH));
		this.setSoundType (SoundType.WOOD);
		this.setCreativeTab (IceAndFire.TAB);
		this.setUnlocalizedName ("iceandfire.lectern");
		GameRegistry.registerBlock (this, "lectern");
		GameRegistry.registerTileEntity (TileEntityLectern.class, "lectern");
	}

	@Override
	public AxisAlignedBB getBoundingBox (IBlockState state, IBlockAccess source, BlockPos pos) {
		return new AxisAlignedBB (0.125F, 0, 0.125F, 0.875F, 1.4375F, 0.875F);
	}

	@Override
	public boolean isOpaqueCube (IBlockState blockstate) {
		return false;
	}

	@Override
	public boolean isFullCube (IBlockState blockstate) {
		return false;
	}

	@Override
	public boolean canPlaceBlockAt (World worldIn, BlockPos pos) {
		IBlockState iblockstate = worldIn.getBlockState (pos.down ());
		Block block = iblockstate.getBlock ();
		return iblockstate.isOpaqueCube ();
	}

	public void neighborChanged (IBlockState state, World worldIn, BlockPos pos, Block blockIn) {
		worldIn.scheduleUpdate (pos, this, this.tickRate (worldIn));
	}

	public void updateTick (World worldIn, BlockPos pos, IBlockState state, Random rand) {
		this.checkFall (worldIn, pos);
	}

	private boolean checkFall (World worldIn, BlockPos pos) {
		if (!this.canPlaceBlockAt (worldIn, pos)) {
			worldIn.destroyBlock (pos, true);
			return false;
		} else {
			return true;
		}
	}

	public IBlockState getStateForPlacement (World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
		return super.getStateForPlacement (worldIn, pos, facing, hitX, hitY, hitZ, meta, placer).withProperty (FACING, placer.getHorizontalFacing ().getOpposite ());
	}

	@Override
	public IBlockState getStateFromMeta (int meta) {
		return this.getDefaultState ().withProperty (FACING, EnumFacing.getHorizontal (meta));
	}

	@Override
	public int getMetaFromState (IBlockState state) {
		return state.getValue (FACING).getHorizontalIndex ();
	}

	@Override
	protected BlockStateContainer createBlockState () {
		return new BlockStateContainer (this, new IProperty[]{FACING});
	}

	@Override
	@SideOnly (Side.CLIENT)
	public BlockRenderLayer getBlockLayer () {
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public EnumBlockRenderType getRenderType (IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public boolean onBlockActivated (World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (playerIn.isSneaking ()) {
			return false;
		} else {
			playerIn.openGui (IceAndFire.INSTANCE, 2, worldIn, pos.getX (), pos.getY (), pos.getZ ());
			return true;
		}
	}

	@Override
	public TileEntity createNewTileEntity (World world, int meta) {
		return new TileEntityLectern ();
	}
}