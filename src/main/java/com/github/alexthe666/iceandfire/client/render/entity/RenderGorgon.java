package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.client.model.ModelGorgon;
import com.github.alexthe666.iceandfire.client.render.entity.layer.LayerGorgonEyes;
import com.github.alexthe666.iceandfire.entity.EntityGorgon;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class RenderGorgon extends MobRenderer<EntityGorgon, ModelGorgon> {

    public static final ResourceLocation PASSIVE_TEXTURE = new ResourceLocation("iceandfire:textures/models/gorgon/gorgon_passive.png");
    public static final ResourceLocation AGRESSIVE_TEXTURE = new ResourceLocation("iceandfire:textures/models/gorgon/gorgon_active.png");
    public static final ResourceLocation DEAD_TEXTURE = new ResourceLocation("iceandfire:textures/models/gorgon/gorgon_decapitated.png");

    public RenderGorgon(EntityRendererProvider.Context context) {
        super(context, new ModelGorgon(), 0.4F);
        this.layers.add(new LayerGorgonEyes(this));
    }

    @Override
    public void scale(@NotNull EntityGorgon LivingEntityIn, PoseStack stack, float partialTickTime) {
        stack.scale(0.85F, 0.85F, 0.85F);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(EntityGorgon gorgon) {
        if (gorgon.getAnimation() == EntityGorgon.ANIMATION_SCARE) {
            return AGRESSIVE_TEXTURE;
        } else if (gorgon.deathTime > 0) {
            return DEAD_TEXTURE;
        } else {
            return PASSIVE_TEXTURE;
        }
    }

}
