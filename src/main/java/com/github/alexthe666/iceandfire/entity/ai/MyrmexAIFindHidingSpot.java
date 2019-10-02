package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityMyrmexSentinel;
import com.google.common.base.Predicate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class MyrmexAIFindHidingSpot extends EntityAIBase {
    private static final int RADIUS = 32;
    protected final DragonAITargetItems.Sorter theNearestAttackableTargetSorter;
    protected final Predicate<? super Entity> targetEntitySelector;
    private final EntityMyrmexSentinel myrmex;
    private BlockPos targetBlock = BlockPos.ORIGIN;
    private int wanderRadius = RADIUS;

    public MyrmexAIFindHidingSpot(EntityMyrmexSentinel myrmex) {
        super();
        this.theNearestAttackableTargetSorter = new DragonAITargetItems.Sorter(myrmex);
        this.targetEntitySelector = new Predicate<Entity>() {
            @Override
            public boolean apply(@Nullable Entity myrmex) {
                return myrmex != null && myrmex instanceof EntityMyrmexSentinel;
            }
        };
        this.myrmex = myrmex;
    }

    @Override
    public boolean shouldExecute() {
        this.targetBlock = getTargetPosition(wanderRadius);
        return this.myrmex.canMove() && this.myrmex.getAttackTarget() == null && myrmex.canSeeSky();
    }

    @Override
    public boolean shouldContinueExecuting() {
        return !myrmex.shouldEnterHive() && this.myrmex.getNavigator().noPath();
    }

    @Override
    public void updateTask() {
        if (areMyrmexNear(RADIUS) || this.myrmex.isOnResin()) {
            this.myrmex.getNavigator().tryMoveToXYZ(this.targetBlock.getX() + 0.5D, this.targetBlock.getY(), this.targetBlock.getZ() + 0.5D, 1D);
            if (this.myrmex.getDistanceSqToCenter(this.targetBlock) < 2) {
                this.wanderRadius += RADIUS;
                this.targetBlock = getTargetPosition(wanderRadius);
            }
        } else {
            if (this.myrmex.getAttackTarget() == null) {
                this.myrmex.setHiding(true);
                resetTask();
            }
        }

    }

    public void resetTask() {
        this.targetBlock = BlockPos.ORIGIN;
        wanderRadius = RADIUS;
    }

    protected AxisAlignedBB getTargetableArea(double targetDistance) {
        return this.myrmex.getEntityBoundingBox().grow(targetDistance, 14.0D, targetDistance);
    }

    public BlockPos getTargetPosition(int radius) {
        int x = (int) myrmex.posX + myrmex.getRNG().nextInt(radius * 2) - radius;
        int z = (int) myrmex.posZ + myrmex.getRNG().nextInt(radius * 2) - radius;
        return myrmex.world.getHeight(new BlockPos(x, 0, z));
    }

    private boolean areMyrmexNear(double distance) {
        List<Entity> sentinels = this.myrmex.world.getEntitiesInAABBexcluding(this.myrmex, this.getTargetableArea(distance), this.targetEntitySelector);
        List<Entity> hiddenSentinels = new ArrayList<>();
        for (Entity sentinel : sentinels) {
            if (sentinel instanceof EntityMyrmexSentinel && ((EntityMyrmexSentinel) sentinel).isHiding()) {
                hiddenSentinels.add(sentinel);
            }
        }
        return !hiddenSentinels.isEmpty();
    }

}
