package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.client.render.entity.ModelDreadLichSkull;
import com.github.alexthe666.iceandfire.entity.EntityDreadLichSkull;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class RenderDreadLichSkull extends Render<EntityDreadLichSkull> {

    public static final ResourceLocation TEXTURE = new ResourceLocation("iceandfire:textures/models/dread/dread_lich_skull.png");
    private static final ModelDreadLichSkull MODEL_SPIRIT = new ModelDreadLichSkull();

    public RenderDreadLichSkull(RenderManager manager) {
        super(manager);
    }

    public void doRender(EntityDreadLichSkull entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.pushMatrix();
        GlStateManager.disableCull();
        GlStateManager.translate((float) x, (float) y, (float) z);
        float f = 0.0625F;
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableAlpha();
        this.bindEntityTexture(entity);

        if (this.renderOutlines) {
            GlStateManager.enableColorMaterial();
            GlStateManager.enableOutlineMode(this.getTeamColor(entity));
        }

        GlStateManager.pushMatrix();
        GlStateManager.scale(1.0F, -1.0F, 1.0F);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.disableLighting();
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 0.0F);
        float yaw = entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks;
        GlStateManager.translate(0F, 0.0F, 0F);
        GlStateManager.rotate(yaw - 180, 0.0F, 1.0F, 0.0F);
        new ModelDreadLichSkull().render(entity, 0, 0, partialTicks, 0, 0, 0.0625F);
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
        if (this.renderOutlines) {
            GlStateManager.disableOutlineMode();
            GlStateManager.disableColorMaterial();
        }

        GlStateManager.enableLighting();
        GlStateManager.popMatrix();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    private float interpolateValue(float start, float end, float pct) {
        return start + (end - start) * pct;
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(EntityDreadLichSkull entity) {
        return TEXTURE;
    }
}
