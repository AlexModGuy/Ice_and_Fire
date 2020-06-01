package com.github.alexthe666.iceandfire.client.render.entity.layer;

import com.github.alexthe666.iceandfire.ClientProxy;
import com.github.alexthe666.iceandfire.client.model.util.IceAndFireTabulaModel;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.EntityDreadQueen;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelHorse;
import net.minecraft.client.model.ModelQuadruped;
import net.minecraft.client.renderer.GlStateManager;
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

@SideOnly(Side.CLIENT)
public class LayerDragonRider implements LayerRenderer<EntityDragonBase> {
    private final RenderLiving render;
    private final boolean excludeDreadQueenMob;

    public LayerDragonRider(RenderLiving renderIn, boolean excludeDreadQueenMob) {
        this.render = renderIn;
        this.excludeDreadQueenMob = excludeDreadQueenMob;
    }

    public void doRenderLayer(EntityDragonBase dragon, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        GlStateManager.pushMatrix();
        if (!dragon.getPassengers().isEmpty()) {
            float dragonScale = dragon.getRenderSize() / 3;
            for (Entity passenger : dragon.getPassengers()) {
                boolean prey = dragon.getControllingPassenger() == null || dragon.getControllingPassenger().getEntityId() != passenger.getEntityId();
                if(excludeDreadQueenMob && passenger instanceof EntityDreadQueen){
                    prey = false;
                }
                ClientProxy.currentDragonRiders.remove(passenger.getUniqueID());
                float riderRot = passenger.prevRotationYaw + (passenger.rotationYaw - passenger.prevRotationYaw) * partialTicks;
                int animationTicks = 0;
                if (dragon.getAnimation() == EntityDragonBase.ANIMATION_SHAKEPREY) {
                    animationTicks = dragon.getAnimationTick();
                }
                if (animationTicks == 0 || animationTicks >= 15) {
                    translateToBody();
                }
                if (prey) {
                    if (animationTicks == 0 || animationTicks >= 15 || dragon.isFlying()) {
                        translateToHead();
                        Render render = Minecraft.getMinecraft().getRenderManager().getEntityRenderObject(passenger);
                        ModelBase modelBase = null;
                        if (render instanceof RenderLiving) {
                            modelBase = ((RenderLiving) render).getMainModel();
                        }
                        if ((passenger.height > passenger.width || modelBase instanceof ModelBiped) && !(modelBase instanceof ModelQuadruped) && !(modelBase instanceof ModelHorse)) {
                            GlStateManager.translate(-0.15F * passenger.height, 0.1F * dragonScale - 0.1F * passenger.height, -0.1F * dragonScale - 0.1F * passenger.width);
                            GlStateManager.rotate(90, 0, 0, 1);
                            GlStateManager.rotate(45, 0, 1, 0);
                        } else {
                            boolean horse = modelBase instanceof ModelHorse;
                            GlStateManager.translate((horse ? -0.08F : -0.15F) * passenger.width, 0.1F * dragonScale - 0.15F * passenger.width, -0.1F * dragonScale - 0.1F * passenger.width);
                            GlStateManager.rotate(-90, 0, 1, 0);
                        }
                    } else {
                        GlStateManager.translate(0, 0.555F * dragonScale, -0.5F * dragonScale);
                    }

                }else{
                    GlStateManager.translate(0, -0.01F * dragonScale, -0.035F * dragonScale);
                }
                GlStateManager.pushMatrix();
                GlStateManager.rotate(180, 0, 0, 1);
                GlStateManager.rotate(riderRot + 180, 0, 1, 0);
                GlStateManager.scale(1 / dragonScale, 1 / dragonScale, 1 / dragonScale);
                GlStateManager.translate(0, -0.25F, 0);
                renderEntity(passenger, 0.0D, 0.0D, 0.0D, 0.0F, partialTicks, true);
                GlStateManager.popMatrix();
                ClientProxy.currentDragonRiders.add(passenger.getUniqueID());
            }
        }
        GlStateManager.popMatrix();
    }

    protected void translateToBody() {
        postRender(((IceAndFireTabulaModel) this.render.getMainModel()).getCube("BodyUpper"), 0.0625F);
        postRender(((IceAndFireTabulaModel) this.render.getMainModel()).getCube("Neck1"), 0.0625F);
    }

    protected void translateToHead() {
        postRender(((IceAndFireTabulaModel) this.render.getMainModel()).getCube("Neck2"), 0.0625F);
        postRender(((IceAndFireTabulaModel) this.render.getMainModel()).getCube("Neck3"), 0.0625F);
        postRender(((IceAndFireTabulaModel) this.render.getMainModel()).getCube("Head"), 0.0625F);
    }

    protected void postRender(AdvancedModelRenderer renderer, float scale) {
        if (renderer.rotateAngleX == 0.0F && renderer.rotateAngleY == 0.0F && renderer.rotateAngleZ == 0.0F) {
            if (renderer.rotationPointX != 0.0F || renderer.rotationPointY != 0.0F || renderer.rotationPointZ != 0.0F) {
                GlStateManager.translate(renderer.rotationPointX * scale, renderer.rotationPointY * scale, renderer.rotationPointZ * scale);
            }
        } else {
            GlStateManager.translate(renderer.rotationPointX * scale, renderer.rotationPointY * scale, renderer.rotationPointZ * scale);

            if (renderer.rotateAngleZ != 0.0F) {
                GlStateManager.rotate(renderer.rotateAngleZ * (180F / (float) Math.PI), 0.0F, 0.0F, 1.0F);
            }

            if (renderer.rotateAngleY != 0.0F) {
                GlStateManager.rotate(renderer.rotateAngleY * (180F / (float) Math.PI), 0.0F, 1.0F, 0.0F);
            }

            if (renderer.rotateAngleX != 0.0F) {
                GlStateManager.rotate(renderer.rotateAngleX * (180F / (float) Math.PI), 1.0F, 0.0F, 0.0F);
            }
        }
    }

    public void renderEntity(Entity entityIn, double x, double y, double z, float yaw, float partialTicks, boolean p_188391_10_) {
        Render<Entity> render = null;
        RenderManager manager = Minecraft.getMinecraft().getRenderManager();
        try {
            render = manager.getEntityRenderObject(entityIn);
            if (render != null && manager.renderEngine != null) {
                try {
                    render.doRender(entityIn, x, y, z, yaw, partialTicks);
                } catch (Throwable throwable1) {
                    throw new ReportedException(CrashReport.makeCrashReport(throwable1, "Rendering entity in world"));
                }
            }
        } catch (Throwable throwable3) {
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