package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.*;
import com.github.alexthe666.iceandfire.core.*;
import net.minecraft.block.*;
import net.minecraft.block.material.*;
import net.minecraft.block.state.*;
import net.minecraft.item.*;
import net.minecraftforge.fml.common.registry.*;

import java.util.*;

public class BlockDragonOre extends Block {

	public BlockDragonOre (int toollevel, float hardness, float resistance, String name, String gameName) {
		super (Material.ROCK);
		this.setCreativeTab (IceAndFire.TAB);
		this.setHarvestLevel ("pickaxe", toollevel);
		this.setResistance (resistance);
		this.setHardness (hardness);
		this.setUnlocalizedName (name);
		GameRegistry.registerBlock (this, gameName);

	}

	@Override
	public Item getItemDropped (IBlockState state, Random rand, int fortune) {
		return this == ModBlocks.sapphireOre ? ModItems.sapphireGem : Item.getItemFromBlock (ModBlocks.silverOre);
	}
}
