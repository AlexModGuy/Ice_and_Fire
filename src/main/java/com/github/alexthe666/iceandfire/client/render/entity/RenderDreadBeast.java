package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.client.model.ModelDreadBeast;
import com.github.alexthe666.iceandfire.client.render.entity.layer.LayerGenericGlowing;
import com.github.alexthe666.iceandfire.entity.EntityDreadBeast;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class RenderDreadBeast extends MobRenderer<EntityDreadBeast, ModelDreadBeast> {

    public static final ResourceLocation TEXTURE_EYES = new ResourceLocation("iceandfire:textures/models/dread/dread_beast_eyes.png");
    public static final ResourceLocation TEXTURE_0 = new ResourceLocation("iceandfire:textures/models/dread/dread_beast_1.png");
    public static final ResourceLocation TEXTURE_1 = new ResourceLocation("iceandfire:textures/models/dread/dread_beast_2.png");

    public RenderDreadBeast(EntityRendererProvider.Context context) {
        super(context, new ModelDreadBeast(), 0.5F);
        this.addLayer(new LayerGenericGlowing(this, TEXTURE_EYES));
    }

    @Override
    protected void scale(EntityDreadBeast entity, PoseStack matrixStackIn, float partialTickTime) {
        matrixStackIn.scale(entity.getSize(), entity.getSize(), entity.getSize());
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(EntityDreadBeast beast) {
        return beast.getVariant() == 1 ? TEXTURE_1 : TEXTURE_0;

    }

}
