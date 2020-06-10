package com.github.alexthe666.iceandfire.client.render.entity.layer;

import com.github.alexthe666.iceandfire.client.model.ICustomStatueModel;
import com.github.alexthe666.iceandfire.client.model.ModelGuardianStatue;
import com.github.alexthe666.iceandfire.client.model.ModelHorseStatue;
import com.github.alexthe666.iceandfire.client.model.ModelTroll;
import com.github.alexthe666.iceandfire.entity.props.StoneEntityProperties;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.MobRendererBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.passive.EntityLlama;
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
    public void render(LivingEntity LivingEntityIn, float f, float f1, float i, float f2, float f3, float f4, float f5) {
        if (LivingEntityIn instanceof LivingEntity) {
            StoneEntityProperties properties = EntityPropertiesHandler.INSTANCE.getProperties(LivingEntityIn, StoneEntityProperties.class);
            if (properties != null && properties.isStone && properties.breakLvl > 0) {
                float x = Math.max(this.renderer.getMainModel().textureWidth, 1) / 16F; //default to 4
                float y = Math.max(this.renderer.getMainModel().textureHeight, 1) / 16F; //default to 2

                GlStateManager.enableBlend();
                GlStateManager.enableCull();
                GlStateManager.disableAlpha();
                GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
                GlStateManager.depthMask(true);

                this.renderer.bindTexture(DESTROY_STAGES[properties.breakLvl - 1]);
                GlStateManager.matrixMode(5890);
                GlStateManager.loadIdentity();
                GlStateManager.scale(x, y, 1);
                GlStateManager.matrixMode(5888);

                if (this.renderer.getMainModel() instanceof ModelTroll) {
                    this.renderer.getMainModel().render(LivingEntityIn, f, 0, 0, f3, f4, f5);
                } else if (this.renderer.getMainModel() instanceof ICustomStatueModel) {
                    ((ICustomStatueModel) this.renderer.getMainModel()).renderStatue();
                } else if (LivingEntityIn instanceof AbstractHorse && !(LivingEntityIn instanceof EntityLlama)) {
                    HORSE_MODEL.render(LivingEntityIn, f, 0, 0, f3, f4, f5);
                } else if (LivingEntityIn instanceof EntityGuardian) {
                    GUARDIAN_MODEL.render(LivingEntityIn, f, 0, 0, f3, f4, f5);
                } else {
                    this.renderer.getMainModel().render(LivingEntityIn, f, 0, 0, f3, f4, f5);
                }
                GlStateManager.disableBlend();
                GlStateManager.matrixMode(5890);
                GlStateManager.loadIdentity();
                GlStateManager.matrixMode(5888);

                GlStateManager.disableBlend();
                GlStateManager.disableCull();
                GlStateManager.enableAlpha();
            }
        }
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }

    @Override
    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {

    }
}
