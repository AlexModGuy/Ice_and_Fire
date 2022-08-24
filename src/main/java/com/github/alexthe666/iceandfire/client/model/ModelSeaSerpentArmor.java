package com.github.alexthe666.iceandfire.client.model;

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ArmorStandEntity;

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
        this.texWidth = 64;
        this.texHeight = 64;
        this.headFin2 = new ModelRenderer(this, 0, 32);
        this.headFin2.mirror = true;
        this.headFin2.setPos(3.5F, -8.8F, 3.5F);
        this.headFin2.addBox(-0.5F, -8.4F, -7.9F, 1, 16, 14, 0.0F);
        this.setRotateAngle(headFin2, 3.141592653589793F, 0.5235987755982988F, 0.0F);
        this.armFinR = new ModelRenderer(this, 30, 32);
        this.armFinR.setPos(-1.5F, 4.0F, -0.4F);
        this.armFinR.addBox(-0.5F, -5.4F, -6.0F, 1, 7, 5, 0.0F);
        this.setRotateAngle(armFinR, 3.141592653589793F, -1.3089969389957472F, -0.003490658503988659F);
        this.headFin = new ModelRenderer(this, 0, 32);
        this.headFin.setPos(-3.5F, -8.8F, 3.5F);
        this.headFin.addBox(-0.5F, -8.4F, -7.9F, 1, 16, 14, 0.0F);
        this.setRotateAngle(headFin, 3.141592653589793F, -0.5235987755982988F, 0.0F);
        this.legFinR = new ModelRenderer(this, 45, 31);
        this.legFinR.mirror = true;
        this.legFinR.setPos(1.5F, 5.2F, 1.6F);
        this.legFinR.addBox(-0.5F, -5.4F, -6.0F, 1, 7, 6, 0.0F);
        this.setRotateAngle(legFinR, 3.141592653589793F, 1.3089969389957472F, 0.0F);
        this.shoulderL = new ModelRenderer(this, 38, 46);
        this.shoulderL.mirror = true;
        this.shoulderL.setPos(0.0F, -0.5F, 0.0F);
        this.shoulderL.addBox(-1.5F, -2.0F, -2.5F, 5, 12, 5, 0.0F);
        this.legFinR_1 = new ModelRenderer(this, 45, 31);
        this.legFinR_1.setPos(-1.5F, 5.2F, 1.6F);
        this.legFinR_1.addBox(-0.5F, -5.4F, -6.0F, 1, 7, 6, 0.0F);
        this.setRotateAngle(legFinR_1, 3.141592653589793F, -1.3089969389957472F, 0.0F);
        this.shoulderR = new ModelRenderer(this, 38, 46);
        this.shoulderR.setPos(0.0F, -0.5F, 0.0F);
        this.shoulderR.addBox(-3.5F, -2.0F, -2.5F, 5, 12, 5, 0.0F);
        this.armFinL = new ModelRenderer(this, 30, 32);
        this.armFinL.mirror = true;
        this.armFinL.setPos(1.5F, 4.0F, -0.4F);
        this.armFinL.addBox(-0.5F, -5.4F, -6.0F, 1, 7, 5, 0.0F);
        this.setRotateAngle(armFinL, 3.141592653589793F, 1.3089969389957472F, 0.0F);
        this.head.addChild(this.headFin2);
        this.rightArm.addChild(this.armFinR);
        this.head.addChild(this.headFin);
        this.leftLeg.addChild(this.legFinR);
        this.leftArm.addChild(this.shoulderL);
        this.rightLeg.addChild(this.legFinR_1);
        this.rightArm.addChild(this.shoulderR);
        this.leftArm.addChild(this.armFinL);
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
