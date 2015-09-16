package com.github.alexthe666.iceandfire.block;

import java.util.Random;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.core.ModBlocks;
import com.github.alexthe666.iceandfire.core.ModItems;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockDragonOre extends Block {

	public BlockDragonOre(int toollevel, float hardness, float resistance, String name, String gameName)
	{
		super(Material.rock);
		this.setCreativeTab(IceAndFire.tab);
		this.setHarvestLevel("pickaxe", toollevel);
		this.setResistance(resistance);
		this.setHardness(hardness);
		this.setUnlocalizedName(name);
		GameRegistry.registerBlock(this, gameName);

	}
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return this == ModBlocks.sapphireOre ? ModItems.sapphireGem : Item.getItemFromBlock(ModBlocks.silverOre);
	}
	@Override
	public int getExpDrop(IBlockAccess world, BlockPos pos, int fortune)
	{
		if (this == Blocks.emerald_ore)
		{
			return MathHelper.getRandomIntegerInRange(new Random(), 3, 7);
		}else{
			return 0;
		}
	}
}
