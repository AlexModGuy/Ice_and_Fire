package com.github.alexthe666.iceandfire.client.model;

import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.client.model.ModelAnimator;
import com.github.alexthe666.iceandfire.client.model.util.HideableModelRenderer;
import com.github.alexthe666.iceandfire.entity.EntityDreadLich;
import com.github.alexthe666.iceandfire.entity.EntityDreadThrall;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.IHasArm;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.MathHelper;

public class ModelDreadLich extends ModelDreadBase<EntityDreadLich> implements IHasArm {
    public HideableModelRenderer robe;
    public HideableModelRenderer mask;
    public HideableModelRenderer hood;
    public HideableModelRenderer sleeveRight;
    public HideableModelRenderer robeLowerRight;
    public HideableModelRenderer robeLowerLeft;
    public HideableModelRenderer sleeveLeft;

    public ModelDreadLich(float modelScale) {
        this.textureWidth = 128;
        this.textureHeight = 64;
        this.leftArmPose = BipedModel.ArmPose.EMPTY;
        this.rightArmPose = BipedModel.ArmPose.EMPTY;
        this.sleeveLeft = new HideableModelRenderer(this, 33, 35);
        this.sleeveLeft.mirror = true;
        this.sleeveLeft.setRotationPoint(0.0F, -0.1F, 0.0F);
        this.sleeveLeft.addBox(-1.0F, -2.0F, -2.0F, 3, 12, 4, modelScale);
        this.robeLowerRight = new HideableModelRenderer(this, 48, 35);
        this.robeLowerRight.mirror = true;
        this.robeLowerRight.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.robeLowerRight.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, modelScale);
        this.legLeft = new HideableModelRenderer(this, 0, 16);
        this.legLeft.mirror = true;
        this.legLeft.setRotationPoint(2.0F, 12.0F, 0.1F);
        this.legLeft.addBox(-1.0F, 0.0F, -1.0F, 2, 12, 2, modelScale);
        this.robe = new HideableModelRenderer(this, 4, 34);
        this.robe.setRotationPoint(0.0F, 0.1F, 0.0F);
        this.robe.addBox(-4.5F, 0.0F, -2.5F, 9, 12, 5, modelScale);
        this.legRight = new HideableModelRenderer(this, 0, 16);
        this.legRight.setRotationPoint(-2.0F, 12.0F, 0.1F);
        this.legRight.addBox(-1.0F, 0.0F, -1.0F, 2, 12, 2, modelScale);
        this.sleeveRight = new HideableModelRenderer(this, 33, 35);
        this.sleeveRight.setRotationPoint(0.0F, -0.1F, 0.0F);
        this.sleeveRight.addBox(-2.2F, -2.0F, -2.0F, 3, 12, 4, modelScale);
        this.mask = new HideableModelRenderer(this, 40, 6);
        this.mask.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.mask.addBox(-3.5F, -10F, -4.1F, 7, 8, 0, modelScale);
        this.armRight = new HideableModelRenderer(this, 40, 16);
        this.armRight.setRotationPoint(-5.0F, 2.0F, 0.0F);
        this.armRight.addBox(-1.0F, -2.0F, -1.0F, 2, 12, 2, modelScale);
        this.setRotateAngle(armRight, 0.0F, -0.10000736613927509F, 0.10000736613927509F);
        this.hood = new HideableModelRenderer(this, 60, 0);
        this.hood.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.hood.addBox(-4.5F, -8.6F, -4.5F, 9, 9, 9, modelScale);
        this.head = new HideableModelRenderer(this, 0, 0);
        this.head.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.head.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, modelScale);
        this.body = new HideableModelRenderer(this, 16, 16);
        this.body.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.body.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, modelScale);
        this.armLeft = new HideableModelRenderer(this, 40, 16);
        this.armLeft.mirror = true;
        this.armLeft.setRotationPoint(5.0F, 2.0F, -0.0F);
        this.armLeft.addBox(-1.0F, -2.0F, -1.0F, 2, 12, 2, modelScale);
        this.setRotateAngle(armLeft, 0.0F, 0.10000736613927509F, -0.10000736613927509F);
        this.robeLowerLeft = new HideableModelRenderer(this, 48, 35);
        this.robeLowerLeft.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.robeLowerLeft.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, modelScale);
        this.armLeft.addChild(this.sleeveLeft);
        this.legRight.addChild(this.robeLowerRight);
        this.body.addChild(this.legLeft);
        this.body.addChild(this.robe);
        this.body.addChild(this.legRight);
        this.armRight.addChild(this.sleeveRight);
        this.head.addChild(this.mask);
        this.body.addChild(this.armRight);
        this.head.addChild(this.hood);
        this.body.addChild(this.head);
        this.body.addChild(this.armLeft);
        this.legLeft.addChild(this.robeLowerLeft);
        this.updateDefaultPose();
        animator = ModelAnimator.create();
    }

    public void setLivingAnimations(EntityDreadThrall LivingEntityIn, float limbSwing, float limbSwingAmount, float partialTickTime) {
        this.rightArmPose = BipedModel.ArmPose.EMPTY;
        this.leftArmPose = BipedModel.ArmPose.EMPTY;
        ItemStack itemstack = LivingEntityIn.getHeldItem(Hand.MAIN_HAND);

        if (itemstack.getItem() == Items.BOW) {
            if (LivingEntityIn.getPrimaryHand() == HandSide.RIGHT) {
                this.rightArmPose = BipedModel.ArmPose.BOW_AND_ARROW;
            } else {
                this.leftArmPose = BipedModel.ArmPose.BOW_AND_ARROW;
            }
        }
    }

    @Override
    Animation getSpawnAnimation() {
        return EntityDreadLich.ANIMATION_SPAWN;
    }

    @Override
    public void setRotationAngles(EntityDreadLich entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        super.setRotationAngles(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        if (entityIn.getAnimation() == EntityDreadLich.ANIMATION_SUMMON) {
            this.armRight.rotationPointZ = 0.0F;
            this.armRight.rotationPointX = -5.0F;
            this.armLeft.rotationPointZ = 0.0F;
            this.armLeft.rotationPointX = 5.0F;
            this.armRight.rotateAngleX = MathHelper.cos(ageInTicks * 0.6662F) * 0.25F;
            this.armLeft.rotateAngleX = MathHelper.cos(ageInTicks * 0.6662F) * 0.25F;
            this.armRight.rotateAngleZ = 2.3561945F;
            this.armLeft.rotateAngleZ = -2.3561945F;
            this.armRight.rotateAngleY = 0.0F;
            this.armLeft.rotateAngleY = 0.0F;
        }
    }

}
