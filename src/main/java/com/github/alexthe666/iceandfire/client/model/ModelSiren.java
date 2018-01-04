package com.github.alexthe666.iceandfire.client.model;

import com.github.alexthe666.iceandfire.entity.EntityGorgon;
import com.github.alexthe666.iceandfire.entity.EntitySiren;
import net.ilexiconn.llibrary.client.model.ModelAnimator;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;

public class ModelSiren extends ModelDragonBase {
    public AdvancedModelRenderer Tail_1;
    public AdvancedModelRenderer Tail_2;
    public AdvancedModelRenderer Body;
    public AdvancedModelRenderer Fin1;
    public AdvancedModelRenderer Tail_3;
    public AdvancedModelRenderer Fin2;
    public AdvancedModelRenderer FlukeL;
    public AdvancedModelRenderer FlukeR;
    public AdvancedModelRenderer Fin3;
    public AdvancedModelRenderer Left_Arm;
    public AdvancedModelRenderer Head;
    public AdvancedModelRenderer Right_Arm;
    public AdvancedModelRenderer Neck;
    public AdvancedModelRenderer Hair1;
    public AdvancedModelRenderer HairR;
    public AdvancedModelRenderer HairL;
    public AdvancedModelRenderer Mouth;
    public AdvancedModelRenderer Jaw;
    public AdvancedModelRenderer Hair2;
    private ModelAnimator animator;

