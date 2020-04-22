package com.github.alexthe666.iceandfire.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;

public class BlockDreadStoneBrickSlab {
    public static class Double extends BlockGenericSlab implements IDreadBlock {
        public Double(String name, float hardness, float resistance, SoundType soundType) {
            super(name, hardness, resistance, soundType, Material.WOOD, IafBlockRegistry.dread_stone_bricks);

        }

        @Override
        public boolean isDouble() {
            return true;
        }

        @Override
        public Item getSlabItem() {
            return Item.getItemFromBlock(IafBlockRegistry.dread_stone_bricks_slab);
        }

        @Override
        public ItemBlock getItemBlock() {
            return new GenericSlabBlockItem(this, IafBlockRegistry.dread_stone_bricks_slab, IafBlockRegistry.dread_stone_bricks_double_slab);
        }
    }

    public static class Half extends BlockGenericSlab implements IDreadBlock {
        public Half(String name, float hardness, float resistance, SoundType soundType) {
            super(name, hardness, resistance, soundType, Material.WOOD, IafBlockRegistry.dread_stone_bricks);
        }

        @Override
        public boolean isDouble() {
            return false;
        }

        @Override
        public Item getSlabItem() {
            return Item.getItemFromBlock(IafBlockRegistry.dread_stone_bricks_slab);
        }

        @Override
        public ItemBlock getItemBlock() {
            return new GenericSlabBlockItem(this, IafBlockRegistry.dread_stone_bricks_slab, IafBlockRegistry.dread_stone_bricks_double_slab);
        }
    }
}
