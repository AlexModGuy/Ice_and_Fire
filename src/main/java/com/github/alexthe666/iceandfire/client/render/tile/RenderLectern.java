package com.github.alexthe666.iceandfire.client.render.tile;

import com.github.alexthe666.iceandfire.entity.tile.TileEntityLectern;
import net.minecraft.client.model.ModelBook;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class RenderLectern extends TileEntitySpecialRenderer {

	private static final ResourceLocation bookTex = new ResourceLocation("textures/entity/enchanting_table_book.png");
	private ModelBook book = new ModelBook();

	@Override
	public void renderTileEntityAt(TileEntity te, double x, double y, double z, float partialTicks, int destroyStage){
		TileEntityLectern lectern = (TileEntityLectern) te;
		GlStateManager.pushMatrix();
		GlStateManager.translate((float) x + 0.5F, (float) y + 1.07F, (float) z + 0.5F);
		GlStateManager.scale(0.8F, 0.8F, 0.8F);
		GlStateManager.rotate(this.getRotation(lectern), 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(112.5F, 0.0F, 0.0F, 1.0F);
		GlStateManager.rotate(180F, 1.0F, 0.0F, 0.0F);

		this.bindTexture(bookTex);
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

		GlStateManager.enableCull();
		this.book.render((Entity) null, 0, f4, f5, 1, 0.0F, 0.0625F);
		GlStateManager.popMatrix();
	}

	private float getRotation(TileEntityLectern lectern) {
		switch (lectern.getBlockMetadata()) {
			default:
				return 90;
			case 1:
				return 0;
			case 2:
				return -90;
			case 3:
				return 180;

		}
	}

}
