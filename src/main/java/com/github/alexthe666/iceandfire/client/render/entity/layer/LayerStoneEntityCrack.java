package com.github.alexthe666.iceandfire.client.render.entity.layer;

import com.github.alexthe666.iceandfire.client.model.ICustomStatueModel;
import com.github.alexthe666.iceandfire.client.model.ModelGuardianStatue;
import com.github.alexthe666.iceandfire.client.model.ModelHorseStatue;
import com.github.alexthe666.iceandfire.client.model.ModelTroll;
import com.github.alexthe666.iceandfire.entity.StoneEntityProperties;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.passive.EntityLlama;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class LayerStoneEntityCrack implements LayerRenderer {

    protected static final ResourceLocation[] DESTROY_STAGES = new ResourceLocation[]{new ResourceLocation("textures/blocks/destroy_stage_0.png"), new ResourceLocation("textures/blocks/destroy_stage_1.png"), new ResourceLocation("textures/blocks/destroy_stage_2.png"), new ResourceLocation("textures/blocks/destroy_stage_3.png"), new ResourceLocation("textures/blocks/destroy_stage_4.png"), new ResourceLocation("textures/blocks/destroy_stage_5.png"), new ResourceLocation("textures/blocks/destroy_stage_6.png"), new ResourceLocation("textures/blocks/destroy_stage_7.png"), new ResourceLocation("textures/blocks/destroy_stage_8.png"), new ResourceLocation("textures/blocks/destroy_stage_9.png")};
    private static final ModelHorseStatue HORSE_MODEL = new ModelHorseStatue();
    private static final ModelGuardianStatue GUARDIAN_MODEL = new ModelGuardianStatue();
    private RenderLivingBase renderer;

    public LayerStoneEntityCrack(RenderLivingBase renderer) {
        this.renderer = renderer;
    }

    @Override
    public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float f, float f1, float i, float f2, float f3, float f4, float f5) {
        if (entitylivingbaseIn instanceof EntityLiving) {
            StoneEntityProperties properties = EntityPropertiesHandler.INSTANCE.getProperties(entitylivingbaseIn, StoneEntityProperties.class);
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
                    this.renderer.getMainModel().render(entitylivingbaseIn, f, 0, 0, f3, f4, f5);
                } else if (this.renderer.getMainModel() instanceof ICustomStatueModel) {
                    ((ICustomStatueModel) this.renderer.getMainModel()).renderStatue();
                } else if (entitylivingbaseIn instanceof AbstractHorse && !(entitylivingbaseIn instanceof EntityLlama)) {
                    HORSE_MODEL.render(entitylivingbaseIn, f, 0, 0, f3, f4, f5);
                } else if (entitylivingbaseIn instanceof EntityGuardian) {
                    GUARDIAN_MODEL.render(entitylivingbaseIn, f, 0, 0, f3, f4, f5);
                } else {
                    this.renderer.getMainModel().render(entitylivingbaseIn, f, 0, 0, f3, f4, f5);
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
}
