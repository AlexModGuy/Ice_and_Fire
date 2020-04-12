package com.github.alexthe666.iceandfire.client.model;

import com.github.alexthe666.iceandfire.entity.EntityHydra;
import net.ilexiconn.llibrary.client.model.ModelAnimator;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.entity.Entity;

public class ModelHydraHead extends ModelDragonBase {
    public AdvancedModelRenderer Neck1;
    public AdvancedModelRenderer Neck2;
    public AdvancedModelRenderer Neck3;
    public AdvancedModelRenderer Neck4;
    public AdvancedModelRenderer Head1;
    public AdvancedModelRenderer neckSpike1;
    public AdvancedModelRenderer neckSpike2;
    public AdvancedModelRenderer UpperJaw1;
    public AdvancedModelRenderer LowerJaw1;
    public AdvancedModelRenderer TeethTR1;
    public AdvancedModelRenderer TeethL1;
    public AdvancedModelRenderer TeethR1;
    public AdvancedModelRenderer TeethTL1;
    private ModelAnimator animator;

    public ModelHydraHead() {
        this.textureWidth = 256;
        this.textureHeight = 128;
        this.Neck3 = new AdvancedModelRenderer(this, 25, 90);
        this.Neck3.setRotationPoint(0.0F, -0.5F, -6.7F);
        this.Neck3.addBox(-1.92F, -2.0F, -8.0F, 4, 4, 9, 0.0F);
        this.setRotateAngle(Neck3, -0.12217304763960307F, -0.0F, 0.0F);
        this.TeethR1 = new AdvancedModelRenderer(this, 6, 44);
        this.TeethR1.mirror = true;
        this.TeethR1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.TeethR1.addBox(-0.2F, 0.8F, -4.1F, 2, 2, 6, 0.0F);
        this.neckSpike2 = new AdvancedModelRenderer(this, 0, 0);
        this.neckSpike2.setRotationPoint(0.0F, -0.9F, -3.7F);
        this.neckSpike2.addBox(-0.5F, -0.5F, 0.0F, 1, 1, 4, 0.0F);
        this.setRotateAngle(neckSpike2, 0.6829473363053812F, -0.0F, 0.0F);
        this.Head1 = new AdvancedModelRenderer(this, 6, 77);
        this.Head1.setRotationPoint(0.0F, -0.2F, -8.3F);
        this.Head1.addBox(-3.0F, -1.3F, -2.5F, 6, 3, 4, 0.0F);
        this.setRotateAngle(Head1, 0.7853981633974483F, -0.0F, 0.0F);
        this.LowerJaw1 = new AdvancedModelRenderer(this, 6, 63);
        this.LowerJaw1.setRotationPoint(0.0F, 1.9F, -0.8F);
        this.LowerJaw1.addBox(-2.0F, -0.6F, -7.0F, 4, 1, 7, 0.0F);
        this.Neck2 = new AdvancedModelRenderer(this, 85, 72);
        this.Neck2.setRotationPoint(0.0F, 0.2F, -6.8F);
        this.Neck2.addBox(-2.0F, -2.8F, -7.0F, 4, 5, 8, 0.0F);
        this.setRotateAngle(Neck2, -0.5235987755982988F, 0.0F, 0.0F);
        this.Neck1 = new AdvancedModelRenderer(this, 56, 80);
        this.Neck1.setRotationPoint(-0.0F, 1.5F, -0.2F);
        this.Neck1.addBox(-2.5F, -3.0F, -7.0F, 5, 6, 9, 0.0F);
        this.setRotateAngle(Neck1, -0.3490658503988659F, 0.0F, 0.0F);
        this.TeethL1 = new AdvancedModelRenderer(this, 6, 44);
        this.TeethL1.setRotationPoint(0.0F, 0.9F, -2.8F);
        this.TeethL1.addBox(-1.9F, 0.8F, -4.1F, 2, 2, 6, 0.0F);
        this.setRotateAngle(TeethL1, 0.045553093477052F, -0.0F, -3.141592653589793F);
        this.Neck4 = new AdvancedModelRenderer(this, 35, 70);
        this.Neck4.mirror = true;
        this.Neck4.setRotationPoint(0.0F, 0.0F, -7.4F);
        this.Neck4.addBox(-2.0F, -2.0F, -8.2F, 4, 4, 9, 0.0F);
        this.setRotateAngle(Neck4, 0.47123889803846897F, 0.0F, 0.0F);
        this.TeethTR1 = new AdvancedModelRenderer(this, 6, 44);
        this.TeethTR1.setRotationPoint(0.0F, -0.4F, -3.6F);
        this.TeethTR1.addBox(-1.9F, 0.8F, -4.1F, 2, 2, 6, 0.0F);
        this.setRotateAngle(TeethTR1, -0.091106186954104F, -0.0F, 0.0F);
        this.neckSpike1 = new AdvancedModelRenderer(this, 40, 0);
        this.neckSpike1.setRotationPoint(0.0F, -1.2F, -6.0F);
        this.neckSpike1.addBox(-0.5F, -0.5F, 0.0F, 1, 1, 3, 0.0F);
        this.setRotateAngle(neckSpike1, 0.7740535232594852F, 0.0F, 0.0F);
        this.TeethTL1 = new AdvancedModelRenderer(this, 6, 44);
        this.TeethTL1.mirror = true;
        this.TeethTL1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.TeethTL1.addBox(-0.2F, 0.8F, -4.1F, 2, 2, 6, 0.0F);
        this.UpperJaw1 = new AdvancedModelRenderer(this, 6, 54);
        this.UpperJaw1.setRotationPoint(0.0F, 0.0F, -2.4F);
        this.UpperJaw1.addBox(-2.5F, -1.7F, -5.8F, 5, 3, 6, 0.0F);
        this.setRotateAngle(UpperJaw1, 0.091106186954104F, -0.0F, 0.0F);
        this.Neck2.addChild(this.Neck3);
        this.TeethL1.addChild(this.TeethR1);
        this.Neck4.addChild(this.neckSpike2);
        this.Neck4.addChild(this.Head1);
        this.Head1.addChild(this.LowerJaw1);
        this.Neck1.addChild(this.Neck2);
        this.LowerJaw1.addChild(this.TeethL1);
        this.Neck3.addChild(this.Neck4);
        this.Head1.addChild(this.TeethTR1);
        this.Neck4.addChild(this.neckSpike1);
        this.TeethTR1.addChild(this.TeethTL1);
        this.Head1.addChild(this.UpperJaw1);
        animator = ModelAnimator.create();
        this.updateDefaultPose();
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        animate((IAnimatedEntity) entity, f, f1, f2, f3, f4, f5);
        this.Neck1.render(f5);
    }

    public void animate(IAnimatedEntity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        this.resetToDefaultPose();
        setRotationAngles(f, f1, f2, f3, f4, f5, (EntityHydra) entity);
        animator.update(entity);
    }

    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, EntityHydra entity) {
        float speed_walk = 0.6F;
        float speed_idle = 0.05F;
        float degree_walk = 1F;
        float degree_idle = 0.5F;
    }

    @Override
    public void renderStatue() {
        this.resetToDefaultPose();
        this.Neck1.render(0.0625F);
    }
}
