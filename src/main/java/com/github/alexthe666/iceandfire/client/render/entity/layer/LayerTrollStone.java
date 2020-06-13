package com.github.alexthe666.iceandfire.client.render.entity.layer;

import com.github.alexthe666.citadel.server.entity.EntityPropertiesHandler;
import com.github.alexthe666.iceandfire.client.model.ModelTroll;
import com.github.alexthe666.iceandfire.entity.EntityGorgon;
import com.github.alexthe666.iceandfire.entity.EntityTroll;
import com.github.alexthe666.iceandfire.entity.props.StoneEntityProperties;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;

public class LayerTrollStone extends LayerRenderer<EntityTroll, ModelTroll> {

    private LivingRenderer<EntityTroll, ModelTroll> renderer;

    public LayerTrollStone(LivingRenderer<EntityTroll, ModelTroll> renderer) {
        super(renderer);
        this.renderer = renderer;
    }

    @Override
    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, EntityTroll troll, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        StoneEntityProperties properties = EntityPropertiesHandler.INSTANCE.getProperties(troll, StoneEntityProperties.class);
        if (properties != null && properties.isStone()) {
            RenderType tex = RenderType.getEntityCutout(troll.getTrollType().TEXTURE_STONE);
            IVertexBuilder ivertexbuilder = bufferIn.getBuffer(tex);
            this.getEntityModel().renderStatue(matrixStackIn, ivertexbuilder, packedLightIn, troll);

        }
    }
}
