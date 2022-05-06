package com.github.alexthe666.iceandfire.client.model;

import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.client.model.ModelAnimator;
import com.github.alexthe666.iceandfire.client.model.util.HideableModelRenderer;
import com.github.alexthe666.iceandfire.entity.EntityDreadQueen;
import net.minecraft.client.renderer.entity.model.BipedModel;

public class ModelDreadQueen extends ModelDreadBase<EntityDreadQueen> {
    public HideableModelRenderer chestplate;
    public HideableModelRenderer cloak;
    public HideableModelRenderer necklace;
    public HideableModelRenderer sleeveRight;
    public HideableModelRenderer robeLowerRight;
    public HideableModelRenderer sleeveLeft;
    public HideableModelRenderer robeLowerLeft;

    public ModelDreadQueen(float modelScale) {
        this.textureWidth = 128;
        this.textureHeight = 64;
        this.leftArmPose = BipedModel.ArmPose.EMPTY;
        this.rightArmPose = BipedModel.ArmPose.EMPTY;
        this.body = new HideableModelRenderer(this, 16, 16);
        this.body.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.body.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, modelScale);
        this.headware = new HideableModelRenderer(this, 58, 0);
        this.headware.setRotationPoint(0.0F, 1.0F, 0.0F);
        this.headware.addBox(-4.5F, -12.5F, -4.5F, 9, 13, 9, modelScale);
        this.legRight = new HideableModelRenderer(this, 0, 16);
        this.legRight.setRotationPoint(-1.9F, 12.0F, 0.1F);
        this.legRight.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, modelScale);
        this.sleeveLeft = new HideableModelRenderer(this, 36, 33);
        this.sleeveLeft.mirror = true;
        this.sleeveLeft.setRotationPoint(-0.5F, -0.1F, 0.0F);
        this.sleeveLeft.addBox(-0.5F, -2.1F, -2.4F, 5, 6, 5, modelScale);
        this.robeLowerLeft = new HideableModelRenderer(this, 58, 33);
        this.robeLowerLeft.setRotationPoint(0.0F, -0.2F, 0.0F);
        this.robeLowerLeft.addBox(-1.9F, 0.0F, -2.5F, 4, 7, 5, modelScale);
        this.chestplate = new HideableModelRenderer(this, 1, 32);
        this.chestplate.setRotationPoint(0.0F, 0.1F, 0.0F);
        this.chestplate.addBox(-4.5F, -0.3F, -2.5F, 9, 12, 5, modelScale);
        this.robeLowerRight = new HideableModelRenderer(this, 58, 33);
        this.robeLowerRight.mirror = true;
        this.robeLowerRight.setRotationPoint(0.0F, -0.2F, 0.0F);
        this.robeLowerRight.addBox(-2.1F, 0.0F, -2.5F, 4, 7, 5, modelScale);
        this.sleeveRight = new HideableModelRenderer(this, 36, 33);
        this.sleeveRight.setRotationPoint(0.5F, -0.1F, 0.0F);
        this.sleeveRight.addBox(-4.5F, -2.1F, -2.4F, 5, 6, 5, modelScale);
        this.cloak = new HideableModelRenderer(this, 81, 37);
        this.cloak.setRotationPoint(0.0F, 0.1F, 0.0F);
        this.cloak.addBox(-4.5F, 0.0F, 2.3F, 9, 21, 1, modelScale);
        this.setRotateAngle(cloak, 0.045553093477052F, 0.0F, 0.0F);
        this.armRight = new HideableModelRenderer(this, 40, 16);
        this.armRight.setRotationPoint(-5.0F, 2.0F, 0.0F);
        this.armRight.addBox(-2.5F, -2.0F, -2.0F, 3, 12, 4, modelScale);
        this.setRotateAngle(armRight, 0.0F, 0.0F, 0.10000736613927509F);
        this.necklace = new HideableModelRenderer(this, 1, 52);
        this.necklace.setRotationPoint(0.0F, 0.1F, 0.0F);
        this.necklace.addBox(-4.5F, -0.3F, -2.8F, 9, 7, 5, modelScale);
        this.setRotateAngle(necklace, 0.032114058236695664F, 0.0F, 0.0F);
        this.armLeft = new HideableModelRenderer(this, 40, 16);
        this.armLeft.mirror = true;
        this.armLeft.setRotationPoint(5.0F, 2.0F, -0.0F);
        this.armLeft.addBox(-0.5F, -2.0F, -2.0F, 3, 12, 4, modelScale);
        this.setRotateAngle(armLeft, -0.0F, 0.0F, -0.10000736613927509F);
        this.head = new HideableModelRenderer(this, 0, 0);
        this.head.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.head.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, modelScale);
        this.legLeft = new HideableModelRenderer(this, 0, 16);
        this.legLeft.mirror = true;
        this.legLeft.setRotationPoint(1.9F, 12.0F, 0.1F);
        this.legLeft.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, modelScale);
        this.head.addChild(this.headware);
        this.body.addChild(this.legRight);
        this.armLeft.addChild(this.sleeveLeft);
        this.legLeft.addChild(this.robeLowerLeft);
        this.body.addChild(this.chestplate);
        this.legRight.addChild(this.robeLowerRight);
        this.armRight.addChild(this.sleeveRight);
        this.body.addChild(this.cloak);
        this.body.addChild(this.armRight);
        this.chestplate.addChild(this.necklace);
        this.body.addChild(this.armLeft);
        this.body.addChild(this.head);
        this.body.addChild(this.legLeft);
        this.updateDefaultPose();
        animator = ModelAnimator.create();
    }

    @Override
    public void setLivingAnimations(EntityDreadQueen LivingEntityIn, float limbSwing, float limbSwingAmount, float partialTickTime) {
        this.rightArmPose = BipedModel.ArmPose.EMPTY;
        this.leftArmPose = BipedModel.ArmPose.EMPTY;
        super.setLivingAnimations(LivingEntityIn, limbSwing, limbSwingAmount, partialTickTime);
    }

    @Override
    public Animation getSpawnAnimation() {
        return EntityDreadQueen.ANIMATION_SPAWN;
    }

}
