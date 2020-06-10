package com.github.alexthe666.iceandfire.client.render.entity.layer;

import com.github.alexthe666.iceandfire.client.model.ModelMyrmexBase;
import com.github.alexthe666.iceandfire.client.render.entity.RenderMyrmexBase;
import com.github.alexthe666.iceandfire.entity.EntityMyrmexBase;
import com.github.alexthe666.iceandfire.entity.EntityMyrmexWorker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;

public class LayerMyrmexItem extends LayerRenderer<EntityMyrmexBase> {

    protected final RenderMyrmexBase livingEntityRenderer;

    public LayerMyrmexItem(RenderMyrmexBase livingEntityRendererIn) {
        this.livingEntityRenderer = livingEntityRendererIn;
    }

    public void render(EntityMyrmexBase entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (entity instanceof EntityMyrmexWorker) {
            ItemStack itemstack = entity.getHeldItem(Hand.MAIN_HAND);
            if (!itemstack.isEmpty()) {
                GlStateManager.pushMatrix();
                this.renderHeldItem(entity, itemstack, ItemCameraTransforms.TransformType.HEAD, HandSide.RIGHT);
                GlStateManager.popMatrix();
            }
        }
    }

    private void renderHeldItem(EntityMyrmexBase myrmex, ItemStack stack, ItemCameraTransforms.TransformType transform, HandSide handSide) {
        if (!stack.isEmpty()) {
            GlStateManager.pushMatrix();

            if (myrmex.isShiftKeyDown()) {
                GlStateManager.translate(0.0F, 0.2F, 0.0F);
            }
            this.translateToHand(handSide);
            if (Minecraft.getInstance().getRenderItem().shouldRenderItemIn3D(stack)) {
                GlStateManager.translate(0F, 0.25F, -1.65F);
            } else {
                GlStateManager.translate(0F, 1F, -2F);
            }
            GlStateManager.rotate(160, 1, 0, 0);
            GlStateManager.rotate(180, 0, 1, 0);
            Minecraft.getInstance().getItemRenderer().renderItem(myrmex, stack, transform);
            GlStateManager.popMatrix();
        }
    }

    protected void translateToHand(HandSide side) {
        ((ModelMyrmexBase) this.livingEntityRenderer.getMainModel()).postRenderArm(0.0625F, side);
    }

    public boolean shouldCombineTextures() {
        return false;
    }

}