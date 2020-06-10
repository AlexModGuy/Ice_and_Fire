package com.github.alexthe666.iceandfire.client.model;

import net.minecraft.client.renderer.entity.model.HorseModel;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import net.minecraft.util.math.MathHelper;

public class ModelHorseStatue extends HorseModel {

    public ModelHorseStatue() {
        super(0);
    }

    @Override
    public void setRotationAngles(AbstractHorseEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }
}