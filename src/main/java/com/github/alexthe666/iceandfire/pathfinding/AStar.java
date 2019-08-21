package com.github.alexthe666.iceandfire.pathfinding;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AStar {
    protected List<AStarNode> confirmedList = new ArrayList<AStarNode>();
    protected List<AStarNode> shoppingList = new ArrayList<AStarNode>();

    private Map<Long, AStarNode> nodeMap = new HashMap<Long, AStarNode>();
    private AStarNode start;
    private AStarNode end;
    private int overflowLimit = 0;
    private boolean pathFound = false;
    public static final boolean debugAStar = false;
    public int fallDistance;
    public int width;
    public int height;
    private boolean returnConfirmed;
    public AStar(double checkWidth, double checkHeight, BlockPos startPos, BlockPos endPos, int overflowLimit, int fallDistance, boolean returnConfirmed) {
        start = new AStarNode(this, null, startPos, 0, endPos);
        end = new AStarNode(this, start, endPos, 0, endPos);
        this.width = MathHelper.ceil(checkWidth);
        this.height = MathHelper.ceil(checkHeight);
        this.overflowLimit = overflowLimit;
        this.fallDistance = fallDistance;
        this.returnConfirmed = returnConfirmed;
    }

    public BlockPos[] getPath(IBlockAccess world) {
        shoppingList.add(start);
        while (confirmedList.size() < overflowLimit && !pathFound && shoppingList.size() > 0) {
            AStarNode n = shoppingList.get(0);
            for (AStarNode nt : shoppingList) {
                if (nt.getCost() < n.getCost()) {
                    n = nt;
                }
            }
            if (n.calcCost < 1) {
                pathFound = true;
                end = n;
                break;
            }
            n.generateReachablePos(world);
            shoppingList.remove(n);
            confirmedList.add(n);
        }
        if (!pathFound && returnConfirmed) {
            int length = confirmedList.size();
            BlockPos[] locations = new BlockPos[length];
            for (int i = 0; i < length; i++) {
                locations[i] = confirmedList.get(i).getPos();
            }
            return locations;
        }
        int length = 1;
        AStarNode n = end;
        while (n.start != null) {
            n = n.start;
            length++;
        }
        BlockPos[] locations = new BlockPos[length];
        n = end;
        for (int i = length - 1; i > 0; i--) {
            locations[i] = n.getPos();
            n = n.start;
        }
        locations[0] = start.getPos();
        return locations;
    }

    public AStarNode getNodeFromMap(BlockPos pos) {
        Long toLong = pos.toLong();
        if (nodeMap.get(toLong) == null) {
            AStarNode node = new AStarNode(this, null, pos, 0, end.getPos());
            nodeMap.put(pos.toLong(), node);
            return node;
        } else {
            return nodeMap.get(toLong);
        }
    }
}
