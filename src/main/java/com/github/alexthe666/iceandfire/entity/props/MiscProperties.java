package com.github.alexthe666.iceandfire.entity.props;

import com.github.alexthe666.citadel.Citadel;
import com.github.alexthe666.citadel.server.entity.CitadelEntityData;
import com.github.alexthe666.citadel.server.message.PropertiesMessage;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class MiscProperties {
    private static final String MISC_DATA = "MiscDataIaf";
    private static final String DISMOUNTED_DRAGON = "HasDismounted";
    private static final String IN_LOVE_TIME = "TicksInLove";
    private static final String LUNGE_TICKS = "LungeTicks";
    private static final String PREV_LUNGE_TICKS = "PrevLungeTicks";
    private static final String TARGETED_BY_SCEPTER_HOLDERS = "ScepterHolders";
    private static final String TARGETING_ENTITIES_WITH_SCEPTER = "ScepterTargets";
    private static final String SCEPTER_ENTITY_ID = "ScepterEntityId";
    private static final Random rand = new Random();

    // FIXME: All of these hashmap optimizations are temporary to resolve performance issues, ideally we create a different system
    private static HashMap<CompoundTag, Boolean> containsLoveData = new HashMap<>();

    private static CompoundTag getOrCreateMiscData(LivingEntity entity) {
        return getOrCreateMiscData(CitadelEntityData.getCitadelTag(entity));
    }

    private static CompoundTag getOrCreateMiscData(CompoundTag entityData) {
        if (entityData.contains(MISC_DATA, 10)) {
            return (CompoundTag) entityData.get(MISC_DATA);
        }
        return createDefaultData();
    }

    private static ListTag getOrCreateScepterTargetedBy(LivingEntity entity) {
        return getOrCreateScepterTargetedBy(CitadelEntityData.getCitadelTag(entity));
    }

    private static ListTag getOrCreateScepterTargetedBy(CompoundTag entityData) {
        CompoundTag miscData = getOrCreateMiscData(entityData);
        if (miscData.contains(TARGETED_BY_SCEPTER_HOLDERS, 9)) {
            return miscData.getList(TARGETED_BY_SCEPTER_HOLDERS, 10);
        }
        return new ListTag();
    }

    private static ListTag getOrCreateScepterTargets(CompoundTag entityData) {
        CompoundTag miscData = getOrCreateMiscData(entityData);
        if (miscData.contains(TARGETING_ENTITIES_WITH_SCEPTER, 9)) {
            return miscData.getList(TARGETING_ENTITIES_WITH_SCEPTER, 10);
        }
        return new ListTag();
    }

    private static CompoundTag createDefaultData() {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt(IN_LOVE_TIME, 0);
        nbt.putBoolean(DISMOUNTED_DRAGON, false);
        ListTag scepterHolders = new ListTag();
        nbt.put(TARGETED_BY_SCEPTER_HOLDERS, scepterHolders);
        ListTag scepterTargets = new ListTag();
        nbt.put(TARGETING_ENTITIES_WITH_SCEPTER, scepterTargets);
        return nbt;
    }

    public static boolean hasDismounted(LivingEntity entity) {
        CompoundTag nbt = getOrCreateMiscData(entity);
        if (nbt.contains(DISMOUNTED_DRAGON)) {
            return nbt.getBoolean(DISMOUNTED_DRAGON);
        }
        return false;
    }

    public static int getLoveTicks(LivingEntity entity) {

        CompoundTag citadelTag = CitadelEntityData.getCitadelTag(entity);
        // If we have the nbt data in the hashmap and we know it contains love data
        if (containsLoveData.containsKey(citadelTag) && containsLoveData.getOrDefault(citadelTag, false)) {
            CompoundTag miscData = (CompoundTag) citadelTag.get(MISC_DATA);
            if (miscData.contains(IN_LOVE_TIME)) {
                return miscData.getInt(IN_LOVE_TIME);
            }
            // Otherwise it means we have the nbt data in the hashmap but it doesn't contain love data
        } else if (containsLoveData.containsKey(citadelTag) ) {
            return 0;
            // If we don't have the nbt data in the hashmap
        } else if (citadelTag.contains(MISC_DATA, 10)) {
            CompoundTag miscData = (CompoundTag) citadelTag.get(MISC_DATA);
            if (miscData.contains(IN_LOVE_TIME)) {
                containsLoveData.put(citadelTag, true);
                return miscData.getInt(IN_LOVE_TIME);
            } else {
                containsLoveData.put(citadelTag, false);
            }
        }

        return 0;
    }

    public static int getLungeTicks(LivingEntity entity) {
        CompoundTag nbt = getOrCreateMiscData(entity);
        if (nbt.contains(LUNGE_TICKS)) {
            return nbt.getInt(LUNGE_TICKS);
        }
        return 0;
    }

    public static void setLoveTicks(LivingEntity entity, int duration) {
        CompoundTag entityData = CitadelEntityData.getOrCreateCitadelTag(entity);
        CompoundTag miscData = getOrCreateMiscData(entityData);
        miscData.putInt(IN_LOVE_TIME, duration);
        entityData.put(MISC_DATA, miscData);
        updateData(entity, entityData);
    }

    public static void setDismountedDragon(LivingEntity entity, boolean bool) {
        CompoundTag entityData = CitadelEntityData.getOrCreateCitadelTag(entity);
        CompoundTag miscData = getOrCreateMiscData(entityData);
        miscData.putBoolean(DISMOUNTED_DRAGON, bool);
        entityData.put(MISC_DATA, miscData);
        updateData(entity, entityData);
    }

    public static void setLungeTicks(LivingEntity entity, int ticks) {
        CompoundTag entityData = CitadelEntityData.getOrCreateCitadelTag(entity);
        CompoundTag miscData = getOrCreateMiscData(entityData);
        miscData.putInt(LUNGE_TICKS, ticks);
        entityData.put(MISC_DATA, miscData);
        updateData(entity, entityData);
    }

    public static void addScepterTargetData(LivingEntity caster, LivingEntity target) {
        if (isTargetedBy(caster, target) || isTargeting(caster, target))
            return;
        addTargetedBy(caster, target);
        addTargeting(caster, target);
    }

    public static void addTargetedBy(LivingEntity caster, LivingEntity target) {
        if (isTargetedBy(caster, target))
            return;
        CompoundTag targetData = CitadelEntityData.getOrCreateCitadelTag(target);
        CompoundTag miscData = getOrCreateMiscData(targetData);
        ListTag scepterTargetData = getOrCreateScepterTargetedBy(targetData);
        CompoundTag targetCasterData = new CompoundTag();
        targetCasterData.putInt(SCEPTER_ENTITY_ID, caster.getId());
        scepterTargetData.add(targetCasterData);
        miscData.put(TARGETED_BY_SCEPTER_HOLDERS, scepterTargetData);
        targetData.put(MISC_DATA, miscData);
        updateData(target, targetData);
    }

    public static void addTargeting(LivingEntity caster, LivingEntity target) {
        if (isTargeting(caster, target))
            return;
        CompoundTag casterData = CitadelEntityData.getOrCreateCitadelTag(caster);
        CompoundTag miscData = getOrCreateMiscData(casterData);
        ListTag scepterCasterData = getOrCreateScepterTargets(casterData);
        CompoundTag casterTargetData = new CompoundTag();
        casterTargetData.putInt(SCEPTER_ENTITY_ID, target.getId());
        scepterCasterData.add(casterTargetData);
        miscData.put(TARGETING_ENTITIES_WITH_SCEPTER, scepterCasterData);
        casterData.put(MISC_DATA, miscData);
        updateData(caster, casterData);
    }

    public static boolean isTargetedBy(LivingEntity caster, LivingEntity target) {
        CompoundTag targetData = CitadelEntityData.getOrCreateCitadelTag(target);
        ListTag scepterData = getOrCreateScepterTargetedBy(targetData);
        int entityId = caster.getId();
        for (Tag scepterDatum : scepterData) {
            CompoundTag targetedBy = (CompoundTag) scepterDatum;
            if (!targetedBy.contains(SCEPTER_ENTITY_ID))
                continue;
            int targetedById = targetedBy.getInt(SCEPTER_ENTITY_ID);
            if (entityId == targetedById)
                return true;
        }
        return false;
    }

    public static boolean isTargeting(LivingEntity caster, LivingEntity target) {
        CompoundTag casterData = CitadelEntityData.getOrCreateCitadelTag(caster);
        ListTag scepterData = getOrCreateScepterTargets(casterData);
        int entityId = target.getId();
        for (Tag scepterDatum : scepterData) {
            CompoundTag targetedBy = (CompoundTag) scepterDatum;
            if (!targetedBy.contains(SCEPTER_ENTITY_ID))
                continue;
            int targetedById = targetedBy.getInt(SCEPTER_ENTITY_ID);
            if (entityId == targetedById)
                return true;
        }
        return false;
    }

    public static List<LivingEntity> getTargetedBy(LivingEntity target) {
        CompoundTag targetData = CitadelEntityData.getOrCreateCitadelTag(target);
        ListTag scepterData = getOrCreateScepterTargetedBy(targetData);
        List<LivingEntity> targetedByEntities = new ArrayList<>();
        for (Tag scepterDatum : scepterData) {
            CompoundTag targetedBy = (CompoundTag) scepterDatum;
            if (!targetedBy.contains(SCEPTER_ENTITY_ID))
                continue;
            int targetedById = targetedBy.getInt(SCEPTER_ENTITY_ID);
            Entity entity = target.level().getEntity(targetedById);
            if (entity instanceof LivingEntity)
                targetedByEntities.add((LivingEntity) entity);
        }
        return targetedByEntities;
    }

    public static List<LivingEntity> getTargeting(LivingEntity caster) {
        CompoundTag casterData = CitadelEntityData.getOrCreateCitadelTag(caster);
        ListTag scepterData = getOrCreateScepterTargets(casterData);
        List<LivingEntity> targetingEntities = new ArrayList<>();
        for (Tag scepterDatum : scepterData) {
            CompoundTag targetedBy = (CompoundTag) scepterDatum;
            if (!targetedBy.contains(SCEPTER_ENTITY_ID))
                continue;
            int targetedById = targetedBy.getInt(SCEPTER_ENTITY_ID);
            Entity entity = caster.level().getEntity(targetedById);
            if (entity instanceof LivingEntity)
                targetingEntities.add((LivingEntity) entity);
        }
        return targetingEntities;
    }

    public static void removeTargetedBy(LivingEntity caster, LivingEntity target) {
        CompoundTag targetData = CitadelEntityData.getOrCreateCitadelTag(target);
        CompoundTag miscData = getOrCreateMiscData(targetData);
        ListTag scepterData = getOrCreateScepterTargetedBy(targetData);
        ListTag updatedScepterData = new ListTag();
        int entityId = caster.getId();
        for (Tag scepterDatum : scepterData) {
            CompoundTag targetedBy = (CompoundTag) scepterDatum;
            if (!targetedBy.contains(SCEPTER_ENTITY_ID))
                continue;
            int targetedById = targetedBy.getInt(SCEPTER_ENTITY_ID);
            if (entityId != targetedById)
                updatedScepterData.add(targetedBy);
        }
        miscData.put(TARGETED_BY_SCEPTER_HOLDERS, updatedScepterData);
        targetData.put(MISC_DATA, miscData);
        updateData(target, targetData);
    }

    public static void removeTargets(LivingEntity caster) {
        CompoundTag casterData = CitadelEntityData.getOrCreateCitadelTag(caster);
        CompoundTag miscData = getOrCreateMiscData(casterData);
        miscData.put(TARGETED_BY_SCEPTER_HOLDERS, new ListTag());
        casterData.put(MISC_DATA, miscData);
        updateData(caster, casterData);
    }

    public static void removeTarget(LivingEntity caster, LivingEntity target) {
        CompoundTag casterData = CitadelEntityData.getOrCreateCitadelTag(caster);
        CompoundTag miscData = getOrCreateMiscData(casterData);
        ListTag scepterData = getOrCreateScepterTargets(casterData);
        ListTag updatedScepterData = new ListTag();
        int entityId = target.getId();
        for (Tag scepterDatum : scepterData) {
            CompoundTag targetedBy = (CompoundTag) scepterDatum;
            if (!targetedBy.contains(SCEPTER_ENTITY_ID))
                continue;
            int targetedById = targetedBy.getInt(SCEPTER_ENTITY_ID);
            if (targetedById != entityId)
                updatedScepterData.add(targetedBy);
        }
        miscData.put(TARGETING_ENTITIES_WITH_SCEPTER, updatedScepterData);
        casterData.put(MISC_DATA, miscData);
        updateData(caster, casterData);
    }

    public static void updateData(LivingEntity entity) {
        updateData(entity, CitadelEntityData.getCitadelTag(entity));
    }

    private static void updateData(LivingEntity entity, CompoundTag nbt) {
        CitadelEntityData.setCitadelTag(entity, nbt);
        if (!entity.level().isClientSide()) {
            Citadel.sendMSGToAll(new PropertiesMessage("CitadelPatreonConfig", nbt, entity.getId()));
        }
    }

    private static void createLoveParticles(LivingEntity entity) {
        if (rand.nextInt(7) == 0) {
            for (int i = 0; i < 5; i++) {
                entity.level().addParticle(ParticleTypes.HEART,
                        entity.getX() + ((rand.nextDouble() - 0.5D) * 3),
                        entity.getY() + ((rand.nextDouble() - 0.5D) * 3),
                        entity.getZ() + ((rand.nextDouble() - 0.5D) * 3), 0, 0, 0);
            }
        }
    }

    public static void tickLove(LivingEntity entity) {
        setLoveTicks(entity, getLoveTicks(entity) - 1);
        if (entity instanceof Mob) {
            ((Mob) entity).setTarget(null);
        }
        createLoveParticles(entity);
    }

}
