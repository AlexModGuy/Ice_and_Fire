package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityDeathWorm;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class DeathWormAIFindSandTarget extends Goal {
    private final EntityDeathWorm mob;
    private int range;

    public DeathWormAIFindSandTarget(EntityDeathWorm mob, int range) {
        this.mob = mob;
        this.range = range;
    }

    @Override
    public boolean canUse() {
        if (this.mob.getTarget() != null) {
            return false;
        }

        if (!this.mob.isInSand() || this.mob.isPassenger() || this.mob.isVehicle()) {
            return false;
        }
        if (this.mob.getRandom().nextFloat() < 0.5F) {
            final Path path = this.mob.getNavigation().getPath();
            if (path != null /*
             * || !this.mob.getNavigator().noPath() && !isDirectPathBetweenPoints(this.mob,
             * this.mob.getPositionVec(), new Vector3d(path.getFinalPathPoint().x,
             * path.getFinalPathPoint().y, path.getFinalPathPoint().z))
             */) {
                this.mob.getNavigation().stop();
            }
            if (this.mob.getNavigation().isDone() /* && !this.mob.getMoveControl().hasWanted()*/) {
                BlockPos vec3 = this.findSandTarget();
                if (vec3 != null) {
                    this.mob.getMoveControl().setWantedPosition(vec3.getX(), vec3.getY(), vec3.getZ(), 1.0);
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

    public BlockPos findSandTarget() {
        if (this.mob.getTarget() == null || !this.mob.getTarget().isAlive()) {
            List<BlockPos> sand = new ArrayList<>();
            if (this.mob.isTame() && this.mob.getWormHome() != null) {
                range = 25;
                for (int x = this.mob.getWormHome().getX() - range; x < this.mob.getWormHome().getX() + range; x++) {
                    for (int y = this.mob.getWormHome().getY() - range; y < this.mob.getWormHome().getY() + range; y++) {
                        for (int z = this.mob.getWormHome().getZ() - range; z < this.mob.getWormHome().getZ() + range; z++) {
                            if (this.mob.level().getBlockState(new BlockPos(x, y, z)).is(BlockTags.SAND) && isDirectPathBetweenPoints(this.mob, this.mob.position(), new Vec3(x, y, z))) {
                                sand.add(new BlockPos(x, y, z));
                            }
                        }
                    }
                }
            } else {
                for (int x = (int) this.mob.getX() - range; x < (int) this.mob.getX() + range; x++) {
                    for (int y = (int) this.mob.getY() - range; y < (int) this.mob.getY() + range; y++) {
                        for (int z = (int) this.mob.getZ() - range; z < (int) this.mob.getZ() + range; z++) {
                            if (this.mob.level().getBlockState(new BlockPos(x, y, z)).is(BlockTags.SAND) && isDirectPathBetweenPoints(this.mob, this.mob.position(), new Vec3(x, y, z))) {
                                sand.add(new BlockPos(x, y, z));
                            }

                        }
                    }
                }
            }

            if (!sand.isEmpty()) {
                return sand.get(this.mob.getRandom().nextInt(sand.size()));
            }
        } else {
            BlockPos blockpos1 = this.mob.getTarget().blockPosition();
            return new BlockPos(blockpos1.getX(), blockpos1.getY() - 1, blockpos1.getZ());
        }
        return null;
    }

    public boolean isDirectPathBetweenPoints(Entity entity, Vec3 vec1, Vec3 vec2) {
        return true;
    }
}