package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.IceAndFire;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockGeneric extends Block{

	public BlockGeneric(Material materialIn, String gameName, String name, String toolUsed, int toolStrength, float hardness, float resistance, SoundType sound) {
		super(materialIn);
		this.setUnlocalizedName(name);
		this.setHarvestLevel(toolUsed, toolStrength);
		this.setHardness(hardness);
		this.setResistance(resistance);
		this.setStepSound(sound);
		this.setCreativeTab(IceAndFire.tab);
		GameRegistry.registerBlock(this, gameName);
		
	}

	public BlockGeneric(Material materialIn, String gameName, String name, float hardness, float resistance, SoundType sound) {
		super(materialIn);
		this.setUnlocalizedName(name);
		this.setHardness(hardness);
		this.setResistance(resistance);
		this.setStepSound(sound);
		this.setCreativeTab(IceAndFire.tab);
		GameRegistry.registerBlock(this, gameName);
		
	}
}
