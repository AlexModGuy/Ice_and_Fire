package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityMyrmexSentinel;
import com.github.alexthe666.iceandfire.entity.util.EntityUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;

public class MyrmexAIFindHidingSpot extends Goal {
    private static final int RADIUS = 32;
    protected final EntityUtil.Sorter sorter;
    protected final Predicate<? super Entity> targetEntitySelector;
    private final EntityMyrmexSentinel sentinel;
    private BlockPos targetBlock = null;
    private int wanderRadius = RADIUS;

    public MyrmexAIFindHidingSpot(final EntityMyrmexSentinel sentinel) {
        super();
        this.sorter = new EntityUtil.Sorter(sentinel);
        this.targetEntitySelector = (Predicate<Entity>) myrmex -> myrmex instanceof EntityMyrmexSentinel;
        this.sentinel = sentinel;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        this.targetBlock = getTargetPosition(wanderRadius);
        return this.sentinel.canMove() && this.sentinel.getTarget() == null && sentinel.canSeeSky() && !sentinel.isHiding() && sentinel.visibleTicks <= 0;
    }

    @Override
    public boolean canContinueToUse() {
        return !sentinel.shouldEnterHive() && this.sentinel.getTarget() == null && !sentinel.isHiding() && sentinel.visibleTicks <= 0;
    }

    @Override
    public void tick() {
       if (targetBlock != null) {
           this.sentinel.getNavigation().moveTo(this.targetBlock.getX() + 0.5D, this.targetBlock.getY(), this.targetBlock.getZ() + 0.5D, 1D);
           if (areMyrmexNear(5) || this.sentinel.isOnResin()) {
               if (this.sentinel.distanceToSqr(Vec3.atCenterOf(this.targetBlock)) < 9) {
                   this.wanderRadius += RADIUS;
                   this.targetBlock = getTargetPosition(wanderRadius);
               }
           } else {
               if (this.sentinel.getTarget() == null && this.sentinel.getTradingPlayer() == null && sentinel.visibleTicks == 0 && this.sentinel.distanceToSqr(Vec3.atCenterOf(this.targetBlock)) < 9) {
                   sentinel.setHiding(true);
                   sentinel.getNavigation().stop();
               }
           }
       }

    }

    @Override
    public void stop() {
        this.targetBlock = null;
        wanderRadius = RADIUS;
    }

    protected AABB getTargetableArea(double targetDistance) {
        return this.sentinel.getBoundingBox().inflate(targetDistance, 14.0D, targetDistance);
    }

    public BlockPos getTargetPosition(int radius) {
        final int x = (int) sentinel.getX() + sentinel.getRandom().nextInt(radius * 2) - radius;
        final int z = (int) sentinel.getZ() + sentinel.getRandom().nextInt(radius * 2) - radius;
        return sentinel.level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, new BlockPos(x, 0, z));
    }

    private boolean areMyrmexNear(double distance) {
        List<Entity> sentinels = this.sentinel.level.getEntities(this.sentinel, this.getTargetableArea(distance), this.targetEntitySelector);
        List<Entity> hiddenSentinels = new ArrayList<>();
        for (Entity sentinel : sentinels) {
            if (sentinel instanceof EntityMyrmexSentinel && ((EntityMyrmexSentinel) sentinel).isHiding()) {
                hiddenSentinels.add(sentinel);
            }
        }
        return !hiddenSentinels.isEmpty();
    }

}
