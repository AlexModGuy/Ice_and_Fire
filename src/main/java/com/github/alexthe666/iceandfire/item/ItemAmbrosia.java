package com.github.alexthe666.iceandfire.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class ItemAmbrosia extends ItemGenericFood {

    public ItemAmbrosia() {
        super(5, 0.6F, false, false, true, 1);
    }

    @Override
    public void onFoodEaten(ItemStack stack, Level worldIn, LivingEntity livingEntity) {
        livingEntity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 3600, 2));
        livingEntity.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 3600, 2));
        livingEntity.addEffect(new MobEffectInstance(MobEffects.JUMP, 3600, 2));
        livingEntity.addEffect(new MobEffectInstance(MobEffects.LUCK, 3600, 2));
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level worldIn, LivingEntity livingEntity) {
        super.finishUsingItem(stack, worldIn, livingEntity);
        return livingEntity instanceof Player && ((Player) livingEntity).getAbilities().instabuild ? stack : new ItemStack(Items.BOWL);
    }
}
