package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityMyrmexSentinel;
import com.google.common.base.Predicate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nullable;
import java.util.List;

import static net.minecraft.entity.ai.RandomPositionGenerator.findRandomTarget;

public class MyrmexAIFindHidingSpot extends EntityAIBase {
    private static final int RADIUS = 15;

    private final EntityMyrmexSentinel myrmex;
    private BlockPos targetBlock = BlockPos.ORIGIN;
    private int wanderRadius = RADIUS;
    protected final DragonAITargetItems.Sorter theNearestAttackableTargetSorter;
    protected final Predicate<? super Entity> targetEntitySelector;

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
        this.setMutexBits(1);
    }

    @Override
    public boolean shouldExecute() {
        Vec3d vec = findRandomTarget(this.myrmex, wanderRadius, 7);
        if(vec != null){
            this.targetBlock = new BlockPos(vec);
        }
        return this.myrmex.canMove() && this.myrmex.getAttackTarget() == null && myrmex.canSeeSky();
    }

    @Override
    public boolean shouldContinueExecuting() {
        return !myrmex.shouldEnterHive() && this.myrmex.getNavigator().noPath();
    }

    @Override
    public void updateTask() {
        if(areMyrmexNear(RADIUS) || this.myrmex.isOnResin()){
            this.myrmex.getNavigator().tryMoveToXYZ(this.targetBlock.getX() + 0.5D, this.targetBlock.getY(), this.targetBlock.getZ() + 0.5D, 1D);
            if (this.myrmex.getDistanceSqToCenter(this.targetBlock) < 2) {
                this.wanderRadius += RADIUS;
                Vec3d vec = findRandomTarget(this.myrmex, wanderRadius, 7);
                if(vec != null){
                    this.targetBlock = new BlockPos(vec);
                }
            }
        }else{
            if(this.myrmex.getAttackTarget() == null){
                this.myrmex.setHiding(true);
            }
            resetTask();
        }

    }

    public void resetTask(){
        this.targetBlock = BlockPos.ORIGIN;
        wanderRadius = RADIUS;
    }

    protected AxisAlignedBB getTargetableArea(double targetDistance) {
        return this.myrmex.getEntityBoundingBox().grow(targetDistance, 4.0D, targetDistance);
    }

    private boolean areMyrmexNear(double distance){
        List<Entity> sentinels = this.myrmex.world.getEntitiesInAABBexcluding(this.myrmex, this.getTargetableArea(distance), this.targetEntitySelector);
        return !sentinels.isEmpty();
    }

}
