package com.github.alexthe666.iceandfire.client.render.entity.layer;

import com.github.alexthe666.citadel.client.model.TabulaModel;
import com.github.alexthe666.citadel.server.entity.EntityPropertiesHandler;
import com.github.alexthe666.iceandfire.client.model.ICustomStatueModel;
import com.github.alexthe666.iceandfire.client.model.ModelGuardianStatue;
import com.github.alexthe666.iceandfire.client.model.ModelHorseStatue;
import com.github.alexthe666.iceandfire.client.render.IafRenderType;
import com.github.alexthe666.iceandfire.entity.props.StoneEntityProperties;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.GuardianEntity;

public class LayerStoneEntity extends LayerRenderer {

    private static final ModelHorseStatue HORSE_MODEL = new ModelHorseStatue();
    private static final ModelGuardianStatue GUARDIAN_MODEL = new ModelGuardianStatue();
    private LivingRenderer renderer;

    public LayerStoneEntity(LivingRenderer renderer) {
        super(renderer);
        this.renderer = renderer;
    }

    @Override
    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, Entity living, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (living instanceof LivingEntity) {
            StoneEntityProperties properties = EntityPropertiesHandler.INSTANCE.getProperties(living, StoneEntityProperties.class);
            if (properties != null && properties.isStone()) {
                float x = Math.max(this.renderer.getEntityModel().textureWidth, 1) / 16F; //default to 4
                float y = Math.max(this.renderer.getEntityModel().textureHeight, 1) / 16F; //default to 2
                RenderType tex = IafRenderType.getStoneMobRenderType(x, y);

                IVertexBuilder ivertexbuilder = bufferIn.getBuffer(tex);
                if(this.renderer.getEntityModel() instanceof TabulaModel){
                    ((TabulaModel) this.renderer.getEntityModel()).resetToDefaultPose();
                }
                if (this.renderer.getEntityModel() instanceof ICustomStatueModel) {
                    ((ICustomStatueModel) this.renderer.getEntityModel()).renderStatue(matrixStackIn, ivertexbuilder, packedLightIn, living);
                } else if (living instanceof GuardianEntity) {
                    GUARDIAN_MODEL.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
                } else {
                    this.renderer.getEntityModel().render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
                }

            }
        }
    }
}
