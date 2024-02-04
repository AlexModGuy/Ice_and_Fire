package com.github.alexthe666.iceandfire.entity.props;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.entity.EntitySiren;
import com.github.alexthe666.iceandfire.entity.util.IHearsSiren;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;


public class SirenData {
    public @Nullable EntitySiren charmedBy;
    public int charmTime;
    public boolean isCharmed;

    private @Nullable UUID charmedByUUID;
    private int charmedById;
    private boolean isInitialized;
    private boolean triggerClientUpdate;

    public void tickCharmed(final LivingEntity holder) {
        if (!(holder instanceof Player || holder instanceof AbstractVillager || holder instanceof IHearsSiren)) {
            return;
        }

        if (!isInitialized) {
            initialize(holder.level());
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
            charmTime++;
            if (holder.getRandom().nextInt(7) == 0) {
                for (int i = 0; i < 5; i++) {
                    holder.level().addParticle(ParticleTypes.HEART,
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

        charmedBy = siren;
        isCharmed = true;
        triggerClientUpdate = true;
    }

    public void clearCharm() {
        charmTime = 0;
        isCharmed = false;
        charmedBy = null;
        triggerClientUpdate = true;
    }

    public void serialize(final CompoundTag tag) {
        CompoundTag sirenData = new CompoundTag();

        if (charmedBy != null) {
            sirenData.put("charmedByUUID", NbtUtils.createUUID(charmedBy.getUUID()));
            sirenData.putInt("charmedById", charmedBy.getId());
        } else {
            sirenData.putInt("charmedById", -1);
        }

        sirenData.putInt("charmTime", charmTime);
        sirenData.putBoolean("isCharmed", isCharmed);

        tag.put("sirenData", sirenData);
    }

    public void deserialize(final CompoundTag tag) {
        CompoundTag sirenData = tag.getCompound("sirenData");
        Tag uuidTag = sirenData.get("charmedByUUID");

        if (uuidTag != null) {
            charmedByUUID = NbtUtils.loadUUID(uuidTag);
        }

        charmedById = sirenData.getInt("charmedById");
        charmTime = sirenData.getInt("charmTime");
        isCharmed = sirenData.getBoolean("isCharmed");
        isInitialized = false;
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

    private void initialize(final Level level) {
        charmedBy = null;

        // Make sure server gets the new entity ids on re-join and syncs it to the client
        if (charmedByUUID != null && level instanceof ServerLevel serverLevel) {
            Entity entity = serverLevel.getEntity(charmedByUUID);

            if (entity instanceof EntitySiren siren) {
                triggerClientUpdate = true;
                charmedByUUID = null;
                charmedBy = siren;
            }
        } else if (charmedById != -1) {
            Entity entity = level.getEntity(charmedById);

            if (entity instanceof EntitySiren siren) {
                charmedBy = siren;
            }
        }

        isInitialized = true;
    }
}
