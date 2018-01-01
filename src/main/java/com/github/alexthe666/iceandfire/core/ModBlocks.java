package com.github.alexthe666.iceandfire.core;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.block.*;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityDummyGorgonHead;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityDummyGorgonHeadActive;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModBlocks {

	public static final SoundType SOUND_TYPE_GOLD = new SoundType(1.0F, 1.0F, ModSounds.GOLD_PILE_BREAK, ModSounds.GOLD_PILE_STEP, ModSounds.GOLD_PILE_BREAK, ModSounds.GOLD_PILE_STEP, ModSounds.GOLD_PILE_STEP);

	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":lectern")
	public static Block lectern = new BlockLectern();
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":podium")
	public static Block podium = new BlockPodium();
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":fire_lily")
	public static Block fire_lily = new BlockElementalFlower(true);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":frost_lily")
	public static Block frost_lily = new BlockElementalFlower(false);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":goldpile")
	public static Block goldPile = new BlockGoldPile();
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":silverpile")
	public static Block silverPile = new BlockSilverPile();
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":silver_ore")
	public static Block silverOre = new BlockDragonOre(2, 3.0F, 5.0F, "iceandfire.silverOre", "silver_ore");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":sapphire_ore")
	public static Block sapphireOre = new BlockDragonOre(2, 3.0F, 5.0F, "iceandfire.sapphireOre", "sapphire_ore");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":silver_block")
	public static Block silverBlock = new BlockGeneric(Material.IRON, "silver_block", "iceandfire.silverBlock", "pickaxe", 2, 3.0F, 10.0F, SoundType.METAL);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":sapphire_block")
	public static Block sapphireBlock = new BlockGeneric(Material.IRON, "sapphire_block", "iceandfire.sapphireBlock", "pickaxe", 2, 3.0F, 10.0F, SoundType.METAL);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":chared_dirt")
	public static Block charedDirt = new BlockGeneric(Material.GROUND, "chared_dirt", "iceandfire.charedDirt", "shovel", 0, 0.5F, 0.0F, SoundType.GROUND);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":chared_grass")
	public static Block charedGrass = new BlockGeneric(Material.GRASS, "chared_grass", "iceandfire.charedGrass", "shovel", 0, 0.6F, 0.0F, SoundType.GROUND);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":chared_stone")
	public static Block charedStone = new BlockGeneric(Material.ROCK, "chared_stone", "iceandfire.charedStone", "pickaxe", 0, 1.5F, 10.0F, SoundType.STONE);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":chared_cobblestone")
	public static Block charedCobblestone = new BlockGeneric(Material.ROCK, "chared_cobblestone", "iceandfire.charedCobblestone", "pickaxe", 0, 2F, 10.0F, SoundType.STONE);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":chared_gravel")
	public static Block charedGravel = new BlockFallingGeneric(Material.GROUND, "chared_gravel", "iceandfire.charedGravel", "pickaxe", 0, 0.6F, 0F, SoundType.GROUND);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":chared_grass_path")
	public static Block charedGrassPath = new BlockCharedPath(true);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":ash")
	public static Block ash = new BlockFallingGeneric(Material.SAND, "ash", "iceandfire.ash", "shovel", 0, 0.5F, 0F, SoundType.SAND);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":frozen_dirt")
	public static Block frozenDirt = new BlockGeneric(Material.GROUND, "frozen_dirt", "iceandfire.frozenDirt", "shovel", 0, 0.5F, 0.0F, SoundType.GLASS, true);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":frozen_grass")
	public static Block frozenGrass = new BlockGeneric(Material.GRASS, "frozen_grass", "iceandfire.frozenGrass", "shovel", 0, 0.6F, 0.0F, SoundType.GLASS, true);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":frozen_stone")
	public static Block frozenStone = new BlockGeneric(Material.ROCK, "frozen_stone", "iceandfire.frozenStone", "pickaxe", 0, 1.5F, 10.0F, SoundType.GLASS, true);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":frozen_cobblestone")
	public static Block frozenCobblestone = new BlockGeneric(Material.ROCK, "frozen_cobblestone", "iceandfire.frozenCobblestone", "pickaxe", 0, 2F, 10.0F, SoundType.GLASS, true);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":frozen_gravel")
	public static Block frozenGravel = new BlockFallingGeneric(Material.GROUND, "frozen_gravel", "iceandfire.frozenGravel", "pickaxe", 0, 0.6F, 0F, SoundType.GLASS, true);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":frozen_grass_path")
	public static Block frozenGrassPath = new BlockCharedPath(false);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":frozen_splinters")
	public static Block frozenSplinters = new BlockGeneric(Material.WOOD, "frozen_splinters", "iceandfire.frozenSplinters", "pickaxe", 0, 2.0F, 10.0F, SoundType.GLASS, true);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":dragon_ice")
	public static Block dragon_ice = new BlockGeneric(Material.PACKED_ICE, "dragon_ice", "iceandfire.dragon_ice", "pickaxe", 0, 0.5F, 0F, SoundType.GLASS, true);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":dragon_ice_spikes")
	public static Block dragon_ice_spikes = new BlockIceSpikes();
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":nest")
	public static Block nest = new BlockGeneric(Material.GRASS, "nest", "iceandfire.nest", "axe", 0, 0.5F, 0F, SoundType.GROUND, false);

	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":egginice")
	public static Block eggInIce = new BlockEggInIce();
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":pixie_house")
	public static Block pixieHouse = new BlockPixieHouse();
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":jar")
	public static Block jar = new BlockJar();

	static {
		GameRegistry.registerTileEntity(TileEntityDummyGorgonHead.class, "dummyGorgonHeadIdle");
		GameRegistry.registerTileEntity(TileEntityDummyGorgonHeadActive.class, "dummyGorgonHeadActive");
	}

}
