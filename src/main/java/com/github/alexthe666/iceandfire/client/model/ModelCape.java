package com.github.alexthe666.iceandfire.client.model;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelCape extends ModelBiped {

    public ModelRenderer cape;

    public ModelCape(float f) {
        super(f, 0.0F, 64, 32);
        this.cape = new ModelRenderer(this, 0, 0);
        this.cape.setTextureSize(64, 32);
        this.cape.addBox(-5.0F, 0.0F, -1.0F, 10, 16, 1, f);
    }

    public void renderCape(float f) {
        this.cape.render(f);
    }

    @Override
    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity) {
        super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        if (entity.isShiftKeyDown()) {
            this.cape.rotationPointY = 2.0F;
        } else {
            this.cape.rotationPointY = 0.0F;
        }
    }

}
