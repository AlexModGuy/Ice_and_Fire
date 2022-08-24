package com.github.alexthe666.iceandfire.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.world.World;

public class ItemPixieDust extends ItemGenericFood {

    public ItemPixieDust() {
        super(1, 0.3F, false, false, true, "pixie_dust");
    }

    public void onFoodEaten(ItemStack stack, World worldIn, LivingEntity livingEntity) {
        livingEntity.addEffect(new EffectInstance(Effects.LEVITATION, 100, 1));
        livingEntity.addEffect(new EffectInstance(Effects.GLOWING, 100, 1));
    }
}
