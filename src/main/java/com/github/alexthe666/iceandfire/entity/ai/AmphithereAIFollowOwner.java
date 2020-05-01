package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class AmphithereAIFollowOwner extends EntityAIBase {
    private final EntityAmphithere ampithere;
    private final double followSpeed;
    World world;
    float maxDist;
    float minDist;
    private EntityLivingBase owner;
    private int timeToRecalcPath;
    private float oldWaterCost;

    public AmphithereAIFollowOwner(EntityAmphithere ampithereIn, double followSpeedIn, float minDistIn, float maxDistIn) {
        this.ampithere = ampithereIn;
        this.world = ampithereIn.world;
        this.followSpeed = followSpeedIn;
        this.minDist = minDistIn;
        this.maxDist = maxDistIn;
        this.setMutexBits(3);
    }

    public boolean shouldExecute() {
        EntityLivingBase entitylivingbase = this.ampithere.getOwner();
        if (ampithere.getCommand() != 2) {
            return false;
        }
        if (entitylivingbase == null) {
            return false;
        } else if (entitylivingbase instanceof EntityPlayer && ((EntityPlayer) entitylivingbase).isSpectator()) {
            return false;
        } else if (this.ampithere.isSitting()) {
            return false;
        } else if (this.ampithere.getDistanceSq(entitylivingbase) < (double) (this.minDist * this.minDist)) {
            return false;
        } else {
            this.owner = entitylivingbase;
            return true;
        }
    }

    public boolean shouldContinueExecuting() {
        return !noPath() && this.ampithere.getDistanceSq(this.owner) > (double) (this.maxDist * this.maxDist) && !this.ampithere.isSitting();
    }

    private boolean noPath() {
        if (!ampithere.isFlying()) {
            return this.ampithere.getNavigator().noPath();
        } else {
            return this.ampithere.getMoveHelper().action != EntityMoveHelper.Action.WAIT;
        }
    }

    public void startExecuting() {
        this.timeToRecalcPath = 0;
        this.oldWaterCost = this.ampithere.getPathPriority(PathNodeType.WATER);
        this.ampithere.setPathPriority(PathNodeType.WATER, 0.0F);
    }

    public void resetTask() {
        this.owner = null;
        this.ampithere.getNavigator().clearPath();
        this.ampithere.setPathPriority(PathNodeType.WATER, this.oldWaterCost);
    }

    public void updateTask() {
        this.ampithere.getLookHelper().setLookPositionWithEntity(this.owner, 10.0F, (float) this.ampithere.getVerticalFaceSpeed());

        if (!this.ampithere.isSitting()) {
            if (--this.timeToRecalcPath <= 0) {
                this.timeToRecalcPath = 10;
                tryMoveTo();
                if (!this.ampithere.getLeashed() && !this.ampithere.isRiding()) {
                    if (this.ampithere.getDistanceSq(this.owner) >= 144.0D) {
                        int i = MathHelper.floor(this.owner.posX) - 2;
                        int j = MathHelper.floor(this.owner.posZ) - 2;
                        int k = MathHelper.floor(this.owner.getEntityBoundingBox().minY);

                        for (int l = 0; l <= 4; ++l) {
                            for (int i1 = 0; i1 <= 4; ++i1) {
                                if ((l < 1 || i1 < 1 || l > 3 || i1 > 3) && this.isTeleportFriendlyBlock(i, j, k, l, i1)) {
                                    this.ampithere.setLocationAndAngles((double) ((float) (i + l) + 0.5F), (double) k, (double) ((float) (j + i1) + 0.5F), this.ampithere.rotationYaw, this.ampithere.rotationPitch);
                                    ampithere.getNavigator().clearPath();
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    protected boolean isTeleportFriendlyBlock(int x, int z, int y, int xOffset, int zOffset) {
        BlockPos blockpos = new BlockPos(x + xOffset, y - 1, z + zOffset);
        BlockState BlockState = this.world.getBlockState(blockpos);
        return BlockState.getBlockFaceShape(this.world, blockpos, EnumFacing.DOWN) == BlockFaceShape.SOLID && BlockState.canEntitySpawn(this.ampithere) && this.world.isAirBlock(blockpos.up()) && this.world.isAirBlock(blockpos.up(2));
    }

    private boolean tryMoveTo() {
        if (!ampithere.isFlying()) {
            return ampithere.getNavigator().tryMoveToEntityLiving(this.owner, this.followSpeed);
        } else {
            this.ampithere.getMoveHelper().setMoveTo(this.owner.posX, this.owner.posY + this.owner.getEyeHeight() + 5 + this.ampithere.getRNG().nextInt(8), this.owner.posZ, 0.25D);
            return true;
        }
    }
}