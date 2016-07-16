package com.github.alexthe666.iceandfire.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.core.ModBlocks;
import com.github.alexthe666.iceandfire.core.ModItems;

public class BlockDragonOre extends Block {

	public BlockDragonOre(int toollevel, float hardness, float resistance, String name, String gameName) {
		super(Material.ROCK);
		this.setCreativeTab(IceAndFire.TAB);
		this.setHarvestLevel("pickaxe", toollevel);
		this.setResistance(resistance);
		this.setHardness(hardness);
		this.setUnlocalizedName(name);
		GameRegistry.registerBlock(this, gameName);

	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return this == ModBlocks.sapphireOre ? ModItems.sapphireGem : Item.getItemFromBlock(ModBlocks.silverOre);
	}
}
