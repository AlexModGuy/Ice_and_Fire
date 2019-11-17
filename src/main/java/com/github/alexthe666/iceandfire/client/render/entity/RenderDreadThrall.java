package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.client.model.ModelDeathWorm;
import com.github.alexthe666.iceandfire.client.model.ModelDreadThrall;
import com.github.alexthe666.iceandfire.client.render.entity.layer.LayerGenericGlowing;
import com.github.alexthe666.iceandfire.entity.EntityDeathWorm;
import com.github.alexthe666.iceandfire.entity.EntityDreadThrall;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;

public class RenderDreadThrall extends RenderLiving<EntityDreadThrall> {
    public static final ResourceLocation TEXTURE = new ResourceLocation("iceandfire:textures/models/dread/dread_thrall.png");
    public static final ResourceLocation TEXTURE_EYES = new ResourceLocation("iceandfire:textures/models/dread/dread_thrall_eyes.png");

    public RenderDreadThrall(RenderManager renderManager) {
        super(renderManager, new ModelDreadThrall(), 0.6F);
        this.addLayer(new LayerGenericGlowing(this, TEXTURE_EYES));
    }

    @Override
    protected void preRenderCallback(EntityDreadThrall entity, float f) {
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(EntityDreadThrall entity) {
        return TEXTURE;
    }
}
