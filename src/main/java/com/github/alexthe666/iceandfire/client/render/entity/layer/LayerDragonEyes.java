package com.github.alexthe666.iceandfire.client.render.entity.layer;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.github.alexthe666.iceandfire.client.render.entity.RenderDragonBase;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;

@SideOnly(Side.CLIENT)
public class LayerDragonEyes implements LayerRenderer<EntityDragonBase> {
	private final RenderDragonBase render;

	public LayerDragonEyes(RenderDragonBase renderIn) {
		this.render = renderIn;
	}

	public void doRenderLayer(EntityDragonBase dragon, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		if (dragon.getTextureOverlay() != null) {
			this.render.bindTexture(new ResourceLocation(dragon.getTextureOverlay() + ".png"));
			GlStateManager.enableBlend();
			GlStateManager.disableAlpha();
			GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
			GlStateManager.depthMask(false);
			int i = 61680;
			int j = i % 65536;
			int k = i / 65536;
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) j, (float) k);
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			this.render.getMainModel().render(dragon, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
			i = dragon.getBrightnessForRender(partialTicks);
			j = i % 65536;
			k = i / 65536;
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) j, (float) k);
			this.render.setLightmap(dragon, partialTicks);
			GlStateManager.disableBlend();
			GlStateManager.enableAlpha();
		}
	}

	public boolean shouldCombineTextures() {
		return false;
	}
}