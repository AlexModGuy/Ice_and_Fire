package com.github.alexthe666.iceandfire.entity.util;

import com.github.alexthe666.iceandfire.IafConfig;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.DefaultAttributes;

import java.util.HashMap;
import java.util.Map;

//TODO: Rewrite when updating to newer java version
public interface IHasCustomizableAttributes {
    Map<EntityType<? extends LivingEntity>, AttributeSupplier> ATTRIBUTE_MODIFIER_MAP = new HashMap<>();

    static <T extends LivingEntity & IHasCustomizableAttributes, M extends LivingEntity & IHasCustomizableAttributes> void applyAttributesForEntity(EntityType<T> type, M entity) {
        entity.attributes = new AttributeMap(getAttributesForEntity(type, entity));
        entity.setHealth(entity.getMaxHealth());
    }

    static <T extends LivingEntity & IHasCustomizableAttributes, M extends LivingEntity & IHasCustomizableAttributes> AttributeSupplier getAttributesForEntity(EntityType<T> type, M entity) {
        if (!IafConfig.allowAttributeOverriding)
            return DefaultAttributes.getSupplier(type);
        if (ATTRIBUTE_MODIFIER_MAP.containsKey(type)) {
            return ATTRIBUTE_MODIFIER_MAP.get(type);
        }
        AttributeSupplier originalMap = DefaultAttributes.getSupplier(type);
        AttributeSupplier.Builder originalMutable = new AttributeSupplier.Builder(originalMap);
        originalMutable.combine(entity.getConfigurableAttributes());
        AttributeSupplier newMap = originalMutable.build();
        ATTRIBUTE_MODIFIER_MAP.put(type, newMap);
        return newMap;
    }

    AttributeSupplier.Builder getConfigurableAttributes();
}
