package com.github.alexthe666.iceandfire.entity.props;

import com.github.alexthe666.citadel.Citadel;
import com.github.alexthe666.citadel.server.entity.CitadelEntityData;
import com.github.alexthe666.citadel.server.message.PropertiesMessage;
import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.entity.EntityIceDragon;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundEvents;

import java.util.Random;

public class FrozenProperties {
    private static final String FROZEN_DATA = "FrozenDataIaf";
    private static final String FROZEN_BOOL = "IsFrozen";
    private static final String FROZEN_TIME = "TicksUntilUnfrozen";
    private static final Random rand = new Random();

    private static CompoundNBT getOrCreateFrozenData(LivingEntity entity) {
        return getOrCreateFrozenData(CitadelEntityData.getCitadelTag(entity));
    }

    private static CompoundNBT getOrCreateFrozenData(CompoundNBT entityData) {
        if (entityData.contains(FROZEN_DATA, 10)) {
            return (CompoundNBT) entityData.get(FROZEN_DATA);
        } else return createDefaultData();
    }

    private static CompoundNBT createDefaultData() {
        CompoundNBT nbt = new CompoundNBT();
        return clearFrozenStatus(nbt);
    }

    private static CompoundNBT clearFrozenStatus(CompoundNBT nbt, LivingEntity entity, boolean breakIce) {
        if (breakIce) {
            for (int i = 0; i < 15; i++) {
                entity.level.addParticle(
                    new BlockParticleData(ParticleTypes.BLOCK,
                        IafBlockRegistry.DRAGON_ICE.defaultBlockState()),
                    entity.getX() + ((rand.nextDouble() - 0.5D) * entity.getBbWidth()),
                    entity.getY() + ((rand.nextDouble()) * entity.getBbHeight()),
                    entity.getZ() + ((rand.nextDouble() - 0.5D) * entity.getBbWidth()),
                    0, 0, 0);
            }
            entity.playSound(SoundEvents.GLASS_BREAK, 3, 1);
        }
        return clearFrozenStatus(nbt);
    }

    private static CompoundNBT clearFrozenStatus(CompoundNBT nbt) {
        nbt.putInt(FROZEN_TIME, 0);
        nbt.putBoolean(FROZEN_BOOL, false);
        return nbt;
    }

    public static boolean isFrozen(LivingEntity entity) {
        if (!(entity instanceof EntityIceDragon)) {
            CompoundNBT nbt = getOrCreateFrozenData(entity);
            if (nbt.contains(FROZEN_BOOL)) {
                return nbt.getBoolean(FROZEN_BOOL);
            }
        }
        return false;
    }

    public static int ticksUntilUnfrozen(LivingEntity entity) {
        if (!(entity instanceof EntityIceDragon)) {
            CompoundNBT nbt = getOrCreateFrozenData(entity);
            if (nbt.contains(FROZEN_TIME)) {
                return nbt.getInt(FROZEN_TIME);
            }
        }
        return 0;
    }

    public static void setFrozenFor(LivingEntity entity, int duration) {
        if (!(entity instanceof EntityIceDragon)) {
            CompoundNBT entityData = CitadelEntityData.getOrCreateCitadelTag(entity);
            CompoundNBT frozenData = getOrCreateFrozenData(entityData);
            if (!frozenData.getBoolean(FROZEN_BOOL))
                entity.playSound(SoundEvents.GLASS_PLACE, 1, 1);
            frozenData.putInt(FROZEN_TIME, duration);
            frozenData.putBoolean(FROZEN_BOOL, true);
            entityData.put(FROZEN_DATA, frozenData);
            updateData(entity, entityData);

        }
    }

    public static void updateData(LivingEntity entity) {
        updateData(entity, CitadelEntityData.getCitadelTag(entity));
    }

    private static void updateData(LivingEntity entity, CompoundNBT nbt) {
        CitadelEntityData.setCitadelTag(entity, nbt);
        if (!entity.level.isClientSide()) {
            Citadel.sendMSGToAll(new PropertiesMessage("CitadelPatreonConfig", nbt, entity.getId()));
        }
    }

    public static void tickFrozenEntity(LivingEntity entity) {
        CompoundNBT entityData = CitadelEntityData.getOrCreateCitadelTag(entity);
        CompoundNBT frozenData = getOrCreateFrozenData(entityData);
        if (entity instanceof EntityIceDragon) {
            frozenData.putBoolean(FROZEN_BOOL, false);
        }
        if (entity.level.isClientSide())
            return;
        if (frozenData.contains(FROZEN_TIME)) {
            int frozenTime = frozenData.getInt(FROZEN_TIME);
            // If burning extinguish
            if (entity.isOnFire()) {
                clearFrozenStatus(frozenData, entity, true);
                entity.clearFire();
            }
            // If dead
            else if (entity.deathTime > 0) {
                clearFrozenStatus(frozenData, entity, false);
            }
            // Update frozenTime
            else if (frozenTime > 0) {
                frozenTime--;
                frozenData.putInt(FROZEN_TIME, frozenTime);
            } else {
                clearFrozenStatus(frozenData, entity, true);
            }
        }
        entityData.put(FROZEN_DATA, frozenData);
        updateData(entity, entityData);
    }

}
