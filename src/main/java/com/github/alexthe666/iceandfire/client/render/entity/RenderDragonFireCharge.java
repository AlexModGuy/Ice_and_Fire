package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.projectile.Fireball;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;

public class RenderDragonFireCharge extends EntityRenderer<Fireball> {

    public boolean isFire;

    public RenderDragonFireCharge(EntityRendererProvider.Context context, boolean isFire) {
        super(context);
        this.isFire = isFire;
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull Fireball entity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }

    @Override
    public void render(@NotNull Fireball entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, @NotNull MultiBufferSource bufferIn, int packedLightIn) {
        BlockRenderDispatcher blockrendererdispatcher = Minecraft.getInstance().getBlockRenderer();
        matrixStackIn.pushPose();
        matrixStackIn.translate(0.0D, 0.5D, 0.0D);
        matrixStackIn.mulPose(Axis.YP.rotationDegrees(-90.0F));
        matrixStackIn.translate(-0.5D, -0.5D, 0.5D);
        matrixStackIn.mulPose(Axis.YP.rotationDegrees(90.0F));
        Minecraft.getInstance().getBlockRenderer().renderSingleBlock(isFire ? Blocks.MAGMA_BLOCK.defaultBlockState() : IafBlockRegistry.DRAGON_ICE.get().defaultBlockState(), matrixStackIn, bufferIn, packedLightIn, OverlayTexture.NO_OVERLAY);
        matrixStackIn.popPose();
    }

}
