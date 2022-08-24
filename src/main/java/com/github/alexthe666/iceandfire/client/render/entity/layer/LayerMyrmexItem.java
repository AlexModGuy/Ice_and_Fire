package com.github.alexthe666.iceandfire.client.render.entity.layer;

import com.github.alexthe666.iceandfire.client.model.ModelMyrmexBase;
import com.github.alexthe666.iceandfire.client.render.entity.RenderMyrmexBase;
import com.github.alexthe666.iceandfire.entity.EntityMyrmexBase;
import com.github.alexthe666.iceandfire.entity.EntityMyrmexWorker;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;

public class LayerMyrmexItem extends LayerRenderer<EntityMyrmexBase, SegmentedModel<EntityMyrmexBase>> {

    protected final RenderMyrmexBase livingEntityRenderer;

    public LayerMyrmexItem(RenderMyrmexBase livingEntityRendererIn) {
        super(livingEntityRendererIn);
        this.livingEntityRenderer = livingEntityRendererIn;
    }

    private void renderHeldItem(EntityMyrmexBase myrmex, ItemStack stack, ItemCameraTransforms.TransformType transform, HandSide handSide) {

    }

    protected void translateToHand(HandSide side, MatrixStack stack) {
        ((ModelMyrmexBase) this.livingEntityRenderer.getModel()).postRenderArm(0, stack);
    }

    public boolean shouldCombineTextures() {
        return false;
    }

    @Override
    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, EntityMyrmexBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entitylivingbaseIn instanceof EntityMyrmexWorker) {
            ItemStack itemstack = entitylivingbaseIn.getItemInHand(Hand.MAIN_HAND);
            if (!itemstack.isEmpty()) {
                matrixStackIn.pushPose();
                if (!itemstack.isEmpty()) {
                    matrixStackIn.pushPose();

                    if (entitylivingbaseIn.isShiftKeyDown()) {
                        matrixStackIn.translate(0.0F, 0.2F, 0.0F);
                    }
                    this.translateToHand(HandSide.RIGHT, matrixStackIn);
                    matrixStackIn.translate(0F, 0.3F, -1.6F);
                    if (itemstack.getItem() instanceof BlockItem) {
                        matrixStackIn.translate(0F, 0, 0.2F);
                    } else {
                        matrixStackIn.translate(0F, 0.2F, 0.3F);
                    }
                    matrixStackIn.mulPose(new Quaternion(Vector3f.XP, 160, true));
                    matrixStackIn.mulPose(new Quaternion(Vector3f.YP, 180, true));
                    Minecraft.getInstance().getItemRenderer().renderStatic(itemstack, ItemCameraTransforms.TransformType.FIXED, packedLightIn, OverlayTexture.NO_OVERLAY, matrixStackIn, bufferIn);
                    matrixStackIn.popPose();
                }
                matrixStackIn.popPose();
            }
        }
    }
}