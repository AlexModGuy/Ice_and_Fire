package com.github.alexthe666.iceandfire.entity.props;

import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.entity.EntityIceDragon;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.player.Player;

public class FrozenData {
    public int frozenTicks;
    public boolean isFrozen;

    private boolean triggerClientUpdate;

    public void tickFrozen(final LivingEntity entity) {
        if (!isFrozen) {
            return;
        }

        if (entity instanceof EntityIceDragon) {
            clearFrozen(entity);
            return;
        }

        if (entity.isOnFire()) {
            clearFrozen(entity);
            entity.clearFire();
            return;
        }

        if (entity.isDeadOrDying()) {
            clearFrozen(entity);
            return;
        }

        if (frozenTicks > 0) {
            frozenTicks--;
        } else {
            clearFrozen(entity);
        }

        if (isFrozen && !(entity instanceof Player player && player.isCreative())) {
            entity.setDeltaMovement(entity.getDeltaMovement().multiply(0.25F, 1, 0.25F));

            if (!(entity instanceof EnderDragon) && !entity.onGround()) {
                entity.setDeltaMovement(entity.getDeltaMovement().add(0, -0.2, 0));
            }
        }
    }

    public void setFrozen(final LivingEntity target, int duration) {
        if (!isFrozen) {
            target.playSound(SoundEvents.GLASS_PLACE, 1, 1);
        }

        frozenTicks = duration;
        isFrozen = true;
        triggerClientUpdate = true;
    }

    private void clearFrozen(final LivingEntity entity) {
        for (int i = 0; i < 15; i++) {
            entity.level().addParticle(
                    new BlockParticleOption(ParticleTypes.BLOCK,
                            IafBlockRegistry.DRAGON_ICE.get().defaultBlockState()),
                    entity.getX() + ((entity.getRandom().nextDouble() - 0.5D) * entity.getBbWidth()),
                    entity.getY() + ((entity.getRandom().nextDouble()) * entity.getBbHeight()),
                    entity.getZ() + ((entity.getRandom().nextDouble() - 0.5D) * entity.getBbWidth()),
                    0, 0, 0);
        }

        entity.playSound(SoundEvents.GLASS_BREAK, 3, 1);

        isFrozen = false;
        frozenTicks = 0;
        triggerClientUpdate = true;
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

    public boolean doesClientNeedUpdate() {
        if (triggerClientUpdate) {
            triggerClientUpdate = false;
            return true;
        }

        return false;
    }
}
