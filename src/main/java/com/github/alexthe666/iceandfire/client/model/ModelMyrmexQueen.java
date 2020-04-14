package com.github.alexthe666.iceandfire.client.model;

import com.github.alexthe666.iceandfire.entity.EntityMyrmexQueen;
import net.ilexiconn.llibrary.client.model.ModelAnimator;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class ModelMyrmexQueen extends ModelMyrmexBase {
    public AdvancedModelRenderer Body2;
    public AdvancedModelRenderer Body3;
    public AdvancedModelRenderer Body1;
    public AdvancedModelRenderer legTopR2;
    public AdvancedModelRenderer legTopR2_1;
    public AdvancedModelRenderer Body4;
    public AdvancedModelRenderer legTopR3;
    public AdvancedModelRenderer legTopR3_1;
    public AdvancedModelRenderer Body5;
    public AdvancedModelRenderer Tail1;
    public AdvancedModelRenderer Tail2;
    public AdvancedModelRenderer Tail3;
    public AdvancedModelRenderer Stinger;
    public AdvancedModelRenderer legMidR3;
    public AdvancedModelRenderer legBottomR3;
    public AdvancedModelRenderer legMidR3_1;
    public AdvancedModelRenderer legBottomR3_1;
    public AdvancedModelRenderer Neck1;
    public AdvancedModelRenderer legTopR1;
    public AdvancedModelRenderer legTopR1_1;
    public AdvancedModelRenderer HeadBase;
    public AdvancedModelRenderer EyeR;
    public AdvancedModelRenderer MandibleL;
    public AdvancedModelRenderer MandibleR;
    public AdvancedModelRenderer EyeL;
    public AdvancedModelRenderer crestbase;
    public AdvancedModelRenderer crest1;
    public AdvancedModelRenderer crest2;
    public AdvancedModelRenderer legMidR1;
    public AdvancedModelRenderer legBottomR1;
    public AdvancedModelRenderer legMidR1_1;
    public AdvancedModelRenderer legBottomR1_1;
    public AdvancedModelRenderer legMidR2;
    public AdvancedModelRenderer legBottomR2;
    public AdvancedModelRenderer legMidR2_1;
    public AdvancedModelRenderer legBottomR2_1;
    private ModelAnimator animator;

    public ModelMyrmexQueen() {
        this.textureWidth = 128;
        this.textureHeight = 128;
        this.Tail3 = new AdvancedModelRenderer(this, 55, 12);
        this.Tail3.setRotationPoint(0.0F, 0.3F, 11.0F);
        this.Tail3.addBox(-4.0F, -2.7F, -0.1F, 8, 8, 11, 0.0F);
        this.setRotateAngle(Tail3, -0.22759093446006054F, 0.0F, 0.0F);
        this.crest1 = new AdvancedModelRenderer(this, 0, 88);
        this.crest1.mirror = true;
        this.crest1.setRotationPoint(-1.2F, -0.1F, -1.0F);
        this.crest1.addBox(-1.5F, -2.51F, -0.1F, 3, 1, 6, 0.0F);
        this.setRotateAngle(crest1, 0.40980330836826856F, -0.17453292519943295F, -0.2792526803190927F);
        this.legBottomR3_1 = new AdvancedModelRenderer(this, 22, 51);
        this.legBottomR3_1.mirror = true;
        this.legBottomR3_1.setRotationPoint(0.0F, 10.4F, 0.0F);
        this.legBottomR3_1.addBox(-1.01F, 0.0F, -0.9F, 2, 13, 2, 0.0F);
        this.setRotateAngle(legBottomR3_1, 0.0F, 0.0F, 1.3203415791337103F);
        this.Stinger = new AdvancedModelRenderer(this, 60, 0);
        this.Stinger.setRotationPoint(0.0F, 1.6F, 11.0F);
        this.Stinger.addBox(-1.0F, -2.7F, -1.7F, 2, 8, 2, 0.0F);
        this.setRotateAngle(Stinger, 0.6373942428283291F, 0.0F, 0.0F);
        this.Body1 = new AdvancedModelRenderer(this, 34, 47);
        this.Body1.setRotationPoint(0.0F, -0.7F, -1.0F);
        this.Body1.addBox(-3.5F, -2.1F, -6.3F, 7, 8, 9, 0.0F);
        this.setRotateAngle(Body1, 0.045553093477052F, 0.0F, 0.0F);
        this.Body2 = new AdvancedModelRenderer(this, 70, 53);
        this.Body2.setRotationPoint(0.0F, 10.0F, -6.0F);
        this.Body2.addBox(-3.0F, -2.7F, -0.1F, 6, 7, 4, 0.0F);
        this.setRotateAngle(Body2, -0.045553093477052F, 0.0F, 0.0F);
        this.legTopR2_1 = new AdvancedModelRenderer(this, 0, 54);
        this.legTopR2_1.mirror = true;
        this.legTopR2_1.setRotationPoint(-3.3F, 1.0F, 1.6F);
        this.legTopR2_1.addBox(-1.0F, 0.0F, -1.5F, 2, 7, 3, 0.0F);
        this.setRotateAngle(legTopR2_1, 0.0F, 0.0F, 0.6981317007977318F);
        this.legBottomR1_1 = new AdvancedModelRenderer(this, 22, 51);
        this.legBottomR1_1.mirror = true;
        this.legBottomR1_1.setRotationPoint(0.0F, 10.4F, 0.0F);
        this.legBottomR1_1.addBox(-1.01F, 0.0F, -0.9F, 2, 13, 2, 0.0F);
        this.setRotateAngle(legBottomR1_1, 0.0F, 0.0F, 1.3203415791337103F);
        this.Tail2 = new AdvancedModelRenderer(this, 80, 76);
        this.Tail2.setRotationPoint(0.0F, 0.3F, 12.2F);
        this.Tail2.addBox(-5.0F, -2.7F, -0.1F, 10, 10, 13, 0.0F);
        this.setRotateAngle(Tail2, -0.091106186954104F, 0.0F, 0.0F);
        this.legMidR1 = new AdvancedModelRenderer(this, 11, 50);
        this.legMidR1.setRotationPoint(0.0F, 6.4F, 0.1F);
        this.legMidR1.addBox(-1.5F, 0.0F, -1.0F, 3, 12, 2, 0.0F);
        this.setRotateAngle(legMidR1, 0.0F, 0.0F, 1.1383037381507017F);
        this.MandibleL = new AdvancedModelRenderer(this, 0, 25);
        this.MandibleL.setRotationPoint(3.4F, 3.7F, -7.7F);
        this.MandibleL.addBox(-2.0F, -2.51F, -5.1F, 4, 2, 6, 0.0F);
        this.setRotateAngle(MandibleL, 0.17453292519943295F, 0.18203784098300857F, 0.0F);
        this.legBottomR2 = new AdvancedModelRenderer(this, 22, 51);
        this.legBottomR2.mirror = true;
        this.legBottomR2.setRotationPoint(0.0F, 10.4F, 0.0F);
        this.legBottomR2.addBox(-1.01F, 0.0F, -0.9F, 2, 13, 2, 0.0F);
        this.setRotateAngle(legBottomR2, 0.0F, 0.0F, 1.3203415791337103F);
        this.legMidR3 = new AdvancedModelRenderer(this, 11, 50);
        this.legMidR3.setRotationPoint(0.0F, 6.4F, 0.1F);
        this.legMidR3.addBox(-1.5F, 0.0F, -1.0F, 3, 12, 2, 0.0F);
        this.setRotateAngle(legMidR3, 0.0F, 0.0F, 1.1383037381507017F);
        this.EyeL = new AdvancedModelRenderer(this, 40, 0);
        this.EyeL.setRotationPoint(4.0F, -0.3F, -3.5F);
        this.EyeL.addBox(-1.5F, -1.0F, -3.0F, 3, 2, 6, 0.0F);
        this.setRotateAngle(EyeL, 0.22689280275926282F, -0.08726646259971647F, 1.5707963267948966F);
        this.Body4 = new AdvancedModelRenderer(this, 58, 35);
        this.Body4.setRotationPoint(0.0F, -0.4F, 7.3F);
        this.Body4.addBox(-3.0F, -2.7F, -1.5F, 6, 7, 4, 0.0F);
        this.setRotateAngle(Body4, 0.136659280431156F, 0.0F, 0.0F);
        this.Neck1 = new AdvancedModelRenderer(this, 32, 22);
        this.Neck1.setRotationPoint(0.0F, 0.0F, -6.0F);
        this.Neck1.addBox(-2.5F, -2.0F, -3.5F, 5, 5, 4, 0.0F);
        this.setRotateAngle(Neck1, -0.27314402793711257F, 0.0F, 0.0F);
        this.legMidR2_1 = new AdvancedModelRenderer(this, 11, 50);
        this.legMidR2_1.setRotationPoint(0.0F, 6.4F, 0.1F);
        this.legMidR2_1.addBox(-1.5F, 0.0F, -1.0F, 3, 12, 2, 0.0F);
        this.setRotateAngle(legMidR2_1, 0.0F, 0.0F, 1.1383037381507017F);
        this.legTopR3 = new AdvancedModelRenderer(this, 0, 54);
        this.legTopR3.mirror = true;
        this.legTopR3.setRotationPoint(-3.3F, 1.0F, 1.6F);
        this.legTopR3.addBox(-1.0F, 0.0F, -1.5F, 2, 7, 3, 0.0F);
        this.setRotateAngle(legTopR3, 0.5009094953223726F, 0.22759093446006054F, 0.7740535232594852F);
        this.Tail1 = new AdvancedModelRenderer(this, 80, 51);
        this.Tail1.setRotationPoint(0.0F, -0.4F, 1.2F);
        this.Tail1.addBox(-5.5F, -2.7F, -0.1F, 11, 11, 13, 0.0F);
        this.setRotateAngle(Tail1, -0.091106186954104F, 0.0F, 0.0F);
        this.Body5 = new AdvancedModelRenderer(this, 82, 35);
        this.Body5.setRotationPoint(0.0F, -0.4F, 4.2F);
        this.Body5.addBox(-3.5F, -2.5F, -2.1F, 7, 8, 6, 0.0F);
        this.setRotateAngle(Body5, -0.045553093477052F, 0.0F, 0.0F);
        this.crest2 = new AdvancedModelRenderer(this, 0, 88);
        this.crest2.setRotationPoint(1.2F, -0.1F, -1.0F);
        this.crest2.addBox(-1.5F, -2.51F, -0.1F, 3, 1, 6, 0.0F);
        this.setRotateAngle(crest2, 0.40980330836826856F, 0.17453292519943295F, 0.2792526803190927F);
        this.legTopR2 = new AdvancedModelRenderer(this, 0, 54);
        this.legTopR2.setRotationPoint(3.3F, 1.0F, 1.6F);
        this.legTopR2.addBox(-1.0F, 0.0F, -1.5F, 2, 7, 3, 0.0F);
        this.setRotateAngle(legTopR2, 0.0F, 0.0F, -0.6981317007977318F);
        this.Body3 = new AdvancedModelRenderer(this, 0, 67);
        this.Body3.setRotationPoint(0.0F, 0.2F, 4.1F);
        this.Body3.addBox(-4.5F, -3.4F, -1.4F, 9, 9, 9, 0.0F);
        this.legMidR1_1 = new AdvancedModelRenderer(this, 11, 50);
        this.legMidR1_1.mirror = true;
        this.legMidR1_1.setRotationPoint(0.0F, 6.4F, 0.1F);
        this.legMidR1_1.addBox(-1.5F, 0.0F, -1.0F, 3, 12, 2, 0.0F);
        this.setRotateAngle(legMidR1_1, 0.0F, 0.0F, -1.1383037381507017F);
        this.HeadBase = new AdvancedModelRenderer(this, 0, 0);
        this.HeadBase.setRotationPoint(0.0F, -0.1F, -2.4F);
        this.HeadBase.addBox(-4.0F, -2.51F, -10.1F, 8, 6, 10, 0.0F);
        this.setRotateAngle(HeadBase, 0.6373942428283291F, 0.0F, 0.0F);
        this.crestbase = new AdvancedModelRenderer(this, 0, 100);
        this.crestbase.setRotationPoint(0.0F, -2.9F, 1.0F);
        this.crestbase.addBox(-3.5F, -2.51F, -6.1F, 7, 2, 6, 0.0F);
        this.setRotateAngle(crestbase, 0.4553564018453205F, 0.0F, 0.0F);
        this.legBottomR2_1 = new AdvancedModelRenderer(this, 22, 51);
        this.legBottomR2_1.setRotationPoint(0.0F, 10.4F, 0.0F);
        this.legBottomR2_1.addBox(-1.01F, 0.0F, -0.9F, 2, 13, 2, 0.0F);
        this.setRotateAngle(legBottomR2_1, 0.0F, 0.0F, -1.3203415791337103F);
        this.legTopR3_1 = new AdvancedModelRenderer(this, 0, 54);
        this.legTopR3_1.setRotationPoint(3.3F, 1.0F, 1.6F);
        this.legTopR3_1.addBox(-1.0F, 0.0F, -1.5F, 2, 7, 3, 0.0F);
        this.setRotateAngle(legTopR3_1, 0.5009094953223726F, -0.22759093446006054F, -0.7740535232594852F);
        this.legBottomR1 = new AdvancedModelRenderer(this, 22, 51);
        this.legBottomR1.setRotationPoint(0.0F, 10.4F, 0.0F);
        this.legBottomR1.addBox(-1.01F, 0.0F, -0.9F, 2, 13, 2, 0.0F);
        this.setRotateAngle(legBottomR1, 0.0F, 0.0F, -1.3203415791337103F);
        this.legBottomR3 = new AdvancedModelRenderer(this, 22, 51);
        this.legBottomR3.setRotationPoint(0.0F, 10.4F, 0.0F);
        this.legBottomR3.addBox(-1.01F, 0.0F, -0.9F, 2, 13, 2, 0.0F);
        this.setRotateAngle(legBottomR3, 0.0F, 0.0F, -1.3203415791337103F);
        this.EyeR = new AdvancedModelRenderer(this, 40, 0);
        this.EyeR.mirror = true;
        this.EyeR.setRotationPoint(-4.0F, -0.3F, -3.5F);
        this.EyeR.addBox(-1.5F, -1.0F, -3.0F, 3, 2, 6, 0.0F);
        this.setRotateAngle(EyeR, 0.22689280275926282F, 0.08726646259971647F, -1.5707963267948966F);
        this.legMidR2 = new AdvancedModelRenderer(this, 11, 50);
        this.legMidR2.mirror = true;
        this.legMidR2.setRotationPoint(0.0F, 6.4F, 0.1F);
        this.legMidR2.addBox(-1.5F, 0.0F, -1.0F, 3, 12, 2, 0.0F);
        this.setRotateAngle(legMidR2, 0.0F, 0.0F, -1.1383037381507017F);
        this.legTopR1_1 = new AdvancedModelRenderer(this, 0, 54);
        this.legTopR1_1.setRotationPoint(3.3F, 1.0F, -1.4F);
        this.legTopR1_1.addBox(-1.0F, 0.0F, -1.5F, 2, 7, 3, 0.0F);
        this.setRotateAngle(legTopR1_1, -0.5009094953223726F, 0.22759093446006054F, -0.6981317007977318F);
        this.legMidR3_1 = new AdvancedModelRenderer(this, 11, 50);
        this.legMidR3_1.mirror = true;
        this.legMidR3_1.setRotationPoint(0.0F, 6.4F, 0.1F);
        this.legMidR3_1.addBox(-1.5F, 0.0F, -1.0F, 3, 12, 2, 0.0F);
        this.setRotateAngle(legMidR3_1, 0.0F, 0.0F, -1.1383037381507017F);
        this.legTopR1 = new AdvancedModelRenderer(this, 0, 54);
        this.legTopR1.mirror = true;
        this.legTopR1.setRotationPoint(-3.3F, 1.0F, -1.4F);
        this.legTopR1.addBox(-1.0F, 0.0F, -1.5F, 2, 7, 3, 0.0F);
        this.setRotateAngle(legTopR1, -0.5009094953223726F, -0.22759093446006054F, 0.6981317007977318F);
        this.MandibleR = new AdvancedModelRenderer(this, 0, 25);
        this.MandibleR.mirror = true;
        this.MandibleR.setRotationPoint(-3.4F, 3.7F, -7.7F);
        this.MandibleR.addBox(-2.0F, -2.51F, -5.1F, 4, 2, 6, 0.0F);
        this.setRotateAngle(MandibleR, 0.17453292519943295F, -0.18203784098300857F, 0.0F);
        this.Tail2.addChild(this.Tail3);
        this.crestbase.addChild(this.crest1);
        this.legMidR3_1.addChild(this.legBottomR3_1);
        this.Tail3.addChild(this.Stinger);
        this.Body2.addChild(this.Body1);
        this.Body2.addChild(this.legTopR2_1);
        this.legMidR1_1.addChild(this.legBottomR1_1);
        this.Tail1.addChild(this.Tail2);
        this.legTopR1.addChild(this.legMidR1);
        this.HeadBase.addChild(this.MandibleL);
        this.legMidR2.addChild(this.legBottomR2);
        this.legTopR3.addChild(this.legMidR3);
        this.HeadBase.addChild(this.EyeL);
        this.Body3.addChild(this.Body4);
        this.Body1.addChild(this.Neck1);
        this.legTopR2_1.addChild(this.legMidR2_1);
        this.Body3.addChild(this.legTopR3);
        this.Body5.addChild(this.Tail1);
        this.Body4.addChild(this.Body5);
        this.crestbase.addChild(this.crest2);
        this.Body2.addChild(this.legTopR2);
        this.Body2.addChild(this.Body3);
        this.legTopR1_1.addChild(this.legMidR1_1);
        this.Neck1.addChild(this.HeadBase);
        this.HeadBase.addChild(this.crestbase);
        this.legMidR2_1.addChild(this.legBottomR2_1);
        this.Body3.addChild(this.legTopR3_1);
        this.legMidR1.addChild(this.legBottomR1);
        this.legMidR3.addChild(this.legBottomR3);
        this.HeadBase.addChild(this.EyeR);
        this.legTopR2.addChild(this.legMidR2);
        this.Body1.addChild(this.legTopR1_1);
        this.legTopR3_1.addChild(this.legMidR3_1);
        this.Body1.addChild(this.legTopR1);
        this.HeadBase.addChild(this.MandibleR);
        animator = ModelAnimator.create();
        this.updateDefaultPose();
    }

    @Override
    public void renderAdult(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        animate((IAnimatedEntity) entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        this.Body2.render(f5);
        this.Body5.setScale(1.0F, 1.0F, 1.0F);
        this.Tail1.setScale(1.0F, 1.0F, 1.0F);
        this.Tail2.setScale(1.0F, 1.0F, 1.0F);
        this.Tail3.setScale(1.0F, 1.0F, 1.0F);
    }

    public void animate(IAnimatedEntity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        this.resetToDefaultPose();
        animator.update(entity);
        animator.setAnimation(EntityMyrmexQueen.ANIMATION_DIGNEST);
        animator.startKeyframe(15);
        ModelUtils.rotateFrom(animator, Body2, 26, 0, 0);
        ModelUtils.rotateFrom(animator, Neck1, -5, 0, 0);
        ModelUtils.rotateFrom(animator, Body4, -8, 0, 0);
        ModelUtils.rotateFrom(animator, Body5, -2, 0, 0);
        ModelUtils.rotateFrom(animator, Tail1, -13, 0, 0);
        ModelUtils.rotateFrom(animator, Tail2, -18, 0, 0);
        ModelUtils.rotateFrom(animator, Tail3, -13, 0, 0);
        digPose();
        animator.endKeyframe();
        animator.startKeyframe(10);
        animator.move(Body2, 0, 5, 0);
        ModelUtils.rotateFrom(animator, Body2, 45, 0, 0);
        ModelUtils.rotateFrom(animator, Neck1, -5, 0, 0);
        ModelUtils.rotateFrom(animator, Body4, -8, 0, 0);
        ModelUtils.rotateFrom(animator, Body5, -2, 0, 0);
        ModelUtils.rotateFrom(animator, Tail1, -13, 0, 0);
        ModelUtils.rotateFrom(animator, Tail2, -18, 0, 0);
        ModelUtils.rotateFrom(animator, Tail3, -13, 0, 0);
        digPose();
        animator.endKeyframe();
        animator.startKeyframe(5);
        animator.move(Body2, 0, 30, 0);
        ModelUtils.rotateFrom(animator, Body2, 78, 0, 0);
        digPose();
        animator.endKeyframe();
        animator.startKeyframe(5);
        animator.move(Body2, 0, 80, 0);
        ModelUtils.rotateFrom(animator, Body2, 90, 0, 0);
        digPose();
        animator.endKeyframe();
        animator.startKeyframe(10);
        animator.move(Body2, 0, 80, 0);
        ModelUtils.rotateFrom(animator, Body2, 90, 0, 0);
        digPose();
        animator.endKeyframe();
        animator.resetKeyframe(5);
        animator.setAnimation(EntityMyrmexQueen.ANIMATION_BITE);
        animator.startKeyframe(5);
        ModelUtils.rotate(animator, Neck1, -50, 0, 0);
        ModelUtils.rotate(animator, HeadBase, 50, 0, 0);
        ModelUtils.rotate(animator, MandibleR, 0, 35, 0);
        ModelUtils.rotate(animator, MandibleL, 0, -35, 0);
        animator.endKeyframe();
        animator.startKeyframe(5);
        ModelUtils.rotate(animator, Neck1, 30, 0, 0);
        ModelUtils.rotate(animator, HeadBase, -30, 0, 0);
        ModelUtils.rotate(animator, MandibleR, 0, -50, 0);
        ModelUtils.rotate(animator, MandibleL, 0, 50, 0);
        animator.endKeyframe();
        animator.resetKeyframe(5);
        animator.setAnimation(EntityMyrmexQueen.ANIMATION_STING);
        animator.startKeyframe(5);
        animator.move(Body2, 0, -10, 0);
        ModelUtils.rotate(animator, Body3, -35, 0, 0);
        ModelUtils.rotate(animator, Body4, -49, 0, 0);
        ModelUtils.rotate(animator, Body5, -25, 0, 0);
        ModelUtils.rotate(animator, Tail1, -25, 0, 0);
        ModelUtils.rotate(animator, Tail2, -40, 0, 0);
        ModelUtils.rotate(animator, Tail3, -40, 0, 0);
        ModelUtils.rotate(animator, Stinger, 90, 0, 0);
        ModelUtils.rotateFrom(animator, legTopR3, 64, -7, 44);
        ModelUtils.rotateFrom(animator, legTopR3_1, 64, 7, -44);
        ModelUtils.rotateFrom(animator, legMidR3, 0, 0, 30);
        ModelUtils.rotateFrom(animator, legMidR2, 0, 0, -25);
        ModelUtils.rotateFrom(animator, legMidR1, 0, 0, 25);
        ModelUtils.rotateFrom(animator, legMidR3_1, 0, 0, -30);
        ModelUtils.rotateFrom(animator, legMidR2_1, 0, 0, 25);
        ModelUtils.rotateFrom(animator, legMidR1_1, 0, 0, -25);
        animator.endKeyframe();
        animator.resetKeyframe(10);
        EntityMyrmexQueen myrmexQueen = (EntityMyrmexQueen) entity;
        if (myrmexQueen.getAnimation() == EntityMyrmexQueen.ANIMATION_EGG) {
            int animationTick = MathHelper.clamp(myrmexQueen.getAnimationTick(), 0, 20);
            double swellToPi = animationTick / 20F * Math.PI;
            this.increaseScale(Body5, 0.5F * (float) Math.abs(Math.sin(swellToPi + 0.5F)));
            this.increaseScale(Tail1, 0.75F * (float) Math.abs(Math.sin(swellToPi)));
            this.increaseScale(Tail2, 0.75F * (float) Math.abs(Math.sin(swellToPi - 0.5F)));
            this.increaseScale(Tail3, 0.75F * (float) Math.abs(Math.sin(swellToPi - 1.0F)));
            this.Stinger.rotationPointZ += 10 * (float) Math.abs(Math.sin(swellToPi - 1.0F));
        }
    }

    @Override
    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity) {
        AdvancedModelRenderer[] GASTER = new AdvancedModelRenderer[]{Body4, Body5, Tail1, Tail2, Tail3, Stinger};
        AdvancedModelRenderer[] NECK = new AdvancedModelRenderer[]{Neck1, HeadBase};
        AdvancedModelRenderer[] LEGR1 = new AdvancedModelRenderer[]{legTopR1, legMidR1, legBottomR1};
        AdvancedModelRenderer[] LEGR2 = new AdvancedModelRenderer[]{legTopR2, legMidR2, legBottomR2};
        AdvancedModelRenderer[] LEGR3 = new AdvancedModelRenderer[]{legTopR3, legMidR3, legBottomR3};
        AdvancedModelRenderer[] LEGL1 = new AdvancedModelRenderer[]{legTopR1_1, legMidR1_1, legBottomR1_1};
        AdvancedModelRenderer[] LEGL2 = new AdvancedModelRenderer[]{legTopR2_1, legMidR2_1, legBottomR2_1};
        AdvancedModelRenderer[] LEGL3 = new AdvancedModelRenderer[]{legTopR3_1, legMidR3_1, legBottomR3_1};
        float speed_walk = 1.8F;
        float speed_idle = 0.05F;
        float degree_walk = 0.7F;
        float degree_idle = 0.15F;
        float gasterSwell1 = -0.05F + (0.2F * Math.abs(MathHelper.sin(entity.ticksExisted * 0.15F + 1.0F)));
        float gasterSwell2 = -0.05F + (0.2F * Math.abs(MathHelper.sin(entity.ticksExisted * 0.15F + 0.5F)));
        float gasterSwell3 = -0.05F + (0.2F * Math.abs(MathHelper.sin(entity.ticksExisted * 0.15F)));
        EntityMyrmexQueen myrmexQueen = (EntityMyrmexQueen) entity;
        if (myrmexQueen.getAnimation() != EntityMyrmexQueen.ANIMATION_EGG) {
            this.increaseScale(Tail1, gasterSwell1);
            this.increaseScale(Tail2, gasterSwell2);
            this.increaseScale(Tail3, gasterSwell3);
            this.Stinger.rotationPointZ += 20 * gasterSwell3;
        }
        if (myrmexQueen.getAnimation() == EntityMyrmexQueen.ANIMATION_DIGNEST) {
            this.animateLeg(LEGR1, speed_walk * 0.5F, degree_walk * 0.5F, false, 0, 1, f2, 1);
            this.animateLeg(LEGR3, speed_walk * 0.5F, degree_walk * 0.5F, false, 0, 1, f2, 1);
            this.animateLeg(LEGR2, speed_walk * 0.5F, degree_walk * 0.5F, true, 0, 1, f2, 1);

            this.animateLeg(LEGL1, speed_walk * 0.5F, degree_walk * 0.5F, false, 1, -1, f2, 1);
            this.animateLeg(LEGL3, speed_walk * 0.5F, degree_walk * 0.5F, false, 1, -1, f2, 1);
            this.animateLeg(LEGL2, speed_walk * 0.5F, degree_walk * 0.5F, true, 1, -1, f2, 1);
        }
        if (entity.getPassengers().isEmpty()) {
            this.faceTarget(f3, f4, 2, NECK);
        }
        this.chainWave(GASTER, speed_idle, degree_idle * 0.15F, 0, f2, 1);
        this.chainWave(NECK, speed_idle, degree_idle * -0.15F, 2, f2, 1);
        this.swing(MandibleR, speed_idle * 2F, degree_idle * -0.75F, false, 1, 0.2F, f2, 1);
        this.swing(MandibleL, speed_idle * 2F, degree_idle * -0.75F, true, 1, 0.2F, f2, 1);
        this.animateLeg(LEGR1, speed_walk, degree_walk, false, 0, 1, f, f1);
        this.animateLeg(LEGR3, speed_walk, degree_walk, false, 0, 1, f, f1);
        this.animateLeg(LEGR2, speed_walk, degree_walk, true, 0, 1, f, f1);

        this.animateLeg(LEGL1, speed_walk, degree_walk, false, 1, -1, f, f1);
        this.animateLeg(LEGL3, speed_walk, degree_walk, false, 1, -1, f, f1);
        this.animateLeg(LEGL2, speed_walk, degree_walk, true, 1, -1, f, f1);
    }

    private void animateLeg(AdvancedModelRenderer[] models, float speed, float degree, boolean reverse, float offset, float weight, float f, float f1) {
        this.flap(models[0], speed, degree * 0.4F, reverse, offset, weight * 0.2F, f, f1);
        this.flap(models[1], speed, degree * 2, reverse, offset, weight * -0.4F, f, f1);
        this.flap(models[1], speed, -degree * 1.2F, reverse, offset, weight * 0.5F, f, f1);
        this.walk(models[0], speed, degree, reverse, offset, 0F, f, f1);

    }

    public void increaseScale(AdvancedModelRenderer box, float scale) {
        box.scaleX += scale;
        box.scaleY += scale;
        box.scaleZ += scale;
    }

    @Override
    public AdvancedModelRenderer[] getHeadParts() {
        return new AdvancedModelRenderer[]{Neck1, HeadBase};
    }

    @Override
    public void renderStatue() {
        this.resetToDefaultPose();
        this.Body5.setScale(1.0F, 1.0F, 1.0F);
        this.Tail1.setScale(1.0F, 1.0F, 1.0F);
        this.Tail2.setScale(1.0F, 1.0F, 1.0F);
        this.Tail3.setScale(1.0F, 1.0F, 1.0F);
        this.Body2.render(0.0625F);
    }

    private void digPose() {
        ModelUtils.rotateFrom(animator, legTopR1, -28, -13, 36);
        ModelUtils.rotateFrom(animator, legTopR2, -7, 15, -24);
        ModelUtils.rotateFrom(animator, legTopR3, -2, 31, 13);
        ModelUtils.rotateFrom(animator, legMidR1, 0, 0, 80);
        ModelUtils.rotateFrom(animator, legMidR2, 0, 0, -83);
        ModelUtils.rotateFrom(animator, legMidR3, 0, 0, 65);
        ModelUtils.rotateFrom(animator, legBottomR1, 0, 0, -75);
        ModelUtils.rotateFrom(animator, legBottomR2, 0, 0, 75);
        ModelUtils.rotateFrom(animator, legBottomR3, 0, 0, -54);

        ModelUtils.rotateFrom(animator, legTopR1_1, -28, 13, -36);
        ModelUtils.rotateFrom(animator, legTopR2_1, -7, -15, 24);
        ModelUtils.rotateFrom(animator, legTopR3_1, -2, -31, -13);
        ModelUtils.rotateFrom(animator, legMidR1_1, 0, 0, -80);
        ModelUtils.rotateFrom(animator, legMidR2_1, 0, 0, 83);
        ModelUtils.rotateFrom(animator, legMidR3_1, 0, 0, -65);
        ModelUtils.rotateFrom(animator, legBottomR1_1, 0, 0, 75);
        ModelUtils.rotateFrom(animator, legBottomR2_1, 0, 0, -75);
        ModelUtils.rotateFrom(animator, legBottomR3_1, 0, 0, 54);
    }
}
