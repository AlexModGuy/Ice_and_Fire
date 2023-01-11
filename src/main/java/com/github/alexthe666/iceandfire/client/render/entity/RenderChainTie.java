package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.client.model.ModelChainTie;
import com.github.alexthe666.iceandfire.entity.EntityChainTie;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;


public class RenderChainTie extends EntityRenderer<EntityChainTie> {
    private static final ResourceLocation TEXTURE = new ResourceLocation("iceandfire:textures/models/misc/chain_tie.png");
    private final ModelChainTie leashKnotModel = new ModelChainTie();

    public RenderChainTie(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(@NotNull EntityChainTie entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        matrixStackIn.pushPose();
        matrixStackIn.translate(0, 0.5F, 0);
        matrixStackIn.scale(-1.0F, -1.0F, 1.0F);
        this.leashKnotModel.setupAnim(entityIn, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        VertexConsumer ivertexbuilder = bufferIn.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));
        this.leashKnotModel.renderToBuffer(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixStackIn.popPose();
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    /**
     * Returns the location of an entity's texture.
     */
    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull EntityChainTie entity) {
        return TEXTURE;
    }
}