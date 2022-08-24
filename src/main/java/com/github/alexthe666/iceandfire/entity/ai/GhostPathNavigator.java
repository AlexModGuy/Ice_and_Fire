package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityGhost;
import net.minecraft.entity.Entity;
import net.minecraft.pathfinding.FlyingPathNavigator;
import net.minecraft.world.World;

public class GhostPathNavigator extends FlyingPathNavigator {

    public EntityGhost ghost;

    public GhostPathNavigator(EntityGhost entityIn, World worldIn) {
        super(entityIn, worldIn);
        ghost = entityIn;

    }

    @Override
    public boolean moveTo(Entity entityIn, double speedIn) {
        ghost.getMoveControl().setWantedPosition(entityIn.getX(), entityIn.getY(), entityIn.getZ(), speedIn);
        return true;
    }

    @Override
    public boolean moveTo(double x, double y, double z, double speedIn) {
        ghost.getMoveControl().setWantedPosition(x, y, z, speedIn);
        return true;
    }
}
