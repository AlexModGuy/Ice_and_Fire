package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.client.model.ModelFireDragon;
import com.github.alexthe666.iceandfire.client.render.entity.layer.LayerDragonArmor;
import com.github.alexthe666.iceandfire.client.render.entity.layer.LayerDragonEyes;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderDragonBase extends RenderLiving<EntityDragonBase> {

    public RenderDragonBase(RenderManager renderManager, ModelBase model) {
        super(renderManager, model, 0.8F);
        this.addLayer(new LayerDragonEyes(this));
        this.layerRenderers.add(new LayerDragonArmor(this, 0, model instanceof ModelFireDragon ? model instanceof ModelFireDragon ? "firedragon" : "icedragon" : "icedragon"));
        this.layerRenderers.add(new LayerDragonArmor(this, 1, model instanceof ModelFireDragon ? "firedragon" : "icedragon"));
        this.layerRenderers.add(new LayerDragonArmor(this, 2, model instanceof ModelFireDragon ? "firedragon" : "icedragon"));
        this.layerRenderers.add(new LayerDragonArmor(this, 3, model instanceof ModelFireDragon ? "firedragon" : "icedragon"));

    }

    @Override
    protected void preRenderCallback(EntityDragonBase entity, float f) {
        this.shadowSize = ((EntityDragonBase) entity).getRenderSize() / 3;
        GL11.glScalef(shadowSize, shadowSize, shadowSize);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityDragonBase entity) {
        return new ResourceLocation(((EntityDragonBase) entity).getTexture() + ".png");
    }

}
