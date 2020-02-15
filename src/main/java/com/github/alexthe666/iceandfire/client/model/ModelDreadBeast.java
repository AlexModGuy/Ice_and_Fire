package com.github.alexthe666.iceandfire.client.model;

import com.github.alexthe666.iceandfire.entity.EntityDreadBeast;
import com.github.alexthe666.iceandfire.entity.EntityDreadGhoul;
import net.ilexiconn.llibrary.client.model.ModelAnimator;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;

public class ModelDreadBeast extends ModelDragonBase {
    public AdvancedModelRenderer Body;
    public AdvancedModelRenderer LegL1;
    public AdvancedModelRenderer LowerBody;
    public AdvancedModelRenderer Neck1;
    public AdvancedModelRenderer LegR1;
    public AdvancedModelRenderer LegL2;
    public AdvancedModelRenderer Tail;
    public AdvancedModelRenderer BackLegR1;
    public AdvancedModelRenderer BackLegL1;
    public AdvancedModelRenderer Tail2;
    public AdvancedModelRenderer Tail3;
    public AdvancedModelRenderer BackLegR2;
    public AdvancedModelRenderer BackLegL2;
    public AdvancedModelRenderer HeadBase;
    public AdvancedModelRenderer HeadFront;
    public AdvancedModelRenderer Jaw;
    public AdvancedModelRenderer ChopsR;
    public AdvancedModelRenderer ChopsL;
    public AdvancedModelRenderer Nose;
    public AdvancedModelRenderer LegR2;
    private ModelAnimator animator;

