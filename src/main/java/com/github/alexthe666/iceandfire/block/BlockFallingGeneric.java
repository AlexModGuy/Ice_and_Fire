package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.*;
import net.minecraft.block.*;
import net.minecraft.block.material.*;
import net.minecraftforge.fml.common.registry.*;

public class BlockFallingGeneric extends BlockFalling {

	public BlockFallingGeneric (Material materialIn, String gameName, String name, String toolUsed, int toolStrength, float hardness, float resistance, SoundType sound) {
		super (materialIn);
		this.setUnlocalizedName (name);
		this.setHarvestLevel (toolUsed, toolStrength);
		this.setHardness (hardness);
		this.setResistance (resistance);
		this.setSoundType (sound);
		this.setCreativeTab (IceAndFire.TAB);
		GameRegistry.registerBlock (this, gameName);

	}

	public BlockFallingGeneric (Material materialIn, String gameName, String name, float hardness, float resistance, SoundType sound) {
		super (materialIn);
		this.setUnlocalizedName (name);
		this.setHardness (hardness);
		this.setResistance (resistance);
		this.setSoundType (sound);
		this.setCreativeTab (IceAndFire.TAB);
		GameRegistry.registerBlock (this, gameName);

	}

}
