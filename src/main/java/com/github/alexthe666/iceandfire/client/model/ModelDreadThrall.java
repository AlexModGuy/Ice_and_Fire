package com.github.alexthe666.iceandfire.client.model;

import com.github.alexthe666.iceandfire.entity.EntityDreadThrall;
import com.github.alexthe666.iceandfire.entity.EntityGorgon;
import net.ilexiconn.llibrary.client.model.ModelAnimator;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelBase;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.AbstractSkeleton;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.MathHelper;

public class ModelDreadThrall extends AdvancedModelBase {

    public AdvancedModelRenderer bipedHead;
    public AdvancedModelRenderer bipedHeadwear;
    public AdvancedModelRenderer bipedBody;
    public AdvancedModelRenderer bipedRightArm;
    public AdvancedModelRenderer bipedLeftArm;
    public AdvancedModelRenderer bipedRightLeg;
    public AdvancedModelRenderer bipedLeftLeg;
    public ModelBiped.ArmPose leftArmPose;
    public ModelBiped.ArmPose rightArmPose;
    public boolean isSneak;
    private ModelAnimator animator;

    public ModelDreadThrall() {
        this.textureHeight = 32;
        this.textureWidth = 64;
        this.leftArmPose = ModelBiped.ArmPose.EMPTY;
        this.rightArmPose = ModelBiped.ArmPose.EMPTY;
        float modelSize = 0;

        this.bipedHead = new AdvancedModelRenderer(this, 0, 0);
        this.bipedHead.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, modelSize - 0.5F);
        this.bipedHead.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.bipedHeadwear = new AdvancedModelRenderer(this, 32, 0);
        this.bipedHeadwear.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, modelSize);
        this.bipedHeadwear.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.bipedBody = new AdvancedModelRenderer(this, 16, 16);
        this.bipedBody.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, modelSize);
        this.bipedBody.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.bipedRightArm = new AdvancedModelRenderer(this, 40, 16);
        this.bipedRightArm.addBox(-1.0F, -2.0F, -1.0F, 2, 12, 2, modelSize);
        this.bipedRightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);
        this.bipedLeftArm = new AdvancedModelRenderer(this, 40, 16);
        this.bipedLeftArm.mirror = true;
        this.bipedLeftArm.addBox(-1.0F, -2.0F, -1.0F, 2, 12, 2, modelSize);
        this.bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
        this.bipedRightLeg = new AdvancedModelRenderer(this, 0, 16);
        this.bipedRightLeg.addBox(-1.0F, 0.0F, -1.0F, 2, 12, 2, modelSize);
        this.bipedRightLeg.setRotationPoint(-2.0F, 12.0F, 0.0F);
        this.bipedLeftLeg = new AdvancedModelRenderer(this, 0, 16);
        this.bipedLeftLeg.mirror = true;
        this.bipedLeftLeg.addBox(-1.0F, 0.0F, -1.0F, 2, 12, 2, modelSize);
        this.bipedLeftLeg.setRotationPoint(2.0F, 12.0F, 0.0F);
        this.bipedBody.addChild(bipedHead);
        this.bipedHead.addChild(bipedHeadwear);
        this.bipedBody.addChild(bipedRightArm);
        this.bipedBody.addChild(bipedLeftArm);
        this.bipedBody.addChild(bipedRightLeg);
        this.bipedBody.addChild(bipedLeftLeg);
        animator = ModelAnimator.create();
        this.updateDefaultPose();
    }

    public void setLivingAnimations(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTickTime) {
        this.rightArmPose = ModelBiped.ArmPose.EMPTY;
        this.leftArmPose = ModelBiped.ArmPose.EMPTY;
        ItemStack itemstack = entitylivingbaseIn.getHeldItem(EnumHand.MAIN_HAND);

        if (itemstack.getItem() == Items.BOW && ((AbstractSkeleton) entitylivingbaseIn).isSwingingArms()) {
            if (entitylivingbaseIn.getPrimaryHand() == EnumHandSide.RIGHT) {
                this.rightArmPose = ModelBiped.ArmPose.BOW_AND_ARROW;
            } else {
                this.leftArmPose = ModelBiped.ArmPose.BOW_AND_ARROW;
            }
        }

        super.setLivingAnimations(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTickTime);
    }

    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
        this.resetToDefaultPose();
        animate((IAnimatedEntity) entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
        super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
        ItemStack itemstack = ((EntityLivingBase) entityIn).getHeldItemMainhand();
        EntityDreadThrall thrall = (EntityDreadThrall) entityIn;
        if (false) {
            float f = MathHelper.sin(this.swingProgress * (float) Math.PI);
            float f1 = MathHelper.sin((1.0F - (1.0F - this.swingProgress) * (1.0F - this.swingProgress)) * (float) Math.PI);
            this.bipedRightArm.rotateAngleZ = 0.0F;
            this.bipedLeftArm.rotateAngleZ = 0.0F;
            this.bipedRightArm.rotateAngleY = -(0.1F - f * 0.6F);
            this.bipedLeftArm.rotateAngleY = 0.1F - f * 0.6F;
            this.bipedRightArm.rotateAngleX = -((float) Math.PI / 2F);
            this.bipedLeftArm.rotateAngleX = -((float) Math.PI / 2F);
            this.bipedRightArm.rotateAngleX -= f * 1.2F - f1 * 0.4F;
            this.bipedLeftArm.rotateAngleX -= f * 1.2F - f1 * 0.4F;
            this.bipedRightArm.rotateAngleZ += MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
            this.bipedLeftArm.rotateAngleZ -= MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
            this.bipedRightArm.rotateAngleX += MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
            this.bipedLeftArm.rotateAngleX -= MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
        }
        float speed_walk = 0.6F;
        float speed_idle = 0.05F;
        float degree_walk = 1F;
        float degree_idle = 0.5F;
        if (thrall.getAnimation() == EntityDreadThrall.ANIMATION_SPAWN) {
            //this.walk(bipedRightArm, 1.5F, 0.4F, false, 2, -0.3F, thrall.ticksExisted, 1);
            //this.walk(bipedLeftArm, 1.5F,  0.4F, true, 2, 0.3F, thrall.ticksExisted, 1);
            if (thrall.getAnimationTick() < 30) {
                this.flap(bipedRightArm, 0.5F, 0.5F, false, 2, -0.7F, thrall.ticksExisted, 1);
                this.flap(bipedLeftArm, 0.5F, 0.5F, true, 2, -0.7F, thrall.ticksExisted, 1);
                this.walk(bipedRightArm, 0.5F, 0.5F, true, 1, 0, thrall.ticksExisted, 1);
                this.walk(bipedLeftArm, 0.5F, 0.5F, true, 1, 0, thrall.ticksExisted, 1);
            }
        }
    }

    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
        GlStateManager.pushMatrix();
        this.bipedBody.render(scale);
        GlStateManager.popMatrix();
    }

    public void animate(IAnimatedEntity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        animator.update(entity);
        animator.setAnimation(EntityDreadThrall.ANIMATION_SPAWN);
        animator.startKeyframe(0);
        animator.move(this.bipedBody, 0, 35, 0);
        rotate(animator, this.bipedLeftArm, -180, 0, 0);
        rotate(animator, this.bipedRightArm, -180, 0, 0);
        animator.endKeyframe();
        animator.startKeyframe(30);
        animator.move(this.bipedBody, 0, 0, 0);
        rotate(animator, this.bipedLeftArm, -180, 0, 0);
        rotate(animator, this.bipedRightArm, -180, 0, 0);
        animator.endKeyframe();
        animator.resetKeyframe(5);
    }

    private void rotate(ModelAnimator animator, AdvancedModelRenderer model, float x, float y, float z) {
        animator.rotate(model, (float) Math.toRadians(x), (float) Math.toRadians(y), (float) Math.toRadians(z));
    }

    private void rotateMinus(ModelAnimator animator, AdvancedModelRenderer model, float x, float y, float z) {
        animator.rotate(model, (float) Math.toRadians(x) - model.defaultRotationX, (float) Math.toRadians(y) - model.defaultRotationY, (float) Math.toRadians(z) - model.defaultRotationZ);
    }

    public void postRenderArm(float scale, EnumHandSide side) {
        float f = side == EnumHandSide.RIGHT ? 1.0F : -1.0F;
        ModelRenderer modelrenderer = this.getArmForSide(side);
        modelrenderer.rotationPointX += f;
        modelrenderer.postRender(scale);
        modelrenderer.rotationPointX -= f;
    }

    protected ModelRenderer getArmForSide(EnumHandSide side) {
        return side == EnumHandSide.LEFT ? this.bipedLeftArm : this.bipedRightArm;
    }
}