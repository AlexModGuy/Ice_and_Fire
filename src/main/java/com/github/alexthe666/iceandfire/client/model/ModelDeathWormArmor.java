package com.github.alexthe666.iceandfire.client.model;

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ArmorStandEntity;

public class ModelDeathWormArmor extends BipedModel {
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
        this.texWidth = 64;
        this.texHeight = 64;
        this.spineH1 = new ModelRenderer(this, 32, 40);
        this.spineH1.setPos(0.0F, -9.0F, -3.0F);
        this.spineH1.addBox(-0.5F, -1.7F, -0.5F, 1, 3, 1, 0.0F);
        this.setRotateAngle(spineH1, -0.4914847173616032F, 0.0F, 0.0F);
        this.spineL2 = new ModelRenderer(this, 32, 40);
        this.spineL2.setPos(2.5F, -1.6F, 0.0F);
        this.spineL2.addBox(-0.4F, -1.7F, -0.5F, 1, 2, 1, 0.0F);
        this.setRotateAngle(spineL2, -0.4914847173616032F, 0.0F, 0.0F);
        this.spineL1 = new ModelRenderer(this, 32, 40);
        this.spineL1.setPos(1.0F, -2.7F, 0.0F);
        this.spineL1.addBox(-0.5F, -1.7F, -0.5F, 1, 3, 1, 0.0F);
        this.setRotateAngle(spineL1, -0.4914847173616032F, 0.0F, 0.0F);
        this.spineH6 = new ModelRenderer(this, 32, 40);
        this.spineH6.setPos(0.0F, -3.5F, 5.0F);
        this.spineH6.addBox(-0.5F, -2.7F, -0.5F, 1, 5, 1, 0.0F);
        this.setRotateAngle(spineH6, -2.0032889154390916F, 0.0F, 0.0F);
        this.spineH5 = new ModelRenderer(this, 32, 40);
        this.spineH5.setPos(0.0F, -6.0F, 5.0F);
        this.spineH5.addBox(-0.5F, -1.7F, -0.5F, 1, 3, 1, 0.0F);
        this.setRotateAngle(spineH5, -1.8212510744560826F, 0.0F, 0.0F);
        this.spineH3 = new ModelRenderer(this, 32, 40);
        this.spineH3.setPos(0.0F, -9.0F, 3.0F);
        this.spineH3.addBox(-0.5F, -1.7F, -0.5F, 1, 3, 1, 0.0F);
        this.setRotateAngle(spineH3, -0.8651597102135892F, 0.0F, 0.0F);
        this.spineR2 = new ModelRenderer(this, 32, 40);
        this.spineR2.setPos(-2.5F, -1.6F, 0.0F);
        this.spineR2.addBox(-0.6F, -1.7F, -0.5F, 1, 2, 1, 0.0F);
        this.setRotateAngle(spineR2, -0.4914847173616032F, 0.0F, 0.0F);
        this.spineH2 = new ModelRenderer(this, 32, 40);
        this.spineH2.setPos(0.0F, -9.0F, 0.0F);
        this.spineH2.addBox(-0.5F, -2.7F, -0.5F, 1, 4, 1, 0.0F);
        this.setRotateAngle(spineH2, -0.4914847173616032F, 0.0F, 0.0F);
        this.spineH4 = new ModelRenderer(this, 32, 40);
        this.spineH4.setPos(0.0F, -8.0F, 5.0F);
        this.spineH4.addBox(-0.5F, -2.7F, -0.5F, 1, 4, 1, 0.0F);
        this.setRotateAngle(spineH4, -1.5481070465189704F, 0.0F, 0.0F);
        this.spineH7 = new ModelRenderer(this, 32, 40);
        this.spineH7.setPos(0.0F, -1.3F, 4.5F);
        this.spineH7.addBox(-0.5F, -1.7F, -0.5F, 1, 3, 1, 0.0F);
        this.setRotateAngle(spineH7, -2.0032889154390916F, 0.0F, 0.0F);
        this.spineR1 = new ModelRenderer(this, 32, 40);
        this.spineR1.setPos(-1.0F, -2.7F, 0.0F);
        this.spineR1.addBox(-0.5F, -1.7F, -0.5F, 1, 3, 1, 0.0F);
        this.setRotateAngle(spineR1, -0.4914847173616032F, 0.0F, 0.0F);
        this.hat.addChild(this.spineH1);
        this.leftArm.addChild(this.spineL2);
        this.leftArm.addChild(this.spineL1);
        this.hat.addChild(this.spineH6);
        this.hat.addChild(this.spineH5);
        this.hat.addChild(this.spineH3);
        this.rightArm.addChild(this.spineR2);
        this.hat.addChild(this.spineH2);
        this.hat.addChild(this.spineH4);
        this.hat.addChild(this.spineH7);
        this.rightArm.addChild(this.spineR1);
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
