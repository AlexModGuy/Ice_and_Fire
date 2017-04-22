package com.github.alexthe666.iceandfire.item.block;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockPodium extends ItemBlock {
    final static String[] subBlocks = new String[]{"tile.iceandfire.podium_oak", "tile.iceandfire.podium_spruce", "tile.iceandfire.podium_birch", "tile.iceandfire.podium_jungle", "tile.iceandfire.podium_acacia", "tile.iceandfire.podium_dark_oak"};

    public ItemBlockPodium(Block block) {
        super(block);
        this.setHasSubtypes(true);

    }

    @Override
    public String getUnlocalizedName(ItemStack itemstack) {
        int i = itemstack.getItemDamage();
        if (i < 0 || i >= subBlocks.length) {
            i = 0;
        }
        return subBlocks[i];
    }

    @Override
    public int getMetadata(int meta) {
        return meta;
    }

	/*
     * @Override public ModelResourceLocation getModel(ItemStack stack,
	 * EntityPlayer player, int useRemaining) { switch(stack.getMetadata()){
	 * default: return new ModelResourceLocation("iceandfire:podium_oak",
	 * "inventory"); case 1: return new
	 * ModelResourceLocation("iceandfire:podium_spruce", "inventory"); case 2:
	 * return new ModelResourceLocation("iceandfire:podium_birch", "inventory");
	 * case 3: return new ModelResourceLocation("iceandfire:podium_jungle",
	 * "inventory"); case 4: return new
	 * ModelResourceLocation("iceandfire:podium_acacia", "inventory"); case 5:
	 * return new ModelResourceLocation("iceandfire:podium_dark_oak",
	 * "inventory"); } }
	 */
}
