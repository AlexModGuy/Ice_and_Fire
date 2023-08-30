package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntitySiren;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class SirenAIFindWaterTarget extends Goal {
    private final EntitySiren mob;

    public SirenAIFindWaterTarget(EntitySiren mob) {
        this.mob = mob;
    }

    @Override
    public boolean canUse() {
        if (!this.mob.isInWater()) {
            return false;
        }
        if (this.mob.getRandom().nextFloat() < 0.5F) {
            Path path = this.mob.getNavigation().getPath();
            if (path != null
                && path.getEndNode() != null /*
             * TODO: path is nullable here !this.mob.getNavigator().noPath() &&
             * !this.mob.isDirectPathBetweenPoints(this.mob.getPositionVec(),
             * new Vector3d(path.getFinalPathPoint().x,
             * path.getFinalPathPoint().y, path.getFinalPathPoint().z))
             */) {
                this.mob.getNavigation().stop();
            }
            if (this.mob.getNavigation().isDone()) {
                Vec3 vec3 = this.findWaterTarget();
                if (vec3 != null) {
                    this.mob.getNavigation().moveTo(vec3.x, vec3.y, vec3.z, 1.0);
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

    public Vec3 findWaterTarget() {
        if (this.mob.getTarget() == null || !this.mob.getTarget().isAlive()) {
            List<Vec3> water = new ArrayList<>();
            List<Vec3> singTargets = new ArrayList<>();
            final int posX = (int) this.mob.getX();
            final int posY = (int) this.mob.getY();
            final int posZ = (int) this.mob.getZ();
            for (int x = posX - 5; x < posX + 5; x++) {
                for (int y = posY - 5; y < posY + 5; y++) {
                    for (int z = posZ - 5; z < posZ + 5; z++) {
                        if (mob.wantsToSing()) {
                            if (this.mob.level().getBlockState(new BlockPos(x, y, z)).isSolid() && this.mob.level().isEmptyBlock(new BlockPos(x, y + 1, z)) && this.mob.isDirectPathBetweenPoints(this.mob.position(), new Vec3(x, y + 1, z))) {
                                singTargets.add(new Vec3(x, y + 1, z));
                            }
                        }
                        if (this.mob.level().getBlockState(new BlockPos(x, y, z)).is(Blocks.WATER) && this.mob.isDirectPathBetweenPoints(this.mob.position(), new Vec3(x, y, z))) {
                            water.add(new Vec3(x, y, z));
                        }

                    }
                }
            }
            if (!singTargets.isEmpty()) {
                return singTargets.get(this.mob.getRandom().nextInt(singTargets.size()));

            }
            if (!water.isEmpty()) {
                return water.get(this.mob.getRandom().nextInt(water.size()));
            }
        } else {
            BlockPos blockpos1 = this.mob.getTarget().blockPosition();
            return new Vec3(blockpos1.getX(), blockpos1.getY(), blockpos1.getZ());
        }
        return null;
    }
}