package com.github.alexthe666.iceandfire.client.render.entity.layer;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.util.ResourceLocation;

public interface IHasArmorVariantResource {

    ResourceLocation getArmorResource(int variant, EquipmentSlotType slotType);

}
