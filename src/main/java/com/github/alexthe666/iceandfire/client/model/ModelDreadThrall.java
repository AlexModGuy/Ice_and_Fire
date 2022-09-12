package com.github.alexthe666.iceandfire.client.model;

import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.client.model.ModelAnimator;
import com.github.alexthe666.iceandfire.client.model.util.HideableModelRenderer;
import com.github.alexthe666.iceandfire.entity.EntityDreadThrall;
import net.minecraft.client.model.HumanoidModel;

public class ModelDreadThrall extends ModelDreadBase<EntityDreadThrall> {

    public ModelDreadThrall(float modelScale, boolean bodyArmorModel) {
        this.texHeight = 32;
        this.texWidth = 64;
        this.leftArmPose = HumanoidModel.ArmPose.EMPTY;
        this.rightArmPose = HumanoidModel.ArmPose.EMPTY;
        this.body = new HideableModelRenderer(this, 16, 16);
        this.body.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, modelScale);
        this.body.setPos(0.0F, 0.0F, 0.0F);
        this.armRight = new HideableModelRenderer(this, 40, 16);
        this.armRight.addBox(-1.0F, -2.0F, -1.0F, 2, 12, 2, modelScale);
        this.armRight.setPos(-5.0F, 2.0F, 0.0F);
        this.armLeft = new HideableModelRenderer(this, 40, 16);
        this.armLeft.mirror = true;
        this.armLeft.addBox(-1.0F, -2.0F, -1.0F, 2, 12, 2, modelScale);
        this.armLeft.setPos(5.0F, 2.0F, 0.0F);
        this.legRight = new HideableModelRenderer(this, 0, 16);
        this.legRight.addBox(-1.0F, 0.0F, -1.0F, 2, 12, 2, modelScale);
        this.legRight.setPos(-2.0F, 12.0F, 0.0F);
        this.legLeft = new HideableModelRenderer(this, 0, 16);
        this.legLeft.mirror = true;
        this.legLeft.addBox(-1.0F, 0.0F, -1.0F, 2, 12, 2, modelScale);
        this.legLeft.setPos(2.0F, 12.0F, 0.0F);
        this.head = new HideableModelRenderer(this, 0, 0);
        this.head.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, modelScale - 0.5F);
        this.head.setPos(0.0F, 0.0F, 0.0F);
        this.headware = new HideableModelRenderer(this, 32, 0);
        this.headware.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, modelScale);
        this.headware.setPos(0.0F, 0.0F, 0.0F);
        if (bodyArmorModel) {
            this.head = new HideableModelRenderer(this, 0, 0);
            this.head.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, modelScale);
            this.head.setPos(0.0F, 0.0F + 0.0F, 0.0F);
            this.headware = new HideableModelRenderer(this, 32, 0);
            this.headware.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, modelScale + 0.5F);
            this.headware.setPos(0.0F, 0.0F + 0.0F, 0.0F);
            this.body = new HideableModelRenderer(this, 16, 16);
            this.body.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, modelScale);
            this.body.setPos(0.0F, 0.0F + 0.0F, 0.0F);
            this.armRight = new HideableModelRenderer(this, 40, 16);
            this.armRight.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, modelScale);
            this.armRight.setPos(-5.0F, 2.0F + 0.0F, 0.0F);
            this.armLeft = new HideableModelRenderer(this, 40, 16);
            this.armLeft.mirror = true;
            this.armLeft.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, modelScale);
            this.armLeft.setPos(5.0F, 2.0F + 0.0F, 0.0F);
            this.legRight = new HideableModelRenderer(this, 0, 16);
            this.legRight.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, modelScale);
            this.legRight.setPos(-1.9F, 12.0F + 0.0F, 0.0F);
            this.legLeft = new HideableModelRenderer(this, 0, 16);
            this.legLeft.mirror = true;
            this.legLeft.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, modelScale);
            this.legLeft.setPos(1.9F, 12.0F + 0.0F, 0.0F);
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
    public void prepareMobModel(EntityDreadThrall LivingEntityIn, float limbSwing, float limbSwingAmount, float partialTickTime) {
        this.rightArmPose = HumanoidModel.ArmPose.EMPTY;
        this.leftArmPose = HumanoidModel.ArmPose.EMPTY;
        super.prepareMobModel(LivingEntityIn, limbSwing, limbSwingAmount, partialTickTime);
    }

    @Override
    public void setupAnim(EntityDreadThrall entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        super.setupAnim(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        this.flap(body, 0.5F, 0.15F, false, 1, 0F, limbSwing, limbSwingAmount);
    }

    @Override
    public Animation getSpawnAnimation() {
        return EntityDreadThrall.ANIMATION_SPAWN;
    }

}