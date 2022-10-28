package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.enums.EnumBestiaryPages;
import com.google.common.primitives.Ints;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.DeferredRegister;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;

public class ItemBestiary extends Item {

    public ItemBestiary() {
        super(new Item.Properties().tab(IceAndFire.TAB_ITEMS).stacksTo(1));
    }

    @Override
    public void onCraftedBy(ItemStack stack, Level worldIn, Player playerIn) {
        stack.setTag(new CompoundTag());
        stack.getTag().putIntArray("Pages", new int[]{0});

    }

    @Override
    public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
        if (this.allowdedIn(group)) {
            items.add(new ItemStack(this));
            ItemStack stack = new ItemStack(IafItemRegistry.BESTIARY.get());
            stack.setTag(new CompoundTag());
            int[] pages = new int[EnumBestiaryPages.values().length];
            for (int i = 0; i < EnumBestiaryPages.values().length; i++) {
                pages[i] = i;
            }
            stack.getTag().putIntArray("Pages", pages);
            items.add(stack);
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        ItemStack itemStackIn = playerIn.getItemInHand(handIn);
        if (worldIn.isClientSide) {
            IceAndFire.PROXY.openBestiaryGui(itemStackIn);
        }
        return new InteractionResultHolder<>(InteractionResult.PASS, itemStackIn);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (stack.getTag() == null) {
            stack.setTag(new CompoundTag());
            stack.getTag().putIntArray("Pages", new int[]{EnumBestiaryPages.INTRODUCTION.ordinal()});

        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        if (stack.getTag() != null) {
            if (IceAndFire.PROXY.shouldSeeBestiaryContents()) {
                tooltip.add(new TranslatableComponent("bestiary.contains").withStyle(ChatFormatting.GRAY));
                final Set<EnumBestiaryPages> pages = EnumBestiaryPages
                    .containedPages(Ints.asList(stack.getTag().getIntArray("Pages")));
                for (EnumBestiaryPages page : pages) {
                    tooltip.add(new TextComponent(ChatFormatting.WHITE + "-").append(new TranslatableComponent("bestiary." + EnumBestiaryPages.values()[page.ordinal()].toString().toLowerCase())).withStyle(ChatFormatting.GRAY));
                }
            } else {
                tooltip.add(new TranslatableComponent("bestiary.hold_shift").withStyle(ChatFormatting.GRAY));
            }

        }
    }

}
