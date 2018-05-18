package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.client.model.ModelCyclops;
import com.github.alexthe666.iceandfire.client.model.ModelGorgon;
import com.github.alexthe666.iceandfire.client.model.ModelStymphalianBird;
import com.github.alexthe666.iceandfire.entity.EntityCyclops;
import com.github.alexthe666.iceandfire.entity.EntityGorgon;
import com.github.alexthe666.iceandfire.entity.EntityStymphalianBird;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderStymphalianBird extends RenderLiving<EntityStymphalianBird> {

    public static final ResourceLocation TEXTURE = new ResourceLocation("iceandfire:textures/models/stymphalianbird/stymphalian_bird.png");

    public RenderStymphalianBird(RenderManager renderManager) {
        super(renderManager, new ModelStymphalianBird(), 0.6F);
    }

    @Override
    public void preRenderCallback(EntityStymphalianBird entitylivingbaseIn, float partialTickTime) {

    }

    @Override
    protected ResourceLocation getEntityTexture(EntityStymphalianBird cyclops) {
        return TEXTURE;
    }

}
