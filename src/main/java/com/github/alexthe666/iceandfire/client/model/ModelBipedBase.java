package com.github.alexthe666.iceandfire.client.model;

import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.ModelAnimator;
import com.github.alexthe666.iceandfire.client.model.util.EntityModelPartBuilder;
import com.github.alexthe666.iceandfire.client.model.util.HideableModelRenderer;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.IHasArm;
import net.minecraft.client.renderer.entity.model.IHasHead;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.MathHelper;

import java.util.ArrayList;
import java.util.List;

public abstract class ModelBipedBase<T extends LivingEntity & IAnimatedEntity> extends AdvancedEntityModel<T> implements ICustomStatueModel, IHasHead, IHasArm {

    private final List<AdvancedModelBox> advancedPartList = new ArrayList<>();
    public HideableModelRenderer head;
    public HideableModelRenderer headware;
    public HideableModelRenderer body;
    public HideableModelRenderer armRight;
    public HideableModelRenderer armLeft;
    public HideableModelRenderer legRight;
    public HideableModelRenderer legLeft;
    public BipedModel.ArmPose leftArmPose;
    public BipedModel.ArmPose rightArmPose;
    public boolean isSneak;
    protected ModelAnimator animator;

    @Override
    public ModelRenderer getHead() {
        return this.head;
    }

    @Override
    public void translateToHand(HandSide sideIn, MatrixStack matrixStackIn) {
        this.getArmForSide(sideIn).translateAndRotate(matrixStackIn);
    }

    protected ModelRenderer getArmForSide(HandSide side) {
        return side == HandSide.LEFT ? this.armLeft : this.armRight;
    }

    protected HandSide getMainHand(Entity entityIn) {
        if (entityIn instanceof LivingEntity) {
            LivingEntity LivingEntity = (LivingEntity) entityIn;
            HandSide Handside = LivingEntity.getMainArm();
            return LivingEntity.swingingArm == Hand.MAIN_HAND ? Handside : Handside.getOpposite();
        } else {
            return HandSide.RIGHT;
        }
    }

    @Override
    public void rotate(ModelAnimator animator, AdvancedModelBox model, float x, float y, float z) {
        animator.rotate(model, (float) Math.toRadians(x), (float) Math.toRadians(y), (float) Math.toRadians(z));
    }

    @Override
    public void rotateMinus(ModelAnimator animator, AdvancedModelBox model, float x, float y, float z) {
        animator.rotate(model, (float) Math.toRadians(x) - model.defaultRotationX, (float) Math.toRadians(y) - model.defaultRotationY, (float) Math.toRadians(z) - model.defaultRotationZ);
    }

    public void progressRotationInterp(AdvancedModelBox model, float progress, float rotX, float rotY, float rotZ, float max) {
        model.xRot += progress * (rotX - model.defaultRotationX) / max;
        model.yRot += progress * (rotY - model.defaultRotationY) / max;
        model.zRot += progress * (rotZ - model.defaultRotationZ) / max;
    }

    public void progresPositionInterp(AdvancedModelBox model, float progress, float x, float y, float z, float max) {
        model.x += progress * (x) / max;
        model.y += progress * (y) / max;
        model.z += progress * (z) / max;
    }

    public void progressRotation(AdvancedModelBox model, float progress, float rotX, float rotY, float rotZ) {
        model.xRot += progress * (rotX - model.defaultRotationX) / 20.0F;
        model.yRot += progress * (rotY - model.defaultRotationY) / 20.0F;
        model.zRot += progress * (rotZ - model.defaultRotationZ) / 20.0F;
    }

    public void progressRotationPrev(AdvancedModelBox model, float progress, float rotX, float rotY, float rotZ) {
        model.xRot += progress * (rotX) / 20.0F;
        model.yRot += progress * (rotY) / 20.0F;
        model.zRot += progress * (rotZ) / 20.0F;
    }

    public void progressPosition(AdvancedModelBox model, float progress, float x, float y, float z) {
        model.x += progress * (x - model.defaultPositionX) / 20.0F;
        model.y += progress * (y - model.defaultPositionY) / 20.0F;
        model.z += progress * (z - model.defaultPositionZ) / 20.0F;
    }

    public void progressPositionPrev(AdvancedModelBox model, float progress, float x, float y, float z) {
        model.x += progress * x / 20.0F;
        model.y += progress * y / 20.0F;
        model.z += progress * z / 20.0F;
    }

    @Override
    public void setRotateAngle(AdvancedModelBox modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }

    @Override
    public void accept(ModelRenderer p_accept_1_) {
        if (p_accept_1_ instanceof AdvancedModelBox) {
            advancedPartList.add((AdvancedModelBox) p_accept_1_);
        }
    }

    @Override
    public void updateDefaultPose() {
        advancedPartList.forEach((modelRenderer) -> {
            modelRenderer.updateDefaultPose();
        });
    }

    @Override
    public void resetToDefaultPose() {
        advancedPartList.forEach((modelRenderer) -> {
            modelRenderer.resetToDefaultPose();
        });
    }

    public void setModelAttributes(ModelBipedBase<T> modelIn) {
        super.copyPropertiesTo(modelIn);
        modelIn.animator = this.animator;
        modelIn.leftArmPose = this.leftArmPose;
        modelIn.rightArmPose = this.rightArmPose;
        modelIn.isSneak = this.isSneak;
        modelIn.head.copyFrom(this.head);
        modelIn.headware.copyFrom(this.headware);
        modelIn.body.copyFrom(this.body);
        modelIn.armRight.copyFrom(this.armRight);
        modelIn.armLeft.copyFrom(this.armLeft);
        modelIn.legRight.copyFrom(this.legRight);
        modelIn.legLeft.copyFrom(this.legLeft);
    }

