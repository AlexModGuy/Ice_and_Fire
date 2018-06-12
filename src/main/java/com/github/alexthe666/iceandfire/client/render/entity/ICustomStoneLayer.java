package com.github.alexthe666.iceandfire.client.render.entity;

import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;

public interface ICustomStoneLayer {
    LayerRenderer getStoneLayer(RenderLivingBase render);
    LayerRenderer getCrackLayer(RenderLivingBase render);
}
