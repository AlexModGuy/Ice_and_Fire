package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.client.model.ModelDreadGhoul;
import com.github.alexthe666.iceandfire.client.render.entity.layer.LayerGenericGlowing;
import com.github.alexthe666.iceandfire.entity.EntityDreadGhoul;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class RenderDreadGhoul extends MobRenderer<EntityDreadGhoul, ModelDreadGhoul> {

    public static final ResourceLocation TEXTURE_EYES = new ResourceLocation("iceandfire:textures/models/dread/dread_ghoul_eyes.png");

    public static final ResourceLocation TEXTURE_0 = new ResourceLocation("iceandfire:textures/models/dread/dread_ghoul_closed_1.png");
    public static final ResourceLocation TEXTURE_1 = new ResourceLocation("iceandfire:textures/models/dread/dread_ghoul_closed_2.png");
    public static final ResourceLocation TEXTURE_2 = new ResourceLocation("iceandfire:textures/models/dread/dread_ghoul_closed_3.png");
    public static final ResourceLocation TEXTURE_0_MID = new ResourceLocation("iceandfire:textures/models/dread/dread_ghoul_mid_1.png");
    public static final ResourceLocation TEXTURE_1_MID = new ResourceLocation("iceandfire:textures/models/dread/dread_ghoul_mid_2.png");
    public static final ResourceLocation TEXTURE_2_MID = new ResourceLocation("iceandfire:textures/models/dread/dread_ghoul_mid_3.png");
    public static final ResourceLocation TEXTURE_0_OPEN = new ResourceLocation("iceandfire:textures/models/dread/dread_ghoul_open_1.png");
    public static final ResourceLocation TEXTURE_1_OPEN = new ResourceLocation("iceandfire:textures/models/dread/dread_ghoul_open_2.png");
    public static final ResourceLocation TEXTURE_2_OPEN = new ResourceLocation("iceandfire:textures/models/dread/dread_ghoul_open_3.png");

    public RenderDreadGhoul(EntityRendererProvider.Context context) {
        super(context, new ModelDreadGhoul(0.0F), 0.5F);
        this.addLayer(new LayerGenericGlowing<>(this, TEXTURE_EYES));
    }


    @Override
    protected void scale(EntityDreadGhoul entity, PoseStack matrixStackIn, float partialTickTime) {
        float scale = entity.getSize() < 0.01F ? 1F : entity.getSize();
        matrixStackIn.scale(scale, scale, scale);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(EntityDreadGhoul ghoul) {
        if (ghoul.getScreamStage() == 2) {
            switch (ghoul.getVariant()) {
                case 1:
                    return TEXTURE_1_OPEN;
                case 2:
                    return TEXTURE_2_OPEN;
                default:
                    return TEXTURE_0_OPEN;
            }
        } else if (ghoul.getScreamStage() == 1) {
            switch (ghoul.getVariant()) {
                case 1:
                    return TEXTURE_1_MID;
                case 2:
                    return TEXTURE_2_MID;
                default:
                    return TEXTURE_0_MID;
            }
        } else {
            switch (ghoul.getVariant()) {
                case 1:
                    return TEXTURE_1;
                case 2:
                    return TEXTURE_2;
                default:
                    return TEXTURE_0;
            }
        }

    }

}
