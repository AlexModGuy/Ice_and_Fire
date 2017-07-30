package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.entity.EntityStoneStatue;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;

public class RenderStoneStatue extends Render<EntityStoneStatue> {


    public RenderStoneStatue(RenderManager renderManager) {
        super(renderManager);
    }

    @Override
    public void doRender(EntityStoneStatue entity, double x, double y, double z, float entityYaw, float partialTicks){
        if(entity.getClass() != null) {
            ModelBase model = null;
            Render render = this.renderManager.getEntityClassRenderObject(entity.getModel().getClass());
            if (render instanceof RenderLiving && entity.getModel() != null && entity.getModel() instanceof EntityLiving) {
                RenderLiving renderLiving = (RenderLiving) render;
                GL11.glPushMatrix();
                this.bindTexture(new ResourceLocation(getStoneType(renderLiving.getMainModel(), 1)));
                if(renderLiving.getMainModel() != null) {
                    GL11.glTranslated(x, y, z);
                    GL11.glRotatef(entity.rotationYaw, 0, 1, 0);
                    renderLiving.getMainModel().swingProgress = entity.getSwingProgress(1);
                    renderLiving.prepareScale((EntityLiving)entity.getModel(), 0);
                    renderLiving.getMainModel().render(entity.getModel(), 0, 0, 0, 0, 0, 0.0625F);
                    renderLiving.getMainModel().setRotationAngles(0, 0, 0, 0, 0, 0, entity.getModel());
                }
                GL11.glPopMatrix();
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


    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(EntityStoneStatue entity) {
        return new ResourceLocation("textures/blocks/stone.png");
    }
}
