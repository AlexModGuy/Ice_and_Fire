package com.github.alexthe666.iceandfire.client.model;

import net.minecraft.client.model.HorseModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.animal.horse.AbstractHorse;

public class ModelHorseStatue extends HorseModel {

    public ModelHorseStatue(ModelPart part) {
        super(part);
    }

    @Override
    public void setupAnim(AbstractHorse entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }
}