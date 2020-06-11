package com.github.alexthe666.iceandfire.client.render.entity;

import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderStymphalianArrow extends ArrowRenderer {
    private static final ResourceLocation TEXTURE = new ResourceLocation("iceandfire:textures/models/misc/stymphalian_arrow.png");

    public RenderStymphalianArrow(EntityRendererManager render) {
        super(render);
    }

    @Override
    public ResourceLocation getEntityTexture(Entity entity) {
        return TEXTURE;
    }
}