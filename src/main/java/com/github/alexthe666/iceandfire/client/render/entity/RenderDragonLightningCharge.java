package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.client.model.ModelDreadLichSkull;
import com.github.alexthe666.iceandfire.entity.EntityDragonLightningCharge;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;

import javax.annotation.Nullable;

public class RenderDragonLightningCharge extends EntityRenderer<EntityDragonLightningCharge> {

    public static final ResourceLocation TEXTURE = new ResourceLocation("iceandfire:textures/models/lightningdragon/charge.png");
    public static final ResourceLocation TEXTURE_CORE = new ResourceLocation("iceandfire:textures/models/lightningdragon/charge_core.png");
    private static final ModelDreadLichSkull MODEL_SPIRIT = new ModelDreadLichSkull();

    public RenderDragonLightningCharge() {
        super(Minecraft.getInstance().getEntityRenderDispatcher());
    }

    public void render(EntityDragonLightningCharge entity, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        float f = (float) entity.tickCount + partialTicks;
        float yaw = entity.yRotO + (entity.yRot - entity.yRotO) * partialTicks;
        IVertexBuilder ivertexbuilder2 = bufferIn.getBuffer(RenderType.eyes(TEXTURE_CORE));
        IVertexBuilder ivertexbuilder = bufferIn.getBuffer(RenderType.energySwirl(TEXTURE, f * 0.01F, f * 0.01F));

        matrixStackIn.pushPose();
        matrixStackIn.translate(0F, 0.5F, 0F);
        matrixStackIn.translate(0F, -0.25F, 0F);
        matrixStackIn.mulPose(new Quaternion(Vector3f.YP, yaw - 180, true));
        matrixStackIn.mulPose(new Quaternion(Vector3f.XP, f * 20, true));
        matrixStackIn.translate(0F, 0.25F, 0F);
        MODEL_SPIRIT.renderToBuffer(matrixStackIn, ivertexbuilder2, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixStackIn.popPose();

        matrixStackIn.pushPose();
        matrixStackIn.translate(0F, 0.5F, 0F);
        matrixStackIn.translate(0F, -0.25F, 0F);
        matrixStackIn.mulPose(new Quaternion(Vector3f.YP, yaw - 180, true));
        matrixStackIn.mulPose(new Quaternion(Vector3f.XP, f * 15, true));
        matrixStackIn.translate(0F, 0.25F, 0F);
        matrixStackIn.scale(1.5F, 1.5F, 1.5F);
        MODEL_SPIRIT.renderToBuffer(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixStackIn.popPose();

        matrixStackIn.pushPose();
        matrixStackIn.translate(0F, 0.75F, 0F);
        matrixStackIn.translate(0F, -0.25F, 0F);
        matrixStackIn.mulPose(new Quaternion(Vector3f.YP, yaw - 180, true));
        matrixStackIn.mulPose(new Quaternion(Vector3f.XP, f * 10, true));
        matrixStackIn.translate(0F, 0.75F, 0F);
        matrixStackIn.scale(2.5F, 2.5F, 2.5F);
        MODEL_SPIRIT.renderToBuffer(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixStackIn.popPose();

        super.render(entity, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    private float interpolateValue(float start, float end, float pct) {
        return start + (end - start) * pct;
    }

    @Nullable
    @Override
    public ResourceLocation getTextureLocation(EntityDragonLightningCharge entity) {
        return TEXTURE;
    }
}
