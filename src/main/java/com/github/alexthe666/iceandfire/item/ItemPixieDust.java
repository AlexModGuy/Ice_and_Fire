package com.github.alexthe666.iceandfire.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ItemPixieDust extends ItemGenericFood {

    public ItemPixieDust() {
        super(1, 0.3F, false, false, true);
    }

    @Override
    public void onFoodEaten(ItemStack stack, Level worldIn, LivingEntity livingEntity) {
        livingEntity.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 100, 1));
        livingEntity.addEffect(new MobEffectInstance(MobEffects.GLOWING, 100, 1));
    }
}
