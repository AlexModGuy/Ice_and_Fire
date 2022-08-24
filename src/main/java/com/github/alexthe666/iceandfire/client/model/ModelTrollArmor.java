package com.github.alexthe666.iceandfire.client.model;

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ArmorStandEntity;

public class ModelTrollArmor extends BipedModel {
    public ModelRenderer hornR;
    public ModelRenderer hornL;
    public ModelRenderer hornR2;
    public ModelRenderer hornL2;

    public ModelTrollArmor(float modelSize) {
        super(modelSize, 0, 64, 64);
        this.texWidth = 64;
        this.texHeight = 64;
        this.hornL = new ModelRenderer(this, 3, 41);
        this.hornL.mirror = true;
        this.hornL.setPos(3.0F, -2.2F, -3.0F);
        this.hornL.addBox(-1.0F, -0.5F, 0.0F, 1, 2, 5, 0.0F);
        this.setRotateAngle(hornL, -0.7740535232594852F, 2.9595548126067843F, -0.27314402793711257F);
        this.hornL2 = new ModelRenderer(this, 15, 50);
        this.hornL2.mirror = true;
        this.hornL2.setPos(-0.4F, 1.3F, 4.5F);
        this.hornL2.addBox(-0.51F, -0.8F, -0.0F, 1, 2, 7, 0.0F);
        this.setRotateAngle(hornL2, 1.2747884856566583F, 0.0F, 0.0F);
        this.hornR = new ModelRenderer(this, 4, 41);
        this.hornR.mirror = true;
        this.hornR.setPos(-3.3F, -2.2F, -3.0F);
        this.hornR.addBox(-0.5F, -0.5F, 0.0F, 1, 2, 5, 0.0F);
        this.setRotateAngle(hornR, -0.7740535232594852F, -2.9595548126067843F, 0.27314402793711257F);
        this.hornR2 = new ModelRenderer(this, 15, 50);
        this.hornR2.mirror = true;
        this.hornR2.setPos(-0.6F, 1.3F, 4.5F);
        this.hornR2.addBox(-0.01F, -0.8F, -0.0F, 1, 2, 7, 0.0F);
        this.setRotateAngle(hornR2, 1.2747884856566583F, 0.0F, 0.0F);
        this.head.addChild(this.hornL);
        this.hornL.addChild(this.hornL2);
        this.head.addChild(this.hornR);
        this.hornR.addChild(this.hornR2);
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
    }
}
