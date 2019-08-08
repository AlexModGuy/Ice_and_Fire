package com.github.alexthe666.iceandfire.client.render.entity.layer;

import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.EntityIceDragon;
import com.github.alexthe666.iceandfire.enums.EnumDragonTextures;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LayerDragonMale implements LayerRenderer {
	private final RenderLiving renderer;

	public LayerDragonMale(RenderLiving renderer) {
		this.renderer = renderer;
	}

	public void doRenderLayer(EntityDragonBase entity, float f, float f1, float i, float f2, float f3, float f4, float f5) {
		if (entity.isMale() && !entity.isSkeletal()) {
			GlStateManager.enableNormalize();
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
			if (entity instanceof EntityIceDragon) {
				this.renderer.bindTexture(EnumDragonTextures.getDragonEnum(entity).ICE_MALE_OVERLAY);
			} else {
				this.renderer.bindTexture(EnumDragonTextures.getDragonEnum(entity).FIRE_MALE_OVERLAY);

			}
			this.renderer.getMainModel().render(entity, f, f1, f2, f3, f4, f5);
			GlStateManager.disableBlend();
			GlStateManager.disableNormalize();
		}
	}

	@Override
	public boolean shouldCombineTextures() {
		return true;
	}

	@Override
	public void doRenderLayer(EntityLivingBase entity, float f, float f1, float f2, float f3, float f4, float f5, float f6) {
		this.doRenderLayer((EntityDragonBase) entity, f, f1, f2, f3, f4, f5, f6);
	}
}