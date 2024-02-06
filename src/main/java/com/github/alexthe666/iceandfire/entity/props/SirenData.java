package com.github.alexthe666.iceandfire.entity.props;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.entity.EntitySiren;
import com.github.alexthe666.iceandfire.entity.util.IHearsSiren;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;


public class SirenData {
    public @Nullable EntitySiren charmedBy;
    public int charmTime; // FIXME :: when is this ever increased
    public boolean isCharmed;

    private int charmedById;
    private boolean isInitialized;
    private boolean triggerClientUpdate;

    public void tickCharmed(final LivingEntity holder) {
        if (!(holder instanceof Player || holder instanceof AbstractVillager || holder instanceof IHearsSiren)) {
            return;
        }

        if (!isInitialized) {
            Entity entity = holder.getLevel().getEntity(charmedById);

            if (entity instanceof EntitySiren siren) {
                charmedBy = siren;
            }

            isInitialized = true;
        }

        if (charmedBy == null) {
            return;
        }

        if (charmedBy.isActuallySinging()) {
            if (EntitySiren.isWearingEarplugs(holder) || charmTime > IafConfig.sirenMaxSingTime) {
                charmedBy.singCooldown = IafConfig.sirenTimeBetweenSongs;
                clearCharm();
                return;
            }

            if (!charmedBy.isAlive() || holder.distanceTo(charmedBy) > EntitySiren.SEARCH_RANGE * 2 || holder instanceof Player player && (player.isCreative() || player.isSpectator())) {
                clearCharm();
                return;
            }

            if (holder.distanceTo(charmedBy) < 5) {
                charmedBy.singCooldown = IafConfig.sirenTimeBetweenSongs;
                charmedBy.setSinging(false);
                charmedBy.setTarget(holder);
                charmedBy.setAggressive(true);
                charmedBy.triggerOtherSirens(holder);
                clearCharm();
                return;
            }

            isCharmed = true;

            if (holder.getRandom().nextInt(7) == 0) {
                for (int i = 0; i < 5; i++) {
                    holder.level.addParticle(ParticleTypes.HEART,
                            holder.getX() + ((holder.getRandom().nextDouble() - 0.5D) * 3),
                            holder.getY() + ((holder.getRandom().nextDouble() - 0.5D) * 3),
                            holder.getZ() + ((holder.getRandom().nextDouble() - 0.5D) * 3),
                            0, 0, 0);
                }
            }

            if (holder.horizontalCollision) {
                holder.setJumping(true);
            }

            double motionXAdd = (Math.signum(charmedBy.getX() - holder.getX()) * 0.5D - holder.getDeltaMovement().x) * 0.100000000372529;
            double motionYAdd = (Math.signum(charmedBy.getY() - holder.getY() + 1) * 0.5D - holder.getDeltaMovement().y) * 0.100000000372529;
            double motionZAdd = (Math.signum(charmedBy.getZ() - holder.getZ()) * 0.5D - holder.getDeltaMovement().z) * 0.100000000372529;

            holder.setDeltaMovement(holder.getDeltaMovement().add(motionXAdd, motionYAdd, motionZAdd));

            if (holder.isPassenger()) {
                holder.stopRiding();
            }

            if (!(holder instanceof Player)) {
                double x = charmedBy.getX() - holder.getX();
                double y = charmedBy.getY() - 1 - holder.getY();
                double z = charmedBy.getZ() - holder.getZ();
                double radius = Math.sqrt(x * x + z * z);
                float xRot = (float) (-(Mth.atan2(y, radius) * (180D / Math.PI)));
                float yRot = (float) (Mth.atan2(z, x) * (180D / Math.PI)) - 90.0F;
                holder.setXRot(updateRotation(holder.getXRot(), xRot, 30F));
                holder.setYRot(updateRotation(holder.getYRot(), yRot, 30F));
            }
        }
    }

    public void setCharmed(final Entity entity) {
        if (!(entity instanceof EntitySiren siren)) {
            return;
        }

        charmedById = siren.getId();
        charmedBy = siren;
        isCharmed = true;
        triggerClientUpdate = true;
    }

    public void clearCharm() {
        charmTime = 0;
        isCharmed = false;
        charmedBy = null;
        charmedById = -1;
        triggerClientUpdate = true;
    }

    public void serialize(final CompoundTag tag) {
        CompoundTag sirenData = new CompoundTag();
        sirenData.putInt("charmedById", charmedById); // FIXME :: store uuid for re-join
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

    public boolean doesClientNeedUpdate() {
        if (triggerClientUpdate) {
            triggerClientUpdate = false;
            return true;
        }

        return false;
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
