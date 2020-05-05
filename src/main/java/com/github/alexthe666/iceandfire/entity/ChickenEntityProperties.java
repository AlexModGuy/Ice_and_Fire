package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.citadel.server.entity.EntityProperties;
import net.ilexiconn.llibrary.server.entity.EntityProperties;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.nbt.CompoundNBT;

import java.util.Random;

public class ChickenEntityProperties extends EntityProperties<LivingEntity> {

    private static Random rand = new Random();
    public int timeUntilNextEgg = 1;

    @Override
    public int getTrackingTime() {
        return 20;
    }

    @Override
    public void saveNBTData(CompoundNBT compound) {
        compound.setInteger("TimeUntilNextEgg", timeUntilNextEgg);
    }

    @Override
    public void loadNBTData(CompoundNBT compound) {
        this.timeUntilNextEgg = compound.getInteger("TimeUntilNextEgg");
    }

    @Override
    public void init() {
        timeUntilNextEgg = generateTime();
    }

    @Override
    public String getID() {
        return "Ice And Fire - Chicken Property Tracker";
    }

    @Override
    public Class<EntityAnimal> getEntityClass() {
        return EntityAnimal.class;
    }

    public int generateTime() {
        return rand.nextInt(6000) + 6000;
    }
}
