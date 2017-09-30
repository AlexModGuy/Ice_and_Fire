package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.core.ModBlocks;
import net.minecraft.block.BlockBush;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockElementalFlower extends BlockBush {
	public Item itemBlock;

	public BlockElementalFlower(boolean isFire) {
		this.setTickRandomly(true);
		this.setCreativeTab(IceAndFire.TAB);
		this.setUnlocalizedName(isFire ? "iceandfire.fire_lily" : "iceandfire.frost_lily");
		setRegistryName(IceAndFire.MODID, isFire ? "fire_lily" : "frost_lily");
		GameRegistry.register(itemBlock = (new ItemBlock(this).setRegistryName(this.getRegistryName())));
	}

	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
		IBlockState soil = worldIn.getBlockState(pos.down());
		if (this == ModBlocks.fire_lily) {
			return worldIn.getBlockState(pos).getBlock().isReplaceable(worldIn, pos) && (soil.getMaterial() == Material.SAND || soil.getBlock() == Blocks.NETHERRACK);
		} else {
			return worldIn.getBlockState(pos).getBlock().isReplaceable(worldIn, pos) && (soil.getMaterial() == Material.PACKED_ICE || soil.getMaterial() == Material.ICE);
		}
	}

	protected boolean canSustainBush(IBlockState state) {
		return true;
	}
}
