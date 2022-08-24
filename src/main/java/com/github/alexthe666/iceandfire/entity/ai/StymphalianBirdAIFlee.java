package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityStymphalianBird;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.vector.Vector3d;

import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;

public class StymphalianBirdAIFlee extends Goal {
    private final Predicate<Entity> canBeSeenSelector;
    private final float avoidDistance;
    protected EntityStymphalianBird stymphalianBird;
    protected LivingEntity closestLivingEntity;
    private Vector3d hidePlace;

    public StymphalianBirdAIFlee(EntityStymphalianBird stymphalianBird, float avoidDistanceIn) {
        this.stymphalianBird = stymphalianBird;
        this.canBeSeenSelector = new Predicate<Entity>() {

            @Override
            public boolean test(Entity entity) {
                return entity instanceof PlayerEntity && entity.isAlive() && StymphalianBirdAIFlee.this.stymphalianBird.getSensing().canSee(entity) && !StymphalianBirdAIFlee.this.stymphalianBird.isAlliedTo(entity);
            }
        };
        this.avoidDistance = avoidDistanceIn;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }


    @Override
    public boolean canUse() {
        if (this.stymphalianBird.getVictor() == null) {
            return false;
        }
        List<LivingEntity> list = this.stymphalianBird.level.getEntitiesOfClass(LivingEntity.class, this.stymphalianBird.getBoundingBox().inflate(this.avoidDistance, 3.0D, this.avoidDistance),
            this.canBeSeenSelector);

        if (list.isEmpty())
            return false;

        this.closestLivingEntity = list.get(0);
        if (closestLivingEntity != null && this.stymphalianBird.getVictor() != null && this.closestLivingEntity.equals(this.stymphalianBird.getVictor())) {
            Vector3d Vector3d = RandomPositionGenerator.getPosAvoid(this.stymphalianBird, 32, 7, new Vector3d(this.closestLivingEntity.getX(), this.closestLivingEntity.getY(), this.closestLivingEntity.getZ()));

            if (Vector3d == null) {
                return false;
            } else {
                Vector3d = Vector3d.add(0, 3, 0);
                this.stymphalianBird.getMoveControl().setWantedPosition(Vector3d.x, Vector3d.y, Vector3d.z, 3D);
                this.stymphalianBird.getLookControl().setLookAt(Vector3d.x, Vector3d.y, Vector3d.z, 180.0F, 20.0F);
                hidePlace = Vector3d;
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean canContinueToUse() {
        return hidePlace != null && this.stymphalianBird.distanceToSqr(hidePlace.add(0.5, 0.5, 0.5)) < 2;
    }

    @Override
    public void start() {
        this.stymphalianBird.getMoveControl().setWantedPosition(hidePlace.x, hidePlace.y, hidePlace.z, 3D);
        this.stymphalianBird.getLookControl().setLookAt(hidePlace.x, hidePlace.y, hidePlace.z, 180.0F, 20.0F);
    }

    @Override
    public void stop() {
        this.closestLivingEntity = null;
    }
}