package com.github.alexthe666.iceandfire.entity.props;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;


public class MiscData {
    public int loveTicks;
    public int lungeTicks;
    public boolean hasDismounted;

    // Transient data
    public final List<LivingEntity> targetedByScepter = new ArrayList<>();
    private final List<Integer> targetedByScepterIds = new ArrayList<>();

    private boolean isInitialized;
    private boolean triggerClientUpdate;

    public void tickMisc(final LivingEntity entity) {
        if (!isInitialized) {
            initialize(entity.getLevel());
        }

        if (loveTicks > 0) {
            loveTicks--;

            if (loveTicks == 0) {
                triggerClientUpdate = true;
                return;
            }

            if (entity instanceof Mob mob) {
                mob.setTarget(null);
            }

            createLoveParticles(entity);
        }
    }

    public void addScepterTarget(final LivingEntity target) {
        if (targetedByScepter.contains(target)) {
            return;
        }

        targetedByScepter.add(target);
        triggerClientUpdate = true;
    }

    public void removeScepterTarget(final LivingEntity target) {
        targetedByScepter.remove(target);
        triggerClientUpdate = true;
    }

    public void setLoveTicks(int loveTicks) {
        this.loveTicks = loveTicks;
        triggerClientUpdate = true;
    }

    public void setLungeTicks(int lungeTicks) {
        this.lungeTicks = lungeTicks;
        triggerClientUpdate = true;
    }

    public void setDismounted(boolean hasDismounted) {
        this.hasDismounted = hasDismounted;
        triggerClientUpdate = true;
    }

    public void serialize(final CompoundTag tag) {
        CompoundTag miscData = new CompoundTag();
        miscData.putInt("loveTicks", loveTicks);
        miscData.putInt("lungeTicks", lungeTicks);
        miscData.putBoolean("hasDismounted", hasDismounted);
        int[] ids = new int[targetedByScepterIds.size()];

        for (int i = 0; i < targetedByScepter.size(); i++) {
            ids[i] = targetedByScepter.get(i).getId();
        }

        tag.putIntArray("targetedByScepterIds", ids);
        tag.put("miscData", miscData);
    }

    public void deserialize(final CompoundTag tag) {
        CompoundTag miscData = tag.getCompound("miscData");
        loveTicks = miscData.getInt("loveTicks");
        lungeTicks = miscData.getInt("lungeTicks");
        hasDismounted = miscData.getBoolean("hasDismounted");
        int[] loadedChainedToIds = miscData.getIntArray("targetedByScepterIds");

        isInitialized = false;
        targetedByScepterIds.clear();

        for (int loadedChainedToId : loadedChainedToIds) {
            targetedByScepterIds.add(loadedChainedToId);
        }
    }

    public boolean doesClientNeedUpdate() {
        if (triggerClientUpdate) {
            triggerClientUpdate = false;
            return true;
        }

        return false;
    }

    private void createLoveParticles(final LivingEntity entity) {
        if (entity.getRandom().nextInt(7) == 0) {
            for (int i = 0; i < 5; i++) {
                entity.getLevel().addParticle(ParticleTypes.HEART,
                        entity.getX() + ((entity.getRandom().nextDouble() - 0.5D) * 3),
                        entity.getY() + ((entity.getRandom().nextDouble() - 0.5D) * 3),
                        entity.getZ() + ((entity.getRandom().nextDouble() - 0.5D) * 3), 0, 0, 0);
            }
        }
    }

    private void initialize(final Level level) {
        targetedByScepter.clear();

        for (int id : targetedByScepterIds) {
            Entity entity = level.getEntity(id);

            if (entity instanceof LivingEntity livingEntity) {
                targetedByScepter.add(livingEntity);
            }
        }

        targetedByScepterIds.clear();
        isInitialized = true;
    }
}
