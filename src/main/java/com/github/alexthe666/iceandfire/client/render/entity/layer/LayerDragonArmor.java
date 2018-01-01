package com.github.alexthe666.iceandfire.client.render.entity.layer;

import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LayerDragonArmor implements LayerRenderer {
	private final RenderLiving renderer;

	private int slot;
	private String dragonType;

	public LayerDragonArmor(RenderLiving renderer, int slot, String dragonType) {
		this.renderer = renderer;
		this.slot = slot;
		this.dragonType = dragonType;
	}

	public void doRenderLayer(EntityDragonBase entity, float f, float f1, float i, float f2, float f3, float f4, float f5) {
		if (entity.getArmorInSlot(slot) != 0) {
			this.renderer.bindTexture(new ResourceLocation("iceandfire:textures/models/" + dragonType + "/armor_" + armorPart() + "_" + entity.getArmorInSlot(slot) + ".png"));
			this.renderer.getMainModel().render(entity, f, f1, f2, f3, f4, f5);
		}
	}

	public String armorPart() {
		switch (slot) {
			case 1:
				return "neck";
			case 2:
				return "body";
			case 3:
				return "tail";
			default:
				return "head";
		}
	}

	@Override
	public boolean shouldCombineTextures() {
		return false;
	}

	@Override
	public void doRenderLayer(EntityLivingBase entity, float f, float f1, float f2, float f3, float f4, float f5, float f6) {
		this.doRenderLayer((EntityDragonBase) entity, f, f1, f2, f3, f4, f5, f6);
	}
}