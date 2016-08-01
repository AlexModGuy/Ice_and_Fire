package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityIceDragon;
import net.minecraft.block.material.Material;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DragonAIWaterTarget extends EntityAIBase {

    public EntityIceDragon dragon;
    public DragonAIWaterTarget(EntityIceDragon dragon) {
        this.dragon = dragon;
    }

    @Override
    public boolean shouldExecute() {
        if(!dragon.isInWater()){
            return false;
        }
        if (dragon.airTarget != null && !dragon.isDirectPathBetweenPoints(dragon.getPositionVector(), new Vec3d(dragon.airTarget))) {
            dragon.airTarget = null;
        }
        if (dragon.airTarget != null && dragon.getDistance(dragon.airTarget.getX(), dragon.airTarget.getY(), dragon.airTarget.getZ()) < 10F) {
            return false;
        } else {
            Vec3d vec3 = this.findWaterTarget();
            if (vec3 == null) {
                return false;
            } else {
                dragon.airTarget = new BlockPos(vec3);
                System.out.println( dragon.airTarget );
                return true;
            }
        }
    }

    public boolean continueExecuting() {
        return dragon.airTarget != null;
    }

    public Vec3d findWaterTarget() {
        if (dragon.getAttackTarget() == null) {
            List<Vec3d> water = new ArrayList<Vec3d>();
            for(int x = (int)dragon.posX - 5; x < (int)dragon.posX + 5; x++){
                for(int y = (int)dragon.posY - 5; y < (int)dragon.posY + 5; y++){
                    for(int z = (int)dragon.posZ - 5; z < (int)dragon.posZ + 5; z++){
                        Vec3d vec = new Vec3d(x, y, z);
                        if(dragon.worldObj.getBlockState(new BlockPos(vec)).getMaterial() == Material.WATER && dragon.isDirectPathBetweenPoints(dragon.getPositionVector(), vec)) {
                            water.add(vec);
                        }
                    }
                }
            }
            if(!water.isEmpty()){
                return water.get(dragon.getRNG().nextInt(water.size()));
            }
        } else {
            Random random = this.dragon.getRNG();
            BlockPos blockpos1 = new BlockPos(dragon.getAttackTarget());
            if (dragon.worldObj.getBlockState(blockpos1).getMaterial() == Material.WATER) {
                return new Vec3d((double) blockpos1.getX(), (double) blockpos1.getY(), (double) blockpos1.getZ());
            }
        }
        return null;
    }

}
