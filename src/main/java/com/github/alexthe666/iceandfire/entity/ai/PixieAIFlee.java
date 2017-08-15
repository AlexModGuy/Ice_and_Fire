package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityPixie;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nullable;
import java.util.List;

public class PixieAIFlee<T extends Entity> extends EntityAIBase {
    private final Predicate<Entity> canBeSeenSelector;
    protected EntityPixie pixie;
    protected T closestLivingEntity;
    private final float avoidDistance;
    private Vec3d hidePlace;
    private final Predicate <? super T > avoidTargetSelector;
    private final Class<T> classToAvoid;

    public PixieAIFlee(EntityPixie pixie, Class<T> classToAvoidIn, float avoidDistanceIn, Predicate <? super T > avoidTargetSelectorIn) {
        this.pixie = pixie;
        this.classToAvoid = classToAvoidIn;
        this.canBeSeenSelector = new Predicate<Entity>() {
            public boolean apply(@Nullable Entity entity) {
                return entity.isEntityAlive() && PixieAIFlee.this.pixie.getEntitySenses().canSee(entity) && !PixieAIFlee.this.pixie.isOnSameTeam(entity);
            }
        };
        this.avoidTargetSelector = avoidTargetSelectorIn;
        this.avoidDistance = avoidDistanceIn;
        this.setMutexBits(1);
    }



    public boolean shouldExecute() {
        if (this.pixie.heldItem.isEmpty()) {
            return false;
        }
        if (this.pixie.isTamed()) {
            return false;
        }
        List<T> list = this.pixie.world.<T>getEntitiesWithinAABB(this.classToAvoid, this.pixie.getEntityBoundingBox().grow((double)this.avoidDistance, 3.0D, (double)this.avoidDistance), Predicates.and(new Predicate[] {EntitySelectors.CAN_AI_TARGET, this.canBeSeenSelector, this.avoidTargetSelector}));
        if (list.isEmpty()) {
            return false;
        }
        else {
            this.closestLivingEntity = list.get(0);
            Vec3d vec3d = RandomPositionGenerator.findRandomTargetBlockAwayFrom(this.pixie, 16, 7, new Vec3d(this.closestLivingEntity.posX, this.closestLivingEntity.posY, this.closestLivingEntity.posZ));

            if (vec3d == null) {
                return false;
            }
            else if (this.closestLivingEntity.getDistanceSq(vec3d.x, vec3d.y, vec3d.z) < this.closestLivingEntity.getDistanceSqToEntity(this.pixie)) {
                return false;
            }
            else {
                this.pixie.getMoveHelper().setMoveTo(vec3d.x, vec3d.y, vec3d.z, 1D);
                hidePlace = vec3d;
                return true;
            }
        }
    }

    public boolean shouldContinueExecuting(){
        return this.pixie.getMoveHelper().action != EntityMoveHelper.Action.WAIT;
    }

    public void startExecuting() {
        this.pixie.getMoveHelper().setMoveTo(hidePlace.x, hidePlace.y, hidePlace.z, 1D);
    }

    public void resetTask() {
        this.closestLivingEntity = null;
    }
}