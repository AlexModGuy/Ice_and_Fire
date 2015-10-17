package com.github.alexthe666.iceandfire.structures;

import net.minecraft.block.Block;

public class BlockMeta {
	
	Block block;
	int meta;
	public BlockMeta(Block block, int meta){
		this.block = block;
		this.meta = meta;
	}
	
	public Block getBlock(){
		return block;
	}
	
	public int getMeta(){
		return meta;
	}
}
