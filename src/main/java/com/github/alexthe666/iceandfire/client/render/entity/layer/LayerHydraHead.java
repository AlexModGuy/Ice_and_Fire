package com.github.alexthe666.iceandfire.client.render.entity.layer;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.iceandfire.client.model.ModelHydraBody;
import com.github.alexthe666.iceandfire.client.model.ModelHydraHead;
import com.github.alexthe666.iceandfire.client.render.entity.RenderHydra;
import com.github.alexthe666.iceandfire.entity.EntityHydra;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class LayerHydraHead extends RenderLayer<EntityHydra, ModelHydraBody> {
    public static final ResourceLocation TEXTURE_STONE = new ResourceLocation("iceandfire:textures/models/hydra/stone.png");
    private static final float[][] TRANSLATE = new float[][]{
        {0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F},// 1 total heads
        {-0.15F, 0.15F, 0F, 0F, 0F, 0F, 0F, 0F, 0F},// 2 total heads
        {-0.3F, 0F, 0.3F, 0F, 0F, 0F, 0F, 0F, 0F},// 3 total heads
        {-0.4F, -0.1F, 0.1F, 0.4F, 0F, 0F, 0F, 0F, 0F},//etc...
        {-0.5F, -0.2F, 0F, 0.2F, 0.5F, 0F, 0F, 0F, 0F},
        {-0.7F, -0.4F, -0.2F, 0.2F, 0.4F, 0.7F, 0F, 0F, 0F},
        {-0.7F, -0.4F, -0.2F, 0, 0.2F, 0.4F, 0.7F, 0F, 0F},
        {-0.6F, -0.4F, -0.2F, -0.1F, 0.1F, 0.2F, 0.4F, 0.6F, 0F},
        {-0.6F, -0.4F, -0.2F, -0.1F, 0.0F, 0.1F, 0.2F, 0.4F, 0.6F},
    };
    private static final float[][] ROTATE = new float[][]{
            {0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F},// 1 total heads
            {10F, -10F, 0F, 0F, 0F, 0F, 0F, 0F, 0F},// 2 total heads
            {10F, 0F, -10F, 0F, 0F, 0F, 0F, 0F, 0F},// 3 total heads
            {25F, 10F, -10F, -25F, 0F, 0F, 0F, 0F, 0F},//etc...
            {30F, 15F, 0F, -15F, -30F, 0F, 0F, 0F, 0F},
            {40F, 25F, 5F, -5F, -25F, -40F, 0F, 0F, 0F},
            {40F, 30F, 15F, 0F, -15F, -30F, -40F, 0F, 0F},
            {45F, 30F, 20F, 5F, -5F, -20F, -30F, -45F, 0F},
            {50F, 37F, 25F, 15F, 0, -15F, -25F, -37F, -50F},
    };
    private final RenderHydra renderer;
    private static final ModelHydraHead[] modelArr = new ModelHydraHead[EntityHydra.HEADS];

    static{
        for (int i = 0; i < modelArr.length; i++) {
            modelArr[i] = new ModelHydraHead(i);
        }
    }

    public LayerHydraHead(RenderHydra renderer) {
        super(renderer);
        this.renderer = renderer;

    }

    @Override
    public void render(@NotNull PoseStack matrixStackIn, @NotNull MultiBufferSource bufferIn, int packedLightIn, EntityHydra entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entity.isInvisible()) {
            return;
        }
        renderHydraHeads(renderer.getModel(), false, matrixStackIn, bufferIn, packedLightIn, entity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
    }

    public static void renderHydraHeads(ModelHydraBody model, boolean stone, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, EntityHydra hydra, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        matrixStackIn.pushPose();
        int heads = hydra.getHeadCount();
        translateToBody(model, matrixStackIn);
        RenderType type = RenderType.entityCutout(stone ? TEXTURE_STONE : getHeadTexture(hydra));
        for (int head = 1; head <= heads; head++) {
            matrixStackIn.pushPose();
            float bodyWidth = 0.5F;
            matrixStackIn.translate(TRANSLATE[heads - 1][head - 1] * bodyWidth, 0, 0);
            matrixStackIn.mulPose(Axis.YP.rotationDegrees(ROTATE[heads - 1][head - 1]));
            modelArr[head - 1].setupAnim(hydra, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
            modelArr[head - 1].renderToBuffer(matrixStackIn, bufferIn.getBuffer(type), packedLightIn, LivingEntityRenderer.getOverlayCoords(hydra, 0.0F), 1.0F, 1.0F, 1.0F, 1.0F);
            matrixStackIn.popPose();
        }
        matrixStackIn.popPose();
    }


    public static ResourceLocation getHeadTexture(EntityHydra gorgon) {
        switch (gorgon.getVariant()) {
            default:
                return RenderHydra.TEXUTURE_0;
            case 1:
                return RenderHydra.TEXUTURE_1;
            case 2:
                return RenderHydra.TEXUTURE_2;
        }
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(EntityHydra gorgon) {
        switch (gorgon.getVariant()) {
            default:
                return RenderHydra.TEXUTURE_0;
            case 1:
                return RenderHydra.TEXUTURE_1;
            case 2:
                return RenderHydra.TEXUTURE_2;
        }
    }

    protected static void translateToBody(ModelHydraBody model, PoseStack stack) {
        postRender(model.BodyUpper, stack, 0.0625F);
    }

    protected static void postRender(AdvancedModelBox renderer, PoseStack matrixStackIn, float scale) {
        if (renderer.rotateAngleX == 0.0F && renderer.rotateAngleY == 0.0F && renderer.rotateAngleZ == 0.0F) {
            if (renderer.rotationPointX != 0.0F || renderer.rotationPointY != 0.0F || renderer.rotateAngleZ != 0.0F) {
                matrixStackIn.translate(renderer.rotationPointX * scale, renderer.rotationPointY * scale, renderer.rotateAngleZ * scale);
            }
        } else {
            matrixStackIn.translate(renderer.rotationPointX * scale, renderer.rotationPointY * scale, renderer.rotateAngleZ * scale);
            if (renderer.rotateAngleZ != 0.0F) {
                matrixStackIn.mulPose(Axis.ZP.rotation(renderer.rotateAngleZ));
            }

            if (renderer.rotateAngleY != 0.0F) {
                matrixStackIn.mulPose(Axis.YP.rotation(renderer.rotateAngleY));
            }

            if (renderer.rotateAngleX != 0.0F) {
                matrixStackIn.mulPose(Axis.XP.rotation(renderer.rotateAngleX));
            }
        }
    }
}