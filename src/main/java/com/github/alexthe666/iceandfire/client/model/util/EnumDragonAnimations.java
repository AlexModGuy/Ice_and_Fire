package com.github.alexthe666.iceandfire.client.model.util;

import java.io.IOException;

import net.ilexiconn.llibrary.client.model.tabula.TabulaModelHandler;

public enum EnumDragonAnimations {
    T_POSE(""),
    FLYING_POSE("Flying"),
    GROUND_POSE("Ground"),
    HOVERING_POSE("Hovering"),
    SITTING_POSE("Sitting"),
    SLEEPING_POSE("Sleeping"),
    SWIM_POSE("Swim", 2),
    BITE1("Bite1"),
    BITE2("Bite2"),
    BLAST_BREATH("BlastBreath"),
    BLAST_CHARGE("BlastCharge"),
    DEAD("Dead"),
    GRAB1("Grab1"),
    GRAB2("Grab2"),
    GRAB3("Grab3"),
    GRAB4("Grab4"),
    ROAR1("Roar1"),
    ROAR2("Roar2"),
    ROAR3("Roar3"),
    STREAM_BREATH("StreamBreath"),
    STREAM_CHARGE("StreamCharge"),
    TAIL_WHIP1("TailWhip1"),
    TAIL_WHIP2("TailWhip2"),
    TAIL_WHIP3("TailWhip3"),
    WING_BLAST1("WingBlast1"),
    WING_BLAST2("WingBlast2"),
    WING_BLAST3("WingBlast3"),
    WALK1("Walk1"),
    WALK2("Walk2"),
    WALK3("Walk3"),
    WALK4("Walk4"),
    FLIGHT1("Flight1"),
    FLIGHT2("Flight2"),
    FLIGHT3("Flight3"),
    FLIGHT4("Flight4"),
    FLIGHT5("Flight5"),
    FLIGHT6("Flight6"),
    TACKLE("Tackle");

    public IceAndFireTabulaModel firedragon_model;
    public IceAndFireTabulaModel icedragon_model;
    private String fileSuffix;
    private int dragonType;
    private EnumDragonAnimations(String fileSuffix){
        this.fileSuffix = fileSuffix;
    }

    private EnumDragonAnimations(String fileSuffix, int iceOrFire){
        this.fileSuffix = fileSuffix;
        this.dragonType = iceOrFire;
    }

    public static void initializeDragonModels(){
        for(EnumDragonAnimations animation : values()){
            switch(animation.dragonType){
                case 0:
                    try {
                        animation.firedragon_model = new IceAndFireTabulaModel(TabulaModelHandler.INSTANCE.loadTabulaModel("/assets/iceandfire/models/tabula/firedragon/dragonFire" + animation.fileSuffix));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    /*try {
                        animation.icedragon_model = new IceAndFireTabulaModel(TabulaModelHandler.INSTANCE.loadTabulaModel("/assets/iceandfire/models/tabula/icedragon/dragonIce" + animation.fileSuffix));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }*/
                    break;
                case 1:
                    try {
                        animation.firedragon_model = new IceAndFireTabulaModel(TabulaModelHandler.INSTANCE.loadTabulaModel("/assets/iceandfire/models/tabula/firedragon/dragonFire" + animation.fileSuffix));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    /*try {
                        animation.icedragon_model = new IceAndFireTabulaModel(TabulaModelHandler.INSTANCE.loadTabulaModel("/assets/iceandfire/models/tabula/icedragon/dragonIce" + animation.fileSuffix));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }*/
                    break;
            }

        }
    }
}
