package com.github.alexthe666.iceandfire.client.model.util;

import net.ilexiconn.llibrary.client.model.tabula.TabulaModelHandler;
import net.minecraft.client.model.ModelBase;

import java.io.IOException;

public enum EnumDragonAnimations {
    T_POSE(""),
    FLYING_POSE("Flying"),
    GROUND_POSE("Ground"),
    HOVERING_POSE("Hovering"),
    SITTING_POSE("Sitting"),
    SLEEPING_POSE("Sleeping"),
    SWIM_POSE("Swim", true),
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
    WALK4("Walk4");

    public ModelBase firedragon_model;
    public ModelBase icedragon_model;
    private EnumDragonAnimations(String fileSuffix){
        try {
            firedragon_model = new IceAndFireTabulaModel(TabulaModelHandler.INSTANCE.loadTabulaModel("/assets/iceandfire/models/tabula/firedragon/dragonFire" + fileSuffix));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            icedragon_model = new IceAndFireTabulaModel(TabulaModelHandler.INSTANCE.loadTabulaModel("/assets/iceandfire/models/tabula/icedragon/dragonIce" + fileSuffix));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private EnumDragonAnimations(String fileSuffix, boolean iceOrFire){
        if(!iceOrFire){
            try {
                firedragon_model = new IceAndFireTabulaModel(TabulaModelHandler.INSTANCE.loadTabulaModel("/assets/iceandfire/models/tabula/firedragon/dragonFire" + fileSuffix));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            try {
                icedragon_model = new IceAndFireTabulaModel(TabulaModelHandler.INSTANCE.loadTabulaModel("/assets/iceandfire/models/tabula/icedragon/dragonIce" + fileSuffix));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
}
