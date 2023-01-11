package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.client.model.ModelAmphithere;
import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;


public class RenderAmphithere extends MobRenderer<EntityAmphithere, ModelAmphithere> {

    public static final ResourceLocation TEXTURE_BLUE = new ResourceLocation("iceandfire:textures/models/amphithere/amphithere_blue.png");
    public static final ResourceLocation TEXTURE_BLUE_BLINK = new ResourceLocation("iceandfire:textures/models/amphithere/amphithere_blue_blink.png");
    public static final ResourceLocation TEXTURE_GREEN = new ResourceLocation("iceandfire:textures/models/amphithere/amphithere_green.png");
    public static final ResourceLocation TEXTURE_GREEN_BLINK = new ResourceLocation("iceandfire:textures/models/amphithere/amphithere_green_blink.png");
    public static final ResourceLocation TEXTURE_OLIVE = new ResourceLocation("iceandfire:textures/models/amphithere/amphithere_olive.png");
    public static final ResourceLocation TEXTURE_OLIVE_BLINK = new ResourceLocation("iceandfire:textures/models/amphithere/amphithere_olive_blink.png");
    public static final ResourceLocation TEXTURE_RED = new ResourceLocation("iceandfire:textures/models/amphithere/amphithere_red.png");
    public static final ResourceLocation TEXTURE_RED_BLINK = new ResourceLocation("iceandfire:textures/models/amphithere/amphithere_red_blink.png");
    public static final ResourceLocation TEXTURE_YELLOW = new ResourceLocation("iceandfire:textures/models/amphithere/amphithere_yellow.png");
    public static final ResourceLocation TEXTURE_YELLOW_BLINK = new ResourceLocation("iceandfire:textures/models/amphithere/amphithere_yellow_blink.png");

    public RenderAmphithere(EntityRendererProvider.Context context) {
        super(context, new ModelAmphithere(), 1.6F);
    }

    @Override
    protected void scale(@NotNull EntityAmphithere entity, PoseStack matrixStackIn, float partialTickTime) {
        matrixStackIn.scale(2.0F, 2.0F, 2.0F);

    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(EntityAmphithere amphithere) {
        switch (amphithere.getVariant()) {
            case 0:
                if (amphithere.isBlinking()) {
                    return TEXTURE_BLUE_BLINK;
                } else {
                    return TEXTURE_BLUE;
                }
            case 1:
                if (amphithere.isBlinking()) {
                    return TEXTURE_GREEN_BLINK;
                } else {
                    return TEXTURE_GREEN;
                }
            case 2:
                if (amphithere.isBlinking()) {
                    return TEXTURE_OLIVE_BLINK;
                } else {
                    return TEXTURE_OLIVE;
                }
            case 3:
                if (amphithere.isBlinking()) {
                    return TEXTURE_RED_BLINK;
                } else {
                    return TEXTURE_RED;
                }
            case 4:
                if (amphithere.isBlinking()) {
                    return TEXTURE_YELLOW_BLINK;
                } else {
                    return TEXTURE_YELLOW;
                }
        }
        return TEXTURE_GREEN;
    }

}
