package com.github.alexthe666.iceandfire.block;

import java.util.Random;

import javax.annotation.Nullable;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.core.ModBlocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockGrassPath;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockCharedPath extends BlockGrassPath {
	public Item itemBlock;
	boolean isFire;

	@SuppressWarnings("deprecation")
	public BlockCharedPath(boolean isFire) {
		super();
		this.isFire = isFire;
		this.setUnlocalizedName(isFire ? "iceandfire.charedGrassPath" : "iceandfire.frozenGrassPath");
		this.setHarvestLevel("shovel", 0);
		this.setHardness(0.6F);
		this.setSoundType(isFire ? SoundType.GROUND : SoundType.GLASS);
		this.setCreativeTab(IceAndFire.TAB);
		if (!isFire) {
			this.slipperiness = 0.98F;
		}
		this.setLightOpacity(0);
		setRegistryName(IceAndFire.MODID, isFire ? "chared_grass_path" : "frozen_grass_path");
	}

	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
		switch (side) {
			case UP:
				return true;
			case NORTH:
			case SOUTH:
			case WEST:
			case EAST:
				IBlockState iblockstate = blockAccess.getBlockState(pos.offset(side));
				Block block = iblockstate.getBlock();
				return !iblockstate.isOpaqueCube() && block != Blocks.FARMLAND && block != Blocks.GRASS_PATH && block != ModBlocks.charedGrassPath && block != ModBlocks.frozenGrassPath;
			default:
				return super.shouldSideBeRendered(blockState, blockAccess, pos, side);
		}
	}

	@Nullable
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return isFire ? ModBlocks.charedDirt.getItemDropped(Blocks.DIRT.getDefaultState(), rand, fortune) : ModBlocks.frozenDirt.getItemDropped(Blocks.DIRT.getDefaultState(), rand, fortune);
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
		super.neighborChanged(state, worldIn, pos, blockIn, fromPos);

		if (worldIn.getBlockState(pos.up()).getMaterial().isSolid()) {
			worldIn.setBlockState(pos, isFire ? ModBlocks.charedDirt.getDefaultState() : ModBlocks.frozenDirt.getDefaultState());
		}
	}

}
