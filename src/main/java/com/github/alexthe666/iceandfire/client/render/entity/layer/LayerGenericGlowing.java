package com.github.alexthe666.iceandfire.client.render.entity.layer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;

public class LayerGenericGlowing<T extends LivingEntity, M extends EntityModel<T>> extends LayerRenderer<T, M> {
    private final LivingRenderer render;
    private final ResourceLocation texture;

    public LayerGenericGlowing(LivingRenderer renderIn, ResourceLocation texture) {
        super(renderIn);
        this.render = renderIn;
        this.texture = texture;
    }

    public boolean shouldCombineTextures() {
        return true;
    }

    @Override
    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, LivingEntity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        RenderType eyes = RenderType.eyes(texture);
        IVertexBuilder ivertexbuilder = bufferIn.getBuffer(eyes);
        this.getParentModel().renderToBuffer(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
    }
}