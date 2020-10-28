package com.github.alexthe666.iceandfire.entity.props;

import com.github.alexthe666.citadel.server.entity.EntityProperties;

import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;

public class StoneEntityProperties extends EntityProperties<LivingEntity> {

    private boolean isStone;
    private int breakLvl;

    public boolean isStone(){
        return isStone;
    }

    public void setStone(boolean stone){
        this.isStone = stone;
        this.sync();
    }

    public int getBreakLevel(){
        return breakLvl;
    }

    public void setBreakLevel(int breakLevel){
        this.breakLvl = breakLevel;
        this.sync();
    }

    @Override
    public int getTrackingTime() {
        return 20;
    }

    @Override
    public void saveNBTData(CompoundNBT compound) {
        compound.putBoolean("TurnedToStone", isStone);
        compound.putInt("StoneBreakLvl", breakLvl);
    }

    @Override
    public void loadNBTData(CompoundNBT compound) {
        this.isStone = compound.getBoolean("TurnedToStone");
        this.breakLvl = compound.getInt("StoneBreakLvl");
    }

    @Override
    public void init() {
        isStone = false;
        breakLvl = 0;
    }

    @Override
    public String getID() {
        return "Ice And Fire - Stone Property Tracker";
    }

    @Override
    public Class<LivingEntity> getEntityClass() {
        return LivingEntity.class;
    }
}
