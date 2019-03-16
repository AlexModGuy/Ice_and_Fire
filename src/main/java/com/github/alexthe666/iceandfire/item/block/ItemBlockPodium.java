package com.github.alexthe666.iceandfire.item.block;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockPodium extends ItemBlock {
	private final static String[] SUB_BLOCKS = new String[]{"tile.iceandfire.podium_oak", "tile.iceandfire.podium_spruce", "tile.iceandfire.podium_birch", "tile.iceandfire.podium_jungle", "tile.iceandfire.podium_acacia", "tile.iceandfire.podium_dark_oak"};

	public ItemBlockPodium(Block block) {
		super(block);
		this.setHasSubtypes(true);

	}

	@Override
	public String getTranslationKey(ItemStack itemstack) {
		int i = itemstack.getItemDamage();
		if (i < 0 || i >= SUB_BLOCKS.length) {
			i = 0;
		}
		return SUB_BLOCKS[i];
	}

	@Override
	public int getMetadata(int meta) {
		return meta;
	}
}
