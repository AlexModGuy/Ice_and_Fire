package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityCockatrice;
import com.github.alexthe666.iceandfire.entity.EntityGorgon;
import com.github.alexthe666.iceandfire.entity.util.DragonUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class CockatriceAIStareAttack extends Goal {
    private final EntityCockatrice entity;
    private final double moveSpeedAmp;
    private int seeTime;
    private BlockPos target = null;

    public CockatriceAIStareAttack(EntityCockatrice cockatrice, double speedAmplifier, int delay, float maxDistance) {
        this.entity = cockatrice;
        this.moveSpeedAmp = speedAmplifier;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    public static boolean isEntityLookingAt(LivingEntity looker, LivingEntity seen, double degree) {
        Vec3 Vector3d = looker.getViewVector(1.0F).normalize();
        Vec3 Vector3d1 = new Vec3(seen.getX() - looker.getX(), seen.getBoundingBox().minY + seen.getEyeHeight() - (looker.getY() + looker.getEyeHeight()), seen.getZ() - looker.getZ());
        Vector3d1 = Vector3d1.normalize();
        final double d0 = Vector3d1.length();
        final double d1 = Vector3d.dot(Vector3d1);
        return d1 > 1.0D - degree / d0 && !looker.isSpectator();
    }

    public void setAttackCooldown(int cooldown) {
    }

    @Override
    public boolean canUse() {
        return this.entity.getTarget() != null;
    }

    @Override
    public boolean canContinueToUse() {
        return this.canUse();
    }

    @Override
    public void stop() {
        super.stop();
        this.seeTime = 0;
        this.entity.stopUsingItem();
        this.entity.getNavigation().stop();
        target = null;
    }

    @Override
    public void tick() {
        LivingEntity LivingEntity = this.entity.getTarget();
        if (LivingEntity != null) {

            if (EntityGorgon.isStoneMob(LivingEntity) || !LivingEntity.isAlive()) {
                entity.setTarget(null);
                this.entity.setTargetedEntity(0);
                stop();
                return;
            }
            if (!isEntityLookingAt(LivingEntity, entity, EntityCockatrice.VIEW_RADIUS) || (LivingEntity.xo != entity.getX() || LivingEntity.yo != entity.getY() || LivingEntity.zo != entity.getZ())) {
                this.entity.getNavigation().stop();
                BlockPos pos = DragonUtils.getBlockInTargetsViewCockatrice(this.entity, LivingEntity);
                if (target == null || pos.distSqr(target) > 4) {
                    target = pos;
                }
            }
            this.entity.setTargetedEntity(LivingEntity.getId());

            this.entity.distanceToSqr(LivingEntity.getX(), LivingEntity.getBoundingBox().minY,
                LivingEntity.getZ());
            final boolean flag = this.entity.getSensing().hasLineOfSight(LivingEntity);
            final boolean flag1 = this.seeTime > 0;

            if (flag != flag1) {
                this.seeTime = 0;
            }

            if (flag) {
                ++this.seeTime;
            } else {
                --this.seeTime;
            }
            if (target != null) {
                if (this.entity.distanceToSqr(target.getX(), target.getY(), target.getZ()) > 16 && !isEntityLookingAt(LivingEntity, entity, EntityCockatrice.VIEW_RADIUS)) {
                    this.entity.getNavigation().moveTo(target.getX(), target.getY(), target.getZ(), moveSpeedAmp);
                }

            }
            this.entity.getLookControl().setLookAt(LivingEntity.getX(), LivingEntity.getY() + LivingEntity.getEyeHeight(), LivingEntity.getZ(), this.entity.getMaxHeadYRot(), this.entity.getMaxHeadXRot());
        }
    }

}