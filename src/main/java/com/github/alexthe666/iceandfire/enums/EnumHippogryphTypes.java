package com.github.alexthe666.iceandfire.enums;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.world.biome.Biome;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public enum EnumHippogryphTypes {
    BLACK(IceAndFire.CONFIG.hippogryphBlackBiomes, false),
    BROWN(IceAndFire.CONFIG.hippogryphBrownBiomes, false),
    CHESTNUT(IceAndFire.CONFIG.hippogryphChestnutBiomes, false),
    CREAMY(IceAndFire.CONFIG.hippogryphCreamyBiomes, false),
    DARK_BROWN(IceAndFire.CONFIG.hippogryphDarkBrownBiomes, false),
    GRAY(IceAndFire.CONFIG.hippogryphGrayBiomes, false),
    WHITE(IceAndFire.CONFIG.hippogryphWhiteBiomes, false),
    RAPTOR(new Biome[0], true),
    ALEX(new Biome[0], true);

    boolean developer;
    Biome[] spawnBiomes;

    private EnumHippogryphTypes(Biome[] spawnBiomes, boolean developer){
        this.spawnBiomes = Arrays.copyOf(spawnBiomes, spawnBiomes.length);
        this.developer = developer;
    }

    public static EnumHippogryphTypes getRandomType(){
        return EnumHippogryphTypes.values()[new Random().nextInt(EnumHippogryphTypes.values().length - 2)];
    }

    public static EnumHippogryphTypes getBiomeType(Biome biome){
        List<EnumHippogryphTypes> types = new ArrayList<EnumHippogryphTypes>();
        for(EnumHippogryphTypes type : EnumHippogryphTypes.values()){
            for(int i = 0; i < type.spawnBiomes.length; i++){
                if(type.spawnBiomes[i] == biome){
                    types.add(type);
                }
            }
        }
        if(types.isEmpty()){
            return getRandomType();
        }else{
            return types.get(new Random().nextInt(types.size()));
        }

    }
}
