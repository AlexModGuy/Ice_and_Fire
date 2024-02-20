package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.client.model.ModelGhost;
import com.github.alexthe666.iceandfire.client.render.IafRenderType;
import com.github.alexthe666.iceandfire.entity.EntityGhost;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class RenderGhost extends MobRenderer<EntityGhost, ModelGhost> {

    public static final ResourceLocation TEXTURE_0 = new ResourceLocation("iceandfire:textures/models/ghost/ghost_white.png");
    public static final ResourceLocation TEXTURE_1 = new ResourceLocation("iceandfire:textures/models/ghost/ghost_blue.png");
    public static final ResourceLocation TEXTURE_2 = new ResourceLocation("iceandfire:textures/models/ghost/ghost_green.png");
    public static final ResourceLocation TEXTURE_SHOPPING_LIST = new ResourceLocation("iceandfire:textures/models/ghost/haunted_shopping_list.png");

    public RenderGhost(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ModelGhost(0.0F), 0.55F);

    }

    public static ResourceLocation getGhostOverlayForType(int ghost) {
        switch (ghost) {
            case 1:
                return TEXTURE_1;
            case 2:
                return TEXTURE_2;
            case -1:
                return TEXTURE_SHOPPING_LIST;
            default:
                return TEXTURE_0;
        }
    }

    @Override
    public void render(@NotNull EntityGhost entityIn, float entityYaw, float partialTicks, @NotNull PoseStack matrixStackIn, @NotNull MultiBufferSource bufferIn, int packedLightIn) {
        shadowRadius = 0;
        if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.RenderLivingEvent.Pre<EntityGhost, ModelGhost>(entityIn, this, partialTicks, matrixStackIn, bufferIn, packedLightIn)))
            return;
        matrixStackIn.pushPose();
        this.model.attackTime = this.getAttackAnim(entityIn, partialTicks);

        boolean shouldSit = entityIn.isPassenger() && (entityIn.getVehicle() != null && entityIn.getVehicle().shouldRiderSit());
        this.model.riding = shouldSit;
        this.model.young = entityIn.isBaby();
        float f = Mth.rotLerp(partialTicks, entityIn.yBodyRotO, entityIn.yBodyRot);
        float f1 = Mth.rotLerp(partialTicks, entityIn.yHeadRotO, entityIn.yHeadRot);
        float f2 = f1 - f;
        if (shouldSit && entityIn.getVehicle() instanceof LivingEntity) {
            LivingEntity livingentity = (LivingEntity) entityIn.getVehicle();
            f = Mth.rotLerp(partialTicks, livingentity.yBodyRotO, livingentity.yBodyRot);
            f2 = f1 - f;
            float f3 = Mth.wrapDegrees(f2);
            if (f3 < -85.0F) {
                f3 = -85.0F;
            }

            if (f3 >= 85.0F) {
                f3 = 85.0F;
            }

            f = f1 - f3;
            if (f3 * f3 > 2500.0F) {
                f += f3 * 0.2F;
            }

            f2 = f1 - f;
        }

        float f6 = Mth.lerp(partialTicks, entityIn.xRotO, entityIn.getXRot());
        if (entityIn.getPose() == Pose.SLEEPING) {
            Direction direction = entityIn.getBedOrientation();
            if (direction != null) {
                float f4 = entityIn.getEyeHeight(Pose.STANDING) - 0.1F;
                matrixStackIn.translate((float) (-direction.getStepX()) * f4, 0.0D, (float) (-direction.getStepZ()) * f4);
            }
        }

        float f7 = this.getBob(entityIn, partialTicks);
        this.setupRotations(entityIn, matrixStackIn, f7, f, partialTicks);
        matrixStackIn.scale(-1.0F, -1.0F, 1.0F);
        this.scale(entityIn, matrixStackIn, partialTicks);
        matrixStackIn.translate(0.0D, -1.501F, 0.0D);
        float f8 = 0.0F;
        float f5 = 0.0F;
        if (!shouldSit && entityIn.isAlive()) {
            f8 = entityIn.walkAnimation.speed();
            f5 = entityIn.walkAnimation.position();
            if (entityIn.isBaby()) {
                f5 *= 3.0F;
            }

            if (f8 > 1.0F) {
                f8 = 1.0F;
            }
        }

        this.model.prepareMobModel(entityIn, f5, f8, partialTicks);
        this.model.setupAnim(entityIn, f5, f8, f7, f2, f6);
        float alphaForRender = this.getAlphaForRender(entityIn, partialTicks);
        RenderType rendertype = entityIn.isDaytimeMode() ? IafRenderType.getGhostDaytime(getTextureLocation(entityIn)) : IafRenderType.getGhost(getTextureLocation(entityIn));//this.getRenderType(entityIn, flag, flag1, flag2);
        if (rendertype != null && !entityIn.isInvisible()) {
            VertexConsumer ivertexbuilder = bufferIn.getBuffer(rendertype);
            int i = getOverlayCoords(entityIn, this.getWhiteOverlayProgress(entityIn, partialTicks));
            if (entityIn.isHauntedShoppingList()) {
                matrixStackIn.pushPose();
                matrixStackIn.translate(0, 0.8F + Mth.sin((entityIn.tickCount + partialTicks) * 0.15F) * 0.1F, 0);
                matrixStackIn.scale(0.6F, 0.6F, 0.6F);
                matrixStackIn.mulPose(Axis.YP.rotationDegrees(180.0F));
                {
                    matrixStackIn.pushPose();
                    PoseStack.Pose matrixstack$entry = matrixStackIn.last();
                    Matrix4f matrix4f = matrixstack$entry.pose();
                    Matrix3f matrix3f = matrixstack$entry.normal();
                    this.drawVertex(matrix4f, matrix3f, ivertexbuilder, i, (int) (alphaForRender * 255), -1, -2, 0, 1F, 0.0F, 0, 1, 0, 240);
                    this.drawVertex(matrix4f, matrix3f, ivertexbuilder, i, (int) (alphaForRender * 255), 1, -2, 0, 0.5F, 0.0F, 0, 1, 0, 240);
                    this.drawVertex(matrix4f, matrix3f, ivertexbuilder, i, (int) (alphaForRender * 255), 1, 2, 0, 0.5F, 1, 0, 1, 0, 240);
                    this.drawVertex(matrix4f, matrix3f, ivertexbuilder, i, (int) (alphaForRender * 255), -1, 2, 0, 1F, 1, 0, 1, 0, 240);
                    matrixStackIn.popPose();
                }
                matrixStackIn.mulPose(Axis.YP.rotationDegrees(180.0F));
                {
                    matrixStackIn.pushPose();
                    PoseStack.Pose matrixstack$entry = matrixStackIn.last();
                    Matrix4f matrix4f = matrixstack$entry.pose();
                    Matrix3f matrix3f = matrixstack$entry.normal();
                    this.drawVertex(matrix4f, matrix3f, ivertexbuilder, i, (int) (alphaForRender * 255), -1, -2, 0, 0.0F, 0.0F, 0, 1, 0, 240);
                    this.drawVertex(matrix4f, matrix3f, ivertexbuilder, i, (int) (alphaForRender * 255), 1, -2, 0, 0.5F, 0.0F, 0, 1, 0, 240);
                    this.drawVertex(matrix4f, matrix3f, ivertexbuilder, i, (int) (alphaForRender * 255), 1, 2, 0, 0.5F, 1, 0, 1, 0, 240);
                    this.drawVertex(matrix4f, matrix3f, ivertexbuilder, i, (int) (alphaForRender * 255), -1, 2, 0, 0.0F, 1, 0, 1, 0, 240);
                    matrixStackIn.popPose();
                }
                matrixStackIn.popPose();

            } else {
                this.model.renderToBuffer(matrixStackIn, ivertexbuilder, 240, i, 1.0F, 1.0F, 1.0F, alphaForRender);
            }
        }

        if (!entityIn.isSpectator()) {
            for (RenderLayer<EntityGhost, ModelGhost> layerrenderer : this.layers) {
                layerrenderer.render(matrixStackIn, bufferIn, packedLightIn, entityIn, f5, f8, partialTicks, f7, f2, f6);
            }
        }

        matrixStackIn.popPose();
        net.minecraftforge.client.event.RenderNameTagEvent renderNameplateEvent = new net.minecraftforge.client.event.RenderNameTagEvent(entityIn, entityIn.getDisplayName(), this, matrixStackIn, bufferIn, packedLightIn, partialTicks);
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(renderNameplateEvent);
        if (renderNameplateEvent.getResult() != net.minecraftforge.eventbus.api.Event.Result.DENY && (renderNameplateEvent.getResult() == net.minecraftforge.eventbus.api.Event.Result.ALLOW || this.shouldShowName(entityIn))) {
            this.renderNameTag(entityIn, renderNameplateEvent.getContent(), matrixStackIn, bufferIn, packedLightIn);
        }
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.RenderLivingEvent.Post<EntityGhost, ModelGhost>(entityIn, this, partialTicks, matrixStackIn, bufferIn, packedLightIn));
    }

    @Override
    protected float getFlipDegrees(@NotNull EntityGhost ghost) {
        return 0.0F;
    }

    public float getAlphaForRender(EntityGhost entityIn, float partialTicks) {
        if (entityIn.isDaytimeMode()) {
            return Mth.clamp((101 - Math.min(entityIn.getDaytimeCounter(), 100)) / 100F, 0, 1);
        }
        return Mth.clamp((Mth.sin((entityIn.tickCount + partialTicks) * 0.1F) + 1F) * 0.5F + 0.1F, 0F, 1F);
    }

    @Override
    public void scale(@NotNull EntityGhost LivingEntityIn, @NotNull PoseStack stack, float partialTickTime) {
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(EntityGhost ghost) {
        switch (ghost.getColor()) {
            case 1:
                return TEXTURE_1;
            case 2:
                return TEXTURE_2;
            case -1:
                return TEXTURE_SHOPPING_LIST;
            default:
                return TEXTURE_0;
        }
    }

    public void drawVertex(Matrix4f stack, Matrix3f normal, VertexConsumer builder, int packedRed, int alphaInt, int x, int y, int z, float u, float v, int lightmap, int lightmap3, int lightmap2, int lightmap4) {
        builder.vertex(stack, (float) x, (float) y, (float) z).color(255, 255, 255, alphaInt).uv(u, v).overlayCoords(packedRed).uv2(lightmap4).normal(normal, (float) lightmap, (float) lightmap2, (float) lightmap3).endVertex();
    }
}
