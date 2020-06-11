package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.client.model.ModelStonePlayer;
import com.github.alexthe666.iceandfire.entity.EntityStoneStatue;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public class RenderStoneStatue extends LivingRenderer<EntityStoneStatue, ModelStonePlayer> {

    public static final ResourceLocation TEXTURE = new ResourceLocation("iceandfire:textures/models/gorgon/stone_player.png");
    private static final ModelStonePlayer MODEL = new ModelStonePlayer(0, false);
    private static final ModelStonePlayer MODEL_SLIM = new ModelStonePlayer(0, true);

    public RenderStoneStatue(EntityRendererManager renderManager) {
        super(renderManager, MODEL, 0.5F);
    }

    @Nullable
    @Override
    public ResourceLocation getEntityTexture(EntityStoneStatue entity) {
        return TEXTURE;
    }
}
