package com.github.alexthe666.iceandfire.client.model.util;

import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;

/*
       Code from JurassiCraft, used with permission
       By paul101
 */
public class LegSolver {
    public final Leg[] legs;

    public LegSolver(Leg... legs) {
        this.legs = legs;
    }

    public final void update(EntityDragonBase entity, float scale) {
        this.update(entity, entity.yBodyRot, scale);
    }

    public final void update(EntityDragonBase entity, float yaw, float scale) {
        double sideTheta = yaw / (180 / Math.PI);
        double sideX = Mth.cos((float) sideTheta) * scale;
        double sideZ = Mth.sin((float) sideTheta) * scale;
        double forwardTheta = sideTheta + Math.PI / 2;
        double forwardX = Mth.cos((float) forwardTheta) * scale;
        double forwardZ = Mth.sin((float) forwardTheta) * scale;
        for (Leg leg : this.legs) {
            leg.update(entity, sideX, sideZ, forwardX, forwardZ, scale);
        }
    }

    public static final class Leg {

        public final float forward;
        public final float side;
        private final float range;
        private float height;
        private float prevHeight;
        private final boolean isWing;

        public Leg(float forward, float side, float range, boolean isWing) {
            this.forward = forward;
            this.side = side;
            this.range = range;
            this.isWing = isWing;
        }

        public final float getHeight(float delta) {
            return this.prevHeight + (this.height - this.prevHeight) * delta;
        }

        public void update(EntityDragonBase entity, double sideX, double sideZ, double forwardX, double forwardZ, float scale) {
            this.prevHeight = this.height;
            double posY = entity.getY();
            float settledHeight = this.settle(entity, entity.getX() + sideX * this.side + forwardX * this.forward, posY, entity.getZ() + sideZ * this.side + forwardZ * this.forward, this.height);
            this.height = Mth.clamp(settledHeight, -this.range * scale, this.range * scale);
        }


        private float settle(EntityDragonBase entity, double x, double y, double z, float height) {
            BlockPos pos = BlockPos.containing(x, y + 1e-3, z);
            float dist = this.getDistance(entity.level(), pos);
            if (1 - dist < 1e-3) {
                dist = this.getDistance(entity.level(), pos.below()) + (float) y % 1;
            } else {
                dist -= 1 - (y % 1);
            }
            if (entity.onGround() && height <= dist) {
                return height == dist ? height : Math.min(height + this.getFallSpeed(), dist);
            } else if (height > 0) {
                return Math.max(height - this.getRiseSpeed(), dist);
            }
            return height;
        }

        private float getDistance(Level world, BlockPos pos) {
            BlockState state = world.getBlockState(pos);
            VoxelShape aabb = state.getCollisionShape(world, pos);
            return aabb.isEmpty() ? 1 : 1 - Math.min((float) aabb.max(Direction.Axis.Y, 0.5D, 0.5D), 1);
        }

        protected float getFallSpeed() {
            return 0.25F;
        }

        protected float getRiseSpeed() {
            return 0.25F;
        }
    }
}