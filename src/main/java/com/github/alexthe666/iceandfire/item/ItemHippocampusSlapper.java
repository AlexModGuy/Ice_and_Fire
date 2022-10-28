package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class ItemHippocampusSlapper extends SwordItem {

    public ItemHippocampusSlapper() {
        super(IafItemRegistry.HIPPOCAMPUS_SWORD_TOOL_MATERIAL, 3, -2.4F, new Item.Properties().tab(IceAndFire.TAB_ITEMS));
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity targetEntity, LivingEntity attacker) {
        targetEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 2));
        targetEntity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 100, 2));
        targetEntity.playSound(SoundEvents.GUARDIAN_FLOP, 3, 1);

        return super.hurtEnemy(stack, targetEntity, attacker);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(new TranslatableComponent("item.iceandfire.legendary_weapon.desc").withStyle(ChatFormatting.GRAY));
        tooltip.add(new TranslatableComponent("item.iceandfire.hippocampus_slapper.desc_0").withStyle(ChatFormatting.GRAY));
        tooltip.add(new TranslatableComponent("item.iceandfire.hippocampus_slapper.desc_1").withStyle(ChatFormatting.GRAY));
    }
}