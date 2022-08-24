package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.client.model.ModelTroll;
import com.github.alexthe666.iceandfire.client.render.entity.layer.LayerTrollEyes;
import com.github.alexthe666.iceandfire.client.render.entity.layer.LayerTrollWeapon;
import com.github.alexthe666.iceandfire.entity.EntityTroll;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

public class RenderTroll extends MobRenderer<EntityTroll, ModelTroll> {

    public RenderTroll(EntityRendererManager renderManager) {
        super(renderManager, new ModelTroll(), 0.9F);
        this.layers.add(new LayerTrollWeapon(this));
        this.layers.add(new LayerTrollEyes(this));
    }

    @Override
    public ResourceLocation getTextureLocation(EntityTroll troll) {
        return troll.getTrollType().TEXTURE;
    }
}
