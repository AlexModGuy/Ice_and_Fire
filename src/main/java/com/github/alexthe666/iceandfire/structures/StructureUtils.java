package com.github.alexthe666.iceandfire.structures;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.BlockPos;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.World;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.core.ModBlocks;

public class StructureUtils {

	public static Random rand = new Random();
	

	public static void generateGoldOrChest_DragonDungeon(World world, int x, int y, int z){

		if(rand.nextInt(65) == 0){
			StructureUtils.setBlock(world, x, y, z, Blocks.chest, rand.nextInt(3) + 2, 3);
	        TileEntityChest chest = (TileEntityChest)world.getTileEntity(new BlockPos(x,y,z));
	        if (chest != null)
	        {
	            WeightedRandomChestContent.generateChestContents(rand, IceAndFire.treasure_dragondungeon, chest, 6 + rand.nextInt(6));
	        }
		}else if(rand.nextInt(5) == 0){
			StructureUtils.setBlock(world, x, y, z, Blocks.air, 0, 3);
		}else{
			StructureUtils.setBlock(world, x, y, z, ModBlocks.goldPile, rand.nextInt(7), 3);
		}
	}
	public static Block webOrAir(){
		if(rand.nextInt(65) == 0){
			return Blocks.web;
		}else{
			return Blocks.air;
		}
	}
	public static Block iceOrPackedIce(){
		if(rand.nextInt(3) == 0){
			return Blocks.packed_ice;
		}else{
			return Blocks.ice;
		}
	}
	public static void setBlock(World world, int x, int y, int z, Block block, int meta, int flags){
		BlockPos pos = new BlockPos(x, y, z);
        world.setBlockState(pos, block.getStateFromMeta(meta), flags);
	}
	public static Block dragonLoot(){
		int choice = rand.nextInt(99)+1;
		if(choice < 20){
			return Blocks.diamond_block;
		}else if(choice < 60){
			return Blocks.gold_block;
		}else if(choice < 80){
			return Blocks.emerald_block;
		}else{
			return Blocks.lapis_block;
		}
	}
}

