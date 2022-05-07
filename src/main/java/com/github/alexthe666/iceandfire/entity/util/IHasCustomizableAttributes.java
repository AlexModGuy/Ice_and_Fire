package com.github.alexthe666.iceandfire.entity.util;

import com.github.alexthe666.iceandfire.IafConfig;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierManager;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;

import java.util.HashMap;
import java.util.Map;

//TODO: Rewrite when updating to newer java version
public interface IHasCustomizableAttributes {
    Map<EntityType<? extends LivingEntity>, AttributeModifierMap> ATTRIBUTE_MODIFIER_MAP = new HashMap<>();

    static <T extends LivingEntity & IHasCustomizableAttributes, M extends LivingEntity & IHasCustomizableAttributes> void applyAttributesForEntity(EntityType<T> type, M entity) {
        entity.attributes = new AttributeModifierManager(getAttributesForEntity(type, entity));
        entity.setHealth(entity.getMaxHealth());
    }

    static <T extends LivingEntity & IHasCustomizableAttributes, M extends LivingEntity & IHasCustomizableAttributes> AttributeModifierMap getAttributesForEntity(EntityType<T> type, M entity) {
        if (!IafConfig.allowAttributeOverriding)
            return GlobalEntityTypeAttributes.getAttributesForEntity(type);
        if (ATTRIBUTE_MODIFIER_MAP.containsKey(type)) {
            return ATTRIBUTE_MODIFIER_MAP.get(type);
        }
        AttributeModifierMap originalMap = GlobalEntityTypeAttributes.getAttributesForEntity(type);
        AttributeModifierMap.MutableAttribute originalMutable = new AttributeModifierMap.MutableAttribute(originalMap);
        originalMutable.combine(entity.getAttributes());
        AttributeModifierMap newMap = originalMutable.create();
        ATTRIBUTE_MODIFIER_MAP.put(type, newMap);
        return newMap;
    }

    AttributeModifierMap.MutableAttribute getAttributes();
}
