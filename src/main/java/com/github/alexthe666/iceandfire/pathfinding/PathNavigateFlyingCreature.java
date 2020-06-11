package com.github.alexthe666.iceandfire.pathfinding;

import net.minecraft.entity.MobEntity;
import net.minecraft.pathfinding.FlyingPathNavigator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PathNavigateFlyingCreature extends FlyingPathNavigator {

    public PathNavigateFlyingCreature(MobEntity entity, World world) {
        super(entity, world);
    }

    public boolean canEntityStandOnPos(BlockPos pos) {
        return this.world.isAirBlock(pos.down());
    }
}
