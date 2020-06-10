package com.github.alexthe666.iceandfire.client.render.entity.layer;

import com.github.alexthe666.iceandfire.client.render.entity.RenderDragonBase;
import com.github.alexthe666.iceandfire.client.texture.ArrayLayeredTexture;
import com.github.alexthe666.iceandfire.entity.DragonType;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.enums.EnumDragonTextures;
import com.google.common.collect.Maps;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LayerDragonArmor implements LayerRenderer<EntityDragonBase> {
    private static EquipmentSlotType[] ARMOR_SLOTS = {EquipmentSlotType.HEAD, EquipmentSlotType.CHEST, EquipmentSlotType.LEGS, EquipmentSlotType.FEET};
    private final MobRenderer render;
    private static final Map<String, ResourceLocation> LAYERED_ARMOR_CACHE = Maps.newHashMap();

    public LayerDragonArmor(MobRenderer renderIn, boolean isFireDragon) {
        this.render = renderIn;
    }

    public void doRenderLayer(EntityDragonBase dragon, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        int armorHead = dragon.getArmorOrdinal(dragon.getItemStackFromSlot(EquipmentSlotType.HEAD));
        int armorNeck = dragon.getArmorOrdinal(dragon.getItemStackFromSlot(EquipmentSlotType.CHEST));
        int armorLegs = dragon.getArmorOrdinal(dragon.getItemStackFromSlot(EquipmentSlotType.LEGS));
        int armorFeet = dragon.getArmorOrdinal(dragon.getItemStackFromSlot(EquipmentSlotType.FEET));
        String armorTexture = dragon.dragonType.getName() + "|" + armorHead + "|" + armorNeck + "|" + armorLegs + "|" + armorFeet;
        if (!armorTexture.equals(dragon.dragonType.getName() + "|0|0|0|0")) {
            ResourceLocation resourcelocation = LAYERED_ARMOR_CACHE.get(armorTexture);
            if(resourcelocation == null){
                resourcelocation = new ResourceLocation("iceandfire" + "dragonArmor_" + armorTexture);
                List<String> tex = new ArrayList<String>();
                for (EquipmentSlotType slot : ARMOR_SLOTS) {
                    if (dragon.dragonType == DragonType.FIRE) {
                        tex.add(EnumDragonTextures.Armor.getArmorForDragon(dragon, slot).FIRETEXTURE.toString());
                    } else {
                        tex.add(EnumDragonTextures.Armor.getArmorForDragon(dragon, slot).ICETEXTURE.toString());
                    }
                }
                ArrayLayeredTexture layeredBase = new ArrayLayeredTexture(tex);
                Minecraft.getInstance().getTextureManager().loadTexture(resourcelocation, layeredBase);
                LAYERED_ARMOR_CACHE.put(armorTexture, resourcelocation);
            }
            this.render.bindTexture(resourcelocation);
            this.render.getMainModel().render(dragon, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        }
    }

    public boolean shouldCombineTextures() {
        return false;
    }

    public static void clearCache(String str){
        LAYERED_ARMOR_CACHE.remove(str);
    }
}