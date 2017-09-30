package com.github.alexthe666.iceandfire.client.render.tile;

import com.github.alexthe666.iceandfire.client.model.ModelDragonEgg;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityPodium;
import com.github.alexthe666.iceandfire.enums.EnumDragonEgg;
import com.github.alexthe666.iceandfire.item.ItemDragonEgg;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderPodium extends TileEntitySpecialRenderer {

	public static String getTexture(EnumDragonEgg type) {
		String i = type.isFire ? "firedragon/" : "icedragon/";
		return "iceandfire:textures/models/" + i + "egg_" + type.name().toLowerCase() + ".png";

	}

	@Override
	public void renderTileEntityAt(TileEntity entity, double x, double y, double z, float f, int f1) {
		ModelDragonEgg model = new ModelDragonEgg();
		TileEntityPodium podium = (TileEntityPodium) entity;

		if (!podium.getStackInSlot(0).isEmpty()) {
			if (podium.getStackInSlot(0).getItem() != null) {
				if (podium.getStackInSlot(0).getItem() instanceof ItemDragonEgg) {

					ItemDragonEgg item = (ItemDragonEgg) podium.getStackInSlot(0).getItem();

					GL11.glPushMatrix();
					GL11.glTranslatef((float) x + 0.5F, (float) y + 0.475F, (float) z + 0.5F);
					GL11.glPushMatrix();
					this.bindTexture(new ResourceLocation(getTexture(item.type)));
					GL11.glPushMatrix();
					model.renderPodium();
					GL11.glPopMatrix();
					GL11.glPopMatrix();
					GL11.glPopMatrix();

				}
			}
		}

	}
}
