package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.client.model.util.HideableModelRenderer;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelBase;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelBox;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelDreadLichSkull extends AdvancedModelBase {
    public HideableModelRenderer bipedHead;
    public HideableModelRenderer bipedHeadwear;

    public ModelDreadLichSkull() {
        this(0.0F);
    }

    public ModelDreadLichSkull(float modelSize) {
        this.textureHeight = 32;
        this.textureWidth = 64;
        this.bipedHead = new HideableModelRenderer(this, 0, 0);
        this.bipedHead.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, modelSize - 0.5F);
        this.bipedHead.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.bipedHeadwear = new HideableModelRenderer(this, 32, 0);
        this.bipedHeadwear.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, modelSize);
        this.bipedHeadwear.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.updateDefaultPose();
    }

    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
        this.resetToDefaultPose();
        this.bipedHead.render(scale);
        this.bipedHeadwear.render(scale);
    }
}