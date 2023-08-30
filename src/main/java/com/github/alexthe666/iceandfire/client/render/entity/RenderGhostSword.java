package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.entity.EntityGhostSword;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.joml.AxisAngle4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class RenderGhostSword extends EntityRenderer<EntityGhostSword> {

    public RenderGhostSword(EntityRendererProvider.Context context) {
        super(context);
    }


    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull EntityGhostSword entity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }

    @Override
    public void render(EntityGhostSword entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, @NotNull MultiBufferSource bufferIn, int packedLightIn) {
        matrixStackIn.pushPose();
        matrixStackIn.mulPose(new Quaternionf(new AxisAngle4f((float) Math.PI/180F*Mth.lerp(partialTicks, entityIn.yRotO, entityIn.getYRot())-90.0F, new Vector3f(0.0F, 1.0F, 0.0F))));
        matrixStackIn.mulPose(new Quaternionf(new AxisAngle4f((float) Math.PI/180F*Mth.lerp(partialTicks, entityIn.xRotO, entityIn.getXRot()), new Vector3f(0.0F, 0.0F, 1.0F))));
        matrixStackIn.translate(0, 0.5F, 0);
        matrixStackIn.scale(2F, 2F, 2F);
        matrixStackIn.mulPose(new Quaternionf(new AxisAngle4f(0F, new Vector3f(0.0F, 1.0F, 0.0F))));
        matrixStackIn.mulPose(new Quaternionf(new AxisAngle4f((float) Math.PI/180F*(entityIn.tickCount + partialTicks)*30F, new Vector3f(0.0F, 0.0F, -1.0F))));
        matrixStackIn.translate(0, -0.15F, 0);
        Minecraft.getInstance().getItemRenderer().renderStatic(new ItemStack(IafItemRegistry.GHOST_SWORD.get()), ItemDisplayContext.GROUND, 240, OverlayTexture.NO_OVERLAY, matrixStackIn, bufferIn, Minecraft.getInstance().level, 0);
        matrixStackIn.popPose();
    }
}
