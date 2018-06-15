package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityStymphalianBird;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nullable;
import java.util.List;

public class StymphalianBirdAIFlee extends EntityAIBase {
    private final Predicate<Entity> canBeSeenSelector;
    private final float avoidDistance;
    protected EntityStymphalianBird stymphalianBird;
    protected EntityLivingBase closestLivingEntity;
    private Vec3d hidePlace;

    public StymphalianBirdAIFlee(EntityStymphalianBird stymphalianBird, float avoidDistanceIn) {
        this.stymphalianBird = stymphalianBird;
        this.canBeSeenSelector = new Predicate<Entity>() {
            public boolean apply(@Nullable Entity entity) {
                return entity instanceof  EntityPlayer && entity.isEntityAlive() && StymphalianBirdAIFlee.this.stymphalianBird.getEntitySenses().canSee(entity) && !StymphalianBirdAIFlee.this.stymphalianBird.isOnSameTeam(entity);
            }
        };
        this.avoidDistance = avoidDistanceIn;
        this.setMutexBits(1);
    }


    public boolean shouldExecute() {
        if (this.stymphalianBird.getVictor() == null) {
            return false;
        }
        List<EntityLivingBase> list = this.stymphalianBird.world.<EntityLivingBase>getEntitiesWithinAABB(EntityLivingBase.class, this.stymphalianBird.getEntityBoundingBox().grow((double) this.avoidDistance, 3.0D, (double) this.avoidDistance),
                Predicates.and(new Predicate[]{EntitySelectors.NOT_SPECTATING, this.canBeSeenSelector}));

        if (list.isEmpty()) {
            return false;
        } else {
            this.closestLivingEntity = list.get(0);
            if (closestLivingEntity != null && this.stymphalianBird.getVictor() != null && this.closestLivingEntity.equals(this.stymphalianBird.getVictor())) {
                Vec3d vec3d = RandomPositionGenerator.findRandomTargetBlockAwayFrom(this.stymphalianBird, 32, 7, new Vec3d(this.closestLivingEntity.posX, this.closestLivingEntity.posY, this.closestLivingEntity.posZ));

                if (vec3d == null) {
                    return false;
                } else {
                    vec3d = vec3d.addVector(0, 3, 0);
                    this.stymphalianBird.getMoveHelper().setMoveTo(vec3d.x, vec3d.y, vec3d.z, 3D);
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
        this.stymphalianBird.getMoveHelper().setMoveTo(hidePlace.x, hidePlace.y, hidePlace.z, 3D);
        this.stymphalianBird.getLookHelper().setLookPosition(hidePlace.x, hidePlace.y, hidePlace.z, 180.0F, 20.0F);
    }

    public void resetTask() {
        this.stymphalianBird.getMoveHelper().action = EntityMoveHelper.Action.WAIT;
        this.closestLivingEntity = null;
    }
}