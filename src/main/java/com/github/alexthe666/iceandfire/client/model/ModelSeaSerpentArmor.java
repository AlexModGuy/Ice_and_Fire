package com.github.alexthe666.iceandfire.client.model;

import net.minecraft.client.model.BipedModel;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;

public class ModelSeaSerpentArmor extends BipedModel {
    public ModelRenderer headFin;
    public ModelRenderer headFin2;
    public ModelRenderer armFinR;
    public ModelRenderer shoulderR;
    public ModelRenderer legFinR;
    public ModelRenderer armFinL;
    public ModelRenderer shoulderL;
    public ModelRenderer legFinR_1;

    public ModelSeaSerpentArmor(float modelSize) {
        super(modelSize, 0, 64, 64);
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.headFin2 = new ModelRenderer(this, 0, 32);
        this.headFin2.mirror = true;
        this.headFin2.setRotationPoint(3.5F, -8.8F, 3.5F);
        this.headFin2.addBox(-0.5F, -8.4F, -7.9F, 1, 16, 14, 0.0F);
        this.setRotateAngle(headFin2, 3.141592653589793F, 0.5235987755982988F, 0.0F);
        this.armFinR = new ModelRenderer(this, 30, 32);
        this.armFinR.setRotationPoint(-1.5F, 4.0F, -0.4F);
        this.armFinR.addBox(-0.5F, -5.4F, -6.0F, 1, 7, 5, 0.0F);
        this.setRotateAngle(armFinR, 3.141592653589793F, -1.3089969389957472F, -0.003490658503988659F);
        this.headFin = new ModelRenderer(this, 0, 32);
        this.headFin.setRotationPoint(-3.5F, -8.8F, 3.5F);
        this.headFin.addBox(-0.5F, -8.4F, -7.9F, 1, 16, 14, 0.0F);
        this.setRotateAngle(headFin, 3.141592653589793F, -0.5235987755982988F, 0.0F);
        this.legFinR = new ModelRenderer(this, 45, 31);
        this.legFinR.mirror = true;
        this.legFinR.setRotationPoint(1.5F, 5.2F, 1.6F);
        this.legFinR.addBox(-0.5F, -5.4F, -6.0F, 1, 7, 6, 0.0F);
        this.setRotateAngle(legFinR, 3.141592653589793F, 1.3089969389957472F, 0.0F);
        this.shoulderL = new ModelRenderer(this, 38, 46);
        this.shoulderL.mirror = true;
        this.shoulderL.setRotationPoint(0.0F, -0.5F, 0.0F);
        this.shoulderL.addBox(-1.5F, -2.0F, -2.5F, 5, 12, 5, 0.0F);
        this.legFinR_1 = new ModelRenderer(this, 45, 31);
        this.legFinR_1.setRotationPoint(-1.5F, 5.2F, 1.6F);
        this.legFinR_1.addBox(-0.5F, -5.4F, -6.0F, 1, 7, 6, 0.0F);
        this.setRotateAngle(legFinR_1, 3.141592653589793F, -1.3089969389957472F, 0.0F);
        this.shoulderR = new ModelRenderer(this, 38, 46);
        this.shoulderR.setRotationPoint(0.0F, -0.5F, 0.0F);
        this.shoulderR.addBox(-3.5F, -2.0F, -2.5F, 5, 12, 5, 0.0F);
        this.armFinL = new ModelRenderer(this, 30, 32);
        this.armFinL.mirror = true;
        this.armFinL.setRotationPoint(1.5F, 4.0F, -0.4F);
        this.armFinL.addBox(-0.5F, -5.4F, -6.0F, 1, 7, 5, 0.0F);
        this.setRotateAngle(armFinL, 3.141592653589793F, 1.3089969389957472F, 0.0F);
        this.bipedHead.addChild(this.headFin2);
        this.bipedRightArm.addChild(this.armFinR);
        this.bipedHead.addChild(this.headFin);
        this.bipedLeftLeg.addChild(this.legFinR);
        this.bipedLeftArm.addChild(this.shoulderL);
        this.bipedRightLeg.addChild(this.legFinR_1);
        this.bipedRightArm.addChild(this.shoulderR);
        this.bipedLeftArm.addChild(this.armFinL);
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
