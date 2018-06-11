package com.github.alexthe666.iceandfire.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelTrollArmor extends ModelBiped {
    public ModelRenderer hornR;
    public ModelRenderer hornL;
    public ModelRenderer hornR2;
    public ModelRenderer hornL2;

    public ModelTrollArmor(float modelSize) {
        super(modelSize, 0, 64, 64);
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.hornL = new ModelRenderer(this, 3, 41);
        this.hornL.mirror = true;
        this.hornL.setRotationPoint(3.0F, -2.2F, -3.0F);
        this.hornL.addBox(-1.0F, -0.5F, 0.0F, 1, 2, 5, 0.0F);
        this.setRotateAngle(hornL, -0.7740535232594852F, 2.9595548126067843F, -0.27314402793711257F);
        this.hornL2 = new ModelRenderer(this, 15, 50);
        this.hornL2.mirror = true;
        this.hornL2.setRotationPoint(-0.4F, 1.3F, 4.5F);
        this.hornL2.addBox(-0.51F, -0.8F, -0.0F, 1, 2, 7, 0.0F);
        this.setRotateAngle(hornL2, 1.2747884856566583F, 0.0F, 0.0F);
        this.hornR = new ModelRenderer(this, 4, 41);
        this.hornR.mirror = true;
        this.hornR.setRotationPoint(-3.3F, -2.2F, -3.0F);
        this.hornR.addBox(-0.5F, -0.5F, 0.0F, 1, 2, 5, 0.0F);
        this.setRotateAngle(hornR, -0.7740535232594852F, -2.9595548126067843F, 0.27314402793711257F);
        this.hornR2 = new ModelRenderer(this, 15, 50);
        this.hornR2.mirror = true;
        this.hornR2.setRotationPoint(-0.6F, 1.3F, 4.5F);
        this.hornR2.addBox(-0.01F, -0.8F, -0.0F, 1, 2, 7, 0.0F);
        this.setRotateAngle(hornR2, 1.2747884856566583F, 0.0F, 0.0F);
        this.bipedHead.addChild(this.hornL);
        this.hornL.addChild(this.hornL2);
        this.bipedHead.addChild(this.hornR);
        this.hornR.addChild(this.hornR2);
    }

    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
