package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class ItemHydraHeart extends Item {

    public ItemHydraHeart() {
        super(new Item.Properties().tab(IceAndFire.TAB_ITEMS).stacksTo(1));
    }

    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return !oldStack.sameItem(newStack);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level world, Entity entity, int itemSlot, boolean isSelected) {
        if (entity instanceof Player && itemSlot >= 0 && itemSlot <= 8) {
            double healthPercentage = ((Player) entity).getHealth() / Math.max(1, ((Player) entity).getMaxHealth());
            if (healthPercentage < 1.0D) {
                int level = 0;
                if (healthPercentage < 0.25D) {
                    level = 3;
                } else if (healthPercentage < 0.5D) {
                    level = 2;
                } else if (healthPercentage < 0.75D) {
                    level = 1;
                }
                //Consider using EffectInstance.combine
                if (!((Player) entity).hasEffect(MobEffects.REGENERATION) || ((Player) entity).getEffect(MobEffects.REGENERATION).getAmplifier() < level)
                    ((Player) entity).addEffect(new MobEffectInstance(MobEffects.REGENERATION, 900, level, true, false));
            }
            //In hotbar
        }
    }

    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(new TranslatableComponent("item.iceandfire.legendary_weapon.desc").withStyle(ChatFormatting.GRAY));
        tooltip.add(new TranslatableComponent("item.iceandfire.hydra_heart.desc_0").withStyle(ChatFormatting.GRAY));
        tooltip.add(new TranslatableComponent("item.iceandfire.hydra_heart.desc_1").withStyle(ChatFormatting.GRAY));
    }
}
