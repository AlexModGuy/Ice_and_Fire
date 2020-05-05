package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.citadel.server.entity.EntityProperties;
import net.ilexiconn.llibrary.server.entity.EntityProperties;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;

public class SirenEntityProperties extends EntityProperties<LivingEntity> {

    public boolean isCharmed;
    public int sirenID;
    public int singTime;

    @Override
    public int getTrackingTime() {
        return 20;
    }

    @Override
    public void saveNBTData(CompoundNBT compound) {
        compound.setBoolean("CharmedBySiren", isCharmed);
        compound.setInteger("SirenID", sirenID);
    }

    @Override
    public void loadNBTData(CompoundNBT compound) {
        this.isCharmed = compound.getBoolean("CharmedBySiren");
        this.sirenID = compound.getInteger("SirenID");
    }

    public EntitySiren getSiren(World world) {
        Entity entity = world.getEntityByID(sirenID);
        if (entity != null && entity instanceof EntitySiren) {
            return (EntitySiren) entity;
        }
        return null;
    }

    @Override
    public void init() {
        isCharmed = false;
    }

    @Override
    public String getID() {
        return "Ice And Fire - Siren Property Tracker";
    }

    @Override
    public Class<LivingEntity> getEntityClass() {
        return LivingEntity.class;
    }
}
