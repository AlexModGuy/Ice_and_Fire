package com.github.alexthe666.iceandfire.entity.ai;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Comparator;
import java.util.EnumSet;

public class AquaticAIFindWaterTarget extends Goal {
    protected AquaticAIFindWaterTarget.Sorter fleePosSorter;
    private final Mob mob;

    public AquaticAIFindWaterTarget(Mob mob, int range, boolean avoidAttacker) {
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
            if (path != null && path.getEndNode() != null || !this.mob.getNavigation().isDone() && !isDirectPathBetweenPoints(this.mob, this.mob.position(), new Vec3(path.getEndNode().x, path.getEndNode().y, path.getEndNode().z))) {
                this.mob.getNavigation().stop();
            }
            if (this.mob.getNavigation().isDone()) {
                BlockPos vec3 = this.findWaterTarget();
                if (vec3 != null) { // TODO :: Performance impact
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
        BlockPos blockpos = BlockPos.containing(this.mob.getBlockX(), this.mob.getBoundingBox().minY, mob.getBlockZ());
        if (this.mob.getTarget() == null || !this.mob.getTarget().isAlive()) {
            for (int i = 0; i < 10; ++i) {
                BlockPos blockpos1 = blockpos.offset(mob.getRandom().nextInt(20) - 10, mob.getRandom().nextInt(6) - 3, mob.getRandom().nextInt(20) - 10);
                if (mob.level().getBlockState(blockpos1).is(Blocks.WATER)) {
                    return blockpos1;
                }
            }
        } else {
            return this.mob.getTarget().blockPosition();
        }
        return null;
    }

    public boolean isDirectPathBetweenPoints(Entity entity, Vec3 vec1, Vec3 vec2) {
        return mob.level().clip(new ClipContext(vec1, vec2, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entity)).getType() == HitResult.Type.MISS;

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