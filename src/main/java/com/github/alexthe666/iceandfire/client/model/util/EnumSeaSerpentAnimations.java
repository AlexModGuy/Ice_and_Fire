package com.github.alexthe666.iceandfire.client.model.util;

import com.github.alexthe666.citadel.client.model.TabulaModel;
import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;

public enum EnumSeaSerpentAnimations {
    T_POSE("base"),
    SWIM1("swim1"),
    SWIM2("swim2"),
    SWIM3("swim3"),
    SWIM4("swim4"),
    SWIM5("swim5"),
    SWIM6("swim6"),
    BITE1("bite1"),
    BITE2("bite2"),
    BITE3("bite3"),
    ROAR1("roar1"),
    ROAR2("roar2"),
    ROAR3("roar3"),
    DEAD("dead"),
    JUMPING1("jumping1"),
    JUMPING2("jumping2");


    private final String fileSuffix;
    public TabulaModel seaserpent_model;

    EnumSeaSerpentAnimations(String fileSuffix) {
        this.fileSuffix = fileSuffix;
    }


    public static void initializeSerpentModels() {
        if (FMLEnvironment.dist == Dist.CLIENT) {
            for (EnumSeaSerpentAnimations animation : values()) {
                try {
                    animation.seaserpent_model = new TabulaModel(TabulaModelHandlerHelper.loadTabulaModel("/assets/iceandfire/models/tabula/seaserpent/seaserpent_" + animation.fileSuffix));
                } catch (Exception e) {
                    IceAndFire.LOGGER.warn("sea serpent model at: seaserpent" + animation.fileSuffix + ".tbl doesn't exist!");
                    e.printStackTrace();
                }
            }
        }

    }
}
