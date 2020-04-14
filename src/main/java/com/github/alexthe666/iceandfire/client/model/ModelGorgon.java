package com.github.alexthe666.iceandfire.client.model;

import com.github.alexthe666.iceandfire.entity.EntityGorgon;
import net.ilexiconn.llibrary.client.model.ModelAnimator;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class ModelGorgon extends ModelDragonBase {
    public AdvancedModelRenderer Tail_1;
    public AdvancedModelRenderer Tail_2;
    public AdvancedModelRenderer Body;
    public AdvancedModelRenderer Tail_3;
    public AdvancedModelRenderer Tail_4;
    public AdvancedModelRenderer Tail_5;
    public AdvancedModelRenderer Tail_6;
    public AdvancedModelRenderer Tail_7;
    public AdvancedModelRenderer Tail_7_1;
    public AdvancedModelRenderer Left_Arm;
    public AdvancedModelRenderer Head;
    public AdvancedModelRenderer Right_Arm;
    public AdvancedModelRenderer Neck;
    public AdvancedModelRenderer Head_Details;
    public AdvancedModelRenderer SnakeBaseR1;
    public AdvancedModelRenderer SnakeBaseR2;
    public AdvancedModelRenderer SnakeBaseR3;
    public AdvancedModelRenderer SnakeBaseR4;
    public AdvancedModelRenderer SnakeBaseL2;
    public AdvancedModelRenderer SnakeBaseL3;
    public AdvancedModelRenderer SnakeBaseL1;
    public AdvancedModelRenderer SnakeBaseL4;
    public AdvancedModelRenderer SnakeBodyR1;
    public AdvancedModelRenderer SnakeHeadR1;
    public AdvancedModelRenderer SnakeBodyR2;
    public AdvancedModelRenderer SnakeHeadR2;
    public AdvancedModelRenderer SnakeBodyR3;
    public AdvancedModelRenderer SnakeHeadR3;
    public AdvancedModelRenderer SnakeBodyR4;
    public AdvancedModelRenderer SnakeHeadR4;
    public AdvancedModelRenderer SnakeBodyL2;
    public AdvancedModelRenderer SnakeHeadL2;
    public AdvancedModelRenderer SnakeBodyL3;
    public AdvancedModelRenderer SnakeHeadR3_1;
    public AdvancedModelRenderer SnakeBodyL1;
    public AdvancedModelRenderer SnakeHeadL1;
    public AdvancedModelRenderer SnakeBodyL4;
    public AdvancedModelRenderer SnakeHeadL4;
    private ModelAnimator animator;

    public ModelGorgon() {
        this.textureWidth = 128;
        this.textureHeight = 64;
        this.SnakeHeadR4 = new AdvancedModelRenderer(this, 80, 0);
        this.SnakeHeadR4.setRotationPoint(0.0F, -4.8F, 0.0F);
        this.SnakeHeadR4.addBox(-0.5F, -2.8F, -0.8F, 1, 3, 2, 0.0F);
        this.setRotateAngle(SnakeHeadR4, 1.1383037381507017F, -0.045553093477052F, 0.31869712141416456F);
        this.Tail_3 = new AdvancedModelRenderer(this, 0, 48);
        this.Tail_3.setRotationPoint(0.0F, 4.1F, -0.9F);
        this.Tail_3.addBox(-3.5F, 0.0F, -2.4F, 7, 8, 6, 0.0F);
        this.setRotateAngle(Tail_3, 1.0016444577195458F, 0.0F, 0.0F);
        this.Neck = new AdvancedModelRenderer(this, 40, 25);
        this.Neck.setRotationPoint(0.0F, -12.0F, 0.0F);
        this.Neck.addBox(-3.0F, -3.7F, -1.0F, 6, 4, 1, 0.0F);
        this.Tail_1 = new AdvancedModelRenderer(this, 0, 35);
        this.Tail_1.setRotationPoint(0.0F, 8.8F, -5.0F);
        this.Tail_1.addBox(-4.0F, 0.0F, -2.4F, 8, 6, 6, 0.1F);
        this.setRotateAngle(Tail_1, -0.22689280275926282F, 0.0F, 0.0F);
        this.Right_Arm = new AdvancedModelRenderer(this, 40, 16);
        this.Right_Arm.setRotationPoint(-5.0F, -10.0F, 0.0F);
        this.Right_Arm.addBox(-2.0F, -2.0F, -2.0F, 3, 12, 4, 0.0F);
        this.SnakeBaseR4 = new AdvancedModelRenderer(this, 60, 0);
        this.SnakeBaseR4.setRotationPoint(-0.5F, -3.3F, 3.4F);
        this.SnakeBaseR4.addBox(-0.5F, -6.1F, -1.0F, 1, 5, 1, 0.0F);
        this.setRotateAngle(SnakeBaseR4, -0.5235987755982988F, -0.17453292519943295F, -1.5707963267948966F);
        this.SnakeBodyR4 = new AdvancedModelRenderer(this, 70, 0);
        this.SnakeBodyR4.setRotationPoint(-0.0F, -5.4F, -0.4F);
        this.SnakeBodyR4.addBox(-0.5F, -5.5F, -0.5F, 1, 5, 1, 0.0F);
        this.setRotateAngle(SnakeBodyR4, 1.1344640137963142F, 0.5235987755982988F, 0.0F);
        this.Tail_7 = new AdvancedModelRenderer(this, 61, 48);
        this.Tail_7.setRotationPoint(0.0F, 6.2F, 0.4F);
        this.Tail_7.addBox(-2.0F, 0.1F, -1.9F, 4, 6, 3, 0.0F);
        this.setRotateAngle(Tail_7, 0.045553093477052F, 0.0F, 0.0F);
        this.Head_Details = new AdvancedModelRenderer(this, 32, 0);
        this.Head_Details.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.Head_Details.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0.0F);
        this.SnakeBaseL1 = new AdvancedModelRenderer(this, 60, 0);
        this.SnakeBaseL1.setRotationPoint(3.0F, -5.0F, 1.5F);
        this.SnakeBaseL1.addBox(-0.5F, -6.1F, -1.0F, 1, 5, 1, 0.0F);
        this.setRotateAngle(SnakeBaseL1, 0.6108652381980153F, 0.0F, 1.3962634015954636F);
        this.SnakeBodyL2 = new AdvancedModelRenderer(this, 70, 0);
        this.SnakeBodyL2.mirror = true;
        this.SnakeBodyL2.setRotationPoint(0.0F, -5.4F, -0.4F);
        this.SnakeBodyL2.addBox(-0.5F, -5.5F, -0.5F, 1, 5, 1, 0.0F);
        this.setRotateAngle(SnakeBodyL2, 0.5235987755982988F, -0.2617993877991494F, 0.0F);
        this.SnakeHeadR3_1 = new AdvancedModelRenderer(this, 80, 0);
        this.SnakeHeadR3_1.setRotationPoint(0.0F, -4.4F, 0.0F);
        this.SnakeHeadR3_1.addBox(-1.0F, -3.1F, -0.5F, 1, 3, 2, 0.0F);
        this.setRotateAngle(SnakeHeadR3_1, 1.2747884856566583F, 0.0F, 0.5918411493512771F);
        this.SnakeBaseL4 = new AdvancedModelRenderer(this, 60, 0);
        this.SnakeBaseL4.setRotationPoint(0.5F, -3.3F, 3.4F);
        this.SnakeBaseL4.addBox(-0.5F, -6.1F, -1.0F, 1, 5, 1, 0.0F);
        this.setRotateAngle(SnakeBaseL4, -0.5235987755982988F, 0.17453292519943295F, 1.5707963267948966F);
        this.SnakeBaseR1 = new AdvancedModelRenderer(this, 60, 0);
        this.SnakeBaseR1.setRotationPoint(-3.0F, -5.0F, 1.5F);
        this.SnakeBaseR1.addBox(-0.5F, -6.1F, -1.0F, 1, 5, 1, 0.0F);
        this.setRotateAngle(SnakeBaseR1, 0.6108652381980153F, 0.0F, -1.3962634015954636F);
        this.SnakeHeadL4 = new AdvancedModelRenderer(this, 80, 0);
        this.SnakeHeadL4.setRotationPoint(0.0F, -4.8F, 0.0F);
        this.SnakeHeadL4.addBox(-0.5F, -2.8F, -0.8F, 1, 3, 2, 0.0F);
        this.setRotateAngle(SnakeHeadL4, 1.1383037381507017F, 0.045553093477052F, -0.31869712141416456F);
        this.SnakeBodyR2 = new AdvancedModelRenderer(this, 70, 0);
        this.SnakeBodyR2.setRotationPoint(0.0F, -5.4F, -0.4F);
        this.SnakeBodyR2.addBox(-0.5F, -5.5F, -0.5F, 1, 5, 1, 0.0F);
        this.setRotateAngle(SnakeBodyR2, 0.5235987755982988F, 0.2617993877991494F, 0.0F);
        this.SnakeBodyL4 = new AdvancedModelRenderer(this, 70, 0);
        this.SnakeBodyL4.setRotationPoint(-0.0F, -5.4F, -0.4F);
        this.SnakeBodyL4.addBox(-0.5F, -5.5F, -0.5F, 1, 5, 1, 0.0F);
        this.setRotateAngle(SnakeBodyL4, 1.1344640137963142F, -0.5235987755982988F, 0.0F);
        this.SnakeHeadL2 = new AdvancedModelRenderer(this, 80, 0);
        this.SnakeHeadL2.mirror = true;
        this.SnakeHeadL2.setRotationPoint(0.0F, -4.4F, 0.0F);
        this.SnakeHeadL2.addBox(-1.0F, -3.6F, -0.6F, 1, 3, 2, 0.0F);
        this.setRotateAngle(SnakeHeadL2, 0.8726646259971648F, 0.3490658503988659F, 0.6981317007977318F);
        this.SnakeHeadR2 = new AdvancedModelRenderer(this, 80, 0);
        this.SnakeHeadR2.setRotationPoint(0.0F, -4.4F, 0.0F);
        this.SnakeHeadR2.addBox(0.0F, -3.6F, -0.6F, 1, 3, 2, 0.0F);
        this.setRotateAngle(SnakeHeadR2, 0.8726646259971648F, -0.3302162944773272F, -0.6981317007977318F);
        this.SnakeBaseL2 = new AdvancedModelRenderer(this, 60, 0);
        this.SnakeBaseL2.mirror = true;
        this.SnakeBaseL2.setRotationPoint(1.0F, -6.8F, 0.7F);
        this.SnakeBaseL2.addBox(-0.5F, -6.1F, -1.0F, 1, 5, 1, 0.0F);
        this.setRotateAngle(SnakeBaseL2, 0.3490658503988659F, 0.4363323129985824F, 0.8726646259971648F);
        this.SnakeBodyL3 = new AdvancedModelRenderer(this, 70, 0);
        this.SnakeBodyL3.setRotationPoint(-0.0F, -5.4F, -0.4F);
        this.SnakeBodyL3.addBox(-0.5F, -5.5F, -0.5F, 1, 5, 1, 0.0F);
        this.setRotateAngle(SnakeBodyL3, 0.5235987755982988F, -0.3490658503988659F, 0.0F);
        this.Body = new AdvancedModelRenderer(this, 16, 16);
        this.Body.setRotationPoint(0.0F, 0.9F, 1.0F);
        this.Body.addBox(-4.0F, -12.0F, -2.0F, 8, 12, 4, 0.0F);
        this.setRotateAngle(Body, 0.31869712141416456F, 0.0F, 0.0F);
        this.SnakeHeadR3 = new AdvancedModelRenderer(this, 80, 0);
        this.SnakeHeadR3.setRotationPoint(0.0F, -4.4F, 0.0F);
        this.SnakeHeadR3.addBox(0.0F, -3.1F, -0.5F, 1, 3, 2, 0.0F);
        this.setRotateAngle(SnakeHeadR3, 1.2747884856566583F, 0.0F, -0.5918411493512771F);
        this.SnakeBaseR2 = new AdvancedModelRenderer(this, 60, 0);
        this.SnakeBaseR2.setRotationPoint(-1.0F, -6.8F, 0.7F);
        this.SnakeBaseR2.addBox(-0.5F, -6.1F, -1.0F, 1, 5, 1, 0.0F);
        this.setRotateAngle(SnakeBaseR2, 0.3490658503988659F, -0.4363323129985824F, -0.8726646259971648F);
        this.Tail_7_1 = new AdvancedModelRenderer(this, 52, 56);
        this.Tail_7_1.setRotationPoint(0.0F, 5.8F, -0.1F);
        this.Tail_7_1.addBox(-1.5F, 0.1F, -1.3F, 3, 6, 2, 0.0F);
        this.setRotateAngle(Tail_7_1, 0.045553093477052F, 0.0F, 0.0F);
        this.SnakeBaseL3 = new AdvancedModelRenderer(this, 60, 0);
        this.SnakeBaseL3.setRotationPoint(1.8F, -5.7F, 2.6F);
        this.SnakeBaseL3.addBox(-0.5F, -6.1F, -1.0F, 1, 5, 1, 0.0F);
        this.setRotateAngle(SnakeBaseL3, -0.2617993877991494F, 0.4363323129985824F, 0.8726646259971648F);
        this.Tail_4 = new AdvancedModelRenderer(this, 29, 34);
        this.Tail_4.setRotationPoint(0.0F, 6.4F, -0.2F);
        this.Tail_4.addBox(-3.52F, 0.0F, -2.4F, 7, 8, 5, 0.0F);
        this.setRotateAngle(Tail_4, 0.4553564018453205F, 0.0F, 0.0F);
        this.Head = new AdvancedModelRenderer(this, 0, 0);
        this.Head.setRotationPoint(0.0F, -12.0F, 0.0F);
        this.Head.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0.0F);
        this.Left_Arm = new AdvancedModelRenderer(this, 40, 16);
        this.Left_Arm.mirror = true;
        this.Left_Arm.setRotationPoint(5.0F, -10.0F, 0.0F);
        this.Left_Arm.addBox(-1.0F, -2.0F, -2.0F, 3, 12, 4, 0.0F);
        this.SnakeBodyR3 = new AdvancedModelRenderer(this, 70, 0);
        this.SnakeBodyR3.setRotationPoint(-0.0F, -5.4F, -0.4F);
        this.SnakeBodyR3.addBox(-0.5F, -5.5F, -0.5F, 1, 5, 1, 0.0F);
        this.setRotateAngle(SnakeBodyR3, 0.5235987755982988F, 0.3490658503988659F, 0.0F);
        this.Tail_6 = new AdvancedModelRenderer(this, 54, 35);
        this.Tail_6.setRotationPoint(0.0F, 6.2F, -0.1F);
        this.Tail_6.addBox(-2.5F, 0.1F, -1.9F, 5, 7, 4, 0.0F);
        this.setRotateAngle(Tail_6, -0.045553093477052F, 0.0F, 0.0F);
        this.SnakeHeadL1 = new AdvancedModelRenderer(this, 80, 0);
        this.SnakeHeadL1.mirror = true;
        this.SnakeHeadL1.setRotationPoint(0.0F, -4.4F, 0.0F);
        this.SnakeHeadL1.addBox(-0.7F, -3.6F, -0.5F, 1, 3, 2, 0.0F);
        this.setRotateAngle(SnakeHeadL1, 0.6373942428283291F, 0.091106186954104F, 0.27314402793711257F);
        this.SnakeHeadR1 = new AdvancedModelRenderer(this, 80, 0);
        this.SnakeHeadR1.setRotationPoint(0.0F, -4.4F, 0.0F);
        this.SnakeHeadR1.addBox(-0.3F, -3.6F, -0.5F, 1, 3, 2, 0.0F);
        this.setRotateAngle(SnakeHeadR1, 0.6373942428283291F, -0.091106186954104F, -0.27314402793711257F);
        this.SnakeBodyL1 = new AdvancedModelRenderer(this, 70, 0);
        this.SnakeBodyL1.setRotationPoint(-0.0F, -5.4F, -0.4F);
        this.SnakeBodyL1.addBox(-0.5F, -5.5F, -0.5F, 1, 5, 1, 0.0F);
        this.setRotateAngle(SnakeBodyL1, 0.5009094953223726F, 0.0F, 0.0F);
        this.SnakeBodyR1 = new AdvancedModelRenderer(this, 70, 0);
        this.SnakeBodyR1.setRotationPoint(-0.0F, -5.4F, -0.4F);
        this.SnakeBodyR1.addBox(-0.5F, -5.5F, -0.5F, 1, 5, 1, 0.0F);
        this.setRotateAngle(SnakeBodyR1, 0.5009094953223726F, 0.0F, 0.0F);
        this.Tail_5 = new AdvancedModelRenderer(this, 28, 49);
        this.Tail_5.setRotationPoint(0.0F, 6.18F, -0.09F);
        this.Tail_5.addBox(-3.0F, 0.0F, -2.4F, 6, 8, 5, 0.0F);
        this.Tail_2 = new AdvancedModelRenderer(this, 72, 34);
        this.Tail_2.setRotationPoint(0.0F, 5.3F, 0.0F);
        this.Tail_2.addBox(-4.0F, 0.0F, -2.4F, 8, 6, 6, 0.0F);
        this.setRotateAngle(Tail_2, 0.31869712141416456F, 0.0F, 0.0F);
        this.SnakeBaseR3 = new AdvancedModelRenderer(this, 60, 0);
        this.SnakeBaseR3.setRotationPoint(-1.8F, -5.7F, 2.6F);
        this.SnakeBaseR3.addBox(-0.5F, -6.1F, -1.0F, 1, 5, 1, 0.0F);
        this.setRotateAngle(SnakeBaseR3, -0.2617993877991494F, -0.4363323129985824F, -0.8726646259971648F);
        this.SnakeBodyR4.addChild(this.SnakeHeadR4);
        this.Tail_2.addChild(this.Tail_3);
        this.Body.addChild(this.Neck);
        this.Body.addChild(this.Right_Arm);
        this.Head_Details.addChild(this.SnakeBaseR4);
        this.SnakeBaseR4.addChild(this.SnakeBodyR4);
        this.Tail_6.addChild(this.Tail_7);
        this.Head.addChild(this.Head_Details);
        this.Head_Details.addChild(this.SnakeBaseL1);
        this.SnakeBaseL2.addChild(this.SnakeBodyL2);
        this.SnakeBodyL3.addChild(this.SnakeHeadR3_1);
        this.Head_Details.addChild(this.SnakeBaseL4);
        this.Head_Details.addChild(this.SnakeBaseR1);
        this.SnakeBodyL4.addChild(this.SnakeHeadL4);
        this.SnakeBaseR2.addChild(this.SnakeBodyR2);
        this.SnakeBaseL4.addChild(this.SnakeBodyL4);
        this.SnakeBodyL2.addChild(this.SnakeHeadL2);
        this.SnakeBodyR2.addChild(this.SnakeHeadR2);
        this.Head_Details.addChild(this.SnakeBaseL2);
        this.SnakeBaseL3.addChild(this.SnakeBodyL3);
        this.Tail_1.addChild(this.Body);
        this.SnakeBodyR3.addChild(this.SnakeHeadR3);
        this.Head_Details.addChild(this.SnakeBaseR2);
        this.Tail_7.addChild(this.Tail_7_1);
        this.Head_Details.addChild(this.SnakeBaseL3);
        this.Tail_3.addChild(this.Tail_4);
        this.Body.addChild(this.Head);
        this.Body.addChild(this.Left_Arm);
        this.SnakeBaseR3.addChild(this.SnakeBodyR3);
        this.Tail_5.addChild(this.Tail_6);
        this.SnakeBodyL1.addChild(this.SnakeHeadL1);
        this.SnakeBodyR1.addChild(this.SnakeHeadR1);
        this.SnakeBaseL1.addChild(this.SnakeBodyL1);
        this.SnakeBaseR1.addChild(this.SnakeBodyR1);
        this.Tail_4.addChild(this.Tail_5);
        this.Tail_1.addChild(this.Tail_2);
        this.Head_Details.addChild(this.SnakeBaseR3);
        animator = ModelAnimator.create();
        this.updateDefaultPose();
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        animate((IAnimatedEntity) entity, f, f1, f2, f3, f4, f5);
        this.Tail_1.render(f5);

    }

    public void animate(IAnimatedEntity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        this.resetToDefaultPose();
        setRotationAngles(f, f1, f2, f3, f4, f5, (EntityGorgon) entity);
        animator.update(entity);
        animator.setAnimation(EntityGorgon.ANIMATION_SCARE);
        animator.startKeyframe(5);
        this.rotate(animator, Head, 0, 20, 0);
        this.rotate(animator, Left_Arm, 0, -12.5F, -70F);
        this.rotate(animator, Right_Arm, 0, 12.5F, 70F);
        animator.endKeyframe();
        animator.startKeyframe(5);
        this.rotate(animator, Head, 0, -20, 0);
        this.rotate(animator, Left_Arm, 0, -25, -140);
        this.rotate(animator, Right_Arm, 0, 25, 140);
        animator.endKeyframe();
        animator.startKeyframe(5);
        this.rotate(animator, Head, 0, 20, 0);
        this.rotate(animator, Left_Arm, 0, -25, -140);
        this.rotate(animator, Right_Arm, 0, 25, 140);
        animator.endKeyframe();
        animator.startKeyframe(5);
        this.rotate(animator, Head, 0, -20, 0);
        this.rotate(animator, Left_Arm, 0, -25, -140);
        this.rotate(animator, Right_Arm, 0, 25, 140);
        animator.endKeyframe();
        animator.resetKeyframe(10);
        animator.setAnimation(EntityGorgon.ANIMATION_HIT);
        animator.startKeyframe(5);
        this.rotate(animator, Body, 10F, 0F, 0F);

        this.rotate(animator, Left_Arm, -120F, 0F, 0F);
        this.rotate(animator, Right_Arm, -120F, 0F, 0F);
        animator.endKeyframe();
        animator.resetKeyframe(5);
    }

    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, EntityGorgon entity) {
        float speed_walk = 0.6F;
        float speed_idle = 0.05F;
        float degree_walk = 1F;
        float degree_idle = 0.5F;
        AdvancedModelRenderer[] TAIL = new AdvancedModelRenderer[]{Tail_4, Tail_5, Tail_6, Tail_7};
        AdvancedModelRenderer[] SNAKEL1 = new AdvancedModelRenderer[]{SnakeBaseL1, SnakeBodyL1, SnakeHeadL1};
        AdvancedModelRenderer[] SNAKEL2 = new AdvancedModelRenderer[]{SnakeBaseL2, SnakeBodyL2, SnakeHeadL2};
        AdvancedModelRenderer[] SNAKEL3 = new AdvancedModelRenderer[]{SnakeBaseL3, SnakeBodyL3, SnakeHeadR3_1};
        AdvancedModelRenderer[] SNAKEL4 = new AdvancedModelRenderer[]{SnakeBaseL4, SnakeBaseL4, SnakeBaseL4};

        AdvancedModelRenderer[] SNAKER1 = new AdvancedModelRenderer[]{SnakeBaseR1, SnakeBodyR1, SnakeHeadR1};
        AdvancedModelRenderer[] SNAKER2 = new AdvancedModelRenderer[]{SnakeBaseR2, SnakeBodyR2, SnakeHeadR2};
        AdvancedModelRenderer[] SNAKER3 = new AdvancedModelRenderer[]{SnakeBaseR3, SnakeBodyR3, SnakeHeadR3};
        AdvancedModelRenderer[] SNAKER4 = new AdvancedModelRenderer[]{SnakeBaseR4, SnakeBodyR4, SnakeHeadR4};

        this.chainFlap(TAIL, speed_walk, degree_walk * 0.75F, -3, f, f1);
        this.walk(Right_Arm, speed_idle * 1.5F, degree_idle * 0.4F, false, 2, -0.3F, f2, 1);
        this.walk(Left_Arm, speed_idle * 1.5F, degree_idle * 0.4F, true, 2, 0.3F, f2, 1);
        this.flap(Right_Arm, speed_idle * 1.5F, degree_idle * 0.2F, false, 2, 0.2F, f2, 1);
        this.flap(Left_Arm, speed_idle * 1.5F, degree_idle * 0.2F, true, 2, 0.2F, f2, 1);
        this.Right_Arm.rotateAngleX = MathHelper.cos(f * 0.6662F + (float) Math.PI) * 2.0F * f1 * 0.5F / 1;
        this.Left_Arm.rotateAngleX = MathHelper.cos(f * 0.6662F) * 2.0F * f1 * 0.5F / 1;

        float f12 = (float) Math.toRadians(-13) + f1;
        if (f12 > 0.0F) {
            f12 = 0.0F;
        }
        if (f12 < Math.toRadians(-20)) {
            f12 = (float) Math.toRadians(-20);
        }
        this.Tail_1.rotateAngleX = f12;
        this.Tail_2.rotateAngleX = this.Tail_2.rotateAngleX - f12 + (float) Math.toRadians(-13);
        f12 = 0.0F;

        this.chainFlap(SNAKEL1, speed_idle, degree_idle * 0.75F, -3, f2, 1);
        this.chainSwing(SNAKEL1, speed_idle, degree_idle * 0.75F, -3, f2, 1);

        this.chainFlap(SNAKEL2, speed_idle, degree_idle * 0.75F, -3, f2, 1);
        this.chainSwing(SNAKEL2, speed_idle, degree_idle * 0.75F, -3, f2, 1);

        this.chainFlap(SNAKEL3, speed_idle, degree_idle * 0.75F, -3, f2, 1);
        this.chainSwing(SNAKEL3, speed_idle, degree_idle * 0.75F, -3, f2, 1);

        this.chainFlap(SNAKEL4, speed_idle, degree_idle * 0.75F, -3, f2, 1);
        this.chainSwing(SNAKEL4, speed_idle, degree_idle * 0.75F, -3, f2, 1);

        this.chainFlap(SNAKER1, speed_idle, degree_idle * 0.75F, -3, f2, 1);
        this.chainSwing(SNAKER1, speed_idle, degree_idle * 0.75F, -3, f2, 1);

        this.chainFlap(SNAKER2, speed_idle, degree_idle * 0.75F, -3, f2, 1);
        this.chainSwing(SNAKER2, speed_idle, degree_idle * 0.75F, -3, f2, 1);

        this.chainFlap(SNAKER3, speed_idle, degree_idle * 0.75F, -3, f2, 1);
        this.chainSwing(SNAKER3, speed_idle, degree_idle * 0.75F, -3, f2, 1);

        this.chainFlap(SNAKER4, speed_idle, degree_idle * 0.75F, -3, f2, 1);
        this.chainSwing(SNAKER4, speed_idle, degree_idle * 0.75F, -3, f2, 1);
        this.faceTarget(f3, f4, 1, this.Head);

        float deathProg = Math.min(40, (float) entity.deathTime) / 2;

        this.progressRotation(Tail_1, deathProg, (float) Math.toRadians(5), (float) Math.toRadians(57), 0);
        this.progressPosition(Tail_1, deathProg, -5, 22, -4);
        this.progressRotation(Tail_2, deathProg, (float) Math.toRadians(18), (float) Math.toRadians(-54), 0);
        this.progressRotation(Body, deathProg, (float) Math.toRadians(-9), (float) Math.toRadians(36), 0);
        this.progressRotation(Right_Arm, deathProg, 0, 0, (float) Math.toRadians(20));
        this.progressRotation(Left_Arm, deathProg, 0, 0, (float) Math.toRadians(-20));
        this.Neck.isHidden = deathProg > 0;
    }

    @Override
    public void renderStatue() {
        this.resetToDefaultPose();
        this.Tail_1.render(0.0625F);
    }
}
