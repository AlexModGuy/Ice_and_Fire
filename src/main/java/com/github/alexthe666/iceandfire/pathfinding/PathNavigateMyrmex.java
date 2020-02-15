package com.github.alexthe666.iceandfire.pathfinding;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.message.MessageSpawnParticleAt;
import net.minecraft.entity.EntityLiving;
import net.minecraft.pathfinding.*;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class PathNavigateMyrmex extends PathNavigateGround {
    public BlockPos targetPosition;

    public PathNavigateMyrmex(EntityLiving entitylivingIn, World worldIn) {
        super(entitylivingIn, worldIn);
    }

    protected PathFinder getPathFinder() {
        this.nodeProcessor = new WalkNodeProcessor();
        this.nodeProcessor.setCanEnterDoors(true);
        this.nodeProcessor.setCanSwim(true);
        return new PathFinder(this.nodeProcessor);
    }

    public void clearPath() {
        super.clearPath();
    }

    public void onUpdateNavigation() {
        super.onUpdateNavigation();
    }

    protected void pathFollow() {
        debugPathfinder(this.currentPath);
        Vec3d vec3d = this.getEntityPosition();
        int i = this.currentPath.getCurrentPathLength();
        for (int j = this.currentPath.getCurrentPathIndex(); j < this.currentPath.getCurrentPathLength(); ++j)
        {
            if ((double)this.currentPath.getPathPointFromIndex(j).y != Math.floor(vec3d.y))
            {
                i = j;
                break;
            }
        }

        this.maxDistanceToWaypoint = this.entity.width;
        Vec3d vec3d1 = this.currentPath.getCurrentPos();
        float distX = MathHelper.abs((float)(this.entity.posX - (vec3d1.x + 0.5D)));
        float distZ = MathHelper.abs((float)(this.entity.posZ - (vec3d1.z + 0.5D)));
        float distY = (float)Math.abs(this.entity.posY - vec3d1.y);

        if (distX < this.maxDistanceToWaypoint && distZ < this.maxDistanceToWaypoint && distY <= 1.1F)
        {
            this.currentPath.setCurrentPathIndex(this.currentPath.getCurrentPathIndex() + 1);
        }

        int k = MathHelper.ceil(this.entity.width);
        int l = MathHelper.ceil(this.entity.height);
        int i1 = k;

        for (int j1 = i - 1; j1 >= this.currentPath.getCurrentPathIndex(); --j1)
        {
            if (this.isDirectPathBetweenPoints(vec3d, this.currentPath.getVectorFromIndex(this.entity, j1), k, l, i1))
            {
                this.currentPath.setCurrentPathIndex(j1);
                break;
            }
        }

        this.checkForStuck(vec3d);
    }

    public void debugPathfinder(Path currentPath) {
        if(IceAndFire.DEBUG){
            try{
                for(int i = 0; i < currentPath.getCurrentPathLength(); i++){
                    PathPoint point = currentPath.getPathPointFromIndex(i);
                    int particle = EnumParticleTypes.HEART.getParticleID();
                    IceAndFire.NETWORK_WRAPPER.sendToAll(new MessageSpawnParticleAt(point.x, point.y, point.z, particle));
                }
                if(currentPath.getCurrentPos() != null){
                    Vec3d point = currentPath.getCurrentPos();
                    int particle = EnumParticleTypes.CLOUD.getParticleID();
                    IceAndFire.NETWORK_WRAPPER.sendToAll(new MessageSpawnParticleAt(point.x, point.y, point.z, particle));

                }
            }catch (Exception e){
                //Pathfinders are always unfriendly.
            }

        }
    }

}