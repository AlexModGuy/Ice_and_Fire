package com.github.alexthe666.iceandfire.client.render.entity.layer;

import com.github.alexthe666.iceandfire.ClientProxy;
import com.github.alexthe666.iceandfire.client.model.util.IceAndFireTabulaModel;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.StoneEntityProperties;
import com.github.alexthe666.iceandfire.enums.EnumDragonTextures;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class LayerDragonRider implements LayerRenderer<EntityDragonBase> {
	private final RenderLiving render;

	public LayerDragonRider(RenderLiving renderIn) {
		this.render = renderIn;
	}

	public void doRenderLayer(EntityDragonBase dragon, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		if(!dragon.getPassengers().isEmpty()){
			float dragonScale = dragon.getRenderSize() / 3;;
			for(Entity passenger : dragon.getPassengers()){
				ClientProxy.currentDragonRiders.remove(passenger.getUniqueID());
				float riderRot = passenger.prevRotationYaw + (passenger.rotationYaw - passenger.prevRotationYaw) * partialTicks;
				translateToBody();
				GlStateManager.rotate(180, 0, 0, 1);
				GlStateManager.translate(0, 0.008F * dragonScale, -0.025F * dragonScale);
				GlStateManager.rotate(riderRot + 180, 0, 1, 0);
				GlStateManager.scale(1/dragonScale, 1/dragonScale, 1/dragonScale);
				Minecraft.getMinecraft().getRenderManager().renderEntity(passenger, 0.0D, 0.0D, 0.0D, 0.0F, partialTicks, false);
				ClientProxy.currentDragonRiders.add(passenger.getUniqueID());
			}
		}
	}

	protected void translateToBody() {
		((IceAndFireTabulaModel) this.render.getMainModel()).getCube("BodyUpper").postRender(0.0625F);
		((IceAndFireTabulaModel) this.render.getMainModel()).getCube("Neck1").postRender(0.0625F);
	}

	public boolean shouldCombineTextures() {
		return false;
	}
}