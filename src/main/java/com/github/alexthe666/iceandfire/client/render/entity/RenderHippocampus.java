package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.client.model.ModelHippocampus;
import com.github.alexthe666.iceandfire.client.model.ModelHippocampus;
import com.github.alexthe666.iceandfire.entity.EntityHippocampus;
import com.github.alexthe666.iceandfire.entity.EntityHippocampus;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;

public class RenderHippocampus extends RenderLiving<EntityHippocampus> {

	public RenderHippocampus(RenderManager renderManager) {
		super(renderManager, new ModelHippocampus(), 0.8F);
		this.layerRenderers.add(new RenderHippocampus.LayerHippocampusSaddle(this));
		this.layerRenderers.add(new RenderHippocampus.LayerHippocampusBridle(this));
		this.layerRenderers.add(new RenderHippocampus.LayerHippocampusChest(this));
		this.layerRenderers.add(new RenderHippocampus.LayerHippocampusArmor(this));
	}

	@Nullable
	@Override
	protected ResourceLocation getEntityTexture(EntityHippocampus entity) {
		return new ResourceLocation("iceandfire:textures/models/hippocampus/hippocampus_" + entity.getVariant() + ".png");
	}

	@SideOnly(Side.CLIENT)
	private class LayerHippocampusSaddle implements LayerRenderer {
		private final RenderHippocampus renderer;

		public LayerHippocampusSaddle(RenderHippocampus renderer) {
			this.renderer = renderer;
		}

		public void doRenderLayer(EntityHippocampus entity, float f, float f1, float i, float f2, float f3, float f4, float f5) {
			if (entity.isSaddled()) {
				this.renderer.bindTexture(new ResourceLocation("iceandfire:textures/models/hippocampus/saddle.png"));
				this.renderer.getMainModel().render(entity, f, f1, f2, f3, f4, f5);
			}
		}

		@Override
		public boolean shouldCombineTextures() {
			return false;
		}

		@Override
		public void doRenderLayer(EntityLivingBase entity, float f, float f1, float f2, float f3, float f4, float f5, float f6) {
			this.doRenderLayer((EntityHippocampus) entity, f, f1, f2, f3, f4, f5, f6);
		}
	}

	@SideOnly(Side.CLIENT)
	private class LayerHippocampusBridle implements LayerRenderer {
		private final RenderHippocampus renderer;

		public LayerHippocampusBridle(RenderHippocampus renderer) {
			this.renderer = renderer;
		}

		public void doRenderLayer(EntityHippocampus entity, float f, float f1, float i, float f2, float f3, float f4, float f5) {
			if (entity.isSaddled() && entity.getControllingPassenger() != null) {
				this.renderer.bindTexture(new ResourceLocation("iceandfire:textures/models/hippocampus/bridle.png"));
				this.renderer.getMainModel().render(entity, f, f1, f2, f3, f4, f5);
			}
		}

		@Override
		public boolean shouldCombineTextures() {
			return false;
		}

		@Override
		public void doRenderLayer(EntityLivingBase entity, float f, float f1, float f2, float f3, float f4, float f5, float f6) {
			this.doRenderLayer((EntityHippocampus) entity, f, f1, f2, f3, f4, f5, f6);
		}
	}

	@SideOnly(Side.CLIENT)
	private class LayerHippocampusChest implements LayerRenderer {
		private final RenderHippocampus renderer;

		public LayerHippocampusChest(RenderHippocampus renderer) {
			this.renderer = renderer;
		}

		public void doRenderLayer(EntityHippocampus entity, float f, float f1, float i, float f2, float f3, float f4, float f5) {
			if (entity.isChested()) {
				this.renderer.bindTexture(new ResourceLocation("iceandfire:textures/models/hippocampus/chest.png"));
				this.renderer.getMainModel().render(entity, f, f1, f2, f3, f4, f5);
			}
		}

		@Override
		public boolean shouldCombineTextures() {
			return false;
		}

		@Override
		public void doRenderLayer(EntityLivingBase entity, float f, float f1, float f2, float f3, float f4, float f5, float f6) {
			this.doRenderLayer((EntityHippocampus) entity, f, f1, f2, f3, f4, f5, f6);
		}
	}

	@SideOnly(Side.CLIENT)
	private class LayerHippocampusArmor implements LayerRenderer {
		private final RenderHippocampus renderer;

		public LayerHippocampusArmor(RenderHippocampus renderer) {
			this.renderer = renderer;
		}

		public void doRenderLayer(EntityHippocampus entity, float f, float f1, float i, float f2, float f3, float f4, float f5) {
			if (entity.getArmor() != 0) {
				this.renderer.bindTexture(new ResourceLocation("iceandfire:textures/models/hippocampus/armor_" + (entity.getArmor() != 1 ? entity.getArmor() != 2 ? "diamond" : "gold" : "iron") + ".png"));
				this.renderer.getMainModel().render(entity, f, f1, f2, f3, f4, f5);
			}
		}

		@Override
		public boolean shouldCombineTextures() {
			return false;
		}

		@Override
		public void doRenderLayer(EntityLivingBase entity, float f, float f1, float f2, float f3, float f4, float f5, float f6) {
			this.doRenderLayer((EntityHippocampus) entity, f, f1, f2, f3, f4, f5, f6);
		}
	}
}
