package com.github.alexthe666.iceandfire.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;

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

    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
        if (entityIn instanceof EntityArmorStand) {
            EntityArmorStand entityarmorstand = (EntityArmorStand) entityIn;
            this.bipedHead.rotateAngleX = 0.017453292F * entityarmorstand.getHeadRotation().getX();
            this.bipedHead.rotateAngleY = 0.017453292F * entityarmorstand.getHeadRotation().getY();
            this.bipedHead.rotateAngleZ = 0.017453292F * entityarmorstand.getHeadRotation().getZ();
            this.bipedHead.setRotationPoint(0.0F, 1.0F, 0.0F);
            this.bipedBody.rotateAngleX = 0.017453292F * entityarmorstand.getBodyRotation().getX();
            this.bipedBody.rotateAngleY = 0.017453292F * entityarmorstand.getBodyRotation().getY();
            this.bipedBody.rotateAngleZ = 0.017453292F * entityarmorstand.getBodyRotation().getZ();
            this.bipedLeftArm.rotateAngleX = 0.017453292F * entityarmorstand.getLeftArmRotation().getX();
            this.bipedLeftArm.rotateAngleY = 0.017453292F * entityarmorstand.getLeftArmRotation().getY();
            this.bipedLeftArm.rotateAngleZ = 0.017453292F * entityarmorstand.getLeftArmRotation().getZ();
            this.bipedRightArm.rotateAngleX = 0.017453292F * entityarmorstand.getRightArmRotation().getX();
            this.bipedRightArm.rotateAngleY = 0.017453292F * entityarmorstand.getRightArmRotation().getY();
            this.bipedRightArm.rotateAngleZ = 0.017453292F * entityarmorstand.getRightArmRotation().getZ();
            this.bipedLeftLeg.rotateAngleX = 0.017453292F * entityarmorstand.getLeftLegRotation().getX();
            this.bipedLeftLeg.rotateAngleY = 0.017453292F * entityarmorstand.getLeftLegRotation().getY();
            this.bipedLeftLeg.rotateAngleZ = 0.017453292F * entityarmorstand.getLeftLegRotation().getZ();
            this.bipedLeftLeg.setRotationPoint(1.9F, 11.0F, 0.0F);
            this.bipedRightLeg.rotateAngleX = 0.017453292F * entityarmorstand.getRightLegRotation().getX();
            this.bipedRightLeg.rotateAngleY = 0.017453292F * entityarmorstand.getRightLegRotation().getY();
            this.bipedRightLeg.rotateAngleZ = 0.017453292F * entityarmorstand.getRightLegRotation().getZ();
            this.bipedRightLeg.setRotationPoint(-1.9F, 11.0F, 0.0F);
            copyModelAngles(this.bipedHead, this.bipedHeadwear);
        } else {
            super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
        }
    }
}
