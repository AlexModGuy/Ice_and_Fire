package com.github.alexthe666.iceandfire.client.model;

import com.github.alexthe666.iceandfire.entity.EntityHippocampus;
import net.ilexiconn.llibrary.client.model.ModelAnimator;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.entity.Entity;

public class ModelHippocampus extends ModelDragonBase {
    public AdvancedModelRenderer Body;
    public AdvancedModelRenderer FrontThighR;
    public AdvancedModelRenderer FrontThighL;
    public AdvancedModelRenderer Neck;
    public AdvancedModelRenderer Tail_1;
    public AdvancedModelRenderer FinRBack;
    public AdvancedModelRenderer FinLBack;
    public AdvancedModelRenderer Saddle;
    public AdvancedModelRenderer FrontLegR;
    public AdvancedModelRenderer FrontFootR;
    public AdvancedModelRenderer FinR;
    public AdvancedModelRenderer FrontLegL;
    public AdvancedModelRenderer FrontFootL;
    public AdvancedModelRenderer FinL;
    public AdvancedModelRenderer Head;
    public AdvancedModelRenderer Mane;
    public AdvancedModelRenderer TopJaw;
    public AdvancedModelRenderer BottomJaw;
    public AdvancedModelRenderer NoseBand;
    public AdvancedModelRenderer ReinL;
    public AdvancedModelRenderer ReinR;
    public AdvancedModelRenderer Tail_2;
    public AdvancedModelRenderer Fin;
    public AdvancedModelRenderer Tail_3;
    public AdvancedModelRenderer FlukeR;
    public AdvancedModelRenderer FlukeL;
    public AdvancedModelRenderer StirrupR;
    public AdvancedModelRenderer ChestR;
    public AdvancedModelRenderer ChestL;
    public AdvancedModelRenderer Saddleback;
    public AdvancedModelRenderer SaddleFront;
    public AdvancedModelRenderer StirrupL;
    public AdvancedModelRenderer StirrupIronR;
    public AdvancedModelRenderer StirrupIronL;
    private ModelAnimator animator;

