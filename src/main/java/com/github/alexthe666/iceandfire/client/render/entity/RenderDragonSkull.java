package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.entity.EntityDragonSkull;
import com.github.alexthe666.iceandfire.enums.EnumDragonTextures;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@OnlyIn(Dist.CLIENT)
public class RenderDragonSkull extends Render<EntityDragonSkull> {

    public static final float[] growth_stage_1 = new float[]{1F, 3F};
    public static final float[] growth_stage_2 = new float[]{3F, 7F};
    public static final float[] growth_stage_3 = new float[]{7F, 12.5F};
    public static final float[] growth_stage_4 = new float[]{12.5F, 20F};
    public static final float[] growth_stage_5 = new float[]{20F, 30F};
    public float[][] growth_stages;
    private IceAndFireTabulaModel fireDragonModel;
    private IceAndFireTabulaModel iceDragonModel;

    public RenderDragonSkull(RenderManager renderManager, ModelBase fireDragonModel, ModelBase iceDragonModel) {
        super(renderManager);
        growth_stages = new float[][]{growth_stage_1, growth_stage_2, growth_stage_3, growth_stage_4, growth_stage_5};
        this.fireDragonModel = (IceAndFireTabulaModel) fireDragonModel;
        this.iceDragonModel = (IceAndFireTabulaModel) iceDragonModel;
    }

    private static void setRotationAngles(ModelRenderer cube, float rotX, float rotY, float rotZ) {
        cube.rotateAngleX = rotX;
        cube.rotateAngleY = rotY;
        cube.rotateAngleZ = rotZ;
    }

    public void doRender(EntityDragonSkull entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.pushMatrix();
        GlStateManager.disableCull();
        GlStateManager.translate((float) x, (float) y, (float) z);
        GlStateManager.rotate(entity.getYaw(), 0, -1, 0);
        float f = 0.0625F;
        GlStateManager.enableRescaleNormal();
        GlStateManager.scale(1.0F, -1.0F, 1.0F);
        GlStateManager.enableAlpha();
        this.bindEntityTexture(entity);
        if (this.renderOutlines) {
            GlStateManager.enableColorMaterial();
            GlStateManager.enableOutlineMode(this.getTeamColor(entity));
        }
        float size = getRenderSize(entity) / 3;
        GlStateManager.scale(size, size, size);
        GlStateManager.translate(0, entity.isOnWall() ? -0.24F : -0.12F, 0.5F);
        if (entity.getType() == 0) {
            fireDragonModel.resetToDefaultPose();
            setRotationAngles(fireDragonModel.getCube("Head"), entity.isOnWall() ? (float) Math.toRadians(50F) : 0F, 0, 0);
            fireDragonModel.getCube("Head").render(0.0625F);
        }
        if (entity.getType() == 1) {
            iceDragonModel.resetToDefaultPose();
            setRotationAngles(iceDragonModel.getCube("Head"), entity.isOnWall() ? (float) Math.toRadians(50F) : 0F, 0, 0);
            iceDragonModel.getCube("Head").render(0.0625F);
        }
        if (this.renderOutlines) {
            GlStateManager.disableOutlineMode();
            GlStateManager.disableColorMaterial();
        }
        GlStateManager.popMatrix();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    protected ResourceLocation getEntityTexture(EntityDragonSkull entity) {
        if (entity.getType() == 1) {
            return EnumDragonTextures.getIceDragonSkullTextures(entity);
        }
        return EnumDragonTextures.getFireDragonSkullTextures(entity);
    }

    public float getRenderSize(EntityDragonSkull skull) {
        float step = (growth_stages[skull.getDragonStage() - 1][1] - growth_stages[skull.getDragonStage() - 1][0]) / 25;
        if (skull.getDragonAge() > 125) {
            return growth_stages[skull.getDragonStage() - 1][0] + ((step * 25));
        }
        return growth_stages[skull.getDragonStage() - 1][0] + ((step * this.getAgeFactor(skull)));
    }

    private int getAgeFactor(EntityDragonSkull skull) {
        return (skull.getDragonStage() > 1 ? skull.getDragonAge() - (25 * (skull.getDragonStage() - 1)) : skull.getDragonAge());
    }

}
