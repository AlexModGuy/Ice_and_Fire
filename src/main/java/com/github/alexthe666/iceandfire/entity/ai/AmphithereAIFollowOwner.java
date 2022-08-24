package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.EnumSet;

public class AmphithereAIFollowOwner extends Goal {
    private final EntityAmphithere ampithere;
    private final double followSpeed;
    World world;
    float maxDist;
    float minDist;
    private LivingEntity owner;
    private int timeToRecalcPath;
    private float oldWaterCost;

    public AmphithereAIFollowOwner(EntityAmphithere ampithereIn, double followSpeedIn, float minDistIn, float maxDistIn) {
        this.ampithere = ampithereIn;
        this.world = ampithereIn.level;
        this.followSpeed = followSpeedIn;
        this.minDist = minDistIn;
        this.maxDist = maxDistIn;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        LivingEntity LivingEntity = this.ampithere.getOwner();
        if (ampithere.getCommand() != 2) {
            return false;
        }
        if (LivingEntity == null) {
            return false;
        } else if (LivingEntity instanceof PlayerEntity && LivingEntity.isSpectator()) {
            return false;
        } else if (this.ampithere.isOrderedToSit()) {
            return false;
        } else if (this.ampithere.distanceToSqr(LivingEntity) < this.minDist * this.minDist) {
            return false;
        } else {
            this.owner = LivingEntity;
            return true;
        }
    }

    @Override
    public boolean canContinueToUse() {
        return !noPath() && this.ampithere.distanceToSqr(this.owner) > this.maxDist * this.maxDist
            && !this.ampithere.isOrderedToSit();
    }

    private boolean noPath() {
        if (!ampithere.isFlying()) {
            return this.ampithere.getNavigation().isDone();
        } else {
            return false;
        }
    }

    @Override
    public void start() {
        this.timeToRecalcPath = 0;
        this.oldWaterCost = this.ampithere.getPathfindingMalus(PathNodeType.WATER);
        this.ampithere.setPathfindingMalus(PathNodeType.WATER, 0.0F);
    }

    @Override
    public void stop() {
        this.owner = null;
        this.ampithere.getNavigation().stop();
        this.ampithere.setPathfindingMalus(PathNodeType.WATER, this.oldWaterCost);
    }

    @Override
    public void tick() {
        this.ampithere.getLookControl().setLookAt(this.owner, 10.0F,
            this.ampithere.getMaxHeadXRot());

        if (!this.ampithere.isOrderedToSit()) {
            if (--this.timeToRecalcPath <= 0) {
                this.timeToRecalcPath = 10;
                tryMoveTo();
                if (!this.ampithere.isLeashed() && !this.ampithere.isPassenger()) {
                    if (this.ampithere.distanceToSqr(this.owner) >= 144.0D) {
                        final int i = MathHelper.floor(this.owner.getX()) - 2;
                        final int j = MathHelper.floor(this.owner.getZ()) - 2;
                        final int k = MathHelper.floor(this.owner.getBoundingBox().minY);

                        for (int l = 0; l <= 4; ++l) {
                            for (int i1 = 0; i1 <= 4; ++i1) {
                                if ((l < 1 || i1 < 1 || l > 3 || i1 > 3) && this.canTeleportToBlock(new BlockPos(i, j, k))) {
                                    this.ampithere.moveTo(i + l + 0.5F, k, j + i1 + 0.5F,
                                        this.ampithere.yRot, this.ampithere.xRot);
                                    ampithere.getNavigation().stop();
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    protected boolean canTeleportToBlock(BlockPos pos) {
        BlockState blockstate = this.world.getBlockState(pos);
        return blockstate.isValidSpawn(this.world, pos, this.ampithere.getType()) && this.world.isEmptyBlock(pos.above()) && this.world.isEmptyBlock(pos.above(2));
    }

    private boolean tryMoveTo() {
        if (!ampithere.isFlying()) {
            return ampithere.getNavigation().moveTo(this.owner, this.followSpeed);
        } else {
            this.ampithere.getMoveControl().setWantedPosition(this.owner.getX(), this.owner.getY() + this.owner.getEyeHeight() + 5 + this.ampithere.getRandom().nextInt(8), this.owner.getZ(), 0.25D);
            return true;
        }
    }
}