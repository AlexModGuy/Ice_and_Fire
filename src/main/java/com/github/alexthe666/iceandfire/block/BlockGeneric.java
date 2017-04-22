package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.*;
import com.github.alexthe666.iceandfire.core.*;
import net.minecraft.block.*;
import net.minecraft.block.material.*;
import net.minecraft.block.state.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraftforge.fml.common.registry.*;
import net.minecraftforge.fml.relauncher.*;

public class BlockGeneric extends Block {

	public BlockGeneric (Material materialIn, String gameName, String name, String toolUsed, int toolStrength, float hardness, float resistance, SoundType sound) {
		super (materialIn);
		this.setUnlocalizedName (name);
		this.setHarvestLevel (toolUsed, toolStrength);
		this.setHardness (hardness);
		this.setResistance (resistance);
		this.setSoundType (sound);
		this.setCreativeTab (IceAndFire.TAB);
		GameRegistry.registerBlock (this, gameName);
	}

	public BlockGeneric (Material materialIn, String gameName, String name, String toolUsed, int toolStrength, float hardness, float resistance, SoundType sound, boolean slippery) {
		super (materialIn);
		this.setUnlocalizedName (name);
		this.setHarvestLevel (toolUsed, toolStrength);
		this.setHardness (hardness);
		this.setResistance (resistance);
		this.setSoundType (sound);
		this.setCreativeTab (IceAndFire.TAB);
		GameRegistry.registerBlock (this, gameName);
		if (slippery) {
			this.slipperiness = 0.98F;
		}
	}

	public BlockGeneric (Material materialIn, String gameName, String name, float hardness, float resistance, SoundType sound) {
		super (materialIn);
		this.setUnlocalizedName (name);
		this.setHardness (hardness);
		this.setResistance (resistance);
		this.setSoundType (sound);
		this.setCreativeTab (IceAndFire.TAB);
		GameRegistry.registerBlock (this, gameName);

	}

	@SideOnly (Side.CLIENT)
	public BlockRenderLayer getBlockLayer () {
		return this == ModBlocks.dragon_ice ? BlockRenderLayer.TRANSLUCENT : super.getBlockLayer ();
	}

	public boolean isOpaqueCube (IBlockState state) {
		return this != ModBlocks.dragon_ice;
	}

	public boolean isFullCube (IBlockState state) {
		return this != ModBlocks.dragon_ice;
	}

	@SideOnly (Side.CLIENT)
	public boolean shouldSideBeRendered (IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
		IBlockState iblockstate = blockAccess.getBlockState (pos.offset (side));
		Block block = iblockstate.getBlock ();
		if (this == ModBlocks.dragon_ice) {
			if (blockState != iblockstate) {
				return true;
			}
			if (block == this) {
				return false;
			}
		} else {
			return super.shouldSideBeRendered (blockState, blockAccess, pos, side);
		}
		return block != this ? false : super.shouldSideBeRendered (blockState, blockAccess, pos, side);
	}
}
