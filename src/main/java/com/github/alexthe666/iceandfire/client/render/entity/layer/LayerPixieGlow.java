package com.github.alexthe666.iceandfire.client.render.entity.layer;

import com.github.alexthe666.iceandfire.client.model.ModelPixie;
import com.github.alexthe666.iceandfire.client.render.entity.RenderPixie;
import com.github.alexthe666.iceandfire.entity.EntityPixie;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class LayerPixieGlow extends RenderLayer<EntityPixie, ModelPixie> {

    private final RenderPixie render;

    public LayerPixieGlow(RenderPixie renderIn) {
        super(renderIn);
        this.render = renderIn;
    }

    @Override
    public void render(@NotNull PoseStack matrixStackIn, @NotNull MultiBufferSource bufferIn, int packedLightIn, EntityPixie pixie, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        ResourceLocation texture = RenderPixie.TEXTURE_0;
        switch (pixie.getColor()) {
            default:
                texture = RenderPixie.TEXTURE_0;
                break;
            case 1:
                texture = RenderPixie.TEXTURE_1;
                break;
            case 2:
                texture = RenderPixie.TEXTURE_2;
                break;
            case 3:
                texture = RenderPixie.TEXTURE_3;
                break;
            case 4:
                texture = RenderPixie.TEXTURE_4;
                break;
            case 5:
                texture = RenderPixie.TEXTURE_5;
                break;
        }
        RenderType eyes = RenderType.eyes(texture);
        VertexConsumer ivertexbuilder = bufferIn.getBuffer(eyes);
        this.getParentModel().renderToBuffer(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
    }
}