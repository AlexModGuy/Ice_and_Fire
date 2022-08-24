package com.github.alexthe666.iceandfire.entity.ai;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;

import java.util.Comparator;
import java.util.EnumSet;

public class AquaticAIFindWaterTarget extends Goal {
    protected AquaticAIFindWaterTarget.Sorter fleePosSorter;
    private final MobEntity mob;

    public AquaticAIFindWaterTarget(MobEntity mob, int range, boolean avoidAttacker) {
        this.mob = mob;
        this.setFlags(EnumSet.of(Flag.MOVE));
        fleePosSorter = new Sorter(mob);
    }

    @Override
    public boolean canUse() {
        if (!this.mob.isInWater() || this.mob.isPassenger() || this.mob.isVehicle()) {
            return false;
        }
        Path path = this.mob.getNavigation().getPath();
        if (this.mob.getRandom().nextFloat() < 0.15F || path != null && path.getEndNode() != null && this.mob.distanceToSqr(path.getEndNode().x, path.getEndNode().y, path.getEndNode().z) < 3) {
            if (path != null && path.getEndNode() != null || !this.mob.getNavigation().isDone() && !isDirectPathBetweenPoints(this.mob, this.mob.position(), new Vector3d(path.getEndNode().x, path.getEndNode().y, path.getEndNode().z))) {
                this.mob.getNavigation().stop();
            }
            if (this.mob.getNavigation().isDone()) {
                BlockPos vec3 = this.findWaterTarget();
                if (vec3 != null) {
                    this.mob.getNavigation().moveTo(vec3.getX(), vec3.getY(), vec3.getZ(), 1.0);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean canContinueToUse() {
        return false;
    }

    public BlockPos findWaterTarget() {
        BlockPos blockpos = new BlockPos(this.mob.getX(), this.mob.getBoundingBox().minY, mob.getZ());
        if (this.mob.getTarget() == null || !this.mob.getTarget().isAlive()) {
            for (int i = 0; i < 10; ++i) {
                BlockPos blockpos1 = blockpos.offset(mob.getRandom().nextInt(20) - 10, mob.getRandom().nextInt(6) - 3, mob.getRandom().nextInt(20) - 10);
                if (mob.level.getBlockState(blockpos1).getMaterial() == Material.WATER) {
                    return blockpos1;
                }
            }
        } else {
            return this.mob.getTarget().blockPosition();
        }
        return null;
    }

    public boolean isDirectPathBetweenPoints(Entity entity, Vector3d vec1, Vector3d vec2) {
        return mob.level.clip(new RayTraceContext(vec1, vec2, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, entity)).getType() == RayTraceResult.Type.MISS;

    }

    public class Sorter implements Comparator<BlockPos> {
        private BlockPos pos;

        public Sorter(Entity theEntityIn) {
            this.pos = theEntityIn.blockPosition();
        }

        //further; more prefered.
        @Override
        public int compare(BlockPos p_compare_1_, BlockPos p_compare_2_) {
            this.pos = AquaticAIFindWaterTarget.this.mob.blockPosition();
            final double d0 = this.pos.distSqr(p_compare_1_);
            final double d1 = this.pos.distSqr(p_compare_2_);
            return Double.compare(d1, d0);
        }
    }
}