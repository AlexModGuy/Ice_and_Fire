package com.github.alexthe666.iceandfire.client.render.entity.layer;

import com.github.alexthe666.iceandfire.entity.EntityStoneStatue;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

public class LayerStonePlayerEntityCrack implements LayerRenderer {

	protected static final ResourceLocation[] DESTROY_STAGES = new ResourceLocation[]{new ResourceLocation("textures/blocks/destroy_stage_0.png"), new ResourceLocation("textures/blocks/destroy_stage_1.png"), new ResourceLocation("textures/blocks/destroy_stage_2.png"), new ResourceLocation("textures/blocks/destroy_stage_3.png"), new ResourceLocation("textures/blocks/destroy_stage_4.png"), new ResourceLocation("textures/blocks/destroy_stage_5.png"), new ResourceLocation("textures/blocks/destroy_stage_6.png"), new ResourceLocation("textures/blocks/destroy_stage_7.png"), new ResourceLocation("textures/blocks/destroy_stage_8.png"), new ResourceLocation("textures/blocks/destroy_stage_9.png")};
	private RenderLivingBase renderer;

	public LayerStonePlayerEntityCrack(RenderLivingBase renderer) {
		this.renderer = renderer;
	}

	@Override
	public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float f, float f1, float i, float f2, float f3, float f4, float f5) {
		if (entitylivingbaseIn instanceof EntityStoneStatue) {
			int breakCount = ((EntityStoneStatue) entitylivingbaseIn).getCrackAmount();
			if (breakCount > 0) {
				GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.DST_COLOR, GlStateManager.DestFactor.SRC_COLOR, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
				GlStateManager.doPolygonOffset(-3.0F, -3.0F);
				GlStateManager.enablePolygonOffset();
				GlStateManager.enableBlend();
				GlStateManager.depthMask(true);
				this.renderer.bindTexture(DESTROY_STAGES[breakCount - 1]);
				this.renderer.getMainModel().render(entitylivingbaseIn, f, f1, f2, f3, f4, f5);
				GlStateManager.doPolygonOffset(0.0F, 0.0F);
				GlStateManager.disablePolygonOffset();
			}
		}
	}


	@Override
	public boolean shouldCombineTextures() {
		return true;
	}
}
