package com.github.alexthe666.iceandfire.entity.ai;

import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;

import com.github.alexthe666.iceandfire.entity.EntityMyrmexBase;
import com.github.alexthe666.iceandfire.entity.EntityMyrmexRoyal;
import com.github.alexthe666.iceandfire.entity.util.MyrmexHive;
import com.github.alexthe666.iceandfire.util.IAFMath;
import com.github.alexthe666.iceandfire.world.MyrmexWorldData;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.goal.TargetGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.AxisAlignedBB;

import javax.annotation.Nonnull;

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
        this.setMutexFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean shouldExecute() {
        if (!this.myrmex.shouldHaveNormalAI()) {
            list = IAFMath.emptyEntityList;
            return false;
        }
        if (!this.myrmex.canMove() || this.myrmex.getAttackTarget() != null || this.myrmex.releaseTicks < 400 || this.myrmex.mate != null) {
            list = IAFMath.emptyEntityList;
            return false;
        }
        MyrmexHive village = this.myrmex.getHive();
        if (village == null) {
            village = MyrmexWorldData.get(this.myrmex.world).getNearestHive(this.myrmex.getPosition(), 100);
        }
        if (village != null && village.getCenter().distanceSq(this.myrmex.getPosX(), village.getCenter().getY(), this.myrmex.getPosZ(), true) < 2000) {
            list = IAFMath.emptyEntityList;
            return false;
        }

        if (this.myrmex.world.getGameTime() % 4 == 0) // only update the list every 4 ticks
            list = this.goalOwner.world.getEntitiesInAABBexcluding(myrmex, this.getTargetableArea(100), this.targetEntitySelector);

        if (list.isEmpty())
            return false;

        list.sort(this.theNearestAttackableTargetSorter);
        for (Entity royal : list) {
            if (this.myrmex.canMateWith((EntityMyrmexRoyal) royal)) {
                this.myrmex.mate = (EntityMyrmexRoyal) royal;
                this.myrmex.world.setEntityState(this.myrmex, (byte) 76);
                return true;
            }
        }
        return false;
    }

    protected AxisAlignedBB getTargetableArea(double targetDistance) {
        return this.goalOwner.getBoundingBox().grow(targetDistance, targetDistance/2, targetDistance);
    }

    @Override
    public boolean shouldContinueExecuting() {
        return false;
    }

    public static class Sorter implements Comparator<Entity> {
        private final Entity theEntity;

        public Sorter(EntityMyrmexBase theEntityIn) {
            this.theEntity = theEntityIn;
        }

        @Override
        public int compare(Entity p_compare_1_, Entity p_compare_2_) {
            final double d0 = this.theEntity.getDistanceSq(p_compare_1_);
            final double d1 = this.theEntity.getDistanceSq(p_compare_2_);
            return Double.compare(d0, d1);
        }
    }
}