package com.github.alexthe666.iceandfire.client.render.entity.layer;

import com.github.alexthe666.iceandfire.ClientProxy;
import com.github.alexthe666.iceandfire.client.model.util.IceAndFireTabulaModel;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.StoneEntityProperties;
import com.github.alexthe666.iceandfire.enums.EnumDragonTextures;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelHorse;
import net.minecraft.client.model.ModelQuadruped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.util.ReportedException;
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
				boolean prey = dragon.getControllingPassenger() == null || dragon.getControllingPassenger().getEntityId() != passenger.getEntityId();
				ClientProxy.currentDragonRiders.remove(passenger.getUniqueID());
				float riderRot = passenger.prevRotationYaw + (passenger.rotationYaw - passenger.prevRotationYaw) * partialTicks;
				int animationTicks = 0;
				if(dragon.getAnimation() == EntityDragonBase.ANIMATION_SHAKEPREY){
					animationTicks = dragon.getAnimationTick();
				}
				if(animationTicks == 0 || animationTicks >= 15){
					translateToBody();
				}
				if(prey){
					if(animationTicks == 0 || animationTicks >= 15){
						translateToHead();
						Render render = Minecraft.getMinecraft().getRenderManager().getEntityRenderObject(passenger);
						ModelBase modelBase = null;
						if(render instanceof RenderLiving){
							modelBase = ((RenderLiving) render).getMainModel();
						}
						if((passenger.height > passenger.width || modelBase instanceof ModelBiped) && !(modelBase instanceof ModelQuadruped) && !(modelBase instanceof ModelHorse)) {
							GlStateManager.translate(-0.15F * passenger.height, 0.1F * dragonScale - 0.15F * passenger.width, -0.1F * dragonScale - 0.1F * passenger.width);
							GlStateManager.rotate(90, 0, 0, 1);
							GlStateManager.rotate(45, 0, 1, 0);
						}else{
							boolean horse = modelBase instanceof ModelHorse;
							GlStateManager.translate((horse ? - 0.08F : -0.15F) * passenger.width, 0.1F * dragonScale , -0.1F * dragonScale - 0.1F * passenger.width);
							GlStateManager.rotate(-90, 0, 1, 0);
						}
					}else{
						GlStateManager.translate(0, 0.555F * dragonScale, -0.5F * dragonScale);
					}

				}
				GlStateManager.rotate(180, 0, 0, 1);
				GlStateManager.translate(0, -.008F * dragonScale, -0.025F * dragonScale);
				GlStateManager.rotate(riderRot + 180, 0, 1, 0);
				GlStateManager.scale(1/dragonScale, 1/dragonScale, 1/dragonScale);
				Minecraft.getMinecraft().getRenderManager().setRenderOutlines(false);
				renderEntity(passenger, 0.0D, 0.0D, 0.0D, 0.0F, partialTicks, true);
				ClientProxy.currentDragonRiders.add(passenger.getUniqueID());
			}
		}
	}

	protected void translateToBody() {
		((IceAndFireTabulaModel) this.render.getMainModel()).getCube("BodyUpper").postRender(0.0625F);
		((IceAndFireTabulaModel) this.render.getMainModel()).getCube("Neck1").postRender(0.0625F);
	}

	protected void translateToHead() {
		((IceAndFireTabulaModel) this.render.getMainModel()).getCube("Neck2").postRender(0.0625F);
		((IceAndFireTabulaModel) this.render.getMainModel()).getCube("Neck3").postRender(0.0625F);
		((IceAndFireTabulaModel) this.render.getMainModel()).getCube("Head").postRender(0.0625F);
	}

	public void renderEntity(Entity entityIn, double x, double y, double z, float yaw, float partialTicks, boolean p_188391_10_)
	{
		Render<Entity> render = null;
		RenderManager manager = Minecraft.getMinecraft().getRenderManager();
		try
		{
			render = manager.<Entity>getEntityRenderObject(entityIn);

			if (render != null && manager.renderEngine != null)
			{
				try
				{
					render.doRender(entityIn, x, y, z, yaw, partialTicks);
				}
				catch (Throwable throwable1)
				{
					throw new ReportedException(CrashReport.makeCrashReport(throwable1, "Rendering entity in world"));
				}
			}
		}
		catch (Throwable throwable3)
		{
			CrashReport crashreport = CrashReport.makeCrashReport(throwable3, "Rendering entity in world");
			CrashReportCategory crashreportcategory = crashreport.makeCategory("Entity being rendered");
			entityIn.addEntityCrashInfo(crashreportcategory);
			CrashReportCategory crashreportcategory1 = crashreport.makeCategory("Renderer details");
			crashreportcategory1.addCrashSection("Assigned renderer", render);
			crashreportcategory1.addCrashSection("Location", CrashReportCategory.getCoordinateInfo(x, y, z));
			crashreportcategory1.addCrashSection("Rotation", Float.valueOf(yaw));
			crashreportcategory1.addCrashSection("Delta", Float.valueOf(partialTicks));
			throw new ReportedException(crashreport);
		}
	}

	public boolean shouldCombineTextures() {
		return false;
	}
}