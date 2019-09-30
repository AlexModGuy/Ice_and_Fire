package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.client.render.entity.layer.LayerDragonEyes;
import com.github.alexthe666.iceandfire.client.render.entity.layer.LayerDragonRider;
import com.github.alexthe666.iceandfire.client.texture.ArrayLayeredTexture;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.EntityIceDragon;
import com.github.alexthe666.iceandfire.enums.EnumDragonTextures;
import com.google.common.collect.Maps;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.LayeredTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SideOnly(Side.CLIENT)
public class RenderDragonBase extends RenderLiving<EntityDragonBase> {

    private static final Map<String, ResourceLocation> LAYERED_LOCATION_CACHE = Maps.<String, ResourceLocation>newHashMap();

    public RenderDragonBase(RenderManager renderManager, ModelBase model, boolean fire) {
        super(renderManager, model, 0.8F);
        this.addLayer(new LayerDragonEyes(this));
        this.addLayer(new LayerDragonRider(this));

    }

    public boolean shouldRender(EntityDragonBase dragon, ICamera camera, double camX, double camY, double camZ) {
        return true;
        //return super.shouldRender(dragon, camera, camX, camY, camZ) ||  dragon.shouldRender(camera)|| Minecraft.getMinecraft().player.isRidingOrBeingRiddenBy(dragon);
    }

    @Override
    protected void preRenderCallback(EntityDragonBase entity, float f) {
        this.shadowSize = ((EntityDragonBase) entity).getRenderSize() / 3;
        GL11.glScalef(shadowSize, shadowSize, shadowSize);
        float f7 = entity.prevDragonPitch + (entity.dragonPitch - entity.prevDragonPitch) * f;
        GL11.glRotatef(f7, 1, 0, 0);
    }

    protected ResourceLocation getEntityTexture(EntityDragonBase entity) {
        String armorString = "ARMOR{Head=" + entity.getArmorInSlot(0) + ", Neck=" + entity.getArmorInSlot(1) + ", Body=" + entity.getArmorInSlot(2) + " Tail=" + entity.getArmorInSlot(3) + "}";
        String dragonOverallTexture = entity.getVariantName(entity.getVariant()) + " " + entity.getDragonStage() + armorString
               + entity.isModelDead() + entity.isMale() + entity.isSkeletal() + entity.isSleeping() + entity.isBlinking();
        ResourceLocation resourcelocation = LAYERED_LOCATION_CACHE.get(dragonOverallTexture);
        if (resourcelocation == null) {
            resourcelocation = EnumDragonTextures.getTextureFromDragon(entity);
            List<String> tex = new ArrayList<String>();
            boolean ice = entity instanceof EntityIceDragon;
            tex.add(resourcelocation.toString());
            if(entity.isMale() && !entity.isSkeletal()){
                if (ice) {
                    tex.add(EnumDragonTextures.getDragonEnum(entity).ICE_MALE_OVERLAY.toString());
                } else {
                    tex.add(EnumDragonTextures.getDragonEnum(entity).FIRE_MALE_OVERLAY.toString());
                }
            }
            for(int slot = 0; slot <= 3; slot++) {
                if (entity.getArmorInSlot(slot) != 0) {
                    if (ice) {
                        tex.add(EnumDragonTextures.Armor.getArmorForDragon(entity, slot).ICETEXTURE.toString());
                    } else {
                        tex.add(EnumDragonTextures.Armor.getArmorForDragon(entity, slot).FIRETEXTURE.toString());
                    }
                }
            }
            Minecraft.getMinecraft().getTextureManager().loadTexture(resourcelocation, new ArrayLayeredTexture(tex));
            LAYERED_LOCATION_CACHE.put(dragonOverallTexture, resourcelocation);
        }
        return resourcelocation;
    }
}