    public void setModelAttributes(BipedModel<T> modelIn) {
        super.copyPropertiesTo(modelIn);
        modelIn.leftArmPose = this.leftArmPose;
        modelIn.rightArmPose = this.rightArmPose;
        modelIn.crouching = this.isSneak;
        modelIn.head.copyFrom(this.head);
        modelIn.hat.copyFrom(this.headware);
        modelIn.body.copyFrom(this.body);
        modelIn.rightArm.copyFrom(this.armRight);
        modelIn.leftArm.copyFrom(this.armLeft);
        modelIn.rightLeg.copyFrom(this.legRight);
        modelIn.leftLeg.copyFrom(this.legLeft);
    }

    public void setVisible(boolean visible) {
        this.head.invisible = !visible;
        this.headware.invisible = !visible;
        this.body.invisible = !visible;
        this.armRight.invisible = !visible;
        this.armLeft.invisible = !visible;
        this.legRight.invisible = !visible;
        this.legLeft.invisible = !visible;
    }

    public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        animate(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, 0);
        this.faceTarget(netHeadYaw, headPitch, 1.0F, head);
        float f = 1.0F;
        this.armRight.xRot += MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 2.0F * limbSwingAmount * 0.5F / f;
        this.armLeft.xRot += MathHelper.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F / f;
        this.legRight.xRot = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount / f;
        this.legLeft.xRot = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount / f;
        this.legRight.yRot = 0.0F;
        this.legLeft.yRot = 0.0F;
        this.legRight.zRot = 0.0F;
        this.legLeft.zRot = 0.0F;

        if (entityIn.isPassenger()) {
            this.armRight.xRot += -((float) Math.PI / 5F);
            this.armLeft.xRot += -((float) Math.PI / 5F);
            this.legRight.xRot = -1.4137167F;
            this.legRight.yRot = ((float) Math.PI / 10F);
            this.legRight.zRot = 0.07853982F;
            this.legLeft.xRot = -1.4137167F;
            this.legLeft.yRot = -((float) Math.PI / 10F);
            this.legLeft.zRot = -0.07853982F;
        }
        if (this.attackTime > 0.0F) {
            HandSide handSide = this.getMainHand(entityIn);
            ModelRenderer modelrenderer = this.getArmForSide(handSide);
            float f1 = this.attackTime;
            this.body.yRot = MathHelper.sin(MathHelper.sqrt(f1) * ((float) Math.PI * 2F)) * 0.2F;

            if (handSide == HandSide.LEFT) {
                this.body.yRot *= -1.0F;
            }

            this.armRight.z = MathHelper.sin(this.body.yRot) * 5.0F;
            this.armRight.x = -MathHelper.cos(this.body.yRot) * 5.0F;
            this.armLeft.z = -MathHelper.sin(this.body.yRot) * 5.0F;
            this.armLeft.x = MathHelper.cos(this.body.yRot) * 5.0F;
            this.armRight.yRot += this.body.yRot;
            this.armLeft.yRot += this.body.yRot;
            this.armLeft.xRot += this.body.xRot;
            f1 = 1.0F - this.attackTime;
            f1 = f1 * f1;
            f1 = f1 * f1;
            f1 = 1.0F - f1;
            float f2 = MathHelper.sin(f1 * (float) Math.PI);
            float f3 = MathHelper.sin(this.attackTime * (float) Math.PI) * -(this.head.xRot - 0.7F) * 0.75F;
            modelrenderer.xRot = (float) ((double) modelrenderer.xRot - ((double) f2 * 1.2D + (double) f3));
            modelrenderer.yRot += this.body.yRot * 2.0F;
            modelrenderer.zRot += MathHelper.sin(this.attackTime * (float) Math.PI) * -0.4F;
        }
        if (this.isSneak) {
            this.body.xRot = 0.5F;
            this.armRight.xRot += 0.4F;
            this.armLeft.xRot += 0.4F;
            this.legRight.z = 4.0F;
            this.legLeft.z = 4.0F;
            this.legRight.y = 9.0F;
            this.legLeft.y = 9.0F;
            this.head.y = 1.0F;
        } else {
            this.body.xRot = 0.0F;
            this.legRight.z = 0.1F;
            this.legLeft.z = 0.1F;
            this.legRight.y = 12.0F;
            this.legLeft.y = 12.0F;
            this.head.y = 0.0F;
        }

        this.armRight.zRot += MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
        this.armLeft.zRot -= MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
        this.armRight.xRot += MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
        this.armLeft.xRot -= MathHelper.sin(ageInTicks * 0.067F) * 0.05F;

    }

    @Override
    public Iterable<ModelRenderer> parts() {
        return ImmutableList.of(body);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return EntityModelPartBuilder.getAllPartsFromClass(this.getClass(), this.getClass().getName());
    }

    @Override
    public void renderStatue(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, Entity living) {
        this.renderToBuffer(matrixStackIn, bufferIn, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
    }

    abstract void animate(T entity, float limbSwing, float limbSwingAmount,
                          float ageInTicks, float netHeadYaw, float headPitch, float f);
}
