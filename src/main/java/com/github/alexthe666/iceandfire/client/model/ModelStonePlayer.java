package com.github.alexthe666.iceandfire.client.model;

import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;

public class ModelStonePlayer extends PlayerModel {

    public ModelStonePlayer(float modelSize, boolean smallArmsIn) {
        super(modelSize, smallArmsIn);
    }

    public void setRotationAngles(LivingEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        super.setRotationAngles(entityIn, 0, 0, 0, 0, 0);
    }
}
