package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityMyrmexSentinel;
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
    protected final DragonAITargetItems.Sorter theNearestAttackableTargetSorter;
    protected final Predicate<? super Entity> targetEntitySelector;
    private final EntityMyrmexSentinel myrmex;
    private BlockPos targetBlock = null;
    private int wanderRadius = RADIUS;

    public MyrmexAIFindHidingSpot(EntityMyrmexSentinel myrmex) {
        super();
        this.theNearestAttackableTargetSorter = new DragonAITargetItems.Sorter(myrmex);
        this.targetEntitySelector = new Predicate<Entity>() {
            @Override
            public boolean test(Entity myrmex) {
                return myrmex instanceof EntityMyrmexSentinel;
            }
        };
        this.myrmex = myrmex;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        this.targetBlock = getTargetPosition(wanderRadius);
        return this.myrmex.canMove() && this.myrmex.getTarget() == null && myrmex.canSeeSky() && !myrmex.isHiding() && myrmex.visibleTicks <= 0;
    }

    @Override
    public boolean canContinueToUse() {
        return !myrmex.shouldEnterHive() && this.myrmex.getTarget() == null && !myrmex.isHiding() && myrmex.visibleTicks <= 0;
    }

    @Override
    public void tick() {
       if (targetBlock != null) {
           this.myrmex.getNavigation().moveTo(this.targetBlock.getX() + 0.5D, this.targetBlock.getY(), this.targetBlock.getZ() + 0.5D, 1D);
           if (areMyrmexNear(5) || this.myrmex.isOnResin()) {
               if (this.myrmex.distanceToSqr(Vec3.atCenterOf(this.targetBlock)) < 9) {
                   this.wanderRadius += RADIUS;
                   this.targetBlock = getTargetPosition(wanderRadius);
               }
           } else {
               if (this.myrmex.getTarget() == null && this.myrmex.getTradingPlayer() == null && myrmex.visibleTicks == 0 && this.myrmex.distanceToSqr(Vec3.atCenterOf(this.targetBlock)) < 9) {
                   myrmex.setHiding(true);
                   myrmex.getNavigation().stop();
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
        return this.myrmex.getBoundingBox().inflate(targetDistance, 14.0D, targetDistance);
    }

    public BlockPos getTargetPosition(int radius) {
        final int x = (int) myrmex.getX() + myrmex.getRandom().nextInt(radius * 2) - radius;
        final int z = (int) myrmex.getZ() + myrmex.getRandom().nextInt(radius * 2) - radius;
        return myrmex.level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, new BlockPos(x, 0, z));
    }

    private boolean areMyrmexNear(double distance) {
        List<Entity> sentinels = this.myrmex.level.getEntities(this.myrmex, this.getTargetableArea(distance), this.targetEntitySelector);
        List<Entity> hiddenSentinels = new ArrayList<>();
        for (Entity sentinel : sentinels) {
            if (sentinel instanceof EntityMyrmexSentinel && ((EntityMyrmexSentinel) sentinel).isHiding()) {
                hiddenSentinels.add(sentinel);
            }
        }
        return !hiddenSentinels.isEmpty();
    }

}
