package com.github.alexthe666.iceandfire.pathfinding;

import com.github.alexthe666.citadel.server.entity.collision.CustomCollisionsNavigator;
import com.github.alexthe666.iceandfire.entity.EntityCyclops;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;

public class PathNavigateCyclops extends CustomCollisionsNavigator {

    public PathNavigateCyclops(EntityCyclops LivingEntityIn, Level worldIn) {
        super(LivingEntityIn, worldIn);
    }

    @Override
    protected PathFinder createPathFinder(int i) {
        this.nodeEvaluator = new WalkNodeEvaluator();
        this.nodeEvaluator.setCanPassDoors(true);
        this.nodeEvaluator.setCanFloat(true);
        return new PathFinder(this.nodeEvaluator, i);
    }

}