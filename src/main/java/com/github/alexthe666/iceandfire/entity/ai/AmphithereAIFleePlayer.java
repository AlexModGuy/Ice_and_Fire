package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import com.github.alexthe666.iceandfire.util.IAFMath;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nonnull;
import java.util.EnumSet;
import java.util.List;

public class AmphithereAIFleePlayer extends Goal {
    private final double farSpeed;
    private final double nearSpeed;
    private final float avoidDistance;
    protected EntityAmphithere entity;
    protected Player closestLivingEntity;
    private Path path;

    @Nonnull
    private List<Player> list = IAFMath.emptyPlayerEntityList;

    public AmphithereAIFleePlayer(EntityAmphithere entityIn, float avoidDistanceIn, double farSpeedIn, double nearSpeedIn) {
        this.entity = entityIn;
        this.avoidDistance = avoidDistanceIn;
        this.farSpeed = farSpeedIn;
        this.nearSpeed = nearSpeedIn;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (!this.entity.isFlying() && !this.entity.isTame()) {

            if (this.entity.level().getGameTime() % 4 == 0) // only update the list every 4 ticks
                list = this.entity.level().getEntitiesOfClass(Player.class, this.entity.getBoundingBox().inflate(this.avoidDistance, 6D, this.avoidDistance), EntitySelector.NO_CREATIVE_OR_SPECTATOR);

            if (list.isEmpty())
                return false;

            this.closestLivingEntity = list.get(0);
            Vec3 Vector3d = DefaultRandomPos.getPosAway(this.entity, 20, 7, new Vec3(this.closestLivingEntity.getX(), this.closestLivingEntity.getY(), this.closestLivingEntity.getZ()));

            if (Vector3d == null) {
                return false;
            } else if (this.closestLivingEntity.distanceToSqr(Vector3d) < this.closestLivingEntity.distanceToSqr(this.entity)) {
                return false;
            } else {
                this.path = this.entity.getNavigation().createPath(Vector3d.x, Vector3d.y, Vector3d.z, 0);
                return this.path != null;
            }

        } else {
            list = IAFMath.emptyPlayerEntityList;
            return false;
        }
    }

    @Override
    public boolean canContinueToUse() {
        return !this.entity.getNavigation().isDone();
    }

    @Override
    public void start() {
        this.entity.getNavigation().moveTo(this.path, this.farSpeed);
    }

    @Override
    public void stop() {
        this.closestLivingEntity = null;
    }

    @Override
    public void tick() {
        if (this.entity.distanceToSqr(this.closestLivingEntity) < 49.0D) {
            this.entity.getNavigation().setSpeedModifier(this.nearSpeed);
        } else {
            this.entity.getNavigation().setSpeedModifier(this.farSpeed);
        }
    }
}