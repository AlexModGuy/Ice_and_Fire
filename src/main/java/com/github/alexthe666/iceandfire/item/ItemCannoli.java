package com.github.alexthe666.iceandfire.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class ItemCannoli extends ItemGenericFood {

    public ItemCannoli() {
        super(20, 2.0F, false, false, true);
    }

    public void onFoodEaten(ItemStack stack, Level worldIn, LivingEntity livingEntity) {
        livingEntity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 3600, 2));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(new TranslatableComponent("item.iceandfire.cannoli.desc").withStyle(ChatFormatting.GRAY));
    }
}
