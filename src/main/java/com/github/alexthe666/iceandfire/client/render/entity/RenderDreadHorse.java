package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.client.render.entity.layer.LayerGenericGlowing;
import com.github.alexthe666.iceandfire.entity.EntityDreadHorse;
import com.google.common.collect.Maps;
import net.minecraft.client.model.ModelHorse;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.passive.*;
import net.minecraft.util.ResourceLocation;

import java.util.Map;

public class RenderDreadHorse extends RenderLiving<EntityDreadHorse> {
    private final float scale;
    public static final ResourceLocation TEXTURE = new ResourceLocation("iceandfire:textures/models/dread/dread_knight_horse.png");
    public static final ResourceLocation TEXTURE_EYES = new ResourceLocation("iceandfire:textures/models/dread/dread_knight_horse_eyes.png");

    public RenderDreadHorse(RenderManager manager)
    {
        this(manager, 1.0F);
    }

    public RenderDreadHorse(RenderManager renderManagerIn, float scaleIn)
    {
        super(renderManagerIn, new ModelHorse(), 0.75F);
        this.addLayer(new LayerGenericGlowing(this, TEXTURE_EYES));
        this.scale = scaleIn;
    }

    protected void preRenderCallback(EntityDreadHorse LivingEntityIn, float partialTickTime)
    {
        GlStateManager.scale(this.scale, this.scale, this.scale);
        super.preRenderCallback(LivingEntityIn, partialTickTime);
    }

    protected ResourceLocation getEntityTexture(EntityDreadHorse entity)
    {
        return TEXTURE;
    }
}
