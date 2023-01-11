package com.github.alexthe666.iceandfire.client.render.entity.layer;

import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.iceandfire.entity.EntitySeaSerpent;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class LayerSeaSerpentAncient extends RenderLayer<EntitySeaSerpent, AdvancedEntityModel<EntitySeaSerpent>> {

    private static final ResourceLocation TEXTURE = new ResourceLocation("iceandfire:textures/models/seaserpent/ancient_overlay.png");
    private static final ResourceLocation TEXTURE_BLINK = new ResourceLocation("iceandfire:textures/models/seaserpent/ancient_overlay_blink.png");

    public LayerSeaSerpentAncient(MobRenderer<EntitySeaSerpent, AdvancedEntityModel<EntitySeaSerpent>> renderer) {
        super(renderer);
    }

    @Override
    public void render(@NotNull PoseStack matrixStackIn, @NotNull MultiBufferSource bufferIn, int packedLightIn, EntitySeaSerpent serpent, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (serpent.isAncient()) {
            RenderType tex = RenderType.entityNoOutline(serpent.isBlinking() ? TEXTURE_BLINK : TEXTURE);
            VertexConsumer ivertexbuilder = bufferIn.getBuffer(tex);
            this.getParentModel().renderToBuffer(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

        }
    }
}
