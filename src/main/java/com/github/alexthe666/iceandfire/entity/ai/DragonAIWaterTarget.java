package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityIceDragon;
import net.minecraft.block.material.Material;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;

public class DragonAIWaterTarget extends EntityAIBase {
    private EntityIceDragon dragon;

    public DragonAIWaterTarget(EntityIceDragon dragon) {
        this.dragon = dragon;
        this.setMutexBits(1);
    }

    @Override
    public boolean shouldExecute() {
        if (!this.dragon.isInWater()) {
            return false;
        }
        if (this.dragon.getRNG().nextFloat() < 0.5F) {
            Path path = this.dragon.getNavigator().getPath();
            if (!this.dragon.getNavigator().noPath() && !this.dragon.isDirectPathBetweenPoints(this.dragon.getPositionVector(), new Vec3d(path.getFinalPathPoint().xCoord, path.getFinalPathPoint().yCoord, path.getFinalPathPoint().zCoord))) {
                this.dragon.getNavigator().clearPathEntity();
            }
            if (this.dragon.getNavigator().noPath()) {
                Vec3d vec3 = this.findWaterTarget();
                if (vec3 != null) {
                    this.dragon.getNavigator().tryMoveToXYZ(vec3.xCoord, vec3.yCoord, vec3.zCoord, 1.0);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean continueExecuting() {
        return false;
    }

    public Vec3d findWaterTarget() {
        if (this.dragon.getAttackTarget() == null) {
            List<Vec3d> water = new ArrayList<Vec3d>();
            for (int x = (int) this.dragon.posX - 5; x < (int) this.dragon.posX + 5; x++) {
                for (int y = (int) this.dragon.posY - 5; y < (int) this.dragon.posY + 5; y++) {
                    for (int z = (int) this.dragon.posZ - 5; z < (int) this.dragon.posZ + 5; z++) {
                        if (this.dragon.isDirectPathBetweenPoints(this.dragon.getPositionVector(), new Vec3d(x, y, z))) {
                            water.add(new Vec3d(x, y, z));
                        }
                    }
                }
            }
            if (!water.isEmpty()) {
                return water.get(this.dragon.getRNG().nextInt(water.size()));
            }
        } else {
            BlockPos blockpos1 = new BlockPos(this.dragon.getAttackTarget());
            if (this.dragon.worldObj.getBlockState(blockpos1).getMaterial() == Material.WATER) {
                return new Vec3d((double) blockpos1.getX(), (double) blockpos1.getY(), (double) blockpos1.getZ());
            }
        }
        return null;
    }
}