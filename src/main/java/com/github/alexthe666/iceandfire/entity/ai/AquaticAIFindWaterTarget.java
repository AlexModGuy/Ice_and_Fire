package com.github.alexthe666.iceandfire.entity.ai;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

import java.util.Comparator;

public class AquaticAIFindWaterTarget extends EntityAIBase {
    private EntityCreature mob;
    private int range;
    private boolean avoidAttacker;
    protected AquaticAIFindWaterTarget.Sorter fleePosSorter;

    public AquaticAIFindWaterTarget(EntityCreature mob, int range, boolean avoidAttacker) {
        this.mob = mob;
        this.range = range;
        this.avoidAttacker = avoidAttacker;
        this.setMutexBits(1);
        fleePosSorter = new Sorter(mob);
    }

    @Override
    public boolean shouldExecute() {
        if (!this.mob.isInWater() || this.mob.isRiding() || this.mob.isBeingRidden()) {
            return false;
        }
        Path path = this.mob.getNavigator().getPath();
        if (this.mob.getRNG().nextFloat() < 0.5F ||path != null && path.getFinalPathPoint() != null && this.mob.getDistanceSq((double)path.getFinalPathPoint().x, (double)path.getFinalPathPoint().y, (double)path.getFinalPathPoint().z) < 3) {
            if (path != null && path.getFinalPathPoint() != null || !this.mob.getNavigator().noPath() && !isDirectPathBetweenPoints(this.mob, this.mob.getPositionVector(), new Vec3d(path.getFinalPathPoint().x, path.getFinalPathPoint().y, path.getFinalPathPoint().z))) {
                this.mob.getNavigator().clearPath();
            }
            if (this.mob.getNavigator().noPath()) {
                BlockPos vec3 = this.findWaterTarget();
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

    public BlockPos findWaterTarget() {
        BlockPos blockpos = new BlockPos(this.mob.posX, this.mob.getEntityBoundingBox().minY, mob.posZ);
        if (this.mob.getAttackTarget() == null || this.mob.getAttackTarget().isDead) {
            for (int i = 0; i < 10; ++i) {
                BlockPos blockpos1 = blockpos.add(mob.getRNG().nextInt(20) - 10, mob.getRNG().nextInt(6) - 3, mob.getRNG().nextInt(20) - 10);
                if (mob.world.getBlockState(blockpos1).getMaterial() == Material.WATER) {
                    return blockpos1;
                }
            }
        } else {
            return  new BlockPos(this.mob.getAttackTarget());
        }
        return null;
    }

    public boolean isDirectPathBetweenPoints(Entity entity, Vec3d vec1, Vec3d vec2) {
        RayTraceResult movingobjectposition = entity.world.rayTraceBlocks(vec1, new Vec3d(vec2.x, vec2.y + (double) entity.height * 0.5D, vec2.z), false, true, false);
        return movingobjectposition == null || movingobjectposition.typeOfHit != RayTraceResult.Type.BLOCK;
    }

    public class Sorter implements Comparator<BlockPos> {
        private BlockPos pos;

        public Sorter(Entity theEntityIn) {
            this.pos = theEntityIn.getPosition();
        }
        //further; more prefered.
        public int compare(BlockPos p_compare_1_, BlockPos p_compare_2_) {
            this.pos = AquaticAIFindWaterTarget.this.mob.getPosition();
            double d0 = this.pos.distanceSq(p_compare_1_);
            double d1 = this.pos.distanceSq(p_compare_2_);
            return d0 < d1 ? 1 : (d0 > d1 ? -1 : 0);
        }
    }
}