    public ModelHippocampus() {
        this.animator = ModelAnimator.create();
        this.textureWidth = 128;
        this.textureHeight = 128;
        this.Body = new AdvancedModelRenderer(this, 0, 34);
        this.Body.setRotationPoint(0.0F, 11.0F, 9.0F);
        this.Body.addBox(-5.0F, -8.0F, -19.0F, 10, 10, 24, 0.0F);
        this.Fin = new AdvancedModelRenderer(this, 57, 33);
        this.Fin.setRotationPoint(0.0F, 8.2F, -0.7F);
        this.Fin.addBox(0.0F, -10.1F, 3.7F, 1, 18, 5, 0.0F);
        this.setRotateAngle(Fin, -0.091106186954104F, 0.0F, 0.0F);
        this.BottomJaw = new AdvancedModelRenderer(this, 24, 27);
        this.BottomJaw.setRotationPoint(0.0F, 0.3F, -5.5F);
        this.BottomJaw.addBox(-2.0F, 0.0F, -6.5F, 4, 2, 5, 0.0F);
        this.Saddleback = new AdvancedModelRenderer(this, 80, 9);
        this.Saddleback.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.Saddleback.addBox(-4.0F, -1.0F, 3.0F, 8, 1, 2, 0.0F);
        this.NoseBand = new AdvancedModelRenderer(this, 85, 60);
        this.NoseBand.setRotationPoint(0.0F, 7.9F, -4.5F);
        this.NoseBand.addBox(-3.0F, -11.1F, -7.0F, 6, 6, 12, 0.0F);
        this.FlukeL = new AdvancedModelRenderer(this, 106, 90);
        this.FlukeL.setRotationPoint(0.0F, 16.2F, 0.1F);
        this.FlukeL.addBox(-3.5F, -0.1F, -0.5F, 7, 18, 1, 0.0F);
        this.setRotateAngle(FlukeL, -0.03490658503988659F, -0.08726646259971647F, -0.5235987755982988F);
        this.Saddle = new AdvancedModelRenderer(this, 80, 0);
        this.Saddle.setRotationPoint(0.0F, -8.9F, -7.0F);
        this.Saddle.addBox(-5.0F, 0.0F, -3.0F, 10, 1, 8, 0.0F);
        this.FinL = new AdvancedModelRenderer(this, 80, 43);
        this.FinL.mirror = true;
        this.FinL.setRotationPoint(-0.5F, 1.0F, -0.7F);
        this.FinL.addBox(-0.5F, -7.6F, 2.2F, 1, 9, 5, 0.0F);
        this.StirrupR = new AdvancedModelRenderer(this, 80, 0);
        this.StirrupR.setRotationPoint(-5.0F, 1.0F, 0.0F);
        this.StirrupR.addBox(-0.5F, 0.0F, -0.5F, 1, 6, 1, 0.0F);
        this.FrontThighL = new AdvancedModelRenderer(this, 96, 29);
        this.FrontThighL.mirror = true;
        this.FrontThighL.setRotationPoint(4.0F, -2.0F, -16.7F);
        this.FrontThighL.addBox(-2.5F, -2.0F, -2.5F, 4, 9, 5, 0.0F);
        this.setRotateAngle(FrontThighL, 0.0F, 0.0F, -0.22689280275926282F);
        this.FrontLegL = new AdvancedModelRenderer(this, 96, 43);
        this.FrontLegL.mirror = true;
        this.FrontLegL.setRotationPoint(0.0F, 7.0F, 0.0F);
        this.FrontLegL.addBox(-2.0F, 0.0F, -1.5F, 3, 5, 3, 0.0F);
        this.setRotateAngle(FrontLegL, 0.6283185307179586F, 0.0F, 0.0F);
        this.Tail_3 = new AdvancedModelRenderer(this, 44, 102);
        this.Tail_3.setRotationPoint(0.0F, 10.4F, 0.1F);
        this.Tail_3.addBox(-3.0F, 0.0F, -1.9F, 6, 16, 4, 0.0F);
        this.setRotateAngle(Tail_3, 0.22759093446006054F, 0.0F, 0.0F);
        this.FrontFootL = new AdvancedModelRenderer(this, 96, 51);
        this.FrontFootL.mirror = true;
        this.FrontFootL.setRotationPoint(0.0F, 5.0F, 0.0F);
        this.FrontFootL.addBox(-2.5F, 0.0F, -2.0F, 4, 3, 4, 0.0F);
        this.FinRBack = new AdvancedModelRenderer(this, 80, 43);
        this.FinRBack.setRotationPoint(-3.1F, 0.0F, -0.7F);
        this.FinRBack.addBox(-2.0F, -4.5F, 0.4F, 1, 6, 7, 0.0F);
        this.setRotateAngle(FinRBack, -0.5235987755982988F, -0.17453292519943295F, 0.17453292519943295F);
        this.Mane = new AdvancedModelRenderer(this, 57, 33);
        this.Mane.setRotationPoint(0.0F, -5.6F, -0.3F);
        this.Mane.addBox(0.0F, -8.6F, 3.5F, 1, 18, 6, 0.0F);
        this.StirrupIronL = new AdvancedModelRenderer(this, 74, 0);
        this.StirrupIronL.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.StirrupIronL.addBox(-0.5F, 6.0F, -1.0F, 1, 2, 2, 0.0F);
        this.ChestL = new AdvancedModelRenderer(this, 0, 47);
        this.ChestL.setRotationPoint(4.5F, 1.0F, 8.0F);
        this.ChestL.addBox(-3.0F, 0.0F, 0.0F, 8, 8, 3, 0.0F);
        this.setRotateAngle(ChestL, 0.0F, 1.5707963267948966F, 0.0F);
        this.Head = new AdvancedModelRenderer(this, 0, 0);
        this.Head.setRotationPoint(0.0F, -10.6F, 2.7F);
        this.Head.addBox(-2.5F, -2.9F, -7.0F, 5, 5, 7, 0.0F);
        this.setRotateAngle(Head, -0.091106186954104F, 0.0F, 0.0F);
        this.Neck = new AdvancedModelRenderer(this, 0, 12);
        this.Neck.setRotationPoint(0.0F, -4.4F, -16.3F);
        this.Neck.addBox(-2.05F, -13.1F, -4.0F, 4, 14, 8, 0.0F);
        this.setRotateAngle(Neck, 0.6829473363053812F, 0.0F, 0.0F);
        this.FinR = new AdvancedModelRenderer(this, 80, 43);
        this.FinR.setRotationPoint(0.5F, 1.0F, -0.7F);
        this.FinR.addBox(-0.5F, -7.6F, 2.2F, 1, 9, 5, 0.0F);
        this.FrontFootR = new AdvancedModelRenderer(this, 96, 51);
        this.FrontFootR.setRotationPoint(0.0F, 5.0F, 0.0F);
        this.FrontFootR.addBox(-1.5F, 0.0F, -2.0F, 4, 3, 4, 0.0F);
        this.TopJaw = new AdvancedModelRenderer(this, 24, 18);
        this.TopJaw.setRotationPoint(0.0F, -0.1F, -5.6F);
        this.TopJaw.addBox(-2.0F, -2.6F, -7.0F, 4, 3, 6, 0.0F);
        this.Tail_2 = new AdvancedModelRenderer(this, 37, 80);
        this.Tail_2.setRotationPoint(0.0F, 11.8F, 0.1F);
        this.Tail_2.addBox(-3.5F, 0.0F, -3.0F, 7, 11, 6, 0.0F);
        this.setRotateAngle(Tail_2, -0.091106186954104F, 0.0F, 0.0F);
        this.FlukeR = new AdvancedModelRenderer(this, 106, 90);
        this.FlukeR.mirror = true;
        this.FlukeR.setRotationPoint(0.0F, 16.2F, 0.1F);
        this.FlukeR.addBox(-3.5F, -0.1F, -0.5F, 7, 18, 1, 0.0F);
        this.setRotateAngle(FlukeR, -0.03490658503988659F, 0.08726646259971647F, 0.5235987755982988F);
        this.StirrupL = new AdvancedModelRenderer(this, 70, 0);
        this.StirrupL.setRotationPoint(5.0F, 1.0F, 0.0F);
        this.StirrupL.addBox(-0.5F, 0.0F, -0.5F, 1, 6, 1, 0.0F);
        this.FrontThighR = new AdvancedModelRenderer(this, 96, 29);
        this.FrontThighR.setRotationPoint(-4.0F, -2.0F, -16.7F);
        this.FrontThighR.addBox(-1.5F, -2.0F, -2.5F, 4, 9, 5, 0.0F);
        this.setRotateAngle(FrontThighR, 0.0F, 0.0F, 0.22689280275926282F);
        this.FinLBack = new AdvancedModelRenderer(this, 80, 43);
        this.FinLBack.mirror = true;
        this.FinLBack.setRotationPoint(3.1F, 0.0F, -0.7F);
        this.FinLBack.addBox(1.0F, -4.5F, 0.4F, 1, 6, 7, 0.0F);
        this.setRotateAngle(FinLBack, -0.5235987755982988F, 0.17453292519943295F, -0.17453292519943295F);
        this.ReinL = new AdvancedModelRenderer(this, 46, 55);
        this.ReinL.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.ReinL.addBox(3.1F, -6.3F, -3.4F, 0, 3, 19, 0.0F);
        this.setRotateAngle(ReinL, -0.04363323129985824F, 0.0F, 0.0F);
        this.ReinR = new AdvancedModelRenderer(this, 46, 55);
        this.ReinR.mirror = true;
        this.ReinR.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.ReinR.addBox(-3.1F, -6.0F, -3.4F, 0, 3, 19, 0.0F);
        this.setRotateAngle(ReinR, -0.04363323129985824F, 0.0F, 0.0F);
        this.Tail_1 = new AdvancedModelRenderer(this, 0, 80);
        this.Tail_1.setRotationPoint(0.0F, -3.3F, 2.5F);
        this.Tail_1.addBox(-4.0F, 0.0F, -4.0F, 8, 13, 8, 0.0F);
        this.setRotateAngle(Tail_1, 1.5025539530419183F, 0.0F, 0.0F);
        this.FrontLegR = new AdvancedModelRenderer(this, 96, 43);
        this.FrontLegR.setRotationPoint(0.0F, 7.0F, 0.0F);
        this.FrontLegR.addBox(-1.0F, 0.0F, -1.5F, 3, 5, 3, 0.0F);
        this.setRotateAngle(FrontLegR, 0.6283185307179586F, 0.0F, 0.0F);
        this.SaddleFront = new AdvancedModelRenderer(this, 106, 9);
        this.SaddleFront.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.SaddleFront.addBox(-1.5F, -1.0F, -3.0F, 3, 1, 2, 0.0F);
        this.StirrupIronR = new AdvancedModelRenderer(this, 74, 4);
        this.StirrupIronR.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.StirrupIronR.addBox(-0.5F, 6.0F, -1.0F, 1, 2, 2, 0.0F);
        this.ChestR = new AdvancedModelRenderer(this, 0, 34);
        this.ChestR.setRotationPoint(-4.5F, 1.0F, 8.0F);
        this.ChestR.addBox(-3.0F, 0.0F, -3.0F, 8, 8, 3, 0.0F);
        this.setRotateAngle(ChestR, 0.0F, 1.5707963267948966F, 0.0F);
        this.Tail_1.addChild(this.Fin);
        this.Head.addChild(this.BottomJaw);
        this.Saddle.addChild(this.Saddleback);
        this.Head.addChild(this.NoseBand);
        this.Tail_3.addChild(this.FlukeL);
        this.Body.addChild(this.Saddle);
        this.FrontFootL.addChild(this.FinL);
        this.Saddle.addChild(this.StirrupR);
        this.Body.addChild(this.FrontThighL);
        this.FrontThighL.addChild(this.FrontLegL);
        this.Tail_2.addChild(this.Tail_3);
        this.FrontLegL.addChild(this.FrontFootL);
        this.Body.addChild(this.FinRBack);
        this.Neck.addChild(this.Mane);
        this.StirrupL.addChild(this.StirrupIronL);
        this.Saddle.addChild(this.ChestL);
        this.Neck.addChild(this.Head);
        this.Body.addChild(this.Neck);
        this.FrontFootR.addChild(this.FinR);
        this.FrontLegR.addChild(this.FrontFootR);
        this.Head.addChild(this.TopJaw);
        this.Tail_1.addChild(this.Tail_2);
        this.Tail_3.addChild(this.FlukeR);
        this.Saddle.addChild(this.StirrupL);
        this.Body.addChild(this.FrontThighR);
        this.Body.addChild(this.FinLBack);
        this.NoseBand.addChild(this.ReinL);
        this.NoseBand.addChild(this.ReinR);
        this.Body.addChild(this.Tail_1);
        this.FrontThighR.addChild(this.FrontLegR);
        this.Saddle.addChild(this.SaddleFront);
        this.StirrupR.addChild(this.StirrupIronR);
        this.Saddle.addChild(this.ChestR);
        this.updateDefaultPose();
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        animate((IAnimatedEntity) entity, f, f1, f2, f3, f4, f5);
        if (this.isChild) {
            this.Body.setShouldScaleChildren(true);
            this.Body.setScale(0.5F, 0.5F, 0.5F);
            this.Head.setScale(1.5F, 1.5F, 1.5F);
            this.TopJaw.setRotationPoint(0.0F, -0.1F, -7.6F);
            this.Body.setRotationPoint(0.0F, 12.5F, 4.0F);
            this.BottomJaw.setRotationPoint(0.0F, 0.3F, -7.5F);

        } else {
            this.Body.setScale(1, 1, 1);
            this.Head.setScale(1, 1, 1);
        }
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        this.Body.render(f5);
    }

