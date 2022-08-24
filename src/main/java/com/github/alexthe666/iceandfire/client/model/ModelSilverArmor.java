package com.github.alexthe666.iceandfire.client.model;

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ArmorStandEntity;

public class ModelSilverArmor extends BipedModel {
    public ModelRenderer faceGuard;
    public ModelRenderer helmWingR;
    public ModelRenderer crest;
    public ModelRenderer helmWingL;
    public ModelRenderer robeLower;
    public ModelRenderer robeLowerBack;

    public ModelSilverArmor(float modelSize) {
        super(modelSize, 0, 64, 64);
        this.texWidth = 64;
        this.texHeight = 64;
        this.faceGuard = new ModelRenderer(this, 30, 47);
        this.faceGuard.setPos(0.0F, -6.6F, 1.9F);
        this.faceGuard.addBox(-4.5F, -3.0F, -6.1F, 9, 9, 8, modelSize);
        this.setRotateAngle(faceGuard, -0.7285004297824331F, 0.0F, 0.0F);
        this.robeLowerBack = new ModelRenderer(this, 4, 55);
        this.robeLowerBack.mirror = true;
        this.robeLowerBack.setPos(0.0F, 12.0F, 0.0F);
        this.robeLowerBack.addBox(-4.0F, 0.0F, -2.5F, 8, 8, 1, modelSize);
        this.setRotateAngle(robeLowerBack, 0.0F, 3.141592653589793F, 0.0F);
        this.crest = new ModelRenderer(this, 18, 32);
        this.crest.setPos(0.0F, -7.9F, -0.1F);
        this.crest.addBox(0.0F, -0.5F, 0.0F, 1, 9, 9, 0.0F);
        this.setRotateAngle(crest, 1.2292353921796064F, 0.0F, 0.0F);
        this.robeLower = new ModelRenderer(this, 4, 55);
        this.robeLower.setPos(0.0F, 12.0F, 0.0F);
        this.robeLower.addBox(-4.0F, 0.0F, -2.5F, 8, 8, 1, modelSize);
        this.helmWingR = new ModelRenderer(this, 2, 37);
        this.helmWingR.setPos(-3.0F, -6.3F, 1.3F);
        this.helmWingR.addBox(-0.5F, -1.0F, 0.0F, 1, 4, 6, modelSize);
        this.setRotateAngle(helmWingR, 0.5235987755982988F, -0.4363323129985824F, -0.05235987755982988F);
        this.helmWingL = new ModelRenderer(this, 2, 37);
        this.helmWingL.mirror = true;
        this.helmWingL.setPos(3.0F, -6.3F, 1.3F);
        this.helmWingL.addBox(-0.5F, -1.0F, 0.0F, 1, 4, 6, modelSize);
        this.setRotateAngle(helmWingL, 0.5235987755982988F, 0.4363323129985824F, 0.05235987755982988F);
        this.hat.addChild(this.crest);
        this.head.addChild(this.faceGuard);
        this.body.addChild(this.robeLowerBack);
        this.body.addChild(this.robeLower);
        this.head.addChild(this.helmWingR);
        this.head.addChild(this.helmWingL);
    }


    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }

    public void setupAnim(LivingEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entityIn instanceof ArmorStandEntity) {
            ArmorStandEntity entityarmorstand = (ArmorStandEntity) entityIn;
            this.head.xRot = 0.017453292F * entityarmorstand.getHeadPose().getX();
            this.head.yRot = 0.017453292F * entityarmorstand.getHeadPose().getY();
            this.head.zRot = 0.017453292F * entityarmorstand.getHeadPose().getZ();
            this.head.setPos(0.0F, 1.0F, 0.0F);
            this.body.xRot = 0.017453292F * entityarmorstand.getBodyPose().getX();
            this.body.yRot = 0.017453292F * entityarmorstand.getBodyPose().getY();
            this.body.zRot = 0.017453292F * entityarmorstand.getBodyPose().getZ();
            this.leftArm.xRot = 0.017453292F * entityarmorstand.getLeftArmPose().getX();
            this.leftArm.yRot = 0.017453292F * entityarmorstand.getLeftArmPose().getY();
            this.leftArm.zRot = 0.017453292F * entityarmorstand.getLeftArmPose().getZ();
            this.rightArm.xRot = 0.017453292F * entityarmorstand.getRightArmPose().getX();
            this.rightArm.yRot = 0.017453292F * entityarmorstand.getRightArmPose().getY();
            this.rightArm.zRot = 0.017453292F * entityarmorstand.getRightArmPose().getZ();
            this.leftLeg.xRot = 0.017453292F * entityarmorstand.getLeftLegPose().getX();
            this.leftLeg.yRot = 0.017453292F * entityarmorstand.getLeftLegPose().getY();
            this.leftLeg.zRot = 0.017453292F * entityarmorstand.getLeftLegPose().getZ();
            this.leftLeg.setPos(1.9F, 11.0F, 0.0F);
            this.rightLeg.xRot = 0.017453292F * entityarmorstand.getRightLegPose().getX();
            this.rightLeg.yRot = 0.017453292F * entityarmorstand.getRightLegPose().getY();
            this.rightLeg.zRot = 0.017453292F * entityarmorstand.getRightLegPose().getZ();
            this.rightLeg.setPos(-1.9F, 11.0F, 0.0F);
            this.hat.copyFrom(this.head);
        } else {
            super.setupAnim(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        }
        float f = 0;
        float f1 = 12;
        if (crouching) {
            f = -1;
            f1 = 10;
        }
        this.robeLower.xRot = Math.min(0, Math.min(this.leftLeg.xRot, this.rightLeg.xRot)) - this.body.xRot;
        this.robeLower.z = f;
        this.robeLower.y = f1;

        this.robeLowerBack.xRot = -Math.max(this.leftLeg.xRot, this.rightLeg.xRot);
    }
}
