package com.github.alexthe666.iceandfire.entity.props;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.event.ServerEvents;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public class ChickenData {
    public int timeUntilNextEgg = -1;

    public void tickChicken(final LivingEntity entity) {
        if (!IafConfig.chickensLayRottenEggs || entity.level().isClientSide() || !ServerEvents.isChicken(entity) || entity.isBaby()) {
            return;
        }

        if (timeUntilNextEgg == -1) {
            timeUntilNextEgg = createDefaultTime(entity.getRandom());
        }

        if (timeUntilNextEgg == 0) {
            if (entity.tickCount > 30 && entity.getRandom().nextInt(IafConfig.cockatriceEggChance + 1) == 0) {
                entity.playSound(SoundEvents.CHICKEN_HURT, 2.0F, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2F + 1.0F);
                entity.playSound(SoundEvents.CHICKEN_EGG, 1.0F, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2F + 1.0F);
                entity.spawnAtLocation(IafItemRegistry.ROTTEN_EGG.get(), 1);
            }

            timeUntilNextEgg = -1;
        } else {
            timeUntilNextEgg--;
        }
    }

    public void setTime(int timeUntilNextEgg) {
        this.timeUntilNextEgg = timeUntilNextEgg;
    }

    public void serialize(final CompoundTag tag) {
        CompoundTag chickenData = new CompoundTag();
        chickenData.putInt("timeUntilNextEgg", timeUntilNextEgg);
        tag.put("chickenData", chickenData);
    }

    public void deserialize(final CompoundTag tag) {
        CompoundTag chickenData = tag.getCompound("chickenData");
        timeUntilNextEgg = chickenData.getInt("timeUntilNextEgg");
    }

    private int createDefaultTime(@NotNull final RandomSource random) {
        return random.nextInt(6000) + 6000;
    }
}
