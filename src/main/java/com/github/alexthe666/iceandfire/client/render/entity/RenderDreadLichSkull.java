package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.client.model.ModelDreadLichSkull;
import com.github.alexthe666.iceandfire.entity.EntityDreadLichSkull;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class RenderDreadLichSkull extends EntityRenderer<EntityDreadLichSkull> {

    public static final ResourceLocation TEXTURE = new ResourceLocation("iceandfire:textures/models/dread/dread_lich_skull.png");
    private static final ModelDreadLichSkull MODEL_SPIRIT = new ModelDreadLichSkull();

    public RenderDreadLichSkull(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(EntityDreadLichSkull entity, float entityYaw, float partialTicks, @NotNull PoseStack matrixStackIn, @NotNull MultiBufferSource bufferIn, int packedLightIn) {
        float f = 0.0625F;
        if (entity.tickCount > 3) {
            matrixStackIn.pushPose();
            matrixStackIn.scale(1.5F, -1.5F, 1.5F);
            float yaw = entity.yRotO + (entity.getYRot() - entity.yRotO) * partialTicks;
            matrixStackIn.translate(0F, 0F, 0F);
            matrixStackIn.mulPose(Axis.YP.rotationDegrees(yaw - 180.0F));
            VertexConsumer ivertexbuilder = ItemRenderer.getFoilBuffer(bufferIn, RenderType.eyes(TEXTURE), false, false);
            MODEL_SPIRIT.renderToBuffer(matrixStackIn, ivertexbuilder, 240, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            matrixStackIn.popPose();
        }

        super.render(entity, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    private float interpolateValue(float start, float end, float pct) {
        return start + (end - start) * pct;
    }

    @Nullable
    @Override
    public ResourceLocation getTextureLocation(@NotNull EntityDreadLichSkull entity) {
        return TEXTURE;
    }
}
