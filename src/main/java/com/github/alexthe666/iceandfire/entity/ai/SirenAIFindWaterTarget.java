package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntitySiren;
import net.minecraft.block.material.Material;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;

public class SirenAIFindWaterTarget extends EntityAIBase {
    private EntitySiren mob;

    public SirenAIFindWaterTarget(EntitySiren mob) {
        this.mob = mob;
        this.setMutexBits(1);
    }

    @Override
    public boolean shouldExecute() {
        if (!this.mob.isInWater()) {
            return false;
        }
        if (this.mob.getRNG().nextFloat() < 0.5F) {
            Path path = this.mob.getNavigator().getPath();
            if (path != null && path.getTarget() != null && this.mob.world.getBlockState(new BlockPos(path.getTarget().x, path.getTarget().y, path.getTarget().z)).getMaterial() != Material.WATER || !this.mob.getNavigator().noPath() && !this.mob.isDirectPathBetweenPoints(this.mob.getPositionVector(), new Vec3d(path.getFinalPathPoint().x, path.getFinalPathPoint().y, path.getFinalPathPoint().z))) {
                this.mob.getNavigator().clearPath();
            }
            if (this.mob.getNavigator().noPath()) {
                Vec3d vec3 = this.findWaterTarget();
                if (vec3 != null) {
                    this.mob.getNavigator().tryMoveToXYZ(vec3.x, vec3.y, vec3.z, 1.0);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean shouldContinueExecuting() {
        return false;
    }

    public Vec3d findWaterTarget() {
        if (this.mob.getAttackTarget() == null) {
            List<Vec3d> water = new ArrayList<>();
            for (int x = (int) this.mob.posX - 5; x < (int) this.mob.posX + 5; x++) {
                for (int y = (int)this.mob.posY - 5; y < (int) this.mob.posY + 5; y++) {
                    for (int z = (int) this.mob.posZ - 5; z < (int) this.mob.posZ + 5; z++) {
                        if (this.mob.world.getBlockState(new BlockPos(x, y, z)).getMaterial() == Material.WATER && this.mob.isDirectPathBetweenPoints(this.mob.getPositionVector(), new Vec3d(x, y, z))) {
                            water.add(new Vec3d(x, y, z));
                        }
                    }
                }
            }
            if (!water.isEmpty()) {
                return water.get(this.mob.getRNG().nextInt(water.size()));
            }
        } else {
            BlockPos blockpos1 = new BlockPos(this.mob.getAttackTarget());
            if (this.mob.world.getBlockState(blockpos1).getMaterial() == Material.WATER) {
                return new Vec3d((double) blockpos1.getX(), (double) blockpos1.getY(), (double) blockpos1.getZ());
            }
        }
        return null;
    }
}