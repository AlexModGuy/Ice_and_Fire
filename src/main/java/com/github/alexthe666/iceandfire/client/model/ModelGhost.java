package com.github.alexthe666.iceandfire.client.model;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.ModelAnimator;
import com.github.alexthe666.iceandfire.client.model.util.HideableModelRenderer;
import com.github.alexthe666.iceandfire.entity.EntityGhost;
import com.google.common.collect.ImmutableList;

public class ModelGhost extends ModelBipedBase<EntityGhost> {
    public AdvancedModelBox robe;
    public AdvancedModelBox mask;
    public AdvancedModelBox hood;
    public AdvancedModelBox jaw;
    public AdvancedModelBox sleeveRight;
    public AdvancedModelBox robeLowerRight;
    public AdvancedModelBox robeLowerLeft;
    public AdvancedModelBox sleeveLeft;

    public ModelGhost(float modelScale) {
        super();
        this.texWidth = 128;
        this.texHeight = 64;
        this.sleeveRight = new AdvancedModelBox(this, 33, 35);
        this.sleeveRight.setPos(0.0F, -0.1F, 0.0F);
        this.sleeveRight.addBox(-2.2F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, 0.0F, 0.0F, modelScale);
        this.body = new HideableModelRenderer(this, 16, 16);
        this.body.setPos(0.0F, 0.0F, 0.0F);
        this.body.addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 0.0F, 0.0F, modelScale);
        this.robeLowerRight = new AdvancedModelBox(this, 48, 35);
        this.robeLowerRight.mirror = true;
        this.robeLowerRight.setPos(0.0F, 0.0F, 0.0F);
        this.robeLowerRight.addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, 0.0F, modelScale);
        this.armLeft = new HideableModelRenderer(this, 40, 16);
        this.armLeft.mirror = true;
        this.armLeft.setPos(5.0F, 2.0F, -0.0F);
        this.armLeft.addBox(-1.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F, 0.0F, 0.0F, modelScale);
        this.setRotateAngle(armLeft, -1.4570009181544104F, 0.10000736647217022F, -0.10000736647217022F);
        this.jaw = new AdvancedModelBox(this, 98, 8);
        this.jaw.setPos(0.0F, -1.4F, 0.2F);
        this.jaw.addBox(-2.0F, 0.0F, -4.5F, 4.0F, 2.0F, 8.0F, 0.0F, 0.0F, modelScale);
        this.setRotateAngle(jaw, 0.13665927909957545F, 0.0F, 0.0F);
        this.hood = new AdvancedModelBox(this, 60, 0);
        this.hood.setPos(0.0F, 0.0F, 0.0F);
        this.hood.addBox(-4.5F, -8.6F, -4.5F, 9.0F, 9.0F, 9.0F, 0.0F, 0.0F, modelScale);
        this.legLeft = new HideableModelRenderer(this, 0, 16);
        this.legLeft.mirror = true;
        this.legLeft.setPos(2.2F, 12.0F, 0.1F);
        this.legLeft.addBox(-1.0F, 0.0F, -1.0F, 2.0F, 12.0F, 2.0F, 0.0F, 0.0F, modelScale);
        this.head = new HideableModelRenderer(this, 0, 0);
        this.head.setPos(0.0F, 0.0F, 0.0F);
        this.head.addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.0F, 0.0F, modelScale);
        this.mask = new AdvancedModelBox(this, 40, 8);
        this.mask.setPos(0.0F, 0.0F, 0.0F);
        this.mask.addBox(-4.0F, -8.6F, -4.4F, 8.0F, 8.0F, 0.0F, 0.0F, 0.0F, modelScale);
        this.armRight = new HideableModelRenderer(this, 40, 16);
        this.armRight.setPos(-5.0F, 2.0F, 0.0F);
        this.armRight.addBox(-1.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F, 0.0F, 0.0F, modelScale);
        this.setRotateAngle(armRight, -1.4570009181544104F, -0.10000736647217022F, 0.10000736647217022F);
        this.robe = new AdvancedModelBox(this, 4, 34);
        this.robe.setPos(0.0F, 0.1F, 0.0F);
        this.robe.addBox(-4.5F, 0.0F, -2.5F, 9.0F, 12.0F, 5.0F, 0.0F, 0.0F, modelScale);
        this.legRight = new HideableModelRenderer(this, 0, 16);
        this.legRight.setPos(-2.3F, 12.0F, 0.1F);
        this.legRight.addBox(-1.0F, 0.0F, -1.0F, 2.0F, 12.0F, 2.0F, 0.0F, 0.0F, modelScale);
        this.robeLowerLeft = new AdvancedModelBox(this, 48, 35);
        this.robeLowerLeft.setPos(0.0F, 0.0F, 0.0F);
        this.robeLowerLeft.addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, 0.0F, modelScale);
        this.sleeveLeft = new AdvancedModelBox(this, 33, 35);
        this.sleeveLeft.mirror = true;
        this.sleeveLeft.setPos(0.0F, -0.1F, 0.0F);
        this.sleeveLeft.addBox(-1.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, 0.0F, 0.0F, modelScale);
        this.armRight.addChild(this.sleeveRight);
        this.legRight.addChild(this.robeLowerRight);
        this.body.addChild(this.armLeft);
        this.mask.addChild(this.jaw);
        this.head.addChild(this.hood);
        this.body.addChild(this.legLeft);
        this.body.addChild(this.head);
        this.head.addChild(this.mask);
        this.body.addChild(this.armRight);
        this.body.addChild(this.robe);
        this.body.addChild(this.legRight);
        this.legLeft.addChild(this.robeLowerLeft);
        this.armLeft.addChild(this.sleeveLeft);
        animator = ModelAnimator.create();
        this.updateDefaultPose();
    }

    @Override
    public void setupAnim(EntityGhost entity, float f, float f1, float f2, float f3, float f4) {
        this.resetToDefaultPose();
        animate(entity, f, f1, f2, f3, f4, 1);
        this.faceTarget(f3, f4, 1, this.head);
        float speed_walk = 0.6F;
        float speed_idle = 0.05F;
        float degree_walk = 1F;
        float degree_idle = 0.25F;

        float f12 = (float) Math.toRadians(-1.29f) + f1;
        if (f12 < 0.0F) {
            f12 = 0.0F;
        }
        if (f12 > Math.toRadians(20)) {
            f12 = (float) Math.toRadians(20);
        }
        this.body.rotateAngleX = f12;
        this.head.rotateAngleX = this.head.rotateAngleX - f12;
        this.armRight.rotateAngleX = this.armRight.rotateAngleX - f12;
        this.armLeft.rotateAngleX = this.armLeft.rotateAngleX - f12;

        this.walk(jaw, speed_idle * 2F, degree_idle * 0.5F, false, 0, 0.1F, f2, 1);
        this.walk(armRight, speed_idle * 1.5F, degree_idle * 0.4F, false, 2, 0.0F, f2, 1);
        this.walk(armLeft, speed_idle * 1.5F, degree_idle * 0.4F, true, 2, 0.0F, f2, 1);
        this.flap(armRight, speed_idle * 1.5F, degree_idle * 0.2F, false, 2, 0.2F, f2, 1);
        this.flap(armRight, speed_idle * 1.5F, degree_idle * 0.2F, true, 2, 0.2F, f2, 1);
        this.walk(legLeft, speed_idle * 1.5F, degree_idle * 0.4F, false, 2, 0.2F, f2, 1);
        this.walk(legRight, speed_idle * 1.5F, degree_idle * 0.4F, false, 2, 0.2F, f2, 1);
        this.flap(body, speed_idle, degree_idle * 0.1F, true, 3, 0, f2, 1);
        this.bob(body, speed_idle * 0.5F, degree_idle * 4.1F, false, f2, 1);
        this.bob(body, speed_walk * 0.75F, degree_walk * 2.1F, false, f, f1);

    }

    @Override
    public void animate(EntityGhost entity, float f, float f1, float f2, float f3, float f4, float f5) {
        animator.update(entity);
        if (animator.setAnimation(EntityGhost.ANIMATION_SCARE)) {
            animator.startKeyframe(5);
            animator.move(head, 0, -2, 0);
            animator.move(armLeft, 0, 1, 0);
            animator.move(armRight, 0, 1, 0);
            this.rotateMinus(animator, head, 0, 20, 0);
            this.rotateMinus(animator, jaw, 20, 10, 0);
            this.rotateMinus(animator, armLeft, 30, -12.5F, -70F);
            this.rotateMinus(animator, armRight, -30, 12.5F, 70F);
            animator.endKeyframe();
            animator.startKeyframe(5);
            animator.move(head, 0, -1, 0);
            animator.move(armLeft, 0, -1, 0);
            animator.move(armRight, 0, -1, 0);
            this.rotateMinus(animator, head, 0, -20, 0);
            this.rotateMinus(animator, jaw, 20, -10, 0);
            this.rotateMinus(animator, armLeft, -20, -25, -140);
            this.rotateMinus(animator, armRight, 20, 25, 140);
            animator.endKeyframe();
            animator.startKeyframe(5);
            animator.move(head, 0, -2, 0);
            animator.move(armLeft, 0, 1, 0);
            animator.move(armRight, 0, 1, 0);
            this.rotateMinus(animator, head, 0, 20, 0);
            this.rotateMinus(animator, jaw, 20, 10, 0);
            this.rotateMinus(animator, armLeft, 30, -25, -140);
            this.rotateMinus(animator, armRight, -30, 25, 140);
            animator.endKeyframe();
            animator.startKeyframe(5);
            animator.move(head, 0, -1, 0);
            animator.move(armLeft, 0, -1, 0);
            animator.move(armRight, 0, -1, 0);
            this.rotateMinus(animator, head, 0, -20, 0);
            this.rotateMinus(animator, jaw, 20, -10, 0);
            this.rotateMinus(animator, armLeft, -20, -25, -140);
            this.rotateMinus(animator, armRight, 20, 25, 140);
            animator.endKeyframe();
            animator.resetKeyframe(10);
        }
        if (animator.setAnimation(EntityGhost.ANIMATION_HIT)) {
            animator.startKeyframe(5);
            animator.move(head, 0, -1, 0);
            this.rotateMinus(animator, body, 0, 0F, 0F);
            this.rotateMinus(animator, head, -20, 0F, 0F);
            this.rotateMinus(animator, jaw, 70, 0F, 0F);
            this.rotateMinus(animator, armRight, -180F, 0F, -30F);
            this.rotateMinus(animator, armLeft, -180F, 0F, 30F);
            animator.endKeyframe();
            animator.resetKeyframe(5);
        }
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(head, headware, body, armRight, armLeft, legRight, legLeft, robe, mask, hood, jaw,
            sleeveRight, robeLowerRight, robeLowerLeft, sleeveLeft);
    }

}
