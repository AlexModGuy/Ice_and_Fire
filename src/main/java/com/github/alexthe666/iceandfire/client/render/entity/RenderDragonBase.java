package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.client.render.entity.layer.LayerDragonArmor;
import com.github.alexthe666.iceandfire.client.render.entity.layer.LayerDragonBanner;
import com.github.alexthe666.iceandfire.client.render.entity.layer.LayerDragonEyes;
import com.github.alexthe666.iceandfire.client.render.entity.layer.LayerDragonRider;
import com.github.alexthe666.iceandfire.client.texture.ArrayLayeredTexture;
import com.github.alexthe666.iceandfire.entity.EntityCockatrice;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.enums.EnumDragonTextures;
import com.google.common.collect.Maps;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.culling.ClippingHelperImpl;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class RenderDragonBase extends MobRenderer<EntityDragonBase, SegmentedModel<EntityDragonBase>> {

    private Map<String, ResourceLocation> LAYERED_TEXTURE_CACHE = Maps.newHashMap();
    private int dragonType;

    public RenderDragonBase(EntityRendererManager manager, SegmentedModel model, int dragonType) {
        super(manager, model, 0.15F);
        this.addLayer(new LayerDragonEyes(this));
        this.addLayer(new LayerDragonRider(this, false));
        this.addLayer(new LayerDragonBanner(this));
        this.addLayer(new LayerDragonArmor(this, dragonType));
        this.dragonType = dragonType;
    }

    private Vec3d getPosition(LivingEntity LivingEntityIn, double p_177110_2_, float p_177110_4_) {
        double d0 = LivingEntityIn.lastTickPosX + (LivingEntityIn.getPosX() - LivingEntityIn.lastTickPosX) * (double) p_177110_4_;
        double d1 = p_177110_2_ + LivingEntityIn.lastTickPosY + (LivingEntityIn.getPosY() - LivingEntityIn.lastTickPosY) * (double) p_177110_4_;
        double d2 = LivingEntityIn.lastTickPosZ + (LivingEntityIn.getPosZ() - LivingEntityIn.lastTickPosZ) * (double) p_177110_4_;
        return new Vec3d(d0, d1, d2);
    }


    @Override
    protected void preRenderCallback(EntityDragonBase entity, MatrixStack matrixStackIn, float partialTickTime) {
        this.shadowSize = entity.getRenderSize() / 3;
        matrixStackIn.scale(shadowSize, shadowSize, shadowSize);
        float f7 = entity.prevDragonPitch + (entity.getDragonPitch() - entity.prevDragonPitch) * partialTickTime;
        matrixStackIn.rotate(new Quaternion(Vector3f.XP, f7, true));
    }


    public ResourceLocation getEntityTexture(EntityDragonBase entity) {
        String baseTexture = entity.getVariantName(entity.getVariant()) + entity.getDragonStage() + entity.isModelDead() + entity.isMale() + entity.isSkeletal() + entity.isSleeping() + entity.isBlinking();
        ResourceLocation resourcelocation = LAYERED_TEXTURE_CACHE.get(baseTexture);
        if (resourcelocation == null) {
            resourcelocation = new ResourceLocation("iceandfire:" + "dragon_texture_" + baseTexture);
            List<String> tex = new ArrayList<String>();
            tex.add(EnumDragonTextures.getTextureFromDragon(entity).toString());
            if (entity.isMale() && !entity.isSkeletal()) {
                if (dragonType == 0) {
                    tex.add(EnumDragonTextures.getDragonEnum(entity).FIRE_MALE_OVERLAY.toString());
                } else if(dragonType == 1){
                    tex.add(EnumDragonTextures.getDragonEnum(entity).ICE_MALE_OVERLAY.toString());
                } else if(dragonType == 2){
                    tex.add(EnumDragonTextures.getDragonEnum(entity).LIGHTNING_MALE_OVERLAY.toString());
                }
            } else {
                tex.add(EnumDragonTextures.Armor.EMPTY.FIRETEXTURE.toString());

            }
            ArrayLayeredTexture layeredBase = new ArrayLayeredTexture(tex);
            Minecraft.getInstance().getTextureManager().loadTexture(resourcelocation, layeredBase);
            LAYERED_TEXTURE_CACHE.put(baseTexture, resourcelocation);
        }
        return resourcelocation;
    }

    private String simplifyBoolean(boolean bool){
        return bool ? "1" : "0";
    }
}
