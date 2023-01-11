package com.github.alexthe666.iceandfire.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class RenderNothing<T extends Entity> extends EntityRenderer<T> {

    public RenderNothing(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(@NotNull T entityIn, float entityYaw, float partialTicks, @NotNull PoseStack matrixStackIn, @NotNull MultiBufferSource bufferIn, int packedLightIn) {
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    // Only render if the debug bboxes are enabled
    @Override
    public boolean shouldRender(@NotNull T livingEntityIn, @NotNull Frustum camera, double camX, double camY, double camZ) {
        if (!this.entityRenderDispatcher.shouldRenderHitBoxes())
            return false;
        return super.shouldRender(livingEntityIn, camera, camX, camY, camZ);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull Entity entity) {
        return null;
    }
}
