package com.github.alexthe666.iceandfire.client.model;

import net.minecraft.client.model.ModelPlayer;
import net.minecraft.entity.Entity;

public class ModelStonePlayer extends ModelPlayer{

    public ModelStonePlayer(float modelSize, boolean smallArmsIn) {
        super(modelSize, smallArmsIn);
    }

    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
        super.setRotationAngles(0, 0, 0, 0, 0, 0.0625F, entityIn);
    }
}
