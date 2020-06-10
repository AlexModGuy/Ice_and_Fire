package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.client.model.ModelTroll;
import com.github.alexthe666.iceandfire.client.render.entity.layer.LayerStoneEntityCrack;
import com.github.alexthe666.iceandfire.client.render.entity.layer.LayerTrollEyes;
import com.github.alexthe666.iceandfire.client.render.entity.layer.LayerTrollStone;
import com.github.alexthe666.iceandfire.client.render.entity.layer.LayerTrollWeapon;
import com.github.alexthe666.iceandfire.entity.EntityTroll;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.MobRendererBase;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@OnlyIn(Dist.CLIENT)
public class RenderTroll extends MobRenderer<EntityTroll> implements ICustomStoneLayer {

    public RenderTroll(EntityRendererManager renderManager) {
        super(renderManager, new ModelTroll(), 0.9F);
        this.layerRenderers.add(new LayerTrollWeapon(this));
        this.layerRenderers.add(new LayerTrollEyes(this));
    }

    @Override
    public void preRenderCallback(EntityTroll LivingEntityIn, float partialTickTime) {
    }

    @Override
    public ResourceLocation getEntityTexture(EntityTroll troll) {
        return troll.getType().TEXTURE;
    }

    @Override
    public LayerRenderer getStoneLayer(MobRendererBase render) {
        return new LayerTrollStone(render);
    }

    @Override
    public LayerRenderer getCrackLayer(MobRendererBase render) {
        return new LayerStoneEntityCrack(render);
    }
}
