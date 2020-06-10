package com.github.alexthe666.iceandfire.client.render.tile;

import com.github.alexthe666.iceandfire.block.BlockLectern;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityJar;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityLectern;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.model.BookModel;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class RenderLectern<T extends TileEntityLectern> extends TileEntityRenderer<T> {

    private static final RenderType ENCHANTMENT_TABLE_BOOK_TEXTURE = RenderType.getEntityCutoutNoCull(new ResourceLocation("iceandfire:textures/models/lectern_book.png"));
    private BookModel book = new BookModel();

    public RenderLectern(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(T entity, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        TileEntityLectern lectern = (TileEntityLectern) entity;
        matrixStackIn.push();
        matrixStackIn.translate(0.5F, 1.07F, 0.5F);
        matrixStackIn.scale(0.8F, 0.8F, 0.8F);
        matrixStackIn.rotate(new Quaternion(Vector3f.YP, this.getRotation(lectern), true));
        matrixStackIn.rotate(new Quaternion(Vector3f.ZP, 112.5F, true));
        matrixStackIn.rotate(new Quaternion(Vector3f.XP, 180F, true));
        float f4 = lectern.pageFlipPrev + (lectern.pageFlip - lectern.pageFlipPrev) * partialTicks + 0.25F;
        float f5 = lectern.pageFlipPrev + (lectern.pageFlip - lectern.pageFlipPrev) * partialTicks + 0.75F;
        f4 = (f4 - MathHelper.fastFloor(f4)) * 1.6F - 0.3F;
        f5 = (f5 - MathHelper.fastFloor(f5)) * 1.6F - 0.3F;

        if (f4 < 0.0F) {
            f4 = 0.0F;
        }

        if (f5 < 0.0F) {
            f5 = 0.0F;
        }

        if (f4 > 1.0F) {
            f4 = 1.0F;
        }

        if (f5 > 1.0F) {
            f5 = 1.0F;
        }
        this.book.render(matrixStackIn, bufferIn.getBuffer(ENCHANTMENT_TABLE_BOOK_TEXTURE), combinedLightIn, combinedOverlayIn, 1, 1F, 1, 1);
        matrixStackIn.pop();
    }

    private float getRotation(TileEntityLectern lectern) {
        switch (lectern.getBlockState().get(BlockLectern.FACING)) {
            default:
                return 90;
            case EAST:
                return 0;
            case WEST:
                return -90;
            case SOUTH:
                return 180;

        }
    }

}
