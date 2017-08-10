package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.client.model.ModelDragonEgg;
import com.github.alexthe666.iceandfire.entity.EntityDragonEgg;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RenderDragonEgg extends RenderLiving {

    public RenderDragonEgg(RenderManager renderManager) {
        super(renderManager, new ModelDragonEgg(), 0.3F);
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
