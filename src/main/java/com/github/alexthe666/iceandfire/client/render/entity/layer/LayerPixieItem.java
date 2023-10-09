package com.github.alexthe666.iceandfire.client.render.entity.layer;

import com.github.alexthe666.iceandfire.client.model.ModelPixie;
import com.github.alexthe666.iceandfire.client.render.entity.RenderPixie;
import com.github.alexthe666.iceandfire.entity.EntityPixie;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class LayerPixieItem extends RenderLayer<EntityPixie, ModelPixie> {

    RenderPixie renderer;

    public LayerPixieItem(RenderPixie renderer) {
        super(renderer);
        this.renderer = renderer;
    }

    @Override
    public void render(@NotNull PoseStack matrixStackIn, @NotNull MultiBufferSource bufferIn, int packedLightIn, EntityPixie entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        ItemStack itemstack = entity.getItemInHand(InteractionHand.MAIN_HAND);
        if (!itemstack.isEmpty()) {
            matrixStackIn.pushPose();
            matrixStackIn.translate(-0.0625F, 0.53125F, 0.21875F);
            Item item = itemstack.getItem();
            Minecraft minecraft = Minecraft.getInstance();
            if (!(item instanceof BlockItem)) {
                matrixStackIn.translate(-0.075F, 0, -0.05F);
            } else {
                matrixStackIn.translate(-0.075F, 0, -0.05F);
            }
            matrixStackIn.translate(0.05F, 0.55F, -0.4F);
            matrixStackIn.mulPose(Axis.XP.rotationDegrees(200.0F));
            matrixStackIn.mulPose(Axis.YP.rotationDegrees(180.0F));
            Minecraft.getInstance().getItemRenderer().renderStatic(itemstack, ItemDisplayContext.FIXED, packedLightIn, OverlayTexture.NO_OVERLAY, matrixStackIn, bufferIn, Minecraft.getInstance().level, 0);
            matrixStackIn.popPose();
        }
    }
}