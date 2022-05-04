package com.github.alexthe666.iceandfire.client.model;

import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.client.model.ModelAnimator;
import com.github.alexthe666.iceandfire.client.model.util.HideableModelRenderer;
import com.github.alexthe666.iceandfire.entity.EntityDreadKnight;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;

public class ModelDreadKnight extends ModelDreadBase<EntityDreadKnight> {
    public HideableModelRenderer chestplate;
    public HideableModelRenderer cloak;
    public HideableModelRenderer crown;
    public HideableModelRenderer sleeveRight;
    public HideableModelRenderer robeLowerRight;
    public HideableModelRenderer sleeveLeft;
    public HideableModelRenderer robeLowerLeft;

    public ModelDreadKnight(float modelScale) {
        this.textureWidth = 128;
        this.textureHeight = 64;
        this.leftArmPose = BipedModel.ArmPose.EMPTY;
        this.rightArmPose = BipedModel.ArmPose.EMPTY;
        this.sleeveRight = new HideableModelRenderer(this, 35, 33);
        this.sleeveRight.setRotationPoint(0.0F, -0.1F, 0.0F);
        this.sleeveRight.addBox(-4.0F, -2.1F, -2.5F, 5, 6, 5, modelScale);
        this.chestplate = new HideableModelRenderer(this, 1, 32);
        this.chestplate.setRotationPoint(0.0F, 0.1F, 0.0F);
        this.chestplate.addBox(-4.5F, 0.0F, -2.5F, 9, 11, 5, modelScale);
        this.crown = new HideableModelRenderer(this, 58, -1);
        this.crown.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.crown.addBox(-4.5F, -10.2F, -4.5F, 9, 11, 9, modelScale);
        this.body = new HideableModelRenderer(this, 16, 16);
        this.body.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.body.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, modelScale);
        this.legRight = new HideableModelRenderer(this, 0, 16);
        this.legRight.setRotationPoint(-1.9F, 12.0F, 0.1F);
        this.legRight.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, modelScale);
        this.armLeft = new HideableModelRenderer(this, 40, 16);
        this.armLeft.mirror = true;
        this.armLeft.setRotationPoint(5.0F, 2.0F, -0.0F);
        this.armLeft.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, modelScale);
        this.setRotateAngle(armLeft, -0.0F, 0.10000736613927509F, -0.10000736613927509F);
        this.legLeft = new HideableModelRenderer(this, 0, 16);
        this.legLeft.mirror = true;
        this.legLeft.setRotationPoint(1.9F, 12.0F, 0.1F);
        this.legLeft.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, modelScale);
        this.head = new HideableModelRenderer(this, 0, 0);
        this.head.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.head.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, modelScale);
        this.sleeveLeft = new HideableModelRenderer(this, 35, 33);
        this.sleeveLeft.mirror = true;
        this.sleeveLeft.setRotationPoint(0.0F, -0.1F, 0.0F);
        this.sleeveLeft.addBox(-1.0F, -2.1F, -2.5F, 5, 6, 5, modelScale);
        this.robeLowerRight = new HideableModelRenderer(this, 58, 33);
        this.robeLowerRight.mirror = true;
        this.robeLowerRight.setRotationPoint(0.0F, -0.2F, 0.0F);
        this.robeLowerRight.addBox(-2.1F, 0.0F, -2.5F, 4, 7, 5, modelScale);
        this.cloak = new HideableModelRenderer(this, 81, 37);
        this.cloak.setRotationPoint(0.0F, 0.1F, 0.0F);
        this.cloak.addBox(-4.5F, 0.0F, 2.3F, 9, 21, 1, modelScale);
        this.setRotateAngle(cloak, 0.045553093477052F, 0.0F, 0.0F);
        this.armRight = new HideableModelRenderer(this, 40, 16);
        this.armRight.setRotationPoint(-5.0F, 2.0F, 0.0F);
        this.armRight.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, modelScale);
        this.setRotateAngle(armRight, -0.0F, -0.10000736613927509F, 0.10000736613927509F);
        this.robeLowerLeft = new HideableModelRenderer(this, 58, 33);
        this.robeLowerLeft.setRotationPoint(0.0F, -0.2F, 0.0F);
        this.robeLowerLeft.addBox(-1.9F, 0.0F, -2.5F, 4, 7, 5, modelScale);
        this.armRight.addChild(this.sleeveRight);
        this.body.addChild(this.chestplate);
        this.head.addChild(this.crown);
        this.body.addChild(this.legRight);
        this.body.addChild(this.armLeft);
        this.body.addChild(this.legLeft);
        this.body.addChild(this.head);
        this.armLeft.addChild(this.sleeveLeft);
        this.legRight.addChild(this.robeLowerRight);
        this.body.addChild(this.cloak);
        this.body.addChild(this.armRight);
        this.legLeft.addChild(this.robeLowerLeft);
        this.updateDefaultPose();
        animator = ModelAnimator.create();
    }

    @Override
    public void setLivingAnimations(EntityDreadKnight LivingEntityIn, float limbSwing, float limbSwingAmount, float partialTickTime) {
        this.rightArmPose = BipedModel.ArmPose.EMPTY;
        this.leftArmPose = BipedModel.ArmPose.EMPTY;
        ItemStack itemstack = LivingEntityIn.getHeldItem(Hand.MAIN_HAND);

        if (itemstack.getItem() == Items.BOW && LivingEntityIn.isSwingInProgress) {
            if (LivingEntityIn.getPrimaryHand() == HandSide.RIGHT) {
                this.rightArmPose = BipedModel.ArmPose.BOW_AND_ARROW;
            } else {
                this.leftArmPose = BipedModel.ArmPose.BOW_AND_ARROW;
            }
        }
        super.setLivingAnimations(LivingEntityIn, limbSwing, limbSwingAmount, partialTickTime);
    }

    @Override
    public void setRotationAnglesSpawn(EntityDreadKnight entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        return;
    }

    @Override
    public void animate(EntityDreadKnight entity, float f, float f1, float f2, float f3, float f4, float f5) {
        return;
    }

    @Override
    Animation getSpawnAnimation() {
        return EntityDreadKnight.ANIMATION_SPAWN;
    }

    @Override
    public void copyModelAttributesTo(EntityModel<EntityDreadKnight> p_217111_1_) {
        super.copyModelAttributesTo(p_217111_1_);
        if (p_217111_1_ instanceof BipedModel) {
            BipedModel modelbiped = (BipedModel) p_217111_1_;
            modelbiped.leftArmPose = this.leftArmPose;
            modelbiped.rightArmPose = this.rightArmPose;
            modelbiped.isSneak = this.isSneak;
        }
    }


}