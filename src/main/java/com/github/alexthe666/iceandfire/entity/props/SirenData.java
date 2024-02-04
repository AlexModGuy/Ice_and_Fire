package com.github.alexthe666.iceandfire.entity.props;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.entity.EntitySiren;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;


public class SirenData {
    public Entity charmedBy;
    public int charmTime; // FIXME :: when is this ever increased
    public boolean isCharmed;

    private int charmedById;
    private boolean isInitialized;

    public boolean tickCharmed(final LivingEntity entity) {
        if (!isInitialized) {
            charmedBy = entity.getLevel().getEntity(charmedById);
        }

        if (charmedBy instanceof EntitySiren siren && siren.isActuallySinging()) {
            if (EntitySiren.isWearingEarplugs(entity) || charmTime > IafConfig.sirenMaxSingTime) {
                clearCharm(entity);
                siren.singCooldown = IafConfig.sirenTimeBetweenSongs;
                return true;
            }

            if (!siren.isAlive() || entity.distanceTo(siren) > EntitySiren.SEARCH_RANGE * 2 || entity instanceof Player player && (player.isCreative() || player.isSpectator())) {
                clearCharm(entity);
                return true;
            }

            if (entity.distanceTo(siren) < 5) {
                clearCharm(entity);
                siren.singCooldown = IafConfig.sirenTimeBetweenSongs;
                siren.setSinging(false);
                siren.setTarget(entity);
                siren.setAggressive(true);
                siren.triggerOtherSirens(entity);
                return true;
            }

            isCharmed = true;

            if (entity.getRandom().nextInt(7) == 0) {
                for (int i = 0; i < 5; i++) {
                    entity.level.addParticle(ParticleTypes.HEART,
                            entity.getX() + ((entity.getRandom().nextDouble() - 0.5D) * 3),
                            entity.getY() + ((entity.getRandom().nextDouble() - 0.5D) * 3),
                            entity.getZ() + ((entity.getRandom().nextDouble() - 0.5D) * 3),
                            0, 0, 0);
                }
            }

            if (entity.horizontalCollision) {
                entity.setJumping(true);
            }

            double motionXAdd = (Math.signum(siren.getX() - entity.getX()) * 0.5D - entity.getDeltaMovement().x) * 0.100000000372529;
            double motionYAdd = (Math.signum(siren.getY() - entity.getY() + 1) * 0.5D - entity.getDeltaMovement().y) * 0.100000000372529;
            double motionZAdd = (Math.signum(siren.getZ() - entity.getZ()) * 0.5D - entity.getDeltaMovement().z) * 0.100000000372529;

            entity.setDeltaMovement(entity.getDeltaMovement().add(motionXAdd, motionYAdd, motionZAdd));

            if (entity.isPassenger()) {
                entity.stopRiding();
            }

            if (!(entity instanceof Player)) {
                double d0 = siren.getX() - entity.getX();
                double d2 = siren.getZ() - entity.getZ();
                double d1 = siren.getY() - 1 - entity.getY();
                double d3 = Math.sqrt(d0 * d0 + d2 * d2);
                float f = (float) (Mth.atan2(d2, d0) * (180D / Math.PI)) - 90.0F;
                float f1 = (float) (-(Mth.atan2(d1, d3) * (180D / Math.PI)));
                entity.setXRot(updateRotation(entity.getXRot(), f1, 30F));
                entity.setYRot(updateRotation(entity.getYRot(), f, 30F));
            }
        }

        return false;
    }

    public void setCharmed(final Entity charmed, final Entity siren) {
        charmedById = siren.getId();
        charmedBy = siren;
        isCharmed = true;

        CapabilityHandler.syncEntityData(charmed);
    }

    public void clearCharm(final Entity charmed) {
        charmTime = 0;
        isCharmed = false;
        charmedBy = null;
        charmedById = -1;
    }

    public void serialize(final CompoundTag tag) {
        CompoundTag sirenData = new CompoundTag();
        sirenData.putInt("charmedById", charmedById);
        sirenData.putInt("charmTime", charmTime);
        sirenData.putBoolean("isCharmed", isCharmed);

        tag.put("sirenData", sirenData);
    }

    public void deserialize(final CompoundTag tag) {
        CompoundTag sirenData = tag.getCompound("sirenData");

        isInitialized = false;

        charmedById = sirenData.getInt("charmedById");
        charmTime = sirenData.getInt("charmTime");
        isCharmed = sirenData.getBoolean("isCharmed");
    }

    private float updateRotation(float angle, float targetAngle, float maxIncrease) {
        float f = Mth.wrapDegrees(targetAngle - angle);

        if (f > maxIncrease) {
            f = maxIncrease;
        }

        if (f < -maxIncrease) {
            f = -maxIncrease;
        }

        return angle + f;
    }
}
