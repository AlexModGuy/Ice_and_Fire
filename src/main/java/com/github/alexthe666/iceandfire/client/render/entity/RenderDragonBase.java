package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.client.render.entity.layer.LayerDragonBanner;
import com.github.alexthe666.iceandfire.client.render.entity.layer.LayerDragonEyes;
import com.github.alexthe666.iceandfire.client.render.entity.layer.LayerDragonRider;
import com.github.alexthe666.iceandfire.client.render.entity.layer.LayerDragonArmor;
import com.github.alexthe666.iceandfire.client.texture.ArrayLayeredTexture;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.enums.EnumDragonTextures;
import com.google.common.collect.Maps;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.culling.ICamera;
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
public class RenderDragonBase extends RenderLiving<EntityDragonBase> {

    private Map<String, ResourceLocation> LAYERED_TEXTURE_CACHE = Maps.newHashMap();
    private boolean fire;

    public RenderDragonBase(RenderManager renderManager, ModelBase model, boolean fire) {
        super(renderManager, model, 0.8F);
        this.addLayer(new LayerDragonEyes(this));
        this.addLayer(new LayerDragonRider(this, false));
        this.addLayer(new LayerDragonBanner(this));
        this.addLayer(new LayerDragonArmor(this, fire));
        this.fire = fire;
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
        String baseTexture = entity.getVariantName(entity.getVariant()) + " " + entity.getDragonStage() + entity.isModelDead() + entity.isMale() + entity.isSkeletal() + entity.isSleeping() + entity.isBlinking();
        ResourceLocation resourcelocation = LAYERED_TEXTURE_CACHE.get(baseTexture);
        if (resourcelocation == null) {
            resourcelocation = new ResourceLocation("iceandfire:" + "dragonTexture_" + baseTexture);
            List<String> tex = new ArrayList<String>();
            tex.add(EnumDragonTextures.getTextureFromDragon(entity).toString());
            if (entity.isMale() && !entity.isSkeletal()) {
                if (fire) {
                    tex.add(EnumDragonTextures.getDragonEnum(entity).FIRE_MALE_OVERLAY.toString());
                } else {
                    tex.add(EnumDragonTextures.getDragonEnum(entity).ICE_MALE_OVERLAY.toString());
                }
            }else{
                tex.add(EnumDragonTextures.Armor.EMPTY.FIRETEXTURE.toString());

            }
            ArrayLayeredTexture layeredBase = new ArrayLayeredTexture(tex);
            Minecraft.getMinecraft().getTextureManager().loadTexture(resourcelocation, layeredBase);
            LAYERED_TEXTURE_CACHE.put(baseTexture, resourcelocation);
        }
        return resourcelocation;
    }
}
