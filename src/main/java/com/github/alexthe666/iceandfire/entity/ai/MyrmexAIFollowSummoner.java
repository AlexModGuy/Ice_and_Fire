package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityMyrmexSwarmer;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.EnumSet;

public class MyrmexAIFollowSummoner extends Goal {
    private final EntityMyrmexSwarmer tameable;
    World world;
    float maxDist;
    float minDist;
    private LivingEntity owner;
    private int timeToRecalcPath;
    private float oldWaterCost;

    public MyrmexAIFollowSummoner(EntityMyrmexSwarmer tameableIn, double followSpeedIn, float minDistIn, float maxDistIn) {
        this.tameable = tameableIn;
        this.world = tameableIn.level;
        this.minDist = minDistIn;
        this.maxDist = maxDistIn;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        LivingEntity LivingEntity = this.tameable.getSummoner();
        if (tameable.getTarget() != null) {
            return false;
        }
        if (LivingEntity == null) {
            return false;
        } else if (LivingEntity instanceof PlayerEntity && LivingEntity.isSpectator()) {
            return false;
        } else if (this.tameable.distanceToSqr(LivingEntity) < this.minDist * this.minDist) {
            return false;
        } else {
            this.owner = LivingEntity;
            return true;
        }
    }

    @Override
    public boolean canContinueToUse() {
        return this.tameable.getTarget() == null
            && this.tameable.distanceToSqr(this.owner) > this.maxDist * this.maxDist;
    }

    @Override
    public void start() {
        this.timeToRecalcPath = 0;
        this.oldWaterCost = this.tameable.getPathfindingMalus(PathNodeType.WATER);
        this.tameable.setPathfindingMalus(PathNodeType.WATER, 0.0F);
    }

    @Override
    public void stop() {
        this.owner = null;
        this.tameable.setPathfindingMalus(PathNodeType.WATER, this.oldWaterCost);
    }

    private boolean isEmptyBlock(BlockPos pos) {
        BlockState BlockState = this.world.getBlockState(pos);
        return BlockState.getMaterial() == Material.AIR || !BlockState.canOcclude();
    }

    @Override
    public void tick() {
        if (this.tameable.getTarget() != null) {
            return;
        }
        this.tameable.getLookControl().setLookAt(this.owner, 10.0F,
            this.tameable.getMaxHeadXRot());
        if (--this.timeToRecalcPath <= 0) {
            this.timeToRecalcPath = 10;
            this.tameable.getMoveControl().setWantedPosition(this.owner.getX(), this.owner.getY() + this.owner.getEyeHeight(), this.owner.getZ(), 0.25D);
            if (!this.tameable.isLeashed()) {
                if (this.tameable.distanceToSqr(this.owner) >= 50.0D) {
                    final int i = MathHelper.floor(this.owner.getX()) - 2;
                    final int j = MathHelper.floor(this.owner.getZ()) - 2;
                    final int k = MathHelper.floor(this.owner.getBoundingBox().minY);

                    for (int l = 0; l <= 4; ++l) {
                        for (int i1 = 0; i1 <= 4; ++i1) {
                            if ((l < 1 || i1 < 1 || l > 3 || i1 > 3) && this.isEmptyBlock(new BlockPos(i + l, k, j + i1)) && this.isEmptyBlock(new BlockPos(i + l, k + 1, j + i1))) {
                                this.tameable.moveTo(i + l + 0.5F, k + 1.5, j + i1 + 0.5F,
                                    this.tameable.yRot, this.tameable.xRot);
                                return;
                            }
                        }
                    }
                }
            }
        }
    }
}