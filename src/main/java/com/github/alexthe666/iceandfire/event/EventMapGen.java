package com.github.alexthe666.iceandfire.event;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.world.MapGenDragonDens;

import net.minecraftforge.event.terraingen.ChunkGeneratorEvent.ReplaceBiomeBlocks;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventMapGen {

	@SubscribeEvent
	public void onBiomeReplaceBlocks(ReplaceBiomeBlocks event) {
		if (IceAndFire.CONFIG.generateDragonDens) {
		//	dragon_dens.generate(event.getWorld(), event.getX(), event.getZ(), event.getPrimer());
		}
	}
}
