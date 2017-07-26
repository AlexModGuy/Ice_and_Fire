package com.github.alexthe666.iceandfire.core;

import com.github.alexthe666.iceandfire.block.*;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityDummyGorgonHead;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityDummyGorgonHeadActive;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModBlocks {

    public static Block lectern;
    public static Block podium;
    public static Block fire_lily;
    public static Block frost_lily;
    public static Block goldPile;
    public static Block silverPile;
    public static Block silverOre;
    public static Block sapphireOre;
    public static Block silverBlock;
    public static Block sapphireBlock;
    public static Block charedDirt;
    public static Block charedGrass;
    public static Block charedStone;
    public static Block charedCobblestone;
    public static Block charedGravel;
    public static Block charedGrassPath;
    public static Block ash;
    public static Block frozenDirt;
    public static Block frozenGrass;
    public static Block frozenStone;
    public static Block frozenCobblestone;
    public static Block frozenGravel;
    public static Block frozenGrassPath;
    public static Block dragon_ice;
    public static Block dragon_ice_spikes;
    public static Block eggInIce;

    public static void init() {
        lectern = new BlockLectern();
        podium = new BlockPodium();
        fire_lily = new BlockElementalFlower(true);
        frost_lily = new BlockElementalFlower(false);
        goldPile = new BlockGoldPile();
        silverPile = new BlockSilverPile();
        silverOre = new BlockDragonOre(2, 3.0F, 5.0F, "iceandfire.silverOre", "silver_ore");
        sapphireOre = new BlockDragonOre(2, 3.0F, 5.0F, "iceandfire.sapphireOre", "sapphire_ore");
        silverBlock = new BlockGeneric(Material.IRON, "silver_block", "iceandfire.silverBlock", "pickaxe", 2, 3.0F, 10.0F, SoundType.METAL);
        sapphireBlock = new BlockGeneric(Material.IRON, "sapphire_block", "iceandfire.sapphireBlock", "pickaxe", 2, 3.0F, 10.0F, SoundType.METAL);
        charedDirt = new BlockGeneric(Material.GROUND, "chared_dirt", "iceandfire.charedDirt", "shovel", 0, 0.5F, 0.0F, SoundType.GROUND);
        charedGrass = new BlockGeneric(Material.GRASS, "chared_grass", "iceandfire.charedGrass", "shovel", 0, 0.6F, 0.0F, SoundType.GROUND);
        charedStone = new BlockGeneric(Material.ROCK, "chared_stone", "iceandfire.charedStone", "pickaxe", 0, 1.5F, 10.0F, SoundType.STONE);
        charedCobblestone = new BlockGeneric(Material.ROCK, "chared_cobblestone", "iceandfire.charedCobblestone", "pickaxe", 0, 2F, 10.0F, SoundType.STONE);
        charedGravel = new BlockFallingGeneric(Material.GROUND, "chared_gravel", "iceandfire.charedGravel", "pickaxe", 0, 0.6F, 0F, SoundType.GROUND);
        charedGrassPath = new BlockCharedPath(true);
        ash = new BlockFallingGeneric(Material.SAND, "ash", "iceandfire.ash", "shovel", 0, 0.5F, 0F, SoundType.SAND);
        frozenDirt = new BlockGeneric(Material.GROUND, "frozen_dirt", "iceandfire.frozenDirt", "shovel", 0, 0.5F, 0.0F, SoundType.GLASS, true);
        frozenGrass = new BlockGeneric(Material.GRASS, "frozen_grass", "iceandfire.frozenGrass", "shovel", 0, 0.6F, 0.0F, SoundType.GLASS, true);
        frozenStone = new BlockGeneric(Material.ROCK, "frozen_stone", "iceandfire.frozenStone", "pickaxe", 0, 1.5F, 10.0F, SoundType.GLASS, true);
        frozenCobblestone = new BlockGeneric(Material.ROCK, "frozen_cobblestone", "iceandfire.frozenCobblestone", "pickaxe", 0, 2F, 10.0F, SoundType.GLASS, true);
        frozenGravel = new BlockFallingGeneric(Material.GROUND, "frozen_gravel", "iceandfire.frozenGravel", "pickaxe", 0, 0.6F, 0F, SoundType.GLASS, true);
        frozenGrassPath = new BlockCharedPath(false);
        dragon_ice = new BlockGeneric(Material.PACKED_ICE, "dragon_ice", "iceandfire.dragon_ice", "pickaxe", 0, 0.5F, 0F, SoundType.GLASS, true);
        dragon_ice_spikes = new BlockIceSpikes();
        eggInIce = new BlockEggInIce();
        GameRegistry.registerTileEntity(TileEntityDummyGorgonHead.class, "dummyGorgonHeadIdle");
        GameRegistry.registerTileEntity(TileEntityDummyGorgonHeadActive.class, "dummyGorgonHeadActive");
    }

}
