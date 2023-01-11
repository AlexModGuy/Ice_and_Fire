package com.github.alexthe666.iceandfire.client.render.entity.layer;

import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.iceandfire.client.texture.ArrayLayeredTexture;
import com.github.alexthe666.iceandfire.entity.DragonType;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.enums.EnumDragonTextures;
import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LayerDragonArmor extends RenderLayer<EntityDragonBase, AdvancedEntityModel<EntityDragonBase>> {
    private static final Map<String, ResourceLocation> LAYERED_ARMOR_CACHE = Maps.newHashMap();
    private static final EquipmentSlot[] ARMOR_SLOTS = {EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};
    private final MobRenderer render;

    public LayerDragonArmor(MobRenderer renderIn, int type) {
        super(renderIn);
        this.render = renderIn;
    }

    public static void clearCache(String str) {
        LAYERED_ARMOR_CACHE.remove(str);
    }

    @Override
    public void render(@NotNull PoseStack matrixStackIn, @NotNull MultiBufferSource bufferIn, int packedLightIn, EntityDragonBase dragon, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        int armorHead = dragon.getArmorOrdinal(dragon.getItemBySlot(EquipmentSlot.HEAD));
        int armorNeck = dragon.getArmorOrdinal(dragon.getItemBySlot(EquipmentSlot.CHEST));
        int armorLegs = dragon.getArmorOrdinal(dragon.getItemBySlot(EquipmentSlot.LEGS));
        int armorFeet = dragon.getArmorOrdinal(dragon.getItemBySlot(EquipmentSlot.FEET));
        String armorTexture = dragon.dragonType.getName() + "_" + armorHead + "_" + armorNeck + "_" + armorLegs + "_" + armorFeet;
        if (!armorTexture.equals(dragon.dragonType.getName() + "_0_0_0_0")) {
            ResourceLocation resourcelocation = LAYERED_ARMOR_CACHE.get(armorTexture);
            if (resourcelocation == null) {
                resourcelocation = new ResourceLocation("iceandfire" + "dragon_armor_" + armorTexture);
                List<String> tex = new ArrayList<String>();
                for (EquipmentSlot slot : ARMOR_SLOTS) {
                    if (dragon.dragonType == DragonType.FIRE) {
                        tex.add(EnumDragonTextures.Armor.getArmorForDragon(dragon, slot).FIRETEXTURE.toString());
                    } else if (dragon.dragonType == DragonType.ICE) {
                        tex.add(EnumDragonTextures.Armor.getArmorForDragon(dragon, slot).ICETEXTURE.toString());
                    } else {
                        tex.add(EnumDragonTextures.Armor.getArmorForDragon(dragon, slot).LIGHTNINGTEXTURE.toString());
                    }
                }
                ArrayLayeredTexture layeredBase = new ArrayLayeredTexture(tex);
                Minecraft.getInstance().getTextureManager().register(resourcelocation, layeredBase);
                LAYERED_ARMOR_CACHE.put(armorTexture, resourcelocation);
            }
            VertexConsumer ivertexbuilder = bufferIn.getBuffer(RenderType.entityCutoutNoCull(resourcelocation));
            this.getParentModel().renderToBuffer(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        }
    }
}