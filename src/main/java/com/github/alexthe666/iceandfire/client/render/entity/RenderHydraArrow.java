package com.github.alexthe666.iceandfire.client.render.entity;

import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RenderHydraArrow extends ArrowRenderer {
    private static final ResourceLocation TEXTURES = new ResourceLocation("iceandfire:textures/models/misc/hydra_arrow.png");

    public RenderHydraArrow(EntityRendererManager render) {
        super(render);
    }

    @Override
    public ResourceLocation getEntityTexture(Entity entity) {
        return TEXTURES;
    }

}