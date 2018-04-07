package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.core.ModBlocks;
import com.github.alexthe666.iceandfire.core.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.Random;

public class BlockDragonOre extends Block {
	public Item itemBlock;

	public BlockDragonOre(int toollevel, float hardness, float resistance, String name, String gameName) {
		super(Material.ROCK);
		this.setCreativeTab(IceAndFire.TAB);
		this.setHarvestLevel("pickaxe", toollevel);
		this.setResistance(resistance);
		this.setHardness(hardness);
		this.setUnlocalizedName(name);
		setRegistryName(IceAndFire.MODID, gameName);

	}

	@Override
	public int getExpDrop(IBlockState state, net.minecraft.world.IBlockAccess world, BlockPos pos, int fortune) {
		Random rand = world instanceof World ? ((World)world).rand : new Random();
		if (this.getItemDropped(state, rand, fortune) != Item.getItemFromBlock(this)) {
			if (this == ModBlocks.sapphireOre) {
				return MathHelper.getInt(rand, 3, 7);
			}
		}
		return 0;
	}
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return this == ModBlocks.sapphireOre ? ModItems.sapphireGem : Item.getItemFromBlock(ModBlocks.silverOre);
	}
}
