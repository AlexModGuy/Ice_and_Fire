package com.github.alexthe666.iceandfire.client.model;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.ModelAnimator;
import com.github.alexthe666.iceandfire.client.model.util.HideableModelRenderer;
import com.github.alexthe666.iceandfire.entity.EntityDreadGhoul;

public class ModelDreadGhoul extends ModelBipedBase<EntityDreadGhoul> {

    public AdvancedModelBox head2;
    public AdvancedModelBox clawsRight;
    public AdvancedModelBox clawsLeft;

    public ModelDreadGhoul(float modelScale) {
        super();
        this.texWidth = 128;
        this.texHeight = 64;
        this.head = new HideableModelRenderer(this, 0, 0);
        this.head.setPos(0.0F, 0.0F, 0.0F);
        this.head.addBox(-4.0F, -7.4F, -4.0F, 8, 8, 8, modelScale);
        this.setRotateAngle(head, 0.045553093477052F, 0.0F, 0.0F);
        this.legLeft = new HideableModelRenderer(this, 0, 16);
        this.legLeft.mirror = true;
        this.legLeft.setPos(1.9F, 12.0F, 0.1F);
        this.legLeft.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, modelScale);
        this.setRotateAngle(legLeft, -0.045553093477052F, 0.0F, 0.0F);
        this.armRight = new HideableModelRenderer(this, 40, 16);
        this.armRight.setPos(-4.0F, 2.0F, 0.0F);
        this.armRight.addBox(-3.0F, -2.0F, -2.0F, 3, 12, 4, modelScale);
        this.setRotateAngle(armRight, -0.136659280431156F, 0.091106186954104F, 0.22759093446006054F);
        this.armLeft = new HideableModelRenderer(this, 40, 16);
        this.armLeft.mirror = true;
        this.armLeft.setPos(4.0F, 2.0F, -0.0F);
        this.armLeft.addBox(0.0F, -2.0F, -2.0F, 3, 12, 4, modelScale);
        this.setRotateAngle(armLeft, -0.136659280431156F, 0.091106186954104F, -0.22759093446006054F);
        this.head2 = new AdvancedModelBox(this, 32, 0);
        this.head2.setPos(0.0F, 0.4F, 0.0F);
        this.head2.addBox(-4.5F, -6.4F, -4.1F, 9, 8, 8, modelScale);
        this.clawsLeft = new AdvancedModelBox(this, 56, 25);
        this.clawsLeft.mirror = true;
        this.clawsLeft.setPos(-0.5F, 11.0F, 0.0F);
        this.clawsLeft.addBox(-1.0F, -2.0F, -2.0F, 4, 3, 4, modelScale);
        this.setRotateAngle(clawsLeft, -0.0F, 0.0F, 0.2897246558310587F);
        this.body = new HideableModelRenderer(this, 16, 16);
        this.body.setPos(0.0F, 0.0F, 0.0F);
        this.body.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, modelScale);
        this.setRotateAngle(body, 0.045553093477052F, 0.0F, 0.0F);
        this.legRight = new HideableModelRenderer(this, 0, 16);
        this.legRight.setPos(-1.9F, 12.0F, 0.1F);
        this.legRight.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, modelScale);
        this.setRotateAngle(legRight, -0.045553093477052F, 0.0F, 0.0F);
        this.clawsRight = new AdvancedModelBox(this, 56, 25);
        this.clawsRight.setPos(0.5F, 11.0F, 0.0F);
        this.clawsRight.addBox(-3.0F, -2.0F, -2.0F, 4, 3, 4, modelScale);
        this.setRotateAngle(clawsRight, 0.0F, 0.0F, -0.2897246558310587F);
        this.body.addChild(this.head);
        this.body.addChild(this.legRight);
        this.body.addChild(this.legLeft);
        this.body.addChild(this.armRight);
        this.body.addChild(this.armLeft);
        this.head.addChild(this.head2);
        this.armLeft.addChild(this.clawsLeft);
        this.armRight.addChild(this.clawsRight);
        animator = ModelAnimator.create();
        this.updateDefaultPose();
    }

    @Override
    public void setupAnim(EntityDreadGhoul thrall, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        this.faceTarget(netHeadYaw, headPitch, 1.0F, head);
        animate(thrall, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, 0f);
        float speed_walk = 0.6F;
        float speed_idle = 0.05F;
        float degree_walk = 1F;
        if (thrall.getAnimation() == EntityDreadGhoul.ANIMATION_SPAWN) {
            if (thrall.getAnimationTick() < 30) {
                this.swing(armRight, 0.5F, 0.5F, false, 2, -0.7F, thrall.tickCount, 1);
                this.swing(armLeft, 0.5F, 0.5F, true, 2, -0.7F, thrall.tickCount, 1);
                this.flap(armRight, 0.5F, 0.5F, true, 1, 0, thrall.tickCount, 1);
                this.flap(armLeft, 0.5F, 0.5F, true, 1, 0, thrall.tickCount, 1);
            }
        }
        this.flap(armLeft, speed_idle, 0.15F, false, 2, -0.1F, thrall.tickCount, 1);
        this.flap(armRight, speed_idle, 0.15F, true, 2, -0.1F, thrall.tickCount, 1);
        this.walk(head, speed_idle, 0.1F, true, 1, -0.05F, thrall.tickCount, 1);

        this.walk(legRight, speed_walk, degree_walk, false, 0, 0, limbSwing, limbSwingAmount);
        this.walk(legLeft, speed_walk, degree_walk, true, 0, 0, limbSwing, limbSwingAmount);
        this.flap(legRight, speed_walk, degree_walk * 0.1F, true, 3, -0.05F, limbSwing, limbSwingAmount);
        this.flap(legLeft, speed_walk, degree_walk * 0.1F, true, 3, 0.05F, limbSwing, limbSwingAmount);
        this.flap(body, speed_walk, degree_walk * 0.1F, true, 1, 0, limbSwing, limbSwingAmount);

        this.walk(armRight, speed_walk, degree_walk, true, -2, 0, limbSwing, limbSwingAmount);
        this.walk(armLeft, speed_walk, degree_walk, false, -2, 0, limbSwing, limbSwingAmount);
        this.flap(armRight, speed_walk, degree_walk * 0.8F, true, -2, -0.1F, limbSwing, limbSwingAmount);
        this.flap(armLeft, speed_walk, degree_walk * 0.8F, true, -2, 0.1F, limbSwing, limbSwingAmount);
        this.flap(head, speed_walk, degree_walk * 0.2F, false, 0, 0, limbSwing, limbSwingAmount);

    }

    @Override
    void animate(EntityDreadGhoul entity, float limbSwing, float limbSwingAmount,
                 float ageInTicks, float netHeadYaw, float headPitch, float f) {
        this.resetToDefaultPose();
        animator.update(entity);
        if (animator.setAnimation(EntityDreadGhoul.ANIMATION_SLASH)) {
            animator.startKeyframe(5);
            rotate(animator, this.armRight, 20, 45, 80);
            rotate(animator, this.body, 0, 30, 0);
            rotate(animator, this.head, 0, -20, 0);
            animator.endKeyframe();
            animator.startKeyframe(5);
            rotate(animator, this.armRight, -80, -15, 10);
            rotate(animator, this.body, 0, -70, 0);
            rotate(animator, this.head, 0, 60, 0);
            animator.endKeyframe();
            animator.startKeyframe(5);
            rotate(animator, this.armLeft, 20, -45, -80);
            rotate(animator, this.body, 0, -30, 0);
            rotate(animator, this.head, 0, 20, 0);
            animator.endKeyframe();
            animator.startKeyframe(5);
            rotate(animator, this.armLeft, -80, 15, -10);
            rotate(animator, this.body, 0, 70, 0);
            rotate(animator, this.head, 0, -60, 0);
            animator.endKeyframe();
            animator.resetKeyframe(5);
        }
        if (animator.setAnimation(EntityDreadGhoul.ANIMATION_SPAWN)) {
            animator.startKeyframe(0);
            animator.move(this.body, 0, 35, 0);
            rotateMinus(animator, this.armLeft, -180, -90, 50);
            rotateMinus(animator, this.head, -60, 0, 0);
            rotateMinus(animator, this.armRight, -180, 90, -50);
            animator.endKeyframe();
            animator.startKeyframe(30);
            animator.move(this.body, 0, 0, 0);
            rotate(animator, this.armLeft, -30, -90, 0);
            rotate(animator, this.armRight, -30, 90, 0);
            animator.endKeyframe();
            animator.resetKeyframe(5);
        }
    }

}
