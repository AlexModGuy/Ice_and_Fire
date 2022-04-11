package com.github.alexthe666.iceandfire.entity.props;

import com.github.alexthe666.citadel.server.entity.CitadelEntityData;
import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.SoundEvents;

import java.util.Random;

public class ChickenProperties {
    private static final String CHICKEN_DATA = "TimeUntilNextEggIaf";
    private static final Random rand = new Random();

    private static int createDefaultTime() {
        return rand.nextInt(6000) + 6000;
    }

    private static CompoundNBT createDefaultData(CompoundNBT nbt) {
        nbt.putInt(CHICKEN_DATA, createDefaultTime());
        return nbt;
    }

    public static int getTimeRemaining(LivingEntity entity) {
        CompoundNBT nbt = CitadelEntityData.getOrCreateCitadelTag(entity);
        if (!nbt.contains(CHICKEN_DATA)) {
            createDefaultData(nbt);
            CitadelEntityData.setCitadelTag(entity, nbt);
        }
        return nbt.getInt(CHICKEN_DATA);
    }

    public static void setTimeRemaining(LivingEntity entity, int time) {
        CompoundNBT nbt = CitadelEntityData.getOrCreateCitadelTag(entity);
        nbt.putInt(CHICKEN_DATA, time);
        CitadelEntityData.setCitadelTag(entity, nbt);
        // No need to message clients since this can stay server only
    }

    public static void tickChicken(LivingEntity entity) {
        int timeUntilNextEgg = getTimeRemaining(entity);
        if (timeUntilNextEgg <= 0) {
            if (entity.getRNG().nextInt(IafConfig.cockatriceEggChance + 1) == 0 && entity.ticksExisted > 30) {
                entity.playSound(SoundEvents.ENTITY_CHICKEN_HURT, 2.0F,
                    (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
                entity.playSound(SoundEvents.ENTITY_CHICKEN_EGG, 1.0F,
                    (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
                entity.entityDropItem(IafItemRegistry.ROTTEN_EGG, 1);
            }
            setTimeRemaining(entity, createDefaultTime());
        } else {
            setTimeRemaining(entity, timeUntilNextEgg - 1);
        }
    }
}
