package com.github.alexthe666.iceandfire.entity.props;

import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.entity.EntityIceDragon;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;

public class FrozenData {
    public int frozenTicks;
    public boolean isFrozen;

    public boolean tickFrozen(final LivingEntity entity) {
        if (entity instanceof EntityIceDragon) {
            clearFrozen(entity);
            return true;
        }

        if (entity.isOnFire()) {
            clearFrozen(entity);
            entity.clearFire();
            return true;
        }

        if (entity.isDeadOrDying()) {
            clearFrozen(entity);
            return true;
        }

        if (frozenTicks > 0) {
            frozenTicks--;
        } else {
            clearFrozen(entity);
            return true;
        }

        return false;
    }

    public void setFrozen(final LivingEntity entity, int duration) {
        if (!isFrozen) {
            entity.playSound(SoundEvents.GLASS_PLACE, 1, 1);
        }

        frozenTicks = duration;
        isFrozen = true;

        CapabilityHandler.syncEntityData(entity);
    }

    private void clearFrozen(final LivingEntity entity) {
        if (isFrozen) {
            for (int i = 0; i < 15; i++) {
                entity.level.addParticle(
                        new BlockParticleOption(ParticleTypes.BLOCK,
                                IafBlockRegistry.DRAGON_ICE.get().defaultBlockState()),
                        entity.getX() + ((entity.getRandom().nextDouble() - 0.5D) * entity.getBbWidth()),
                        entity.getY() + ((entity.getRandom().nextDouble()) * entity.getBbHeight()),
                        entity.getZ() + ((entity.getRandom().nextDouble() - 0.5D) * entity.getBbWidth()),
                        0, 0, 0);
            }

            entity.playSound(SoundEvents.GLASS_BREAK, 3, 1);
        }

        isFrozen = false;
        frozenTicks = 0;
    }

    public void serialize(final CompoundTag tag) {
        CompoundTag frozenData = new CompoundTag();
        frozenData.putInt("frozenTicks", frozenTicks);
        frozenData.putBoolean("isFrozen", isFrozen);

        tag.put("frozenData", frozenData);
    }

    public void deserialize(final CompoundTag tag) {
        CompoundTag frozenData = tag.getCompound("frozenData");
        frozenTicks = frozenData.getInt("frozenTicks");
        isFrozen = frozenData.getBoolean("isFrozen");
    }
}
