package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.client.model.ModelCyclops;
import com.github.alexthe666.iceandfire.entity.EntityCyclops;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class RenderCyclops extends MobRenderer<EntityCyclops, ModelCyclops> {

    public static final ResourceLocation TEXTURE_0 = new ResourceLocation("iceandfire:textures/models/cyclops/cyclops_0.png");
    public static final ResourceLocation BLINK_0_TEXTURE = new ResourceLocation("iceandfire:textures/models/cyclops/cyclops_0_blink.png");
    public static final ResourceLocation BLINDED_0_TEXTURE = new ResourceLocation("iceandfire:textures/models/cyclops/cyclops_0_injured.png");
    public static final ResourceLocation TEXTURE_1 = new ResourceLocation("iceandfire:textures/models/cyclops/cyclops_1.png");
    public static final ResourceLocation BLINK_1_TEXTURE = new ResourceLocation("iceandfire:textures/models/cyclops/cyclops_1_blink.png");
    public static final ResourceLocation BLINDED_1_TEXTURE = new ResourceLocation("iceandfire:textures/models/cyclops/cyclops_1_injured.png");
    public static final ResourceLocation TEXTURE_2 = new ResourceLocation("iceandfire:textures/models/cyclops/cyclops_2.png");
    public static final ResourceLocation BLINK_2_TEXTURE = new ResourceLocation("iceandfire:textures/models/cyclops/cyclops_2_blink.png");
    public static final ResourceLocation BLINDED_2_TEXTURE = new ResourceLocation("iceandfire:textures/models/cyclops/cyclops_2_injured.png");
    public static final ResourceLocation TEXTURE_3 = new ResourceLocation("iceandfire:textures/models/cyclops/cyclops_3.png");
    public static final ResourceLocation BLINK_3_TEXTURE = new ResourceLocation("iceandfire:textures/models/cyclops/cyclops_3_blink.png");
    public static final ResourceLocation BLINDED_3_TEXTURE = new ResourceLocation("iceandfire:textures/models/cyclops/cyclops_3_injured.png");

    public RenderCyclops(EntityRendererProvider.Context context) {
        super(context, new ModelCyclops(), 1.6F);
    }

    @Override
    protected void scale(@NotNull EntityCyclops entity, PoseStack matrixStackIn, float partialTickTime) {
        matrixStackIn.scale(2.25F, 2.25F, 2.25F);

    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(EntityCyclops cyclops) {
        switch (cyclops.getVariant()) {
            case 0:
                if (cyclops.isBlinded()) {
                    return BLINDED_0_TEXTURE;
                } else if (cyclops.isBlinking()) {
                    return BLINK_0_TEXTURE;
                } else {
                    return TEXTURE_0;
                }
            case 1:
                if (cyclops.isBlinded()) {
                    return BLINDED_1_TEXTURE;
                } else if (cyclops.isBlinking()) {
                    return BLINK_1_TEXTURE;
                } else {
                    return TEXTURE_1;
                }
            case 2:
                if (cyclops.isBlinded()) {
                    return BLINDED_2_TEXTURE;
                } else if (cyclops.isBlinking()) {
                    return BLINK_2_TEXTURE;
                } else {
                    return TEXTURE_2;
                }
            case 3:
                if (cyclops.isBlinded()) {
                    return BLINDED_3_TEXTURE;
                } else if (cyclops.isBlinking()) {
                    return BLINK_3_TEXTURE;
                } else {
                    return TEXTURE_3;
                }
        }
        return TEXTURE_0;
    }

}
