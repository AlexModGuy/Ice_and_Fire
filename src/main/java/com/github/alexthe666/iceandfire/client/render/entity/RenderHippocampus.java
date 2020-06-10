package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.client.model.ModelHippocampus;
import com.github.alexthe666.iceandfire.entity.EntityHippocampus;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public class RenderHippocampus extends MobRenderer<EntityHippocampus> {

    private static final ResourceLocation VARIANT_0 = new ResourceLocation("iceandfire:textures/models/hippocampus/hippocampus_0.png");
    private static final ResourceLocation VARIANT_0_BLINK = new ResourceLocation("iceandfire:textures/models/hippocampus/hippocampus_0_blinking.png");
    private static final ResourceLocation VARIANT_1 = new ResourceLocation("iceandfire:textures/models/hippocampus/hippocampus_1.png");
    private static final ResourceLocation VARIANT_1_BLINK = new ResourceLocation("iceandfire:textures/models/hippocampus/hippocampus_1_blinking.png");
    private static final ResourceLocation VARIANT_2 = new ResourceLocation("iceandfire:textures/models/hippocampus/hippocampus_2.png");
    private static final ResourceLocation VARIANT_2_BLINK = new ResourceLocation("iceandfire:textures/models/hippocampus/hippocampus_2_blinking.png");
    private static final ResourceLocation VARIANT_3 = new ResourceLocation("iceandfire:textures/models/hippocampus/hippocampus_3.png");
    private static final ResourceLocation VARIANT_3_BLINK = new ResourceLocation("iceandfire:textures/models/hippocampus/hippocampus_3_blinking.png");
    private static final ResourceLocation VARIANT_4 = new ResourceLocation("iceandfire:textures/models/hippocampus/hippocampus_4.png");
    private static final ResourceLocation VARIANT_4_BLINK = new ResourceLocation("iceandfire:textures/models/hippocampus/hippocampus_4_blinking.png");
    private static final ResourceLocation VARIANT_5 = new ResourceLocation("iceandfire:textures/models/hippocampus/hippocampus_5.png");
    private static final ResourceLocation VARIANT_5_BLINK = new ResourceLocation("iceandfire:textures/models/hippocampus/hippocampus_5_blinking.png");


    public RenderHippocampus(EntityRendererManager renderManager) {
        super(renderManager, new ModelHippocampus(), 0.8F);
        this.layerRenderers.add(new RenderHippocampus.LayerHippocampusSaddle(this));
        this.layerRenderers.add(new RenderHippocampus.LayerHippocampusBridle(this));
        this.layerRenderers.add(new RenderHippocampus.LayerHippocampusChest(this));
        this.layerRenderers.add(new RenderHippocampus.LayerHippocampusRainbow(this));
        this.layerRenderers.add(new RenderHippocampus.LayerHippocampusArmor(this));
    }

    @Nullable
    @Override
    public ResourceLocation getEntityTexture(EntityHippocampus entity) {
        switch (entity.getVariant()) {
            default:
                return entity.isBlinking() ? VARIANT_0_BLINK : VARIANT_0;
            case 1:
                return entity.isBlinking() ? VARIANT_1_BLINK : VARIANT_1;
            case 2:
                return entity.isBlinking() ? VARIANT_2_BLINK : VARIANT_2;
            case 3:
                return entity.isBlinking() ? VARIANT_3_BLINK : VARIANT_3;
            case 4:
                return entity.isBlinking() ? VARIANT_4_BLINK : VARIANT_4;
            case 5:
                return entity.isBlinking() ? VARIANT_5_BLINK : VARIANT_5;

        }
    }

    @OnlyIn(Dist.CLIENT)
    private class LayerHippocampusSaddle implements LayerRenderer {
        private final RenderHippocampus renderer;

        public LayerHippocampusSaddle(RenderHippocampus renderer) {
            this.renderer = renderer;
        }

        public void doRenderLayer(EntityHippocampus entity, float f, float f1, float i, float f2, float f3, float f4, float f5) {
            if (entity.isSaddled()) {
                this.renderer.bindTexture(new ResourceLocation("iceandfire:textures/models/hippocampus/saddle.png"));
                this.renderer.getMainModel().render(entity, f, f1, f2, f3, f4, f5);
            }
        }

        @Override
        public boolean shouldCombineTextures() {
            return false;
        }

        @Override
        public void doRenderLayer(LivingEntity entity, float f, float f1, float f2, float f3, float f4, float f5, float f6) {
            this.doRenderLayer((EntityHippocampus) entity, f, f1, f2, f3, f4, f5, f6);
        }
    }

    private class LayerHippocampusRainbow implements LayerRenderer {
        private final RenderHippocampus renderer;
        private final ResourceLocation TEXTURE = new ResourceLocation("iceandfire:textures/models/hippocampus/rainbow.png");
        private final ResourceLocation TEXTURE_BLINK = new ResourceLocation("iceandfire:textures/models/hippocampus/rainbow_blink.png");

        public LayerHippocampusRainbow(RenderHippocampus renderer) {
            this.renderer = renderer;
        }

        public void doRenderLayer(EntityHippocampus LivingEntityIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
            if (LivingEntityIn.hasCustomName() && LivingEntityIn.getCustomNameTag().toLowerCase().contains("rainbow")) {
                GL11.glPushMatrix();
                this.renderer.bindTexture(LivingEntityIn.isBlinking() ? TEXTURE_BLINK : TEXTURE);
                int i1 = 25;
                int i = LivingEntityIn.ticksExisted / 25 + LivingEntityIn.getEntityId();
                int j = EnumDyeColor.values().length;
                int k = i % j;
                int l = (i + 1) % j;
                float f = ((float) (LivingEntityIn.ticksExisted % 25) + partialTicks) / 25.0F;
                float[] afloat1 = EntitySheep.getDyeRgb(EnumDyeColor.byMetadata(k));
                float[] afloat2 = EntitySheep.getDyeRgb(EnumDyeColor.byMetadata(l));
                GlStateManager.color(afloat1[0] * (1.0F - f) + afloat2[0] * f, afloat1[1] * (1.0F - f) + afloat2[1] * f, afloat1[2] * (1.0F - f) + afloat2[2] * f);
                this.renderer.getMainModel().render(LivingEntityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
                GL11.glPopMatrix();
            }
        }

        @Override
        public boolean shouldCombineTextures() {
            return false;
        }

        @Override
        public void doRenderLayer(LivingEntity entity, float f, float f1, float f2, float f3, float f4, float f5, float f6) {
            this.doRenderLayer((EntityHippocampus) entity, f, f1, f2, f3, f4, f5, f6);
        }
    }


    @OnlyIn(Dist.CLIENT)
    private class LayerHippocampusBridle implements LayerRenderer {
        private final RenderHippocampus renderer;
        private final ResourceLocation TEXTURE = new ResourceLocation("iceandfire:textures/models/hippocampus/bridle.png");

        public LayerHippocampusBridle(RenderHippocampus renderer) {
            this.renderer = renderer;
        }

        public void doRenderLayer(EntityHippocampus entity, float f, float f1, float i, float f2, float f3, float f4, float f5) {
            if (entity.isSaddled() && entity.getControllingPassenger() != null) {
                this.renderer.bindTexture(TEXTURE);
                this.renderer.getMainModel().render(entity, f, f1, f2, f3, f4, f5);
            }
        }

        @Override
        public boolean shouldCombineTextures() {
            return false;
        }

        @Override
        public void doRenderLayer(LivingEntity entity, float f, float f1, float f2, float f3, float f4, float f5, float f6) {
            this.doRenderLayer((EntityHippocampus) entity, f, f1, f2, f3, f4, f5, f6);
        }
    }

    @OnlyIn(Dist.CLIENT)
    private class LayerHippocampusChest implements LayerRenderer {
        private final RenderHippocampus renderer;
        private final ResourceLocation TEXTURE = new ResourceLocation("iceandfire:textures/models/hippocampus/chest.png");

        public LayerHippocampusChest(RenderHippocampus renderer) {
            this.renderer = renderer;
        }

        public void doRenderLayer(EntityHippocampus entity, float f, float f1, float i, float f2, float f3, float f4, float f5) {
            if (entity.isChested()) {
                this.renderer.bindTexture(TEXTURE);
                this.renderer.getMainModel().render(entity, f, f1, f2, f3, f4, f5);
            }
        }

        @Override
        public boolean shouldCombineTextures() {
            return false;
        }

        @Override
        public void doRenderLayer(LivingEntity entity, float f, float f1, float f2, float f3, float f4, float f5, float f6) {
            this.doRenderLayer((EntityHippocampus) entity, f, f1, f2, f3, f4, f5, f6);
        }
    }

    @OnlyIn(Dist.CLIENT)
    private class LayerHippocampusArmor implements LayerRenderer {
        private final RenderHippocampus renderer;
        private final ResourceLocation TEXTURE_DIAMOND = new ResourceLocation("iceandfire:textures/models/hippocampus/armor_diamond.png");
        private final ResourceLocation TEXTURE_GOLD = new ResourceLocation("iceandfire:textures/models/hippocampus/armor_gold.png");
        private final ResourceLocation TEXTURE_IRON = new ResourceLocation("iceandfire:textures/models/hippocampus/armor_iron.png");

        public LayerHippocampusArmor(RenderHippocampus renderer) {
            this.renderer = renderer;
        }

        public void doRenderLayer(EntityHippocampus entity, float f, float f1, float i, float f2, float f3, float f4, float f5) {
            if (entity.getArmor() != 0) {
                GL11.glPushMatrix();
                switch (entity.getArmor()) {
                    case 1:
                        this.renderer.bindTexture(TEXTURE_IRON);
                        break;
                    case 2:
                        this.renderer.bindTexture(TEXTURE_GOLD);
                        break;
                    case 3:
                        this.renderer.bindTexture(TEXTURE_DIAMOND);
                        break;
                }
                GlStateManager.color(1F, 1F, 1F);
                this.renderer.getMainModel().render(entity, f, f1, f2, f3, f4, f5);
                GL11.glPopMatrix();
            }
        }

        @Override
        public boolean shouldCombineTextures() {
            return false;
        }

        @Override
        public void doRenderLayer(LivingEntity entity, float f, float f1, float f2, float f3, float f4, float f5, float f6) {
            this.doRenderLayer((EntityHippocampus) entity, f, f1, f2, f3, f4, f5, f6);
        }
    }
}
