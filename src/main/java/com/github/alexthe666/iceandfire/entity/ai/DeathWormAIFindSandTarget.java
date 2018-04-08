package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityDeathWorm;
import com.github.alexthe666.iceandfire.entity.EntitySiren;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DeathWormAIFindSandTarget extends EntityAIBase {
    private EntityDeathWorm mob;
    private int range;
    private boolean avoidAttacker;

    public DeathWormAIFindSandTarget(EntityDeathWorm mob, int range) {
        this.mob = mob;
        this.range = range;
        this.setMutexBits(1);
    }

    @Override
    public boolean shouldExecute() {
        if (!this.mob.isInSand() || this.mob.isRiding() || this.mob.isBeingRidden()) {
            return false;
        }
        if (this.mob.getRNG().nextFloat() < 0.5F) {
            Path path = this.mob.getNavigator().getPath();
            if (path != null || !this.mob.getNavigator().noPath() && !isDirectPathBetweenPoints(this.mob, this.mob.getPositionVector(), new Vec3d(path.getFinalPathPoint().x, path.getFinalPathPoint().y, path.getFinalPathPoint().z))) {
                this.mob.getNavigator().clearPath();
            }
            if (this.mob.getNavigator().noPath()) {
                BlockPos vec3 = this.findSandTarget();
                if (vec3 != null) {
                    this.mob.getNavigator().tryMoveToXYZ(vec3.getX(), vec3.getY(), vec3.getZ(), 1.0);
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

    public BlockPos findSandTarget() {
        if (this.mob.getAttackTarget() == null || this.mob.getAttackTarget().isDead) {
            List<BlockPos> sand = new ArrayList<>();
            if(this.mob.isTamed() && this.mob.getWormHome() != null){
                range = 25;
                for (int x = (int) this.mob.getWormHome().getX() - range; x < (int) this.mob.getWormHome().getX() + range; x++) {
                    for (int y = (int)this.mob.getWormHome().getY() - range; y < (int) this.mob.getWormHome().getY() + range; y++) {
                        for (int z = (int) this.mob.getWormHome().getZ() - range; z < (int) this.mob.getWormHome().getZ() + range; z++) {
                            if (this.mob.world.getBlockState(new BlockPos(x, y, z)).getMaterial() == Material.SAND && isDirectPathBetweenPoints(this.mob, this.mob.getPositionVector(), new Vec3d(x, y, z))) {
                                sand.add(new BlockPos(x, y, z));
                            }
                        }
                    }
                }
            }else{
                for (int x = (int) this.mob.posX - range; x < (int) this.mob.posX + range; x++) {
                    for (int y = (int)this.mob.posY - range; y < (int) this.mob.posY + range; y++) {
                        for (int z = (int) this.mob.posZ - range; z < (int) this.mob.posZ + range; z++) {
                            if (this.mob.world.getBlockState(new BlockPos(x, y, z)).getMaterial() == Material.SAND && isDirectPathBetweenPoints(this.mob, this.mob.getPositionVector(), new Vec3d(x, y, z))) {
                                sand.add(new BlockPos(x, y, z));
                            }

                        }
                    }
                }
            }

            if (!sand.isEmpty()) {
                return sand.get(this.mob.getRNG().nextInt(sand.size()));
            }
        } else {
            BlockPos blockpos1 = new BlockPos(this.mob.getAttackTarget());
            return new BlockPos((double) blockpos1.getX(), (double) blockpos1.getY() - 1, (double) blockpos1.getZ());
        }
        return null;
    }

    public boolean isDirectPathBetweenPoints(Entity entity, Vec3d vec1, Vec3d vec2) {
        return true;
    }
}