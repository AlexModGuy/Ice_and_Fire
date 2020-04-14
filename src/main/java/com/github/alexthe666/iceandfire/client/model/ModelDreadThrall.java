package com.github.alexthe666.iceandfire.client.model;

import com.github.alexthe666.iceandfire.client.model.util.HideableModelRenderer;
import com.github.alexthe666.iceandfire.entity.EntityDreadThrall;
import net.ilexiconn.llibrary.client.model.ModelAnimator;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelBase;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.client.model.ModelBase;
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

    public HideableModelRenderer bipedHead;
    public HideableModelRenderer bipedHeadwear;
    public HideableModelRenderer bipedBody;
    public HideableModelRenderer bipedRightArm;
    public HideableModelRenderer bipedLeftArm;
    public HideableModelRenderer bipedRightLeg;
    public HideableModelRenderer bipedLeftLeg;
    public ModelBiped.ArmPose leftArmPose;
    public ModelBiped.ArmPose rightArmPose;
    public boolean isSneak;
    private ModelAnimator animator;
    private boolean armor = false;

    public ModelDreadThrall(float modelSize, boolean armorArms) {
        this.armor = armorArms;
        this.textureHeight = 32;
        this.textureWidth = 64;
        this.leftArmPose = ModelBiped.ArmPose.EMPTY;
        this.rightArmPose = ModelBiped.ArmPose.EMPTY;
        this.bipedBody = new HideableModelRenderer(this, 16, 16);
        this.bipedBody.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, modelSize);
        this.bipedBody.setRotationPoint(0.0F, 0.0F, 0.0F);

            /*
                    this.bipedBody.addChild(bipedHead);
        this.bipedHead.addChild(bipedHeadwear);
        this.bipedBody.addChild(bipedRightArm);
        this.bipedBody.addChild(bipedLeftArm);
        this.bipedBody.addChild(bipedRightLeg);
        this.bipedBody.addChild(bipedLeftLeg);
             */
        this.bipedRightArm = new HideableModelRenderer(this, 40, 16);
        this.bipedRightArm.addBox(-1.0F, -2.0F, -1.0F, 2, 12, 2, modelSize);
        this.bipedRightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);
        this.bipedLeftArm = new HideableModelRenderer(this, 40, 16);
        this.bipedLeftArm.mirror = true;
        this.bipedLeftArm.addBox(-1.0F, -2.0F, -1.0F, 2, 12, 2, modelSize);
        this.bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
        this.bipedRightLeg = new HideableModelRenderer(this, 0, 16);
        this.bipedRightLeg.addBox(-1.0F, 0.0F, -1.0F, 2, 12, 2, modelSize);
        this.bipedRightLeg.setRotationPoint(-2.0F, 12.0F, 0.0F);
        this.bipedLeftLeg = new HideableModelRenderer(this, 0, 16);
        this.bipedLeftLeg.mirror = true;
        this.bipedLeftLeg.addBox(-1.0F, 0.0F, -1.0F, 2, 12, 2, modelSize);
        this.bipedLeftLeg.setRotationPoint(2.0F, 12.0F, 0.0F);
        this.bipedHead = new HideableModelRenderer(this, 0, 0);
        this.bipedHead.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, modelSize - 0.5F);
        this.bipedHead.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.bipedHeadwear = new HideableModelRenderer(this, 32, 0);
        this.bipedHeadwear.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, modelSize);
        this.bipedHeadwear.setRotationPoint(0.0F, 0.0F, 0.0F);
        if (armorArms) {
            this.bipedHead = new HideableModelRenderer(this, 0, 0);
            this.bipedHead.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, modelSize);
            this.bipedHead.setRotationPoint(0.0F, 0.0F + 0.0F, 0.0F);
            this.bipedHeadwear = new HideableModelRenderer(this, 32, 0);
            this.bipedHeadwear.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, modelSize + 0.5F);
            this.bipedHeadwear.setRotationPoint(0.0F, 0.0F + 0.0F, 0.0F);
            this.bipedBody = new HideableModelRenderer(this, 16, 16);
            this.bipedBody.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, modelSize);
            this.bipedBody.setRotationPoint(0.0F, 0.0F + 0.0F, 0.0F);
            this.bipedRightArm = new HideableModelRenderer(this, 40, 16);
            this.bipedRightArm.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, modelSize);
            this.bipedRightArm.setRotationPoint(-5.0F, 2.0F + 0.0F, 0.0F);
            this.bipedLeftArm = new HideableModelRenderer(this, 40, 16);
            this.bipedLeftArm.mirror = true;
            this.bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, modelSize);
            this.bipedLeftArm.setRotationPoint(5.0F, 2.0F + 0.0F, 0.0F);
            this.bipedRightLeg = new HideableModelRenderer(this, 0, 16);
            this.bipedRightLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, modelSize);
            this.bipedRightLeg.setRotationPoint(-1.9F, 12.0F + 0.0F, 0.0F);
            this.bipedLeftLeg = new HideableModelRenderer(this, 0, 16);
            this.bipedLeftLeg.mirror = true;
            this.bipedLeftLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, modelSize);
            this.bipedLeftLeg.setRotationPoint(1.9F, 12.0F + 0.0F, 0.0F);
        }
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

        super.setLivingAnimations(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTickTime);
    }

    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
        super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
        this.resetToDefaultPose();
        animate((IAnimatedEntity) entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
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
        float f = 1.0F;
        this.bipedRightArm.rotateAngleX += MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 2.0F * limbSwingAmount * 0.5F / f;
        this.bipedLeftArm.rotateAngleX += MathHelper.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F / f;
        this.bipedRightLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount / f;
        this.bipedLeftLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount / f;
        this.bipedRightLeg.rotateAngleY = 0.0F;
        this.bipedLeftLeg.rotateAngleY = 0.0F;
        this.bipedRightLeg.rotateAngleZ = 0.0F;
        this.bipedLeftLeg.rotateAngleZ = 0.0F;

        if (this.isRiding) {
            this.bipedRightArm.rotateAngleX += -((float) Math.PI / 5F);
            this.bipedLeftArm.rotateAngleX += -((float) Math.PI / 5F);
            this.bipedRightLeg.rotateAngleX = -1.4137167F;
            this.bipedRightLeg.rotateAngleY = ((float) Math.PI / 10F);
            this.bipedRightLeg.rotateAngleZ = 0.07853982F;
            this.bipedLeftLeg.rotateAngleX = -1.4137167F;
            this.bipedLeftLeg.rotateAngleY = -((float) Math.PI / 10F);
            this.bipedLeftLeg.rotateAngleZ = -0.07853982F;
        }
        if (this.swingProgress > 0.0F) {
            EnumHandSide enumhandside = this.getMainHand(entityIn);
            ModelRenderer modelrenderer = this.getArmForSide(enumhandside);
            float f1 = this.swingProgress;
            this.bipedBody.rotateAngleY = MathHelper.sin(MathHelper.sqrt(f1) * ((float) Math.PI * 2F)) * 0.2F;

            if (enumhandside == EnumHandSide.LEFT) {
                this.bipedBody.rotateAngleY *= -1.0F;
            }

            this.bipedRightArm.rotationPointZ = MathHelper.sin(this.bipedBody.rotateAngleY) * 5.0F;
            this.bipedRightArm.rotationPointX = -MathHelper.cos(this.bipedBody.rotateAngleY) * 5.0F;
            this.bipedLeftArm.rotationPointZ = -MathHelper.sin(this.bipedBody.rotateAngleY) * 5.0F;
            this.bipedLeftArm.rotationPointX = MathHelper.cos(this.bipedBody.rotateAngleY) * 5.0F;
            this.bipedRightArm.rotateAngleY += this.bipedBody.rotateAngleY;
            this.bipedLeftArm.rotateAngleY += this.bipedBody.rotateAngleY;
            this.bipedLeftArm.rotateAngleX += this.bipedBody.rotateAngleY;
            f1 = 1.0F - this.swingProgress;
            f1 = f1 * f1;
            f1 = f1 * f1;
            f1 = 1.0F - f1;
            float f2 = MathHelper.sin(f1 * (float) Math.PI);
            float f3 = MathHelper.sin(this.swingProgress * (float) Math.PI) * -(this.bipedHead.rotateAngleX - 0.7F) * 0.75F;
            modelrenderer.rotateAngleX = (float) ((double) modelrenderer.rotateAngleX - ((double) f2 * 1.2D + (double) f3));
            modelrenderer.rotateAngleY += this.bipedBody.rotateAngleY * 2.0F;
            modelrenderer.rotateAngleZ += MathHelper.sin(this.swingProgress * (float) Math.PI) * -0.4F;
        }
        if (this.isSneak) {
            this.bipedBody.rotateAngleX = 0.5F;
            this.bipedRightArm.rotateAngleX += 0.4F;
            this.bipedLeftArm.rotateAngleX += 0.4F;
            this.bipedRightLeg.rotationPointZ = 4.0F;
            this.bipedLeftLeg.rotationPointZ = 4.0F;
            this.bipedRightLeg.rotationPointY = 9.0F;
            this.bipedLeftLeg.rotationPointY = 9.0F;
            this.bipedHead.rotationPointY = 1.0F;
        } else {
            this.bipedBody.rotateAngleX = 0.0F;
            this.bipedRightLeg.rotationPointZ = 0.1F;
            this.bipedLeftLeg.rotationPointZ = 0.1F;
            this.bipedRightLeg.rotationPointY = 12.0F;
            this.bipedLeftLeg.rotationPointY = 12.0F;
            this.bipedHead.rotationPointY = 0.0F;
        }

        this.bipedRightArm.rotateAngleZ += MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
        this.bipedLeftArm.rotateAngleZ -= MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
        this.bipedRightArm.rotateAngleX += MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
        this.bipedLeftArm.rotateAngleX -= MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
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
        this.flap(bipedBody, 0.5F, 0.15F, false, 1, 0F, limbSwing, limbSwingAmount);

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
        this.bipedBody.postRender(scale);
        this.getArmForSide(side).postRender(scale);
    }

    protected ModelRenderer getArmForSide(EnumHandSide side) {
        return side == EnumHandSide.LEFT ? this.bipedLeftArm : this.bipedRightArm;
    }

    protected EnumHandSide getMainHand(Entity entityIn) {
        if (entityIn instanceof EntityLivingBase) {
            EntityLivingBase entitylivingbase = (EntityLivingBase) entityIn;
            EnumHandSide enumhandside = entitylivingbase.getPrimaryHand();
            return entitylivingbase.swingingHand == EnumHand.MAIN_HAND ? enumhandside : enumhandside.opposite();
        } else {
            return EnumHandSide.RIGHT;
        }
    }

    public void setModelAttributes(ModelBase model) {
        super.setModelAttributes(model);
        if (model instanceof ModelBiped) {
            ModelBiped modelbiped = (ModelBiped) model;
            this.leftArmPose = modelbiped.leftArmPose;
            this.rightArmPose = modelbiped.rightArmPose;
            this.isSneak = modelbiped.isSneak;
        }
    }

    public void setVisible(boolean visible) {
        this.bipedHead.invisible = !visible;
        this.bipedHeadwear.invisible = !visible;
        this.bipedBody.invisible = !visible;
        this.bipedRightArm.invisible = !visible;
        this.bipedLeftArm.invisible = !visible;
        this.bipedRightLeg.invisible = !visible;
        this.bipedLeftLeg.invisible = !visible;
    }
}