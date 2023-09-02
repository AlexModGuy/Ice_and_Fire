package com.github.alexthe666.iceandfire.block;

import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;

public class BlockDreadWoodLog extends RotatedPillarBlock implements IDragonProof, IDreadBlock {

    public BlockDreadWoodLog() {
        super(
    		Properties
				.of()
				.mapColor(MapColor.WOOD)
				.instrument(NoteBlockInstrument.BASS)
				.ignitedByLava()
				.strength(2F, 10000F)
    			.sound(SoundType.WOOD)
		);
    }
}
