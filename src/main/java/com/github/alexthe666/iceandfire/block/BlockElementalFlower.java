package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.*;
import com.github.alexthe666.iceandfire.core.*;
import net.minecraft.block.*;
import net.minecraft.block.material.*;
import net.minecraft.block.state.*;
import net.minecraft.init.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraftforge.fml.common.registry.*;

public class BlockElementalFlower extends BlockBush {
	public BlockElementalFlower (boolean isFire) {
		GameRegistry.registerBlock (this, isFire ? "iceandfire:fire_lily" : "iceandfire:frost_lily");
		this.setTickRandomly (true);
		this.setCreativeTab (IceAndFire.TAB);
		this.setUnlocalizedName (isFire ? "iceandfire.fire_lily" : "iceandfire.frost_lily");
	}

	public boolean canPlaceBlockAt (World worldIn, BlockPos pos) {
		IBlockState soil = worldIn.getBlockState (pos.down ());
		if (this == ModBlocks.fire_lily) {
			return worldIn.getBlockState (pos).getBlock ().isReplaceable (worldIn, pos) && (soil.getMaterial () == Material.SAND || soil.getBlock () == Blocks.NETHERRACK);
		} else {
			return worldIn.getBlockState (pos).getBlock ().isReplaceable (worldIn, pos) && (soil.getMaterial () == Material.PACKED_ICE || soil.getMaterial () == Material.ICE);
		}
	}

	protected boolean canSustainBush (IBlockState state) {
		return true;
	}
}
