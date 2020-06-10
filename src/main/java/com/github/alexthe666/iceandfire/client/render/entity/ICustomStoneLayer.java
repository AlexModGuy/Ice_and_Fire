package com.github.alexthe666.iceandfire.client.render.entity;

import net.minecraft.client.renderer.entity.MobRendererBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;

public interface ICustomStoneLayer {
    LayerRenderer getStoneLayer(MobRendererBase render);

    LayerRenderer getCrackLayer(MobRendererBase render);
}
