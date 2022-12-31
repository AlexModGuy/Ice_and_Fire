package com.github.alexthe666.iceandfire.loot;

import com.github.alexthe666.iceandfire.entity.DragonType;
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

import java.util.Random;

public class CustomizeToDragon extends LootItemConditionalFunction {

    public CustomizeToDragon(LootItemCondition[] conditionsIn) {
        super(conditionsIn);
    }

    protected ItemStack run(ItemStack stack, LootContext context) {
        if (!stack.isEmpty() && context.getParamOrNull(LootContextParams.THIS_ENTITY) instanceof EntityDragonBase) {
            Random random = new Random();
            EntityDragonBase dragon = (EntityDragonBase) context.getParamOrNull(LootContextParams.THIS_ENTITY);
            if (dragon == null) {
                return stack;
            }

            if (stack.getItem() == IafItemRegistry.DRAGON_BONE.get()) {
                stack.setCount(1 + random.nextInt(1 + (dragon.getAgeInDays() / 25)));
                return stack;
            }
            if (stack.getItem() instanceof ItemDragonScales) {
                stack.setCount(dragon.getAgeInDays() / 25 + random.nextInt(1 + (dragon.getAgeInDays() / 5)));
                return new ItemStack(dragon.getVariantScale(dragon.getVariant()), stack.getCount());
            }
            else if (stack.getItem() instanceof ItemDragonEgg) {
                if (dragon.shouldDropLoot()) {
                    return new ItemStack(dragon.getVariantEgg(dragon.getVariant()), stack.getCount());
                } else {
                    stack.setCount(1 + random.nextInt(1 + (dragon.getAgeInDays() / 5)));
                    return new ItemStack(dragon.getVariantScale(dragon.getVariant()), stack.getCount());
                }
            }
            else if (stack.getItem() instanceof ItemDragonFlesh) {
                stack.setCount(1 + random.nextInt(1 + (dragon.getAgeInDays() / 25)));
                return new ItemStack(stack.getItem(), stack.getCount());
            }
            else if (stack.getItem() instanceof ItemDragonSkull) {
                ItemStack stack1 = new ItemStack(dragon.dragonType == DragonType.FIRE ? IafItemRegistry.DRAGON_SKULL_FIRE.get() : IafItemRegistry.DRAGON_SKULL_ICE.get(), stack.getCount());
                stack1.setTag(stack.getTag());
                return stack1;
            }
            if (stack.getItem() == IafItemRegistry.FIRE_DRAGON_BLOOD.get() || stack.getItem() == IafItemRegistry.ICE_DRAGON_BLOOD.get()) {
                return new ItemStack(dragon.dragonType == DragonType.FIRE ? IafItemRegistry.FIRE_DRAGON_BLOOD.get() : IafItemRegistry.ICE_DRAGON_BLOOD.get(), stack.getCount());
            }
            else if (stack.getItem() == IafItemRegistry.FIRE_DRAGON_HEART.get() || stack.getItem() == IafItemRegistry.ICE_DRAGON_HEART.get()) {
                return new ItemStack(dragon.dragonType == DragonType.FIRE ? IafItemRegistry.FIRE_DRAGON_HEART.get() : IafItemRegistry.ICE_DRAGON_HEART.get(), stack.getCount());
            }
        }
        return stack;
    }

    @Override
    public LootItemFunctionType getType() {
        return IafLootRegistry.CUSTOMIZE_TO_DRAGON;
    }


    public static class Serializer extends LootItemConditionalFunction.Serializer<CustomizeToDragon> {
        public Serializer() {
            super();
        }

        public void serialize(JsonObject object, CustomizeToDragon functionClazz, JsonSerializationContext serializationContext) {
        }

        @Override
        public CustomizeToDragon deserialize(JsonObject object, JsonDeserializationContext deserializationContext, LootItemCondition[] conditionsIn) {
            return new CustomizeToDragon(conditionsIn);
        }
    }
}