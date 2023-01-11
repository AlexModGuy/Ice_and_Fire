package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.iceandfire.client.model.ModelDragonEgg;
import com.github.alexthe666.iceandfire.entity.EntityMyrmexEgg;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class RenderMyrmexEgg extends LivingEntityRenderer<EntityMyrmexEgg, AdvancedEntityModel<EntityMyrmexEgg>> {

    public static final ResourceLocation EGG_JUNGLE = new ResourceLocation("iceandfire:textures/models/myrmex/myrmex_jungle_egg.png");
    public static final ResourceLocation EGG_DESERT = new ResourceLocation("iceandfire:textures/models/myrmex/myrmex_desert_egg.png");

    public RenderMyrmexEgg(EntityRendererProvider.Context context) {
        super(context, new ModelDragonEgg(), 0.3F);
    }

    @Override
    protected boolean shouldShowName(EntityMyrmexEgg entity) {
        return entity.shouldShowName() && entity.hasCustomName();
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(EntityMyrmexEgg entity) {
        return entity.isJungle() ? EGG_JUNGLE : EGG_DESERT;
    }

}
