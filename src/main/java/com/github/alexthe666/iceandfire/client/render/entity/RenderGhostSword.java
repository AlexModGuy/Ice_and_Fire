package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.entity.EntityGhostSword;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;

public class RenderGhostSword extends EntityRenderer<EntityGhostSword> {

    private final ItemStack SWORD_STACK = new ItemStack(IafItemRegistry.GHOST_SWORD.get());

    public RenderGhostSword(EntityRendererProvider.Context context) {
        super(context);
    }


    @Override
    public ResourceLocation getTextureLocation(EntityGhostSword entity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }

    @Override
    public void render(EntityGhostSword entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        matrixStackIn.pushPose();
        matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(Mth.lerp(partialTicks, entityIn.yRotO, entityIn.getYRot()) - 90.0F));
        matrixStackIn.mulPose(Vector3f.ZP.rotationDegrees(Mth.lerp(partialTicks, entityIn.xRotO, entityIn.getXRot())));
        matrixStackIn.translate(0, 0.5F, 0);
        matrixStackIn.scale(2F, 2F, 2F);
        matrixStackIn.mulPose(new Quaternion(Vector3f.YP, 0F, true));
        matrixStackIn.mulPose(new Quaternion(Vector3f.ZN, (entityIn.tickCount + partialTicks) * 30F, true));
        matrixStackIn.translate(0, -0.15F, 0);
        Minecraft.getInstance().getItemRenderer().renderStatic(SWORD_STACK, ItemTransforms.TransformType.GROUND, 240, OverlayTexture.NO_OVERLAY, matrixStackIn, bufferIn, 0);
        matrixStackIn.popPose();


    }

}
