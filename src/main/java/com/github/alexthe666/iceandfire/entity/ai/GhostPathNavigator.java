package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityGhost;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.level.Level;

public class GhostPathNavigator extends FlyingPathNavigation {

    public EntityGhost ghost;

    public GhostPathNavigator(EntityGhost entityIn, Level worldIn) {
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
