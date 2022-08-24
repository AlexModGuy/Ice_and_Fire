package com.github.alexthe666.iceandfire.client.render.tile;

import com.github.alexthe666.iceandfire.client.model.ModelDeathWormGauntlet;
import com.github.alexthe666.iceandfire.client.render.entity.RenderDeathWorm;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.item.ItemStack;

public class RenderDeathWormGauntlet {
    private static final ModelDeathWormGauntlet MODEL = new ModelDeathWormGauntlet();

    public void renderItem(ItemStack stack, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        RenderType texture;

        if (stack.getItem() == IafItemRegistry.DEATHWORM_GAUNTLET_RED) {
            texture = RenderType.entityCutout(RenderDeathWorm.TEXTURE_RED);
        } else if (stack.getItem() == IafItemRegistry.DEATHWORM_GAUNTLET_WHITE) {
            texture = RenderType.entityCutout(RenderDeathWorm.TEXTURE_WHITE);
        } else {
            texture = RenderType.entityCutout(RenderDeathWorm.TEXTURE_YELLOW);
        }
        matrixStackIn.pushPose();
        matrixStackIn.translate(0.5F, 0.5F, 0.5F);
        matrixStackIn.pushPose();
        matrixStackIn.pushPose();
        MODEL.animate(stack, Minecraft.getInstance().getFrameTime());
        MODEL.renderToBuffer(matrixStackIn, bufferIn.getBuffer(texture), combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixStackIn.popPose();
        matrixStackIn.popPose();
        matrixStackIn.popPose();
    }
}
