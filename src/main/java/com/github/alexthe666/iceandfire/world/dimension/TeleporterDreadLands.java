package com.github.alexthe666.iceandfire.world.dimension;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.ITeleporter;

public class TeleporterDreadLands extends Teleporter implements ITeleporter {

    public TeleporterDreadLands(WorldServer world) {
        super(world);
    }

    @Override
    public boolean placeInExistingPortal(Entity entity, float rotationYaw) {
        entity.setPositionAndRotation(0, 110, 0, 0, 0);
        placeInPortal(entity);
        return false;
    }

    public void placeInPortal(Entity entity) {
        entity.motionX = entity.motionY = entity.motionZ = 0.0D;
        entity.setPositionAndRotation(0, 110, 0, 0, 0);
    }
}
