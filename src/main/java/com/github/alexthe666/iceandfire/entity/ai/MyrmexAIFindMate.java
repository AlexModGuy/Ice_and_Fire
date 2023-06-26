package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityMyrmexBase;
import com.github.alexthe666.iceandfire.entity.EntityMyrmexRoyal;
import com.github.alexthe666.iceandfire.entity.util.MyrmexHive;
import com.github.alexthe666.iceandfire.util.IAFMath;
import com.github.alexthe666.iceandfire.world.MyrmexWorldData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.phys.AABB;

import javax.annotation.Nonnull;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;

public class MyrmexAIFindMate<T extends EntityMyrmexBase> extends TargetGoal {
    protected final DragonAITargetItems.Sorter theNearestAttackableTargetSorter;
    protected final Predicate<? super Entity> targetEntitySelector;
    public EntityMyrmexRoyal myrmex;
    protected EntityMyrmexBase targetEntity;

    @Nonnull
    private List<Entity> list = IAFMath.emptyEntityList;

    public MyrmexAIFindMate(EntityMyrmexRoyal myrmex) {
        super(myrmex, false, false);
        this.theNearestAttackableTargetSorter = new DragonAITargetItems.Sorter(myrmex);
        this.targetEntitySelector = new Predicate<Entity>() {
            @Override
            public boolean test(Entity myrmex) {
                return myrmex instanceof EntityMyrmexRoyal && ((EntityMyrmexRoyal) myrmex).getGrowthStage() >= 2;
            }
        };
        this.myrmex = myrmex;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (!this.myrmex.shouldHaveNormalAI()) {
            list = IAFMath.emptyEntityList;
            return false;
        }
        if (!this.myrmex.canMove() || this.myrmex.getTarget() != null || this.myrmex.releaseTicks < 400 || this.myrmex.mate != null) {
            list = IAFMath.emptyEntityList;
            return false;
        }
        MyrmexHive village = this.myrmex.getHive();
        if (village == null) {
            village = MyrmexWorldData.get(this.myrmex.level()).getNearestHive(this.myrmex.blockPosition(), 100);
        }
        if (village != null && village.getCenter().distToCenterSqr(this.myrmex.getX(), village.getCenter().getY(), this.myrmex.getZ()) < 2000) {
            list = IAFMath.emptyEntityList;
            return false;
        }

        if (this.myrmex.level().getGameTime() % 4 == 0) // only update the list every 4 ticks
            list = this.mob.level().getEntities(myrmex, this.getTargetableArea(100), this.targetEntitySelector);

        if (list.isEmpty())
            return false;

        list.sort(this.theNearestAttackableTargetSorter);
        for (Entity royal : list) {
            if (this.myrmex.canMate((EntityMyrmexRoyal) royal)) {
                this.myrmex.mate = (EntityMyrmexRoyal) royal;
                this.myrmex.level().broadcastEntityEvent(this.myrmex, (byte) 76);
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

    public static class Sorter implements Comparator<Entity> {
        private final Entity theEntity;

        public Sorter(EntityMyrmexBase theEntityIn) {
            this.theEntity = theEntityIn;
        }

        @Override
        public int compare(Entity p_compare_1_, Entity p_compare_2_) {
            final double d0 = this.theEntity.distanceToSqr(p_compare_1_);
            final double d1 = this.theEntity.distanceToSqr(p_compare_2_);
            return Double.compare(d0, d1);
        }
    }
}