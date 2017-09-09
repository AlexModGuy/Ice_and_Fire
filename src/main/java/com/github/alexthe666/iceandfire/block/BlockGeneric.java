package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.core.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockGeneric extends Block {
	public Item itemBlock;

	public BlockGeneric(Material materialIn, String gameName, String name, String toolUsed, int toolStrength, float hardness, float resistance, SoundType sound) {
		super(materialIn);
		this.setUnlocalizedName(name);
		this.setHarvestLevel(toolUsed, toolStrength);
		this.setHardness(hardness);
		this.setResistance(resistance);
		this.setSoundType(sound);
		this.setCreativeTab(IceAndFire.TAB);
		setRegistryName(IceAndFire.MODID, gameName);
		GameRegistry.register(this);
		GameRegistry.register(itemBlock = (new ItemBlock(this).setRegistryName(this.getRegistryName())));
	}

	public BlockGeneric(Material materialIn, String gameName, String name, String toolUsed, int toolStrength, float hardness, float resistance, SoundType sound, boolean slippery) {
		super(materialIn);
		this.setUnlocalizedName(name);
		this.setHarvestLevel(toolUsed, toolStrength);
		this.setHardness(hardness);
		this.setResistance(resistance);
		this.setSoundType(sound);
		this.setCreativeTab(IceAndFire.TAB);
		if (slippery) {
			this.slipperiness = 0.98F;
		}
		setRegistryName(IceAndFire.MODID, gameName);
		GameRegistry.register(this);
		GameRegistry.register(itemBlock = (new ItemBlock(this).setRegistryName(this.getRegistryName())));
	}

	public BlockGeneric(Material materialIn, String gameName, String name, float hardness, float resistance, SoundType sound) {
		super(materialIn);
		this.setUnlocalizedName(name);
		this.setHardness(hardness);
		this.setResistance(resistance);
		this.setSoundType(sound);
		this.setCreativeTab(IceAndFire.TAB);
		setRegistryName(IceAndFire.MODID, gameName);
		GameRegistry.register(this);
		GameRegistry.register(itemBlock = (new ItemBlock(this).setRegistryName(this.getRegistryName())));

	}

	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return this == ModBlocks.dragon_ice ? BlockRenderLayer.TRANSLUCENT : super.getBlockLayer();
	}

	@SuppressWarnings("deprecation")
	public boolean isOpaqueCube(IBlockState state) {
		return this != ModBlocks.dragon_ice;
	}

	@SuppressWarnings("deprecation")
	public boolean isFullCube(IBlockState state) {
		return this != ModBlocks.dragon_ice;
	}

	@SuppressWarnings("deprecation")
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
		IBlockState iblockstate = blockAccess.getBlockState(pos.offset(side));
		Block block = iblockstate.getBlock();
		if (this == ModBlocks.dragon_ice) {
			if (blockState != iblockstate) {
				return true;
			}
			if (block == this) {
				return false;
			}
		} else {
			return super.shouldSideBeRendered(blockState, blockAccess, pos, side);
		}
		return block != this ? false : super.shouldSideBeRendered(blockState, blockAccess, pos, side);
	}
}
