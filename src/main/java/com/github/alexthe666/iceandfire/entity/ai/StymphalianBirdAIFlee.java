package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityStymphalianBird;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nullable;
import java.util.List;

public class StymphalianBirdAIFlee<T extends Entity> extends EntityAIBase {
    private final Predicate<Entity> canBeSeenSelector;
    private final float avoidDistance;
    private final Predicate<? super T> avoidTargetSelector;
    private final Class<T> classToAvoid;
    protected EntityStymphalianBird stymphalianBird;
    protected T closestLivingEntity;
    private Vec3d hidePlace;

    public StymphalianBirdAIFlee(EntityStymphalianBird stymphalianBird, Class<T> classToAvoidIn, float avoidDistanceIn, Predicate<? super T> avoidTargetSelectorIn) {
        this.stymphalianBird = stymphalianBird;
        this.classToAvoid = classToAvoidIn;
        this.canBeSeenSelector = new Predicate<Entity>() {
            public boolean apply(@Nullable Entity entity) {
                return entity.isEntityAlive() && StymphalianBirdAIFlee.this.stymphalianBird.getEntitySenses().canSee(entity) && !StymphalianBirdAIFlee.this.stymphalianBird.isOnSameTeam(entity);
            }
        };
        this.avoidTargetSelector = avoidTargetSelectorIn;
        this.avoidDistance = avoidDistanceIn;
        this.setMutexBits(1);
    }


    public boolean shouldExecute() {
        if (!this.stymphalianBird.hasVictorEntity()) {
            return false;
        }
        List<T> list = this.stymphalianBird.world.<T>getEntitiesWithinAABB(this.classToAvoid, this.stymphalianBird.getEntityBoundingBox().grow((double) this.avoidDistance, 3.0D, (double) this.avoidDistance),
                Predicates.and(new Predicate[]{EntitySelectors.NOT_SPECTATING, this.canBeSeenSelector, this.avoidTargetSelector}));

        if (list.isEmpty()) {
            return false;
        } else {
            this.closestLivingEntity = list.get(0);
            if (closestLivingEntity != null) {
                Vec3d vec3d = RandomPositionGenerator.findRandomTargetBlockAwayFrom(this.stymphalianBird, 16, 7, new Vec3d(this.closestLivingEntity.posX, this.closestLivingEntity.posY, this.closestLivingEntity.posZ));

                if (vec3d == null) {
                    return false;
                } else {
                    vec3d = vec3d.addVector(0, 3, 0);
                    this.stymphalianBird.getMoveHelper().setMoveTo(vec3d.x, vec3d.y, vec3d.z, 1D);
                    this.stymphalianBird.getLookHelper().setLookPosition(vec3d.x, vec3d.y, vec3d.z, 180.0F, 20.0F);
                    hidePlace = vec3d;
                    return true;
                }
            }
            return false;
        }
    }

    public boolean shouldContinueExecuting() {
        return hidePlace != null && this.stymphalianBird.getDistanceSqToCenter(new BlockPos(hidePlace)) < 2;
    }

    public void startExecuting() {
        this.stymphalianBird.getMoveHelper().setMoveTo(hidePlace.x, hidePlace.y, hidePlace.z, 1D);
        this.stymphalianBird.getLookHelper().setLookPosition(hidePlace.x, hidePlace.y, hidePlace.z, 180.0F, 20.0F);
    }

    public void resetTask() {
        this.stymphalianBird.getMoveHelper().action = EntityMoveHelper.Action.WAIT;
        this.closestLivingEntity = null;
    }
}