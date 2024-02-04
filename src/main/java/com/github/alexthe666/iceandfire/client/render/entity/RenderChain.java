package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import java.util.List;

public class RenderChain {

    private static final ResourceLocation TEXTURE = new ResourceLocation("iceandfire:textures/models/misc/chain_link.png");

    public static void render(LivingEntity entityLivingIn, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int lightIn, List<Entity> chainedTo) {
        for (Entity chainTarget : chainedTo) {
            if (chainTarget == null) {
                IceAndFire.LOGGER.warn("Found null value in list of target entities");
                continue;
            }
            try {
                renderLink(entityLivingIn, partialTicks, matrixStackIn, bufferIn, lightIn, chainTarget);
            } catch (Exception e) {
                IceAndFire.LOGGER.warn("Could not render chain link for {} connected to {}", entityLivingIn.toString(), chainTarget.toString());
            }
        }
    }

    public static <E extends Entity> void renderLink(LivingEntity entityLivingIn, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int lightIn, E chainTarget) {
        // Most of this code stems from the guardian lasers
        float f3 = entityLivingIn.getBbHeight() * 0.4f;
        matrixStackIn.pushPose();
        matrixStackIn.translate(0.0D, f3, 0.0D);
        Vec3 vector3d = getPosition(chainTarget, (double) chainTarget.getBbHeight() * 0.5D, partialTicks);
        Vec3 vector3d1 = getPosition(entityLivingIn, f3, partialTicks);
        Vec3 vector3d2 = vector3d.subtract(vector3d1);
        float f4 = (float) (vector3d2.length() + 0.0D);
        vector3d2 = vector3d2.normalize();
        float f5 = (float) Math.acos(vector3d2.y);
        float f6 = (float) Math.atan2(vector3d2.z, vector3d2.x);
        matrixStackIn.mulPose(Axis.YP.rotation((float) Math.PI / 2.0F - f6));
        matrixStackIn.mulPose(Axis.XP.rotation(f5));
        float f7 = -1.0F;
        int j = 255;
        int k = 255;
        int l = 255;
        float f19 = 0;
        float f20 = 0.2F;
        float f21 = 0F;
        float f22 = -0.2F;
        float f23 = Mth.cos(f7 + ((float) Math.PI / 2F)) * 0.2F;
        float f24 = Mth.sin(f7 + ((float) Math.PI / 2F)) * 0.2F;
        float f25 = Mth.cos(f7 + ((float) Math.PI * 1.5F)) * 0.2F;
        float f26 = Mth.sin(f7 + ((float) Math.PI * 1.5F)) * 0.2F;
        float f29 = 0;
        float f30 = f4 + f29;
        float f32 = 0.75F;
        float f31 = f4 + f32;

        VertexConsumer ivertexbuilder = bufferIn.getBuffer(RenderType.entityCutoutNoCull(getTexture()));
        PoseStack.Pose matrixstack$entry = matrixStackIn.last();
        Matrix4f matrix4f = matrixstack$entry.pose();
        Matrix3f matrix3f = matrixstack$entry.normal();
        matrixStackIn.pushPose();
        vertex(ivertexbuilder, matrix4f, matrix3f, f19, f4, f20, j, k, l, 0.4999F, f30, lightIn);
        vertex(ivertexbuilder, matrix4f, matrix3f, f19, 0.0F, f20, j, k, l, 0.4999F, f29, lightIn);
        vertex(ivertexbuilder, matrix4f, matrix3f, f21, 0.0F, f22, j, k, l, 0.0F, f29, lightIn);
        vertex(ivertexbuilder, matrix4f, matrix3f, f21, f4, f22, j, k, l, 0.0F, f30, lightIn);

        vertex(ivertexbuilder, matrix4f, matrix3f, f23, f4, f24, j, k, l, 0.4999F, f31, lightIn);
        vertex(ivertexbuilder, matrix4f, matrix3f, f23, 0.0F, f24, j, k, l, 0.4999F, f32, lightIn);
        vertex(ivertexbuilder, matrix4f, matrix3f, f25, 0.0F, f26, j, k, l, 0.0F, f32, lightIn);
        vertex(ivertexbuilder, matrix4f, matrix3f, f25, f4, f26, j, k, l, 0.0F, f31, lightIn);
        matrixStackIn.popPose();
        matrixStackIn.popPose();
    }

    private static void vertex(VertexConsumer p_229108_0_, Matrix4f p_229108_1_, Matrix3f p_229108_2_, float p_229108_3_, float p_229108_4_, float p_229108_5_, int p_229108_6_, int p_229108_7_, int p_229108_8_, float p_229108_9_, float p_229108_10_, int packedLight) {
        p_229108_0_.vertex(p_229108_1_, p_229108_3_, p_229108_4_, p_229108_5_).color(p_229108_6_, p_229108_7_, p_229108_8_, 255).uv(p_229108_9_, p_229108_10_).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(p_229108_2_, 0.0F, 1.0F, 0.0F).endVertex();
    }

    private static Vec3 getPosition(Entity LivingEntityIn, double p_177110_2_, float p_177110_4_) {
        double d0 = LivingEntityIn.xOld + (LivingEntityIn.getX() - LivingEntityIn.xOld) * (double) p_177110_4_;
        double d1 = p_177110_2_ + LivingEntityIn.yOld + (LivingEntityIn.getY() - LivingEntityIn.yOld) * (double) p_177110_4_;
        double d2 = LivingEntityIn.zOld + (LivingEntityIn.getZ() - LivingEntityIn.zOld) * (double) p_177110_4_;
        return new Vec3(d0, d1, d2);
    }

    public static ResourceLocation getTexture() {
        return TEXTURE;
    }
}
