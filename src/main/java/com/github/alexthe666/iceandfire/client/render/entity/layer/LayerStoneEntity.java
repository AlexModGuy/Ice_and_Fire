package com.github.alexthe666.iceandfire.client.render.entity.layer;

import com.github.alexthe666.iceandfire.entity.StoneEntityProperties;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

public class LayerStoneEntity implements LayerRenderer {

    private RenderLivingBase renderer;

    public LayerStoneEntity(RenderLivingBase renderer) {
        this.renderer = renderer;
    }

    @Override
    public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float f, float f1, float i, float f2, float f3, float f4, float f5) {
        if(entitylivingbaseIn instanceof EntityLiving) {
            StoneEntityProperties properties = EntityPropertiesHandler.INSTANCE.getProperties(entitylivingbaseIn, StoneEntityProperties.class);
            if(properties != null && properties.isStone){
                this.renderer.bindTexture(new ResourceLocation(getStoneType(renderer.getMainModel(), 1)));
                this.renderer.getMainModel().render(entitylivingbaseIn, f, 0, 0, f3, f4, f5);
            }
        }
    }

    public String getStoneType(ModelBase model, int size){
        int x = model.textureWidth;
        int y = model.textureHeight;
        int sizeX = Math.min(128, x * size);
        int sizeY = Math.min(128, y * size);
        String str = "iceandfire:textures/models/gorgon/stone" + sizeX + "x" + sizeY +".png";
        if(sizeX <= 16 && sizeY <= 16){
            return "textures/blocks/stone.png";
        }else{
            return str;
        }
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}
