package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.*;
import com.github.alexthe666.iceandfire.core.*;
import net.minecraft.block.*;
import net.minecraft.block.state.*;
import net.minecraft.init.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraftforge.fml.common.registry.*;
import net.minecraftforge.fml.relauncher.*;

import javax.annotation.*;
import java.util.*;

public class BlockCharedPath extends BlockGrassPath {
	boolean isFire;

	public BlockCharedPath (boolean isFire) {
		super ();
		this.isFire = isFire;
		this.setUnlocalizedName (isFire ? "iceandfire.charedGrassPath" : "iceandfire.frozenGrassPath");
		this.setHarvestLevel ("shovel", 0);
		this.setHardness (0.6F);
		this.setSoundType (isFire ? SoundType.GROUND : SoundType.GLASS);
		this.setCreativeTab (IceAndFire.TAB);
		GameRegistry.registerBlock (this, isFire ? "chared_grass_path" : "frozen_grass_path");
		if (!isFire) {
			this.slipperiness = 0.98F;
		}
		this.setLightOpacity (0);
	}

	@SideOnly (Side.CLIENT)
	public boolean shouldSideBeRendered (IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
		switch (side) {
			case UP:
				return true;
			case NORTH:
			case SOUTH:
			case WEST:
			case EAST:
				IBlockState iblockstate = blockAccess.getBlockState (pos.offset (side));
				Block block = iblockstate.getBlock ();
				return !iblockstate.isOpaqueCube () && block != Blocks.FARMLAND && block != Blocks.GRASS_PATH && block != ModBlocks.charedGrassPath && block != ModBlocks.frozenGrassPath;
			default:
				return super.shouldSideBeRendered (blockState, blockAccess, pos, side);
		}
	}

	@Nullable
	public Item getItemDropped (IBlockState state, Random rand, int fortune) {
		return isFire ? ModBlocks.charedDirt.getItemDropped (Blocks.DIRT.getDefaultState (), rand, fortune) : ModBlocks.frozenDirt.getItemDropped (Blocks.DIRT.getDefaultState (), rand, fortune);
	}

	public void neighborChanged (IBlockState state, World worldIn, BlockPos pos, Block blockIn) {
		super.neighborChanged (state, worldIn, pos, blockIn);

		if (worldIn.getBlockState (pos.up ()).getMaterial ().isSolid ()) {
			worldIn.setBlockState (pos, isFire ? ModBlocks.charedDirt.getDefaultState () : ModBlocks.frozenDirt.getDefaultState ());
		}
	}

}
