package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import net.minecraft.pathfinding.PathFinder;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.pathfinding.WalkNodeProcessor;
import net.minecraft.world.World;

public class PathNavigateExperimentalGround extends PathNavigateGround {

    public PathNavigateExperimentalGround(EntityDragonBase entityDragonBase, World worldIn) {
        super(entityDragonBase, worldIn);
    }

    protected PathFinder getPathFinder(){
        this.nodeProcessor = new ExperimentalWalkNodeProcessor();
        this.nodeProcessor.setCanEnterDoors(true);
        return new PathFinder(this.nodeProcessor);
    }
}
