package com.github.alexthe666.iceandfire.client.render.tile;

import com.github.alexthe666.iceandfire.entity.tile.TileEntityDreadSpawner;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BaseSpawner;
import org.jetbrains.annotations.NotNull;
import org.joml.AxisAngle4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class RenderDreadSpawner<T extends TileEntityDreadSpawner> implements BlockEntityRenderer<T> {

    public RenderDreadSpawner(BlockEntityRendererProvider.Context context) {

    }

    @Override
    public void render(TileEntityDreadSpawner tileEntityIn, float partialTicks, PoseStack matrixStackIn, @NotNull MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        matrixStackIn.pushPose();
        matrixStackIn.translate(0.5D, 0.0D, 0.5D);
        BaseSpawner abstractspawner = tileEntityIn.getSpawner();
        Entity entity = abstractspawner.getOrCreateDisplayEntity(tileEntityIn.getLevel(), RandomSource.create(), tileEntityIn.getBlockPos());
        if (entity != null) {
            float f = 0.53125F;
            float f1 = Math.max(entity.getBbWidth(), entity.getBbHeight());
            if ((double) f1 > 1.0D) {
                f /= f1;
            }

            matrixStackIn.translate(0.0D, 0.4F, 0.0D);
            matrixStackIn.mulPose(new Quaternionf(new AxisAngle4f((float) Math.PI/180F*((float) Mth.lerp(partialTicks, abstractspawner.getoSpin(), abstractspawner.getSpin()) * 10.0F), new Vector3f(0.0F, 1.0F, 0.0F))));
            matrixStackIn.translate(0.0D, -0.2F, 0.0D);
            matrixStackIn.mulPose(new Quaternionf(new AxisAngle4f((float) -Math.PI/180F*30.0F, new Vector3f(1.0F, 0.0F, 0.0F))));
            matrixStackIn.scale(f, f, f);
            Minecraft.getInstance().getEntityRenderDispatcher().render(entity, 0.0D, 0.0D, 0.0D, 0.0F, partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        }

        matrixStackIn.popPose();
    }
}