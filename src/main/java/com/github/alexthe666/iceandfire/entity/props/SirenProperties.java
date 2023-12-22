package com.github.alexthe666.iceandfire.entity.props;

import com.github.alexthe666.citadel.Citadel;
import com.github.alexthe666.citadel.server.entity.CitadelEntityData;
import com.github.alexthe666.citadel.server.message.PropertiesMessage;
import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.entity.EntitySiren;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Random;

public class SirenProperties {
    private static final String SIREN_DATA = "SirenDataIaf";
    private static final String SIREN_CHARMED = "IsCharmed";
    private static final String SIREN_ID = "CharmedBy";
    private static final String SIREN_TIME = "CharmeTime";
    private static final Random rand = new Random();

    // FIXME: All of these hashmap optimizations are temporary to resolve performance issues, ideally we create a different system
    private static HashMap<CompoundTag, Boolean> containsCharmedData = new HashMap<>();

    private static CompoundTag getOrCreateCharmData(LivingEntity entity) {
        return getOrCreateCharmData(CitadelEntityData.getCitadelTag(entity));
    }

    private static CompoundTag getOrCreateCharmData(CompoundTag entityData) {
        if (containsCharmedData.containsKey(entityData) && containsCharmedData.get(entityData) && entityData.contains(SIREN_DATA, 10)) {
            return (CompoundTag) entityData.get(SIREN_DATA);
        } else if (entityData.contains(SIREN_DATA, 10)) {
            containsCharmedData.put(entityData, true);
            return (CompoundTag) entityData.get(SIREN_DATA);
        } else {
            containsCharmedData.put(entityData, false);
            return createDefaultData();
        }
    }

    private static void clearCharmedStatus(LivingEntity entity) {
        CompoundTag charmData = getOrCreateCharmData(entity);
        clearCharmedStatus(charmData);
        updateCharmData(entity, charmData);
    }

    private static CompoundTag clearCharmedStatus(CompoundTag nbt) {
        nbt.putInt(SIREN_TIME, 0);
        nbt.putBoolean(SIREN_CHARMED, false);
        nbt.putInt(SIREN_ID, -1);
        return nbt;
    }

    private static CompoundTag createDefaultData() {
        CompoundTag nbt = new CompoundTag();
        return clearCharmedStatus(nbt);
    }

    public static void updateData(LivingEntity entity) {
        updateData(entity, CitadelEntityData.getCitadelTag(entity));
    }

    private static void updateData(LivingEntity entity, CompoundTag nbt) {
        CitadelEntityData.setCitadelTag(entity, nbt);
        if (!entity.level.isClientSide()) {
            Citadel.sendMSGToAll(new PropertiesMessage("CitadelPatreonConfig", nbt, entity.getId()));
        }
    }

    private static void updateCharmData(LivingEntity entity, CompoundTag nbt) {
        CompoundTag entityData = CitadelEntityData.getOrCreateCitadelTag(entity);
        entityData.put(SIREN_DATA, nbt);
        CitadelEntityData.setCitadelTag(entity, entityData);
        if (!entity.level.isClientSide()) {
            Citadel.sendMSGToAll(new PropertiesMessage("CitadelPatreonConfig", entityData, entity.getId()));
        }
    }

    private static int getSingTime(LivingEntity entity) {
        CompoundTag nbt = getOrCreateCharmData(entity);
        if (nbt.contains(SIREN_TIME)) {
            return nbt.getInt(SIREN_TIME);
        }
        return 0;
    }

    public static void setCharmedBy(LivingEntity entity, LivingEntity charmedBy) {
        CompoundTag nbt = getOrCreateCharmData(entity);
        nbt.putInt(SIREN_ID, charmedBy.getId());
        nbt.putBoolean(SIREN_CHARMED, true);
        updateCharmData(entity, nbt);
    }

    private static int getCharmedBy(LivingEntity entity) {
        CompoundTag nbt = getOrCreateCharmData(entity);
        if (nbt.contains(SIREN_ID)) {
            return nbt.getInt(SIREN_ID);
        }
        return -1;
    }

    @Nullable
    public static EntitySiren getSiren(LivingEntity entity) {
        Entity siren = entity.level.getEntity(getCharmedBy(entity));
        if (siren instanceof EntitySiren) {
            return (EntitySiren) siren;
        }
        return null;
    }

    public static boolean isCharmed(LivingEntity entity) {
        CompoundTag nbt = getOrCreateCharmData(entity);
        if (!nbt.contains(SIREN_CHARMED)) {
            nbt = createDefaultData();
            updateCharmData(entity, nbt);
        }
        return nbt.getBoolean(SIREN_CHARMED);
    }

    public static void tickCharmedEntity(LivingEntity entity) {
        EntitySiren siren = getSiren(entity);
        if (siren != null && siren.isActuallySinging()) {
            if (EntitySiren.isWearingEarplugs(entity) || getSingTime(entity) > IafConfig.sirenMaxSingTime) {
                clearCharmedStatus(entity);
                siren.singCooldown = IafConfig.sirenTimeBetweenSongs;
            } else {
                if (!siren.isAlive() || entity.distanceTo(siren) > EntitySiren.SEARCH_RANGE * 2 || entity instanceof Player && ((Player) entity).isCreative()) {
                    clearCharmedStatus(entity);
                    return;
                }
                if (entity.distanceTo(siren) < 5D) {
                    clearCharmedStatus(entity);
                    siren.singCooldown = IafConfig.sirenTimeBetweenSongs;
                    siren.setSinging(false);
                    siren.setTarget(entity);
                    siren.setAggressive(true);
                    siren.triggerOtherSirens(entity);
                    return;
                }
                CompoundTag sirenData = getOrCreateCharmData(entity);
                sirenData.putBoolean(SIREN_CHARMED, true);
                sirenData.putInt(SIREN_TIME, getSingTime(entity));
                updateCharmData(entity, sirenData);

                if (rand.nextInt(7) == 0) {
                    for (int i = 0; i < 5; i++) {
                        entity.level.addParticle(ParticleTypes.HEART,
                            entity.getX() + ((rand.nextDouble() - 0.5D) * 3),
                            entity.getY() + ((rand.nextDouble() - 0.5D) * 3),
                            entity.getZ() + ((rand.nextDouble() - 0.5D) * 3),
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
        }
    }

    public static float updateRotation(float angle, float targetAngle, float maxIncrease) {
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
