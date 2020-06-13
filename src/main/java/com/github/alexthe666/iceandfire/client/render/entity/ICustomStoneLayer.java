package com.github.alexthe666.iceandfire.client.render.entity;

import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;

public interface ICustomStoneLayer {
    LayerRenderer getStoneLayer(LivingRenderer render);

    LayerRenderer getCrackLayer(LivingRenderer render);
}
