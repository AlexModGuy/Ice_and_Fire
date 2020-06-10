package com.github.alexthe666.iceandfire.client.render.entity.layer;

import com.github.alexthe666.citadel.server.entity.EntityPropertiesHandler;
import com.github.alexthe666.iceandfire.client.model.ICustomStatueModel;
import com.github.alexthe666.iceandfire.client.model.ModelGuardianStatue;
import com.github.alexthe666.iceandfire.client.model.ModelHorseStatue;
import com.github.alexthe666.iceandfire.client.model.ModelTroll;
import com.github.alexthe666.iceandfire.entity.props.StoneEntityProperties;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.GuardianEntity;
import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import net.minecraft.entity.passive.horse.LlamaEntity;
import net.minecraft.util.ResourceLocation;

public class LayerStoneEntityCrack<T extends Entity, M extends EntityModel<T>> extends LayerRenderer<T, M> {

    protected static final ResourceLocation[] DESTROY_STAGES = new ResourceLocation[]{new ResourceLocation("textures/blocks/destroy_stage_0.png"), new ResourceLocation("textures/blocks/destroy_stage_1.png"), new ResourceLocation("textures/blocks/destroy_stage_2.png"), new ResourceLocation("textures/blocks/destroy_stage_3.png"), new ResourceLocation("textures/blocks/destroy_stage_4.png"), new ResourceLocation("textures/blocks/destroy_stage_5.png"), new ResourceLocation("textures/blocks/destroy_stage_6.png"), new ResourceLocation("textures/blocks/destroy_stage_7.png"), new ResourceLocation("textures/blocks/destroy_stage_8.png"), new ResourceLocation("textures/blocks/destroy_stage_9.png")};
    private static final ModelHorseStatue HORSE_MODEL = new ModelHorseStatue();
    private static final ModelGuardianStatue GUARDIAN_MODEL = new ModelGuardianStatue();
    private IEntityRenderer<T, M> renderer;

    public LayerStoneEntityCrack(IEntityRenderer<T, M> entityRendererIn) {
        super(entityRendererIn);
        this.renderer = renderer;
    }

    @Override
    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, Entity living, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (living instanceof LivingEntity) {
            StoneEntityProperties properties = EntityPropertiesHandler.INSTANCE.getProperties(living, StoneEntityProperties.class);
            if (properties != null && properties.isStone) {
                float x = Math.max(this.renderer.getEntityModel().textureWidth, 1) / 16F; //default to 4
                float y = Math.max(this.renderer.getEntityModel().textureHeight, 1) / 16F; //default to 2
                RenderType tex = RenderType.getEntitySolid(DESTROY_STAGES[properties.breakLvl - 1]);

                GlStateManager.matrixMode(5890);
                GlStateManager.loadIdentity();
                GlStateManager.scalef(x, y, 1);
                GlStateManager.matrixMode(5888);
                IVertexBuilder ivertexbuilder = bufferIn.getBuffer(tex);
                if (this.renderer.getEntityModel() instanceof ICustomStatueModel) {
                    ((ICustomStatueModel) this.renderer.getEntityModel()).renderStatue();
                } else if (living instanceof AbstractHorseEntity && !(living instanceof LlamaEntity)) {
                    HORSE_MODEL.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
                } else if (living instanceof GuardianEntity) {
                    GUARDIAN_MODEL.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
                } else {
                    this.renderer.getEntityModel().render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
                }

                GlStateManager.matrixMode(5890);
                GlStateManager.loadIdentity();
                GlStateManager.matrixMode(5888);
            }
        }
    }
}
