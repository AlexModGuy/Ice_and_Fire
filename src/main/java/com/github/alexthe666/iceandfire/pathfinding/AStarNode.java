package com.github.alexthe666.iceandfire.pathfinding;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class AStarNode {
    private static final float H = 2F;
    private AStar aStar;

    public AStarNode start;
    private BlockPos pos;
    private BlockPos end;
    private double baseCost;
    protected double calcCost = -1;
    private static float fallDist = 100;

    public AStarNode(AStar aStar, AStarNode start, BlockPos pos, double cost, BlockPos end) {
        this.aStar = aStar;
        this.start = start;
        this.pos = pos;
        this.baseCost = cost;
        this.end = end;
    }

    public double getCost() {
        if (calcCost == -1) {
            calcCost = distanceManhattan(pos, end);
        }
        return baseCost + H * calcCost;
    }

    public void generateReachablePos(IBlockAccess world) {
        for (int i = -1; i <= 1; i++) {
            for (int k = -1; k <= 1; k++) {
                if ((!(i == 0 && k == 0) && i * k == 0)) {
                    BlockPos offset = pos.add(i, 0, k);
                    if (canStandAt(world, offset))
                        travel(offset, baseCost + 1);
                    if (!isBlocked(world, offset.add(-i, 2, -k))){
                        BlockPos up = offset.add(0, 1, 0);
                        if (canStandAt(world, up))
                            travel(up, baseCost + 1.4142);
                    }
                    if (!isBlocked(world, offset.add(0, 1, 0))){
                        BlockPos down = offset.add(0, -1, 0);
                        if (canStandAt(world, down))
                            travel(down, baseCost + 1.4142);
                        else if (!isBlocked(world, down) && !isBlocked(world, down.add(0, 1, 0))){
                            int currentFall = 1;
                            while (currentFall <= aStar.fallDistance && !isBlocked(world, offset.add(0, -currentFall, 0))) {
                                BlockPos locF = offset.add(0, -currentFall, 0);
                                if (canStandAt(world, locF)) {
                                    AStarNode fallNode = new AStarNode(aStar, this, offset, baseCost + 1, end);
                                    fallNode.travel(locF, baseCost + currentFall * 2);
                                }
                                currentFall++;
                            }
                        }
                    }
                }
            }
        }
    }

    public boolean isBlocked(IBlockAccess world, BlockPos pos) {
        return world.getBlockState(pos).isOpaqueCube();
    }

    public boolean canStandAt(IBlockAccess world, BlockPos pos) {
        boolean clear = true;
        for(BlockPos entityPos : BlockPos.getAllInBox(pos.add(-aStar.width/2, 0, -aStar.width/2), pos.add(aStar.width/2, aStar.height, aStar.width/2))){
            if(isBlocked(world, pos)){
                clear = false;
            }
        }
        return clear && isBlocked(world, pos.down());
    }


    private void travel(BlockPos offset, double cost) {
        AStarNode nt = aStar.getNodeFromMap(offset);
        if (nt.start == null && nt != start) {
            nt.baseCost = cost;
            nt.start = this;
            aStar.shoppingList.add(nt);
            return;
        }
        if (nt.baseCost > cost) {
            nt.baseCost = cost;
            nt.start = this;
        }
    }

    public BlockPos getPos() {
        return pos;
    }

    public double distanceManhattan(BlockPos pos1, BlockPos pos2){
        double deltaX = Math.abs(pos1.getX() - pos2.getX());
        double deltaY = Math.abs(pos1.getY() - pos2.getY());
        double deltaZ = Math.abs(pos1.getZ() - pos2.getZ());
        double distanceXZ = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);
        double distanceWY = Math.sqrt(distanceXZ * distanceXZ + deltaY * deltaY);
        return distanceWY;
    }

}