    public ModelSiren() {
        this.textureWidth = 128;
        this.textureHeight = 64;
        this.Left_Arm = new AdvancedModelRenderer(this, 40, 16);
        this.Left_Arm.mirror = true;
        this.Left_Arm.setRotationPoint(5.0F, -10.0F, 0.0F);
        this.Left_Arm.addBox(-1.0F, -2.0F, -2.0F, 3, 12, 4, 0.0F);
        this.setRotateAngle(Left_Arm, -0.6981317007977318F, 0.0F, 0.0F);
        this.HairR = new AdvancedModelRenderer(this, 80, 16);
        this.HairR.setRotationPoint(-1.8F, -7.8F, 3.2F);
        this.HairR.addBox(-1.9F, -10.7F, -0.3F, 2, 11, 4, 0.0F);
        this.setRotateAngle(HairR, -2.5830872929516078F, 0.0F, 0.08726646259971647F);
        this.Mouth = new AdvancedModelRenderer(this, 40, 0);
        this.Mouth.setRotationPoint(0.0F, -1.3F, 0.0F);
        this.Mouth.addBox(-2.5F, -0.6F, -4.6F, 5, 3, 2, 0.0F);
        this.setRotateAngle(Mouth, -0.36425021489121656F, 0.0F, 0.0F);
        this.Fin2 = new AdvancedModelRenderer(this, 72, 34);
        this.Fin2.setRotationPoint(0.0F, 5.8F, 1.9F);
        this.Fin2.addBox(-1.0F, -5.5F, 0.8F, 1, 11, 4, 0.0F);
        this.Tail_3 = new AdvancedModelRenderer(this, 52, 34);
        this.Tail_3.setRotationPoint(0.0F, 10.4F, 0.1F);
        this.Tail_3.addBox(-3.0F, 0.0F, -1.9F, 6, 13, 4, 0.0F);
        this.Neck = new AdvancedModelRenderer(this, 40, 8);
        this.Neck.setRotationPoint(0.0F, -12.0F, 0.0F);
        this.Neck.addBox(-3.0F, -3.7F, -1.0F, 6, 4, 1, 0.0F);
        this.Hair2 = new AdvancedModelRenderer(this, 81, 16);
        this.Hair2.setRotationPoint(0.0F, -1.5F, 2.9F);
        this.Hair2.addBox(-3.5F, -11.9F, 0.2F, 7, 11, 3, 0.0F);
        this.setRotateAngle(Hair2, -0.22759093446006054F, 0.0F, 0.0F);
        this.Fin3 = new AdvancedModelRenderer(this, 72, 15);
        this.Fin3.setRotationPoint(0.0F, 6.1F, 1.9F);
        this.Fin3.addBox(-0.9F, -5.5F, 0.3F, 1, 13, 3, 0.0F);
        this.Fin1 = new AdvancedModelRenderer(this, 84, 34);
        this.Fin1.setRotationPoint(0.0F, 6.1F, 1.9F);
        this.Fin1.addBox(-1.0F, -5.4F, 0.8F, 1, 11, 3, 0.0F);
        this.Tail_1 = new AdvancedModelRenderer(this, 0, 35);
        this.Tail_1.setRotationPoint(0.0F, 22.2F, -0.2F);
        this.Tail_1.addBox(-4.0F, -0.1F, -1.8F, 8, 11, 5, 0.1F);
        this.setRotateAngle(Tail_1, 1.5707963267948966F, 0.0F, 0.0F);
        this.Head = new AdvancedModelRenderer(this, 0, 0);
        this.Head.setRotationPoint(0.0F, -12.0F, 0.0F);
        this.Head.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0.0F);
        this.setRotateAngle(Head, -0.4553564018453205F, 0.0F, 0.0F);
        this.FlukeL = new AdvancedModelRenderer(this, 106, 34);
        this.FlukeL.setRotationPoint(0.0F, 12.3F, 0.1F);
        this.FlukeL.addBox(-3.5F, -0.1F, -0.5F, 7, 11, 1, 0.0F);
        this.setRotateAngle(FlukeL, -0.03490658503988659F, -0.08726646259971647F, -0.5235987755982988F);
        this.Tail_2 = new AdvancedModelRenderer(this, 27, 34);
        this.Tail_2.setRotationPoint(0.0F, 10.4F, 0.1F);
        this.Tail_2.addBox(-3.5F, 0.0F, -1.9F, 7, 11, 5, 0.0F);
        this.FlukeR = new AdvancedModelRenderer(this, 106, 34);
        this.FlukeR.mirror = true;
        this.FlukeR.setRotationPoint(0.0F, 12.3F, 0.1F);
        this.FlukeR.addBox(-3.5F, -0.1F, -0.5F, 7, 11, 1, 0.0F);
        this.setRotateAngle(FlukeR, -0.03490658503988659F, 0.08726646259971647F, 0.5235987755982988F);
        this.Right_Arm = new AdvancedModelRenderer(this, 40, 16);
        this.Right_Arm.setRotationPoint(-5.0F, -10.0F, 0.0F);
        this.Right_Arm.addBox(-2.0F, -2.0F, -2.0F, 3, 12, 4, 0.0F);
        this.setRotateAngle(Right_Arm, -0.6981317007977318F, 0.045553093477052F, 0.0F);
        this.Hair1 = new AdvancedModelRenderer(this, 80, 16);
        this.Hair1.setRotationPoint(0.0F, -7.8F, 3.2F);
        this.Hair1.addBox(-3.5F, -10.7F, -0.3F, 7, 11, 4, 0.0F);
        this.setRotateAngle(Hair1, -2.1855012893472994F, 0.0F, 0.0F);
        this.Jaw = new AdvancedModelRenderer(this, 24, 0);
        this.Jaw.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.Jaw.addBox(-2.0F, -0.6F, -4.6F, 4, 1, 3, 0.0F);
        this.setRotateAngle(Jaw, 0.045553093477052F, 0.0F, 0.0F);
        this.Body = new AdvancedModelRenderer(this, 16, 16);
        this.Body.setRotationPoint(0.0F, 0.9F, 1.0F);
        this.Body.addBox(-4.0F, -12.0F, -2.0F, 8, 12, 4, 0.0F);
        this.setRotateAngle(Body, -0.8196066167365371F, 0.0F, 0.0F);
        this.HairL = new AdvancedModelRenderer(this, 80, 16);
        this.HairL.mirror = true;
        this.HairL.setRotationPoint(1.8F, -7.3F, 3.2F);
        this.HairL.addBox(0.1F, -10.7F, -0.3F, 2, 11, 4, 0.0F);
        this.setRotateAngle(HairL, -2.5830872929516078F, 0.0F, -0.08726646259971647F);
        this.Body.addChild(this.Left_Arm);
        this.Head.addChild(this.HairR);
        this.Head.addChild(this.Mouth);
        this.Tail_2.addChild(this.Fin2);
        this.Tail_2.addChild(this.Tail_3);
        this.Body.addChild(this.Neck);
        this.Hair1.addChild(this.Hair2);
        this.Tail_3.addChild(this.Fin3);
        this.Tail_1.addChild(this.Fin1);
        this.Body.addChild(this.Head);
        this.Tail_3.addChild(this.FlukeL);
        this.Tail_1.addChild(this.Tail_2);
        this.Tail_3.addChild(this.FlukeR);
        this.Body.addChild(this.Right_Arm);
        this.Head.addChild(this.Hair1);
        this.Head.addChild(this.Jaw);
        this.Tail_1.addChild(this.Body);
        this.Head.addChild(this.HairL);
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
        setRotationAngles(f, f1, f2, f3, f4, f5, (EntitySiren) entity);
        animator.update(entity);
    }

    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, EntitySiren entity) {

    }

    @Override
    public void renderStatue() {
        this.resetToDefaultPose();
        this.Tail_1.render(0.0625F);
    }
}
