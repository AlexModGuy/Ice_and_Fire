package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.client.model.ModelTroll;
import com.github.alexthe666.iceandfire.client.render.entity.layer.LayerStoneEntityCrack;
import com.github.alexthe666.iceandfire.client.render.entity.layer.LayerTrollEyes;
import com.github.alexthe666.iceandfire.client.render.entity.layer.LayerTrollStone;
import com.github.alexthe666.iceandfire.client.render.entity.layer.LayerTrollWeapon;
import com.github.alexthe666.iceandfire.entity.EntityTroll;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderTroll extends MobRenderer<EntityTroll, ModelTroll> implements ICustomStoneLayer {

    public RenderTroll(EntityRendererManager renderManager) {
        super(renderManager, new ModelTroll(), 0.9F);
        this.layerRenderers.add(new LayerTrollWeapon(this));
        this.layerRenderers.add(new LayerTrollStone(this));
        this.layerRenderers.add(new LayerTrollEyes(this));
    }

    @Override
    public ResourceLocation getEntityTexture(EntityTroll troll) {
        return troll.getTrollType().TEXTURE;
    }

    @Override
    public LayerRenderer getStoneLayer(LivingRenderer render) {
        return new LayerTrollStone(render);
    }

    @Override
    public LayerRenderer getCrackLayer(LivingRenderer render) {
        return new LayerStoneEntityCrack(render);
    }
}
