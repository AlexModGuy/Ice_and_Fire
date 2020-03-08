package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.client.render.entity.layer.*;
import com.github.alexthe666.iceandfire.client.texture.ArrayLayeredTexture;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.enums.EnumDragonTextures;
import com.google.common.collect.Maps;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SideOnly(Side.CLIENT)
public class RenderBlackFrostDragon extends RenderLiving<EntityDragonBase> {

    public static final ResourceLocation TEXTURE = new ResourceLocation("iceandfire:textures/models/dread/black_frost.png");
    public static final ResourceLocation TEXTURE_EYES = new ResourceLocation("iceandfire:textures/models/dread/black_frost_eyes.png");

    public RenderBlackFrostDragon(RenderManager renderManager, ModelBase model, boolean fire) {
        super(renderManager, model, 0.8F);
        this.addLayer(new LayerGenericGlowing(this, TEXTURE_EYES));
        this.addLayer(new LayerDragonRider(this, true));
        this.addLayer(new LayerDragonBanner(this));
    }

    public boolean shouldRender(EntityDragonBase dragon, ICamera camera, double camX, double camY, double camZ) {
        return true;
        //return super.shouldRender(dragon, camera, camX, camY, camZ) ||  dragon.shouldRender(camera)|| Minecraft.getMinecraft().player.isRidingOrBeingRiddenBy(dragon);
    }

    @Override
    protected void preRenderCallback(EntityDragonBase entity, float f) {
        this.shadowSize = entity.getRenderSize() / 3;
        GL11.glScalef(shadowSize, shadowSize, shadowSize);
        float f7 = entity.prevDragonPitch + (entity.getDragonPitch() - entity.prevDragonPitch) * f;
        GL11.glRotatef(f7, 1, 0, 0);
    }


    protected ResourceLocation getEntityTexture(EntityDragonBase entity) {
        return TEXTURE;
    }
}
