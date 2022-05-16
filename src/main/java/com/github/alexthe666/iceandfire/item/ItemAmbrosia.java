package com.github.alexthe666.iceandfire.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.world.World;

public class ItemAmbrosia extends ItemGenericFood {

    public ItemAmbrosia() {
        super(5, 0.6F, false, false, true, "ambrosia", 1);
    }

    public void onFoodEaten(ItemStack stack, World worldIn, LivingEntity livingEntity) {
        livingEntity.addPotionEffect(new EffectInstance(Effects.STRENGTH, 3600, 2));
        livingEntity.addPotionEffect(new EffectInstance(Effects.ABSORPTION, 3600, 2));
        livingEntity.addPotionEffect(new EffectInstance(Effects.JUMP_BOOST, 3600, 2));
        livingEntity.addPotionEffect(new EffectInstance(Effects.LUCK, 3600, 2));
    }

    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity livingEntity) {
        super.onItemUseFinish(stack, worldIn, livingEntity);
        return livingEntity instanceof PlayerEntity && ((PlayerEntity) livingEntity).abilities.isCreativeMode ? stack : new ItemStack(Items.BOWL);
    }
}
