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
import org.jetbrains.annotations.NotNull;

public class CustomizeToSeaSerpent extends LootItemConditionalFunction {

    public CustomizeToSeaSerpent(LootItemCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Override
    public @NotNull ItemStack run(ItemStack stack, @NotNull LootContext context) {
        if (!stack.isEmpty() && context.getParamOrNull(LootContextParams.THIS_ENTITY) instanceof EntitySeaSerpent seaSerpent) {
            final int ancientModifier = seaSerpent.isAncient() ? 2 : 1;
            if (stack.getItem() instanceof ItemSeaSerpentScales) {
                stack.setCount(1 + seaSerpent.getRandom().nextInt(1 + (int) Math.ceil(seaSerpent.getSeaSerpentScale() * 3 * ancientModifier)));
                return new ItemStack(seaSerpent.getEnum().scale.get(), stack.getCount());
            }
            if (stack.getItem() == IafItemRegistry.SERPENT_FANG.get()) {
                stack.setCount(1 + seaSerpent.getRandom().nextInt(1 + (int) Math.ceil(seaSerpent.getSeaSerpentScale() * 2 * ancientModifier)));
                return stack;
            }
        }
        return stack;
    }

    @Override
    public @NotNull LootItemFunctionType getType() {
        return IafLootRegistry.CUSTOMIZE_TO_SERPENT;
    }

    public static class Serializer extends LootItemConditionalFunction.Serializer<CustomizeToSeaSerpent> {
        public Serializer() {
            super();
        }

        @Override
        public void serialize(@NotNull JsonObject object, @NotNull CustomizeToSeaSerpent functionClazz, @NotNull JsonSerializationContext serializationContext) {
        }

        @Override
        public @NotNull CustomizeToSeaSerpent deserialize(@NotNull JsonObject object, @NotNull JsonDeserializationContext deserializationContext, LootItemCondition @NotNull [] conditionsIn) {
            return new CustomizeToSeaSerpent(conditionsIn);
        }
    }
}