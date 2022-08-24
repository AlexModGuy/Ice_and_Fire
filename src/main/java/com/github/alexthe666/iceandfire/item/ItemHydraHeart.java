package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemHydraHeart extends Item {

    public ItemHydraHeart() {
        super(new Item.Properties().tab(IceAndFire.TAB_ITEMS).stacksTo(1));
        this.setRegistryName(IceAndFire.MODID, "hydra_heart");
    }

    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return !oldStack.sameItem(newStack);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
        if (entity instanceof PlayerEntity && itemSlot >= 0 && itemSlot <= 8) {
            double healthPercentage = ((PlayerEntity) entity).getHealth() / Math.max(1, ((PlayerEntity) entity).getMaxHealth());
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
                if (!((PlayerEntity) entity).hasEffect(Effects.REGENERATION) || ((PlayerEntity) entity).getEffect(Effects.REGENERATION).getAmplifier() < level)
                    ((PlayerEntity) entity).addEffect(new EffectInstance(Effects.REGENERATION, 900, level, true, false));
            }
            //In hotbar
        }
    }

    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent("item.iceandfire.legendary_weapon.desc").withStyle(TextFormatting.GRAY));
        tooltip.add(new TranslationTextComponent("item.iceandfire.hydra_heart.desc_0").withStyle(TextFormatting.GRAY));
        tooltip.add(new TranslationTextComponent("item.iceandfire.hydra_heart.desc_1").withStyle(TextFormatting.GRAY));
    }
}
