package com.github.alexthe666.iceandfire.client.model.util;

import net.ilexiconn.llibrary.client.model.tabula.TabulaModelHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public enum EnumSeaSerpentAnimations {
    T_POSE(""),
    SWIM1("Swim1"),
    SWIM2("Swim2"),
    SWIM3("Swim3"),
    SWIM4("Swim4"),
    SWIM5("Swim5"),
    SWIM6("Swim6"),
    DEAD("Dead");

    @SideOnly(Side.CLIENT)
    public IceAndFireTabulaModel seaserpent_model;
    private String fileSuffix;

    private EnumSeaSerpentAnimations(String fileSuffix){
        this.fileSuffix = fileSuffix;
    }

    @SideOnly(Side.CLIENT)
    public static void initializeDragonModels(){
        if(FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT){
            for(EnumSeaSerpentAnimations animation : values()){
                try {
                    animation.seaserpent_model = new IceAndFireTabulaModel(TabulaModelHandler.INSTANCE.loadTabulaModel("/assets/iceandfire/models/tabula/seaserpent/seaserpent" + animation.fileSuffix));
                } catch (Exception e) {
                    System.out.println("sea serpent model at: seaserpent" + animation.fileSuffix + ".tbl doesn't exist!");
                    e.printStackTrace();
                }
            }
        }

    }
}
