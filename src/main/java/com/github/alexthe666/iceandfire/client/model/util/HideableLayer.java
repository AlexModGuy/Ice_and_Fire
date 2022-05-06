package com.github.alexthe666.iceandfire.client.model.util;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.entity.Entity;


public class HideableLayer<T extends Entity, M extends EntityModel<T>, C extends LayerRenderer<T, M>> extends LayerRenderer<T, M> {

    public boolean hidden;
    C layerRenderer;

    public HideableLayer(C layerRenderer, IEntityRenderer<T, M> entityRendererIn) {
        super(entityRendererIn);
        hidden = false;
        this.layerRenderer = layerRenderer;
    }

    @Override
    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!hidden)
            layerRenderer.render(matrixStackIn, bufferIn, packedLightIn, entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
    }
}
