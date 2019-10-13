package com.github.alexthe666.iceandfire.world.dimension;

import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.ITeleporter;

public class TeleporterDreadLands extends Teleporter implements ITeleporter {
    public TeleporterDreadLands(WorldServer world) {
        super(world);
    }
}
