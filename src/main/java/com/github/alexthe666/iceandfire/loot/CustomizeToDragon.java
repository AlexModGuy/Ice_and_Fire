package com.github.alexthe666.iceandfire.loot;

import com.github.alexthe666.iceandfire.datagen.tags.IafItemTags;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.item.*;
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

public class CustomizeToDragon extends LootItemConditionalFunction {

    public CustomizeToDragon(LootItemCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Override
    protected @NotNull ItemStack run(final ItemStack stack, @NotNull final LootContext context) {
        if (!stack.isEmpty() && context.getParamOrNull(LootContextParams.THIS_ENTITY) instanceof EntityDragonBase dragon) {
            if (stack.getItem() == IafItemRegistry.DRAGON_BONE.get()) {
                stack.setCount(1 + dragon.getRandom().nextInt(1 + (dragon.getAgeInDays() / 25)));
                return stack;
            } else if (stack.getItem() instanceof ItemDragonScales) {
                stack.setCount(dragon.getAgeInDays() / 25 + dragon.getRandom().nextInt(1 + (dragon.getAgeInDays() / 5)));
                return new ItemStack(dragon.getVariantScale(dragon.getVariant()), stack.getCount());
            } else if (stack.getItem() instanceof ItemDragonEgg) {
                if (dragon.shouldDropLoot()) {
                    return new ItemStack(dragon.getVariantEgg(dragon.getVariant()), stack.getCount());
                } else {
                    stack.setCount(1 + dragon.getRandom().nextInt(1 + (dragon.getAgeInDays() / 5)));
                    return new ItemStack(dragon.getVariantScale(dragon.getVariant()), stack.getCount());
                }
            } else if (stack.getItem() instanceof ItemDragonFlesh) {
                return new ItemStack(dragon.getFleshItem(), 1 + dragon.getRandom().nextInt(1 + (dragon.getAgeInDays() / 25)));
            } else if (stack.getItem() instanceof ItemDragonSkull) {
                ItemStack skull = dragon.getSkull();
                skull.setCount(stack.getCount());
                skull.setTag(stack.getTag());
                return skull;
            } else if (stack.is(IafItemTags.DRAGON_BLOODS)) {
                return new ItemStack(dragon.getBloodItem(), stack.getCount());
            } else if (stack.is(IafItemTags.DRAGON_HEARTS)) {
                return new ItemStack(dragon.getHeartItem(), stack.getCount());
            }
        }
        return stack;
    }

    @Override
    public @NotNull LootItemFunctionType getType() {
        return IafLootRegistry.CUSTOMIZE_TO_DRAGON;
    }

    public static class Serializer extends LootItemConditionalFunction.Serializer<CustomizeToDragon> {
        public Serializer() {
            super();
        }

        @Override
        public void serialize(@NotNull JsonObject object, @NotNull CustomizeToDragon functionClazz, @NotNull JsonSerializationContext serializationContext) {
        }

        @Override
        public @NotNull CustomizeToDragon deserialize(@NotNull JsonObject object, @NotNull JsonDeserializationContext deserializationContext, LootItemCondition @NotNull [] conditionsIn) {
            return new CustomizeToDragon(conditionsIn);
        }
    }
}