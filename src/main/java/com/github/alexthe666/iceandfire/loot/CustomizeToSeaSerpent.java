package com.github.alexthe666.iceandfire.loot;

import com.github.alexthe666.iceandfire.entity.EntitySeaSerpent;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.item.ItemSeaSerpentScales;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.Random;

public class CustomizeToSeaSerpent extends LootItemConditionalFunction {

    public CustomizeToSeaSerpent(LootItemCondition[] conditionsIn) {
        super(conditionsIn);
    }

    public ItemStack run(ItemStack stack, LootContext context) {
        if (!stack.isEmpty() && context.getParamOrNull(LootContextParams.THIS_ENTITY) instanceof EntitySeaSerpent) {
            Random random = new Random();
            EntitySeaSerpent seaSerpent = (EntitySeaSerpent) context.getParamOrNull(LootContextParams.THIS_ENTITY);
            if (seaSerpent == null) {
                return stack;
            }
            final int ancientModifier = seaSerpent.isAncient() ? 2 : 1;
            if (stack.getItem() instanceof ItemSeaSerpentScales) {
                stack.setCount(1 + random.nextInt(1 + (int) Math.ceil(seaSerpent.getSeaSerpentScale() * 3 * ancientModifier)));
                return new ItemStack(seaSerpent.getEnum().scale.get(), stack.getCount());
            }
            if (stack.getItem() == IafItemRegistry.SERPENT_FANG.get()) {
                stack.setCount(1 + random.nextInt(1 + (int) Math.ceil(seaSerpent.getSeaSerpentScale() * 2 * ancientModifier)));
                return stack;
            }
        }
        return stack;
    }

    @Override
    public LootItemFunctionType getType() {
        return IafLootRegistry.CUSTOMIZE_TO_SERPENT;
    }

    public static class Serializer extends LootItemConditionalFunction.Serializer<CustomizeToSeaSerpent> {
        public Serializer() {
            super();
        }

        public void serialize(JsonObject object, CustomizeToSeaSerpent functionClazz, JsonSerializationContext serializationContext) {
        }

        public CustomizeToSeaSerpent deserialize(JsonObject object, JsonDeserializationContext deserializationContext, LootItemCondition[] conditionsIn) {
            return new CustomizeToSeaSerpent(conditionsIn);
        }
    }
}