    public ModelDreadBeast() {
        this.textureWidth = 256;
        this.textureHeight = 128;
        this.LegR1 = new AdvancedModelRenderer(this, 0, 54);
        this.LegR1.setRotationPoint(-3.3F, 1.0F, -3.5F);
        this.LegR1.addBox(-1.0F, 0.0F, -1.5F, 2, 7, 3, 0.0F);
        this.LegR2 = new AdvancedModelRenderer(this, 11, 54);
        this.LegR2.setRotationPoint(0.0F, 6.4F, 0.1F);
        this.LegR2.addBox(-1.01F, 0.0F, -1.6F, 2, 6, 2, 0.0F);
        this.LegL1 = new AdvancedModelRenderer(this, 0, 54);
        this.LegL1.mirror = true;
        this.LegL1.setRotationPoint(3.3F, 1.0F, -3.5F);
        this.LegL1.addBox(-1.0F, 0.0F, -1.5F, 2, 7, 3, 0.0F);
        this.ChopsR = new AdvancedModelRenderer(this, 38, 0);
        this.ChopsR.setRotationPoint(-2.2F, 1.6F, -1.0F);
        this.ChopsR.addBox(-3.0F, -0.5F, -3.6F, 4, 2, 5, 0.0F);
        this.setRotateAngle(ChopsR, -2.5953045977155678F, 0.8196066167365371F, -0.9560913642424937F);
        this.Neck1 = new AdvancedModelRenderer(this, 0, 27);
        this.Neck1.setRotationPoint(0.0F, -1.0F, -4.2F);
        this.Neck1.addBox(-2.5F, -2.0F, -6.2F, 5, 7, 7, 0.0F);
        this.setRotateAngle(Neck1, -0.136659280431156F, 0.0F, 0.0F);
        this.Tail = new AdvancedModelRenderer(this, 50, 15);
        this.Tail.setRotationPoint(0.0F, -1.6F, 11.2F);
        this.Tail.addBox(-1.5F, -1.0F, 0.0F, 3, 3, 7, 0.0F);
        this.setRotateAngle(Tail, -1.0471975511965976F, 0.0F, 0.0F);
        this.BackLegR2 = new AdvancedModelRenderer(this, 0, 43);
        this.BackLegR2.mirror = true;
        this.BackLegR2.setRotationPoint(0.0F, 7.5F, 0.4F);
        this.BackLegR2.addBox(-1.01F, 0.0F, -0.3F, 2, 7, 2, 0.0F);
        this.LowerBody = new AdvancedModelRenderer(this, 30, 25);
        this.LowerBody.setRotationPoint(0.0F, 0.0F, 2.0F);
        this.LowerBody.addBox(-3.0F, -2.7F, -0.1F, 6, 8, 12, 0.0F);
        this.BackLegR1 = new AdvancedModelRenderer(this, 20, 52);
        this.BackLegR1.mirror = true;
        this.BackLegR1.setRotationPoint(2.5F, -0.5F, 8.3F);
        this.BackLegR1.addBox(-1.0F, 0.0F, -1.5F, 2, 8, 4, 0.0F);
        this.HeadFront = new AdvancedModelRenderer(this, 0, 7);
        this.HeadFront.setRotationPoint(0.0F, -0.4F, -3.1F);
        this.HeadFront.addBox(-1.5F, -1.4F, -5.4F, 3, 3, 5, 0.0F);
        this.setRotateAngle(HeadFront, -0.31869712141416456F, 0.0F, 0.0F);
        this.LegL2 = new AdvancedModelRenderer(this, 11, 54);
        this.LegL2.mirror = true;
        this.LegL2.setRotationPoint(0.0F, 6.4F, 0.1F);
        this.LegL2.addBox(-1.01F, 0.0F, -1.6F, 2, 6, 2, 0.0F);
        this.ChopsL = new AdvancedModelRenderer(this, 37, 0);
        this.ChopsL.mirror = true;
        this.ChopsL.setRotationPoint(2.2F, 1.6F, -1.0F);
        this.ChopsL.addBox(-1.0F, -0.5F, -3.6F, 4, 2, 5, 0.0F);
        this.setRotateAngle(ChopsL, -2.5953045977155678F, -0.8196066167365371F, 0.9560913642424937F);
        this.Nose = new AdvancedModelRenderer(this, 0, 3);
        this.Nose.setRotationPoint(0.0F, -1.2F, -4.4F);
        this.Nose.addBox(-1.0F, -0.4F, -1.1F, 2, 1, 1, 0.0F);
        this.setRotateAngle(Nose, 0.136659280431156F, 0.0F, 0.0F);
        this.HeadBase = new AdvancedModelRenderer(this, 0, 16);
        this.HeadBase.setRotationPoint(0.0F, 0.7F, -5.6F);
        this.HeadBase.addBox(-3.5F, -2.51F, -3.8F, 7, 6, 5, 0.0F);
        this.setRotateAngle(HeadBase, 0.6829473363053812F, 0.0F, 0.0F);
        this.Tail2 = new AdvancedModelRenderer(this, 66, 22);
        this.Tail2.setRotationPoint(0.0F, -0.2F, 6.0F);
        this.Tail2.addBox(-1.51F, -1.0F, 0.0F, 3, 4, 8, 0.0F);
        this.setRotateAngle(Tail2, 0.136659280431156F, 0.0F, 0.0F);
        this.Body = new AdvancedModelRenderer(this, 34, 45);
        this.Body.setRotationPoint(0.0F, 10.4F, -4.0F);
        this.Body.addBox(-4.0F, -3.4F, -6.0F, 8, 9, 9, 0.0F);
        this.BackLegL2 = new AdvancedModelRenderer(this, 0, 43);
        this.BackLegL2.setRotationPoint(0.0F, 7.5F, 0.4F);
        this.BackLegL2.addBox(-1.01F, 0.0F, -0.3F, 2, 7, 2, 0.0F);
        this.Jaw = new AdvancedModelRenderer(this, 29, 11);
        this.Jaw.setRotationPoint(0.0F, 2.6F, -3.2F);
        this.Jaw.addBox(-1.0F, -0.7F, -5.9F, 2, 2, 6, 0.0F);
        this.setRotateAngle(Jaw, 0.4553564018453205F, 0.0F, -3.141592653589793F);
        this.BackLegL1 = new AdvancedModelRenderer(this, 20, 52);
        this.BackLegL1.setRotationPoint(-2.5F, -0.5F, 8.3F);
        this.BackLegL1.addBox(-1.0F, 0.0F, -1.5F, 2, 8, 4, 0.0F);
        this.Tail3 = new AdvancedModelRenderer(this, 64, 13);
        this.Tail3.setRotationPoint(0.0F, 1.0F, 6.7F);
        this.Tail3.addBox(-1.01F, -1.7F, 0.0F, 2, 3, 3, 0.0F);
        this.setRotateAngle(Tail3, 0.008726646259971648F, 0.0F, 0.0F);
        this.Body.addChild(this.LegR1);
        this.LegR1.addChild(this.LegR2);
        this.Body.addChild(this.LegL1);
        this.HeadBase.addChild(this.ChopsR);
        this.Body.addChild(this.Neck1);
        this.LowerBody.addChild(this.Tail);
        this.BackLegR1.addChild(this.BackLegR2);
        this.Body.addChild(this.LowerBody);
        this.LowerBody.addChild(this.BackLegR1);
        this.HeadBase.addChild(this.HeadFront);
        this.LegL1.addChild(this.LegL2);
        this.HeadBase.addChild(this.ChopsL);
        this.HeadFront.addChild(this.Nose);
        this.Neck1.addChild(this.HeadBase);
        this.Tail.addChild(this.Tail2);
        this.BackLegL1.addChild(this.BackLegL2);
        this.HeadBase.addChild(this.Jaw);
        this.LowerBody.addChild(this.BackLegL1);
        this.Tail2.addChild(this.Tail3);
        animator = ModelAnimator.create();
        this.updateDefaultPose();
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        animate((IAnimatedEntity) entity, f, f1, f2, f3, f4, f5);
        this.Body.render(f5);
    }

    public void animate(IAnimatedEntity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        this.resetToDefaultPose();
        setRotationAngles(f, f1, f2, f3, f4, f5, (EntityDreadBeast) entity);
        animator.update(entity);
    }

    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, EntityDreadBeast beast) {
        float speed_walk = 0.6F;
        float speed_idle = 0.05F;
        float degree_walk = 1F;
        float degree_idle = 0.5F;

    }

    @Override
    public void renderStatue() {
        this.resetToDefaultPose();
        this.Body.render(0.0625F);
    }
}
