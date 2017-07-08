package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.entity.EntityHippogryph;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class RenderHippogryph extends RenderLiving<EntityHippogryph> {

    public RenderHippogryph(RenderManager renderManager, ModelBase model) {
        super(renderManager, model, 0.8F);
        this.layerRenderers.add(new LayerHippogriffSaddle(this));
        this.layerRenderers.add(new LayerHippogriffBridle(this));
        this.layerRenderers.add(new LayerHippogriffChest(this));
        this.layerRenderers.add(new LayerHippogriffArmor(this));

    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(EntityHippogryph entity) {
        return new ResourceLocation("iceandfire:textures/models/hippogryph/" + entity.getEnumVariant().name().toLowerCase() + ".png");
    }

    @SideOnly(Side.CLIENT)
    private class LayerHippogriffSaddle implements LayerRenderer {
        private final RenderHippogryph renderer;

        public LayerHippogriffSaddle(RenderHippogryph renderer) {
            this.renderer = renderer;
        }

        public void doRenderLayer(EntityHippogryph entity, float f, float f1, float i, float f2, float f3, float f4, float f5) {
            if(entity.isSaddled()){
                this.renderer.bindTexture(new ResourceLocation("iceandfire:textures/models/hippogryph/saddle.png"));
                this.renderer.getMainModel().render(entity, f, f1, f2, f3, f4, f5);
            }
        }

        @Override
        public boolean shouldCombineTextures() {
            return false;
        }

        @Override
        public void doRenderLayer(EntityLivingBase entity, float f, float f1, float f2, float f3, float f4, float f5, float f6) {
            this.doRenderLayer((EntityHippogryph) entity, f, f1, f2, f3, f4, f5, f6);
        }
    }

    @SideOnly(Side.CLIENT)
    private class LayerHippogriffBridle implements LayerRenderer {
        private final RenderHippogryph renderer;

        public LayerHippogriffBridle(RenderHippogryph renderer) {
            this.renderer = renderer;
        }

        public void doRenderLayer(EntityHippogryph entity, float f, float f1, float i, float f2, float f3, float f4, float f5) {
            if(entity.isSaddled() && entity.getControllingPassenger() != null){
                this.renderer.bindTexture(new ResourceLocation("iceandfire:textures/models/hippogryph/bridle.png"));
                this.renderer.getMainModel().render(entity, f, f1, f2, f3, f4, f5);
            }
        }

        @Override
        public boolean shouldCombineTextures() {
            return false;
        }

        @Override
        public void doRenderLayer(EntityLivingBase entity, float f, float f1, float f2, float f3, float f4, float f5, float f6) {
            this.doRenderLayer((EntityHippogryph) entity, f, f1, f2, f3, f4, f5, f6);
        }
    }

    @SideOnly(Side.CLIENT)
    private class LayerHippogriffChest implements LayerRenderer {
        private final RenderHippogryph renderer;

        public LayerHippogriffChest(RenderHippogryph renderer) {
            this.renderer = renderer;
        }

        public void doRenderLayer(EntityHippogryph entity, float f, float f1, float i, float f2, float f3, float f4, float f5) {
            if(entity.isChested()){
                this.renderer.bindTexture(new ResourceLocation("iceandfire:textures/models/hippogryph/chest.png"));
                this.renderer.getMainModel().render(entity, f, f1, f2, f3, f4, f5);
            }
        }

        @Override
        public boolean shouldCombineTextures() {
            return false;
        }

        @Override
        public void doRenderLayer(EntityLivingBase entity, float f, float f1, float f2, float f3, float f4, float f5, float f6) {
            this.doRenderLayer((EntityHippogryph) entity, f, f1, f2, f3, f4, f5, f6);
        }
    }

    @SideOnly(Side.CLIENT)
    private class LayerHippogriffArmor implements LayerRenderer {
        private final RenderHippogryph renderer;

        public LayerHippogriffArmor(RenderHippogryph renderer) {
            this.renderer = renderer;
        }

        public void doRenderLayer(EntityHippogryph entity, float f, float f1, float i, float f2, float f3, float f4, float f5) {
            if(entity.getArmor() != 0){
                this.renderer.bindTexture(new ResourceLocation("iceandfire:textures/models/hippogryph/armor_" + (entity.getArmor() != 1 ?  entity.getArmor() != 2 ? "diamond" : "gold" : "iron") + ".png"));
                this.renderer.getMainModel().render(entity, f, f1, f2, f3, f4, f5);
            }
        }

        @Override
        public boolean shouldCombineTextures() {
            return false;
        }

        @Override
        public void doRenderLayer(EntityLivingBase entity, float f, float f1, float f2, float f3, float f4, float f5, float f6) {
            this.doRenderLayer((EntityHippogryph) entity, f, f1, f2, f3, f4, f5, f6);
        }
    }
}
