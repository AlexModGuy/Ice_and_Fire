package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.client.model.ModelGhost;
import com.github.alexthe666.iceandfire.client.render.IafRenderType;
import com.github.alexthe666.iceandfire.entity.EntityGhost;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Pose;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;

public class RenderGhost extends MobRenderer<EntityGhost, ModelGhost> {

    public static final ResourceLocation TEXTURE_0 = new ResourceLocation("iceandfire:textures/models/ghost/ghost_white.png");
    public static final ResourceLocation TEXTURE_1 = new ResourceLocation("iceandfire:textures/models/ghost/ghost_blue.png");
    public static final ResourceLocation TEXTURE_2 = new ResourceLocation("iceandfire:textures/models/ghost/ghost_green.png");
    public static final ResourceLocation TEXTURE_SHOPPING_LIST = new ResourceLocation("iceandfire:textures/models/ghost/haunted_shopping_list.png");

    public RenderGhost(EntityRendererManager renderManager) {
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
    public void render(EntityGhost entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        shadowRadius = 0;
        if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.RenderLivingEvent.Pre<EntityGhost, ModelGhost>(entityIn, this, partialTicks, matrixStackIn, bufferIn, packedLightIn)))
            return;
        matrixStackIn.pushPose();
        this.model.attackTime = this.getAttackAnim(entityIn, partialTicks);

        boolean shouldSit = entityIn.isPassenger() && (entityIn.getVehicle() != null && entityIn.getVehicle().shouldRiderSit());
        this.model.riding = shouldSit;
        this.model.young = entityIn.isBaby();
        float f = MathHelper.rotLerp(partialTicks, entityIn.yBodyRotO, entityIn.yBodyRot);
        float f1 = MathHelper.rotLerp(partialTicks, entityIn.yHeadRotO, entityIn.yHeadRot);
        float f2 = f1 - f;
        if (shouldSit && entityIn.getVehicle() instanceof LivingEntity) {
            LivingEntity livingentity = (LivingEntity) entityIn.getVehicle();
            f = MathHelper.rotLerp(partialTicks, livingentity.yBodyRotO, livingentity.yBodyRot);
            f2 = f1 - f;
            float f3 = MathHelper.wrapDegrees(f2);
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

        float f6 = MathHelper.lerp(partialTicks, entityIn.xRotO, entityIn.xRot);
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
            f8 = MathHelper.lerp(partialTicks, entityIn.animationSpeedOld, entityIn.animationSpeed);
            f5 = entityIn.animationPosition - entityIn.animationSpeed * (1.0F - partialTicks);
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
            IVertexBuilder ivertexbuilder = bufferIn.getBuffer(rendertype);
            int i = getOverlayCoords(entityIn, this.getWhiteOverlayProgress(entityIn, partialTicks));
            if (entityIn.isHauntedShoppingList()) {
                matrixStackIn.pushPose();
                matrixStackIn.translate(0, 0.8F + MathHelper.sin((entityIn.tickCount + partialTicks) * 0.15F) * 0.1F, 0);
                matrixStackIn.scale(0.6F, 0.6F, 0.6F);
                matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(180.0F));
                {
                    matrixStackIn.pushPose();
                    MatrixStack.Entry matrixstack$entry = matrixStackIn.last();
                    Matrix4f matrix4f = matrixstack$entry.pose();
                    Matrix3f matrix3f = matrixstack$entry.normal();
                    this.drawVertex(matrix4f, matrix3f, ivertexbuilder, i, (int) (alphaForRender * 255), -1, -2, 0, 1F, 0.0F, 0, 1, 0, 240);
                    this.drawVertex(matrix4f, matrix3f, ivertexbuilder, i, (int) (alphaForRender * 255), 1, -2, 0, 0.5F, 0.0F, 0, 1, 0, 240);
                    this.drawVertex(matrix4f, matrix3f, ivertexbuilder, i, (int) (alphaForRender * 255), 1, 2, 0, 0.5F, 1, 0, 1, 0, 240);
                    this.drawVertex(matrix4f, matrix3f, ivertexbuilder, i, (int) (alphaForRender * 255), -1, 2, 0, 1F, 1, 0, 1, 0, 240);
                    matrixStackIn.popPose();
                }
                matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(180.0F));
                {
                    matrixStackIn.pushPose();
                    MatrixStack.Entry matrixstack$entry = matrixStackIn.last();
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
            for (LayerRenderer<EntityGhost, ModelGhost> layerrenderer : this.layers) {
                layerrenderer.render(matrixStackIn, bufferIn, packedLightIn, entityIn, f5, f8, partialTicks, f7, f2, f6);
            }
        }

        matrixStackIn.popPose();
        net.minecraftforge.client.event.RenderNameplateEvent renderNameplateEvent = new net.minecraftforge.client.event.RenderNameplateEvent(entityIn, entityIn.getDisplayName(), this, matrixStackIn, bufferIn, packedLightIn, partialTicks);
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(renderNameplateEvent);
        if (renderNameplateEvent.getResult() != net.minecraftforge.eventbus.api.Event.Result.DENY && (renderNameplateEvent.getResult() == net.minecraftforge.eventbus.api.Event.Result.ALLOW || this.shouldShowName(entityIn))) {
            this.renderNameTag(entityIn, renderNameplateEvent.getContent(), matrixStackIn, bufferIn, packedLightIn);
        }
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.RenderLivingEvent.Post<EntityGhost, ModelGhost>(entityIn, this, partialTicks, matrixStackIn, bufferIn, packedLightIn));
    }

    @Override
    protected float getFlipDegrees(EntityGhost ghost) {
        return 0.0F;
    }

    public float getAlphaForRender(EntityGhost entityIn, float partialTicks) {
        if (entityIn.isDaytimeMode()) {
            return MathHelper.clamp((101 - Math.min(entityIn.getDaytimeCounter(), 100)) / 100F, 0, 1);
        }
        return MathHelper.clamp((MathHelper.sin((entityIn.tickCount + partialTicks) * 0.1F) + 1F) * 0.5F + 0.1F, 0F, 1F);
    }

    @Override
    public void scale(EntityGhost LivingEntityIn, MatrixStack stack, float partialTickTime) {
    }

    @Override
    public ResourceLocation getTextureLocation(EntityGhost ghost) {
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

    public void drawVertex(Matrix4f stack, Matrix3f normal, IVertexBuilder builder, int packedRed, int alphaInt, int x, int y, int z, float u, float v, int lightmap, int lightmap3, int lightmap2, int lightmap4) {
        builder.vertex(stack, (float) x, (float) y, (float) z).color(255, 255, 255, alphaInt).uv(u, v).overlayCoords(packedRed).uv2(lightmap4).normal(normal, (float) lightmap, (float) lightmap2, (float) lightmap3).endVertex();
    }
}
