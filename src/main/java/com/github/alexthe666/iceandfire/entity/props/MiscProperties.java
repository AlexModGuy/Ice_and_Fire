package com.github.alexthe666.iceandfire.entity.props;

import com.github.alexthe666.citadel.Citadel;
import com.github.alexthe666.citadel.server.entity.CitadelEntityData;
import com.github.alexthe666.citadel.server.message.PropertiesMessage;
import com.github.alexthe666.iceandfire.entity.EntityGorgon;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.particles.ParticleTypes;

import java.util.ArrayList;
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

    private static CompoundNBT getOrCreateMiscData(LivingEntity entity) {
        return getOrCreateMiscData(CitadelEntityData.getCitadelTag(entity));
    }

    private static CompoundNBT getOrCreateMiscData(CompoundNBT entityData) {
        if (entityData.contains(MISC_DATA, 10)) {
            return (CompoundNBT) entityData.get(MISC_DATA);
        }
        return createDefaultData();
    }

    private static ListNBT getOrCreateScepterTargetedBy(LivingEntity entity) {
        return getOrCreateScepterTargetedBy(CitadelEntityData.getCitadelTag(entity));
    }

    private static ListNBT getOrCreateScepterTargetedBy(CompoundNBT entityData) {
        CompoundNBT miscData = getOrCreateMiscData(entityData);
        if (miscData.contains(TARGETED_BY_SCEPTER_HOLDERS, 9)) {
            return miscData.getList(TARGETED_BY_SCEPTER_HOLDERS, 10);
        }
        return new ListNBT();
    }

    private static ListNBT getOrCreateScepterTargets(CompoundNBT entityData) {
        CompoundNBT miscData = getOrCreateMiscData(entityData);
        if (miscData.contains(TARGETING_ENTITIES_WITH_SCEPTER, 9)) {
            return miscData.getList(TARGETING_ENTITIES_WITH_SCEPTER, 10);
        }
        return new ListNBT();
    }

    private static CompoundNBT createDefaultData() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putInt(IN_LOVE_TIME, 0);
        nbt.putBoolean(DISMOUNTED_DRAGON, false);
        ListNBT scepterHolders = new ListNBT();
        nbt.put(TARGETED_BY_SCEPTER_HOLDERS, scepterHolders);
        ListNBT scepterTargets = new ListNBT();
        nbt.put(TARGETING_ENTITIES_WITH_SCEPTER, scepterTargets);
        return nbt;
    }

    public static boolean hasDismounted(LivingEntity entity) {
        CompoundNBT nbt = getOrCreateMiscData(entity);
        if (nbt.contains(DISMOUNTED_DRAGON)) {
            return nbt.getBoolean(DISMOUNTED_DRAGON);
        }
        return false;
    }

    public static int getLoveTicks(LivingEntity entity) {
        CompoundNBT nbt = getOrCreateMiscData(entity);
        if (nbt.contains(IN_LOVE_TIME)) {
            return nbt.getInt(IN_LOVE_TIME);
        }
        return 0;
    }

    public static int getLungeTicks(LivingEntity entity) {
        CompoundNBT nbt = getOrCreateMiscData(entity);
        if (nbt.contains(LUNGE_TICKS)) {
            return nbt.getInt(LUNGE_TICKS);
        }
        return 0;
    }

    public static void setLoveTicks(LivingEntity entity, int duration) {
        CompoundNBT entityData = CitadelEntityData.getOrCreateCitadelTag(entity);
        CompoundNBT miscData = getOrCreateMiscData(entityData);
        miscData.putInt(IN_LOVE_TIME, duration);
        entityData.put(MISC_DATA, miscData);
        updateData(entity, entityData);
    }

    public static void setDismountedDragon(LivingEntity entity, boolean bool) {
        CompoundNBT entityData = CitadelEntityData.getOrCreateCitadelTag(entity);
        CompoundNBT miscData = getOrCreateMiscData(entityData);
        miscData.putBoolean(DISMOUNTED_DRAGON, bool);
        entityData.put(MISC_DATA, miscData);
        updateData(entity, entityData);
    }

    public static void setLungeTicks(LivingEntity entity, int ticks) {
        CompoundNBT entityData = CitadelEntityData.getOrCreateCitadelTag(entity);
        CompoundNBT miscData = getOrCreateMiscData(entityData);
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
        CompoundNBT targetData = CitadelEntityData.getOrCreateCitadelTag(target);
        CompoundNBT miscData = getOrCreateMiscData(targetData);
        ListNBT scepterTargetData = getOrCreateScepterTargetedBy(targetData);
        CompoundNBT targetCasterData = new CompoundNBT();
        targetCasterData.putInt(SCEPTER_ENTITY_ID, caster.getEntityId());
        scepterTargetData.add(targetCasterData);
        miscData.put(TARGETED_BY_SCEPTER_HOLDERS, scepterTargetData);
        targetData.put(MISC_DATA, miscData);
        updateData(target, targetData);
    }

    public static void addTargeting(LivingEntity caster, LivingEntity target) {
        if (isTargeting(caster, target))
            return;
        CompoundNBT casterData = CitadelEntityData.getOrCreateCitadelTag(caster);
        CompoundNBT miscData = getOrCreateMiscData(casterData);
        ListNBT scepterCasterData = getOrCreateScepterTargets(casterData);
        CompoundNBT casterTargetData = new CompoundNBT();
        casterTargetData.putInt(SCEPTER_ENTITY_ID, target.getEntityId());
        scepterCasterData.add(casterTargetData);
        miscData.put(TARGETING_ENTITIES_WITH_SCEPTER, scepterCasterData);
        casterData.put(MISC_DATA, miscData);
        updateData(caster, casterData);
    }

    public static boolean isTargetedBy(LivingEntity caster, LivingEntity target) {
        CompoundNBT targetData = CitadelEntityData.getOrCreateCitadelTag(target);
        ListNBT scepterData = getOrCreateScepterTargetedBy(targetData);
        int entityId = caster.getEntityId();
        for (INBT scepterDatum : scepterData) {
            CompoundNBT targetedBy = (CompoundNBT) scepterDatum;
            if (!targetedBy.contains(SCEPTER_ENTITY_ID))
                continue;
            int targetedById = targetedBy.getInt(SCEPTER_ENTITY_ID);
            if (entityId == targetedById)
                return true;
        }
        return false;
    }

    public static boolean isTargeting(LivingEntity caster, LivingEntity target) {
        CompoundNBT casterData = CitadelEntityData.getOrCreateCitadelTag(caster);
        ListNBT scepterData = getOrCreateScepterTargets(casterData);
        int entityId = target.getEntityId();
        for (INBT scepterDatum : scepterData) {
            CompoundNBT targetedBy = (CompoundNBT) scepterDatum;
            if (!targetedBy.contains(SCEPTER_ENTITY_ID))
                continue;
            int targetedById = targetedBy.getInt(SCEPTER_ENTITY_ID);
            if (entityId == targetedById)
                return true;
        }
        return false;
    }

    public static List<LivingEntity> getTargetedBy(LivingEntity target) {
        CompoundNBT targetData = CitadelEntityData.getOrCreateCitadelTag(target);
        ListNBT scepterData = getOrCreateScepterTargetedBy(targetData);
        List<LivingEntity> targetedByEntities = new ArrayList<>();
        for (INBT scepterDatum : scepterData) {
            CompoundNBT targetedBy = (CompoundNBT) scepterDatum;
            if (!targetedBy.contains(SCEPTER_ENTITY_ID))
                continue;
            int targetedById = targetedBy.getInt(SCEPTER_ENTITY_ID);
            Entity entity = target.world.getEntityByID(targetedById);
            if (entity instanceof LivingEntity)
                targetedByEntities.add((LivingEntity) entity);
        }
        return targetedByEntities;
    }

    public static List<LivingEntity> getTargeting(LivingEntity caster) {
        CompoundNBT casterData = CitadelEntityData.getOrCreateCitadelTag(caster);
        ListNBT scepterData = getOrCreateScepterTargets(casterData);
        List<LivingEntity> targetingEntities = new ArrayList<>();
        for (INBT scepterDatum : scepterData) {
            CompoundNBT targetedBy = (CompoundNBT) scepterDatum;
            if (!targetedBy.contains(SCEPTER_ENTITY_ID))
                continue;
            int targetedById = targetedBy.getInt(SCEPTER_ENTITY_ID);
            Entity entity = caster.world.getEntityByID(targetedById);
            if (entity instanceof LivingEntity)
                targetingEntities.add((LivingEntity) entity);
        }
        return targetingEntities;
    }

    public static void removeTargetedBy(LivingEntity caster, LivingEntity target) {
        CompoundNBT targetData = CitadelEntityData.getOrCreateCitadelTag(target);
        CompoundNBT miscData = getOrCreateMiscData(targetData);
        ListNBT scepterData = getOrCreateScepterTargetedBy(targetData);
        ListNBT updatedScepterData = new ListNBT();
        int entityId = caster.getEntityId();
        for (INBT scepterDatum : scepterData) {
            CompoundNBT targetedBy = (CompoundNBT) scepterDatum;
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
        CompoundNBT casterData = CitadelEntityData.getOrCreateCitadelTag(caster);
        CompoundNBT miscData = getOrCreateMiscData(casterData);
        miscData.put(TARGETED_BY_SCEPTER_HOLDERS, new ListNBT());
        casterData.put(MISC_DATA, miscData);
        updateData(caster, casterData);
    }

    public static void removeTarget(LivingEntity caster, LivingEntity target) {
        CompoundNBT casterData = CitadelEntityData.getOrCreateCitadelTag(caster);
        CompoundNBT miscData = getOrCreateMiscData(casterData);
        ListNBT scepterData = getOrCreateScepterTargets(casterData);
        ListNBT updatedScepterData = new ListNBT();
        int entityId = target.getEntityId();
        for (INBT scepterDatum : scepterData) {
            CompoundNBT targetedBy = (CompoundNBT) scepterDatum;
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

    private static void updateData(LivingEntity entity, CompoundNBT nbt) {
        CitadelEntityData.setCitadelTag(entity, nbt);
        if (!entity.world.isRemote()) {
            Citadel.sendMSGToAll(new PropertiesMessage("CitadelPatreonConfig", nbt, entity.getEntityId()));
        }
    }

    private static void createLoveParticles(LivingEntity entity) {
        if (rand.nextInt(7) == 0) {
            for (int i = 0; i < 5; i++) {
                entity.world.addParticle(ParticleTypes.HEART,
                    entity.getPosX() + ((rand.nextDouble() - 0.5D) * 3),
                    entity.getPosY() + ((rand.nextDouble() - 0.5D) * 3),
                    entity.getPosZ() + ((rand.nextDouble() - 0.5D) * 3), 0, 0, 0);
            }
        }
    }

    public static void tickLove(LivingEntity entity) {
        setLoveTicks(entity, getLoveTicks(entity) - 1);
        if (entity instanceof MobEntity) {
            ((MobEntity) entity).setAttackTarget(null);
        }
        createLoveParticles(entity);
    }

}
