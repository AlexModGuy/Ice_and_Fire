package com.github.alexthe666.iceandfire.client.model;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;

public class ModelDeathWormArmor extends ModelBiped {
    public ModelRenderer spineH1;
    public ModelRenderer spineH2;
    public ModelRenderer spineH3;
    public ModelRenderer spineH4;
    public ModelRenderer spineH5;
    public ModelRenderer spineH6;
    public ModelRenderer spineH7;
    public ModelRenderer spineR1;
    public ModelRenderer spineR2;
    public ModelRenderer spineL1;
    public ModelRenderer spineL2;

    public ModelDeathWormArmor(float modelSize) {
        super(modelSize, 0, 64, 64);
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.spineH1 = new ModelRenderer(this, 32, 40);
        this.spineH1.setRotationPoint(0.0F, -9.0F, -3.0F);
        this.spineH1.addBox(-0.5F, -1.7F, -0.5F, 1, 3, 1, 0.0F);
        this.setRotateAngle(spineH1, -0.4914847173616032F, 0.0F, 0.0F);
        this.spineL2 = new ModelRenderer(this, 32, 40);
        this.spineL2.setRotationPoint(2.5F, -1.6F, 0.0F);
        this.spineL2.addBox(-0.4F, -1.7F, -0.5F, 1, 2, 1, 0.0F);
        this.setRotateAngle(spineL2, -0.4914847173616032F, 0.0F, 0.0F);
        this.spineL1 = new ModelRenderer(this, 32, 40);
        this.spineL1.setRotationPoint(1.0F, -2.7F, 0.0F);
        this.spineL1.addBox(-0.5F, -1.7F, -0.5F, 1, 3, 1, 0.0F);
        this.setRotateAngle(spineL1, -0.4914847173616032F, 0.0F, 0.0F);
        this.spineH6 = new ModelRenderer(this, 32, 40);
        this.spineH6.setRotationPoint(0.0F, -3.5F, 5.0F);
        this.spineH6.addBox(-0.5F, -2.7F, -0.5F, 1, 5, 1, 0.0F);
        this.setRotateAngle(spineH6, -2.0032889154390916F, 0.0F, 0.0F);
        this.spineH5 = new ModelRenderer(this, 32, 40);
        this.spineH5.setRotationPoint(0.0F, -6.0F, 5.0F);
        this.spineH5.addBox(-0.5F, -1.7F, -0.5F, 1, 3, 1, 0.0F);
        this.setRotateAngle(spineH5, -1.8212510744560826F, 0.0F, 0.0F);
        this.spineH3 = new ModelRenderer(this, 32, 40);
        this.spineH3.setRotationPoint(0.0F, -9.0F, 3.0F);
        this.spineH3.addBox(-0.5F, -1.7F, -0.5F, 1, 3, 1, 0.0F);
        this.setRotateAngle(spineH3, -0.8651597102135892F, 0.0F, 0.0F);
        this.spineR2 = new ModelRenderer(this, 32, 40);
        this.spineR2.setRotationPoint(-2.5F, -1.6F, 0.0F);
        this.spineR2.addBox(-0.6F, -1.7F, -0.5F, 1, 2, 1, 0.0F);
        this.setRotateAngle(spineR2, -0.4914847173616032F, 0.0F, 0.0F);
        this.spineH2 = new ModelRenderer(this, 32, 40);
        this.spineH2.setRotationPoint(0.0F, -9.0F, 0.0F);
        this.spineH2.addBox(-0.5F, -2.7F, -0.5F, 1, 4, 1, 0.0F);
        this.setRotateAngle(spineH2, -0.4914847173616032F, 0.0F, 0.0F);
        this.spineH4 = new ModelRenderer(this, 32, 40);
        this.spineH4.setRotationPoint(0.0F, -8.0F, 5.0F);
        this.spineH4.addBox(-0.5F, -2.7F, -0.5F, 1, 4, 1, 0.0F);
        this.setRotateAngle(spineH4, -1.5481070465189704F, 0.0F, 0.0F);
        this.spineH7 = new ModelRenderer(this, 32, 40);
        this.spineH7.setRotationPoint(0.0F, -1.3F, 4.5F);
        this.spineH7.addBox(-0.5F, -1.7F, -0.5F, 1, 3, 1, 0.0F);
        this.setRotateAngle(spineH7, -2.0032889154390916F, 0.0F, 0.0F);
        this.spineR1 = new ModelRenderer(this, 32, 40);
        this.spineR1.setRotationPoint(-1.0F, -2.7F, 0.0F);
        this.spineR1.addBox(-0.5F, -1.7F, -0.5F, 1, 3, 1, 0.0F);
        this.setRotateAngle(spineR1, -0.4914847173616032F, 0.0F, 0.0F);
        this.bipedHeadwear.addChild(this.spineH1);
        this.bipedLeftArm.addChild(this.spineL2);
        this.bipedLeftArm.addChild(this.spineL1);
        this.bipedHeadwear.addChild(this.spineH6);
        this.bipedHeadwear.addChild(this.spineH5);
        this.bipedHeadwear.addChild(this.spineH3);
        this.bipedRightArm.addChild(this.spineR2);
        this.bipedHeadwear.addChild(this.spineH2);
        this.bipedHeadwear.addChild(this.spineH4);
        this.bipedHeadwear.addChild(this.spineH7);
        this.bipedRightArm.addChild(this.spineR1);
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
