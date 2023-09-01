package com.github.alexthe666.iceandfire.client.render.entity.layer;

import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.stream.StreamSupport;

public class LayerDragonBanner extends RenderLayer<EntityDragonBase, AdvancedEntityModel<EntityDragonBase>> {
    private final RenderLayerParent<EntityDragonBase, AdvancedEntityModel<EntityDragonBase>> renderer;

    public LayerDragonBanner(RenderLayerParent<EntityDragonBase, AdvancedEntityModel<EntityDragonBase>> renderIn) {
        super(renderIn);
        this.renderer = renderIn;
    }

    @Override
    public void render(PoseStack matrixStackIn, @NotNull MultiBufferSource bufferIn, int packedLightIn, EntityDragonBase entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        ItemStack itemstack = entity.getItemInHand(InteractionHand.OFF_HAND);
        matrixStackIn.pushPose();
        if (!itemstack.isEmpty() && itemstack.getItem() instanceof BannerItem) {
            float f = (entity.getRenderSize() / 3F);
            float f2 = 1F / f;
            matrixStackIn.pushPose();
            postRender(StreamSupport.stream(this.renderer.getModel().getAllParts().spliterator(), false).filter(cube -> cube.boxName.equals("BodyUpper")).findFirst().get(), matrixStackIn, 0.0625F);
            matrixStackIn.translate(0, -0.2F, 0.4F);
            matrixStackIn.mulPose(Axis.XP.rotationDegrees(180.0F));
            matrixStackIn.pushPose();
            matrixStackIn.scale(f2, f2, f2);
            Minecraft.getInstance().getItemRenderer().renderStatic(itemstack, ItemDisplayContext.NONE, packedLightIn, OverlayTexture.NO_OVERLAY, matrixStackIn, bufferIn, Minecraft.getInstance().level, 0);
            matrixStackIn.popPose();
            matrixStackIn.popPose();
        }
        matrixStackIn.popPose();
    }

    protected void postRender(AdvancedModelBox renderer, PoseStack matrixStackIn, float scale) {
        if (renderer.rotateAngleX == 0.0F && renderer.rotateAngleY == 0.0F && renderer.rotateAngleZ == 0.0F) {
            if (renderer.rotationPointX != 0.0F || renderer.rotationPointY != 0.0F || renderer.offsetZ != 0.0F) {
                matrixStackIn.translate(renderer.rotationPointX * scale, renderer.rotationPointY * scale, renderer.rotationPointZ * scale);
            }
        } else {
            matrixStackIn.translate(renderer.rotationPointX * scale, renderer.rotationPointY * scale, renderer.rotationPointZ * scale);
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