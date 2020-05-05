package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.client.model.ModelDreadBeast;
import com.github.alexthe666.iceandfire.client.model.ModelDreadScuttler;
import com.github.alexthe666.iceandfire.client.render.entity.layer.LayerGenericGlowing;
import com.github.alexthe666.iceandfire.entity.EntityDreadBeast;
import com.github.alexthe666.iceandfire.entity.EntityDreadScuttler;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderDreadScuttler extends RenderLiving<EntityDreadScuttler> {

    public static final ResourceLocation TEXTURE_EYES = new ResourceLocation("iceandfire:textures/models/dread/dread_scuttler_eyes.png");
    public static final ResourceLocation TEXTURE = new ResourceLocation("iceandfire:textures/models/dread/dread_scuttler.png");

    public RenderDreadScuttler(RenderManager renderManager) {
        super(renderManager, new ModelDreadScuttler(), 0.75F);
        this.addLayer(new LayerGenericGlowing(this, TEXTURE_EYES));
    }

    @Override
    public void preRenderCallback(EntityDreadScuttler LivingEntityIn, float partialTickTime) {
        GlStateManager.scale(LivingEntityIn.getScale(), LivingEntityIn.getScale(), LivingEntityIn.getScale());
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityDreadScuttler beast) {
        return TEXTURE;

    }

}
