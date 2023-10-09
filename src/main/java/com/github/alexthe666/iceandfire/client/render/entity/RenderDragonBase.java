package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.iceandfire.client.render.entity.layer.LayerDragonArmor;
import com.github.alexthe666.iceandfire.client.render.entity.layer.LayerDragonBanner;
import com.github.alexthe666.iceandfire.client.render.entity.layer.LayerDragonEyes;
import com.github.alexthe666.iceandfire.client.render.entity.layer.LayerDragonRider;
import com.github.alexthe666.iceandfire.client.texture.ArrayLayeredTexture;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.enums.EnumDragonTextures;
import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RenderDragonBase extends MobRenderer<EntityDragonBase, AdvancedEntityModel<EntityDragonBase>> {

    private final Map<String, ResourceLocation> LAYERED_TEXTURE_CACHE = Maps.newHashMap();
    private final int dragonType;

    public RenderDragonBase(EntityRendererProvider.Context context, AdvancedEntityModel<EntityDragonBase> model, int dragonType) {
        super(context, model, 0.15F);
        this.addLayer(new LayerDragonEyes(this));
        this.addLayer(new LayerDragonRider(this, false));
        this.addLayer(new LayerDragonBanner(this));
        this.addLayer(new LayerDragonArmor(this, dragonType));
        this.dragonType = dragonType;
    }

    private Vec3 getPosition(LivingEntity LivingEntityIn, double p_177110_2_, float p_177110_4_) {
        double d0 = LivingEntityIn.xOld + (LivingEntityIn.getX() - LivingEntityIn.xOld) * (double) p_177110_4_;
        double d1 = p_177110_2_ + LivingEntityIn.yOld + (LivingEntityIn.getY() - LivingEntityIn.yOld) * (double) p_177110_4_;
        double d2 = LivingEntityIn.zOld + (LivingEntityIn.getZ() - LivingEntityIn.zOld) * (double) p_177110_4_;
        return new Vec3(d0, d1, d2);
    }

    @Override
    protected void scale(EntityDragonBase entity, PoseStack matrixStackIn, float partialTickTime) {
        this.shadowRadius = entity.getRenderSize() / 3;
        float f7 = entity.prevDragonPitch + (entity.getDragonPitch() - entity.prevDragonPitch) * partialTickTime;
        matrixStackIn.mulPose(Axis.XP.rotationDegrees(f7));
        matrixStackIn.scale(shadowRadius, shadowRadius, shadowRadius);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(EntityDragonBase entity) {
        String baseTexture = entity.getVariantName(entity.getVariant()) + entity.getDragonStage() + entity.isModelDead() + entity.isMale() + entity.isSkeletal() + entity.isSleeping() + entity.isBlinking();
        ResourceLocation resourcelocation = LAYERED_TEXTURE_CACHE.get(baseTexture);
        if (resourcelocation == null) {
            resourcelocation = new ResourceLocation("iceandfire:" + "dragon_texture_" + baseTexture);
            List<String> tex = new ArrayList<String>();
            tex.add(EnumDragonTextures.getTextureFromDragon(entity).toString());
            if (entity.isMale() && !entity.isSkeletal()) {
                if (dragonType == 0) {
                    tex.add(EnumDragonTextures.getDragonEnum(entity).FIRE_MALE_OVERLAY.toString());
                } else if (dragonType == 1) {
                    tex.add(EnumDragonTextures.getDragonEnum(entity).ICE_MALE_OVERLAY.toString());
                } else if (dragonType == 2) {
                    tex.add(EnumDragonTextures.getDragonEnum(entity).LIGHTNING_MALE_OVERLAY.toString());
                }
            } else {
                tex.add(EnumDragonTextures.Armor.EMPTY.FIRETEXTURE.toString());

            }
            ArrayLayeredTexture layeredBase = new ArrayLayeredTexture(tex);
            Minecraft.getInstance().getTextureManager().register(resourcelocation, layeredBase);
            LAYERED_TEXTURE_CACHE.put(baseTexture, resourcelocation);
        }
        return resourcelocation;
    }

}
