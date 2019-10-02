package com.github.alexthe666.iceandfire.client.render.entity.layer;

import com.github.alexthe666.iceandfire.client.render.entity.RenderTroll;
import com.github.alexthe666.iceandfire.entity.EntityGorgon;
import com.github.alexthe666.iceandfire.entity.EntityTroll;
import com.github.alexthe666.iceandfire.entity.StoneEntityProperties;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;

public class LayerTrollEyes implements LayerRenderer<EntityTroll> {

    private RenderTroll renderer;

    public LayerTrollEyes(RenderTroll renderer) {
        this.renderer = renderer;
    }

    @Override
    public void doRenderLayer(EntityTroll troll, float f, float f1, float f6, float f2, float f3, float f4, float f5) {
        StoneEntityProperties properties = EntityPropertiesHandler.INSTANCE.getProperties(troll, StoneEntityProperties.class);
        if (!EntityGorgon.isStoneMob(troll)) {
            this.renderer.bindTexture(troll.getType().TEXTURE_EYES);
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
            GlStateManager.disableLighting();
            GlStateManager.depthFunc(514);
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 0.0F);
            GlStateManager.enableLighting();
            Minecraft.getMinecraft().entityRenderer.setupFogColor(true);
            this.renderer.getMainModel().render(troll, f, f1, f2, f3, f4, f5);
            Minecraft.getMinecraft().entityRenderer.setupFogColor(false);
            this.renderer.setLightmap(troll);
            GlStateManager.disableBlend();
            GlStateManager.depthFunc(515);
        }
    }

    @Override
    public boolean shouldCombineTextures() {
        return true;
    }
}
