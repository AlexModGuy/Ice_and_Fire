package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.block.BlockEggInIce;
import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityEggInIce;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;

import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;

public abstract class DragonType {
    public static final DragonType FIRE = new FireDragonType();
    public static final DragonType ICE = new IceDragonType();
    public static final DragonType LIGHTNING = new LightningDragonType();

    private String name;
    private boolean piscivore;

    public abstract int getIntValue();

    private static class FireDragonType extends DragonType {
        public int getIntValue() {
            return 0;
        }
    }

    private static class IceDragonType extends DragonType {
        public int getIntValue() {
            return 1;
        }
    }

    private static class LightningDragonType extends DragonType {
        public int getIntValue() {
            return 2;
        }
    }

    public static int getIntFromType(DragonType type) {
        return type.getIntValue();
    }

    public static String getNameFromInt(int type){
        if(type == 2){
            return "lightning";
        }else if (type == 1){
            return "ice";
        }else{
            return "fire";
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isPiscivore() {
        return piscivore;
    }

    public DragonType setPiscivore() {
        piscivore = true;
        return this;
    }
}