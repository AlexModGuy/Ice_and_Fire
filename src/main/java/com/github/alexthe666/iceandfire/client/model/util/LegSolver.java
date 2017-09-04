package com.github.alexthe666.iceandfire.client.model.util;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
/*
       Code from JurassiCraft, used with permission
       By paul101
 */
public class LegSolver {
    public final Leg[] legs;

    public LegSolver(Leg... legs) {
        this.legs = legs;
    }

    public final void update(EntityLivingBase entity) {
        double sideTheta = entity.renderYawOffset / (180 / Math.PI);
        double sideX = Math.cos(sideTheta);
        double sideZ = Math.sin(sideTheta);
        double forwardTheta = sideTheta + Math.PI / 2;
        double forwardX = Math.cos(forwardTheta);
        double forwardZ = Math.sin(forwardTheta);
        for (Leg leg : this.legs) {
            leg.update(entity, sideX, sideZ, forwardX, forwardZ);
        }
    }

    public static final class Leg {
        public final float forward;

        public final float side;

        private float height;

        private float prevHeight;

        public Leg(float forward, float side) {
            this.forward = forward;
            this.side = side;
        }

        public float getHeight(float delta) {
            return this.prevHeight + (this.height - this.prevHeight) * delta;
        }

        public void update(EntityLivingBase entity, double sideX, double sideZ, double forwardX, double forwardZ) {
            this.prevHeight = this.height;
            this.height = settle(entity, entity.posX + sideX * this.side + forwardX * this.forward, entity.posY, entity.posZ + sideZ * this.side + forwardZ * this.forward, this.height);
        }

        private float settle(EntityLivingBase entity, double x, double y, double z, float height) {
            BlockPos pos = new BlockPos(x, y + 1e-3, z);
            float dist = getDistance(entity.world, pos);
            if (1 - dist < 1e-3) {
                dist = getDistance(entity.world, pos.down()) + (float) y % 1;
            } else {
                dist -= 1 - (y % 1);
            }
            if (entity.onGround && height <= dist) {
                return height == dist ? height : Math.min(height + 0.3F, dist);
            } else if (height > 0) {
                return Math.max(height - 0.4F, dist);
            }
            return height;
        }

        private float getDistance(World world, BlockPos pos) {
            IBlockState state = world.getBlockState(pos);
            AxisAlignedBB aabb = state.getCollisionBoundingBox(world, pos);
            return aabb == null ? 1 : 1 - Math.min((float) aabb.maxY, 1);
        }
    }
}
