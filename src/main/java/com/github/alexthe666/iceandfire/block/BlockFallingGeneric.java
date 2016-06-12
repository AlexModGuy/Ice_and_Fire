package com.github.alexthe666.iceandfire.block;

import net.minecraft.block.BlockFalling;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.github.alexthe666.iceandfire.IceAndFire;

public class BlockFallingGeneric extends BlockFalling {

	public BlockFallingGeneric(Material materialIn, String gameName, String name, String toolUsed, int toolStrength, float hardness, float resistance, SoundType sound) {
		super(materialIn);
		this.setUnlocalizedName(name);
		this.setHarvestLevel(toolUsed, toolStrength);
		this.setHardness(hardness);
		this.setResistance(resistance);
		this.setStepSound(sound);
		this.setCreativeTab(IceAndFire.tab);
		GameRegistry.registerBlock(this, gameName);

	}

	public BlockFallingGeneric(Material materialIn, String gameName, String name, float hardness, float resistance, SoundType sound) {
		super(materialIn);
		this.setUnlocalizedName(name);
		this.setHardness(hardness);
		this.setResistance(resistance);
		this.setStepSound(sound);
		this.setCreativeTab(IceAndFire.tab);
		GameRegistry.registerBlock(this, gameName);

	}

}
