package com.github.alexthe666.iceandfire.core;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

import com.github.alexthe666.iceandfire.block.BlockDragonOre;
import com.github.alexthe666.iceandfire.block.BlockEggInIce;
import com.github.alexthe666.iceandfire.block.BlockFallingGeneric;
import com.github.alexthe666.iceandfire.block.BlockGeneric;
import com.github.alexthe666.iceandfire.block.BlockGoldPile;
import com.github.alexthe666.iceandfire.block.BlockLectern;
import com.github.alexthe666.iceandfire.block.BlockPodium;

public class ModBlocks {

	public static Block lectern;
	public static Block podium;
	public static Block goldPile;
	public static Block silverOre;
	public static Block sapphireOre;
	public static Block silverBlock;
	public static Block sapphireBlock;
	public static Block charedDirt;
	public static Block charedGrass;
	public static Block charedStone;
	public static Block charedCobblestone;
	public static Block charedGravel;
	public static Block eggInIce;

	public static void init(){
		lectern = new BlockLectern();
		podium = new BlockPodium();
		goldPile = new BlockGoldPile();
		silverOre = new BlockDragonOre(2, 3.0F, 5.0F, "iceandfire.silverOre", "silver_ore");
		sapphireOre = new BlockDragonOre(2, 3.0F, 5.0F, "iceandfire.sapphireOre", "sapphire_ore");
		silverBlock = new BlockGeneric(Material.iron, "silver_block", "iceandfire.silverBlock", "pickaxe", 2, 3.0F, 10.0F, Block.soundTypeMetal);
		sapphireBlock = new BlockGeneric(Material.iron, "sapphire_block", "iceandfire.sapphireBlock", "pickaxe", 2, 3.0F, 10.0F, Block.soundTypeMetal);
		charedDirt = new BlockGeneric(Material.ground, "chared_dirt", "iceandfire.charedDirt", "shovel", 0, 0.5F, 0.0F, Block.soundTypeGravel);
		charedGrass = new BlockGeneric(Material.grass, "chared_grass", "iceandfire.charedGrass", "shovel", 0, 0.6F, 0.0F, Block.soundTypeGrass);
		charedStone = new BlockGeneric(Material.rock, "chared_stone", "iceandfire.charedStone", "pickaxe", 0, 1.5F, 10.0F, Block.soundTypeStone);
		charedCobblestone = new BlockGeneric(Material.rock, "chared_cobblestone", "iceandfire.charedCobblestone", "pickaxe", 0, 2F, 10.0F, Block.soundTypeStone);
		charedGravel = new BlockFallingGeneric(Material.ground, "chared_gravel", "iceandfire.charedGravel", "pickaxe", 0, 0.6F, 0F, Block.soundTypeGravel);
		eggInIce = new BlockEggInIce();
	}	
	
}
