package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.entity.EntityDragonArrow;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;


public class RenderDragonArrow extends ArrowRenderer<EntityDragonArrow> {
    private static final ResourceLocation TEXTURE = new ResourceLocation("iceandfire:textures/models/misc/dragonbone_arrow.png");

    public RenderDragonArrow(EntityRendererProvider.Context context) {
        super(context);
    }


    @Override
    public ResourceLocation getTextureLocation(EntityDragonArrow entity) {
        return TEXTURE;
    }
}