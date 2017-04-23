package com.github.alexthe666.iceandfire.client.render.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RenderDragonEgg extends RenderLiving {

    public RenderDragonEgg(RenderManager renderManager, ModelBase modelDragonEgg) {
        super(renderManager, modelDragonEgg, 0.3F);
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        if (entity instanceof EntityDragonEgg) {
            return new ResourceLocation(((EntityDragonEgg) entity).getTexture());
        } else {
            return new ResourceLocation("iceandfire:textures/models/firedragon/egg_red.png");
        }
    }

}
