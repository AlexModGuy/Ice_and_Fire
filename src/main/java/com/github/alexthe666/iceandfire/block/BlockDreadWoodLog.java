package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;

public class BlockDreadWoodLog extends RotatedPillarBlock implements IDragonProof, IDreadBlock {

    public BlockDreadWoodLog() {
        super(
    		Properties
				.of(Material.WOOD)
				.strength(2F, 10000F)
    			.sound(SoundType.WOOD)
		);
    }
}
