package com.github.alexthe666.iceandfire.client.render.entity.layer;

import com.github.alexthe666.iceandfire.client.model.ICustomStatueModel;
import com.github.alexthe666.iceandfire.client.model.ModelGuardianStatue;
import com.github.alexthe666.iceandfire.client.model.ModelHorseStatue;
import com.github.alexthe666.iceandfire.entity.StoneEntityProperties;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.passive.EntityLlama;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class LayerStoneEntity implements LayerRenderer {

    private static final ModelHorseStatue HORSE_MODEL = new ModelHorseStatue();
    private static final ModelGuardianStatue GUARDIAN_MODEL = new ModelGuardianStatue();
    private RenderLivingBase renderer;

    public LayerStoneEntity(RenderLivingBase renderer) {
        this.renderer = renderer;
    }

    @Override
    public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float f, float f1, float i, float f2, float f3, float f4, float f5) {
        if (entitylivingbaseIn instanceof EntityLiving) {
            StoneEntityProperties properties = EntityPropertiesHandler.INSTANCE.getProperties(entitylivingbaseIn, StoneEntityProperties.class);
            if (properties != null && properties.isStone) {
                GL11.glEnable(GL11.GL_CULL_FACE);
                this.renderer.bindTexture(new ResourceLocation(getStoneType(renderer.getMainModel(), 1)));
                if (this.renderer.getMainModel() instanceof ICustomStatueModel) {
                    ((ICustomStatueModel) this.renderer.getMainModel()).renderStatue();
                } else if (entitylivingbaseIn instanceof AbstractHorse && !(entitylivingbaseIn instanceof EntityLlama)) {
                    HORSE_MODEL.render(entitylivingbaseIn, f, 0, 0, f3, f4, f5);
                } else if (entitylivingbaseIn instanceof EntityGuardian) {
                    GUARDIAN_MODEL.render(entitylivingbaseIn, f, 0, 0, f3, f4, f5);
                } else {
                    this.renderer.getMainModel().render(entitylivingbaseIn, f, 0, 0, f3, f4, f5);
                }
                GL11.glDisable(GL11.GL_CULL_FACE);
            }
        }
    }

    public String getStoneType(ModelBase model, int size) {
        int x = model.textureWidth;
        int y = model.textureHeight;

        int sizeX = clampToMultipleOfFour(Math.min(128, x * size));
        int sizeY = clampToMultipleOfFour(Math.min(128, y * size));
        String str = "iceandfire:textures/models/gorgon/stone" + sizeX + "x" + sizeY + ".png";
        if (sizeX <= 16 && sizeY <= 16) {
            return "textures/blocks/stone.png";
        } else {
            return str;
        }
    }

    public int clampToMultipleOfFour(int i) {
        if (i % 4 != 0) {//usually 86
            if (i > 128) {
                return 128;
            } else if (i > 64) {
                return 64;
            } else if (i > 32) {
                return 32;
            } else {
                return 16;
            }
        } else {
            return i;
        }
    }

    @Override
    public boolean shouldCombineTextures() {
        return true;
    }
}
