package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.client.model.ModelDreadLichSkull;
import com.github.alexthe666.iceandfire.entity.EntityDreadLichSkull;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;

import javax.annotation.Nullable;

public class RenderDreadLichSkull extends EntityRenderer<EntityDreadLichSkull> {

    public static final ResourceLocation TEXTURE = new ResourceLocation("iceandfire:textures/models/dread/dread_lich_skull.png");
    private static final ModelDreadLichSkull MODEL_SPIRIT = new ModelDreadLichSkull();

    public RenderDreadLichSkull() {
        super(Minecraft.getInstance().getEntityRenderDispatcher());
    }

    public void render(EntityDreadLichSkull entity, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        float f = 0.0625F;
        if (entity.tickCount > 3) {
            matrixStackIn.pushPose();
            matrixStackIn.scale(1.5F, -1.5F, 1.5F);
            float yaw = entity.yRotO + (entity.yRot - entity.yRotO) * partialTicks;
            matrixStackIn.translate(0F, 0F, 0F);
            matrixStackIn.mulPose(new Quaternion(Vector3f.YP, yaw - 180, true));
            IVertexBuilder ivertexbuilder = ItemRenderer.getFoilBuffer(bufferIn, RenderType.eyes(TEXTURE), false, false);
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
    public ResourceLocation getTextureLocation(EntityDreadLichSkull entity) {
        return TEXTURE;
    }
}
