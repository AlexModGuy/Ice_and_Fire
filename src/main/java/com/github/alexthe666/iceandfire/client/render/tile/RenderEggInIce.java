package com.github.alexthe666.iceandfire.client.render.tile;

import com.github.alexthe666.iceandfire.client.model.ModelDragonEgg;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityDreadSpawner;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityEggInIce;
import com.github.alexthe666.iceandfire.enums.EnumDragonEgg;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.opengl.GL11;

public class RenderEggInIce<T extends TileEntityEggInIce> extends TileEntityRenderer<T> {

    public RenderEggInIce(TileEntityRendererDispatcher p_i226016_1_) {
        super(p_i226016_1_);
    }

    @Override
    public void render(T egg, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        ModelDragonEgg model = new ModelDragonEgg();
        if (egg.type != null) {
            matrixStackIn.push();
            matrixStackIn.translate(0.5, 1.5F, 0.5F);
            matrixStackIn.push();
            model.renderFrozen(egg);
            model.render(matrixStackIn, bufferIn.getBuffer(RenderPodium.getEggTexture(egg.type)), combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
            matrixStackIn.pop();
            matrixStackIn.pop();
        }
    }

}
