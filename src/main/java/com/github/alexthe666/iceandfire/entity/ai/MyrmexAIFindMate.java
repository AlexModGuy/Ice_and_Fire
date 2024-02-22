package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityMyrmexRoyal;
import com.github.alexthe666.iceandfire.entity.util.EntityUtil;
import com.github.alexthe666.iceandfire.entity.util.MyrmexHive;
import com.github.alexthe666.iceandfire.util.IAFMath;
import com.github.alexthe666.iceandfire.world.MyrmexWorldData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.phys.AABB;

import javax.annotation.Nonnull;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;

public class MyrmexAIFindMate extends TargetGoal {
    private final EntityUtil.Sorter sorter;
    private final Predicate<? super Entity> targetEntitySelector;
    private final EntityMyrmexRoyal royal;
    private @Nonnull List<Entity> list = IAFMath.emptyEntityList;

    public MyrmexAIFindMate(final EntityMyrmexRoyal royal) {
        super(royal, false, false);
        this.sorter = new EntityUtil.Sorter(royal);
        this.targetEntitySelector = (Predicate<Entity>) entity -> entity instanceof EntityMyrmexRoyal otherRoyal && otherRoyal.getGrowthStage() >= 2;
        this.royal = royal;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (!this.royal.shouldHaveNormalAI()) {
            list = IAFMath.emptyEntityList;
            return false;
        }
        if (!this.royal.canMove() || this.royal.getTarget() != null || this.royal.releaseTicks < 400 || this.royal.mate != null) {
            list = IAFMath.emptyEntityList;
            return false;
        }
        MyrmexHive village = this.royal.getHive();
        if (village == null) {
            village = MyrmexWorldData.get(this.royal.level).getNearestHive(this.royal.blockPosition(), 100);
        }
        if (village != null && village.getCenter().distToCenterSqr(this.royal.getX(), village.getCenter().getY(), this.royal.getZ()) < 2000) {
            list = IAFMath.emptyEntityList;
            return false;
        }

        if (this.royal.level.getGameTime() % 4 == 0) // only update the list every 4 ticks
            list = this.mob.level.getEntities(royal, this.getTargetableArea(100), this.targetEntitySelector);

        if (list.isEmpty())
            return false;

        list.sort(this.sorter);
        for (Entity royal : list) {
            if (this.royal.canMate((EntityMyrmexRoyal) royal)) {
                this.royal.mate = (EntityMyrmexRoyal) royal;
                this.royal.level.broadcastEntityEvent(this.royal, (byte) 76);
                return true;
            }
        }
        return false;
    }

    protected AABB getTargetableArea(double targetDistance) {
        return this.mob.getBoundingBox().inflate(targetDistance, targetDistance / 2, targetDistance);
    }

    @Override
    public boolean canContinueToUse() {
        return false;
    }
}