package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.client.model.ModelStymphalianBird;
import com.github.alexthe666.iceandfire.entity.EntityStymphalianBird;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class RenderStymphalianBird extends MobRenderer<EntityStymphalianBird, ModelStymphalianBird> {

    public static final ResourceLocation TEXTURE = new ResourceLocation("iceandfire:textures/models/stymphalianbird/stymphalian_bird.png");

    public RenderStymphalianBird(EntityRendererProvider.Context context) {
        super(context, new ModelStymphalianBird(), 0.6F);
    }

    @Override
    public void scale(@NotNull EntityStymphalianBird LivingEntityIn, PoseStack stack, float partialTickTime) {
        stack.scale(0.75F, 0.75F, 0.75F);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull EntityStymphalianBird cyclops) {
        return TEXTURE;
    }

}
