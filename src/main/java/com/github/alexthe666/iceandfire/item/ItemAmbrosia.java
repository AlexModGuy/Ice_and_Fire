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
        livingEntity.addEffect(new EffectInstance(Effects.DAMAGE_BOOST, 3600, 2));
        livingEntity.addEffect(new EffectInstance(Effects.ABSORPTION, 3600, 2));
        livingEntity.addEffect(new EffectInstance(Effects.JUMP, 3600, 2));
        livingEntity.addEffect(new EffectInstance(Effects.LUCK, 3600, 2));
    }

    public ItemStack finishUsingItem(ItemStack stack, World worldIn, LivingEntity livingEntity) {
        super.finishUsingItem(stack, worldIn, livingEntity);
        return livingEntity instanceof PlayerEntity && ((PlayerEntity) livingEntity).abilities.instabuild ? stack : new ItemStack(Items.BOWL);
    }
}
