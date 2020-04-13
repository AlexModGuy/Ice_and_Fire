package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.IVillagerFear;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class VillagerAIFearUntamed extends EntityAIAvoidEntity<EntityLivingBase> {

    private double avoidDistance;
    private final Predicate <? super EntityLivingBase > avoidTargetSelector;
    private Path path;
    private final PathNavigate navigation;

    public VillagerAIFearUntamed(EntityCreature entityIn, Class<EntityLivingBase> classToAvoidIn, Predicate<EntityLivingBase> avoidTargetSelectorIn, float avoidDistanceIn, double farSpeedIn, double nearSpeedIn) {
        super(entityIn, classToAvoidIn, avoidTargetSelectorIn, avoidDistanceIn, farSpeedIn, nearSpeedIn);
        avoidTargetSelector = avoidTargetSelectorIn;
        this.navigation = entityIn.getNavigator();
    }

    public boolean shouldExecuteVanilla() {
        List<EntityLivingBase> list = this.entity.world.<EntityLivingBase>getEntitiesWithinAABB(EntityLivingBase.class, this.entity.getEntityBoundingBox().grow((double)this.avoidDistance, 3.0D, (double)this.avoidDistance), this.avoidTargetSelector);

        if (list.isEmpty())
        {
            return false;
        }
        else
        {
            this.closestLivingEntity = list.get(0);
            Vec3d vec3d = RandomPositionGenerator.findRandomTargetBlockAwayFrom(this.entity, 16, 7, new Vec3d(this.closestLivingEntity.posX, this.closestLivingEntity.posY, this.closestLivingEntity.posZ));

            if (vec3d == null)
            {
                return false;
            }
            else if (this.closestLivingEntity.getDistanceSq(vec3d.x, vec3d.y, vec3d.z) < this.closestLivingEntity.getDistanceSq(this.entity))
            {
                return false;
            }
            else
            {
                this.path = this.navigation.getPathToXYZ(vec3d.x, vec3d.y, vec3d.z);
                return this.path != null;
            }
        }
    }

    public void startExecuting()
    {
        this.navigation.setPath(this.path, 0.8D);
    }


    public boolean shouldExecute() {
        boolean should = shouldExecuteVanilla();
        if (should && this.closestLivingEntity != null) {
            if (closestLivingEntity instanceof EntityTameable && ((EntityTameable) closestLivingEntity).isTamed()) {
                return false;
            }
            if (closestLivingEntity instanceof IVillagerFear && !((IVillagerFear) closestLivingEntity).shouldFear()) {
                return false;
            }
        }
        return should;
    }
}