    public void renderStatue() {
        this.resetToDefaultPose();
        this.Body.render(0.0625F);
        this.NoseBand.isHidden = true;
        this.ReinL.isHidden = true;
        this.ReinR.isHidden = true;
        this.ChestL.isHidden = true;
        this.ChestR.isHidden = true;
        this.Saddle.isHidden = true;
        this.Saddleback.isHidden = true;
        this.StirrupIronL.isHidden = true;
        this.StirrupIronR.isHidden = true;
        this.SaddleFront.isHidden = true;
        this.StirrupL.isHidden = true;
        this.StirrupR.isHidden = true;
    }

    public void animate(IAnimatedEntity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        this.resetToDefaultPose();
        animator.update(entity);
        animator.setAnimation(EntityHippocampus.ANIMATION_SPEAK);
        animator.startKeyframe(10);
        this.rotate(animator, Head, -10, 0, 0);
        this.rotate(animator, BottomJaw, 20, 0, 0);
        animator.endKeyframe();
        animator.resetKeyframe(5);
    }

    @Override
    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity) {
        EntityHippocampus hippo = (EntityHippocampus) entity;
        float speed_walk = 0.9F;
        float speed_idle = 0.05F;
        float speed_swim = 0.35F;
        float degree_walk = 1.5F;
        float degree_idle = 0.5F;
        float degree_swim = 0.75F;
        this.progressRotation(Body, hippo.onLandProgress, (float) Math.toRadians(-5F), 0.0F, 0.0F);
        this.progressRotation(FrontThighL, Math.max(0, hippo.onLandProgress), 0.0F, 0.0F, (float) Math.toRadians(-65F));
        this.progressRotation(FrontThighR, Math.max(0, hippo.onLandProgress), 0.0F, 0.0F, (float) Math.toRadians(65F));
        this.progressPosition(Body, hippo.onLandProgress, 0.0F, 20.0F, 0.0F);

        this.progressRotation(Body, hippo.sitProgress, (float) Math.toRadians(-5F), 0.0F, 0.0F);
        this.progressRotation(Tail_1, hippo.sitProgress, (float) Math.toRadians(55F), 0.0F, 0.0F);
        this.progressRotation(Tail_2, hippo.sitProgress, (float) Math.toRadians(-26F), 0.0F, 0.0F);
        this.progressRotation(Tail_3, hippo.sitProgress, (float) Math.toRadians(-33F), 0.0F, 0.0F);
        this.progressRotation(FlukeR, Math.max(0, hippo.sitProgress), (float) Math.toRadians(-50F), (float) Math.toRadians(5F), (float) Math.toRadians(30F));
        this.progressRotation(FlukeL, Math.max(0, hippo.sitProgress), (float) Math.toRadians(-50F), (float) Math.toRadians(-5F), (float) Math.toRadians(-30F));
        this.progressRotation(Body, hippo.sitProgress * hippo.onLandProgress * 0.05F, (float) Math.toRadians(-5F), (float) Math.toRadians(-5F), (float) Math.toRadians(85F));
        this.progressPosition(Body, hippo.sitProgress * hippo.onLandProgress * 0.05F, 0.0F, 10, 0.0F);
        if (hippo.onGround && !hippo.isInWater()) {
            this.progressRotation(FrontThighL, Math.max(0, hippo.sitProgress), 0.0F, 0.0F, (float) Math.toRadians(60F));
            this.progressRotation(FrontThighR, Math.max(0, hippo.sitProgress), 0.0F, 0.0F, (float) Math.toRadians(-60F));
        }
        this.progressRotation(Tail_2, hippo.sitProgress * hippo.onLandProgress * 0.05F, (float) Math.toRadians(-7F), (float) Math.toRadians(-25F), (float) Math.toRadians(1));
        this.progressRotation(Tail_3, hippo.sitProgress * hippo.onLandProgress * 0.05F, (float) Math.toRadians(20), (float) Math.toRadians(-36), (float) Math.toRadians(36));


        AdvancedModelRenderer[] TAIL = {Tail_1, Tail_2, Tail_3};
        AdvancedModelRenderer[] TAIL_W_BODY = {Body, Tail_1, Tail_2, Tail_3};
        AdvancedModelRenderer[] LEG_L = {FrontThighL, FrontLegL};
        AdvancedModelRenderer[] LEG_R = {FrontThighR, FrontLegR};
        AdvancedModelRenderer[] NECK = new AdvancedModelRenderer[]{Neck, Head};
        if (hippo.isInWater()) {
            this.chainWave(NECK, speed_swim, degree_swim * 0.15F, -2, f, f1);
            this.chainWave(TAIL_W_BODY, speed_swim, degree_swim * 0.15F, -3, f, f1);
            this.walk(Tail_3, speed_swim, degree_swim * -0.5F, false, 0, 0, f, f1);
            this.chainWave(LEG_L, speed_swim, degree_swim * 0.75F, 1, f, f1);
            this.chainWave(LEG_R, speed_swim, degree_swim * 0.75F, 1, f, f1);
            this.walk(Tail_1, speed_idle, degree_idle * 0.15F, false, 0, 0F, f2, 1);
            this.walk(Tail_2, speed_idle, degree_idle * 0.25F, false, 0, 0F, f2, 1);

        } else {
            this.chainWave(LEG_L, speed_walk, degree_walk * 0.5F, 1, f, f1);
            this.chainWave(LEG_R, speed_walk, degree_walk * 0.5F, 1, f, f1);
            this.walk(Body, speed_walk, degree_walk * 0.05F, false, 0, 0.1F, f, f1);
            this.walk(Tail_1, speed_walk, degree_walk * 0.05F, false, 1, 0.1F, f, f1);
            this.walk(Tail_2, speed_walk, degree_walk * 0.05F, false, 2, 0.1F, f, f1);
            this.walk(FrontThighL, speed_idle, degree_idle * 0.25F, false, 0, -0.1F, f2, 1);
            this.walk(FrontThighR, speed_idle, degree_idle * 0.25F, false, 0, -0.1F, f2, 1);
            this.walk(FrontLegL, speed_idle, degree_idle * 0.25F, false, 0, -0.1F, f2, 1);
            this.walk(FrontLegR, speed_idle, degree_idle * 0.25F, false, 0, -0.1F, f2, 1);
            this.swing(FinLBack, speed_idle, degree_idle * 0.25F, false, 0, -0.1F, f2, 1);
            this.swing(FinRBack, speed_idle, degree_idle * 0.25F, true, 0, -0.1F, f2, 1);
        }
        this.chainWave(NECK, speed_idle, degree_idle * 0.15F, -2, f2, 1);
        hippo.tail_buffer.applyChainSwingBuffer(TAIL);
    }
}
