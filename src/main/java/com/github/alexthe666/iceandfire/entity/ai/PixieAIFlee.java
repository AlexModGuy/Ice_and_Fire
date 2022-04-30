package com.github.alexthe666.iceandfire.entity.ai;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;

import com.github.alexthe666.iceandfire.entity.EntityPixie;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.Hand;
import net.minecraft.util.math.vector.Vector3d;

import javax.annotation.Nonnull;


public class PixieAIFlee<T extends Entity> extends Goal {
    private final float avoidDistance;
    private final Class<T> classToAvoid;
    protected EntityPixie pixie;
    protected T closestLivingEntity;
    private Vector3d hidePlace;

    @Nonnull
    private List<T> list = Collections.emptyList();

    public PixieAIFlee(EntityPixie pixie, Class<T> classToAvoidIn, float avoidDistanceIn, Predicate<? super T> avoidTargetSelectorIn) {
        this.pixie = pixie;
        this.classToAvoid = classToAvoidIn;
        this.avoidDistance = avoidDistanceIn;
        this.setMutexFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean shouldExecute() {
        if (this.pixie.getHeldItem(Hand.MAIN_HAND).isEmpty() || this.pixie.isTamed()) {
            return false;
        }

        if (this.pixie.world.getGameTime() % 4 == 0) // only update the list every 4 ticks
            list = this.pixie.world.getEntitiesWithinAABB(this.classToAvoid, this.pixie.getBoundingBox().grow(this.avoidDistance, 3.0D, this.avoidDistance), EntityPredicates.NOT_SPECTATING);

        if (list.isEmpty())
            return false;

        this.closestLivingEntity = list.get(0);
        if (closestLivingEntity != null) {
            Vector3d Vector3d = RandomPositionGenerator.findRandomTargetBlockAwayFrom(this.pixie, 16, 4, new Vector3d(this.closestLivingEntity.getPosX(), this.closestLivingEntity.getPosY(), this.closestLivingEntity.getPosZ()));

            if (Vector3d == null) {
                return false;
            } else {
                Vector3d = Vector3d.add(0, 1, 0);
                this.pixie.getMoveHelper().setMoveTo(Vector3d.x, Vector3d.y, Vector3d.z, calculateRunSpeed());
                this.pixie.getLookController().setLookPosition(Vector3d.x, Vector3d.y, Vector3d.z, 180.0F, 20.0F);
                hidePlace = Vector3d;
                pixie.slowSpeed = true;
                return true;
            }
        }
        return false;
    }

    private double calculateRunSpeed() {
        if(pixie.ticksHeldItemFor > 6000){
            return 0.1D;
        }
        if(pixie.ticksHeldItemFor > 1200){
            return 0.25D;
        }
        if(pixie.ticksHeldItemFor > 600){
            return 0.25D;
        }
        return 1D;
    }

    @Override
    public boolean shouldContinueExecuting() {
        return hidePlace != null && this.pixie.getDistanceSq(hidePlace.add(0.5, 0.5, 0.5)) < 2;
    }

    @Override
    public void startExecuting() {
        this.pixie.getMoveHelper().setMoveTo(hidePlace.x, hidePlace.y, hidePlace.z, calculateRunSpeed());
        this.pixie.getLookController().setLookPosition(hidePlace.x, hidePlace.y, hidePlace.z, 180.0F, 20.0F);
    }

    @Override
    public void resetTask() {
        this.closestLivingEntity = null;
        pixie.slowSpeed = false;
    }
}