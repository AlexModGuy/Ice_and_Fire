package com.github.alexthe666.iceandfire.client.model;

import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.ModelAnimator;
import com.github.alexthe666.iceandfire.client.model.util.EntityModelPartBuilder;
import com.github.alexthe666.iceandfire.client.model.util.HideableModelRenderer;
import com.github.alexthe666.iceandfire.entity.EntityDreadThrall;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;

public class ModelDreadThrall extends ModelDreadBase<EntityDreadThrall> {

    public ModelDreadThrall(float modelScale, boolean bodyArmorModel) {
        this.textureHeight = 32;
        this.textureWidth = 64;
        this.leftArmPose = BipedModel.ArmPose.EMPTY;
        this.rightArmPose = BipedModel.ArmPose.EMPTY;
        this.body = new HideableModelRenderer(this, 16, 16);
        this.body.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, modelScale);
        this.body.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.armRight = new HideableModelRenderer(this, 40, 16);
        this.armRight.addBox(-1.0F, -2.0F, -1.0F, 2, 12, 2, modelScale);
        this.armRight.setRotationPoint(-5.0F, 2.0F, 0.0F);
        this.armLeft = new HideableModelRenderer(this, 40, 16);
        this.armLeft.mirror = true;
        this.armLeft.addBox(-1.0F, -2.0F, -1.0F, 2, 12, 2, modelScale);
        this.armLeft.setRotationPoint(5.0F, 2.0F, 0.0F);
        this.legRight = new HideableModelRenderer(this, 0, 16);
        this.legRight.addBox(-1.0F, 0.0F, -1.0F, 2, 12, 2, modelScale);
        this.legRight.setRotationPoint(-2.0F, 12.0F, 0.0F);
        this.legLeft = new HideableModelRenderer(this, 0, 16);
        this.legLeft.mirror = true;
        this.legLeft.addBox(-1.0F, 0.0F, -1.0F, 2, 12, 2, modelScale);
        this.legLeft.setRotationPoint(2.0F, 12.0F, 0.0F);
        this.head = new HideableModelRenderer(this, 0, 0);
        this.head.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, modelScale - 0.5F);
        this.head.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.headware = new HideableModelRenderer(this, 32, 0);
        this.headware.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, modelScale);
        this.headware.setRotationPoint(0.0F, 0.0F, 0.0F);
        if (bodyArmorModel) {
            this.head = new HideableModelRenderer(this, 0, 0);
            this.head.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, modelScale);
            this.head.setRotationPoint(0.0F, 0.0F + 0.0F, 0.0F);
            this.headware = new HideableModelRenderer(this, 32, 0);
            this.headware.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, modelScale + 0.5F);
            this.headware.setRotationPoint(0.0F, 0.0F + 0.0F, 0.0F);
            this.body = new HideableModelRenderer(this, 16, 16);
            this.body.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, modelScale);
            this.body.setRotationPoint(0.0F, 0.0F + 0.0F, 0.0F);
            this.armRight = new HideableModelRenderer(this, 40, 16);
            this.armRight.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, modelScale);
            this.armRight.setRotationPoint(-5.0F, 2.0F + 0.0F, 0.0F);
            this.armLeft = new HideableModelRenderer(this, 40, 16);
            this.armLeft.mirror = true;
            this.armLeft.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, modelScale);
            this.armLeft.setRotationPoint(5.0F, 2.0F + 0.0F, 0.0F);
            this.legRight = new HideableModelRenderer(this, 0, 16);
            this.legRight.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, modelScale);
            this.legRight.setRotationPoint(-1.9F, 12.0F + 0.0F, 0.0F);
            this.legLeft = new HideableModelRenderer(this, 0, 16);
            this.legLeft.mirror = true;
            this.legLeft.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, modelScale);
            this.legLeft.setRotationPoint(1.9F, 12.0F + 0.0F, 0.0F);
        }
        this.body.addChild(head);
        this.head.addChild(headware);
        this.body.addChild(armRight);
        this.body.addChild(armLeft);
        this.body.addChild(legRight);
        this.body.addChild(legLeft);
        animator = ModelAnimator.create();
        this.updateDefaultPose();
    }

    @Override
    public void setLivingAnimations(EntityDreadThrall LivingEntityIn, float limbSwing, float limbSwingAmount, float partialTickTime) {
        this.rightArmPose = BipedModel.ArmPose.EMPTY;
        this.leftArmPose = BipedModel.ArmPose.EMPTY;
        super.setLivingAnimations(LivingEntityIn, limbSwing, limbSwingAmount, partialTickTime);
    }

    @Override
    public void setRotationAngles(EntityDreadThrall entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        super.setRotationAngles(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        this.flap(body, 0.5F, 0.15F, false, 1, 0F, limbSwing, limbSwingAmount);
    }

    @Override
    public Animation getSpawnAnimation() {
        return EntityDreadThrall.ANIMATION_SPAWN;
    }

    @Override
    public Iterable<ModelRenderer> getParts() {
        return ImmutableList.of(body);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return EntityModelPartBuilder.getAllPartsFromClass(this.getClass(), this.getClass().getName());
    }

}