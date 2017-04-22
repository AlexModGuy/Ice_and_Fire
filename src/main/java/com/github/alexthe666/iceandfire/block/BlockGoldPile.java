package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.*;
import com.github.alexthe666.iceandfire.core.*;
import com.github.alexthe666.iceandfire.structures.*;
import net.minecraft.block.*;
import net.minecraft.block.material.*;
import net.minecraft.block.properties.*;
import net.minecraft.block.state.*;
import net.minecraft.entity.player.*;
import net.minecraft.init.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraftforge.fml.common.registry.*;
import net.minecraftforge.fml.relauncher.*;

import java.util.*;

public class BlockGoldPile extends Block {
	public static final PropertyInteger LAYERS = PropertyInteger.create ("layers", 1, 8);
	protected static final AxisAlignedBB[] SNOW_AABB = new AxisAlignedBB[]{new AxisAlignedBB (0.0D, 0.0D, 0.0D, 1.0D, 0.0D, 1.0D), new AxisAlignedBB (0.0D, 0.0D, 0.0D, 1.0D, 0.125D, 1.0D), new AxisAlignedBB (0.0D, 0.0D, 0.0D, 1.0D, 0.25D, 1.0D), new AxisAlignedBB (0.0D, 0.0D, 0.0D, 1.0D, 0.375D, 1.0D), new AxisAlignedBB (0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D), new AxisAlignedBB (0.0D, 0.0D, 0.0D, 1.0D, 0.625D, 1.0D), new AxisAlignedBB (0.0D, 0.0D, 0.0D, 1.0D, 0.75D, 1.0D), new AxisAlignedBB (0.0D, 0.0D, 0.0D, 1.0D, 0.875D, 1.0D), new AxisAlignedBB (0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D)};

	public BlockGoldPile () {
		super (Material.GROUND);
		GameRegistry.registerBlock (this, "goldpile");
		this.setDefaultState (this.blockState.getBaseState ().withProperty (LAYERS, 1));
		this.setTickRandomly (true);
		this.setCreativeTab (IceAndFire.TAB);
		this.setUnlocalizedName ("iceandfire.goldpile");
		this.setHardness (0.3F);
	}

	@Override
	public AxisAlignedBB getBoundingBox (IBlockState state, IBlockAccess source, BlockPos pos) {
		return SNOW_AABB[state.getValue (LAYERS)];
	}

	@Override
	public boolean isPassable (IBlockAccess worldIn, BlockPos pos) {
		return worldIn.getBlockState (pos).getValue (LAYERS) < 5;
	}

	/**
	 * Checks if an IBlockState represents a block that is opaque and a full
	 * cube.
	 *
	 * @param state The block state to check.
	 */
	@Override
	public boolean isFullyOpaque (IBlockState state) {
		return state.getValue (LAYERS) == 7;
	}

	@Override
	public AxisAlignedBB getSelectedBoundingBox (IBlockState blockState, World worldIn, BlockPos pos) {
		int i = blockState.getValue (LAYERS) - 1;
		float f = 0.125F;
		AxisAlignedBB axisalignedbb = blockState.getBoundingBox (worldIn, pos);
		return new AxisAlignedBB (axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ, axisalignedbb.maxX, i * f, axisalignedbb.maxZ);
	}

	@Override
	public boolean canPlaceBlockAt (World worldIn, BlockPos pos) {
		IBlockState iblockstate = worldIn.getBlockState (pos.down ());
		Block block = iblockstate.getBlock ();
		return block != Blocks.ICE && block != Blocks.PACKED_ICE ? (iblockstate.getBlock ().isLeaves (iblockstate, worldIn, pos.down ()) ? true : (block == this && iblockstate.getValue (LAYERS) >= 7 ? true : iblockstate.isOpaqueCube () && iblockstate.getMaterial ().blocksMovement ())) : false;
	}

	@Override
	public boolean onBlockActivated (World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		ItemStack item = playerIn.inventory.getCurrentItem ();

		if (item != null) {
			if (item.getItem () != null) {
				if (item.getItem () == Items.GOLD_NUGGET || item.getItem () == Item.getItemFromBlock (ModBlocks.goldPile)) {
					if (item != null) {
						if (this.getMetaFromState (state) < 7) {
							WorldUtils.setBlock (worldIn, pos.getX (), pos.getY (), pos.getZ (), ModBlocks.goldPile, this.getMetaFromState (state) + 1, 3);
							if (!playerIn.capabilities.isCreativeMode) {
								--item.stackSize;

								if (item.stackSize <= 0) {
									playerIn.inventory.setInventorySlotContents (playerIn.inventory.currentItem, (ItemStack) null);
								}
							}
							return true;
						}
					}
				}
			}
		}
		return super.onBlockActivated (worldIn, pos, state, playerIn, hand, item, side, hitX, hitY, hitZ);
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
	public void onNeighborChange (IBlockAccess world, BlockPos pos, BlockPos neighbor) {
		if (world instanceof World) {
			this.checkAndDropBlock ((World) world, pos, ((World) world).getBlockState (neighbor));
		}
	}

	private boolean checkAndDropBlock (World worldIn, BlockPos pos, IBlockState state) {
		if (!this.canPlaceBlockAt (worldIn, pos)) {
			worldIn.destroyBlock (pos, true);
			return false;
		} else {
			return true;
		}
	}

	@Override
	public Item getItemDropped (IBlockState state, Random rand, int fortune) {
		return Items.GOLD_NUGGET;
	}

	@Override
	@SideOnly (Side.CLIENT)
	public boolean shouldSideBeRendered (IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
		return side == EnumFacing.UP ? true : super.shouldSideBeRendered (blockState, blockAccess, pos, side);
	}

	@Override
	public IBlockState getStateFromMeta (int meta) {
		return this.getDefaultState ().withProperty (LAYERS, (meta & 7) + 1);
	}

	@Override
	public boolean isReplaceable (IBlockAccess worldIn, BlockPos pos) {
		return worldIn.getBlockState (pos).getValue (LAYERS) == 1;
	}

	@Override
	public int getMetaFromState (IBlockState state) {
		return state.getValue (LAYERS) - 1;
	}

	@Override
	public int quantityDropped (IBlockState state, int fortune, Random random) {
		return (state.getValue (LAYERS)) + 1;
	}

	@Override
	protected BlockStateContainer createBlockState () {
		return new BlockStateContainer (this, new IProperty[]{LAYERS});
	}
}