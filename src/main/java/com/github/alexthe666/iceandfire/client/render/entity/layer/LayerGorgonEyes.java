package com.github.alexthe666.iceandfire.client.render.entity.layer;

import com.github.alexthe666.iceandfire.client.render.entity.RenderGorgon;
import com.github.alexthe666.iceandfire.entity.EntityGorgon;
import com.github.alexthe666.iceandfire.entity.StoneEntityProperties;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@OnlyIn(Dist.CLIENT)
public class LayerGorgonEyes implements LayerRenderer<EntityGorgon> {
    private static final ResourceLocation TEXTURE = new ResourceLocation("iceandfire:textures/models/gorgon/gorgon_eyes.png");
    private final RenderGorgon render;

    public LayerGorgonEyes(RenderGorgon renderIn) {
        this.render = renderIn;
    }

    public void doRenderLayer(EntityGorgon gorgon, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        StoneEntityProperties properties = EntityPropertiesHandler.INSTANCE.getProperties(gorgon, StoneEntityProperties.class);
        if (properties == null || !properties.isStone) {
            if (gorgon.getAnimation() == EntityGorgon.ANIMATION_SCARE || gorgon.getAnimation() == EntityGorgon.ANIMATION_HIT) {
                this.render.bindTexture(TEXTURE);
                GlStateManager.enableBlend();
                GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
                GlStateManager.disableLighting();
                GlStateManager.depthMask(!gorgon.isInvisible());
                int i = 61680;
                int j = 61680;
                int k = 0;
                OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 61680.0F, 0.0F);
                GlStateManager.enableLighting();
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                this.render.getMainModel().render(gorgon, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
                this.render.setLightmap(gorgon);
                GlStateManager.depthMask(true);
                GlStateManager.disableBlend();
            }
        }
    }

    public boolean shouldCombineTextures() {
        return false;
    }
}