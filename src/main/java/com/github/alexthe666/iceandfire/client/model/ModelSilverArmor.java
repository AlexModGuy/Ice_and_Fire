package com.github.alexthe666.iceandfire.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.BipedModel;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;

public class ModelSilverArmor extends BipedModel {
    public ModelRenderer faceGuard;
    public ModelRenderer helmWingR;
    public ModelRenderer crest;
    public ModelRenderer helmWingL;
    public ModelRenderer robeLower;
    public ModelRenderer robeLowerBack;

    public ModelSilverArmor(float modelSize) {
        super(modelSize, 0, 64, 64);
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.faceGuard = new ModelRenderer(this, 30, 47);
        this.faceGuard.setRotationPoint(0.0F, -6.6F, 1.9F);
        this.faceGuard.addBox(-4.5F, -3.0F, -6.1F, 9, 9, 8, modelSize);
        this.setRotateAngle(faceGuard, -0.7285004297824331F, 0.0F, 0.0F);
        this.robeLowerBack = new ModelRenderer(this, 4, 55);
        this.robeLowerBack.mirror = true;
        this.robeLowerBack.setRotationPoint(0.0F, 12.0F, 0.0F);
        this.robeLowerBack.addBox(-4.0F, 0.0F, -2.5F, 8, 8, 1, modelSize);
        this.setRotateAngle(robeLowerBack, 0.0F, 3.141592653589793F, 0.0F);
        this.crest = new ModelRenderer(this, 18, 32);
        this.crest.setRotationPoint(0.0F, -7.9F, -0.1F);
        this.crest.addBox(0.0F, -0.5F, 0.0F, 1, 9, 9, 0.0F);
        this.setRotateAngle(crest, 1.2292353921796064F, 0.0F, 0.0F);
        this.robeLower = new ModelRenderer(this, 4, 55);
        this.robeLower.setRotationPoint(0.0F, 12.0F, 0.0F);
        this.robeLower.addBox(-4.0F, 0.0F, -2.5F, 8, 8, 1, modelSize);
        this.helmWingR = new ModelRenderer(this, 2, 37);
        this.helmWingR.setRotationPoint(-3.0F, -6.3F, 1.3F);
        this.helmWingR.addBox(-0.5F, -1.0F, 0.0F, 1, 4, 6, modelSize);
        this.setRotateAngle(helmWingR, 0.5235987755982988F, -0.4363323129985824F, -0.05235987755982988F);
        this.helmWingL = new ModelRenderer(this, 2, 37);
        this.helmWingL.mirror = true;
        this.helmWingL.setRotationPoint(3.0F, -6.3F, 1.3F);
        this.helmWingL.addBox(-0.5F, -1.0F, 0.0F, 1, 4, 6, modelSize);
        this.setRotateAngle(helmWingL, 0.5235987755982988F, 0.4363323129985824F, 0.05235987755982988F);
        this.bipedHeadwear.addChild(this.crest);
        this.bipedHead.addChild(this.faceGuard);
        this.bipedBody.addChild(this.robeLowerBack);
        this.bipedBody.addChild(this.robeLower);
        this.bipedHead.addChild(this.helmWingR);
        this.bipedHead.addChild(this.helmWingL);
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
        float f = 0;
        float f1 = 12;
        if(isSneak){
            f = -1;
            f1 = 10;
        }
        this.robeLower.rotateAngleX = Math.min(0, Math.min(this.bipedLeftLeg.rotateAngleX, this.bipedRightLeg.rotateAngleX)) - this.bipedBody.rotateAngleX;
        this.robeLower.rotationPointZ = f;
        this.robeLower.rotationPointY = f1;

        this.robeLowerBack.rotateAngleX =  -Math.max(this.bipedLeftLeg.rotateAngleX, this.bipedRightLeg.rotateAngleX);

    }
}
