package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.client.model.ModelStymphalianBird;
import com.github.alexthe666.iceandfire.entity.EntityStymphalianBird;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

public class RenderStymphalianBird extends MobRenderer<EntityStymphalianBird, ModelStymphalianBird> {

    public static final ResourceLocation TEXTURE = new ResourceLocation("iceandfire:textures/models/stymphalianbird/stymphalian_bird.png");

    public RenderStymphalianBird(EntityRendererManager renderManager) {
        super(renderManager, new ModelStymphalianBird(), 0.6F);
    }

    @Override
    public void scale(EntityStymphalianBird LivingEntityIn, MatrixStack stack, float partialTickTime) {
        stack.scale(0.75F, 0.75F, 0.75F);
    }

    @Override
    public ResourceLocation getTextureLocation(EntityStymphalianBird cyclops) {
        return TEXTURE;
    }

}
